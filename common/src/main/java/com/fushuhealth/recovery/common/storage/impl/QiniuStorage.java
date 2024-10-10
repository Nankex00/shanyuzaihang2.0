package com.fushuhealth.recovery.common.storage.impl;

import com.alibaba.fastjson.JSON;
import com.fushuhealth.recovery.common.api.ResultCode;
import com.fushuhealth.recovery.common.entity.dto.FileInfoDto;
import com.fushuhealth.recovery.common.exception.OldServiceException;
import com.fushuhealth.recovery.common.exception.StorageException;
import com.fushuhealth.recovery.common.storage.FileType;
import com.fushuhealth.recovery.common.storage.OldFileType;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.PathUtil;
import com.fushuhealth.recovery.common.util.StringUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.*;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

@Component
@ConfigurationProperties(prefix = "storage.cloud")
@ConditionalOnProperty(value = "storage.mode", havingValue = "cloud")
public class QiniuStorage implements InitializingBean, Storage {

    private final static Logger log = LoggerFactory.getLogger(QiniuStorage.class);

    @Value(value = "${storage.cloud.access-key:''}")
    private String accessKey;

    @Value(value = "${storage.cloud.secret-key:''}")
    private String secretKey;

    @Value(value = "${storage.cloud.endpoint:''}")
    private String endpoint;

    @Value(value = "${storage.cloud.bucket:''}")
    private String bucket;

    @Value(value = "${video.wm-url}")
    private String wmUrl;

    @Value(value = "${video.pm3u8}")
    private String pm3u8;

    @Value(value = "${video.qiniu-pipeline}")
    private String qiniuPipeline;

    @Override
    public InputStream getFile(String type, String filePath) throws Exception {
        return null;
    }

    @Override
    public void getFile(String type, String filePath, File destFile) throws Exception {

    }

    @Override
    public void putFile(String type, File file, String destFilePath) throws Exception {
        Configuration cfg = new Configuration(Region.region1());
        UploadManager uploadManager = new UploadManager(cfg);
        String key = PathUtil.concatPaths(type, destFilePath);
        String uploadToken = getUploadToken(type, destFilePath);
        try {
            Response response = uploadManager.put(file.getAbsolutePath(), key, uploadToken);
            DefaultPutRet defaultPutRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            log.info("upload file success : {}", defaultPutRet.key);
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("upload file error : {}", r.toString());
            try {
                log.error("upload file error : {}", r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            throw new StorageException("upload file to qiniu error");
        }
    }

    @Override
    public void putFileContent(String type, String content, String destFilePath) throws Exception {

    }

    @Override
    public void copyFile(String type, String srcFilePath, String destFilePath) throws Exception {

    }

    @Override
    public void copyFile(String srcType, String srcFilePath, String destType, String destFilePath) throws Exception {

    }

    @Override
    public boolean checkFileExists(String type, String filePath) throws Exception {
        return false;
    }

    @Override
    public void deleteFile(String type, String filePath) throws Exception {

    }

    @Override
    public String getFileUrl(String type, String filePath, boolean origin) {
        String key = PathUtil.concatPaths(type, filePath);
        if (!origin) {
            if (OldFileType.VIDEO.equals(FileType.getType(type))) {
                key = StringUtil.getM3u8(key);
            } else if (OldFileType.PICTURE.equals(FileType.getType(type))){
                key = StringUtil.getPicWm(key);
            }
        }

        DownloadUrl url = new DownloadUrl(endpoint, true, key);
        if (!origin) {
            if (OldFileType.VIDEO.equals(FileType.getType(type))) {
                url.setFop(pm3u8); // 配置 fop
            }
        }
        String urlString = "";
//        long expireInSeconds = 3600000;//10年，可以自定义链接过期时间
        long expireInSeconds = 60 * 60;//1小时，可以自定义链接过期时间
        Auth auth = Auth.create(accessKey, secretKey);
        try {
            urlString = url.buildURL(auth, DateUtil.getCurrentTimeStamp() + expireInSeconds);
//            urlString = url.buildURL(auth, 1735574400L);
        } catch (QiniuException e) {
            throw new StorageException("get qiniu file url error");
        }
        return urlString;
    }

    @Override
    public String getLongExpiredFileUrl(String type, String filePath, boolean origin) {
        String key = PathUtil.concatPaths(type, filePath);
        if (!origin) {
            if (OldFileType.VIDEO.equals(FileType.getType(type))) {
                key = StringUtil.getM3u8(key);
            } else if (OldFileType.PICTURE.equals(FileType.getType(type))){
                key = StringUtil.getPicWm(key);
            }
        }

        DownloadUrl url = new DownloadUrl(endpoint, true, key);
        if (!origin) {
            if (OldFileType.VIDEO.equals(FileType.getType(type))) {
                url.setFop(pm3u8); // 配置 fop
            }
        }
        String urlString = "";
        //30天，可以自定义链接过期时间
        long expireInSeconds = 60 * 60 * 24 * 30;
        Auth auth = Auth.create(accessKey, secretKey);
        try {
            urlString = url.buildURL(auth, DateUtil.getCurrentTimeStamp() + expireInSeconds);
//            urlString = url.buildURL(auth, 1735574400L);
        } catch (QiniuException e) {
            throw new StorageException("get qiniu file url error");
        }
        return urlString;
    }

    @Override
    public String getFileUrl(String filePath) {
        DownloadUrl url = new DownloadUrl(endpoint, true, filePath);
//        url.setAttname(attname) // 配置 attname
//                .setFop(fop) // 配置 fop
//                .setStyle(style, styleSeparator, styleParam) // 配置 style
        // 带有效期
        String urlString = "";
        long expireInSeconds = 3600;//10小时，可以自定义链接过期时间
        Auth auth = Auth.create(accessKey, secretKey);
        try {
            urlString = url.buildURL(auth, DateUtil.getCurrentTimeStamp() + expireInSeconds);
        } catch (QiniuException e) {
            throw new StorageException("get qiniu file url error");
        }
        return urlString;
    }

    @Override
    public String getFilePath(String type, String filePath) {
        return "http://" + PathUtil.concatPaths(endpoint, type, filePath);
    }

    @Override
    public String getUploadToken(String type, String filePath) {
        String key = PathUtil.concatPaths(type, filePath);
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        if (OldFileType.VIDEO.equals(FileType.getType(type))) {
            String ops = addPersistentOpsOnVideo(StringUtil.getM3u8(key));
            putPolicy.put("persistentOps", ops);
            putPolicy.put("persistentPipeline", qiniuPipeline);
        } else if (OldFileType.PICTURE.equals(FileType.getType(type))) {
            String ops = addPersistentOpsOnPicture(StringUtil.getPicWm(key));
            putPolicy.put("persistentOps", ops);
            putPolicy.put("persistentPipeline", qiniuPipeline);
        }
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket, key, expireSeconds, putPolicy);
        return upToken;
    }

    @Override
    public String moveFile(String path, String destType, String destFilePath) throws Exception{
        Configuration cfg = new Configuration(Region.region1());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        bucketManager.move(bucket, path, bucket, PathUtil.concatPaths(destType, destFilePath), true);
        return destFilePath;
    }

    @Override
    public boolean addWaterMark(String type, String key) {
        Configuration cfg = new Configuration(Region.region1());
        Auth auth = Auth.create(accessKey, secretKey);
        key = PathUtil.concatPaths(type, key);
        String ops = addPersistentOpsOnPicture(StringUtil.getPicWm(key));

        //构建持久化数据处理对象
        OperationManager operationManager = new OperationManager(auth, cfg);
        try {
            String persistentId = operationManager.pfop(bucket, key, ops, qiniuPipeline, true);
            return true;
        } catch (QiniuException e) {
            return false;
        }
    }

    @Override
    public boolean convertToM3u8(String type, String key) {
        Configuration cfg = new Configuration(Region.region1());
        Auth auth = Auth.create(accessKey, secretKey);
        key = PathUtil.concatPaths(type, key);
        String ops = addPersistentOpsOnVideo(StringUtil.getM3u8(key));

        //构建持久化数据处理对象
        OperationManager operationManager = new OperationManager(auth, cfg);
        try {
            String persistentId = operationManager.pfop(bucket, key, ops, qiniuPipeline, true);
            return true;
        } catch (QiniuException e) {
            return false;
        }
    }

    @Override
    public boolean rotateVideo(String type, String key, String newKey, Integer degree) {
        Configuration cfg = new Configuration(Region.region1());
        Auth auth = Auth.create(accessKey, secretKey);
        key = PathUtil.concatPaths(type, key);
        newKey = PathUtil.concatPaths(type, newKey);
        String ops = rotateVideo(newKey, degree);

        //构建持久化数据处理对象
        OperationManager operationManager = new OperationManager(auth, cfg);
        try {
            String persistentId = operationManager.pfop(bucket, key, ops, qiniuPipeline, true);
            return true;
        } catch (QiniuException e) {
            return false;
        }
    }

    @Override
    public void putBytes(String type, byte[] bytes, String destFilePath) throws Exception {

    }

    @Override
    public void putStream(String type, InputStream inputStream, String destFilePath) throws Exception {

    }

    @Override
    public byte[] getBytes(String type, String destFilePath) throws Exception {
        return new byte[0];
    }

    @Override
    public FileInfoDto fetchFile(String url, String type, String filePath) {
        Configuration cfg = new Configuration(Region.region1());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        String key = PathUtil.concatPaths(type, filePath);
        try {
            FetchRet fetch = bucketManager.fetch(url, bucket, key);
            if (OldFileType.getType(type) == OldFileType.PICTURE) {
                addWaterMark(type, key);
            }
            return new FileInfoDto(FilenameUtils.getName(filePath), filePath, fetch.fsize);
        } catch (QiniuException e) {
            log.error("fetch file from {} error, {}", url, e);
            throw new OldServiceException(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean isLocalMode() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    private String addPersistentOpsOnVideo(String m3u8Name) {
        StringBuffer sb = new StringBuffer();
        sb.append("avthumb/mp4/");
        //加水印
        sb.append("wmImage/" + UrlSafeBase64.encodeToString(wmUrl));
        sb.append("/wmScale/0.15|avthumb/m3u8/noDomain/1/segtime/10|saveas/");
        sb.append(UrlSafeBase64.encodeToString(bucket + ":" + m3u8Name));
        sb.append(";");
        sb.append(pm3u8);
        return sb.toString();
    }

    private String rotateVideo(String key, Integer degree) {
        StringBuffer sb = new StringBuffer();
        sb.append("avthumb/mp4/rotate/" + degree);
        sb.append("|saveas/");
        sb.append(UrlSafeBase64.encodeToString(bucket + ":" + key));
        return sb.toString();
    }

    private String addPersistentOpsOnPicture(String wmName) {
        StringBuffer sb = new StringBuffer();
        sb.append("watermark/1/image/");
        sb.append(UrlSafeBase64.encodeToString(wmUrl));
        sb.append("/dissolve/50/gravity/NorthEast/ws/0.15|saveas/");
        sb.append(UrlSafeBase64.encodeToString(bucket + ":" + wmName));
        return sb.toString();
    }

    public static void main(String[] args) {
        String accessKey = "5tWU4jx332LnEkvTlzONEFO00KdRXQApLvLQGmIc";
        String secretKey = "oO08CqPkhJdX7l_gV8KyFm_qpNncC9UnQptmXl6Z";
        Auth auth = Auth.create(accessKey, secretKey);
        Configuration cfg = new Configuration(Region.region1());

        String[] keys = new String[]{"2023/01/15/前臂旋前的干预纠正方法.mp4","2023/01/15/自发足拇指上翘.mp4","2023/01/15/肩内旋的干预纠正方法.mp4","2023/01/15/肌性足内翻-肌性足外翻.mp4","2023/01/15/徐动.mp4","2023/01/15/徐动.mp4","2023/01/15/尖足-足背屈.mp4","2023/01/15/尖足-足背屈.mp4","2023/01/15/尖足-足背屈.mp4","2023/01/15/尖足-足背屈.mp4","2023/01/15/尖足-足背屈.mp4","2023/01/15/头后仰.mp4","2023/01/15/自发拉弓射箭样姿势.mp4","2023/01/15/拇指内扣收紧.mp4","2023/01/15/紧张时头偏向一侧.mp4","2023/01/15/飞机手的干预纠正方法.mp4","2023/01/15/肩内收的干预纠正方法.mp4","2023/01/15/婴儿抚触统合操.mp4","2023/01/15/婴儿抚触统合操.mp4","2023/01/15/下肢交叉.mp4","2023/01/15/肘屈曲的干预纠正方法.mp4","2023/01/15/7-床单荡悠练翻身.mp4","2023/01/15/1-头控式翻身.mp4","2023/01/15/6-球上翻身.mp4","2023/01/15/8-快速翻滚.mp4","2023/01/15/2-臂控式翻身.mp4","2023/01/15/4-上肢穴位刺激诱发翻身.mp4","2023/01/15/5-下肢穴位刺激诱发翻身.mp4","2023/01/15/3-腿控式翻身.mp4","2023/01/15/7-跨步站重心前移.mp4","2023/01/15/4-牵手直走.mp4","2023/01/15/6-原地踏步.mp4","2023/01/15/3-跨空间侧方行走.mp4","2023/01/15/2-扶墙侧方行走.mp4","2023/01/15/5-推椅行走.mp4","2023/01/15/9-鼓励迈步.mp4","2023/01/15/8-扶踝行走.mp4","2023/01/15/1-扶物侧方行走.mp4","2023/01/15/7-重心控制.mp4","2023/01/15/4-点穴促站.mp4","2023/01/15/5-前后脚跨步站立.mp4","2023/01/15/8-激发站立.mp4","2023/01/15/1-高位坐姿.mp4","2023/01/15/2-靠物站立.mp4","2023/01/15/6-平衡板站立.mp4","2023/01/15/10-足底平衡反应.mp4","2023/01/15/3-抓物扶物站.mp4","2023/01/15/9-跪立位转换.mp4","2023/01/15/7-手足抱球.mp4","2023/01/15/2-胸口趴.mp4","2023/01/15/4-球趴.mp4","2023/01/15/1-床趴.mp4","2023/01/15/9-坐位.mp4","2023/01/15/5-拉坐.mp4","2023/01/15/6-骑马式靠坐.mp4","2023/01/15/8-球上抱球.mp4","2023/01/15/3-大腿趴.mp4","2023/01/15/7-长坐位穴位刺激直腰.mp4","2023/01/15/10-立位屈髋.mp4","2023/01/15/1-手撑地坐.mp4","2023/01/15/4-坐位感知.mp4","2023/01/15/2-撑膝骑坐.mp4","2023/01/15/8-长坐位平衡.mp4","2023/01/15/3-盘腿扶膝坐.mp4","2023/01/15/9-抱位屈髋.mp4","2023/01/15/12-花生球坐位.mp4","2023/01/15/11-圆球坐位.mp4","2023/01/15/13-椅子坐位.mp4","2023/01/15/5-长坐位直腰.mp4","2023/01/15/6-长坐位腰背肌.mp4","2023/01/15/4-转式.mp4","2023/01/15/6-点穴促爬.mp4","2023/01/15/3-承式.mp4","2023/01/15/1-交叉模式.mp4","2023/01/15/2-启式.mp4","2023/01/15/5-合式.mp4","2023/01/15/7-交互四点爬.mp4"};
        String bucket = "fushu-storage";

        String type = "video";
        for (String key : keys) {
            String path = PathUtil.concatPaths(type, key);
            StringBuffer sb = new StringBuffer();
//        sb.append("avthumb/m3u8/noDomain/1/segtime/10|saveas/");
//        sb.append(UrlSafeBase64.encodeToString(bucket + ":" + m3u8Name));

            sb.append("avthumb/mp4/wmImage/");
            sb.append(UrlSafeBase64.encodeToString("kodo://fushu-storage/fushu-wm.png"));
            sb.append("/wmScale/0.15|avthumb/m3u8/noDomain/1/segtime/10|saveas/");
            String m3u8 = StringUtil.getM3u8(path);
            sb.append(UrlSafeBase64.encodeToString(bucket + ":" + m3u8));

            sb.append(";");
            sb.append("pm3u8/0/expires/43200");

            //构建持久化数据处理对象
            OperationManager operationManager = new OperationManager(auth, cfg);
            try {
                String persistentId = operationManager.pfop(bucket, path, sb.toString(), "recovery-video", true);
            } catch (QiniuException e) {
                System.out.println(e.getMessage());
            }
        }

//        Configuration cfg = new Configuration(Region.region1());
//        UploadManager uploadManager = new UploadManager(cfg);
////        String key = PathUtil.concatPaths("ts/test20.mp4");
//        String key = PathUtil.concatPaths("picture/test21.jpeg");
//
//        StringMap putPolicy = new StringMap();
////        String newName = "fushu-recovery:test20.m3u8";
//        String newName = "fushu-recovery:picture/test21_wm.jpeg";
//        String wm = "kodo://fushu-recovery/fushu-wm.png";
//
////        putPolicy.put("persistentOps", "avthumb/mp4/wmImage/" + UrlSafeBase64.encodeToString(wm) + "/wmScale/0.15|avthumb/m3u8/noDomain/1/segtime/10|saveas/" + UrlSafeBase64.encodeToString(newName) + ";pm3u8/0/expires/43200");
//        putPolicy.put("persistentOps", "watermark/1/image/" + UrlSafeBase64.encodeToString(wm) + "/gravity/NorthEast/ws/0.15|saveas/" + UrlSafeBase64.encodeToString(newName));
//        putPolicy.put("persistentPipeline", "recovery-video");
//        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
//        long expireSeconds = 3600;
//        String upToken = auth.uploadToken("fushu-recovery", key, expireSeconds, putPolicy);
////        File file = new File("/Users/tiger/Desktop/recovery/1626697060465_986a01323eb9429f82a9b338e6af660d_2055999258.mp4");
//        File file = new File("/Users/tiger/Desktop/southeast.jpeg");
//        try {
//            Response response = uploadManager.put(file.getAbsolutePath(), key, upToken);
//            DefaultPutRet defaultPutRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
//            log.info("upload file success : {}", defaultPutRet.key);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            log.error("upload file error : {}", r.toString());
//            try {
//                log.error("upload file error : {}", r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//            throw new StorageException("upload file to qiniu error");
//        }
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        String mkey = "picture/test21_wm.jpeg";
//        DownloadUrl url = new DownloadUrl("kfqn.fushuhealth.com", false, mkey);
////        url.setFop("pm3u8/0/expires/43200"); // 配置 fop
//        // 带有效期
//        String urlString = "";
//        long expireInSeconds = 3600;//10小时，可以自定义链接过期时间
////        Auth auth = Auth.create(accessKey, secretKey);
//        try {
//            urlString = url.buildURL(auth, DateUtil.getCurrentTimeStamp() + expireInSeconds);
//        } catch (QiniuException e) {
//            throw new StorageException("get qiniu file url error");
//        }
//        System.out.println(urlString);

//        Configuration cfg = new Configuration(Region.region1());
////        Auth auth = Auth.create(accessKey, secretKey);
//        String key = "video/1630934289369635_yinsi.mp4";
//        String bucket = "fushu-recovery";
//
//        String m3u8Name = StringUtil.getM3u8(key);
//
//        StringBuffer sb = new StringBuffer();
//        sb.append("avthumb/m3u8/noDomain/1/segtime/10|saveas/");
//        sb.append(UrlSafeBase64.encodeToString(bucket + ":" + m3u8Name));
//        sb.append(";");
//        sb.append("pm3u8/0/expires/43200");
//
//        String ops = sb.toString();
//
//        //构建持久化数据处理对象
//        OperationManager operationManager = new OperationManager(auth, cfg);
//
//
//        try {
//            String persistentId = operationManager.pfop(bucket, key, ops, "recovery-video", true);
//
//        } catch (QiniuException e) {
//
//        }
    }
}

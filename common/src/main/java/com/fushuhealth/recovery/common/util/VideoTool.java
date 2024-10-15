package com.fushuhealth.recovery.common.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.fushuhealth.recovery.common.core.domin.VideoInfo;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoTool.class);

    // 获取要取得的帧数
    private static final int GET_FRAMES_LENGTH = 5;

    /**
     * 功能:获取一张视频截图并保存同名的jpg文件到指定目录
     *
     * @param filePath 视频文件地址
     * @return
     */
    public static VideoInfo getVideoInfo(String filePath, String coverName) throws Exception {

        VideoInfo videoInfo = new VideoInfo();
        FFmpegFrameGrabber grabber;

        grabber = FFmpegFrameGrabber.createDefault(filePath);
        // 第一帧图片存储位置(也是视频路径)
        String targerFilePath = filePath.substring(0, filePath.lastIndexOf(File.separator));
        // 视频文件名
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        // 图片名称
        String targetFileName = StringUtil.uuid();
        grabber.start();
        // 视频总帧数
        int videoLength = grabber.getLengthInFrames();

        Frame frame = null;
        int i = 0;
        while (i < videoLength) {
            // 过滤前5帧,避免出现全黑的图片,依自己情况而定(每循环一次取一帧)
            frame = grabber.grabFrame();
            if ((i > GET_FRAMES_LENGTH) && (frame.image != null)) {
                break;
            }
            i++;
        }

        // 视频旋转度
        String rotate = grabber.getVideoMetadata("rotate");
        Java2DFrameConverter converter = new Java2DFrameConverter();
        // 绘制图片
        BufferedImage bi = converter.getBufferedImage(frame);
        if (rotate != null) {
            // 旋转图片
            bi = rotate(bi, Integer.parseInt(rotate));
        }
        // 图片的类型
//        String imageMat = "jpg";
        // 图片的完整路径
        String imagePath = targerFilePath + File.separator + coverName;

        // 创建文件
        File output = new File(imagePath);
        ImageIO.write(bi, "jpg", output);

        // 拼接Map信息
//            result.put("videoLength", videoLength); // 视频总帧数
//            result.put("videoWide", bi.getWidth()); // 视频的宽
//            result.put("videoHigh", bi.getHeight());// 频的高
//            result.put("rotate", (null == rotate || "".equals(rotate))? "0" : rotate); // 视频的旋转度
//            result.put("format", grabber.getFormat()); // 视频的格式
//            result.put("imgPath", output.getPath());
//            result.put("duration", duration);

        int duration = (int)grabber.getLengthInTime() / (1000 * 1000); // 此视频时长(s/秒)
        videoInfo.setDuration(duration);
        videoInfo.setFormat(grabber.getFormat());
        videoInfo.setHeight(bi.getHeight());
        videoInfo.setWidth(bi.getWidth());
        videoInfo.setCover(output);

        grabber.stop();
        return videoInfo;
    }

    public static File convertToMp4(File file) throws Exception {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file);
        String fileName = null;
        Frame captured_frame = null;
        FFmpegFrameRecorder recorder = null;
        frameGrabber.start();
        fileName = file.getAbsolutePath() + ".mp4";

        recorder = new FFmpegFrameRecorder(fileName, frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), frameGrabber.getAudioChannels());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        recorder.setFrameRate(frameGrabber.getFrameRate());
        recorder.setSampleRate(frameGrabber.getSampleRate());

        recorder.setAudioChannels(frameGrabber.getAudioChannels());
        recorder.setFrameRate(frameGrabber.getFrameRate());
        recorder.start();
        while ((captured_frame = frameGrabber.grabFrame()) != null) {
            recorder.record(captured_frame);
        }
        recorder.stop();
        recorder.release();
        frameGrabber.stop();
        return new File(fileName);
    }

    public static void main(String[] args) throws Exception {
        String input = "/Users/tiger/Desktop/0_keypoints.mp4";
        File file = convertToMp4(new File(input));
        System.out.println(file.getAbsolutePath());
    }

    // ==================== private method ====================

    /**
     * 功能:根据视频旋转度来调整图片
     *
     * @param src 捕获的图像
     * @param angel 视频旋转度
     * @return BufferedImage
     */
    private static BufferedImage rotate(BufferedImage src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        int type = src.getColorModel().getTransparency();
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
        BufferedImage bi = new BufferedImage(rect_des.width, rect_des.height, type);
        Graphics2D g2 = bi.createGraphics();
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bi;
    }

    /**
     * 功能:计算图片旋转大小
     *
     * @param src 屏幕坐标中捕获的矩形区域
     * @param angel 视频旋转度
     * @return
     */
    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }

    public static boolean videoConvert(File originFile, File destFile) throws Exception{

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(originFile.getAbsoluteFile());
        grabber.start();
        int widthTemp = grabber.getImageWidth();
        int heightTemp = grabber.getImageHeight();
        double width = 844;
        int height = (int)(width / widthTemp * heightTemp);

        Runtime runtime = Runtime.getRuntime();
        String[] command = {"ffmpeg", "-i", originFile.getAbsolutePath(), "-s", "800x450", destFile.getAbsolutePath()};
        LOGGER.info(Arrays.deepToString(command));
        Process exec = runtime.exec(command);

        logInfo(exec);
        logError(exec);

        int result = exec.waitFor();
        LOGGER.info("========= result {} =========", result);
        if (0 == result) {
            return true;
        }
        return false;

    }

    private static void logError(Process exec) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
            String line = null;
            while ( (line = br.readLine()) != null) {
                LOGGER.info("===== {} =====", line);
            }
        } catch (IOException e) {

        }
    }

    private static void logInfo(Process exec) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String line = null;
            while ( (line = br.readLine()) != null) {
                LOGGER.info("===== {} =====", line);
            }
        } catch (IOException e) {

        }
    }
}

package com.fushuhealth.recovery.common.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.UUID;

import static org.apache.fontbox.afm.AFMParser.CHARACTERS;


public class StringUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateFileName(String ext) {
        String rand = String.valueOf(Math.abs(new Random(System.currentTimeMillis()).nextInt()));
        String uuid = StringUtil.uuid();
        String filename = StringUtils.joinWith("_", System.currentTimeMillis(), uuid, rand) + "." + ext;
        return filename;
    }

    public static String getUrlPath(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return uri.getPath().substring(1);
    }

    public static String getUrlAction(String url) throws URISyntaxException {
        String path = getUrlPath(url);
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }

    public static String getM3u8(String originName) {
        String extension = FilenameUtils.getExtension(originName);
        return originName.replace(extension, "m3u8");
    }

    public static String getRotateName(String originName) {
        String extension = FilenameUtils.getExtension(originName);
        return originName.replace("."+ extension, "_r." + extension);
    }

    public static String getPicWm(String originName) {
        String extension = FilenameUtils.getExtension(originName);
        return originName.replace("." + extension, "_wm." + extension);
    }

    public static String getRandomNumber(int length) {
        String random=(int)((Math.random()*length+1)*100000)+"";
        return random;
    }

    public static String getOrderNo() {
        String time = DateUtil.getNowYMDHMS();
        String randomString = RandomStringUtils.randomNumeric(8);
        return time + randomString;
    }

    public static String fenToYuan(long fen) {
        return BigDecimal.valueOf(Double.valueOf((double)fen) / 100.0D).setScale(2, 4).toPlainString();
    }

    public static Long yuanToFen(String amount) {
        BigDecimal bigDecimal = new BigDecimal(amount).setScale(2);
        return bigDecimal.multiply(new BigDecimal(100)).longValue();
    }

    public static String generateRandomString(int length) {
        // 创建 Random 实例
        Random random = new Random();

        // 创建 StringBuilder 以构建随机字符串
        StringBuilder stringBuilder = new StringBuilder();

        // 从字符集中随机选择字符，并构建字符串
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        // 返回生成的随机字符串
        return stringBuilder.toString();
    }

    public static int[] convert2int(String str) {

        int[] result = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            result[i] = Integer.parseInt(str.charAt(i) + "");
        }
        return result;
    }
}

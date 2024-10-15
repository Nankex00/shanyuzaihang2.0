package com.fushuhealth.recovery.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfUtil {

    private static final Integer DPI = 300;

    private static final String IMG_TYPE = "png";

    public static List<File> pdfToImg(File pdfFile) throws IOException {

        ArrayList<File> list = new ArrayList<>();

        PDDocument document = PDDocument.load(pdfFile);
        PDFRenderer renderer = new PDFRenderer(document);

        BufferedImage[] bufferedImages = new BufferedImage[document.getNumberOfPages()];
        for (int i = 0; i< document.getNumberOfPages(); i++) {
            BufferedImage bufferedImage = renderer.renderImageWithDPI(i, DPI);
            bufferedImages[i] = bufferedImage;
//            File img = new File(pdfFile.getParent(), pdfFile.getName() + "_" + i + ".png");
//            ImageIO.write(bufferedImage, IMG_TYPE, img);
//            list.add(img);
        }
        File img = new File(pdfFile.getParent(), pdfFile.getName() + ".png");
        mergeImage(bufferedImages, 2, img.getAbsolutePath());
        list.add(img);
        return list;
    }


    /**
     * 图片拼接
     * @param images     图片数组
     * @param type      1 横向拼接， 2 纵向拼接
     * （注意：必须两张图片长宽一致）
     */
    public static void mergeImage(BufferedImage[] images, int type, String targetFile) throws IOException {
        int len = images.length;
        int[][] ImageArrays = new int[len][];

        for (int i = 0; i < len; i++) {
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }
        int newHeight = 0;
        int newWidth = 0;
        for (int i = 0; i < images.length; i++) {
            // 横向
            if (type == 1) {
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();
            } else if (type == 2) {// 纵向
                newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
                newHeight += images[i].getHeight();
            }
        }
        if (type == 1 && newWidth < 1) {
            return;
        }
        if (type == 2 && newHeight < 1) {
            return;
        }
        // 生成新图片
        try {
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {
                if (type == 1) {
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0,
                            images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }
            //输出想要的图片
            ImageIO.write(ImageNew, IMG_TYPE, new File(targetFile));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static void main(String[] args) {
        long start = DateUtil.getCurrentTimeStamp();
        try {
            pdfToImg(new File("/Users/tiger/Desktop/test.pdf"));
        } catch (IOException e) {
            System.out.printf("Error:"+ e);
        }
        long end = DateUtil.getCurrentTimeStamp();
        System.out.println("cost:" + (end-start));
    }
}

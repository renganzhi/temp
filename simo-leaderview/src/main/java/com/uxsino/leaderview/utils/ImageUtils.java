package com.uxsino.leaderview.utils;

import com.idrsolutions.image.png.PngCompressor;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    public static byte[] compressImage(byte[] sourceImg, String extension) throws NullPointerException{
        if(sourceImg.length == 0)
            throw new NullPointerException("要压缩的文件为空");

        ByteArrayInputStream sourceInputStream = new ByteArrayInputStream(sourceImg);
        ByteArrayOutputStream targetOutputStream = new ByteArrayOutputStream();

        try {
            if(extension.equals("jpeg") || extension.equals("jpg")){
                Thumbnails.of(sourceInputStream)
                        .scale(1.0)
                        .outputFormat("jpeg")
                        .outputQuality(0.1f)
                        .toOutputStream(targetOutputStream);
            }else if(extension.equals("png") || extension.equals("gif")){
                PngCompressor.compress(sourceInputStream, targetOutputStream);
            }
        } catch (IOException e) {
            log.error("LEADERVIEW -> 压缩图片抛出IO异常！");
            return null;
        }

        byte[] targetImg = targetOutputStream.toByteArray();

        if(sourceImg.length!=0 && targetImg.length==0)
            return null;
        else
            return targetImg;
    }
}

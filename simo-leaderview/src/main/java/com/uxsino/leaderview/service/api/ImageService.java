package com.uxsino.leaderview.service.api;

import com.google.common.collect.Lists;
import com.uxsino.leaderview.entity.HomeTemplateImg;
import com.uxsino.leaderview.entity.HomeTemplateImgCompressed;
import com.uxsino.leaderview.entity.UploadedFile;
import com.uxsino.leaderview.entity.UploadedFileCompressed;
import com.uxsino.leaderview.service.HomeTemplateImgService;
import com.uxsino.leaderview.service.UploadedFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;

@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final List<String> IMG_EXTENSION_LIST = Lists
            .newArrayList("bmp,jpg,jpeg,png,tif,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,wmf,webp,jfif"
                    .split(","));

    @Autowired
    private UploadedFileService uploadedFileService;

    @Autowired
    private HomeTemplateImgService templateImgService;

    /**
     *
     * @param id
     * @param isCustom
     * @param isCompressed
     * @param action 用于不同接口对图片进行不同的处理，比如直接返回、加水印等其他处理
     */
    public void getImg(long id, boolean isCustom, boolean isCompressed, BiConsumer<Byte[], String> action){
        byte[] data;
        String ext;

        if (isCustom) {
            UploadedFileCompressed uploadedFileCompressed = uploadedFileService.findByOriginFileId(id);
            UploadedFile f = uploadedFileCompressed.getUploadedFile();
            if(f == null){
                logger.error("文件 -> {} not exists ！", id);
                return;
            }

            ext = f.getExtension();
            if(!isCompressed || uploadedFileCompressed.getCompressedFileStream()==null)
                data = f.getFileStream();
            else
                data = uploadedFileCompressed.getCompressedFileStream();
        }else {
            HomeTemplateImgCompressed imgCompressed = templateImgService.getCompressedImgByOriginId(id);
            HomeTemplateImg f = imgCompressed.getHomeTemplateImg();
            if(f == null){
                logger.error("文件 -> {} not exists ！", id);
                return;
            }
            ext = f.getExtension();
            if(!isCompressed || imgCompressed.getCompressedFileStream()==null)
                data = f.getFileStream();
            else
                data = imgCompressed.getCompressedFileStream();
        }

        if (!IMG_EXTENSION_LIST.contains(ext)) {
            logger.error("文件 -> {} 非图片！", id);
            return;
        }

        Byte[] dataWrap = new Byte[data.length];
        for(int i=0; i<data.length; i++)
            dataWrap[i] = data[i];

        action.accept(dataWrap, ext);
    }
}

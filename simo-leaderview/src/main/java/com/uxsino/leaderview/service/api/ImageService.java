package com.uxsino.leaderview.service.api;

import com.google.common.collect.Lists;
import com.uxsino.leaderview.entity.HomeTemplateImg;
import com.uxsino.leaderview.entity.UploadedFile;
import com.uxsino.leaderview.service.HomeTemplateImgService;
import com.uxsino.leaderview.service.UploadedFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final List<String> IMG_EXTENSION_LIST = Lists
            .newArrayList("bmp,jpg,jpeg,png,tif,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,wmf,webp,jfif"
                    .split(","));

    private final Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("gif", "image/gif");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("png", "image/png");
        }
    };

    @Autowired
    private UploadedFileService uploadedFileService;

    @Autowired
    private HomeTemplateImgService templateImgService;

    public void getImg(long id, boolean isCustom, BiConsumer<Byte[], String> action){
        byte[] data;
        String ext;

        if (isCustom) {
            UploadedFile f = uploadedFileService.findById(id);
            if(f == null){
                logger.error("文件 -> {} not exists ！", id);
                return;
            }
            ext = f.getExtension();
            data = f.getFileStream();
        }else {
            HomeTemplateImg f = templateImgService.getById(id);
            if(f == null){
                logger.error("文件 -> {} not exists ！", id);
                return;
            }
            ext = f.getExtension();
            data = f.getFileStream();
        }

        if (!IMG_EXTENSION_LIST.contains(ext)) {
            logger.error("文件 -> {} 非图片！", id);
            return;
        }

        Byte[] dataWrap = new Byte[data.length];
        for(int i=0; i<data.length; i++)
            dataWrap[i] = data[i];

        action.accept(dataWrap, CONTENT_TYPE_MAP.getOrDefault(ext, "image/png"));
    }
}

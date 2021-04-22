package com.uxsino.leaderview.service;

import com.google.common.io.Files;
import com.google.common.primitives.Longs;
import com.uxsino.commons.utils.ClassPathResourceWalker;
import com.uxsino.leaderview.dao.IHomeTemplateImgDao;
import com.uxsino.leaderview.entity.HomeTemplateImg;

import com.uxsino.leaderview.entity.HomeTemplateImgCompressed;
import com.uxsino.leaderview.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 模板图片的操作方法
 */
@Service
public class HomeTemplateImgService {

    @Autowired
    private IHomeTemplateImgDao imgDao;

    private Logger logger = LoggerFactory.getLogger(HomeTemplateImgService.class);

    private static final String FILEPATH = "classpath*:/img/home/*/*.*";

    /**
     * 初始化模板的图片
     */
    @Transactional
    public void init() {
        // 如果表中存在数据则不进行插入数据的操作
        long count = imgDao.count();
        try {
            if (count > 0) {
                delAll();
            }
            new ClassPathResourceWalker(FILEPATH).forEach(file -> {
                InputStream in;
                try {
                    in = file.openStream();
                } catch (IOException e) {
                    logger.error("读取文件失败: ", e);
                    return;
                }
                String extension = Files.getFileExtension(file.getFile());
                String name = Files.getNameWithoutExtension(file.getFile());
                // 图片id取决于图片名称最后一个"_"后的数字
                Long id = Longs.tryParse(name.substring(name.lastIndexOf("_") + 1));
                if (id == null) {
                    return;
                }
                try {
                    // 说明：读取文件不能使用  in.read(byte[in.available()])一次性读取，读取大文件可能存在读取不完整的情况，使用缓存进行逐步读取。
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] cache = new byte[1024];
                    int size;
                    while ((size = in.read(cache)) != -1) {
                        out.write(cache, 0, size);
                    }
                    HomeTemplateImg img = new HomeTemplateImg();
                    HomeTemplateImgCompressed compressedImg = new HomeTemplateImgCompressed();
                    img.setId(id);
                    img.setExtension(extension);
                    img.setFileStream(out.toByteArray());
                    img.setName(name + "." + extension);
                    compressedImg.setId(id);
                    compressedImg.setCompressedFileStream(ImageUtils.compressImage(out.toByteArray(), extension));
                    img.setHomeTemplateImgCompressed(compressedImg);
                    this.save(img);
                } catch (IOException e) {
                    logger.error("", e);
                } finally {
                    try {
                        in.close();
                    } catch (Exception e) {
                        logger.error("关闭流失败", e);
                    }
                }
            });
        } catch (IOException e) {
            logger.error("初始化主页大屏模板的图片失败：", e);
        }
    }

    /**
     * 清空所有表并重新初始化
     */
    @Transactional
    public void reFresh() {
        this.delAll();
        this.init();
    }

    @Transactional
    public void delAll() {
        imgDao.deleteAll();
    }

    /**
     * 保存单个的图片文件
     */
    @Transactional
    public void save(HomeTemplateImg img) {
        imgDao.save(img);
    }


    public HomeTemplateImg getById(Long id) {
        return imgDao.findOne(id);
    }

    public Long getMaxId() {
        return imgDao.getMaxId();
    }

    @Transactional
    public void saveAll(List<HomeTemplateImg> imgs){
        imgDao.saveAll(imgs);
    }

}

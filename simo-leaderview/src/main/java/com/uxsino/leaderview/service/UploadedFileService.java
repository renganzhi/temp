package com.uxsino.leaderview.service;

import javax.transaction.Transactional;

import com.uxsino.leaderview.entity.UploadedFileCompressed;
import com.uxsino.leaderview.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxsino.leaderview.dao.IUploadedFileDao;
import com.uxsino.leaderview.entity.UploadedFile;

import java.util.List;

@Service
@Transactional
public class UploadedFileService {

    private static final Logger log = LoggerFactory.getLogger(UploadedFileService.class);

    @Autowired
    private IUploadedFileDao uploadedFileDao;

    /**
     * 保存文件
     * @param file
     */
    public void save(UploadedFile file) {
        uploadedFileDao.save(file);
    }

    /**
     * 查询文件
     * @param id 文件ID
     * @return
     */
    public UploadedFile findById(Long id) {
        return uploadedFileDao.findOne(id);
    }

    public List<UploadedFile> findByIds(List<Long> ids){
        return uploadedFileDao.getByIdIn(ids);
    }

    /**
     * 对老环境下的数据库，已经在simo_upload_file存有一些图片而没有对应的压缩数据时，调用该方法后将会遍历该表
     * 一个一个生成对应的压缩数据
     * @return
     */
    public boolean generateCompressedCustomImage(){
        List<UploadedFile> uploadedFiles = uploadedFileDao.findAll();
        for(UploadedFile temp: uploadedFiles){
            byte[] compressedData = ImageUtils.compressImage(temp.getFileStream(), temp.getExtension());
            if(compressedData != null){
                UploadedFileCompressed compressedFile = new UploadedFileCompressed();
                try {
                    compressedFile.setCompressedFileStream(compressedData);
                } catch (Exception e) {
                    log.error("id为{}的文件长度为0！", temp.getId());
                }
                temp.setUploadedFileCompressed(compressedFile);
                uploadedFileDao.save(temp);
            }
        }

        return true;
    }
}

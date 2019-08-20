package com.uxsino.leaderview.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxsino.leaderview.dao.IUploadedFileDao;
import com.uxsino.leaderview.entity.UploadedFile;

@Service
@Transactional
public class UploadedFileService {

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

}

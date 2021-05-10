package com.uxsino.leaderview.service;

import javax.transaction.Transactional;

import com.uxsino.leaderview.dao.IUploadedFileCompressedDao;
import com.uxsino.leaderview.entity.UploadedFileCompressed;
import com.uxsino.leaderview.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    private IUploadedFileCompressedDao uploadedFileCompressedDao;

    /**
     * 保存文件
     * @param file
     */
    public void save(UploadedFile file, UploadedFileCompressed fileCompressed) {
        uploadedFileDao.save(file);
        uploadedFileCompressedDao.save(fileCompressed);
    }

    /**
     * 查询文件
     * @param id 文件ID
     * @return
     */
    public UploadedFileCompressed findByOriginFileId(Long id) {
        return uploadedFileCompressedDao.findByOriginFileId(id);
    }

    public List<UploadedFile> findByIds(List<Long> ids){
        return uploadedFileDao.getByIdIn(ids);
    }

    /**
     * 对老环境下的数据库，已经在simo_upload_file存有一些图片而没有对应的压缩数据时，调用该方法后将会遍历该表
     * 一个一个生成对应的压缩数据
     * @return
     */
    @Transactional
    public boolean generateCompressedCustomImage(){
        long size = uploadedFileDao.count();
        int pageSize = 50;
        int totalPage = size%pageSize==0? (int)size/pageSize: (int)size/pageSize+1;
        long justNow = System.currentTimeMillis();
        try {
            for(int i=0; i<totalPage; i++){
                Page<UploadedFile> uploadedFiles = uploadedFileDao.findAll(PageRequest.of(i, pageSize));
                for(UploadedFile temp: uploadedFiles){
                    byte[] compressedData = ImageUtils.compressImage(temp.getFileStream(), temp.getExtension());
                    UploadedFileCompressed fileCompressed = new UploadedFileCompressed();
                    fileCompressed.setUploadedFile(temp);
                    if(compressedData != null)
                        fileCompressed.setCompressedFileStream(compressedData);
                    uploadedFileCompressedDao.save(fileCompressed);
                }
                log.info("LEADERVIEW -> 分页完成第" + i + "页， 共" + totalPage + "页");
            }
        } catch (Exception e) {
            log.error("LEADERVIEW -> 压缩自定义图片抛出异常，stacktrace如下：", e);
            return false;
        }
        long difference = System.currentTimeMillis() - justNow;
        log.info("LEADERVIEW -> 压缩自定义图片完成，总共花费时间：" + difference/1000 + "s");
        return true;
    }
}

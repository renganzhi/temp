package com.uxsino.leaderview.service;

import com.uxsino.leaderview.dao.IVideoFileDao;
import com.uxsino.leaderview.entity.VideoFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class VideoFileService {

    @Autowired
    private IVideoFileDao videoFileDao;

    /**
     * 保存视频文件
     * @param videoFile
     */
    public void save(VideoFile videoFile){
        videoFileDao.save(videoFile);
    }

    /**
     * 根据文件名获取文件基本信息
     * @param name
     * @return
     */
    public VideoFile getByName(String name){
        return videoFileDao.getByName(name);
    }

}

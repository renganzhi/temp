package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.VideoFile;

public interface IVideoFileDao extends ICustomRepository<VideoFile, Long> {
    VideoFile getByName(String name);
}

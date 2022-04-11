package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.VideoScheme;

public interface IVideoSchemeDao extends ICustomRepository<VideoScheme, Long> {
    VideoScheme getByName(String name);
}

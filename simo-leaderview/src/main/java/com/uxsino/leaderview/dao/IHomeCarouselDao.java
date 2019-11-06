package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.HomeCarousel;

import java.util.List;

public interface IHomeCarouselDao extends ICustomRepository<HomeCarousel, Long> {
    List<HomeCarousel> findByUserId(Long UserId);
}

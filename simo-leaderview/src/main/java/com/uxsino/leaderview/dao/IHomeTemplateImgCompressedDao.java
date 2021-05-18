package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.HomeTemplateImgCompressed;
import feign.Param;
import org.springframework.data.jpa.repository.Query;

public interface IHomeTemplateImgCompressedDao extends ICustomRepository<HomeTemplateImgCompressed, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM simo_mc_home_template_img_compressed AS htic WHERE htic.origin_img_id = ?1")
    HomeTemplateImgCompressed findByOriginImgId(Long id);
}

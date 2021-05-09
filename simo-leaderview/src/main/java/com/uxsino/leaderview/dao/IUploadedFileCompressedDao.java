package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.UploadedFileCompressed;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IUploadedFileCompressedDao extends ICustomRepository<UploadedFileCompressed, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM simo_uploaded_file_compressed AS ufc WHERE ufc.origin_file_id = ?1")
    UploadedFileCompressed findByOriginFileId(Long id);
}

package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.UploadedFile;

import java.util.List;

public interface IUploadedFileDao extends ICustomRepository<UploadedFile, Long> {

    List<UploadedFile> getByIdIn(List<Long> ids);


    List<UploadedFile> getByName(String name);



}

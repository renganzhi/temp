package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.CustomDotScheme;

public interface ICustomDotSchemeDao extends ICustomRepository<CustomDotScheme, Long> {
    CustomDotScheme getByName(String name);
}

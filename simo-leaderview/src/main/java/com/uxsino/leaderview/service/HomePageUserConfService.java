package com.uxsino.leaderview.service;

import com.uxsino.leaderview.dao.IHomePageUserConfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomePageUserConfService {
    @Autowired
    private IHomePageUserConfDao homePageUserConfDao;


}

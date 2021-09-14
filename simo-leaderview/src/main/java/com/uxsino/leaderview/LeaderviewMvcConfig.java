package com.uxsino.leaderview;

import com.uxsino.leaderview.utils.LeaderviewResourceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LeaderviewMvcConfig implements WebMvcConfigurer {

    @Value("${web.upload.file.path}")
    private String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fullPath = LeaderviewResourceUtil.getFilesFullPath(filePath);
        registry.addResourceHandler("/staticlv/**").addResourceLocations("file:" + fullPath);
        //registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/");
    }
}

package com.uxsino.leaderview.utils;

import com.uxsino.commons.utils.SimoResourceUtil;

import java.io.File;

/**
 * 3d机房资源工具类
 */
public class LeaderviewResourceUtil {
    public static String getFilesFullPath(String relativePath){
        String fullPath = SimoResourceUtil.getCurrentRunningPath() + File.separator + relativePath + File.separator;
        return fullPath;
    }
}

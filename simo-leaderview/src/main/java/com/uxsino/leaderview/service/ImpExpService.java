package com.uxsino.leaderview.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.entity.*;
import com.uxsino.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 完成模板的导入导出、zip打包功能、大屏sql处理的service
 * author xu_zhudong
 */
@Service
@Slf4j
public class ImpExpService {

    @Autowired
    private HomeTemplateImgService homeTemplateImgService;

    @Autowired
    private UploadedFileService uploadedFileService;

    @Autowired
    private VideoFileService videoFileService;

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private HomePageUserConfService homePageUserConfService;

    private static Set<String> videoSet = Sets.newHashSet();

    private static Pattern imgPattern = Pattern.compile("/leaderview/home/getImg/true/(\\d*)\"");
    private static Pattern img2Pattern = Pattern.compile("/leaderview/home/getImg/true/(\\d*)");
    private static Pattern numPattern = Pattern.compile("[^0-9]");
    private static Pattern videoPattern = Pattern.compile("/leaderview/home/file/getVideo/uploaded_file(\\d*)\\.mp4");
    private static Pattern ipPattern = Pattern.compile("http://(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d*)/leaderview/home/file/getVideo/uploaded_file(\\d*)\\.mp4");
    private static Pattern localPattern = Pattern.compile("http://localhost:(\\d*)/leaderview/home/file/getVideo/uploaded_file(\\d*)\\.mp4");
    private static Pattern videoNamePattern = Pattern.compile("uploaded_file(\\d*)\\.mp4");
    private static Pattern linkPattern = Pattern.compile("linkId\":(\\d*)");
    private static Integer zipNum = 1;

    private String zipPath = this.getClass().getClassLoader().getResource("img").getFile();

    @Value("${web.upload.file.path}")
    private String upload_path;

    @Transactional
    @SuppressWarnings("unchecked")
    public String  makeTemplate(List<HomePage> pages){
        JSONArray json = new JSONArray();
        Set<Long> ids = Sets.newConcurrentHashSet();
        pages.forEach(p -> ids.add(p.getId()));
        for (HomePage page: pages) {
            json.add(makeTemplate(page,ids));
        }
        String jsonPath = zipPath + "/templateZip" + zipNum + "/json/config.json";
        writeConfigJson(json.toJSONString(),jsonPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(zipPath + "/templateZip" + zipNum + ".zip"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipUtils.toZip(zipPath + "/templateZip" + zipNum + "/",fos, true);
        return zipPath + "/templateZip" + zipNum++ + ".zip";
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public JSONObject makeTemplate(HomePage page, Set<Long> ids) {
        if (!videoSet.isEmpty()){
            videoSet = Sets.newHashSet();
        }
        Integer num = homeTemplateImgService.getMaxId();
        JSONObject viewConf = templateTransform(page.getViewConf(),num,ids);
        JSONObject viewImage = templateTransform(page.getViewImage(),viewConf.getInteger("num"));
        JSONObject composeObj = templateTransform(page.getComposeObj(),viewImage.getInteger("num"));
        JSONObject paintObj = templateTransform(page.getPaintObj(), composeObj.getInteger("num"));
        JSONObject result = new JSONObject();
        result.put("name",page.getName());
        result.put("id",page.getId());
        result.put("viewConf",viewConf.getString("str"));
        result.put("viewImage",viewImage.getString("str"));
        result.put("paintObj",paintObj.getString("str"));
        result.put("composeObj",composeObj.getString("str"));
        result.put("linkConfig",viewConf.getJSONObject("linkConfig"));
        return result;
    }

    private JSONObject templateTransform(String origin, Integer num, Set<Long> ids){
        JSONObject result = templateTransform(origin, num);
        //跳转处理
        linkProcess(result, ids);
        return result;
    }



    private JSONObject templateTransform(String origin, Integer num){
        JSONObject result = new JSONObject();
        result.put("str",origin);
        //图片处理
        imgProcess(result, num);
        //视频处理
        videoProcess(result);
        return result;
    }

    private JSONObject videoProcess(JSONObject result){
        String str = result.getString("str");
        Set<String> set = Sets.newHashSet();
        Matcher vm = ipPattern.matcher(str);
        while (vm.find()){
            Matcher vm2 = videoPattern.matcher(vm.group());
            set.add(vm2.replaceAll("").trim());
        }
        vm = localPattern.matcher(str);
        while (vm.find()){
            Matcher vm2 = videoPattern.matcher(vm.group());
            set.add(vm2.replaceAll("").trim());
        }
        for (String s: set) {
            str = str.replace(s,"");
        }

        //视频移植
        set = Sets.newHashSet();
        Matcher videoMatcher = videoNamePattern.matcher(str);
        while (videoMatcher.find()){
            set.add(videoMatcher.group());
        }
        String videoPath = zipPath + "/templateZip" + zipNum + "/video/";
        for (String s: set) {
            VideoFile videoFile = videoFileService.getByName(s);
            copyFile(videoFile.getFilePath(), videoPath + videoFile.getName());
        }
        result.put("str",str);
        return result;
    }

    private JSONObject imgProcess(JSONObject result, Integer num){
        String str = result.getString("str");
        // 自定义图片id处理
        List<String> imgList = new ArrayList<>();
        List<Long> ids = Lists.newArrayList();
        Map<Long,Integer> map = Maps.newHashMap();
        Matcher m = imgPattern.matcher(str);
        while (m.find()){
            if (imgList.contains(m.group())){
                continue;
            }
            imgList.add(m.group());
            Matcher m2 = numPattern.matcher(m.group());
            ids.add(Long.valueOf(m2.replaceAll("").trim()));
        }
        for (String string : imgList) {
            str = str.replace(string, "/leaderview/home/getImg/false/" + ++num +"\"");
            Matcher m2 = numPattern.matcher(string);
            map.put(Long.valueOf(m2.replaceAll("").trim()), num);
        }
        if (ObjectUtils.isEmpty(imgList)){
            m = img2Pattern.matcher(str);
            while (m.find()){
                if (imgList.contains(m.group())){
                    continue;
                }
                imgList.add(m.group());
                Matcher m2 = numPattern.matcher(m.group());
                ids.add(Long.valueOf(m2.replaceAll("").trim()));
            }
            for (String string : imgList) {
                str = str.replace(string, "/leaderview/home/getImg/false/" + ++num);
                Matcher m2 = numPattern.matcher(string);
                map.put(Long.valueOf(m2.replaceAll("").trim()), num);
            }
        }


        //图片移植
        List<UploadedFile> uploadedFiles = uploadedFileService.findByIds(ids);
        List<HomeTemplateImg> imgs = Lists.newArrayList();
        for (UploadedFile uploadedFile: uploadedFiles) {
            String name = "templateCustom_" + map.get(uploadedFile.getId()) + "." + uploadedFile.getExtension();
            HomeTemplateImg img = new HomeTemplateImg();
            img.setExtension(uploadedFile.getExtension());
            img.setId(Long.valueOf(map.get(uploadedFile.getId()).toString()));
            img.setName(name);
            img.setFileStream(uploadedFile.getFileStream());
            String imgPath = zipPath + "/templateZip" + zipNum + "/img/";
            saveToFile(imgPath + name, uploadedFile.getFileStream());
            imgs.add(img);
        }
        homeTemplateImgService.saveAll(imgs);
        result.put("list", imgList);
        result.put("num", num);
        result.put("str", str);
        return result;
    }

    private JSONObject linkProcess(JSONObject result, Set<Long> ids) {
        String str = result.getString("str");
        Matcher m = linkPattern.matcher(str);
        Integer i = 0;
        Map<String ,String > map = Maps.newHashMap();
        String s;
        JSONObject linkConfig = new JSONObject();
        Set<Long> processedId = Sets.newHashSet();
        while (m.find()){
            Matcher numMatcher = numPattern.matcher(m.group());
            Long id = Long.valueOf(numMatcher.replaceAll("").trim());
            if (processedId.contains(id)){
                continue;
            }
            if (ids.contains(id)){
                s = "linkId\":${" + i + "}";
                linkConfig.put("${" + i + "}", id);
                i++;
                processedId.add(id);
            }else {
                s = "linkId\": 0";
            }
            map.put(m.group(), s);
        }
        for (Map.Entry<String,String> entry: map.entrySet()) {
            str = str.replace(entry.getKey(), entry.getValue());
        }
        result.put("str",str);
        result.put("linkConfig",linkConfig);
        return result;
    }

    private static Boolean saveToFile(String fName, byte[] data){
        OutputStream fos = null;
        try {
            File file = new File(fName);
            File parent = file.getParentFile();
            if ((!parent.exists()) && (!parent.mkdirs())) {
                return false;
            }
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                File newFile = new File(newPath);
                File parent = newFile.getParentFile();
                if ((!parent.exists()) && (!parent.mkdirs())) {
                    return;
                }
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
    }

    public void writeConfigJson(String json, String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.delete();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Boolean processZip(String file, String tempName){
        ZipFile zf = null;
        InputStream in = null;
        JSONArray config = new JSONArray();
        Boolean success = true;
        Map<Long ,Long > idMap = Maps.newHashMap();
        JSONArray linkArray = new JSONArray();
        try {
            zf = new ZipFile(file);
            in = new BufferedInputStream(new FileInputStream(file));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry ze;
            java.util.Enumeration zipEnum = zf.entries();
            while (zipEnum.hasMoreElements()) {
                ze = (ZipEntry) zipEnum.nextElement();
                if (ze.isDirectory()) {

                } else {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(zf.getInputStream(ze)));
                    br.close();
                    long size = ze.getSize();
                    String name = ze.getName();
                    InputStream zfIn = zf.getInputStream(ze);
                    // 图片处理
                    if (name.contains("img")){
                        try {
                            byte[] b = new byte[(int)size];
                            int readCount = 0; // 已经成功读取的字节的个数
                            while (readCount < size) {
                                readCount += zfIn.read(b, readCount, (int)size - readCount);
                            }
                            HomeTemplateImg img = new HomeTemplateImg();
                            img.setExtension(name.substring(name.indexOf(".") + 1));
                            img.setId(Long.valueOf(name.substring(name.indexOf("_") + 1,name.indexOf("."))));
                            img.setFileStream(b);
                            img.setName(name.substring(name.indexOf("img/") + 4 ));
                            homeTemplateImgService.save(img);
                        }catch (Exception e){
                            log.error("图片解析错误");
                            e.printStackTrace();
                        }finally {
                            success = false;
                            zfIn.close();
                        }
                    }
                    // 导出配置处理
                    else if (name.contains("json")){
                        try {
                            br = new BufferedReader(new InputStreamReader(zfIn));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null)
                            {
                                sb.append(line);
                            }
                            config = JSON.parseArray(sb.toString());
                        }catch (Exception e){
                            log.error("导出配置解析错误");
                            success = false;
                        }finally {
                            zfIn.close();
                        }
                    }
                    // 视频处理
                    else if(name.contains("video")){
                        FileOutputStream fileOut = null;
                        try {
                            File path = new File(upload_path + File.separator + "video");
                            File videoFile = new File(path.getAbsoluteFile() + File.separator + name.substring(name.indexOf("uploaded")));
                            if (videoFile.exists()){
                                continue;
                            }
                            fileOut = new FileOutputStream(videoFile);
                            byte[] buf = new byte[1024 * 8];
                            while (true) {
                                int read = 0;
                                if (zfIn != null) {
                                    read = zfIn.read(buf);
                                }
                                if (read == -1) {
                                    break;
                                }
                                fileOut.write(buf, 0, read);
                            }
                            fileOut.flush();
                            VideoFile video = new VideoFile();
                            video.setExtension("video/mp4");
                            video.setFilePath(path.getAbsoluteFile() + File.separator + name.substring(name.indexOf("uploaded")));
                            video.setName(name.substring(name.indexOf("uploaded")));
                            if (!ObjectUtils.isEmpty(videoFileService.getByName(name.substring(name.indexOf("uploaded"))))){
                                continue;
                            }
                            //TODO 从session中获取ID
                            video.setUserId(1L);
                            videoFileService.save(video);
                            fileOut.close();
                        }catch (Exception e){
                            log.error("视频解析错误");
                            success = false;
                        }finally {
                            zfIn.close();
                        }
                    }else {
                        zfIn.close();

                    }

                }
            }
            zin.closeEntry();

            //跳转处理
            for (int i = 0; i < config.size(); i++) {
                JSONObject obj = config.getJSONObject(i);
                HomePage page = new HomePage();
                //TODO 从session中获取ID
                page.setCreateUserId(1L);
                page.setHandoverId(1L);
                page.setUserId(1L);
                page.setLastUpdateTime(new Date());
                page.setComposeObj(obj.getString("composeObj"));
                page.setName(tempName == null ? obj.getString("name") : obj.getString("name") + "_" + tempName);
                page.setViewConf(obj.getString("viewConf"));
                page.setViewImage(obj.getString("viewImage"));
                page.setPaintObj(obj.getString("paintObj"));

                Long pageId = homePageService.addAndGetId(page);
                idMap.put(obj.getLong("id"), pageId);
                HomePageUserConf homePageUserConf = new HomePageUserConf();
                homePageUserConf.setPageId(pageId);
                homePageUserConf.setUserId(1L);
                homePageUserConf.setVisible(true);
                //TODO 从session中获取ID
                homePageUserConfService.add(homePageUserConf, true, new String[]{"1"});
            }
            linkImpProcess(idMap, config);
        } catch (Exception e) {
            log.error("解析错误，请检查导入文件是否为正确的模板zip");
            e.printStackTrace();
            return false;
        }
        return success;
    }

    private void linkImpProcess(Map<Long,Long> idMap, JSONArray config) {
        for (int i = 0; i < config.size(); i++) {
            JSONObject obj = config.getJSONObject(i);
            HomePage page = homePageService.getById(idMap.get(obj.getLong("id")));
            JSONObject linkConfig = obj.getJSONObject("linkConfig");
            Integer num = 0;
            String viewConf = obj.getString("viewConf");
            while (linkConfig.getLong("${" + num + "}") != null){
                String replace = "${" + num + "}";
                Long originId = linkConfig.getLong(replace);
                Long newId = idMap.get(originId);
                viewConf = viewConf.replace("${" + num + "}", newId.toString());
                num ++;
            }
            page.setViewConf(viewConf);
            homePageService.save(page);
        }
    }

    public void download(String path, HttpServletResponse response) {
        try {
            // path
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
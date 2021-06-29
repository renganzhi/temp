package com.uxsino.leaderview.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.entity.*;
import com.uxsino.leaderview.utils.ImageUtils;
import com.uxsino.leaderview.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private final int MAX_PAGE_INDEX = 20;
    //以\结尾的为目录文件
    private static Pattern imgPattern = Pattern.compile("/leaderview/home/getImg/true/(\\d*)\"");
    private static Pattern img2Pattern = Pattern.compile("/leaderview/home/getImg/true/(\\d*)");

    private static Pattern imgFalsePattern = Pattern.compile("/leaderview/home/getImg/false/(\\d*)\"");

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

    /**
     * 整合各方法完成模板的导出
     * @param pages
     * @return
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public String  makeTemplate(List<HomePage> pages){
        JSONArray json = new JSONArray();
        //需要导出的模板数
        Set<Long> ids = Sets.newConcurrentHashSet();
        pages.forEach(p -> ids.add(p.getId()));
        for (HomePage page: pages) {
            //完成模板的处理
            json.add(makeTemplate(page,ids));
        }
        //创建config.json文件
        String jsonPath = zipPath + File.separator + "templateZip" + zipNum + File.separator + "json" + File.separator + "config.json";
        //编写配置文件
        writeConfigJson(json.toJSONString(),jsonPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(zipPath + File.separator +"templateZip" + zipNum + ".zip"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipUtils.toZip(zipPath + File.separator +"templateZip" + zipNum + File.separator ,fos, true);
        return zipPath + File.separator + "templateZip" + zipNum++ + ".zip";
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public JSONObject makeTemplate(HomePage page, Set<Long> ids) {
        if (!videoSet.isEmpty()){
            videoSet = Sets.newHashSet();
        }
        //num就是当前数据表内自定义图片中id最大的那一个
        Long num = homeTemplateImgService.getMaxId();
        //对page的各字段进行图片、视频、跳转处理
        JSONObject viewConf = templateTransform(page.getViewConf(),num,ids);
        JSONObject viewImage = templateTransform(page.getViewImage(),viewConf.getLong("num"));
        JSONObject composeObj = templateTransform(page.getComposeObj(),viewImage.getLong("num"));
        JSONObject paintObj = templateTransform(page.getPaintObj(), composeObj.getLong("num"));
        //这个result是用来输出config.json的
        JSONObject result = new JSONObject();
        result.put("name",page.getName());
        result.put("id",page.getId());
        result.put("viewConf",viewConf.getString("str"));
        result.put("viewImage",viewImage.getString("str"));
        result.put("paintObj",paintObj.getString("str"));
        result.put("composeObj",composeObj.getString("str"));
        //这个linkConfig为null，不用管它
        result.put("linkConfig",viewConf.getJSONObject("linkConfig"));
        return result;
    }

    private JSONObject templateTransform(String origin, Long num, Set<Long> ids){
        //先做图片处理、视频处理
        //这个origin就是page中需要处理的字段,如viewconf、viewimage
        JSONObject result = templateTransform(origin, num);
        //再做跳转处理
        //这个跳转处理不知道是干什么的，很多viewconf里面都没有linkid
        linkProcess(result, ids);
        return result;
    }



    private JSONObject templateTransform(String origin, Long num){
        JSONObject result = new JSONObject();
        result.put("str",origin);
        imgProcess(result, num);
        //视频处理
        videoProcess(result);
        return result;
    }

    private JSONObject videoProcess(JSONObject result){
        String str = result.getString("str");
        if (Strings.isEmpty(str)){
            return result;
        }
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

    private JSONObject imgProcess(JSONObject result, Long num){
        //str是TemplateTransform中的参数origin，调用时传入的是需要处理的内容，如page.getViewConf()
        String str = result.getString("str");
        if (Strings.isEmpty(str)){
            return result;
        }

//        // 把模板中的图片也下载下来
//        List<Long> tempImgIds = Lists.newArrayList();
//        Matcher m3 = imgFalsePattern.matcher(str);
//        List<String> imgFalseList = new ArrayList<>();
//        while (m3.find()){
//            if (imgFalseList.contains(m3.group())){
//                continue;
//            }
//            imgFalseList.add(m3.group());
//            Matcher m2 = numPattern.matcher(m3.group());
//            tempImgIds.add(Long.valueOf(m2.replaceAll("").trim()));
//        }
//        for (Long id : tempImgIds) {
//            HomeTemplateImg templateImg = homeTemplateImgService.getById(id);
//            String imgPath = zipPath + "/templateZip" + zipNum + "/img/";
//            saveToFile(imgPath + "temp" + templateImg.getName() , templateImg.getFileStream());
//        }


        // 自定义图片id处理
        // 问题：系统模板中的图片需要处理吗
        List<String> imgList = new ArrayList<>();
        List<Long> ids = Lists.newArrayList();
        Map<Long,Long> map = Maps.newHashMap();
        //匹配viewconf中的url
        Matcher m = imgPattern.matcher(str);
        while (m.find()){
            if (imgList.contains(m.group())){
                continue;
            }
            //将图片url加入到imglist
            imgList.add(m.group());
            Matcher m2 = numPattern.matcher(m.group());
            //将除数字外的所有部分删除（用空字符串替换），并去掉空格，转化为long，赋给ids
            //将uploadfile的id添加进ids集合
            ids.add(Long.valueOf(m2.replaceAll("")  .trim()));
        }
        for (String string : imgList) {
            //这里需要将isCustom的值false改为true
            //这里num后面的"\"是什么意思：\" 是转义一个"

            //str = str.replace(string, "/leaderview/home/getImg/false/" + ++num +"\"");
            str = str.replace(string, "/leaderview/home/getImg/true/" + ++num +"\"");
            //这一步是做什么的？
            //将num存入map。key为uploadfile的id（原url的数字部分）,值为num
            Matcher m2 = numPattern.matcher(string);
            map.put(Long.valueOf(m2.replaceAll("").trim()), num);
        }
        //如果imgList为空，则说明上面的imgPattern没有匹配上,说明str不是viewConf，则进行下面viewImage的匹配
        if (ObjectUtils.isEmpty(imgList)){
            m = img2Pattern.matcher(str);
            while (m.find()){
                if (imgList.contains(m.group())){
                    continue;
                }
                imgList.add(m.group());
                Matcher m2 = numPattern.matcher(m.group());
                //将缩略图的id存入ids集合
                ids.add(Long.valueOf(m2.replaceAll("").trim()));
            }
            for (String string : imgList) {
                //这里需要将isCustom的值false改为true
                //str = str.replace(string, "/leaderview/home/getImg/false/" + ++num);
                str = str.replace(string, "/leaderview/home/getImg/true/" + ++num);
                Matcher m2 = numPattern.matcher(string);
                map.put(Long.valueOf(m2.replaceAll("").trim()), num);
            }
        }


        //图片移植：将图片从uploadfile表中存入要导出的文件中，并且插入homeTemplateImg表中
        List<UploadedFile> uploadedFiles = uploadedFileService.findByIds(ids);
        List<HomeTemplateImg> imgs = Lists.newArrayList();
        for (UploadedFile uploadedFile: uploadedFiles) {
            //将图片id设置为templateCustom_+num.png
            //可以将templateCustom_换成import_/,以表示是导入的图片,id也可以不用num，就用upload原来的id
            //也可以将name设置成uploadfile表中它原本的id
            //String name = "templateCustom_" + map.get(uploadedFile.getId()) + "." + uploadedFile.getExtension();
            String name = "import_" + uploadedFile.getId() + "." + uploadedFile.getExtension();
            HomeTemplateImg img = new HomeTemplateImg();
            img.setExtension(uploadedFile.getExtension());
            img.setId(Long.valueOf(map.get(uploadedFile.getId()).toString()));
            img.setName(name);
            img.setFileStream(uploadedFile.getFileStream());
            String imgPath = zipPath + "/templateZip" + zipNum + "/img/";
            saveToFile(imgPath + name, uploadedFile.getFileStream());
            imgs.add(img);
        }
        //为什么要将图片插入到模板图片表？
        homeTemplateImgService.saveAll(imgs);
        result.put("list", imgList);
        result.put("num", num);
        result.put("str", str);
        return result;
    }

    private JSONObject linkProcess(JSONObject result, Set<Long> ids) {
        //str是TemplateTransform中的参数origin，调用时传入的是需要处理的内容，如page.getViewConf()
        String str = result.getString("str");
        if (Strings.isEmpty(str)){
            return result;
        }
        Matcher m = linkPattern.matcher(str);
        Integer i = 0;
        Map<String ,String > map = Maps.newHashMap();
        String s;
        JSONObject linkConfig = new JSONObject();
        Set<Long> processedId = Sets.newHashSet();
        while (m.find()){
            Matcher numMatcher = numPattern.matcher(m.group());
            String trim = numMatcher.replaceAll("").trim();
            log.info(trim);
            if (Strings.isBlank(trim)) continue;
            Long id = Long.valueOf(trim);
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

    /**
     * 处理导入的zip文件
     * @param file zip文件
     * @param tempName 需要添加到name后面的"导入"
     * @param session
     * @return
     */
    public JsonModel processZip(String file, String tempName, HttpSession session){
        Long userId = SessionUtils.getCurrentUserIdFromSession(session);
        if (ObjectUtils.isEmpty(userId)){
            return new JsonModel(false, "用户信息获取失败");
        }
        ZipFile zf = null;
        InputStream in = null;
        JSONArray config = new JSONArray();

        //表示zip中指向大屏的位置，方便在满20时立即返回，并告知用户导入了几个
        int j;
        Map<Long ,Long > idMap = Maps.newHashMap();
        try {
            try {
                zf = new ZipFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            in = new BufferedInputStream(new FileInputStream(file));
            //创建一个新的Zip文件输入流
            ZipInputStream zin = new ZipInputStream(in);
            //zip文件类
            //templateZip3/img/templateCustom_1621323644623.png 搜索下在哪生成的
            ZipEntry ze;
            //zip文件的枚举集合
            java.util.Enumeration zipEnum = zf.entries();
            //存放uploadfile表新生成的自增id，下面替换原id的时候要用
            List<Long> ids = Lists.newArrayList();
            while (zipEnum.hasMoreElements()) {
                //返回的是zip枚举的下一个元素，那么第一个元素呢？
                ze = (ZipEntry) zipEnum.nextElement();
                //如果ze是个目录文件，则不处理
                if (ze.isDirectory()) {

                } else {
                    //创建一个读取zip文件的缓冲字符输入流
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(zf.getInputStream(ze)));
                    //为什么刚创建一个输入流又把它关闭了
                    br.close();
                    //zip文件的大小
                    long size = ze.getSize();
                    String name = ze.getName();
                    InputStream zfIn = zf.getInputStream(ze);
                    // 图片处理
                    if (name.contains("img")){
                        try {
                            byte[] b = new byte[(int)size];
                            int readCount = 0; // 已经成功读取的字节的个数
                            while (readCount < size) {
                                //将字节流读取缓冲区字节数组 b
                                readCount += zfIn.read(b, readCount, (int)size - readCount);
                            }
                            //这里要想从存到模板图标改为存到上传文件表，应该创建UploadFile实体，而不是HomeTemplateImg的实体。
                            //这两个表字段的唯一区别是upload_file表有user_id字段，因为它是上传表，需要标注属于哪个用户的
                            //这里导入的时候也应该标注属于哪个user

                            //原代码 修改2021年6月25日 10:11:36
                            /*HomeTemplateImg img = new HomeTemplateImg();
                            img.setExtension(name.substring(name.indexOf(".") + 1));
                            img.setId(Long.valueOf(name.substring(name.indexOf("_") + 1,name.indexOf("."))));
                            img.setFileStream(b);
                            img.setName(name.substring(name.indexOf("img/") + 4 ));
                            HomeTemplateImgCompressed imgCompressed = new HomeTemplateImgCompressed();
                            imgCompressed.setId(img.getId());
                            imgCompressed.setCompressedFileStream(ImageUtils.compressImage(b, img.getExtension()));
                            imgCompressed.setHomeTemplateImg(img);
                            //调用的这个方法只有这里用到了
                            //存到upload_file表则调用uploadedFileService.save(fileInfo, uploadedFileCompressed);
                            //这里需要获取插入uploadfile表中的自增id
                            //由于可能有多个图片，所以需要创建一个int数组，把它们都存进去
                            homeTemplateImgService.save(img, imgCompressed);*/

                            //从存到模板图表改为存到上传文件表，这样初始化的时候就不会被清除了
                            UploadedFile fileInfo = new UploadedFile();
                            UploadedFileCompressed uploadedFileCompressed = new UploadedFileCompressed();
                            //文件名能不能取到view_conf中的imgsrc的原图片名
                            //templateZip3/img/templateCustom_1621323644623.png
                            fileInfo.setName(name.substring(name.indexOf("img/") + 4 ));
                            String extension = name.substring(name.indexOf(".") + 1);
                            fileInfo.setExtension(extension);
                            fileInfo.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
                            fileInfo.setFileStream(b);
                            if ("jpg,jpeg,png,gif".contains(extension))
                                uploadedFileCompressed.setCompressedFileStream(ImageUtils.compressImage(b, extension));
                            uploadedFileCompressed.setUploadedFile(fileInfo);
                            uploadedFileService.save(fileInfo, uploadedFileCompressed);
                            //取出id放入数组
                            //如果id为空，则这里没取到id
                            ids.add(fileInfo.getId());

                        }catch (Exception e){
                            log.error("图片解析错误");
                            e.printStackTrace();
                            return new JsonModel(false,"图片解析错误");
                        }finally {
                            zfIn.close();
                        }
                    }
                    // 导出配置处理
                    //对导出的一些配置进行解析
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
                            e.printStackTrace();
                            return new JsonModel(false,"导出配置解析错误");
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
                            video.setUserId(userId);
                            videoFileService.save(video);
                            fileOut.close();
                        }catch (Exception e){
                            log.error("视频解析错误");
                            e.printStackTrace();
                            return new JsonModel(false,"视频解析错误");
                        }finally {
                            zfIn.close();
                        }
                    }else {
                        zfIn.close();
                        return new JsonModel(false,"文件内容有误，请检查导入文件是否为正确的模板zip");
                    }

                }
            }
            zin.closeEntry();
            Long currentUserId = SessionUtils.getCurrentUserIdFromSession(session);
            List<HomePage> allHomePages = homePageService.findByUserId(currentUserId);
            int pageCount = homePageUserConfService.getMaxMinePage(userId, false);
            //跳转处理
            for (j = 0; j<config.size() && pageCount<MAX_PAGE_INDEX;j++, pageCount++ ) {
                //这里的config是上面导出配置解析出的config,有几个config就有几个页面
                //查看导出接口，确认这里的config是什么配置
                JSONObject obj = config.getJSONObject(j);
                HomePage page = new HomePage();
                //给pageName后面添加"_导入"
                String pageName = tempName == null ? obj.getString("name") : obj.getString("name") + "_" + tempName;
                //如果有多个page的name相同，则在它们后面添加"_+当前系统时间"以做区分。
                for(HomePage temp : allHomePages){
                    if(temp.getName().equals(pageName)){
                        pageName += "_" + new Date().getTime();
                    }
                }

                //处理page中的viewconf和viewimage
                List<String> imgList = new ArrayList<>();
                List<String> imgList2 = new ArrayList<>();
                String viewConf = obj.getString("viewConf");
                String viewImage = obj.getString("viewImage");
                long num;
                int i = 0;
                //处理viewConf
                Matcher m = imgPattern.matcher(viewConf);
                while (m.find()){
                    if (imgList.contains(m.group())){
                        continue;
                    }
                    imgList.add(m.group());
                }
                for (String string : imgList) {
                    num = ids.get(i++);
                    viewConf = viewConf.replace(string, "/leaderview/home/getImg/true/" + num +"\"");
                }

                //处理viewImage
                Matcher m2 = img2Pattern.matcher(viewImage);
                while (m2.find()) {
                    if (imgList2.contains(m2.group())) {
                        continue;
                    }
                    imgList2.add(m2.group());
                }
                for (String string : imgList2) {
                    num = ids.get(i++);
                    viewImage = viewImage.replace(string, "/leaderview/home/getImg/true/" + num);
                }

                page.setCreateUserId(userId);
                page.setHandoverId(userId);
                page.setUserId(userId);
                page.setLastUpdateTime(new Date());
                page.setComposeObj(obj.getString("composeObj"));
                page.setName(pageName);
                //这里需要将获取到的viewConf中url里getimg/true/后面的id替换为上面获取到的插入上传表的自增id
                //如果viewConf中有多个图片，需要用到前面的id来匹配对应的url
                //这里可以用
                //page.setViewConf(obj.getString("viewConf"));
                log.info("viewConf = "+viewConf);
                log.info("viewImage = "+viewImage);
                page.setViewConf(viewConf);
                //这里viewImage需要修改url中的id为uploadfile中插入的
                //page.setViewImage(obj.getString("viewImage"));
                page.setViewImage(viewImage);
                page.setPaintObj(obj.getString("paintObj"));
                page.setPageIndex(pageCount);

                //在这里就添加了页面并且得到了它的自增id
                //可能这里需要打个断点
                Long pageId = homePageService.addAndGetId(page);

                //为了防止此次循环无法执行完成，导致for循环第三个表达式无法正常执行，基本操作完成后在这里完成第三个表达式。
                //log.info("导入页面的自增ID是:"+pageId);

                idMap.put(obj.getLong("id"), pageId);
                HomePageUserConf homePageUserConf = new HomePageUserConf();
                homePageUserConf.setPageId(pageId);
                homePageUserConf.setUserId(userId);
                homePageUserConf.setVisible(true);
                homePageUserConfService.add(homePageUserConf, true, null);
            }
            //linkImpProcess(idMap, config);
        } catch (Exception e) {
            log.error("解析错误，请检查导入文件是否为正确的模板zip");
            e.printStackTrace();
            return new JsonModel(false,"解析错误，请检查导入文件是否为正确的模板zip");
        }
        int remaining = config.size() - j;
        if(remaining == 0)
            return new JsonModel(true, "导入成功！");
        else
            return new JsonModel(false,"由于您只能创建最多20个大屏，当前成功导入" + j + "个，还剩" + remaining + "个未导入");
    }

    //在貌似是处理viewConfig中的linkConfig的
    private void linkImpProcess(Map<Long,Long> idMap, JSONArray config) {
        //这里原来是config.size()，因为限制20个的缘故，idMap.size()<=config.size(),否则会引起空指针异常
        for (int i = 0; i < idMap.size(); i++) {
            //obj就是传入的config信息
            JSONObject obj = config.getJSONObject(i);

            HomePage page = homePageService.getById(idMap.get(obj.getLong("id")));
            //这个linkConfig是哪写入的？ 应该是导出的时候写入的
            //从数据库中得知它为空，不用管它。
            JSONObject linkConfig = obj.getJSONObject("linkConfig");
            //输出一下它是否为空，为空则后面while的处理都没有生效。
            log.info("lonkConfig的值为:"+linkConfig);

            Integer num = 0;
            //viewconf需要处理下
            // 把里面的getimg/true/后面的num换成插入upload_file表生成的自增id
            String viewConf = obj.getString("viewConf");
            //这里的num不是导出接口中url里getimg/true/后面的ID那个num,是上面的Integer num.
            while (linkConfig!=null && linkConfig.getLong("${" + num + "}") != null){
                String replace = "${" + num + "}";
                //从replace中取出num，赋值给originId
                Long originId = linkConfig.getLong(replace);
                //从idMap中取出新id
                //这个新id应该从upload_file表中取生成的自增id.在这里能用现有的信息查询到对应的id吗？还是说需要上面插入的时候就把id一路传下来。
                Long newId = idMap.get(originId);
                //用newId替换掉num
                viewConf = viewConf.replace("${" + num + "}", newId.toString());
                num ++;
            }
            //将viewConf设置到page中
            page.setViewConf(viewConf);
            homePageService.save(page);
        }
    }

    public void download(String path, HttpServletRequest request, HttpServletResponse response) {
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
            response.setHeader("Content-Type", "multipart/form-data");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }

    public void deleteTempFile(String path){
        File folder = new File(path);
        deleteTempFile(folder);
    }

    private void deleteTempFile(File folder) {
        File[] files = folder.listFiles();
        if (files != null){
            for (File file: files) {
                if (file.isDirectory()){
                    deleteTempFile(file);
                }else {
                    file.delete();
                }
            }
        }
    }
}

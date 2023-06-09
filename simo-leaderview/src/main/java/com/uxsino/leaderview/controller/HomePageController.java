package com.uxsino.leaderview.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import com.uxsino.authority.lib.annotation.Permission;
import com.uxsino.commons.db.redis.service.SiteUserRedis;
import com.uxsino.commons.utils.SimoResourceUtil;
import com.uxsino.leaderview.model.ShareState;
import com.uxsino.leaderview.rpc.MCService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.cache.DataViewCache;
import com.uxsino.leaderview.entity.*;
import com.uxsino.leaderview.service.*;
import com.uxsino.watcher.lib.annoation.Business;
import com.uxsino.watcher.lib.enums.BusinessConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "大屏展示数据接口-HomePageController")
@RestController
@RequestMapping(value = "/home")
@Business(name = BusinessConstants.SYSTEM)
public class HomePageController {

    @Value("${web.upload.file.path}")
    private String upload_path;

    private final static Logger logger = LoggerFactory.getLogger(HomePageController.class);

    private final int MAX_PAGE_INDEX = 20;

    private final int MIN_PAGE_INDEX = 1;

    private final List<String> IMG_EXTENSION_LIST = Lists
        .newArrayList("bmp,jpg,jpeg,png,tif,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,wmf,webp"
            .split(","));

    private final Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("gif", "image/gif");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("png", "image/png");
        }
    };

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private HomeCarouselService homeCarouselService;

    @Autowired
    private HomePageUserConfService homePageUserConfService;

    @Autowired
    private UploadedFileService uploadedFileService;

    @Autowired
    private HomeTemplateService templateService;

    @Autowired
    private HomeTemplateImgService templateImgService;

    @Autowired
    private SiteUserRedis userRedis;

    @Autowired
    private MCService mcService;

    @Autowired
    private VideoFileService videoFileService;

    @ApiOperation("获取可用数据接口")
    @RequestMapping(value = "/getUrl", method = RequestMethod.GET)
    public JsonModel getUrl(@ApiParam("图表类型") @RequestParam String typeStr) {
        JSONArray allApi = DataViewCache.apiList;
        JSONArray result = new JSONArray();
        for (int i = 0; i < allApi.size(); i++) {
            JSONObject json = allApi.getJSONObject(i);
            String type = json.getString("type");
            if (Strings.isNullOrEmpty(type)) {
                return new JsonModel(true);
            }
            List<String> types = Arrays.stream(type.split(",")).collect(Collectors.toList());
            if (types.contains(typeStr)) {
                result.add(json);
            }
        }
        return new JsonModel(true, result);
    }

    @ApiOperation("增加一页")
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"W"})
    @RequestMapping(value = "/homePage/add", method = RequestMethod.POST)
    @Transactional
    public JsonModel addHomePage(HttpSession session, @ApiParam("待添加页面名称") @RequestParam String name,
                                 @ApiParam("插入位置，null或者0则默认加到最后") @RequestParam(required = false) Integer index,
                                 @ApiParam("模板ID") @RequestParam(required = false) Long templateId,
                                 @RequestParam(required = false) String[] adminId,
                                 @RequestParam boolean visible ) {
        int maxIndex = homePageUserConfService.getMaxMinePage(SessionUtils.getCurrentUserIdFromSession(session),false);
        if (maxIndex >= MAX_PAGE_INDEX) {
            return new JsonModel(false, "当前页面已达到最大数[20]");
        }
        if (index == null || index < MIN_PAGE_INDEX || index > maxIndex + 1) {
            index = maxIndex + 1;
        }
        HomePage homePage = new HomePage();
        homePage.setName(name);
        homePage.setPageIndex(index);
        homePage.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
        homePage.setRoleIds(SessionUtils.getSessionUserRoleIds(session));
        homePage.setCreateUserId(SessionUtils.getCurrentUserIdFromSession(session));
        homePage.setHandoverId(SessionUtils.getCurrentUserIdFromSession(session));
        if (null != templateId && templateId != 0) {
            HomeTemplate template = templateService.one(templateId);
            if (null != template) {
                homePage.setViewImage(template.getViewImage());
                homePage.setViewConf(template.getViewConf());
                homePage.setComposeObj(template.getComposeObj());
                homePage.setPaintObj(template.getPaintObj());
                //从当前session中获取用户id
            }
        }
        Long pageId = homePageService.addAndGetId(homePage);
        HomePageUserConf homePageUserConf = new HomePageUserConf();
        homePageUserConf.setPageId(pageId);
        homePageUserConf.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
        homePageUserConf.setVisible(visible);
        homePageUserConfService.add(homePageUserConf, visible, adminId);
        return new JsonModel(true, homePage);
    }

    @ApiOperation("修改页面名称")
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"W"})
    @RequestMapping(value = "/homePage/edit", method = RequestMethod.POST)
    public JsonModel editPageName(HttpSession session, @ApiParam("修改后的名称") @RequestParam String name,
        @ApiParam("预修改的页面ID") @RequestParam Long id) {
        if (id == null || id == 0) {
            return new JsonModel(false, "提交参数错误，ID不可为0或空");
        }
        HomePage homePage = homePageService.getById(id);
        if (homePage == null) {
            return new JsonModel(false, "预修改的页面不存在，请刷新重试！");
        }
        homePage.setName(name);
        homePageService.update(homePage);
        return new JsonModel(true);
    }

    @ApiOperation("移动")
    @RequestMapping(value = "/homePage/move/{pageIndex}/{back}/{one}", method = RequestMethod.POST)
    public JsonModel moveHomePage(HttpSession session, @ApiParam("待移动页面当前序号") @PathVariable int pageIndex,
        @ApiParam("true-向前，false-向后") @PathVariable boolean back,
        @ApiParam("true-移动一个序号，false-移到最前或最后") @PathVariable boolean one) {
        HomePage homePage = homePageService.getByIndex(pageIndex);
        if (homePage == null) {
            return new JsonModel(false, "所移动的页面不存在！");
        }
        if (one) {
            homePageService.move(homePage, back);
        } else {
            homePageService.moveAll(homePage, back);
        }
        Long userId = SessionUtils.getCurrentUserIdFromSession(session);
        HomePageUserConf homePageUserConf = homePageUserConfService.findOneByIndexAndUserId(pageIndex, userId);
        if (homePageUserConf == null) {
            return new JsonModel(false, "所移动的页面不存在！");
        }
        if (one) {
            homePageUserConfService.move(homePageUserConf, back ,userId);
        } else {
            homePageUserConfService.moveAll(homePageUserConf, back, userId);
        }
        return new JsonModel(true);
    }

    @ApiOperation("复制一页到末尾")
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"W"})
    @RequestMapping(value = "/homePage/copy", method = RequestMethod.GET)
    @Transactional
    public JsonModel copyHomePage(HttpSession session, @ApiParam("待复制页面ID") @RequestParam Long pageId,
                                  @RequestParam(required = false) String[] adminId) {
        HomePage sourcePage = homePageService.getById(pageId);
        if (ObjectUtils.isEmpty(sourcePage)) {
            return new JsonModel(false, "待复制页面不存在!");
        }
        int maxIndex = homePageUserConfService.getMaxMinePage(SessionUtils.getCurrentUserIdFromSession(session), false);
        if (maxIndex >= MAX_PAGE_INDEX) {
            return new JsonModel(false, "当前页面已达到最大数[20]");
        }
        HomePage targetPage = new HomePage();
        targetPage.setName(sourcePage.getName() + "_副本");
        targetPage.setPageIndex(maxIndex + 1);
        targetPage.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
        targetPage.setRoleIds(SessionUtils.getSessionUserRoleIds(session));
        targetPage.setViewConf(sourcePage.getViewConf());
        targetPage.setViewImage(sourcePage.getViewImage());
        targetPage.setVisible(false);
        targetPage.setPaintObj(sourcePage.getPaintObj());
        targetPage.setComposeObj(sourcePage.getComposeObj());
        targetPage.setCreateUserId(SessionUtils.getCurrentUserIdFromSession(session));
        targetPage.setHandoverId(SessionUtils.getCurrentUserIdFromSession(session));
        pageId = homePageService.addAndGetId(targetPage);
        HomePageUserConf homePageUserConf = new HomePageUserConf();
        homePageUserConf.setPageId(pageId);
        homePageUserConf.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
        homePageUserConf.setVisible(false);
        homePageUserConfService.add(homePageUserConf, false, adminId);
        return new JsonModel(true, "复制成功");
    }

    @ApiOperation("大屏配置-配置大屏展示内容-并且保存")
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"W"})
    @RequestMapping(value = "/homePage", method = RequestMethod.POST)
    public JsonModel homePage(HttpSession session, @ApiParam("封装好的页面配置对象") HomePage homePage) {
        HomePage existHomePage = homePageService.getById(homePage.getId());
        if (existHomePage == null) {
            return new JsonModel(false, "所配置的页面不存在！");
        }
        existHomePage.setViewConf(homePage.getViewConf());
        existHomePage.setViewImage(homePage.getViewImage());
        existHomePage.setPaintObj(homePage.getPaintObj());
        existHomePage.setComposeObj(homePage.getComposeObj());
        homePageService.update(existHomePage);
        return new JsonModel(true);
    }

    @ApiOperation("获取大屏页面列表")
    @RequestMapping(value = "/homePage", method = RequestMethod.GET)
    public JsonModel homePage(HttpSession session) {
        Long userId = SessionUtils.getCurrentUserIdFromSession(session);
        JSONObject result = new JSONObject();
        HomeCarousel homeCarousel = homeCarouselService.getByUserId(userId);
        if (!ObjectUtils.isEmpty(homeCarousel)) {
            // 添加轮播设置的信息
            result = JSON.parseObject(JSON.toJSONString(homeCarousel));
        }
        // 添加页面的信息
        result.put("pages", homePageService.findVisible(userId));
        // 判断是不是一个页面都没有的全新用户
        Boolean isNewUser = false;
        for (int i = 1 ; i <= homePageUserConfService.getMaxPageByUserId(userId); i++) {
            try {
                homePageUserConfService.findOneByIndexAndUserId(i,userId);
            }catch (Exception e){
                homePageUserConfService.RescueConfSort(userId);
            }
        }
        try {
            isNewUser = ObjectUtils.isEmpty(homePageUserConfService.findOneByIndexAndUserId(1, userId));
        }catch (Exception e){
            // 如果出现同一位置有两个值的问题，说明排序出了问题，调用排序修复方案
            homePageUserConfService.RescueConfSort(userId);
            isNewUser = false;
        }finally {
            result.put("isNewUser", isNewUser);
        }
        return new JsonModel(true, result);
    }

    @ApiOperation("获取当前用户可见的大屏页面列表，不含内容配置信息")
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"R"})
    @RequestMapping(value = "/homePage/noConf", method = RequestMethod.GET)
    public JsonModel homePageNoConf(HttpSession session) {
        List<HomePage> result = homePageService.getByAuthority(session);
        return new JsonModel(true, result);
    }

    @ApiOperation("根据序号查询大屏页面")
    @RequestMapping(value = "/homePage/getByIndex/{pageIndex}", method = RequestMethod.GET)
    public JsonModel homePage(HttpSession session, @ApiParam("待查询的页面序号") @PathVariable int pageIndex) {
        HomePage homePage = homePageService.getByIndex(pageIndex);
        if (homePage == null) {
            return new JsonModel(false, "未查询到对应的页面数据");
        }
        return new JsonModel(true, JSON.parseObject(JSON.toJSONString(homePage)));
    }

    @ApiOperation("根据ID查询大屏页面")
    @RequestMapping(value = "/homePage/getById/{pageId}", method = RequestMethod.GET)
    public JsonModel homePage(HttpSession session, @ApiParam("待查询的页面ID") @PathVariable Long pageId) {
        HomePage homePage = homePageService.getById(pageId);
        if (homePage == null) {
            return new JsonModel(false, "未查询到对应的页面数据");
        }
        return new JsonModel(true, JSON.parseObject(JSON.toJSONString(homePage)));
    }

    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"W"})
    @ApiOperation("根据序号删除大屏配置")
    @RequestMapping(value = "/homePage/deleteByIndex/{pageIndex}", method = RequestMethod.DELETE)
    public JsonModel deleteHomePage(HttpSession session, @ApiParam("待删除的页面序号") @PathVariable int pageIndex) {
        HomePage homePage = homePageService.getByIndex(pageIndex);
        if (homePage == null) {
            return new JsonModel(true, "预删除的页面不存在！");
        }
        try {
            homePageService.delete(homePage);
            return new JsonModel(true);
        } catch (Exception e) {
            logger.error("systemHomePageService.deteleByEmployeeIdAndPageNo执行失败：", e);
            return new JsonModel(true, "大屏配置删除失败！");
        }
    }

    @Transactional
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"W"})
    @ApiOperation("根据ID删除大屏配置")
    @RequestMapping(value = "/homePage/deleteById/{pageId}", method = RequestMethod.DELETE)
    public JsonModel deleteHomePageById(HttpSession session, @ApiParam("待删除的页面序号") @PathVariable Long pageId) {
        HomePage homePage = homePageService.getById(pageId);
        if (homePage == null) {
            return new JsonModel(true, "预删除的页面不存在！");
        }
        try {
            homePageService.delete(homePage);
            List<HomePageUserConf> pageUserConfList = homePageUserConfService.findByPageId(pageId);
            for (HomePageUserConf page : pageUserConfList) {
                Long uesrId = page.getUserId();
                int startIndex = page.getPageIndex();
                int endIndex = homePageUserConfService.getMaxPageByUserId(uesrId);
                if (startIndex < endIndex){
                    homePageUserConfService.leftPageIndex(startIndex, endIndex, uesrId);
                }
                homePageUserConfService.delete(page);
            }
            return new JsonModel(true);
        } catch (Exception e) {
            logger.error("systemHomePageService.deteleByEmployeeIdAndPageNo执行失败：", e);
            return new JsonModel(true, "大屏配置删除失败！");
        }
    }

    @ApiOperation("大屏展示轮播配置")
    @RequestMapping(value = "/carousel/conf", method = RequestMethod.POST)
    public JsonModel carouselTimeConf(HttpSession session, @ApiParam("轮播配置对象") HomeCarousel homeCarousel, String pages) {
        List<HomePageVo> pageList = JSON.parseObject(pages, new TypeReference<List<HomePageVo>>() {
        });
        Long userId = SessionUtils.getCurrentUserIdFromSession(session);
        homeCarousel.setUserId(userId);
        return homeCarouselService.save(homeCarousel, pageList, userId);
    }

    /**
     * 获取当前登录用户相同角色设置的首页轮播间隔时间
     * 
     */
    @ApiOperation("获取轮播配置")
    @RequestMapping(value = "/getCarouselTimeConf", method = RequestMethod.GET)
    public JsonModel getCarouselTimeConf(HttpSession session) {
        Long userId = SessionUtils.getCurrentUserIdFromSession(session);
        List<HomePageVo> list = homePageService.findNoViewByUserId(userId);
        HomeCarousel homeCarousel = homeCarouselService.getByUserId(userId);
        JSONObject json = new JSONObject();
        if (!ObjectUtils.isEmpty(homeCarousel)) {
            json = JSON.parseObject(JSON.toJSONString(homeCarousel));
        }
        json.put("pages", list);
        return new JsonModel(true, json);
    }

    /**
     * 保存视频文件
     * @param multipartFile
     * @param session
     * @return
     */
    @ApiOperation("保存视频文件")
    @RequestMapping(value = "/file/uploadVideoFile", method = RequestMethod.POST)
    public JsonModel uploadVideoFile(@ApiParam("上传的文件") @RequestParam("uploaded_file") MultipartFile multipartFile,
                                     HttpSession session){
        if (multipartFile.isEmpty() || StringUtils.isBlank(multipartFile.getOriginalFilename())) {
            return new JsonModel(false, "请上传文件");
        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        int begin = multipartFile.getOriginalFilename().indexOf(".");
        int length = multipartFile.getOriginalFilename().length();
        String backupFileName = multipartFile.getName() + sdf.format(d) + multipartFile.getOriginalFilename().substring(begin, length);
        File path = new File(upload_path + File.separator + "video");
        path.mkdirs();
        File file = new File(path.getAbsoluteFile() + File.separator + backupFileName);
        VideoFile videoFile = new VideoFile();
        videoFile.setName(backupFileName);
        videoFile.setExtension(multipartFile.getContentType());
        videoFile.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
        videoFile.setFilePath(path.getAbsoluteFile() + File.separator + backupFileName);
        try {
            multipartFile.transferTo(file);
            videoFileService.save(videoFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "/leaderview/home/file/getVideo/" + backupFileName;
        return new JsonModel(true, "保存成功", url);
    }

    @ApiOperation("根据视频文件名获取视频文件")
    @RequestMapping(value = "/file/getVideo/{fileName}", method = RequestMethod.GET)
    public void getVideo(@ApiParam("视频名") @PathVariable String fileName, HttpServletRequest request,
                              HttpServletResponse resp, HttpSession session){
        VideoFile videoFileInfo = videoFileService.getByName(fileName);
        BufferedInputStream bis = null;
        if (ObjectUtils.isEmpty(videoFileInfo)){
            logger.error("文件 -> {} not exists ！", fileName);
            return;
        }
        try {
            File file = new File(videoFileInfo.getFilePath());
            if (file.exists()) {
                long p = 0L;
                long toLength = 0L;
                long contentLength = 0L;
                int rangeSwitch = 0; // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
                long fileLength;
                String rangBytes = "";
                fileLength = file.length();

                // get file content
                InputStream ins = new FileInputStream(file);
                bis = new BufferedInputStream(ins);

                // tell the client to allow accept-ranges
                resp.reset();
                resp.setHeader("Accept-Ranges", "bytes");

                // client requests a file block download start byte
                String range = request.getHeader("Range");
                if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                    resp.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                    rangBytes = range.replaceAll("bytes=", "");
                    if (rangBytes.endsWith("-")) { // bytes=270000-
                        rangeSwitch = 1;
                        p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                        contentLength = fileLength - p; // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
                    } else { // bytes=270000-320000
                        rangeSwitch = 2;
                        String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                        String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
                        p = Long.parseLong(temp1);
                        toLength = Long.parseLong(temp2);
                        contentLength = toLength - p + 1; // 客户端请求的是 270000-320000 之间的字节
                    }
                } else {
                    contentLength = fileLength;
                }

                // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
                // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
                resp.setHeader("Content-Length", new Long(contentLength).toString());

                // 断点开始
                // 响应的格式是:
                // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                if (rangeSwitch == 1) {
                    String contentRange = new StringBuffer("bytes ").append(new Long(p).toString()).append("-")
                            .append(new Long(fileLength - 1).toString()).append("/")
                            .append(new Long(fileLength).toString()).toString();
                    resp.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else if (rangeSwitch == 2) {
                    String contentRange = range.replace("=", " ") + "/" + new Long(fileLength).toString();
                    resp.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else {
                    String contentRange = new StringBuffer("bytes ").append("0-").append(fileLength - 1).append("/")
                            .append(fileLength).toString();
                    resp.setHeader("Content-Range", contentRange);
                }

                resp.setContentType("application/octet-stream");
                resp.addHeader("Content-Disposition", "attachment;filename=" + fileName);

                OutputStream out = resp.getOutputStream();
                int n = 0;
                long readLength = 0;
                int bsize = 1024;
                byte[] bytes = new byte[bsize];
                if (rangeSwitch == 2) {
                    // 针对 bytes=27000-39000 的请求，从27000开始写数据
                    while (readLength <= contentLength - bsize) {
                        n = bis.read(bytes);
                        readLength += n;
                        out.write(bytes, 0, n);
                    }
                    if (readLength <= contentLength) {
                        n = bis.read(bytes, 0, (int) (contentLength - readLength));
                        out.write(bytes, 0, n);
                    }
                } else {
                    while ((n = bis.read(bytes)) != -1) {
                        out.write(bytes, 0, n);
                    }
                }
                out.flush();
                out.close();
                bis.close();
            }
        } catch (IOException ie) {
            logger.error("文件读取失败", ie);
            ie.printStackTrace();
        } catch (Exception e) {
            logger.error("流关闭异常", e);
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param file 上传的文件
     * @param session  会话
     */
    @ApiOperation("上传文件")
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public JsonModel uploadFile(@ApiParam("上传的文件") @RequestParam("uploaded_file") MultipartFile file,
        HttpSession session) {
        if (file.isEmpty() || StringUtils.isBlank(file.getOriginalFilename())) {
            return new JsonModel(false, "请上传文件");
        }
        try {
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            UploadedFile fileInfo = new UploadedFile();
            fileInfo.setName(fileName);
            fileInfo.setExtension(extension);
            fileInfo.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
            fileInfo.setFileStream(file.getBytes());
            uploadedFileService.save(fileInfo);
            JSONObject result = new JSONObject();
            result.put("id", fileInfo.getId());
            result.put("isCustom", true);
            return new JsonModel(true, "上传成功", result);
        } catch (IOException e) {
            logger.error("上传失败", e);
            return new JsonModel(false, "上传失败");
        }
    }

    /**
     * base64方式解析保存文件
     * @param base64Str 文件base64字符串
     * @param extension 文件扩展名
     * @param session  会话
     */
    @ApiOperation("base64方式解析保存文件")
    @RequestMapping(value = "/file/saveByBase64", method = RequestMethod.POST)
    public JsonModel saveByBase64(@ApiParam("文件base64字符串") String base64Str, @ApiParam("文件扩展名") String extension,
        HttpSession session) {
        if (StringUtils.isBlank(base64Str) || StringUtils.isBlank(extension)) {
            return new JsonModel(false, "请求无效");
        }
        try {
            String fileName = System.currentTimeMillis() + "." + extension;
            UploadedFile fileInfo = new UploadedFile();
            fileInfo.setName(fileName);
            fileInfo.setExtension(extension);
            fileInfo.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
            String[] url = base64Str.split(",");
            String img = url[1];
            // Base64解码
            byte[] fileStream = Base64.decodeBase64(img);
            fileInfo.setFileStream(fileStream);
            uploadedFileService.save(fileInfo);
            return new JsonModel(true, "保存成功", fileInfo.getId());
        } catch (Exception e) {
            logger.error("保存失败", e);
            return new JsonModel(false, "保存失败");
        }
    }

    /**
     * 根据文件ID获取图片
     * @param id 文件ID
     * @param resp 响应消息
     */
    @ApiOperation("根据文件ID获取图片")
    @RequestMapping(value = "/getImg/{isCustom}/{id}", method = RequestMethod.GET)
    public void getImage(@ApiParam("文件ID") @PathVariable Long id,
                         @ApiParam(value = "是否是用户自定义图片") @PathVariable Boolean isCustom,
                         HttpServletResponse resp) {

        byte[] data;
        String ext;

        if (isCustom) {
            UploadedFile f = uploadedFileService.findById(id);
            if(f == null){
                logger.error("文件 -> {} not exists ！", id);
                return;
            }
            ext = f.getExtension();
            data = f.getFileStream();
        }else {
            HomeTemplateImg f = templateImgService.getById(id);
            if(f == null){
                logger.error("文件 -> {} not exists ！", id);
                return;
            }
            ext = f.getExtension();
            data = f.getFileStream();
        }

        if (!IMG_EXTENSION_LIST.contains(ext)) {
            logger.error("文件 -> {} 非图片！", id);
            return;
        }
        try {
            resp.setContentType(CONTENT_TYPE_MAP.getOrDefault(ext, "image/png"));
            ServletOutputStream out = resp.getOutputStream();
            out.write(data);
            out.flush();
        } catch (IOException e) {
            logger.error("下载图片失败:" + id, e);
        }
    }

    /**
     * 返回当前服务端的时间
     */
    @ApiOperation("返回服务端当前时间")
    @RequestMapping(value = "/getTime" , method = RequestMethod.GET)
    public JsonModel getTime(){
        JSONArray result = new JSONArray();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        result.add(df.format(new Date()));
        return new JsonModel(true , result);
    }

    @ApiOperation("遍历主页模板")
    @GetMapping("/template/list")
    public JsonModel getTemplateList() {
        return new JsonModel(true, templateService.noConf());
    }

    @ApiOperation("将某个页面分享给其他用户或者域")
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"W"})
    @PostMapping("/share/{pageId}")
    @Transactional
    public JsonModel shareById(HttpSession session, @PathVariable Long pageId,
                               @RequestParam(value = "uids") String uids,
                               @RequestParam(value = "roles") String roles,
                               @RequestParam(value = "uidsByRoles") String uidsByRoles){
        JSONObject shareConf = new JSONObject();
        JSONArray uidArray = new JSONArray();
        JSONArray roleArray = new JSONArray();
        HomePage homePage = homePageService.getById(pageId);
        if (ObjectUtils.isEmpty(homePage)){
            return new JsonModel(false, "该页面已不存在！");
        }
        if (!StringUtils.isEmpty(uids)){
            String[] uidArr = uids.split(",");
            for (String uid : uidArr) {
                uidArray.add(Long.parseLong(uid));
                homePageUserConfService.addShare(Long.parseLong(uid), homePage);
            }
        }
        if (!StringUtils.isEmpty(roles)){
            String[] roleArr = roles.split(",");
            for (String role : roleArr){
                roleArray.add(Long.parseLong(role));
            }
        }
        if (!StringUtils.isEmpty(uidsByRoles)){
            String[] uidsByRolesArr = uidsByRoles.split(",");
            for (String uidByRole: uidsByRolesArr){
                homePageUserConfService.addShare(Long.parseLong(uidByRole), homePage);
            }
        }
        shareConf.put("roles", roleArray);
        shareConf.put("uids", uidArray);
        homePage.setShareConf(shareConf.toJSONString());
        homePageService.update(homePage);
        return new JsonModel(true);
    }

    @ApiOperation("判断当前用户是否是超级管理员")
    @GetMapping(value = "/validSuperAdmin")
    public JsonModel getSuperAdmin(HttpSession session){
        LinkedHashMap result = new LinkedHashMap();
        if (SessionUtils.isSuperAdmin(session)){
            result.put("isSuperAdmin",true);
        }else {
            result.put("isSuperAdmin",false);
        }
        return new JsonModel(true, result);
    }

    @ApiOperation("获取超级管理员下的所有用户Id")
    @GetMapping(value = "/getAdminUserId")
    public JsonModel getAdminUserId(HttpSession session){
        Long[] adminRoles = {1L};
        return mcService.findAllUserByRoleId("SESSION=" + session , adminRoles);
    }

    /**
     * 用于判断某个页面是否已被分享给当前用户，用于页面显示
     * @param homePage
     * @param userId
     * @param userRole
     * @return
     */
    private int validSharedorAuthor(HomePage homePage, String userId, String userRole) {
        JSONObject shareConf = JSONObject.parseObject(homePage.getShareConf());
        Long handoverId = homePage.getHandoverId();
        Long createUserId = homePage.getCreateUserId();
        if ((!ObjectUtils.isEmpty(handoverId) && userId.equals(handoverId.toString()))
                || (!ObjectUtils.isEmpty(createUserId) && userId.equals(createUserId.toString()))){
            return ShareState.IS_BELONGS_CURRENT.getValue();
        }
        if (ObjectUtils.isEmpty(shareConf)){
            return ShareState.INDEPENDENT.getValue();
        }
        JSONArray shareUids = shareConf.getJSONArray("uids");
        JSONArray shareRoles = shareConf.getJSONArray("roles");
        for (int i = 0; i < shareUids.size(); i++) {
            if (userId.equals(shareUids.get(i).toString())){
                return ShareState.IS_BE_SHARED.getValue();
            }
        }
        for (int i = 0; i < shareRoles.size(); i++) {
            if (userRole.equals(shareRoles.get(i).toString())){
                return ShareState.IS_BE_SHARED.getValue();
            }
        }
        return ShareState.INDEPENDENT.getValue();
    }
}

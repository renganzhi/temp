package com.uxsino.leaderview.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UploadedFileService uploadedFileService;

    @Autowired
    private HomeTemplateService templateService;

    @Autowired
    private HomeTemplateImgService templateImgService;

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
    @RequestMapping(value = "/homePage/add", method = RequestMethod.POST)
    public JsonModel addHomePage(HttpSession session, @ApiParam("待添加页面名称") @RequestParam String name,
                                 @ApiParam("插入位置，null或者0则默认加到最后") @RequestParam(required = false) Integer index,
                                 @ApiParam("模板ID") @RequestParam(required = false) Long templateId) {
        int maxIndex = homePageService.getMaxIndex();
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
        if (null != templateId && templateId != 0) {
            HomeTemplate template = templateService.one(templateId);
            if (null != template) {
                homePage.setViewImage(template.getViewImage());
                homePage.setViewConf(template.getViewConf());
            }
        }
        homePageService.add(homePage);
        return new JsonModel(true, homePage);
    }

    @ApiOperation("修改页面名称")
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
        return new JsonModel(true);
    }

    @ApiOperation("复制一页到末尾")
    @RequestMapping(value = "/homePage/copy/{pageId}", method = RequestMethod.GET)
    public JsonModel copyHomePage(HttpSession session, @ApiParam("待复制页面ID") @PathVariable Long pageId) {
        HomePage sourcePage = homePageService.getById(pageId);
        if (ObjectUtils.isEmpty(sourcePage)) {
            return new JsonModel(false, "待复制页面不存在!");
        }
        int maxIndex = homePageService.getMaxIndex();
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
        targetPage.setVisible(sourcePage.isVisible());
        homePageService.save(targetPage);
        return new JsonModel(true, "复制成功");
    }

    @ApiOperation("大屏配置-配置大屏展示内容")
    @RequestMapping(value = "/homePage", method = RequestMethod.POST)
    public JsonModel homePage(HttpSession session, @ApiParam("封装好的页面配置对象") HomePage homePage) {
        HomePage existHomePage = homePageService.getById(homePage.getId());
        if (existHomePage == null) {
            return new JsonModel(false, "所配置的页面不存在！");
        }
        existHomePage.setViewConf(homePage.getViewConf());
        existHomePage.setViewImage(homePage.getViewImage());
        homePageService.update(existHomePage);
        return new JsonModel(true);
    }

    @ApiOperation("获取大屏页面列表")
    @RequestMapping(value = "/homePage", method = RequestMethod.GET)
    public JsonModel homePage(HttpSession session) {
        JSONObject result = new JSONObject();
        HomeCarousel homeCarousel = homeCarouselService.get();
        if (!ObjectUtils.isEmpty(homeCarousel)) {
            // 添加轮播设置的信息
            result = JSON.parseObject(JSON.toJSONString(homeCarousel));
        }
        // 添加页面的信息
        result.put("pages", homePageService.findVisible());
        return new JsonModel(true, result);
    }

    @ApiOperation("获取大屏页面列表，不含配置信息")
    @RequestMapping(value = "/homePage/noConf", method = RequestMethod.GET)
    public JsonModel homePageNoConf(HttpSession session) {
        return new JsonModel(true, homePageService.findAllWithoutConf());
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

    @ApiOperation("根据ID删除大屏配置")
    @RequestMapping(value = "/homePage/deleteById/{pageId}", method = RequestMethod.DELETE)
    public JsonModel deleteHomePageById(HttpSession session, @ApiParam("待删除的页面序号") @PathVariable Long pageId) {
        HomePage homePage = homePageService.getById(pageId);
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

    @ApiOperation("大屏展示轮播配置")
    @RequestMapping(value = "/carousel/conf", method = RequestMethod.POST)
    public JsonModel carouselTimeConf(HttpSession session, @ApiParam("轮播配置对象") HomeCarousel homeCarousel, String pages) {
        List<HomePage> pageList = JSON.parseObject(pages, new TypeReference<List<HomePage>>() {
        });
        homeCarouselService.save(homeCarousel, pageList);
        return new JsonModel(true);
    }

    /**
     * 获取当前登录用户相同角色设置的首页轮播间隔时间
     * 
     */
    @ApiOperation("获取轮播配置")
    @RequestMapping(value = "/getCarouselTimeConf", method = RequestMethod.GET)
    public JsonModel getCarouselTimeConf() {
        HomeCarousel homeCarousel = homeCarouselService.get();
        List<HomePage> pageList = homePageService.findNoView();
        JSONObject json = new JSONObject();
        if (!ObjectUtils.isEmpty(homeCarousel)) {
            json = JSON.parseObject(JSON.toJSONString(homeCarousel));
        }
        json.put("pages", pageList);
        return new JsonModel(true, json);
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

    @ApiOperation("遍历主页模板")
    @GetMapping("/template/list")
    public JsonModel getTemplateList() {
        return new JsonModel(true, templateService.noConf());
    }
}

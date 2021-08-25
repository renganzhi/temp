package com.uxsino.leaderview.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.uxsino.authority.lib.annotation.Permission;
import com.uxsino.commons.db.redis.service.SiteUserRedis;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.ClassPathResourceWalker;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.cache.DataViewCache;
import com.uxsino.leaderview.entity.*;
import com.uxsino.leaderview.model.ShareState;
import com.uxsino.leaderview.rpc.MCService;
import com.uxsino.leaderview.service.*;
import com.uxsino.leaderview.service.api.ImageService;
import com.uxsino.leaderview.utils.ImageUtils;
import com.uxsino.leaderview.utils.ZipUtils;
import com.uxsino.watcher.lib.annoation.Business;
import com.uxsino.watcher.lib.enums.BusinessConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            .newArrayList("bmp,jpg,jpeg,png,tif,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,wmf,webp,jfif"
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
    private String jsonPath = this.getClass().getClassLoader().getResource("static").getFile();

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

    @Autowired
    private ImpExpService impExpService;

    @Autowired
    private ImageService imageService;

    @Value("${datasource.source:#{null}}")
    private String datasource;

    @ApiOperation("获取数据来源接口")
    @RequestMapping(value = "/getDatasource", method = RequestMethod.GET)
    public JsonModel getDatasource() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        if (!StringUtils.isNotBlank(datasource)) {
            return new JsonModel(true, map);
        }
        String[] datasources = datasource.split(",");
        for (int i = 0; i < datasources.length; i++) {
            String datasourceStr = datasources[i];
            if (!StringUtils.isNotBlank(datasourceStr)) {
                continue;
            }
            String[] parts = datasourceStr.split(":");
            if (parts.length > 2) {
                map.put(parts[0], parts[1] + ":" + parts[2]);
            } else if (parts.length == 1) {
                map.put(parts[0], "");
            }
        }
        return new JsonModel(true, map);
    }


    @ApiOperation("获取可用数据接口")
    @RequestMapping(value = "/getUrl", method = RequestMethod.GET)
    public JsonModel getUrl(@ApiParam("图表类型") @RequestParam String typeStr) {
        JSONArray allApi = DataViewCache.apiList;
        JSONArray result = new JSONArray();
        for (int i = 0; i < allApi.size(); i++) {
            JSONObject json = allApi.getJSONObject(i);
            String type = json.getString("type");
            if (Strings.isNullOrEmpty(type)) {
                continue;
            }
            List<String> types = Arrays.stream(type.split(",")).collect(Collectors.toList());
            if (types.contains(typeStr)) {
                result.add(json);
            }
        }
        return new JsonModel(true, result);
    }

    @ApiOperation("根据模板生成目标大屏")
    @PostMapping(value = "/createPageByTemplate")
    public JsonModel createPageByTemplate(@ApiParam("是否删除原有屏幕") @RequestParam(value = "deleteOrigin") Boolean deleteOrigin,
                                          @ApiParam("统一添加的名字") @RequestParam(value = "name", required = false) String name) {
        try {
            List<HomeTemplate> list = templateService.findAll();
            Map<Long, Long> map = homePageService.createAllTemplate(list, deleteOrigin, name);
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                homePageService.updateId(entry.getKey(), entry.getValue());
                homePageUserConfService.updatePageId(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, "模板生成出错");
        }
        return new JsonModel(true);
    }

    @ApiOperation("增加一页")
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"W"})
    @RequestMapping(value = "/homePage/add", method = RequestMethod.POST)
    @Transactional
    public JsonModel addHomePage(HttpSession session, @ApiParam("待添加页面名称") @RequestParam String name,
                                 @ApiParam("插入位置，null或者0则默认加到最后") @RequestParam(required = false) Integer index,
                                 @ApiParam("模板ID") @RequestParam(required = false) Long templateId,
                                 @RequestParam(required = false) String[] adminId,
                                 @RequestParam boolean visible) {
        long currentUserId = SessionUtils.getCurrentUserIdFromSession(session);
        int maxIndex = homePageUserConfService.getMaxMinePage(currentUserId, false);
        if (maxIndex >= MAX_PAGE_INDEX) {
            return new JsonModel(false, "当前页面已达到最大数[20]");
        }
        List<HomePage> allHomePages = homePageService.findByUserId(currentUserId);
        for (HomePage homePage : allHomePages) {
            if (homePage.getName().equals(name)) {
                return new JsonModel(false, "所选页面名称已存在，请更换一个名称！");
            }
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
        Long currentUserId = SessionUtils.getCurrentUserIdFromSession(session);
        List<HomePage> allHomePages = homePageService.findByUserId(currentUserId);
        for (HomePage temp : allHomePages) {
            if (temp.getId().equals(id))
                continue;
            if (temp.getName().equals(name)) {
                return new JsonModel(false, "所选页面名称已存在，请更换一个名称！");
            }
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
            homePageUserConfService.move(homePageUserConf, back, userId);
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
        //由于已经保证了每个用户页面不能超过20个，因此全部查到内存中影响不大
        Long currentUserId = SessionUtils.getCurrentUserIdFromSession(session);
        List<HomePage> homePages = homePageService.findByUserId(currentUserId);
        HomePage sourcePage = null;
        for (HomePage temp : homePages) {
            if (pageId.equals(temp.getId())) {
                sourcePage = temp;
                break;
            }
        }
        if (sourcePage == null || ObjectUtils.isEmpty(sourcePage)) {
            return new JsonModel(false, "待复制页面不存在!");
        }
        int maxIndex = homePageUserConfService.getMaxMinePage(currentUserId, false);
        if (maxIndex >= MAX_PAGE_INDEX) {
            return new JsonModel(false, "当前页面已达到最大数[20]");
        }
        HomePage targetPage = new HomePage();
        String targetName = sourcePage.getName() + "_副本";
        boolean isNameExisted = false;
        for (HomePage temp : homePages) {
            if (targetName.equals(temp.getName())) {
                isNameExisted = true;
                break;
            }
        }
        if (isNameExisted) {
            int maxReplicaIndex = 1;
            Pattern pattern = Pattern.compile("_副本\\(\\d+\\)");
            String[] targetParts = null;
            String[] tempParts = null;
            if (pattern.matcher(targetName).find()) {
                int leftBracketIndex = targetName.lastIndexOf('(');
                int rightBracketIndex = targetName.lastIndexOf(')');
                targetParts = new String[]{
                        targetName.substring(0, leftBracketIndex + 1),
                        targetName.substring(leftBracketIndex + 1, rightBracketIndex),
                        targetName.substring(rightBracketIndex)
                };
                int targetIndex = Integer.parseInt(targetParts[1]);
                if (targetIndex > maxReplicaIndex)
                    maxReplicaIndex = targetIndex;
                for (HomePage temp : homePages) {
                    String tempName = temp.getName();
                    leftBracketIndex = tempName.lastIndexOf('(');
                    if (leftBracketIndex == -1)
                        continue;
                    rightBracketIndex = tempName.lastIndexOf(')');
                    if (rightBracketIndex == -1)
                        continue;
                    tempParts = new String[]{
                            tempName.substring(0, leftBracketIndex + 1),
                            tempName.substring(leftBracketIndex + 1, rightBracketIndex),
                            tempName.substring(rightBracketIndex)
                    };
                    if (targetParts[0].equals(tempParts[0])
                            && targetParts[2].equals(tempParts[2]) && Pattern.matches("\\d+", tempParts[1])) {
                        int tempIndex = Integer.parseInt(tempParts[1]);
                        if (tempIndex > maxReplicaIndex)
                            maxReplicaIndex = tempIndex;
                    }
                }
                int currentIndex = maxReplicaIndex + 1;
                targetPage.setName(targetParts[0] + currentIndex + targetParts[2]);
            } else {
                for (HomePage temp : homePages) {
                    String tempName = temp.getName();
                    int leftBracketIndex = tempName.lastIndexOf('(');
                    if (leftBracketIndex == -1)
                        continue;
                    int rightBracketIndex = tempName.lastIndexOf(')');
                    if (rightBracketIndex == -1)
                        continue;
                    tempParts = new String[]{
                            tempName.substring(0, leftBracketIndex + 1),
                            tempName.substring(leftBracketIndex + 1, rightBracketIndex),
                            tempName.substring(rightBracketIndex)
                    };
                    if (targetName.equals(tempParts[0].substring(0, tempParts[0].length() - 1))
                            && Pattern.matches("\\d+", tempParts[1])) {
                        int tempIndex = Integer.parseInt(tempParts[1]);
                        if (tempIndex > maxReplicaIndex)
                            maxReplicaIndex = tempIndex;
                    }
                }
                int currentIndex = maxReplicaIndex + 1;
                targetPage.setName(targetName + "(" + currentIndex + ")");
            }
        } else {
            targetPage.setName(targetName);
        }
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
        existHomePage.setViewConf(homePageService.processVideoUrl(homePage.getViewConf()));
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
        result.put("pages", homePageService.findVisible(userId, session));
        // 判断是不是一个页面都没有的全新用户
        Boolean isNewUser = false;
        Integer pageCount = homePageUserConfService.getMaxPageByUserId(userId);
        for (int i = 1; i <= pageCount; i++) {
            try {
                homePageUserConfService.findOneByIndexAndUserId(i, userId);
            } catch (Exception e) {
                homePageUserConfService.RescueConfSort(userId);
            }
        }
        try {
            isNewUser = pageCount == 0;
        } catch (Exception e) {
            // 如果出现同一位置有两个值的问题，说明排序出了问题，调用排序修复方案
            homePageUserConfService.RescueConfSort(userId);
            isNewUser = false;
        } finally {
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

    @ApiOperation("获取当前用户可以导出的页面列表")
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"R"})
    @RequestMapping(value = "/homePage/export/list", method = RequestMethod.GET)
    public JsonModel exportList(HttpSession session) {
        boolean isSuperAdmin = SessionUtils.isSuperAdmin(session);
        List<HomePage> result = Lists.newArrayList();
        if (isSuperAdmin) {
            result = homePageService.getByAuthority(session);
        } else {
            Long userId = SessionUtils.getCurrentUserIdFromSession(session);
            if (ObjectUtils.isEmpty(userId)) {
                return new JsonModel(false, "获取用户信息失败", null);
            }
            result = homePageService.getByUserIdNoView(userId);
        }
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
                if (startIndex < endIndex) {
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

    @Transactional
    @Permission(value = {"VIEW01"}, text = "大屏展示", permission = {"W"})
    @ApiOperation("被分享者删除被分享的大屏")
    @RequestMapping(value = "/homePage/cancelShareById/{pageId}", method = RequestMethod.DELETE)
    public JsonModel cancelSharedHomePage(HttpSession session, @PathVariable Long pageId) {
        HomePage homePage = homePageService.getById(pageId);
        if (homePage == null) {
            return new JsonModel(true, "预取消的页面不存在");
        }
        try {
            Long userId = SessionUtils.getCurrentUserIdFromSession(session);
            JSONArray roleIdArr = SessionUtils.getSessionUserRoleIdArr(session);
            JSONObject shareConf = JSONObject.parseObject(homePage.getShareConf());
            JSONObject newshareConf = new JSONObject();
            List list = new ArrayList();
            JSONArray uids = shareConf.getJSONArray("uids");
            for (int i = 0; i < uids.size(); i++) {
                list.add(uids.get(i));
                if (uids.get(i).equals(userId.intValue())) {
                    list.remove(i);
                }
            }
            uids = new JSONArray(list);
            JSONArray roles = shareConf.getJSONArray("roles");
//            if (roles.containsAll(roleIdArr)) {
//                roles.removeAll(roleIdArr);
//            }
            newshareConf.put("uids", uids);
            newshareConf.put("roles", roles);
            homePage.setShareConf(newshareConf.toJSONString());
            homePageService.update(homePage);


            List<HomePageUserConf> homePageUserConfList = homePageUserConfService.findByPageId(pageId);
            for (HomePageUserConf page : homePageUserConfList) {
                if (page.getUserId().equals(userId) && page.isShared()) {
                    int startIndex = page.getPageIndex();
                    int endIndex = homePageUserConfService.getMaxPageByUserId(userId);
                    if (startIndex < endIndex) {
                        //这一步抛出异常
                        homePageUserConfService.leftPageIndex(startIndex, endIndex, userId);
                    }
                    homePageUserConfService.delete(page);
                }
            }
            return new JsonModel(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(true, "取消大屏分享失败！");
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
     *
     * @param multipartFile
     * @param session
     * @return
     */
    @ApiOperation("保存视频文件")
    @RequestMapping(value = "/file/uploadVideoFile", method = RequestMethod.POST)
    public JsonModel uploadVideoFile(@ApiParam("上传的文件") @RequestParam("uploaded_file") MultipartFile multipartFile,
                                     HttpSession session) {
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

    /**
     * 初始化模板视频
     */
    public void videoInit() {
        String FILEPATH = "classpath*:/img/home/templateVideo/*.*";
        try {
            new ClassPathResourceWalker(FILEPATH).forEach(file -> {
                InputStream in;
                try {
                    in = file.openStream();
                } catch (IOException e) {
                    logger.error("读取文件失败: ", e);
                    return;
                }
                String extension = Files.getFileExtension(file.getFile());
                String name = Files.getNameWithoutExtension(file.getFile());
                try {
                    // 说明：读取文件不能使用  in.read(byte[in.available()])一次性读取，读取大文件可能存在读取不完整的情况，使用缓存进行逐步读取。
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] cache = new byte[1024];
                    int size;
                    while ((size = in.read(cache)) != -1) {
                        out.write(cache, 0, size);
                    }
                    out.toByteArray();
                    File path = new File(upload_path + File.separator + "video");
                    path.mkdirs();
                    File saveFile = new File(path.getAbsoluteFile() + File.separator + name + "." + extension);
                    if (ObjectUtils.isEmpty(videoFileService.getByName(name + "." + extension))) {
                        VideoFile videoFile = new VideoFile();
                        videoFile.setName(name + "." + extension);
                        videoFile.setExtension(extension);
                        videoFile.setFilePath(path.getAbsoluteFile() + File.separator + name + "." + extension);
                        try {
                            FileOutputStream fos = new FileOutputStream(saveFile);
                            out.writeTo(fos);
                            videoFileService.save(videoFile);
                            out.close();
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    logger.error("", e);
                } finally {
                    try {
                        in.close();
                        logger.info("模板视频保存成功");
                    } catch (Exception e) {
                        logger.error("关闭流失败", e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("根据视频文件名获取视频文件")
    @RequestMapping(value = "/file/getVideo/{fileName}", method = RequestMethod.GET)
    public void getVideo(@ApiParam("视频名") @PathVariable String fileName, HttpServletRequest request,
                         HttpServletResponse resp, HttpSession session) {
        VideoFile videoFileInfo = videoFileService.getByName(fileName);
        BufferedInputStream bis = null;
        if (ObjectUtils.isEmpty(videoFileInfo)) {
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
     *
     * @param file    上传的文件
     * @param session 会话
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
            UploadedFileCompressed uploadedFileCompressed = new UploadedFileCompressed();
            fileInfo.setName(fileName);
            fileInfo.setExtension(extension);
            fileInfo.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
            fileInfo.setFileStream(file.getBytes());
            if ("jpg,jpeg,png,gif".contains(extension))
                uploadedFileCompressed.setCompressedFileStream(ImageUtils.compressImage(file.getBytes(), extension));
            uploadedFileCompressed.setUploadedFile(fileInfo);
            uploadedFileService.save(fileInfo, uploadedFileCompressed);
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
     *
     * @param base64Str 文件base64字符串
     * @param extension 文件扩展名
     * @param session   会话
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
            UploadedFileCompressed fileCompressed = new UploadedFileCompressed();
            if ("jpg,jpeg,png,gif".contains(extension))
                fileCompressed.setCompressedFileStream(ImageUtils.compressImage(fileStream, extension));
            uploadedFileService.save(fileInfo, fileCompressed);
            return new JsonModel(true, "保存成功", fileInfo.getId());
        } catch (Exception e) {
            logger.error("保存失败", e);
            return new JsonModel(false, "保存失败");
        }
    }

    /**
     * 根据文件ID获取图片
     *
     * @param id   文件ID
     * @param resp 响应消息
     */
    @ApiOperation("根据文件ID获取图片")
    @RequestMapping(value = "/getImg/{isCustom}/{id}", method = RequestMethod.GET)
    public void getImage(@ApiParam("文件ID") @PathVariable Long id,
                         @ApiParam(value = "是否是用户自定义图片") @PathVariable Boolean isCustom,
                         HttpServletResponse resp) {

        imageService.getImg(id, isCustom, false,
                (dataWrap, ext) -> {
                    byte[] data = new byte[dataWrap.length];
                    for (int i = 0; i < dataWrap.length; i++)
                        data[i] = dataWrap[i];
                    try {
                        resp.setContentType(CONTENT_TYPE_MAP.get(ext));
                        OutputStream os = resp.getOutputStream();
                        os.write(data);
                        os.flush();
                    } catch (IOException e) {
                        logger.error("下载图片失败:" + id, e);
                    }
                }
        );
    }

    /**
     * 根据文件ID获取图片（鹰眼重新获取图片需要压缩）
     *
     * @param id   文件ID
     * @param resp 响应消息
     */
    @ApiOperation("根据文件ID获取图片")
    @RequestMapping(value = "/getImg/{isCustom}/{id}/HawEye", method = RequestMethod.GET)
    public void getImageForHawEye(@ApiParam("文件ID") @PathVariable Long id,
                                  @ApiParam(value = "是否是用户自定义图片") @PathVariable Boolean isCustom,
                                  HttpServletResponse resp) {

        imageService.getImg(id, isCustom, true,
                (dataWrap, ext) -> {
                    byte[] data = new byte[dataWrap.length];
                    for (int i = 0; i < dataWrap.length; i++)
                        data[i] = dataWrap[i];
                    try {
                        resp.setContentType(CONTENT_TYPE_MAP.get(ext));
                        OutputStream os = resp.getOutputStream();
                        os.write(data);
                        os.flush();
                    } catch (IOException e) {
                        logger.error("下载图片失败:" + id, e);
                    }
                }
        );
    }

	/*
		考虑到所有老版本用户升级到有压缩图片的版本时，都会遇到补充图片压缩版本的问题，因此将手动访问该接口启动该操作
		变成了在大屏启动时自动判断该表中是否有压缩数据，没有则自动进行压缩
	 */
//	@ApiOperation("新增表储存压缩后的用户上传图片，如果原表中已经有图片了，需要遍历一遍来生成压缩图")
//	@RequestMapping(value = "/generateCompressedCustomizedImage", method = RequestMethod.GET)
//	public JsonModel generateCompressedCustomizedImage(){
//		try {
//			return new JsonModel(uploadedFileService.generateCompressedCustomImage(), "操作成功！");
//		}catch (Exception e){
//			logger.error("LEADERVIEW -> 生成自定义上传图片的压缩数据抛出异常! stackTrace如下：", e);
//			return new JsonModel(false, "操作失败！");
//		}
//	}


    /**
     * 返回当前服务端的时间
     */
    @ApiOperation("返回服务端当前时间")
    @RequestMapping(value = "/getTime", method = RequestMethod.GET)
    public JsonModel getTime() {
        JSONArray result = new JSONArray();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        result.add(df.format(new Date()));
        return new JsonModel(true, result);
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
                               @RequestParam(value = "uidsByRoles") String uidsByRoles) {
        JSONObject shareConf = new JSONObject();
        JSONArray uidArray = new JSONArray();
        JSONArray roleArray = new JSONArray();
        HomePage homePage = homePageService.getById(pageId);
        if (ObjectUtils.isEmpty(homePage)) {
            return new JsonModel(false, "该页面已不存在！");
        }
        if (!StringUtils.isEmpty(uids)) {
            String[] uidArr = uids.split(",");
            for (String uid : uidArr) {
                uidArray.add(Long.parseLong(uid));
                homePageUserConfService.addShare(Long.parseLong(uid), homePage);
            }
        }
        if (!StringUtils.isEmpty(roles)) {
            String[] roleArr = roles.split(",");
            for (String role : roleArr) {
                roleArray.add(Long.parseLong(role));
            }
        }
        if (!StringUtils.isEmpty(uidsByRoles)) {
            String[] uidsByRolesArr = uidsByRoles.split(",");
            for (String uidByRole : uidsByRolesArr) {
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
    public JsonModel getSuperAdmin(HttpSession session) {
        LinkedHashMap result = new LinkedHashMap();
        if (SessionUtils.isSuperAdmin(session)) {
            result.put("isSuperAdmin", true);
        } else {
            result.put("isSuperAdmin", false);
        }
        return new JsonModel(true, result);
    }

    @ApiOperation("获取超级管理员下的所有用户Id")
    @GetMapping(value = "/getAdminUserId")
    public JsonModel getAdminUserId(HttpSession session) {
        Long[] adminRoles = {1L};
        return mcService.findAllUserByRoleId("SESSION=" + session, adminRoles);
    }

    @ApiOperation("查询可用的用户-大屏下拉框格式")
    @GetMapping(value = "/getUsersForDropDown")
    public JsonModel getUsersForDropDown() {
        List<String> all = userRedis.getAll();
        JSONArray result = new JSONArray();
        all.forEach(str -> {
            JSONObject user = JSONObject.parseObject(str);
            if (Objects.equals("OK", user.getString("status"))) {
                JSONObject itm = new JSONObject();
                itm.put("name", user.getString("userName"));
                itm.put("value", user.getLong("id"));
                result.add(itm);
            }
        });
        return new JsonModel().success(result);
    }

    /**
     * 用于判断某个页面是否已被分享给当前用户，用于页面显示
     *
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
                || (!ObjectUtils.isEmpty(createUserId) && userId.equals(createUserId.toString()))) {
            return ShareState.IS_BELONGS_CURRENT.getValue();
        }
        if (ObjectUtils.isEmpty(shareConf)) {
            return ShareState.INDEPENDENT.getValue();
        }
        JSONArray shareUids = shareConf.getJSONArray("uids");
        JSONArray shareRoles = shareConf.getJSONArray("roles");
        for (int i = 0; i < shareUids.size(); i++) {
            if (userId.equals(shareUids.get(i).toString())) {
                return ShareState.IS_BE_SHARED.getValue();
            }
        }
        for (int i = 0; i < shareRoles.size(); i++) {
            if (userRole.equals(shareRoles.get(i).toString())) {
                return ShareState.IS_BE_SHARED.getValue();
            }
        }
        return ShareState.INDEPENDENT.getValue();
    }

    @ApiOperation("将目标大屏制作成模板数据")
    @PostMapping(value = "/makeTemplate")
    public JsonModel makeTemplate(@RequestBody Map map) {
        Long id = Long.valueOf(map.get("id").toString());
        HomePage page = homePageService.getById(id);
        if (ObjectUtils.isEmpty(page)) {
            return new JsonModel(false, "页面不存在");
        }
        String sql = homePageService.makeTemplate(page);
        return new JsonModel(true, sql);
    }

    /**
     * 将多个目标大屏制作成模板数据并且导出资源
     *
     * @param ids     目标大屏id 用","分割
     * @param tempImg 是否下载模板图片，当目标大屏服务器的模板资源与当前大屏模板资源不一致时，需要通过此项获取目标大屏完整的模板数据
     * @return
     */
    @ApiOperation("将多个目标大屏制作成模板数据并且导出资源")
    @GetMapping(value = "/makeTemplateList")
    public void makeTemplateList(@RequestParam("ids") String ids, @RequestParam(value = "tempImg", required = false) Boolean tempImg,
                                 HttpServletRequest request, HttpServletResponse response) {
        Set<HomePage> pages = Sets.newHashSet();
        for (String str : ids.split(",")) {
            Long id = Long.valueOf(str);
            HomePage page = homePageService.getById(id);
            if (ObjectUtils.isEmpty(page)) {
                logger.error("目标大屏不存在");
                return;
            }
            pages.add(page);
        }
        logger.info("导出开始");
        tempImg = ObjectUtils.isEmpty(tempImg) ? false : tempImg;
        String path = homePageService.makeTemplateList(pages, tempImg);
        logger.info("缓存目标地址： " + path);
        impExpService.download(path, request, response);
        impExpService.deleteTempFile(path);

    }


    @ApiOperation("模板导出成zip包")
    @GetMapping("/exportTemplate")
    public void exportTemplate(@RequestParam("ids") String ids, HttpServletRequest request, HttpServletResponse response) {
//		try {
        List<HomePage> pages = Lists.newArrayList();
        for (String str : ids.split(",")) {
            Long id = Long.valueOf(str);
            HomePage page = homePageService.getById(id);
//			if (ObjectUtils.isEmpty(page)){
//				return new JsonModel(false, "页面不存在");
//			}
            pages.add(page);
        }
        logger.info("导出开始");
        String path = impExpService.makeTemplate(pages);
        logger.info("缓存目标地址： " + path);
        impExpService.download(path, request, response);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
    }


    @ResponseBody
    @PostMapping("/importTemplate")
    @ApiOperation("zip模板导入")
    public JsonModel importTemplate(HttpServletRequest request,
                                    MultipartFile file,
                                    HttpServletResponse response,
                                    @RequestParam(required = false) String name,
                                    HttpSession session) {
        JsonModel result = new JsonModel();
        /*
         *创建临时文件夹
         * 解压文件
         */
        String fileName = file.getOriginalFilename();
        //upload_path = simo_file
        File filePath = new File(upload_path + "/zipTemp");
        File fileDir = new File(filePath.getAbsoluteFile() + File.separator);
        fileDir.mkdirs();
        File saveFile = new File(fileDir, fileName);//将压缩包解析到指定位置
        //导入的文件名统一添加后缀“_导入” 问题：为什么为空则name = "导入"，而不是添加"_导入"
        //因为这个name是在processzip里面添加到name后面的，那里会拼接"_"
        name = Strings.isNullOrEmpty(name) ? "导入" : name;
        try {
            //将压缩包文件传输到saveFile
            file.transferTo(saveFile);
            String newFilePath = fileDir + File.separator + fileName;
            //解压文件
            result = impExpService.processZip(newFilePath, name, session);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, "模板导入失败");
        }
        //程序结束时，删除临时文件
        ZipUtils.deleteFiles(fileDir.getPath());//删除压缩包文件夹
        return result;
    }

    @GetMapping("/deleteAllForTest")
    @ApiOperation("删除所有大屏")
    public JsonModel deleteAllForTest() {
        List<HomePage> pages = homePageService.findAll();
        for (HomePage page : pages) {
            homePageService.delete(page);
        }
        homePageUserConfService.deleteAll();
        return new JsonModel(true, "删除成功");
    }

    @ResponseBody
    @PostMapping("/uploadJson")
    @ApiOperation("geoJson文件上传")
    public JsonModel uploadJson(MultipartFile file,
                                @RequestParam(required = false) String name
                                ) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.indexOf(".") + 1);
        //存放文件的目录
        File filePath = new File(jsonPath + "/mapJson");
        File fileDir = new File(filePath.getAbsoluteFile() + File.separator);
        fileDir.mkdirs();
        name = Strings.isNullOrEmpty(name) ? fileName : name+extension;
        File saveFile = new File(fileDir, name);

        File pathName = new File("leaderview/mapJson/"+name);
        String path = pathName.toString();
        String jsonPath = path.replaceAll("\\\\", "/");
        JSONObject result = new JSONObject();
        result.put("url",jsonPath);
        try {
            file.transferTo(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, "上传失败");
        }
        return new JsonModel(true, "上传成功",result);
    }

    @RequestMapping("/getJson")
    public JsonModel getJson(String name){
        JSONObject result = new JSONObject();
        File filePath = new File("leaderview/mapJson/"+name+".json");
        //String absolutePath = filePath.getAbsolutePath();
        String path = filePath.toString();
        String jsonPath = path.replaceAll("\\\\", "/");
        result.put("文件路径",jsonPath);
        return new JsonModel(true,result);
    }
}

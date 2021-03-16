package com.uxsino.leaderview.service;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.uxsino.commons.db.redis.service.SiteUserRedis;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.entity.*;
import com.uxsino.leaderview.model.ShareState;
import com.uxsino.leaderview.utils.ZipUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxsino.leaderview.dao.IHomePageDao;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
public class HomePageService {
    private static Logger log = LoggerFactory.getLogger(HomePageService.class);

    @Autowired
    private IHomePageDao homePageDao;
    @Autowired
    private SiteUserRedis userRedis;
    @Autowired
    private ImpExpService impExpService;
    @Autowired
    private UploadedFileService uploadedFileService;
    @Autowired
    private HomeTemplateService homeTemplateService;
    @Autowired
    private HomePageUserConfService homePageUserConfService;
    @Autowired
    private HomeTemplateImgService homeTemplateImgService;

    private static Set<String> videoSet = Sets.newHashSet();
    private static Pattern videoPattern = Pattern.compile("/leaderview/home/file/getVideo/uploaded_file(\\d*)\\.mp4");
    private static Pattern ipPattern = Pattern.compile("http://(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d*)/leaderview/home/file/getVideo/uploaded_file(\\d*)\\.mp4");
    private static Pattern localPattern = Pattern.compile("http://localhost:(\\d*)/leaderview/home/file/getVideo/uploaded_file(\\d*)\\.mp4");

    private String zipPath  = this.getClass().getClassLoader().getResource("img").getFile();
    private static Integer zipNum = 1;

    private static Pattern strPattern = Pattern.compile("/leaderview/home/getImg/true/(\\d*)");
    private static Pattern numPattern = Pattern.compile("[^0-9]");
    private static Pattern imgFalsePattern = Pattern.compile("/leaderview/home/getImg/false/(\\d*)\"");


    @Transactional
    public void add(HomePage homePage) {
        homePage.setLastUpdateTime(new Date());
        homePageDao.rightPageIndex(homePage.getPageIndex(), getMaxIndex(homePage.getUserId()));
        homePageDao.save(homePage);
    }

    /**
     * 添加一个页面之后得到它的自增id
     * @param homePage
     * @return
     */
    @Transactional
    public Long addAndGetId(HomePage homePage) {
        homePage.setLastUpdateTime(new Date());
        homePageDao.rightPageIndex(homePage.getPageIndex(), getMaxIndex(homePage.getUserId()));
        HomePage result = homePageDao.save(homePage);
        return result.getId();
    }

    /**
     * 保存大屏页面配置
     *
     * @param homePage
     */
    @Transactional
    public void save(HomePage homePage) {
        homePage.setLastUpdateTime(new Date());
        homePageDao.save(homePage);
    }

    @Transactional
    public void update(HomePage homePage) {
        homePage.setLastUpdateTime(new Date());
        homePageDao.update(homePage.getId(), homePage);
    }

    @Transactional
    public void move(HomePage homePage, boolean back) {
        int index = homePage.getPageIndex();
        if (index == 0) {
            return;
        }
        if (back) {
            index = index - 1;
        } else {
            index = index + 1;
        }
        HomePage otherPage = homePageDao.getByPageIndex(index);
        if (otherPage != null) {
            otherPage.setPageIndex(homePage.getPageIndex());
            homePageDao.update(otherPage.getId(), otherPage);
        }
        homePage.setPageIndex(index);
        homePageDao.update(homePage.getId(), homePage);
    }

    @Transactional
    public void moveAll(HomePage homePage, boolean back) {
        int index = homePage.getPageIndex();
        if (index == 0) {
            return;
        }
        if (back) {
            if (index == 1) {
                return;
            }
            homePageDao.rightPageIndex(1, index - 1);
            homePage.setPageIndex(1);
        } else {
            int maxIndex = getMaxIndex(homePage.getUserId());
            if (maxIndex == index) {
                return;
            }
            homePageDao.leftPageIndex(index + 1, maxIndex);
            homePage.setPageIndex(maxIndex);
        }
        homePageDao.update(homePage.getId(), homePage);
    }

    public int getMaxIndex(Long userId) {
        Integer maxIndex = homePageDao.countByUserId(userId);
        return maxIndex == null ? 0 : maxIndex;
    }


    public HomePage getById(Long id) {
        return homePageDao.findOne(id);
    }

    public HomePage getByIndex(int pageIndex) {
        return homePageDao.getByPageIndex(pageIndex);
    }

    /**
     * 过滤掉pageIndex<=0的信息
     */
    public List<HomePage> findAll() {
        return homePageDao.findAllOrderly();
    }

    public List<HomePage> findAllWithoutConf(Long userId) {
        List<HomePage> result = Lists.newArrayList();
        String sql = "SELECT a.id, a.user_id, a.role_ids, b.page_index, a.name, b.visible, a.last_update_time, a.view_image," +
                " a.create_user_id, a.handover_id, a.share_conf " +
                " FROM simo_mc_home_page a JOIN simo_mc_home_page_user_conf b on (a.id = b.page_id)" +
                " WHERE b.user_id = ?1" +
                " ORDER BY b.page_index, b.user_id";
        List<Object[]> list = Lists.newArrayList();
        try {
            list = (List<Object[]>) homePageDao.findBySQL(sql, userId);
        }catch (Exception e) {
            log.error("", e);
        }
        for (Object[] obj : list) {
            Long id = Long.parseLong(obj[0].toString());
            Long user = Long.parseLong(obj[1].toString());
            String roleIds = (String) obj[2];
            int pageIndex = (Integer) obj[3];
            String name = (String) obj[4];
            boolean visible = (boolean) obj[5];
            Date lastUpdateTime = (Date) obj[6];
            String viewImage = (String) obj[7];
            Long createUserId = Long.parseLong(obj[8].toString());
            Long handoverId = Long.parseLong(obj[9].toString());
            String shareConf = (String) obj[10];
            result.add(new HomePage(id,user,roleIds,pageIndex,name,visible,
                    lastUpdateTime,viewImage,createUserId,handoverId,shareConf));
        }
        return result;
    }

    public List<HomePage> findAllWithoutConf(){
        return homePageDao.findAllWithoutConfOrderly();
    }

    /**
     * 获取所有页面，排除掉页面视图配置和缩略图
     */
    public List<HomePage> findNoView() {
        return homePageDao.findNoView();
    }

    public List<HomePageVo> findNoViewByUserId(Long userId){
        String sql = "SELECT a.id,a.page_id,a.page_index,a.user_id,a.visible,b.name,b.last_update_time" +
                " FROM simo_mc_home_page_user_conf as a LEFT JOIN simo_mc_home_page as b on(b.id = a.page_id)" +
                " where a.user_id = ?1 order by a.page_index";
        List<Object[]> list = Lists.newArrayList();
        try {
            list = (List<Object[]>) homePageDao.findBySQL(sql,userId);
        } catch (Exception e) {
            log.error("", e);
        }
        List<HomePageVo> result = Lists.newArrayList();
        for (Object[] obj : list) {
            Long id = Long.parseLong(obj[0].toString());
            Long pageId = Long.parseLong(obj[1].toString());
            int pageIndex = (Integer) obj[2];
            Long userid = Long.parseLong(obj[3].toString());
            Boolean visible = (Boolean) obj[4];
            String name = (String) obj[5];
            Date lastUpdateTime = (Date) obj[6];
            result.add(new HomePageVo(id,pageId,pageIndex,userid,visible,name,lastUpdateTime));
        }
        return result;
    }

    /**
     * 查询可参与轮播的主页，即visible = true
     * 排除掉viewImage字段
     */
    public List<HomePage> findVisible(Long userId) {
        String sql = "SELECT b.id,b.compose_obj,b.last_update_time,b.name,a.page_index,b.paint_obj,b.view_conf,a.visible,b.create_user_id,b.handover_id,b.share_conf" +
                " FROM simo_mc_home_page_user_conf a LEFT JOIN simo_mc_home_page b" +
                " ON(b.id = a.page_id)" +
                " where a.visible = TRUE" +
                " AND a.user_id =?1" +
                " ORDER BY a.page_index";
        List<Object[]> objList = (List<Object[]>) homePageDao.findBySQL(sql,userId);
        List<HomePage> pages = Lists.newArrayList();
        objList.forEach(obj ->{
            Long id = Long.parseLong(obj[0].toString());
            String composeObj = (String) obj[1];
            Date lastUpdateTime = (Date) obj[2];
            String name = (String) obj[3];
            int pageIndex = (Integer) obj[4];
            String paintObj = (String) obj[5];
            String viewConf = (String) obj[6];
            boolean visible = (Boolean) obj[7];
            Long createUserId = null;
            if (!ObjectUtils.isEmpty(obj[8])){
                createUserId = Long.parseLong(obj[8].toString());
            }
            Long handoverId = null;
            if (!ObjectUtils.isEmpty(obj[9])){
                handoverId = Long.parseLong(obj[9].toString());
            }
            String shareConf = (String) obj[10];
            pages.add(new HomePage(id,composeObj,lastUpdateTime,name,pageIndex,paintObj,viewConf,visible,createUserId,handoverId,shareConf));
        });
        return pages;
    }

    /**
     * 查询指定用户的首页配置信息
     *
     * @param employeeId
     * @return
     */
    public List<HomePage> findByUserId(long employeeId) {
        return homePageDao.findByUserId(employeeId);
    }

    /**
     * 查询指定角色的首页配置信息
     *
     * @param userIds
     * @return
     */
    public List<HomePage> findByRoleIds(String userIds) {
        return homePageDao.findByRoleIds(userIds);
    }

    /**
     * 查询指定用户的首页指定序号配置信息
     *
     * @param employeeId
     * @param index
     * @return
     */
    public HomePage getByUserIdAndIndex(long employeeId, int index) {
        return homePageDao.getByUserIdAndPageIndex(employeeId, index);
    }

    public List<HomePage> getByUserIdNoView(long userId){
        return homePageDao.getByUserIdNoView(userId);
    }

    /**
     * 查询指定角色的首页指定序号配置信息
     *
     * @param pageIndex
     * @return
     */
    public HomePage getByRoleIdsAndPageIndex(String roleIds, int pageIndex) {
        return homePageDao.getByRoleIdsAndPageIndex(roleIds, pageIndex);
    }

    /**
     * 先将后续page_index往前移一位再删除该主页
     * @param page 待删除主页
     */
    @Transactional
    public void delete(HomePage page) {
        homePageDao.leftPageIndex(page.getPageIndex() + 1, getMaxIndex(page.getUserId()));
        homePageDao.delete(page);
    }

    /**
     * 根据当前用户的权限与分享配置获取其所能看到的所有页面
     * @param session
     * @return
     */
    public List<HomePage> getByAuthority(HttpSession session){
        boolean isSuperAdmin = SessionUtils.isSuperAdmin(session);
        String userId = SessionUtils.getCurrentUserIdFromSession(session).toString();
        //获取当前用户所拥有的角色
        JSONArray userRole = SessionUtils.getSessionUserRoleIdArr(session);
        //JSONObject userObj = JSONObject.parseObject(userRedis.get(userId));
        //String userRole = userObj.getString("departmentId");
        List<HomePage> AllhomePage = Lists.newArrayList();
        try {
            AllhomePage = findAllWithoutConf(SessionUtils.getCurrentUserIdFromSession(session));
        }catch (Exception e){
            log.error("获取页面失败", e);
        }
        List<HomePage> result = Lists.newArrayList();
        AllhomePage.forEach(homePage -> {
            JSONObject obj = new JSONObject();
            Integer validState = validSharedorAuthor(homePage, userId, userRole);
            if (isSuperAdmin || validState.equals(ShareState.IS_BE_SHARED.getValue())
                    || validState.equals(ShareState.IS_BELONGS_CURRENT.getValue())){
                if (!ObjectUtils.isEmpty(homePage.getHandoverId())){
                    obj = JSONObject.parseObject(userRedis.get(homePage.getHandoverId().toString()));
                }else {
                    obj = JSONObject.parseObject(userRedis.get(homePage.getCreateUserId().toString()));
                }
                if (validState.equals(ShareState.IS_BELONGS_CURRENT.getValue())){
                    homePage.setBelongCurrentUser("true");
                }else {
                    homePage.setBelongCurrentUser("false");
                }
                homePage.setShareName(obj.getString("userName") + "(" + obj.getString("employeeCode")+ ")");
                result.add(homePage);
            }
        });
        return result;
    }

    /**
     * 用于判断某个页面是否已被分享给当前用户，用于页面显示
     * @param homePage
     * @param userId
     * @param userRole
     * @return
     */
    private int validSharedorAuthor(HomePage homePage, String userId, JSONArray userRole) {
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
        List<String> shareUidList =
                shareUids != null ? shareUids.stream().map(s -> s.toString()).collect(Collectors.toList()) : null;
        if (shareUidList != null && shareUidList.contains(userId)) {
            return ShareState.IS_BE_SHARED.getValue();
        }
        JSONArray shareRoles = shareConf.getJSONArray("roles");
        List<String> shareRoleList =
                shareRoles != null ? shareRoles.stream().map(sr -> sr.toString()).collect(Collectors.toList()) : null;
        if (shareRoleList != null && CollectionUtils.containsAny(userRole,shareRoleList)) {
            return ShareState.IS_BE_SHARED.getValue();
        }
        /*for (int i = 0; i < shareUids.size(); i++) {
            if (userId.equals(shareUids.get(i).toString())){
                return ShareState.IS_BE_SHARED.getValue();
            }
        }
        for (int i = 0; i < shareRoles.size(); i++) {
            if (userRole.equals(shareRoles.get(i).toString())){
                return ShareState.IS_BE_SHARED.getValue();
            }
        }*/
        return ShareState.INDEPENDENT.getValue();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public String makeTemplate(HomePage page) {
        Long num = new Date().getTime();
        JSONObject viewConf = templateImgTransform(page.getViewConf(),num, false);
        JSONObject viewImage = templateImgTransform(page.getViewImage(),viewConf.getLong("num"), false);
        JSONObject composeObj = templateImgTransform(page.getComposeObj(),viewImage.getLong("num"), false);
        JSONObject paintObj = templateImgTransform(page.getPaintObj(), composeObj.getLong("num"), false);
        HomeTemplate homeTemplate = new HomeTemplate();
        homeTemplate.setLastUpdateTime(new Date());
        homeTemplate.setPaintObj(paintObj.getString("str"));
        homeTemplate.setComposeObj(composeObj.getString("str"));
        homeTemplate.setViewImage(viewImage.getString("str"));
        homeTemplate.setViewConf(viewConf.getString("str"));
        homeTemplate.setName(page.getName());
        String name = page.getName();
        String sql = "insert into public.simo_mc_home_template (name, last_update_time, view_conf, view_image, paint_obj, compose_obj) values ('" + name
                + "', now() ,'" + viewConf.getString("str")
                + "', '" + viewImage.getString("str")
                + "', '" + paintObj.getString("str")
                + "', '" + composeObj.getString("str")
                + "');";
        System.out.println(sql);
        homeTemplateService.save(homeTemplate);
        return sql;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public String makeTemplateList(Set<HomePage> pages, Boolean tempImg) {
//        if (!videoSet.isEmpty()){
//            videoSet = Sets.newHashSet();
//        }
        StringBuilder sb = new StringBuilder();
        for (HomePage page: pages) {
            Long num = new Date().getTime();
            JSONObject viewConf = templateImgTransform(page.getViewConf(),num, tempImg);
            JSONObject viewImage = templateImgTransform(page.getViewImage(),viewConf.getLong("num"), tempImg);
            JSONObject composeObj = templateImgTransform(page.getComposeObj(),viewImage.getLong("num"), tempImg);
            JSONObject paintObj = templateImgTransform(page.getPaintObj(), composeObj.getLong("num"), tempImg);

            String name = page.getName();
            String sql = "insert into public.simo_mc_home_template (name, last_update_time, view_conf, view_image, paint_obj, compose_obj) values ('" + name
                    + "', now() ,'" + viewConf.getString("str")
                    + "', '" + viewImage.getString("str")
                    + "', '" + paintObj.getString("str")
                    + "', '" + composeObj.getString("str")
                    + "');";
            sb.append(sql);
            sb.append("\n");
        }
        String jsonPath = zipPath + File.separator + "templateCustom" + zipNum + File.separator + "json" + File.separator + "template.txt";
        impExpService.writeConfigJson(sb.toString(),jsonPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(zipPath + File.separator +"templateCustom" + zipNum + ".zip"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipUtils.toZip(zipPath + File.separator +"templateCustom"  + zipNum + File.separator ,fos, true);
        return zipPath + File.separator +"templateCustom"  + zipNum++ + ".zip";
    }

    public void outputVideo(Set<String> sets, String url) {
        for (String fileName: sets) {
            log.info(url + fileName);
        }
    }

    private JSONObject templateImgTransform(String str, Long num, Boolean tempImg){
        String imgPath = zipPath +  File.separator +"templateCustom" + zipNum + File.separator + "img" + File.separator;
        if (tempImg){
            // 把模板中的图片也下载下来
            Set<Long> tempImgIds = Sets.newHashSet();
            Matcher m3 = imgFalsePattern.matcher(str);
            List<String> imgFalseList = new ArrayList<>();
            while (m3.find()){
                if (imgFalseList.contains(m3.group())){
                    continue;
                }
                imgFalseList.add(m3.group());
                Matcher m2 = numPattern.matcher(m3.group());
                tempImgIds.add(Long.valueOf(m2.replaceAll("").trim()));
            }
            for (Long id : tempImgIds) {
                HomeTemplateImg templateImg = homeTemplateImgService.getById(id);
                saveToFile(imgPath + templateImg.getName() , templateImg.getFileStream());
            }
        }

        JSONObject result = new JSONObject();
        List<String> list = new ArrayList<>();
        List<Long> ids = Lists.newArrayList();
        Map<Long,Long> map = Maps.newHashMap();
        Matcher m = strPattern.matcher(str);
        while (m.find()){
            if (list.contains(m.group())){
                continue;
            }
            list.add(m.group());
            Matcher m2 = numPattern.matcher(m.group());
            ids.add(Long.valueOf(m2.replaceAll("").trim()));
        }
        Matcher vm = videoPattern.matcher(str);
        while (vm.find()){
            videoSet.add(vm.group());
        }
        for (String string : list) {
            str = str.replace(string, "/leaderview/home/getImg/false/" + ++num);
            Matcher m2 = numPattern.matcher(string);
            map.put(Long.valueOf(m2.replaceAll("").trim()), num);
        }
        List<UploadedFile> uploadedFiles = uploadedFileService.findByIds(ids);
        for (UploadedFile uploadedFile: uploadedFiles) {
            String name = "templateCustom_" + map.get(uploadedFile.getId()) + "." + uploadedFile.getExtension();
            saveToFile(imgPath + name , uploadedFile.getFileStream());
        }
        result.put("list", list);
        result.put("str", str);
        result.put("num", num);
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

    @Transactional
    public void updateId(Long origin, Long target){
        homePageDao.updateId(origin, target);
    }

    @Transactional
    public Map<Long,Long> createAllTemplate(List<HomeTemplate> list, Boolean deleteOrigin,String name) {
        if (deleteOrigin){
            homePageDao.deleteAll();
            homePageUserConfService.deleteAll();
        }
        Map<Long,Long> map = Maps.newLinkedHashMap();
        for (HomeTemplate template: list) {
            HomePage page = new HomePage();
            if (Strings.isNullOrEmpty(name)){
                page.setName(template.getName());
            }else {
                page.setName(name + " " + template.getName());
            }
            page.setViewConf(template.getViewConf());
            page.setViewImage(template.getViewImage());
            page.setPaintObj(template.getPaintObj());
            page.setComposeObj(template.getComposeObj());
            page.setCreateUserId(1L);
            page.setUserId(1L);
            page.setHandoverId(1L);
            Long pageId = addAndGetId(page);
            map.put(pageId,template.getId());
            HomePageUserConf homePageUserConf = new HomePageUserConf();
            homePageUserConf.setPageId(pageId);
            homePageUserConf.setUserId(1L);
            homePageUserConf.setVisible(true);
            homePageUserConfService.add(homePageUserConf, true, null);
        }
        return map;
    }

    public void saveAll(List<HomePage> homePages) {
        homePageDao.saveAll(homePages);
    }

    //处理video保存时的url前缀问题
    public String processVideoUrl(String viewConf){
        Set<String> set = Sets.newHashSet();
        Matcher vm = ipPattern.matcher(viewConf);
        while (vm.find()){
            Matcher vm2 = videoPattern.matcher(vm.group());
            set.add(vm2.replaceAll("").trim());
        }
        vm = localPattern.matcher(viewConf);
        while (vm.find()){
            Matcher vm2 = videoPattern.matcher(vm.group());
            set.add(vm2.replaceAll("").trim());
        }
        for (String s: set) {
            viewConf = viewConf.replace(s,"");
        }
        return viewConf;
    }

}

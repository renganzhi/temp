package com.uxsino.leaderview.service;

import com.uxsino.commons.utils.ClassPathResourceWalker;
import com.uxsino.leaderview.dao.IHomeTemplateDao;
import com.uxsino.leaderview.entity.HomeTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class HomeTemplateService {

	private Logger logger = LoggerFactory.getLogger(HomeTemplateService.class);

	@Autowired
	private IHomeTemplateDao templateDao;

	@Autowired
	JdbcTemplate template;

	@Autowired
	private HomeTemplateImgService templateImgService;

	/**
	 * 初始化文件名称
	 */
	private static final String FILEPATH = "classpath*:sql/init_home_template.sql";

	/**
	 * 初始化时插入sql
	 */
	@Transactional
	public void init() {
		// 初始化模板的图片信息
		templateImgService.init();
		// 查看表是否存在数据
		long count = templateDao.count();
		try {
			if (count > 0) {
				delAll();
			}
			new ClassPathResourceWalker(FILEPATH).forEach(file -> {
				InputStream in;
				try {
					in = file.openStream();
				} catch (IOException e) {
					logger.error("Logo Icon file failed: ", e);
					return;
				}
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] cache = new byte[1024];
					int size = -1;
					while ((size = in.read(cache)) != -1) {
						out.write(cache, 0, size);
					}
					String sql = out.toString("UTF-8");
					template.execute(sql);
				} catch (IOException e) {
					logger.error("", e);
				} finally {
					try {
						in.close();
					} catch (Exception e) {
						logger.error("关闭流失败", e);
					}
				}
			});
		} catch (FileNotFoundException e) {
			logger.error("未找到主页模板的文件[init_home_template.sql]", e);
		} catch (IOException e) {
			logger.error("未找到主页模板文件:[init_home_template.sql]的路径", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void reFresh() {
		this.delAll();
		this.init();
	}

	@Transactional
	public void delAll() {
		templateDao.deleteAll();
	}

	/**
	 * 遍历模板表，排除掉viewConf字段
	 */
	public List<HomeTemplate> noConf() {
		return templateDao.noConf();
	}

	public HomeTemplate one(Long id) {
		return templateDao.findOne(id);
	}
}

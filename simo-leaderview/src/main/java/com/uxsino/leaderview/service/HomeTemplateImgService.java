package com.uxsino.leaderview.service;

import com.google.common.io.Files;
import com.google.common.primitives.Longs;
import com.uxsino.commons.utils.ClassPathResourceWalker;
import com.uxsino.leaderview.dao.IHomeTemplateImgCompressedDao;
import com.uxsino.leaderview.dao.IHomeTemplateImgDao;
import com.uxsino.leaderview.dao.IUploadedFileCompressedDao;
import com.uxsino.leaderview.entity.HomeTemplateImg;

import com.uxsino.leaderview.entity.HomeTemplateImgCompressed;
import com.uxsino.leaderview.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * 模板图片的操作方法
 */
@Service
public class HomeTemplateImgService {

    @Autowired
    private IHomeTemplateImgDao imgDao;

    @Autowired
    private IHomeTemplateImgCompressedDao imgCompressedDao;

    @Autowired
    private IUploadedFileCompressedDao fileCompressedDao;

    @Autowired
    private UploadedFileService uploadedFileService;

    private Logger logger = LoggerFactory.getLogger(HomeTemplateImgService.class);

    private static final String ORIGINALFILEPATH = "classpath*:/img/home/template1/*.*";
    private static final String COMPRESSEDFILEPATH = "classpath*:/img/home/compressed/*.*";

    private static final int ORIGINAL_IMAGE = 1;
    private static final int COMPRESSED_IMAGE = 2;

    /**
     * 初始化模板的图片
     */
    public void init() {
        // 如果表中存在数据则不进行插入数据的操作
        long count = imgDao.count();
        if (count > 0) {
            delAll();
        }
        logger.info("LEADERVIEW -> Start reading original template image resources...");
        this.readAndInsert(ORIGINALFILEPATH, ORIGINAL_IMAGE);
        logger.info("LEADERVIEW -> Start reading compressed template image resources...");
        this.readAndInsert(COMPRESSEDFILEPATH, COMPRESSED_IMAGE);

        //对自定义图片是否已经压缩进行判断，如果simo_uploaded_file_compressed表中没有数据，则证明当前用户的大屏是从
        //老版本升级上来，有原图没有压缩图，需要进行一遍遍历压缩。
        if(fileCompressedDao.count() == 0){
            logger.info("LEADERVIEW -> 检测到自定义图片没有压缩版本，如果有原图将会进行压缩操作");
            // 用户自定义图片压缩改为异步执行，当压缩未完成前访问大屏时，对应图片将返回原图
            new Thread(() -> {
                boolean isSuccess = uploadedFileService.generateCompressedCustomImage();
                if(isSuccess)
                    logger.info("LEADERVIEW -> 恢复自定义图片压缩版本成功！");
                else
                    logger.warn("LEADERVIEW -> 压缩自定义图片失败，向前查看异常详细");
            }).start();
        }
    }

    @Transactional
    public void readAndInsert(String path, int type) {
        try {
            new ClassPathResourceWalker(path).forEach(file -> {
                InputStream in;
                try {
                    in = file.openStream();
                } catch (IOException e) {
                    logger.error("读取文件失败: ", e);
                    return;
                }
                String extension = Files.getFileExtension(file.getFile());
                String name = Files.getNameWithoutExtension(file.getFile());
                // 图片id取决于图片名称最后一个"_"后的数字
                Long id = Longs.tryParse(name.substring(name.lastIndexOf("_") + 1));
                if (id == null) {
                    return;
                }
                try {
                    // 说明：读取文件不能使用  in.read(byte[in.available()])一次性读取，读取大文件可能存在读取不完整的情况，使用缓存进行逐步读取。
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] cache = new byte[1024];
                    int size;
                    while ((size = in.read(cache)) != -1) {
                        out.write(cache, 0, size);
                    }
                    // 如果以后图片还会进行其他特殊处理，可以继续追加其他图片类型的读取
                    if (type == HomeTemplateImgService.ORIGINAL_IMAGE) {
                        HomeTemplateImg img = new HomeTemplateImg();
                        img.setId(id);
                        img.setExtension(extension);
                        img.setFileStream(out.toByteArray());
                        img.setName(name + "." + extension);
                        imgDao.save(img);
                    } else if (type == HomeTemplateImgService.COMPRESSED_IMAGE) {
                        HomeTemplateImgCompressed compressedImg = new HomeTemplateImgCompressed();
                        compressedImg.setId(id);
                        compressedImg.setCompressedFileStream(ImageUtils.compressImage(out.toByteArray(), extension));
                        compressedImg.setHomeTemplateImg(imgDao.findOne(id));
                        imgCompressedDao.save(compressedImg);
                    }

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
        } catch (IOException e) {
            logger.error("初始化主页大屏模板的图片失败：", e);
        }
    }

    /**
     * 清空所有表并重新初始化
     */
    @Transactional
    public void reFresh() {
        this.delAll();
        this.init();
    }

    @Transactional
    public void delAll() {
        imgCompressedDao.deleteAll();
        imgDao.deleteAll();
    }

    /**
     * 保存单个的图片文件
     */
    @Transactional
    public void save(HomeTemplateImg img, HomeTemplateImgCompressed compressedImg) {
        imgDao.save(img);
        imgCompressedDao.save(compressedImg);
    }


    public HomeTemplateImgCompressed getCompressedImgByOriginId(Long id) {
        return imgCompressedDao.findByOriginImgId(id);
    }

    public HomeTemplateImg getById(Long id){
        return imgDao.findOne(id);
    }

    public Long getMaxId() {
        return imgDao.getMaxId();
    }

    @Transactional
    public void saveAll(List<HomeTemplateImg> imgs){
        imgDao.saveAll(imgs);
    }

    /**
     * 用于完成线下压缩模板图片，在添加资源时在本地使用该类，平时打包注释掉
     * @param args
     * @throws Exception
     */
    /*
    public static void main(String[] args) throws Exception {
        HomeTemplateImgService service = new HomeTemplateImgService();
        URL url = service.getClass().getClassLoader().getResource("img/home/template1");
        String urlStr = url.toString();
        String sourceStr = urlStr;
        sourceStr = sourceStr.substring(6);
        File directory = new File(sourceStr);
        String[] filenames = null;
        if(directory.isDirectory()) {
            filenames = directory.list();
        }
        if(filenames != null) {
            for(int i=0; i<6; i++) {
                urlStr = urlStr.substring(0, urlStr.lastIndexOf('/'));
            }
            urlStr = urlStr.substring(6);
            urlStr += "/src/main/resources/img/home/compressed";
            File file = new File(urlStr);
            if(!file.exists())
                file.mkdir();
            service.logger.info("LEADERVIEW -> Start compressing template images, amount to " + filenames.length);
            int count = 1;
            for(String filename: filenames) {
                InputStream is = service.getClass().getClassLoader().getResourceAsStream("img/home/template1/" + filename);
                InputStreamReader isr = new InputStreamReader(is);

                String[] nameParts = filename.split("_");
                String newFilename = "/" + nameParts[0] + "_compressed_" + nameParts[1];
                FileOutputStream fos = new FileOutputStream(urlStr + newFilename);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] temp = new byte[1024];
                int len = 0;
                while((len = is.read(temp, 0, 1024)) != -1) {
                    baos.write(temp, 0, len);
                }
                fos.write(ImageUtils.compressImage(baos.toByteArray(), filename.substring(filename.lastIndexOf('.')+1)));
                service.logger.info("LEADERVIEW -> Compressing finished images amount to " + (count++));
            }
        }
    }
    */
}

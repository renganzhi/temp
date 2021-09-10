package com.uxsino.leaderview.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩工具类
 */
@Slf4j
public class ZipUtil {
    private static final int BUFFER_SIZE = 2 * 1024;
    /**
     * 是否保留原来的目录结构
     * true:  保留目录结构;
     * false: 所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    private static final boolean KeepDirStructure = true;

//    public static void main(String[] args) {
//        try {
//            toZip("E:/app1", "E:/app.zip",true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 压缩成ZIP
     * @param srcDir         压缩 文件/文件夹 路径
     * @param outPathFile    压缩 文件/文件夹 输出路径+文件名 D:/xx.zip
     * @param isDelSrcFile   是否删除原文件: 压缩前文件
     */
    public static void toZip(String srcDir, String outPathFile,boolean isDelSrcFile) throws Exception {
        long start = System.currentTimeMillis();
        FileOutputStream out = null;
        ZipOutputStream zos = null;
        try {
            out = new FileOutputStream(new File(outPathFile));
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            if(!sourceFile.exists()){
                throw new Exception("需压缩文件或者文件夹不存在");
            }
            compress(sourceFile, zos, sourceFile.getName());
            if(isDelSrcFile){
                delDir(srcDir);
            }
            log.info("原文件:{}. 压缩到:{}完成. 是否删除原文件:{}. 耗时:{}ms. ",srcDir,outPathFile,isDelSrcFile,System.currentTimeMillis()-start);
        } catch (Exception e) {
            log.error("zip error from ZipUtils: {}. ",e.getMessage());
            throw new Exception("zip error from ZipUtils");
        } finally {
            try {
                if (zos != null) {zos.close();}
                if (out != null) {out.close();}
            } catch (Exception e) {}
        }
    }

    /**
     * 递归压缩方法
     * @param sourceFile 源文件
     * @param zos zip输出流
     * @param name 压缩后的名称
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name)
            throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName());
                    } else {
                        compress(file, zos, file.getName());
                    }
                }
            }
        }
    }

    /**
     *
     * 解压文件到指定目录
     * 输出解压出的所有文件名
     * @param zipPath 压缩包
     * @param destDir 解压目的路径
     * @param isResource 压缩包是否为resource
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "resource" })
    public  static List<String> unZipFiles(String zipPath, String destDir, boolean isResource) throws IOException {
        log.info("文件:{}. 解压路径:{}. 解压开始.",zipPath,destDir);
        long start = System.currentTimeMillis();
        List<String> fileNames = new ArrayList<>();
        try{
            File zipFile = null;
            if (isResource){
                // ClassPathResource类的构造方法接收路径名称，自动去classpath路径下找文件
                ClassPathResource classPathResource = new ClassPathResource("default_models.zip");
                String tempPath = destDir + "default_models_temp.zip";
                File tempFile = new File(tempPath);
                OutputStream out = new FileOutputStream(tempPath);
                IOUtils.copy(classPathResource.getInputStream(),out);
                zipPath = tempPath;//"resources/default_models.zip";
                // 获得File对象，当然也可以获取输入流对象
                //zipFile = classPathResource.getFile();//打包后无法获取resource目录下文件
            }
            if (StringUtils.isEmpty(zipPath)){
                log.warn("文件解压路径为空！");
                return null;
            }
            zipFile = new File(zipPath);
            System.err.println(zipFile.getName());
            if(zipFile == null || !zipFile.exists()){
                throw new IOException("需解压文件不存在.");
            }
            File pathFile = new File(destDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }

            fileNames = CompressUtils.unZip(zipFile,destDir);
            log.info("文件:{}. 解压路径:{}. 解压完成. 耗时:{}ms. ",zipPath,destDir,System.currentTimeMillis()-start);
        }catch(Exception e){
            log.info("文件:{}. 解压路径:{}. 解压异常:{}. 耗时:{}ms. ",zipPath,destDir,e,System.currentTimeMillis()-start);
            throw new IOException(e);
        }
        return fileNames;
    }

    // 删除文件或文件夹以及文件夹下所有文件
    public static void delDir(String dirPath) throws IOException {
        log.info("删除文件开始:{}.",dirPath);
        long start = System.currentTimeMillis();
        try{
            File dirFile = new File(dirPath);

            if (!dirFile.exists()) {
                return;
            }
            if (dirFile.isFile()) {
                dirFile.delete();
                log.info("删除文件:{}. 耗时:{}ms. ",dirPath,System.currentTimeMillis()-start);
                return;
            }
            File[] files = dirFile.listFiles();
            if(files==null){
                return;
            }
            for (int i = 0; i < files.length; i++) {
                delDir(files[i].toString());
            }
            dirFile.delete();
            log.info("删除文件:{}. 耗时:{}ms. ",dirPath,System.currentTimeMillis()-start);
        }catch(Exception e){
            log.info("删除文件:{}. 异常:{}. 耗时:{}ms. ",dirPath,e,System.currentTimeMillis()-start);
            throw new IOException("删除文件异常.");
        }
    }


//    public static void main(String... args) throws IOException {
//
//        String destPath = "E:\\UXSINO\\projects\\SIMO\\simo-public\\3dsroom_file\\models";
//        String srcPath = "E:\\UXSINO\\projects\\SIMO\\simo-public\\3dsroom_file\\models\\default_models_temp.zip";
//        unZipFiles(srcPath,destPath,false);
//    }
}

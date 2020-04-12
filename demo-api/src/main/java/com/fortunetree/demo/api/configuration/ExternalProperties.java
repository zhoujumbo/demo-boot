package com.fortunetree.demo.api.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
public class ExternalProperties implements EnvironmentPostProcessor {
    private Logger log = LoggerFactory.getLogger(getClass());
//    private static final String  LOCATION = "blindboxback.properties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("系统参数>>>"+System.getProperty("MY-HOME-CONFIG"));
        // 获取指定目录下所有配置文件
        String path = System.getProperty("MY-HOME-CONFIG");
        if(path==null || com.fortunetree.demo.core.common.file.FileUtil.isNotExistDir(path)){
            log.warn("{MY-HOME-CONFIG} is null");
            return;
        }
        List<String> files = new ArrayList<String>();
        getAllFileName(path,files);
        if(files.size()>0){
            for(String fileName : files){
                File file = new File(path, fileName);
                /**添加高优先级外部配置**/
                if (file.exists()) {
                    MutablePropertySources propertySources = environment.getPropertySources();
                    log.info("Loading local settings from {}" , file.getAbsolutePath());
                    Properties properties = loadProperties(file);
                    propertySources.addFirst(new PropertiesPropertySource("Config", properties));
                }
            }
        }
    }

    private Properties loadProperties(File f) {
        FileSystemResource resource = new FileSystemResource(f);
        try {
            return PropertiesLoaderUtils.loadProperties(resource);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to load local settings from " + f.getAbsolutePath(), ex);
        }
    }


    /**
     * 获取某个文件夹下的所有文件
     * @param fileNameList 存放文件名称的list
     * @param path 文件夹的路径
     * @return
     */
    private List<String> getAllFileName(String path,List<String> fileNameList) {
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String filename = tempList[i].getName();
                if(filename ==null || filename.equals("") || filename.lastIndexOf(".")<=0){
                    continue;
                }
                String suffix = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
                log.info("外部配置文件名称：" + filename);
                if(suffix.equals("properties")){
                    log.info(filename+"已加入");
                    fileNameList.add(filename);
                }else{
                    continue;
                }
            }
            if (tempList[i].isDirectory()) {
                log.info("外部配置子文件夹：" + tempList[i]);
                getAllFileName(tempList[i].getAbsolutePath(),fileNameList);
            }
        }
        return fileNameList;
    }
}


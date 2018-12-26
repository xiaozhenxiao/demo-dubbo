package com.controller;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * wangzhen23
 * 2018/11/26.
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(Map map) {

        map.put("name", "小明");
        map.put("index", "1");
        return "index";
    }


    @RequestMapping("/download")
    public ResponseEntity<byte[]> export(String fileName) throws IOException, ZipException {
        File fileToArchive = new File("E:/myworkspaces/idea-dubbo/wz-java/src/main/resources/mutil_output.xlsx");
        ZipFile zipFile = new ZipFile("D:/order.zip");
        ArrayList<File> files = new ArrayList<>();
        files.add(fileToArchive);
        //设置压缩文件参数
        ZipParameters parameters = new ZipParameters();
        //设置压缩方法
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        //设置压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        //设置压缩文件加密
        parameters.setEncryptFiles(true);
        //设置加密方法
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        //设置aes加密强度
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        //设置密码
        parameters.setPassword("123456");

        zipFile.addFiles(files, parameters);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName+".zip");


        return new ResponseEntity<>(FileUtils.readFileToByteArray(zipFile.getFile()), headers, HttpStatus.CREATED);
    }
}

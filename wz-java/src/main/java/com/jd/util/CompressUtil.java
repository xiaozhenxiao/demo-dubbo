package com.jd.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.CRCUtil;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * wangzhen23
 * 2018/11/23.
 */
public class CompressUtil {

    public static void main(String[] args) throws IOException, ZipException {
        File fileToArchive = new File("/lottery_output.xlsx");
        FileInputStream inputStream = new FileInputStream("E:/lottery_output.xlsx");
        File outZip = new File("zipExcel.zip");
        /** commons-compress **/
        /*final OutputStream out = Files.newOutputStream(outZip.toPath());
        ArchiveOutputStream o = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);

        ArchiveEntry entry = o.createArchiveEntry(fileToArchive, fileToArchive.getName());
        // potentially add more flags to entry
        o.putArchiveEntry(entry);

        try (InputStream i = Files.newInputStream(fileToArchive.toPath())) {
            IOUtils.copy(i, o);
        }

        o.closeArchiveEntry();
        o.finish();*/

        byte[] zipByte = ZipOutput.getEncryptZipByte(fileToArchive, "123456");
        FileUtils.writeByteFile(zipByte, outZip);

        /*ZipFile zipFile = new ZipFile("D:/test.zip");
        ArrayList<File> files = new ArrayList<>();
        files.add(fileToArchive);
        //设置压缩文件参数
        ZipParameters parameters = new ZipParameters();
        parameters.setSourceExternalStream(true);

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
        parameters.setSourceFileCRC((int)CRCUtil.computeFileCRC("E:/lottery_output.xlsx"));
        //设置密码
        parameters.setPassword("123456");

        //添加文件到压缩文件
//        zipFile.addFiles(files, parameters);

        parameters.setFileNameInZip("yyyyyyyyyyyy.xlsx");
        zipFile.addStream(inputStream, parameters);

//        char[] password = {'1', '2', '3', '4', '5', '6'};
//        FileHeader fileHeader = zipFile.getFileHeader("yyyyyyyyyyyy.xlsx");
//        fileHeader.setPassword(password);
        File file = zipFile.getFile();

        System.out.println("file:" + file);*/

//        FileUtils.copyInputStreamToFile(resultInputStream, new File("E:/xxxxxxxxx.zip"));
    }
}

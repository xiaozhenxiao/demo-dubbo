package com.jd.util;


import com.jd.util.encrypt.EncryptZipEntry;
import com.jd.util.encrypt.EncryptZipOutput;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public final class ZipOutput {
    public static byte[] getEncryptZipByte(File srcfile, String password) {
        ByteArrayOutputStream tempOStream = new ByteArrayOutputStream(1024);
        byte[] tempBytes = (byte[]) null;
        byte[] buf = new byte[1024];
        try {
            EncryptZipOutput out = new EncryptZipOutput(tempOStream, password);

            FileInputStream in = new FileInputStream(srcfile);
            out.putNextEntry(new EncryptZipEntry(srcfile.getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
            tempOStream.flush();
            out.close();
            tempBytes = tempOStream.toByteArray();
            tempOStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempBytes;
    }
}

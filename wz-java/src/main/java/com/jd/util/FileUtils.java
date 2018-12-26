package com.jd.util;


/*     */

import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import javax.activation.DataHandler;

/*     */
/*     */ public class FileUtils
        /*     */ {
    /*     */
    public static File[] getFileList(String fileDir)
    /*     */ {
        /*  32 */
        File dir = new File(fileDir);
        /*  33 */
        for (String children : dir.list()) {
            /*  34 */
            System.out.println(children);
            /*     */
        }
        /*  36 */
        return dir.listFiles();
        /*     */
    }

    /*     */
    /*     */
    public static byte[] readFileByte(File file)
    /*     */ {
        /*  47 */
        FileInputStream fis = null;
        /*  48 */
        FileChannel fc = null;
        /*  49 */
        byte[] data = (byte[]) null;
        /*     */
        try {
            /*  51 */
            fis = new FileInputStream(file);
            /*  52 */
            fc = fis.getChannel();
            /*  53 */
            data = new byte[(int) fc.size()];
            /*  54 */
            fc.read(ByteBuffer.wrap(data));
            /*     */
        }
        /*     */ catch (FileNotFoundException e) {
            /*  57 */
            e.printStackTrace();
            /*     */
        } catch (IOException e) {
            /*  59 */
            e.printStackTrace();
            /*     */
        } finally {
            /*  61 */
            if (fc != null) {
                /*     */
                try {
                    /*  63 */
                    fc.close();
                    /*     */
                }
                /*     */ catch (IOException e) {
                    /*  66 */
                    e.printStackTrace();
                    /*     */
                }
                /*     */
            }
            /*  69 */
            if (fis != null) {
                /*     */
                try {
                    /*  71 */
                    fis.close();
                    /*     */
                } catch (IOException e) {
                    /*  73 */
                    e.printStackTrace();
                    /*     */
                }
                /*     */
            }
            /*     */
        }
        /*     */
        /*  78 */
        return data;
        /*     */
    }

    /*     */
    /*     */
    public static byte[] readFileByte(String filename)
    /*     */     throws IOException
    /*     */ {
        /*  91 */
        if ((filename == null) || (filename.equals(""))) {
            /*  92 */
            throw new NullPointerException("无效的文件路径");
            /*     */
        }
        /*  94 */
        File file = new File(filename);
        /*  95 */
        long len = file.length();
        /*  96 */
        byte[] bytes = new byte[(int) len];
        /*     */
        /*  98 */
        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                /*  99 */       new FileInputStream(file));
        /* 100 */
        int r = bufferedInputStream.read(bytes);
        /* 101 */
        if (r != len)
            /* 102 */ throw new IOException("读取文件不正确");
        /* 103 */
        bufferedInputStream.close();
        /*     */
        /* 105 */
        return bytes;
        /*     */
    }

    /*     */
    /*     */
    public static Boolean writeByteFile(byte[] bytes, File file)
    /*     */ {
        /* 118 */
        FileOutputStream fos = null;
        /*     */
        try {
            /* 120 */
            fos = new FileOutputStream(file);
            /* 121 */
            fos.write(bytes);
            /*     */
        } catch (FileNotFoundException e) {
            /* 123 */
            e.printStackTrace();
            /*     */
        }
        /*     */ catch (IOException e) {
            /* 126 */
            e.printStackTrace();
            /*     */
        }
        /*     */ finally {
            /* 129 */
            if (fos != null) {
                /*     */
                try {
                    /* 131 */
                    fos.close();
                    /*     */
                } catch (IOException e) {
                    /* 133 */
                    e.printStackTrace();
                    /*     */
                }
                /*     */
            }
            /*     */
        }
        /* 137 */
        return false;
        /*     */
    }

    /*     */
    /*     */
    public static void moveFile(String fromDir, String toDir, String errDir)
    /*     */ {
        /*     */
        try
            /*     */ {
            /* 154 */
            File destDir = new File(toDir);
            /* 155 */
            if (!(destDir.exists())) {
                /* 156 */
                destDir.mkdirs();
                /*     */
            }
            /*     */
            /* 159 */
            for (File file : new File(fromDir).listFiles())
                /* 160 */
                if (file.isDirectory()) {
                    /* 161 */
                    moveFile(file.getAbsolutePath(), toDir + File.separator +
                            /* 162 */             file.getName(), errDir);
                    /* 163 */
                    file.delete();
                    /* 164 */
                    System.out.println("文件夹" + file.getName() + "删除成功");
                    /*     */
                } else {
                    /* 166 */
                    File moveFile = new File(toDir + File.separator +
                            /* 167 */             file.getName());
                    /* 168 */
                    if (moveFile.exists()) {
                        /* 169 */
                        moveFileToErrDir(moveFile, errDir);
                        /*     */
                    }
                    /* 171 */
                    file.renameTo(moveFile);
                    /* 172 */
                    System.out.println("文件" + moveFile.getName() + "转移到错误目录成功");
                    /*     */
                }
            /*     */
        }
        /*     */ catch (Exception e) {
            /* 176 */
            e.printStackTrace();
            /*     */
        }
        /*     */
    }

    /*     */
    /*     */
    private static void moveFileToErrDir(File moveFile, String errDir)
    /*     */ {
        /* 183 */
        int i = 0;
        /* 184 */
        String errFile = errDir + File.separator + "rnError" +
                /* 185 */       moveFile.getName();
        /* 186 */
        while (new File(errFile).exists()) {
            /* 187 */
            ++i;
            /* 188 */
            errFile = errDir + File.separator + i + "rnError" +
                    /* 189 */         moveFile.getName();
            /*     */
        }
        /* 191 */
        moveFile.renameTo(new File(errFile));
        /*     */
    }

    /*     */
    /*     */
    public static byte[] getFileByte(InputStream in)
    /*     */ {
        /* 200 */
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        /*     */
        try {
            /* 202 */
            copy(in, out);
            /*     */
        } catch (IOException e) {
            /* 204 */
            e.printStackTrace();
            /*     */
        }
        /* 206 */
        return out.toByteArray();
        /*     */
    }

    /*     */
    /*     */
    private static void copy(InputStream in, OutputStream out)
    /*     */     throws IOException
    /*     */ {
        /*     */
        try
            /*     */ {
            /* 218 */
            byte[] buffer = new byte[4096];
            /* 219 */
            int nrOfBytes = -1;
            /* 220 */
            while ((nrOfBytes = in.read(buffer)) != -1) {
                /* 221 */
                out.write(buffer, 0, nrOfBytes);
                /*     */
            }
            /* 223 */
            out.flush();
            /*     */
        } catch (IOException localIOException) {
            /*     */
        }
        /*     */ finally {
            /*     */
            try {
                /* 228 */
                if (in != null)
                    /* 229 */ in.close();
                /*     */
            }
            /*     */ catch (IOException localIOException3) {
                /*     */
            }
            /*     */
            try {
                /* 234 */
                if (out != null)
                    /* 235 */ out.close();
                /*     */
            }
            /*     */ catch (IOException localIOException4)
                /*     */ {
                /*     */
            }
            /*     */
        }
        /*     */
    }

    /*     */
    /*     */
    public static boolean writeDataHandlerToFile(DataHandler attachinfo, String filename)
    /*     */ {
        /* 246 */
        FileOutputStream fos = null;
        /*     */
        try {
            /* 248 */
            fos = new FileOutputStream(filename);
            /* 249 */
            writeInputStreamToFile(attachinfo.getInputStream(), fos);
            /* 250 */
            fos.close();
            /*     */
        } catch (Exception e) {
            /* 252 */
            return false;
            /*     */
        } finally {
            /* 254 */
            if (fos != null)
                /*     */ try {
                /* 256 */
                fos.close();
                /*     */
            }
            /*     */ catch (Exception localException2) {
                /*     */
            }
            /*     */
        }
        /* 261 */
        return true;
        /*     */
    }

    /*     */
    /*     */
    private static void writeInputStreamToFile(InputStream is, OutputStream os) throws Exception
    /*     */ {
        /* 266 */
        int n = 0;
        /* 267 */
        byte[] buffer = new byte[8192];
        /* 268 */
        while ((n = is.read(buffer)) > 0)
            /* 269 */ os.write(buffer, 0, n);
        /*     */
    }
    /*     */
}

/* Location:           E:\移联百汇\二期设计\java压缩加密\win.jar
 * Qualified Name:     com.training.commons.file.FileUtils
 * JD-Core Version:    0.5.3
 */
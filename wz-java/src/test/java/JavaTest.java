import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 * wangzhen23
 * 2017/9/14.
 */
public class JavaTest {
    static {
        ii = 0;
        ii = 5 + 1;
    }

    static int ii = 1;

    public static void main(String[] args) {
//        bina();
        try {
            http();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void bina() {
        int i = 0x0001;
        int j = 0x0010;
        int k = 0x0020;
        int l = 0x0200;
        int m = 0x0400;
        int n = 0x1000;
        int o = 0x2000;
        int p = 0x4000;

        int HASH_INCREMENT = 0x61c88647;
        AtomicInteger nextHashCode = new AtomicInteger();
        for (int q = 0; q < 16; q++) {
            int index = nextHashCode.getAndAdd(HASH_INCREMENT) & 15;
            System.out.println(q + "， 十六进制" + Integer.toHexString(index) + " 十进制：" + index);
//            System.out.println(q + "， 二进制：" + Integer.toBinaryString(index));
        }

        System.out.printf("HASH_INCREMENT=0x%04x\n", HASH_INCREMENT);

        System.out.println("二进制 i：" + Integer.toBinaryString(i));
        System.out.println("二进制 j：" + Integer.toBinaryString(j));
        System.out.println("二进制 k：" + Integer.toBinaryString(k));
        System.out.println("二进制 l：" + Integer.toBinaryString(l));
        System.out.println("二进制 m：" + Integer.toBinaryString(m));
        System.out.println("二进制 n：" + Integer.toBinaryString(n));
        System.out.println("二进制 o：" + Integer.toBinaryString(o));
        System.out.println("二进制 p：" + Integer.toBinaryString(p));
        System.out.println("二进制 HASH_INCREMENT：" + Integer.toBinaryString(HASH_INCREMENT));

        System.out.println("inc:" + inc());

        System.out.println("ii=" + ii);

        String name = "12)34567890";
        int ix = name.indexOf(')');
        System.out.println("index:" + ix);
        System.out.println("subStart:" + name.substring(0, ix + 1));
        System.out.println("substring:" + name.substring(ix + 1));

        Stack<String> javaCodeStack = new Stack<String>();
        javaCodeStack.push("var1");
        javaCodeStack.push("var2");
        javaCodeStack.push(javaCodeStack.pop() + "+" + javaCodeStack.pop());
        javaCodeStack.push("return " + javaCodeStack.pop());
        for (String s : javaCodeStack) {
            System.out.println("=====:" + s);
        }
    }

    public static int inc() {
        int x;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
        }
    }

    public static void http() throws IOException, InterruptedException {
        String[] paths = {"http://blog.csdn.net/a_zhenzhen/article/details/77977345",
                "http://blog.csdn.net/a_zhenzhen/article/details/77917991",
                "http://blog.csdn.net/a_zhenzhen/article/details/77862607",
                "http://blog.csdn.net/a_zhenzhen/article/details/78112312",
                "http://blog.csdn.net/a_zhenzhen/article/details/78036920",
                "http://blog.csdn.net/a_zhenzhen/article/details/78028706",
                "http://blog.csdn.net/a_zhenzhen/article/details/77946471"
        };
        for (int i = 0; i < 1000; i++) {
            for (String path : paths) {
                URL url = new URL(path.trim());
                //打开连接
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (200 == urlConnection.getResponseCode()) {
                    //得到输入流
                    InputStream is = urlConnection.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while (-1 != (len = is.read(buffer))) {
                        baos.write(buffer, 0, len);
                        baos.flush();
                    }
                    System.out.println(i + " - " + path);
                }
            }
            Thread.sleep(60*1000);
        }
    }
}

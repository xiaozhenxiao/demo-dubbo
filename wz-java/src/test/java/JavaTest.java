import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
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
//        stack();
        try {
            http();
//            read();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        int test = Integer.parseInt("02",16);
//        System.out.println("test:" + test);

        /*int r = 0 << 8 | 3;
        System.out.println("r=" + r);

        int[] src = new int[]{1,2,3,4,5,6,7,8,9,0};
        int[] dest = new int[10];
        System.arraycopy(src, 1, dest, 0, 8);
        for (int i : dest) {
            System.out.println(i);
        }*/
    }

    private static void stack() {
        Stack<String> javaCodeStack = new Stack<String>();
        javaCodeStack.push("a");//loadattributes
        javaCodeStack.push("b");
        pushCalc(javaCodeStack, javaCodeStack.pop(), javaCodeStack.pop(), " + ");//iadd
        javaCodeStack.push("int i = " + javaCodeStack.pop());//istore
        javaCodeStack.push("a");//iload
        javaCodeStack.push("b");//iload
        pushCalc(javaCodeStack, javaCodeStack.pop(), javaCodeStack.pop(), " * ");//imul
        javaCodeStack.push("int j = " + javaCodeStack.pop());//istore 4
        javaCodeStack.push("i");//iload_3
        javaCodeStack.push("j");//iload_4
        pushCalc(javaCodeStack, javaCodeStack.pop(), javaCodeStack.pop(), " + ");//iadd
        javaCodeStack.push("int k = " + javaCodeStack.pop());//istore 5
        javaCodeStack.push("k");//iload 5
        javaCodeStack.push("return " + javaCodeStack.pop());//ireturn
        int num = 0;
        for (String s : javaCodeStack) {
            System.out.println(num + ": " + s);
            num++;
        }
    }

    public static Stack<String> pushCalc(Stack<String> stack, String second, String first, String operateType) {
        stack.push(first + operateType + second);
        return stack;
    }

    public static void read() throws IOException {
        FileReader fr = new FileReader("C:/Users/wangzhen23/Desktop/class.txt");
        FileWriter fw = new FileWriter("E:/bytecode.txt");
        BufferedReader br = new BufferedReader(fr);
        BufferedWriter bw = new BufferedWriter(fw);
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line != null) {
                String[] line2 = line.trim().split("\\s+");
                if (line2.length > 1) {
                    String ll = line2[0].trim() + " " + line2[1].trim() + "\r\n";
                    System.out.println("====" + ll);
                    bw.write(ll);
                    bw.flush();
                }
            }
        }
        bw.flush();
        fr.close();
        br.close();
        fw.close();
        bw.close();
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
        String[] paths = {
                "http://blog.csdn.net/a_zhenzhen/article/details/77962376",
                "http://blog.csdn.net/a_zhenzhen/article/details/77865310",
                "http://blog.csdn.net/a_zhenzhen/article/details/77867589",
                "http://blog.csdn.net/a_zhenzhen/article/details/77977345",
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
            Thread.sleep(60 * 1000);
        }
    }
}

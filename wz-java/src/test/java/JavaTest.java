/**
 * TODO
 * wangzhen23
 * 2017/9/14.
 */
public class JavaTest {
    static {
        ii = 0;
        ii = 5+1;
    }

    static int ii = 1;
    public static void main(String[] args) {
        int i = 0x0001;
        int j = 0x0002;
        int k = 0x0004;
        int l = 0x0008;
        int m = 0x0010;
        int n = 0x0040;
        int o = 0x0080;
        int p = 0x1000;

        int a = 11;
        String aHex = Integer.toHexString(a);
        System.out.println("====" + aHex);
        System.out.printf("a=0x%04x\n", a);
//        System.out.printf("j=0x%04x\n", j);
//        System.out.printf("a=0x%04x\n", a);

        System.out.println("二进制 i：" + Integer.toBinaryString(i));
        System.out.println("二进制 j：" + Integer.toBinaryString(j));
        System.out.println("二进制 k：" + Integer.toBinaryString(k));
        System.out.println("二进制 l：" + Integer.toBinaryString(l));
        System.out.println("二进制 m：" + Integer.toBinaryString(m));
        System.out.println("二进制 n：" + Integer.toBinaryString(n));
        System.out.println("二进制 o：" + Integer.toBinaryString(o));
        System.out.println("二进制 p：" + Integer.toBinaryString(p));
        System.out.println("二进制 a：" + Integer.toBinaryString(a));

        System.out.println("inc:" + inc());

        System.out.println("ii=" + ii);
    }

    public static int inc(){
        int x;
        try {
            x = 1;
            return x;
        }catch (Exception e){
            x = 2;
            return x;
        }finally {
            x = 3;
        }
    }
}

package example;

public class Util {
    public static byte[] byteSum (byte[] a,byte[] b){
        byte[] c= new byte[a.length+b.length];
        System.arraycopy(a,0,c,0,a.length);
        System.arraycopy(b,0,c,a.length,b.length);
        return c;
    }

    public static byte[] byteSum (String a_string,byte[] b){
        byte[] a = a_string.getBytes();
        byte[] c= new byte[a.length+b.length];
        System.arraycopy(a,0,c,0,a.length);
        System.arraycopy(b,0,c,a.length,b.length);
        return c;
    }
}

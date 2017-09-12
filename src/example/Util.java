package example;

import java.io.IOException;
import java.net.ServerSocket;

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

    public static String byteSum (String header,String value,String origin){
        StringBuffer sb = new StringBuffer();
        sb.append(origin);
        sb.append(header + ": ");
        sb.append(value + "\r\n");
        return sb.toString();
    }

    public static void scanPort (){
        for(int port=1;port<=65535;port++){
            try{
                ServerSocket serverSocket=new ServerSocket(port);
                serverSocket.close();   //及时关闭ServerSocket
            }catch(IOException e){
                System.out.println("端口"+port+" 已经被其他服务器进程占用");
            }
        }
    }

    //提示区添加文字
    public static void addJTextArea (String s){
        HttpServer.mainUiCopy.textArea1.append("\n\r" + s);
        HttpServer.mainUiCopy.textArea1.setCaretPosition(HttpServer.mainUiCopy.textArea1.getDocument().getLength());
    }


}

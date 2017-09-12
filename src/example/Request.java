package example;

import java.io.InputStream;
import java.io.IOException;

public class Request {
    private InputStream input;
    private String uri;
    private String method;
    private String suffix;
    private String fileName;

    public Request(InputStream input) {
        this.input = input;
    }

    //从InputStream中读取request信息，并从request中获取uri值
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        Util.addJTextArea(request.toString());
//        System.out.print(request.toString());
        parseAll(request.toString());
    }
    /*
     *
     * requestString形式如下：
     * GET /index.html HTTP/1.1
     * Host: localhost:8080
     * Connection: keep-alive
     * Cache-Control: max-age=0
     * ...
     * 该函数目的就是为了获取/index.html字符串
     */
    private void parseAll(String requestString){
        uri = parseUri(requestString);
        method = parseMethod(requestString);
        suffix = parseSuffix(uri);
        fileName = parseFilename(uri);
    }

    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    private String parseMethod(String requestString) {//GET、POST、HEAD、PUT、DELETE、TRACE、CONNECT、OPTIONS
        int index1 = requestString.indexOf(' ');
        if (index1 != -1) {
                return requestString.substring(0, index1);
        }
        return null;
    }

    private String parseSuffix(String requestString) {//获取文件后缀
        int index1, index2;
        index1 = requestString.lastIndexOf('/');
        if (index1 != -1) {
            index2 = requestString.lastIndexOf('.');
            if (index2 > index1)
                return requestString.substring(index2 + 1, requestString.length());
        }
        return null;
    }

    private String parseFilename(String requestString) {//获取文件名
        int index1, index2;
        index1 = requestString.lastIndexOf('/');
        if (index1 != -1) {
            index2 = requestString.lastIndexOf('.');
            if (index2 > index1)
                return requestString.substring(index1 + 1, requestString.length());
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getFileName() {
        return fileName;
    }
}
package example;

import java.io.InputStream;
import java.io.IOException;
import java.util.*;

public class Request {
    private InputStream input;
    private String uri;
    private String method;
    private String suffix;
    private String prefix;
    private String fileName;
    private Boolean isQuery;
    private Map<String,String> map ;



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
//            input.close();
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
        prefix = parsePrefix(uri);
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
            index2 = requestString.indexOf('.',index1+1);
            if (index2 == -1){
                index2 = requestString.indexOf('?',index1+1);
            }
            if (index2 > index1)
                return requestString.substring(index2 + 1, requestString.length());
        }
        return null;
    }

    private String parsePrefix(String requestString) {//获取文件前缀,并提取查询map
        int index1, index2;
        isQuery = false;
        index1 = requestString.lastIndexOf('/');
        if (index1 != -1) {
            index2 = requestString.indexOf('?',index1+1);
            if (index2 == -1){
                index2 = requestString.indexOf('.',index1+1);
            }else {
                isQuery = true;
            }
            if (index2 > index1){
                map = new HashMap<>();
                String result = requestString.substring(index1 + 1, index2);
                String result2 = requestString.substring(index2 + 1, requestString.length());
                if (isQuery){//如果是查询模式 切割map
                    String[] split = result2.split("\\&");
                    for (int i=0;i<split.length;i++){
                        String[] split2 = split[i].split("\\=");
                        if (split2.length == 2){//处理一下避免遇到 pass= 这种空值
                            map.put(split2[0],split2[1]);
                        }else {
                            map.put(split2[0],"");
                        }
                    }
                }
                return result;
            }

        }
        return null;
    }


    private String parseFilename(String requestString) {//获取文件名
        int index1, index2;
        index1 = requestString.lastIndexOf('/');
        if (index1 != -1) {
            index2 = requestString.lastIndexOf('.');
            if (index2 > index1){
                return requestString.substring(index1 + 1, index2);
            }
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

    public Map<String, String> getMap() {
        return map;
    }
}
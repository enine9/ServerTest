package example;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

/*
  HTTP Response = Status-Line
    *(( general-header | response-header | entity-header ) CRLF)
    CRLF
    [ message-body ]
    Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
*/

public class Response {

    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;
    int lenghtStream;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        int ch = 0;
        String headerMessage = null;
        byte[] newBytes = null;
        try {
//            将web文件写入到OutputStream字节流中
            request.getMethod();
            String suffix = request.getSuffix();
            String filename = request.getFileName();
            filename = filename==null?"":filename;
            switch (filename){
                case "copyout": //返回后缀文字
                    headerMessage = "HTTP/1.1 200 OK\r\n" ;
                    headerMessage = Util.byteSum("Content-Type","text/html; charset=UTF-8",headerMessage);
                    headerMessage = Util.byteSum("Content-Length",Integer.toString(suffix.length()),headerMessage);
                    headerMessage = Util.byteSum("Connection","keep-alive",headerMessage);
                    headerMessage = headerMessage + "\r\n" + suffix; //结尾要多一个空行
                    output.write(headerMessage.getBytes());
                    break;
                case "servercontrol":
                    switch (suffix){
                        case "close":
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    suffix = filename==""?"index.html":request.getUri();
                    File file = new File(HttpServer.WEB_ROOT, suffix);
                    if (file.exists()) { //如果访问路径存在
                        fis = new FileInputStream(file);
                        lenghtStream = fis.available();//最大获取2GB,File的length()方法可获取大于2GB大小，或者用java.nio
                        ch = fis.read(bytes, 0, BUFFER_SIZE);
                        if (ch != -1){//添加header
                            headerMessage = "HTTP/1.1 200 OK\r\n" ;
                            headerMessage = Util.byteSum("Content-Type","text/html; charset=UTF-8",headerMessage);
                            headerMessage = Util.byteSum("Content-Length",Integer.toString(lenghtStream),headerMessage);
                            headerMessage = Util.byteSum("Connection","keep-alive",headerMessage);
                            if (suffix.equals("rar") || suffix.equals("zip")){//添加下载文件头
                                headerMessage = Util.byteSum("Content-Disposition","attachment; filename=\""
                                        + request.getFileName() + "\"",headerMessage);
                            }
                            //TODO:断点续传 Clien Range: bytes=0-800 //一般请求下载整个文件是bytes=0- 或不用这个头
                            //TODO:断点续传 Server Content-Range: bytes 0-800/801 //801:文件总大小
                            headerMessage = headerMessage + "\r\n"; //结尾要多一个空行
                            newBytes = Util.byteSum(headerMessage,bytes);
                            output.write(newBytes, 0, newBytes.length);
                            ch = fis.read(bytes, 0, BUFFER_SIZE);
                            while (ch != -1) {
                                output.write(bytes, 0, ch);
                                ch = fis.read(bytes, 0, BUFFER_SIZE);
                            }

                        }
                    } else {
                        // file not found
                        String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
                                + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
                        output.write(errorMessage.getBytes());
                    }

            }


        } catch (Exception e) {
            // thrown if cannot instantiate a File object
            System.out.println(e.toString());
        } finally {
            if (fis != null)
                fis.close();
        }
    }
}
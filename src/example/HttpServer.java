package example;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.File;

public class HttpServer {
  /*
   * WEB_ROOT是HTML和其它文件存放的目录. 这里的WEB_ROOT为工作目录下的webroot目录
   */
  public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
  public static final String SERVERIP = "127.0.0.1";
  public static final int PORT = 8088;

  // 关闭服务命令
  public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
  public static boolean closeServer;

  public static void main(String[] args) {
    HttpServer server = new HttpServer();
    //等待连接请求
//    Util.scanPort();
    server.swait();
  }

  public void swait() {
    ServerSocket serverSocket = null;
    Thread receiveThread = null;
    System.out.println("WEB_ROOT:"+ WEB_ROOT + "port:" + PORT);
    try {
      //服务器套接字对象
      serverSocket = new ServerSocket(PORT, 10, InetAddress.getByName(SERVERIP));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    // 循环等待一个请求
    while (true) {
      Socket socket = null;
      try {
        //等待连接，连接成功后，返回一个Socket对象
        socket = serverSocket.accept();
        receiveThread =new Thread(new serverThread(socket));
        receiveThread.start();
        // 检查是否是关闭服务命令
        if (closeServer) {
          break;
        }
      } catch (Exception e) {
        e.printStackTrace();
        continue;
      }
    }
  }
}

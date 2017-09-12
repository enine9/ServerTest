package example;
import javax.swing.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.File;

public class HttpServer {
  /*
   * WEB_ROOT是HTML和其它文件存放的目录. 这里的WEB_ROOT为工作目录下的webroot目录
   */
  public static  String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
  public static  String SERVERIP = "127.0.0.1";
  public static  int PORT = 8088;
  public static JTextArea JTextAreaCopy;
  public static mainUi mainUiCopy;


  // 关闭服务命令
  public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
  public static boolean closeServer;

  public static void main(String[] args) {
//    HttpServer server = new HttpServer();
//    Util.scanPort();
//    server.swait();
  }

  //初始化
  public void init(mainUi mainui){
    SERVERIP = mainui.serverIpTextField.getText();
    PORT =Integer.parseInt(mainui.portTextField.getText());
    Util.addJTextArea("WEB_ROOT: \n\r"+ WEB_ROOT + " Server: \n\r"+SERVERIP+":" + PORT);
  }

  public void swait(mainUi mainui) {
    ServerSocket serverSocket = null;
    Thread receiveThread = null;
    mainUiCopy = mainui; //获取Ui
    init(mainUiCopy);//初始化
//    System.out.println("WEB_ROOT:"+ WEB_ROOT + "port:" + PORT);
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
        if (closeServer) {
          closeServer=false;
          break;
        }
        receiveThread = new Thread(new serverThread(socket));
        receiveThread.start();
        // 检查是否是关闭服务命令
        if (closeServer) {
          closeServer=false;
          break;
        }
      } catch (Exception e) {
        e.printStackTrace();
        continue;
      }
    }
  }
}

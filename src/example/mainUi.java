package example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainUi {
    public JTextField serverIpTextField;
    private JButton runServerButton;
    private JButton clearButton;
    public JTextField portTextField;
    private JPanel JP;
    public JTextArea textArea1;
    public static mainUi mainui;
    public static JFrame frame;

    public mainUi() {
        runServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (runServerButton.getText() == "RunServer"){
                    HttpServer.closeServer=false;
                    runServerButton.setText("StopServer");
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpServer server = new HttpServer();
                            server.swait(mainui);
                        }
                    });
                    thread.start();
                }else {
                    HttpServer.closeServer = true;
                    Util.addJTextArea("close ServerWeb");
                    HttpServer.shutdown();
                    runServerButton.setText("RunServer"); //关闭服务器
                }

            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.setText("");
            }
        });
    }

    public static void main(String[] args) {
        mainui = new mainUi();
        frame = new JFrame("Easy Web Server v0.1");
        frame.setContentPane(mainui.JP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,600);//窗口大小
        frame.setResizable(false);//是否可以拖动
        frame.pack();
        frame.setVisible(true);
        mainui.textArea1.setLineWrap(true); //自动换行
        mainui.textArea1.setWrapStyleWord(true); //换行不断字
        mainui.textArea1.setText("Hello World! \n\r 支持2G以内文件读取和简单http协议。\n\rweb目录为程序运行目录下的webroot。");

//        test();

    }

    private static void test(){
        String[] split = "name=lee&pass&age=32".split("\\&");
        String[] split2 = split[1].split("\\=");
        System.out.println(split);
    }

}

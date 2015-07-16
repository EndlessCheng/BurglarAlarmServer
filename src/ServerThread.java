import bean.Message;

import java.util.*;
import java.io.*;
import java.net.*;

// 服务器监听端口线程
public class ServerThread extends Thread {
    // 声明ServerSocket类对象
    private ServerSocket serverSocket;
    // 指定服务器监听端口常量
    public static final int PORT = 36500;

    public Vector<ClientThread> clients;
    // 创建一个Vector对象，用于存储客户端发送过来的信息
    public Vector<Message> messages;
    // BroadCast类负责服务器向客户端广播消息
    private BroadCast broadcast;

    private String ip;
    private InetAddress myIPaddress = null;

    public ServerThread() {
        /***
         * 创建两个Vector数组非常重要 ， clients负责存储所有与服务器建立连接的客户端，
         * messages负责存储服务器接收到的未发送出去的全部客户端的信息
         *
         **/
        clients = new Vector<ClientThread>();
        messages = new Vector<Message>();

        try {
            // 创建ServerSocket类对象
            serverSocket = new ServerSocket(PORT);
        } catch (IOException E) {
            E.printStackTrace();
        }
        // 获取本地服务器地址信息
        try {
            myIPaddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        ip = myIPaddress.getHostAddress();
        Server.messageTextArea.append("服务器地址：" + ip + "端口号:" + serverSocket.getLocalPort() + "\n");

        // 创建广播信息线程并启动
        broadcast = new BroadCast(this);
        broadcast.start();
    }

    /**
     * 一旦监听到有新的客户端创建就 new Socket(ip, PORT) 创建一个 ClientThread 来维持服务器与这个客户端的连接
     */
    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                System.out.println("socket.getInetAddress().getHostAddress(): " + socket.getInetAddress().getHostAddress());
                // 创建ClientThread线程并启动,可以监听该连接对应的客户端是否发送来消息，并获取消息
                ClientThread clientThread = new ClientThread(socket, this);
                clientThread.start();
                // if (socket != null) {
                synchronized (clients) {
                    // 将客户端连接加入到Vector数组中保存
                    clients.addElement(clientThread);
                    System.out.println("clients.size(): " + clients.size());
                }
                // }
            } catch(SocketException e){
                System.out.println("socket closed");
                break;
            }
            catch (IOException E) {
                E.printStackTrace();
//                System.out.println("发生异常：" + E);
//                System.out.println("建立客户端联机失败！");
                System.exit(2);
            }
        }
    }

    @Override
    public void finalize() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverSocket = null;
        //super.finalize();
    }
}

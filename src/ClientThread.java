import bean.Message;

import java.net.*;
import java.io.*;

/**
 * 维持服务器与单个客户端的连接线程，负责接收客户端发来的信息,
 * 声明一个新的Socket对象，
 * 用于保存服务器端用accept方法得到的客户端的连接
 */
public class ClientThread extends Thread {
    Socket clientSocket;

    //声明服务器端中存储的Socket对象的数据输入／输出流
    DataInputStream in = null;
    DataOutputStream out = null;

    //声明ServerThread对象
    ServerThread serverThread;

    public ClientThread(Socket socket, ServerThread serverThread) {
        clientSocket = socket;
        this.serverThread = serverThread;
        try {
            //创建服务器端数据输入／输出流
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e2) {
            System.out.println("发生异常" + e2);
            System.out.println("建立I/O通道失败！");
            System.exit(3);
        }
    }

    public void run() {
        Message message;

        String locationString;

        int bytesRead;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos;
        while (true) {
            try {
                message = new Message();

                int messageType = in.readInt();
                message.setMessageType(messageType);
                if (0 == messageType) {
                    locationString = in.readUTF();
                    message.setMessageData(locationString);
                    Server.messageTextArea.append("locationString: " + locationString + "\n");
                } else {
                    baos = new ByteArrayOutputStream(1024);
                    while ((bytesRead = in.read(buffer)) > 0) { // 等 BroadCast 转发完图片再 close()
                        baos.write(buffer, 0, bytesRead);
                        Server.messageTextArea.append("Picture part size: " + bytesRead + "\n");
                        //      Server.messageTextArea.append(new String(buffer, 0, bytesRead) + "\n\n");

                        if (bytesRead < 1024) {
                            break;
                        }
                    }
                    baos.close(); // ************
                    byte[] pic = baos.toByteArray();
                    if (pic.length == 0) {  //  TODO: why????
                        continue;
                    }
                    message.setMessageData(pic);
                    Server.messageTextArea.append("\nPicture final size: " + pic.length + "\n\n");
                    //        synchronized (serverThread.messages) {
//                  if (message != null) {
//                        if ("exit".equals(message)) {
//                            clientSocket.close();
//                            // TODO: remove Vector ?
//                        }

                }
                //将客户端发送来得信息存于serverThread的messages数组中
                serverThread.messages.addElement(message);

                //在服务器端的文本框中显示新消息
                //Server.messageTextArea.append(message + '\n');
                // }
                //   }
            } catch (IOException E) {
                break;
            }
        }
    }
}

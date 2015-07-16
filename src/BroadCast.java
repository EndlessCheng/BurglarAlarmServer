import bean.Message;

import java.io.*;

// 服务器向客户端广播线程
public class BroadCast extends Thread {
    ClientThread clientThread;
    // 声明ServerThread对象
    ServerThread serverThread;
    Message message;

    public BroadCast(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    // 该方法的作用是不停地向所有客户端发送新消息
    public void run() {
        while (true) {
//            try {
//                // 线程休眠200 ms
//                Thread.sleep(200);
//            } catch (InterruptedException E) {
//                E.printStackTrace();
//            }

            // 同步化serverThread.messages
            synchronized (serverThread.messages) {
                // 判断是否有未发的消息
                if (serverThread.messages.isEmpty()) {
                    continue;
                }
                // 获取服务器端存储的需要发送的第一条数据信息
                message = this.serverThread.messages.firstElement();

                // 同步化serverThread.clients
                synchronized (serverThread.clients) {
                    // 利用循环获取服务器中存储的所有建立的与客户端的连接
                    for (int i = 0; i < serverThread.clients.size(); i++) {
                        clientThread = serverThread.clients.elementAt(i);
                        if (null == clientThread) {
                            continue;
                        }
                        try {
                            if (0 == message.getMessageType()) {
                                clientThread.out.writeUTF((String) message.getMessageData());
                                // needn't close IO
                            } else {
                                Server.messageTextArea.append("正在发送...");

                                clientThread.out.write((byte[]) message.getMessageData());
                                //      clientThread.out.flush();
                                clientThread.out.close();
                                clientThread = null;
                                Server.messageTextArea.append("ok!\n\n");
                            }

                        } catch (IOException E) {
                            E.printStackTrace();
                        }
                    }
                }
                this.serverThread.messages.removeElement(message);
            }
        }
    }
}

import bean.Message;

import java.io.*;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class BroadCast extends Thread {

    private ServerThread serverThread;

    public BroadCast(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    public void run() {
        while (true) {
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException E) {
//                E.printStackTrace();
//            }
            synchronized (serverThread.messages) {
                if (serverThread.messages.isEmpty()) {
                    continue;
                }
                Message message = serverThread.messages.get(0);
                Iterator<ClientThread> i = serverThread.clients.iterator();
                while(i.hasNext()) {
                    ClientThread c = i.next();
                    try {
                        String head = message.getMessageHead();
                        c.out.writeUTF(head);
                        c.out.flush();
                        switch (message.getMessageType()) {
                            case "setLocation":
                            case "getPhoto":
                                c.out.writeUTF((String) message.getMessageData());
                                c.out.flush();
                                Server.messageTextArea.append("发送: " + message.getMessageData()+"\n");
                                break;
                            case "setPhoto":
                                Server.messageTextArea.append("正在发送...");
                                c.out.write((byte[]) message.getMessageData());
                                c.out.close();
                                // c.out = null;
                                i.remove();
                                Server.messageTextArea.append("ok!\n\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                serverThread.messages.remove(message);
            }
        }
    }
}

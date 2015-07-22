import bean.Message;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

// 服务器监听端口线程
public class ServerThread extends Thread {

    public static final int PORT = 36500;

    public CopyOnWriteArrayList<ClientThread> clients;
    public List<Message> messages;

    private ServerSocket serverSocket;

    public ServerThread() {
        clients = new CopyOnWriteArrayList<ClientThread>();
        messages = Collections.synchronizedList(new ArrayList<Message>());
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException E) {
            E.printStackTrace();
        }
        Server.messageTextArea.append("服务器已开启\n");

        new BroadCast(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket s = serverSocket.accept();

                System.out.println("socket.getInetAddress().getHostAddress(): " + s.getInetAddress().getHostAddress());

                ClientThread c = new ClientThread(s, this);
                c.start();
                clients.add(c);
                System.out.println("clients.size(): " + clients.size());
            } catch (SocketException e) {
                System.out.println("socket closed");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
    }

    @Override
    public void finalize() {
        try {
            super.finalize();
            serverSocket.close();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            serverSocket = null;
        }
    }
}

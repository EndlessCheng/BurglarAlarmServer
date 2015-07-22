import bean.Message;

import java.net.*;
import java.io.*;

public class ClientThread extends Thread {
    private static final int BUFFER_SIZE = 50 * 1024;

    private DataInputStream in;
    public DataOutputStream out;

    private ServerThread serverThread;

    public ClientThread(Socket clientSocket, ServerThread serverThread) {
        this.serverThread = serverThread;
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Message message;
        ByteArrayOutputStream baos;
        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            try {
                String data = in.readUTF();
                Server.messageTextArea.append("data: " + data + "\n");
                String[] result = data.split(",");
                message = new Message(result[0], result[1], result[2]);
                switch (result[0]) {
                    case "setLocation":
                        String locationString = in.readUTF();
                        message.setMessageData(locationString);
                        Server.messageTextArea.append("setLocation: " + locationString + "\n");
                        break;
                    case "setPhoto":
                        long fileLength = Long.parseLong(result[3]);
                        Server.messageTextArea.append("fileLength: " + fileLength + "\n");

                        baos = new ByteArrayOutputStream(BUFFER_SIZE);
                        int bytesRead;
                        for (long count = 0L; count < fileLength; ) {
                            bytesRead = in.read(buffer);
                            baos.write(buffer, 0, bytesRead);
                            Server.messageTextArea.append("bytesRead: " + bytesRead + "\n");
                            count += bytesRead;
                            Server.messageTextArea.append("now count: " + count + "\n");
                        }
                        baos.close();

                        byte[] pictureData = baos.toByteArray();
                        if (pictureData.length == 0) { // TODO: why????
                            continue;
                        }
                        message.setMessageData(pictureData);
                        break;
                    default:
                }
                serverThread.messages.add(message);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

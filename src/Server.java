import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.*;

/**
 * 服务器端主程序负责界面，以及服务段主线程ServerThread的启动
 * 服务端主线程ServerThread又产生BroadCast及ClientThread线程 建立服务器端主界面中所用到的布局方式
 */
public class Server extends JFrame implements ActionListener {
    private JButton startServerButton;
    private JButton closeServerButton;

    public static JTextArea messageTextArea;

    public Server() {
        super("防盗 App 服务器");

        JPanel buttonsPanel = new JPanel();

        startServerButton = new JButton();
        startServerButton.setText("启动服务器");
        startServerButton.addActionListener(this);
        closeServerButton = new JButton();
        closeServerButton.setText("关闭服务器");
        closeServerButton.addActionListener(this);

        buttonsPanel.add(startServerButton);
        buttonsPanel.add(closeServerButton);
        getContentPane().add(buttonsPanel, java.awt.BorderLayout.NORTH);


        JScrollPane messageScrollPane = new JScrollPane();

        messageTextArea = new JTextArea();
        messageTextArea.setText("");

        messageScrollPane.getViewport().add(messageTextArea);
        getContentPane().add(messageScrollPane, java.awt.BorderLayout.CENTER);


        setSize(600, 600);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startServerButton) {
            ServerThread serverThread = new ServerThread();
            serverThread.start();
            startServerButton.setEnabled(false);
        } else if (e.getSource() == closeServerButton) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        Server sever = new Server();
        sever.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

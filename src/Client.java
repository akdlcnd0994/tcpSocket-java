import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;


public class Client {
    static JFrame f = new JFrame("채팅프로그램");
    static JTextArea ta = new JTextArea();
    static JTextField tf = new JTextField();
    static JTextArea ta2 = new JTextArea("[접속자 명단]");


    public static void main(String[] args) {
        try {
            String name;
            String SIp = "127.0.0.1";
            Socket socket = new Socket(SIp, 7777);
            System.out.println("서버에 연결되었습니다.");

            name = JOptionPane.showInputDialog("닉네임을 입력하세요.");
            new Sender(socket, name);
            new Receiver(socket).start();
        } catch (IOException e) {}
    }


    static class Sender implements KeyListener {
        Socket socket;
        String name;
        static DataOutputStream out;
        Sender(Socket socket, String name) {
            this.socket = socket;
            this.name = name;

            f.setSize(600, 500);
            f.add(ta, "Center");
            ta.setBackground(Color.gray);
            ta.setFont(new Font("굴림체", Font.BOLD, 15));
            tf.addKeyListener(this);
            f.add(tf, "South");
            f.add(ta2, "East");
            f.setVisible(true);
            f.addWindowListener(new FrameListener());
            try {
                out = new DataOutputStream(socket.getOutputStream());
                if (out != null) {
                    out.writeUTF(name);
                }
            } catch (IOException e) {}
        }
        @Override
        public void keyPressed(KeyEvent ke) {
            String t;
            if (ke.getKeyCode() == 10) {
                t = tf.getText();
                t = "[" + name + "] : " + t;
                try {
                    out.writeUTF(t);

                } catch (IOException e) {}
                tf.setText("");
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub

        }
        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
        }
        static class FrameListener extends WindowAdapter {
            public void windowClosing(WindowEvent e) {
                try {
                    out.writeUTF("##프로그램종료");
                } catch (IOException e1) {}
                System.exit(0);
            }
        }
    }


    static class Receiver extends Thread {
        Socket socket;
        DataInputStream in ;
        Receiver(Socket socket) {
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {}
        }

        public void run() {
            String temp;
            String[] tt = new String[2];
            while ( in != null) {
                try {
                    temp = in .readUTF();
                    tt = temp.split("}");

                    ta.append(tt[0] + "\n");
                    ta2.setText(tt[1]);

                } catch (IOException e) {}
            }
        }
    }
}
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.Entry;
import java.awt.*;
import java.net.*;

class Server {
    static HashMap clients = new HashMap();
    static HashSet hs = new HashSet();
    public static void main(String args[]) throws IOException, InterruptedException {
        ServerSocket server = null;
        Socket socket = null;
        String tname = null;


        System.out.println("채팅프로그램서버가 시작되었습니다.");
        Collections.synchronizedMap(clients);
        server = new ServerSocket(7777);
        while (true) {
            Thread.sleep(1500);
            System.err.println(clients.size());
            if (clients.size() >= 2) {
                Thread.sleep(200);
                continue;
            }
            try {
                socket = server.accept();
                System.out.println("[" + socket.getInetAddress() + "] 님이 접속하셨습니다.[Login]");

                service sv = new service(socket);
                sv.start();
            } catch (Exception e) {}
        }
    }

    private static void sleep(int i) {
        // TODO Auto-generated method stub

    }

    static void send(String msg) throws IOException {
        Iterator it = clients.keySet().iterator();
        Iterator h = hs.iterator();
        int t = 0, i = 0;
        String temp = "}[접속자 명단]";

        while (h.hasNext()) {
            temp += "\n" + h.next();
            i++;
        }
        msg += temp;
        while (it.hasNext()) {
            try {
                DataOutputStream out = (DataOutputStream) clients.get(it.next());
                out.writeUTF(msg);
            } catch (IOException e) {}
        }
    }

    static class service extends Thread {
        Socket socket;
        DataInputStream in ;
        DataOutputStream out;
        String name;
        String msg;

        service(Socket socket) {
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {}
        }

        public void run() {
            String a = null;
            try {
                name = in .readUTF();
                clients.put(name, out);
                hs.add(name);

                send(name + "님이 접속하셨습니다.");
                System.out.println("현재 서버 접속인원 : " + clients.size());
                while (true) {
                    if ((a = in .readUTF()).equals("##프로그램종료")) {
                        break;
                    }
                    try {
                        send(a);
                    } catch (IOException e) {}
                }
            } catch (IOException e) {} finally {
                clients.remove(name);
                hs.remove(name);
                try {
                    send(name + "님이 접속을 종료하셨습니다.");
                } catch (IOException e) {}
                System.out.println("[" + socket.getInetAddress() + "] 님이 접속을 종료하셨습니다.[Logout]");
                System.out.println("현재 서버 접속인원 : " + clients.size());
            }
        }
    }

}
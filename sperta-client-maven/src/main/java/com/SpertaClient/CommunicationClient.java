package com.SpertaClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import com.sperta.Sperta.*;
import com.google.protobuf.*;

public class CommunicationClient {
    private Socket socket;
    private OutputStream out;

    public CommunicationClient(String host) {
        String[] ipport = host.split(":");
        String ip = ipport[0];
        int port = 22345;
        if (ipport.length == 2) {
            port = Integer.parseInt(ipport[1]);
        }

        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(ip, port));

            this.socket = sock;
            this.out = socket.getOutputStream();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void close() throws IOException {
        socket.close();
        out.close();
    }

    private void sendMessage(GeneratedMessage msg) throws IOException {
        byte[] data = msg.toByteArray();
        out.write(data);
    }

    public void sendAuthenticate(String username, String password) throws IOException {
        AuthenticateUser au = AuthenticateUser.newBuilder()
        .setUsername(username)
        .setPassword(password)
        .build();
        
        sendMessage(au);
    }

    public void sendCreate(int home) throws IOException {
        Create c = Create.newBuilder()
        .setHome(home)
        .build();

        sendMessage(c);
    }

    public void sendAdd(String user, int home, String section) throws IOException {
        Add a = Add.newBuilder()
        .setUser(user)
        .setHome(home)
        .setSection(section)
        .build();

        sendMessage(a);
    }

    public void sendRD(int home, String section) throws IOException {
        RD rd = RD.newBuilder()
        .setHome(home)
        .setSection(section)
        .build();

        sendMessage(rd);
    }

    public void sendEC(int home, String device, int command) throws IOException {
        EC ec = EC.newBuilder()
        .setHome(home)
        .setDevice(device)
        .setCommand(command)
        .build();

        sendMessage(ec);
    }

    public void sendRT(int home) throws IOException {
        RT rt = RT.newBuilder()
        .setHome(home)
        .build();

        sendMessage(rt);
    }

    public void sendRH(int home, String device) throws IOException {
        RH rh = RH.newBuilder()
        .setHome(home)
        .setDevice(device)
        .build();

        sendMessage(rh);
    }
}

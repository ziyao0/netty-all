package com.kissings.io.socket.bio;

import com.sun.org.apache.xpath.internal.operations.String;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangziyao
 */
@Slf4j
public class BioSocketServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            log.info("等待连接======================");
            Socket socket = serverSocket.accept();
            log.info("有客户端连接成功==============");

            new Thread(() -> {
                try {
                    handler(socket);
                } catch (IOException e) {
                    log.error("异常信息：", e);
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    private static void handler(Socket socket) throws IOException {
        log.info("线程->{}连接成功===========", Thread.currentThread().getId());
        byte[] bytes = new byte[1024];
        log.info("服务端准备读取-------");
        int read = socket.getInputStream().read();

        if (read != -1) {
            log.info("接收到的消息为：{}", new java.lang.String(bytes, 0, read));
            log.info("线程id->{}", Thread.currentThread().getId());
        }
        socket.getOutputStream().write("kinssings".getBytes());
        socket.getOutputStream().flush();
    }

}

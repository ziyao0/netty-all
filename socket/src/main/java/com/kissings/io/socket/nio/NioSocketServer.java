package com.kissings.io.socket.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangziyao
 */
@Slf4j
public class NioSocketServer {

    public static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        //创建一个socket通道
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        //配置非阻塞
        socketChannel.configureBlocking(false);
        socketChannel.socket().bind(new InetSocketAddress(8080));
        //创建selector
        Selector selector = Selector.open();
        //把channel注册到selector上 {@code SelectionKey}
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            log.info("等待时间发生====");
            selector.select();
            log.info("有事件发生-=====");
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey key = selectionKeyIterator.next();
                //删除本次已处理的key，防止下次select重复处理
                selectionKeyIterator.remove();
                pool.execute(() -> {
                    try {
                        handle(key);
                    } catch (IOException e) {
                        log.error("异常：", e);
                    }
                });
            }
        }
    }

    private static void handle(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            log.info("有客户端连接=======");
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel accept = channel.accept();
            //nio是非阻塞 ，而此处的accept方法是阻塞的但是{@code key.isAcceptable()}
            // 发生了改事件代表有连接发生所以这个方法会马上执行，不会发生阻塞
            // 而且处理完了客户端请求不会继续等待客户端的其他操作而是等待再次在执行
            accept.configureBlocking(false);
            //通过Selector监听Channel时对读事件感兴趣
            accept.register(key.selector(), SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            log.info("有客户端发送可读事件==========");
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //nio 非阻塞提现，首先read方法不会被阻塞
            //其次{@code key.isReadable()}当该条件满足时，证明有read的事件发生，所以不会阻塞
            int read = socketChannel.read(buffer);
            if (read != -1) {
                log.info("接受到的消息：{}", new String(buffer.array(), 0, read));
            }
        } else if (key.isWritable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            System.out.println("write事件");
            // NIO事件触发是水平触发
            // 使用Java的NIO编程的时候，在没有数据可以往外写的时候要取消写事件，
            // 在有数据往外写的时候再注册写事件
            key.interestOps(SelectionKey.OP_READ);
            sc.close();
        }
    }
}

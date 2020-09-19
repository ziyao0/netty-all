package com.kissings.io.netty.client;

import com.kissings.io.netty.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangziyao
 */
@Slf4j
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            log.info("netty client is start...");
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 8080).sync();
            sync.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}

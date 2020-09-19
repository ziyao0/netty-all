package com.kissings.io.netty.server;

import com.kissings.io.netty.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangziyao
 */
@Slf4j
public class NettyServer {

    @SneakyThrows
    public static void main(String[] args) {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建通道初始化对象
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //对workerGroup的socketChannel设置处理器
                            channel.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            log.info("netty server is start...");

            ChannelFuture sync = bootstrap.bind(8080).sync();
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

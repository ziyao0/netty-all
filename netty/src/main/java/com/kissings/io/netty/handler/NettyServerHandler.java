package com.kissings.io.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangziyao
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发送的数据
     *
     * @param ctx 上下文对象，含有通道{@link io.netty.channel.Channel}  管道{@link io.netty.channel.ChannelPipeline}
     * @param msg 客户端发送来的数据
     * @throws Exception 异常
     * @see io.netty.channel.Channel
     * @see io.netty.channel.ChannelPipeline
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        log.info("接受到的信息为：{}", buffer.toString(CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完毕处理的方法
     *
     * @param ctx 上下文对象，含有通道{@link io.netty.channel.Channel}  管道{@link io.netty.channel.ChannelPipeline}
     * @throws Exception 异常
     * @see io.netty.channel.Channel
     * @see io.netty.channel.ChannelPipeline
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = Unpooled.copiedBuffer("kissings".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(byteBuf);
    }
}

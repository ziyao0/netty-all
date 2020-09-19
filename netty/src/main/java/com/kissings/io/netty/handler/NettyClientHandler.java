package com.kissings.io.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 入站通道适配器
 *
 * @author zhangziyao
 * {@link ChannelInboundHandlerAdapter} to see
 * @see io.netty.channel.ChannelInboundHandler
 * @see io.netty.channel.ChannelHandler
 * @see io.netty.channel.ChannelHandlerAdapter
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * @param ctx 上下文对象，含有通道{@link io.netty.channel.Channel}  管道{@link io.netty.channel.ChannelPipeline}
     * @throws Exception 异常
     * @see io.netty.channel.Channel
     * @see io.netty.channel.ChannelPipeline
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello server".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 当通道有读取时间是触发
     *
     * @param ctx 上下文对象
     * @param msg 消息
     * @throws Exception 抛出异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        log.info("接受到的信息为：{}", buffer.toString(CharsetUtil.UTF_8));
        log.info("客户端的ip地址为：{}", ctx.channel().remoteAddress());
    }

    /**
     * @param ctx   上下文对象
     * @param cause 异常 {@link Throwable}
     * @throws Exception 抛出异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

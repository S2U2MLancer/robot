package com.s2u2m.robot.http.svr;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HttpSvrHandler extends ChannelInboundHandlerAdapter {
	private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
	private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("error cached", cause);
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpContent) {
			@Cleanup( value = "release")
			HttpContent httpContent = (HttpContent)msg;
			String content = httpContent.content().toString(CharsetUtil.UTF_8);
			log.info("receive:{}", content);
			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
				Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
			response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
			response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		}
	}
}

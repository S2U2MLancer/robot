package com.s2u2m.robot.http.svr;

import com.s2u2m.net.ISvr;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServer implements ISvr, Runnable {

	private int httpPort;

	public HttpServer(int httpPort) {
		this.httpPort = httpPort;
	}

	public void run() {
		try {
			startSvr();
		} catch (InterruptedException e) {
			log.warn(e.getMessage(), e.getCause());
			System.exit(1);
		}
	}

	public void startSvr() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.channel(NioServerSocketChannel.class)
				.childHandler(new HttpChannelInitializer());
			Channel ch = serverBootstrap.bind(httpPort).sync().channel();
			log.warn("Server is running, port:{}", httpPort);
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void stopSvr() {}
}

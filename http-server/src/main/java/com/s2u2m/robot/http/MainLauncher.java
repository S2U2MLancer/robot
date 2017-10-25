package com.s2u2m.robot.http;

import com.s2u2m.robot.http.svr.HttpServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainLauncher {
	private static HttpServer httpServer;

	public static void main(String[] args) {
		startServer();
		addShutdownHook();
	}

	private static void startServer() {
		httpServer = new HttpServer(8090);
		new Thread(httpServer, "svr-main-thread").start();
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				httpServer.stopSvr();
				log.warn("Server is shutdown");
			}
		});
	}

}

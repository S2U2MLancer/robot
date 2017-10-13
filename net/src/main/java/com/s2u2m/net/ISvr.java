package com.s2u2m.net;

import java.net.InetSocketAddress;

public interface ISvr {
	void startSvr(InetSocketAddress socketAddress) throws InterruptedException;

	void stopSvr() throws InterruptedException;
}


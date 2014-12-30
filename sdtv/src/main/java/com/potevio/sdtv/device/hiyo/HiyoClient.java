package com.potevio.sdtv.device.hiyo;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HiyoClient {
	private static final int bindPort = 21235;
	private static final Logger logger = LoggerFactory
			.getLogger(HiyoClient.class);
	private static NioSocketConnector connector = new NioSocketConnector();
	private static ConnectFuture cf;

	public static ConnectFuture connect() {
		cf = connector
				.connect(new InetSocketAddress("api.hi-yo.com", bindPort));

		cf.awaitUninterruptibly();
		return cf;
	}

	@PostConstruct
	public void dojob() {
		initConnector();
		checkSession();
	}

	private void checkSession() {
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {
					@Override
					public void run() {
						logger.info("check hiyo client.");
						try {
							if (cf == null) {
								cf = connect();
							} else {
								System.out.println(cf.isConnected());
								if(cf.isConnected()){
									System.out.println("---"+cf.getSession().isConnected());
								}
								if(!cf.isConnected()||!cf.getSession().isConnected()){
									cf.cancel();
									cf=connect();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, 1, 10, TimeUnit.SECONDS);
	}

	private void initConnector() {
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		ProtocolCodecFilter filter = new ProtocolCodecFilter(
				new HiyoCodecFactory());
		chain.addLast("hiyofilter", filter);
		connector.setHandler(new HiyoClientHandler());
		connector.setConnectTimeoutCheckInterval(30);
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
	}

	private static void check() {
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {

					@Override
					public void run() {
						if (cf == null) {
							logger.info("Connect to hiyo server.");
							cf = connect();
						} else {
							if (!cf.isConnected()) {
								logger.error("RE CONNECT HIYOServer.");
								cf.cancel();
								cf = connect();
							}
						}
					}
				}, 1, 30, TimeUnit.SECONDS);
	}

//	public static void start() {
//		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
//		ProtocolCodecFilter filter = new ProtocolCodecFilter(
//				new HiyoCodecFactory());
//		chain.addLast("hiyofilter", filter);
//		connector.setHandler(new HiyoClientHandler());
//		connector.setConnectTimeoutCheckInterval(30);
//		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
//		check();
//	}

	// @Override
	// public void run() {
	// start();
	// }
}

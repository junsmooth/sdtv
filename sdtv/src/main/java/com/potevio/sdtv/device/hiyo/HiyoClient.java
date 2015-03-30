package com.potevio.sdtv.device.hiyo;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterAdapter;
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
	// private static final int bindPort = 21235;
	private static final Logger logger = LoggerFactory
			.getLogger(HiyoClient.class);
	private static NioSocketConnector connector = new NioSocketConnector();
	// private static ConnectFuture cf;
	private static IoSession session;
	private static String host = "api.hi-yo.com";
	private static int port = 21235;

	// public static ConnectFuture connect() {
	// cf = connector
	// .connect(new InetSocketAddress("api.hi-yo.com", bindPort));
	//
	// cf.awaitUninterruptibly();
	// return cf;
	// }

	@PostConstruct
	public void start() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					initConnector();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// checkSession();
	}

	// private void checkSession() {
	// Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
	// new Runnable() {
	// @Override
	// public void run() {
	//
	// try {
	// if (cf == null) {
	// logger.info("CHECK HIYO SESSION. cf=" + cf);
	// cf = connect();
	// } else {
	// if (!cf.isConnected()
	// || !cf.getSession().isConnected()) {
	// logger.info("RECONNECT HIYOSERVER");
	// cf.cancel();
	// cf = connect();
	// }
	// }
	// logger.info("CHECK HIYO SESSION. cf=" + cf
	// + ",cf.isConnected=" + cf.isConnected()
	// + ",cf.getSession=" + cf.getSession()
	// + ",cf.getsession.isconnected="
	// + cf.getSession().isConnected());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }, 1, 60, TimeUnit.SECONDS);
	// }

	private void initConnector() {
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		chain.addFirst("reconnection", new IoFilterAdapter() {
			@Override
			public void sessionClosed(NextFilter nextFilter, IoSession ioSession)
					throws Exception {
				logger.info("INTO Session Closed Filter.");
				for (;;) {
					try {

						Thread.sleep(3000);
						logger.info("Connecting to " + host);
						ConnectFuture future = connector.connect();
						future.awaitUninterruptibly();//
						session = future.getSession();//
						if (session.isConnected()) {
							logger.info("["
									+ connector.getDefaultRemoteAddress()
											.getHostName()
									+ ":"
									+ connector.getDefaultRemoteAddress()
											.getPort() + "]");
							break;
						} else {
							logger.info("Connecion failed.");
						}
					} catch (Exception ex) {
						logger.info(""
								+ host
								+ ":"
								+ port
								+ "[]"
								+ ",,:"
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(new Date()));
					}
				}
			}
		});

		ProtocolCodecFilter filter = new ProtocolCodecFilter(
				new HiyoCodecFactory());
		chain.addLast("hiyofilter", filter);

		connector.setHandler(new HiyoClientHandler());
		connector.setConnectTimeoutCheckInterval(30);
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
		connector.getSessionConfig().setKeepAlive(true);
		connector.getSessionConfig().setWriteTimeout(10);
		connector.setDefaultRemoteAddress(new InetSocketAddress(host, port));

		for (;;) {
			try {
				ConnectFuture future = connector.connect();
				future.awaitUninterruptibly(); //
				session = future.getSession(); //
				logger.info(""
						+ host
						+ ":"
						+ port
						+ "[]"
						+ ",,:"
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(new Date()));
				break;
			} catch (Exception e) {
				logger.error(
						""
								+ host
								+ ":"
								+ port
								+ ""
								+ ",,:"
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(new Date())
								+ ", MSG,MSGIP,MSG,:" + e.getMessage(), e);
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}

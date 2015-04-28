package com.potevio.sdtv.device.hiyo;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

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


	private void initConnector() {
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		chain.addFirst("reconnection", new IoFilterAdapter() {
			@Override
			public void sessionClosed(NextFilter nextFilter, IoSession ioSession)
					throws Exception {
				logger.info("INTO Session Closed Filter.");
				for (;;) {
					try {
						logger.info("RE-Connect Sleep 30 Seconds");
						Thread.sleep(30000);
						logger.info("RE-Connecting to " + host);
						ConnectFuture future = connector.connect();
						future.awaitUninterruptibly();//
						session = future.getSession();//
						if (session.isConnected()) {
							logger.info("RE-Connect to "+host+" success.");
							break;
						} else {
							logger.info("RE-Connection failed.");
						}
					} catch (Exception ex) {
						logger.info("Re-Connect Exception");
					}
				}
			}
		});

		ProtocolCodecFilter filter = new ProtocolCodecFilter(
				new HiyoCodecFactory());
		chain.addLast("hiyofilter", filter);

		connector.setHandler(new HiyoClientHandler());
		connector.setConnectTimeoutCheckInterval(30);
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
		connector.getSessionConfig().setKeepAlive(true);
		connector.getSessionConfig().setWriteTimeout(10);
		connector.setDefaultRemoteAddress(new InetSocketAddress(host, port));

		for (;;) {
			try {
				ConnectFuture future = connector.connect();
				future.awaitUninterruptibly(); //
				session = future.getSession(); //
				logger.info("Connect Success."+session);
				break;
			} catch (Exception e) {
				logger.error("Connect Exception."+e.getMessage());
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}

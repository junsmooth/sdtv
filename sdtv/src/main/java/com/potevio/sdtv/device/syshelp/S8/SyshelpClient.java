package com.potevio.sdtv.device.syshelp.S8;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
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
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
//syshelp server is down, so close this
public class SyshelpClient {
	private static final Logger logger = LoggerFactory
			.getLogger(SyshelpClient.class);

	private static NioSocketConnector connector = new NioSocketConnector();
	private static IoSession session;
	public static String host = "27.17.44.180";
	private static int port = 8500;

	public static IoSession getSession() {
		return session;
	}

//	@PostConstruct
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
						Thread.sleep(5000);
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
						}
					} catch (Exception ex) {
						logger.error(""
								+ host
								+ ":"
								+ port
								+ ""
								+ ",,:"
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(new Date()) + ","
								+ ex.getMessage());
					}
				}
			}
		});

		chain.addLast("codec", new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8"))));
		connector.setHandler(new SyshelpClientHandler());
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
				logger.error(""
						+ host
						+ ":"
						+ port
						+ ""
						+ ",,:"
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(new Date()) + "," + e.getMessage());
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}

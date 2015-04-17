package com.potevio.sdtv.device.ythtjr.android.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AndroidServer {
	private static int PORT = 5859;
	private static Logger logger = LoggerFactory.getLogger(AndroidServer.class);

	@Autowired
	private AndroidMsgHandler handler;

	@PostConstruct
	public void start() {
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));
		acceptor.getFilterChain().addLast("threadPool",
				new ExecutorFilter(Executors.newCachedThreadPool()));
		acceptor.setHandler(handler);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 120);
		acceptor.setReuseAddress(true);
		try {
			acceptor.bind(new InetSocketAddress(PORT));
			logger.info("Android Server Bind To:" + PORT);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

}

package com.potevio.sdtv.service;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BedTimer {
	private static final int TIMEOUT_SECONDS = 20;

	/**
	 * http://182.92.169.222/zhyl/saveBedMsg.action?deviceid=2D
	 * 5EB08B-A517-8E1A-F79A-06FEE05D1031&heartrating=62&resping=102&status=0
	 */

	static {
		BedServer server = new BedServer();
		server.start();
	}

	@Scheduled(fixedDelay = 3000)
	private void sendDataToPlatform() throws Exception {
		BedMSG msg = (BedMSG) BedMsgHandler.getQueue().take();
		
		
	}

	public void fluentAPIDemo(String contentUrl) throws IOException {
		try {
			String resultString = Request.Get(contentUrl).execute()
					.returnContent().asString();

		} catch (HttpResponseException e) {
		}
	}
}

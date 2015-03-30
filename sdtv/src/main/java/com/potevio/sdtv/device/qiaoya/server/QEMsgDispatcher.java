package com.potevio.sdtv.device.qiaoya.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.potevio.sdtv.App;
import com.potevio.sdtv.device.qiaoya.server.processor.QEMsgProcessor;
import com.potevio.sdtv.device.qiaoya.server.processor.T1;

@Component
public class QEMsgDispatcher {
	private static final Logger logger = LoggerFactory
			.getLogger(QEMsgDispatcher.class);

	public void dispatch(QEClientMsg msg) {
		String msgCode = msg.getMsgCode();

		try {
			QEMsgProcessor processor = (QEMsgProcessor) App.getContext()
					.getBean(msgCode.toLowerCase());
			processor.process(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// try {
		// Class<QEMsgProcessor> cls = (Class<QEMsgProcessor>) Class
		// .forName("com.potevio.sdtv.device.qiaoya.server.processor."
		// + msgCode);
		// QEMsgProcessor processor = cls.newInstance();
		//
		// processor.process(msg);
		//
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// } catch (InstantiationException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// }

	}
}

package com.potevio.sdtv.device.qiaoya.server;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.potevio.sdtv.device.qiaoya.ResultCode;
import com.potevio.sdtv.device.qiaoya.server.processor.T10;
import com.potevio.sdtv.device.qiaoya.server.processor.T13;
import com.potevio.sdtv.device.qiaoya.server.processor.T15;
import com.potevio.sdtv.device.qiaoya.server.processor.T17;
import com.potevio.sdtv.device.qiaoya.server.processor.T19;
import com.potevio.sdtv.device.qiaoya.server.processor.T46;
import com.potevio.sdtv.device.qiaoya.server.processor.T5;
import com.potevio.sdtv.device.qiaoya.server.processor.T7;
import com.potevio.sdtv.device.qiaoya.server.processor.T8;

@Component
public class QEMsgHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(QEMsgHandler.class);
	public static final String KEY_IMEI = "key_imei";
	public static final String KEY_PACKAGE_SN = "key_package_sn";
	private static ConcurrentHashMap<String, IoSession> sessionMap = new ConcurrentHashMap<String, IoSession>();
	private static int TIME_OUT = 10000;

	@Autowired
	private QEMsgDispatcher dispatcher;

	public static IoSession getSessionByImei(String imei) {
		return sessionMap.get(imei);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		logger.info("QE <-" + message);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String msg = message.toString();

		String str = msg.replace('[', ' ').replace(']', ' ').trim();
		String[] arr = str.split(",");
		QEClientMsg qeMsg = new QEClientMsg();
		qeMsg.setVer(arr[0]);
		qeMsg.setPackSN(arr[1]);
		qeMsg.setEncryptType(Integer.parseInt(arr[2]));
		qeMsg.setEncryptValue(arr[3]);
		qeMsg.setDateTime(arr[4]);
		qeMsg.setTerminalType(arr[5]);
		qeMsg.setImei(arr[6]);
		qeMsg.setZone(arr[7]);
		qeMsg.setMsgCode(arr[8]);
		// 增加session，用以回复报文
		qeMsg.setSession(session);
		int len = arr.length;
		for (int i = 9; i < len; i++) {
			qeMsg.getParamsList().add(arr[i]);
		}

		logger.info("QE->" + qeMsg.getMsgCode() + "," + msg);
		dispatcher.dispatch(qeMsg);
		session.setAttribute(KEY_IMEI, qeMsg.getImei());
		session.setAttribute(KEY_PACKAGE_SN, qeMsg.getPackSN());
		sessionMap.put(qeMsg.getImei(), session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		if (session != null) {
			Object imeiObject = session.getAttribute(KEY_IMEI);
			if (imeiObject != null) {
				logger.info("QE-> IMEI:" + imeiObject + " SESSION CLOSED");
				sessionMap.remove(imeiObject);
			}
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
	}

	public static ResultCode sendS46Message(String imei, String status) {
		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S46", imei);
			baseMsg.getParamsList().add(status);
			session.write(baseMsg.toString());
			// wait T46
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T46.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not online");
		}

	}

	public static ResultCode sendS19Message(String imei, String params) {

		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S19", imei);
			baseMsg.getParamsList().add(params);
			session.write(baseMsg.toString());
			// wait
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T19.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not online");
		}

	}

	public static ResultCode sendS18Message(String imei, String params) {

		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S18", imei);
			baseMsg.getParamsList().add(params);
			session.write(baseMsg.toString());
			// wait
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T19.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not online");
		}

	}

	public static ResultCode sendS7Message(String imei, String time) {

		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S7", imei);
			baseMsg.getParamsList().add(time);
			session.write(baseMsg.toString());
			// wait
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T7.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not online");
		}

	}

	public static ResultCode sendS15Message(String imei, String params) {

		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S15", imei);
			baseMsg.getParamsList().add(params);
			session.write(baseMsg.toString());
			// wait
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T15.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not online");
		}

	}

	public static ResultCode sendS10Message(String imei) {

		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S10", imei);
			session.write(baseMsg.toString());
			// wait
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T10.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not online");
		}

	}

	public static ResultCode sendS17Message(String imei) {

		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S17", imei);
			session.write(baseMsg.toString());
			// wait
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T17.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not online");
		}

	}

	public static ResultCode sendS5Message(String imei) {

		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S5", imei);
			session.write(baseMsg.toString());
			// wait
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T5.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not online");
		}

	}

	private static String createReturnKey(String imei, QEBaseMsg baseMsg) {
		String pacageSN = baseMsg.getPackSN();
		String sendCode = baseMsg.getMsgCode();
		String code = sendCode.replace("S", "T");
		String keyString = imei + pacageSN + code;
		return keyString;
	}

	public static ResultCode sendS13Message(String imei) {

		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S13", imei);
			session.write(baseMsg.toString());
			// wait
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T13.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not online");
		}

	}

	public static ResultCode sendS8Message(String imei, String paramString) {

		IoSession session = getSessionByImei(imei);
		if (session != null) {
			QEBaseMsg baseMsg = Util.createBaseMsg(session, "S8", imei);
			baseMsg.getParamsList().add(paramString);
			session.write(baseMsg.toString());
			// wait
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < TIME_OUT) {
				QEClientMsg msg = T8.getAndRemoveMsg(createReturnKey(imei,
						baseMsg));
				if (msg != null) {
					return ResultCode.getSuccessMsg();
				} else {
					try {
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			return new ResultCode(false, imei + " Not Respond");
			// time out
		} else {
			return new ResultCode(false, imei + " Not Online");
		}

	}
}

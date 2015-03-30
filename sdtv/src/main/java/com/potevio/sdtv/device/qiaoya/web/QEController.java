package com.potevio.sdtv.device.qiaoya.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.potevio.sdtv.device.qiaoya.ResultCode;
import com.potevio.sdtv.device.qiaoya.server.QEMsgHandler;

@Controller
@RequestMapping(value = "QE")
public class QEController {
	private static final Logger logger = LoggerFactory
			.getLogger(QEController.class);

	/**
	 * close off SMS
	 * 
	 * @param imei
	 * @param params
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "{imei}/S46/{params}")
	public @ResponseBody ResultCode S46(@PathVariable String imei,
			@PathVariable String params, HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());
		if (!"on".equals(params) && !"off".equals(params)) {
			return new ResultCode(false,
					"URL not valid. Only on / off is allowd as params");
		}
		return QEMsgHandler.sendS46Message(imei, params);
	}

	/**
	 * 分段定时上传脉搏 Period timer send pulse pulse
	 * 
	 * @param imei
	 * @param params
	 * @param req
	 * @return
	 */
	@RequestMapping(value = { "{imei}/S8" })
	public @ResponseBody ResultCode S8(@PathVariable String imei,
			HttpServletRequest req) {
		
		String paramString=req.getParameter("param");
		if(StringUtils.isEmpty(paramString)){
			paramString="";
		}
		logger.info(req.getRequestURL().toString());
		// return QEMsgHandler.sendS46Message(imei, params);
		return QEMsgHandler.sendS8Message(imei, paramString);
	}

	/**
	 * family number
	 * 
	 * @param imei
	 * @param params
	 *            6 Numbers, with ; as delimiter
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "{imei}/S19/{params}")
	public @ResponseBody ResultCode S19(@PathVariable String imei,
			@PathVariable String params, HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());
		String[] arr = params.split(";");
		if (arr.length > 6) {
			return new ResultCode(false, "Only 6 family number allowd");
		}

		return QEMsgHandler.sendS19Message(imei, params);

	}

	/**
	 * SOS Number
	 * 
	 * @param imei
	 * @param params
	 *            3 Numbers, with ; as delimiter
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "{imei}/S18/{params}")
	public @ResponseBody ResultCode S18(@PathVariable String imei,
			@PathVariable String params, HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());

		String[] arr = params.split(";");
		if (arr.length > 3) {
			return new ResultCode(false, "Only 3 family number allowd");
		}

		return QEMsgHandler.sendS18Message(imei, params);
	}

	/**
	 * Timer send Pulse
	 * 
	 * @param imei
	 * @param params
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "{imei}/S7/{params}")
	public @ResponseBody ResultCode S7(@PathVariable String imei,
			@PathVariable String params, HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());
		int time = 180;
		try {
			time = Integer.parseInt(params);
			if (time < 180) {
				return new ResultCode(false, "param seconds need > 180");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new ResultCode(false, "param seconds need to be int");
		}

		return QEMsgHandler.sendS7Message(imei, params);
	}

	/**
	 * timer send GPS
	 * 
	 * @param imei
	 * @param params
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "{imei}/S15/{params}")
	public @ResponseBody ResultCode S15(@PathVariable String imei,
			@PathVariable String params, HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());
		int time = 300;
		try {
			time = Integer.parseInt(params);
			if (time < 300) {
				return new ResultCode(false, "param seconds need > 300");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new ResultCode(false, "param seconds need to be int");
		}

		return QEMsgHandler.sendS15Message(imei, params);
	}

	/**
	 * Close timer pulse
	 * 
	 * @param imei
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "{imei}/S10")
	public @ResponseBody ResultCode S10(@PathVariable String imei,
			HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());

		return QEMsgHandler.sendS10Message(imei);
	}

	/**
	 * Close GPS timer
	 * 
	 * @param imei
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "{imei}/S17")
	public @ResponseBody ResultCode S17(@PathVariable String imei,
			HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());

		return QEMsgHandler.sendS17Message(imei);
	}

	/**
	 * Get Pulse
	 * 
	 * @param imei
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "{imei}/S5")
	public @ResponseBody ResultCode S5(@PathVariable String imei,
			HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());

		return QEMsgHandler.sendS5Message(imei);
	}

	/**
	 * Get GPS
	 * 
	 * @param imei
	 * @param req
	 * @return
	 */

	@RequestMapping(value = "{imei}/S13")
	public @ResponseBody ResultCode S13(@PathVariable String imei,
			HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());

		return QEMsgHandler.sendS13Message(imei);
	}
}

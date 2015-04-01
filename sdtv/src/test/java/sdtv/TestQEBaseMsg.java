package sdtv;

import org.junit.Test;

import com.potevio.sdtv.device.qiaoya.server.QEBaseMsg;

public class TestQEBaseMsg {

	@Test
	public void testTostring() {
		QEBaseMsg msg = new QEBaseMsg();
		msg.getParamsList().add("110;120;130");
		System.out.println(msg.toString());
	}
}

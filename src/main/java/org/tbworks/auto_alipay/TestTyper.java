package org.tbworks.auto_alipay;

public class TestTyper {

	
	public static void main(String[] args) throws InterruptedException {
		
		// firefox
		AlipayEditInputTyper.setPassword("MozillaWindowClass", "支付宝", "mypassword");
		// chrome
		AlipayEditInputTyper.setPassword("Chrome_WidgetWin_1", "支付宝", "mypassword");
		
	}
	
}

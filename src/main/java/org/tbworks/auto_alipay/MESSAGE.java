package org.tbworks.auto_alipay;

public enum MESSAGE {

	WM_SETTEXT(0x000C),//输入文本
	WM_CHAR(0x0102), //输入字符
	BM_CLICK(0xF5),//点击事件，即按下和抬起两个动作
	KEYEVENTF_KEYUP(0x0002),//键盘按键抬起
	KEYEVENTF_KEYDOWN(0x0); //键盘按键按下
	
	private MESSAGE(int value)
	{
	   this.value = value;	
	}
	
	private int value;
	
	public String toString()
	{
		return String.valueOf(value);
	}
	
	public int toInt()
	{
		return value;
	}
}

package org.tbworks.auto_alipay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.win32.W32APIOptions;

/**
 * @author tangb Note: className means the class name of window (frame), you can
 *         get it by spy++
 */
public class AlipayEditInputTyper {

	// load the user32.dll
	final protected static User324J user32 = (User324J) Native.loadLibrary(
			"user32", User324J.class, W32APIOptions.DEFAULT_OPTIONS);

	
	/**
	 * @author tangb
	 * You can not modify a final variable in the anonymous class, but you can modify its member if the member was not final.
	 */
	private static final class finalHandleContainer
	{
		private HWND handle;
	}
	
	// -----------------------privates-------------------------------

	/**
	 * get browser frame's window handle by class name and window title
	 * 
	 * @param browserClassName
	 *            : browser window's class name, you can get it by spy++
	 * @param browserTitle
	 * @return
	 */
	private static HWND getAlipayWindowHandle(String browserClassName,
			 String browserTitle) {

		HWND deskTopHandle = user32.GetDesktopWindow();
		
		char[] winClass = new char[100];
		user32.GetClassName(deskTopHandle, winClass, 100);
		
		System.out.println(winClass);
		
		if( deskTopHandle == null )
			throw new RuntimeException("Can not find the desktop's window handle!");

		
		return getSpecifiedWindowHandle(deskTopHandle,browserClassName,browserTitle);
	}

	
	/** search the children windows of the father window specified by father-handle and find the child window with specified className and substring of title
	 * @param fatherHandle  handle of the father window
	 * @param className	 className of the child window
	 * @param title title of the child window
	 * @return
	 */
	private static HWND getSpecifiedWindowHandle(HWND fatherHandle, final String className,final String title)
	{
		final finalHandleContainer alipayWindowHandleContainer = new finalHandleContainer();
		
		boolean result = user32.EnumChildWindows(fatherHandle, new WNDENUMPROC() {
			@Override
			public boolean callback(HWND hWnd, Pointer data) {
				// TODO Auto-generated method stub
				
				char[] winClass = new char[100];
				char[] winText = new char[200];
				user32.GetClassName(hWnd, winClass, 100);
				user32.GetWindowText(hWnd, winText, 200);
				
				System.out.println("Class Name:"+Native.toString(winClass)+"   Title:"+Native.toString(winText));
				
				if(user32.IsWindowVisible(hWnd) && Native.toString(winClass).equals(className) && Native.toString(winText).contains(title))
				{
					alipayWindowHandleContainer.handle = hWnd;
					return false;//false means stop
				}
				return true; 
			}
		}, Pointer.NULL);
		
		return alipayWindowHandleContainer.handle;
	}
	

	/** get all children's class-names and handles under specified father windows, and store them in a map.
	 * @param fatherHandle the handle of father window
	 * @return a map stores every class name and its window handles, e.g., key="CLASSONE", value="12312"
	 */
	private static Map<String,List<HWND>> getChildWindowClassHandleMap(HWND fatherHandle)
	{
		final finalHandleContainer alipayWindowHandleContainer = new finalHandleContainer();
		
		final Map<String,List<HWND>> resultMap = new HashMap<String,List<HWND>>();
		
		boolean result = user32.EnumChildWindows(fatherHandle, new WNDENUMPROC() {
			@Override
			public boolean callback(HWND hWnd, Pointer data) {
				// TODO Auto-generated method stub
				
				char[] winClass = new char[100];
				user32.GetClassName(hWnd, winClass, 100);				
				if(user32.IsWindowVisible(hWnd))
				{
					String tempClassName = Native.toString(winClass);
					if(resultMap.containsKey(tempClassName))
					{
						resultMap.get(tempClassName).add(hWnd);
					}
					else
					{
						List<HWND> tempHWNDList = new ArrayList<HWND>();
						tempHWNDList.add(hWnd);
						resultMap.put(tempClassName, tempHWNDList);
					}
				}
				return true; 
			}
		}, Pointer.NULL);
		
		return resultMap;
	}

	
	/** In term of alipay password edit control, its class name changes very time. And I come up with one solution: the window class name with only one instance may be the alipay edit, others are not definitely. But times always change, so maybe this is not a permanently valid way! Cautions! 
	 * Get several potential alipay edit window. As sending messages to all the sub windows are not efficient, so it is necessary to shrink the range.
	 * @param browserWindowHandle
	 * @return
	 */
	private static List<HWND> getPotentialAlipayEditHandle(HWND browserWindowHandle) {
		
	    Map<String,List<HWND>> candidates = getChildWindowClassHandleMap(browserWindowHandle);
	    List<HWND> resultList = new ArrayList<HWND>();
	    
	    for(Iterator it =candidates.entrySet().iterator();it.hasNext();)
	    {
	    	Map.Entry<String, List<HWND>> next = (Map.Entry<String, List<HWND>>) it.next();
	    	List<HWND> tempList = next.getValue();
	    	if(tempList.size()==1)
	    	{
	    		resultList.add(tempList.get(0));
	    	}
	    }
	    	
	    return resultList;
	}
	
	
	// -----------------------publics--------------------------------
	/**
	 * Set password to all potential alipay password edit controls embedded in the browser page
	 * 
	 * @param browserClassName
	 *            : browser's class name. e.g., MozillaWindowClass for firefox
	 * @param browserTitleOrSubTitle
	 *            : browser frame's title, or you can just specified a sub-title
	 *            which can mark the alipay window uniquely. E.g., frame's title
	 *            is "123456-scs**0s90", you can just utilize "234" if it is ok.
	 * @param className
	 *            : alipay edit frame's class name.
	 * @param title
	 *            : alipay edit frame's title.
	 * @param password
	 *            : the password.
	 * @return
	 * @throws InterruptedException 
	 */
	public static boolean setPassword(String browserClassName,
			String browserTitleOrSubTitle,String password) throws InterruptedException {
		HWND browserWindowHandle = getAlipayWindowHandle(browserClassName,
				browserTitleOrSubTitle);

		System.out.println("------------------------------------------------------------------------------------------------------");
		
		List<HWND> list = getPotentialAlipayEditHandle(browserWindowHandle);

		
		for (char c : password.toCharArray()) {
			TimeUnit.MILLISECONDS.sleep(500);
			for(HWND handle:list)
				user32.SendMessage(handle, MESSAGE.WM_CHAR.toInt(), (byte) c, 0);
		}
		

		return  true;
	}
}

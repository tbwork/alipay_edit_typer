package org.tbworks.auto_alipay;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.win32.StdCallLibrary;

/**Define the exported functions provided by user32.dll
 * @author tangb 
 */
public interface User324J extends StdCallLibrary, WinUser {

	/**
	 * Retrieves a handle to a window whose class name and window name match the
	 * specified strings. The function searches child windows, beginning with
	 * the one following the specified child window. This function does not
	 * perform a case-sensitive search.
	 * 
	 * @param lpParent
	 *            : its parent's handle
	 * @param lpChild
	 *            : handle of the window before it
	 * @param lpClassName
	 *            : class name
	 * @param lpWindowName
	 *            : windows name
	 * @return return the window handle if success, or return null
	 */
	HWND FindWindowEx(HWND lpParent, HWND lpChild, String lpClassName,
			String lpWindowName);

	/**
	 * Retrieves a handle to the desktop window. The desktop window covers the
	 * entire screen. The desktop window is the area on top of which other
	 * windows are painted.
	 * 
	 * @return is a handle to the desktop window.
	 */
	HWND GetDesktopWindow();

	/**
	 * Use to send text to a edit window
	 * 
	 * @param hWnd
	 *            : the handle of the destination's window
	 * @param Msg
	 *            : the message to be sent. more can be found in :
	 *            https://msdn.microsoft
	 *            .com/en-us/library/ms644927(v=vs.85).aspx#quequed_messages
	 * @param wParam
	 *            : Additional message-specific information. In this context,
	 *            it's the char needed to be sent to the alipay edit.
	 * @param lParam
	 *            : Additional message-specific information. 
	 * @return The return value specifies the result of the message processing;
	 *         it depends on the message sent.
	 */
	int SendMessage(HWND hWnd, int Msg, int wParam, int lParam);

	/**
	 * Switches focus to the specified window and brings it to the foreground.
	 * 
	 * @param hWnd
	 *            : A handle to the window.
	 * @param fAltTab
	 *            : A TRUE for this parameter indicates that the window is being
	 *            switched to using the Alt/Ctl+Tab key sequence. This parameter
	 *            should be FALSE otherwise.
	 */
	void SwitchToThisWindow(HWND hWnd, boolean fAltTab);

	/**
	 * The EnumChildWindows function enumerates the child windows that belong to
	 * the specified parent window by passing the handle to each child window,
	 * in turn, to an application-defined callback function. EnumChildWindows
	 * continues until the last child window is enumerated or the callback
	 * function returns FALSE.
	 * 
	 * @param hWnd
	 *            Handle to the parent window whose child windows are to be
	 *            enumerated. If this parameter is NULL, this function is
	 *            equivalent to EnumWindows.
	 * @param lpEnumFunc
	 *            Pointer to an application-defined callback function.
	 * @param data
	 *            Specifies an application-defined value to be passed to the
	 *            callback function.
	 * @return If the function succeeds, the return value is nonzero. If the
	 *         function fails, the return value is zero. To get extended error
	 *         information, call GetLastError. If EnumChildProc returns zero,
	 *         the return value is also zero. In this case, the callback
	 *         function should call SetLastError to obtain a meaningful error
	 *         code to be returned to the caller of EnumChildWindows.
	 */
	boolean EnumChildWindows(HWND hWnd, WNDENUMPROC lpEnumFunc, Pointer data);

	/**
	 * This function retrieves the name of the class to which the specified
	 * window belongs.
	 * 
	 * @param hWnd
	 *            Handle to the window and, indirectly, the class to which the
	 *            window belongs.
	 * @param lpClassName
	 *            Long pointer to the buffer that is to receive the class name
	 *            string.
	 * @param nMaxCount
	 *            Specifies the length, in characters, of the buffer pointed to
	 *            by the lpClassName parameter. The class name string is
	 *            truncated if it is longer than the buffer.
	 * @return The number of characters copied to the specified buffer indicates
	 *         success. Zero indicates failure. To get extended error
	 *         information, call GetLastError.
	 */
	int GetClassName(HWND hWnd, char[] lpClassName, int nMaxCount);

	/**
	 * Determines the visibility state of the specified window.
	 *
	 * @param hWnd
	 *            A handle to the window to be tested.
	 *
	 * @return If the specified window, its parent window, its parent's parent
	 *         window, and so forth, have the WS_VISIBLE style, the return value
	 *         is nonzero. Otherwise, the return value is zero.
	 *
	 *         Because the return value specifies whether the window has the
	 *         WS_VISIBLE style, it may be nonzero even if the window is totally
	 *         obscured by other windows.
	 */
	boolean IsWindowVisible(HWND hWnd);

	
	/**
	 * This function copies the text of the specified window's title bar - if it has one - into a buffer. If 
	 * the specified window is a control, the text of the control is copied.
	 * @param hWnd
	 *  Handle to the window or control containing the text. 
	 * @param lpString
	 *  Long pointer to the buffer that will receive the text. 
	 * @param nMaxCount
	 *  Specifies the maximum number of characters to copy to the buffer, including the NULL character. 
	 *  If the text exceeds this limit, it is truncated. 
	 * @return
	 *  The length, in characters, of the copied string, not including the terminating null character, 
	 *  indicates success. Zero indicates that the window has no title bar or text, if the title bar is
	 *  empty, or if the window or control handle is invalid. To get extended error information, call 
	 *  GetLastError. This function cannot retrieve the text of an edit control in another application.
	 */
	int GetWindowText(HWND hWnd, char[] lpString, int nMaxCount);
	
	

}

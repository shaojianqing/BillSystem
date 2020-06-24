package com.sys.main;

import com.sys.ui.WinMain;

public class Application {

	public static void main(String[] args) {
		try {			
			WinMain window = new WinMain();
			window.open();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
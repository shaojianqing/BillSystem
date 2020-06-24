package com.sys.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class InfoBox extends Shell {

	private Rectangle screenBound;
	
	private Label infoLbl;
	
	private Button confirmBtn;
	
	public static void showInfo(Display display, String info) { 
		InfoBox infoBox = new InfoBox(display);
		infoBox.infoLbl.setText(info);
		infoBox.open();
	}

	private InfoBox(Display display) {
		super(display, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setImage(SWTResourceManager.getImage(InfoBox.class, "/res/images/ico_info_small.png"));
		
		infoLbl = new Label(this, SWT.NONE);
		infoLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		infoLbl.setAlignment(SWT.CENTER);
		infoLbl.setBounds(80, 50, 200, 70);
		
		confirmBtn = new Button(this, SWT.NONE);
		confirmBtn.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		confirmBtn.setBounds(100, 126, 160, 36);
		confirmBtn.setText("\u786E    \u5B9A");
		
		Label icoLbl = new Label(this, SWT.NONE);
		icoLbl.setBounds(38, 46, 36, 36);
		icoLbl.setImage(SWTResourceManager.getImage(InfoBox.class, "/res/images/ico_info_large.png"));
		
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InfoBox.this.close();
			}
		});
		
		screenBound = display.getPrimaryMonitor().getBounds();
		createContents();
	}

	protected void createContents() {
		setSize(360, 200);
		setBounds((screenBound.width-360)/2, (screenBound.height-200)/2, 360, 200);
	}

	@Override
	protected void checkSubclass() {
	}
}

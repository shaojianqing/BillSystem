package com.sys.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class WinInfo extends Shell {

	private Rectangle screenBound;
	
	private Button confirmBtn;
	
	public WinInfo(Display display) {
		super(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setSize(500, 352);
		
		confirmBtn = new Button(this, SWT.NONE);
		confirmBtn.setText("\u786E    \u5B9A");
		confirmBtn.setBounds(376, 326, 108, 36);
		
		Label titleLbl = new Label(this, SWT.NONE);
		titleLbl.setAlignment(SWT.CENTER);
		titleLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 16, SWT.NORMAL));
		titleLbl.setBounds(100, 35, 300, 28);
		titleLbl.setText("\u7269\u6D41\u5355\u636E\u7BA1\u7406\u7CFB\u7EDF");
		
		Label authorLbl = new Label(this, SWT.NONE);
		authorLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		authorLbl.setBounds(50, 90, 75, 20);
		authorLbl.setText("\u4F5C\u8005\uFF1A");
		
		Label label = new Label(this, SWT.NONE);
		label.setText("\u624B\u673A\uFF1A");
		label.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label.setBounds(50, 130, 75, 20);
		
		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("\u90AE\u7BB1\uFF1A");
		label_1.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_1.setBounds(49, 170, 75, 20);
		
		Label lblQq = new Label(this, SWT.NONE);
		lblQq.setText("QQ\uFF1A");
		lblQq.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		lblQq.setBounds(49, 210, 75, 20);
		
		Label label_2 = new Label(this, SWT.NONE);
		label_2.setText("\u90B5\u5EFA\u9752");
		label_2.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_2.setBounds(131, 90, 300, 20);
		
		Label label_3 = new Label(this, SWT.NONE);
		label_3.setText("18268017588");
		label_3.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_3.setBounds(131, 130, 300, 20);
		
		Label lblShaojianqingcom = new Label(this, SWT.NONE);
		lblShaojianqingcom.setText("shao_jian_qing@126.com");
		lblShaojianqingcom.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		lblShaojianqingcom.setBounds(131, 170, 300, 20);
		
		Label label_5 = new Label(this, SWT.NONE);
		label_5.setText("282411794");
		label_5.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_5.setBounds(131, 210, 300, 20);
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinInfo.this.close();
			}
		});
		screenBound = display.getPrimaryMonitor().getBounds();
		createContents();
	}

	protected void createContents() {
		setText("Èí¼þ°æ±¾ÐÅÏ¢");
		setBounds((screenBound.width-500)/2, (screenBound.height-400)/2, 500, 400);
	}

	@Override
	protected void checkSubclass() {
		
	}
}
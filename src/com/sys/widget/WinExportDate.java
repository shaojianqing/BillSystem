package com.sys.widget;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class WinExportDate extends Shell 
{	
	private IExportClient client;
	
	private Display display;
	
	private Button cancelBtn;
	
	private Button confirmBtn;
	
	private DateTime startDate;
	
	private DateTime endDate;
	
	private Rectangle screenBound;
	
	public WinExportDate(Display display, IExportClient client, String title) {
		super(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		
		this.client = client;
		this.display = display;
		Label titleLbl = new Label(this, SWT.NONE);
		titleLbl.setFont(SWTResourceManager.getFont("微软雅黑", 18, SWT.NORMAL));
		titleLbl.setAlignment(SWT.CENTER);
		titleLbl.setBounds(75, 24, 300, 50);
		titleLbl.setText(title);
		
		cancelBtn = new Button(this, SWT.NONE);
		cancelBtn.setBounds(58, 205, 120, 44);
		cancelBtn.setText("\u53D6\u6D88");
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinExportDate.this.close();
			}
		});	
		
		confirmBtn = new Button(this, SWT.NONE);
		confirmBtn.setText("\u5BFC\u51FA");
		confirmBtn.setBounds(255, 205, 120, 44);
		
		Label startLbl = new Label(this, SWT.NONE);
		startLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		startLbl.setBounds(24, 80, 70, 24);
		startLbl.setText("\u5F00\u59CB\u65E5\u671F");
		
		Label endLbl = new Label(this, SWT.NONE);
		endLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		endLbl.setText("\u7ED3\u675F\u65E5\u671F");
		endLbl.setBounds(24, 120, 70, 24);
		
		startDate = new DateTime(this, SWT.BORDER);
		startDate.setBounds(100, 80, 240, 24);
		
		endDate = new DateTime(this, SWT.BORDER);
		endDate.setBounds(100, 120, 240, 24);
		
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinExportDate.this.selectExportDate();
			}
		});	
		
		screenBound = display.getPrimaryMonitor().getBounds();
		createContents();
	}

	protected void createContents() {
		this.setText("\u5BFC\u51FA\u7269\u6D41\u5355\u636E");
		this.setBounds((screenBound.width-450)/2, (screenBound.height-300)/2, 450, 300);
	}

	@Override
	protected void checkSubclass() {
	}
	
	private void selectExportDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.startDate.getYear(), this.startDate.getMonth(), this.startDate.getDay());		
		Date startDate = calendar.getTime();
		calendar = Calendar.getInstance();
		calendar.set(this.endDate.getYear(), this.endDate.getMonth(), this.endDate.getDay());
		Date endDate = calendar.getTime();
		
		if (!startDate.after(endDate)) {
			this.client.selectExportDate(startDate, endDate);
		} else {
			InfoBox.showInfo(display, "开始日期不能晚于结束日期哦^_^");
		}	
	}
}

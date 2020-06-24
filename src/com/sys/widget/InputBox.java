package com.sys.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sys.util.StringUtil;

public class InputBox extends Shell {
	
	private Rectangle screenBound;
	
	private StyledText inputTxt;
	
	private IInputClient client;
	
	private Label titleLbl;
	
	private Button cancelBtn;
	
	private Button confirmBtn;
	
	private Label infoLbl;
	
	public static void showInput(Display display, String title, String input, IInputClient client) 
	{ 
		InputBox infoBox = new InputBox(display, client);
		infoBox.titleLbl.setText(title);
		infoBox.inputTxt.setText(input);
		infoBox.open();
	}

	private InputBox(Display display, IInputClient inputClient)
	{
		super(display, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setImage(SWTResourceManager.getImage(MessageBox.class, "/res/images/ico_info_small.png"));
		
		client = inputClient;
		titleLbl = new Label(this, SWT.NONE);
		titleLbl.setText("\u8BF7\u8F93\u5165\u5907\u6CE8");
		titleLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		titleLbl.setAlignment(SWT.CENTER);
		titleLbl.setBounds(80, 10, 200, 27);
		
		inputTxt = new StyledText(this, SWT.BORDER | SWT.WRAP);
		inputTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		inputTxt.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 16, SWT.NORMAL));
		inputTxt.setBounds(20, 43, 310, 66);
		
		cancelBtn = new Button(this, SWT.NONE);
		cancelBtn.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		cancelBtn.setBounds(20, 156, 120, 36);
		cancelBtn.setText("\u53D6    \u6D88");
		
		confirmBtn = new Button(this, SWT.NONE);
		confirmBtn.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		confirmBtn.setBounds(210, 156, 120, 36);
		confirmBtn.setText("\u786E    \u5B9A");
		
		infoLbl = new Label(this, SWT.NONE);
		infoLbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		infoLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 10, SWT.NORMAL));
		infoLbl.setBounds(20, 123, 200, 20);
		
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (client!=null) {
					String content = inputTxt.getText();
					if (StringUtil.isNotBlank(content)) {
						client.inputContent(content);
						InputBox.this.close();
					} else {
						infoLbl.setText("ÊäÈë²»ÄÜÎª¿ÕÅ¶");
					}
				}
			}
		});
		
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputBox.this.close();
			}
		});
		
		screenBound = display.getPrimaryMonitor().getBounds();
		createContents();
	}

	protected void createContents() {
		setSize(360, 223);
		setBounds((screenBound.width-360)/2, (screenBound.height-230)/2, 360, 230);
	}

	@Override
	protected void checkSubclass() {
	}
}

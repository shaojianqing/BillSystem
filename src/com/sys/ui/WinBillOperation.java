package com.sys.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sys.bo.Region;
import com.sys.bo.UserInfo;
import com.sys.constant.KeyConstants;
import com.sys.service.BillService;
import com.sys.service.CustomerService;
import com.sys.service.RegionService;
import com.sys.service.UserService;
import com.sys.ui.data.CustLinkContProvider;
import com.sys.ui.data.CustLinkLabelProvider;
import com.sys.util.ConstUtil;
import com.sys.util.StringUtil;
import com.sys.vo.CustomerLink;

public abstract class WinBillOperation extends Shell {

	protected Display display;

	protected BillService billService;
	
	protected UserService userService;
	
	protected RegionService regionService;
	
	protected CustomerService customerService;
	
	protected Rectangle screenBound;
	
	protected Label codeLbl;
	
	protected Label infoTipLbl;
	
	protected DateTime dateTime;
	
	protected StyledText senderTxt;
	
	protected StyledText senderCertNoTxt;
	
	protected StyledText receiverTxt;
	
	protected StyledText senderPhoneTxt;
	
	protected StyledText receiverPhoneTxt;
	
	protected StyledText productNameTxt;
	
	protected StyledText packageTxt;
	
	protected StyledText numberTxt;
	
	protected StyledText priceFeeTxt;
	
	protected StyledText bargeFeeTxt;
	
	protected StyledText totalFeeTxt;
	
	protected StyledText deliverFeeTxt;
	
	protected CCombo senderSignerCmb;
	
	protected CCombo backBillNumCmb;
	
	protected CCombo destCmb;
	
	protected Button confirmBtn;
	
	protected Button cancelBtn;
	
	protected Button selfGetBtn;	
	
	protected Button sendProdBtn;	
	
	protected Button getPayBtn;	
	
	protected Button cashPayBtn;	
	
	protected Button backPayBtn;	
	
	protected Button backBillBtn;
	
	protected TableViewer custGrid;
	
	protected Table custTable;
	
	protected List<UserInfo> userInfoList;	
	
	protected String[] userNameList;
	
	protected String[] userIdList;
	
	protected List<Region> regionList;
	
	protected String[] regionNameList;
	
	protected String[] regionIdList;
	
	protected StyledText currentInput;
	
	protected boolean isDataInited = false;
	
	public WinBillOperation(Display displayP) {
		super(displayP, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		setSize(800, 640);
		setImage(SWTResourceManager.getImage(WinBillNew.class, "/res/images/ico_win_new_bill.png"));
		
		this.display = displayP;
		this.billService = BillService.getInstance();
		this.userService = UserService.getInstance();
		this.regionService = RegionService.getInstance();
		this.customerService = CustomerService.getInstance();
		this.userInfoList = userService.getUserInfoByQuery();
		this.regionList = regionService.getRegionByQuery();
		
		this.initTitleView();
		this.initMainView();
		this.initViewEvent();
	}

	protected void initTitleView() {
		screenBound = display.getPrimaryMonitor().getBounds();
	}
	
	protected void initMainView() {
		
		custGrid = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		custGrid.setContentProvider(new CustLinkContProvider());
		custGrid.setLabelProvider(new CustLinkLabelProvider());
		custTable = custGrid.getTable();
		custTable.setLinesVisible(true);
		custTable.setHeaderVisible(true);
		custTable.setVisible(false);
		custTable.setBounds(111, 191, 369, 176);
		
		TableColumn nameColumn = new TableColumn(custTable, SWT.NONE);
		nameColumn.setWidth(200);
		nameColumn.setText("客户名称");
		nameColumn.setResizable(false);
		
		TableColumn phoneColumn = new TableColumn(custTable, SWT.NONE);
		phoneColumn.setWidth(140);
		phoneColumn.setText("电话号码");
		phoneColumn.setResizable(false);
		
		Label codeSignLbl = new Label(this, SWT.NONE);
		codeSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		codeSignLbl.setBounds(580, 44, 50, 20);
		codeSignLbl.setText("\u7F16\u53F7\uFF1A");
		
		codeLbl = new Label(this, SWT.NONE);
		codeLbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		codeLbl.setFont(SWTResourceManager.getFont("微软雅黑", 22, SWT.BOLD));
		codeLbl.setBounds(630, 32, 144, 36);
		
		Label hline1 = new Label(this, SWT.NONE);
		hline1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline1.setBounds(14, 110, 760, 1);
		
		Label hline2 = new Label(this, SWT.NONE);
		hline2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline2.setBounds(14, 190, 760, 1);
		
		Label hline3 = new Label(this, SWT.NONE);
		hline3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline3.setBounds(14, 230, 760, 1);
		
		Label hline4 = new Label(this, SWT.NONE);
		hline4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline4.setBounds(14, 267, 760, 1);
		
		Label hline5 = new Label(this, SWT.NONE);
		hline5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline5.setBounds(15, 415, 574, 1);
		
		Label hline6 = new Label(this, SWT.NONE);
		hline6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline6.setBounds(14, 452, 574, 1);
		
		Label hline7 = new Label(this, SWT.NONE);
		hline7.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline7.setBounds(14, 488, 760, 1);
		
		Label hline8 = new Label(this, SWT.NONE);
		hline8.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline8.setBounds(14, 528, 760, 1);
		
		Label hline9 = new Label(this, SWT.NONE);
		hline9.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline9.setBounds(588, 304, 186, 1);
		
		Label hine10 = new Label(this, SWT.NONE);
		hine10.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hine10.setBounds(588, 341, 186, 1);
		
		Label hline11 = new Label(this, SWT.NONE);
		hline11.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline11.setBounds(588, 378, 186, 1);
		
		Label hline12 = new Label(this, SWT.NONE);
		hline12.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline12.setBounds(588, 415, 186, 1);
		
		Label hline13 = new Label(this, SWT.NONE);
		hline13.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline13.setBounds(588, 452, 186, 1);
		
		Label hline14 = new Label(this, SWT.BORDER | SWT.SEPARATOR | SWT.WRAP);
		hline14.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		hline14.setBounds(15, 341, 576, 1);
		
		Label hline15 = new Label(this, SWT.NONE);
		hline15.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline15.setBounds(14, 150, 760, 1);
		
		Label vline1 = new Label(this, SWT.NONE);
		vline1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline1.setBounds(14, 110, 1, 419);
		
		Label vline2 = new Label(this, SWT.NONE);
		vline2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline2.setBounds(774, 110, 1, 419);
		
		Label vline3 = new Label(this, SWT.NONE);
		vline3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline3.setBounds(110, 110, 1, 120);
		
		Label vline4 = new Label(this, SWT.NONE);
		vline4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline4.setBounds(480, 110, 1, 120);
		
		Label vline5 = new Label(this, SWT.NONE);
		vline5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline5.setBounds(560, 110, 1, 120);
		
		Label vline6 = new Label(this, SWT.NONE);
		vline6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline6.setBounds(220, 230, 1, 185);
		
		Label vline7 = new Label(this, SWT.NONE);
		vline7.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline7.setBounds(301, 230, 1, 185);
		
		Label vline8 = new Label(this, SWT.NONE);
		vline8.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline8.setBounds(382, 230, 1, 185);
		
		Label vline9 = new Label(this, SWT.NONE);
		vline9.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline9.setBounds(480, 230, 1, 185);
		
		Label vline10 = new Label(this, SWT.NONE);
		vline10.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline10.setBounds(588, 230, 1, 259);
		
		Label vline11 = new Label(this, SWT.NONE);
		vline11.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline11.setBounds(676, 230, 1, 259);
		
		Label vline12 = new Label(this, SWT.NONE);
		vline12.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline12.setBounds(130, 415, 1, 114);
		
		Label vline13 = new Label(this, SWT.NONE);
		vline13.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline13.setBounds(270, 488, 1, 40);
		
		Label vline14 = new Label(this, SWT.NONE);
		vline14.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline14.setBounds(388, 488, 1, 40);
		
		Label vline15 = new Label(this, SWT.NONE);
		vline15.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline15.setBounds(528, 488, 1, 40);
		
		Label vline16 = new Label(this, SWT.NONE);
		vline16.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline16.setBounds(605, 489, 1, 40);
		
		Label dateSignLbl = new Label(this, SWT.NONE);
		dateSignLbl.setText("\u65E5\u671F\uFF1A");
		dateSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		dateSignLbl.setBounds(14, 81, 40, 20);
		
		Label sendSignLbl = new Label(this, SWT.NONE);
		sendSignLbl.setAlignment(SWT.CENTER);
		sendSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		sendSignLbl.setBounds(22, 117, 80, 28);
		sendSignLbl.setText("\u53D1\u8D27\u5355\u4F4D");
		
		Label senderCertNoLbl = new Label(this, SWT.NONE);
		senderCertNoLbl.setAlignment(SWT.CENTER);
		senderCertNoLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		senderCertNoLbl.setBounds(22, 157, 80, 28);
		senderCertNoLbl.setText("\u8bc1\u4ef6\u53f7\u7801");
		
		Label receiverSignLbl = new Label(this, SWT.NONE);
		receiverSignLbl.setText("\u6536\u8D27\u5355\u4F4D");
		receiverSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		receiverSignLbl.setAlignment(SWT.CENTER);
		receiverSignLbl.setBounds(22, 197, 80, 28);
		
		Label senderPhoneSignLbl = new Label(this, SWT.NONE);
		senderPhoneSignLbl.setText("\u7535\u8BDD");
		senderPhoneSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		senderPhoneSignLbl.setAlignment(SWT.CENTER);
		senderPhoneSignLbl.setBounds(490, 117, 60, 28);
		
		Label receiverPhoneSignLbl = new Label(this, SWT.NONE);
		receiverPhoneSignLbl.setText("\u7535\u8BDD");
		receiverPhoneSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		receiverPhoneSignLbl.setAlignment(SWT.CENTER);
		receiverPhoneSignLbl.setBounds(490, 197, 60, 28);
		
		Label productNameSignLbl = new Label(this, SWT.NONE);
		productNameSignLbl.setText("\u8D27\u7269\u540D\u79F0");
		productNameSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		productNameSignLbl.setAlignment(SWT.CENTER);
		productNameSignLbl.setBounds(71, 236, 80, 28);
		
		Label packageSignLbl = new Label(this, SWT.NONE);
		packageSignLbl.setText("\u5305\u88C5");
		packageSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		packageSignLbl.setAlignment(SWT.CENTER);
		packageSignLbl.setBounds(231, 236, 60, 28);
		
		Label numSignLbl = new Label(this, SWT.NONE);
		numSignLbl.setText("\u4EF6\u6570");
		numSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		numSignLbl.setAlignment(SWT.CENTER);
		numSignLbl.setBounds(310, 236, 60, 28);
		
		Label priceFeeSignLbl = new Label(this, SWT.NONE);
		priceFeeSignLbl.setText("\u4FDD\u4EF7\u8D394%");
		priceFeeSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		priceFeeSignLbl.setAlignment(SWT.CENTER);
		priceFeeSignLbl.setBounds(384, 236, 94, 28);
		
		Label changeFeeSignLbl = new Label(this, SWT.NONE);
		changeFeeSignLbl.setText("\u5E02\u533A\u77ED\u9A73\u8D39");
		changeFeeSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		changeFeeSignLbl.setAlignment(SWT.CENTER);
		changeFeeSignLbl.setBounds(482, 236, 104, 28);
		
		Label selfGetSignLbl = new Label(this, SWT.NONE);
		selfGetSignLbl.setText("\u81EA  \u63D0");
		selfGetSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		selfGetSignLbl.setAlignment(SWT.CENTER);
		selfGetSignLbl.setBounds(602, 236, 60, 28);
		
		Label sendProdSignLbl = new Label(this, SWT.NONE);
		sendProdSignLbl.setText("\u9001  \u8D27");
		sendProdSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		sendProdSignLbl.setAlignment(SWT.CENTER);
		sendProdSignLbl.setBounds(602, 273, 60, 28);
		
		Label getPaySignLbl = new Label(this, SWT.NONE);
		getPaySignLbl.setText("\u63D0  \u4ED8");
		getPaySignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		getPaySignLbl.setAlignment(SWT.CENTER);
		getPaySignLbl.setBounds(602, 310, 60, 28);
		
		Label cashPaySignLbl = new Label(this, SWT.NONE);
		cashPaySignLbl.setAlignment(SWT.CENTER);
		cashPaySignLbl.setText("\u73B0  \u4ED8");
		cashPaySignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		cashPaySignLbl.setBounds(602, 347, 60, 28);
		
		Label backPaySignLbl = new Label(this, SWT.NONE);
		backPaySignLbl.setText("\u56DE  \u7ED3");
		backPaySignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		backPaySignLbl.setAlignment(SWT.CENTER);
		backPaySignLbl.setBounds(602, 384, 60, 28);
		
		Label backBillSignTxt = new Label(this, SWT.NONE);
		backBillSignTxt.setText("\u7B7E\u56DE\u5355");
		backBillSignTxt.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		backBillSignTxt.setAlignment(SWT.CENTER);
		backBillSignTxt.setBounds(602, 421, 60, 28);
		
		Label totalFeeSignLbl = new Label(this, SWT.NONE);
		totalFeeSignLbl.setText("\u5408\u8BA1\u8FD0\u8D39");
		totalFeeSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		totalFeeSignLbl.setAlignment(SWT.CENTER);
		totalFeeSignLbl.setBounds(22, 419, 80, 28);
		
		Label productFeeSignLbl = new Label(this, SWT.NONE);
		productFeeSignLbl.setText("\u4EE3\u6536\u8D27\u6B3E");
		productFeeSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		productFeeSignLbl.setAlignment(SWT.CENTER);
		productFeeSignLbl.setBounds(22, 456, 80, 28);
		
		Label carrierSignLbl = new Label(this, SWT.NONE);
		carrierSignLbl.setText("\u6258\u8FD0\u4EBA\u7B7E\u5B57");
		carrierSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		carrierSignLbl.setAlignment(SWT.CENTER);
		carrierSignLbl.setBounds(21, 494, 106, 28);
		
		Label processorSignLbl = new Label(this, SWT.NONE);
		processorSignLbl.setText("\u53D7\u7406\u4EBA\u7B7E\u5B57");
		processorSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		processorSignLbl.setAlignment(SWT.CENTER);
		processorSignLbl.setBounds(277, 494, 106, 28);
		
		Label senderSignLbl = new Label(this, SWT.NONE);
		senderSignLbl.setText("\u7B7E\u5B57\u4EBA");
		senderSignLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		senderSignLbl.setAlignment(SWT.CENTER);
		senderSignLbl.setBounds(532, 495, 70, 28);
		
		dateTime = new DateTime(this, SWT.BORDER);
		dateTime.setForeground(SWTResourceManager.getColor(0, 153, 255));
		dateTime.setBounds(63, 80, 100, 24);
		
		infoTipLbl = new Label(this, SWT.NONE);
		infoTipLbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		infoTipLbl.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		infoTipLbl.setBounds(14, 535, 429, 34);
	
		senderTxt = new StyledText(this, SWT.NONE);
		senderTxt.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		senderTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		senderTxt.setBounds(111, 111, 369, 39);
		senderTxt.setToolTipText("请输入名字或简拼！");
		
		senderCertNoTxt = new StyledText(this, SWT.NONE);
		senderCertNoTxt.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		senderCertNoTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		senderCertNoTxt.setBounds(111, 151, 369, 39);
		senderCertNoTxt.setToolTipText("请输入证件号码！");
		
		receiverTxt = new StyledText(this, SWT.NONE);
		receiverTxt.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		receiverTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		receiverTxt.setBounds(111, 191, 369, 39);
		receiverTxt.setToolTipText("请输入名字或简拼！");
		
		senderPhoneTxt = new StyledText(this, SWT.WRAP);
		senderPhoneTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		senderPhoneTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		senderPhoneTxt.setBounds(561, 111, 213, 39);
		
		receiverPhoneTxt = new StyledText(this, SWT.WRAP);
		receiverPhoneTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		receiverPhoneTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		receiverPhoneTxt.setBounds(561, 191, 213, 39);
		
		productNameTxt = new StyledText(this, SWT.WRAP);
		productNameTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		productNameTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		productNameTxt.setBounds(15, 268, 205, 146);
		
		packageTxt = new StyledText(this, SWT.WRAP);
		packageTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		packageTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		packageTxt.setBounds(221, 268, 80, 146);
		
		numberTxt = new StyledText(this, SWT.WRAP);
		numberTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		numberTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		numberTxt.setBounds(302, 268, 80, 146);
		
		priceFeeTxt = new StyledText(this, SWT.WRAP);
		priceFeeTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		priceFeeTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		priceFeeTxt.setBounds(384, 268, 95, 146);
		
		bargeFeeTxt = new StyledText(this, SWT.WRAP);
		bargeFeeTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		bargeFeeTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		bargeFeeTxt.setBounds(482, 268, 106, 146);
		
		totalFeeTxt = new StyledText(this, SWT.WRAP);
		totalFeeTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		totalFeeTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		totalFeeTxt.setBounds(131, 416, 457, 35);
		
		deliverFeeTxt = new StyledText(this, SWT.WRAP);
		deliverFeeTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		deliverFeeTxt.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		deliverFeeTxt.setBounds(131, 453, 457, 34);
		
		userNameList = userService.getUserNameList(userInfoList);
		userIdList = userService.getUserIdList(userInfoList);
		
		senderSignerCmb = new CCombo(this, SWT.BORDER);
		senderSignerCmb.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		senderSignerCmb.setBounds(606, 489, 168, 39);
		senderSignerCmb.setItems(userNameList);
		senderSignerCmb.select(0);
		
		selfGetBtn = new Button(this, SWT.CHECK);
		selfGetBtn.setBounds(698, 241, 13, 17);	
		
		sendProdBtn = new Button(this, SWT.CHECK);
		sendProdBtn.setBounds(698, 277, 13, 17);
		
		getPayBtn = new Button(this, SWT.CHECK);
		getPayBtn.setBounds(698, 313, 13, 17);
		
		cashPayBtn = new Button(this, SWT.CHECK);
		cashPayBtn.setBounds(698, 349, 13, 17);
		
		backPayBtn = new Button(this, SWT.CHECK);
		backPayBtn.setBounds(698, 385, 13, 17);
		
		backBillBtn = new Button(this, SWT.CHECK);
		backBillBtn.setBounds(698, 421, 13, 17);
		
		confirmBtn = new Button(this, SWT.NONE);
		confirmBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		confirmBtn.setBounds(636, 545, 140, 44);
		confirmBtn.setText("\u786E\u5B9A");
		confirmBtn.setImage(SWTResourceManager.getImage(WinBillNew.class, "/res/images/icon_confirm.png"));	
		
		cancelBtn = new Button(this, SWT.NONE);
		cancelBtn.setAlignment(SWT.LEFT);
		cancelBtn.setImage(SWTResourceManager.getImage(WinBillNew.class, "/res/images/icon_cancel.png"));
		cancelBtn.setText("\u53D6\u6D88");
		cancelBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		cancelBtn.setBounds(466, 545, 140, 44);	
		
		Label label = new Label(this, SWT.NONE);
		label.setText("\u5230\u7AD9\uFF1A");
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setBounds(560, 81, 40, 20);
		
		backBillNumCmb = new CCombo(this, SWT.BORDER);
		backBillNumCmb.setBounds(714, 420, 58, 25);
		
		Label defaultDestLbl = new Label(this, SWT.RIGHT);
		defaultDestLbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		defaultDestLbl.setText("\u5BCC\u9633");
		defaultDestLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		defaultDestLbl.setBounds(500, 81, 50, 20);
		
		backBillNumCmb.setVisible(false);
		backBillNumCmb.setItems(ConstUtil.BILL_NUM);
		
		regionNameList = regionService.getRegionNameList(regionList);
		regionIdList = regionService.getRegionIdList(regionList);
		
		destCmb = new CCombo(this, SWT.BORDER);
		destCmb.setItems(new String[] {"\u8BF7\u9009\u62E9"});
		destCmb.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		destCmb.setBounds(606, 75, 168, 28);
		destCmb.setItems(regionNameList);
		destCmb.select(0);
	}
	
	protected void initViewEvent() {
		custTable.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.width = custTable.getGridLineWidth();
				event.height = 24;
			}
		});
		custTable.addListener(SWT.MouseDown, new Listener() {  
            public void handleEvent(Event event) {
            	try {
            		if (custTable.getItems().length>0) {
    	                int index = custTable.getSelectionIndex();
    	                TableItem item = custTable.getItem(index);
    	                CustomerLink customerLink = (CustomerLink)item.getData();
    	                currentInput.setText(customerLink.getCustomerName());
    	                if (currentInput==senderTxt) {
    	                	senderPhoneTxt.setText(customerLink.getCustomerPhone());
    	                	senderCertNoTxt.setText(StringUtil.trimString(customerLink.getCustomerCertNo()));
    	                } else if (currentInput==receiverTxt) {    	                	
    	                	receiverPhoneTxt.setText(customerLink.getCustomerPhone());
    	                }
    	                custTable.setVisible(false);
                	}
				} catch (Exception e) {					
				}
            }  
        });
		
		senderTxt.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				if (isDataInited) {
					currentInput = senderTxt;
					String sender = StringUtil.trimString(senderTxt.getText());
					prepareSenderInfo(sender);
				}
			}
		});
		
		senderTxt.addKeyListener(new KeyListener() {			
			@Override
			public void keyReleased(KeyEvent event) {
				if (event.keyCode==KeyConstants.KEY_CODE_ENTER) {					
					TableItem items[] = custTable.getSelection();
					if (items !=null && items.length>0) {
						TableItem item = items[0];
						CustomerLink customerLink = (CustomerLink)item.getData();
						currentInput.setText(customerLink.getCustomerName());
		                senderPhoneTxt.setText(customerLink.getCustomerPhone());
		                custTable.setVisible(false);
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent event) {
			}
		});
		
		receiverTxt.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				if (isDataInited) {
					currentInput = receiverTxt;
					String receiver = StringUtil.trimString(receiverTxt.getText());
					prepareReceiverInfo(receiver);
				}
			}
		});
		
		receiverTxt.addKeyListener(new KeyListener() {			
			@Override
			public void keyReleased(KeyEvent event) {
				if (event.keyCode==KeyConstants.KEY_CODE_ENTER) {					
					TableItem items[] = custTable.getSelection();
					if (items !=null && items.length>0) {
						TableItem item = items[0];
						CustomerLink customerLink = (CustomerLink)item.getData();
						currentInput.setText(customerLink.getCustomerName());
		                receiverPhoneTxt.setText(customerLink.getCustomerPhone());
		                custTable.setVisible(false);
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent event) {
			}
		});
		
		selfGetBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selfGetBtn.getSelection()) {
					sendProdBtn.setSelection(false);
				}
			}
		});
		
		sendProdBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (sendProdBtn.getSelection()) {
					selfGetBtn.setSelection(false);
				}
			}
		});
		
		getPayBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (getPayBtn.getSelection()) {
					cashPayBtn.setSelection(false);
					backPayBtn.setSelection(false);
					backBillBtn.setSelection(false);
					backBillNumCmb.setVisible(false);
				}
			}
		});
		
		cashPayBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cashPayBtn.getSelection()) {
					getPayBtn.setSelection(false);
					backPayBtn.setSelection(false);
					backBillBtn.setSelection(false);
					backBillNumCmb.setVisible(false);
				}
			}
		});
		
		backPayBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (backPayBtn.getSelection()) {
					getPayBtn.setSelection(false);
					cashPayBtn.setSelection(false);
				} else {
					backBillBtn.setSelection(false);
					backBillNumCmb.setVisible(false);					
				}
			}
		});
		
		backBillBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (backBillBtn.getSelection()) {
					backBillNumCmb.setVisible(true);
					backBillNumCmb.select(0);
				} else {
					backBillNumCmb.setVisible(false);
				}
			}
		});
		
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {				
				confirmOperation();
			}
		});
		
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cancelOperation();
			}
		});
	}
	
	protected void prepareSenderInfo(String sender) {
		this.senderPhoneTxt.setText("");
		if (StringUtil.isNotBlank(sender)) {			
			this.custTable.setVisible(true);
			this.custTable.setBounds(111, 151, 369, 176);
			List<CustomerLink> customerLinks = customerService.getCustomerLinkByKeyword(sender);
			this.custGrid.setInput(customerLinks);
			if (customerLinks!=null && customerLinks.size()>0) {
				this.custGrid.setInput(customerLinks);
				this.custTable.setSelection(0);
			} else {
				this.custGrid.setInput(null);
				this.custTable.setVisible(false);
			}
		} else {
			this.custTable.setVisible(false);
		}
	}
	
	protected void prepareReceiverInfo(String receiver) {
		this.receiverPhoneTxt.setText("");
		if (StringUtil.isNotBlank(receiver)) {			
			this.custTable.setVisible(true);
			this.custTable.setBounds(111, 230, 369, 176);
			List<CustomerLink> customerLinks = customerService.getCustomerLinkByKeyword(receiver);
			this.custGrid.setInput(customerLinks);
			if (customerLinks!=null && customerLinks.size()>0) {
				this.custGrid.setInput(customerLinks);
				this.custTable.setSelection(0);
			} else {
				this.custGrid.setInput(null);
				this.custTable.setVisible(false);
			}
		} else {
			this.custTable.setVisible(false);
		}
	}
	
	protected abstract void initDataView();
	
	protected boolean isValid() {		
		if (this.destCmb.getSelectionIndex() == 0) {
			this.infoTipLbl.setText("请选择目的地!");
			return false;
		}
		
		String productName = StringUtil.trimString(this.productNameTxt.getText());
		if (StringUtil.isBlank(productName)) {
			this.infoTipLbl.setText("货物名称不能为空!");
			return false;
		}
		
		String senderName = StringUtil.trimString(this.senderTxt.getText());
		if (StringUtil.isBlank(senderName)) {
			this.infoTipLbl.setText("发货单位不能为空!");
			return false;
		}
		
		String senderCertNo = StringUtil.trimString(this.senderCertNoTxt.getText());
		if (StringUtil.isBlank(senderCertNo)) {
			this.infoTipLbl.setText("证件号码不能为空!");
			return false;
		}
		
		String receiverName = StringUtil.trimString(this.receiverTxt.getText());
		if (StringUtil.isBlank(receiverName)) {
			this.infoTipLbl.setText("收货单位不能为空!");
			return false;
		}
		
		String senderPhone = StringUtil.trimString(this.senderPhoneTxt.getText());
		if (StringUtil.isBlank(senderPhone)) {
			this.infoTipLbl.setText("发货电话不能为空!");
			return false;
		}
		
		String receiverPhone = StringUtil.trimString(this.receiverPhoneTxt.getText());
		if (StringUtil.isBlank(receiverPhone)) {
			this.infoTipLbl.setText("收货电话不能为空!");
			return false;
		}
		
		String numStr = StringUtil.trimString(this.numberTxt.getText());
		if (StringUtil.isBlank(numStr)) {
			this.infoTipLbl.setText("件数不能为空!");
			return false;
		}
		
		if (StringUtil.isNotExpress(numStr)) {
			this.infoTipLbl.setText("件数必须为合法的加法表达式!");
			return false;
		}
		
		String priceFee = StringUtil.trimString(this.priceFeeTxt.getText());
		if (StringUtil.isNotBlank(priceFee) && StringUtil.isNotInteger(priceFee)) {
			this.infoTipLbl.setText("保价费必须为合法的整数!");
			return false;
		}		
		
		String bargeFee = StringUtil.trimString(this.bargeFeeTxt.getText());
		if (StringUtil.isNotBlank(bargeFee) && StringUtil.isNotInteger(bargeFee)) {
			this.infoTipLbl.setText("市区短驳费必须为合法的整数!");
			return false;
		}
		
		String totalFee = StringUtil.trimString(this.totalFeeTxt.getText());
		if (StringUtil.isBlank(totalFee)) {
			this.infoTipLbl.setText("合计运费不能为空!");
			return false;
		}
		
		if (StringUtil.isNotInteger(totalFee)) {
			this.infoTipLbl.setText("合计运费必须为合法的整数!");
			return false;
		}
		
		if (selfGetBtn.getSelection()==false && sendProdBtn.getSelection()==false) {
			this.infoTipLbl.setText("自提或送货必须选一个!");
			return false;
		}
		
		if (getPayBtn.getSelection()==false && cashPayBtn.getSelection()==false && backPayBtn.getSelection()==false) {
			this.infoTipLbl.setText("提付，现付或回结必须选一个!");
			return false;
		}
		
		String deliveryFee = StringUtil.trimString(this.deliverFeeTxt.getText());
		if (StringUtil.isNotBlank(deliveryFee) && StringUtil.isNotInteger(deliveryFee)) {
			this.infoTipLbl.setText("代收货款必须为合法的整数!");
			return false;
		}
		
		if (senderSignerCmb.getSelectionIndex() == 0) {
			this.infoTipLbl.setText("请选择签字人!");
			return false;
		}
		
		return true;
	}
	
	protected Date getDateTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.dateTime.getYear(), this.dateTime.getMonth(), this.dateTime.getDay());
		return calendar.getTime();
	}
	
	protected abstract void confirmOperation();
	
	protected abstract void cancelOperation();
}

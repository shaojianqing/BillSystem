package com.sys.ui;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sys.bo.Base;
import com.sys.bo.Bill;
import com.sys.util.ConstUtil;
import com.sys.util.StringUtil;
import com.sys.vo.CustomerLink;

public class WinBillMemo extends WinBillOperation {
	
	private WinPrintList winPrintList;
	
	private StyledText memoTxt;

	private Bill currentBill;
	
	public WinBillMemo(Display displayP, WinPrintList winPrintList, Bill bill) {
		super(displayP);
		this.currentBill = bill;
		this.winPrintList = winPrintList;
		this.initDataView();
	}
	
	@Override
	protected void initTitleView() {
		
		super.initTitleView();
		this.setSize(800, 640);
		this.setImage(SWTResourceManager.getImage(WinBillNew.class, "/res/images/ico_update_bill.png"));
		
		this.setText("\u66F4\u65B0\u7269\u6D41\u5355\u636E");		
		this.setBounds((screenBound.width-800)/2, (screenBound.height-640)/2, 800, 680);
		
		Label nameSignLbl = new Label(this, SWT.NONE);
		nameSignLbl.setAlignment(SWT.CENTER);
		nameSignLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 28, SWT.NORMAL));
		nameSignLbl.setBounds(220, 20, 360, 50);
		nameSignLbl.setText("\u5BCC\u9633\u5E02\u8054\u5408\u5FEB\u8FD0\u516C\u53F8");
	}
	
	protected void initMainView() {
		
		super.initMainView();	
		
		Label vline17 = new Label(this, SWT.NONE);
		vline17.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline17.setBounds(14, 540, 1, 40);
		
		Label vline18 = new Label(this, SWT.NONE);
		vline18.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline18.setBounds(130, 540, 1, 40);
		
		Label vline19 = new Label(this, SWT.NONE);
		vline19.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		vline19.setBounds(774, 540, 1, 40);
		
		Label hline15 = new Label(this, SWT.NONE);
		hline15.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline15.setBounds(14, 540, 760, 1);
		
		Label hline16 = new Label(this, SWT.NONE);
		hline16.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		hline16.setBounds(14, 580, 760, 1);
		
		Label memoLbl = new Label(this, SWT.NONE);
		memoLbl.setText("\u5907\u6CE8");
		memoLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		memoLbl.setBounds(21, 548, 106, 28);
		
		memoTxt = new StyledText(this, SWT.WRAP);
		memoTxt.setForeground(SWTResourceManager.getColor(0, 51, 255));
		memoTxt.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 16, SWT.NORMAL));
		memoTxt.setBounds(131, 541, 643, 39);
		
		confirmBtn.setBounds(636, 585, 140, 44);
		cancelBtn.setBounds(466, 585, 140, 44);
		infoTipLbl.setBounds(14, 595, 429, 34);
	}
	
	protected void initDataView() {
		if (currentBill != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentBill.getSendDate());
			this.dateTime.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			this.productNameTxt.setText(currentBill.getName());
			this.packageTxt.setText(currentBill.getPack());
			this.numberTxt.setText(currentBill.getNumStr());
			this.codeLbl.setText(currentBill.getCode());
			this.priceFeeTxt.setText(String.valueOf(currentBill.getPriceFee()));
			this.bargeFeeTxt.setText(String.valueOf(currentBill.getBargeFee()));
			this.totalFeeTxt.setText(String.valueOf(currentBill.getTotalFee()));
			this.deliverFeeTxt.setText(String.valueOf(currentBill.getDeliverFee()));
			this.senderTxt.setText(currentBill.getSenderUnit());
			this.senderPhoneTxt.setText(currentBill.getSenderPhone());
			this.senderCertNoTxt.setText(StringUtil.trimString(currentBill.getSenderCertNo()));
			this.receiverTxt.setText(currentBill.getReceiverUnit());
			this.receiverPhoneTxt.setText(currentBill.getReceiverPhone());
			this.selfGetBtn.setSelection(currentBill.getIsSelfGet()==Base.BASE_TRUE);
			this.sendProdBtn.setSelection(currentBill.getIsSendProd()==Base.BASE_TRUE);
			this.getPayBtn.setSelection(currentBill.getIsGetPay()==Base.BASE_TRUE);
			this.cashPayBtn.setSelection(currentBill.getIsCashPay()==Base.BASE_TRUE);
			this.backPayBtn.setSelection(currentBill.getIsBackPay()==Base.BASE_TRUE);
			this.backBillBtn.setSelection(currentBill.getIsBackBill()==Base.BASE_TRUE);
			if (this.backBillBtn.getSelection()) {
				this.backBillNumCmb.setVisible(true);
				this.backBillNumCmb.select(currentBill.getBackBillNum()-1);
			} else {
				this.backBillNumCmb.setVisible(false);
				this.backBillNumCmb.select(0);
			}
			
			if (StringUtil.isNotBlank(currentBill.getMemo())) {
				this.memoTxt.setText(currentBill.getMemo());
			} else {
				this.memoTxt.setText("");
			}
			
			int signerIndex = userService.getUserIndex(userInfoList, currentBill.getSignerId());
			this.senderSignerCmb.select(signerIndex);
			
			int destionIndex = regionService.getRegionIndex(regionList, currentBill.getDestinationId());
			this.destCmb.select(destionIndex);
			this.isDataInited = true;
		}
	}
	
	@Override
	protected void confirmOperation() {
		this.updateBill();
	}

	@Override
	protected void cancelOperation() {
		this.close();
	}
	
	protected void updateBill() {	
		try {
			if (this.isValid()) {
				String senderName = StringUtil.trimString(this.senderTxt.getText());
				String senderPhone = StringUtil.trimString(this.senderPhoneTxt.getText());
				String senderCertNo = StringUtil.trimString(this.senderCertNoTxt.getText());
				String receiverName = StringUtil.trimString(this.receiverTxt.getText());
				String receiverPhone = StringUtil.trimString(this.receiverPhoneTxt.getText());
				
				CustomerLink senderData = customerService.prepareCustomerLinkWayWithCertNo(senderName, senderPhone, senderCertNo);
				CustomerLink receiverData = customerService.prepareCustomerLinkWayInfo(receiverName, receiverPhone);
				
				currentBill.setCode(StringUtil.trimString(billService.getCurrentCode()));
				currentBill.setName(StringUtil.trimString(this.productNameTxt.getText()));
				currentBill.setPack(StringUtil.trimString(this.packageTxt.getText()));
				currentBill.setSendDate(this.getDateTime());
				currentBill.setNumStr(StringUtil.trimString(this.numberTxt.getText()));
				
				String priceFeeStr = StringUtil.trimString(this.priceFeeTxt.getText());
				if (StringUtil.isNotBlank(priceFeeStr) && StringUtil.isInteger(priceFeeStr)) {
					currentBill.setPriceFee(Integer.valueOf(priceFeeStr));
				} else {
					currentBill.setPriceFee(0);
				}
				
				String bargeFeeStr = StringUtil.trimString(this.bargeFeeTxt.getText());
				if (StringUtil.isNotBlank(bargeFeeStr) && StringUtil.isInteger(bargeFeeStr)) {
					currentBill.setBargeFee(Integer.valueOf(bargeFeeStr));
				} else {
					currentBill.setBargeFee(0);
				}				
		
				currentBill.setTotalFee(Integer.valueOf(StringUtil.trimString(this.totalFeeTxt.getText())));
				currentBill.setDeliverFee(Integer.valueOf(StringUtil.trimString(this.deliverFeeTxt.getText())));
				currentBill.setSenderUnit(senderData.getCustomerName());
				currentBill.setSenderUnitId(senderData.getCustomerId());
				currentBill.setSenderPhone(senderData.getCustomerPhone());
				currentBill.setSenderCertNo(senderData.getCustomerCertNo());
				currentBill.setReceiverUnit(receiverData.getCustomerName());
				currentBill.setReceiverUnitId(receiverData.getCustomerId());
				currentBill.setReceiverPhone(receiverData.getCustomerPhone());
				
				String senderSigner = userNameList[this.senderSignerCmb.getSelectionIndex()];
				String senderSignerId = userIdList[this.senderSignerCmb.getSelectionIndex()];
				currentBill.setSigner(senderSigner);
				currentBill.setSignerId(senderSignerId);
				
				String destination = regionNameList[this.destCmb.getSelectionIndex()];
				String destinationId = regionIdList[this.destCmb.getSelectionIndex()];
				currentBill.setDestination(destination);
				currentBill.setDestinationId(destinationId);
				
				currentBill.setSource("¸»Ñô");
				currentBill.setIsSelfGet((short)(this.selfGetBtn.getSelection()?1:0));
				currentBill.setIsSendProd((short)(this.sendProdBtn.getSelection()?1:0));
				currentBill.setIsGetPay((short)(this.getPayBtn.getSelection()?1:0));			
				currentBill.setIsCashPay((short)(this.cashPayBtn.getSelection()?1:0));
				currentBill.setIsBackPay((short)(this.backPayBtn.getSelection()?1:0));
				currentBill.setIsBackBill((short)(this.backBillBtn.getSelection()?1:0));
				currentBill.setMemo(StringUtil.trimString(this.memoTxt.getText()));
				
				if (backBillBtn.getSelection()) {
					int index = backBillNumCmb.getSelectionIndex();
					int backBillNum = Integer.valueOf(ConstUtil.BILL_NUM[index]);
					currentBill.setBackBillNum(backBillNum);
				} else {
					currentBill.setBackBillNum(0);
				}
				currentBill.initData();
				billService.updateBillMemo(currentBill);
				WinBillMemo.this.close();
				WinBillMemo.this.winPrintList.initDataView();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	protected void checkSubclass() {
	}
}
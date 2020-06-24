package com.sys.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sys.bo.Bill;
import com.sys.util.ConstUtil;
import com.sys.util.PrintUtil;
import com.sys.util.StringUtil;
import com.sys.vo.CustomerLink;

public class WinBillNew extends WinBillOperation {

	private WinMain winMain;
	
	public WinBillNew(Display displayP, WinMain winMain) {
		super(displayP);		
		this.winMain = winMain;
		this.initDataView();
	}
	
	@Override
	protected void initTitleView() {
		
		super.initTitleView();
		this.setSize(800, 640);
		this.setImage(SWTResourceManager.getImage(WinBillNew.class, "/res/images/ico_win_new_bill.png"));
		
		this.setText("\u65B0\u5EFA\u7269\u6D41\u5355\u636E");		
		this.setBounds((screenBound.width-800)/2, (screenBound.height-660)/2, 800, 640);
		
		Label nameSignLbl = new Label(this, SWT.NONE);
		nameSignLbl.setAlignment(SWT.CENTER);
		nameSignLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 28, SWT.NORMAL));
		nameSignLbl.setBounds(220, 20, 360, 50);
		nameSignLbl.setText("\u5BCC\u9633\u5E02\u8054\u5408\u5FEB\u8FD0\u516C\u53F8");
	}
	
	protected void initDataView() {
		this.codeLbl.setText(billService.getCurrentCode());
		this.isDataInited = true;
	}
	
	@Override
	protected void confirmOperation() {
		this.saveBill();
	}

	@Override
	protected void cancelOperation() {
		this.close();		
	}

	protected void saveBill() {	
		try {
			if (this.isValid()) {
				
				String senderName = StringUtil.trimString(this.senderTxt.getText());
				String senderPhone = StringUtil.trimString(this.senderPhoneTxt.getText());
				String senderCertNo = StringUtil.trimString(this.senderCertNoTxt.getText());
				String receiverName = StringUtil.trimString(this.receiverTxt.getText());
				String receiverPhone = StringUtil.trimString(this.receiverPhoneTxt.getText());
				
				CustomerLink senderData = customerService.prepareCustomerLinkWayWithCertNo(senderName, senderPhone, senderCertNo);
				CustomerLink receiverData = customerService.prepareCustomerLinkWayInfo(receiverName, receiverPhone);
				
				Bill bill = new Bill();
				bill.setCode(billService.getCurrentCode());
				bill.setName(StringUtil.trimString(this.productNameTxt.getText()));
				bill.setPack(StringUtil.trimString(this.packageTxt.getText()));
				bill.setSendDate(this.getDateTime());
				bill.setNumStr(StringUtil.trimString(this.numberTxt.getText()));
				
				String priceFeeStr = StringUtil.trimString(this.priceFeeTxt.getText());
				if (StringUtil.isNotBlank(priceFeeStr) && StringUtil.isInteger(priceFeeStr)) {
					bill.setPriceFee(Integer.valueOf(priceFeeStr));
				} else {
					bill.setPriceFee(0);
				}
				
				String bargeFeeStr = StringUtil.trimString(this.bargeFeeTxt.getText());
				if (StringUtil.isNotBlank(bargeFeeStr) && StringUtil.isInteger(bargeFeeStr)) {
					bill.setBargeFee(Integer.valueOf(bargeFeeStr));
				} else {
					bill.setBargeFee(0);
				}				
				
				String deliverFeeStr = StringUtil.trimString(this.deliverFeeTxt.getText());
				if (StringUtil.isNotBlank(deliverFeeStr) && StringUtil.isInteger(deliverFeeStr)) {
					bill.setDeliverFee(Integer.valueOf(deliverFeeStr));
				} else {
					bill.setDeliverFee(0);
				}	
		
				bill.setTotalFee(Integer.valueOf(StringUtil.trimString(this.totalFeeTxt.getText())));
				bill.setSenderUnit(senderData.getCustomerName());
				bill.setSenderCertNo(senderData.getCustomerCertNo());
				bill.setSenderUnitId(senderData.getCustomerId());
				bill.setSenderPhone(senderData.getCustomerPhone());
				bill.setReceiverUnit(receiverData.getCustomerName());
				bill.setReceiverUnitId(receiverData.getCustomerId());
				bill.setReceiverPhone(receiverData.getCustomerPhone());
				
				String senderSigner = userNameList[this.senderSignerCmb.getSelectionIndex()];
				String senderSignerId = userIdList[this.senderSignerCmb.getSelectionIndex()];
				bill.setSigner(senderSigner);
				bill.setSignerId(senderSignerId);
				
				String destination = regionNameList[this.destCmb.getSelectionIndex()];
				String destinationId = regionIdList[this.destCmb.getSelectionIndex()];
				bill.setDestination(destination);
				bill.setDestinationId(destinationId);
				
				bill.setSource("¸»Ñô");
				bill.setIsSelfGet((short)(this.selfGetBtn.getSelection()?1:0));
				bill.setIsSendProd((short)(this.sendProdBtn.getSelection()?1:0));
				bill.setIsGetPay((short)(this.getPayBtn.getSelection()?1:0));			
				bill.setIsCashPay((short)(this.cashPayBtn.getSelection()?1:0));
				bill.setIsBackPay((short)(this.backPayBtn.getSelection()?1:0));
				bill.setIsBackBill((short)(this.backBillBtn.getSelection()?1:0));
				
				if (backBillBtn.getSelection()) {
					int index = backBillNumCmb.getSelectionIndex();
					int backBillNum = Integer.valueOf(ConstUtil.BILL_NUM[index]);
					bill.setBackBillNum(backBillNum);
				} else {
					bill.setBackBillNum(0);
				}
				bill.initData();
				billService.saveBill(bill);
				PrintUtil.printBillDetail(bill);
				WinBillNew.this.close();
				WinBillNew.this.winMain.initDataView();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	protected void checkSubclass() {
	}
}
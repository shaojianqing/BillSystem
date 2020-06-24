package com.sys.printer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Calendar;
import java.util.List;

import com.sys.bo.Bill;
import com.sys.util.StringUtil;

public class BillDetailPrinter implements Printable {

	private Bill bill;
	
	public BillDetailPrinter(Bill bill) {
		this.bill = bill;
	}

	@Override
	public int print(Graphics graphics, PageFormat format, int index) throws PrinterException {
		if (bill != null) {
			Graphics2D context = (Graphics2D) graphics;
			context.setColor(Color.black);
			Font font = new Font("ÐÂËÎÌå", Font.PLAIN, 12);
			context.setFont(font);
		    if (bill.getSendDate()!=null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(bill.getSendDate());
				String year = String.valueOf(calendar.get(Calendar.YEAR));
				String month = String.valueOf((calendar.get(Calendar.MONTH)+1));
				String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
				context.drawString(year, 100, 83);
				context.drawString(month, 150, 83);
				context.drawString(day, 190, 83);
			}
		    
			String source = StringUtil.formatString(bill.getSource());
			context.drawString(source, 380-11*source.length(), 83);			
			String destination = StringUtil.formatString(bill.getDestination());
			context.drawString(destination, 420, 83);
			String senderUnit = StringUtil.formatString(bill.getSenderUnit());
			context.drawString(senderUnit, 105, 104);
			String senderPhone = StringUtil.formatString(bill.getSenderPhone());
			context.drawString(senderPhone,  340, 104);
			String receiverUnit = StringUtil.formatString(bill.getReceiverUnit());
			context.drawString(receiverUnit, 105, 132);
			String receiverPhone = StringUtil.formatString(bill.getReceiverPhone());
			context.drawString(receiverPhone, 340, 132);
			String productName = StringUtil.formatString(bill.getName());
			List<String> stringList = StringUtil.splitString(productName, 150);
			if (stringList != null) {
				for (int i=0;i<stringList.size();++i) {
					context.drawString(stringList.get(i), 56, 172+i*12);
				}
			}
			String pack = StringUtil.formatString(bill.getPack());
			context.drawString(pack, 192, 178);
			context.drawString(bill.getNumStr(), 240, 178);
			context.drawString(String.valueOf(bill.getPriceFee()), 280, 178);
			context.drawString(String.valueOf(bill.getBargeFee()), 350, 178);
			
			String[] totalFeeList = StringUtil.transferString(bill.getTotalFee());
			String totalFeeHigh = totalFeeList[0];
			context.drawString(totalFeeHigh, 190-8*totalFeeHigh.length(), 232);
			for (int i=1;i<totalFeeList.length;++i) {
				context.drawString(totalFeeList[i], 176+i*34, 232);
			}
			
			String[] deliverFeeList = StringUtil.transferString(bill.getDeliverFee());
			String deliverFeeHigh = deliverFeeList[0];
			context.drawString(deliverFeeHigh, 190-8*deliverFeeHigh.length(), 248);
			for (int i=1;i<deliverFeeList.length;++i) {
				context.drawString(deliverFeeList[i], 176+i*34, 248);
			}
			context.drawString(String.valueOf(bill.getTotalFee()), 372, 232);
			context.drawString(String.valueOf(bill.getDeliverFee()), 372, 248);
			String signer = StringUtil.formatString(bill.getSigner());
			context.drawString(signer, 416, 294);
			if (bill.getIsSelfGet()==Bill.BASE_TRUE) {
				context.drawString("¡Ì", 490, 152);
			}
			if (bill.getIsSendProd()==Bill.BASE_TRUE) {
				context.drawString("¡Ì", 490, 172);
			}
			if (bill.getIsGetPay()==Bill.BASE_TRUE) {
				context.drawString("¡Ì", 490, 192);
			}
			if (bill.getIsCashPay()==Bill.BASE_TRUE) {
				context.drawString("¡Ì", 490, 212);
			}
			if (bill.getIsBackPay()==Bill.BASE_TRUE) {
				context.drawString("¡Ì", 490, 232);
			}
			if (bill.getIsBackBill()==Bill.BASE_TRUE) {
				context.drawString("¡Ì", 475, 252);
				context.drawString("("+bill.getBackBillNum()+"·Ý)", 485, 252);
			}
		}
		return PAGE_EXISTS;
	}
}



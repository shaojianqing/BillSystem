package com.sys.printer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

import com.sys.bo.Base;
import com.sys.bo.Bill;
import com.sys.bo.BillDetail;
import com.sys.util.StringUtil;

public class BillAdminPrinter implements Printable {
	
	private final static int TABLE_START = 95;
	
	private final static int TABLE_HEIGHT = 24;
	
	private List<Bill> billList;
	
	private BillDetail billDetail;
	
	public BillAdminPrinter(List<Bill> billList, BillDetail billDetail) 
	{
		this.billList = billList;
		this.billDetail = billDetail;
	}

	@Override
	public int print(Graphics graphics, PageFormat format, int index) throws PrinterException 
	{
		if (billList != null && billList.size()>0 && billDetail != null) {
			Graphics2D context = (Graphics2D) graphics;
			context.setColor(Color.black);

			this.drawBillList(context);
		}
		return PAGE_EXISTS;
	}
	
	private void drawBillList(Graphics2D context) {
		context.setFont(new Font("ÐÂËÎÌå", Font.PLAIN, 9));
		
		int limit = billList.size();
		for (int i=0;i<limit;++i) {
			drawBillCell(context, billList.get(i), i, 12, TABLE_START+i*TABLE_HEIGHT);
		}
	}
	
	private void drawBillCell(Graphics2D context, Base base, int seq, int x, int y) 
	{
		Bill bill = (Bill)base;
		String sequence = String.valueOf(seq);
		context.drawString(sequence, 2, y+16);
		context.drawString(bill.getCode(), x, y+16);
		String senderUnit = StringUtil.formatShortString(bill.getSenderUnit(), 8);
		context.drawString(senderUnit, x+40, y+16);
		String senderCertNo = StringUtil.trimString(bill.getSenderCertNo());
		
		List<String> stringList = StringUtil.splitString(senderCertNo, 120);
		if (stringList != null) {
			for (int i=0;i<stringList.size();++i) {
				context.drawString(stringList.get(i), x+140, y+12+8*i);
			}
		}
		context.drawString(bill.getSenderPhone(), x+200, y+16);
		
		stringList = StringUtil.splitString(bill.getName(), 70);
		if (stringList != null) {
			for (int i=0;i<stringList.size();++i) {
				context.drawString(stringList.get(i), x+265, y+12+8*i);
			}
		}
		
		context.drawString(bill.getNumStr(), x+308, y+16);
		String receiverUnit = StringUtil.formatShortString(bill.getReceiverUnit(), 8);
		context.drawString(receiverUnit, x+380, y+16);
		
		List<String> destionationList = StringUtil.splitString(bill.getDestination(), 80);
		if (destionationList != null) {
			for (int i=0;i<destionationList.size();++i) {
				context.drawString(destionationList.get(i), x+445, y+12+8*i);
			}
		}
		
		stringList = StringUtil.splitString(bill.getReceiverPhone(), 150);
		if (stringList != null) {
			for (int i=0;i<stringList.size();++i) {
				context.drawString(stringList.get(i), x+490, y+12+8*i);
			}
		}
		
		context.drawString(this.billDetail.getVehicle(), x+557, y+16);
		context.drawString(this.billDetail.getOperator(), x+607, y+16);
		
		stringList = StringUtil.splitString(bill.getMemo(), 130);
		if (stringList != null) {
			for (int i=0;i<stringList.size();++i) {
				context.drawString(stringList.get(i), x+690, y+12+8*i);
			}
		}
	}
}

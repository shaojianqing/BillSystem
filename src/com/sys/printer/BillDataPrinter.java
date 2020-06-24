package com.sys.printer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

import com.sys.bo.Bill;
import com.sys.util.DateUtil;
import com.sys.util.StringUtil;

public class BillDataPrinter implements Printable {

	private final static int TABLE_LEFT = 16;
	
	private final static int TABLE_RIGHT = 580;
	
	private final static int TABLE_TOP = 86;
	
	private final static int TABLE_START = 100;
	
	private final static int TABLE_HEIGHT = 32;
	
	private List<Bill> billList;
	
	private String title;
	
	public BillDataPrinter(List<Bill> billList, String title) 
	{
		this.title = title;
		this.billList = billList;
	}

	@Override
	public int print(Graphics graphics, PageFormat format, int index) throws PrinterException 
	{
		if (billList != null && billList.size()>0) {
			Graphics2D context = (Graphics2D) graphics;
			context.setColor(Color.black);
			
			int yAxis = TABLE_HEIGHT*billList.size()+TABLE_TOP+14;
			
			this.drawFrame(context, yAxis);
			this.drawTitle(context);
			this.drawHeader(context);
			this.drawBillList(context);
			this.drawFooter(context, yAxis+20);
		}
		return PAGE_EXISTS;
	}

	private void drawTitle(Graphics2D context) 
	{
		context.setFont(new Font("新宋体", Font.PLAIN, 18));		
		int x = (TABLE_RIGHT + TABLE_LEFT - StringUtil.getStringLength(title))/2;		
		context.drawString(title, x, 60);
	}
	
	private void drawHeader(Graphics2D context) 
	{
		context.setFont(new Font("新宋体", Font.PLAIN, 10));
		context.drawString("发货日期", 18, 97);	
		context.drawString("收货单位(电话)", 90, 97);
		context.drawString("到站", 180, 97);
		context.drawString("货物名称", 220, 97);
		context.drawString("包装", 310, 97);
		context.drawString("件数", 350, 97);	
		context.drawString("合计", 385, 97);
		context.drawString("代收", 420, 97);
		context.drawString("签回单", 455, 97);
		context.drawString("提货方式", 490, 97);
		context.drawString("签字人", 540, 97);
		
		context.drawLine(88, TABLE_TOP, 88, TABLE_TOP+14);
		context.drawLine(178, TABLE_TOP, 178, TABLE_TOP+14);
		context.drawLine(218, TABLE_TOP, 218, TABLE_TOP+14);
		context.drawLine(308, TABLE_TOP, 308, TABLE_TOP+14);
		context.drawLine(348, TABLE_TOP, 348, TABLE_TOP+14);
		context.drawLine(383, TABLE_TOP, 383, TABLE_TOP+14);
		context.drawLine(418, TABLE_TOP, 418, TABLE_TOP+14);
		context.drawLine(453, TABLE_TOP, 453, TABLE_TOP+14);
		context.drawLine(488, TABLE_TOP, 488, TABLE_TOP+14);
		context.drawLine(538, TABLE_TOP, 538, TABLE_TOP+14);
	}
	
	private void drawFrame(Graphics2D context, int yAxis) 
	{
		context.setStroke(new BasicStroke(0));
		context.drawLine(TABLE_LEFT, TABLE_TOP, TABLE_RIGHT, TABLE_TOP);
		context.drawLine(TABLE_LEFT, yAxis, TABLE_RIGHT, yAxis);		
		
		context.drawLine(TABLE_LEFT, TABLE_TOP, TABLE_LEFT, yAxis);
		context.drawLine(TABLE_RIGHT, TABLE_TOP, TABLE_RIGHT, yAxis);
		
		context.drawLine(TABLE_LEFT, 100, TABLE_RIGHT, 100);
	}
	
	private void drawBillList(Graphics2D context) {
		context.setFont(new Font("新宋体", Font.PLAIN, 9));
		int baseYaxis = TABLE_START;	
		
		int limit = billList.size();
		for (int i=0;i<limit;++i) {
			drawBillCell(context, billList.get(i), 20, baseYaxis+i*TABLE_HEIGHT);
			context.drawLine(TABLE_LEFT, baseYaxis+(i+1)*TABLE_HEIGHT, TABLE_RIGHT, baseYaxis+(i+1)*TABLE_HEIGHT);
		}
	}
	
	private void drawBillCell(Graphics2D context, Bill bill, int x, int y) 
	{
		String sendDate = DateUtil.formatChineseDate(bill.getSendDate());
		context.drawString(sendDate, x, y+18);
		
		String receiverUnit = StringUtil.formatShortString(bill.getReceiverUnit(), 8);
		context.drawString(receiverUnit, x+72, y+10);
		List<String> phoneList = StringUtil.splitString(bill.getReceiverPhone(), 190);
		if (phoneList != null) {
			for (int i=0;i<phoneList.size();++i) {
				context.drawString(phoneList.get(i), x+72, y+(i+1)*10+10);
			}
		}
		
		List<String> destionationList = StringUtil.splitString(bill.getDestination(), 56);
		if (destionationList != null) {
			for (int i=0;i<destionationList.size();++i) {
				context.drawString(destionationList.get(i), x+162, y+i*10+10);
			}
		}
		
		List<String> nameList = StringUtil.splitString(bill.getName(), 140);
		if (nameList != null) {
			for (int i=0;i<nameList.size();++i) {
				context.drawString(nameList.get(i), x+202, y+i*10+10);
			}
		}
		
		List<String> packageList = StringUtil.splitString(bill.getPack(), 56);
		if (packageList != null) {
			for (int i=0;i<packageList.size();++i) {
				context.drawString(packageList.get(i), x+292, y+i*10+10);
			}
		}
		
		context.drawString(bill.getNumStr(), x+332, y+18);
		
		if (bill.getTotalFee()>0) {
			context.drawString(String.valueOf(bill.getTotalFee()), x+367, y+18);
		}
		
		if (bill.getDeliverFee()>0) {
			context.drawString(String.valueOf(bill.getDeliverFee()), x+402, y+18);
		}
		
		if (bill.getIsBackBill() == Bill.BASE_TRUE) {
			context.drawString("√", x+437, y+18);
			context.drawString(String.format("(%d份)", bill.getBackBillNum()), x+445, y+18);
		}
		
		if (bill.getIsSelfGet() == Bill.BASE_TRUE) {
			context.drawString("自提", x+482, y+18);
		} else if (bill.getIsSendProd() == Bill.BASE_TRUE) {
			context.drawString("送货", x+482, y+18);
		}
		
		context.drawString(bill.getSigner(), x+522, y+18);
		
		context.drawLine(88, y, 88, y+TABLE_HEIGHT);
		context.drawLine(178, y, 178, y+TABLE_HEIGHT);
		context.drawLine(218, y, 218, y+TABLE_HEIGHT);
		context.drawLine(308, y, 308, y+TABLE_HEIGHT);
		context.drawLine(348, y, 348, y+TABLE_HEIGHT);
		context.drawLine(383, y, 383, y+TABLE_HEIGHT);
		context.drawLine(418, y, 418, y+TABLE_HEIGHT);
		context.drawLine(453, y, 453, y+TABLE_HEIGHT);
		context.drawLine(488, y, 488, y+TABLE_HEIGHT);
		context.drawLine(538, y, 538, y+TABLE_HEIGHT);
	}
	
	private void drawFooter(Graphics2D context, int yAxis) 
	{
		context.setFont(new Font("新宋体", Font.PLAIN, 10));
		
		int totalNum = 0, totalFee = 0;
		for (Bill bill:billList) {
			totalNum+=bill.getNumber();
			totalFee+=bill.getTotalFee();
		}
		
		context.drawString("件数:",42, yAxis);
		context.drawString(String.valueOf(totalNum), 72, yAxis);
		
		context.drawString("合计:",460, yAxis);
		context.drawString(totalFee+"元", 530, yAxis);	
	}
}

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

import com.sys.bo.Base;
import com.sys.bo.Bill;
import com.sys.bo.BillDetail;
import com.sys.bo.Region;
import com.sys.util.DateUtil;
import com.sys.util.StringUtil;

public class BillListPrinter implements Printable {

	private final static int TABLE_LEFT = 40;
	
	private final static int TABLE_RIGHT = 560;
	
	private final static int TABLE_TOP = 116;
	
	private final static int TABLE_START = 130;
	
	private final static int TABLE_HEIGHT = 28;
	
	private List<Base> billList;
	
	private BillDetail billDetail;
	
	public BillListPrinter(List<Base> billList, BillDetail billDetail) 
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
			
			int yAxis = TABLE_HEIGHT*billList.size()+TABLE_TOP+14;
			
			this.drawFrame(context, yAxis);
			this.drawTitle(context);
			this.drawBanner(context);
			this.drawHeader(context);
			this.drawBillList(context);
			this.drawFooter(context, yAxis+20);
		}
		return PAGE_EXISTS;
	}

	private void drawTitle(Graphics2D context) 
	{
		context.setFont(new Font("新宋体", Font.PLAIN, 24));
		context.drawString("富阳联合快运公司货物受理清单", 140, 80);
	}
	
	private void drawBanner(Graphics2D context) 
	{
		context.setFont(new Font("新宋体", Font.PLAIN, 10));
		context.drawString("车号:", 40, 110);		
		String vehicle = StringUtil.formatString(billDetail.getVehicle());
		context.drawString(vehicle, 70, 110);
		
		context.drawString("驾驶员:", 200, 110);		
		String operator = StringUtil.formatString(billDetail.getOperator());
		context.drawString(operator, 240, 110);
		
		context.drawString("制单日期:", 440, 110);		
		String createDate = DateUtil.formatChineseDate(billDetail.getCreateDate());
		context.drawString(createDate, 490, 110);
	}
	
	private void drawHeader(Graphics2D context) 
	{
		context.setFont(new Font("新宋体", Font.PLAIN, 10));
		context.drawString("序号", 42, 127);	
		context.drawString("托运人(电话)", 82, 127);	
		context.drawString("到站", 157, 127);	
		context.drawString("提货人(电话)", 207, 127);	
		context.drawString("件数", 280, 127);	
		context.drawString("到付", 308, 127);
		context.drawString("已付", 336, 127);
		context.drawString("月结", 364, 127);
		context.drawString("回单", 392, 127);
		context.drawString("服务", 420, 127);
		context.drawString("代收", 448, 127);
		context.drawString("备注", 476, 127);
		
		context.drawLine(80, TABLE_TOP, 80, TABLE_TOP+14);
		context.drawLine(155, TABLE_TOP, 155, TABLE_TOP+14);
		context.drawLine(205, TABLE_TOP, 205, TABLE_TOP+14);
		context.drawLine(278, TABLE_TOP, 278, TABLE_TOP+14);
		context.drawLine(306, TABLE_TOP, 306, TABLE_TOP+14);
		context.drawLine(334, TABLE_TOP, 334, TABLE_TOP+14);
		context.drawLine(362, TABLE_TOP, 362, TABLE_TOP+14);
		context.drawLine(390, TABLE_TOP, 390, TABLE_TOP+14);
		context.drawLine(418, TABLE_TOP, 418, TABLE_TOP+14);
		context.drawLine(446, TABLE_TOP, 446, TABLE_TOP+14);
		context.drawLine(474, TABLE_TOP, 474, TABLE_TOP+14);
	}
	
	private void drawFrame(Graphics2D context, int yAxis) 
	{
		context.setStroke(new BasicStroke(0));
		context.drawLine(TABLE_LEFT, TABLE_TOP, TABLE_RIGHT, TABLE_TOP);
		context.drawLine(TABLE_LEFT, yAxis, TABLE_RIGHT, yAxis);		
		
		context.drawLine(TABLE_LEFT, TABLE_TOP, TABLE_LEFT, yAxis);
		context.drawLine(TABLE_RIGHT, TABLE_TOP, TABLE_RIGHT, yAxis);
		
		context.drawLine(TABLE_LEFT, 130, TABLE_RIGHT, 130);
	}
	
	private void drawBillList(Graphics2D context) {
		context.setFont(new Font("新宋体", Font.PLAIN, 9));
		int baseYaxis = TABLE_START;	
		
		int limit = billList.size();
		for (int i=0;i<limit;++i) {
			drawBillCell(context, billList.get(i), 42, baseYaxis+i*TABLE_HEIGHT);
			context.drawLine(TABLE_LEFT, baseYaxis+(i+1)*TABLE_HEIGHT, TABLE_RIGHT, baseYaxis+(i+1)*TABLE_HEIGHT);
		}
	}
	
	private void drawBillCell(Graphics2D context, Base base, int x, int y) 
	{
		if (base instanceof Bill) {
			Bill bill = (Bill)base;
			context.drawString(bill.getCode(), x, y+16);
			String senderUnit = StringUtil.formatShortString(bill.getSenderUnit(), 8);
			context.drawString(senderUnit, x+40, y+10);		
			context.drawString(bill.getSenderPhone(), x+40, y+20);
			
			List<String> destionationList = StringUtil.splitString(bill.getDestination(), 80);
			if (destionationList != null) {
				for (int i=0;i<destionationList.size();++i) {
					context.drawString(destionationList.get(i), x+115, y+i*10+10);
				}
			}
			
			String receiverUnit = StringUtil.formatShortString(bill.getReceiverUnit(), 8);
			context.drawString(receiverUnit, x+165, y+10);
			List<String> receiverPhoneList = StringUtil.splitString(bill.getReceiverPhone(), 160);
			if (receiverPhoneList!=null) {
				for (int i=0;i<receiverPhoneList.size();++i) {
					context.drawString(receiverPhoneList.get(i), x+165, y+18+8*i);
				}
			}
			
			context.drawString(bill.getNumStr(), x+238, y+16);
			
			if (bill.getIsGetPay() == Bill.BASE_TRUE) {
				context.drawString(String.valueOf(bill.getTotalFee()), x+266, y+16);
			}
			
			if (bill.getIsCashPay() == Bill.BASE_TRUE) {
				context.drawString("√", x+294, y+16);
			}
			
			if (bill.getIsBackPay() == Bill.BASE_TRUE) {
				context.drawString("√", x+322, y+16);
			}
			
			if (bill.getIsBackBill() == Bill.BASE_TRUE) {
				context.drawString("√", x+346, y+16);
				context.drawString(String.format("(%d份)", bill.getBackBillNum()), x+353, y+16);
			}
			
			if (bill.getIsSelfGet() == Bill.BASE_TRUE) {
				context.drawString("自提", x+378, y+16);
			} else if (bill.getIsSendProd() == Bill.BASE_TRUE) {
				context.drawString("送货", x+378, y+16);
			}
			
			if (bill.getDeliverFee()>0) {
				context.drawString(String.valueOf(bill.getDeliverFee()), x+406, y+16);
			}
			
			if (StringUtil.isNotBlank(bill.getMemo())) {
				List<String> memoList = StringUtil.splitString(bill.getMemo(), 110);
				if (memoList != null) {
					for (int i=0;i<memoList.size();++i) {
						context.drawString(memoList.get(i), x+434, y+i*10+10);
					}
				}	
			}
			context.drawLine(80, y, 80, y+TABLE_HEIGHT);
			context.drawLine(155, y, 155, y+TABLE_HEIGHT);
			context.drawLine(205, y, 205, y+TABLE_HEIGHT);
			context.drawLine(278, y, 278, y+TABLE_HEIGHT);
			context.drawLine(306, y, 306, y+TABLE_HEIGHT);
			context.drawLine(334, y, 334, y+TABLE_HEIGHT);
			context.drawLine(362, y, 362, y+TABLE_HEIGHT);
			context.drawLine(390, y, 390, y+TABLE_HEIGHT);
			context.drawLine(418, y, 418, y+TABLE_HEIGHT);
			context.drawLine(446, y, 446, y+TABLE_HEIGHT);
			context.drawLine(474, y, 474, y+TABLE_HEIGHT);
		} else if (base instanceof Region) {
			Region region = (Region)base;
			context.drawString("到站:", x, y+16);
			context.drawString(region.getName(), x+30, y+16);
		}
	}
	
	private void drawFooter(Graphics2D context, int yAxis) 
	{
		context.setFont(new Font("新宋体", Font.PLAIN, 10));
		
		int getSum = 0;
		for (Base base:billList) {
			if (base instanceof Bill) {
				Bill bill = (Bill)base;
				if (bill.getIsGetPay() == Bill.BASE_TRUE) 
				{
					getSum+=bill.getTotalFee();
				}
			}
		}
		
		context.drawString("提付:",42, yAxis);
		context.drawString(getSum+"元", 72, yAxis);
		
		context.drawString("制单人:", 460, yAxis);
		String creator = StringUtil.formatString(billDetail.getCreator());
		context.drawString(creator, 530, yAxis);	
	}
}

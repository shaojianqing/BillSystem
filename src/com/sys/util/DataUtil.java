package com.sys.util;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import com.sys.bo.Base;
import com.sys.bo.Bill;
import com.sys.bo.Customer;

public class DataUtil {
	
	public static final int CODE_WIDTH = 10*256;
	
	public static final int SEND_DATE_WIDTH = 10*256;
	
	public static final int SENDER_WIDTH = 15*256;
	
	public static final int SEND_PHONE_WIDTH = 12*256;
	
	public static final int RECEIVER_WIDTH = 13*256;
	
	public static final int RECEIVE_PHONE_WIDTH = 12*256;
	
	public static final int SOURCE_WIDTH = 10*256;
	
	public static final int DESTINATION_WIDTH = 8*256;
	
	public static final int PRODUCT_WIDTH = 16*256;
	
	public static final int PACKAGE_WIDTH = 6*256;
	
	public static final int NUMBER_WIDTH = 5*256;
	
	public static final int PRICE_FEE_WIDTH = 5*256;
	
	public static final int DELIVER_FEE_WIDTH = 5*256;
	
	public static final int TOTAL_FEE_WIDTH = 5*256;
	
	public static final int RECEIVE_FEE_WIDTH = 5*256;
	
	public static final int GET_FEE_WIDTH = 5*256;
	
	public static final int PAID_FEE_WIDTH = 5*256;
	
	public static final int MONTH_FEE_WIDTH = 5*256;
	
	public static final int BACK_BILL_WIDTH = 8*256;
	
	public static final int FETCH_WAY_WIDTH = 10*256;
	
	public static final int SIGNER_WIDTH = 8*256;
	
	public static final int FAILURE = 0;
	
	public static final int SUCCESS = 1;
	
	public static final int FILE_EXIST = 2;

	@SuppressWarnings("deprecation")
	public static int exportBill(String fileName, String title, List<Base> billList) {
		if (StringUtil.isNotBlank(fileName) && billList!=null && billList.size()>0) {
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet("物流单据汇总");
			
			HSSFRow row = sheet.createRow(0);
			HSSFCellStyle cellStyle = workBook.createCellStyle();
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	        
	        HSSFFont font = workBook.createFont();
	        font.setFontName(HSSFFont.FONT_ARIAL);//字体
	        font.setFontHeightInPoints((short) 24);//字号 
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
	        font.setColor(HSSFColor.BLACK.index);//颜色
	        cellStyle.setFont(font);
	        cellStyle.setWrapText(true);
	        HSSFCell cell = row.createCell((short)1);
	        cell.setCellValue(title);
	        cell.setCellStyle(cellStyle);
	        sheet.addMergedRegion(new Region(0,(short)1, 2 , (short)21));
	        
	        row = sheet.createRow(3);
	        row.setHeight((short) (10*64));
	        cellStyle = workBook.createCellStyle();  
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
	        cellStyle.setBorderLeft((short)1);
	        cellStyle.setBorderRight((short)1);
	        cellStyle.setBorderTop((short)1);
	        cellStyle.setBorderBottom((short)1);
	        cellStyle.setWrapText(true);
	        
	        HSSFCellStyle senderCellStyle = workBook.createCellStyle();  
    		senderCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    		senderCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    		senderCellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
    		senderCellStyle.setBorderLeft((short)1);
    		senderCellStyle.setBorderRight((short)1);
    		senderCellStyle.setBorderTop((short)1);
    		senderCellStyle.setBorderBottom((short)1);
    		senderCellStyle.setWrapText(true);
	        
	        cell = row.createCell((short) 1);
	        cell.setCellValue("序号");
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 2);  
	        cell.setCellValue("发货日期");  
	        cell.setCellStyle(cellStyle); 
	        cell = row.createCell((short) 3);  
	        cell.setCellValue("发货单位");  
	        cell.setCellStyle(cellStyle);  
	        cell = row.createCell((short) 4);  
	        cell.setCellValue("发货单位电话");  
	        cell.setCellStyle(cellStyle);  
	        cell = row.createCell((short) 5);  
	        cell.setCellValue("收货单位");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 6);  
	        cell.setCellValue("收货单位电话");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 7);  
	        cell.setCellValue("始发站");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 8);  
	        cell.setCellValue("终点站");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 9);  
	        cell.setCellValue("货物名称");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 10);  
	        cell.setCellValue("包装");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 11);  
	        cell.setCellValue("件数");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 12);  
	        cell.setCellValue("保价费");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 13);  
	        cell.setCellValue("短驳费");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 14);  
	        cell.setCellValue("合计运费");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 15);  
	        cell.setCellValue("代收货款");  
	        cell.setCellStyle(cellStyle);	        
	        cell = row.createCell((short) 16);  
	        cell.setCellValue("到付");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 17);  
	        cell.setCellValue("已付");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 18);  
	        cell.setCellValue("月结");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 19);  
	        cell.setCellValue("签回单");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 20);  
	        cell.setCellValue("提货方式");  
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell((short) 21);  
	        cell.setCellValue("签字人");  
	        cell.setCellStyle(cellStyle);
	        
	        sheet.setColumnWidth(1, CODE_WIDTH);
	        sheet.setColumnWidth(2, SEND_DATE_WIDTH); 
	        sheet.setColumnWidth(3, SENDER_WIDTH);
	        sheet.setColumnWidth(4, SEND_PHONE_WIDTH);
	        sheet.setColumnWidth(5, RECEIVER_WIDTH);
	        sheet.setColumnWidth(6, RECEIVE_PHONE_WIDTH);
	        sheet.setColumnWidth(7, SOURCE_WIDTH);
	        sheet.setColumnWidth(8, DESTINATION_WIDTH);
	        sheet.setColumnWidth(9, PRODUCT_WIDTH);
	        sheet.setColumnWidth(10, PACKAGE_WIDTH);
	        sheet.setColumnWidth(11, NUMBER_WIDTH);
	        sheet.setColumnWidth(12, PRICE_FEE_WIDTH);
	        sheet.setColumnWidth(13, DELIVER_FEE_WIDTH);
	        sheet.setColumnWidth(14, TOTAL_FEE_WIDTH);
	        sheet.setColumnWidth(15, RECEIVE_FEE_WIDTH);	        
	        sheet.setColumnWidth(16, GET_FEE_WIDTH);
	        sheet.setColumnWidth(17, PAID_FEE_WIDTH);
	        sheet.setColumnWidth(18, MONTH_FEE_WIDTH);
	        sheet.setColumnWidth(19, BACK_BILL_WIDTH);	        
	        sheet.setColumnWidth(20, FETCH_WAY_WIDTH);	        
	        sheet.setColumnWidth(21, SIGNER_WIDTH);
	        
	        int priceFee = 0, bargeFee = 0;
	        int number = 0, totalFee = 0, deliverFee = 0;
	        int getPayFee = 0, cashPayFee = 0, backPayFee = 0;
	        
	        for (int i = 0; i<billList.size(); i++) {
	        	Base base = billList.get(i);
	        	if (base instanceof Bill) {
	        		Bill bill = (Bill)base;
		        	number+=bill.getNumber();
		        	priceFee+=bill.getPriceFee();
		        	bargeFee+=bill.getBargeFee();
		        	totalFee+=bill.getTotalFee();
		        	deliverFee+=bill.getDeliverFee();
		        	
		        	if (bill.getIsGetPay()==Bill.BASE_TRUE) {
		        		getPayFee+=bill.getTotalFee();
		        	} else if (bill.getIsCashPay()==Bill.BASE_TRUE) {
		        		cashPayFee+=bill.getTotalFee();
		        	} else if (bill.getIsBackPay()==Bill.BASE_TRUE) {
		        		backPayFee+=bill.getTotalFee();
		        	}
		        	
		            row = sheet.createRow((int) i + 4);
		            row.setHeight((short) (10*64));
		            
		            cell = row.createCell((short) 1); 
		            cell.setCellValue(bill.getCode());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 2);  
			        cell.setCellValue(DateUtil.formatDate(bill.getSendDate()));  
			        cell.setCellStyle(cellStyle); 
			        cell = row.createCell((short) 3);  
			        cell.setCellValue(bill.getSenderUnit());  
			        cell.setCellStyle(cellStyle);  
			        cell = row.createCell((short) 4);  
			        cell.setCellValue(bill.getSenderPhone());  
			        cell.setCellStyle(cellStyle);  
			        cell = row.createCell((short) 5);  
			        cell.setCellValue(bill.getReceiverUnit());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 6);  
			        cell.setCellValue(bill.getReceiverPhone());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 7);  
			        cell.setCellValue(bill.getSource());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 8);  
			        cell.setCellValue(bill.getDestination());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 9);  
			        cell.setCellValue(bill.getName());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 10);  
			        cell.setCellValue(bill.getPack());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 11);  
			        cell.setCellValue(bill.getNumStr());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 12);  
			        cell.setCellValue(bill.getPriceFee());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 13);  
			        cell.setCellValue(bill.getBargeFee());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 14);  
			        cell.setCellValue(bill.getTotalFee());  
			        cell.setCellStyle(cellStyle);
			        cell = row.createCell((short) 15);  
			        cell.setCellValue(bill.getDeliverFee());  
			        cell.setCellStyle(cellStyle);
			        
			        if (bill.getIsGetPay()==Bill.BASE_TRUE) {
			        	cell = row.createCell((short) 16);  
				        cell.setCellValue("√");  
				        cell.setCellStyle(cellStyle);
			        } else {
			        	cell = row.createCell((short) 16);  
				        cell.setCellValue("");  
				        cell.setCellStyle(cellStyle);
			        }
			        
			        if (bill.getIsCashPay()==Bill.BASE_TRUE) {
			        	cell = row.createCell((short) 17);  
				        cell.setCellValue("√");  
				        cell.setCellStyle(cellStyle);
			        } else {
			        	cell = row.createCell((short) 17);  
				        cell.setCellValue("");  
				        cell.setCellStyle(cellStyle);
			        }
			        
			        if (bill.getIsBackPay()==Bill.BASE_TRUE) {
			        	cell = row.createCell((short) 18);  
				        cell.setCellValue("√");  
				        cell.setCellStyle(cellStyle);
			        } else {
			        	cell = row.createCell((short) 18);  
				        cell.setCellValue("");  
				        cell.setCellStyle(cellStyle);
			        }
			        
			        if (bill.getIsBackBill()==Bill.BASE_TRUE) {
			        	cell = row.createCell((short) 19);  
				        cell.setCellValue("√ "+"("+bill.getBackBillNum()+"份)");  
				        cell.setCellStyle(cellStyle);
			        } else {
			        	cell = row.createCell((short) 19);  
				        cell.setCellValue("");  
				        cell.setCellStyle(cellStyle);
			        }
			        
			        if (bill.getIsSelfGet()==Bill.BASE_TRUE) {
			        	cell = row.createCell((short) 20);  
				        cell.setCellValue("自提");  
				        cell.setCellStyle(cellStyle);
			        } else if (bill.getIsSendProd()==Bill.BASE_TRUE) {
			        	cell = row.createCell((short) 20);  
				        cell.setCellValue("送货");  
				        cell.setCellStyle(cellStyle);
			        }
			        
			        cell = row.createCell((short) 21);  
			        cell.setCellValue(bill.getSigner());  
			        cell.setCellStyle(cellStyle);
	        	} else if (base instanceof Customer) {	        		
	        		Customer customer = (Customer)base;
	        		row = sheet.createRow((int) i + 4);	        		
		            row.setHeight((short) (10*64));
		            cell = row.createCell((short) 1); 
		            cell.setCellValue("发货单位: "+customer.getName());  
			        cell.setCellStyle(senderCellStyle);
			        sheet.addMergedRegion(new Region(i + 4, (short)1, i + 4, (short)21));
			        
	        	}
	        }
	        
	        row = sheet.createRow(billList.size() + 4);
            row.setHeight((short) (10*64));
            cell = row.createCell((short) 1);
	        cell.setCellValue("合计");
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 2);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle); 
	        cell = row.createCell((short) 3);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);  
	        cell = row.createCell((short) 4);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);  
	        cell = row.createCell((short) 5);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 6);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 7);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 8);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 9);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 10);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 11);  
	        cell.setCellValue(String.valueOf(number));  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 12);  
	        cell.setCellValue(String.valueOf(priceFee));  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 13);  
	        cell.setCellValue(String.valueOf(bargeFee));  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 14);  
	        cell.setCellValue(String.valueOf(totalFee));  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 15);  
	        cell.setCellValue(String.valueOf(deliverFee));  
	        cell.setCellStyle(cellStyle);	        
	        cell = row.createCell((short) 16);  
	        cell.setCellValue(String.valueOf(getPayFee));  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 17);  
	        cell.setCellValue(String.valueOf(cashPayFee));  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 18);  
	        cell.setCellValue(String.valueOf(backPayFee));  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 19);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 20);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);	        
	        cell = row.createCell((short) 21);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        
	        try {  
	            FileOutputStream fout = new FileOutputStream(fileName);  
	            workBook.write(fout);  
	            fout.close();
	            return SUCCESS;
	        } catch (Exception e) {  
	            e.printStackTrace(); 
	            return FILE_EXIST;
	        }
		}
		return FAILURE;
	}
	
	@SuppressWarnings("deprecation")
	public static int exportCustomerBill(String fileName, String title, List<Bill> billList) {
		if (StringUtil.isNotBlank(fileName) && billList!=null && billList.size()>0) {
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet("物流单据汇总");
			sheet.setHorizontallyCenter(true);
			sheet.setVerticallyCenter(false);
			HSSFPrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setLandscape(true);
			printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
			printSetup.setHeaderMargin(1.0);
			printSetup.setFooterMargin(1.0);
			
			HSSFRow row = sheet.createRow(0);
			HSSFCellStyle cellStyle = workBook.createCellStyle();
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	        
	        HSSFFont font = workBook.createFont();
	        font.setFontName(HSSFFont.FONT_ARIAL);//字体
	        font.setFontHeightInPoints((short) 18);//字号 
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
	        font.setColor(HSSFColor.BLACK.index);//颜色
	        cellStyle.setFont(font);
	        cellStyle.setWrapText(true);
	        HSSFCell cell = row.createCell((short)1);
	        cell.setCellValue(title);
	        cell.setCellStyle(cellStyle);
	        sheet.addMergedRegion(new Region(0,(short)1, 1 , (short)13));
	        
	        row = sheet.createRow(3);
	        row.setHeight((short) (10*64));
	        cellStyle = workBook.createCellStyle();  
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
	        cellStyle.setBorderLeft((short)1);
	        cellStyle.setBorderRight((short)1);
	        cellStyle.setBorderTop((short)1);
	        cellStyle.setBorderBottom((short)1);
	        cellStyle.setWrapText(true);
	        
	        HSSFCellStyle senderCellStyle = workBook.createCellStyle();  
    		senderCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    		senderCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    		senderCellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
    		senderCellStyle.setBorderLeft((short)1);
    		senderCellStyle.setBorderRight((short)1);
    		senderCellStyle.setBorderTop((short)1);
    		senderCellStyle.setBorderBottom((short)1);
    		senderCellStyle.setWrapText(true);
    		
    		cell = row.createCell((short) 1);
	        cell.setCellValue("序号");
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 2);  
	        cell.setCellValue("发货日期");  
	        cell.setCellStyle(cellStyle); 
	        cell = row.createCell((short) 3);  
	        cell.setCellValue("收货单位");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 4);  
	        cell.setCellValue("收货单位电话");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 5);  
	        cell.setCellValue("终点站");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 6);  
	        cell.setCellValue("货物名称");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 7);  
	        cell.setCellValue("包装");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 8);  
	        cell.setCellValue("件数");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 9);  
	        cell.setCellValue("合计运费");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 10);  
	        cell.setCellValue("代收货款");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 11);  
	        cell.setCellValue("签回单");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 12);  
	        cell.setCellValue("提货方式");  
	        cell.setCellStyle(cellStyle);	        
	        cell = row.createCell((short) 13);  
	        cell.setCellValue("签字人");  
	        cell.setCellStyle(cellStyle);
	        
	        sheet.setColumnWidth(1, CODE_WIDTH);
	        sheet.setColumnWidth(2, SEND_DATE_WIDTH);
	        sheet.setColumnWidth(3, RECEIVER_WIDTH);
	        sheet.setColumnWidth(4, RECEIVE_PHONE_WIDTH);
	        sheet.setColumnWidth(5, DESTINATION_WIDTH);
	        sheet.setColumnWidth(6, PRODUCT_WIDTH);
	        sheet.setColumnWidth(7, PACKAGE_WIDTH);
	        sheet.setColumnWidth(8, NUMBER_WIDTH);
	        sheet.setColumnWidth(9, TOTAL_FEE_WIDTH);
	        sheet.setColumnWidth(10, RECEIVE_FEE_WIDTH);	        
	        sheet.setColumnWidth(11, BACK_BILL_WIDTH);	        
	        sheet.setColumnWidth(12, FETCH_WAY_WIDTH);	        
	        sheet.setColumnWidth(13, SIGNER_WIDTH);
	        
	        int number = 0, totalFee = 0, deliverFee = 0;
	        
	        for (int i = 0; i<billList.size(); i++) {
	        	Bill bill = billList.get(i);
	        	number+=bill.getNumber();
	        	totalFee+=bill.getTotalFee();
	        	deliverFee+=bill.getDeliverFee();

	            row = sheet.createRow((int) i + 4);
	            row.setHeight((short) (10*64));
	            
	            cell = row.createCell((short) 1); 
	            cell.setCellValue(bill.getCode());  
		        cell.setCellStyle(cellStyle);
		        cell = row.createCell((short) 2);  
		        cell.setCellValue(DateUtil.formatDate(bill.getSendDate()));  
		        cell.setCellStyle(cellStyle);  
		        cell = row.createCell((short) 3);  
		        cell.setCellValue(bill.getReceiverUnit());  
		        cell.setCellStyle(cellStyle);
		        cell = row.createCell((short) 4);  
		        cell.setCellValue(bill.getReceiverPhone());  
		        cell.setCellStyle(cellStyle);
		        cell = row.createCell((short) 5);  
		        cell.setCellValue(bill.getDestination());  
		        cell.setCellStyle(cellStyle);
		        cell = row.createCell((short) 6);  
		        cell.setCellValue(bill.getName());  
		        cell.setCellStyle(cellStyle);
		        cell = row.createCell((short) 7);  
		        cell.setCellValue(bill.getPack());  
		        cell.setCellStyle(cellStyle);
		        cell = row.createCell((short) 8);  
		        cell.setCellValue(bill.getNumStr());  
		        cell.setCellStyle(cellStyle);
		        cell = row.createCell((short) 9);  
		        cell.setCellValue(bill.getTotalFee());  
		        cell.setCellStyle(cellStyle);
		        cell = row.createCell((short) 10);  
		        cell.setCellValue(bill.getDeliverFee());  
		        cell.setCellStyle(cellStyle);
		        
		        if (bill.getIsBackBill()==Bill.BASE_TRUE) {
		        	cell = row.createCell((short) 11);  
			        cell.setCellValue("√ "+"("+bill.getBackBillNum()+"份)");  
			        cell.setCellStyle(cellStyle);
		        } else {
		        	cell = row.createCell((short) 11);  
			        cell.setCellValue("");  
			        cell.setCellStyle(cellStyle);
		        }
		        
		        if (bill.getIsSelfGet()==Bill.BASE_TRUE) {
		        	cell = row.createCell((short) 12);  
			        cell.setCellValue("自提");  
			        cell.setCellStyle(cellStyle);
		        } else if (bill.getIsSendProd()==Bill.BASE_TRUE) {
		        	cell = row.createCell((short) 12);  
			        cell.setCellValue("送货");  
			        cell.setCellStyle(cellStyle);
		        }
		        
		        cell = row.createCell((short) 13);  
		        cell.setCellValue(bill.getSigner());  
		        cell.setCellStyle(cellStyle);
	        }
	        
	        row = sheet.createRow(billList.size() + 4);
            row.setHeight((short) (10*64));
            cell = row.createCell((short) 1);
	        cell.setCellValue("合计");
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 2);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);  
	        cell = row.createCell((short) 3);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 4);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 5);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 6);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 7);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 8);  
	        cell.setCellValue(String.valueOf(number));  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 9);  
	        cell.setCellValue(String.valueOf(totalFee));  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 10);  
	        cell.setCellValue(String.valueOf(deliverFee));  
	        cell.setCellStyle(cellStyle);	        
	        cell = row.createCell((short) 11);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        cell = row.createCell((short) 12);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);	        
	        cell = row.createCell((short) 13);  
	        cell.setCellValue("");  
	        cell.setCellStyle(cellStyle);
	        
	        try {  
	            FileOutputStream fout = new FileOutputStream(fileName);  
	            workBook.write(fout);  
	            fout.close();
	            return SUCCESS;
	        } catch (Exception e) {  
	            e.printStackTrace(); 
	            return FILE_EXIST;
	        }
		}
		return FAILURE;
	}
}

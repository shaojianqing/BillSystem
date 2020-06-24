package com.sys.util;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

import com.sys.bo.Base;
import com.sys.bo.Bill;
import com.sys.bo.BillDetail;
import com.sys.printer.BillAdminPrinter;
import com.sys.printer.BillDataPrinter;
import com.sys.printer.BillDetailPrinter;
import com.sys.printer.BillListPrinter;

public class PrintUtil {
	public static void printBillDetail(Bill bill) {
		 Book book = new Book();
		 PageFormat pf = new PageFormat();
		 pf.setOrientation(PageFormat.PORTRAIT);
		 Paper p = new Paper();
		 p.setSize(512, 372);//ֽ�Ŵ�С 
		 p.setImageableArea(10, 10, 512, 372);
		 pf.setPaper(p);
		 //�� PageFormat �� Printable ��ӵ����У����һ��ҳ��
		 book.append(new BillDetailPrinter(bill), pf);	 
		 //��ȡ��ӡ�������
		 PrinterJob job = PrinterJob.getPrinterJob();      
	     // ���ô�ӡ��
		 job.setPageable(book);
		  
		 try {       
	     	job.print();
		 } catch (PrinterException e) {
			 e.printStackTrace();
	     }
	}
	
	public static void printBillList(List<Base> billList, BillDetail billDetail) {
		 Book book = new Book();
		 PageFormat pf = new PageFormat();
		 pf.setOrientation(PageFormat.PORTRAIT);
		 Paper p = new Paper();
		 p.setSize(590, 860);//ֽ�Ŵ�С 
		 p.setImageableArea(10, 10, 590, 840);
		 pf.setPaper(p);
		 //�� PageFormat �� Printable ��ӵ����У����һ��ҳ��
		 book.append(new BillListPrinter(billList, billDetail), pf);	 
		 //��ȡ��ӡ�������
		 PrinterJob job = PrinterJob.getPrinterJob();      
	     // ���ô�ӡ��
		 job.setPageable(book);
		  
		 try {       
	     	job.print();
		 } catch (PrinterException e) {
			 e.printStackTrace();
	     }
	}
	
	public static void printBillAdmin(List<Bill> billList, BillDetail billDetail) {
		 Book book = new Book();
		 PageFormat pf = new PageFormat();
		 pf.setOrientation(PageFormat.PORTRAIT);
		 Paper p = new Paper();
		 p.setSize(860, 590);//ֽ�Ŵ�С 
		 p.setImageableArea(10, 10, 840, 590);
		 pf.setPaper(p);
		 //�� PageFormat �� Printable ��ӵ����У����һ��ҳ��
		 book.append(new BillAdminPrinter(billList, billDetail), pf);	 
		 //��ȡ��ӡ�������
		 PrinterJob job = PrinterJob.getPrinterJob();      
	     // ���ô�ӡ��
		 job.setPageable(book);
		  
		 try {       
	     	job.print();
		 } catch (PrinterException e) {
			 e.printStackTrace();
	     }
	}
	
	public static void printBillData(List<Bill> billList, String title) {
		 Book book = new Book();
		 PageFormat pf = new PageFormat();
		 pf.setOrientation(PageFormat.PORTRAIT);
		 Paper p = new Paper();
		 p.setSize(590, 860);//ֽ�Ŵ�С 
		 p.setImageableArea(10, 10, 590, 840);
		 pf.setPaper(p);
		 //�� PageFormat �� Printable ��ӵ����У����һ��ҳ��
		 book.append(new BillDataPrinter(billList, title), pf);	 
		 //��ȡ��ӡ�������
		 PrinterJob job = PrinterJob.getPrinterJob();      
	     // ���ô�ӡ��
		 job.setPageable(book);
		  
		 try {       
	     	job.print();
		 } catch (PrinterException e) {
			 e.printStackTrace();
	     }
	}
}

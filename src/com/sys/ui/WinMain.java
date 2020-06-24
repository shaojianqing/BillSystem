package com.sys.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sys.bo.Base;
import com.sys.bo.Bill;
import com.sys.bo.Customer;
import com.sys.service.BillService;
import com.sys.service.CustomerService;
import com.sys.ui.data.BillContProvider;
import com.sys.util.DataUtil;
import com.sys.util.DateUtil;
import com.sys.util.PrintUtil;
import com.sys.util.StringUtil;
import com.sys.widget.IExportClient;
import com.sys.widget.IMessageClient;
import com.sys.widget.ISelectCustomer;
import com.sys.widget.InfoBox;
import com.sys.widget.MessageBox;
import com.sys.widget.WinExportDate;

public class WinMain implements IMessageClient, IExportClient, ISelectCustomer {

	public final static String UNDEFINED_SENDER = "undefinedSender";
	
	private final static int PRINT_DATA_PAGE_NUM = 22;
	
	private Display display;
	
	protected Shell shell;
	
	private ToolBar toolBar;
	
	private ToolItem newBillItem;
	
	private ToolItem updateBillItem;
	
	private ToolItem removeBillItem;
	
	private ToolItem attachBillItem;
	
	private ToolItem exportDataItem;
	
	private ToolItem printBillItem;
	
	private ToolItem printListItem;
	
	private ToolItem custInfoItem;
	
	private ToolItem userInfoItem;
	
	private ToolItem regionInfoItem;
	
	private ToolItem softInfoItem;
	
	private Label separateLine1;
	
	private Label separateLine2;
	
	private TableViewer mainGrid;
	
	private Table mainTable;
	
	private Text keywordTxt;
	
	private Button senderUnitBtn;
	
	private DateTime startDate;
	
	private DateTime endDate;
	
	private Button searchBtn;
	
	private Button resetBtn;
	
	private Button printBtn;
	
	private List<Bill> billList;
	
	private Bill currentBill;
	
	private BillService billService = BillService.getInstance();
	
	private CustomerService customerService = CustomerService.getInstance();
	
	private WinExportDate winBillExport;

	public void open() {
		display = Display.getDefault();
		initMainView();
		initDataView();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void initMainView() {
		shell = new Shell(SWT.CLOSE | SWT.MIN | SWT.MAX);
		shell.setSize(new Point(1000, 700));
		shell.setMaximized(true);
		shell.setText("物流单据管理系统");
		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				toolBar.setSize(shell.getSize().x, 48);
				separateLine1.setSize(shell.getSize().x, 2);
				separateLine2.setSize(shell.getSize().x, 2);
				mainTable.setBounds(2, 96, shell.getSize().x-10, shell.getSize().y-126);
			}
		});
		
		toolBar = new ToolBar(shell, SWT.RIGHT);
		toolBar.setBounds(0, 0, shell.getSize().x, 48);
		
		newBillItem = new ToolItem(toolBar, SWT.FLAT);
		newBillItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_new_bill.png"));
		newBillItem.setWidth(80);
		newBillItem.setText("\u65B0\u5EFA\u7269\u6D41\u5355\u636E");
		newBillItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinBillNew winBillNew = new WinBillNew(display, WinMain.this);
				winBillNew.open();
			}
		});
		
		updateBillItem = new ToolItem(toolBar, SWT.FLAT);
		updateBillItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_update_bill.png"));
		updateBillItem.setWidth(80);
		updateBillItem.setText("\u66F4\u65B0\u7269\u6D41\u5355\u636E");
		updateBillItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinMain.this.update();
			}
		});
		
		removeBillItem = new ToolItem(toolBar, SWT.FLAT);
		removeBillItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_remove_bill.png"));
		removeBillItem.setWidth(80);
		removeBillItem.setText("\u5220\u9664\u7269\u6D41\u5355\u636E");
		removeBillItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinMain.this.remove();
			}
		});
		
		attachBillItem = new ToolItem(toolBar, SWT.FLAT);
		attachBillItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/icon_attach_bill.png"));
		attachBillItem.setWidth(80);
		attachBillItem.setText("\u9644\u52A0\u7269\u6D41\u5355\u636E");
		attachBillItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinMain.this.attach();
			}
		});
		
		printBillItem = new ToolItem(toolBar, SWT.FLAT);
		printBillItem.setText("\u6253\u5370\u7269\u6D41\u5355\u636E");
		printBillItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_print_bill.png"));
		printBillItem.setWidth(80);
		printBillItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinMain.this.printBill();
			}
		});
		
		printListItem = new ToolItem(toolBar, SWT.FLAT);
		printListItem.setText("\u6253\u5370\u5355\u636E\u5217\u8868");
		printListItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_print_list.png"));
		printListItem.setWidth(80);
		printListItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinMain.this.printList();
			}
		});
		
		custInfoItem = new ToolItem(toolBar, SWT.NONE);
		custInfoItem.setWidth(80);
		custInfoItem.setText("\u76f8\u5173\u5ba2\u6237\u7ba1\u7406");
		custInfoItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_cust_info.png"));
		custInfoItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinCustomer winCustomer = new WinCustomer(display);
				winCustomer.open();
			}
		});	
		
		userInfoItem = new ToolItem(toolBar, SWT.NONE);
		userInfoItem.setWidth(80);
		userInfoItem.setText("\u76F8\u5173\u4EBA\u5458\u7BA1\u7406");
		userInfoItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_user_info.png"));
		userInfoItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinUserInfo winUserInfo = new WinUserInfo(display);
				winUserInfo.open();
			}
		});
		
		regionInfoItem = new ToolItem(toolBar, SWT.NONE);
		regionInfoItem.setWidth(80);
		regionInfoItem.setText("\u76F8\u5173\u533A\u57DF\u7BA1\u7406");
		regionInfoItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_region.png"));
		regionInfoItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinRegion winRegion = new WinRegion(display);
				winRegion.open();
			}
		});
		
		exportDataItem = new ToolItem(toolBar, SWT.FLAT);
		exportDataItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/icon_export.png"));
		exportDataItem.setWidth(80);
		exportDataItem.setText("\u5BFC\u51FA\u7535\u5B50\u8868\u683C");
		exportDataItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinMain.this.exportBill();
			}
		});
		
		softInfoItem = new ToolItem(toolBar, SWT.NONE);
		softInfoItem.setWidth(80);
		softInfoItem.setText("\u8F6F\u4EF6\u7248\u672C\u4FE1\u606F");
		softInfoItem.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_version.png"));
		softInfoItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinInfo winInfo = new WinInfo(display);
				winInfo.open();
			}
		});		
		
		separateLine1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separateLine1.setBounds(0, 50, shell.getSize().x, 2);
		
		separateLine2 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separateLine2.setBounds(0, 90, shell.getSize().x, 2);
		
		mainGrid = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		mainTable = mainGrid.getTable();
		mainTable.setLinesVisible(true);
		mainTable.setHeaderVisible(true);
		mainTable.setBounds(2, 96, shell.getSize().x-10, shell.getSize().y-126);
		mainGrid.setContentProvider(new BillContProvider());
		mainTable.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.width = mainTable.getGridLineWidth();
				event.height = 36;
			}
		});
		
		final TableViewerColumn codeColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
		codeColumn.getColumn().setText("序号");
		codeColumn.getColumn().setWidth(80);
		codeColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return ((Bill)element).getCode();
		    }
		});

        final TableViewerColumn senderColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        senderColumn.getColumn().setResizable(true);
        senderColumn.getColumn().setWidth(250);
        senderColumn.getColumn().setText("托运人(电话)");
        senderColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		    	Bill bill = (Bill)element;
		        return bill.getSenderUnit()+"("+bill.getSenderPhone()+")";
		    }
		});
        
        final TableViewerColumn destinationColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        destinationColumn.getColumn().setResizable(true);
        destinationColumn.getColumn().setWidth(120);
        destinationColumn.getColumn().setText("到站");
        destinationColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return ((Bill)element).getDestination();
		    }
		});
        
        final TableViewerColumn receiverColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        receiverColumn.getColumn().setResizable(true);
        receiverColumn.getColumn().setWidth(250);
        receiverColumn.getColumn().setText("提货人(电话)");
        receiverColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		    	Bill bill = (Bill)element;
		        return bill.getReceiverUnit()+"("+bill.getReceiverPhone()+")";
		    }
		});
        
        final TableViewerColumn numberColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        numberColumn.getColumn().setResizable(true);
        numberColumn.getColumn().setWidth(60);
        numberColumn.getColumn().setText("件数");
        numberColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return ((Bill)element).getNumStr();
		    }
		});
        
        final TableViewerColumn totalFeeColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        totalFeeColumn.getColumn().setResizable(true);
        totalFeeColumn.getColumn().setWidth(100);
        totalFeeColumn.getColumn().setText("合计运费");
        totalFeeColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return String.valueOf(((Bill)element).getTotalFee());
		    }
		});
        
        final TableViewerColumn deliverFeeColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        deliverFeeColumn.getColumn().setResizable(true);
        deliverFeeColumn.getColumn().setWidth(100);
        deliverFeeColumn.getColumn().setText("代收货款");
        deliverFeeColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return String.valueOf(((Bill)element).getDeliverFee());
		    }
		});
        
        final TableViewerColumn getPayColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        getPayColumn.getColumn().setResizable(true);
        getPayColumn.getColumn().setWidth(60);
        getPayColumn.getColumn().setText("到付");
        getPayColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return formatBoolValue(((Bill)element).getIsGetPay());
		    }
		});
        
        final TableViewerColumn hasPayColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        hasPayColumn.getColumn().setResizable(true);
        hasPayColumn.getColumn().setWidth(60);
        hasPayColumn.getColumn().setText("已付");
        hasPayColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return formatBoolValue(((Bill)element).getIsCashPay());
		    }
		});
        
        final TableViewerColumn monthPayColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        monthPayColumn.getColumn().setResizable(true);
        monthPayColumn.getColumn().setWidth(60);
        monthPayColumn.getColumn().setText("月结");
        monthPayColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return formatBoolValue(((Bill)element).getIsBackPay());
		    }
		});
        
        final TableViewerColumn billBackColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        billBackColumn.getColumn().setResizable(true);
        billBackColumn.getColumn().setWidth(60);
        billBackColumn.getColumn().setText("回单");
        billBackColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		    	Bill bill = (Bill)element;
		    	if (bill.getIsBackBill()==Bill.BASE_TRUE) {
		    		return formatBoolValue(bill.getIsBackBill())+"("+bill.getBackBillNum()+"份)";
		    	} else {
		    		 return formatBoolValue(bill.getIsBackBill());
		    	}
		    }
		});
        
        Label keywordLbl = new Label(shell, SWT.NONE);
        keywordLbl.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        keywordLbl.setBounds(10, 64, 51, 20);
        keywordLbl.setText("\u5173\u952E\u5B57:");
        
        keywordTxt = new Text(shell, SWT.BORDER);
        keywordTxt.setBounds(66, 58, 160, 24);
        
        Label senderLbl = new Label(shell, SWT.NONE);
        senderLbl.setText("\u53d1\u8d27\u5355\u4f4d:");
        senderLbl.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        senderLbl.setBounds(240, 64, 60, 20);
        
        senderUnitBtn = new Button(shell, SWT.NONE);
        senderUnitBtn.setBounds(302, 56, 130, 28);
        senderUnitBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				WinMain.this.selectSenderUnit();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
        
        Label startDateLbl = new Label(shell, SWT.NONE);
        startDateLbl.setText("\u5F00\u59CB\u65E5\u671F:");
        startDateLbl.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        startDateLbl.setBounds(440, 64, 60, 20);
        
        startDate = new DateTime(shell, SWT.BORDER);
        startDate.setBounds(502, 58, 116, 24);
        
        Label endDateLbl = new Label(shell, SWT.NONE);
        endDateLbl.setText("\u7ED3\u675F\u65E5\u671F:");
        endDateLbl.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        endDateLbl.setBounds(640, 64, 60, 20);
        
        endDate = new DateTime(shell, SWT.BORDER);
        endDate.setBounds(702, 58, 116, 24);
        
        searchBtn = new Button(shell, SWT.NONE);
        searchBtn.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_search_small.png"));
        searchBtn.setBounds(840, 56, 100, 28);
        searchBtn.setText("\u67E5\u8BE2");
        searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinMain.this.startSearch();
			}
		});		
        
        resetBtn = new Button(shell, SWT.NONE);
        resetBtn.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_reset_small.png"));
        resetBtn.setText("\u91CD\u7F6E");
        resetBtn.setBounds(960, 56, 100, 28);
        resetBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinMain.this.resetSearch();
			}
		});
        
        printBtn = new Button(shell, SWT.NONE);
        printBtn.setImage(SWTResourceManager.getImage(WinMain.class, "/res/images/ico_print_bill_small.png"));
        printBtn.setText("\u6253\u5370");
        printBtn.setBounds(1080, 56, 100, 28);
        printBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinMain.this.printData();
			}
		});
	}

	public void initDataView() 
	{
		senderUnitBtn.setText("全部");
		senderUnitBtn.setData("0");
		
		String keyword = StringUtil.trimString(this.keywordTxt.getText());
		String senderUnitId = (String)this.senderUnitBtn.getData();
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.startDate.getYear(), this.startDate.getMonth(), this.startDate.getDay());		
		Date startDate = calendar.getTime();
		calendar = Calendar.getInstance();
		calendar.set(this.endDate.getYear(), this.endDate.getMonth(), this.endDate.getDay());
		Date endDate = calendar.getTime();
		
		if (!startDate.after(endDate)) {
			billList = billService.queryBillByCondition(keyword, senderUnitId, startDate, endDate);
			mainGrid.setInput(billList);
		}
	}
	
	protected void startSearch() 
	{
		String keyword = StringUtil.trimString(this.keywordTxt.getText());
		String senderUnitId = (String)this.senderUnitBtn.getData();
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.startDate.getYear(), this.startDate.getMonth(), this.startDate.getDay());		
		Date startDate = calendar.getTime();
		calendar = Calendar.getInstance();
		calendar.set(this.endDate.getYear(), this.endDate.getMonth(), this.endDate.getDay());
		Date endDate = calendar.getTime();
		
		if (!startDate.after(endDate)) {
			billList = billService.queryBillByCondition(keyword, senderUnitId, startDate, endDate);
			mainGrid.setInput(billList);
		} else {
			InfoBox.showInfo(display, "开始日期不能晚于结束日期哦^_^");
		}	
	}
	
	protected void selectSenderUnit() 
	{
		WinSelectCust winSelectCust = new WinSelectCust(display, this);
		winSelectCust.open();
	}
	
	protected void printData() 
	{
		if (billList != null && billList.size()>0) {			
			Calendar calendar = Calendar.getInstance();
			calendar.set(this.startDate.getYear(), this.startDate.getMonth(), this.startDate.getDay());		
			Date startDate = calendar.getTime();
			calendar = Calendar.getInstance();
			calendar.set(this.endDate.getYear(), this.endDate.getMonth(), this.endDate.getDay());
			Date endDate = calendar.getTime();
			String senderUnitId = (String)this.senderUnitBtn.getData();
			String senderUnit = "";
			if (StringUtil.isNotBlank(senderUnitId) && (!"0".equals(senderUnitId))) {
				senderUnit = this.senderUnitBtn.getText();
			}
			
			String title = String.format("%s至%s%s的物流单据汇总", DateUtil.formatDate(startDate), 
																  DateUtil.formatDate(endDate), 
																  senderUnit);
			int page = 0;
			if (billList.size()%PRINT_DATA_PAGE_NUM==0) {
				page=billList.size()/PRINT_DATA_PAGE_NUM;
			} else {
				page=billList.size()/PRINT_DATA_PAGE_NUM+1;
			}
			for (int i=0;i<page;++i) {
				int limit = (i+1)*PRINT_DATA_PAGE_NUM>billList.size()?billList.size():(i+1)*PRINT_DATA_PAGE_NUM;
				List<Bill> billPage = billList.subList(i*PRINT_DATA_PAGE_NUM, limit);				
				PrintUtil.printBillData(billPage, title);
			}
			InfoBox.showInfo(display, "本次打印共需要打印"+page+"页内容");
		} else {
			InfoBox.showInfo(display, "没有需要打印的单据列表^_^");
		}
	}
	
	protected void resetSearch() 
	{
		this.keywordTxt.setText("");
		this.senderUnitBtn.setText("全部");
		this.senderUnitBtn.setData("0");
		Calendar calendar = Calendar.getInstance();	
		this.startDate.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		this.endDate.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		
		Date startDate = calendar.getTime();
		Date endDate = calendar.getTime();
		
		String senderUnitId = (String)this.senderUnitBtn.getData();
		
		if (!startDate.after(endDate)) {
			billList = billService.queryBillByCondition(null, senderUnitId, startDate, endDate);
			mainGrid.setInput(billList);
		} else {
			InfoBox.showInfo(display, "开始日期不能晚于结束日期哦^_^");
		}	
	}
	
	private void printBill()
	{
		TableItem[] selectedItems = mainTable.getSelection();
		if (selectedItems!=null && selectedItems.length>0) {
			TableItem tableItem = selectedItems[0];
			Bill bill = (Bill)tableItem.getData();
			PrintUtil.printBillDetail(bill);
		} else {
			InfoBox.showInfo(display, "请选择要打印的单据^_^");
		}
	}
	
	protected void update() 
	{
		TableItem[] selectedItems = mainTable.getSelection();
		if (selectedItems!=null && selectedItems.length>0) {
			TableItem tableItem = selectedItems[0];
			Bill bill = (Bill)tableItem.getData();
			WinBillUpdate winBillUpdate = new WinBillUpdate(display, WinMain.this, bill);
			winBillUpdate.open();
		} else {
			InfoBox.showInfo(display, "请选择要更新的单据^_^");
		}
	}
	
	protected void remove() 
	{
		TableItem[] selectedItems = mainTable.getSelection();
		if (selectedItems!=null && selectedItems.length>0) {
			TableItem tableItem = selectedItems[0];
			currentBill = (Bill)tableItem.getData();
			if (currentBill!=null) {
				MessageBox.showInfo(display, "您真的要删除这张单据吗?", this);
			}
		} else {
			InfoBox.showInfo(display, "请选择要删除的单据^_^");
		}
	}
	
	protected void attach() 
	{
		TableItem[] selectedItems = mainTable.getSelection();
		if (selectedItems!=null && selectedItems.length>0) {
			TableItem tableItem = selectedItems[0];
			Bill bill = (Bill)tableItem.getData();
			WinBillAttach winBillAttach = new WinBillAttach(display, WinMain.this, bill);
			winBillAttach.open();
		} else {
			InfoBox.showInfo(display, "请选择要附加的前一张单据^_^");
		}
	}
	
	private void printList()
	{
		WinPrintList winPrintList = new WinPrintList(display, this);
		winPrintList.open();		
	}
	
	private void exportBill()
	{
		winBillExport = new WinExportDate(display, this, "导出汇总物流单据");
		winBillExport.open();
	}
	
	private String formatBoolValue(short value) {
		if (value == Bill.BASE_TRUE) {
			return "√";
		} else if (value == Bill.BASE_FALSE) {
			return "×";
		}
		return "×";
	}

	@Override
	public void confirmOperation() {
		if (currentBill!=null) {
			billService.removeBill(currentBill);
			initDataView();
		}
	}

	@Override
	public void selectExportDate(Date startDate, Date endDate) {
		
		List<Bill> billList = billService.queryBillByCondition(null, "0", startDate, endDate);
		if (billList!=null && billList.size()>0) {
			List<Base> baseList = new ArrayList<Base>();
			Map<String, List<Bill>> billListMap = new HashMap<String, List<Bill>>();
			
			for (int i=0;i<billList.size();++i) {
				Bill bill = billList.get(i);
				String key = UNDEFINED_SENDER;
				if (StringUtil.isNotBlank(bill.getSenderUnitId())) {
					key = bill.getSenderUnitId();
				}
				if (billListMap.containsKey(key)==false) {
					billListMap.put(key, new ArrayList<Bill>());
				}
				List<Bill> bills = billListMap.get(key);
				bills.add(bill);
			}
			
			Set<String> senderIds = billListMap.keySet();
			for (String senderId:senderIds) {
				Customer customer = customerService.getCustomerById(senderId);
				if (customer==null) {
					customer = new Customer(senderId, "未指定");
				}
				baseList.add(customer);
				List<Bill> bills = billListMap.get(senderId);
				baseList.addAll(bills);
			}
			
			String fileName=null,fileTitle=null;
			if (startDate.equals(endDate)) {				
				fileName = DateUtil.formatDate(startDate)+"物流单据汇总.xls";
				fileTitle = DateUtil.formatDate(startDate)+"物流单据汇总";
			} else {
				fileName = DateUtil.formatDate(startDate)+"至"+DateUtil.formatDate(endDate)+"物流单据汇总.xls";
				fileTitle = DateUtil.formatDate(startDate)+"至"+DateUtil.formatDate(endDate)+"物流单据汇总";
			}
			FileDialog exportDialog = new FileDialog(shell);
			exportDialog.setText("请选择所要导出的文件");
			exportDialog.setFileName(fileName);
			String filePath = exportDialog.open();
			if (StringUtil.isNotBlank(filePath)) {
				int status = DataUtil.exportBill(filePath, fileTitle, baseList);
				if (status==DataUtil.FILE_EXIST) {
					InfoBox.showInfo(display, "已有重名文件，请删除该文件后再次导出^_^");
				} else if (status==DataUtil.SUCCESS) {
					winBillExport.close();
					InfoBox.showInfo(display, "物流单据导出成功!");
				} else if (status==DataUtil.FAILURE) {
					InfoBox.showInfo(display, "物流单据导出失败!");
				}					
			}
		} else {
			InfoBox.showInfo(display, "所选时间段内暂无物流单据需要导出!");
		}
	}
	
	@Override
	public void selectCustomer(Customer customer) {
		if (customer!=null) {
			this.senderUnitBtn.setText(customer.getName());
			this.senderUnitBtn.setData(customer.getId());
		}
	}
}

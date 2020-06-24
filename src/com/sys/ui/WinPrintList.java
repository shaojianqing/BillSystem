package com.sys.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sys.bo.Base;
import com.sys.bo.Bill;
import com.sys.bo.BillDetail;
import com.sys.bo.Region;
import com.sys.bo.UserInfo;
import com.sys.service.BillService;
import com.sys.service.RegionService;
import com.sys.service.UserService;
import com.sys.ui.data.BillContProvider;
import com.sys.util.PrintUtil;
import com.sys.util.StringUtil;
import com.sys.widget.InfoBox;

public class WinPrintList extends Shell 
{	
	private final static int PRINT_PAGE_NUM = 20;
	
	private BillService billService = BillService.getInstance();
	
	private RegionService regionService = RegionService.getInstance();
	
	private UserService userService;
	
	private Rectangle screenBound;
	
	private List<Bill> billList;
	
	private Display display;
	
	private WinMain winMain;
	
	private Text vehicleTxt;
	
	private DateTime dateTime;
	
	private Combo operatorCmb;
	
	private Combo creatorCmb;
	
	private TableViewer mainGrid;
	
	private Table mainTable;
	
	private Button printListBtn;
	
	private Button printAdminBtn;
	
	private Button cancelBtn;
	
	private Label creatorLbl;
	
	private List<UserInfo> userInfoList;
	
	private Date createDate;
	
	public WinPrintList(final Display display, final WinMain winMain) 
	{
		super(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setSize(600, 388);
		this.userService = UserService.getInstance();
		this.userInfoList = userService.getUserInfoByQuery();
		this.display = display;
		this.winMain = winMain;
		
		Label titleLbl = new Label(this, SWT.NONE);
		titleLbl.setAlignment(SWT.CENTER);
		titleLbl.setFont(SWTResourceManager.getFont("微软雅黑", 24, SWT.NORMAL));
		titleLbl.setBounds(300, 20, 400, 49);
		titleLbl.setText("\u5BCC\u9633\u5E02\u8054\u5408\u5FEB\u8FD0\u516C\u53F8\u6E05\u5355");
		
		Label vehicleLbl = new Label(this, SWT.NONE);
		vehicleLbl.setBounds(10, 89, 40, 20);
		vehicleLbl.setText("\u8F66\u53F7\uFF1A");
		
		vehicleTxt = new Text(this, SWT.BORDER);
		vehicleTxt.setBounds(56, 86, 160, 24);
		
		operatorCmb = new Combo(this, SWT.NONE);
		operatorCmb.setBounds(300, 86, 120, 25);
		operatorCmb.setItems(userService.getUserNameList(userInfoList));
		operatorCmb.select(0);
		
		creatorCmb = new Combo(this, SWT.NONE);
		creatorCmb.setItems(userService.getUserNameList(userInfoList));
		creatorCmb.setBounds(516, 86, 120, 20);
		creatorCmb.select(0);
		
		mainGrid = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		mainTable = mainGrid.getTable();
		mainTable.setLinesVisible(true);
		mainTable.setHeaderVisible(true);
		mainTable.setBounds(10, 115, 974, 384);
		mainGrid.setContentProvider(new BillContProvider());
	    
		mainTable.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.width = mainTable.getGridLineWidth();
				event.height = 36;
			}
		});
		
		mainTable.addListener(SWT.MouseDown, new Listener() {  
            public void handleEvent(Event event) {
            	if (mainTable.getItems().length>0) {
	                int index = mainTable.getSelectionIndex();
	                TableItem item = mainTable.getItem(index);
	                Bill bill = (Bill)item.getData();
	                WinBillMemo winBillMemo = new WinBillMemo(display, WinPrintList.this, bill);
	                winBillMemo.open();
            	}
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
        senderColumn.getColumn().setWidth(300);
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
        destinationColumn.getColumn().setWidth(160);
        destinationColumn.getColumn().setText("到站");
        destinationColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return ((Bill)element).getDestination();
		    }
		});
        
        final TableViewerColumn receiverColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        receiverColumn.getColumn().setResizable(true);
        receiverColumn.getColumn().setWidth(300);
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
        
        final TableViewerColumn memoColumn = new TableViewerColumn(mainGrid, SWT.LEFT);
        memoColumn.getColumn().setResizable(true);
        memoColumn.getColumn().setWidth(160);
        memoColumn.getColumn().setText("备注");
        memoColumn.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        return "";
		    }
		});
		
		Label operator = new Label(this, SWT.NONE);
		operator.setText("\u9A7E\u9A76\u5458\uFF1A");
		operator.setBounds(240, 89, 55, 20);
		
		Label label = new Label(this, SWT.NONE);
		label.setText("\u5236\u5355\u65E5\u671F\uFF1A");
		label.setBounds(750, 89, 60, 20);
		
		dateTime = new DateTime(this, SWT.BORDER);
		dateTime.setBounds(816, 86, 168, 24);
		dateTime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinPrintList.this.initDataView();
			}
		});		
		
		printListBtn = new Button(this, SWT.NONE);
		printListBtn.setText("\u6253\u5370\u7269\u6d41\u5355");
		printListBtn.setImage(SWTResourceManager.getImage(WinPrintList.class, "/res/images/icon_confirm.png"));
		printListBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		printListBtn.setBounds(784, 516, 200, 44);
		printListBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinPrintList.this.printList();
			}
		});	
		
		printAdminBtn = new Button(this, SWT.NONE);
		printAdminBtn.setText("\u6253\u5370\u8fd0\u7ba1\u5355");
		printAdminBtn.setImage(SWTResourceManager.getImage(WinPrintList.class, "/res/images/icon_confirm.png"));
		printAdminBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		printAdminBtn.setBounds(560, 516, 200, 44);
		printAdminBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinPrintList.this.printAdmin();
			}
		});	
		
		cancelBtn = new Button(this, SWT.NONE);
		cancelBtn.setText("\u53D6\u6D88");
		cancelBtn.setImage(SWTResourceManager.getImage(WinPrintList.class, "/res/images/icon_cancel.png"));
		cancelBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		cancelBtn.setAlignment(SWT.LEFT);
		cancelBtn.setBounds(396, 516, 140, 44);
		
		creatorLbl = new Label(this, SWT.NONE);
		creatorLbl.setText("\u5236\u5355\u5458\uFF1A");
		creatorLbl.setBounds(458, 89, 55, 20);
		
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinPrintList.this.close();
			}
		});		
		
		screenBound = display.getPrimaryMonitor().getBounds();
		
		createContents();
		initDataView();
	}

	protected void createContents() {
		setText("打印单据列表");
		setBounds((screenBound.width-1000)/2, (screenBound.height-600)/2, 1000, 600);
	}
	
	public void initDataView() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.dateTime.getYear(), this.dateTime.getMonth(), this.dateTime.getDay());		
		createDate = calendar.getTime();
		
		billList = billService.queryBillByCondition(null, "0", createDate, createDate);
		mainGrid.setInput(billList);
		
		winMain.initDataView();
	}

	@Override
	protected void checkSubclass() {
	}
	
	private void printAdmin() {
		
		if (!checkParam()) {
			return;
		}
		
		String vehicle = StringUtil.trimString(vehicleTxt.getText());
		int creatorIndex = creatorCmb.getSelectionIndex();
		int operatorIndex = operatorCmb.getSelectionIndex();
		
		UserInfo operator = userInfoList.get(operatorIndex-1);		
		BillDetail billDetail = new BillDetail();
		billDetail.setVehicle(vehicle);
		billDetail.setCreateDate(createDate);
		billDetail.setOperator(operator.getName());
		UserInfo creator = userInfoList.get(creatorIndex-1);
		billDetail.setCreator(creator.getName());
		
		if (billList != null && billList.size()>0) {
			int page = 0;
			if (billList.size()%PRINT_PAGE_NUM==0) {
				page = billList.size()/PRINT_PAGE_NUM;
			} else {
				page = billList.size()/PRINT_PAGE_NUM+1;
			}
			for (int i=0;i<page;++i) {
				int limit = (i+1)*PRINT_PAGE_NUM>billList.size()?billList.size():(i+1)*PRINT_PAGE_NUM;
				List<Bill> billPage = billList.subList(i*PRINT_PAGE_NUM, limit);				
				PrintUtil.printBillAdmin(billPage, billDetail);
			}
			this.close();
			InfoBox.showInfo(display, "本次打印共需要打印"+page+"页内容");
		} else {
			InfoBox.showInfo(display, "没有需要打印的单据列表^_^");
		}
	}
	
	private void printList() {
		
		if (!checkParam()) {
			return;
		}
		
		String vehicle = StringUtil.trimString(vehicleTxt.getText());
		int creatorIndex = creatorCmb.getSelectionIndex();
		int operatorIndex = operatorCmb.getSelectionIndex();
		
		UserInfo operator = userInfoList.get(operatorIndex-1);		
		BillDetail billDetail = new BillDetail();
		billDetail.setVehicle(vehicle);
		billDetail.setCreateDate(createDate);
		billDetail.setOperator(operator.getName());
		UserInfo creator = userInfoList.get(creatorIndex-1);
		billDetail.setCreator(creator.getName());
		
		if (billList != null && billList.size()>0) {
			List<Base> billDataList = new ArrayList<Base>();
			Map<String, List<Bill>> billDataMap = new HashMap<String, List<Bill>>();
			for(int i=0;i<billList.size();++i) {
				Bill bill = billList.get(i);
				if (bill!=null) {
					Region region = regionService.getRegionById(bill.getDestinationId());
					if (region==null) {
						region = new Region();
						region.setId(bill.getDestinationId());
						region.setName(bill.getDestination());
					}
					
					if (billDataMap.containsKey(region.getId())==false) {
						billDataMap.put(region.getId(), new ArrayList<Bill>()); 
					}
					List<Bill> bills = billDataMap.get(region.getId());
					bills.add(bill);
				}								
			}
			
			for (String key:billDataMap.keySet()) {
				List<Bill> bills = billDataMap.get(key);
				if (bills!=null && bills.size()>0) {
					Region region = regionService.getRegionById(key);
					if (region==null) {
						region = new Region();
						region.setId(bills.get(0).getDestinationId());
						region.setName(bills.get(0).getDestination());
					}
					billDataList.add(region);
					billDataList.addAll(bills);
				}
			}
			
			int page = 0;
			if (billDataList.size()%PRINT_PAGE_NUM==0) {
				page=billDataList.size()/PRINT_PAGE_NUM;
			} else {
				page=billDataList.size()/PRINT_PAGE_NUM+1;
			}
			for (int i=0;i<page;++i) {
				int limit = (i+1)*PRINT_PAGE_NUM>billDataList.size()?billDataList.size():(i+1)*PRINT_PAGE_NUM;
				List<Base> billPage = billDataList.subList(i*PRINT_PAGE_NUM, limit);				
				PrintUtil.printBillList(billPage, billDetail);
			}
			this.close();
			InfoBox.showInfo(display, "本次打印共需要打印"+page+"页内容");
		} else {
			InfoBox.showInfo(display, "没有需要打印的单据列表^_^");
		}
	}
	
	private boolean checkParam() {
		String vehicle = StringUtil.trimString(vehicleTxt.getText());
		if (StringUtil.isBlank(vehicle)) {
			InfoBox.showInfo(display, "车号不能为空哦^_^");
			return false;
		}
		
		int creatorIndex = creatorCmb.getSelectionIndex();
		if (creatorIndex==0) {
			InfoBox.showInfo(display, "请选择制单员^_^");
			return false;
		}
		
		int operatorIndex = operatorCmb.getSelectionIndex();
		if (operatorIndex==0) {
			InfoBox.showInfo(display, "请选择驾驶员^_^");
			return false;
		}
		return true;
	}
	
	private String formatBoolValue(short value) {
		if (value == Bill.BASE_TRUE) {
			return "√";
		} else if (value == Bill.BASE_FALSE) {
			return "×";
		}
		return "×";
	}
}

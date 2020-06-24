package com.sys.ui;

import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sys.bo.Bill;
import com.sys.bo.Customer;
import com.sys.bo.LinkWay;
import com.sys.service.BillService;
import com.sys.service.CustomerService;
import com.sys.service.LinkWayService;
import com.sys.ui.data.CustomerContProvider;
import com.sys.ui.data.CustomerLabelProvider;
import com.sys.ui.data.LinkWayContProvider;
import com.sys.ui.data.LinkWayLabelProvider;
import com.sys.util.DataUtil;
import com.sys.util.DateUtil;
import com.sys.util.StringUtil;
import com.sys.widget.IExportClient;
import com.sys.widget.IInputClient;
import com.sys.widget.IMessageClient;
import com.sys.widget.InfoBox;
import com.sys.widget.InputBox;
import com.sys.widget.MessageBox;
import com.sys.widget.WinExportDate;

public class WinCustomer extends Shell implements IMessageClient, IExportClient, IInputClient {
	
	public static final short OPERMODE_ADD = 1;
	
	public static final short OPERMODE_UPDATE = 2;
	
	public static final short OPERMODE_REMOVE = 3;
	
	public static final short OPERATE_MODE_NEW = 1;
	
	public static final short OPERATE_MODE_UPDATE = 2;
	
	public static final short REMOVE_MODE_CUST = 1;
	
	public static final short REMOVE_MODE_LINK = 2;
	
	private Display display;
	
	private CustomerService customerService;
	
	private BillService billService;
	
	private LinkWayService linkWayService;
	
	private Rectangle screenBound;
	
	private TableViewer mainGrid;
	
	private Table mainTable;
	
	private TableViewer phoneGrid;
	
	private Table phoneTable;
	
	private List<Customer> customerList;
	
	private Label tipLbl;
	
	private Text nameTxt;
	
	private Text searchTxt;
	
	private Button exportBtn;
	
	private Button cancelBtn;
	
	private Button confirmBtn;
	
	private Button addBtn;
	
	private Button updateBtn;
	
	private Button removeBtn;
	
	private short operMode;
	
	private Customer currentCustomer;
	
	private WinExportDate winBillExport;
	
	private LinkWay currentLinkWay;
	
	private short operationMode;
	
	private short removeMode;
	
	public WinCustomer(final Display displayP) {
		super(displayP, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setSize(900, 600);
		setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/ico_cust_info_small.png"));
		
		display = displayP;
		billService = BillService.getInstance();
		customerService = CustomerService.getInstance();
		linkWayService = LinkWayService.getInstance();
		mainGrid = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		mainGrid.setContentProvider(new CustomerContProvider());
		mainGrid.setLabelProvider(new CustomerLabelProvider());
		mainTable = mainGrid.getTable();
		mainTable.setLinesVisible(true);
		mainTable.setHeaderVisible(true);
		mainTable.setBounds(8, 43, 492, 519);
		mainTable.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.width = mainTable.getGridLineWidth();
				event.height = 28;
			}
		});
		mainTable.addListener(SWT.MouseDown, new Listener() {  
            public void handleEvent(Event event) {
            	try {
            		if (mainTable.getItems().length>0) {
    	                int index = mainTable.getSelectionIndex();
    	                TableItem item = mainTable.getItem(index);
    	                currentCustomer = (Customer)item.getData();
    	                WinCustomer.this.selectCustomer(currentCustomer);
                	}
				} catch (Exception e) {					
				}
            }  
        });
		
		TableColumn nameColumn = new TableColumn(mainTable, SWT.NONE);
		nameColumn.setWidth(460);
		nameColumn.setText("客户名称");
		nameColumn.setResizable(false);
		
		phoneGrid = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		phoneGrid.setContentProvider(new LinkWayContProvider());
		phoneGrid.setLabelProvider(new LinkWayLabelProvider());
		phoneTable = phoneGrid.getTable();
		phoneTable.setLinesVisible(true);
		phoneTable.setHeaderVisible(true);
		phoneTable.setBounds(554, 112, 308, 300);
		
		Menu menu = new Menu(this, SWT.POP_UP);		
		MenuItem newLinkWayMenu = new MenuItem(menu, SWT.NONE);		
		newLinkWayMenu.setText("添加联系方式");
		newLinkWayMenu.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/ico_new_menu.png"));
		newLinkWayMenu.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent event) {
				WinCustomer.this.newLinkWay();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		MenuItem updateLinkWayMenu = new MenuItem(menu, SWT.NONE); 
		updateLinkWayMenu.setText("修改联系方式");
		updateLinkWayMenu.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/ico_update_menu.png"));
		updateLinkWayMenu.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent event) {
				WinCustomer.this.updateLinkWay();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		MenuItem removeLinkWayMenu = new MenuItem(menu, SWT.NONE); 
		removeLinkWayMenu.setText("删除联系方式");
		removeLinkWayMenu.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/ico_remove_menu.png"));
		removeLinkWayMenu.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent event) {
				WinCustomer.this.removeLinkWay();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		
		phoneTable.setMenu(menu);
		
		phoneTable.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.width = phoneTable.getGridLineWidth();
				event.height = 24;
			}
		});
		
		TableColumn phoneColumn = new TableColumn(phoneTable, SWT.NONE);
		phoneColumn.setWidth(280);
		phoneColumn.setText("联系方式");
		phoneColumn.setResizable(false);
		
		Label vhline1 = new Label(this, SWT.NONE);
		vhline1.setBackground(SWTResourceManager.getColor(153, 153, 153));
		vhline1.setBounds(520, 10, 1, 552);
		
		Label hline1 = new Label(this, SWT.NONE);
		hline1.setBackground(SWTResourceManager.getColor(153, 153, 153));
		hline1.setBounds(520, 10, 350, 1);
		
		Label hline2 = new Label(this, SWT.NONE);
		hline2.setBackground(SWTResourceManager.getColor(153, 153, 153));
		hline2.setBounds(520, 561, 350, 1);
		
		Label vline2 = new Label(this, SWT.NONE);
		vline2.setBackground(SWTResourceManager.getColor(153, 153, 153));
		vline2.setBounds(869, 10, 1, 552);
		
		Label nameLbl = new Label(this, SWT.NONE);
		nameLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		nameLbl.setBounds(554, 62, 80, 24);
		nameLbl.setText("\u5BA2\u6237\u540D\u79F0:");
		
		nameTxt = new Text(this, SWT.BORDER);
		nameTxt.setEnabled(false);
		nameTxt.setBounds(640, 58, 223, 24);		
		
		exportBtn = new Button(this, SWT.NONE);
		exportBtn.setText("\u5bfc\u51fa\u7269\u6d41\u5355\u636e");
		exportBtn.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/icon_export_small.png"));
		exportBtn.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		exportBtn.setAlignment(SWT.LEFT);
		exportBtn.setBounds(664, 440, 200, 32);
		exportBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinCustomer.this.exportData();
			}
		});	
		
		addBtn = new Button(this, SWT.NONE);
		addBtn.setText("\u6DFB\u52A0");
		addBtn.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/ico_add_small.png"));
		addBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		addBtn.setAlignment(SWT.LEFT);
		addBtn.setBounds(526, 506, 100, 44);
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinCustomer.this.addCustomerInfo();
			}
		});	
		
		updateBtn = new Button(this, SWT.NONE);
		updateBtn.setText("\u66F4\u65B0");
		updateBtn.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/ico_update_small.png"));
		updateBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		updateBtn.setAlignment(SWT.LEFT);
		updateBtn.setBounds(645, 506, 100, 44);		
		updateBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinCustomer.this.updateCustomerInfo();
			}
		});	
		
		removeBtn = new Button(this, SWT.NONE);
		removeBtn.setText("\u5220\u9664");
		removeBtn.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/ico_remove_small.png"));
		removeBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		removeBtn.setAlignment(SWT.LEFT);
		removeBtn.setBounds(763, 506, 100, 44);
		removeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinCustomer.this.removeCustomerInfo();
			}
		});	
		
		cancelBtn = new Button(this, SWT.NONE);
		cancelBtn.setText("\u53D6\u6D88");
		cancelBtn.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/icon_cancel.png"));
		cancelBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		cancelBtn.setAlignment(SWT.LEFT);
		cancelBtn.setBounds(526, 506, 140, 44);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinCustomer.this.cancel();
			}
		});	
		
		confirmBtn = new Button(this, SWT.NONE);
		confirmBtn.setText("\u786E\u5B9A");
		confirmBtn.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/icon_confirm.png"));
		confirmBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		confirmBtn.setBounds(723, 506, 140, 44);
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinCustomer.this.confirm();
			}
		});	
		
		tipLbl = new Label(this, SWT.NONE);
		tipLbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		tipLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		tipLbl.setBounds(556, 236, 304, 28);
		
		searchTxt = new Text(this, SWT.BORDER);
		searchTxt.setBounds(8, 10, 492, 27);
		searchTxt.setToolTipText("请输入名字或简拼！");
		searchTxt.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {				
				startSearch();
			}
		});
		
		screenBound = display.getPrimaryMonitor().getBounds();
		createContents();
		initDataView();
	}

	protected void createContents() {
		setText("\u76f8\u5173\u5ba2\u6237\u7ba1\u7406");
		setBounds((screenBound.width-900)/2, (screenBound.height-600)/2, 900, 600);
		cancelBtn.setVisible(false);
		confirmBtn.setVisible(false);
		searchTxt.setFocus();
	}
	
	private void initDataView() {		
		String keyword = StringUtil.trimString(this.searchTxt.getText());
		customerList = customerService.getCustomerByKeyword(keyword);
		mainGrid.setInput(customerList);
		if (customerList!=null && customerList.size()>0) {
			currentCustomer = customerList.get(0);
		} else {
			currentCustomer = null;
			nameTxt.setText("");
		}
		selectCustomer(currentCustomer);
	}
	
	private void exportData() {
		if (currentCustomer!=null) {
			winBillExport = new WinExportDate(display, this, "导出\""+currentCustomer.getName()+"\"的物流单据");
			winBillExport.open();
		}
	}
	
	private void addCustomerInfo() {
		operMode = OPERMODE_ADD;
		setEditMode();		
		nameTxt.setText("");
	}
	
	protected void updateCustomerInfo() {
		operMode = OPERMODE_UPDATE;
		setEditMode();
	}
	
	protected void removeCustomerInfo() {
		if (currentCustomer!=null) {
			removeMode = REMOVE_MODE_CUST;
			MessageBox.showInfo(display, "您确定要删除这一项吗?", this);
		}
	}
	
	private void setEditMode() {
		addBtn.setVisible(false);
		updateBtn.setVisible(false);
		removeBtn.setVisible(false);
		cancelBtn.setVisible(true);
		confirmBtn.setVisible(true);
		phoneTable.setVisible(false);
		exportBtn.setVisible(false);
		
		tipLbl.setText("");
		nameTxt.setEnabled(true);
	}
	
	private void setViewMode() {
		addBtn.setVisible(true);
		updateBtn.setVisible(true);
		removeBtn.setVisible(true);
		cancelBtn.setVisible(false);
		confirmBtn.setVisible(false);
		phoneTable.setVisible(true);
		exportBtn.setVisible(true);
		
		tipLbl.setText("");
		nameTxt.setEnabled(false);
	}
	
	private void selectCustomer(Customer customer) {
		setViewMode();
		if (customer!=null) {
			List<LinkWay> linkWays = linkWayService.getLinkWayByCustomerId(customer.getId());
			phoneGrid.setInput(linkWays);
			nameTxt.setText(customer.getName());
			updateBtn.setEnabled(true);
			removeBtn.setEnabled(true);			
		} else {
			nameTxt.setText("");
			updateBtn.setEnabled(false);
			removeBtn.setEnabled(false);
		}
	}
	
	protected void startSearch() {
		String keyword = StringUtil.trimString(this.searchTxt.getText());
		customerList = customerService.getCustomerByKeyword(keyword);
		mainGrid.setInput(customerList);
	}
	
	private void confirm() {
		if (isValid()) {
			if (OPERMODE_ADD == operMode) {
				Customer customer = new Customer();
				customer.setName(StringUtil.trimString(nameTxt.getText()));
				customerService.saveCustomer(customer);
				initDataView();
				setViewMode();
			} else if (OPERMODE_UPDATE == operMode) {
				Customer customer = currentCustomer;
				customer.setName(StringUtil.trimString(nameTxt.getText()));
				customerService.updateCustomer(customer);
				initDataView();
				setViewMode();
			}
		}		
	}
	
	private void cancel() {
		selectCustomer(currentCustomer);
	}
	
	private boolean isValid() {
		String name = StringUtil.trimString(nameTxt.getText());
		if (StringUtil.isBlank(name)) {
			tipLbl.setText("客户名称不能为空哦！");
			return false;
		}
		return true;
	}
	
	public void confirmOperation() {
		if (removeMode == REMOVE_MODE_CUST) {
			if (currentCustomer!=null) {
				customerService.removeCustomer(currentCustomer);
				initDataView();
			}
		} else if (removeMode == REMOVE_MODE_LINK) {
			if (currentLinkWay!=null) {
				linkWayService.removeLinkWay(currentLinkWay);
				selectCustomer(currentCustomer);
			}
		}
	}
	
	@Override
	public void selectExportDate(Date startDate, Date endDate) {
		if (currentCustomer!=null) {
			List<Bill> billList = billService.queryBillBySender(currentCustomer.getId(), startDate, endDate);
			if (billList!=null && billList.size()>0) {				
				String fileName=null,fileTitle=null;
				if (startDate.equals(endDate)) {				
					fileName = DateUtil.formatDate(startDate)+currentCustomer.getName()+"的物流单据汇总.xls";
					fileTitle = DateUtil.formatDate(startDate)+currentCustomer.getName()+"的物流单据汇总";
				} else {
					fileName = DateUtil.formatDate(startDate)+"至"+DateUtil.formatDate(endDate)+currentCustomer.getName()+"的物流单据汇总.xls";
					fileTitle = DateUtil.formatDate(startDate)+"至"+DateUtil.formatDate(endDate)+currentCustomer.getName()+"的物流单据汇总";
				}
				FileDialog exportDialog = new FileDialog(this);
				exportDialog.setText("请选择所要导出的文件");
				exportDialog.setFileName(fileName);
				String filePath = exportDialog.open();
				if (StringUtil.isNotBlank(filePath)) {
					int status = DataUtil.exportCustomerBill(filePath, fileTitle, billList);
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
	}
	
	protected void newLinkWay() {
		operationMode = OPERATE_MODE_NEW;
		InputBox.showInput(display, "新建联系方式", "", this);
	}
	
	protected void updateLinkWay() {
		TableItem[] selectedItems = phoneTable.getSelection();
		if (selectedItems!=null && selectedItems.length>0) {
			TableItem tableItem = selectedItems[0];
			operationMode = OPERATE_MODE_UPDATE;
			currentLinkWay = (LinkWay)tableItem.getData();			
			InputBox.showInput(display, "更新联系方式", currentLinkWay.getPhone(), this);
		} else {
			InfoBox.showInfo(display, "请选择要更新的联系方式");
		}
	}
	
	@Override
	public void inputContent(String content) {
		if (currentCustomer!=null) {
			if (operationMode==OPERATE_MODE_NEW) {
				LinkWay linkWay = new LinkWay(currentCustomer.getId(), content);
				linkWayService.saveLinkWay(linkWay);
				this.selectCustomer(currentCustomer);
			} else if (operationMode==OPERATE_MODE_UPDATE) {
				if (currentLinkWay!=null) {
					currentLinkWay.setPhone(content);
					linkWayService.updateLinkWay(currentLinkWay);
					this.selectCustomer(currentCustomer);
				}
			}
		}
	}

	protected void removeLinkWay() {
		TableItem[] selectedItems = phoneTable.getSelection();
		if (selectedItems!=null && selectedItems.length>0) {
			TableItem tableItem = selectedItems[0];
			currentLinkWay = (LinkWay)tableItem.getData();
			removeMode = REMOVE_MODE_LINK;
			MessageBox.showInfo(display, "您确实要删除("+currentLinkWay.getPhone()+")这项联系方式吗?", this);
		} else {
			InfoBox.showInfo(display, "请选择要删除的联系方式");
		}
	}

	@Override
	protected void checkSubclass() {
	}
}

package com.sys.ui;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sys.bo.Customer;
import com.sys.service.CustomerService;
import com.sys.ui.data.CustomerContProvider;
import com.sys.ui.data.CustomerLabelProvider;
import com.sys.util.StringUtil;
import com.sys.widget.ISelectCustomer;

public class WinSelectCust extends Shell {

	private Table mainTable;
	
	private Text searchTxt;
	
	private Button cancelBtn;
	
	private TableViewer mainGrid;
	
	private Rectangle screenBound;
	
	private CustomerService customerService;
	
	private List<Customer> customerList;
	
	private ISelectCustomer customerClient;
	
	public WinSelectCust(Display display, ISelectCustomer client) {
		super(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setSize(900, 600);
		setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/ico_cust_info_small.png"));
		
		customerClient = client;
		customerService = CustomerService.getInstance();
		mainGrid = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		mainGrid.setContentProvider(new CustomerContProvider());
		mainGrid.setLabelProvider(new CustomerLabelProvider());
		mainTable = mainGrid.getTable();
		mainTable.setLinesVisible(true);
		mainTable.setHeaderVisible(true);
		mainTable.setBounds(8, 44, 478, 360);
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
    	                Customer customer = (Customer)item.getData();
    	                customerClient.selectCustomer(customer);
    	                WinSelectCust.this.close();
                	}
				} catch (Exception e) {					
				}
            }  
        });
		
		TableColumn nameColumn = new TableColumn(mainTable, SWT.NONE);
		nameColumn.setWidth(450);
		nameColumn.setText("客户名称");
		nameColumn.setResizable(false);
		
		cancelBtn = new Button(this, SWT.NONE);
		cancelBtn.setText("\u53D6\u6D88");
		cancelBtn.setImage(SWTResourceManager.getImage(WinCustomer.class, "/res/images/icon_cancel.png"));
		cancelBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		cancelBtn.setAlignment(SWT.LEFT);
		cancelBtn.setBounds(346, 410, 140, 44);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinSelectCust.this.close();
			}
		});
		
		searchTxt = new Text(this, SWT.BORDER);
		searchTxt.setBounds(8, 10, 478, 27);
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
		setText("选择发货单位");
		setBounds((screenBound.width-500)/2, (screenBound.height-500)/2, 500, 500);
		searchTxt.setFocus();
	}
	
	private void initDataView() {		
		String keyword = StringUtil.trimString(this.searchTxt.getText());
		customerList = customerService.getCustomerByKeyword(keyword);
		mainGrid.setInput(customerList);
	}

	protected void startSearch() {
		String keyword = StringUtil.trimString(this.searchTxt.getText());
		customerList = customerService.getCustomerByKeyword(keyword);
		mainGrid.setInput(customerList);
	}
	
	@Override
	protected void checkSubclass() {
	}
}

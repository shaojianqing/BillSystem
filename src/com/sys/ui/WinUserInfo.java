package com.sys.ui;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sys.bo.UserInfo;
import com.sys.service.UserService;
import com.sys.ui.data.UserContProvider;
import com.sys.ui.data.UserLabelProvider;
import com.sys.util.RoleUtil;
import com.sys.util.StringUtil;
import com.sys.widget.IMessageClient;
import com.sys.widget.MessageBox;

public class WinUserInfo extends Shell implements IMessageClient {
	
	public static final short OPERMODE_ADD = 1;
	
	public static final short OPERMODE_UPDATE = 2;
	
	public static final short OPERMODE_REMOVE = 3;
	
	private Display display;
	
	private UserService userService;
	
	private Rectangle screenBound;
	
	private TableViewer mainGrid;
	
	private Table mainTable;
	
	private List<UserInfo> userInfoList;
	
	private Label tipLbl;
	
	private Text nameTxt;
	
	private Text linkTxt;
	
	private CCombo roleCmb;
	
	private Button cancelBtn;
	
	private Button confirmBtn;
	
	private Button addBtn;
	
	private Button updateBtn;
	
	private Button removeBtn;
	
	private short operMode;
	
	private UserInfo currentUserInfo;
	
	public WinUserInfo(final Display displayP) {
		super(displayP, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setSize(900, 600);
		setImage(SWTResourceManager.getImage(WinUserInfo.class, "/res/images/ico_user_info_small.png"));
		
		display = displayP;
		userService = UserService.getInstance();
		mainGrid = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		mainGrid.setContentProvider(new UserContProvider());
		mainGrid.setLabelProvider(new UserLabelProvider());
		mainTable = mainGrid.getTable();
		mainTable.setLinesVisible(true);
		mainTable.setHeaderVisible(true);
		mainTable.setBounds(8, 10, 492, 552);
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
		                currentUserInfo = (UserInfo)item.getData();
		                WinUserInfo.this.selectUserInfo(currentUserInfo);            		
            		}
            	} catch (Exception e) {					
				}
            }  
        });
		
		TableColumn nameColumn = new TableColumn(mainTable, SWT.NONE);
		nameColumn.setWidth(160);
		nameColumn.setText("姓名");
		nameColumn.setResizable(false);
		
		TableColumn roleColumn = new TableColumn(mainTable, SWT.NONE);
		roleColumn.setWidth(160);
		roleColumn.setText("工作角色");
		roleColumn.setResizable(false);
		
		TableColumn linkColumn = new TableColumn(mainTable, SWT.NONE);
		linkColumn.setWidth(160);
		linkColumn.setText("联系方式");
		linkColumn.setResizable(true);
		
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
		nameLbl.setBounds(556, 58, 80, 28);
		nameLbl.setText("\u59D3\u540D:");
		
		Label roleLbl = new Label(this, SWT.NONE);
		roleLbl.setText("\u5DE5\u4F5C\u89D2\u8272:");
		roleLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		roleLbl.setBounds(556, 108, 80, 28);
		
		Label linkLbl = new Label(this, SWT.NONE);
		linkLbl.setText("\u8054\u7CFB\u65B9\u5F0F:");
		linkLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		linkLbl.setBounds(556, 158, 80, 28);
		
		nameTxt = new Text(this, SWT.BORDER);
		nameTxt.setEnabled(false);
		nameTxt.setBounds(640, 58, 223, 24);
		
		roleCmb = new CCombo(this, SWT.BORDER);
		roleCmb.setEnabled(false);
		roleCmb.setBounds(640, 108, 223, 24);
		roleCmb.setItems(RoleUtil.getRoleList());
		
		linkTxt = new Text(this, SWT.BORDER);
		linkTxt.setEnabled(false);
		linkTxt.setBounds(640, 158, 223, 24);		
		
		addBtn = new Button(this, SWT.NONE);
		addBtn.setText("\u6DFB\u52A0");
		addBtn.setImage(SWTResourceManager.getImage(WinUserInfo.class, "/res/images/ico_add_small.png"));
		addBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		addBtn.setAlignment(SWT.LEFT);
		addBtn.setBounds(526, 506, 100, 44);
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinUserInfo.this.addUserInfo();
			}
		});	
		
		updateBtn = new Button(this, SWT.NONE);
		updateBtn.setText("\u66F4\u65B0");
		updateBtn.setImage(SWTResourceManager.getImage(WinUserInfo.class, "/res/images/ico_update_small.png"));
		updateBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		updateBtn.setAlignment(SWT.LEFT);
		updateBtn.setBounds(645, 506, 100, 44);		
		updateBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinUserInfo.this.updateUserInfo();
			}
		});	
		
		removeBtn = new Button(this, SWT.NONE);
		removeBtn.setText("\u5220\u9664");
		removeBtn.setImage(SWTResourceManager.getImage(WinUserInfo.class, "/res/images/ico_remove_small.png"));
		removeBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		removeBtn.setAlignment(SWT.LEFT);
		removeBtn.setBounds(763, 506, 100, 44);
		removeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinUserInfo.this.removeUserInfo();
			}
		});	
		
		cancelBtn = new Button(this, SWT.NONE);
		cancelBtn.setText("\u53D6\u6D88");
		cancelBtn.setImage(SWTResourceManager.getImage(WinUserInfo.class, "/res/images/icon_cancel.png"));
		cancelBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		cancelBtn.setAlignment(SWT.LEFT);
		cancelBtn.setBounds(526, 506, 140, 44);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinUserInfo.this.cancel();
			}
		});	
		
		confirmBtn = new Button(this, SWT.NONE);
		confirmBtn.setText("\u786E\u5B9A");
		confirmBtn.setImage(SWTResourceManager.getImage(WinUserInfo.class, "/res/images/icon_confirm.png"));
		confirmBtn.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		confirmBtn.setBounds(723, 506, 140, 44);
		
		tipLbl = new Label(this, SWT.NONE);
		tipLbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		tipLbl.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		tipLbl.setBounds(556, 236, 304, 28);
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinUserInfo.this.confirm();
			}
		});	
		
		screenBound = display.getPrimaryMonitor().getBounds();
		createContents();
		initDataView();
	}

	protected void createContents() {
		setText("相关人员管理");
		setBounds((screenBound.width-900)/2, (screenBound.height-600)/2, 900, 600);
		cancelBtn.setVisible(false);
		confirmBtn.setVisible(false);
	}
	
	private void initDataView() {
		userInfoList = userService.getUserInfoByQuery();
		mainGrid.setInput(userInfoList);
		if (userInfoList!=null && userInfoList.size()>0) {
			currentUserInfo = userInfoList.get(0);
		} else {
			currentUserInfo = null;
			nameTxt.setText("");
			roleCmb.select(0);
			linkTxt.setText("");
		}
		selectUserInfo(currentUserInfo);
	}
	
	private void addUserInfo() {
		operMode = OPERMODE_ADD;
		setEditMode();		
		nameTxt.setText("");
		roleCmb.select(0);
		linkTxt.setText("");
	}
	
	protected void updateUserInfo() {
		operMode = OPERMODE_UPDATE;
		setEditMode();
	}
	
	protected void removeUserInfo() {
		if (currentUserInfo!=null) {
			MessageBox.showInfo(display, "您确定要删除这一项吗?", this);
		}
	}
	
	private void setEditMode() {
		addBtn.setVisible(false);
		updateBtn.setVisible(false);
		removeBtn.setVisible(false);
		cancelBtn.setVisible(true);
		confirmBtn.setVisible(true);
		
		tipLbl.setText("");
		nameTxt.setEnabled(true);
		roleCmb.setEnabled(true);
		linkTxt.setEnabled(true);
	}
	
	private void setViewMode() {
		addBtn.setVisible(true);
		updateBtn.setVisible(true);
		removeBtn.setVisible(true);
		cancelBtn.setVisible(false);
		confirmBtn.setVisible(false);
		
		tipLbl.setText("");
		nameTxt.setEnabled(false);
		roleCmb.setEnabled(false);
		linkTxt.setEnabled(false);
	}
	
	private void selectUserInfo(UserInfo userInfo) {
		setViewMode();
		if (userInfo!=null) {
			nameTxt.setText(userInfo.getName());
			linkTxt.setText(userInfo.getLink());
			roleCmb.select(userInfo.getRole());
			
			updateBtn.setEnabled(true);
			removeBtn.setEnabled(true);
			
		} else {
			nameTxt.setText("");
			linkTxt.setText("");
			roleCmb.select(0);
			
			updateBtn.setEnabled(false);
			removeBtn.setEnabled(false);
		}
	}
	
	private void confirm() {
		if (isValid()) {
			if (OPERMODE_ADD == operMode) {
				UserInfo userInfo = new UserInfo();
				userInfo.setName(StringUtil.trimString(nameTxt.getText()));
				userInfo.setRole((short)roleCmb.getSelectionIndex());
				userInfo.setLink(StringUtil.trimString(linkTxt.getText()));
				userService.saveUserInfo(userInfo);
				initDataView();
				setViewMode();
			} else if (OPERMODE_UPDATE == operMode) {
				UserInfo userInfo = currentUserInfo;
				userInfo.setName(StringUtil.trimString(nameTxt.getText()));
				userInfo.setRole((short)roleCmb.getSelectionIndex());
				userInfo.setLink(StringUtil.trimString(linkTxt.getText()));
				userService.updateUserInfo(userInfo);
				initDataView();
				setViewMode();
			}
		}		
	}
	
	private void cancel() {
		selectUserInfo(currentUserInfo);
	}
	
	private boolean isValid() {
		String name = StringUtil.trimString(nameTxt.getText());
		if (StringUtil.isBlank(name)) {
			tipLbl.setText("姓名不能为空哦！");
			return false;
		}
		String link = StringUtil.trimString(linkTxt.getText());
		if (StringUtil.isBlank(link)) {
			tipLbl.setText("联系方式不能为空哦！");
			return false;
		}
		return true;
	}
	
	public void confirmOperation() {
		if (currentUserInfo!=null) {
			userService.removeUserInfo(currentUserInfo);
			initDataView();
		}
	}

	@Override
	protected void checkSubclass() {
	}
}

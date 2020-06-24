package com.sys.ui;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
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

import com.sys.bo.Region;
import com.sys.service.RegionService;
import com.sys.ui.data.RegionContProvider;
import com.sys.ui.data.RegionLabelProvider;
import com.sys.util.StringUtil;
import com.sys.widget.IMessageClient;
import com.sys.widget.MessageBox;

public class WinRegion extends Shell implements IMessageClient {
	
	public static final short OPERMODE_ADD = 1;
	
	public static final short OPERMODE_UPDATE = 2;
	
	public static final short OPERMODE_REMOVE = 3;
	
	private Display display;
	
	private RegionService regionService;
	
	private Rectangle screenBound;
	
	private TableViewer mainGrid;
	
	private Table mainTable;
	
	private List<Region> regionList;
	
	private Label tipLbl;
	
	private Text nameTxt;
	
	private Button cancelBtn;
	
	private Button confirmBtn;
	
	private Button addBtn;
	
	private Button updateBtn;
	
	private Button removeBtn;
	
	private short operMode;
	
	private Region currentRegion;
	
	public WinRegion(final Display displayP) {
		super(displayP, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setSize(900, 600);
		setImage(SWTResourceManager.getImage(WinRegion.class, "/res/images/ico_region_small.png"));
		
		display = displayP;
		regionService = RegionService.getInstance();
		mainGrid = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		mainGrid.setContentProvider(new RegionContProvider());
		mainGrid.setLabelProvider(new RegionLabelProvider());
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
    	                currentRegion = (Region)item.getData();
    	                WinRegion.this.selectRegion(currentRegion);
                	}
				} catch (Exception e) {					
				}
            }  
        });
		
		TableColumn nameColumn = new TableColumn(mainTable, SWT.NONE);
		nameColumn.setWidth(400);
		nameColumn.setText("ÇøÓòÃû³Æ");
		nameColumn.setResizable(false);
		
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
		nameLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		nameLbl.setBounds(554, 62, 80, 24);
		nameLbl.setText("\u533A\u57DF\u540D\u79F0:");
		
		nameTxt = new Text(this, SWT.BORDER);
		nameTxt.setEnabled(false);
		nameTxt.setBounds(640, 58, 223, 24);
		
		addBtn = new Button(this, SWT.NONE);
		addBtn.setText("\u6DFB\u52A0");
		addBtn.setImage(SWTResourceManager.getImage(WinRegion.class, "/res/images/ico_add_small.png"));
		addBtn.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 16, SWT.NORMAL));
		addBtn.setAlignment(SWT.LEFT);
		addBtn.setBounds(526, 506, 100, 44);
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinRegion.this.addRegionInfo();
			}
		});	
		
		updateBtn = new Button(this, SWT.NONE);
		updateBtn.setText("\u66F4\u65B0");
		updateBtn.setImage(SWTResourceManager.getImage(WinRegion.class, "/res/images/ico_update_small.png"));
		updateBtn.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 16, SWT.NORMAL));
		updateBtn.setAlignment(SWT.LEFT);
		updateBtn.setBounds(645, 506, 100, 44);		
		updateBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinRegion.this.updateRegionInfo();
			}
		});	
		
		removeBtn = new Button(this, SWT.NONE);
		removeBtn.setText("\u5220\u9664");
		removeBtn.setImage(SWTResourceManager.getImage(WinRegion.class, "/res/images/ico_remove_small.png"));
		removeBtn.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 16, SWT.NORMAL));
		removeBtn.setAlignment(SWT.LEFT);
		removeBtn.setBounds(763, 506, 100, 44);
		removeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinRegion.this.removeRegionInfo();
			}
		});	
		
		cancelBtn = new Button(this, SWT.NONE);
		cancelBtn.setText("\u53D6\u6D88");
		cancelBtn.setImage(SWTResourceManager.getImage(WinRegion.class, "/res/images/icon_cancel.png"));
		cancelBtn.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 16, SWT.NORMAL));
		cancelBtn.setAlignment(SWT.LEFT);
		cancelBtn.setBounds(526, 506, 140, 44);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinRegion.this.cancel();
			}
		});	
		
		confirmBtn = new Button(this, SWT.NONE);
		confirmBtn.setText("\u786E\u5B9A");
		confirmBtn.setImage(SWTResourceManager.getImage(WinRegion.class, "/res/images/icon_confirm.png"));
		confirmBtn.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 16, SWT.NORMAL));
		confirmBtn.setBounds(723, 506, 140, 44);
		confirmBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WinRegion.this.confirm();
			}
		});	
		
		tipLbl = new Label(this, SWT.NONE);
		tipLbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		tipLbl.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		tipLbl.setBounds(556, 236, 304, 28);
		
		screenBound = display.getPrimaryMonitor().getBounds();
		createContents();
		initDataView();
	}

	protected void createContents() {
		setText("\u76F8\u5173\u533A\u57DF\u7BA1\u7406");
		setBounds((screenBound.width-900)/2, (screenBound.height-600)/2, 900, 600);
		cancelBtn.setVisible(false);
		confirmBtn.setVisible(false);
	}
	
	private void initDataView() {
		regionList = regionService.getRegionByQuery();
		mainGrid.setInput(regionList);
		if (regionList!=null && regionList.size()>0) {
			currentRegion = regionList.get(0);
		} else {
			currentRegion = null;
			nameTxt.setText("");
		}
		selectRegion(currentRegion);
	}
	
	private void addRegionInfo() {
		operMode = OPERMODE_ADD;
		setEditMode();		
		nameTxt.setText("");
	}
	
	protected void updateRegionInfo() {
		operMode = OPERMODE_UPDATE;
		setEditMode();
	}
	
	protected void removeRegionInfo() {
		if (currentRegion!=null) {
			MessageBox.showInfo(display, "ÄúÈ·¶¨ÒªÉ¾³ýÕâÒ»ÏîÂð?", this);
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
	}
	
	private void setViewMode() {
		addBtn.setVisible(true);
		updateBtn.setVisible(true);
		removeBtn.setVisible(true);
		cancelBtn.setVisible(false);
		confirmBtn.setVisible(false);
		
		tipLbl.setText("");
		nameTxt.setEnabled(false);
	}
	
	private void selectRegion(Region region) {
		setViewMode();
		if (region!=null) {
			nameTxt.setText(region.getName());			
			updateBtn.setEnabled(true);
			removeBtn.setEnabled(true);			
		} else {
			nameTxt.setText("");			
			updateBtn.setEnabled(false);
			removeBtn.setEnabled(false);
		}
	}
	
	private void confirm() {
		if (isValid()) {
			if (OPERMODE_ADD == operMode) {
				Region region = new Region();
				region.setName(StringUtil.trimString(nameTxt.getText()));
				regionService.saveRegion(region);
				initDataView();
				setViewMode();
			} else if (OPERMODE_UPDATE == operMode) {
				Region region = currentRegion;
				region.setName(StringUtil.trimString(nameTxt.getText()));
				regionService.updateRegion(region);
				initDataView();
				setViewMode();
			}
		}		
	}
	
	private void cancel() {
		selectRegion(currentRegion);
	}
	
	private boolean isValid() {
		String name = StringUtil.trimString(nameTxt.getText());
		if (StringUtil.isBlank(name)) {
			tipLbl.setText("ÇøÓòÃû³Æ²»ÄÜÎª¿ÕÅ¶£¡");
			return false;
		}
		return true;
	}
	
	public void confirmOperation() {
		if (currentRegion!=null) {
			regionService.removeRegion(currentRegion);
			initDataView();
		}
	}

	@Override
	protected void checkSubclass() {
	}
}

package com.sys.ui.data;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sys.bo.Bill;

public class BillLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener arg0) 
	{
	}

	@Override
	public void dispose() 
	{
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) 
	{
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) 
	{
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) 
	{
		return null;
	}

	@Override
	public String getColumnText(Object element, int index) {
		Bill bill = (Bill)element;
		if(index==0){
			return bill.getCode();
		}
		if(index==1){
			return "aaaa";
		} else {
			
		}
		return "bbbb";
	}
}

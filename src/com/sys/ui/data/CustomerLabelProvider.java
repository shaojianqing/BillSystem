package com.sys.ui.data;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sys.bo.Customer;

public class CustomerLabelProvider extends LabelProvider implements ITableLabelProvider  {
	
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Customer){
        	Customer customer = (Customer)element;
            if(columnIndex == 0){
                return customer.getName();
            }
        }
        return null;
    }

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}
}

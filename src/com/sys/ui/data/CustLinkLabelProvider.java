package com.sys.ui.data;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sys.vo.CustomerLink;

public class CustLinkLabelProvider extends LabelProvider implements ITableLabelProvider  {
	
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof CustomerLink){
        	CustomerLink customerLink = (CustomerLink)element;
            if(columnIndex == 0){
                return customerLink.getCustomerName();
            } else if (columnIndex == 1) {
            	return customerLink.getCustomerPhone();
            }
        }
        return null;
    }

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}
}
package com.sys.ui.data;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sys.bo.LinkWay;

public class LinkWayLabelProvider extends LabelProvider implements ITableLabelProvider  {
	
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof LinkWay){
        	LinkWay linkWay = (LinkWay)element;
            if(columnIndex == 0){
                return linkWay.getPhone();
            }
        }
        return null;
    }

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}
}

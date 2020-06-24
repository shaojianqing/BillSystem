package com.sys.ui.data;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sys.bo.Region;

public class RegionLabelProvider extends LabelProvider implements ITableLabelProvider  {
	
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Region){
        	Region region = (Region)element;
            if(columnIndex == 0){
                return region.getName();
            }
        }
        return null;
    }

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}
}

package com.sys.ui.data;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sys.bo.UserInfo;
import com.sys.util.RoleUtil;

public class UserLabelProvider extends LabelProvider implements ITableLabelProvider  {
	
	public String getColumnText(Object element, int columnIndex) {
        if (element instanceof UserInfo){
        	UserInfo userInfo = (UserInfo)element;
            if(columnIndex == 0){
                return userInfo.getName();
            }else if(columnIndex == 1){
                return RoleUtil.parseRole(userInfo.getRole());
            }else if (columnIndex ==2){
                return userInfo.getLink();
            }
        }
        return null;
    }

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}
}

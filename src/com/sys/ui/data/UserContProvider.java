package com.sys.ui.data;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class UserContProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {		
	}

	@Override
	@SuppressWarnings("rawtypes")	
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List){
            return ((List)inputElement).toArray();
        } else {
            return new Object[0];
        }
	}
}

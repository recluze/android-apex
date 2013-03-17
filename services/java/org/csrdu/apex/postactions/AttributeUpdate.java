package org.csrdu.apex.postactions;

import java.util.Vector;


import org.csrdu.apex.AttributeManager;
import org.csrdu.apex.interfaces.IApexPostAction;

public class AttributeUpdate implements IApexPostAction {

    @Override
	public Object execute(Vector<Object> params, AttributeManager am, String pkgName, String targetAttString) {
        // We can set only one value per attribute 
	    String newVal = params.get(0).toString(); 
	    return am.updateApplicationAttribute(pkgName, targetAttString, newVal);
	}
}

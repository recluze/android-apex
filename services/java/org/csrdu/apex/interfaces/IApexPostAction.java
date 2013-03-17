/* 
	Part of the Apex Framework. 

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

*/ 
package org.csrdu.apex.interfaces;

import java.util.Vector;

import org.csrdu.apex.AttributeManager;
import org.csrdu.apex.policy.Expression;

public interface IApexPostAction {

	public Object execute(Vector<Object> exprs, AttributeManager am, String pkgName, String targetAttr); 
	
}

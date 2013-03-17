/* 
	Part of the Apex Framework. 

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

*/ 

package org.csrdu.apex.functions;

import java.util.Vector;

import org.csrdu.apex.interfaces.IApexFunction;

/**
 * @author Nauman
 *
 */
public class NumberLessThan implements IApexFunction { 

	/** 
	 * @see org.csrdu.apex.interfaces.IApexFunction#evaluate(java.util.Vector)
	 */
	@Override
	public Object evaluate(Vector<Object> params) {
		int num1 = Integer.parseInt(params.get(0).toString());
		int num2 = Integer.parseInt(params.get(1).toString());
		return (num1 < num2);
	}
}

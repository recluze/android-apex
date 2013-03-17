/* 
	Part of the Apex Framework. 

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

*/ 

package org.csrdu.apex.policy;

import java.util.Vector;

import org.csrdu.apex.AttributeManager;
import org.csrdu.apex.interfaces.IApexCombiningAlgorithms;
import org.csrdu.apex.interfaces.IApexPostAction;

import android.util.Log;

public class PostAction {
	public String TAG = "APEX:PostAction";
    private Vector<Expression> expressions;
    private String ActionId = ""; 
    private String TargetAttribute = "";
    private String packageName = ""; 


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTargetAttribute() {
        return TargetAttribute;
    }

    public void setTargetAttribute(String targetAttribute) {
        TargetAttribute = targetAttribute;
    }

    public PostAction() {
        expressions = new Vector<Expression>();
    }
    
	/**
     * Add an expression to the constraint
     * 
     * @param expr
     *            Expression to be added
     */
    public void addExpression(Expression expr) {
        expressions.add(expr);
    }


    public String toString() {
        String strVal = "<PostAction ActionId="+this.ActionId+" TargetAttribute="+this.TargetAttribute+">";
        for (Expression expr : expressions) {
            strVal += expr.toString();
        }
        strVal += "</PostAction>\n";
        return strVal;

    }
    
    /**
     * Execute all post actions i.e. expressions. 
     * 
     * @param attributeManager
     *            AttributeManager for resolving/updating the application and system
     *            attributes
     * 
     * @return true if matched, false if it doesn't.
     */
    public boolean execute(AttributeManager attributeManager, String packageName) {
        Log.d(TAG, "Evaluating post action with ID: " + this.ActionId);
        Vector<Object> exprResults = new Vector<Object>();
        for (Expression expr : expressions) {
            exprResults.add(expr.evaluate(attributeManager, packageName));
        }
        
        IApexPostAction action;
        try {
            action = (IApexPostAction) Class.forName(this.ActionId).newInstance();
        } catch (IllegalAccessException e) {
            Log.d(TAG, "IllegalAccessException in post action.");
            e.printStackTrace();
            return false; // signifying NA 
        } catch (InstantiationException e) {
            Log.d(TAG, "InstantiationException in post action.");
            e.printStackTrace();
            return false; // signifying NA
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "Action not found: " + this.ActionId + ".");
            e.printStackTrace();
            return false; // signifying NA
        }
        Log.d(TAG, "Calling ActionId: " + this.ActionId + " with values:");
        for (Object _v : exprResults)
            Log.d(TAG, _v.toString());
        Boolean retVal = (Boolean) action.execute(exprResults, attributeManager, packageName, TargetAttribute);
        Log.d(TAG, "PostAction [" + this.ActionId + "] returning: " + retVal.toString());
        
        return true; 
    }

    public void setActionId(String _actionId) {
        this.ActionId = _actionId;
    }
}

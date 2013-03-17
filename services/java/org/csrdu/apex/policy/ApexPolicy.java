/* 
	Part of the Apex Framework. 

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

 */

package org.csrdu.apex.policy;

import java.util.Vector;

import org.csrdu.apex.AttributeManager;

import android.util.Log;

public class ApexPolicy {
	public String TAG = "APEX:ApexPolicy";

	static enum PolicyEffect {
		PERMIT, DENY
	}

	private String name;
	private PolicyEffect effect;

	private String permission = "";
	private Constraint constraint;
	private Vector<PostAction> postactions;

	
	public ApexPolicy(){ 
	    this.postactions = new Vector<PostAction>();
	}
	
	/**
	 * Get the constraint for this policy
	 * 
	 * @return The constraint
	 */
	public Constraint getConstraint() {
		return constraint;
	}

	/**
	 * Set the constraint for this policy
	 * 
	 * @param constraint
	 *            Constraint to set
	 */
	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	/**
     * Get the post actions for this policy
     * 
     * @return The postactions 
     */
	public Vector<PostAction> getPostActions() {
		return postactions;
	}

	/**
     * Add to post action for this policy
     * 
     * @param postaction
     *            PostAction to add 
     */
	public void addPostAction(PostAction postaction) {
		this.postactions.add(postaction);
	}

	/**
	 * Set the Effect of the policy
	 * 
	 * @param effect
	 *            The effect. May be PERMIT, DENY or NA 
	 * 
	 */
	public void setEffect(PolicyEffect effect) {
		this.effect = effect;
	}

	/**
	 * Overridden toString function
	 */
	public String toString() {
		String strVal = "\n<ApexPolicy Effect='" + effect + "'>\n";
		strVal += "<Permission Name='" + permission + "'/>\n";

		if (this.constraint != null)
			strVal += this.constraint.toString();
		
		for (PostAction p : this.postactions)
            strVal += p.toString();

		strVal += "</ApexPolicy>\n";
		return strVal;
	}

	/**
	 * Evaluate the policy.
	 * 
	 * @param attributeManager
	 *            AttributeManager for resolving application and system
	 *            attributes
	 * @param packageName
	 *            Package this policy is associated with
	 * 
	 * @return true if granted, false if denies.
	 */
	public boolean evaluatePolicy(AttributeManager attributeManager, String packageName) {
		Log.d(TAG, "Evaluating policy. " + this.name);
		// evaluate all constraints
		boolean evaluationResult = constraint.evaluate(attributeManager, packageName);

		Log.d(TAG, "Got policy result:" + String.valueOf(evaluationResult));
        if (evaluationResult) {
            // updates are run only if evaluation result is true. 
            Log.d(TAG, "Running updates for policy. " + this.name);
            for (PostAction a : postactions) { 
                a.execute(attributeManager, packageName);
            }

            Log.d(TAG, "Policy effect is:" + String.valueOf(effect));
            if (effect == PolicyEffect.PERMIT) {
                Log.d(TAG, "Final evaluation result with permit is:" + String.valueOf(evaluationResult));
                return evaluationResult;
            } else if (effect == PolicyEffect.DENY) {
                Log.d(TAG, "Final evaluation result with deny is:" + String.valueOf(!evaluationResult));
                return !evaluationResult;
            } else {
                Log.d(TAG, "Found unexpected Policy Effect. Returning false.");
                return true; // signifying grant access
            }
        } else {
		    Log.d(TAG, "Final evaluation result is NA");
		    return true; 
		}
	}

	/**
	 * Set associated permission
	 * 
	 * @param permission
	 *            the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * Get associated permission
	 * 
	 * @param permission
	 *            the permission to set
	 * @return
	 */
	public String getPermission() {
		return this.permission;
	}

}

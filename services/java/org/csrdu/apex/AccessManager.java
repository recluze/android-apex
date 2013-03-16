/* 
Part of the Apex Framework. 


Provided under GPLv2. 

author: Nauman (recluze@gmail.com) 
		http://csrdu.org/nauman 

 */

package org.csrdu.apex;

import java.util.HashMap;

import org.csrdu.apex.policy.ApexPackagePolicy;
import org.csrdu.apex.policy.PolicyNotFoundException;

import android.util.Log;

public class AccessManager {
	private String TAG = "APEX:AccessManager";
	// private String permDirectory = "/system/etc/apex/perms/";
	private String permDirectory = "/data/data/apex-";

	private HashMap<String, ApexPackagePolicy> packagePolicies = new HashMap<String, ApexPackagePolicy>();
	private AttributeManager attributeManager;

	public AccessManager() {
		Log.d(TAG, "Initializing AccessManager...");
	}

	public boolean checkExtendedPermissionByPackage(String permName, String packageName) {
		// get the attribute manager
		attributeManager = AttributeManager.getSingletonInstance();

		long startTime = System.currentTimeMillis();

		Log.d(TAG, "Checking permission: " + permName + " for " + packageName);
		Log.d(TAG, "Checking if package policy is present in cache.");
		ApexPackagePolicy app = null;     

		if (packagePolicies.containsKey(packageName)) {
			Log.d(TAG, "Loaded package policy from cache. Skipping file parsing.");
			app = packagePolicies.get(packageName);
		} else {
			Log.d(TAG, "Failed to find package policy in cache. Parsing file...");
			app = new ApexPackagePolicy(packageName);
			try {
				app.loadPoliciesForPackage(packageName, permDirectory);
			} catch (PolicyNotFoundException e) {
				Log.d(TAG, "Failed to load policy. Granting access.");
			}
			Log.d(TAG, "Policy: " + app.toString());
			packagePolicies.put(packageName, app);
			Log.d(TAG, "Saved policy for package in cache: " + packageName);
		}

		long endTime = System.currentTimeMillis();
		Log.d(TAG, "Loaded policy in " + (endTime - startTime) + " ms");

		startTime = System.currentTimeMillis();

		Log.d(TAG, "Checking if there are any policies associated with permission: " + permName);
		boolean evaluationResult;
		if (!app.hasPoliciesForPermission(permName)) {
			Log.d(TAG, "No policies for permission: " + permName + ". Granting access.");
			logRet(packageName, permName, true);
			return true;
		}

		try {
			Log.d(TAG, "Evaluating policy for: " + packageName);
			evaluationResult = app.evaluatePolicies(attributeManager, permName);
			Log.d(TAG, "Got final result for policy evaluation: " + evaluationResult);
		} catch (Exception e) {
			Log.d(TAG, "Unexpected error while evaluating policies for: " + packageName);
			Log.d(TAG, "Grudgingly allowing access...");
			logRet(packageName, permName, true);
			return true;
		}
		endTime = System.currentTimeMillis();
		Log.d(TAG, "Evaluated policy in " + (endTime - startTime) + " ms");
		
		logRet(packageName, permName, evaluationResult);
		// return true if all checks pass
		return evaluationResult;
	}

	private void logRet(String packageName, String permName, boolean evaluationResult){ 
		Log.i(TAG, "PERM: [" + String.format("%-50s", permName) + "] for ["
				+ String.format("%-50s", packageName) + "] - ["
				+ (evaluationResult ? "PERMIT" : " DENY ") + "]");
	}
	
	/**
	 * Remove package policy from the cahce. Might be useful when the user
	 * updates policy from the package manager. After invalidation, the policy
	 * will be parsed automatically when it's next needed.
	 * 
	 * @param packageName
	 *            The package for which the policy should be removed from the
	 *            cache.
	 */
	public void invalidatePackagePolicyInCache(String packageName) {
		Log.d(TAG, "Invalidating package policy for package: " + packageName);
		this.packagePolicies.remove(packageName);
	}
}

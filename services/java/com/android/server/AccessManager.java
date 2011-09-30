/* 
	Part of the Apex Framework. 
	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

*/ 

package com.android.server;

import android.util.Log;

public class AccessManager{
	private String TAG = "ACCESSMANAGER"; 
	public AccessManager(){
		Log.d(TAG, "Initiating AccessManager...");
	
	}
	
	public boolean checkExtendedPermissionByPackage(String permName, String packageName){
		Log.d(TAG, "Checking permission: " + permName + " for " + packageName);
		
		return true; 
	}

}

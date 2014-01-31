package com.desire2learn.android.sdk.test;

import java.util.Iterator;
import java.util.Set;

import com.desire2learn.android.sdk.ModuleIntent;
import com.desire2learn.android.sdk.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is a very simple activity wrapper.
 * 
 * Do not modify!
 * 
 * @author wlee
 *
 */
public class LaunchModuleActivity extends Activity {
	private static final String MODULE_CLASSNAME_META = "module_classname";
	private static final String MODULE_TITLE_META = "module_title";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.d2l_sdk_testharness_main);
        final TextView infoBox = (TextView) findViewById(R.id.InfoBox);
                
        final Bundle metaDataBundle;
		try {
			metaDataBundle = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA).metaData;
		} catch (NameNotFoundException e1) {			
			infoBox.setText("Error loading metadata.  Please verify it is specified in the AndroidManifest.xml");			
			return;
		}

		if (metaDataBundle == null) {
			infoBox.setText("Error loading metadata.  Please verify it is specified in the AndroidManifest.xml");
			return;
		}
		
    	final String moduleClassName = metaDataBundle.getString(MODULE_CLASSNAME_META);
		final String moduleTitle = metaDataBundle.getString(MODULE_TITLE_META);
		try {
			Class.forName(moduleClassName)	;
		} catch (ClassNotFoundException e) {
			infoBox.setText("Unable to find class: "+moduleClassName+"\n Please verify metadata:module_classname specified in LaunchModuleActivity in the AndroidManifest.xml file.");
			return;
		}
		
		StringBuilder moduleInfo = new StringBuilder();
		moduleInfo.append("**Module Info**\n\n");
		moduleInfo.append("Classname: '" + moduleClassName+"'\n\n");
		moduleInfo.append("Title: '" + moduleTitle + "'\n\n");
		StringBuilder sbParams = new StringBuilder();
		sbParams.append("**Bundle Parameters**\n\n");
		
		
		Set<String> meta = metaDataBundle.keySet();
		Iterator<String> i = meta.iterator();
		while (i.hasNext())
		{
			String key = i.next();	
			String value = metaDataBundle.getString(key);
			if (key.equals(MODULE_CLASSNAME_META) || key.equals(MODULE_TITLE_META)) {
				continue;
			} else {
				sbParams.append(key + " = '" + value + "'\n\n");
			}
		}
		moduleInfo.append(sbParams.toString());		
		infoBox.setText(moduleInfo.toString());
                
        Button button = (Button) findViewById(R.id.launch_button);        
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new OnClickListener() {
       	
		public void onClick(View v) {			
			try {
				Intent i = new Intent(LaunchModuleActivity.this, Class.forName(moduleClassName));
				i.putExtra(ModuleIntent.EXTRA_MODULE_TITLE, moduleTitle);
				i.putExtra(ModuleIntent.EXTRA_MODULE_PARAMETER_BUNDLE, metaDataBundle);
				startActivity(i);				
			} catch (ClassNotFoundException e) {				
				infoBox.setText("Unable to find class: "+moduleClassName+"\n Please verify metadata:module_classname specified in LaunchModuleActivity in the AndroidManifest.xml file.");
			}
		}
				
		});
        
    }
}
package com.baidu.soushang;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class SouShangApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		DisplayImageOptions displayOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(displayOptions).build();
		ImageLoader.getInstance().init(config);
	}

}

package com.baidu.soushang;

import com.baidu.soushang.bean.FeatureEventBean;
import com.baidu.soushang.cloudapis.ShopInfo;

public class Variables {
	public static FeatureEventBean feBean = null;
	public static ShopInfo shBean = null;
	public static int homeFlag = 1;
	public static int daliyFeatureFlag = 0;
	public static String CATID = "1";
	static {
		feBean = new FeatureEventBean();
		shBean = new ShopInfo();
	}
}

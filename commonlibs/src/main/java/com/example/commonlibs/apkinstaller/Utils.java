package com.example.commonlibs.apkinstaller;

import java.util.List;

public class Utils {
	
	public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }
	
	public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

}

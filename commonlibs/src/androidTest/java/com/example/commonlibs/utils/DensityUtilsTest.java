package com.example.commonlibs.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DensityUtilsTest {

    @Test
    public void dp2px() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        float scale = appContext.getResources().getDisplayMetrics().density;
        int iscale = (int) (12.5 * scale + 0.5);
        System.out.println(iscale);
        int result = DensityUtils.INSTANCE.dp2px(appContext, 12.5f);
        System.out.println(result);
        assertEquals(iscale, result, 0);
    }

    @Test
    public void sp2px() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        float fontScale = appContext.getResources().getDisplayMetrics().scaledDensity;
        int scale = (int) (12 * fontScale + 0.5f);
        int result = (int) DensityUtils.INSTANCE.sp2px(appContext, 12);
        System.out.println(scale);
        System.out.println(result);
        assertEquals(scale, result, 0);
    }

    @Test
    public void px2dp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        float scale =  appContext.getResources().getDisplayMetrics().density;
        int scal = (int) (12 / scale + 0.5f);
        int result = (int) DensityUtils.INSTANCE.px2dp(appContext, 12);
        System.out.println(scal);
        System.out.println(result);
        assertEquals(scal, result, 0);
    }

    @Test
    public void px2sp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        float scale = appContext.getResources().getDisplayMetrics().scaledDensity;
        int scal = (int) (12/scale+0.5f);
        int result  = (int) DensityUtils.INSTANCE.px2sp(appContext,12);
        System.out.println(scal);
        System.out.println(result);
        assertEquals(scal, result, 0);
    }
}
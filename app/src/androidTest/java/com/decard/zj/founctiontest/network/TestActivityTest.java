package com.decard.zj.founctiontest.network;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.decard.zj.founctiontest.BitmapActivity;
import com.decard.zj.founctiontest.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestActivityTest {

    @Rule
    public ActivityTestRule<BitmapActivity> activityScenarioRule
            = new ActivityTestRule<>(BitmapActivity.class);

//    @Test
//    public void onCreate() {
//        onView(withId(R.id.btn_test)).perform(click());
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
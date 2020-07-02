package com.decard.zj.founctiontest.network;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.decard.zj.founctiontest.picture.BitmapActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
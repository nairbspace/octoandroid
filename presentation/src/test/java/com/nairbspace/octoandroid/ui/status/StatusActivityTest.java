package com.nairbspace.octoandroid.ui.status;

import com.nairbspace.octoandroid.BuildConfig;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class StatusActivityTest {
    private StatusActivity mActivity;

    @Before
    public void setup() {
        mActivity = Robolectric.setupActivity(StatusActivity.class);
    }
}

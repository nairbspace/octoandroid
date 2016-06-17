package com.nairbspace.octoandroid.ui.add_printer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;

import com.nairbspace.octoandroid.BuildConfig;
import com.nairbspace.octoandroid.NetworkSecurityPolicyHack;
import com.nairbspace.octoandroid.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, shadows = NetworkSecurityPolicyHack.class)
public class AddPrinterActivityTest {
    private ActivityController<AddPrinterActivity> mController;
    private AddPrinterActivity mActivity;

    @Before
    public void setup() {

        // Issues:

        // Cannot have custom dimension in layout_width
        // <item name="match_parent" type="dimen">-1</item>
        // <dimen name="add_printer_scroll_view_width">@dimen/match_parent</dimen>
        // android:layout_width="@dimen/add_printer_scroll_view_width"

        // Need to hardcode imeActionId and reference from integer resource
        // android:imeActionId="@+id/add_printer"


        // Normal setup
//        mActivity = Robolectric.setupActivity(AddPrinterActivity.class);

        mController = Robolectric.buildActivity(AddPrinterActivity.class);
    }

    @Test
    public void validatePrinterNameEditTextWithRotate() {
        createWithIntent("extra");
        TextInputEditText printerNameEditText = (TextInputEditText) mActivity.findViewById(R.id.printer_name_edit_text);
        assertNotNull("Printer Name could not be found", printerNameEditText);
        String restore = "Restore";
        printerNameEditText.setText(restore);

        destroyActivity(null);
        mActivity = createNewActivity(null);
        printerNameEditText = (TextInputEditText) mActivity.findViewById(R.id.printer_name_edit_text);
        assert printerNameEditText != null;
        assertTrue("PrinterName does not restore", restore.equals(printerNameEditText.getText().toString()));
    }

    @Test
    public void recreatesActivity() {
        createWithIntent("extra");
        Bundle bundle = new Bundle();

        destroyActivity(bundle);
        mActivity = createNewActivity(bundle);
    }

    private void destroyActivity(Bundle bundle) {
        mController.saveInstanceState(bundle)
                .pause().stop().destroy();
    }

    private AddPrinterActivity createNewActivity(Bundle bundle) {
        mController = Robolectric.buildActivity(AddPrinterActivity.class)
                .create(bundle)
                .start()
                .restoreInstanceState(bundle)
                .resume()
                .visible();
        return mController.get();
    }

    @After
    public void tearDown() {
        mController.pause().stop().destroy();
    }

    private void createWithIntent(String extra) {
        Intent intent = new Intent(RuntimeEnvironment.application, AddPrinterActivity.class);
        intent.putExtra("activity_extra", extra);
        mActivity = mController
                .withIntent(intent)
                .create()
                .start()
                .resume()
                .visible()
                .get();
    }
}

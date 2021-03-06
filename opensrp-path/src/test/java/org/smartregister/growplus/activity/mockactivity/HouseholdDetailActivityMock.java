package org.smartregister.path.activity.mockactivity;

import android.os.Bundle;
import android.view.Menu;

import org.smartregister.Context;
import org.smartregister.growplus.R;
import org.smartregister.growplus.activity.HouseholdDetailActivity;
import org.smartregister.growplus.activity.HouseholdSmartRegisterActivity;

/**
 * Created by kaderchowdhury on 04/12/17.
 */

public class HouseholdDetailActivityMock extends HouseholdDetailActivity {

    public static Context mContext;

    @Override
    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme); //we need this here
        super.onCreate(bundle);
    }

    public static void setmContext(Context Context) {
        mContext = Context;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

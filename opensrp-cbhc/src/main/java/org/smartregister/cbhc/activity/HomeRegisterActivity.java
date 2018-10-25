package org.smartregister.cbhc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import org.smartregister.cbhc.fragment.AdvancedSearchFragment;
import org.smartregister.cbhc.fragment.BaseRegisterFragment;
import org.smartregister.cbhc.fragment.HomeRegisterFragment;
import org.smartregister.cbhc.fragment.LibraryFragment;
import org.smartregister.cbhc.fragment.MeFragment;
import org.smartregister.cbhc.fragment.SortFilterFragment;
import org.smartregister.cbhc.presenter.RegisterPresenter;
import org.smartregister.cbhc.util.Constants;
import org.smartregister.configurableviews.model.Field;

import java.util.Arrays;
import java.util.List;

/**
 * Created by keyman on 26/06/2018.
 */

public class HomeRegisterActivity extends BaseRegisterActivity {
	
	public static final int ADVANCED_SEARCH_POSITION = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public BaseRegisterFragment getRegisterFragment() {
        return new HomeRegisterFragment();
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[]{new AdvancedSearchFragment(), new SortFilterFragment(), new MeFragment(), new LibraryFragment()};
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return superOnOptionsItemsSelected(item);

    }

    @Override
    protected void initializePresenter() {
        presenter = new RegisterPresenter(this);
    }

    @Override
    public List<String> getViewIdentifiers() {
        return Arrays.asList(Constants.CONFIGURATION.HOME_REGISTER);
    }

    protected boolean superOnOptionsItemsSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void updateSortAndFilter(List<Field> filterList, Field sortField) {
        mBaseFragment.updateSortAndFilter(filterList, sortField);
        switchToBaseFragment();
    }
	
	public void startAdvancedSearch() {
		try {
			mPager.setCurrentItem(ADVANCED_SEARCH_POSITION, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

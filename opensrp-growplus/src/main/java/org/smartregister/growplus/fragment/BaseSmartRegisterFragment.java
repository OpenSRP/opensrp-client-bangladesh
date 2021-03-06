package org.smartregister.growplus.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;


import org.json.JSONException;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.growplus.R;
import org.smartregister.growplus.activity.BaseRegisterActivity;
import org.smartregister.growplus.activity.ChildImmunizationActivity;
import org.smartregister.growplus.view.LocationPickerView;
import org.smartregister.provider.SmartRegisterClientsProvider;
import org.smartregister.view.activity.SecuredNativeSmartRegisterActivity;

import util.JsonFormUtils;
import util.PathConstants;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class BaseSmartRegisterFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {

    private LocationPickerView clinicSelection;

    @Override
    protected SecuredNativeSmartRegisterActivity.DefaultOptionsProvider getDefaultOptionsProvider() {
        return null;
    }

    @Override
    protected SecuredNativeSmartRegisterActivity.NavBarOptionsProvider getNavBarOptionsProvider() {
        return null;
    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }

    @Override
    protected void onInitialization() {
    }

    @Override
    protected void startRegistration() {

    }

    @Override
    protected void onCreation() {
    }

    protected TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(final CharSequence cs, int start, int before, int count) {
            filter(cs.toString(), "", "");
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void openVaccineCard(final String filterString) {
        FilterForClientTask filterForClientTask = new FilterForClientTask();
        filterForClientTask.execute(filterString);
    }

    @Override
    protected void setupViews(View view) {
        super.setupViews(view);

        View viewParent = (View) appliedSortView.getParent();
        viewParent.setVisibility(View.GONE);

        clinicSelection = (LocationPickerView) view.findViewById(R.id.clinic_selection);
        clinicSelection.init(context());

    }

    protected void filter(String filterString, String joinTableString, String mainConditionString) {
        filters = filterString;
        joinTable = joinTableString;
        mainCondition = mainConditionString;
        getSearchCancelView().setVisibility(isEmpty(filterString) ? INVISIBLE : VISIBLE);
        CountExecute();
        filterandSortExecute();
    }

    @Override
    public void showProgressView() {
        if (clientsProgressView.getVisibility() == INVISIBLE) {
            clientsProgressView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressView() {
        if (clientsProgressView.getVisibility() == VISIBLE) {
            clientsProgressView.setVisibility(INVISIBLE);
        }
    }

    private class FilterForClientTask extends AsyncTask<String, Integer, CommonPersonObjectClient> {
        private String searchQuery;

        public FilterForClientTask() {
            this.searchQuery = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressView();
        }

        @Override
        protected CommonPersonObjectClient doInBackground(String... params) {
            searchQuery = params[0];
            Cursor cursor = null;
            CommonPersonObjectClient client = null;
            try {
                String query = "";

                SmartRegisterQueryBuilder sqb = new SmartRegisterQueryBuilder(mainSelect);
                query = sqb.addCondition(" WHERE " + getTablename() + ".zeir_id = " + searchQuery);
                query = sqb.Endquery(sqb.addlimitandOffset(query, 1, 0));

                cursor = commonRepository().rawCustomQueryForAdapter(query);
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                    CommonPersonObject personinlist = commonRepository().readAllcommonforCursorAdapter(cursor);
                    client = new CommonPersonObjectClient(personinlist.getCaseId(), personinlist.getDetails(), personinlist.getDetails().get("FWHOHFNAME"));
                    client.setColumnmaps(personinlist.getColumnmaps());
                }

            } catch (Exception e) {
                Log.e(getClass().getName(), e.toString(), e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return client;
        }

        @Override
        protected void onPostExecute(CommonPersonObjectClient client) {
            super.onPostExecute(client);
            hideProgressView();
            if (client != null) {
                ChildImmunizationActivity.launchActivity(getActivity(), client, null);
            } else {
                NotInCatchmentDialogFragment.launchDialog((BaseRegisterActivity) getActivity(),
                        DIALOG_TAG, searchQuery);
            }
        }
    }

    protected void updateLocationText() {
        if(clinicSelection != null) {
            clinicSelection.setText(JsonFormUtils.getOpenMrsReadableName(
                    clinicSelection.getSelectedItem()));
            try {

                String locationId = JsonFormUtils.getOpenMrsLocationId(context(), clinicSelection.getSelectedItem());
                Context.getInstance().allSharedPreferences().savePreference(PathConstants.CURRENT_LOCATION_ID,locationId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public LocationPickerView getClinicSelection() {
        return clinicSelection;
    }

    public boolean onBackPressed(){
        return false;
    }
}
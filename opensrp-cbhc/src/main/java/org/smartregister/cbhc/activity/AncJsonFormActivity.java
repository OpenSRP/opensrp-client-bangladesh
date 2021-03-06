package org.smartregister.cbhc.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.cbhc.application.AncApplication;
import org.smartregister.cbhc.domain.draft_form_object;
import org.smartregister.cbhc.fragment.AncJsonFormFragment;
import org.smartregister.cbhc.repository.DraftFormRepository;
import org.smartregister.cbhc.util.JsonFormUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.smartregister.cbhc.util.JsonFormUtils.METADATA;
import static org.smartregister.util.JsonFormUtils.getJSONObject;


/**
 * Created by ndegwamartin on 30/06/2018.
 */
public class AncJsonFormActivity extends JsonFormActivity {

    private static final String TAG = AncJsonFormActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initializeFormFragment() {
        initializeFormFragmentCore();
    }

    @Override
    public void writeValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId) throws JSONException {
        callSuperWriteValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
    }

    @Override
    public void onFormFinish() {
        callSuperFinish();
    }

    protected void callSuperFinish() {
        super.onFormFinish();
    }

    protected void callSuperWriteValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId) throws JSONException {
        super.writeValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
    }

    protected void initializeFormFragmentCore() {
        AncJsonFormFragment ancJsonFormFragment = AncJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction()
                .add(com.vijay.jsonwizard.R.id.container, ancJsonFormFragment).commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(this, com.vijay.jsonwizard.R.style.AppThemeAlertDialog)
                .setTitle(getConfirmCloseTitle())
                .setMessage(getConfirmCloseMessage())
                .setNegativeButton(com.vijay.jsonwizard.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveDraft();
                        finish();
                    }
                })
                .setPositiveButton(com.vijay.jsonwizard.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "No button on dialog in " + JsonFormActivity.class.getCanonicalName());


                    }
                })
                .create();

        dialog.show();
    }

    private void saveDraft() {
        JSONObject partialform = getmJSONObject();

        DraftFormRepository draftFormRepository = new DraftFormRepository(AncApplication.getInstance().getRepository());
        draft_form_object draftFormObject = new draft_form_object();
        draftFormObject.setDraftFormJson(currentJsonState());
        processDraftForm(partialform,draftFormObject);
        draftFormRepository.add(draftFormObject);
    }

    private void processDraftForm(JSONObject partialform, draft_form_object draftFormObject) {
        try {
            String formname = partialform.getString("encounter_type");
            draftFormObject.setFormNAME(formname);

            JSONObject metadata = getJSONObject(partialform, METADATA);
            JSONObject lookUpJSONObject = getJSONObject(metadata, "look_up");
            String lookUpEntityId = "";
            String lookUpBaseEntityId = "";
            if (lookUpJSONObject != null) {
                lookUpEntityId = JsonFormUtils.getString(lookUpJSONObject, "entity_id");
                lookUpBaseEntityId = JsonFormUtils.getString(lookUpJSONObject, "value");
            }
            if(!isBlank(lookUpEntityId)&& !isBlank(lookUpBaseEntityId)){
                draftFormObject.setHousehold_BASE_ENTITY_ID(lookUpBaseEntityId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


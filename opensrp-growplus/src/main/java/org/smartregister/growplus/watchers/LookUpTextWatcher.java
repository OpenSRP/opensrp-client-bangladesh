package org.smartregister.growplus.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.vijay.jsonwizard.R;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.event.Listener;
import org.smartregister.growplus.domain.EntityLookUp;
import org.smartregister.growplus.fragment.PathJsonFormFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.MotherLookUpUtils;

public class LookUpTextWatcher implements TextWatcher {
    private static Map<String, EntityLookUp> lookUpMap;

    private final View mView;
    private final JsonFormFragment formFragment;
    private final String mEntityId;
    public String relationalid;

    public LookUpTextWatcher(JsonFormFragment formFragment, View view, String entityId,String relationalid) {
        this.formFragment = formFragment;
        mView = view;
        mEntityId = entityId;
        lookUpMap = new HashMap<>();
        this.relationalid=relationalid;

    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    public void afterTextChanged(Editable editable) {
        String text = (String) mView.getTag(R.id.raw_value);

        if (text == null) {
            text = editable.toString();
        }

        String key = (String) mView.getTag(R.id.key);

        boolean afterLookUp = (Boolean) mView.getTag(R.id.after_look_up);
        if (afterLookUp) {
            mView.setTag(R.id.after_look_up, false);
            return;
        }

        EntityLookUp entityLookUp = new EntityLookUp();
        if (lookUpMap.containsKey(mEntityId)) {
            entityLookUp = lookUpMap.get(mEntityId);
        }

        if (StringUtils.isBlank(text)) {
            if (entityLookUp.containsKey(key)) {
                entityLookUp.remove(key);
            }
        } else {
            entityLookUp.put(key, text);
        }

        lookUpMap.put(mEntityId, entityLookUp);

        Context context = null;
        Listener<HashMap<CommonPersonObject, List<CommonPersonObject>>> listener = null;
        if (formFragment instanceof PathJsonFormFragment) {
            PathJsonFormFragment pathJsonFormFragment = (PathJsonFormFragment) formFragment;
            context = pathJsonFormFragment.context();
            listener = pathJsonFormFragment.motherLookUpListener();
        }

        if (mEntityId.equalsIgnoreCase("mother")) {
            MotherLookUpUtils.motherLookUp(context, lookUpMap.get(mEntityId), listener, null,relationalid);
        }

    }

}

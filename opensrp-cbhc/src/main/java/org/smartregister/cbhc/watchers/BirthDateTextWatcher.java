package org.smartregister.cbhc.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.vijay.jsonwizard.R;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.cbhc.domain.EntityLookUp;
import org.smartregister.cbhc.fragment.AncJsonFormFragment;

import java.util.HashMap;
import java.util.Map;


public class BirthDateTextWatcher implements TextWatcher {
    private static Map<String, EntityLookUp> lookUpMap;
    public String relationalid;
    private View mView;
    private JsonFormFragment formFragment;
    private String mEntityId;


    public BirthDateTextWatcher(JsonFormFragment formFragment, View view) {

        this.formFragment = formFragment;
        mView = view;
        lookUpMap = new HashMap<>();

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
        if (!StringUtils.isEmpty(text))
            ((AncJsonFormFragment) formFragment).setAgeFromBirthDate(text);
    }

}
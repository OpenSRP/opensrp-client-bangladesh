package org.smartregister.path.interactors;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import org.smartregister.growplus.widgets.PathDatePickerFactory;
import org.smartregister.growplus.widgets.PathEditTextFactory;

/**
 * Created by keyman on 11/04/2017.
 */
public class PathJsonFormInteractor extends JsonFormInteractor {

    private static final JsonFormInteractor INSTANCE = new PathJsonFormInteractor();

    private PathJsonFormInteractor() {
        super();
    }

    @Override
    protected void registerWidgets() {
        super.registerWidgets();
        map.put(JsonFormConstants.EDIT_TEXT, new PathEditTextFactory());
        map.put(JsonFormConstants.DATE_PICKER, new PathDatePickerFactory());
//        map.put(JsonFormConstants.LABEL, new PathCalculateLabelFactory());
    }

    public static JsonFormInteractor getInstance() {
        return INSTANCE;
    }
}

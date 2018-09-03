package org.smartregister.cbhc.contract;

import android.content.Context;

import org.smartregister.cbhc.domain.QuickCheck;
import org.smartregister.cbhc.domain.QuickCheckConfiguration;
import org.smartregister.configurableviews.model.Field;


public interface QuickCheckContract {
    interface Presenter {

        void setReason(Field reason);

        QuickCheckConfiguration getConfig();

        boolean containsComplaintOrDangerSign(Field field, boolean isDangerSign);

        void modifyComplaintsOrDangerList(Field field, boolean isChecked, boolean isDangerSign);

        void proceedToNormalContact(String specify);

        void referAndCloseContact(String specify, Boolean refer);

        void setBaseEntityId(String baseEntityId);

    }

    interface View {

        Context getContext();

        String getString(int resId);

        void displayComplaintLayout();

        void hideComplaintLayout();

        void notifyComplaintAdapter();

        void displayDangerSignLayout();

        void notifyDangerSignAdapter();

        void showSpecifyEditText();

        void hideSpecifyEditText();

        void displayNavigationLayout();

        void displayReferButton();

        void hideReferButton();

        void displayToast(int resourceId);

        void dismiss();

    }

    interface Interactor {

        void saveQuickCheckEvent(QuickCheck quickCheck, String baseEntityId, InteractorCallback callback);

    }

    interface InteractorCallback {

        void quickCheckSaved(boolean saved);
    }

}

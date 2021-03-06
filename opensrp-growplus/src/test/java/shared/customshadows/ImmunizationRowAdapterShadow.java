package shared.customshadows;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;

import org.robolectric.ShadowsAdapter;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.util.Scheduler;
import org.smartregister.immunization.adapter.ImmunizationRowAdapter;
import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.ArrayList;

/**
 * Created by coder on 06/07/2017.
 */

@Implements(ImmunizationRowAdapter.class)
public class ImmunizationRowAdapterShadow implements ShadowsAdapter {
    @Override
    public Scheduler getBackgroundScheduler() {
        return null;
    }

    @Override
    public ShadowLooperAdapter getMainLooper() {
        return null;
    }

    @Override
    public String getShadowActivityThreadClassName() {
        return null;
    }

    @Override
    public void setupLogging() {

    }

    @Override
    public String getShadowContextImplClassName() {
        return null;
    }

    @Override
    public void bind(Application application, AndroidManifest appManifest) {

    }


    @Implementation
    public void update(ArrayList<VaccineWrapper> vaccinesToUpdate) {
    }

    @Implementation
    public ArrayList<VaccineWrapper> getDueVaccines() {
        return new ArrayList<>();
    }

    @Implementation
    private void updateWrapper(VaccineWrapper tag) {

    }

    @Implementation
    public int getCount() {
        return 0;
    }

    @Implementation
    public Object getItem(int position) {
        return 0;
    }

    @Implementation
    public long getItemId(int position) {
        return 231231 + position;
    }

    @Implementation
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}

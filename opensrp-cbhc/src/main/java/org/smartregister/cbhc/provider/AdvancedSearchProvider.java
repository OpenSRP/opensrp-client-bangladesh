package org.smartregister.cbhc.provider;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;
import org.smartregister.cbhc.R;
import org.smartregister.cbhc.fragment.BaseRegisterFragment;
import org.smartregister.cbhc.helper.ImageRenderHelper;
import org.smartregister.cbhc.util.DBConstants;
import org.smartregister.cbhc.util.Utils;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.cursoradapter.RecyclerViewProvider;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.smartregister.util.Utils.getName;

/**
 * Created by keyman on 26/06/2018.
 */

public class AdvancedSearchProvider implements RecyclerViewProvider<AdvancedSearchProvider.AdvancedSearchViewHolder> {


    private final LayoutInflater inflater;
    private Set<org.smartregister.configurableviews.model.View> visibleColumns;
    private View.OnClickListener onClickListener;

    private Context context;
    private CommonRepository commonRepository;
    private ImageRenderHelper imageRenderHelper;


    public AdvancedSearchProvider(Context context, CommonRepository commonRepository, Set visibleColumns, View.OnClickListener onClickListener) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.visibleColumns = visibleColumns;
        this.onClickListener = onClickListener;
        this.context = context;
        this.commonRepository = commonRepository;
        this.imageRenderHelper = new ImageRenderHelper(context);
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient client, AdvancedSearchViewHolder viewHolder) {
        CommonPersonObjectClient pc = (CommonPersonObjectClient) client;
        if (visibleColumns.isEmpty()) {
            populatePatientColumn(pc, client, viewHolder);
            populateIdentifierColumn(pc, viewHolder);
            populateLastColumn(pc, viewHolder);

            return;
        }

       /* for (org.smartregister.configurableviews.model.View columnView : visibleColumns) {
            switch (columnView.getIdentifier()) {
                case ID:
                    populatePatientColumn(pc, client, convertView);
                    break;
                case NAME:
                    populateIdentifierColumn(pc, convertView);
                    break;
                case DOSE:
                    populateDoseColumn(pc, convertView);
                    break;
                default:
            }
        }

        Map<String, Integer> mapping = new HashMap();
        mapping.put(ID, R.id.patient_column);
        mapping.put(DOSE, R.id.identifier_column);
        mapping.put(NAME, R.id.dose_column);
        ConfigurableViewsLibrary.getInstance().getConfigurableViewsHelper().processRegisterColumns(mapping, convertView, visibleColumns, R.id.register_columns);
        */
    }

    private void populatePatientColumn(CommonPersonObjectClient pc, SmartRegisterClient client, AdvancedSearchViewHolder viewHolder) {

        String firstName = org.smartregister.util.Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.FIRST_NAME, true);
        String lastName = org.smartregister.util.Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LAST_NAME, true);
        String patientName = getName(firstName, lastName);

        fillValue(viewHolder.patientName, WordUtils.capitalize(patientName));

        String dobString = Utils.getDuration(org.smartregister.util.Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.DOB, false));
        dobString = dobString.contains("y") ? dobString.substring(0, dobString.indexOf("y")) : dobString;
        fillValue((viewHolder.age), String.format(context.getString(R.string.age_text), dobString));

        View patient = viewHolder.patientColumn;
        attachPatientOnclickListener(patient, client);
    }


    private void populateIdentifierColumn(CommonPersonObjectClient pc, AdvancedSearchViewHolder viewHolder) {
        String ancId = org.smartregister.util.Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.ANC_ID, false);
        fillValue(viewHolder.ancId, String.format(context.getString(R.string.anc_id_text), ancId));
    }

    private void attachPatientOnclickListener(View view, SmartRegisterClient client) {
        view.setOnClickListener(onClickListener);
        view.setTag(client);
        view.setTag(R.id.VIEW_ID, BaseRegisterFragment.CLICK_VIEW_NORMAL);
    }


    private void populateLastColumn(CommonPersonObjectClient pc, AdvancedSearchViewHolder viewHolder) {

        if (commonRepository != null) {
            CommonPersonObject commonPersonObject = commonRepository.findByBaseEntityId(pc.entityId());
            if (commonPersonObject != null) {
                imageRenderHelper.refreshProfileImage(pc.entityId(), viewHolder.profile);

                viewHolder.sync.setVisibility(View.GONE);
                viewHolder.profile.setVisibility(View.VISIBLE);
            } else {
                viewHolder.profile.setVisibility(View.GONE);
                viewHolder.sync.setVisibility(View.VISIBLE);
                attachSyncOnclickListener(viewHolder.sync, pc);
            }
        }
    }

    private void attachSyncOnclickListener(View view, SmartRegisterClient client) {
        view.setOnClickListener(onClickListener);
        view.setTag(client);
        view.setTag(R.id.VIEW_ID, BaseRegisterFragment.CLICK_VIEW_SYNC);
    }

    @Override
    public SmartRegisterClients updateClients(FilterOption filterOption, ServiceModeOption serviceModeOption, FilterOption filterOption1, SortOption sortOption) {
        return null;
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) { //Implement Abstract Method
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String s, String s1, String s2) {
        return null;
    }

    @Override
    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public AdvancedSearchProvider.AdvancedSearchViewHolder createViewHolder(ViewGroup parent) {
        View view;
        view = inflater.inflate(R.layout.advanced_result_list_row, parent, false);

        /*
        ConfigurableViewsHelper helper = ConfigurableViewsLibrary.getInstance().getConfigurableViewsHelper();
        if (helper.isJsonViewsEnabled()) {

            ViewConfiguration viewConfiguration = helper.getViewConfiguration(Constants.CONFIGURATION.HOME_REGISTER_ROW);
            ViewConfiguration commonConfiguration = helper.getViewConfiguration(COMMON_REGISTER_ROW);

            if (viewConfiguration != null) {
                return helper.inflateDynamicView(viewConfiguration, commonConfiguration, view, R.id.register_columns, false);
            }
        }*/

        return new AdvancedSearchViewHolder(view);
    }

    public static void fillValue(TextView v, String value) {
        if (v != null)
            v.setText(value);

    }

    ////////////////////////////////////////////////////////////////
    // Inner classes
    ////////////////////////////////////////////////////////////////

    public class AdvancedSearchViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName;
        public TextView age;
        public TextView ga;
        public TextView ancId;
        public TextView risk;

        public CircleImageView profile;
        public Button sync;

        public View patientColumn;


        public AdvancedSearchViewHolder(View itemView) {
            super(itemView);

            patientName = itemView.findViewById(R.id.patient_name);
            age = itemView.findViewById(R.id.age);
            ga = itemView.findViewById(R.id.ga);
            ancId = itemView.findViewById(R.id.anc_id);
            risk = itemView.findViewById(R.id.risk);
            profile = itemView.findViewById(R.id.profile);
            sync = itemView.findViewById(R.id.sync);

            patientColumn = itemView.findViewById(R.id.patient_column);
        }
    }

}

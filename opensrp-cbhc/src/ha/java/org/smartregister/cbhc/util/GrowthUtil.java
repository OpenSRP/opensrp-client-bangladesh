package org.smartregister.cbhc.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.smartregister.cbhc.R;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Photo;
import org.smartregister.growthmonitoring.GrowthMonitoringLibrary;
import org.smartregister.growthmonitoring.domain.Height;
import org.smartregister.growthmonitoring.domain.HeightWrapper;
import org.smartregister.growthmonitoring.domain.Weight;
import org.smartregister.growthmonitoring.domain.WeightWrapper;

import org.smartregister.growthmonitoring.fragment.EditGrowthDialogFragment;
import org.smartregister.growthmonitoring.fragment.RecordGrowthDialogFragment;
import org.smartregister.growthmonitoring.repository.HeightRepository;
import org.smartregister.growthmonitoring.repository.WeightRepository;
import org.smartregister.growthmonitoring.util.ImageUtils;
import org.smartregister.util.DateUtil;
import org.smartregister.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static org.smartregister.util.Utils.getName;
import static org.smartregister.util.Utils.getValue;

public class GrowthUtil {
    // Dummpy values, Can be changed manually
    public static String ENTITY_ID = "1";
    public static final double BIRTH_WEIGHT = 3.8d;
    public static final double BIRTH_HEIGHT = 50d;
    public static String GENDER = (new Random()).nextBoolean() ? "male" : "female";
    public static String DOB_STRING = "2012-01-01T00:00:00.000Z";
    public static CommonPersonObjectClient childDetails;
    private static String KG_FORMAT = "%s kg";
    private static String CM_FORMAT = "%s cm";
    public static void showGrowthDialog(FragmentActivity context, View view, String tag) {
        WeightWrapper weightWrapper = view.getTag() != null ? (WeightWrapper) view.getTag() : new WeightWrapper();
        HeightWrapper heightWrapper = view.getTag() != null ? (HeightWrapper) view.getTag() : new HeightWrapper();

        weightWrapper.setPatientName(childDetails.getColumnmaps().get("first_name")+" "+childDetails.getColumnmaps().get("last_name"));
        heightWrapper.setPatientName(childDetails.getColumnmaps().get("first_name")+" "+childDetails.getColumnmaps().get("last_name"));

        String dob = childDetails.getColumnmaps().get("dob");
        if(dob!=null){
//            dob = dob.substring(0,dob.indexOf("T"));
            String duration = DateUtil.getDuration(new DateTime().getMillis()-new DateTime(dob).getMillis());
            weightWrapper.setPatientAge(duration);
            heightWrapper.setPatientAge(duration);
        }



        RecordGrowthDialogFragment recordGrowthDialogFragment = RecordGrowthDialogFragment
                .newInstance(getDateOfBirth(), weightWrapper, heightWrapper);
        recordGrowthDialogFragment.show(initFragmentTransaction(context, tag), tag);
    }

    public static Date getDateOfBirth() {
        LocalDate localDate = new LocalDate();
        //DOB for sample app needs to ba dynamic
        DateTime dateTime = localDate.minusYears(5).plusMonths(2).toDateTime(LocalTime.now());
        dateTime = new DateTime(DOB_STRING);
        Date dob = dateTime.toDate();

        return dob;
    }

    public static android.support.v4.app.FragmentTransaction initFragmentTransaction(FragmentActivity context, String tag) {
        FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
        Fragment prev = context.getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
    }

    public static void showEditGrowthMonitoringDialog(FragmentActivity context, CommonPersonObjectClient childDetails, int i, String tag) {

//        CommonPersonObjectClient childDetails = dummydetails();

        String firstName = Utils.getValue(childDetails.getColumnmaps(), "first_name", true);
        String lastName = Utils.getValue(childDetails.getColumnmaps(), "last_name", true);
        String childName = getName(firstName, lastName).trim();

        String gender = getValue(childDetails.getColumnmaps(), "gender", true);

        String zeirId = getValue(childDetails.getColumnmaps(), "zeir_id", false);
        String duration = "";
        String dobString = getValue(childDetails.getColumnmaps(), "dob", false);
        if (StringUtils.isNotBlank(dobString)) {
            DateTime dateTime = new DateTime(getValue(childDetails.getColumnmaps(), "dob", false));
            duration = DateUtil.getDuration(dateTime);
        }

        Photo photo = ImageUtils.profilePhotoByClient(childDetails);

        WeightWrapper weightWrapper = getWeightWrapper(i, childDetails, childName, gender, zeirId, duration, photo);
        HeightWrapper heightWrapper = getHeightWrapper(i, childDetails, childName, gender, zeirId, duration, photo);

        EditGrowthDialogFragment editGrowthDialogFragment = EditGrowthDialogFragment
                .newInstance(context, getDateOfBirth(), weightWrapper, heightWrapper);
        editGrowthDialogFragment.show(initFragmentTransaction(context, tag), tag);

    }


//    public static CommonPersonObjectClient dummydetails() {
//        HashMap<String, String> columnMap = new HashMap<>();
//        columnMap.put("first_name", "Test");
//        columnMap.put("last_name", "Doe");
//        columnMap.put("zeir_id", "1");
//        columnMap.put("dob", StringUtils.reverseDelimited(
//                new SimpleDateFormat(DateUtil.DATE_FORMAT_FOR_TIMELINE_EVENT, new Locale("en"))
//                        .format(GrowthUtil.getDateOfBirth()), '-'));
//        columnMap.put("gender", GENDER);
//
//
//        CommonPersonObjectClient personDetails = new CommonPersonObjectClient(ENTITY_ID, columnMap, "Test");
//        personDetails.setColumnmaps(columnMap);
//
//        return personDetails;
//    }

    private static WeightWrapper getWeightWrapper(int i, CommonPersonObjectClient childDetails, String childName,
                                                  String gender, String zeirId, String duration, Photo photo) {
        WeightWrapper weightWrapper = new WeightWrapper();
        weightWrapper.setId(childDetails.entityId());
        WeightRepository wp = GrowthMonitoringLibrary.getInstance().weightRepository();
        List<Weight> weightlist = wp.findLast5(childDetails.entityId());
        if (!weightlist.isEmpty()) {
            weightWrapper.setWeight(weightlist.get(i).getKg());
            weightWrapper.setUpdatedWeightDate(new DateTime(weightlist.get(i).getDate()), false);
            weightWrapper.setDbKey(weightlist.get(i).getId());
        }

        weightWrapper.setGender(gender);
        weightWrapper.setPatientName(childName);
        weightWrapper.setPatientNumber(zeirId);
        weightWrapper.setPatientAge(duration);
        weightWrapper.setPhoto(photo);
        weightWrapper.setPmtctStatus(getValue(childDetails.getColumnmaps(), "pmtct_status", false));
        return weightWrapper;
    }

    private static HeightWrapper getHeightWrapper(int i, CommonPersonObjectClient childDetails, String childName,
                                                  String gender, String zeirId, String duration, Photo photo) {
        HeightWrapper heightWrapper = new HeightWrapper();
        heightWrapper.setId(childDetails.entityId());
        HeightRepository wp = GrowthMonitoringLibrary.getInstance().heightRepository();
        List<Height> heightList = wp.findLast5(childDetails.entityId());
        if (!heightList.isEmpty()) {
            heightWrapper.setHeight(heightList.get(i).getCm());
            heightWrapper.setUpdatedHeightDate(new DateTime(heightList.get(i).getDate()), false);
            heightWrapper.setDbKey(heightList.get(i).getId());
        }

        heightWrapper.setGender(gender);
        heightWrapper.setPatientName(childName);
        heightWrapper.setPatientNumber(zeirId);
        heightWrapper.setPatientAge(duration);
        heightWrapper.setPhoto(photo);
        heightWrapper.setPmtctStatus(getValue(childDetails.getColumnmaps(), "pmtct_status", false));
        return heightWrapper;
    }

    public static void createWeightWidget(Activity context, View fragmentContainer,
                                          HashMap<Long, Pair<String, String>> last_five_weight_map,
                                          ArrayList<View.OnClickListener> listeners, ArrayList<Boolean> editenabled) {

        LinearLayout tableLayout = fragmentContainer.findViewById(R.id.weightvalues);
        tableLayout.removeAllViews();

        int i = 0;
        for (Map.Entry<Long, Pair<String, String>> entry : last_five_weight_map.entrySet()) {
            Pair<String, String> pair = entry.getValue();
            View view = createTableRowForWeight(context, tableLayout, pair.first, pair.second, editenabled.get(i),
                    listeners.get(i));

            tableLayout.addView(view);
            i++;
        }
    }

    public static View createTableRowForWeight(Activity context, ViewGroup container, String labelString, String valueString,
                                               boolean editenabled, View.OnClickListener listener) {
        View rows = context.getLayoutInflater().inflate(R.layout.tablerows_weight, container, false);
        TextView label = rows.findViewById(R.id.label);
        TextView value = rows.findViewById(R.id.value);
        Button edit = rows.findViewById(R.id.edit);
        if (editenabled) {
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(listener);
        } else {
            edit.setVisibility(View.INVISIBLE);
        }
        label.setText(labelString);
        value.setText(valueString);
        return rows;
    }

    public static void createHeightWidget(Activity context, View fragmentContainer,
                                          HashMap<Long, Pair<String, String>> last_five_weight_map,
                                          ArrayList<View.OnClickListener> listeners, ArrayList<Boolean> editenabled) {

        LinearLayout tableLayout = fragmentContainer.findViewById(R.id.heightvalues);
        tableLayout.removeAllViews();

        int i = 0;
        for (Map.Entry<Long, Pair<String, String>> entry : last_five_weight_map.entrySet()) {
            Pair<String, String> pair = entry.getValue();
            View view = createTableRowForHeight(context, tableLayout, pair.first, pair.second, editenabled.get(i),
                    listeners.get(i));

            tableLayout.addView(view);
            i++;
        }
    }

    public static View createTableRowForHeight(Activity context, ViewGroup container, String labelString, String valueString,
                                               boolean editenabled, View.OnClickListener listener) {
        View rows = context.getLayoutInflater().inflate(R.layout.tablerows_weight, container, false);
        TextView label = rows.findViewById(R.id.label);
        TextView value = rows.findViewById(R.id.value);
        Button edit = rows.findViewById(R.id.edit);
        if (editenabled) {
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(listener);
        } else {
            edit.setVisibility(View.INVISIBLE);
        }
        label.setText(labelString);
        value.setText(valueString);
        return rows;
    }
    public static String kgStringSuffix(Float weight) {
        return String.format(KG_FORMAT, weight);
    }

    public static String kgStringSuffix(String weight) {
        return String.format(KG_FORMAT, weight);
    }

    public static String cmStringSuffix(Float height) {
        return String.format(CM_FORMAT, height);
    }

    public static String cmStringSuffix(String height) {
        return String.format(CM_FORMAT, height);
    }
}

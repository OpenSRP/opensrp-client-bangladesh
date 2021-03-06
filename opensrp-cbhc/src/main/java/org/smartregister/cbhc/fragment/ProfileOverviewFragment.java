package org.smartregister.cbhc.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.opensrp.api.constants.Gender;
import org.smartregister.CoreLibrary;
import org.smartregister.cbhc.R;
import org.smartregister.cbhc.activity.ProfileActivity;
import org.smartregister.cbhc.application.AncApplication;
import org.smartregister.cbhc.repository.AncRepository;
import org.smartregister.cbhc.util.Constants;
import org.smartregister.cbhc.util.DBConstants;
import org.smartregister.cbhc.util.ImageUtils;
import org.smartregister.cbhc.util.JsonFormUtils;
import org.smartregister.cbhc.util.Utils;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.domain.ProfileImage;
import org.smartregister.repository.DetailsRepository;
import org.smartregister.repository.ImageRepository;
import org.smartregister.util.DateUtil;
import org.smartregister.util.OpenSRPImageLoader;
import org.smartregister.view.activity.DrishtiApplication;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.smartregister.util.Utils.getName;
import static org.smartregister.util.Utils.getValue;

/**
 * Created by ndegwamartin on 12/07/2018.
 */
public class ProfileOverviewFragment extends BaseProfileFragment {

    private CommonPersonObjectClient householdDetails;
    private ListView householdList;
    public static final String EXTRA_HOUSEHOLD_DETAILS = "household_details";
    public View fragmentView;
    HashMap<String,Drawable>profile_photo = new HashMap<String,Drawable>();
    public View getFragmentView() {
        return fragmentView;
    }

    public static ProfileOverviewFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        ProfileOverviewFragment fragment = new ProfileOverviewFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getActivity().getIntent().getExtras();
        if (extras != null) {
            Serializable serializable = extras.getSerializable(EXTRA_HOUSEHOLD_DETAILS);
            if (serializable != null && serializable instanceof CommonPersonObjectClient) {
                householdDetails = (CommonPersonObjectClient) serializable;
            }
        }
    }

    @Override
    protected void onCreation() {
        //Overriden
    }

    @Override
    protected void onResumption() {
        //Overriden
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_profile_overview, container, false);
        refreshadapter(fragmentView);
        return fragmentView;
    }

    public void refreshadapter(View view) {
        //setAdapter data of Household member
        AncRepository repo = (AncRepository) AncApplication.getInstance().getRepository();
        net.sqlcipher.database.SQLiteDatabase db = repo.getReadableDatabase();
        String mother_id = householdDetails.getDetails().get("_id");

        String tableName = DBConstants.WOMAN_TABLE_NAME;
        String childtableName = DBConstants.CHILD_TABLE_NAME;
        String membertablename = DBConstants.MEMBER_TABLE_NAME;

        SmartRegisterQueryBuilder queryBUilder = new SmartRegisterQueryBuilder();
        queryBUilder.SelectInitiateMainTable(tableName, new String[]{
                tableName + ".relationalid",
                tableName + ".details",
                tableName + ".first_name",
                tableName + "." + DBConstants.KEY.LAST_NAME,
                tableName + ".dob"
        });
        String currentquery = queryBUilder.mainCondition("relational_id = '"+mother_id+"'").concat(" Union all ");
        SmartRegisterQueryBuilder queryBUilder2 = new SmartRegisterQueryBuilder();
        queryBUilder2.SelectInitiateMainTable(membertablename, new String[]{
                membertablename + ".relationalid",
                membertablename + ".details",
                membertablename + ".first_name",
                membertablename + "." + DBConstants.KEY.LAST_NAME,
                membertablename + ".dob"
        });
        currentquery = currentquery.concat(queryBUilder2.mainCondition("relational_id = '"+mother_id+"'").concat(" Union all "));
        queryBUilder.SelectInitiateMainTable(childtableName, new String[]{
                childtableName + ".relationalid",
                childtableName + ".details",
                childtableName + ".first_name",
                childtableName + "." + DBConstants.KEY.LAST_NAME,
                childtableName + ".dob"
        });

//        cursor = db.rawQuery(currentquery.concat(queryBUilder.mainCondition("relational_id = ?")),new String[]{mother_id});
String rawQuery = queryfortheadapterthing(mother_id);
        cursor = db.rawQuery(rawQuery,new String[]{});
        householdList = (ListView)view.findViewById(R.id.household_list);

        HouseholdCursorAdpater cursorAdpater = new HouseholdCursorAdpater(getContext(),cursor);

        householdList.setAdapter(cursorAdpater);
    }

    public String queryfortheadapterthing(String id){
        String query = "Select woman.id as _id , woman.relationalid , woman.details , woman.first_name , woman.last_name , woman.dob , details.value as relation " +
                "FROM ec_woman as woman, ec_details as details WHERE woman.relational_id = '</>' " +
                "and details.base_entity_id = woman.id and details.key = 'Realtion_With_Household_Head' " +
                "Union all " +
                " " +
                "Select member.id as _id , member.relationalid , member.details , member.first_name , member.last_name , member.dob, details.value as relation " +
                "FROM ec_member as member, ec_details as details WHERE member.relational_id = '</>' " +
                "and details.base_entity_id = member.id and details.key = 'Realtion_With_Household_Head' " +
                "Union all " +
                "" +
                "Select child.id as _id , child.relationalid , child.details , child.first_name , child.last_name , child.dob , details.value as relation " +
                "FROM ec_child as child, ec_details as details WHERE child.relational_id = '</>'  " +
                "and details.base_entity_id = child.id " +
                "and details.key = 'Realtion_With_Household_Head'";
        return query.replaceAll("</>",id);
    }
    Cursor cursor;
    class HouseholdCursorAdpater extends CursorAdapter {
        private Context context;
        private LayoutInflater inflater = null;

        public HouseholdCursorAdpater(Context context, Cursor c) {
            super(context, c);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public void bindView(View view, final Context context, Cursor cursor) {
            Log.e("------------","bind org.smartregister.cbhc.view call");
            CommonRepository commonRepository = org.smartregister.Context.getInstance().commonrepository(DBConstants.WOMAN_TABLE_NAME);
            CommonPersonObject personinlist = commonRepository.readAllcommonforCursorAdapter(cursor);
            final CommonPersonObjectClient pClient = new CommonPersonObjectClient(personinlist.getCaseId(), personinlist.getDetails(), personinlist.getDetails().get("FWHOHFNAME"));
            pClient.setColumnmaps(personinlist.getColumnmaps());
            TextView member_name = (TextView) view.findViewById(R.id.name_tv);
            TextView relation_tv = (TextView) view.findViewById(R.id.relation_tv);
            TextView member_age = (TextView) view.findViewById(R.id.age_tv);
            String relation = personinlist.getColumnmaps().get("relation");

            String firstName = org.smartregister.util.Utils.getValue(pClient.getColumnmaps(), DBConstants.KEY.FIRST_NAME, true);
            String lastName = org.smartregister.util.Utils.getValue(pClient.getColumnmaps(), DBConstants.KEY.LAST_NAME, true);
            if((lastName!=null&&lastName.equalsIgnoreCase("null"))||lastName==null){
                lastName = "";
            }
            if(relation!=null)
                relation_tv.setText("("+relation+")");
            String patientName = getName(firstName, lastName);

            if(profile_photo.get(pClient.entityId())==null){
                ImageRepository imageRepo = CoreLibrary.getInstance().context().imageRepository();
                ProfileImage imageRecord = imageRepo.findByEntityId(pClient.entityId());
                if(imageRecord!=null){
                    profile_photo.put(pClient.entityId(),Drawable.createFromPath(imageRecord.getFilepath()));
                }
            }

            ;
//            int nameColumnIndex = cursor.getColumnIndex("first_name");
//            member_name.setText("Name : " + cursor.getString(nameColumnIndex));
//            member_name.setText(getValue(personinlist.getColumnmaps(),"first_name",true));

            member_name.setText(patientName);

            DetailsRepository detailsRepository = AncApplication.getInstance().getContext().detailsRepository();
            Map<String, String> detailmap = detailsRepository.getAllDetailsForClient(pClient.getCaseId());
            String gender = detailmap.get("gender");
            //            String dobString = cursor.getString(cursor.getColumnIndex("dob"));
            String dobString = getValue(personinlist.getColumnmaps(),"dob",true);
            int age = 0;
            try {
             age =   getAge((new DateTime(dobString)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String durationString = "";
            if (StringUtils.isNotBlank(dobString)) {
                try {
                    DateTime birthDateTime = new DateTime(dobString);

                    String duration = DateUtil.getDuration(birthDateTime);
                    if (duration != null) {
                        durationString = duration;
                    }
                } catch (Exception e) {
                    Log.e(getClass().getName(), e.toString(), e);
                }
            }
            member_age.setText("Age : "+durationString);
            ((LinearLayout)view.findViewById(R.id.profile_name_layout)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    WomanImmunizationActivity.launchActivity(HouseholdDetailActivity.this,pClient,null);
                }
            });

            pClient.getColumnmaps().putAll(detailmap);
            LinearLayout editButton = (LinearLayout)view.findViewById(R.id.edit_member);
            editButton.setTag(pClient);
            editButton.setOnClickListener((ProfileActivity)getActivity());
            ImageView profileImageIV = (ImageView)view.findViewById(R.id.profile_image_iv);
            String clientype = "";

//
//            if (pClient.entityId() != null) {//image already in local storage most likey ):
//                //set profile image by passing the client id.If the image doesn't exist in the image org.smartregister.cbhc.repository then download and save locally
//                profileImageIV.setTag(org.smartregister.R.id.entity_id, pClient.entityId());
//                DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(pClient.entityId(), OpenSRPImageLoader.getStaticImageListener((ImageView) profileImageIV, R.drawable.woman_placeholder, R.drawable.woman_placeholder));
//
//            }
            Drawable d = null;
            if(age<5){
                if(gender.equalsIgnoreCase("m")){
                    if (pClient.entityId() != null) {//image already in local storage most likey ):
                        //set profile image by passing the client id.If the image doesn't exist in the image org.smartregister.cbhc.repository then download and save locally
                        profileImageIV.setTag(org.smartregister.R.id.entity_id, pClient.entityId());
                        if(profile_photo.get(pClient.entityId())==null){
                            d = getResources().getDrawable(R.drawable.child_boy_infant);
                            profile_photo.put(pClient.entityId(),d);
                        }


//                        profileImageIV.setImageDrawable(getResources().getDrawable(R.drawable.child_boy_infant));
//                        DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(pClient.entityId(), OpenSRPImageLoader.getStaticImageListener(profileImageIV, R.drawable.child_boy_infant, R.drawable.child_boy_infant));
                        clientype = "malechild";
                    }
                }else if(gender.equalsIgnoreCase("f")){
                    if (pClient.entityId() != null) {//image already in local storage most likey ):
                        //set profile image by passing the client id.If the image doesn't exist in the image org.smartregister.cbhc.repository then download and save locally
                        profileImageIV.setTag(org.smartregister.R.id.entity_id, pClient.entityId());
                        if(profile_photo.get(pClient.entityId())==null){
                            d = getResources().getDrawable(R.drawable.child_girl_infant);
                            profile_photo.put(pClient.entityId(),d);
                        }


//                        profileImageIV.setImageDrawable(getResources().getDrawable(R.drawable.child_girl_infant));
//                        DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(pClient.entityId(), OpenSRPImageLoader.getStaticImageListener(profileImageIV, R.drawable.child_girl_infant, R.drawable.child_girl_infant));
                        clientype = "femalechild";
                    }
                }
            }else{
                if(gender.equalsIgnoreCase("m")){
                    if (pClient.entityId() != null) {//image already in local storage most likey ):
                        //set profile image by passing the client id.If the image doesn't exist in the image org.smartregister.cbhc.repository then download and save locally
                        profileImageIV.setTag(org.smartregister.R.id.entity_id, pClient.entityId());
                        if(profile_photo.get(pClient.entityId())==null){
                            d = getResources().getDrawable(R.drawable.male_cbhc_placeholder);
                            profile_photo.put(pClient.entityId(),d);
                        }


//                        profileImageIV.setImageDrawable(getResources().getDrawable(R.drawable.male_cbhc_placeholder));
//                        DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(pClient.entityId(), OpenSRPImageLoader.getStaticImageListener(profileImageIV, R.drawable.male_cbhc_placeholder, R.drawable.male_cbhc_placeholder));
                        clientype = "member";
                    }
                }else if(gender.equalsIgnoreCase("f")){
                    if (pClient.entityId() != null) {//image already in local storage most likey ):
                        //set profile image by passing the client id.If the image doesn't exist in the image org.smartregister.cbhc.repository then download and save locally
                        profileImageIV.setTag(org.smartregister.R.id.entity_id, pClient.entityId());
                        if(profile_photo.get(pClient.entityId())==null){
                            d = getResources().getDrawable(R.drawable.women_cbhc_placeholder);
                            profile_photo.put(pClient.entityId(),d);
                        }


//                        profileImageIV.setImageDrawable(getResources().getDrawable(R.drawable.women_cbhc_placeholder));
//                        DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(pClient.entityId(), OpenSRPImageLoader.getStaticImageListener(profileImageIV, R.drawable.women_cbhc_placeholder, R.drawable.women_cbhc_placeholder));
                        clientype = "woman";
                    }
                }
            }
            if(profile_photo.get(pClient.entityId())!=null){
                profileImageIV.setImageDrawable(profile_photo.get(pClient.entityId()));
            }
            profileImageIV.setTag(R.id.clientformemberprofile,pClient);
            profileImageIV.setTag(R.id.typeofclientformemberprofile,clientype);
            profileImageIV.setOnClickListener((ProfileActivity)getActivity());

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            Log.e("------------","new org.smartregister.cbhc.view call");

            View view = inflater.inflate(R.layout.household_details_list_row,parent,false);

            return  view;
        }

        public void addChild(LinearLayout household_details_list_row, String mother_id){
            Log.e("--------------",mother_id);


            AncRepository repo = (AncRepository) AncApplication.getInstance().getRepository();
            net.sqlcipher.database.SQLiteDatabase db = repo.getReadableDatabase();


            String tableName = DBConstants.CHILD_TABLE_NAME;
            String parentTableName = DBConstants.WOMAN_TABLE_NAME;
            SmartRegisterQueryBuilder queryBUilder = new SmartRegisterQueryBuilder();
            queryBUilder.SelectInitiateMainTable(tableName, new String[]{
                    tableName + ".relationalid",
                    tableName + ".details",
                    tableName + ".openmrs_id",
                    tableName + ".relational_id",
                    tableName + ".first_name",
                    tableName + ".last_name",
                    tableName + ".gender",
                    parentTableName + ".first_name as mother_first_name",
                    parentTableName + ".last_name as mother_last_name",
                    parentTableName + ".dob as mother_dob",
                    parentTableName + ".nrc_number as mother_nrc_number",
                    tableName + ".father_name",
                    tableName + ".dob",
                    tableName + ".epi_card_number",
                    tableName + ".contact_phone_number",
                    tableName + ".pmtct_status",
                    tableName + ".provider_uc",
                    tableName + ".provider_town",
                    tableName + ".provider_id",
                    tableName + ".provider_location_id",
                    tableName + ".client_reg_date",
                    tableName + ".last_interacted_with",
                    tableName + ".inactive",
                    tableName + ".lost_to_follow_up"
            });
            queryBUilder.customJoin("LEFT JOIN " + parentTableName + " ON  " + tableName + ".relational_id =  " + parentTableName + ".id");
            String mainCondition = " (dod is NULL OR dod = '' )";
            String mainSelect = queryBUilder.mainCondition(mainCondition);




            Cursor cursor = db.rawQuery(mainSelect+ "and "+tableName+".relational_id = ?",new String[]{mother_id});



            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                CommonRepository commonRepository = org.smartregister.Context.getInstance().commonrepository(tableName);
                CommonPersonObject personinlist = commonRepository.readAllcommonforCursorAdapter(cursor);
                final CommonPersonObjectClient pClient = new CommonPersonObjectClient(personinlist.getCaseId(), personinlist.getDetails(), personinlist.getDetails().get("FWHOHFNAME"));
                pClient.setColumnmaps(personinlist.getColumnmaps());
                LinearLayout childrenLayout = (LinearLayout)inflater.inflate(R.layout.household_details_child_row, null);
                ((TextView)childrenLayout.findViewById(R.id.name_tv)).setText("Name : " + cursor.getString(cursor.getColumnIndex("first_name")));

                String dobString = cursor.getString(cursor.getColumnIndex("dob"));
                String durationString = "";
                if (StringUtils.isNotBlank(dobString)) {
                    try {
                        DateTime birthDateTime = new DateTime(dobString);
                        String duration = DateUtil.getDuration(birthDateTime);
                        if (duration != null) {
                            durationString = duration;
                        }
                    } catch (Exception e) {
                        Log.e(getClass().getName(), e.toString(), e);
                    }
                }
                ((TextView)childrenLayout.findViewById(R.id.age_tv)).setText("Age : "+durationString);

                Gender gender = Gender.UNKNOWN;
                if (pClient != null && pClient.getDetails() != null) {
                    String genderString = getValue(pClient, "gender", false);
                    if (genderString != null && genderString.toLowerCase().equals("female")) {
                        gender = Gender.FEMALE;
                    } else if (genderString != null && genderString.toLowerCase().equals("male")) {
                        gender = Gender.MALE;
                    }
                    ImageView profileImageIV = (ImageView)childrenLayout.findViewById(R.id.profile_image_iv);

                    if (pClient.entityId() != null) {//image already in local storage most likey ):
                        //set profile image by passing the client id.If the image doesn't exist in the image org.smartregister.cbhc.repository then download and save locally
                        profileImageIV.setTag(org.smartregister.R.id.entity_id, pClient.entityId());
                        DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(pClient.entityId(), OpenSRPImageLoader.getStaticImageListener((ImageView) profileImageIV, ImageUtils.profileImageResourceByGender(gender), ImageUtils.profileImageResourceByGender(gender)));

                    }
                }


                household_details_list_row.addView(childrenLayout);
                childrenLayout.findViewById(R.id.profile_name_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ChildImmunizationActivity.launchActivity(HouseholdDetailActivity.this,pClient,null);
                    }
                });
                cursor.moveToNext();
            }
            cursor.close();

        }

    }

    public static int getAge(DateTime dateOfBirth) {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        SimpleDateFormat dateFormat = JsonFormUtils.DATE_FORMAT;
        Date convertedDate = new Date();
        try {
            convertedDate = dateOfBirth.toDate();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        birthDate.setTime(convertedDate);
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of
        // leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR)
                - today.get(Calendar.DAY_OF_YEAR) > 3)
                || (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of
            // month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH))
                && (birthDate.get(Calendar.DAY_OF_MONTH) > today
                .get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cursor!=null&&!cursor.isClosed()){
            cursor.close();
        }
        profile_photo.clear();
    }
}

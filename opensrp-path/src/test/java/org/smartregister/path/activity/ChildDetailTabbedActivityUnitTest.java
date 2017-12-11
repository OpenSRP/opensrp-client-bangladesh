package org.smartregister.path.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.path.R;
import org.smartregister.path.activity.mocks.mockactivity.ChildDetailTabbedActivityTestVersion;
import org.smartregister.path.activity.mocks.MenuItemTestVersion;
import org.smartregister.path.toolbar.ChildDetailsToolbar;
import org.smartregister.repository.DetailsRepository;
import org.smartregister.util.EasyMap;
import org.smartregister.util.FormUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import shared.BaseUnitTest;
import shared.customshadows.ImmunizationRowAdapterShadow;
import shared.customshadows.ImmunizationRowCardShadow;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.times;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

/**
 * created by onadev on 07/06/2017.
 */

@PrepareForTest({org.smartregister.Context.class})
@Config(shadows = {ImmunizationRowAdapterShadow.class, ImmunizationRowCardShadow.class})
public class ChildDetailTabbedActivityUnitTest extends BaseUnitTest {

    @InjectMocks
    private ChildDetailTabbedActivityTestVersion activity;

    @Mock
    private FormUtils formUtils;

    @Mock
    private CommonPersonObjectClient childDetails;

    @Mock
    private DetailsRepository detailsRepository;

    @Mock
    private org.smartregister.Context context_;
    private ActivityController<ChildDetailTabbedActivityTestVersion> controller;
    private Map<String, String> details;

    @Mock
    private ServiceWrapper serviceWrapper;

    @Mock
    private View view;
    public static final String EXTRA_CHILD_DETAILS = "child_details";

    @Before
    public void setUp() throws Exception {
        details = new HashMap<>();
        Intent intent = new Intent(RuntimeEnvironment.application, ChildDetailTabbedActivityTestVersion.class);
        intent.putExtra("location_name", "Nairobi");
//        HashMap<String,String>details = new HashMap<>();
//        CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient("caseId",details,"name");
//        commonPersonObjectClient.setColumnmaps(details);
//        intent.putExtra(EXTRA_CHILD_DETAILS,commonPersonObjectClient);
        controller = Robolectric.buildActivity(ChildDetailTabbedActivityTestVersion.class, intent);
        activity = controller.get();

        org.mockito.MockitoAnnotations.initMocks(this);

        CoreLibrary.init(context_);

        activity.detailsRepository = getDetailsRepository();
        controller.setup();
}

    @After
    public void tearDown() {
        destroyController();
        details = null;
        activity = null;
        controller = null;
        context_ = null;
        detailsRepository = null;
        childDetails = null;

    }

    @Test
    public void shouldRenderAvatarImageView() {

        ImageView logoImageView = (ImageView) activity.findViewById(R.id.profile_image_iv);
        assertNotNull(logoImageView);

    }

    @Test
    public void shouldRenderEditIconImageView() {

        ImageView logoImageView = (ImageView) activity.findViewById(R.id.imageView2);
        assertNotNull(logoImageView);

    }

    @Test
    public void shouldRenderChildNameTextView() {

        TextView nameView = (TextView) activity.findViewById(R.id.name);
        assertNotNull(nameView);

    }


    @Test
    public void shouldRenderZierIDTextView() {

        TextView textView = (TextView) activity.findViewById(R.id.idforclient);
        assertNotNull(textView);

    }

    @Test
    public void shouldRenderStatusImageView() {

        ImageView logoImageView = (ImageView) activity.findViewById(R.id.statusimage);
        assertNotNull(logoImageView);

    }

    @Test
    public void shouldRenderAgeForClientTextView() {

        TextView textView = (TextView) activity.findViewById(R.id.ageforclient);
        assertNotNull(textView);

    }

    @Test
    public void shouldRenderStatusNameTextView() {

        TextView textView = (TextView) activity.findViewById(R.id.statusname);
        assertNotNull(textView);

    }

    @Test
    public void shouldRenderStatusTextView() {

        TextView textView = (TextView) activity.findViewById(R.id.status);
        assertNotNull(textView);

    }

    @Test
    public void shouldRenderRegistrationDataTabTitle() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Age",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderChildsHomeHealthFacilityRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Child\'s home health facility",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Ignore
    @Test
    public void shouldRenderChildsZeirIdRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Child\'s ZEIR ID",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderChildsRegisterCardNumberRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Child\'s register card number",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderChildBirthCertificateNumberRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Child\'s birth certificate number",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Ignore
    @Test
    public void shouldRenderFirstNameRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "First Name",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Ignore
    @Test
    public void shouldRenderLastNameRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Last Name",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderSexRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Sex",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderChildsDobRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Child's DOB",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Ignore
    @Test
    public void shouldRenderAgeRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Age",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Ignore
    @Test
    public void shouldRenderDateFirstSeenRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Date first seen",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderBirthWeightRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Birth Weight",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderMotherGuardianNameRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Mother/guardian Name",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Ignore
    @Test
    public void shouldRenderMotherGuardianFirstNameRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Mother/guardian first name",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Ignore
    @Test
    public void shouldRenderMotherGuardianLastNameRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Mother/guardian last name",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Ignore
    @Test
    public void shouldRenderMotherGuardianDOBRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Mother/guardian DOB",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Ignore
    @Test
    public void shouldRenderMotherGuardianNRCNumberRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Mother/guardian NRC number",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderMotherGuardianPhoneNumberRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Mother/guardian phone number",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Ignore
    @Test
    public void shouldRenderMotherGuardianFullNameRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Father/guardian full name",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Ignore
    @Test
    public void shouldRenderFatherGuardianNRCnumberRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Father/guardian NRC number",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderPlaceOfBirtRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Place of birth",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderMotherGuardianHealthFacilityRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Health facility the child was born in",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void shouldRenderChildsResidentialAreaRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Child's residential area",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Test
    public void viewpagerSelectedToPositionZero() {
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.viewpager);
        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);
        viewPager.invalidate();
        Assert.assertEquals(viewPager.getCurrentItem(),0);
    }

    @Test
    public void statusViewPerformClickCreatesStatusEditDialogFragment() {
        LinearLayout statusview = (LinearLayout) activity.findViewById(R.id.statusview);
        statusview.performClick();
        Assert.assertEquals(statusview!=null,true);
    }

    @Ignore
    @Test
    public void shouldRenderHomeAddressRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Home Address",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Test
    public void saveButtonOnPoerformClick() {
        ChildDetailsToolbar detailtoolbar = (ChildDetailsToolbar) activity.findViewById(R.id.child_detail_toolbar);
        TextView saveButton = (TextView) detailtoolbar.findViewById(R.id.save);
        saveButton.performClick();
        Assert.assertEquals(saveButton.getVisibility(),View.INVISIBLE);
    }

    @Ignore
    @Test
    public void getChildDetailsMethodShouldNotReturnNull() {

        assertNotNull(activity.getChildDetails());

    }

    @Ignore@Test  
    public void shouldRenderLandmarkRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "Landmark",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Ignore
    @Test
    public void shouldRenderCHWNameRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "CHW name",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Ignore
    @Test
    public void shouldRenderCHWphoneNumber() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "CHW phone number",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }

    @Ignore
    @Test
    public void shouldRenderHIVexposureRow() {
        final ArrayList<View> outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.rowholder).findViewsWithText(outViews, "HIV exposure",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

    }
    @Ignore
    @Test
    public void shouldDisplayOnOptionsMenuCaseRegistrationData() {
        MenuItemTestVersion menuItem = new MenuItemTestVersion();
        menuItem.setItemId(R.id.registration_data);
        activity.onOptionsItemSelected(menuItem);
        ArrayList<View> outViews = new ArrayList<>();

        //Validate correct form view loaded by validating various fields

        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.scrollView2).findViewsWithText(outViews, "Child's home health facility",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

//        outViews = new ArrayList<>();
//        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.scrollView2).findViewsWithText(outViews, "Child's ZEIR ID",
//                View.FIND_VIEWS_WITH_TEXT);
//        assertFalse(outViews.isEmpty());

        outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.scrollView2).findViewsWithText(outViews, "Child's register card number",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());

        outViews = new ArrayList<>();
        activity.getViewPagerAdapter().getItem(0).getView().findViewById(R.id.scrollView2).findViewsWithText(outViews, "Child's birth certificate number",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());


    }
    
    @Test
    public void shouldDisplayOnOptionsMenuCaseImmunizationData() {
        MenuItemTestVersion menuItem = new MenuItemTestVersion();
        menuItem.setItemId(R.id.immunization_data);
        activity.onOptionsItemSelected(menuItem);
        ArrayList<View> outViews = new ArrayList<>();

        //Validate correct form view loaded by validating various fields

        activity.findViewById(R.id.immunizations).findViewsWithText(outViews, "IMMUNIZATIONS",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());


    }
    
    @Test
    public void shouldDisplayOnOptionsMenuCaseWeightData() {
        MenuItemTestVersion menuItem = new MenuItemTestVersion();
        menuItem.setItemId(R.id.weight_data);
        activity.onOptionsItemSelected(menuItem);
        ArrayList<View> outViews = new ArrayList<>();

        //Validate correct form view loaded by validating various fields


//        activity.findViewById(R.id.scrollView).findViewsWithText(outViews, "PMTCT",
//                View.FIND_VIEWS_WITH_TEXT);
//        assertFalse(outViews.isEmpty());

        outViews = new ArrayList<>();
        activity.findViewById(R.id.scrollView).findViewsWithText(outViews, "LAST 5 WEIGHTS",
                View.FIND_VIEWS_WITH_TEXT);
        assertFalse(outViews.isEmpty());
        assertTrue(outViews.get(0).getVisibility() == View.VISIBLE);


    }
    @Ignore
    @Test
    public void shouldDisplayOnOptionsMenuCaseReportDeceased() {
        MenuItemTestVersion menuItem = new MenuItemTestVersion();
        menuItem.setItemId(R.id.report_deceased);
        boolean result = activity.onOptionsItemSelected(menuItem);

        //Testing whether function call returned true
        assertTrue(result);

    }
    
    @Test
    public void shouldDisplayOnOptionsMenuCaseChangeStatus() {
        MenuItemTestVersion menuItem = new MenuItemTestVersion();
        menuItem.setItemId(R.id.change_status);
        activity.onOptionsItemSelected(menuItem);

        boolean result = activity.onOptionsItemSelected(menuItem);

        //Testing whether function call returned true
        assertTrue(result);


    }
    
    @Test
    public void shouldReturnTrueOnOptionsMenuCaseRecurringServices() {
        MenuItemTestVersion menuItem = new MenuItemTestVersion();
        menuItem.setItemId(R.id.recurring_services_data);
        activity.onOptionsItemSelected(menuItem);

        boolean result = activity.onOptionsItemSelected(menuItem);

        //Testing whether function call returned true
        assertTrue(result);

    }

    @Test
    public void getViewPagerAdapterShouldNotReturnNull() {

        assertNotNull(activity.getViewPagerAdapter());

    }

    @Test
    public void getViewPagerAdapterShouldHaveTwoFragments() {

        assertNotNull(activity.getViewPagerAdapter().getItem(0));
        assertNotNull(activity.getViewPagerAdapter().getItem(1));

    }

    @Test
    public void getDetailsRepositoryShouldNotReturnNull() {

        assertNotNull(activity.getDetailsRepository());

    }

    @Test
    public void onReturnSelectItemOptionsShouldReturnTrue() {
        assertTrue(activity.onPrepareOptionsMenu(null));

    }

    public void onBackPressShouldFinishActivity() {
        activity.onBackPressed();
        assertTrue(activity.isFinishing());

    }

    @Test
    public void getVaccinatorApplicationInstanceShouldNotReturnNull() {

        assertNotNull(activity.getVaccinatorApplicationInstance());

    }

    @Test
    public void showWeightDialogShouldRender() {

        activity.showWeightDialog(0);
        assertNotNull(activity.getFragmentManager().findFragmentByTag(ChildDetailTabbedActivity.DIALOG_TAG));

    }
    @Ignore
    @Test
    public void clickingToolBarNavigationButtonClosesTheActivity() {
        ChildDetailsToolbar toolbar = (ChildDetailsToolbar) activity.findViewById(R.id.child_detail_toolbar);
        ArrayList<View> outViews = new ArrayList<>();
        toolbar.findViewsWithText(outViews, "NAVIGATE UP",
                View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);

        assertFalse(outViews.isEmpty());
        assertTrue(outViews.size() == 1); //only one
        outViews.get(0).performClick();
        assertTrue(activity.isFinishing());


    }

    @Ignore
    @Test
    public void shouldRenderStatusFragmentOnStatusViewClick() {

        LinearLayout statusView = (LinearLayout) activity.findViewById(R.id.statusview);
        assertNotNull(statusView);
        statusView.performClick();

        ArrayList<View> outViews = new ArrayList<>();
        View view = activity.getFragmentManager().findFragmentByTag(ChildDetailTabbedActivityTestVersion.DIALOG_TAG).getView();
        assertNotNull(view); //make sure view exists
        view.findViewsWithText(outViews, "Child Status",
                View.FIND_VIEWS_WITH_TEXT);

        assertFalse(outViews.isEmpty());
        assertTrue(outViews.size() == 1); //only one
        assertTrue(outViews.get(0).getVisibility() == View.VISIBLE);
    }

    @Ignore
    @Test
    public void onCreateSetsUpSuccessfullyWithSerializedChildDetails() {


        destroyController(); //destroy controller

        //Recreate and start controller with bundles this time

        Intent intent = new Intent(RuntimeEnvironment.application, ChildDetailTabbedActivityTestVersion.class);
        intent.putExtra("location_name", "Nairobi");
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChildDetailTabbedActivityTestVersion.EXTRA_CHILD_DETAILS, childDetails);
        intent.putExtras(bundle);


        controller = Robolectric.buildActivity(ChildDetailTabbedActivityTestVersion.class, intent);
        activity = controller.get();
        activity.detailsRepository = getDetailsRepository();
        controller.setup();

        //Certify started successfully by checking if at least one random element rendered
        TextView nameView = (TextView) activity.findViewById(R.id.name);
        assertNotNull(nameView);
    }

    @Ignore
    @Test
    public void statusViewShouldUpdateToInactiveIfChildDetailsInactiveParamIsSetToTrue() {

        destroyController(); //destroy controller

        //Recreate and start controller with bundles this time

        Intent intent = new Intent(RuntimeEnvironment.application, ChildDetailTabbedActivityTestVersion.class);
        intent.putExtra("location_name", "Nairobi");
        Bundle bundle = new Bundle();
        CommonPersonObjectClient newChildDetails = new CommonPersonObjectClient("1", new HashMap<String, String>(), "test");
        Map<String, String> columnMap = EasyMap.create(ChildDetailTabbedActivityTestVersion.inactive, "true").map();
        newChildDetails.setColumnmaps(columnMap);
        newChildDetails.setDetails(columnMap);
        details = columnMap; //save for later call to getAllDetailsForClient method
        bundle.putSerializable(ChildDetailTabbedActivityTestVersion.EXTRA_CHILD_DETAILS, newChildDetails);
        intent.putExtras(bundle);


        controller = Robolectric.buildActivity(ChildDetailTabbedActivityTestVersion.class, intent);
        activity = controller.get();

        activity.detailsRepository = getDetailsRepository();
        controller.setup();
        LinearLayout statusView = (LinearLayout) activity.findViewById(R.id.statusview);
        assertNotNull(statusView);

        TextView statusTextView = (TextView) statusView.findViewById(R.id.statusname);
        assertTrue(statusTextView.getVisibility() == View.VISIBLE);
        assertEquals("Inactive", statusTextView.getText().toString());

    }

    @Ignore
    @Test
    public void onBackActivityShouldReturnChildImmunizationActivityClass() {

        assertNotNull(activity.getVaccinatorApplicationInstance());
        assertTrue(activity.onBackActivity() == ChildImmunizationActivity.class);

    }


    @SuppressWarnings("unchecked")
    @Ignore
    @Test
    public void statusViewShouldUpdateToActiveifChildStatusParamListIsEmpty() {

        destroyController(); //destroy controller

        //Recreate and start controller with bundles this time

        Intent intent = new Intent(RuntimeEnvironment.application, ChildDetailTabbedActivityTestVersion.class);
        intent.putExtra("location_name", "Nairobi");
        Bundle bundle = new Bundle();
        CommonPersonObjectClient newChildDetails = new CommonPersonObjectClient("1", new HashMap<String, String>(), "test");
        newChildDetails.setColumnmaps(Collections.EMPTY_MAP);
        newChildDetails.setDetails(Collections.EMPTY_MAP);
        bundle.putSerializable(ChildDetailTabbedActivityTestVersion.EXTRA_CHILD_DETAILS, newChildDetails);
        intent.putExtras(bundle);


        controller = Robolectric.buildActivity(ChildDetailTabbedActivityTestVersion.class, intent);
        activity = controller.get();
        activity.detailsRepository = getDetailsRepository();
        controller.setup();

        LinearLayout statusView = (LinearLayout) activity.findViewById(R.id.statusview);
        assertNotNull(statusView);

        TextView statusTextView = (TextView) statusView.findViewById(R.id.statusname);
        assertTrue(statusTextView.getVisibility() == View.VISIBLE);
        assertEquals("Active", statusTextView.getText().toString());


    }

    @Ignore
    @Test
    public void statusViewShouldUpdateToLostToFollowUpWhenChildStatusLostToFollowUpParamIsTrue() {

        destroyController(); //destroy controller

        //Recreate and start controller with bundles this time

        Intent intent = new Intent(RuntimeEnvironment.application, ChildDetailTabbedActivityTestVersion.class);
        intent.putExtra("location_name", "Nairobi");
        Bundle bundle = new Bundle();
        CommonPersonObjectClient newChildDetails = new CommonPersonObjectClient("1", new HashMap<String, String>(), "test");
        Map<String, String> columnMap = EasyMap.create(ChildDetailTabbedActivityTestVersion.lostToFollowUp, "true").map();
        newChildDetails.setColumnmaps(columnMap);
        newChildDetails.setDetails(columnMap);
        details = columnMap; //save for later call to getAllDetailsForClient method
        bundle.putSerializable(ChildDetailTabbedActivityTestVersion.EXTRA_CHILD_DETAILS, newChildDetails);
        intent.putExtras(bundle);


        controller = Robolectric.buildActivity(ChildDetailTabbedActivityTestVersion.class, intent);
        activity = controller.get();

        activity.detailsRepository = getDetailsRepository();
        controller.setup();
        LinearLayout statusView = (LinearLayout) activity.findViewById(R.id.statusview);
        assertNotNull(statusView);

        TextView statusTextView = (TextView) statusView.findViewById(R.id.status);
        assertTrue(statusTextView.getVisibility() == View.VISIBLE);
        assertEquals("Lost to\nFollow-Up", statusTextView.getText().toString());

    }

    private void destroyController() {
        try {
            activity.finish();
            controller.pause().stop().destroy(); //destroy controller if we can

        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage());
        }

        System.gc();
    }

    @Ignore@Test  
    public void onGiveTodayCallsSaveServiceMethodWithCorrectParameters() throws Exception {
        ChildDetailTabbedActivity spy = PowerMockito.spy(activity);
        spy.onGiveToday(serviceWrapper, view);
        String privateMethodName = "saveService";
        PowerMockito.doNothing().when(spy, method(ChildDetailTabbedActivity.class, privateMethodName, ServiceWrapper.class, View.class)).withArguments(serviceWrapper, view);
        PowerMockito.verifyPrivate(spy, times(1)).invoke(privateMethodName, serviceWrapper, view);
    }

    private DetailsRepository getDetailsRepository() {

        return new DetailsRepositoryLocal();
    }

    class DetailsRepositoryLocal extends DetailsRepository {

        @Override
        public Map<String, String> getAllDetailsForClient(String baseEntityId) {
            return details;
        }
    }

}

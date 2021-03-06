package org.smartregister.growplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.smartregister.growplus.R;
import org.smartregister.growplus.application.VaccinatorApplication;

public class HomeDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_dashboard);
        final Bundle extras = this.getIntent().getExtras();
        LinearLayout household = (LinearLayout)findViewById(R.id.household_dashboard_button);
        household.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeDashboardActivity.this, HouseholdSmartRegisterActivity.class);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        LinearLayout mother = (LinearLayout)findViewById(R.id.woman_dashboard_button);
        mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeDashboardActivity.this, WomanSmartRegisterActivity.class);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        LinearLayout child = (LinearLayout)findViewById(R.id.child_dashboard_button);
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeDashboardActivity.this, ChildSmartRegisterActivity.class);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        LinearLayout report = (LinearLayout)findViewById(R.id.report_dashboard_button);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeDashboardActivity.this, GrowthReportActivity.class);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        TextView initialsTV = (TextView) findViewById(R.id.name_inits);
        String preferredName = VaccinatorApplication.getInstance().context().allSharedPreferences().getANMPreferredName(
                VaccinatorApplication.getInstance().context().allSharedPreferences().fetchRegisteredANM());
        if (!TextUtils.isEmpty(preferredName)) {
            String[] initialsArray = preferredName.split(" ");
            String initials = "";
            if (initialsArray.length > 0) {
                initials = initialsArray[0].substring(0, 1);
                if (initialsArray.length > 1) {
                    initials = initials + initialsArray[1].substring(0, 1);
                }
            }

            initialsTV.setText(initials.toUpperCase());
        }

        TextView nameTV = (TextView) findViewById(R.id.provider_name);
        nameTV.setText(capitalize(preferredName));

    }

    public static String capitalize(@NonNull String input) {

        String[] words = input.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (i > 0 && word.length() > 0) {
                builder.append(" ");
            }

            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
        }
        return builder.toString();
    }

}

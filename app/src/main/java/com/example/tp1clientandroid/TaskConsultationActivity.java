package com.example.tp1clientandroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tp1clientandroid.databinding.ActivityMainBinding;
import com.example.tp1clientandroid.databinding.ActivityTaskConsultationBinding;
import com.example.tp1clientandroid.http.AppService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;

import java.util.Date;

public class TaskConsultationActivity extends AppCompatActivity {
    private ActivityTaskConsultationBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Slider slider;
    private TextView taskNameTV;
    private TextView completionPercentageTV;
    private TextView timeElapsedPercentageTV;
    private TextView deadlineDateTV;
    private Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.task_consultation_activity_title);

        // Recherche des éléments de la vue
        NavigationView nv = binding.navView;
        DrawerLayout dLayout = binding.drawerLayout;
        slider = binding.taskNameConsultationSlider;
        taskNameTV = binding.taskNameConsultation;
        completionPercentageTV = binding.completionPercentageConsultation;
        timeElapsedPercentageTV = binding.timeElapsedPercentageConsultation;
        deadlineDateTV = binding.deadlineDateConsultation;
        buttonSave = binding.buttonSave;


        // Affichage de l'icône de menu et interaction
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(TaskConsultationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Changement automatique de l'icône de menu et du titre avec l'action du tiroir
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dLayout, R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }

            @Override
            public void onDrawerClosed(View drawerView) { super.onDrawerClosed(drawerView); }

        };

        // Tirroir
        dLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;

                if (item.getItemId() == R.id.nav_home) {
                    i =  new Intent(TaskConsultationActivity.this, MainActivity.class);
                    startActivity(i);
                    return true;

                } else if (item.getItemId() == R.id.nav_add_task) {
                    i = new Intent(TaskConsultationActivity.this, TaskCreationActivity.class);
                    startActivity(i);
                    return true;

                } else if (item.getItemId() == R.id.nav_logout) {
                    AppService.signout(TaskConsultationActivity.this);
                    return true;
                }
                return false;
            }
        });

        // Écouteur de changement de valeur pour le slider
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                String newCompletionPercentage = String.valueOf((int) value) + "%";
                completionPercentageTV.setText(newCompletionPercentage);
            }
        });

        // Set éléments du Layout
        slider.setValue(Float.parseFloat(getIntent().getStringExtra("selectedTaskCompletionPercentage")));
        taskNameTV.setText(getIntent().getStringExtra("selectedTaskName"));
        completionPercentageTV.setText(getIntent().getStringExtra("selectedTaskCompletionPercentage")+ "%");
        timeElapsedPercentageTV.setText(getIntent().getStringExtra("selectedTaskTimeElapsedPercentage") + "%");
        deadlineDateTV.setText(getIntent().getStringExtra("selectedTaskDeadlineDate"));

    }

    // Interaction avec l'icône de menu lorsque l'utilisateur appuie dessus
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Conservation de l'état de l'icône de menu lorsqu'il y a un changement d'orientation
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    // Conservation de l'état de l'icône de menu lorsqu'il y a un changement d'orientation
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
}

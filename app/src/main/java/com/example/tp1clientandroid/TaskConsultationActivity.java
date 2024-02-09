package com.example.tp1clientandroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tp1clientandroid.databinding.ActivityMainBinding;
import com.example.tp1clientandroid.databinding.ActivityTaskConsultationBinding;
import com.google.android.material.navigation.NavigationView;

public class TaskConsultationActivity extends AppCompatActivity {
    private ActivityTaskConsultationBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;

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

        // Affichage de l'icône de menu et interaction
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Changement automatique de l'icône de menu et du titre avec l'action du tiroir
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dLayout, R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.drawer_open);
                Toast.makeText(TaskConsultationActivity.this, "Ouvert", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(R.string.drawer_close);
                Toast.makeText(TaskConsultationActivity.this, "Fermé", Toast.LENGTH_SHORT).show();
            }

        };

        dLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;

                if (item.getItemId() == R.id.nav_home) {
                    i =  new Intent(TaskConsultationActivity.this, MainActivity.class);
                    startActivity(i);
                    i.putExtra("id", -2);
                    return true;


                } else if (item.getItemId() == R.id.nav_add_task) {
                    i = new Intent(TaskConsultationActivity.this, TaskCreationActivity.class);
                    startActivity(i);
                    return true;

                } else if (item.getItemId() == R.id.nav_logout) {
                    Toast.makeText(TaskConsultationActivity.this, R.string.logout, Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "Utilisateur déconnecté");
                    i =  new Intent(TaskConsultationActivity.this, ConnexionActivity.class);
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });
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

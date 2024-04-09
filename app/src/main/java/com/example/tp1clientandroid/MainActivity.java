package com.example.tp1clientandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tp1clientandroid.databinding.ActivityMainBinding;
import com.example.tp1clientandroid.http.AppService;
import com.example.tp1clientandroid.http.RetrofitUtil;
import com.example.tp1clientandroid.http.Service;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.HomeItemResponse;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TaskAdapter taskAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton buttonFAB;
    private TextView navHeaderUsernameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.main_activity_title);

        // initialisation du recycler
        this.initRecycler();
        this.fillRecycler();

        // Recherche des éléments de la vue
        NavigationView nv = binding.navView;
        DrawerLayout dLayout = binding.drawerLayout;
        buttonFAB = binding.fab;
        // BUG: L'objet binding peut être null avant son initialisation, comme ici :
        // navHeaderUsernameTV = findViewById(R.id.nav_header_usernameTV);
        // SOLUTION: Accédez à nav_header_usernameTV depuis la vue NavigationView :
        // navHeaderUsernameTV = binding.navView.getHeaderView(0).findViewById(R.id.nav_header_usernameTV);
        navHeaderUsernameTV = nv.getHeaderView(0).findViewById(R.id.nav_header_usernameTV);





        // Affichage de l'icône de menu et interaction
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskCreationActivity.class);
                startActivity(intent);
            }
        });

        // Changement automatique de l'icône de menu et du titre avec l'action du tiroir
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dLayout, R.string.drawer_open,R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };

        // Tirroir
        dLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navHeaderUsernameTV.setText(UserManager.getInstance().getUsername());
        
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;

                if (item.getItemId() == R.id.nav_home) {
                    i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                    return true;

                } else if (item.getItemId() == R.id.nav_add_task) {
                    i = new Intent(MainActivity.this, TaskCreationActivity.class);
                    startActivity(i);
                    return true;

                } else if (item.getItemId() == R.id.nav_logout) {
                    AppService.signout(MainActivity.this);
                    return true;
                }
                return false;
            }
        });
    }

    // Interaction avec l'icône de menu lorsque l'utilisateur appuie dessus
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
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

    // RecyclerView
    private void initRecycler() {
        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        taskAdapter = new TaskAdapter();
        recyclerView.setAdapter(taskAdapter);
    }
    private void fillRecycler() {
        final Service service = RetrofitUtil.get();
        service.home().enqueue(new Callback<List<HomeItemResponse>>() {
            @Override
            public void onResponse(Call<List<HomeItemResponse>> call, Response<List<HomeItemResponse>> response) {
                if (!response.isSuccessful()){
                    Log.i("RETROFIT", response.code()+" service.home() onResponse 400");
                }else{
                    List<HomeItemResponse> resultat = response.body();
                    Log.i("RETROFIT", resultat.size()+" service.home() onResponse response.isSuccessful()");

                    for (HomeItemResponse item : resultat){
                        Long id = item.id;
                        String taskName = item.name;
                        int completionPercentage = item.percentageDone;
                        Date deadlineDate = item.deadline;
                        double percentageTimeSpent = item.percentageTimeSpent;
                        taskAdapter.taskList.add(new Task(id, taskName, completionPercentage, percentageTimeSpent, deadlineDate));
                    }
                    taskAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<HomeItemResponse>> call, Throwable t) {
                Log.i("RETROFIT", t.getMessage() + " onFailure");
            }
        });
    }


}
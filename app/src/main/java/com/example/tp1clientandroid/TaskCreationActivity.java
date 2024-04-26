package com.example.tp1clientandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tp1clientandroid.databinding.ActivityTaskCreationBinding;
import com.example.tp1clientandroid.http.AppService;
import com.example.tp1clientandroid.http.RetrofitUtil;
import com.example.tp1clientandroid.http.Service;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.kickmyb.transfer.AddTaskRequest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskCreationActivity extends AppCompatActivity {
    private ActivityTaskCreationBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Button buttonAddTask;
    private EditText taskName;
    private TextView taskDeadline;
    private DatePicker datePicker;
    private TextView navHeaderUsernameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskCreationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.task_creation_activity_title);

        // Recherche des éléments de la vue
        NavigationView nv = binding.navView;
        DrawerLayout dLayout = binding.drawerLayout;
        buttonAddTask = binding.buttonAddTask;
        taskName = binding.editTextTaskName;
        taskDeadline = binding.deadLineTV;
        datePicker = binding.datePicker;
        navHeaderUsernameTV = nv.getHeaderView(0).findViewById(R.id.nav_header_usernameTV);

        LocalDateTime dt = LocalDateTime.now();
        datePicker.init(dt.getYear(), dt.getMonth().getValue(), dt.getDayOfMonth(), (view1, year, monthOfYear, dayOfMonth) -> {
            taskDeadline.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
        });

        // Affichage de l'icône de menu et interaction
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer la date sélectionnée du DatePicker
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();

                // Créer un objet Calendar avec la date sélectionnée
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                // Convertir le Calendar en Date
                Date selectedDate = calendar.getTime();

                // Formater la date dans le format requis
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String formattedDate = sdf.format(selectedDate);

                try {
                    // Convertir la chaîne de date en objet Date
                    Date deadlineDate = sdf.parse(formattedDate);

                    // Retrofit: AddTaskRequest
                    AddTaskRequest addTaskRequest = new AddTaskRequest();
                    addTaskRequest.name = taskName.getText().toString();
                    addTaskRequest.deadline = deadlineDate;

                    // Retrofit: service Retrofit pour initaliser la connection
                    final Service service = RetrofitUtil.get();
                    service.addOne(addTaskRequest).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (!response.isSuccessful()){
                                // ERROR ERROR ERROR
                                try {
                                    String corpsErreur = response.errorBody().string();
                                    Log.i("RETROFIT", "le code " + response.code());
                                    Log.i("RETROFIT", "le message " + response.message());
                                    Log.i("RETROFIT", "le corps " + corpsErreur);
                                    Log.i("RETROFIT", "le corps encore " + response.errorBody().string());
                                    if (corpsErreur.contains("Existing")) {
                                        Snackbar.make(binding.getRoot(), R.string.task_name_exist, Snackbar.LENGTH_SHORT).show();
                                    }
                                    if (corpsErreur.contains("Empty")) {
                                        Snackbar.make(binding.getRoot(), R.string.task_name_empty, Snackbar.LENGTH_SHORT).show();
                                    }
                                    if (corpsErreur.contains("TooShort")) {
                                        Snackbar.make(binding.getRoot(), R.string.task_name_too_short, Snackbar.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Log.i("RETROFIT", String.valueOf(R.string.task_added));
                                Toast.makeText(TaskCreationActivity.this, R.string.task_added, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TaskCreationActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            // Code 500: Erreur de connection serveur
                            Log.i("RETROFIT", t.getMessage() + " service.addOne(addTaskRequest) onFailure");
                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskCreationActivity.this);
                            builder.setTitle(R.string.error_dialog_title)
                                    .setMessage(R.string.error_network)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Fermer le dialogue
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(R.drawable.error_icon)
                                    .show();
                        }
                    });




                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // Changement automatique de l'icône de menu
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dLayout, R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }

            @Override
            public void onDrawerClosed(View drawerView) { super.onDrawerClosed(drawerView); }

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
                    i =  new Intent(TaskCreationActivity.this, MainActivity.class);
                    startActivity(i);
                    return true;

                } else if (item.getItemId() == R.id.nav_add_task) {
                    i = new Intent(TaskCreationActivity.this, TaskCreationActivity.class);
                    startActivity(i);
                    return true;

                } else if (item.getItemId() == R.id.nav_logout) {
                    AppService.signout(TaskCreationActivity.this);
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

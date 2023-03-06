package com.codegama.todolistapplication.activity;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codegama.todolistapplication.R;
import com.codegama.todolistapplication.adapter.TaskAdapter;
import com.codegama.todolistapplication.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.codegama.todolistapplication.bottomSheetFragment.ShowCalendarViewBottomSheet;
import com.codegama.todolistapplication.broadcastReceiver.AlarmBroadcastReceiver;
import com.codegama.todolistapplication.database.DatabaseClient;
import com.codegama.todolistapplication.model.Task;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements CreateTaskBottomSheetFragment.setRefreshListener {

    @BindView(R.id.taskRecycler)
    RecyclerView taskRecycler;
    @BindView(R.id.addTask)
    TextView addTask;
    TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();
    @BindView(R.id.noDataImage)
    ImageView noDataImage;
    @BindView(R.id.calendar)
    ImageView calendar;


    ImageButton croButtonn;

    ImageButton engButtonn;

     Context context;

    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpAdapter();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        Glide.with(getApplicationContext()).load(R.drawable.first_note).into(noDataImage);

        addTask.setOnClickListener(view -> {
            CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
            createTaskBottomSheetFragment.setTaskId(0, false, this, MainActivity.this);
            createTaskBottomSheetFragment.show(getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
        });

        getSavedTasks();

        calendar.setOnClickListener(view -> {
            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());
        });


        croButtonn = findViewById(R.id.croButton);
        engButtonn = findViewById(R.id.engButton);

        LocaleHelper lang=new LocaleHelper(this);

         croButtonn.setOnClickListener(view -> {
             lang.updateResource("hr");
             recreate();

         });

        engButtonn.setOnClickListener(view -> {
            lang.updateResource("en");
            recreate();

        });
        }





    public void setUpAdapter() {
        taskAdapter = new TaskAdapter(this, tasks, this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskRecycler.setAdapter(taskAdapter);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    @Override
    public void refresh() {
        getSavedTasks();
    }


    }

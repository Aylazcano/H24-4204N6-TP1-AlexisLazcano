package com.example.tp1clientandroid;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Task {
    public String taskName;
    public int completionPercentage;
    public int timeElapsedPercentage;
    public Date creationDate;
    public Date deadlineDate;

    public Task(String pTaskName, int pCompletionPercentage, int pTimeElapsedPercentage, Date pCreationDate, Date pDeadlineDate){
        taskName = pTaskName;
        completionPercentage = pCompletionPercentage;
        timeElapsedPercentage = pTimeElapsedPercentage;
        creationDate = pCreationDate;
        deadlineDate = pDeadlineDate;
    }
}

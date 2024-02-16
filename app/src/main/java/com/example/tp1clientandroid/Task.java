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

    public Task(String pTaskName, int pCompletionPercentage, Date pCreationDate, Date pDeadlineDate){
        taskName = pTaskName;
        completionPercentage = pCompletionPercentage;
        creationDate = pCreationDate;
        deadlineDate = pDeadlineDate;

        long elapsedTime = System.currentTimeMillis() - creationDate.getTime();
        long totalTime = deadlineDate.getTime() - creationDate.getTime();
        timeElapsedPercentage = (int) ((elapsedTime * 100) / totalTime);
    }
}

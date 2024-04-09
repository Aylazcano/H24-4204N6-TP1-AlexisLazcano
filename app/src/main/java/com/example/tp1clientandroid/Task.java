package com.example.tp1clientandroid;

import java.util.Date;

public class Task {
    public Long id;
    public String taskName;
    public int percentageDone;
    public double percentageTimeSpent;
    public Date creationDate;
    public Date deadlineDate;

    public Task(Long pId, String pTaskName, int pPercentageDone, double pPercentageTimeSpent, Date pDeadlineDate){
        id = pId;
        taskName = pTaskName;
        percentageDone = pPercentageDone;
        percentageTimeSpent = pPercentageTimeSpent;
        deadlineDate = pDeadlineDate;
        creationDate = null;
    }

}

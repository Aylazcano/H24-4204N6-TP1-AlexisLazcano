package com.example.tp1clientandroid;

import java.util.Date;

public class Task {
    public Long id;
    public String taskName;
    public int percentageDone;
    public double percentageTimeSpent;
    public Date creationDate;
    public Date deadlineDate;

    public Task(String pTaskName, int pCompletionPercentage, Date pCreationDate, Date pDeadlineDate){
        taskName = pTaskName;
        percentageDone = pCompletionPercentage;
        creationDate = pCreationDate;
        deadlineDate = pDeadlineDate;
        long elapsedTime = System.currentTimeMillis() - creationDate.getTime();
        long totalTime = deadlineDate.getTime() - creationDate.getTime();
        percentageTimeSpent = (int) ((elapsedTime * 100) / totalTime);
    }
    public Task(Long pId, String pTaskName, int pPercentageDone, double pPercentageTimeSpent, Date pDeadlineDate){
        id = pId;
        taskName = pTaskName;
        percentageDone = pPercentageDone;
        percentageTimeSpent = pPercentageTimeSpent;
        deadlineDate = pDeadlineDate;
        creationDate = null;
    }

}

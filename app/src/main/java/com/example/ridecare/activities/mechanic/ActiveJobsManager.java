package com.example.ridecare.activities.mechanic;

import java.util.ArrayList;
import java.util.List;


public class ActiveJobsManager {
    private static ActiveJobsManager instance;
    private List<CarJob> activeJobs;

    private ActiveJobsManager() {
        activeJobs = new ArrayList<>();
    }

    public static synchronized ActiveJobsManager getInstance() {
        if (instance == null) {
            instance = new ActiveJobsManager();
        }
        return instance;
    }

    public void addJob(CarJob job) {
        if (!activeJobs.contains(job)) {
            activeJobs.add(job);
        }
    }

    public void removeJob(CarJob job) {
        activeJobs.remove(job);
    }

    public List<CarJob> getActiveJobs() {
        return new ArrayList<>(activeJobs);
    }

    public void clearAll() {
        activeJobs.clear();
    }
}
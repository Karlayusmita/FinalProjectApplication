package com.example.finalprojectapplication.data.service;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import static com.example.finalprojectapplication.Contract.AUTHORITY;
import static com.example.finalprojectapplication.Contract.CONTENT;
import static com.example.finalprojectapplication.Contract.TASKS;

public class CleanupJobService extends JobService {
    public static final Uri URI = new Uri.Builder().scheme(CONTENT)
            .authority(AUTHORITY)
            .appendPath(TASKS)
            .build();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new Cleanup().execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private class Cleanup extends AsyncTask<JobParameters, Void, JobParameters> {
        @Override
        protected JobParameters doInBackground(JobParameters... jobParameters) {
            String status = String.format("%s", "is_complete");
            String[] args = {"1"};

            int count = getContentResolver().delete(URI, status, args);
            Log.d("Cleanup", count + "completed tasks");
            return jobParameters[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobFinished(jobParameters, false);
        }
    }
}

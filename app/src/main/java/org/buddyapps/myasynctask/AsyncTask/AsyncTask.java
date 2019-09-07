package org.buddyapps.myasynctask.AsyncTask;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

public abstract class AsyncTask<Param, Result> {
    @MainThread
    protected void onPreExecute() {
    }

    @WorkerThread
    protected abstract Result doInBackground(@Nullable Param param);

    @MainThread
    protected void onPostExecute(@Nullable Result result) {
    }

    public final void execute(final Param param) {
        onPreExecute();
        //Run the doInBackground on background thread
        ExecutorProvider.getInstance().getBackgroundExecutor().submit(new Runnable() {
            @Override
            public void run() {
                final Result result = doInBackground(param);
                //Run the onPostExecute on Main thread
                ExecutorProvider.getInstance().getMainExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(result);
                    }
                });
            }
        });
    }

}

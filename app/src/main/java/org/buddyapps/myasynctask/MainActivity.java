package org.buddyapps.myasynctask;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.buddyapps.myasynctask.AsyncTask.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());
        new TestAsyncTask(new WeakReference<>(textView)).execute("https://www.google.com/");
    }

    //My implementation to test the self defined Async Task
    private static class TestAsyncTask extends AsyncTask<String, String> {
        WeakReference<TextView> textViewWeakReference;

        TestAsyncTask(WeakReference<TextView> textViewWeakReference) {
            this.textViewWeakReference = textViewWeakReference;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (textViewWeakReference.get() != null) {
                textViewWeakReference.get().setText("Loading...");
            }
        }

        @Override
        protected String doInBackground(@Nullable String s) {
            String result = null;
            try {
                URL url = new URL(s);
                URLConnection connection = url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                result = br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(@Nullable String s) {
            super.onPostExecute(s);
            if (s == null) {
                s = "Empty String";
            }
            if (textViewWeakReference.get() != null) {
                textViewWeakReference.get().setText(s);
            }
        }
    }
}

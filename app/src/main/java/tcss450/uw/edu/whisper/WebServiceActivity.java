package tcss450.uw.edu.whisper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tcss450.uw.edu.whisper.file.AudioFile;
import tcss450.uw.edu.whisper.signin.SignInActivity;


/**
 * Activity used as base for all fragments using the web service.  Used to retrieve the audio files
 * and to store login and registration data.
 * @author Winfield Brooks
 * @author Jacob Tillet
 */
public class WebServiceActivity extends AppCompatActivity implements FileFragment.OnListFragmentInteractionListener,
        FileFragment.DeleteFileInteractionListener {

    private static final String DELETE_FILE_URL
            = "http://cssgate.insttech.washington.edu/~_450team4/deleteFile.php?";

    /**
     * Set up connection to File Fragment and web service activity layout.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            FileFragment fileFragment = new FileFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fileFragment)
                    .commit();
        }

    }

    /**
     * Opens Listen Fragment for selected file.
     * @param item
     */
    @Override
    public void onListFragmentInteraction(AudioFile item) {
        Log.i("CA onListFragment", item.toString());
        ListenFragment listenFragment = new ListenFragment();
        Bundle args = new Bundle();
        args.putSerializable(FileFragment.FILE_ITEM_SELECTED, item);
        listenFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, listenFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Opens Listen Fragment for selected file.
     * @param item
     */
    @Override
    public void onDeleteInteraction(AudioFile item) {

        String user = SignInActivity.getUserName();
        Log.i("onDelete user:", user);
        String url = DELETE_FILE_URL + "fileName=" + item.getFileName() + "&user=" + user;
        Log.i("delete url", url);
        DeleteFileTask task = new DeleteFileTask();
        task.execute(new String[]{url});
        recreate();
    }

    /**
     * Route to recording activity view.
     * @param view
     */
    public void toRecordActivity(View view) {
        Intent intent = new Intent(this, AudioActivity.class);
        startActivity(intent);
    }
    /**
     * Route to speech to text activity view.
     * @param view
     */
    public void toTextActivity(View view) {
        Intent intent = new Intent(this, SpeechNote.class);
        startActivity(intent);
    }

    /**
     * this method inflates the menu bar
     * @param menu the menu being passed in
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.webservice_logout_menu, menu);
        return true;
    }

    /**
     * logs the user out
     * @param item the logout option
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();

            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();

        }
        return true;
    }

    /**
     * async task for using web services for uploading file info
     */
    private class DeleteFileTask extends AsyncTask<String, Void, String> {


        /**
         * calls super
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * tries to add user
         *
         * @param urls the url to upload file
         * @return the response for trying to upload file
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to upload file, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "File successfully deleted!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to delete: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data " +
                        e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("JSON exception", e.getMessage());
            }
        }
    }

}

package tcss450.uw.edu.whisper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import tcss450.uw.edu.whisper.file.AudioFile;
import tcss450.uw.edu.whisper.signin.SignInActivity;


/**
 * Activity used as base for all fragments using the web service.  Used to retrieve the audio files
 * and to store login and registration data.
 * @author Winfield Brooks
 * @author Jacob Tillet
 */
public class WebServiceActivity extends AppCompatActivity implements FileFragment.OnListFragmentInteractionListener {

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

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (getSupportFragmentManager().findFragmentById(R.id.list) == null) {
//            FileFragment fileFragment = new FileFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, fileFragment)
//                    .commit();
//        }
//    }

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
     * Route to recording activity view.
     * @param view
     */
    public void toRecordActivity(View view) {
        Intent intent = new Intent(this, AudioActivity.class);
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

}

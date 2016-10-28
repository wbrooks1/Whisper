package tcss450.uw.edu.whisper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import tcss450.uw.edu.whisper.file.AudioFile;

public class WebServiceActivity extends AppCompatActivity implements FileFragment.OnListFragmentInteractionListener {

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

    @Override
    public void onListFragmentInteraction(AudioFile item) {

    }
}

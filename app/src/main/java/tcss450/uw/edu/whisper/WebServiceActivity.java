package tcss450.uw.edu.whisper;

<<<<<<< HEAD
import android.content.Intent;
=======
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
>>>>>>> a8d7778e028d4b37ffb06699d5741792d6ec80fc
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;

import tcss450.uw.edu.whisper.file.AudioFile;

public class WebServiceActivity extends AppCompatActivity implements FileFragment.OnListFragmentInteractionListener {
    private static final String LOG_TAG = "AudioRecord";
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);
//        ll.addView(mRecordButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
        mPlayButton = new PlayButton(this);
//        ll.addView(mPlayButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
//        setContentView(ll);
        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            FileFragment fileFragment = new FileFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fileFragment)
                    .commit();
        }


    }

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

    public void toRecordActivity(View view) {
        Intent intent = new Intent(this, AudioAcivity.class);
        startActivity(intent);
    }

<<<<<<< HEAD

=======
    private class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }
    private void onRecord(boolean start){
        if (start){
            startRecording();
        } else {
            stopRecording();
        }
    }
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }
    private void onPlay(boolean start){
        if(start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }
    private class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };
        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
>>>>>>> a8d7778e028d4b37ffb06699d5741792d6ec80fc
}

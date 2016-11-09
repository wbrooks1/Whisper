    package tcss450.uw.edu.whisper;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.IOException;

    /**
     * @author William Almond
     * Modified by Winfield Brooks
     * Class controls recording and playing audio.
     * Code largly similar to Android APK example.
     */
    public class AudioActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private PlayButton mPlayButton = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer;

        /**
         * Switch that calls start and stop recording.
         * @param start boolean true indicating start, false indicating stop.
         */
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

        /**
         * Sets up the audio source, output format and output and starts the recording.
         */
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

        /**
         * stops the recording, releases memory and dereferences the recorder.
         */
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

        /**
         * @author William Almond
         * Creates custom Record button with a listener.
         */
    class RecordButton extends Button {
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

            /**
             * Class contructor for RecordButton
             * @param ctx Context is the parent of Activity and View.
             */
        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }
        /**
         * @author William Almond
         * Creates custom Play button with a listener.
         */
    class PlayButton extends Button{
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
            /**
             * Class contructor for PlayButton
             * @param ctx Context is the parent of Activity and View.
             */
        public PlayButton(Context ctx){
            super(ctx);
            setText("Preview Audio");
            setOnClickListener(clicker);

        }
    }

        /**
         * Switch that calls start and stop playing.
         * @param start boolean true indicating start, false indicating stop.
         */
    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
        /**
         * Sets up the audio output, gets the file name and starts the playing.
         */
    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            Log.e(LOG_TAG, mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

        /**
         * Releases the file being played and dereferences the player.
         */
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

        /**
         * Constructor for the AudioActivity class.
         */
    public AudioActivity() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

        /**
         * Creates the layout and buttons being drawn on the device screen.
         * @param icicle Bundle.
         */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);
    }

        /**
         * Controls actions in the event the device is paused.
         */
    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

        /**
         * Gets the file name and returns a string of the name.
         * @return mFileName that is the file name in string format.
         */
    public String getmFileName(){
        return mFileName;
    }
}

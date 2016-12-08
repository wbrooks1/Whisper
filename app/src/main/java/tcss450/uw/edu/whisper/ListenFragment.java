package tcss450.uw.edu.whisper;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import tcss450.uw.edu.whisper.file.AudioFile;
import tcss450.uw.edu.whisper.signin.SignInActivity;


/**
 * @author Winfield Brooks
 * Fragment to play back selected audio file from the list of previously saved audio files.
 */
public class ListenFragment extends Fragment implements View.OnClickListener {

    private static final String URL = "http://cssgate.insttech.washington.edu/~_450team4/uploads/";
    private String mFileName;
    private String mFilePath;
    private TextView mFileNameTextView;
    private TextView mAudioDurationTextView;
    public final static String FILE_ITEM_SELECTED = "file_selected";
    private MediaPlayer mMediaPlayer;
    private FragmentManager mFragmentManager;



    public ListenFragment() {
        // Required empty public constructor
    }

    /**
     * Set up media player and connect to web service to retrieve audio file.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return created view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listen, container, false);
        mFileNameTextView = (TextView) view.findViewById(R.id.fileName);
        mAudioDurationTextView = (TextView) view.findViewById(R.id.audioDuration);
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            Log.i("ListenFragment", "args not null" + args.toString());
            mFileName = getFileName((AudioFile) args.getSerializable(FILE_ITEM_SELECTED));
            mFilePath =  getContent((AudioFile) args.getSerializable(FILE_ITEM_SELECTED));

        }
        Log.i("LF onCreateView", mFilePath);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mFilePath);
            mMediaPlayer.prepareAsync();


        } catch (IOException e) {
            Toast.makeText(getContext(), "file not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            /**
             * Async prepare for media player.
             * @param player
             */
            @Override
            public void onPrepared(MediaPlayer player) {
                int duration = player.getDuration();
                String durationString = String.format("%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                );
                mAudioDurationTextView.setText(durationString);

                player.start();
            }

        });
        mFileNameTextView.setText(mFileName);
        ImageButton playButton = (ImageButton) view.findViewById(R.id.media_play);
        ImageButton pauseButton = (ImageButton) view.findViewById(R.id.media_pause);
        ImageButton shareButton = (ImageButton) view.findViewById(R.id.media_share);
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);


        return view;
    }

    /**
     * Retrieve file name from audio file.
     * @param file audio file
     * @return name
     */
    public String getFileName(AudioFile file) {
        Log.i("LF getFileName", file.getFileName());
        if (file != null) {
            return file.getFileName();
        } else {
            return  null;
        }
    }

    /**
     * Retrieve file name from audio file.
     * @param file audio file
     * @return name
     */
    public String getContent(AudioFile file) {
        Log.i("LF getContent", file.getContent());
        if (file != null) {
            return file.getContent();
        } else {
            return  null;
        }
    }


    /**
     * On click actions for play and pause button.
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.media_play:
                mMediaPlayer.start();
                break;
            case R.id.media_pause:
                mMediaPlayer.pause();
                break;
            case R.id.media_share:
                shareFile();
                break;
        }
    }

    /**
     * Stop and release media player when not active screen.
     */
    @Override
    public void onPause() {
        super.onPause();
        mMediaPlayer.pause();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    /**
     * Shares url for audio file.
     */
    public void shareFile() {
        Log.i("Share uri", mFilePath);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mFilePath);
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
        mFragmentManager = getFragmentManager();
        mFragmentManager.popBackStack();
    }
}

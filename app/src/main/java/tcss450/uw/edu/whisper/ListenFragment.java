package tcss450.uw.edu.whisper;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
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
    private SeekBar mSeek;
    private TextView mTextView;



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
        mSeek = (SeekBar)view.findViewById(R.id.mSeek);
        mTextView = (TextView)view.findViewById(R.id.mTextView);
        mFileNameTextView = (TextView) view.findViewById(R.id.fileName);
        mAudioDurationTextView = (TextView) view.findViewById(R.id.audioDuration);
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            Log.i("ListenFragment", "args not null" + args.toString());
            mFileName = getFileName((AudioFile) args.getSerializable(FILE_ITEM_SELECTED));
        }

        String user = SignInActivity.getUserName();
        mFilePath = URL + mFileName + user + ".3gp";
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
        mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                seekBar.setMax(mMediaPlayer.getDuration() / 1000);
                mTextView.setText(progress + "/" + seekBar.getMax());

                if(fromUser)
                {
                    mMediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
    //TODO: make work or delete
    public void shareFile() {
        Uri uri = Uri.parse(mFilePath);
        Log.i("Share uri", uri.toString());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("audio/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        shareIntent.setType("audio/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
    }
}

package tcss450.uw.edu.whisper;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import tcss450.uw.edu.whisper.file.AudioFile;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListenFragment extends Fragment implements View.OnClickListener {

    private static final String URL = "http://cssgate.insttech.washington.edu/~_450team4/";
    private String mFileName;
    private TextView mFileNameTextView;
    private TextView mAudioDurationTextView;
    public final static String FILE_ITEM_SELECTED = "file_selected";
    private MediaPlayer mMediaPlayer;



    public ListenFragment() {
        // Required empty public constructor

    }


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
        }
        String url = URL + mFileName + ".3gp";
        Log.i("LF onCreateView", url);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();


        } catch (IOException e) {
            Toast.makeText(getContext(), "mp3 not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //mp3 will be started after completion of preparing...
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                int duration = player.getDuration();
                String durationString = String.format("%d:%d",
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
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);


        return view;
    }

    public String getFileName(AudioFile file) {
        Log.i("LF getFileName", file.getFileName());
        if (file != null) {
            return file.getFileName();
        } else {
            return  null;
        }
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.media_play:
                mMediaPlayer.start();
                break;
            case R.id.media_pause:
                mMediaPlayer.pause();
                break;
        }
    }


}

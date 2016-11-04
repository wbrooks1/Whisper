package tcss450.uw.edu.whisper;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import tcss450.uw.edu.whisper.file.AudioFile;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListenFragment extends Fragment implements View.OnClickListener {

    private static final String URL = "http://cssgate.insttech.washington.edu/~_450team4/";
    private String mFileName;
    public final static String FILE_ITEM_SELECTED = "file_selected";
    private MediaPlayer mMediaPlayer;


    public ListenFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

        } catch (IOException e) {
            Toast.makeText(getContext(), "mp3 not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //mp3 will be started after completion of preparing...
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }

        });

        View view = inflater.inflate(R.layout.fragment_listen, container, false);

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

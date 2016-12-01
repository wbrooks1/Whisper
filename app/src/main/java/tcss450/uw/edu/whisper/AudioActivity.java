package tcss450.uw.edu.whisper;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.text.InputType;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

/**
 * @author William Almond
 * Modified by Winfield Brooks
 * Class controls recording and playing audio.
 * Code largly similar to Android APK example.
 */
public class AudioActivity extends AppCompatActivity {

    private final static String FILE_UPLOAD_URL
            = "http://cssgate.insttech.washington.edu/~_450team4/saveFile.php?";

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFilePath = null;
    private String mFileName = null;
    private String mFileDescription = null;


    private RecordButton mRecordButton = null;
    private PlayButton mPlayButton = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer;


    /**
     * Switch that calls start and stop recording.
     *
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
        mRecorder.setOutputFile(mFilePath);
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
        fileInfoPrompt();
//        byte[] soundBytes;
//
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(Uri.fromFile(new File(mFilePath)));
//            soundBytes = new byte[inputStream.available()];
//
//            soundBytes = toByteArray(inputStream);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        uploadFile();

    }

    /**
     * @author Winfield Brooks
     * Prompts user for file name and description after recording has been finished.
     */
    public void fileInfoPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File Name and Description");

        Context context = builder.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText fileNameInput = new EditText(context);
        fileNameInput.setHint("Title");
        layout.addView(fileNameInput);

        final EditText fileDescriptionInput = new EditText(context);
        fileDescriptionInput.setHint("Description (optional))");
        layout.addView(fileDescriptionInput);

        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (fileNameInput.getText() == null) {
                    mFilePath.replace("audiorecordtest.3gp", "untitiled.3gp");
                    mFileName = "untitled";
                } else {
                    mFilePath.replace("audiorecordtest.3gp", fileNameInput.getText().toString() + ".3gp");
                    mFileName = fileNameInput.getText().toString();
                }
                mFileDescription = fileDescriptionInput.getText().toString();
                upladAudioFile(buildFileUploadURL());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    /**
     * @author William Almond
     *         Creates custom Record button with a listener.
     */
    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                    mRecordButton.setBackgroundColor(Color.RED);
                } else {
                    setText("Start recording");
                    mRecordButton.setBackgroundColor(Color.BLUE);
                }
                mStartRecording = !mStartRecording;
            }
        };

        /**
         * Class contructor for RecordButton
         *
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
     *         Creates custom Play button with a listener.
     */
    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                    mRecordButton.setBackgroundColor(Color.RED);
                } else {
                    setText("Start playing");
                    mRecordButton.setBackgroundColor(Color.GREEN);
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        /**
         * Class contructor for PlayButton
         *
         * @param ctx Context is the parent of Activity and View.
         */
        public PlayButton(Context ctx) {
            super(ctx);
            setText("Preview Audio");
            setOnClickListener(clicker);

        }
    }

    /**
     * Switch that calls start and stop playing.
     *
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
            mPlayer.setDataSource(mFilePath);
            Log.e(LOG_TAG, mFilePath);
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
        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath += "/audiorecordtest.3gp";
    }

    /**
     * Creates the layout and buttons being drawn on the device screen.
     *
     * @param icicle Bundle.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);
        mRecordButton.setBackgroundColor(Color.BLUE);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(this);
        mPlayButton.setBackgroundColor(Color.GREEN);
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
     *
     * @return mFileName that is the file name in string format.
     */
    public String getmFileName() {
        return mFilePath;
    }

    public void upladAudioFile(String url) {
        Log.i("uploadAudioFile", url.toString());
        FileUploadTask task = new FileUploadTask();
        task.execute(new String[]{url.toString()});
    }

    private String buildFileUploadURL() {
        View v = this.findViewById(android.R.id.content);
        StringBuilder sb = new StringBuilder(FILE_UPLOAD_URL);

        try {
            sb.append("fileName=");
            sb.append(URLEncoder.encode(mFileName, "UTF-8"));
            sb.append("&fileDesc=");
            sb.append(URLEncoder.encode(mFileDescription, "UTF-8"));
            sb.append("&content=");
            String content = "a url will go here";
            sb.append(URLEncoder.encode(content, "UTF-8"));
            Log.i("AudioActivity", sb.toString());
        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return sb.toString();
    }


    private byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer, 0, read);
        }
        out.close();
        return out.toByteArray();
    }


    public void uploadFile() {

    }


    /**
     * async task for using web services for uploading file info
     */
    private class FileUploadTask extends AsyncTask<String, Void, String> {


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
                Log.i("status", "right before status");
                JSONObject jsonObject = new JSONObject(result);
                Log.i("status", "right after JSONObject call");
                String status = (String) jsonObject.get("result");
                Log.i("status", status);
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "File successfully uploaded!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to upload: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data " +
                        e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("JSON exception", e.getMessage());
            }
            //updateFileFragment();
       }
    }

//    /**
//     * Update the list view of files
//     */
//    public interface UpdateFileFragment {
//        void updateFileFragment();
//    }
}

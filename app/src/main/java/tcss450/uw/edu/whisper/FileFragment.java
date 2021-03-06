package tcss450.uw.edu.whisper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.whisper.file.AudioFile;
import tcss450.uw.edu.whisper.signin.SignInActivity;

/**
 * @author Winfield Brooks
 * List fragment containing all previously saved audio files.
 */
public class FileFragment extends Fragment {

    private static final String FILE_URL
            = "http://cssgate.insttech.washington.edu/~_450team4/fileList.php?cmd=files&user=";
    private RecyclerView mRecyclerView;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private DeleteFileInteractionListener mDeleteListener;
    private EditFileInteractionListener mEditListener;

    public final static String FILE_ITEM_SELECTED = "file_selected";


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FileFragment() {
    }

    /**
     *
     * @param columnCount
     * @return
     */
    @SuppressWarnings("unused")
    public static FileFragment newInstance(int columnCount) {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setNestedScrollingEnabled(false);
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            String user = SignInActivity.getUserName();
            String url = FILE_URL + user;
            DownloadCoursesTask task = new DownloadCoursesTask();
            task.execute(new String[]{url});
        }
        return view;
    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
            getActivity().recreate();
        }
    }

    /**
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            mDeleteListener = (DeleteFileInteractionListener) context;
            mEditListener = (EditFileInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     *
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * List item interaction interface implemented by WebServiceActivity.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(AudioFile item);
    }

    /**
     * Delete item interaction interface implemented by WebServiceActivity.
     */
    public interface DeleteFileInteractionListener {
        void onDeleteInteraction(AudioFile item);
    }

    /**
     * Edit item interaction interface implemented by WebServiceActivity.
     */
    public interface EditFileInteractionListener {
        void onEditInteraction(AudioFile item);
    }

    /**
     * Async Task to download AudioFiles from web service.
     */
    private class DownloadCoursesTask extends AsyncTask<String, Void, String> {

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
                    response = "Unable to download the list of files, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * Create List from retrieved AudioFiles for display.
         * @param result
         */
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            Log.i("FileFragment result", result);

            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            List<AudioFile> fileList = new ArrayList<AudioFile>();
            result = AudioFile.parseFileJSON(result, fileList);
            Log.i("FileFragment", fileList.toString());
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            // Everything is good, show the list of courses.
            if (!fileList.isEmpty()) {
                Log.i("FileFragment", mListener.toString());

                mRecyclerView.setAdapter(new MyFileRecyclerViewAdapter(fileList, mListener, mDeleteListener, mEditListener));
            }
        }
    }
}

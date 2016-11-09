package tcss450.uw.edu.whisper;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tcss450.uw.edu.whisper.FileFragment.OnListFragmentInteractionListener;
import tcss450.uw.edu.whisper.file.AudioFile;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AudioFile} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFileRecyclerViewAdapter extends RecyclerView.Adapter<MyFileRecyclerViewAdapter.ViewHolder> {

    private final List<AudioFile> mValues;
    private final OnListFragmentInteractionListener mListener;

    /**
     * Class constructor.
     * @param items a list of audio files.
     * @param listener the list fragment listener.
     */
    public MyFileRecyclerViewAdapter(List<AudioFile> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Class creates the ViewHolder object by inflating the view.
     * @param parent ViewGroup
     * @param viewType integer.
     * @return ViewHolder object
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_file, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the ViewHolder to a listener and creates text in it.
     * @param holder ViewHolder
     * @param position integer
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getFileName());
        holder.mContentView.setText(mValues.get(position).getDesc());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Getter for the number of values.
     * @return integer
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * @author Winfield Brooks
     * Class that creates the instance of ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mThumbnailView;
        public final TextView mIdView;
        public final TextView mContentView;
        public AudioFile mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        /**
         * Method to display the string representation of the content specified plus
         * content text single in quotes.
         * @return String
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

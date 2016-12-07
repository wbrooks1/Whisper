package tcss450.uw.edu.whisper;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tcss450.uw.edu.whisper.FileFragment.OnListFragmentInteractionListener;
import tcss450.uw.edu.whisper.file.AudioFile;

import java.util.List;

/**
 * Recycler view adapter to display list of files.
 */
public class MyFileRecyclerViewAdapter extends RecyclerView.Adapter<MyFileRecyclerViewAdapter.ViewHolder> {

    private final List<AudioFile> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final FileFragment.DeleteFileInteractionListener mDeleteListener;
    private final FileFragment.EditFileInteractionListener mEditListener;

    /**
     * Class constructor.
     * @param items a list of audio files.
     * @param listener the list fragment listener.
     */
    public MyFileRecyclerViewAdapter(List<AudioFile> items, OnListFragmentInteractionListener listener, FileFragment.DeleteFileInteractionListener delete, FileFragment.EditFileInteractionListener edit) {
        mValues = items;
        mListener = listener;
        mDeleteListener = delete;
        mEditListener = edit;
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
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mDeleteListener) {
                    mDeleteListener.onDeleteInteraction(holder.mItem);
                }
            }
        });
        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mEditListener) {
                    mEditListener.onEditInteraction(holder.mItem);
                }
            }
        });
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
        public final ImageButton mEditButton;
        public final ImageButton mDeleteButton;
        public AudioFile mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mEditButton = (ImageButton) view.findViewById(R.id.edit_button);
            mDeleteButton = (ImageButton) view.findViewById(R.id.delete_button);
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

//    /**
//     * Deletes file from database.
//     * @param view
//     */
//    public void deleteFile(View view) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Are you sure you to delete file: " + (mValues.get(position).getFileName());
//
//        Context context = builder.getContext();
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        builder.setView(layout);
//        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();
//
//    }
}

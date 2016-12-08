package tcss450.uw.edu.whisper.file;

/**
 * Created by Winfield Brooks on 10/27/16.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Serializable object for file saving and referencing.
 */
public class AudioFile implements Serializable {

    private String mFileName;
    private String mDesc;
    private String mContent;

    public static final String FILE_NAME = "fileName";
    public static final String DESC = "fileDesc";
    public static final String CONTENT = "content";

    public AudioFile(String mFileName, String mDesc, String mContent) {
        this.mFileName = mFileName;
        this.mDesc = mDesc;
        this.mContent = mContent;
    }

    /**
     * Parse incoming JSON from web service.
     * @param fileJSON
     * @param audioFileList
     * @return exception handling
     */
    public static String parseFileJSON(String fileJSON, List<AudioFile> audioFileList) {
        String reason = null;
        if (fileJSON != null) {
            try {
                JSONArray arr = new JSONArray(fileJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    //Log.i("AudioFile", obj.toString());
                    AudioFile file = new AudioFile(obj.getString(AudioFile.FILE_NAME), obj.getString(AudioFile.DESC)
                            , obj.getString(AudioFile.CONTENT));
                    audioFileList.add(file);
                }
            } catch (JSONException e) {
                reason = "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

    /**
     * Get file name.
     * @return file name
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     * Get file description.
     * @return file description
     */
    public String getDesc() {
        return mDesc;
    }

    /**
     * Get file content.
     * @return file content
     */
    public String  getContent() {
        return mContent;
    }

    /**
     * Set file name.
     * @param  new file name
     */
    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    /**
     * Set file description.
     * @param  new file name
     */
    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    /**
     * Set file content.
     * @param new file content
     */
    public void setContent(String mContent) {
        this.mContent = mContent;
    }
}

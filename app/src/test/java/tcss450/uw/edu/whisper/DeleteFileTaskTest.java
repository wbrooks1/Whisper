package tcss450.uw.edu.whisper;

import android.test.suitebuilder.annotation.LargeTest;

import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.whisper.file.AudioFile;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by Will on 12/7/2016.
 */

public class DeleteFileTaskTest {

    public void testDoInBackgroundReturnString(){
        AudioFile file = new AudioFile("File", "Description", "Content");
        String result = null;
        List<AudioFile> fileList = new ArrayList<AudioFile>();
        result = AudioFile.parseFileJSON(result, fileList);
        assertEquals(0, fileList.size());
        assertNull(result);
    }
    public void testDoInBackgroundReturnNull(){
        AudioFile file = new AudioFile("File", "Description", "Content");
        String result = "Not parsable string";
        List<AudioFile> fileList = new ArrayList<AudioFile>();
        result = AudioFile.parseFileJSON(result, fileList);
        assertEquals("Unable to upload file ", result.substring(0, 20));
    }

}

package tcss450.uw.edu.whisper;

import junit.framework.Assert;

import org.junit.Test;

import tcss450.uw.edu.whisper.file.AudioFile;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AudioFileTest {
    @Test
    public void testAudioFileConstructor() {
        assertNotNull(new AudioFile("File", "Description", "Content"));
    }


    @Test
    public void testAudioFileGetFileName() {
        AudioFile file = new AudioFile("File", "Description", "Content");
        assertEquals("File", file.getFileName());
    }

    @Test
    public void testAudioFileGetDesc() {
        AudioFile file = new AudioFile("File", "Description", "Content");
        assertEquals("Description", file.getDesc());
    }

    @Test
    public void testAudioFileGetContent() {
        AudioFile file = new AudioFile("File", "Description", "Content");
        assertEquals("Content", file.getContent());
    }




}



package tcss450.uw.edu.whisper;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.whisper.file.AudioFile;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Test for all methods in AudioFile.
 * @author Winfield Brooks
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

    @Test
    public void testAudioFileSetFileName() {
        AudioFile file = new AudioFile("File", "Description", "Content");
        file.setFileName("NewName");
        assertEquals("NewName", file.getFileName());
    }

    @Test
    public void testAudioFileSetDescription() {
        AudioFile file = new AudioFile("File", "Description", "Content");
        file.setDesc("New Description");
        assertEquals("New Description", file.getDesc());
    }

    @Test
    public void testAudioFileSetContent() {
        AudioFile file = new AudioFile("File", "Description", "Content");
        file.setContent("New Content");
        assertEquals("New Content", file.getContent());
    }



    @Test
    public void testParseFileJSON() {
        AudioFile file = new AudioFile("File", "Description", "Content");
        String result = "[{\"fileName\":\"File\",\"fileDesc\":\"Description\",\"content\":\"Description\"}]";
        List<AudioFile> fileList = new ArrayList<AudioFile>();
        result = AudioFile.parseFileJSON(result, fileList);
        assertEquals(1, fileList.size());
        assertNull(result);
    }

    @Test
    public void testParseFileJSONNullJSONString() {
        AudioFile file = new AudioFile("File", "Description", "Content");
        String result = null;
        List<AudioFile> fileList = new ArrayList<AudioFile>();
        result = AudioFile.parseFileJSON(result, fileList);
        assertEquals(0, fileList.size());
        assertNull(result);
    }

    @Test
    public void testParseFileJSONBadJSONString() {
        AudioFile file = new AudioFile("File", "Description", "Content");
        String result = "Not parsable string";
        List<AudioFile> fileList = new ArrayList<AudioFile>();
        result = AudioFile.parseFileJSON(result, fileList);
        assertEquals("Unable to parse data", result.substring(0, 20));
    }

}



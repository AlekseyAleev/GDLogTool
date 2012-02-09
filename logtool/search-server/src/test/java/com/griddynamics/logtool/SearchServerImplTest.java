package com.griddynamics.logtool;


import org.junit.*;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class SearchServerImplTest {

    private SearchServerImpl searchServer;
    private File solrDir;

    @Before
    public void init() {
        String path = new File("").getAbsolutePath();
        String fs = System.getProperty("file.separator");
        String solrPath = path + fs + "test_solr";
        solrDir = new File(solrPath);
        int postfix = 0;
        while(solrDir.exists()) {
            postfix ++;
            solrDir = new File(solrPath + postfix);
        }
        solrDir.mkdir();
        searchServer = new SearchServerImpl(solrDir.getAbsolutePath(), 100, 300000);
        
        Map<String, Object> doc = new HashMap<String, Object>();
        doc.put("path", "some path");
        doc.put("startIndex", "0");
        doc.put("length", "100");
        doc.put("content", "Test message");
        List<String> tags = new LinkedList<String>();
        tags.add("exception");
        tags.add("serverFailure");
        tags.add("loginFailure");
        doc.put("tags", tags);
        searchServer.index(doc);

        Map<String, Object> emptyTagsDoc = new HashMap<String, Object>();
        emptyTagsDoc.put("path", "another path");
        emptyTagsDoc.put("startIndex", "0");
        emptyTagsDoc.put("length", "100");
        emptyTagsDoc.put("content", "Test message");
        emptyTagsDoc.put("tags", new LinkedList<String>());
        searchServer.index(emptyTagsDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectIndex() {
        Map<String, Object> incorrectDoc = new HashMap<String, Object>();
        incorrectDoc.put("path", "some path");
        searchServer.index(incorrectDoc);
    }

    @Test
    public void testEmptySearch() {
        List<Map<String, String>> result = searchServer.search("path:nonexistent", -1, 0, "", "");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIndexAndSearch() {
        List<Map<String, String>> result = searchServer.search("path:\"some path\"", -1, 0, "", "");
        for(Map<String, String> entry : result) {
            assertTrue(entry.get("path").equals("some path"));
            assertTrue(entry.get("startIndex").equals("0"));
            assertTrue(entry.get("length").equals("100"));
            assertTrue(entry.get("content").equals("Test message"));
        }
    }
    
    @Test
    public void testIndexAndSearchTags() {
        List<Map<String, String>> result = searchServer.search("path:\"some path\"", -1, 0, "", "");
        for(Map<String, String> entry : result) {
            assertTrue(entry.get("tags").equals("[exception, serverFailure, loginFailure]"));
        }
    }

    @Test
    public void testIndexAndSearchEmptyTags() {
        List<Map<String, String>> result = searchServer.search("path:\"another path\"", -1, 0, "", "");
        for(Map<String, String> entry : result) {
            assertNull(entry.get("tags"));
        }
    }

    @Test
    public void testDelete() {
        searchServer.delete("path:\"some path\"");
        List<Map<String, String>> result = searchServer.search("path:\"some path\"", -1, 0, "", "");
        assertTrue(result.isEmpty());
    }

    @After
    public void terminate() {
        searchServer.shutdown();
        deleteDirectory(solrDir);

    }

    static public boolean deleteDirectory(File path) {
    if(path.exists()) {
        File[] files = path.listFiles();
        for(int i = 0; i < files.length; i ++) {
            if(files[i].isDirectory()) {
                deleteDirectory(files[i]);
            }
            else {
                files[i].delete();
            }
        }
    }
    return(path.delete());
  }
}

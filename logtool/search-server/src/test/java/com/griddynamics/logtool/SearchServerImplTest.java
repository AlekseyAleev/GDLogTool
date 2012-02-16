package com.griddynamics.logtool;


import org.junit.*;

import java.io.File;
import java.util.*;

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

        Map<String, Object> doc1 = new HashMap<String, Object>();
        doc1.put("path", "some path");
        doc1.put("startIndex", "0");
        doc1.put("length", "100");
        doc1.put("content", "Test message");
        List<String> tags1 = new LinkedList<String>();
        tags1.add("exception");
        tags1.add("serverFailure");
        tags1.add("loginFailure");
        doc1.put("tags", tags1);
        searchServer.index(doc1);

        for (int i = 0; i < 10; ++i) {
            Map<String, Object> doc = new HashMap<String, Object>();
            doc.put("path", "some other path");
            doc.put("startIndex", "0");
            doc.put("length", (100 + i));
            doc.put("content", "Test message " + i);
            doc.put("timestamp", "2012-02-13T15:30:0" + i + "Z");
            List<String> tags = new LinkedList<String>();
            tags.add("exception" + i);
            tags.add("serverFailure");
            tags.add("loginFailure");
            doc.put("tags", tags);
            searchServer.index(doc);
        }

        for (int i = 9; i >= 0; --i) {
            Map<String, Object> doc = new HashMap<String, Object>();
            doc.put("path", "some other path2");
            doc.put("startIndex", "0");
            doc.put("length", (100 + i));
            doc.put("content", "Test message " + i);
            doc.put("timestamp", "2012-02-13T15:30:0" + i + ".05" + "Z");
            List<String> tags = new LinkedList<String>();
            tags.add("exception" + i);
            tags.add("serverFailure");
            tags.add("loginFailure");
            doc.put("tags", tags);
            searchServer.index(doc);
        }

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
    public void testAssureTags() {
        List<Map<String, String>> result = searchServer.search("path:\"some path\"", -1, 0, "", "");
        for(Map<String, String> entry : result) {
            assertTrue(entry.get("tags").equals("[exception, serverFailure, loginFailure]"));
        }
    }

    @Test
    public void testAssureEmptyTags() {
        List<Map<String, String>> result = searchServer.search("path:\"another path\"", -1, 0, "", "");
        for(Map<String, String> entry : result) {
            assertNull(entry.get("tags"));
        }
    }

    @Test
    public void testIndexAndSearchSortedByLengthAsc() {
        List<Map<String, String>> result = searchServer.search("path:\"some other path\"", 0, 50, "length", "asc");
        int counter = 0;
        for(Map<String, String> entry : result) {
            assertEquals(Integer.toString(100 + counter), entry.get("length"));
            ++counter;
        }
    }

    @Test
    public void testIndexAndSearchAllOccurrencesSortedByLengthAsc() {
        List<Map<String, String>> result = searchServer.search("path:\"some other path\"", -1, 0, "length", "asc");
        int counter = 0;
        for(Map<String, String> entry : result) {
            assertEquals(Integer.toString(100 + counter), entry.get("length"));
            ++counter;
        }
    }

    @Test
    @Ignore //due to different locales
    public void testIndexAndSearchSortedByTimestampAsc() {
        List<Map<String, String>> result = searchServer.search("path:\"some other path\"", 0, 50, "timestamp", "asc");
        int seconds = 0;
        for(Map<String, String> entry : result) {
            String timestamp = entry.get("timestamp");
            assertEquals(6, entry.size());
            assertEquals("Mon Feb 13 18:30:0" + seconds + " 2012",
                    timestamp.substring(0, 19) + " " + timestamp.substring(timestamp.length() - 4));
            ++seconds;
        }
    }

    @Test
    @Ignore //due to different locales
    public void testIndexAndSearchSortedByTimestampDesc() {
        List<Map<String, String>> result = searchServer.search("path:\"some other path\"", 0, 50, "timestamp", "desc");
        int seconds = 9;
        for(Map<String, String> entry : result) {
            String timestamp = entry.get("timestamp");
            assertEquals(6, entry.size());
            assertEquals("Mon Feb 13 18:30:0" + seconds + " 2012",
                    timestamp.substring(0, 19) + " " + timestamp.substring(timestamp.length() - 4));
            --seconds;
        }
    }

    @Test
    public void testIndexAndSearchDateRange() {
        List<Map<String, String>> result = searchServer.search("timestamp:[2012-02-13T15:30:02Z TO 2012-02-13T15:30:06Z]", -1, 0, "length", "asc");
        assertEquals(9, result.size());
    }

    @Test
    @Ignore //due to different locales
    public void testIndexAndSearchDateRangeSortedByTimestampDesc() {
        List<Map<String, String>> result = searchServer.search("timestamp:[2012-02-13T15:30:02Z TO 2012-02-13T15:30:06Z]", -1, 0, "timestamp", "desc");
        assertEquals(9, result.size());
        int seconds = 6;
        boolean changeSeconds = true;
        for(Map<String, String> entry : result) {
            String timestamp = entry.get("timestamp");
            assertEquals("Mon Feb 13 18:30:0" + seconds + " 2012",
                    timestamp.substring(0, 19) + " " + timestamp.substring(timestamp.length() - 4));
            if (changeSeconds) --seconds;
            changeSeconds = !changeSeconds;
        }
    }

    @Test
    @Ignore //due to different locales
    public void testIndexAndSearchDateRangeSortedByTimestampAsc() {
        List<Map<String, String>> result = searchServer.search("timestamp:[2012-02-13T15:30:02Z TO 2012-02-13T15:30:06Z]", -1, 0, "timestamp", "asc");
        assertEquals(9, result.size());
        int seconds = 2;
        boolean changeSeconds = false;
        for(Map<String, String> entry : result) {
            String timestamp = entry.get("timestamp");
            assertEquals("Mon Feb 13 18:30:0" + seconds + " 2012",
                    timestamp.substring(0, 19) + " " + timestamp.substring(timestamp.length() - 4));
            if (changeSeconds) ++seconds;
            changeSeconds = !changeSeconds;
        }
    }

    @Test
    public void testIndexAndSearchTags() {
        List<Map<String, String>> result = searchServer.search("tags:\"serverFailure\"", -1, 0, "length", "asc");
        assertEquals(21, result.size());
    }

    @Test
    public void testIndexAndSearchMultipleTags() {
        List<Map<String, String>> result = searchServer.search("tags:\"serverFailure\" AND tags:\"exception5\"", -1, 0, "length", "asc");
        assertEquals(2, result.size());
    }

    @Test
    public void testIndexAndSearchMultipleTagsWithOr() {
        List<Map<String, String>> result = searchServer.search("tags:serverFailure OR exception5", -1, 0, "length", "asc");
        assertEquals(21, result.size());
    }

    @Test
    public void testIndexAndSearchMultipleTagsWithoutQuotes() {
        List<Map<String, String>> result = searchServer.search("tags:serverFailure AND tags:exception5", -1, 0, "length", "asc");
        assertEquals(2, result.size());
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
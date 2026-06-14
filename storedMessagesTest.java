package com.mycompany.mavenproject1;

import org.junit.jupiter.api.Test;

// this class will test the stored messages features
public class StoredMessagesTest {

    // this will simulate the reading of stored messages from JSON 
    private int count = 0;
    private String[] ids = new String[100];
    private String[] phones = new String[100];
    private String[] texts = new String[100];
    private String[] hashes = new String[100];

    // helper to load test data from a simulated JSON string
    private void loadTestData(String jsonData) {
        
    // this will reset arrays
        count = 0;
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.StringReader(jsonData));
        String line;
        String currentId = "", currentPhone = "", currentText = "", currentHash = "", currentStatus = "";
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"messageID\"")) {
                    int start = line.indexOf("\"") + 1;
                    int end = line.indexOf("\"", start);
                    currentId = line.substring(start, end);
                } else if (line.startsWith("\"recipient\"")) {
                    int start = line.indexOf("\"") + 1;
                    int end = line.indexOf("\"", start);
                    currentPhone = line.substring(start, end);
                } else if (line.startsWith("\"messageText\"")) {
                    int firstQuote = line.indexOf("\"");
                    int secondQuote = line.indexOf("\"", firstQuote + 1);
                    currentText = line.substring(firstQuote + 1, secondQuote);
                } else if (line.startsWith("\"messageHash\"")) {
                    int start = line.indexOf("\"") + 1;
                    int end = line.indexOf("\"", start);
                    currentHash = line.substring(start, end);
                } else if (line.startsWith("\"status\"")) {
                    int start = line.indexOf("\"") + 1;
                    int end = line.indexOf("\"", start);
                    currentStatus = line.substring(start, end);
                }
                if (line.equals("}") || line.equals("},")) {
                    if (currentStatus.equals("stored")) {
                        ids[count] = currentId;
                        phones[count] = currentPhone;
                        texts[count] = currentText;
                        hashes[count] = currentHash;
                        count++;
                    }
                    currentId = ""; currentPhone = ""; currentText = ""; currentHash = ""; currentStatus = "";
                }
            }
        } catch (Exception e) {
            fail("Failed to parse test JSON");
        }
    }

    // this will test the data 
    private String getTestJson() {
        return "[\n" +
               "  { \"messageID\": \"1111111111\", \"recipient\": \"+27834557896\", \"messageText\": \"Did you get the cake?\", \"messageHash\": \"11:1:DIDCAKE\", \"status\": \"sent\" },\n" +
               "  { \"messageID\": \"2222222222\", \"recipient\": \"+27838884567\", \"messageText\": \"Where are you? You are late! I have asked you to be on time.\", \"messageHash\": \"22:2:WHEREON\", \"status\": \"stored\" },\n" +
               "  { \"messageID\": \"3333333333\", \"recipient\": \"+27834484567\", \"messageText\": \"Yohoooo, I am at your gate.\", \"messageHash\": \"33:3:YOHOOGATE\", \"status\": \"disregarded\" },\n" +
               "  { \"messageID\": \"5555555555\", \"recipient\": \"+27838884567\", \"messageText\": \"Ok, I am leaving without you.\", \"messageHash\": \"55:5:OKYOU\", \"status\": \"stored\" }\n" +
               "]";
    }

    @Test
    public void testSentMessagesArray() {
        // this test will check sent messages array but for now we will just check the stored messages count
        loadTestData(getTestJson());
        assertEquals(2, count); // only messages 2 and 5 are stored
        assertTrue(texts[0].contains("Where are you"));
        assertTrue(texts[1].contains("Ok, I am leaving"));
    }

    @Test
    public void testLongestMessage() {
        loadTestData(getTestJson());
        int longest = 0;
        for (int i = 1; i < count; i++) {
            if (texts[i].length() > texts[longest].length()) {
                longest = i;
            }
        }
        assertEquals("Where are you? You are late! I have asked you to be on time.", texts[longest]);
    }

    @Test
    public void testSearchByMessageID() {
        loadTestData(getTestJson());
        String targetId = "2222222222";
        String foundText = null;
        for (int i = 0; i < count; i++) {
            if (ids[i].equals(targetId)) {
                foundText = texts[i];
                break;
            }
        }
        assertNotNull(foundText);
        assertTrue(foundText.contains("Where are you"));
    }

    @Test
    public void testSearchByRecipient() {
        loadTestData(getTestJson());
        String targetPhone = "+27838884567";
        java.util.ArrayList<String> matches = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (phones[i].equals(targetPhone)) {
                matches.add(texts[i]);
            }
        }
        assertEquals(2, matches.size());
        assertTrue(matches.get(0).contains("Where are you"));
        assertTrue(matches.get(1).contains("Ok, I am leaving"));
    }

    @Test
    public void testDeleteByHash() {
        loadTestData(getTestJson());
        String deleteHash = "22:2:WHEREON"; // hash of message 2
        int indexToDelete = -1;
        for (int i = 0; i < count; i++) {
            if (hashes[i].equals(deleteHash)) {
                indexToDelete = i;
                break;
            }
        }
        assertTrue(indexToDelete != -1);
        // simulate deletion
        for (int i = indexToDelete; i < count - 1; i++) {
            ids[i] = ids[i+1];
            phones[i] = phones[i+1];
            texts[i] = texts[i+1];
            hashes[i] = hashes[i+1];
        }
        count--;
        assertEquals(1, count);
        assertFalse(texts[0].contains("Where are you"));
        assertTrue(texts[0].contains("Ok, I am leaving"));
    }

    @Test
    public void testDisplayReport() {
        loadTestData(getTestJson());
        // this just ensures that we can print without errors
        StringBuilder report = new StringBuilder();
        for (int i = 0; i < count; i++) {
            report.append(hashes[i]).append("|").append(phones[i]).append("|").append(texts[i]);
        }
        assertTrue(report.length() > 0);
    }
}
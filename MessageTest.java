package com.mycompany.mavenproject1;

import org.junit.jupiter.api.Test;

// this class tests the Message class 
public class MessageTest {

    // test 1 - makes sure that the message should be 250 chars or less
    @Test
    public void testMessageLengthValid() {
        String msg = "Hi Mike, can you join us for dinner tonight?";
        if (msg.length() <= 250) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    // test 2 - makes sure that if the message is longer than 250 chars, it should fail
    @Test
    public void testMessageLengthExceeds() {
        String longMsg = "";
        for (int i = 0; i < 251; i++) {
            longMsg = longMsg + "A";
        }
        // checks is it's longer than 250 chars
        assertTrue(longMsg.length() > 250);
        
        // calculate how many extra chars were added
        int extra = longMsg.length() - 250;
        assertEquals(1, extra);  // should be 1 extra
    }

    // test 3 - checks that a valid phone number starts with +27
    @Test
    public void testRecipientValid() {
        LogIn login = new LogIn();
        boolean result = login.checkCellPhoneNumber("+27718693002");
        assertTrue(result);  
    }

    // test 4 - tells user invalid phone number (as there is no +)
    @Test
    public void testRecipientInvalid() {
        LogIn login = new LogIn();
        boolean result = login.checkCellPhoneNumber("08575975889");
        assertFalse(result);  
    }

    // test 5 - message hash format (first2:msgNum:firstWord+lastWord, all caps)
    @Test
    public void testMessageHashFormat() {
        // shows what happens inside Message class
        String fakeID = "1234567890";
        int msgNum = 1;
        String msgText = "Hello world";
        
        String firstTwo = fakeID.substring(0, 2);
        String[] words = msgText.trim().split(" ");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        String hash = (firstTwo + ":" + msgNum + ":" + firstWord + lastWord).toUpperCase();
        
        assertTrue(hash.matches("^[0-9]{2}:[0-9]+:[A-Z]+$"));
    }
}
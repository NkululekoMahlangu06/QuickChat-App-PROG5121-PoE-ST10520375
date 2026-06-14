package com.mycompany.mavenproject1;

import java.util.Scanner;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class Message {
    Scanner userInput = new Scanner(System.in);
    
    // arrays (same style as UserData)
    private int[] messageNum;
    private String[] messageID;
    private String[] phoneNumber;
    private String[] messageDetails;
    private String[] messageHash;
    private String[] messageStatus; // "sent", "stored", "disregarded"
    private String[] sentMessages; // these are my added populated arrays
    private String[] disregardedMessages;
    private String[] storedMessages;
    private String[] allHashes;
    private String[] allIDs;
    
    public Message() {}
    
    public void captureDetails() {
        System.out.print("How many messages do you wish to send? ");
        int num = userInput.nextInt();
        userInput.nextLine();  // eat the enter
        
        // set array sizes
        messageNum = new int[num];
        messageID = new String[num];
        phoneNumber = new String[num];
        messageDetails = new String[num];
        messageHash = new String[num];
        messageStatus = new String[num];
        
        int totalSent = 0;
        
        for (int i = 0; i < num; i++) {
            System.out.println("\n--- Message " + (i+1) + " ---");
            messageNum[i] = i + 1;
            
            // random 10-digit ID
            Random rand = new Random();
            long idLong = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
            messageID[i] = Long.toString(idLong).substring(0, 10);
            
            // phone number
            System.out.print("Enter phone number (+27...): ");
            String p = userInput.nextLine();
            
            LogIn check = new LogIn();
            if (check.checkCellPhoneNumber(p) == false) {
                System.out.println("Phone number wrong format. Try again.");
                i--;
                continue;
            }
            phoneNumber[i] = p;
            
            // message text
            System.out.print("Enter message (max 250 chars): ");
            String m = userInput.nextLine();
            
            if (m.length() > 250) {
                int extra = m.length() - 250;
                System.out.println("Too long by " + extra + " chars. Shorten it.");
                i--;
                continue;
            }
            System.out.println("Message ready to send.");
            messageDetails[i] = m;
            
            // create hash (first2:msgNum:firstWord+lastWord) all caps
            String firstTwo = messageID[i].substring(0, 2);
            String[] words = m.trim().split(" ");
            String firstWord = words[0];
            String lastWord = words[words.length - 1];
            String hash = firstTwo + ":" + messageNum[i] + ":" + firstWord + lastWord;
            messageHash[i] = hash.toUpperCase();
            
            // send / store / disregard
            System.out.println("\n1. Send Message");
            System.out.println("2. Store Message");
            System.out.println("3. Disregard Message");
            System.out.print("Choose: ");
            int choice = userInput.nextInt();
            userInput.nextLine();
            
            if (choice == 1) {
                messageStatus[i] = "sent";
                System.out.println("Message successfully sent.");
                totalSent++;
            } else if (choice == 2) {
                messageStatus[i] = "stored";
                System.out.println("Message successfully stored.");
            } else {
                messageStatus[i] = "disregarded";
                System.out.println("Press 0 to delete the message.");
            }
            
            // show details
            System.out.println("\nMessage ID: " + messageID[i]);
            System.out.println("Message Hash: " + messageHash[i]);
            System.out.println("Recipient: " + phoneNumber[i]);
            System.out.println("Message: " + messageDetails[i]);
        }
        // we first need to count
        int sentCount = 0;
        int disregardedCount = 0;
        int storedCount = 0;
        for (int i = 0; i < messageNum.length; i++) {
            if (messageStatus[i].equals("sent")) sentCount++;
            else if (messageStatus[i].equals("disregarded")) disregardedCount++;
            else if (messageStatus[i].equals("stored")) storedCount++;
        }
        
        //we then need to create arrays based on the size
        sentMessages = new String[sentCount];
        disregardedMessages = new String[disregardedCount];
        storedMessages = new String[storedCount];
        allHashes = new String[messageNum.length];
        allIDs = new String[messageNum.length];
        
        //we need to fill the arrays
        int s = 0, d = 0, st = 0;
        for (int i = 0; i < messageNum.length; i++) {
            allIDs[i] = messageID[i];
            allHashes[i] = messageHash[i];

            if (messageStatus[i].equals("sent")) {
                sentMessages[s] = messageDetails[i];
                s++;
            } else if (messageStatus[i].equals("disregarded")) {
                disregardedMessages[d] = messageDetails[i];
                d++;
            } else if (messageStatus[i].equals("stored")) {
                storedMessages[st] = messageDetails[i];
                st++;
            }
        }
        // save to JSON
        saveToJson();
        System.out.println("\nTotal number of messages sent: " + totalSent);
    }
   
    private void saveToJson() {
        try {
            FileWriter fw = new FileWriter("messages.json");
            fw.write("[\n");
            for (int i = 0; i < messageNum.length; i++) {
                if (messageID[i] == null) continue;
                fw.write("  {\n");
                fw.write("    \"messageID\": \"" + messageID[i] + "\",\n");
                fw.write("    \"messageNumber\": " + messageNum[i] + ",\n");
                fw.write("    \"recipient\": \"" + phoneNumber[i] + "\",\n");
                fw.write("    \"messageText\": \"" + messageDetails[i].replace("\"", "\\\"") + "\",\n");
                fw.write("    \"messageHash\": \"" + messageHash[i] + "\",\n");
                fw.write("    \"status\": \"" + messageStatus[i] + "\"\n");
                fw.write("  }");
                if (i < messageNum.length - 1) fw.write(",");
                fw.write("\n");
            }
            fw.write("]\n");
            fw.close();
            System.out.println("Messages saved to messages.json");
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }
}
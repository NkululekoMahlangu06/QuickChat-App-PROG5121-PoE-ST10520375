package com.mycompany.mavenproject1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LogIn myLogin = new LogIn();
 
        // Registration
        System.out.println("=====================================");
        System.out.println("   USER REGISTRATION SYSTEM");
        System.out.println("=====================================");
        System.out.print("Enter first name: ");
        String first = scanner.nextLine();
        System.out.print("Enter last name: ");
        String last = scanner.nextLine();
        System.out.print("Create username (must have _ and be 5 or less chars): ");
        String username = scanner.nextLine();
        System.out.print("Create password (8+ chars, 1 capital, 1 number, 1 special): ");
        String password = scanner.nextLine();
        System.out.print("Enter cell number (must start with + like +27731234567): ");
        String phone = scanner.nextLine();

        System.out.println("\n--- REGISTRATION RESULT ---");
        String result = myLogin.registerUser(username, password, phone, first, last);
        System.out.println(result);

        if (!result.startsWith("Username successfully captured")) {
            System.out.println("Registration failed. Exiting.");
            scanner.close();
            return;
        }

        // Login
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter username: ");
        String loginUser = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPass = scanner.nextLine();
        System.out.println("\n--- LOGIN RESULT ---");
        String status = myLogin.returnLoginStatus(loginUser, loginPass);
        System.out.println(status);

        if (!myLogin.loginUser(loginUser, loginPass)) {
            System.out.println("Login failed. Exiting.");
            scanner.close();
            return;
        }

        // QuickChat menu
        System.out.println("\nWelcome to QuickChat");
        Message msg = new Message();
        boolean running = true;

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Stored Messages");
            System.out.println("4. Quit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                msg.captureDetails();
            } else if (choice == 2) {
                System.out.println("Coming Soon.");
            } else if (choice == 3) {
                showStoredMessages(scanner);   // call the stored messages menu
            } else if (choice == 4) {
                running = false;
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    // ---------- STORED MESSAGES MENU ----------
    public static void showStoredMessages(Scanner scanner) {
        // temporary arrays (max 100 stored messages)
        String[] ids = new String[100];
        String[] phones = new String[100];
        String[] texts = new String[100];
        String[] hashes = new String[100];
        int count = 0;

        // read messages.json and collect only stored messages
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("messages.json"));
            String line;
            String currentId = "", currentPhone = "", currentText = "", currentHash = "", currentStatus = "";

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

                // end of one message object
                if (line.equals("}") || line.equals("},")) {
                    if (currentStatus.equals("stored")) {
                        ids[count] = currentId;
                        phones[count] = currentPhone;
                        texts[count] = currentText;
                        hashes[count] = currentHash;
                        count++;
                    }
                    // reset for next message
                    currentId = ""; currentPhone = ""; currentText = ""; currentHash = ""; currentStatus = "";
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading messages.json: " + e.getMessage());
            return;
        }

        if (count == 0) {
            System.out.println("No stored messages found.");
            return;
        }

        // sub‑menu
        boolean back = false;
        while (!back) {
            System.out.println("\n--- STORED MESSAGES MENU ---");
            System.out.println("1. Display all stored messages");
            System.out.println("2. Show longest stored message");
            System.out.println("3. Search by message ID");
            System.out.println("4. Search by recipient");
            System.out.println("5. Delete a message by hash");
            System.out.println("6. Display full report");
            System.out.println("7. Back to main menu");
            System.out.print("Choose: ");
            String option = scanner.nextLine();

            if (option.equals("1")) {
                System.out.println("\nAll stored messages:");
                for (int i = 0; i < count; i++) {
                    System.out.println("Recipient: " + phones[i]);
                    System.out.println("Message: " + texts[i]);
                    System.out.println("------------------------");
                }
            } else if (option.equals("2")) {
                int longest = 0;
                for (int i = 1; i < count; i++) {
                    if (texts[i].length() > texts[longest].length()) {
                        longest = i;
                    }
                }
                System.out.println("\nLongest stored message:");
                System.out.println("Message: " + texts[longest]);
                System.out.println("Recipient: " + phones[longest]);
            } else if (option.equals("3")) {
                System.out.print("Enter message ID: ");
                String searchId = scanner.nextLine();
                boolean found = false;
                for (int i = 0; i < count; i++) {
                    if (ids[i].equals(searchId)) {
                        System.out.println("Recipient: " + phones[i]);
                        System.out.println("Message: " + texts[i]);
                        found = true;
                        break;
                    }
                }
                if (!found) System.out.println("ID not found.");
            } else if (option.equals("4")) {
                System.out.print("Enter recipient phone number: ");
                String searchPhone = scanner.nextLine();
                boolean found = false;
                System.out.println("Messages for " + searchPhone + ":");
                for (int i = 0; i < count; i++) {
                    if (phones[i].equals(searchPhone)) {
                        System.out.println("- " + texts[i]);
                        found = true;
                    }
                }
                if (!found) System.out.println("No messages for this recipient.");
            } else if (option.equals("5")) {
                System.out.print("Enter message hash to delete: ");
                String deleteHash = scanner.nextLine();
                int index = -1;
                for (int i = 0; i < count; i++) {
                    if (hashes[i].equals(deleteHash)) {
                        index = i;
                        break;
                    }
                }
                if (index == -1) {
                    System.out.println("Hash not found.");
                } else {
                    // shift arrays left
                    for (int i = index; i < count - 1; i++) {
                        ids[i] = ids[i+1];
                        phones[i] = phones[i+1];
                        texts[i] = texts[i+1];
                        hashes[i] = hashes[i+1];
                    }
                    count--;
                    System.out.println("Message deleted successfully.");
                }
            } else if (option.equals("6")) {
                System.out.println("\n--- FULL REPORT ---");
                for (int i = 0; i < count; i++) {
                    System.out.println("Hash: " + hashes[i]);
                    System.out.println("Recipient: " + phones[i]);
                    System.out.println("Message: " + texts[i]);
                    System.out.println("------------------");
                }
            } else if (option.equals("7")) {
                back = true;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
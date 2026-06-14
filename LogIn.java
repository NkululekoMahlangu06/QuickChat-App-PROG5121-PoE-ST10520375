// this is my login class
package com.mycompany.mavenproject1;

// these are my variables 
public class LogIn {
    
    private String savedUser;
    private String savedPass;
    private String savedPhone;
    private String first;
    private String last;
    
    // this is the constructor
    // it runs when i make a new LogIn object
    public LogIn() {
        savedUser = "";
        savedPass = "";
        savedPhone = "";
        first = "";
        last = "";
    }
    
    // this method saves the users first and last name
    public void setUserNames(String f, String l) {
        first = f;
        last = l;
    }
    
    // this checks if the username is correct
    public boolean checkUserName(String u) {
        
        // first check if username is empty or null
        if (u == null || u.equals("")) {
            return false;
        }
        
        // check length
        if (u.length() > 5) {
            return false;
        }
        
        // look for underscore
        boolean found = false;
        for (int i = 0; i < u.length(); i++) {
            if (u.charAt(i) == '_') {
                found = true;
            }
        }
        
        if (found == true) {
            return true;
        } else {
            return false;
        }
    }
    
    // this checks if the password is strong enough
    public boolean checkPasswordComplexity(String p) {
        
        // check empty
        if (p == null || p.equals("")) {
            return false;
        }
        
        // check length
        if (p.length() < 8) {
            return false;
        }
        
        // check for capital letter
        boolean hasCap = false;
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                hasCap = true;
            }
        }
        if (hasCap == false) {
            return false;
        }
        
        // check for number
        boolean hasNum = false;
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c >= '0' && c <= '9') {
                hasNum = true;
            }
        }
        if (hasNum == false) {
            return false;
        }
        
        // check for special character
        boolean hasSpec = false;
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c == '!' || c == '@' || c == '#' || c == '$' || c == '%' || c == '&' || c == '*') {
                hasSpec = true;
            }
        }
        if (hasSpec == false) {
            return false;
        }
        
        return true;
    }
    
    // this checks if the cell phone number has the international code
    public boolean checkCellPhoneNumber(String ph) {
       
        // check empty
        if (ph == null || ph.equals("")) {
            return false;
        }
        
        // check if starts with +
        if (ph.charAt(0) != '+') {
            return false;
        }
        
        // check length
        if (ph.length() < 4) {
            return false;
        }
        if (ph.length() > 14) {
            return false;
        }
        
        // check if rest are numbers
        for (int i = 1; i < ph.length(); i++) {
            char c = ph.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        
        return true;
    }
    
    // this is the main registration method
    public String registerUser(String u, String p, String ph, String f, String l) {
        first = f;
        last = l;
        
        // check username
        boolean userOk = checkUserName(u);
        if (userOk == false) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        } else {
            savedUser = u;
        }
        
        // check password
        boolean passOk = checkPasswordComplexity(p);
        if (passOk == false) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        } else {
            savedPass = p;
        }
        
        // check phone
        boolean phoneOk = checkCellPhoneNumber(ph);
        if (phoneOk == false) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        } else {
            savedPhone = ph;
        }
        
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }
    
    // this checks if the login details match
    public boolean loginUser(String u, String p) {
        if (savedUser.equals(u) && savedPass.equals(p)) {
            return true;
        } else {
            return false;
        }
    }
    
    // this returns the login message
    public String returnLoginStatus(String u, String p) {
        boolean check = loginUser(u, p);
        if (check == true) {
            return "Welcome " + first + " , " + last + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
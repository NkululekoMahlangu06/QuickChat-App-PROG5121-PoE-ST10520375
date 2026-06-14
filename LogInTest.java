// this is my test class
package com.mycompany.mavenproject1;

public class LogInTest {
    public static void main(String[] args) {
        LogIn x = new LogIn();
        
        // test 1 - checking good username
        boolean t1 = x.checkUserName("kyl_1");
        System.out.println("Test 1 (username good): " + (t1 ? "PASS" : "FAIL"));
        
        // test 2 - checking bad username
        boolean t2 = x.checkUserName("kyleee");
        System.out.println("Test 2 (username bad): " + (t2 ? "FAIL" : "PASS"));
        
        // test 3 - checking good password
        boolean t3 = x.checkPasswordComplexity("Pass123!");
        System.out.println("Test 3 (password good): " + (t3 ? "PASS" : "FAIL"));
        
        // test 4 - checking bad password
        boolean t4 = x.checkPasswordComplexity("password");
        System.out.println("Test 4 (password bad): " + (t4 ? "FAIL" : "PASS"));
        
        // test 5 - checking good phone number
        boolean t5 = x.checkCellPhoneNumber("+27731234567");
        System.out.println("Test 5 (phone good): " + (t5 ? "PASS" : "FAIL"));
        
        // test 6 - checking bad phone number
        boolean t6 = x.checkCellPhoneNumber("0834567890");
        System.out.println("Test 6 (phone bad): " + (t6 ? "FAIL" : "PASS"));
    }
}
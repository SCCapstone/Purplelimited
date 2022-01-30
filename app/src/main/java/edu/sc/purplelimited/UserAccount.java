package edu.sc.purplelimited;

public class UserAccount {
    // Set default values for UserAccount
    private String userName;
    private String userPassword;

    // Constructor
    UserAccount(String UserName, String UserPassword){
        this.userName = UserName;
        this.userPassword = UserPassword;
    }

    public String getuserName() {

        return userName;
    }

    public void setuserName(String UserName) {

        userName = UserName;
    }

    public String getuserPassword() {

        return userPassword;
    }

    public void setuserPassword(String UserPassword) {

        userPassword = UserPassword;
    }
}
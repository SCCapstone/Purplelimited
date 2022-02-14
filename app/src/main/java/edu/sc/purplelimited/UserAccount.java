package edu.sc.purplelimited;

import java.util.HashMap;
import java.util.Map;

public class UserAccount {

    // Creating HashMap
    private HashMap<String, String> accountMap = new HashMap<String, String>();

    public void addAccount(String savedUsername, String savedPassword) {
        accountMap.put(savedUsername, savedPassword);
    }

    public boolean checkUsername(String savedUsername) {
        return accountMap.containsKey(savedUsername);
    }

    public boolean checkAccount(String savedUsername, String savedPassword) {
        if(accountMap.containsKey(savedUsername)) {
            return savedPassword.equals(accountMap.get(savedUsername));
        }
        return false;
    }

    public void loadAccount(Map<String, ?> preferencesMap) {
        for(Map.Entry<String, ?> entries : preferencesMap.entrySet()) {
            if(!entries.getKey().equals("RememeberMe") || !entries.getKey().equals("SavedUsername")) {
                if(!entries.getKey().equals("SavedPassword")) {
                    accountMap.put(entries.getKey(), entries.getValue().toString());
                }
            }
        }
    }
}
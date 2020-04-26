package com.jai.mystarter.security;



import com.jai.mystarter.models.auth.User;

import java.util.HashMap;
import java.util.Map;

public class UserSessionCache {
    private UserSessionCache() {
    }

    private static UserSessionCache INSTANCE = new UserSessionCache();

    public static UserSessionCache getInstance() {
        return INSTANCE;
    }

    Map<String, User> sessionCache = new HashMap<>();

    public void addUserSession(String username, User user) {
        sessionCache.put(username, user);
    }

    public void deleteUserSession(String username) {
        sessionCache.remove(username);
    }

    public User getUserSession(String username) {
        return sessionCache.get(username);
    }
}

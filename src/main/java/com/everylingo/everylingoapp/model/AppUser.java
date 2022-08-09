package com.everylingo.everylingoapp.model;

import java.util.List;

public class AppUser {
    private String authProviderId;
    private AppUserRole role;
    private boolean enabled;
    private List<Request> requests;
    private List<Language> preferredLanguages;
}
package com.example.tp1clientandroid;

public class UserManager {
    private static UserManager instance;
    private String username;

    // Constructeur privé pour empêcher l'instanciation directe depuis l'extérieur de la classe
    private UserManager() {}

    // Méthode pour obtenir l'instance unique du singleton
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // Méthode pour définir le nom d'utilisateur
    public void setUsername(String username) {
        this.username = username;
    }

    // Méthode pour récupérer le nom d'utilisateur
    public String getUsername() {
        return username;
    }
}

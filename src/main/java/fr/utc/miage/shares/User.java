package fr.utc.miage.shares;

public class User {

    private String name;
    private String firstName;
    private Portefeuille portefeuille;

    public User(String name, String firstName, Portefeuille portefeuille) {
        this.name = name;
        this.firstName = firstName;
        this.portefeuille = portefeuille;
    }
    
}

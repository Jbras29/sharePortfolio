package fr.utc.miage.shares;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    void testGetFirstName() {
        User user = new User("Doe", "John");
        assertEquals("John", user.getFirstName());   }

    @Test
    void testGetName() {
        User user = new User("Doe", "John");
        assertEquals("Doe", user.getName());

    }

    @Test
    void testGetPortefeuille() {
        User user = new User("Doe", "John");
        assertEquals("Doe John", user.getPortefeuille().getNom());
        assertEquals("Standard", user.getPortefeuille().getType());
        assertEquals(Map.of(), user.getPortefeuille().getMapActions());

    }

    @Test
    void testSetFirstName() {
        User user = new User("Doe", "John");
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    void testSetName() {
        User user = new User("Doe", "John");
        user.setName("Smith");
        assertEquals("Smith", user.getName());

    }

    @Test
    void testSetPortefeuille() {
        User user = new User("Doe", "John");
        Portefeuille portefeuille = new Portefeuille("Doe John", "Standard", Map.of());
        user.setPortefeuille(portefeuille);
        assertEquals(portefeuille, user.getPortefeuille());
    }
}

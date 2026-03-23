package fr.utc.miage.shares;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortefeuilleTest {

    private Action action1;
    private Action action2;



    @Test
    void testConstructeurEtGetters() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(action1, 5);

        Portefeuille p = new Portefeuille("MonPortefeuille", "actions", map);

        assertEquals("MonPortefeuille", p.getNom());
        assertEquals("actions", p.getType());
        assertEquals(map, p.getMapActions());
    }


    @Test
    void testSetType() {
        Portefeuille p = new Portefeuille("P1", "actions", new HashMap<>());
        p.setType("crypto");
        assertEquals("crypto", p.getType());
    }

    @Test
    void testSetMapActions() {
        Portefeuille p = new Portefeuille("P1", "actions", new HashMap<>());
        Map<Action, Integer> newMap = new HashMap<>();
        newMap.put(action1, 10);
        p.setMapActions(newMap);
        assertEquals(newMap, p.getMapActions());
    }


    @Test
    void testAfficherPortefeuilleVide() {
        Portefeuille p = new Portefeuille("MonPortefeuille", "actions", new HashMap<>());
        String result = p.afficher();
        assertTrue(result.contains("vide"));
    }

    @Test
    void testAfficherPortefeuilleNonVide() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(action1, 3);
        Portefeuille p = new Portefeuille("MonPortefeuille", "actions", map);
        String result = p.afficher();
        assertTrue(result.contains("actions"));
    }


    @Test
    void testContientActionPresente() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(action1, 2);
        Portefeuille p = new Portefeuille("P1", "actions", map);
        assertTrue(p.contientAction(action1));
    }

    @Test
    void testContientActionAbsente() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(action1, 2);
        Portefeuille p = new Portefeuille("P1", "actions", map);
        assertFalse(p.contientAction(action2));
    }

    @Test
    void testContientActionPortefeuilleVide() {
        Portefeuille p = new Portefeuille("P1", "actions", new HashMap<>());
        assertFalse(p.contientAction(action1));
    }
}
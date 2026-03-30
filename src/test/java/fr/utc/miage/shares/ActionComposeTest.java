/*
 * Copyright 2025 David Navarre &lt;David.Navarre at irit.fr&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.utc.miage.shares;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;

import org.junit.jupiter.api.Test;

class ActionComposeTest {
    private static final String Test_ActionCompose = "CAC40";
    private static final String Test_Action1 = "EDF";
    private static final String Test_Action2 = "Total";

    @Test
    void testConstructorShouldntThrowException() {
        assertDoesNotThrow(() -> new ActionCompose(Test_ActionCompose));
    }

    @Test
    void testAddActionWithValidValuesShouldReturnTrue() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.5f));
        assertTrue(actionCompose.addAction(action2, 0.5f));
    }

    @Test
    void testGetActionsShouldReturnMap() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.5f));
        assertTrue(actionCompose.addAction(action2, 0.5f));

        Map<Action, Float> actions = actionCompose.getActions();
        assertTrue(actions.containsKey(action1));
        assertTrue(actions.containsKey(action2));
        assertTrue(actions.get(action1) == 0.5f);
        assertTrue(actions.get(action2) == 0.5f);
    }

    @Test
    void testAddActionWithInvalidProportionShouldThrowException() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);

        assertThrows(IllegalArgumentException.class, () -> actionCompose.addAction(action1, -0.1f));
        assertThrows(IllegalArgumentException.class, () -> actionCompose.addAction(action1, 1.1f));
    }

    @Test
    void testAddActionWithAlreadyExistingActionShouldReturnFalse() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);

        assertTrue(actionCompose.addAction(action1, 0.5f));
        assertTrue(!actionCompose.addAction(action1, 0.5f));
    }

    @Test
    void testIsComposeValidWithValidProportionsShouldReturnTrue() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.5f));
        assertTrue(actionCompose.addAction(action2, 0.5f));

        assertTrue(actionCompose.isComposeValid());
    }

    @Test
    void testIsComposeValidWithOverProportionsShouldReturnFalse() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.7f));
        assertTrue(actionCompose.addAction(action2, 0.5f));

        assertFalse(actionCompose.isComposeValid());
    }

    @Test
    void testIsComposeValidWithUnderProportionsShouldReturnFalse() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.1f));
        assertTrue(actionCompose.addAction(action2, 0.5f));

        assertFalse(actionCompose.isComposeValid());
    }

    @Test
    void testGetProportionWithExistingActionShouldReturnProportion() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);

        Float proportion = 0.5f;

        assertTrue(actionCompose.addAction(action1, proportion));

        assertEquals(proportion, actionCompose.getProportion(action1));
    }

    @Test
    void testValeurWithValidCompositionShouldReturnCorrectValue() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.5f));
        assertTrue(actionCompose.addAction(action2, 0.5f));

        Jour jour = new Jour(2025, 3,23);
        float expectedValue = (action1.valeur(jour) * 0.5f) + (action2.valeur(jour) * 0.5f);

        assertEquals(expectedValue, actionCompose.valeur(jour), 0.0001);
    }

    @Test
    void testValeurWithInvalidCompositionShouldThrowIllegalStateException() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.1f));
        assertTrue(actionCompose.addAction(action2, 0.5f));

        Jour jour = new Jour(2025, 3,23);
        assertThrows(IllegalStateException.class, () -> actionCompose.valeur(jour));
    }

    @Test
    void testEqualsAndHashCode() {
        ActionCompose actionCompose1 = new ActionCompose(Test_ActionCompose);
        ActionCompose actionCompose2 = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose1.addAction(action1, 0.5f));
        assertTrue(actionCompose1.addAction(action2, 0.5f));
        assertTrue(actionCompose2.addAction(action1, 0.5f));
        assertTrue(actionCompose2.addAction(action2, 0.5f));

        assertEquals(actionCompose1, actionCompose2);
        assertEquals(actionCompose1.hashCode(), actionCompose2.hashCode());
    }

    @Test
    void testToString() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.5f));
        assertTrue(actionCompose.addAction(action2, 0.5f));

        String expectedString = "ActionCompose [libelle=" + Test_ActionCompose + ", actions=["
                + action2.toString() + ": 0.5, " + action1.toString() + ": 0.5]";

        assertEquals(expectedString, actionCompose.toString());
    }

    @Test
    void testRemoveActionWithExistingActionShouldReturnTrue() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.5f));
        assertTrue(actionCompose.addAction(action2, 0.5f));

        assertTrue(actionCompose.removeAction(action1));
        assertFalse(actionCompose.getActions().containsKey(action1));
    }

    @Test
    void testRemoveActionWithNonExistingActionShouldReturnFalse() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        assertTrue(actionCompose.addAction(action1, 0.5f));

        assertFalse(actionCompose.removeAction(action2));
    }

    @Test
    void testUpdateProportionWithExistingActionShouldReturnTrue() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);

        assertTrue(actionCompose.addAction(action1, 0.5f));

        assertTrue(actionCompose.updateProportion(action1, 0.7f));
        assertEquals(0.7f, actionCompose.getProportion(action1));
    }

    @Test
    void testUpdateProportionWithNonExistingActionShouldReturnFalse() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);

        assertFalse(actionCompose.updateProportion(action1, 0.7f));
    }

    @Test
    void testUpdateProportionWithInvalidProportionShouldThrowException() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);

        assertTrue(actionCompose.addAction(action1, 0.5f));
        assertThrows(IllegalArgumentException.class, () -> actionCompose.updateProportion(action1, -0.1f));
    }

    @Test
    void valeur_compositionAvecCours_retourneValeurCorrecte() {
        ActionCompose actionCompose = new ActionCompose(Test_ActionCompose);
        ActionSimple action1 = new ActionSimple(Test_Action1);
        ActionSimple action2 = new ActionSimple(Test_Action2);

        Jour jour = new Jour(2025, 3, 23);
        action1.enrgCours(jour, 100f);
        action2.enrgCours(jour, 200f);

        actionCompose.addAction(action1, 0.4f);
        actionCompose.addAction(action2, 0.6f);

        float result = actionCompose.valeur(jour);

        assertEquals(160f, result, 0.0001f); // (100*0.4) + (200*0.6)
    }
}
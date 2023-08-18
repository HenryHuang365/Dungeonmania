package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MercenaryTest {

    @Test
    @Tag("12-1")
    @DisplayName("Test mercenary in line with Player moves towards them")
    public void simpleMovement() {
        //                                  Wall    Wall   Wall    Wall    Wall    Wall
        // P1       P2      P3      P4      M4      M3      M2      M1      .      Wall
        //                                  Wall    Wall   Wall    Wall    Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_simpleMovement", "c_mercenaryTest_simpleMovement");

        assertEquals(new Position(8, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), getMercPos(res));
    }

    @Test
    @Tag("12-2")
    @DisplayName("Test mercenary stops if they cannot move any closer to the player")
    public void stopMovement() {
        //                  Wall     Wall    Wall
        // P1       P2      Wall      M1     Wall
        //                  Wall     Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_stopMovement", "c_mercenaryTest_stopMovement");

        Position startingPos = getMercPos(res);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(startingPos, getMercPos(res));
    }

    @Test
    @Tag("12-3")
    @DisplayName("Test mercenaries can not move through closed doors")
    public void doorMovement() {
        //                  Wall     Door    Wall
        // P1       P2      Wall      M1     Wall
        // Key              Wall     Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_doorMovement", "c_mercenaryTest_doorMovement");

        Position startingPos = getMercPos(res);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(startingPos, getMercPos(res));
    }

    @Test
    @Tag("12-4")
    @DisplayName("Test mercenary moves around a wall to get to the player")
    public void evadeWall() {
        //                  Wall      M2
        // P1       P2      Wall      M1
        //                  Wall      M2
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_evadeWall", "c_mercenaryTest_evadeWall");

        res = dmc.tick(Direction.RIGHT);
        assertTrue(new Position(4, 1).equals(getMercPos(res))
            || new Position(4, 3).equals(getMercPos(res)));
    }

    @Test
    @Tag("12-5")
    @DisplayName("Testing a mercenary can be bribed with a certain amount")
    public void bribeAmount() {
        //                                                          Wall     Wall     Wall    Wall    Wall
        // P1       P2/Treasure      P3/Treasure    P4/Treasure      M4       M3       M2     M1      Wall
        //                                                          Wall     Wall     Wall    Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_bribeAmount", "c_mercenaryTest_bribeAmount");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up first treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getMercPos(res));

        // attempt bribe
        assertThrows(InvalidActionException.class, () ->
                dmc.interact(mercId)
        );
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // pick up second treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getMercPos(res));

        // attempt bribe
        assertThrows(InvalidActionException.class, () ->
                dmc.interact(mercId)
        );
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // pick up third treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(5, 1), getMercPos(res));

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("12-6")
    @DisplayName("Testing a mercenary can be bribed within a radius")
    public void bribeRadius() {
        //                                         Wall     Wall    Wall    Wall  Wall
        // P1       P2/Treasure      P3    P4      M4       M3       M2     M1    Wall
        //                                         Wall     Wall    Wall    Wall  Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_bribeRadius", "c_mercenaryTest_bribeRadius");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getMercPos(res));

        // attempt bribe
        assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("12-7")
    @DisplayName("Testing an allied mercenary does not battle the player")
    public void allyBattle() {
        //                                  Wall    Wall    Wall
        // P1       P2/Treasure      .      M2      M1      Wall
        //                                  Wall    Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_allyBattle", "c_mercenaryTest_allyBattle");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());


    }
    @Test
    @Tag("12-8")
    @DisplayName("Testing the movement of an allied mercenary")
    public void alliedMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_allyBattle", "simple");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        // the bribed mercenary
        // should follow the player by jumping to the player's previous location
        assertEquals(new Position(3, 1), TestUtils.getPlayerPos(res));
        //previous location of player is (2, 1) Merc should jump to (2, 1)
        assertEquals(new Position(2, 1), getMercPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 2), TestUtils.getPlayerPos(res));
        //previous location of player is (3, 1) Merc should jump to (3, 1)
        assertEquals(new Position(3, 1), getMercPos(res));

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(3, 1), TestUtils.getPlayerPos(res));
        //previous location of player is (3, 2) Merc should jump to (3, 2)
        assertEquals(new Position(3, 2), getMercPos(res));


    }

    @Test
    @Tag("12-9")
    @DisplayName("Test is player moves at tick 20 and tick 21")
    public void playerMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_simpleMovement", "c_mercenaryTest_simpleMovement");

        assertEquals(new Position(1, 1), getPlayerPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 2), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 3), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 4), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 5), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 6), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 7), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 8), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 9), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 10), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 11), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 12), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 13), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 14), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 15), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 16), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 17), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 18), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 19), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 20), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 21), getPlayerPos(res));

        // At the frontend, at tick 21, player does not move but stays at (1, 21)
        // but the test works well, so the frontend has a bug for entities' movement at tick 20-21

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 22), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 23), getPlayerPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 24), getPlayerPos(res));
    }

    @Test
    @Tag("12-10")
    @DisplayName("Testing an allied mercenary does not battle the player")
    public void mindControlledAllyBattle() {
        //                                  Wall    Wall    Wall
        // P1       P2/Treasure      .      M2      M1      Wall
        //                                  Wall    Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_mercenaryTest_mindControlledAllyBattle", "c_mercenaryTest_mindControlledAllybattle");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());


    }

    @Test
    @Tag("12-11")
    @DisplayName("Testing the movement of a mind controlled allied mercenary")
    public void mindControlledAlliedMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_mercenaryTest_mindControlledAllyBattle", "c_mercenaryTest_mindControlledAlliedMovement");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        // the bribed mercenary
        // should follow the player by jumping to the player's previous location
        assertEquals(new Position(3, 1), TestUtils.getPlayerPos(res));
        //previous location of player is (2, 1) Merc should jump to (2, 1)
        assertEquals(new Position(2, 1), getMercPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 2), TestUtils.getPlayerPos(res));
        //previous location of player is (3, 1) Merc should jump to (3, 1)
        assertEquals(new Position(3, 1), getMercPos(res));

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(3, 1), TestUtils.getPlayerPos(res));
        //previous location of player is (3, 2) Merc should jump to (3, 2)
        assertEquals(new Position(3, 2), getMercPos(res));
    }

    @Test
    @Tag("12-12")
    @DisplayName("Testing the duration of a mind controlled allied mercenary")
    public void mindControlledDuration() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_mercenaryTest_mindControllDuration", "c_mercenaryTest_mindControlledAlliedMovement");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // the bribed mercenary
        // should follow the player
        assertEquals(new Position(1, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 6), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 5), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 4), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 3), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 2), getMercPos(res));

        // mind control duration finished, mercenary should be interactable again
        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        assertEquals(new Position(5, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(8, 2), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(7, 2), getMercPos(res));

        // walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        assertEquals(new Position(7, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(6, 2), getMercPos(res));
    }

    @Test
    @Tag("12-13")
    @DisplayName("Testing the duration of a mind controlled allied mercenary")
    public void mindControlledDuration2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_mercenaryTest_mindControllDuration", "c_mercenaryTest_mindControlledAlliedMovement");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // the bribed mercenary
        // should follow the player
        assertEquals(new Position(1, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 6), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 5), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 4), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 3), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(9, 2), getMercPos(res));

        // mind control duration finished, mercenary should be interactable and hostile again
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        assertEquals(new Position(5, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(8, 2), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(7, 2), getMercPos(res));

        // walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        assertEquals(new Position(7, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(6, 2), getMercPos(res));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        assertEquals(new Position(8, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(7, 2), getMercPos(res));

        res = dmc.tick(Direction.LEFT);
        assertEquals(0, res.getBattles().size());
        assertEquals(new Position(7, 2), TestUtils.getPlayerPos(res));
        assertEquals(new Position(8, 2), getMercPos(res));

        // walk into mercenary, a battle does occur and mercenary is destroyed
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertEquals(new Position(8, 2), TestUtils.getPlayerPos(res));
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
    }

    // @Test
    // @Tag("12-13")
    // @DisplayName("Testing the duration of a mind controlled allied mercenary")
    // public void mindControlledDuration3() {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse res = dmc.newGame("d_mercenaryTest_mindControllDuration", "simple");

    //     String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

    //     // pick up sunstone
    //     res = dmc.tick(Direction.DOWN);
    //     assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    //     // build sceptre
    //     res = assertDoesNotThrow(() -> dmc.build("sceptre"));
    //     assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    //     assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

    //     // achieve bribe
    //     res = assertDoesNotThrow(() -> dmc.interact(mercId));
    //     assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    //     assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

    //     // the bribed mercenary
    //     // should follow the player
    //     assertEquals(new Position(1, 2), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(9, 6), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(2, 2), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(9, 5), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(3, 2), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(9, 4), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(4, 2), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(9, 3), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(5, 2), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(9, 2), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(6, 2), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(8, 2), getMercPos(res));

    //     // mind control duration finished, mercenary should be interactable and hostile again
    //     // achieve bribe
    //     res = assertDoesNotThrow(() -> dmc.interact(mercId));
    //     assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    //     assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

    //     assertEquals(new Position(6, 2), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(7, 2), getMercPos(res));

    //     res = dmc.tick(Direction.DOWN);
    //     assertEquals(new Position(6, 3), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(6, 2), getMercPos(res));

    //     res = dmc.tick(Direction.DOWN);
    //     assertEquals(new Position(6, 4), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(6, 3), getMercPos(res));

    //     res = dmc.tick(Direction.DOWN);
    //     assertEquals(new Position(6, 5), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(6, 4), getMercPos(res));

    //     res = dmc.tick(Direction.DOWN);
    //     assertEquals(new Position(6, 6), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(6, 5), getMercPos(res));

    //     res = dmc.tick(Direction.DOWN);
    //     assertEquals(new Position(6, 7), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(6, 6), getMercPos(res));

    //     // mind control duration finished, mercenary should be interactable and hostile again
    //     // achieve bribe
    //     res = assertDoesNotThrow(() -> dmc.interact(mercId));
    //     assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    //     assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

    //     assertEquals(new Position(6, 7), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(6, 6), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(7, 7), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(6, 7), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(8, 7), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(7, 7), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(9, 7), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(8, 7), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(10, 7), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(9, 7), getMercPos(res));

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(new Position(11, 7), TestUtils.getPlayerPos(res));
    //     assertEquals(new Position(10, 7), getMercPos(res));
    //     assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
    //     // mind control duration finished, mercenary should be interactable and hostile again
    //     // achieve bribe
    //     res = assertDoesNotThrow(() -> dmc.interact(mercId));
    //     assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    //     assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

    //     assertEquals(0, res.getBattles().size());
    //     assertEquals(new Position(11, 7), TestUtils.getPlayerPos(res));
    //     assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
    //     // assertEquals(new Position(6, 6), getMercPos(res));
    // }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }
}

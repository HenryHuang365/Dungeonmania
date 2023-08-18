package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


public class LogicalTest {
    @Test
    @Tag("17-1")
    @DisplayName("OR logic with lightbulb")
    public void orlightbulb() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_circuit_ORlightbulb", "c_logicalTest");
        Position lightbulbPos = getLightBulbOffPos(res);
        //activate the top switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        //OR logic satisfied, two ajecent conductor is activated
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //deactivate top switch, light bulb is off
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

        //activate the top switch again, light bulb is on
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //activate right switch, light bulb is still on
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //deactivate right switch, light bulb is still on
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //deactivate top switch, light bulb is off
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

    }

    @Test
    @Tag("17-2")
    @DisplayName("OR logic with switch door")
    public void orlightswitchdoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_circuit_ORswitchDoor", "c_logicalTest");
        //activate the top switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        //OR logic satisfied, two ajecent conductor is activated
        //go down and go through the door
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        //player can go though the door
        assertEquals(getPlayerPos(res), getSwitchDoorPos(res));
        //deactivate switch, door is closed
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);

        //try to go through the door again, door is closed
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertNotEquals(getPlayerPos(res), getSwitchDoorPos(res));

    }

    @Test
    @Tag("17-3")
    @DisplayName("OR logic Switch only")
    public void orSwitchOnly() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_switchOnlyOr", "c_logicalTest");
        Position lightbulbPos = getLightBulbOffPos(res);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        //activate the top switch
        //lights on
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
    }

    @Test
    @Tag("17-4")
    @DisplayName("AND logic with lightbulb")
    public void andlightbulb() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_circuit_ANDlightbulb", "c_logicalTest");
        Position lightbulbPos = getLightBulbOffPos(res);
        //activate the top switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        //AND logic is not satisfied, all ajecent conductor should be activated
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
        //activate other three switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        //all conductor near bulb are on, light bulb should be on
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //deactivate bottom switch, light bulb is off
        res = dmc.tick(Direction.UP);

    }

    @Test
    @Tag("17-5")
    @DisplayName("AND logic with switch door")
    public void andlightswitchdoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_circuit_ANDswitchDoor", "c_logicalTest");
        //activate the top switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        //AND logic is not satisfied, check if we can go through the door
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        //door is at the bottom
        //player can't go through the door
        Position curr = getPlayerPos(res);
        res = dmc.tick(Direction.DOWN);
        assertEquals(curr, getPlayerPos(res));
        assertNotEquals(getSwitchDoorPos(res), getPlayerPos(res));
        //go to activate other two switch and try to go through the door
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        //now all adjecent switch of door is activated
        //try to go through the door and should be fine
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(getSwitchDoorPos(res), getPlayerPos(res));
        //deactivate the bottom switch, we can't go
        //through the door
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        Position curr2 = getPlayerPos(res);
        res = dmc.tick(Direction.UP);
        assertEquals(curr2, getPlayerPos(res));
        assertNotEquals(getSwitchDoorPos(res), getPlayerPos(res));


    }

    @Test
    @Tag("17-6")
    @DisplayName("AND logic Switch only")
    public void andSwitchOnly() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_switchOnlyAND", "c_logicalTest");
        Position lightbulbPos = getLightBulbOffPos(res);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        //activate the top switch
        //lights is not on (one more switch is needed to turn)
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        //activated < 2 light off
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

        //active all four switch and check the situation every time
        //active a switch
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));

    }

    @Test
    @Tag("17-7")
    @DisplayName("XOR logic with lightbulb")
    public void xorlightbulb() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_circuit_XORlightbulb", "c_logicalTest");
        Position lightbulbPos = getLightBulbOffPos(res);
        //activate the top switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        //XOR logic doesn't satisfied, two ajecent conductor is activated
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
        //deactivate top switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

        //activate right switch, light bulb is on XOR satisfied
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //deactivate right switch, light bulb is still off
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
        //activate buttom switch, light bulb is on
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //activate another switch, light bulb is off (violate XOR)
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

    }

    @Test
    @Tag("17-8")
    @DisplayName("XOR logic with switch door")
    public void xorlightswitchdoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_circuit_XORswitchDoor", "c_logicalTest");
        //activate the top switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        //XOR logic is not satisfied, two ajecent conductor is activated
        //go down and go through the door, but can't go through
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        //player can't go though the door
        assertNotEquals(getPlayerPos(res), getSwitchDoorPos(res));
        //activate another switch, door is still closed
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);

        Position curr2 = getPlayerPos(res);
        res = dmc.tick(Direction.LEFT);
        assertEquals(curr2, getPlayerPos(res));

        //remove the boulder on the top switch so XOR satisfied
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(getSwitchDoorPos(res), getPlayerPos(res));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        //move all the boulder away XOR not satisfied
        Position curr3 = getPlayerPos(res);
        res = dmc.tick(Direction.LEFT);
        assertEquals(curr3, getPlayerPos(res));

    }

    @Test
    @Tag("17-9")
    @DisplayName("XOR logic Switch only")
    public void xorSwitchOnly() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_switchOnlyXOR", "c_logicalTest");
        Position lightbulbPos = getLightBulbOffPos(res);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        //activate the top switch satisfy XOR
        //lights on
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        //activate another switch, lights off, it does
        //not satisfy XOR
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
        //activate all other two switches, lights is not on
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

        //deactivate three switch and leaves one activated,
        //XOR is satisfied and light is on again

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));

    }

    @Test
    @Tag("17-10")
    @DisplayName("COAND logic with lightbulb")
    public void coandlightbulb() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTest_circuit_COANDlightbulb", "c_logicalTest");
        Position lightbulbPos = getLightBulbOffPos(res);
        //activate the top switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        //COAND logic satisfied, two ajecent conductor is activated
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //deactivate top switch, light bulb is off
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

        //activate the top switch again, light bulb is on
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //activate right switch, light bulb is still on
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //deactivate right switch, light bulb is still on
        res = dmc.tick(Direction.LEFT);
        assertEquals(lightbulbPos, getLightBulbOnPos(res));
        //deactivate top switch, light bulb is off
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertEquals(lightbulbPos, getLightBulbOffPos(res));

    }
    private Position getLightBulbOffPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "light_bulb_off").get(0).getPosition();
    }

    private Position getLightBulbOnPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "light_bulb_on").get(0).getPosition();
    }

    private Position getSwitchDoorPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "switch_door").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }
    // private Position getEntityPos(String e, DungeonResponse res) {
    //     return TestUtils.getEntities(res, e).get(0).getPosition();
    // }
}

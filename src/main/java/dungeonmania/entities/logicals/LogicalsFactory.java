package dungeonmania.entities.logicals;

import dungeonmania.entities.logicals.LogicStrategy.AND;
import dungeonmania.entities.logicals.LogicStrategy.COAND;
import dungeonmania.entities.logicals.LogicStrategy.OR;
import dungeonmania.entities.logicals.LogicStrategy.XOR;
import dungeonmania.util.Position;

public class LogicalsFactory {
    public static LogicalEntity buildLogicalEntity(String type, String logic, Position pos) {
        LogicalEntity ent = null;
        if (type.equals("light_bulb_off")) {
            if (logic.equals("and")) {
                ent = new LightBulb(pos, new AND());
            } else if (logic.equals("or")) {
                ent = new LightBulb(pos, new OR());
            } else if (logic.equals("xor")) {
                ent = new LightBulb(pos, new XOR());
            } else if (logic.equals("co_and")) {
                ent = new LightBulb(pos, new COAND());
            }
        }

        if (type.equals("switch_door")) {
            if (logic.equals("and")) {
                ent = new SwitchDoor(pos, new AND());
            } else if (logic.equals("or")) {
                ent = new SwitchDoor(pos, new OR());
            } else if (logic.equals("xor")) {
                ent = new SwitchDoor(pos, new XOR());
            } else if (logic.equals("co_and")) {
                ent = new SwitchDoor(pos, new COAND());
            }
        }

        return ent;
    }

}

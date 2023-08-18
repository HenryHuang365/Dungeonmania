package dungeonmania.entities.logicals;

import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.logicals.LogicStrategy.LogicalStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends LogicalEntity {

    private LogicalStrategy logic;

    public SwitchDoor(Position position, LogicalStrategy logic) {
        super(position);
        this.logic = logic;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (isOn() || entity instanceof Spider) {
            return true;
        }
        return false;
    }

    @Override
    public void turnOn(GameMap map) {
        if (logic.canBeActivated(map, this)) this.setOnOff(true);
    }

    @Override
    public void turnOff(GameMap map) {
        if (logic.canBeTurnOff(map, this)) this.setOnOff(false);
    }

    public LogicalStrategy getLogic() {
        return logic;
    }

    public void notifyXOR(int countOfActived) {
        if (countOfActived != 1) this.setOnOff(false);
        if (countOfActived == 1) this.setOnOff(true);
    }

    // public void notifyCOAND(int countOfActived) {
    //     if (countOfActived < 2) this.setOnOff(false);
    // }
}

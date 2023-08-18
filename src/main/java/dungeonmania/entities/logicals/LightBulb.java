package dungeonmania.entities.logicals;

import dungeonmania.entities.logicals.LogicStrategy.LogicalStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LightBulb extends LogicalEntity {

    private LogicalStrategy logic;

    public LightBulb(Position position, LogicalStrategy logic) {
        super(position);
        this.logic = logic;
    }

    @Override
    public void turnOn(GameMap map) {
        if (logic.canBeActivated(map, this)) this.setOnOff(true);
    }

    @Override
    public void turnOff(GameMap map) {
        //check if the other wire/switch adjecent to the light bulb is
        //activated, if yes, don't turn the light off
        //(in case other circuit is activating this entity)
        if (logic.canBeTurnOff(map, this)) this.setOnOff(false);
    }

    public LogicalStrategy getLogic() {
        return logic;
    }

    public void notifyXOR(int countOfActived) {
        if (countOfActived != 1) this.setOnOff(false);
        if (countOfActived == 1) this.setOnOff(true);
    }

    // public void notifyCOAND(int countOfActivedAtTick) {
    //     if (countOfActivedAtTick < 2) this.setOnOff(false);
    // }


}

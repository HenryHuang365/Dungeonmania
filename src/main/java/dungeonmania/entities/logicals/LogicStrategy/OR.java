package dungeonmania.entities.logicals.LogicStrategy;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.logicals.LogicalEntity;
import dungeonmania.entities.logicals.Wire;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class OR implements LogicalStrategy {

    @Override
    public boolean canBeActivated(GameMap map, LogicalEntity e) {
        return true;
    }

    @Override
    public boolean canBeTurnOff(GameMap map, LogicalEntity le) {
        //check if the other wire/switch adjecent to the light bulb is
        //activated, if yes, don't turn the light off
        //(in case other circuit is activating this entity)
        boolean canBeTurnOff = true;
        for (Position p : le.getCardinallyAdjacentPositions()) {
            for (Entity e : map.getEntities(p)) {
                if (e instanceof Wire) {
                    if (((Wire) e).isOn()) canBeTurnOff = false;
                }
                if (e instanceof Switch) {
                    if (((Switch) e).isActivated()) canBeTurnOff = false;
                }
            }
        }
        return canBeTurnOff;
    }

}

package dungeonmania.entities.logicals.LogicStrategy;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.logicals.LogicalEntity;
import dungeonmania.entities.logicals.Wire;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class XOR implements LogicalStrategy {

    @Override
    public boolean canBeActivated(GameMap map, LogicalEntity le) {
        int countOfActivated = 0;
        for (Position p : le.getCardinallyAdjacentPositions()) {
            for (Entity e : map.getEntities(p)) {
                if (e instanceof Wire) {
                    if (((Wire) e).isOn()) countOfActivated += 1;
                }
                if (e instanceof Switch) {
                    if (((Switch) e).isActivated()) countOfActivated += 1;
                }
            }
        }
        return countOfActivated == 1;
    }

    @Override
    public boolean canBeTurnOff(GameMap map, LogicalEntity le) {
        int countOfActivated = 0;
        for (Position p : le.getCardinallyAdjacentPositions()) {
            for (Entity e : map.getEntities(p)) {
                if (e instanceof Wire) {
                    if (((Wire) e).isOn()) countOfActivated += 1;
                }
                if (e instanceof Switch) {
                    if (((Switch) e).isActivated()) countOfActivated += 1;
                }
            }
        }
        return countOfActivated != 1;
    }

}

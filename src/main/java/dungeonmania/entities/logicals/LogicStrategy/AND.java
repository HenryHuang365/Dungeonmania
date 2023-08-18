package dungeonmania.entities.logicals.LogicStrategy;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.logicals.LogicalEntity;
import dungeonmania.entities.logicals.Wire;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class AND implements LogicalStrategy {

    @Override
    public boolean canBeActivated(GameMap map, LogicalEntity le) {
        int countOfActivated = 0;
        int countOfConductor = 0;
        for (Position p : le.getCardinallyAdjacentPositions()) {
            for (Entity e : map.getEntities(p)) {
                if (e instanceof Wire) {
                    if (((Wire) e).isOn()) countOfActivated += 1;
                    countOfConductor += 1;
                }
                if (e instanceof Switch) {
                    if (((Switch) e).isActivated()) {
                        countOfActivated += 1;
                        countOfConductor += 1;
                    }
                }
            }
        }
        return countOfActivated >= 2 && countOfActivated == countOfConductor;
    }

    @Override
    public boolean canBeTurnOff(GameMap map, LogicalEntity le) {
        int countOfActivated = 0;
        int countOfConductor = 0;
        for (Position p : le.getCardinallyAdjacentPositions()) {
            for (Entity e : map.getEntities(p)) {
                if (e instanceof Wire) {
                    if (((Wire) e).isOn()) countOfActivated += 1;
                    countOfConductor += 1;
                }
                if (e instanceof Switch) {
                    if (((Switch) e).isActivated()) {
                        countOfActivated += 1;
                        countOfConductor += 1;
                    }
                }
            }
        }
        return !(countOfActivated >= 2 && countOfActivated == countOfConductor);
    }

}

package dungeonmania.entities.logicals.LogicStrategy;

import dungeonmania.entities.logicals.LogicalEntity;
import dungeonmania.map.GameMap;

public interface LogicalStrategy {
    public boolean canBeActivated(GameMap map, LogicalEntity e);
    public boolean canBeTurnOff(GameMap map, LogicalEntity e);
}

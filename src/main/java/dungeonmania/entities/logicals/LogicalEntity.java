package dungeonmania.entities.logicals;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class LogicalEntity extends Entity {

    private boolean isOn = false;

    public LogicalEntity(Position position) {
        super(position);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOnOff(boolean status) {
        this.isOn = status;
    }

    public int countActivatedCAdjecent(GameMap map) {
        int countOfActivated = 0;
        for (Position p : this.getCardinallyAdjacentPositions()) {
            for (Entity e : map.getEntities(p)) {
                if (e instanceof Wire) {
                    if (((Wire) e).isOn()) countOfActivated += 1;
                }
                if (e instanceof Switch) {
                    if (((Switch) e).isActivated()) countOfActivated += 1;
                }
            }
        }
        return countOfActivated;
    }

    public abstract void turnOn(GameMap gameMap);
    public abstract void turnOff(GameMap gameMap);

}

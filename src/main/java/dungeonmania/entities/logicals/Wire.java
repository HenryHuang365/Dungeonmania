package dungeonmania.entities.logicals;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends LogicalEntity {


    public Wire(Position position) {
        super(position);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void turnOn(GameMap map) {
        // Turn On itself first, if it's already on, do nothing
        //if (this.isOn()) return;
        this.setOnOff(true);
        List<Position> pos = getCardinallyAdjacentPositions();
        map.turnOnLogicalEntity(pos);
    }

    @Override
    public void turnOff(GameMap map) {
        this.setOnOff(false);
        List<Position> pos = getCardinallyAdjacentPositions();
        map.turnOffLogicalEntity(pos);
    }

}

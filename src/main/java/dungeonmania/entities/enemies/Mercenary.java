package dungeonmania.entities.enemies;
import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.enemies.enemymovement.AlliedMovement;
import dungeonmania.entities.enemies.enemymovement.FollowPlayerMove;
import dungeonmania.entities.enemies.enemymovement.Movement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean alliedAndFoundPlayer = false;
    private Movement moveStrategy;
    private int mindcontrolduration = 0;
    private boolean isMindControlled = false;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (isAllied()) return;
        super.onOverlap(map, entity);
    }

    public void setMoveStrategy(Movement moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return (bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount)
        || (player.countEntityOfType(Sceptre.class) >= 1);
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        if (player.countEntityOfType(Sceptre.class) == 0) {
            // the player has sceptre in the inventory, so the player has to bribe the mercenary with treasure
            for (int i = 0; i < bribeAmount; i++) {
                player.use(Treasure.class);
            }
        }
    }

    @Override
    public void interact(Player player, Game game) {
        if (player.countEntityOfType(Sceptre.class) >= 1) {
            // the player has sceptre in the inventory, so use sceptre to control mercenary's mind
            setMindcontrolduration(((Sceptre) player.getFirstEntityOfType(Sceptre.class)).getMindcontrolduration());
            // this.isMindControlled = true;
            isMindControlled = true;
        }
        // allied = true;
        setAllied(true);
        if (isCardinallyAdjacentToPlayer(game) && isAllied()) this.alliedAndFoundPlayer = true;
        bribe(player);
        // if (isCardinallyAdjacentToPlayer(game) && allied) this.alliedAndFoundPlayer = true;
    }

    @Override
    public void move(Game game) {
        //once the player is adjecent to the allied mercenary, mercenary stick the player's movement
        if (isCardinallyAdjacentToPlayer(game) && isAllied()) this.alliedAndFoundPlayer = true;

        if (isAllied()) {
            if (!alliedAndFoundPlayer) {
                //friendly mercenary hasn't been Cardinally Adjacent To Player
                setMoveStrategy(new FollowPlayerMove());
                if (!ifStucked()) {
                    moveStrategy.moveToByMovement(this, game);
                } else {
                    getStuckedTile().tryToMoveAway(this);
                }
            } else {
                //stick the player's movement
                setMoveStrategy(new AlliedMovement());
                moveStrategy.moveToByMovement(this, game);
            }
        } else {
            setMoveStrategy(new FollowPlayerMove());
            if (!ifStucked()) {
                moveStrategy.moveToByMovement(this, game);
            } else {
                getStuckedTile().tryToMoveAway(this);
            }
        }

        //in case the player does not move
        if (isCardinallyAdjacentToPlayer(game) && isAllied()) this.alliedAndFoundPlayer = true;
        if (mindcontrolduration > 0) setMindcontrolduration(this.mindcontrolduration - 1);
        if (isMindControlled && mindcontrolduration == 0) {
            // allied = false;
            setAllied(false);
            isMindControlled = false;
        }


    }

    @Override
    public boolean isInteractable(Player player) {
        return !isAllied() && canBeBribed(player);
    }

    public int getMindcontrolduration() {
        return mindcontrolduration;
    }

    public void setMindcontrolduration(int mindcontrolduration) {
        this.mindcontrolduration = mindcontrolduration;
    }

}

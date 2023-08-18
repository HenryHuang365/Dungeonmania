package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.InventoryItem;

public class Sceptre extends Buildable  {

    private int mindcontrolduration;

    public Sceptre(int mindcontrolduration) {
        super(null);
        this.mindcontrolduration = mindcontrolduration;
    }

    @Override
    public void use(Game game) {
        // durability--;
        // if (durability <= 0) {
        //     // law of demeter - fixed
        //     // game.getPlayer().remove(this);
        //     game.removeSceptre(this, game.getPlayer());
        // }
        return;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            0,
            0,
            1,
            1,
            origin.getPlayer(),
            true)
        );
    }

    @Override
    public int getDurability() {
        return 1;
    }

    public static boolean isBuildable(int woodcount, int arrowcount,
    int keycount, int treasurecount, int sunstonecount) {
        return (woodcount >= 1 && arrowcount >= 2) || (keycount >= 1 && treasurecount >= 1)
        || (sunstonecount >= 1 && treasurecount >= 1) || (sunstonecount >= 1);
    }

    public static boolean isBuildableWithWoodAndArrows(int woodcount, int arrowcount) {
        return (woodcount >= 1 && arrowcount >= 2);
    }

    public static boolean isBuildableWithKeyAndTreasure(int keycount, int treasurecount) {
        return (keycount >= 1 && treasurecount >= 1);
    }

    public static boolean isBuildableWithSunstoneAndTreasure(int treasurecount, int sunstonecount) {
        return (sunstonecount >= 1 && treasurecount >= 1);
    }

    public static boolean isBuildableWithSunstoneAndKey(int keycount, int sunstonecount) {
        return (sunstonecount >= 1 && keycount >= 1);
    }

    public static boolean isBuildableWithSunstoneOnly(int sunstonecount) {
        return sunstonecount >= 1;
    }

    public static void build(List<InventoryItem> items, List<Arrow> arrows,
    List<Wood> wood, List<Key> keys, List<Treasure> treasures, List<SunStone> sunStones) {
        int maxwoodcount = 1;
        int maxarrowcount = 2;
        int maxkeycount = 1;
        int maxtreasurecount = 1;
        int maxsunstonecount = 1;
        int currwoodcount = wood.size();
        int currarrowcount = arrows.size();
        int currkeycount = keys.size();
        int currtreasurecount = treasures.size();
        int currsunstonecount = sunStones.size();

        // consume material
        if (isBuildableWithSunstoneAndTreasure(currtreasurecount, currsunstonecount)) {
            // consume sunstone and treasure to build sceptre
            int i = 0;
            while (i < maxtreasurecount && i < currtreasurecount) {
                items.remove(treasures.get(i));
                i += 1;
            }

            i = 0;
            while (i < maxsunstonecount && i < currsunstonecount) {
                // items.remove(sunStones.get(i));
                i += 1;
            }
        } else if (isBuildableWithSunstoneAndKey(currkeycount, currsunstonecount)) {
            // consume sunstone and key to build sceptre
            int i = 0;
            while (i < maxkeycount && i < currkeycount) {
                items.remove(keys.get(i));
                i += 1;
            }

            i = 0;
            while (i < maxsunstonecount && i < currsunstonecount) {
                // items.remove(sunStones.get(i));
                i += 1;
            }
        } else if (isBuildableWithKeyAndTreasure(currkeycount, currtreasurecount)) {
            // consume treasure and key to build sceptre
            int i = 0;
            while (i < maxkeycount && i < currkeycount) {
                items.remove(keys.get(i));
                i += 1;
            }

            i = 0;
            while (i < maxtreasurecount && i < currtreasurecount) {
                items.remove(treasures.get(i));
                i += 1;
            }
        } else if (isBuildableWithWoodAndArrows(currwoodcount, currarrowcount)) {
            // consume wood and arrows to build sceptre
            int i = 0;
            while (i < maxwoodcount && i < currwoodcount) {
                items.remove(wood.get(i));
                i += 1;
            }

            i = 0;
            while (i < maxarrowcount && i < currarrowcount) {
                items.remove(arrows.get(i));
                i += 1;
            }
        } else if (isBuildableWithSunstoneOnly(currsunstonecount)) {
            // consume sunstone to build sceptre
            int i = 0;
            while (i < maxsunstonecount && i < currsunstonecount) {
                items.remove(sunStones.get(i));
                i += 1;
            }
        }
    }

    public int getMindcontrolduration() {
        return mindcontrolduration;
    }
}


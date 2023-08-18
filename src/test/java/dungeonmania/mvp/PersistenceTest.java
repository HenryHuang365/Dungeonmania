package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
// import dungeonmania.util.Direction;
// import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
public class PersistenceTest {

    @Test
    @Tag("19-1")
    @DisplayName("Test loading the game")
    public void loadGame() throws InterruptedException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest", "c_assassinTest");

        String assaId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        assertDoesNotThrow(() -> dmc.saveGame("saved2"));
        assertDoesNotThrow(() -> dmc.allGames());
        TimeUnit.SECONDS.sleep(1);
        assertDoesNotThrow(() -> dmc.loadGame("saved2"));

        // dmc.saveGame("saved2");
        // dmc.allGames();
        // TimeUnit.SECONDS.sleep(1);
        // dmc.loadGame("saved2");
    }

    @Test
    @Tag("19-2")
    @DisplayName("Test save works on many different of entities")
    public void testSaveCompplexDungeon() throws InterruptedException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest", "c_assassinTest");

        dmc.saveGame("LOL");
        dmc = new DungeonManiaController();
        TimeUnit.SECONDS.sleep(1);
        dmc.loadGame("LOL");
    }
}

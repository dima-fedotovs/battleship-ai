package online.macities.battleship.model;

import online.macities.battleship.ai.PlayerAi;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameManager {
    public static final String ATTR = "gameManager";
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Game pending;

    public synchronized Game join(Player player) {
        Game result;
        if (pending == null) {
            pending = new Game(player);
            result = pending;
            scheduleAi(result);
        } else {
            pending.start(player);
            result = pending;
            pending = null;
        }
        return result;
    }

    private void scheduleAi(Game game) {
        executor.schedule(() -> {
            tryStartAi(game);
        }, 30, TimeUnit.SECONDS);
    }

    private synchronized void tryStartAi(Game game) {
        if (pending != game) {
            return;
        }
        var player = new Player("Computer");
        join(player);
        new PlayerAi(game, player, executor).start();
    }

    public synchronized void surrender(Game game, Player player) {
        if (pending == game) {
            pending = null;
            return;
        }
        if (game.getStatus() == GameStatus.FINISHED) {
            return;
        }
        game.surrender(player);
    }
}

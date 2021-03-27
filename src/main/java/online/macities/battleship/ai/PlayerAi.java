package online.macities.battleship.ai;

import online.macities.battleship.model.Game;
import online.macities.battleship.model.GameStatus;
import online.macities.battleship.model.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class PlayerAi {
    private static final List<String> COLS = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
    private static final List<String> ROWS = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    private static final List<Integer> SIZES = List.of(4, 3, 3, 2, 2, 2, 1, 1, 1, 1);

    private final Game game;
    private final Player player;
    private final ScheduledExecutorService executor;

    public PlayerAi(Game game, Player player, ScheduledExecutorService executor) {
        this.game = game;
        this.player = player;
        this.executor = executor;
    }

    public void start() {
        schedule(this::placeShips);
    }

    private void placeShips() {
        if (gameIsClosed()) {
            return;
        }
        var committedShips = new HashSet<String>();
        for (var size : SIZES) {
            var uncommittedShips = new HashSet<String>();
            do {
                uncommittedShips.clear();
                uncommittedShips.addAll(committedShips);
                var ship = randomShip(size);
                uncommittedShips.addAll(ship);
            } while ((uncommittedShips.size() - committedShips.size() != size) || !player.validateIncompleteShips(uncommittedShips));
            committedShips.clear();
            committedShips.addAll(uncommittedShips);
        }
        player.setShips(committedShips);
        if (!player.isPlayerFieldValid()) {
            throw new IllegalStateException("Ships are not valid");
        }
        game.checkGameStart();
        schedule(this::tryFire);
    }

    private Set<String> randomShip(Integer size) {
        var result = new HashSet<String>();
        var rand = ThreadLocalRandom.current();
        var vertical = rand.nextBoolean();
        if (vertical) {
            var colIdx = rand.nextInt(COLS.size());
            var boundRow = ROWS.size() - size + 1;
            var rowIdx = rand.nextInt(boundRow);
            for (int i = 0; i < size; i++) {
                var addr = COLS.get(colIdx) + ROWS.get(rowIdx + i);
                result.add(addr);
            }
        } else {
            var boundCol = COLS.size() - size + 1;
            var colIdx = rand.nextInt(boundCol);
            var rowIdx = rand.nextInt(ROWS.size());
            for (int i = 0; i < size; i++) {
                var addr = COLS.get(colIdx + i) + ROWS.get(rowIdx);
                result.add(addr);
            }
        }
        return result;
    }

    private void tryFire() {
        if (gameIsClosed()) {
            return;
        }
        if (game.getStatus() == GameStatus.IN_PROGRESS && game.getCurrentPlayer() == player) {
            var addr = randomAddr();
            game.fire(addr);
        }
        schedule(this::tryFire);
    }

    private String randomAddr() {
        var rand = ThreadLocalRandom.current();
        var colIdx = rand.nextInt(COLS.size());
        var rowIdx = rand.nextInt(ROWS.size());
        var col = COLS.get(colIdx);
        var row = ROWS.get(rowIdx);
        return col + row;
    }

    private boolean gameIsClosed() {
        return game.getStatus() == GameStatus.FINISHED;
    }

    private void schedule(Runnable action) {
        var rand = ThreadLocalRandom.current();
        var delay = rand.nextInt(3000, 15000);
        executor.schedule(action, delay, TimeUnit.MILLISECONDS);
    }
}

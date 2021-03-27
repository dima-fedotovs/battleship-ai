package online.macities.battleship.model;

public class Game {
    public static final String ATTR = "game";
    private final Player player1;
    private Player player2;
    private boolean player1turn = true;
    private GameStatus status = GameStatus.INCOMPLETE;
    private GameResultType result = GameResultType.NOT_FINISHED;

    public Game(Player player1) {
        this.player1 = player1;
    }

    public void start(Player player2) {
        this.player2 = player2;
        this.status = GameStatus.SETTING_UP;
    }

    public void fire(String address) {
        var curr = getCurrentPlayer();
        var await = getAwaitPlayer();
        var firedCellStatus = await.getPlayerFieldCell(address);
        switch (firedCellStatus) {
            case SHIP:
                curr.setOpponentViewCell(address, CellStatus.HIT);
                await.setPlayerFieldCell(address, CellStatus.HIT);
                if (!await.hasMoreShips()) {
                    status = GameStatus.FINISHED;
                    result = player1turn ? GameResultType.PLAYER1_WON : GameResultType.PLAYER2_WON;
                }
                break;
            case EMPTY:
                curr.setOpponentViewCell(address, CellStatus.MISS);
                await.setPlayerFieldCell(address, CellStatus.MISS);
                player1turn = !player1turn;
                break;
            case MISS:
            case HIT:
                player1turn = !player1turn;
                break;
            default:
                // should never happens
                throw new IllegalStateException("Unknown cell status " + firedCellStatus);
        }
    }

    void surrender(Player player) {
        status = GameStatus.FINISHED;
        if (player1 == player) {
            result = GameResultType.PLAYER1_SURRENDERED;
        } else {
            result = GameResultType.PLAYER2_SURRENDERED;
        }
    }

    public void checkGameStart() {
        if (player1.isPlayerFieldValid() && player2.isPlayerFieldValid()) {
            status = GameStatus.IN_PROGRESS;
        }
    }

    public Player getCurrentPlayer() {
        if (player1turn) {
            return player1;
        } else {
            return player2;
        }
    }

    public Player getAwaitPlayer() {
        if (player1turn) {
            return player2;
        } else {
            return player1;
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player opponentOf(Player player) {
        if (player1 == player) {
            return player2;
        } else {
            return player1;
        }
    }

    public GameStatus getStatus() {
        return status;
    }

    public GameResultType getResult() {
        return result;
    }

    public boolean isWinner(Player player) {
        if (player1 == player) {
            return result == GameResultType.PLAYER1_WON || result == GameResultType.PLAYER2_SURRENDERED;
        } else {
            return result == GameResultType.PLAYER2_WON || result == GameResultType.PLAYER1_SURRENDERED;
        }
    }
}

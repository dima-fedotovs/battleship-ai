package online.macities.battleship.model;

import java.util.*;
import java.util.function.Consumer;

public class Player {
    public static final String ATTR = "player";
    private static final List<String> COLS = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
    private static final List<String> ROWS = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    private final String name;
    private final Map<String, CellStatus> playerField = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, CellStatus> playerFieldReadOnly = Collections.unmodifiableMap(playerField);
    private final Map<String, CellStatus> opponentView = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, CellStatus> opponentViewReadOnly = Collections.unmodifiableMap(opponentView);
    private boolean playerFieldValid = false;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPlayerFieldCell(String addr, CellStatus status) {
        playerField.put(addr, status);
    }

    public CellStatus getPlayerFieldCell(String addr) {
        return playerField.getOrDefault(addr, CellStatus.EMPTY);
    }

    public void setOpponentViewCell(String addr, CellStatus status) {
        opponentView.put(addr, status);
    }

    public Map<String, CellStatus> getPlayerField() {
        return playerFieldReadOnly;
    }

    public Map<String, CellStatus> getOpponentView() {
        return opponentViewReadOnly;
    }

    public boolean isPlayerFieldValid() {
        return playerFieldValid;
    }

    public void setShips(Set<String> shipAddresses) {
        if (playerFieldValid) {
            throw new IllegalStateException("Ships already set");
        }
        playerField.clear();
        for (var a : shipAddresses) {
            playerField.put(a, CellStatus.SHIP);
        }
        var errors = new HashSet<String>();
        errors.addAll(addressesValid());
        errors.addAll(cornersValid());
        errors.addAll(sizesValid());
        playerFieldValid = errors.isEmpty() && playerField.size() == 20;
        for (var addr : errors) {
            playerField.put(addr, CellStatus.HIT);
        }
    }

    public boolean validateIncompleteShips(Set<String> shipAddresses) {
        if (playerFieldValid) {
            throw new IllegalStateException("Ships already set");
        }
        playerField.clear();
        for (var a : shipAddresses) {
            playerField.put(a, CellStatus.SHIP);
        }
        return addressesValid().isEmpty() && cornersValid().isEmpty() && sizesValid().isEmpty();
    }

    private Set<String> addressesValid() {
        var errors = new HashSet<>(playerField.keySet());
        for (var col : COLS) {
            for (var row : ROWS) {
                var addr = col + row;
                errors.remove(addr);
            }
        }
        return errors;
    }

    private boolean doIfShip(int colIdx, int rowIdx, Consumer<String> action) {
        if (colIdx < 0 || colIdx >= COLS.size() || rowIdx < 0 || rowIdx >= ROWS.size()) {
            return false;
        }
        var col = COLS.get(colIdx);
        var row = ROWS.get(rowIdx);
        var addr = col + row;
        var result = playerField.get(addr) == CellStatus.SHIP;
        if (result) {
            action.accept(addr);
        }
        return result;
    }

    private Set<String> cornersValid() {
        var errors = new HashSet<String>();
        for (int colIdx = 0; colIdx < COLS.size(); colIdx++) {
            for (int rowIdx = 0; rowIdx < ROWS.size(); rowIdx++) {
                final var c = colIdx;
                final var r = rowIdx;
                doIfShip(c, r, addr -> {
                    doIfShip(c - 1, r - 1, errors::add);
                    doIfShip(c + 1, r - 1, errors::add);
                    doIfShip(c - 1, r + 1, errors::add);
                    doIfShip(c + 1, r + 1, errors::add);
                });
            }
        }
        return errors;
    }

    private Set<String> sizesValid() {
        var errors = new HashSet<String>();
        var requiredSizes = new ArrayList<>(List.of(4, 3, 3, 2, 2, 2, 1, 1, 1, 1));
        var checkedCells = new HashSet<String>();
        for (int colIdx = 0; colIdx < COLS.size(); colIdx++) {
            for (int rowIdx = 0; rowIdx < ROWS.size(); rowIdx++) {
                var addr = COLS.get(colIdx) + ROWS.get(rowIdx);
                if (checkedCells.contains(addr)) {
                    continue;
                }
                var isShip = playerField.get(addr) == CellStatus.SHIP;
                if (isShip) {
                    var connected = getConnectedShips(colIdx, rowIdx);
                    checkedCells.addAll(connected);
                    var found = requiredSizes.remove((Object) connected.size());
                    if (!found) {
                        errors.addAll(connected);
                    }
                }
            }
        }
        return errors;
    }

    private List<String> getConnectedShips(int colStartIdx, int rowStartIdx) {
        var horiz = new ArrayList<String>();
        for (int colIdx = colStartIdx; colIdx < COLS.size(); colIdx++) {
            var addr = COLS.get(colIdx) + ROWS.get(rowStartIdx);
            var isShip = playerField.get(addr) == CellStatus.SHIP;
            if (isShip) {
                horiz.add(addr);
            } else {
                break;
            }
        }
        var vert = new ArrayList<String>();
        for (int rowIdx = rowStartIdx; rowIdx < ROWS.size(); rowIdx++) {
            var addr = COLS.get(colStartIdx) + ROWS.get(rowIdx);
            var isShip = playerField.get(addr) == CellStatus.SHIP;
            if (isShip) {
                vert.add(addr);
            } else {
                break;
            }
        }
        return vert.size() > horiz.size() ? vert : horiz;
    }

    public boolean hasMoreShips() {
        return playerField.containsValue(CellStatus.SHIP);
    }
}

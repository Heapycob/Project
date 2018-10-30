package fall2018.csc207project.gamecenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class BoardManager implements Serializable {

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    BoardManager(Board board) {
        this.board = board;
    }

    /**
     * Manage a new shuffled board.
     */
    BoardManager() {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = Board.NUM_ROWS * Board.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }

        Collections.shuffle(tiles);
        this.board = new Board(tiles);
    }

    /**
     * Return the current board.
     */
    Board getBoard() {
        return board;
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        // the solution for whether the puzzle is solved.
        boolean solved = true;
        // default id.
        int previous = board.getTile(0, 0).getId() - 1;

        for (Tile tile : board) {
            solved = previous + 1 == tile.getId() && solved;
            previous = tile.getId();
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {

        int row = position / Board.NUM_COLS;
        int col = position % Board.NUM_COLS;
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == Board.NUM_ROWS - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == Board.NUM_COLS - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {
        // Which row for the current tapping Tile.
        int row = position / Board.NUM_ROWS;
        // Which column for the current tapping Tile.
        int col = position % Board.NUM_COLS;
        // The blank id for the blank Tile.
        int blankId = board.numTiles();

        if (isValidTap(position)) {
            // Determine whether the row or column can reach one more position.
            int rMax, cMax;
            rMax = row == Board.NUM_ROWS - 1 ? 1 : 2;
            cMax = col == Board.NUM_COLS - 1 ? 1 : 2;
            for (int r = row == 0 ? 0 : -1; r < rMax; r++)
                for (int c = col == 0 ? 0 : -1; c < cMax; c++)
                    if (r - c != 0 && r + c != 0
                            && board.getTile(row + r, col + c).getId() == blankId)
                        board.swapTiles(row, col, row + r, col + c);
        }
    }
}
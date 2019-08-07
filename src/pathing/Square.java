package pathing;

import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;

// Models a single square in the grid/board
class Square extends Rectangle {
    private double distFromPrev;
    private Square previous;
    private HashSet<Square> neighbors;

    Square(int x, int y, int width, int height) {
        super(x, y, width, height);
        super.setFill(Color.web("#CCD1D1"));

        // Initialize tentative distance to a high number for the pathfinding algorithm to work
        this.distFromPrev = 2147483645;
    }

    void setPrevious(Square s) {
        this.previous = s;
    }

    Square getPrevious() {
        return this.previous;
    }

    void setDistFromPrev(double distance) {
        this.distFromPrev = distance;
    }

    double getDistFromPrev() {
        return distFromPrev;
    }

    HashSet<Square> getNeighbors() {
        return neighbors;
    }

    // Fill the square's list of neighbors
    void fillNeighbors(Square[][] values, boolean diagonalActive) {
        neighbors = new HashSet<>();
        int x = (int) super.getX();
        int y = (int) super.getY();

        // Diagonal movement allowed, add diagonal neighbors
        if (diagonalActive) {
            // Not against the left wall
            if (x > 0) {
                // Not against the top wall
                if (y > 0) {
                    addTopLeftSquare(values, x, y);
                    addTopSquare(values, x, y);
                }
                // Not against the bottom wall
                if (y < 26) {
                    addBottomLeftSquare(values, x, y);
                }
                addLeftSquare(values, x, y);
            }
            // Not against the right wall
            if (x < 44) {
                // Not against the bottom wall
                if (y < 26) {
                    addBottomRightSquare(values, x, y);
                    addBottomSquare(values, x, y);
                }
                // Not against the top wall
                if (y > 0) {
                    addTopRightSquare(values, x, y);
                }
                addRightSquare(values, x, y);
            }
        }
        // Only add horizontal/vertical neighbors
        else {
            if (x > 0) {
                addLeftSquare(values, x, y);
            }
            if (x < 44) {
                addRightSquare(values, x, y);
            }
            if (y > 0) {
                addTopSquare(values, x, y);
            }
            if (y < 26) {
                addBottomSquare(values, x, y);
            }
        }
    }

    private void addLeftSquare(Square[][] values, int x, int y) {
        Square left = values[y][x - 1];
        neighbors.add(left);
    }

    private void addTopSquare(Square[][] values, int x, int y) {
        Square top = values[y - 1][x];
        neighbors.add(top);
    }

    private void addRightSquare(Square[][] values, int x, int y) {
        Square right = values[y][x + 1];
        neighbors.add(right);
    }

    private void addBottomSquare(Square[][] values, int x, int y) {
        Square bottom = values[y + 1][x];
        neighbors.add(bottom);
    }

    private void addTopLeftSquare(Square[][] values, int x, int y) {
        Square topLeft = values[y - 1][x - 1];
        neighbors.add(topLeft);
    }

    private void addBottomLeftSquare(Square[][] values, int x, int y) {
        Square bottomLeft = values[y + 1][x - 1];
        neighbors.add(bottomLeft);
    }

    private void addTopRightSquare(Square[][] values, int x, int y) {
        Square topRight = values[y - 1][x + 1];
        neighbors.add(topRight);
    }

    private void addBottomRightSquare(Square[][] values, int x, int y) {
        Square bottomRight = values[y + 1][x + 1];
        neighbors.add(bottomRight);
    }

    // Set behavior for filling the square
    void addAction(HashMap<Integer, Integer> coordinates, Board b) {
        // On click
        super.setOnMouseClicked(e -> fillSquare(coordinates, b));
        // On key press / hover
        super.setOnMouseEntered(e -> {
                // Only allow mass coloring if setting obstacle
                if (b.getMode() == Mode.SETTING_OBSTACLE && b.getPressedKeys().contains(KeyCode.G)) {
                    fillSquare(coordinates, b);
                }
        });
    }

    // Records coordinates and colors the squares depending on mode
    private void fillSquare(HashMap<Integer, Integer> coordinates, Board b) {
        Mode mode = b.getMode();

        // Square placed in the current mode
        if (Square.super.getFill().equals(mode.getColor())) {
            reset(coordinates);
        }
        // Starting/Ending point already placed
        else if ((mode == Mode.SETTING_START || mode == Mode.SETTING_END) && coordinates.size() == 1) {
            int x = (int) coordinates.keySet().toArray()[0];
            int y = (int) coordinates.values().toArray()[0];

            // Reset the existing point
            Square s = b.getValues()[y][x];
            s.reset(coordinates);

            // Add the new point
            fillDefault(coordinates, mode);
        }
        // Empty square
        else if (Square.super.getFill().equals(Color.web("#CCD1D1"))) {
            fillDefault(coordinates, mode);
        }
    }

    // Fill empty square
    private void fillDefault(HashMap<Integer, Integer> coordinates, Mode mode) {
        Square.super.setFill(mode.getColor());
        // Insert coordinates of starting/ending point if applicable
        if (coordinates != null) {
            coordinates.put((int) Square.super.getX(), (int) Square.super.getY());
        }
    }

    // Reset square to empty
    private void reset(HashMap<Integer, Integer> coordinates) {
        // Fill with gray color
        super.setFill(Color.web("#CCD1D1"));
        // Remove coordinates
        if (coordinates != null) {
            coordinates.remove((int) Square.super.getX());
        }
    }



}

package pathing;

import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;

public class Square extends Rectangle {
    private double distFromPrev;
    private Square previous;
    private HashSet<Square> neighbors;

    public Square(int x, int y, int width, int height) {
        super(x, y, width, height);
        super.setFill(Color.web("#CCD1D1"));
        this.distFromPrev = 1000000;
    }

    public void setPrevious(Square s) {
        this.previous = s;
    }

    public Square getPrevious() {
        return this.previous;
    }

    public void setDistFromPrev(double distance) {
        this.distFromPrev = distance;
    }

    public double getDistFromPrev() {
        return distFromPrev;
    }

    public HashSet<Square> getNeighbors() {
        return neighbors;
    }

    public void fillNeighbors(Square[][] values) {
        neighbors = new HashSet<>();

        if (super.getX() > 0) {
            if (super.getY() > 0) {
                Square top = values[(int) super.getY() - 1][(int) super.getX()];
                Square topLeft = values[(int) super.getY() - 1][(int) super.getX() - 1];
                neighbors.add(top);
                neighbors.add(topLeft);
            }
            if (super.getY() < 26) {
                Square bottomLeft = values[(int) super.getY() + 1][(int) super.getX() - 1];
                neighbors.add(bottomLeft);
            }
            Square left = values[(int) super.getY()][(int) super.getX() - 1];
            neighbors.add(left);
        }
        if (super.getX() < 44) {
            if (super.getY() < 26) {
                Square bottom = values[(int) super.getY() + 1][(int) super.getX()];
                Square bottomRight = values[(int) super.getY() + 1][(int) super.getX() + 1];
                neighbors.add(bottom);
                neighbors.add(bottomRight);
            }
            if (super.getY() > 0) {
                Square topRight = values[(int) super.getY() - 1][(int) super.getX() + 1];
                neighbors.add(topRight);
            }
            Square right = values[(int) super.getY()][(int) super.getX() + 1];
            neighbors.add(right);
        }
    }

    public void addAction(HashMap<Integer, Integer> coordinates, Board b) {
        super.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Mode mode = b.getMode();

                if (Square.super.getFill().equals(mode.getColor())) {
                    reset(coordinates);
                }
                else if ((mode == Mode.SETTING_START || mode == Mode.SETTING_END) && coordinates.size() == 1) {
                    int x = (int) coordinates.keySet().toArray()[0];
                    int y = (int) coordinates.values().toArray()[0];

                    Square s = b.getValues()[y][x];
                    s.reset(coordinates);

                    fillDefault(coordinates, mode);
                }
                else if (Square.super.getFill().equals(Color.web("#CCD1D1"))) {
                    fillDefault(coordinates, mode);
                }
            }
        });
    }

    private void fillDefault(HashMap<Integer, Integer> coordinates, Mode mode) {
        Square.super.setFill(mode.getColor());
        if (coordinates != null) {
            coordinates.put((int) Square.super.getX(), (int) Square.super.getY());
        }
    }

    private void reset(HashMap<Integer, Integer> coordinates) {
        super.setFill(Color.web("#CCD1D1"));
        if (coordinates != null) {
            coordinates.remove((int) Square.super.getX());
        }
    }



}

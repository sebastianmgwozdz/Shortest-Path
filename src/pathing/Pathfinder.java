package pathing;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

import javafx.util.Duration;

import java.util.*;

public class Pathfinder {
    private Square[][] board;

    public Pathfinder(Board board) {
        this.board = board.getValues();

    }

    public void run(Square start, Square end) {
        LinkedHashSet<Square> path = new LinkedHashSet<>();
        HashSet<Square> notVisited = new HashSet<>();
        HashSet<Square> visited = new HashSet<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                notVisited.add(board[i][j]);
            }
        }

        Square current = board[(int) start.getY()][(int) start.getX()];
        current.setDistFromPrev(0);
        current.setPrevious(null);

        while (true) {
            notVisited.remove(current);
            visited.add(current);

            if (!notVisited.contains(end)) {
                break;
            }

            for (Square neighbor : current.getNeighbors()) {
                if (current.getDistFromPrev() + getCost(neighbor, end) < neighbor.getDistFromPrev() && notVisited.contains(neighbor)) {
                    neighbor.setDistFromPrev(current.getDistFromPrev() + getCost(neighbor, end));
                    neighbor.setPrevious(current);
                }
                if (!neighbor.getFill().equals(Mode.SETTING_OBSTACLE.getColor()) || neighbor.equals(end)) {
                    path.add(neighbor);
                }
            }

            current = getNext(current, notVisited, visited);
        }

        draw(end, path);
    }

    private double getCost(Square neighbor, Square goal) {
        double xDiff = Math.abs(goal.getX() - neighbor.getX());
        double yDiff = Math.abs(goal.getY()- neighbor.getY());
        double distanceToEnd = xDiff * xDiff + yDiff * yDiff;
        return distanceToEnd;
    }

    private Square getNext(Square current, HashSet<Square> notVisited, HashSet<Square> visited) {
        Square optimal = new Square(0, 0, 0, 0);
        optimal.setDistFromPrev(Integer.MAX_VALUE);

        for (Square s : current.getNeighbors()) {
            if (s.getDistFromPrev() < optimal.getDistFromPrev() && notVisited.contains(s) && !s.getFill().equals(Mode.SETTING_OBSTACLE.getColor())) {
                optimal = s;
            }
        }

        if (optimal.getDistFromPrev() == Integer.MAX_VALUE) {
            for (Square s : visited) {
                for (Square neighbor : s.getNeighbors()) {
                    if (neighbor.getDistFromPrev() < optimal.getDistFromPrev() && notVisited.contains(neighbor) && !neighbor.getFill().equals(Mode.SETTING_OBSTACLE.getColor())) {
                        optimal = neighbor;
                    }
                }
            }
        }

        return optimal;
    }

    private void draw(Square destination, LinkedHashSet<Square> path) {
        Timeline timeline = new Timeline();
        Duration timepoint = Duration.ZERO;
        Duration pause = Duration.seconds(0.005);

        for (Square s : path) {
            timepoint = timepoint.add(pause);
            if (!s.getFill().equals(Mode.SETTING_START.getColor()) && !s.getFill().equals(Mode.SETTING_END.getColor())) {
                KeyFrame keyframe = new KeyFrame(timepoint, (ActionEvent event) -> {s.setFill(Color.web("#C0392B"));});
                timeline.getKeyFrames().add(keyframe);
            }
        }

        ArrayList<Square> shortestPath = getShortestPath(destination);
        for (Square s : shortestPath) {
            timepoint = timepoint.add(pause);
            if (!s.getFill().equals(Mode.SETTING_START.getColor()) && !s.getFill().equals(Mode.SETTING_END.getColor())) {
                KeyFrame keyframe = new KeyFrame(timepoint, (ActionEvent event) -> {
                    s.setFill(Color.web("#82E0AA"));
                });
                timeline.getKeyFrames().add(keyframe);
            }
        }

        timeline.play();
    }

    private ArrayList<Square> getShortestPath(Square destination) {
        LinkedHashSet<Square> reversePath = new LinkedHashSet<>();

        Square copy = destination;
        while (copy.getPrevious() != null) {
            reversePath.add(copy);
            copy = copy.getPrevious();
        }
        reversePath.add(copy);

        ArrayList<Square> arr = new ArrayList<>(reversePath);
        Collections.reverse(arr);

        return arr;
    }
}

package pathing;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

import javafx.util.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;

public class Pathfinder {
    private Square[][] board;

    public Pathfinder(Board board) {
        this.board = board.getValues();

    }

    public LinkedHashSet<Square> getAllSquaresVisited(HashMap<Integer, Integer> start, HashMap<Integer, Integer> obstacle,
                                                 HashMap<Integer, Integer> end) {
        LinkedHashSet<Square> path = new LinkedHashSet<>();
        HashSet<Square> notVisited = new HashSet<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                notVisited.add(board[i][j]);
            }
        }
        Square destination = board[(int) end.values().toArray()[0]][(int) end.keySet().toArray()[0]];;

        Square current = board[(int) start.values().toArray()[0]][(int) start.keySet().toArray()[0]];
        current.setDistFromPrev(0);

        while (true) {
            path.add(current);

            notVisited.remove(current);

            if (!notVisited.contains(destination)) {
                break;
            }

            for (Square neighbor : current.getNeighbors()) {
                double cost = getCost(neighbor, destination);
                neighbor.setDistFromPrev(current.getDistFromPrev() + cost);
                if (!neighbor.getFill().equals(Color.web("#6C3483"))) {
                    path.add(neighbor);
                }
            }

            Square optimal = new Square(0, 0, 0, 0);
            optimal.setDistFromPrev(Double.MAX_VALUE);

            for (Square s : current.getNeighbors()) {
                if (s.getDistFromPrev() < optimal.getDistFromPrev() && notVisited.contains(s) && !s.getFill().equals(Color.web("#6C3483"))) {
                    optimal = s;
                }
            }


            current = optimal;
        }

        Timeline timeline = new Timeline();
        Duration timepoint = Duration.ZERO;
        Duration pause = Duration.seconds(0.1);

        for (Square s : path) {
            timepoint = timepoint.add(pause);
            KeyFrame keyframe = new KeyFrame(timepoint, (ActionEvent event) -> {s.setFill(Color.RED);});
            timeline.getKeyFrames().add(keyframe);
        }
        timeline.play();

        return path;
    }

    private double getCost(Square neighbor, Square goal) {
        double xDiff = Math.abs(goal.getX() - neighbor.getX());
        double yDiff = Math.abs(goal.getY()- neighbor.getY());
        double distanceToEnd = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        return distanceToEnd;
    }
}

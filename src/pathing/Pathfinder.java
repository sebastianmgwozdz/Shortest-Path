package pathing;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

import javafx.util.Duration;

import java.util.*;

// Used to find the shortest path and draw it
class Pathfinder {
    private Square[][] board;

    Pathfinder(Board board) {
        this.board = board.getValues();

    }

    // Main algorithm (Dijkstra / A*), finds shortest distance from starting point to every node it checks
    void run(Square start, Square end) {
        LinkedHashSet<Square> path = new LinkedHashSet<>();
        HashSet<Square> notVisited = new HashSet<>();
        HashSet<Square> visited = new HashSet<>();

        // Add each square to notVisited set
        for (Square[] squares : board) {
            notVisited.addAll(Arrays.asList(squares).subList(0, board[0].length));
        }

        // Begin with starting point, tentative distance = 0
        Square current = board[(int) start.getY()][(int) start.getX()];
        current.setDistFromPrev(0);
        current.setPrevious(null);

        // Main loop, continues until end point is deemed unreachable or end point is reached
        while (true) {
            notVisited.remove(current);
            visited.add(current);

            // Check if destination has been reached
            if (!notVisited.contains(end)) {
                break;
            }

            // Check if destination cannot be reached
            if (current.getNeighbors() == null) {
                return;
            }

            // Update tentative distances of all the current node's neighbors
            for (Square neighbor : current.getNeighbors()) {
                // Only update if the new distance is less than the old
                if (current.getDistFromPrev() + getCost(neighbor, end) < neighbor.getDistFromPrev() && notVisited.contains(neighbor)) {
                    neighbor.setDistFromPrev(current.getDistFromPrev() + getCost(neighbor, end));
                    // Update the shortest path
                    neighbor.setPrevious(current);
                }
                // Add the checked nodes to the set
                if (!neighbor.getFill().equals(Mode.SETTING_OBSTACLE.getColor()) || neighbor.equals(end)) {
                    path.add(neighbor);
                }
            }

            current = getNext(current, notVisited, visited);
        }

        draw(end, path);
    }

    // Calculates the cost of the node, determined by the euclidean distance between the given node and the destination
    private double getCost(Square neighbor, Square goal) {
        double xDiff = Math.abs(goal.getX() - neighbor.getX());
        double yDiff = Math.abs(goal.getY()- neighbor.getY());
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    // Returns the unvisited neighboring square with the smallest tentative distance (After recalculating)
    private Square getNext(Square current, HashSet<Square> notVisited, HashSet<Square> visited) {
        Square optimal = new Square(0, 0, 0, 0);
        optimal.setDistFromPrev(Integer.MAX_VALUE);

        for (Square s : current.getNeighbors()) {
            if (s.getDistFromPrev() < optimal.getDistFromPrev() && notVisited.contains(s) && !s.getFill().equals(Mode.SETTING_OBSTACLE.getColor())) {
                optimal = s;
            }
        }

        // In case there is no way out of the current path, pick the unvisited neighboring node closest to the destination
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

    // Draw the squares visited by the algorithm, and also the shortest path
    private void draw(Square destination, LinkedHashSet<Square> path) {
        Timeline timeline = new Timeline();
        Duration timepoint = Duration.ZERO;
        // Delay between the coloring of each block
        Duration pause = Duration.seconds(0.005);

        // Color every block that was visited
        for (Square s : path) {
            timepoint = timepoint.add(pause);
            if (!s.getFill().equals(Mode.SETTING_START.getColor()) && !s.getFill().equals(Mode.SETTING_END.getColor())) {
                KeyFrame keyframe = new KeyFrame(timepoint, (ActionEvent event) -> s.setFill(Color.web("#C0392B")));
                timeline.getKeyFrames().add(keyframe);
            }
        }

        // Color every block found in the shortest path
        ArrayList<Square> shortestPath = getShortestPath(destination);
        for (Square s : shortestPath) {
            timepoint = timepoint.add(pause);
            if (!s.getFill().equals(Mode.SETTING_START.getColor()) && !s.getFill().equals(Mode.SETTING_END.getColor())) {
                KeyFrame keyframe = new KeyFrame(timepoint, e -> s.setFill(Color.web("#82E0AA")));
                timeline.getKeyFrames().add(keyframe);
            }
        }

        timeline.play();
    }

    // Returns a list with all squares located along the shortest path
    private ArrayList<Square> getShortestPath(Square destination) {
        LinkedHashSet<Square> reversePath = new LinkedHashSet<>();

        // Starting from the end node, go backwards and add each node to the list
        Square copy = destination;
        while (copy.getPrevious() != null) {
            reversePath.add(copy);
            copy = copy.getPrevious();
        }
        reversePath.add(copy);

        // Flip the list to get the right order
        ArrayList<Square> arr = new ArrayList<>(reversePath);
        Collections.reverse(arr);

        return arr;
    }
}

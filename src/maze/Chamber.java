package maze;

import java.awt.Color;
import rendering.WallArt;
public class Chamber {
    private Color wallColor;
    private boolean visited = false;
    private Chamber[] adjacentChambers;
    private WallArt wallArt;
    private Coordinate coordinates;

    private boolean lastDoor = false;

    public boolean isLastDoor() {
        return lastDoor;
    }

    public void setLastDoor(boolean lastDoor) {
        this.lastDoor = lastDoor;
    }

    public Chamber() {
        adjacentChambers = new Chamber[6];
    }
    public Color getWallColor() {
        return wallColor;
    }
    public void setWallColor(Color c) {
        wallColor = c;
    }

    public WallArt getWallArt() {
        return wallArt;
    }

    public void setWallArt(WallArt art) {
        this.wallArt = art;
    }

    public boolean getVisited() {
        return visited;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public Chamber getAdjacentChamber(int index) {
        return adjacentChambers[index];
    }
    public void setAdjacentChamber(int index, Chamber c) {
        adjacentChambers[index] = c;
    }
    public Chamber[] getChambers() {
        return adjacentChambers;
    }
    public void setChambers (Chamber[] chambers) {
        adjacentChambers = chambers;
    }
    public Coordinate getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(Coordinate c) {
        coordinates = c;
    }
}

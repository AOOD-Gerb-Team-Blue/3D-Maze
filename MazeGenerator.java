package maze;

import java.util.*;
import utils.Direction;
import rendering.ColorUtils;
import rendering.ImageWallArt;
public class MazeGenerator {
    private Maze generatedMaze;
    private boolean[][][] visited;
    private int unvisited;
    private char[][][] walk;
    private ArrayList<ArrayList<ArrayList<ArrayList<Coordinate>>>> connections;
    private ArrayList<Character> directions;
    private Random rand;
    public MazeGenerator(String difficulty){
        generatedMaze = new Maze(difficulty);
        connections = new ArrayList<>();
        directions = new ArrayList<Character>();
        directions.add('N');
        directions.add('E');
        directions.add('S');
        directions.add('W');
        directions.add('T');
        directions.add('B');
        rand = new Random();
        if (difficulty.equals("easy")){
            visited = new boolean[4][4][4];
            for (int i = 0; i < 4; i++){
                connections.add(new ArrayList<ArrayList<ArrayList<Coordinate>>>());
                for (int j = 0; j < 4; j++){
                    connections.get(i).add(new ArrayList<ArrayList<Coordinate>>());
                    for (int k = 0; k < 4; k++){
                        connections.get(i).get(j).add(new ArrayList<Coordinate>());
                    }
                }
            }
            easy();
        }
        else if (difficulty.equals("medium")){
            unvisited = 124;
            visited = new boolean[5][5][5];
            walk = new char[5][5][5];
            for (int i = 0; i < 5; i++){
                connections.add(new ArrayList<ArrayList<ArrayList<Coordinate>>>());
                for (int j = 0; j < 5; j++){
                    connections.get(i).add(new ArrayList<ArrayList<Coordinate>>());
                    for (int k = 0; k < 5; k++){
                        connections.get(i).get(j).add(new ArrayList<Coordinate>());
                    }
                }
            }
            visited[4][4][4] = true;
            medium();
        }
        else{
            visited = new boolean[6][6][6];
            for (int i = 0; i < 6; i++){
                connections.add(new ArrayList<ArrayList<ArrayList<Coordinate>>>());
                for (int j = 0; j < 6; j++){
                    connections.get(i).add(new ArrayList<ArrayList<Coordinate>>());
                    for (int k = 0; k < 6; k++){
                        connections.get(i).get(j).add(new ArrayList<Coordinate>());
                    }
                }
            }
            hard(new Coordinate(0, 0, 0));
        }
    }
    public void easy(){
        int[][][] color = new int[4][4][4];
        int currColor = 1;
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                for (int c = 0; c < 4; c++) {
                    color[i][k][c] = currColor;
                    currColor++;
                }
            }
        }
        ArrayList<Coordinate[]> edges = new ArrayList<Coordinate[]>();
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                for (int c = 0; c < 4; c++) {
                    //try each of six neighbors
                    int[][] offsets = {{1,0,0},{-1,0,0},
                            {0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};
                    for (int[] offset : offsets) {
                        int z = i + offset[0];
                        int y = k + offset[1];
                        int x = c + offset[2];
                        boolean zGood = z <= 3 && z >= 0;
                        boolean yGood = y <= 3 && y >= 0;
                        boolean xGood = x <= 3 && x >= 0;
                        if (zGood && yGood && xGood) {
                            //fill out second coordinate in edge
                            Coordinate[] edge = new Coordinate[2];
                            edge[0] = new Coordinate(i,k,c);
                            edge[1] = new Coordinate(z,y,x);
                            boolean contained = false;
                            for (Coordinate[] other : edges) {
                                if (other[0] == edge[0] && other[1] == edge[1]) {
                                    contained = true;
                                }
                            }
                            if (!contained) {
                                edges.add(edge);
                            }
                        }
                    }
                }
            }
        }
        Collections.shuffle(edges);
        boolean endFound = false;
        while (edges.size() > 0) {
            Coordinate[] edge = edges.get(0);
            edges.remove(0);
            int z1,y1,x1;
            int z2,y2,x2;
            z1 = edge[0].getLevel();
            y1 = edge[0].getRow();
            x1 = edge[0].getColumn();
            z2 = edge[1].getLevel();
            y2 = edge[1].getRow();
            x2 = edge[1].getColumn();
            boolean endContained = (z1==3 && y1==3 && x1==3) || (z2==3 && y2==3 && x2==3);
            if (color[z1][y1][x1] != color[z2][y2][x2] && !(endFound && endContained)) {
                if (endContained) {
                    endFound = true;
                }
                connections.get(z1).get(y1).get(x1).add(new Coordinate(z2,y2,x2));
                connections.get(z2).get(y2).get(x2).add(new Coordinate(z1,y1,x1));
                int oldColor = color[z1][y1][x1];
                int newColor = color[z2][y2][x2];
                for (int i = 0; i < 4; i++) {
                    for (int k = 0; k < 4; k++) {
                        for (int c = 0; c < 4; c++) {
                            if (color[i][k][c] == oldColor) {
                                color[i][k][c] = newColor;
                            }
                        }
                    }
                }
            }
        }
    }
    public void medium(){
        Coordinate start;
        while (unvisited > 0){
            do{
                start = new Coordinate(rand.nextInt(5), rand.nextInt(5), rand.nextInt(5));
            }
            while (visited[start.getLevel()][start.getRow()][start.getColumn()]);
            walk(start);
            while (!visited[start.getLevel()][start.getRow()][start.getColumn()]){
                visited[start.getLevel()][start.getRow()][start.getColumn()] = true;
                addConnections(start, walk[start.getLevel()][start.getRow()][start.getColumn()]);
                if (walk[start.getLevel()][start.getRow()][start.getColumn()] == 'N'){
                    start = new Coordinate(start.getLevel(), start.getRow() - 1, start.getColumn());
                }
                else if (walk[start.getLevel()][start.getRow()][start.getColumn()] == 'E'){
                    start = new Coordinate(start.getLevel(), start.getRow(), start.getColumn() + 1);
                }
                else if (walk[start.getLevel()][start.getRow()][start.getColumn()] == 'S'){
                    start = new Coordinate(start.getLevel(), start.getRow() + 1, start.getColumn());
                }
                else if (walk[start.getLevel()][start.getRow()][start.getColumn()] == 'W'){
                    start = new Coordinate(start.getLevel(), start.getRow(), start.getColumn() - 1);
                }
                else if (walk[start.getLevel()][start.getRow()][start.getColumn()] == 'T'){
                    start = new Coordinate(start.getLevel() - 1, start.getRow(), start.getColumn());
                }
                else{
                    start = new Coordinate(start.getLevel() + 1, start.getRow(), start.getColumn());
                }
                unvisited--;
            }
        }
    }
    private void walk(Coordinate coord){
        while (!visited[coord.getLevel()][coord.getRow()][coord.getColumn()]) {
            Collections.shuffle(directions);
            boolean found = false;
            for (int i = 0; i < 6; i++) {
                if (directions.get(i) == 'N') {
                    if (coord.getRow() - 1 >= 0) {
                        if (unvisited != 124 && coord.getLevel() == 4 && coord.getRow() - 1 == 4
                                && coord.getColumn() == 4) {
                            continue;
                        }
                        else{
                            walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'N';
                            coord = new Coordinate(coord.getLevel(), coord.getRow() - 1, coord.getColumn());
                            found = true;
                        }
                    }
                }
                if (directions.get(i) == 'E') {
                    if (coord.getColumn() + 1 < 5) {
                        if (unvisited != 124 && coord.getLevel() == 4 && coord.getRow() == 4
                                && coord.getColumn() + 1 == 4) {
                            continue;
                        }
                        else {
                            walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'E';
                            coord = new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn() + 1);
                            found = true;
                        }
                    }
                }
                if (directions.get(i) == 'S') {
                    if (coord.getRow() + 1 < 5) {
                        if (unvisited != 124 && coord.getLevel() == 4 && coord.getRow() + 1 == 4
                                && coord.getColumn() == 4) {
                            continue;
                        }
                        else {
                            walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'S';
                            coord = new Coordinate(coord.getLevel(), coord.getRow() + 1, coord.getColumn());
                            found = true;
                        }
                    }
                }
                if (directions.get(i) == 'W') {
                    if (coord.getColumn() - 1 >= 0) {
                        if (unvisited != 124 && coord.getLevel() == 4 && coord.getRow() == 4
                                && coord.getColumn() - 1 == 4) {
                            continue;
                        }
                        else {
                            walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'W';
                            coord = new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn() - 1);
                            found = true;
                        }
                    }
                }
                if (directions.get(i) == 'T') {
                    if (coord.getLevel() - 1 >= 0) {
                        if (unvisited != 124 && coord.getLevel() - 1 == 4 && coord.getRow() == 4
                                && coord.getColumn() == 4) {
                            continue;
                        }
                        else {
                            walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'T';
                            coord = new Coordinate(coord.getLevel() - 1, coord.getRow(), coord.getColumn());
                            found = true;
                        }
                    }
                }
                if (directions.get(i) == 'B') {
                    if (coord.getLevel() + 1 < 5) {
                        if (unvisited != 124 && coord.getLevel() + 1 == 4 && coord.getRow() == 4
                                && coord.getColumn() == 4) {
                            continue;
                        }
                        else {
                            walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'B';
                            coord = new Coordinate(coord.getLevel() + 1, coord.getRow(), coord.getColumn());
                            found = true;
                        }
                    }
                }
                if (found) {
                    break;
                }
            }
        }
    }
    public void hard(Coordinate coord){
        int[][][] color = new int[6][6][6];
        int currColor = 1;
        for (int i = 0; i < 6; i++) {
            for (int k = 0; k < 6; k++) {
                for (int c = 0; c < 6; c++) {
                    color[i][k][c] = currColor;
                    currColor++;
                }
            }
        }
        ArrayList<Coordinate[]> edges = new ArrayList<Coordinate[]>();
        for (int i = 0; i < 6; i++) {
            for (int k = 0; k < 6; k++) {
                for (int c = 0; c < 6; c++) {
                    //try each of six neighbors
                    int[][] offsets = {{1,0,0},{-1,0,0},
                            {0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};
                    for (int[] offset : offsets) {
                        int z = i + offset[0];
                        int y = k + offset[1];
                        int x = c + offset[2];
                        boolean zGood = z <= 5 && z >= 0;
                        boolean yGood = y <= 5 && y >= 0;
                        boolean xGood = x <= 5 && x >= 0;
                        if (zGood && yGood && xGood) {
                            //fill out second coordinate in edge
                            Coordinate[] edge = new Coordinate[2];
                            edge[0] = new Coordinate(i,k,c);
                            edge[1] = new Coordinate(z,y,x);
                            boolean contained = false;
                            for (Coordinate[] other : edges) {
                                if (other[0] == edge[0] && other[1] == edge[1]) {
                                    contained = true;
                                }
                            }
                            if (!contained) {
                                edges.add(edge);
                            }
                        }
                    }
                }
            }
        }
        Collections.shuffle(edges);
        boolean endFound = false;
        while (edges.size() > 0) {
            Coordinate[] edge = edges.get(0);
            edges.remove(0);
            int z1,y1,x1;
            int z2,y2,x2;
            z1 = edge[0].getLevel();
            y1 = edge[0].getRow();
            x1 = edge[0].getColumn();
            z2 = edge[1].getLevel();
            y2 = edge[1].getRow();
            x2 = edge[1].getColumn();
            boolean endContained = (z1==5 && y1==5 && x1==5) || (z2==5 && y2==5 && x2==5);
            if (color[z1][y1][x1] != color[z2][y2][x2] && !(endFound && endContained)) {
                if (endContained) {
                    endFound = true;
                }
                connections.get(z1).get(y1).get(x1).add(new Coordinate(z2,y2,x2));
                connections.get(z2).get(y2).get(x2).add(new Coordinate(z1,y1,x1));
                int oldColor = color[z1][y1][x1];
                int newColor = color[z2][y2][x2];
                for (int i = 0; i < 6; i++) {
                    for (int k = 0; k < 6; k++) {
                        for (int c = 0; c < 6; c++) {
                            if (color[i][k][c] == oldColor) {
                                color[i][k][c] = newColor;
                            }
                        }
                    }
                }
            }
        }
    }
    private void addConnections(Coordinate coord, char dir){
        if (dir == 'N'){
            connections.get(coord.getLevel()).get(coord.getRow()).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel(), coord.getRow() - 1, coord.getColumn())
            );
            connections.get(coord.getLevel()).get(coord.getRow() - 1).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn())
            );
        }
        if (dir == 'E'){
            connections.get(coord.getLevel()).get(coord.getRow()).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn() + 1)
            );
            connections.get(coord.getLevel()).get(coord.getRow()).get(coord.getColumn() + 1).add(
                    new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn())
            );
        }
        if (dir == 'S'){
            connections.get(coord.getLevel()).get(coord.getRow()).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel(), coord.getRow() + 1, coord.getColumn())
            );
            connections.get(coord.getLevel()).get(coord.getRow() + 1).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn())
            );
        }
        if (dir == 'W'){
            connections.get(coord.getLevel()).get(coord.getRow()).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn() - 1)
            );
            connections.get(coord.getLevel()).get(coord.getRow()).get(coord.getColumn() - 1).add(
                    new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn())
            );
        }
        if (dir == 'T'){
            connections.get(coord.getLevel()).get(coord.getRow()).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel() - 1, coord.getRow(), coord.getColumn())
            );
            connections.get(coord.getLevel() - 1).get(coord.getRow()).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn())
            );
        }
        if (dir == 'B'){
            connections.get(coord.getLevel()).get(coord.getRow()).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel() + 1, coord.getRow(), coord.getColumn())
            );
            connections.get(coord.getLevel() + 1).get(coord.getRow()).get(coord.getColumn()).add(
                    new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn())
            );
        }
    }
    public void toMaze(){
        int size = connections.size();
        //store all of the chambers in 1d array, access location with getCoord method
        Chamber[][][] chambers = new Chamber[size][size][size];
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                for (int c = 0; c < size; c++) {
                    Chamber toPlace = new Chamber();
                    //set coords of new chamber
                    toPlace.setCoordinates(new Coordinate(i,k,c));
                    //place chamber in 3d array
                    chambers[i][k][c] = toPlace;
                }
            }
        }
        //set adjacency data
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                for (int c = 0; c < size; c++) {
                    for (Coordinate other : connections.get(i).get(k).get(c)) {
                        int z = other.getLevel();
                        int y = other.getRow();
                        int x = other.getColumn();
                        //The stuff here down to the 2nd print is all just to make sure we're good
                        if (Math.abs(z-i) > 1 || Math.abs(y-k) > 1 || Math.abs(x-c) > 1) {
                            System.out.println("Uh oh, the difference between these is >1");
                        };
                        int diffCount = 0;
                        if (z != i) {
                            diffCount++;
                        }
                        if (y != k) {
                            diffCount++;
                        }
                        if (x != c) {
                            diffCount++;
                        }
                        if (diffCount >1) {
                            System.out.println("Uh oh, coordinates differ in >1 dimension");
                        }
                        //actual filling in data down here
                        int dir = -1;
                        if (z > i) {
                            dir = Direction.DOWN;
                        } else if (z < i) {
                            dir = Direction.UP;
                        } else if (y > k) {
                            dir = Direction.SOUTH;
                        } else if (y < k) {
                            dir = Direction.NORTH;
                        } else if (x > c) {
                            dir = Direction.EAST;
                        } else if (x < c) {
                            dir = Direction.WEST;
                        }
                        chambers[i][k][c].setAdjacentChamber(dir, chambers[z][y][x]);
                    }
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                for (int c = 0; c < size; c++) {
                	chambers[i][k][c].setWallColor(ColorUtils.randomChamberColor());
                	boolean hasArt = Math.random() > 0.5;
                	int artAmount = (int)(Math.random() * 2) +  1;
                	if (hasArt) {
                		chambers[i][k][c].setWallArt(ImageWallArt.generateWallArtFor(chambers[i][k][c], artAmount));
                	}
                    Coordinate loc = new Coordinate(i,k,c);
                    generatedMaze.setChamber(loc, chambers[i][k][c]);
                }
            }
        }
        //set root chamber, solution chamber, and moves
        generatedMaze.setRootChamber(generatedMaze.getChamberAt(new Coordinate(0,0,0)));
        generatedMaze.setSolutionChamber(generatedMaze.getChamberAt(new Coordinate(size-1,size-1,size-1)));
        OptimalSolver solver = new OptimalSolver(generatedMaze);
        generatedMaze.setMoves(solver.getMoves());
    }
    public Maze getMaze(){
        toMaze();
        return generatedMaze;
    }
}
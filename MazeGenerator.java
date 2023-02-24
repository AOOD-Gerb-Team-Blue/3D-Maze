import java.util.*;
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
                connections.add(new ArrayList<>());
                for (int j = 0; j < 4; j++){
                    connections.get(i).add(new ArrayList<>());
                    for (int k = 0; k < 4; k++){
                        connections.get(i).get(j).add(new ArrayList<>());
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
                connections.add(new ArrayList<>());
                for (int j = 0; j < 5; j++){
                    connections.get(i).add(new ArrayList<>());
                    for (int k = 0; k < 5; k++){
                        connections.get(i).get(j).add(new ArrayList<>());
                    }
                }
            }
            visited[2][2][2] = true;
            medium();
        }
        else{
            visited = new boolean[6][6][6];
            for (int i = 0; i < 6; i++){
                connections.add(new ArrayList<>());
                for (int j = 0; j < 6; j++){
                    connections.get(i).add(new ArrayList<>());
                    for (int k = 0; k < 6; k++){
                        connections.get(i).get(j).add(new ArrayList<>());
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
        for (Coordinate[] e : edges) {
        	int a,b,c;
        	a = e[0].getLevel();
        	b = e[0].getRow();
        	c = e[0].getColumn();
        	int x,y,z;
        	x = e[1].getLevel();
        	y = e[1].getRow();
        	z = e[1].getColumn();
        	//System.out.println(a + " " + b + " " + c + ", " + x + " " + y + " " + z);
        }
        Collections.shuffle(edges);
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
        	if (color[z1][y1][x1] != color[z2][y2][x2]) {
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
                start = connections.get(start.getLevel()).get(start.getRow()).get(start.getColumn()).
                        get(connections.size() - 1);
                unvisited--;
            }
        }
    }
    private void walk(Coordinate coord){
    	System.out.println(coord.getLevel()+ " " + coord.getRow() + " " + coord.getColumn());
        if (visited[coord.getLevel()][coord.getRow()][coord.getColumn()]){
            return;
        }
        Collections.shuffle(directions);
        boolean found = false;
        for (int i = 0; i < 6; i++){
            if (directions.get(i) == 'N'){
                if (coord.getRow() - 1 >= 0){
                    walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'N';
                    walk(new Coordinate(coord.getLevel(), coord.getRow() - 1, coord.getColumn()));
                    found = true;
                }
            }
            if (directions.get(i) == 'E'){
                if (coord.getColumn() + 1 < 5){
                    walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'E';
                    walk(new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn() + 1));
                    found = true;
                }
            }
            if (directions.get(i) == 'S'){
                if (coord.getRow() + 1 < 5){
                    walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'S';
                    walk(new Coordinate(coord.getLevel(), coord.getRow() + 1, coord.getColumn()));
                    found = true;
                }
            }
            if (directions.get(i) == 'W'){
                if (coord.getColumn() - 1 >= 0){
                    walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'W';
                    walk(new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn() - 1));
                    found = true;
                }
            }
            if (directions.get(i) == 'T'){
                if (coord.getLevel() - 1 >= 0){
                    walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'T';
                    walk(new Coordinate(coord.getLevel() - 1, coord.getRow(), coord.getColumn()));
                    found = true;
                }
            }
            if (directions.get(i) == 'B') {
                if (coord.getLevel() + 1 < 5) {
                    walk[coord.getLevel()][coord.getRow()][coord.getColumn()] = 'B';
                    walk(new Coordinate(coord.getLevel() + 1, coord.getRow(), coord.getColumn()));
                    found = true;
                }
            }
            if (found){
                return;
            }
        }
    }
    public void hard(Coordinate coord){
        visited[coord.getLevel()][coord.getRow()][coord.getColumn()] = true;
        Collections.shuffle(directions);
        for (int i = 0; i < 6; i++){
            if (directions.get(i) == 'N'){
                if (coord.getRow() - 1 >= 0 &&!visited[coord.getLevel()][coord.getRow() - 1][coord.getColumn()]){
                    addConnections(coord, 'N');
                    hard(new Coordinate(coord.getLevel(), coord.getRow() - 1, coord.getColumn()));
                }
            }
            if (directions.get(i) == 'E'){
                if (coord.getColumn() + 1 < 6 &&!visited[coord.getLevel()][coord.getRow()][coord.getColumn() + 1]){
                    addConnections(coord, 'E');
                    hard(new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn() + 1));
                }
            }
            if (directions.get(i) == 'S'){
                if (coord.getRow() + 1 < 6 &&!visited[coord.getLevel()][coord.getRow() + 1][coord.getColumn()]){
                    addConnections(coord, 'S');
                    hard(new Coordinate(coord.getLevel(), coord.getRow() + 1, coord.getColumn()));
                }
            }
            if (directions.get(i) == 'W'){
                if (coord.getColumn() - 1 >= 0 &&!visited[coord.getLevel()][coord.getRow()][coord.getColumn() - 1]){
                    addConnections(coord, 'W');
                    hard(new Coordinate(coord.getLevel(), coord.getRow(), coord.getColumn() - 1));
                }
            }
            if (directions.get(i) == 'T'){
                if (coord.getLevel() - 1 >= 0 &&!visited[coord.getLevel() - 1][coord.getRow()][coord.getColumn()]){
                    addConnections(coord, 'T');
                    hard(new Coordinate(coord.getLevel() - 1, coord.getRow(), coord.getColumn()));
                }
            }
            if (directions.get(i) == 'B'){
                if (coord.getLevel() + 1 < 6 && !visited[coord.getLevel() + 1][coord.getRow()][coord.getColumn()]){
                    addConnections(coord, 'B');
                    hard(new Coordinate(coord.getLevel() + 1, coord.getRow(), coord.getColumn()));
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
    	printConnections();
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
                    Coordinate loc = new Coordinate(i,k,c);
                    generatedMaze.setChamber(loc, chambers[i][k][c]);
                }
            }
        }
    }
    public void printConnections() {
     	for (int i = 0; i < 4; i++) {
    		for (int k = 0; k < 4; k++) {
    			for (int c = 0; c < 4; c++) {
    				String out = "";
    				out += "coord " + i + ", " + k + ", " + c + ":";
    				for (Coordinate co : connections.get(i).get(k).get(c)) {
    					String p = "{";
    					p += co.getLevel();
    					p += co.getRow();
    					p += co.getColumn();
    					p += "} ";
    					out += p;
    				}
    				System.out.println(out);
    			}
    		}
    	}   	
    }
    public Maze getMaze(){
        toMaze();
        return generatedMaze;
    }
}
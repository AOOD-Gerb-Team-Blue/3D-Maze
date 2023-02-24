
public class MazeSolver {
	private Maze toSolve;
	private static boolean[][][] visited;
	private static int size;
	public MazeSolver(String difficulty) {
		toSolve = new MazeGenerator(difficulty).getMaze();
		size = toSolve.getLevel(0).length;
		visited = new boolean[size][size][size];
	}
	private static boolean dfs(Chamber room) {
		Coordinate coord = room.getCoordinates();
		int z,y,x;
		z = coord.getLevel();
		y = coord.getRow();
		x = coord.getColumn();
		if (visited[z][y][x]) {
			return false;
		}
		visited[z][y][x] = true;
		System.out.print(z + ", " + y + ", " + x);
		System.out.println();
		boolean out = false;
		if (z == size-1 && y == size-1 && x == size-1) {
			out = true;
		}
		for (Chamber c : room.getChambers()) {
			if (c != null) {
				out = out || dfs(c);
			}
		}
		return out;
	}
	public static void main(String[] args) {
		MazeSolver solver = new MazeSolver("medium");
		if (dfs(solver.toSolve.getChamberAt(new Coordinate(0,0,0)))) {
			System.out.println("solved");
		} else {
			System.out.println("not solved");
		}
	}
}

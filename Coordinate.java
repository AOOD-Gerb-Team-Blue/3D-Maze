public class Coordinate {
    private int row;
    private int column;
    private int level;
    public Coordinate(int row, int column, int level) {
        this.row = row;
        this.column = column;
        this.level = level;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public int getLevel() {
        return level;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public void setColumn(int column) {
        this.column = column;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public boolean equals(Coordinate c) {
    	boolean out = true;
    	if (c.getRow() != row || c.getColumn() != column || c.getLevel() != level) {
    		out = false;
    	}
    	return out;
    }
}
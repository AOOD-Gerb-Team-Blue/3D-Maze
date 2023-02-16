public class BackendEngine {
    private ViewEngine viewEngine;
    private int currentMoves, direction;

    public BackendEngine(){
        viewEngine = new ViewEngine();
        currentMoves = 0;
    }
    public static void main(String[] args){
        BackendEngine backend = new BackendEngine();
    }

    public int getMoves(){
        return currentMoves;
    }

    public int getDirection(){
        return direction;
    }

    public void setDirection(int newDirection){
        direction = newDirection;
    }

    public void move(int direction){
        currentMoves += 1;
        setDirection(direction);
        
    }

    public void changeView(String newView){
        if(newView.equals("chamberview")){
            if(viewEngine.getGameView().equals("mainview")){
            } else if(viewEngine.getGameView().equals("mapview")){
            }
        } else if(newView.equals("mapview")){
            if(viewEngine.getGameView().equals("chamberview")){
            }
        } else if(newView.equals("endview")){
            if(viewEngine.getGameView().equals("chamberview")){
            }
        } else if(newView.equals("close")){
            if(viewEngine.getGameView().equals("endview")){
            }
        } 
    }
}

package data;

public enum FlyingCourse {
    DOWN, UP, LEFT, RIGHT;
    public FlyingCourse getReverse(){
        if(this == DOWN) return UP;
        else if(this == UP) return DOWN;
        else if(this == RIGHT) return LEFT;
        else return RIGHT;
    }
}
package bomberman.entities.character.enemy.ai;

public class PathFinding {
    double G; //khoang cach tu diem bat dau den dieu hien tai
    double H; //khoang cach tu o hien tai den diem dich
    double F; //gia tri G + H
    int direction;

    public PathFinding(double g, double h, double f, int direction) {
        G = g;
        H = h;
        F = f;
        this.direction = direction;
    }

    public double getF() {
        return F;
    }


    public int getDirection() {
        return direction;
    }

}

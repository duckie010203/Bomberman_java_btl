package bomberman.entities.character.enemy.ai;

import bomberman.Board;
import bomberman.entities.bomb.Bomb;
import bomberman.entities.character.Bomber;
import bomberman.entities.character.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

public class AIMedium2 extends AI{
    Bomber _bomber;
    Enemy _e;
    List<Bomb> bombs = new ArrayList<Bomb>();

    public AIMedium2(Board board, Enemy enemy) {
        _bomber = board.getBomber();
        _e = enemy;
        bombs = board.getBombs();
    }

    @Override
    public int calculateDirection() {


        return findDirection();
    }

    public int findDirection() {
        for (Bomb bomb : bombs) {
            if (bomb.getXTile() == _e.getXTile()) {
                if (_e.getYTile() - 1 == bomb.getYTile()) return 2;
                if (_e.getYTile() + 1 == bomb.getYTile()) return 0;
            }
            if (bomb.getYTile() == _e.getYTile()) {
                if (_e.getXTile() - 1 == bomb.getXTile()) return 1;
                if (_e.getXTile() + 1 == bomb.getXTile()) return 3;
            }
        }

        int way = random.nextInt(10);
        if (way % 2 == 0) {
            int _dir = dicrectionRow();
            if (_dir != - 1) {
                    return _dir;
            }
            return dicrectionCol();
        }else {
            int _dir = dicrectionCol();
            if (_dir != - 1) {
                    return _dir;
            }
            return dicrectionRow();
        }
    }

    public int dicrectionRow() {
        if (_bomber.getXTile() > _e.getXTile()) return 1;
        if (_bomber.getXTile() < _e.getXTile()) return 3;
        return -1;
    }
    public int dicrectionCol(){
        if (_bomber.getYTile() < _e.getYTile()) return 0;
        if (_bomber.getYTile() > _e.getYTile()) return 2;
        return -1;
    }
}

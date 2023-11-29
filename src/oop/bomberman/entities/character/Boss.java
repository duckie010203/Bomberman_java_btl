package oop.bomberman.entities.character;

import oop.bomberman.Board;
import oop.bomberman.CommonVariables;
import oop.bomberman.Game;
import oop.bomberman.entities.Entity;
import oop.bomberman.entities.bomb.Bomb;
import oop.bomberman.entities.bomb.DirectionalExplosion;
import oop.bomberman.entities.character.enemy.Enemy;
import oop.bomberman.graphics.Screen;
import oop.bomberman.graphics.Sprite;
import oop.bomberman.level.Coordinates;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Boss extends Character implements CommonVariables {

    private List<Bomb> _bombs;
    protected int finalAnimation = 30;
    protected int _timeBetweenPutBombs = 0;
    private static int _bombrate = 1;

    private static boolean isPlaceBomb = false;
    private static Bomb bB = null;

    private int [][] dire = {{1,0,1},{0,1,0},{-1,0,3},{0,-1,2}};
    private int [][] grid = new int[13][31];

    public Boss(int x, int y, Board board){
        super(x, y, board);
        _bombs = _board.getBombs();
        _sprite = Sprite.boss_right;
    }

    /*
    |--------------------------------------------------------------------------
    | Update & Render
    |--------------------------------------------------------------------------
     */
    @Override
    public void update() {
        clearBombs();
        if (_alive == false) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--; //don't let this get too big

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.boss_dead3;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    /*
    |--------------------------------------------------------------------------
    | Mob Unique
    |--------------------------------------------------------------------------
     */
    private void detectPlaceBomb() {
        if (isPlaceBomb && Game.getBombRate() > 0 && _timeBetweenPutBombs < 0) {

            int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
            int yt = Coordinates.pixelToTile((_y + _sprite.getSize() / 2) - _sprite.getSize()); //subtract half player height and minus 1 y position

            placeBomb(xt, yt);
            _bombrate = 0;

            _timeBetweenPutBombs = 30;
            isPlaceBomb = false;
        }
    }

    protected void placeBomb(int x, int y) {
        Bomb b = new Bomb(x, y, _board);
        Enemy.avoidBomb(x, y);
        _board.addBomb(b);
        bB = b;
        placeBombAudio.playSound(0);
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved() && bB == b) {
                bs.remove();
                bB = null;
                return;
            }
        }

    }

    private void createGrid(){
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 31; j++){
                if(matrix[i][j] == 0){

                }
            }
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Mob Colide & Kill
    |--------------------------------------------------------------------------
     */
    @Override
    public void kill() {
        if (!_alive) return;

        _alive = false;
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) {
            _timeAfter--;
        } else {
            if (finalAnimation > 0) {
                --finalAnimation;
            } else {
                remove();
            }
        }
    }

    /*
    |--------------------------------------------------------------------------
    | Mob Movement
    |--------------------------------------------------------------------------
     */
    @Override
    protected void calculateMove() {
        int xa = 0, ya = 0;
        if (xa != 0 || ya != 0) {
            move(xa * Game.getPlayerSpeed(), ya * Game.getPlayerSpeed());
            _moving = true;
        } else {
            _moving = false;
        }
    }

    @Override
    public boolean canMove(double x, double y) {
        for (int c = 0; c < 4; c++) {
            double xt = ((_x + x) + c % 2 * 13) / Game.TILES_SIZE;
            double yt = ((_y + y) + c / 2 * 12 - 13) / Game.TILES_SIZE;

            Entity a = _board.getEntity(xt, yt, this);
            if (!a.collide(this)) {
                return false;
            }

        }
        return true;
    }

    @Override
    public void move(double xa, double ya) {
        if (xa > 0) _direction = 1;
        if (xa < 0) _direction = 3;
        if (ya > 0) _direction = 2;
        if (ya < 0) _direction = 0;

        if (canMove(0, ya)) { //separate the moves for the player can slide when is colliding
            _y += ya;
        }

        if (canMove(xa, 0)) {
            _x += xa;
        }
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof DirectionalExplosion) {
            kill();
            return false;
        }
        return true;
    }


    /*
    |--------------------------------------------------------------------------
    | Mob Sprite
    |--------------------------------------------------------------------------
     */
    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.boss_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.boss_up_1, Sprite.boss_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.boss_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.boss_right_1, Sprite.boss_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.boss_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.boss_down_1, Sprite.boss_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.boss_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.boss_left_1, Sprite.boss_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.boss_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.boss_right_1, Sprite.boss_right_2, _animate, 20);
                }
                break;
        }
    }
}

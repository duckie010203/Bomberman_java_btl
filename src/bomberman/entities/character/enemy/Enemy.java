package bomberman.entities.character.enemy;

import bomberman.Board;
import bomberman.Game;
import bomberman.entities.Entity;
import bomberman.entities.LayeredEntity;
import bomberman.entities.Message;
import bomberman.entities.bomb.Bomb;
import bomberman.entities.bomb.Flame;
import bomberman.entities.character.Bomber;
import bomberman.entities.character.Character;
import bomberman.entities.character.enemy.ai.AI;
import bomberman.entities.character.enemy.ai.AILow;
import bomberman.graphics.Screen;
import bomberman.graphics.Sprite;
import bomberman.level.Coordinates;

import java.awt.*;

public abstract class Enemy extends Character {

	protected int _points;
	
	protected double _speed;
	protected AI _ai;

	protected final double MAX_STEPS;
	protected final double rest;
	protected double _steps;
	
	protected int _finalAnimation = 30;
	protected Sprite _deadSprite;
	
	public Enemy(int x, int y, Board board, Sprite dead, double speed, int points) {
		super(x, y, board);
		
		_points = points;
		_speed = speed;
		
		MAX_STEPS = Game.TILES_SIZE / _speed;
		rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
		_steps = MAX_STEPS;
		
		_timeAfter = 20;
		_deadSprite = dead;
	}


	public static void avoidBomb(int x, int y) {
		Game.matrix[y][x] = 0;
		// Up
		if (y - 1 >= 0 && Game.matrix[y - 1][x] == 1) {
			Game.matrix[y - 1][x] = -1;
		}
		// Right
		if (x + 1 < 13 && Game.matrix[y][x + 1] == 1) {
			Game.matrix[y][x + 1] = -1;
		}
		// Down
		if (y + 1 < 31 && Game.matrix[y + 1][x] == 1) {
			Game.matrix[y + 1][x] = -1;
		}
		// Left
		if (x - 1 >= 0 && Game.matrix[y][x - 1] == 1) {
			Game.matrix[y][x - 1] = -1;
		}
	}

	public static void resetAvoidBomb(int x, int y) {
		Game.matrix[y][x] = 1;

		// Up
		if (y - 1 >= 0 && Game.matrix[y - 1][x] == -1) {
			Game.matrix[y - 1][x] = 1;
		}
		// Right
		if (x + 1 < 31 && Game.matrix[y][x + 1] == -1) {
			Game.matrix[y][x + 1] = 1;
		}
		// Down
		if (y + 1 < 13 && Game.matrix[y + 1][x] == -1) {
			Game.matrix[y + 1][x] = 1;
		}
		// Left
		if (x - 1 >= 0 && Game.matrix[y][x - 1] == -1) {
			Game.matrix[y][x - 1] = 1;
		}


	}

	@Override
	public void update() {
		animate();
		
		if(!_alive) {
			afterKill();
			return;
		}
		
		if(_alive)
			calculateMove();
	}
	
	@Override
	public void render(Screen screen) {
		
		if(_alive)
			chooseSprite();
		else {
			if(_timeAfter > 0) {
				_sprite = _deadSprite;
				_animate = 0;
			} else {
				_sprite = Sprite.movingSprite(Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, _animate, 60);
			}
				
		}
			
		screen.renderEntity((int)_x, (int)_y - _sprite.SIZE, this);
	}
	
	@Override
	public void calculateMove() {
		// TODO: Tính toán hướng đi và di chuyển Enemy theo _ai và cập nhật giá trị cho _direction
		// TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không
		// TODO: sử dụng move() để di chuyển
		// TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
		int x= 0, y = 0;
		if (_steps<=0)
		{
			_direction  = _ai.calculateDirection();
			_steps = MAX_STEPS;
		}


		if (_direction==0) y--;
		if (_direction==1) x ++;
		if (_direction==2) y++;
		if (_direction==3) x --;
		if (canMove(x,y))
		{
			_steps -= 1 +rest;
			move(x* _speed, y *_speed);
			_moving = true;
		} else {
			_steps = 0;
			_moving = false;
		}


	}
	
	@Override
	public void move(double xa, double ya) {
		if(!_alive) return;
		_y += ya;
		_x += xa;
	}
	
	@Override
	public boolean canMove(double x, double y) {
		// TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không
		double x1 = _x, y1 = _y ;
		// y = _y -16

		if(_direction == 0) { x1=x1 + 8; y1-- ; } // 8 = _sprite.getSize()/2
		if(_direction == 1) { x1 ++; y1 = y1 -8;}
		if(_direction == 2) { x1= x1+8; y1= y1-15;}
		if(_direction == 3) { x1= x1 +15; y1 = y1-8;}

		int xx = Coordinates.pixelToTile(x1) +(int)x;
		int yy = Coordinates.pixelToTile(y1) +(int)y;

		Entity a = _board.getEntity(xx, yy, this);

		return a.collide(this);
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý va chạm với Flame
		if (e instanceof Flame)
		{
			kill();
			return false;
		}
		// TODO: xử lý va chạm với Bomber
		if (e instanceof Bomber)
		{
			((Bomber) e).kill();
			return false;
		}
		return true;
	}
	
	@Override
	public void kill() {
		if(!_alive) return;
		_alive = false;
		
		_board.addPoints(_points);

		Message msg = new Message("+" + _points, getXMessage(), getYMessage(), 2, Color.white, 14);
		_board.addMessage(msg);
	}
	
	
	@Override
	protected void afterKill() {
		if(_timeAfter > 0) --_timeAfter;
		else {
			if(_finalAnimation > 0) --_finalAnimation;
			else
				remove();
		}
	}
	
	protected abstract void chooseSprite();
}

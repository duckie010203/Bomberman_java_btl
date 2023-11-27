package bomberman.entities.bomb;

import bomberman.Audio.Sound;
import bomberman.Board;
import bomberman.Game;
import bomberman.entities.AnimatedEntitiy;
import bomberman.entities.Entity;
import bomberman.entities.character.Bomber;
import bomberman.entities.character.Character;
import bomberman.graphics.Screen;
import bomberman.graphics.Sprite;
import bomberman.level.Coordinates;

import java.util.Collection;

public class Bomb extends AnimatedEntitiy {

	Sound explosionBombAudio = new Sound("res/sound/Explosion.wav");
	protected double _timeToExplode = 120; //2 seconds
	public int _timeAfter = 20;
	
	protected Board _board;
	protected Flame[] _flames;
	protected boolean _exploded = false;
	protected boolean _allowedToPassThru = true;
	
	public Bomb(int x, int y, Board board) {
		_x = x;
		_y = y;
		_board = board;
		_sprite = Sprite.bomb;
	}
	
	@Override
	public void update() {
		if(_timeToExplode > 0) 
			_timeToExplode--;
		else {
			if(!_exploded) 
				explode();
			else
				updateFlames();
			
			if(_timeAfter > 0) 
				_timeAfter--;
			else
				remove();
		}
			
		animate();
	}
	
	@Override
	public void render(Screen screen) {
		if(_exploded) {
			_sprite =  Sprite.bomb_exploded2;
			renderFlames(screen);
		} else
			_sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, _animate, 60);
		
		int xt = (int)_x << 4;
		int yt = (int)_y << 4;
		
		screen.renderEntity(xt, yt , this);
	}
	
	public void renderFlames(Screen screen) {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].render(screen);
		}
	}
	
	public void updateFlames() {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].update();
		}
	}

    /**
     * Xử lý Bomb nổ
     */
	protected void explode() {

		explosionBombAudio.playSound(0);
		// TODO: xử lý khi Character đứng tại vị trí Bomb
		_allowedToPassThru = true;
		_exploded = true;
		Character a = _board.getCharacterAtExcluding((int)_x,(int)_y,null);
		if (a!= null)
		{
			a.kill();
		}
		// TODO: tạo các Flame
		_flames = new Flame[4];
		for (int i = 0; i < _flames.length; i++)
			_flames[i] = new Flame((int)_x,(int)_y,i, Game.getBombRadius(),_board);
	}
	
	public FlameSegment flameAt(int x, int y) {
		if(!_exploded) return null;
		
		for (int i = 0; i < _flames.length; i++) {
			if(_flames[i] == null) return null;
			FlameSegment e = _flames[i].flameSegmentAt(x, y);
			if(e != null) return e;
		}
		
		return null;
	}

	@Override
	public boolean collide(Entity e) {
        // TODO: xử lý khi Bomber đi ra sau khi vừa đặt bom (_allowedToPassThru)
		if (e instanceof Bomber)
		{
			double x = e.getX() - Coordinates.tileToPixel(getX());
			double y = e.getY() - Coordinates.tileToPixel(getY());
			// kiem tra cac gia tri xem nguoi choi da ra ngoai bom chua thu cac gia tri
			if (!(x>=-10)&& x <16 && y >=1 && y <=28)
			{
				_allowedToPassThru = false;
			}
			return _allowedToPassThru;
		}

        // TODO: xử lý va chạm với Flame của Bomb khác
		if (e instanceof Bomb)
		{
			_timeToExplode = 0;
			return true;
		}
        return false;
	}
}

package bomberman.entities.tile;

import bomberman.Board;
import bomberman.entities.Entity;
import bomberman.entities.bomb.Bomb;
import bomberman.entities.character.Bomber;
import bomberman.graphics.Sprite;

public class Portal extends Tile {
	private Board _board;
	public Portal(int x, int y, Sprite sprite,Board board) {
		super(x, y, sprite);
		_board = board;
	}
	
	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý khi Bomber đi vào
		if (e instanceof Bomber)
		{
			if (_board.detectNoEnemies()==false) return false;
			if (_board.detectNoEnemies() && e.getXTile() == getX() && e.getYTile() == getY())
			{
				_board.nextLevel();

			}
			return true;
		}
		return false;
	}

}

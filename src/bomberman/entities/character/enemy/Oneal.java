package bomberman.entities.character.enemy;


import bomberman.Board;
import bomberman.Game;
import bomberman.entities.character.enemy.ai.AIHigh;
import bomberman.entities.character.enemy.ai.AIMedium;
import bomberman.graphics.Sprite;
import bomberman.level.LevelLoader;

public class Oneal extends Enemy {
	
	public Oneal(int x, int y, Board board) {
		super(x, y, board, Sprite.oneal_dead, Game.getBomberSpeed(), 200);
		
		_sprite = Sprite.oneal_left1;
		if(LevelLoader._level == 2) _ai = new AIHigh(_board.getBomber(),this);
		else _ai = new AIMedium(_board.getBomber(), this, _board);
		_direction  = _ai.calculateDirection();
	}
	
	@Override
	protected void chooseSprite() {
		switch(_direction) {
			case 0:
			case 1:
				if(_moving)
					_sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, _animate, 60);
				else
					_sprite = Sprite.oneal_left1;
				break;
			case 2:
			case 3:
				if(_moving)
					_sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, _animate, 60);
				else
					_sprite = Sprite.oneal_left1;
				break;
		}
	}
}

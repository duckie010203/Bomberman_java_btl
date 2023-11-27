package bomberman.entities.tile.item;

import bomberman.Audio.Sound;
import bomberman.Game;
import bomberman.entities.Entity;
import bomberman.entities.character.Bomber;
import bomberman.graphics.Sprite;

public class BombItem extends Item {
	Sound upItemAudio = new Sound("res/sound/UpItem.wav");
	public BombItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý Bomber ăn Item
		if (e instanceof Bomber)
		{
			if (!e.isRemoved())
			{
				Game.addBombRate(1);
				upItemAudio.playSound(0);
				remove();
			}
		}
		return false;
	}
	


}

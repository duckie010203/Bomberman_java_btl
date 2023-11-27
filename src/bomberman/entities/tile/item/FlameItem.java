package bomberman.entities.tile.item;

import bomberman.Game;
import bomberman.Audio.Sound;
import bomberman.entities.Entity;
import bomberman.entities.character.Bomber;
import bomberman.graphics.Sprite;

public class FlameItem extends Item {

	public FlameItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	Sound upItemAudio = new Sound("res/sound/UpItem.wav");
	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý Bomber ăn Item
		if (e instanceof Bomber)
		{
			if (!e.isRemoved())
			{
				Game.addBombRadius(1);
				upItemAudio.playSound(0);
				remove();
			}
		}
		return false;
	}

}

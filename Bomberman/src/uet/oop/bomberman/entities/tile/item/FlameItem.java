package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class FlameItem extends Item {

	public FlameItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}



	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý Bomber ăn Item
		if (isAvailable() && e instanceof Bomber) {
			Game.addBombRadius(1);
//			System.out.println(Game.getBombRadius());
			this.remove();
			setAvailable(false);
		}
		return isAvailable();
	}

}
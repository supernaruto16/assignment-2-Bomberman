package uet.oop.bomberman.entities.tile;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Sound.SoundManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class Portal extends Tile {
	Board _board;

	public Portal(int x, int y, Sprite sprite, Board _board) {
		super(x, y, sprite);
		this._board = _board;
	}
	
	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý khi Bomber đi vào
		if(e instanceof Bomber) {
			if(_board.detectNoEnemies() == false)
				return true;
			if(e.getXTile() == getX() && e.getYTile() == getY()) {
				if(_board.detectNoEnemies())
					SoundManager.play("Victory");
					_board.nextLevel();
			}
		}
		return false;
	}

}

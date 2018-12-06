package uet.oop.bomberman.level;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.exceptions.GameException;

public class Coordinates {
	
	public static int pixelToTile(double i) {
		return (int)((i / Game.TILES_SIZE));
	}
	
	public static int tileToPixel(int i) {
		return i * Game.TILES_SIZE;
	}
	
	public static int tileToPixel(double i) {
		return (int)(i * Game.TILES_SIZE);
	}
	
	public static int mahattan(Entity e1, Entity e2) {
		return Math.abs(e1.getXTile() - e2.getXTile()) + Math.abs(e1.getYTile() - e2.getYTile());
	}
}

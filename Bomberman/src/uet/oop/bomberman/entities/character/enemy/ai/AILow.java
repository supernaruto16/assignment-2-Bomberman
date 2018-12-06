package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.level.Coordinates;

import java.util.Random;

public class AILow extends AI {

	Enemy e;
	Board _board;

	public AILow(Enemy e, Board _board) {
		this.e = e;
		this._board = _board;
	}

	@Override
	public int calculateDirection() {
		int x = e.getXTile(), y = e.getYTile();
		int hx[] = {0, 1, -1, 0, 0};
		int hy[] = {1, 0, 0, -1, 0};
		int ans = 0;
		int curDir = e.getDirection();
		if(curDir == -1)
			curDir = 0;
		hx[4] = hx[curDir];
		hy[4] = hy[curDir];
		for(int i = 0; i <= 4; ++i) {
			int u = x + hx[i], v = y + hy[i];
			if(validate(u, v)) {
				Entity entity = _board.getEntity(u, v, e);
				if (entity instanceof Grass)
					ans = i;
			}
		}
		if(ans == 4)
			ans = curDir;
		return ans;
	}

	public boolean validate(int u,int v) {
		return u >= 0 && v >=0 && u < _board.getWidth() && v < _board.getHeight();
	}
}

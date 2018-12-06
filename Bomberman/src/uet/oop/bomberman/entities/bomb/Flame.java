package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.graphics.Screen;

public class Flame extends Entity {

	protected Board _board;
	protected int _direction;
	private int _radius;
	protected int xOrigin, yOrigin;
	protected FlameSegment[] _flameSegments;
	private int dx, dy;

	/**
	 *
	 * @param x hoành độ bắt đầu của Flame
	 * @param y tung độ bắt đầu của Flame
	 * @param direction là hướng của Flame
	 * @param radius độ dài cực đại của Flame
	 */
	public Flame(int x, int y, int direction, int radius, Board board) {
		xOrigin = x;
		yOrigin = y;
		_x = x;
		_y = y;
		_direction = direction;
		_radius = radius;
		_board = board;
		switch (_direction) {
			case 0:
				dy--;
				break;
			case 1:
				dx++;
				break;
			case 2:
				dy++;
				break;
			case 3:
				dx--;
				break;
		}
		createFlameSegments();
	}

	/**
	 * Tạo các FlameSegment, mỗi segment ứng một đơn vị độ dài
	 */
	private void createFlameSegments() {
		/**
		 * tính toán độ dài Flame, tương ứng với số lượng segment
		 */
		int maxSize = calculatePermitedDistance();
		_flameSegments = new FlameSegment[maxSize];

		/**
		 * biến last dùng để đánh dấu cho segment cuối cùng
		 */
		boolean last = true;

		// TODO: tạo các segment dưới đây
		if(maxSize <= 0) return;
		for (int i = 1; i <= maxSize - 1; i++)
			_flameSegments[i - 1] = new FlameSegment(xOrigin + dx * i, yOrigin + dy * i, _direction, false);
		_flameSegments[maxSize-1] = new FlameSegment(xOrigin + dx * maxSize, yOrigin + dy * maxSize, _direction, last);
	}

	/**
	 * Tính toán độ dài của Flame, nếu gặp vật cản là Brick/Wall, độ dài sẽ bị cắt ngắn
	 * @return
	 */
	private int calculatePermitedDistance() {
		// TODO: thực hiện tính toán độ dài của Flame
		for (int i = 1; i <= _radius; i++) {
			if (xOrigin + dx * i < 0 || xOrigin + dx * i >= _board.getWidth()) return i - 1;
			if (yOrigin + dy * i < 0 || yOrigin + dy * i >= _board.getHeight()) return i - 1;
			Entity tmp = _board.getEntity(xOrigin + dx * i, yOrigin + dy * i, null);
//			System.out.println(tmp.getClass().getName());
			if (!tmp.collide(this)) return i - 1;
		}
		return _radius;
	}
	
	public FlameSegment flameSegmentAt(int x, int y) {
		for (int i = 0; i < _flameSegments.length; i++) {
			if(_flameSegments[i].getX() == x && _flameSegments[i].getY() == y)
				return _flameSegments[i];
		}
		return null;
	}

	@Override
	public void update() {}
	
	@Override
	public void render(Screen screen) {
		for (int i = 0; i < _flameSegments.length; i++) {
			_flameSegments[i].render(screen);
		}
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý va chạm với Bomber, Enemy. Chú ý đối tượng này có vị trí chính là vị trí của Bomb đã nổ
//		if (e instanceof Character)
//			((Character) e).kill();
		return true;
	}
}

package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Sound.SoundManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.ai.AI;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import java.awt.*;

public abstract class Enemy extends Character{

	protected int _points;

	protected double _speed;
	protected AI _ai;

	protected final double MAX_STEPS;
	protected final double rest;
	protected double _steps;

	protected int _finalAnimation = 30;
	protected Sprite _deadSprite;

	public Enemy(int x, int y, Board board, Sprite dead, double speed, int points) {
		super(x, y, board);

		_points = points;
		_speed = speed;

		MAX_STEPS = Game.TILES_SIZE / _speed;
//		System.out.println("MAX_STEPS = " + MAX_STEPS);
		rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
		_steps = MAX_STEPS;

		_timeAfter = 20;
		_deadSprite = dead;
	}

	@Override
	public void update() {
		animate();

		if(!_alive) {
			afterKill();
			return;
		}

		if(_alive)
			calculateMove();
	}

	@Override
	public void render(Screen screen) {

		if(_alive)
			chooseSprite();
		else {
			if(_timeAfter > 0) {
				_sprite = _deadSprite;
				_animate = 0;
			} else {
				_sprite = Sprite.movingSprite(Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, _animate, 60);
			}

		}

		screen.renderEntity((int)_x, (int)_y - _sprite.SIZE, this);
	}

	@Override
	public void calculateMove() {
		// TODO: Tính toán hướng đi và di chuyển Enemy theo _ai và cập nhật giá trị cho _direction
		// TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không
		// TODO: sử dụng move() để di chuyển
		// TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển

		_moving = true;
		if(_steps != MAX_STEPS) {
			_steps--;
			if(_steps <= 0) _steps = MAX_STEPS;
		}
		else {
			_steps--;
			_direction = _ai.calculateDirection();
		}


		_moving = _direction >= 0;
		if(_direction == 0) move(_x, _y + _speed);
		if(_direction == 1) move(_x + _speed, _y);
		if(_direction == 2) move(_x - _speed, _y);
		if(_direction == 3) move(_x, _y - _speed);
	}

	@Override
	public void move(double xa, double ya) {
		if(!_alive) return;
		if(canMove(xa, ya)) {
			_x = xa;
			_y = ya;
		}
	}

	@Override
	public boolean canMove(double x, double y) {
		y = y - 1;
		double size = Game.TILES_SIZE - 1;
		int[] hx = {0, 0, 1, 1};
		int[] hy = {0, -1, 0, -1};
		for(int i = 0; i < 4; ++i) {
			int u = Coordinates.pixelToTile(x + hx[i] * size),
					v = Coordinates.pixelToTile(y + hy[i] * size);
			Entity entity = _board.getEntity(u, v, this);
			if(!entity.collide(this))
				return false;
		}
		return true;
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý va chạm với Flame
		if (e instanceof FlameSegment || e instanceof Flame)
			this.kill();
		// TODO: xử lý va chạm với Bomber
		if (e instanceof Bomber)
			((Bomber) e).kill();
		return true;
	}

	@Override
	public void kill() {
		if(!_alive) return;
		_alive = false;

		//SoundManager.allMuteExceptBGM();
		if (_board.getPoints() == 0)
			SoundManager.play("firstBlood");
		else SoundManager.play("KillingEnemy");

		_board.addPoints(_points);

		Message msg = new Message("+" + _points, getXMessage(), getYMessage(), 2, Color.white, 14);
		_board.addMessage(msg);

	}


	@Override
	protected void afterKill() {
		if(_timeAfter > 0) --_timeAfter;
		else {
			if(_finalAnimation > 0) --_finalAnimation;
			else
				remove();
		}
	}

	public int getDirection() {
		return _direction;
	}

	protected abstract void chooseSprite();
}

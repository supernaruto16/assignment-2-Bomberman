package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Sound.SoundManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.lib.Pair;

import java.awt.*;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Random;

public class Doria extends Enemy {

    public Doria(int x,int y, Board  board) {
        super(x, y, board, Sprite.kondoria_dead, Game.getBomberSpeed() / 5, 1000);
        _sprite = Sprite.kondoria_left1;
        _ai = new AIMedium(board, this);
    }

    @Override
    public void update() {
        animate();

        if(!_alive) {
            afterKill();
            return;
        }

        if(_alive) {
//
            calculateMove();
        }
    }

    public void calculateFastMove(Bomber p) {
        _moving = true;
        Random random = new Random();
        Pair rTg = randTarget(p.getXTile(), p.getYTile());
        if (random.nextDouble() < 0.5) {
            calculateFastMoveX(rTg.first, rTg.second);
            calculateFastMoveY(rTg.first, rTg.second);
        } else {
            calculateFastMoveY(rTg.first, rTg.second);
            calculateFastMoveX(rTg.first, rTg.second);
        }
    }

    public Pair randTarget(int xTg, int yTg) {
        ArrayList<Pair> res = new ArrayList<>();
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                if (0 > xTg + i || xTg + i >= Game.WIDTH || 0 > yTg + j || yTg + j >= Game.HEIGHT) continue;
                Entity e = _board.getEntity(xTg + i, yTg + j, null);
                if (e instanceof Wall || (e instanceof LayeredEntity && ((LayeredEntity) e).getTopEntity() instanceof Brick))
                    continue;
                res.add(new Pair(xTg + i, yTg + j));
            }
        if (res.size() == 0) return new Pair(xTg, yTg);
        Random random = new Random();
        return res.get(random.nextInt(res.size() - 1));
    }

    public void calculateFastMoveX(int xTg, int yTg) {
        double xa, _speed1;
        if (xTg > _x) {
            _direction = 1;
            _speed1 = (xTg - _x) * Game.TILES_SIZE;

        }
    }

    public void calculateFastMoveY(int xTg, int yTg) {

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
    public void kill() {
        if(!_alive) return;
        _alive = false;

        if (_board.getPoints() == 0)
            SoundManager.play("firstBlood");
        else SoundManager.play("Legendary");

        _board.addPoints(_points);

        Message msg = new Message("+" + _points, getXMessage(), getYMessage(), 2, Color.white, 14);
        _board.addMessage(msg);

    }

    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý va chạm với Flame
        if (e instanceof FlameSegment || e instanceof Flame)
            this.kill();
        // TODO: xử lý va chạm với Bomber
        if (e instanceof Bomber)
            ((Bomber) e).kill();
        return false;
    }

    protected void chooseSprite() {
        switch(_direction) {
            case 0:
            case 1:
                _sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, _animate, 60);
            case 2:
            case 3:
                _sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, _animate, 60);
                break;
        }
    }

}

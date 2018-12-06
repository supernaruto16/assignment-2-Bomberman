package uet.oop.bomberman.entities.character.enemy;


import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Sound.SoundManager;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;

public class Doll extends Enemy {
    private int _lives;
    private int _timeAfter = 20;
    private int _timeBurn = 30;
    private Flame[] _flames;

    public Doll(int x, int y, Board board) {
        super(x, y, board, Sprite.doll_dead, Game.getBomberSpeed(), 2000);

        _sprite = Sprite.doll_left1;
        _lives = 2;

        _ai = new AILow(this, board);
        _timeBurn = 120;
        _direction  = _ai.calculateDirection();
    }


    @Override
    public void update() {
        super.update();
        if (_timeAfter > 0) _timeAfter--;
        if (!_alive) return;

        if (_timeBurn > 0) {
            if (_timeBurn == 120) _flames = new Flame[4];
            _timeBurn--;
            if (_timeBurn <= 120 - 2) _direction = _ai.calculateDirection();
        }
        else {
            for (int i = 0; i < 4; i++) {
                _flames[i] = new Flame((int) _x, (int) _y, i, 2, _board);
            }
            _direction = -1;
            _timeBurn = 120;
        }
//        System.out.println(_timeBurn);
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
    public void kill() {
        if(!_alive) return;
        if (_timeAfter > 0) return;
        this._lives--;
        Message msg1 = new Message("-1 live", getXMessage(), getYMessage(), 2, Color.white, 14);
        _board.addMessage(msg1);

        _timeAfter = 20;
        if (this._lives == 0) {
            _alive = false;
        } else return;

        if (_board.getPoints() == 0)
            SoundManager.play("firstBlood");
        else SoundManager.play("Legendary");

        _board.addPoints(_points);

        Message msg = new Message("+" + _points, getXMessage(), getYMessage(), 2, Color.white, 14);
        _board.addMessage(msg);

    }

    @Override
    protected void chooseSprite() {
        switch(_direction) {
            case 0:
            case 1:
                if(_moving)
                    _sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, _animate, 60);
                else
                    _sprite = Sprite.doll_left1;
                break;
            case 2:
            case 3:
                if(_moving)
                    _sprite = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, _animate, 60);
                else
                    _sprite = Sprite.doll_left1;
                break;
        }
    }
}

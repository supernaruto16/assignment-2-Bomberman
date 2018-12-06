package uet.oop.bomberman.entities.character.enemy;


import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Sound.SoundManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import java.awt.*;

public class New1 extends Enemy {
    boolean canDouble;
    private int _timeDouble;

    public New1(int x, int y, Board board, boolean canDouble) {
        super(x, y, board, Sprite.new1_dead, Game.BOMBERSPEED / 3, 200);

        _sprite = Sprite.new1_left1;

        _ai = new AILow(this, board);
        this.canDouble = canDouble;
        _timeDouble = 60;
        _direction  = _ai.calculateDirection();
    }

    public void update() {
        super.update();
        if (_timeDouble > 0) _timeDouble--;
    }

    @Override
    public void kill() {
        if(!_alive) return;
        if(canDouble && _timeDouble > 0) return;
        _alive = false;

        if (canDouble) {
            _board.addCharacter(new New1(Coordinates.tileToPixel(_x), Coordinates.tileToPixel(_y) + Game.TILES_SIZE, _board, false));
            _board.addCharacter(new New1(Coordinates.tileToPixel(_x + 1), Coordinates.tileToPixel(_y + 1) + Game.TILES_SIZE, _board, false));
            return;
        }
        //SoundManager.allMuteExceptBGM();
        if (_board.getPoints() == 0)
            SoundManager.play("firstBlood");
        else SoundManager.play("KillingEnemy");

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
                    _sprite = Sprite.movingSprite(Sprite.new1_right1, Sprite.new1_right2, Sprite.new1_right3, _animate, 60);
                else
                    _sprite = Sprite.new1_left1;
                break;
            case 2:
            case 3:
                if(_moving)
                    _sprite = Sprite.movingSprite(Sprite.new1_left1, Sprite.new1_left2, Sprite.new1_left3, _animate, 60);
                else
                    _sprite = Sprite.new1_left1;
                break;
        }
    }
}

package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;

public class Minvo extends Enemy {
    private int _timeEat;

    public Minvo(int x, int y, Board board) {
        super(x, y, board, Sprite.minvo_dead, Game.getBomberSpeed() / 3, 300);

        _sprite = Sprite.minvo_left1;

        _ai = new AILow(this, board);
        _direction  = _ai.calculateDirection();
    }

    @Override
    public void move(double xa, double ya) {
        if(!_alive) return;
        if(canMove(xa, ya)) {
            _x = xa;
            _y = ya;

            if (_timeEat > 0) _timeEat--;
            else {
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++) {
                        Bomb b = _board.getBombAt(getXTile() + i, getYTile() + j);
                        if (b != null) {
                            b.remove();
                            Message msg = new Message("defused", getXMessage(), getYMessage(), 2, Color.white, 14);
                            _board.addMessage(msg);
                            _timeEat = 60 * 7;
                        }
                    }
            }
        }
    }

    @Override
    protected void chooseSprite() {
        switch(_direction) {
            case 0:
            case 1:
                if(_moving)
                    _sprite = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2, Sprite.minvo_right3, _animate, 60);
                else
                    _sprite = Sprite.minvo_left1;
                break;
            case 2:
            case 3:
                if(_moving)
                    _sprite = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2, Sprite.minvo_left3, _animate, 60);
                else
                    _sprite = Sprite.minvo_left1;
                break;
        }
    }
}

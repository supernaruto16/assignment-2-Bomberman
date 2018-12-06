package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.lib.Pair;
import uet.oop.bomberman.entities.Entity;

import java.util.*;

public class AIEnemy extends AI{

    protected int _x, _y;
    protected Random random = new Random();
    protected Board board;
    protected char[][] map;
    protected int[] hx = {1, 0, 0, -1};
    protected int[] hy = {0, 1, -1, 0};
    protected int m, n, u_bomber, v_bomber, _bestDirection;
    protected Pair[][] trace;
    protected boolean[][] inDanger;

    public AIEnemy(int _x, int _y, Board board) {
        this._x = _x;
        this._y = _y;
        this.board = board;
        if(board == null)
            throw new NullPointerException("Board hasn't loaded!");
        m = board.getHeight();
        n = board.getWidth();
        getMap();
        findPath();
    }

    public void getMap() {
        for(int i = 0; i <m; ++i) {
            for (int k = 0; k < n; ++k) {
                Entity entity = board.getEntityAt(i, k);
                map[i][k] = ' ';
                if (entity instanceof Wall) {
                    map[i][k] = 'w';
                }
                if (entity instanceof Bomber) {
                    map[i][k] = 'p';
                    u_bomber = i;
                    v_bomber = k;
                }
                if (entity instanceof Bomb) {
                    map[i][k] = 'b';
                    for (int u = 0; u < 4; ++u) {
                        for (int v = 0; v < 3; ++v) {
                            int x = i + hx[u] * v, y = i + hy[u] * v;
                            if (valid(x, y)) {
                                map[i][k] = 'f';
                            }
                        }
                    }
                }
                if (entity instanceof Flame) {
                    map[i][k] = 'f';
                }

                if(entity instanceof Brick) {
                    if(!entity.collide(board.getEntityAt(_x, _y)))
                        map[i][k] = 'w';
                }
            }
        }
    }

    public void findPath() {
        int[][] D = new int[m][n];
        for(int i = 0; i < m; ++i) {
            for (int k = 0; k < n; ++k)
                D[i][k] = -1;
        }
        D[u_bomber][v_bomber] = 0;
        trace[u_bomber][v_bomber] = null;
        Queue<Pair> queue = new Queue<Pair>() {
            @Override
            public boolean add(Pair pair) {
                return false;
            }

            @Override
            public boolean offer(Pair pair) {
                return false;
            }

            @Override
            public Pair remove() {
                return null;
            }

            @Override
            public Pair poll() {
                return null;
            }

            @Override
            public Pair element() {
                return null;
            }

            @Override
            public Pair peek() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Pair> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Pair> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
        queue.add(new Pair(u_bomber, v_bomber));
        while(!queue.isEmpty()) {
            Pair cur = queue.remove();
            int u = cur.first, v = cur.second;
            for(int i = 0; i < n; ++i) {
                int x = u + hx[i], y = v + hy[i];
                if(map[x][y] != 'w' && D[x][y] == -1) {
                    if(map[u][v] != 'f' || x != _x || y != _y) {
                        trace[x][y] = new Pair(u, v);
                        queue.add(new Pair(x, y));
                    }
                }
            }
        }
        Pair cur = trace[_x][_y];
        int x = cur.first, y = cur.second;
        if(x > _x) _bestDirection = 0;
        if(y > _y) _bestDirection = 1;
        if(y < _y) _bestDirection = 2;
        if(x < _x) _bestDirection = 3;
    }

    public boolean valid(int u,int v) {
        return u >= 0 && v >=0 && u < m && v < n;
    }

    /**
     * Thuật toán tìm đường đi
     * @return hướng đi xuống/phải/trái/lên tương ứng với các giá trị 0/1/2/3
     */
    public int calculateDirection() {
        return _bestDirection;
    };
}

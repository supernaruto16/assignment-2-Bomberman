package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Doria;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.destroyable.DestroyableTile;
import uet.oop.bomberman.lib.Pair;

import javax.security.auth.Destroyable;
import java.util.*;

public class AIMedium extends AI{

	protected int _x, _y;
	protected Enemy e;
	protected Random random = new Random();
	protected Board board;
	protected char[][] map;
	protected int[] hx = {1, 0, 0, -1};
	protected int[] hy = {0, 1, -1, 0};
	protected int m, n, u_bomber, v_bomber, _bestDirection;
	protected Pair[][] trace;
	protected boolean[][] inDanger;

	public AIMedium(Board board, Enemy e) {
		//System.out.println(_x + " " + _y);
		this.board = board;
		this.e = e;
		if(board == null)
			throw new NullPointerException("Board hasn't loaded!");
		m = board.getHeight();
		n = board.getWidth();
	}

	public void getMap() {
		map = new char[m][n];
		for(int i = 0; i < m; ++i) {
			for (int k = 0; k < n; ++k) {
				Entity entity = board.getEntity(k, i, e);
				map[i][k] = ' ';
				if (entity instanceof Tile && !entity.collide(e)) {
					map[i][k] = 'w';
				}
				if(entity instanceof LayeredEntity && !(e instanceof Doria)) {
					LayeredEntity tempE = (LayeredEntity)entity;
					if(tempE.getTopEntity() instanceof  Brick)
						map[i][k] = 'w';
				}
				if (entity instanceof Bomber) {
					map[i][k] = 'p';
					u_bomber = i;
					v_bomber = k;
				}
				if (entity instanceof Bomb) {
					//System.out.println(i + " " + k);
					map[i][k] = 'b';
					for (int u = 0; u < 4; ++u) {
						for (int v = 0; v < Game.getBombRadius(); ++v) {
							int x = i + hx[u] * v, y = k + hy[u] * v;
							if (valid(x, y) && map[x][y] != 'w') {
								if(map[x][y] != 'p')
									map[x][y] = 'f';
							}
							else
								break;
						}
					}
				}

				if (entity instanceof Flame) {
					map[i][k] = 'f';
				}

			}
		}
		for(int i = 0; i < m; ++i) {
			for(int k = 0; k < n; ++k) {
				Entity entity = board.getEntity(k, i, e);
				if (entity instanceof Bomb) {
					//System.out.println(i + " " + k);
					map[i][k] = 'b';
					for (int u = 0; u < 4; ++u) {
						for (int v = 0; v < 1; ++v) {
							int x = i + hx[u] * v, y = k + hy[u] * v;
							if (valid(x, y) && map[x][y] != 'w') {
								if(map[x][y] != 'p')
									map[x][y] = 'f';
							}
							else
								break;
						}
					}
				}
			}
		}
		_y = e.getXTile();
		_x = e.getYTile();
		map[_x][_y] = 'e';
		//System.out.println(e.getXTile() + " " + e.getYTile());
		//System.out.flush();
	}

	public void printMap(char[][] map) {
		for(int i = 0; i < m; ++i) {
			for(int k = 0; k < n; ++k)
				System.out.print(map[i][k]);
			System.out.println();
		}
	}

	public void findPath() {
		int[][] D = new int[m][n];
		for(int i = 0; i < m; ++i) {
			for (int k = 0; k < n; ++k)
				D[i][k] = -1;
		}
		D[u_bomber][v_bomber] = 0;
		trace = new Pair[m][n];
		trace[u_bomber][v_bomber] = null;
		Queue<Pair> queue = new Queue<Pair>() {

			class Node {
				Pair value;
				Node next = null;

				Node(Pair value) {
					this.value = value;
				}
			}

			private Node head = null;
			private Node tail = null;

			@Override
			public boolean add(Pair pair) {
				if(head == null) {
					head = new Node(pair);
					tail = head;
				}
				tail.next = new Node(pair);
				tail = tail.next;
				return true;
			}

			@Override
			public boolean offer(Pair pair) {
				return false;
			}

			@Override
			public Pair remove() {
				if(head == null)
					return null;
				else {
					Pair ans = head.value;
					head = head.next;
					if(head == null)
						tail = head;
					return ans;
				}
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
				return (head == null);
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
			//System.out.println(u + " " + v);
			for(int i = 0; i < 4; ++i) {
				int x = u + hx[i], y = v + hy[i];
				if(valid(x, y) && map[x][y] != 'w' && (map[x][y] != 'g' || e instanceof  Doria) && D[x][y] == -1) {
					//System.out.println(u + " " + v + " " + x + " " + y);
					if(map[u][v] != 'f') {
						trace[x][y] = new Pair(u, v);
						D[x][y] = D[u][v] + 1;
						queue.add(new Pair(x, y));
					}
				}
			}
		}
		//System.out.println(_x + " " + _y);
		Pair cur = trace[_x][_y];
		if(cur != null) { ;
			int x = cur.first, y = cur.second;
			if (x > _x) _bestDirection = 0;
			if (y > _y) _bestDirection = 1;
			if (y < _y) _bestDirection = 2;
			if (x < _x) _bestDirection = 3;
		}
		else {
			AI newAI = new AILow(e, board);
			_bestDirection = newAI.calculateDirection();
		}
	}

	public boolean valid(int u,int v) {
		return u >= 0 && v >=0 && u < m && v < n;
	}

	/**
	 * Thuật toán tìm đường đi
	 * @return hướng đi xuống/phải/trái/lên tương ứng với các giá trị 0/1/2/3
	 */
	public int calculateDirection() {
		getMap();
		findPath();
		//System.out.println(_bestDirection);
		if(e instanceof Oneal) {
			printMap(map);
			System.out.println(_bestDirection);
		}
		return _bestDirection;
	};
}
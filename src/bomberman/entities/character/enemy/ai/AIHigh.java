package bomberman.entities.character.enemy.ai;

import java.util.*;

import bomberman.Game;
import bomberman.entities.character.Bomber;
import bomberman.entities.character.enemy.Enemy;
import static bomberman.entities.character.enemy.ai.BFS.shortestPath;
public class AIHigh extends AI{
    Bomber player;
    Enemy enemy;

    public AIHigh(Bomber player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    @Override
    public int calculateDirection() {
        if (player == null) {
            return random.nextInt(4);
        }

        List<Point> path = shortestPath(Game.matrix,
                new Point(player.getYTile(), player.getXTile()),
                new Point(enemy.getYTile(), enemy.getXTile()));

        if (path == null) {
            return random.nextInt(4);
        } else if (path.size() == 1) {
            return -1;
        } else {
            /* Debug
            System.out.println(player.getYTile() + " " + player.getXTile());
            System.out.println(enemy.getYTile() + " " + enemy.getXTile());
            for (int i = 0; i < 13; i++) {
                for (int j = 0; j < 31; j++) System.out.print(matrix[i][j] + " ");
                System.out.println();
            }
            for (Point point : path) System.out.println(point.row + " " + point.col);
             */
            return calculateDirections(path.get(0), path.get(1));
        }
    }

    protected int calculateDirections(Point start, Point end) {
        if (end.row < start.row) return 0;
        if (end.col > start.col) return 1;
        if (end.row > start.row) return 2;
        if (end.col < start.col) return 3;
        return -1;
    }

}

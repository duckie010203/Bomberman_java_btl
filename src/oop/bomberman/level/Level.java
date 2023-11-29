package oop.bomberman.level;

import oop.bomberman.Board;


public abstract class Level implements ILevel {

    protected int width, height, level;
    protected String[] lineTiles;
    protected Board board;

    protected static String[] codes = {
            "test1",
            "test2",
            "test3",
            "test4",
            "test5",
            "test6",
            "test7",
    };

    public Level(String path, Board board) {
        loadLevel(path);
        this.board = board;
    }

    @Override
    public abstract void loadLevel(String path);

    public abstract void createEntities();

    public int validCode(String str) {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLevel() {
        return level;
    }

}

package com.javarush.task.task35.task3513;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Будет содержать игровую логику и хранить игровое поле
 */
public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    int score;
    int maxTile;

    private boolean isSaveNeeded = true;
    private Stack<Tile[][]> previousStates=new Stack<>();
    private Stack<Integer> previousScores=new Stack<>();


    public Model(){
        resetGameTiles();
    }

    /** Метод заполняет массив gameTiles новыми плитками и меняет значение двух из них
     * с помощью двух вызовов метода addTile
     */
    public void resetGameTiles(){
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++)
        {
            for (int j = 0; j < FIELD_WIDTH; j++)
            {
                gameTiles[i][j] = new Tile();
            }
        }
        score = 0;
        maxTile = 2;
        addTile();
        addTile();
    }

    /** Метод возвращяет список свободных плиток в массиве gameTiles
     */
    private List<Tile> getEmptyTiles(){
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if(gameTiles[i][j].value == 0){
                    tiles.add(gameTiles[i][j]);
                }
            }
        }
        return tiles;
    }

    /** Метод который смотрит какие клетки пустуют и менять вес одной из них,
     * выбранной случайным образом, на 2 или 4
     */
    private void addTile(){
        List<Tile> tiles = getEmptyTiles();
        if(tiles.size() > 0)
        {
            tiles.get( (int) (tiles.size() * Math.random()) )
                    .value = Math.random() < 0.9 ? 2 : 4;
        }
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean change = false;
        for (int k = 0; k < tiles.length - 1; k++) {
            for (int i = 0; i < tiles.length - 1; i++) {
                if (tiles[i].isEmpty() && !tiles[i+1].isEmpty()) {
                    change = true;
                    tiles[i] = tiles[i+1];
                    tiles[i+1] = new Tile();
                }
            }
        }
        return change;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean change = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i].value == tiles[i+1].value && !tiles[i].isEmpty() && !tiles[i+1].isEmpty()) {
                change = true;
                tiles[i].value *= 2;
                tiles[i+1] = new Tile();
                maxTile = maxTile > tiles[i].value ? maxTile : tiles[i].value;
                score += tiles[i].value;
                compressTiles(tiles);
            }
        }
        return change;
    }

    /** Движение строк плиток в лево
     */
    public void left() {
        boolean isChanged = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                isChanged = true;
            }
        }
        if (isChanged) addTile();
    }

    private void rotateToRight() {
        Tile tmp;
        for (int i = 0; i < FIELD_WIDTH / 2; i++) {
            for (int j = i; j < FIELD_WIDTH - 1 - i; j++) {
                tmp = gameTiles[i][j];
                gameTiles[i][j] = gameTiles[FIELD_WIDTH-j-1][i];
                gameTiles[FIELD_WIDTH-j-1][i] = gameTiles[FIELD_WIDTH-i-1][FIELD_WIDTH-j-1];
                gameTiles[FIELD_WIDTH-i-1][FIELD_WIDTH-j-1] = gameTiles[j][FIELD_WIDTH-i-1];
                gameTiles[j][FIELD_WIDTH-i-1] = tmp;
            }
        }
    }

    void right() {
        rotateToRight();
        rotateToRight();
        left();
        rotateToRight();
        rotateToRight();
    }
    void up() {
        rotateToRight();
        rotateToRight();
        rotateToRight();
        left();
        rotateToRight();
    }
    void down() {
        rotateToRight();
        left();
        rotateToRight();
        rotateToRight();
        rotateToRight();
    }

    /**
     * Метод возвращающий true в случае, если в текущей позиции возможно сделать ход так,
     * чтобы состояние игрового поля изменилось. Иначе - false
     */
    public boolean canMove() {
        if(!getEmptyTiles().isEmpty()) return true;

        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 1; j < gameTiles.length; j++) {
                if (gameTiles[i][j].value == gameTiles[i][j-1].value)
                    return true;
            }
        }
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 1; j < gameTiles.length; j++) {
                if (gameTiles[j][i].value == gameTiles[j-1][i]. value)return true;
            }
        }
        return false;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    private void saveState(Tile[][] tiles){
        Tile[][] bufTiles=new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                bufTiles[i][j]=tiles[i][j];
                bufTiles[i][j].value=tiles[i][j].value;
            }
        }
        previousStates.push(bufTiles);
        previousScores.push(this.score);
        isSaveNeeded=false;
    }
    public void rollback(){
        if(!previousScores.isEmpty()&&!previousStates.isEmpty()) {
            gameTiles = previousStates.pop();
            score=previousScores.pop();
        }
    }
}

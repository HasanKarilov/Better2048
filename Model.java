package com.javarush.task.task35.task3513;

import java.util.ArrayList;
import java.util.List;

/**
 * Будет содержать игровую логику и хранить игровое поле
 */
public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    int score;
    int maxTile;

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

    /**
     *
     */
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

}

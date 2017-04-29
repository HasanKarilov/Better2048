package com.javarush.task.task35.task3513;

import java.util.ArrayList;
import java.util.List;

/**
 * Будет содержать игровую логику и хранить игровое поле
 */
public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;

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

}

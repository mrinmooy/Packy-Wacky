package com.salazarisaiahnoel.pacmanandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;

public class Pacman {
    Context context;
    int[][] map;
    int cellSizeRow, cellSizeCol;
    int posX, posY;
    int speed;
    MediaPlayer mp;

    public Pacman(Context context, int[][] map, int cellSizeRow, int cellSizeCol, int speed){
        this.context = context;
        this.map = map;
        this.cellSizeRow = cellSizeRow;
        this.cellSizeCol = cellSizeCol;
        this.speed = speed;
        posX = cellSizeCol * 10;
        posY = cellSizeRow * 15;
        mp = MediaPlayer.create(context, R.raw.pacman_wacca_v6);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(posX, posY, posX + cellSizeCol, posY + cellSizeRow, paint);

        paint.setColor(Color.YELLOW);
        canvas.drawCircle(posX + cellSizeCol / 2, posY + cellSizeRow / 2, cellSizeCol / 2, paint);
    }

    public void move(int dx, int dy) {
        int newX = posX + dx;
        int newY = posY + dy;

        if (isValidMove(newX, newY) && isValidMove(newX + cellSizeCol - 1, newY + cellSizeRow - 1) && isValidMove(newX, newY + cellSizeRow - 1) && isValidMove(newX + cellSizeCol - 1, newY)){
            posX = newX;
            posY = newY;
            consumePellet(newX + cellSizeCol / 2, newY + cellSizeRow / 2);
        }
    }

    private void consumePellet(int x, int y) {
        int col = x / cellSizeCol;
        int row = y / cellSizeRow;

        if (map[row][col] == 2) {
            map[row][col] = 0;
            mp.start();
        }
    }

    boolean isValidMove(int newX, int newY){
        if (newX >= 0 && newX < map[0].length * cellSizeCol && newY >= 0 && newY < map.length * cellSizeRow) {
            int col = newX / cellSizeCol;
            int row = newY / cellSizeRow;

            if (map[row][col] != 1) {
                return true;
            }
        }
        return false;
    }
}

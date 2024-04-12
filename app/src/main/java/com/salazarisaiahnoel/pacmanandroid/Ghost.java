package com.salazarisaiahnoel.pacmanandroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Ghost {
    int[][] map;
    int cellSizeRow, cellSizeCol;
    int posX, posY;
    int speed;
    int color;
    boolean isMoving = false;
    boolean isStart = false;
    int goOutCounter = 0;
    boolean should = false;

    boolean canMoveUp, canMoveDown, canMoveLeft, canMoveRight;
    Random ran = new Random();
    int dir = 0;

    public Ghost(int color, int[][] map, int cellSizeRow, int cellSizeCol, int speed, int position, int place){
        this.color = color;
        this.map = map;
        this.cellSizeRow = cellSizeRow;
        this.cellSizeCol = cellSizeCol;
        this.speed = speed;
        posX = cellSizeCol * (position + 9);
        posY = cellSizeRow * place;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(posX, posY, posX + cellSizeCol, posY + cellSizeRow, paint);

        paint.setColor(color);
        canvas.drawCircle(posX + cellSizeCol / 2, posY + cellSizeRow / 2, cellSizeCol / 2, paint);
        canvas.drawRect(posX, posY + (cellSizeRow / 2), posX + cellSizeCol, posY + cellSizeRow, paint);
    }

    public void move(int dx, int dy) {
        int newX = posX + dx;
        int newY = posY + dy;

        if (isValidMove(newX, newY) && isValidMove(newX + cellSizeCol - 1, newY + cellSizeRow - 1) && isValidMove(newX, newY + cellSizeRow - 1) && isValidMove(newX + cellSizeCol - 1, newY)){
            posX = newX;
            posY = newY;
            isMoving = true;
        } else {
            isMoving = false;
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

    void goOutside(){
        switch (goOutCounter){
            case 0:
                if (!isStart){
                    move(-speed, 0);
                }
                if (!isMoving){
                    isStart = true;
                    goOutCounter++;
                }
                break;
            case 1:
                if (isStart){
                    move(0, -speed);
                }
                if (!isMoving){
                    isStart = false;
                    goOutCounter++;
                }
                break;
            case 2:
                if (!isStart){
                    if (isValidMove(posX, posY - speed) && isValidMove(posX + cellSizeCol - 1, posY + cellSizeRow - 1 - speed) && isValidMove(posX, posY + cellSizeRow - 1 - speed) && isValidMove(posX + cellSizeCol - 1, posY - speed)){
                        move(0, -speed);
                        should = true;
                    } else {
                        if (should){
                            isStart = true;
                            goOutCounter++;
                        }
                        move(speed, 0);
                    }
                }
                break;
            case 3:
                if (isStart){
                    move(-speed, 0);
                }
                if (!isMoving){
                    isStart = false;
                    goOutCounter++;
                }
                break;
        }
    }

    void moveRandomly(){
        canMoveUp = isValidMove(posX, posY - speed) && isValidMove(posX + cellSizeCol - 1, posY + cellSizeRow - 1 - speed) && isValidMove(posX, posY + cellSizeRow - 1 - speed) && isValidMove(posX + cellSizeCol - 1, posY - speed);
        canMoveDown = isValidMove(posX, posY + speed) && isValidMove(posX + cellSizeCol - 1, posY + cellSizeRow - 1 + speed) && isValidMove(posX, posY + cellSizeRow - 1 + speed) && isValidMove(posX + cellSizeCol - 1, posY + speed);
        canMoveLeft = isValidMove(posX - speed, posY) && isValidMove(posX + cellSizeCol - 1 - speed, posY + cellSizeRow - 1) && isValidMove(posX - speed, posY + cellSizeRow - 1) && isValidMove(posX + cellSizeCol - 1 - speed, posY);
        canMoveRight = isValidMove(posX + speed, posY) && isValidMove(posX + cellSizeCol - 1 + speed, posY + cellSizeRow - 1) && isValidMove(posX + speed, posY + cellSizeRow - 1) && isValidMove(posX + cellSizeCol - 1 + speed, posY);
        switch (dir){
            case 0:
                if (canMoveUp){
                    move(0, -speed);
                } else {
                    dir = ran.nextInt(4);
                }
                break;
            case 1:
                if (canMoveDown){
                    move(0, speed);
                } else {
                    dir = ran.nextInt(4);
                }
                break;
            case 2:
                if (canMoveLeft){
                    move(-speed, 0);
                } else {
                    dir = ran.nextInt(4);
                }
                break;
            case 3:
                if (canMoveRight){
                    move(speed, 0);
                } else {
                    dir = ran.nextInt(4);
                }
                break;
        }
    }
}

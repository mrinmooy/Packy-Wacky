package com.salazarisaiahnoel.pacmanandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Play extends AppCompatActivity implements Runnable{

    //Drawing
    int fps = 60;
    Thread thread;
    SurfaceHolder surfaceHolder;
    SurfaceView sv;
    boolean running;
    int drawCount;

    //Values
    int cellSizeRow, cellSizeCol;
    int[][] map = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,2,1,1,1,1,1,2,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,2,2,1},
            {1,1,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,1,1},
            {0,0,0,0,1,2,1,2,2,2,2,2,2,2,1,2,1,0,0,0,0},
            {1,1,1,1,1,2,1,2,1,1,0,1,1,2,1,2,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,1,0,0,0,1,2,2,2,2,2,2,2,1},
            {1,1,1,1,1,2,1,2,1,0,1,0,1,2,1,2,1,1,1,1,1},
            {0,0,0,0,1,2,1,2,1,0,0,0,1,2,1,2,1,0,0,0,0},
            {1,1,1,1,1,2,1,2,1,1,1,1,1,2,1,2,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
            {1,2,2,2,1,2,2,2,2,2,0,2,2,2,2,2,1,2,2,2,1},
            {1,1,1,2,1,2,1,2,1,1,1,1,1,2,1,2,1,2,1,1,1},
            {1,2,2,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };
    int rows = 21, cols = 21;
    int width;
    Pacman pacman;
    static final int mup = 1, mdown = 2, mleft = 3, mright = 4;
    int direction = 0, pendingDirection = 0;
    boolean isGameFinished = false;
    int seconds = 0, minutes = 0;
    int randomDirection = 0;
    Ghost g1, g2, g3, g4;
    boolean gameOver = false;

    //Widgets
    ImageButton up, down, left, right;
    TextView timerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        cellSizeRow = width / rows;
        cellSizeCol = width / cols;

        if (cellSizeRow % 2 != 0){
            cellSizeRow--;
        }
        if (cellSizeCol % 2 != 0){
            cellSizeCol--;
        }

        sv = findViewById(R.id.sv);
        sv.getHolder().setFixedSize(width, width);
        surfaceHolder = sv.getHolder();
        sv.setFocusable(true);

        up = findViewById(R.id.up);
        down = findViewById(R.id.down);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        timerTV = findViewById(R.id.timer);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidDirection(mup)) {
                    direction = mup;
                } else {
                    pendingDirection = mup;
                }
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidDirection(mdown)) {
                    direction = mdown;
                } else {
                    pendingDirection = mdown;
                }
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidDirection(mleft)) {
                    direction = mleft;
                } else {
                    pendingDirection = mleft;
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidDirection(mright)) {
                    direction = mright;
                } else {
                    pendingDirection = mright;
                }
            }
        });

        pacman = new Pacman(this, map, cellSizeRow, cellSizeCol, 2);
        g1 = new Ghost(Color.RED, map, cellSizeRow, cellSizeCol, 1, 1, 9);
        g2 = new Ghost(Color.rgb(228, 142, 183), map, cellSizeRow, cellSizeCol, 2, 1, 11);
        g3 = new Ghost(Color.CYAN, map, cellSizeRow, cellSizeCol, 1, 0, 11);
        g4 = new Ghost(Color.rgb(235, 125, 0), map, cellSizeRow, cellSizeCol, 1, 2, 11);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fullscreen.enableFullscreen(this);
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void update(){
        if (pendingDirection != 0 && isValidDirection(pendingDirection)) {
            direction = pendingDirection;
            pendingDirection = 0;
        }

        pacmanTouchGhost(g1);
        pacmanTouchGhost(g2);
        pacmanTouchGhost(g3);
        pacmanTouchGhost(g4);

        switch (direction) {
            case 1:
                pacman.move(0, -pacman.speed);
                break;
            case 2:
                pacman.move(0, pacman.speed);
                break;
            case 3:
                pacman.move(-pacman.speed, 0);
                break;
            case 4:
                pacman.move(pacman.speed, 0);
                break;
            default:
                break;
        }

        if (seconds > 2 && seconds < 18){
            if (g1.goOutCounter != 4){
                g1.goOutside();
            }
        }
        if (seconds > 7 && seconds < 23){
            if (g2.goOutCounter != 4){
                g2.goOutside();
            }
        }
        if (seconds > 12 && seconds < 28){
            if (g3.goOutCounter != 4){
                g3.goOutside();
            }
        }
        if (seconds > 17 && seconds < 33){
            if (g4.goOutCounter != 4){
                g4.goOutside();
            }
        }

        if (g1.goOutCounter == 4){
            g1.moveRandomly();
        }
        if (g2.goOutCounter == 4){
            g2.moveRandomly();
        }
        if (g3.goOutCounter == 4){
            g3.moveRandomly();
        }
        if (g4.goOutCounter == 4){
            g4.moveRandomly();
        }
    }

    void pacmanTouchGhost(Ghost ghost){
        if (pacman.posX >= ghost.posX && pacman.posX <= ghost.posX + cellSizeCol
                && pacman.posY >= ghost.posY && pacman.posY <= ghost.posY + cellSizeRow){
            gameOver = true;
        }
        if (pacman.posX + pacman.cellSizeCol >= ghost.posX && pacman.posX + pacman.cellSizeCol <= ghost.posX + cellSizeCol
                && pacman.posY + pacman.cellSizeRow >= ghost.posY && pacman.posY + pacman.cellSizeRow <= ghost.posY + cellSizeRow){
            gameOver = true;
        }
    }

    boolean isValidDirection(int direction){
        switch (direction) {
            case 1:
                return isValidMoveTL(0, -pacman.speed) &&
                        isValidMoveTR(0, -pacman.speed) &&
                        isValidMoveBL(0, -pacman.speed) &&
                        isValidMoveBR(0, -pacman.speed);
            case 2:
                return isValidMoveTL(0, pacman.speed) &&
                        isValidMoveTR(0, pacman.speed) &&
                        isValidMoveBL(0, pacman.speed) &&
                        isValidMoveBR(0, pacman.speed);
            case 3:
                return isValidMoveTL(-pacman.speed, 0) &&
                        isValidMoveTR(-pacman.speed, 0) &&
                        isValidMoveBL(-pacman.speed, 0) &&
                        isValidMoveBR(-pacman.speed, 0);
            case 4:
                return isValidMoveTL(pacman.speed, 0) &&
                        isValidMoveTR(pacman.speed, 0) &&
                        isValidMoveBL(pacman.speed, 0) &&
                        isValidMoveBR(pacman.speed, 0);
            default:
                break;
        }
        return false;
    }

    boolean isValidMoveTL(int x, int y) {
        int newX = pacman.posX + x;
        int newY = pacman.posY + y;

        return isValidMove(newX, newY);
    }

    boolean isValidMoveTR(int x, int y) {
        int newX = pacman.posX + x + cellSizeCol;
        int newY = pacman.posY + y;

        return isValidMove(newX - 1, newY);
    }

    boolean isValidMoveBL(int x, int y) {
        int newX = pacman.posX + x;
        int newY = pacman.posY + y + cellSizeRow;

        return isValidMove(newX, newY - 1);
    }

    boolean isValidMoveBR(int x, int y) {
        int newX = pacman.posX + x + cellSizeCol;
        int newY = pacman.posY + y + cellSizeRow;

        return isValidMove(newX - 1, newY - 1);
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

    void drawMap(Canvas canvas){
        Paint paint = new Paint();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                int cellType = map[row][col];
                if (cellType == 1){
                    paint.setColor(Color.BLUE);
                    int left = col * cellSizeCol;
                    int top = row * cellSizeRow;
                    int right = left + cellSizeCol;
                    int bottom = top + cellSizeRow;
                    canvas.drawRect(left, top, right, bottom, paint);
                } else if (cellType == 2) {
                    paint.setColor(Color.WHITE);
                    int centerX = col * cellSizeCol + cellSizeCol / 2;
                    int centerY = row * cellSizeRow + cellSizeRow / 2;
                    int radius = cellSizeCol / 4;
                    canvas.drawCircle(centerX, centerY, radius, paint);
                }
            }
        }
    }

    boolean arePelletsRemaining() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (map[row][col] == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    String setTimer(){
        String sec = "";
        if (seconds == 59){
            seconds = 0;
            minutes++;
        } else {
            seconds++;
        }
        if (seconds < 10){
            sec = "0" + seconds;
        } else {
            sec = String.valueOf(seconds);
        }
        return minutes + ":" + sec;
    }

    @Override
    public void run() {
        double interval = 1000000000.0 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long cTime;
        long timer = 0;
        drawCount = 0;

        while (running) {
            if (!surfaceHolder.getSurface().isValid()){
                continue;
            }

            cTime = System.nanoTime();
            delta += (cTime - lastTime) / interval;
            timer += (cTime - lastTime);
            lastTime = cTime;

            if (delta > 1) {
                update();
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.BLACK);

                    if (!gameOver){
                        if (!arePelletsRemaining()) {
                            if (!isGameFinished){
                                Handler h = new Handler(Looper.getMainLooper());
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        seconds--;
                                        AlertDialog.Builder adb = new AlertDialog.Builder(Play.this)
                                                .setTitle("Game Complete!")
                                                .setMessage("You won! Time: " + setTimer())
                                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                });
                                        AlertDialog ad = adb.create();
                                        ad.show();
                                    }
                                });
                                isGameFinished = true;
                            }
                        } else {
                            drawMap(canvas);
                            g1.draw(canvas);
                            g2.draw(canvas);
                            g3.draw(canvas);
                            g4.draw(canvas);
                            pacman.draw(canvas);
                        }
                    } else {
                        if (!isGameFinished){
                            Handler h = new Handler(Looper.getMainLooper());
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    seconds--;
                                    AlertDialog.Builder adb = new AlertDialog.Builder(Play.this)
                                            .setTitle("Game Over!")
                                            .setMessage("You hit a ghost! Time: " + setTimer())
                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            });
                                    AlertDialog ad = adb.create();
                                    ad.show();
                                }
                            });
                            isGameFinished = true;
                        }
                    }

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isGameFinished){
                            timerTV.setText(setTimer());
                        }
                    }
                });
                randomDirection = (int) (Math.random() * 4);
                drawCount = 0;
                timer = 0;
            }
        }
    }
}
package com.jyashu;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

import java.io.*;

/**
 *
 * @author J Yashu
 */

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private static int highScore = 0;
    private static int score = 0;
    private static int level = 0;
    private static boolean firstRun = true;

    private boolean play = false;
    private boolean won = false;

    private int totalBricks;

    private Timer timer;

    private int playerX = 310;

    private int ballPosX = 330;
    private int ballPosY = 500;
    private int ballXDir = -1;
    private int ballYDir = -2;
    private int speedMultiplier = 1;

    private MapGenerator map;

    static {

        File theDir = new File("res/data");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        try (DataInputStream dataFile = new DataInputStream(new FileInputStream("res/data/log.dat"))) {
            boolean eof = false;
            while (!eof) {
                try {
                    firstRun = dataFile.readBoolean();
                    if (firstRun) {
                        firstRun = false;
                        try (DataOutputStream writeFile = new DataOutputStream(new FileOutputStream("res/data/log.dat"))) {
                            writeFile.writeBoolean(firstRun);
                            writeFile.writeInt(0);
                            writeFile.writeInt(0);
                            writeFile.writeInt(0);
                        } catch (IOException ignored) {

                        }
                    }
                    highScore = dataFile.readInt();
                    score = dataFile.readInt();
                    level = dataFile.readInt();
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (IOException ignored) {

        }
    }

    public Gameplay() {

        generateMap(level);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        int delay = 5;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void generateMap(int level) {
        int row, column;
        switch (level) {
            case 1:
                row = 4;
                column = 4;
                speedMultiplier = 2;
                break;
            case 2:
                row = 4;
                column = 6;
                speedMultiplier = 2;
                break;
            case 3:
                row = 4;
                column = 8;
                speedMultiplier = 2;
                break;
            case 4:
                row = 4;
                column = 10;
                speedMultiplier = 3;
                break;
            case 5:
                row = 4;
                column = 12;
                speedMultiplier = 3;
                break;
            case 6:
                row = 6;
                column = 12;
                speedMultiplier = 3;
                break;
            case 7:
                row = 8;
                column = 12;
                speedMultiplier = 4;
                break;
            case 8:
                row = 8;
                column = 14;
                speedMultiplier = 4;
                break;
            case 9:
                row = 8;
                column = 16;
                speedMultiplier = 5;
                break;
            case 0:
                row = 1;
                column = 4;
                speedMultiplier = 1;
                break;
            default:
                column = 16;
                row = 10;
                speedMultiplier = 5;
                break;
        }
        if (level > 10) {
            row = 12;
        }
        map = new MapGenerator(row, column);
        totalBricks = row * column;
        ballXDir = -1 * speedMultiplier;
        ballYDir = -2 * speedMultiplier;
    }

    public void paint(Graphics g) {

        Font fontBM = new Font("serif", Font.BOLD, 25);
        Font fontRM = new Font("serif", Font.PLAIN, 25);
        Font fontBL = new Font("serif", Font.BOLD, 30);
        Font fontBS = new Font("serif", Font.BOLD, 20);

        // background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 562);

        // footer
        g.setColor(Color.decode("#1D1D1D"));
        g.fillRect(1, 562, 692, 70);

        // drawing map
        map.draw((Graphics2D) g);

        // walls
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 3, 562);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(681, 0, 3, 562);

        // high score
        g.setColor(Color.WHITE);
        g.setFont(fontBM);
        g.drawString("High Score: " + highScore, 50, 595);

        // bricks left
        g.setColor(Color.CYAN);
        g.setFont(fontRM);
        g.drawString("Bricks: " + totalBricks, 50, 30);

        // the level
        g.drawString("Level: " + level, 320, 30);

        // the scores
        g.drawString("Score: " + score, 560, 30);

        // the player
        g.setColor(Color.decode("#A27131"));
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballPosX, ballPosY, 10, 10);

        // when player completes the level
        if (totalBricks <= 0) {

            try (DataOutputStream scoreFile = new DataOutputStream(new FileOutputStream("res/data/log.dat"))) {
                scoreFile.writeBoolean(false);
                scoreFile.writeInt(highScore);
                scoreFile.writeInt(score);
                scoreFile.writeInt(level + 1);
            } catch (IOException ignored) {

            }

            won = true;
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Level Completed", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press [Enter] to Start Next Level", 230, 350);
        }

        // when player loses the game
        if (ballPosY > 570) {

            play = false;
            ballXDir = 0;
            ballYDir = 0;

            // update the log
            try (DataOutputStream scoreFile = new DataOutputStream(new FileOutputStream("res/data/log.dat"))) {
                scoreFile.writeBoolean(false);
                if (score > highScore) {
                    scoreFile.writeInt(score);
                }
                scoreFile.writeInt(0);
                if (level > 0) {
                    scoreFile.writeInt(1);
                } else scoreFile.writeInt(0);
            } catch (IOException ignored) {

            }

            // display the stats
            g.setColor(Color.black);
            g.fillRect(1, 1, 692, 562);

            g.setColor(Color.RED);
            g.setFont(fontBL);
            if (score > highScore) g.drawString("New High Score: " + score, 190, 300);
            else g.drawString("Game Over, Scores: " + score, 190, 300);

            g.setFont(fontBS);
            g.drawString("Press [Enter] to Restart", 230, 350);
        }

        g.dispose();
    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

            if (playerX >= 584) {
                playerX = 584;
            } else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {

            if (playerX <= 10) {
                playerX = -10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballPosX = 330;
                ballPosY = 500;
                ballXDir = -1;
                ballYDir = -2;
                if (won) {
                    level++;
                    won = false;
                } else {
                    playerX = 310;
                    if (level > 0) {
                        level = 1;
                    }
                    if (score > highScore) {
                        highScore = score;
                    }
                    score = 0;
                }
                generateMap(level);

                repaint();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void moveRight() {
        play = true;
        playerX += 20 * speedMultiplier;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20 * speedMultiplier;
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {

            // check wall deflect
            if (ballPosX <= 0) ballXDir = -ballXDir;
            if (ballPosY <= 0) ballYDir = -ballYDir;
            if (ballPosX >= 670) ballXDir = -ballXDir;

            // check player deflect
            if (new Rectangle(ballPosX, ballPosY, 10, 10).intersects(new Rectangle(playerX, 550, 30, 8))) {
                ballYDir = -ballYDir;
                ballXDir = -2;
            } else if (new Rectangle(ballPosX, ballPosY, 10, 10).intersects(new Rectangle(playerX + 70, 550, 30, 8))) {
                ballYDir = -ballYDir;
                ballXDir = ballXDir + 1;
            } else if (new Rectangle(ballPosX, ballPosY, 10, 10).intersects(new Rectangle(playerX + 30, 550, 40, 8))) {
                ballYDir = -ballYDir;
            }

            // check map collision with the ball
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 10, 10);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            score += 5;
                            totalBricks--;

                            if (ballPosX + 9 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballXDir = -ballXDir;
                            } // when ball hit right or left of brick
                            else ballYDir = -ballYDir; // when ball hits top or bottom of brick

                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXDir;
            ballPosY += ballYDir;

            repaint();
        }
    }
}

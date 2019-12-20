import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import static javax.swing.SwingConstants.LEFT;

public class Board {

    private JFrame frame;
    private JLabel[][] board;
    private int width;
    private int height;
    int i, j;
    private boolean moving = false;

    public Board(int width, int height) {
        board = new JLabel[height][width];
        this.width = width;
        this.height = height;
        setup();
        show();
    }

    private void setup() {
        frame = new JFrame("Sliding Puzzle");
        frame.setLayout(new GridLayout(board.length, board[0].length));
        frame.setForeground(new Color(200,200,200));

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < width * height; i++) {
            numbers.add(i);
        }
        Random rand = new Random();
        for (int i = 0; i < width * height; i++) {
            int randomIndex = rand.nextInt(numbers.size());
            JLabel tile;
            if (numbers.get(randomIndex) != 0) {
                tile = new JLabel(String.valueOf(numbers.get(randomIndex)));
            } else {
                tile = new JLabel();
                this.i = i / width;
                this.j = i % width;
            }
            tile.setFont(new Font(null, 0, 40));
            tile.setBorder(BorderFactory.createLineBorder(Color.black));
            tile.setHorizontalAlignment(JLabel.CENTER);
            numbers.remove(randomIndex);
            frame.add(tile);
            board[i / width][i % width] = tile;
        }
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //System.out.println(keyEvent.getKeyCode());
                if (keyEvent.getKeyCode() == 17 + 82) { // ctrl + r
                    frame.dispose();
                } else {
                    if (!moving) {
                        moving = true;
                        move(keyEvent.getKeyCode());
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                moving = false;
            }
        });
    }

    private void move(int direction) { // LEFT: 37, UP: 38, RIGHT: 39, DOWN: 40
        final int LEFT = 37;
        final int UP = 38;
        final int RIGHT = 39;
        final int DOWN = 40;
        switch (direction) {
            case LEFT:
                if (j < 3) {
                    board[i][j].setText(board[i][j + 1].getText());
                    board[i][j + 1].setText("");
                    j++;
                }
                break;

            case RIGHT:
                if (j > 0) {
                    board[i][j].setText(board[i][j - 1].getText());
                    board[i][j - 1].setText("");
                    j--;
                }
                break;

            case UP:
                if (i < 3) {
                    board[i][j].setText(board[i + 1][j].getText());
                    board[i + 1][j].setText("");
                    i++;
                }
                break;

            case DOWN:
                if (i > 0) {
                    board[i][j].setText(board[i - 1][j].getText());
                    board[i - 1][j].setText("");
                    i--;
                }
                break;
        }
    }

    private void scramble() {

    }

    private void show() {
        frame.pack();
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}

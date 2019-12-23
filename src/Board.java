import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Timer;

public class Board {

    private final int LEFT = 37;
    private final int UP = 38;
    private final int RIGHT = 39;
    private final int DOWN = 40;

    private JFrame frame;
    private JLabel[][] board;
    private int width;
    private int height;
    private int i, j;
    private boolean boardCreated = false;
    private boolean started = false;
    private int time;
    private Timer t;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        board = new JLabel[height][width];
        setup();
        show();
    }

    private void setup() {
        frame = new JFrame("Sliding Puzzle");
        frame.setLayout(new GridLayout(height, width));
        frame.getContentPane().setBackground(Color.BLACK);

        reset();
        scramble();
        addListeners(frame);
    }

    private void reset() {
        started = false;
        if (boardCreated) {
            for (JLabel[] row : board) {
                for (JLabel tile : row) {
                    frame.remove(tile);
                }
            }
        }
        boardCreated = true;

        for (int i = 0; i < width * height - 1; i++) {
            JLabel tile = createTile(i + 1);
            frame.add(tile);
            board[i / width][i % width] = tile;
        }

        i = height - 1;
        j = width - 1;

        JLabel tile = createTile(0);
        tile.setText("");
        frame.add(tile);
        board[i][j] = tile;
    }

    private JLabel createTile(int text) {
        JLabel tile = new JLabel(String.valueOf(text));

        tile.setFont(new Font(null, Font.PLAIN, 40));
        tile.setForeground(Color.LIGHT_GRAY);
        tile.setHorizontalAlignment(JLabel.CENTER);
        tile.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        return tile;
    }

    private void addListeners(JFrame frame) {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println(keyEvent.getKeyCode());
                if (e.getKeyCode() == 82) { // r
                    reset();
                    scramble();
                } else {
                    if (!started) {
                        started = true;
                        t = new Timer( );
                        t.scheduleAtFixedRate(new TimerTask() {

                            @Override
                            public void run() {
                                setTime();
                            }
                        }, 1000,1000);
                    }
                    move(e.getKeyCode());
                }
            }
        });
    }
    private void setTime() {
        time++;
        frame.setTitle("Sliding Puzzle - " + time);
        boolean solved = true;
        for (int i = 0; i < height * width - 1; i++) {
            if (!board[i / height][i % width].getText().equals(String.valueOf(i + 1))) {
                solved = false;
                break;
            }
        }
        if (solved) {
            t.cancel();
            t.purge();
        }
    }

    private void move(int direction) { // LEFT: 37, UP: 38, RIGHT: 39, DOWN: 40
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
        int amount = 1000;
        Random rand = new Random();
        ArrayList<Integer> possible = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            if (this.j < 3) { possible.add(LEFT); }
            if (this.j > 0) { possible.add(RIGHT); }
            if (this.i < 3) { possible.add(UP); }
            if (this.i > 0) { possible.add(DOWN); }
            move(possible.get(rand.nextInt(possible.size())));
        }
    }

    private void show() {
        frame.pack();
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}

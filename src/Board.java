import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private boolean currentlyPlaying = false;

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMenuBar();

        newGame();
        addMovementListeners();
    }

    private void show() {
        frame.pack();
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    private void addMovementListeners() {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 82) { // r
                    newGame();
                } else {
                    if (!currentlyPlaying) { // Start timer if game is not running
                        startTimer();
                    }
                    int direction = e.getKeyCode();
                    if (legal(direction)) { // Move if legal
                        move(direction);
                    }
                    checkSolved();
                }
            }
        });
    }

    private void startTimer() {
        currentlyPlaying = true;
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                time++;
                frame.setTitle("Sliding Puzzle - " + time);
            }
        }, 1000,1000);
    }

    private void checkSolved() {
        boolean solved = true;
        for (int i = width * height - 2; i >=  0; i--) { // Check if all tiles are correct value
            if (!board[i / height][i % width].getText().equals(String.valueOf(i + 1))) {
                solved = false;
                break;
            }
        }
        if (solved) { // Stop timer
            t.cancel();
            t.purge();
            frame.setTitle("Sliding Puzzle (Solved) - " + time);
            frame.getContentPane().setBackground(new Color(10, 40, 10));
        }
    }

    private void setMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(actionEvent -> newGame());
        menuFile.add(newGameItem);

        JMenuItem setSizeItem = new JMenuItem("Set Size");
        setSizeItem.addActionListener(actionEvent -> setSize());
        menuFile.add(setSizeItem);
    }

    private void setSize() {
        JTextField widthField = new JTextField();
        JTextField heightField = new JTextField();
        Object[] size = {
                "Width:", widthField,
                "Height:", heightField
        };
        int option = JOptionPane.showConfirmDialog(frame, size, "Enter new size", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            this.width = Integer.parseInt(widthField.getText());
            this.height = Integer.parseInt(heightField.getText());

            newGame();
        }
    }

    private void reset() {
        currentlyPlaying = false;
        if (boardCreated) { // If there is a board, remove it
            for (JLabel[] row : board) {
                for (JLabel tile : row) {
                    frame.remove(tile);
                }
            }
        }
        boardCreated = true;
        time = 0;

        board = new JLabel[height][width];

        for (int i = 0; i < width * height - 1; i++) { // Create tiles
            JLabel tile = new Tile(i + 1);
            frame.add(tile);
            board[i / width][i % width] = tile;
        }

        i = height - 1;
        j = width - 1;

        JLabel tile = new Tile(0);
        tile.setText("");
        frame.add(tile);
        board[i][j] = tile;
    }

    public void newGame() {
        if (currentlyPlaying) {
            frame.setTitle("Sliding Puzzle");
            t.cancel();
            t.purge();
        }
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new GridLayout(height, width));

        reset();
        scramble();
    }

    private void move(int direction) { // LEFT: 37, UP: 38, RIGHT: 39, DOWN: 40
        switch (direction) { // Swap text from target direction
            case LEFT:
                board[i][j].setText(board[i][j + 1].getText());
                board[i][j + 1].setText("");
                j++;
                break;

            case RIGHT:
                board[i][j].setText(board[i][j - 1].getText());
                board[i][j - 1].setText("");
                j--;
                break;

            case UP:
                board[i][j].setText(board[i + 1][j].getText());
                board[i + 1][j].setText("");
                i++;
                break;

            case DOWN:
                board[i][j].setText(board[i - 1][j].getText());
                board[i - 1][j].setText("");
                i--;
                break;
        }
    }

    private void scramble() {
        int amount = width * height * 20;
        Random rand = new Random();

        int counter = 0;
        while (counter < amount) {
            int direction = rand.nextInt(4) + 37;
            if (legal(direction)) {
                move(direction);
                counter++;
            }
        }
    }

    private boolean legal(int direction) {
        switch (direction) {
            case LEFT:
                return j < width - 1;
            case RIGHT:
                return j > 0;
            case UP:
                return i < height - 1;
            case DOWN:
                return i > 0;
        }
        return false;
    }
}

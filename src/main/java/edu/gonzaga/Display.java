package edu.gonzaga;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Display extends JPanel implements ActionListener {
    private int GAME_WIDTH = 1000;
    private int GAME_HEIGHT = 1000;

    // defaults and initialize
    private int gameSpeed = 200;
    private int obstacle = 0;
    private boolean speedSelected = false;
    private boolean obstacleSelected = false;
    private JButton selectedSpeedButton = null;
    private JButton selectedObstacleButton = null;


    private ImageIcon backgroundImage;

    private Music mp;

    public Display() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(Color.green);
        backgroundImage = new ImageIcon("assets/background.gif");
        mp = new Music();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage.getImage(), 0, 0, GAME_WIDTH, GAME_HEIGHT, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public void startMenu(JFrame frame) {
        JPanel startWindow = new JPanel();
        startWindow.setLayout(new BoxLayout(startWindow, BoxLayout.Y_AXIS));
        startWindow.setBackground(Color.black);

        startWindow.add(Box.createRigidArea(new Dimension(0, 300)));

        JLabel title = new JLabel("SNAKE GAME");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.white);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        startWindow.add(title);

        startWindow.add(Box.createRigidArea(new Dimension(0, 20)));

        // settings
        selection("SELECT SPEED:", startWindow);
        startWindow.add(speedButtons());

        selection("SELECT OBSTACLE:", startWindow);
        startWindow.add(obstacleButtons());

        startWindow.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton startGame = new JButton("START");
        startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGame.addActionListener(event -> {
            if (!speedSelected || !obstacleSelected) {
                JOptionPane.showMessageDialog(frame, "Select both speed and obstacle level.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                frame.dispose();
                startGame();
            }
        });
        startGame.setFont(new Font("Arial", Font.BOLD, 30));
        startWindow.add(startGame);

        startWindow.add(Box.createRigidArea(new Dimension(0, 300)));

        frame.add(startWindow);
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // sub headers
    private void selection(String text, JPanel panel) {
        JLabel subtitle = new JLabel(text);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(Color.green);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitle);
    }

    // speed settings
    private JPanel speedButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.setBackground(Color.black);

        JButton easy = createButton("EASY");
        easy.addActionListener(event -> selectSpeed(easy, 200));

        JButton normal = createButton("MEDIUM");
        normal.addActionListener(event -> selectSpeed(normal, 150));

        JButton hard = createButton("HARD");
        hard.addActionListener(event -> selectSpeed(hard, 75));

        buttons.add(easy);
        buttons.add(normal);
        buttons.add(hard);

        return buttons;
    }

    // selected speed
    private void selectSpeed(JButton button, int speed) {
        if (selectedSpeedButton != null) {
            selectedSpeedButton.setBackground(Color.white);
        }
        selectedSpeedButton = button;
        selectedSpeedButton.setBackground(Color.green);
        gameSpeed = speed;
        speedSelected = true;
    }


    // obstacle settings
    private JPanel obstacleButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.setBackground(Color.black);

        JButton easy = createButton("EASY");
        easy.addActionListener(event -> selectObstacle(easy, 0));

        JButton medium = createButton("MEDIUM");
        medium.addActionListener(event -> selectObstacle(medium, 4));

        JButton hard = createButton("HARD");
        hard.addActionListener(event -> selectObstacle(hard, 8));

        buttons.add(easy);
        buttons.add(medium);
        buttons.add(hard);

        return buttons;
    }

    // selected obstacle
    private void selectObstacle(JButton button, int obs) {
        if (selectedObstacleButton != null) {
            selectedObstacleButton.setBackground(Color.gray);
        }
        selectedObstacleButton = button;
        selectedObstacleButton.setBackground(Color.green);
        obstacle = obs;
        obstacleSelected = true;
    }
    
    // hovering animation
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.white);
        button.setForeground(Color.black);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setFont(new Font("Arial", Font.BOLD, 22));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setFont(new Font("Arial", Font.BOLD, 18));
            }
        });

        return button;
    }

    private void startGame() {
        Board board = new Board(obstacle);
        Display display = new Display();

        try {
            mp.loopSound("assets/music.wav");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error playing background music: " + e.getMessage(), "Audio Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // create graphical layout
                BoardLayout layout = new BoardLayout(board, display);

                //timer to control snake movement/game updates
                new Timer(gameSpeed, e -> {
                    board.turnSnake(board.getSnake().getFacing());
                    if (!board.tick()) {
                        gameOverMenu();
                        System.out.println("Game Over!");
                        ((Timer) e.getSource()).stop();
                    } else {
                        layout.updateGame();
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void gameOverMenu() {
        JFrame frame = new JFrame("Game Over");
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
        gameOverPanel.setBackground(Color.black);
    
        // title
        JLabel gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverLabel.setForeground(Color.red);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        gameOverPanel.add(gameOverLabel);

        mp.stopMusic();
    
        // try again button
        JButton tryAgainButton = new JButton("Try Again");
        tryAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // restarts 
        tryAgainButton.addActionListener(event -> {
            frame.dispose(); 
            startGame(); 
        });
    
        // returns to main menu
        JButton menuButton = new JButton("Main Menu");
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.addActionListener(event -> {
            frame.dispose();
            JFrame mainMenuFrame = new JFrame("Snake Game");
            mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            startMenu(mainMenuFrame);
        });
    
        // exits
        JButton exitButton = new JButton("Exit Game");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(event -> System.exit(0));

        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        gameOverPanel.add(tryAgainButton);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(menuButton);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(exitButton);

        frame.add(gameOverPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
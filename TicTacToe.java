import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TicTacToe extends JFrame {

    private JButton[][] buttons;
    private boolean player1Turn;
    private boolean gameOver;
    boolean AIMode;
    JPanel gamePanel = new JPanel();

    public TicTacToe() {
        setTitle("Tic-Tac-Toe");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        buttons = new JButton[3][3];
        player1Turn = true;
        gameOver = false;
        AIMode = false;

        // Initialize buttons
       
        gamePanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                gamePanel.add(buttons[i][j]);
            }
        }

        // Add game panel to center of JFrame
        add(gamePanel, BorderLayout.CENTER);

        // Create toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // Add Help button to toolbar
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "This is a simple Tic-Tac-Toe game.\n The objective of the game is to get three of your symbols (either 'X' or 'O') in a row,\n column, or diagonal. Players take turns placing their symbols on an empty spot on the 3x3 game board.\n The first player to get three of their symbols in a row, column, or diagonal wins the game.\n If all spots on the board are filled and no player has three symbols in a row, the game is a draw.", "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        toolBar.add(helpButton);

        // Add Rules button to toolbar
        JButton rulesButton = new JButton("Rules");
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "1. Players take turns placing their symbols ('X' or 'O') on an empty spot on the 3x3 game board.\n2. The first player to get three of their symbols in a row, column, or diagonal wins the game.\n3. If all spots on the board are filled and no player has three symbols in a row, the game is a draw.", "Rules", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        toolBar.add(rulesButton);

        // Add Play with AI button to toolbar
        JButton aiButton = new JButton("Play with AI");
        aiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (AIMode) {
                    AIMode = false;
                    aiButton.setText("AIMode Off");
                    JOptionPane.showMessageDialog(null, "AIMode OFF", "Play with AI", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    AIMode = true;
                    aiButton.setText("AIMode On");
                    JOptionPane.showMessageDialog(null, "AIMode On", "Play with AI", JOptionPane.INFORMATION_MESSAGE);
                }
                
            }
        });
        toolBar.add(aiButton);

        // Add toolbar to the top of the JFrame
        add(toolBar, BorderLayout.NORTH);
    }

    private class ButtonClickListener implements ActionListener {

        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        private void resetGame() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setEnabled(true);
                }
            }
            player1Turn = true;
            gameOver = false;
        }

        private void makeMove() {
            // Get all unpressed buttons
            ArrayList<JButton> unpressedButtons = new ArrayList<>();
            for (Component component : gamePanel.getComponents()) {
                if (component instanceof JButton) {
                    JButton button = (JButton) component;
                    if (button.getText().isEmpty()) {
                        unpressedButtons.add(button);
                    }
                }
            }

            // Randomly select an unpressed button and set its text to "O"
            if (!checkWinner()) {
                if (!unpressedButtons.isEmpty()) {
                    int randomIndex = (int) (Math.random() * unpressedButtons.size());
                    JButton randomButton = unpressedButtons.get(randomIndex);
                    randomButton.setText("O"); // Set text to "O"
                    randomButton.doClick();
                }
            }  
        }


        public boolean checkWinner() {
            if (checkWin()) {
                String winner = player1Turn ? "Player 2 (O)" : "Player 1 (X)";
                JOptionPane.showMessageDialog(null, winner + " wins!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                gameOver = true;
                resetGame();
                return true;
            } else if (checkDraw()) {
                JOptionPane.showMessageDialog(null, "It's a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                gameOver = true;
                resetGame();
                return true;
            }
            return false;
        }
        

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            if (!gameOver && clickedButton.getText().isEmpty()) {
        
            
                if (player1Turn) {
                    clickedButton.setText("X");
                    if (AIMode) {
                        player1Turn = !player1Turn;
                        if (!checkWinner()) {
                            makeMove();
                            player1Turn = true;
                            checkWinner();
                            return;
                        }
                    }
                }else {
                    clickedButton.setText("O");
                }
                player1Turn = !player1Turn;

                checkWinner();
            }
        }
    }

    private boolean checkWin() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].isEmpty()) {
                return true; // check rows
            }
            if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].isEmpty()) {
                return true; // check columns
            }
        }

        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].isEmpty()) {
            return true; // check diagonal 1
        }
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].isEmpty()) {
            return true; // check diagonal 2
        }

        return false;
    }

        private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    return false; // if any button is empty, game is not a draw
                }
            }
        }
        return true; // all buttons are filled, game is a draw
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToe game = new TicTacToe();
            game.setVisible(true);
        });
    }
}


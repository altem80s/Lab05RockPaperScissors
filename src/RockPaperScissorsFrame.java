import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI frame for the Rock Paper Scissors game.
 * Handles the user interface, game statistics, and strategies.
 *
 * @author
 * @version 1.0
 */
public class RockPaperScissorsFrame extends JFrame
{
    // Game buttons
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;
    private JButton quitButton;

    // Statistics fields
    private JTextField playerWinsField;
    private JTextField computerWinsField;
    private JTextField tiesField;

    // Results display
    private JTextArea resultsArea;

    // Game statistics
    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;

    // Player move counts
    private int rockCount = 0;
    private int paperCount = 0;
    private int scissorsCount = 0;

    // Previous player move
    private String lastPlayerMove = "";

    // Strategies
    private Strategy randomStrategy;
    private Strategy cheatStrategy;
    private Strategy leastUsedStrategy;
    private Strategy mostUsedStrategy;
    private Strategy lastUsedStrategy;

    /**
     * Constructs the Rock Paper Scissors game window.
     */
    public RockPaperScissorsFrame()
    {
        setTitle("Rock Paper Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        buildGUI();
        createStrategies();
    }

    /**
     * Builds all of the GUI components.
     */
    private void buildGUI()
    {
        setLayout(new BorderLayout(10, 10));

        // ---------------------------------------
        // Title
        // ---------------------------------------

        JLabel titleLabel = new JLabel(
                "Rock Paper Scissors Game",
                SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        add(titleLabel, BorderLayout.NORTH);

        // ---------------------------------------
        // Button Panel
        // ---------------------------------------

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBorder(
                BorderFactory.createTitledBorder("Choose Your Move")
        );

        rockButton = new JButton("Rock");
        paperButton = new JButton("Paper");
        scissorsButton = new JButton("Scissors");
        quitButton = new JButton("Quit");

        ImageIcon rockIcon = new ImageIcon("src/rock.png");
        ImageIcon paperIcon = new ImageIcon("src/paper.png");
        ImageIcon scissorsIcon = new ImageIcon("src/scissors.png");

        rockButton.setIcon(new ImageIcon(
                rockIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)
        ));

        paperButton.setIcon(new ImageIcon(
                paperIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)
        ));

        scissorsButton.setIcon(new ImageIcon(
                scissorsIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)
        ));

        rockButton.setPreferredSize(new Dimension(140, 100));
        paperButton.setPreferredSize(new Dimension(140, 100));
        scissorsButton.setPreferredSize(new Dimension(140, 100));
        quitButton.setPreferredSize(new Dimension(100, 40));

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.CENTER);

        // ---------------------------------------
        // Statistics Panel
        // ---------------------------------------

        JPanel statsPanel = new JPanel();
        statsPanel.setBorder(
                BorderFactory.createTitledBorder("Game Statistics")
        );

        playerWinsField = new JTextField("0", 5);
        computerWinsField = new JTextField("0", 5);
        tiesField = new JTextField("0", 5);

        playerWinsField.setEditable(false);
        computerWinsField.setEditable(false);
        tiesField.setEditable(false);

        statsPanel.add(new JLabel("Player Wins:"));
        statsPanel.add(playerWinsField);

        statsPanel.add(new JLabel("Computer Wins:"));
        statsPanel.add(computerWinsField);

        statsPanel.add(new JLabel("Ties:"));
        statsPanel.add(tiesField);

        add(statsPanel, BorderLayout.SOUTH);

        // ---------------------------------------
        // Results Panel
        // ---------------------------------------

        resultsArea = new JTextArea(10, 60);
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultsArea);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Game Results")
        );

        add(scrollPane, BorderLayout.WEST);

        // ---------------------------------------
        // One ActionListener for R, P, and S
        // ---------------------------------------

        ActionListener moveListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                String playerMove;

                if (ae.getSource() == rockButton)
                {
                    playerMove = "R";
                }
                else if (ae.getSource() == paperButton)
                {
                    playerMove = "P";
                }
                else
                {
                    playerMove = "S";
                }

                playGame(playerMove);
            }
        };

        rockButton.addActionListener(moveListener);
        paperButton.addActionListener(moveListener);
        scissorsButton.addActionListener(moveListener);

        // ---------------------------------------
        // Quit button
        // ---------------------------------------

        quitButton.addActionListener(e ->
        {
            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to quit?",
                    "Quit Game",
                    JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        });
    }

    /**
     * Creates instances of all game strategies.
     */
    private void createStrategies()
    {
        randomStrategy = new Random();
        cheatStrategy = new Cheat();

        leastUsedStrategy = new LeastUsed();
        mostUsedStrategy = new MostUsed();
        lastUsedStrategy = new LastUsed();
    }

    /**
     * Plays one round of Rock Paper Scissors.
     *
     * @param playerMove the player's selected move
     */
    private void playGame(String playerMove)
    {
        // Keep track of the player's move.
        updatePlayerMoveCount(playerMove);

        // Generate probability from 1 to 100.
        int probability = (int)(Math.random() * 100) + 1;

        Strategy selectedStrategy;
        String strategyName;

        // 1 - 10: Cheat
        if (probability <= 10)
        {
            selectedStrategy = cheatStrategy;
            strategyName = "Cheat";
        }

        else if (probability <= 30)
        {
            selectedStrategy = leastUsedStrategy;
            strategyName = "Least Used";
        }


        else if (probability <= 50)
        {
            selectedStrategy = mostUsedStrategy;
            strategyName = "Most Used";
        }


        else if (probability <= 70)
        {

            if (lastPlayerMove.equals(""))
            {
                selectedStrategy = randomStrategy;
                strategyName = "Random";
            }
            else
            {
                selectedStrategy = lastUsedStrategy;
                strategyName = "Last Used";
            }
        }


        else
        {
            selectedStrategy = randomStrategy;
            strategyName = "Random";
        }

        // Determine computer move.
        String computerMove = selectedStrategy.getMove(playerMove);

        // Determine result.
        String result = determineWinner(playerMove, computerMove);

        // Update statistics.
        if (result.equals("Player wins"))
        {
            playerWins++;
        }
        else if (result.equals("Computer wins"))
        {
            computerWins++;
        }
        else
        {
            ties++;
        }

        updateStatistics();

        // Create result message.
        String resultMessage = createResultMessage(
                playerMove,
                computerMove,
                result,
                strategyName
        );

        // Add result to text area.
        resultsArea.append(resultMessage + "\n");

        // Remember the player's move for the next round.
        lastPlayerMove = playerMove;
    }


    private void updatePlayerMoveCount(String playerMove)
    {
        if (playerMove.equals("R"))
        {
            rockCount++;
        }
        else if (playerMove.equals("P"))
        {
            paperCount++;
        }
        else if (playerMove.equals("S"))
        {
            scissorsCount++;
        }
    }

    private String determineWinner(String playerMove, String computerMove)
    {
        if (playerMove.equals(computerMove))
        {
            return "Tie";
        }

        if (playerMove.equals("R") && computerMove.equals("S"))
        {
            return "Player wins";
        }

        if (playerMove.equals("P") && computerMove.equals("R"))
        {
            return "Player wins";
        }

        if (playerMove.equals("S") && computerMove.equals("P"))
        {
            return "Player wins";
        }

        return "Computer wins";
    }

    private String createResultMessage(
            String playerMove,
            String computerMove,
            String result,
            String strategyName)
    {
        String playerName = moveName(playerMove);
        String computerName = moveName(computerMove);

        String description;

        if (playerMove.equals(computerMove))
        {
            description = playerName + " vs " + computerName + ".";
        }
        else if (playerMove.equals("R") && computerMove.equals("S"))
        {
            description = "Rock breaks scissors.";
        }
        else if (playerMove.equals("S") && computerMove.equals("P"))
        {
            description = "Scissors cuts paper.";
        }
        else if (playerMove.equals("P") && computerMove.equals("R"))
        {
            description = "Paper covers rock.";
        }
        else if (computerMove.equals("R") && playerMove.equals("S"))
        {
            description = "Rock breaks scissors.";
        }
        else if (computerMove.equals("S") && playerMove.equals("P"))
        {
            description = "Scissors cuts paper.";
        }
        else
        {
            description = "Paper covers rock.";
        }

        String resultText;

        if (result.equals("Tie"))
        {
            resultText = "Tie!";
        }
        else if (result.equals("Player wins"))
        {
            resultText = "Player wins!";
        }
        else
        {
            resultText = "Computer wins!";
        }

        return description + " (" + resultText
                + " Computer: " + strategyName + ")";
    }


    private String moveName(String move)
    {
        switch (move)
        {
            case "R":
                return "Rock";

            case "P":
                return "Paper";

            case "S":
                return "Scissors";

            default:
                return "Unknown";
        }
    }


    private void updateStatistics()
    {
        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(ties));
    }


    private class LeastUsed implements Strategy
    {

        @Override
        public String getMove(String playerMove)
        {
            if (rockCount <= paperCount && rockCount <= scissorsCount)
            {
                return "P";
            }
            else if (paperCount <= rockCount && paperCount <= scissorsCount)
            {
                return "S";
            }
            else
            {
                return "R";
            }
        }
    }

    private class MostUsed implements Strategy
    {

        @Override
        public String getMove(String playerMove)
        {
            if (rockCount >= paperCount && rockCount >= scissorsCount)
            {
                return "P";
            }
            else if (paperCount >= rockCount && paperCount >= scissorsCount)
            {
                return "S";
            }
            else
            {
                return "R";
            }
        }
    }

    private class LastUsed implements Strategy
    {

        @Override
        public String getMove(String playerMove)
        {
            if (lastPlayerMove.equals("R"))
            {
                return "P";
            }
            else if (lastPlayerMove.equals("P"))
            {
                return "S";
            }
            else
            {
                return "R";
            }
        }
    }
}
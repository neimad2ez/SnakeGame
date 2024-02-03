import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
  static final int SCREEN_WIDTH = 600;
  static final int SCREEN_HEIGHT = 600;
  static final int UNIT_SIZE = 25;
  static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
  static final int DELAY = 75; //how fast/slow your game is
  final int x[] = new int[GAME_UNITS]; //holds x co-ordinates of body parts of snake
  final int y[] = new int[GAME_UNITS]; //holds y co-ordinates of body parts of snake
  int bodyParts = 6; //begin with 6 bodyParts
  int applesEaten;
  int appleX; //x co-ordinate of where apple is located
  int appleY; //y co-ordinate of where apple is located
  char direction = 'R'; //snake starts off to the right
  boolean running = false;
  Timer timer;
  Random random;
  public GamePanel() {
    random = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.black);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());
    startGame(); //run game
  }

  public void startGame() {
    newApple(); //new apple on the map
    running = true; //game is now running
    timer = new Timer(DELAY, this); //how fast game is running
    timer.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    if(running) {
      for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
        g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); //draws lines vertically
        g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); //draws lines horizontally
      }
      g.setColor(Color.red);
      g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

      for (int i = 0; i < bodyParts; i++) {
        if (i == 0) {
          g.setColor(Color.GREEN);
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        } else {
          g.setColor(new Color(45, 180, 0));
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
      }
    } else {
      gameOver(g);
    }
    g.setColor(Color.red);
    g.setFont(new Font("Ink Free", Font.BOLD, 40));
    FontMetrics metrics = getFontMetrics(g.getFont());
    g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    //display score on the top of the screen
  }

  public void newApple() {
    appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
    appleY = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
  }

  public void move() {
    for (int i = bodyParts; i> 0; i--) { //iterates through all body parts and moves it
      x[i] = x[i-1]; //changes x and y array to the right
      y[i] = y[i-1];
    }

    switch(direction) {
      case 'U': //up
        y[0] = y[0] - UNIT_SIZE;
        break;
      case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;
      case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
      case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;
    }
  }

  public void checkApple() {
    if ((x[0]) == appleX && y[0] == appleY) {
      bodyParts++;
      applesEaten++;
      newApple();
    }
  }

  public void checkCollisions() {
    //checks if head collides with body
    for (int i = bodyParts; i > 0; i--) {
      if ((x[0] == x[i] && (y[0] == y[i]))) {
        running = false;
      }
    }
    //checks if head touches left border
    if (x[0] < 0) {
      running = false;
    }
    //checks if head touches right border
    if (x[0] > SCREEN_WIDTH) {
      running = false;
    }
    //checks if head touches top border
    if (y[0] < 0) {
      running = false;
    }
    //checks if head touches bottom border
    if (y[0] > SCREEN_HEIGHT) {
      running = false;
    }

    if (!running) {
      timer.stop();
    }
  }

  public void gameOver(Graphics g) {
    //score
    g.setColor(Color.red);
    g.setFont(new Font("Ink Free", Font.BOLD, 40));
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    //game over text
    g.setColor(Color.red);
    g.setFont(new Font("Ink Free", Font.BOLD, 75));
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    //x: SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2 makes the game over text appear in the middle of the
    //program rather than the middle of the actual screen
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (running) {
      move();
      checkApple();
      checkCollisions();
    }
    repaint();
  }

  public class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT: //left arrow
          if (direction != 'R') { //if direction is right then it can't turn 180 degrees
            direction = 'L'; //hence why direction != 'R'
          }
          break;
        case KeyEvent.VK_RIGHT: //right arrow
          if (direction != 'L') {
            direction = 'R';
          }
          break;
        case KeyEvent.VK_UP: //up arrow
          if (direction != 'D') {
            direction = 'U';
          }
          break;
        case KeyEvent.VK_DOWN: //down arrow
          if (direction != 'U') {
            direction = 'D';
          }
          break;
      }
    }
  }
}

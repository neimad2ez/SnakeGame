import javax.swing.JFrame;

public class GameFrame extends JFrame {

  public GameFrame() {
    GamePanel panel = new GamePanel();
    this.add(panel);
    this.setTitle("Snake");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.pack(); //add components, takes JFrame and fit it snuggly around all components in frame
    this.setVisible(true);
    this.setLocationRelativeTo(null); //window appears in the middle of the computer screen
  }

}

package view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

////////////////////////////////////////
// CLASS FerretSuiteTabTest
////////////////////////////////////////
public class FerretSuiteTabTest {
public static void main(String[] args) {
  SwingUtilities.invokeLater(() -> {
    JFrame frame = new JFrame("Ferret Suite Tab Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new FerretSuiteTab(null));
    frame.pack();
    frame.setLocationRelativeTo(null); // Center the frame on the screen
    frame.setVisible(true);
  });
}
}
////////////////////////////////////////
// END CLASS FerretSuiteTabTest
////////////////////////////////////////

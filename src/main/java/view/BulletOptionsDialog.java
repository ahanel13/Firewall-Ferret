package view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

////////////////////////////////////////
// CLASS BulletOptionsDialog
////////////////////////////////////////
public class BulletOptionsDialog extends JDialog {


////////////////////////////////////////
// PUBLIC METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
public BulletOptionsDialog(Frame burpFrame) {
  super(burpFrame, "Bullet Size Options", true);
  JPanel optionsPanel = createOptionsPanel();
  
  setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
  setLocationRelativeTo(burpFrame); // Set location relative to the parent frame
  add(optionsPanel);
}

//-----------------------------------------------------------------------------
public void showDialog() {
  this.setVisible(true);
}

//-----------------------------------------------------------------------------
public int getByteSize() {
  if (selectedSize == null) {
    return -1;
  }
  if (selectedSize.endsWith("KB")) {
    return Integer.parseInt(selectedSize.split(" ")[0]) * 1024;
  } else {
    return Integer.parseInt(selectedSize);
  }
}


////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private static final int    DIALOG_WIDTH  = 250;
private static final int    DIALOG_HEIGHT = 100;
private static final String CUSTOM_OPTION = "Custom";

private JComboBox<String> dropdown;
private JTextField        customSizeField;
private JLabel            customSizeLabel;
private String            selectedSize;


////////////////////////////////////////
// PRIVATE METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
private JPanel createOptionsPanel() {
  JPanel optionsPanel = new JPanel();
  optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

  dropdown        = createSizeDropdown();
  customSizeField = createCustomSizeField();
  customSizeLabel = createCustomSizeLabel();
  
  optionsPanel.add(dropdown);
  optionsPanel.add(customSizeLabel);
  optionsPanel.add(customSizeField);
  optionsPanel.add(createButtonPanel());
  
  return optionsPanel;
}

//-----------------------------------------------------------------------------
private JComboBox<String> createSizeDropdown() {
  String[] junkSizesKB = {"8 KB", "16 KB", "32 KB", "64 KB", "128 KB", "1024 KB", CUSTOM_OPTION};
  JComboBox<String> dropdown = new JComboBox<>(junkSizesKB);
  
  dropdown.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      boolean isCustom = CUSTOM_OPTION.equals(dropdown.getSelectedItem());
      customSizeField.setVisible(isCustom);
      customSizeLabel.setVisible(isCustom);
      revalidate();
      repaint();
    }
  });
  
  return dropdown;
}

//-----------------------------------------------------------------------------
private JTextField createCustomSizeField() {
  JTextField field = new JTextField(10);
  field.setVisible(false);
  return field;
}

//-----------------------------------------------------------------------------
private JLabel createCustomSizeLabel() {
  JLabel label = new JLabel("Custom size (bytes):");
  label.setVisible(false);
  return label;
}

//-----------------------------------------------------------------------------
private JPanel createButtonPanel() {
  JPanel buttonPanel = new JPanel();
  
  JButton okButton = new JButton("OK");
  okButton.addActionListener(e->handleOkButtonAction());
  
  JButton cancelButton = new JButton("Cancel");
  cancelButton.addActionListener(e->handleCancelButtonAction());
  
  buttonPanel.add(okButton);
  buttonPanel.add(cancelButton);
  
  return buttonPanel;
}

//-----------------------------------------------------------------------------
private void handleOkButtonAction() {
  if (CUSTOM_OPTION.equals(dropdown.getSelectedItem())) {
    selectedSize = customSizeField.getText();
  } else {
    selectedSize = (String) dropdown.getSelectedItem();
  }
  dispose();
}

//-----------------------------------------------------------------------------
private void handleCancelButtonAction() {
  selectedSize = null;
  dispose();
}

}
////////////////////////////////////////
// END CLASS BulletOptionsDialog
////////////////////////////////////////


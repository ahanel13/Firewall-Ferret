package view;

import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

////////////////////////////////////////
// CLASS FerretSuiteTab
////////////////////////////////////////
public class FerretSuiteTab extends JPanel {

////////////////////////////////////////
// PUBLIC METHODS
////////////////////////////////////////
public FerretSuiteTab() {
  super(new GridBagLayout());
  JPanel info = getAppInfo();
  JPanel settings = getSettingsPanel();
  
  GridBagConstraints infoCons = new GridBagConstraints();
  infoCons.gridx = 0;
  infoCons.gridy = 0;
  infoCons.weightx = 1.0;
  infoCons.fill = GridBagConstraints.HORIZONTAL;
  infoCons.insets = new Insets(10, 10, 10, 10);
  
  GridBagConstraints settingsCons = new GridBagConstraints();
  settingsCons.gridx = 0;
  settingsCons.gridy = 1;
  settingsCons.weightx = 1.0;
  settingsCons.fill = GridBagConstraints.HORIZONTAL;
  settingsCons.insets = new Insets(10, 10, 10, 10);
  
  add(info, infoCons);
  add(settings, settingsCons);
}

//-----------------------------------------------------------------------------
public void registerListener(ActionListener l){
  checkBox8kb.addActionListener(l);
  checkBox16kb.addActionListener(l);
  checkBox32kb.addActionListener(l);
  checkBox64kb.addActionListener(l);
  checkBox128kb.addActionListener(l);
  checkBox1024kb.addActionListener(l);
  customSizeCheckBox.addActionListener(l);
}

//-----------------------------------------------------------------------------
public Integer getCustomInt(){
  try {
    return Integer.parseInt(customSizeTextField.getText());
  }
  catch(NumberFormatException e){
    return 0;
  }
}

//-----------------------------------------------------------------------------
public boolean is8kbSelected() {
  return checkBox8kb.isSelected();
}

//-----------------------------------------------------------------------------
public boolean is16kbSelected() {
  return checkBox16kb.isSelected();
}

//-----------------------------------------------------------------------------
public boolean is32kbSelected() {
  return checkBox32kb.isSelected();
}

//-----------------------------------------------------------------------------
public boolean is64kbSelected() {
  return checkBox64kb.isSelected();
}

//-----------------------------------------------------------------------------
public boolean is128kbSelected() {
  return checkBox128kb.isSelected();
}

//-----------------------------------------------------------------------------
public boolean is1024kbSelected() {
  return checkBox1024kb.isSelected();
}

//-----------------------------------------------------------------------------
public boolean isCustomSizeSelected() {
  return customSizeCheckBox.isSelected();
}

////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private JTextField customSizeTextField;
private JCheckBox  checkBox8kb;
private JCheckBox  checkBox16kb;
private JCheckBox  checkBox32kb;
private JCheckBox  checkBox64kb;
private JCheckBox  checkBox128kb;
private JCheckBox  checkBox1024kb;
private JCheckBox  customSizeCheckBox;

////////////////////////////////////////
// PRIVATE METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
private JPanel getAppInfo() {
  // Implementation of getAppInfo method
  JPanel infoPanel = new JPanel();
  // Add components to infoPanel
  return infoPanel;
}

//-----------------------------------------------------------------------------
private JPanel getSettingsPanel() {
  JPanel settingsPanel = new JPanel(new GridBagLayout());
  GridBagConstraints gbc = new GridBagConstraints();
  gbc.gridx = 0;
  gbc.gridy = 0;
  gbc.anchor = GridBagConstraints.WEST;
  gbc.insets = new Insets(5, 5, 5, 5);
  
  checkBox8kb = new JCheckBox("8kb bullets");
  checkBox8kb.setSelected(true);
  settingsPanel.add(checkBox8kb, gbc);
  gbc.gridy++;
  
  checkBox16kb = new JCheckBox("16kb bullets");
  checkBox16kb.setSelected(true);
  settingsPanel.add(checkBox16kb, gbc);
  gbc.gridy++;
  
  checkBox32kb = new JCheckBox("32kb bullets");
  checkBox32kb.setSelected(true);
  settingsPanel.add(checkBox32kb, gbc);
  gbc.gridy++;
  
  checkBox64kb = new JCheckBox("64kb bullets");
  checkBox64kb.setSelected(true);
  settingsPanel.add(checkBox64kb, gbc);
  gbc.gridy++;
  
  checkBox128kb = new JCheckBox("128kb bullets");
  checkBox128kb.setSelected(true);
  settingsPanel.add(checkBox128kb, gbc);
  gbc.gridy++;
  
  checkBox1024kb = new JCheckBox("1024kb bullets");
  checkBox1024kb.setSelected(true);
  settingsPanel.add(checkBox1024kb, gbc);
  gbc.gridy++;
  
  customSizeCheckBox = new JCheckBox("Custom size");
  settingsPanel.add(customSizeCheckBox, gbc);
  gbc.gridx = 1;
  customSizeTextField = new JTextField(10);
  settingsPanel.add(customSizeTextField, gbc);
  
  return settingsPanel;
}

}
////////////////////////////////////////
// END CLASS FerretSuiteTab
////////////////////////////////////////

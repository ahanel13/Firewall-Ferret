package view;

import burp.api.montoya.logging.Logging;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
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
public FerretSuiteTab(Logging logger) {
  super(new GridBagLayout());
  _logger = logger;
  JPanel      info       = getAppInfo();
  JPanel      settings   = getSettingsPanel();
  JScrollPane tablePanel = getTablePanel();
  
  GridBagConstraints infoCons = new GridBagConstraints();
  infoCons.gridx = 1;
  infoCons.gridy = 0;
  infoCons.weightx = .5;
  infoCons.fill = GridBagConstraints.HORIZONTAL;
  infoCons.insets = new Insets(10, 10, 10, 10);
  
  GridBagConstraints settingsCons = new GridBagConstraints();
  settingsCons.gridx = 1;
  settingsCons.gridy = 1;
  settingsCons.weightx = .5;
  settingsCons.fill = GridBagConstraints.HORIZONTAL;
  settingsCons.insets = new Insets(10, 10, 10, 10);
  
  GridBagConstraints tableCons = new GridBagConstraints();
  tableCons.gridx = 0;
  tableCons.gridy = 2;
  tableCons.gridwidth = 3;
  tableCons.weightx = 1.0;
  tableCons.weighty = 1.0;
  tableCons.fill = GridBagConstraints.BOTH;
  tableCons.insets = new Insets(50, 50, 50, 50);
  
  add(info, infoCons);
  add(settings, settingsCons);
  add(tablePanel, tableCons);
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

//-----------------------------------------------------------------------------
public void setMessage(String message) {
  messageLabel.setText(message);
}

////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private final Logging _logger;

private JTextField customSizeTextField;
private JCheckBox  checkBox8kb;
private JCheckBox  checkBox16kb;
private JCheckBox  checkBox32kb;
private JCheckBox  checkBox64kb;
private JCheckBox  checkBox128kb;
private JCheckBox  checkBox1024kb;
private JCheckBox  customSizeCheckBox;
private JLabel     messageLabel;

////////////////////////////////////////
// PRIVATE METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
private JPanel getAppInfo() {
  // todo
  return new JPanel();
}

//-----------------------------------------------------------------------------
private JPanel getSettingsPanel() {
  JPanel settingsPanel = new JPanel(new GridBagLayout());
  GridBagConstraints gbc = new GridBagConstraints();
  gbc.gridx = 0;
  gbc.gridy = 0;
  gbc.anchor = GridBagConstraints.WEST;
  gbc.insets = new Insets(5, 5, 5, 5);
  gbc.fill = GridBagConstraints.HORIZONTAL;
  
  // Add description label
  JLabel descriptionLabel = new JLabel(
    "These buttons control the set of bullets used in conjunction with standard burp suite scan checks.");
  gbc.gridwidth = 2; // Span across two columns
  settingsPanel.add(descriptionLabel, gbc);
  gbc.gridy++;
  
  // Reset gridwidth to default (1) for other components
  gbc.gridwidth = 1;
  
  checkBox8kb = new JCheckBox("8 kb bullets");
  checkBox8kb.setSelected(true);
  settingsPanel.add(checkBox8kb, gbc);
  gbc.gridy++;
  
  checkBox16kb = new JCheckBox("16 kb bullets");
  checkBox16kb.setSelected(false);
  settingsPanel.add(checkBox16kb, gbc);
  gbc.gridy++;
  
  checkBox32kb = new JCheckBox("32 kb bullets");
  checkBox32kb.setSelected(false);
  settingsPanel.add(checkBox32kb, gbc);
  gbc.gridy++;
  
  checkBox64kb = new JCheckBox("64 kb bullets");
  checkBox64kb.setSelected(false);
  settingsPanel.add(checkBox64kb, gbc);
  gbc.gridy++;
  
  checkBox128kb = new JCheckBox("128 kb bullets");
  checkBox128kb.setSelected(false);
  settingsPanel.add(checkBox128kb, gbc);
  gbc.gridy++;
  
  checkBox1024kb = new JCheckBox("1024 kb bullets");
  checkBox1024kb.setSelected(false);
  settingsPanel.add(checkBox1024kb, gbc);
  gbc.gridy++;
  
  customSizeCheckBox = new JCheckBox("Custom size");
  gbc.gridx = 0;
  settingsPanel.add(customSizeCheckBox, gbc);
  
  gbc.gridx = 1;
  customSizeTextField = new JTextField(10);
  settingsPanel.add(customSizeTextField, gbc);
  gbc.gridx = 0;
  gbc.gridy++;
  
  // Add messageLabel below the checkboxes
  gbc.gridwidth = 2; // Span across two columns
  gbc.fill = GridBagConstraints.HORIZONTAL;
  messageLabel = new JLabel(" "); // Initialize with empty text
  settingsPanel.add(messageLabel, gbc);
  
  return settingsPanel;
}

//-----------------------------------------------------------------------------
private JScrollPane getTablePanel() {
  // Define column names
  String[] columnNames = {"WAF Provider", "Maximum Request Body Inspection Size Limit", "Sources"};
  
  // Define table data with raw URLs
  Object[][] data = {
    {"Cloudflare", "128 KB for ruleset engine, up to 100 - 500 MB depending on the plan",
      "https://developers.cloudflare.com/ruleset-engine/rules-language/fields/#http-request-body-fields"},
    {"AWS WAF", "8 KB - 64 KB (configurable depending on service)",
      "https://docs.aws.amazon.com/waf/latest/developerguide/waf-oversize-request-components.html"},
    {"Azure WAF", "128 KB - 4 GB (configurable depending on service & rule set version)",
      "https://learn.microsoft.com/en-us/azure/azure-resource-manager/management/azure-subscription-service-limits#application-gateway-limits"},
    {"Akamai", "8 KB, 1 KB, 32 KB",
      "https://techdocs.akamai.com/application-security/reference/put-advanced-settings-request-body"},
    {"Fortiweb by Fortinet", "0 MB - 200 MB (configurable)",
      "https://help.fortinet.com/fweb/582/Content/FortiWeb/fortiweb-admin/limit_file_uploads.htm"},
    {"F5 BIG-IP WAAP", "1 KB (configurable)",
      "https://clouddocs.f5.com/bigip-next/20-2-0/waf_management/cm_awaf_manage_edit_policy.html"},
    {"Palo Alto", "_Unknown_", ""},
    {"Barracuda WAF", "_Unknown_", ""},
    {"Radware AppWall", "30 KB - 20 KB",
      "https://portals.radware.com/releasenotes/appwall_release_notes_7_6_14/index.html#page/AppWall_Release_Notes_7_6_14/AppWall_7614-RN%20-%20final.1.09.html#wwconnect_header"},
    {"Sucuri", "_Unknown_", ""}
  };
  
  // Create table model and table
  JTable table = getjTable(data, columnNames);

  // Set column widths based on percentage of total table width
  final int totalWidth = 1000; // Assume a total table width of 1000 pixels
  table.getColumnModel().getColumn(0).setPreferredWidth((int) (totalWidth * 0.15)); // 15%
  table.getColumnModel().getColumn(1).setPreferredWidth((int) (totalWidth * 0.35)); // 35%
  table.getColumnModel().getColumn(2).setPreferredWidth((int) (totalWidth * 0.50)); // 50%
  
  // Wrap the table in a scroll pane and return it
  JScrollPane scrollPane = new JScrollPane(table);
  scrollPane.setPreferredSize(new Dimension(totalWidth, 400)); // Set preferred size for the scroll pane
  return scrollPane;
}

//-----------------------------------------------------------------------------
private static JTable getjTable(Object[][] data, String[] columnNames){
  DefaultTableModel model = new DefaultTableModel(data, columnNames);
  JTable table = new JTable(model) {
    @Override
    public Class<?> getColumnClass(int column) {
      return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false; // Cells are not editable
    }
  };

  // Enable row selection
  table.setCellSelectionEnabled(true);
  table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
  return table;
}


}
////////////////////////////////////////
// END CLASS FerretSuiteTab
////////////////////////////////////////

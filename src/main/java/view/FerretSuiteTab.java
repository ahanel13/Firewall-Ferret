package view;

import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

////////////////////////////////////////
// CLASS FerretSuiteTab
////////////////////////////////////////
public class FerretSuiteTab extends JPanel {

////////////////////////////////////////
// PUBLIC METHODS
////////////////////////////////////////
public FerretSuiteTab() {
  super(new GridBagLayout());
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
  String version = FerretSuiteTab.class.getPackage().getImplementationVersion();
  if (version == null) version = "dev";

  JEditorPane left = new JEditorPane("text/html",
    "<html><body style='font-family:sans-serif'>" +
    "<h2 style='margin:0'>Firewall Ferret v" + version + "</h2>" +
    "<p style='margin:4px 0'>Bypass WAFs by prepending configurable junk data to HTTP requests.</p>" +
    "</body></html>"
  );
  left.setEditable(false);
  left.setOpaque(false);

  JEditorPane right = new JEditorPane("text/html",
    "<html><body style='font-family:sans-serif; text-align:right'>" +
    "<p style='margin:4px 0'><b>Author:</b> <a href='https://anthonyhanel.me/'>Anthony Hanel</a></p>" +
    "<p style='margin:4px 0'><b>GitHub:</b> <a href='https://github.com/ahanel13/Firewall-Ferret'>github.com/ahanel13/Firewall-Ferret</a></p>" +
    "</body></html>"
  );
  right.setEditable(false);
  right.setOpaque(false);
  right.addHyperlinkListener(e -> {
    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
      try { Desktop.getDesktop().browse(e.getURL().toURI()); }
      catch (Exception ignored) {}
    }
  });

  JPanel panel = new JPanel(new BorderLayout());
  panel.add(left,  BorderLayout.WEST);
  panel.add(right, BorderLayout.EAST);
  return panel;
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
  String[] columnNames = {"WAF Provider", "Maximum Request Body Inspection Size Limit"};

  String[][] rows = {
    {"Cloudflare", "Enterprise: 128 KB inspection window; Free: 1 MB; paid plans can request increase to 1 MB. "},
    {"AWS WAF", "ALB/AppSync: fixed 8 KB. CloudFront/API Gateway/Cognito/App Runner/Verified Access: default 16 KB, configurable in 16 KB steps up to 64 KB."},
    {"Azure WAF", "Application Gateway CRS 3.1: ~128 KB inspection; CRS 3.2+: independently configurable inspection limit (can be disabled). Max request body size separately configurable up to 2 MB. Front Door: ~128 KB."},
    {"Akamai", "Default 8 KB; configurable to 8, 16, or 32 KB. Platform-wide maximum payload size is 128 KB."},
    {"Fortiweb by Fortinet", "Body size limits configurable via HTTP/HTTPS protocol constraints (max-http-body-length); 0 = no enforced limit. Specific maximums are version-dependent."},
    {"F5 BIG-IP WAAP", "On-prem ASM: policy-configurable (multi-MB range). Distributed Cloud WAF: ~64 KB inspection limit. Consult F5 support portal for current details."},
    {"Palo Alto (Prisma Cloud WAAS)", "Configurable via policy (inspectionSizeBytes); default limits not publicly documented. Consult Palo Alto support for specifics."},
    {"Barracuda WAF", "No enforced default limit; configurable up to ~8 MB (8,388,608 bytes). Chunked encoding can bypass Content-Length checks."},
    {"Radware AppWall", "Default 1 MB POST body limit (v7.6.4+); configurable via admin interface or configuration files."},
    {"Sucuri", "~1.25 MB (platform-managed, not user-configurable); enforced via UP010 signature."}
  };

  String[] urls = {
    "https://developers.cloudflare.com/changelog/2025-12-05-waf-max-payload-size-change/",
    "https://docs.aws.amazon.com/waf/latest/developerguide/web-acl-setting-body-inspection-limit.html",
    "https://learn.microsoft.com/en-us/azure/web-application-firewall/ag/application-gateway-waf-request-size-limits",
    "https://techdocs.akamai.com/application-security/reference/put-advanced-settings-request-body",
    "https://docs.fortinet.com/document/fortiweb/latest/administration-guide/277489/http-https-protocol-constraints",
    "https://my.f5.com/manage/s/article/K11434152",
    "https://docs.prismacloud.io/en/compute-edition/34/admin-guide/waas/waas-app-firewall",
    "https://documentation.campus.barracuda.com/wiki/display/BWAFv76/Configuring+Request+Limits",
    "https://support.radware.com/app/answers/answer_view/a_id/1020912/~/appwall-error:-413-payload-too-large",
    "https://labs.sucuri.net/signatures/waf/up010-upload-size-restricted-by-the-administrator/"
  };

  DefaultTableModel model = new DefaultTableModel(rows, columnNames);
  JTable table = new JTable(model) {
    @Override
    public Class<?> getColumnClass(int column) { return String.class; }

    @Override
    public boolean isCellEditable(int row, int column) { return false; }
  };

  // Render the WAF Provider column as a clickable hyperlink
  table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(
        JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
      super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
      setForeground(Color.BLUE);
      setFont(getFont().deriveFont(Font.PLAIN));
      setText(value.toString());
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      return this;
    }
  });

  // Open the URL when the WAF Provider cell is clicked
  table.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
      int col = table.columnAtPoint(e.getPoint());
      int row = table.rowAtPoint(e.getPoint());
      if (col == 0 && row >= 0 && row < urls.length) {
        try { Desktop.getDesktop().browse(new URI(urls[row])); }
        catch (Exception ignored) {}
      }
    }
  });

  // Change cursor to hand when hovering over the provider column
  table.addMouseMotionListener(new MouseAdapter() {
    @Override
    public void mouseMoved(MouseEvent e) {
      int col = table.columnAtPoint(e.getPoint());
      table.setCursor(col == 0
        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        : Cursor.getDefaultCursor());
    }
  });

  table.setCellSelectionEnabled(false);

  final int totalWidth = 1000;
  table.getColumnModel().getColumn(0).setPreferredWidth((int) (totalWidth * 0.20));
  table.getColumnModel().getColumn(1).setPreferredWidth((int) (totalWidth * 0.80));

  JScrollPane scrollPane = new JScrollPane(table);
  scrollPane.setPreferredSize(new Dimension(totalWidth, 400));
  return scrollPane;
}



}
////////////////////////////////////////
// END CLASS FerretSuiteTab
////////////////////////////////////////

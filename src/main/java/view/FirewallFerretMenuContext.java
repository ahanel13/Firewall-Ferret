package view;

import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.contextmenu.MessageEditorHttpRequestResponse;

import javax.swing.JMenuItem;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

////////////////////////////////////////
// CLASS FirewallFerretMenuContext
////////////////////////////////////////
public class FirewallFerretMenuContext implements ContextMenuItemsProvider {


////////////////////////////////////////
// PUBLIC METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
public FirewallFerretMenuContext() {
  // this maintains a single JMenuItem which will have a single event listener
  _insertJunkData = new JMenuItem("Insert Junk Data");
}

//-----------------------------------------------------------------------------
public void addEventListenerToMenuItem(ActionListener listener) {
  _insertJunkData.addActionListener(listener);
}

//-----------------------------------------------------------------------------
@Override
public List<Component> provideMenuItems(ContextMenuEvent event) {
  Optional<MessageEditorHttpRequestResponse> optional     = event.messageEditorRequestResponse();
  List<Component>                            menuItemList = new ArrayList<>();
  
  if(
    optional.isPresent() &&
    optional.get().selectionContext().equals(MessageEditorHttpRequestResponse.SelectionContext.REQUEST)
  ) {
    menuItemList.add(_insertJunkData);
    _reqRespEditor = optional.get();
    _event         = event;
  }
  
  return menuItemList;
}


public MessageEditorHttpRequestResponse getReqResp() {
  return _reqRespEditor;
}

public ContextMenuEvent getEvent() {
  return _event;
}

////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private final JMenuItem                        _insertJunkData;

private MessageEditorHttpRequestResponse _reqRespEditor;
private ContextMenuEvent                 _event;

}
////////////////////////////////////////
// CLASS FirewallFerretMenuContext
////////////////////////////////////////
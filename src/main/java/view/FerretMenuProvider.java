package view;

import burp.api.montoya.http.message.HttpRequestResponse;
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
// CLASS FerretMenuProvider
////////////////////////////////////////
public class FerretMenuProvider implements ContextMenuItemsProvider {


////////////////////////////////////////
// PUBLIC METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
public FerretMenuProvider() {
  // this maintains a single JMenuItem which will have a single event listener
  _insertWafBullet = new JMenuItem("Insert WAF Bullet");
  _addWafBullet    = new JMenuItem("Add WAF Bullet");
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
    menuItemList.add(_insertWafBullet);
    _reqResp = optional.get().requestResponse();
  }
  else {
    _reqResp = event.selectedRequestResponses().get(0);
  }
  
  menuItemList.add(_addWafBullet);
  _reqRespEditor = optional;
  _event         = event;
  
  return menuItemList;
}

//-----------------------------------------------------------------------------
public void addActionListenerToInsertItem(ActionListener l){
  _insertWafBullet.addActionListener(l);
}

//-----------------------------------------------------------------------------
public void addActionListenerToAddItem(ActionListener l){
  _addWafBullet.addActionListener(l);
}

//-----------------------------------------------------------------------------
public Optional<MessageEditorHttpRequestResponse> getReqRespEditor() {
  return _reqRespEditor;
}

//-----------------------------------------------------------------------------
public HttpRequestResponse getReqResp() {
  return _reqResp;
}

//-----------------------------------------------------------------------------
public ContextMenuEvent getEvent() {
  return _event;
}

////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private final JMenuItem _insertWafBullet;
private final JMenuItem _addWafBullet;

private Optional<MessageEditorHttpRequestResponse> _reqRespEditor;
private ContextMenuEvent                 _event;
private HttpRequestResponse              _reqResp;

}
////////////////////////////////////////
// CLASS FerretMenuProvider
////////////////////////////////////////
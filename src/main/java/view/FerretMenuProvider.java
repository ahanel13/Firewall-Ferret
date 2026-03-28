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
  
  HttpRequestResponse reqResp;
  if(
    optional.isPresent() &&
    optional.get().selectionContext().equals(MessageEditorHttpRequestResponse.SelectionContext.REQUEST)
  ) {
    menuItemList.add(_insertWafBullet);
    reqResp = optional.get().requestResponse();
  }
  else {
    List<HttpRequestResponse> selected = event.selectedRequestResponses();
    if (selected.isEmpty()) {
      return menuItemList;
    }
    reqResp = selected.get(0);
  }

  menuItemList.add(_addWafBullet);
  _snapshot = new MenuSnapshot(optional, reqResp, event);
  
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
  return _snapshot.reqRespEditor();
}

//-----------------------------------------------------------------------------
public HttpRequestResponse getReqResp() {
  return _snapshot.reqResp();
}

//-----------------------------------------------------------------------------
public ContextMenuEvent getEvent() {
  return _snapshot.event();
}

////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private final JMenuItem _insertWafBullet;
private final JMenuItem _addWafBullet;

private record MenuSnapshot(
  Optional<MessageEditorHttpRequestResponse> reqRespEditor,
  HttpRequestResponse                        reqResp,
  ContextMenuEvent                           event
) {}

private MenuSnapshot _snapshot;

}
////////////////////////////////////////
// CLASS FerretMenuProvider
////////////////////////////////////////
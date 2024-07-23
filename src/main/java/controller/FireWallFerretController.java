package controller;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.contextmenu.InvocationType;
import burp.api.montoya.ui.contextmenu.MessageEditorHttpRequestResponse;
import model.BulletBuilder;
import model.RequestBuilder;
import view.BulletOptionsDialog;
import view.FirewallFerretMenuContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static burp.api.montoya.ui.contextmenu.InvocationType.INTRUDER_PAYLOAD_POSITIONS;
import static burp.api.montoya.ui.contextmenu.InvocationType.MESSAGE_EDITOR_REQUEST;
import static burp.api.montoya.ui.contextmenu.InvocationType.MESSAGE_EDITOR_RESPONSE;

////////////////////////////////////////
// CLASS FireWallFerretController
////////////////////////////////////////
public class FireWallFerretController {


////////////////////////////////////////
// PUBLIC METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
public FireWallFerretController(MontoyaApi api, FirewallFerretMenuContext menuContext) {
  _api = api;
  _menuContext = menuContext;
  
  registerMenuContext();
}


////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private final MontoyaApi                _api;
private final FirewallFerretMenuContext _menuContext;


////////////////////////////////////////
// PRIVATE METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
private void registerMenuContext() {
  _api.userInterface().registerContextMenuItemsProvider(_menuContext);
  _menuContext.addEventListenerToMenuItem(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      BulletOptionsDialog dialog = new BulletOptionsDialog();
      dialog.showDialog();
      int bulletSize = dialog.getByteSize();
      
      if(bulletSize <= 0)
        return; // !!! EXIT HERE !!!
      
      
      String      bullet = BulletBuilder.buildBullet(bulletSize);
      MessageEditorHttpRequestResponse reqRespEditor = _menuContext.getReqResp();
      HttpRequest req = getRequest(reqRespEditor, bullet);
      
      
      if(_isEditorEvent()) // if event came from an editor then replace the request
        reqRespEditor.setRequest(req);
      else // else if the event came from a viewer, then create a repeater tab
        _api.repeater().sendToRepeater(req);
    }
    
    //-------------------------------------------------------------------------
    private boolean _isEditorEvent() {
      for(InvocationType type : replacingInvocationType) {
        if(_menuContext.getEvent().isFrom(type)) {
          return true;
        }
      }
      return false;
    }
    
    //-------------------------------------------------------------------------
    private HttpRequest getRequest(MessageEditorHttpRequestResponse reqRespEditor, String bullet) {
      /* if selection replace selection with bullet */
      if(reqRespEditor.selectionOffsets().isPresent()){
        return RequestBuilder.build(
          reqRespEditor.requestResponse().request(),
          bullet,
          reqRespEditor.selectionOffsets().get()
        );
      }
      //else if caret insert bullet
      else {
        return RequestBuilder.build(
          reqRespEditor.requestResponse().request(),
          bullet
        );
      }
    }
  });
}

//-----------------------------------------------------------------------------
private List<InvocationType> replacingInvocationType = List.of(
  MESSAGE_EDITOR_REQUEST,
  MESSAGE_EDITOR_RESPONSE,
  INTRUDER_PAYLOAD_POSITIONS
);


}
////////////////////////////////////////
// END CLASS FireWallFerretController
////////////////////////////////////////
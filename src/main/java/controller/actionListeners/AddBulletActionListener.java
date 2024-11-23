package controller.actionListeners;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.contextmenu.InvocationType;
import burp.api.montoya.ui.contextmenu.MessageEditorHttpRequestResponse;
import model.creators.BulletFactory;
import model.creators.RequestBuilder;
import view.BulletOptionsDialog;
import view.FerretMenuProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;

////////////////////////////////////////
// CLASS AddActionListener
////////////////////////////////////////
public class AddBulletActionListener extends AbstractListeners{

//-------------------------------------------------------------------------
public AddBulletActionListener(MontoyaApi montoyaApi, FerretMenuProvider context, List<InvocationType> type){
  super(montoyaApi, context, type);
}

//-------------------------------------------------------------------------
@Override
public void actionPerformed(ActionEvent e){
  BulletOptionsDialog dialog = new BulletOptionsDialog(api.userInterface().swingUtils().suiteFrame());
  dialog.showDialog();
  int bulletSize = dialog.getByteSize();
  
  if(bulletSize <= 0) return; // !!! EXIT HERE !!!

  Optional<MessageEditorHttpRequestResponse> reqRespEditor = menuContext.getReqRespEditor();
  String                                     bullet        = BulletFactory.bullet(bulletSize);
  HttpRequest                                contextReq    = menuContext.getReqResp().request();
  Optional<HttpRequest>                      updatedReq    = getRequest(contextReq, bullet);

  if(updatedReq.isPresent()){
    // if event came from an editor then replace the request
    if(_isEditorEvent() && reqRespEditor.isPresent())
      reqRespEditor.get().setRequest(updatedReq.orElse(null));
    // else if the event came from a viewer, then create a repeater tab
    else
      api.repeater().sendToRepeater(updatedReq.orElse(null));
  }
}

//-------------------------------------------------------------------------
public Optional<HttpRequest> getRequest(HttpRequest request, String bullet){
  try {
    return Optional.of(RequestBuilder.build(request, bullet));
  }
  catch (UnsupportedOperationException e) {
    api.logging().logToError(e);
    api.logging().raiseErrorEvent(e.getMessage());
    JOptionPane.showMessageDialog(this.api.userInterface().swingUtils().suiteFrame(), e.getMessage());
    return Optional.empty();
  }
}

}
////////////////////////////////////////
// END CLASS AddActionListener
////////////////////////////////////////
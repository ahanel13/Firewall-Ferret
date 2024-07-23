package model.actionListeners;////////////////////////////////////////

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.contextmenu.InvocationType;
import burp.api.montoya.ui.contextmenu.MessageEditorHttpRequestResponse;
import model.creators.BulletFactory;
import model.creators.RequestBuilder;
import view.BulletOptionsDialog;
import view.FerretMenuProvider;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;

////////////////////////////////////////
// CLASS AddActionListener
////////////////////////////////////////
public class AddActionListener extends AbstractListeners{

//-------------------------------------------------------------------------
public AddActionListener(MontoyaApi montoyaApi, FerretMenuProvider context, List<InvocationType> type){
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
  String      bullet     = BulletFactory.bullet(bulletSize);
  HttpRequest contextReq = menuContext.getReqResp().request();
  HttpRequest updatedReq = getRequest(contextReq, bullet);
  
  if(_isEditorEvent() && reqRespEditor.isPresent()) // if event came from an editor then replace the request
    reqRespEditor.get().setRequest(updatedReq);
  else // else if the event came from a viewer, then create a repeater tab
    api.repeater().sendToRepeater(updatedReq);
}

//-------------------------------------------------------------------------
public HttpRequest getRequest(HttpRequest request, String bullet){
  return RequestBuilder.build(request, bullet);
}

}
////////////////////////////////////////
// END CLASS AddActionListener
////////////////////////////////////////
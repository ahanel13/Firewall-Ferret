package controller.actionListeners;

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
// CLASS InsertActionListener
////////////////////////////////////////
public class InsertBulletActionListener extends AbstractListeners{

//-------------------------------------------------------------------------
public InsertBulletActionListener(MontoyaApi api, FerretMenuProvider context, List<InvocationType> type){
  super(api, context, type);
}

//-------------------------------------------------------------------------
@Override
public void actionPerformed(ActionEvent e){
  BulletOptionsDialog dialog = new BulletOptionsDialog(api.userInterface().swingUtils().suiteFrame());
  dialog.showDialog();
  int bulletSize = dialog.getByteSize();
  
  if(bulletSize <= 0) return; // !!! EXIT HERE !!!
  
  
  Optional<MessageEditorHttpRequestResponse> reqRespEditor = menuContext.getReqRespEditor();
  String      bullet = BulletFactory.bullet(bulletSize);
  
  HttpRequest req;
  if(reqRespEditor.isPresent())
    req = getRequest(reqRespEditor.get(), bullet);
  else
    throw new UnsupportedOperationException(
      this.getClass().getName() + ":: Failed to find RequestResponseEditor");
  
  
  if(_isEditorEvent()) // if event came from an editor then replace the request
    reqRespEditor.get().setRequest(req);
  else // else if the event came from a viewer, then create a repeater tab
    api.repeater().sendToRepeater(req);
}

//-------------------------------------------------------------------------
public HttpRequest getRequest(MessageEditorHttpRequestResponse reqRespEditor, String bullet){
  // if selection then replace selection with bullet
  if(reqRespEditor.selectionOffsets().isPresent()) {
    return RequestBuilder.build(reqRespEditor.requestResponse().request(), bullet,
      reqRespEditor.selectionOffsets().get()
    );
  }
  //else if caret then insert bullet
  else {
    return RequestBuilder.build(
      reqRespEditor.requestResponse().request(),
      bullet,
      reqRespEditor.caretPosition());
  }
}

}
////////////////////////////////////////
// END CLASS InsertActionListener
////////////////////////////////////////
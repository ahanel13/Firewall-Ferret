package model.actionListeners;////////////////////////////////////////

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.contextmenu.InvocationType;
import burp.api.montoya.ui.contextmenu.MessageEditorHttpRequestResponse;
import model.BulletBuilder;
import model.RequestBuilder;
import view.BulletOptionsDialog;
import view.FerretMenuProvider;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

////////////////////////////////////////
// CLASS AbstractListeners
////////////////////////////////////////
public abstract class AbstractListeners implements ActionListener{


////////////////////////////////////////
// PUBLIC METHODS
////////////////////////////////////////
//-------------------------------------------------------------------------
public AbstractListeners(MontoyaApi api, FerretMenuProvider menuContext, List<InvocationType> type){
  this.api                     = api;
  this.menuContext             = menuContext;
  this.replacingInvocationType = type;
}

//-------------------------------------------------------------------------
public boolean _isEditorEvent(){
  for(InvocationType type : replacingInvocationType) {
    if(menuContext.getEvent().isFrom(type)) {
      return true;
    }
  }
  return false;
}


////////////////////////////////////////
// PROTECTED FIELDS
////////////////////////////////////////
protected FerretMenuProvider   menuContext;
protected List<InvocationType> replacingInvocationType;
protected MontoyaApi           api;


}
////////////////////////////////////////
// END CLASS AbstractListeners
////////////////////////////////////////
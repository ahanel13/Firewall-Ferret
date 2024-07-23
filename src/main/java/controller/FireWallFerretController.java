package controller;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.contextmenu.InvocationType;
import model.InsertPntProvider;
import model.actionListeners.AddActionListener;
import model.actionListeners.InsertActionListener;
import view.FerretMenuProvider;

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
public FireWallFerretController(
  MontoyaApi api, FerretMenuProvider menuContext, InsertPntProvider insPointProvider
) {
  _api = api;
  _menuContext = menuContext;
  _insPointProvider = insPointProvider;
  
  registerMenuContext();
  registerInsertionProvider();
}



////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private final MontoyaApi         _api;
private final FerretMenuProvider _menuContext;
private final InsertPntProvider  _insPointProvider;


////////////////////////////////////////
// PRIVATE METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
private void registerMenuContext() {
  _api.userInterface().registerContextMenuItemsProvider(_menuContext);
  
  _menuContext.addActionListenerToInsertItem(
    new InsertActionListener(_api, _menuContext, replacingInvocationType));
  
  _menuContext.addActionListenerToAddItem(
    new AddActionListener(_api, _menuContext, replacingInvocationType));
}

//-----------------------------------------------------------------------------
private final List<InvocationType> replacingInvocationType = List.of(
  MESSAGE_EDITOR_REQUEST,
  MESSAGE_EDITOR_RESPONSE,
  INTRUDER_PAYLOAD_POSITIONS
);

//-----------------------------------------------------------------------------
private void registerInsertionProvider(){
  _api.scanner().registerInsertionPointProvider(_insPointProvider);
}

}
////////////////////////////////////////
// END CLASS FireWallFerretController
////////////////////////////////////////
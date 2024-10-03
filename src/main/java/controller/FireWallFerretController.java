package controller;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.Registration;
import burp.api.montoya.ui.contextmenu.InvocationType;
import model.InsertPntProvider;
import model.actionListeners.AddActionListener;
import controller.actionListeners.InsertBulletActionListener;
import view.FerretMenuProvider;
import view.FerretSuiteTab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
  MontoyaApi api, FerretMenuProvider menuContext,  FerretSuiteTab view
) {
  _api              = api;
  _menuContext      = menuContext;
  _insPointProvider = new InsertPntProvider(List.of(8, 16, 32, 64, 128, 1024), _api);
  _view             = view;
  
  registerMenuContext();
  registerInsertionProvider();
  registerSuiteTab();
  
  connectView2InsProvider();
}

////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private final MontoyaApi         _api;
private final FerretMenuProvider _menuContext;
private final InsertPntProvider  _insPointProvider;
private final FerretSuiteTab     _view;
private       Registration       _insProviderReg;


////////////////////////////////////////
// PRIVATE METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
private void connectView2InsProvider(){
  _view.registerListener(new ActionListener(){
    @Override
    public void actionPerformed(ActionEvent e){
      _insProviderReg.deregister();
      List<Integer> bulletSizes = getBulletSizeList();
      
      _insProviderReg = _api.scanner()
        .registerInsertionPointProvider(new InsertPntProvider(bulletSizes, _api));
      
      _view.setMessage("Updating Scanner bullets to: " + bulletSizes);
    }
  });
}

//-----------------------------------------------------------------------------
private void registerSuiteTab(){
  _api.userInterface().registerSuiteTab("Firewall Ferret", _view);
}

//-----------------------------------------------------------------------------
private void registerMenuContext() {
  _api.userInterface().registerContextMenuItemsProvider(_menuContext);
  
  _menuContext.addActionListenerToInsertItem(
    new InsertBulletActionListener(_api, _menuContext, replacingInvocationType));
  
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
  _insProviderReg = _api.scanner().registerInsertionPointProvider(_insPointProvider);
}

//-----------------------------------------------------------------------------
private List<Integer> getBulletSizeList(){
  List<Integer> sizes = new ArrayList<>();
  if(_view.is8kbSelected())        sizes.add(8);
  if(_view.is16kbSelected())       sizes.add(16);
  if(_view.is32kbSelected())       sizes.add(32);
  if(_view.is64kbSelected())       sizes.add(64);
  if(_view.is128kbSelected())      sizes.add(128);
  if(_view.is1024kbSelected())     sizes.add(1024);
  if(_view.isCustomSizeSelected()) {
    Integer customInt = _view.getCustomInt();
    if(customInt > 0)
      sizes.add(customInt);
  }
  
  return sizes;
}

}
////////////////////////////////////////
// END CLASS FireWallFerretController
////////////////////////////////////////
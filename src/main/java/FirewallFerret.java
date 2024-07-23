import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.ExtensionUnloadingHandler;
import controller.FireWallFerretController;
import model.InsertPntProvider;
import view.FerretMenuProvider;

////////////////////////////////////////
// CLASS FirewallFerret
////////////////////////////////////////
public class FirewallFerret implements BurpExtension {


////////////////////////////////////////
// PUBLIC METHODS
////////////////////////////////////////
//-----------------------------------------------------------------------------
@Override
public void initialize(MontoyaApi montoyaApi) {
  _api             = montoyaApi;
  _menuContext     = new FerretMenuProvider();
  _insPointProvder = new InsertPntProvider();
  _controller      = new FireWallFerretController(_api, _menuContext, _insPointProvder);
  
  _api.extension().setName("Firewall Ferret");
  _api.logging().logToOutput("Extension Installed");
  
  _registerUnloader();
}

//-----------------------------------------------------------------------------
private void _registerUnloader(){
  _api.extension().registerUnloadingHandler(new ExtensionUnloadingHandler(){
    @Override
    public void extensionUnloaded(){
      _api.logging().logToOutput("Extension unloaded.");
    }
  });
}


////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private MontoyaApi               _api;
private FerretMenuProvider       _menuContext;
private FireWallFerretController _controller;
private InsertPntProvider        _insPointProvder;


}
////////////////////////////////////////
// END CLASS FirewallFerret
////////////////////////////////////////
import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import controller.FireWallFerretController;
import view.FirewallFerretMenuContext;

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
  _api         = montoyaApi;
  _menuContext = new FirewallFerretMenuContext();
  _controller  = new FireWallFerretController(_api, _menuContext);
  
  _api.extension().setName("Firewall Ferret");
  _api.logging().logToOutput("Extension Installed");
}


////////////////////////////////////////
// PRIVATE FIELDS
////////////////////////////////////////
private MontoyaApi                _api;
private FirewallFerretMenuContext _menuContext;
private FireWallFerretController  _controller;


}
////////////////////////////////////////
// END CLASS FirewallFerret
////////////////////////////////////////
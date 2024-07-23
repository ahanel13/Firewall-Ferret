package model;////////////////////////////////////////

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPointProvider;

import java.util.ArrayList;
import java.util.List;

////////////////////////////////////////
// CLASS InsertPntProvider
////////////////////////////////////////
public class InsertPntProvider implements AuditInsertionPointProvider{
@Override
public List<AuditInsertionPoint> provideInsertionPoints(HttpRequestResponse baseHttpReqResp){
  List<AuditInsertionPoint> insPoints = new ArrayList<>(_BULLET_SIZES.size());
  
  for(Integer size : _BULLET_SIZES) {
    insPoints.add(new BulletInsertionPoint(baseHttpReqResp.request(), size));
  }
  
  return insPoints;
}

private static final List<Integer> _BULLET_SIZES = List.of(
  8, 16, 32, 64, 128, 1024
);

}
////////////////////////////////////////
// END CLASS InsertPntProvider
////////////////////////////////////////
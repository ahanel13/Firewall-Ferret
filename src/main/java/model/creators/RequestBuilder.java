package model.creators;

import burp.api.montoya.core.Range;
import burp.api.montoya.http.message.ContentType;
import burp.api.montoya.http.message.requests.HttpRequest;

////////////////////////////////////////
// CLASS RequestBuilder
////////////////////////////////////////
public class RequestBuilder {


//-----------------------------------------------------------------------------
public static HttpRequest build(HttpRequest request, String bullet, Range range) {
  StringBuilder builder = new StringBuilder(request.toString());
  builder.replace(range.startIndexInclusive(), range.endIndexExclusive(), bullet);
  String newRequest = builder.toString();
  return HttpRequest.httpRequest(newRequest);
}

//-----------------------------------------------------------------------------
public static HttpRequest build(HttpRequest request, String bullet, int caretPos) {
  return build(request, bullet, Range.range(caretPos, caretPos));
}

//-----------------------------------------------------------------------------
public static HttpRequest build(HttpRequest request, String bullet) {
  ContentType type = request.contentType();
  
  switch(type){
  case URL_ENCODED -> {return addBodyParam(request, bullet);}
  case JSON        -> {return addJsonParam(request, bullet);}
  case XML         -> {return addXmlParam(request, bullet);}
  case MULTIPART   -> {return addMultiPartParam(request, bullet);}
  case AMF         -> {return padAmfWith(request, bullet);}
  case UNKNOWN     -> {return bestEffort(request, bullet);}
  default          ->
    throw new UnsupportedOperationException("Burp was unable to identify a content type");
  }
}

//-----------------------------------------------------------------------------
private static HttpRequest addBodyParam(HttpRequest request, String bullet) {
  String param = "bullet=";
  String sep   = "&";
  int trimLen  = Math.max(0, bullet.length() - param.length() - sep.length());
  return request.withBody(param + bullet.substring(0, trimLen) + sep + request.bodyToString());
}

//-----------------------------------------------------------------------------
private static HttpRequest addJsonParam(HttpRequest request, String bullet) {
  String strBody = request.bodyToString().trim();

  if (strBody.startsWith("{")) {
    // JSON object: insert bullet as first property
    String prepend = "{\"bullet\": \"";
    String append  = "\",";
    int trimLen = Math.max(0, bullet.length() - prepend.length() - append.length());
    return request.withBody(prepend + bullet.substring(0, trimLen) + append + strBody.substring(1));
  }
  else if (strBody.startsWith("[")) {
    // JSON array: insert bullet as first element
    String prepend = "[\"";
    String append  = "\",";
    int trimLen = Math.max(0, bullet.length() - prepend.length() - append.length());
    return request.withBody(prepend + bullet.substring(0, trimLen) + append + strBody.substring(1));
  }
  else {
    // Unknown JSON shape — best effort: prepend raw
    return bestEffort(request, bullet);
  }
}

//-----------------------------------------------------------------------------
private static HttpRequest addXmlParam(HttpRequest request, String bullet) {
  String prepend = "<!--";
  String append  = "-->";
  int trimLen    = Math.max(0, bullet.length() - prepend.length() - append.length());
  String payload = prepend + bullet.substring(0, trimLen) + append;
  
  String xml = request.bodyToString();
  
  // Check if the XML declaration is present
  String xmlDeclarationStart = "<?xml version=";
  int declarationEnd = 0;
  
  if (xml.startsWith(xmlDeclarationStart)) {
    // Find the end of the XML declaration
    declarationEnd = xml.indexOf("?>") + 2;
  }
  
  // Find the position of the first '>' character after the first tag following the XML declaration
  int firstTagEnd = xml.indexOf('>', declarationEnd) + 1;
  
  // Insert the comment after the first tag
  String modifiedXML = xml.substring(0, firstTagEnd) + payload + xml.substring(firstTagEnd);
  
  return request.withBody(modifiedXML);
}

//-----------------------------------------------------------------------------
private static HttpRequest addMultiPartParam(HttpRequest request, String bullet) {
  String boundary   = extractBoundary(request);
  String partOpen   = "--" + boundary + "\r\n";
  String partHeader = "Content-Disposition: form-data; name=\"bullet\"\r\n\r\n";
  String partClose  = "\r\n";
  int trimLen = Math.max(0, bullet.length() - partOpen.length() - partHeader.length() - partClose.length());
  String newParameterPart = partOpen + partHeader + bullet.substring(0, trimLen) + partClose;
  return request.withBody(newParameterPart + request.bodyToString());
}

//-----------------------------------------------------------------------------
private static String extractBoundary(HttpRequest request) {
  String contentType = request.headerValue("Content-Type");
  if (contentType != null) {
    int idx = contentType.indexOf("boundary=");
    if (idx != -1) {
      return contentType.substring(idx + "boundary=".length());
    }
  }
  throw new IllegalArgumentException("Boundary not found in the request.");
}

//-----------------------------------------------------------------------------
private static HttpRequest padAmfWith(HttpRequest request, String bullet) {
  throw new UnsupportedOperationException(
      "AMF Padding in not yet supported. See https://github.com/ahanel13/Firewall-Ferret/issues/1" +
          " for more information and or to contribute to the project.");
}

//-----------------------------------------------------------------------------
private static HttpRequest bestEffort(HttpRequest request, String bullet) {
  String strBody = request.bodyToString();
  return request.withBody(bullet.concat(strBody));
}


}
////////////////////////////////////////
// END CLASS RequestBuilder
////////////////////////////////////////
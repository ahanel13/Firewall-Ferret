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
  StringBuilder payload = new StringBuilder();
  payload.append(param);
  payload.append(bullet, 0, bullet.length() - param.length() + 1);
  payload.append("&");
  
  String strBody = request.bodyToString();
  payload.append(strBody);
  
  return request.withBody(payload.toString());
}

//-----------------------------------------------------------------------------
private static HttpRequest addJsonParam(HttpRequest request, String bullet) {
  String strBody = request.bodyToString().trim();

  // Check if the JSON body is an array or object
  if (strBody.startsWith("[")) {
    // For JSON arrays, prepend a new array element with the bullet
    String prepend = "[\"";
    String append  = "\",";
    StringBuilder payload = new StringBuilder();
    payload.append(prepend);
    payload.append(bullet);
    payload.append(append);
    payload.append(strBody, 1, strBody.length()); // skipping starting '['

    return request.withBody(payload.toString());
  } else if (strBody.startsWith("{")) {
    // For JSON objects, add a new property
    String prepend = "{\"bullet\": \"";
    String append  = "\",";
    StringBuilder payload = new StringBuilder();
    payload.append(prepend);
    payload.append(bullet);
    payload.append(append);
    payload.append(strBody, 1, strBody.length()); // skipping starting '{'

    return request.withBody(payload.toString());
  } else {
    // For other JSON types (string, number, boolean, null), wrap in an object
    String prepend = "{\"bullet\": \"";
    String middle  = "\", \"value\": ";
    String append  = "}";
    StringBuilder payload = new StringBuilder();
    payload.append(prepend);
    payload.append(bullet);
    payload.append(middle);
    payload.append(strBody);
    payload.append(append);

    return request.withBody(payload.toString());
  }
}

//-----------------------------------------------------------------------------
private static HttpRequest addXmlParam(HttpRequest request, String bullet) {
  String prepend = "<!--";
  String append  = "-->";
  String payload = prepend +
    bullet.substring(0, bullet.length() - prepend.length() + 3) +
    append;
  
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
  // Extract the boundary
  String boundary = extractBoundary(request.toString());
  
  // Create the new parameter part
  String newParameterPart = "--" + boundary + "\r\n" +
    "Content-Disposition: form-data; name=\"bullet\"\r\n\r\n" +
    bullet + "\r\n";
  
  return request.withBody(newParameterPart + request.bodyToString());
}

//-----------------------------------------------------------------------------
private static String extractBoundary(String request) {
  String contentTypeHeader = "Content-Type: multipart/form-data; boundary=";
  int boundaryIndex = request.indexOf(contentTypeHeader);
  if (boundaryIndex != -1) {
    int start = boundaryIndex + contentTypeHeader.length();
    int end = request.indexOf("\r\n", start);
    return request.substring(start, end);
  }
  throw new IllegalArgumentException("Boundary not found in the request.");
}

//-----------------------------------------------------------------------------
private static HttpRequest padAmfWith(HttpRequest request, String bullet) {
  // AMF (Action Message Format) is a binary format used by Flash/Flex applications.
  // Since modifying the AMF structure directly is complex and requires parsing,
  // we use a simple approach: prepend the bullet as padding before the AMF content.
  // This allows the WAF to consume the padding while leaving the AMF structure intact.

  String strBody = request.bodyToString();
  return request.withBody(bullet.concat(strBody));
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
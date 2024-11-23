package model.creators;

import burp.api.montoya.core.Range;
import burp.api.montoya.http.message.ContentType;
import burp.api.montoya.http.message.params.HttpParameter;
import burp.api.montoya.http.message.params.HttpParameterType;
import burp.api.montoya.http.message.params.ParsedHttpParameter;
import burp.api.montoya.http.message.requests.HttpRequest;

import java.util.ArrayList;
import java.util.List;

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

  //todo: what should happen when there is a body and url parameters?
  switch(type){
  case URL_ENCODED -> {return addBodyParam(request, bullet);}
  case JSON        -> {return addJsonParam(request, bullet);}
  case XML         -> {return addXmlParam(request, bullet);}
  case MULTIPART   -> {return addMultiPartParam(request, bullet);}
  case AMF         -> {return padAmfWith(request, bullet);}
  case UNKNOWN     -> {return bestEffort(request, bullet);}
  case NONE        -> {return addUrlParam(request, bullet);}
  default          ->
    throw new UnsupportedOperationException("Burp was unable to identify a content type");
  }
}

//-----------------------------------------------------------------------------
private static HttpRequest addUrlParam(HttpRequest request, String bullet){
  int paramLen = "bullet=".length();
  HttpParameter urlParam = HttpParameter.urlParameter(
      "bullet", bullet.substring(0, bullet.length() - paramLen + 1));

  // extract all URL parameters as a list
  List<ParsedHttpParameter> parsedParams = request.parameters(HttpParameterType.URL);

  // remove the parameters from the request
  request = request.withRemovedParameters(parsedParams);

  // convert ParsedHttpParameter to HttpParameter
  List<HttpParameter> params = new ArrayList<>(parsedParams.size() + 1);
  parsedParams.forEach(p -> params.add(HttpParameter.urlParameter(p.name(), p.value())));

  // append the `urlParam` to beginning of the list
  params.add(0, urlParam);

  return request.withAddedParameters(params);
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
  String prepend = "{\"bullet\": \"";
  String append  = "\",";
  StringBuilder payload = new StringBuilder();
  payload.append(prepend);
  payload.append(bullet, 0, bullet.length() - prepend.length() + append.length());
  payload.append(append);
  
  String strBody = request.bodyToString();
  payload.append(strBody, 1, strBody.length()); // skipping starting '{'
  
  return request.withBody(payload.toString());
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
  throw new UnsupportedOperationException(
      "AMF Padding in not yet supported. See https://github.com/ahanel13/Firewall-Ferret/issues/1" +
          " for more information and or to contribute to the project.");
}

//-----------------------------------------------------------------------------
private static HttpRequest bestEffort(HttpRequest request, String bullet) {
  //todo: is this the best way to handle this?
  String strBody = request.bodyToString();
  return request.withBody(bullet.concat(strBody));
}


}
////////////////////////////////////////
// END CLASS RequestBuilder
////////////////////////////////////////
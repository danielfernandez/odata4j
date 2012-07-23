package org.odata4j.cxf.consumer;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.odata4j.consumer.ODataClientException;
import org.odata4j.consumer.Response;
import org.odata4j.producer.resources.HeaderMap;

public class CxfResponse implements Response {

  private final HttpResponse httpResponse;

  public CxfResponse(HttpResponse httpResponse) {
    this.httpResponse = httpResponse;
  }

  public HttpResponse getHttpResponse() {
    return httpResponse;
  }

  @Override
  public MultivaluedMap<String, String> getHeaders() {
    HeaderMap headers = new HeaderMap();
    for (Header header : httpResponse.getAllHeaders()) {
      List<String> valueList = new ArrayList<String>();
      for (HeaderElement element : header.getElements())
        valueList.add(element.toString());

      headers.put(header.getName(), valueList);
    }

    return headers;
  }

  @Override
  public void close() {
    try {
      if (httpResponse.getEntity() != null)
        httpResponse.getEntity().getContent().close();
    } catch (Exception e) {
      throw new ODataClientException("Error while closing socket", e);
    }
  }
}
package us.teaminceptus.inceptusinsights.api;

import java.util.Map;

public interface ServerInformation {

    Map<String, Object> getServerInformation();

    DataURI getServerIcon();

    String getServerName();

}

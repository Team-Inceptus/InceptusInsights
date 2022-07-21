package us.teaminceptus.inceptusinsights.api;

import us.teaminceptus.inceptusinsights.InsightsServer;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public final class DataURI {

    private String contentType;

    private byte[] data;

    private String base64;

    public DataURI(File f) {
        try {
            this.contentType = Files.probeContentType(f.toPath());
            this.data = Files.readAllBytes(f.toPath());
            this.base64 = Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            InsightsServer.LOGGER.severe(e.getMessage());
        }
    }

    public String toDataURI() {
        return "data:" + contentType + ";base64," + base64;
    }
}

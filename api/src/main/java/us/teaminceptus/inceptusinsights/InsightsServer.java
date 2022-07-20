package us.teaminceptus.inceptusinsights;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class InsightsServer extends HttpServlet {

    private static final class DefaultLogger extends Handler {

        @Override
        public void publish(LogRecord record) {
            String sb = "[InceptusInsights " + record.getMillis() + " " + record.getLevel().getName() + "] " +
                    record.getMessage();

            System.out.println(sb);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

    private static Logger logger = Logger.getLogger(DefaultLogger.class.getName());
    private static final URL FRONTEND_FOLDER = InsightsServer.class.getResource("/frontend/");

    private static int port = 30225;

    public static Server frontend;

    public InsightsServer() {
    }

    public static void main(String... args) {
        start("--test", "--port=30325");
    }

    public static void setLogger(Logger l) {
        logger = l;
    }

    public static void setPort(int port) {
        InsightsServer.port = port;
    }

    public static void start(String... args)  {
        try {
            List<String> lArgs = Arrays.asList(args);

            String portStr = lArgs.stream().filter(s -> s.startsWith("--port=")).findFirst().orElse("--port=30325");
            int port = Integer.parseInt(portStr.split("=")[1]);

            if (lArgs.contains("--test")) {
                logger.info("Running Test Configuration");
            }

            frontend = new Server();
            ServerConnector c = new ServerConnector(frontend);
            c.setPort(port);
            frontend.addConnector(c);

            ServletHandler handler = new ServletHandler();
            handler.addServletWithMapping(InsightsServer.class, "/*");
            frontend.setHandler(handler);

            frontend.start();
            logger.info("Started Server on port " + port);
        } catch (Exception e) {
            logger.severe("Error Loading Servers: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getRequestURI().substring(1);
        if (path.equals("")) path = "index.html";

        String end = path.split("\\.")[1];

        if (end.equalsIgnoreCase("png") || end.equalsIgnoreCase("ico")) res.setContentType("image/" + end);
        else res.setContentType("text/" + end);

        File f = new File(FRONTEND_FOLDER.getFile(), path);

        if (!f.exists()) {
            res.setStatus(404);
            f = new File(FRONTEND_FOLDER.getFile(), "404.html");
        }

        if (res.getContentType().startsWith("image")) {
            byte[] buffer = new byte[1024];
            int read;
            FileInputStream is = new FileInputStream(f);

            while ((read = is.read(buffer)) != -1) res.getOutputStream().write(buffer, 0, read);
            is.close();
        } else try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) res.getWriter().println(line);
        }
    }


}

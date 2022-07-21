package us.teaminceptus.inceptusinsights;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.teaminceptus.inceptusinsights.api.ServerInformation;
import us.teaminceptus.inceptusinsights.api.player.PlayerInformation;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.BiFunction;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    public static Logger LOGGER = Logger.getLogger(DefaultLogger.class.getName());
    private static final URL FRONTEND_FOLDER = InsightsServer.class.getResource("/frontend/");

    private static int port = 30225;

    public static Server frontend;

    public InsightsServer() {
    }

    public static void main(String... args) {
        start("--test", "--port=30325");
    }

    public static void setLogger(Logger l) {
        LOGGER = l;
    }

    public static void setPort(int port) {
        InsightsServer.port = port;
    }

    private static ServerInformation server;
    private static PlayerInformation player;

    public static void setInformationFetchers(ServerInformation server, PlayerInformation player) {
        InsightsServer.server = server;
        InsightsServer.player = player;
    }

    private static final Map<String, BiFunction<String, Map<String, String>, String>> DOCUMENT_FUNCTIONS = new HashMap<>() {
    };

    private static final BiFunction<String, Map<String, String>, String> GLOBAL = (html, params) -> {
        Document doc = Jsoup.parse(html, "UTF-8");

        if (server.getServerIcon() != null) doc.getElementById("server-icon").attr("src", server.getServerIcon().toDataURI());

        return doc.outerHtml();
    };

    public static void start(String... args)  {
        try {
            List<String> lArgs = Arrays.asList(args);

            String portStr = lArgs.stream().filter(s -> s.startsWith("--port=")).findFirst().orElse("--port=30325");
            int port = Integer.parseInt(portStr.split("=")[1]);

            if (lArgs.contains("--test")) LOGGER.info("Running Test Configuration");

            frontend = new Server();
            ServerConnector c = new ServerConnector(frontend);
            c.setPort(port);
            frontend.addConnector(c);

            ServletHandler handler = new ServletHandler();
            handler.addServletWithMapping(InsightsServer.class, "/*");
            frontend.setHandler(handler);

            frontend.start();
            LOGGER.info("Started Server on port " + port);
        } catch (Exception e) {
            LOGGER.severe("Error Loading Servers: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getRequestURI().substring(1);
        if (path.equals("")) path = "index.html";

        Map<String, String> params = req.getParameterMap()
                .entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()[0]))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String end = path.split("\\.")[1];

        if (end.equalsIgnoreCase("png") || end.equalsIgnoreCase("ico")) res.setContentType("image/" + end);
        else res.setContentType("text/" + end);

        File f = new File(FRONTEND_FOLDER.getFile(), path);

        if (!f.exists()) {
            res.setStatus(404);
            f = new File(FRONTEND_FOLDER.getFile(), "404.html");
        }

        // Sending
        if (res.getContentType().startsWith("image")) {
            byte[] buffer = new byte[1024];
            int read;
            FileInputStream is = new FileInputStream(f);

            while ((read = is.read(buffer)) != -1) res.getOutputStream().write(buffer, 0, read);
            is.close();
        } else {
            StringBuilder initialContent = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) initialContent.append(line);
            }
            String content = initialContent.toString();
            content = GLOBAL.apply(content, params);
            if (DOCUMENT_FUNCTIONS.containsKey(path)) content = DOCUMENT_FUNCTIONS.get(path).apply(content, params);

            res.getWriter().write(content);
            res.getWriter().close();
        }
    }


}

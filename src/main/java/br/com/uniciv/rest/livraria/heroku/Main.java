package br.com.uniciv.rest.livraria.heroku;

import java.io.File;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * This class launches the web application in an embedded Jetty container. This is the entry point to your application. The Java
 * command that is used for launching should fire this main method.
 */
public class Main {

    public static void main(String[] args) throws Exception{
    	File keystoreFile = new File("server.keystore");
    	final Server server = new Server();
    	
    	HttpConfiguration httpConfig = new HttpConfiguration();
    	httpConfig.setSecureScheme("https");
    	httpConfig.setSecurePort(8443);
    	
    	ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
    	http.setPort(8080);
    	
    	SslContextFactory sslContextFactory = new SslContextFactory();
    	sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
    	sslContextFactory.setKeyStorePassword("livraria");
    	sslContextFactory.setKeyManagerPassword("livraria");
    	
    	HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
    	ServerConnector https = new ServerConnector(server, 
    			new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
    			new HttpConnectionFactory(httpsConfig));
    	https.setPort(8443);
    	server.setConnectors(new ServerConnector[]{http, https});
    	
        // The port that we should run on can be set into an environment variable
        // Look for that variable and default to 8080 if it isn't there.
        //String webPort = System.getenv("PORT");
        //if (webPort == null || webPort.isEmpty()) {
        //    webPort = "8080";
        //}

        //final Server server = new Server(Integer.valueOf(webPort));
        final WebAppContext root = new WebAppContext();

        root.setContextPath("/livraria-virtual");
        // Parent loader priority is a class loader setting that Jetty accepts.
        // By default Jetty will behave like most web containers in that it will
        // allow your application to replace non-server libraries that are part of the
        // container. Setting parent loader priority to true changes this behavior.
        // Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
        root.setParentLoaderPriority(true);

        final String webappDirLocation = "src/main/webapp/";
        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);

        server.setHandler(root);

        server.start();
        server.join();
    }
}

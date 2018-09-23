/**
 * 
 */
package blockchain.rest.server;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import blockchain.BlockchainFacade;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * @author gerardo
 *
 */
@SwaggerDefinition(host = "35.196.28.36:4567", //
		info = @Info(description = "Novahemjo blockchain REST API", //
				version = "V1.0", //
				title = "The blockchain REST api for the Novahemjo project", //
				contact = @Contact(name = "Bonsai", url = "https://novahemjo.dev")), //
		schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS //
		}, consumes = { "application/json" }, produces = { "application/json" }, tags = { @Tag(name = "swagger") })
@Command(name = "Main", description = "Startup the main blockchain REST server.")
public class Main implements Callable<Void> {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	public static final String APP_PACKAGE = "blockchain.rest.server";// .routes
	@Option(names = { "-p", "--port" }, description = "Webserver port, default to http://127.0.0.1:4567.")
	public static String SPARK_PORT = "4567";
	@Option(names = { "-a", "--address" }, description = "Blockchain address, default points to http://127.0.0.1:7100.")
	public static String IP_ADDRESS = "127.0.0.1";
	@Option(names = { "-m",
			"--mode" }, description = "Default settings for deployment mode (local,cloud1,cloud2), defaults to local.")
	public static String MODE = "local";
	@Option(names = { "-h",
			"--help" }, usageHelp = true, description = "Start blockchain REST server, defaults to localhost in port 4567 "
					+ "with log in INFO mode and connects to blockchain node at localhost at port 7100 in local mode.")
	private boolean helpRequested;
	// coinbase accounts
	public static String COINBASE_ACCOUNT_CLOUD = "0x8a5d1cc170b6a8680f6ae11d2b576c6ebb3e430b";
	// coinbase password
	public static String COINBASE_PASSWORD_CLOUD = "paloit2018";
	// document manager contract address
	public static String ETHDOC_CONTRACT_ADDRESS_CLOUD = "0x0193fd94f75b524bdc09b04b1a3ca533bbf29030";
	// claims erc725 contract address
	public static String ERC725_CONTRACT_ADDRESS_CLOUD = "0x1c886233a096a6ab3332182f14272172613ddf98";

	@Override
	public Void call() throws Exception {
		if (helpRequested) {
			CommandLine.usage(this, System.err);
			return null;
		}

		// show defaults
		LOGGER.log(Level.INFO, "web rest address: http://" + getExternalIpAddress() + ":"
				+ SPARK_PORT/* InetAddress.getLocalHost().getHostAddress() */);
		LOGGER.log(Level.INFO, "blockchain address: " + BlockchainFacade.BLOCKCHAIN_NETWORK_URL);
		LOGGER.log(Level.INFO, "blockchain gas price: " + BlockchainFacade.NOVAHEMJO_GAS_PRICE);
		LOGGER.log(Level.INFO, "blockchain gas limit: " + BlockchainFacade.NOVAHEMJO_GAS_LIMIT);
		LOGGER.log(Level.INFO, "coinbase account address: " + COINBASE_ACCOUNT_CLOUD);
		LOGGER.log(Level.INFO, "coinbase account password: " + COINBASE_PASSWORD_CLOUD.replaceAll(".", "*"));
		LOGGER.log(Level.INFO, "vecinos condominium contract address: " + ETHDOC_CONTRACT_ADDRESS_CLOUD);
		LOGGER.log(Level.INFO, "vecinos payment contract address: " + ERC725_CONTRACT_ADDRESS_CLOUD);

		// otherwise startup the spark java server...
		port(Integer.parseInt(SPARK_PORT));
		staticFiles.location("/ui");

		before(new CorsFilter());
		new OptionsFilter();

		// scan classes with @Api annotation and add as routes
		RouteBuilder.setupRoutes(APP_PACKAGE);

		// Build swagger json description
		final String swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);
		get("/swagger", (req, res) -> {
			return swaggerJson;
		});

		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		CommandLine.call(new Main(), System.err, args);
	}

	public static String getExternalIpAddress() throws Exception {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine();
			return ip;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

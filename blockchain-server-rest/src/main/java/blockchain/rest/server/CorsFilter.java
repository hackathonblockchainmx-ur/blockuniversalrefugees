/**
 * 
 */
package blockchain.rest.server;

import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * @author gerardo
 *
 */
public class CorsFilter implements Filter {
	private static final String headerAllowHeaders = "Access-Control-Allow-Headers";
	private static final String headerAllowOrigin = "Access-Control-Allow-Origin";
	private static final String headerRequestMethod = "Access-Control-Request-Method";
	private static final String wildcard = "*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see spark.Filter#handle(spark.Request, spark.Response)
	 */
	@Override
	public void handle(Request request, Response response) throws Exception {
		response.header(headerAllowOrigin, wildcard);
		response.header(headerRequestMethod, wildcard);
		response.header(headerAllowHeaders, wildcard);
	}

}

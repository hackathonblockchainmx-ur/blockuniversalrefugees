/**
 * 
 */
package blockchain.rest.server.routes;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import blockchain.BlockchainFacade;
import blockchain.rest.server.models.ApiError;
import blockchain.rest.server.models.Document;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author gerardo
 *
 */
@Api
@Produces("application/json")
@Path("/user/{walletaddress}/document/{documentid}")
public class GetFileRoute implements Route {
	private static final Logger LOGGER = Logger.getLogger(GetFileRoute.class.getName());
	private ObjectMapper mapper;

	public GetFileRoute() {
		mapper = new ObjectMapper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see spark.Route#handle(spark.Request, spark.Response)
	 */
	@GET
	@ApiOperation(value = "Get a document from refuge", nickname = "GetDocumentRoute")
	@ApiImplicitParams({ //
			// @ApiImplicitParam(required = true, dataType = "string", name = "auth",
			// paramType = "header"), //
			@ApiImplicitParam(required = true, dataType = "string", name = "walletaddress", paramType = "path"), //
			@ApiImplicitParam(required = true, dataType = "string", name = "documentid", paramType = "path") //
	}) //
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "Success", response = Document.class), //
			@ApiResponse(code = 400, message = "Invalid input data", response = ApiError.class), //
			@ApiResponse(code = 500, message = "Error while retrieveing document", response = ApiError.class) //
	})
	@Override
	public Object handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response)
			throws Exception {
		if (StringUtils.isEmpty(request.params("walletaddress")) || StringUtils.isEmpty(request.params("documentid"))) {
			ApiError error = new ApiError();
			error.setCode(2);
			error.setMessage("Invalid input data");
			response.status(400);
			return mapper.writeValueAsString(error);
		}
		try {
			final Document documentFromRefuge = BlockchainFacade.getInstance().getDocumentOfRefuge(
					request.params("walletaddress"), new BigInteger(request.params("condominiumid")));
			response.status(200);
			return mapper.writeValueAsString(documentFromRefuge);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			ApiError error = new ApiError();
			error.setCode(1);
			error.setMessage(e.getMessage());
			response.status(500);
			return mapper.writeValueAsString(error);
		}
	}

}

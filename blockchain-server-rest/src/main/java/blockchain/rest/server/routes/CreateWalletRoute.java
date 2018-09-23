/**
 * 
 */
package blockchain.rest.server.routes;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;

import blockchain.BlockchainFacade;
import blockchain.rest.server.models.ApiError;
import blockchain.rest.server.models.Wallet;
import io.swagger.annotations.Api;
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
@Path("/user/wallet")
@Produces("application/json")
public class CreateWalletRoute implements Route {
	private static final Logger LOGGER = Logger.getLogger(CreateWalletRoute.class.getName());
	private ObjectMapper mapper;

	public CreateWalletRoute() throws Exception {
		mapper = new ObjectMapper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see spark.Route#handle(spark.Request, spark.Response)
	 */
	@POST
	@ApiOperation(value = "Creates a new user wallet", nickname = "CreateWalletRoute")
	// @ApiImplicitParams({ //
	// @ApiImplicitParam(required = true, dataType = "string", name = "auth",
	// paramType = "header") //
	// }) //
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "Success", response = Wallet.class), //
			@ApiResponse(code = 500, message = "Error while creating wallet", response = ApiError.class) //
	})
	@Override
	public Object handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response)
			throws Exception {
		try {
			final Wallet newWalletForRefuge = BlockchainFacade.getInstance()
					.createWalletForRefuge();
			response.status(200);
			return mapper.writeValueAsString(newWalletForRefuge);
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

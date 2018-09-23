/**
 * 
 */
package blockchain.rest.server.routes;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import blockchain.BlockchainFacade;
import blockchain.rest.server.models.ApiError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author gerardo
 *
 */
@Api
@Produces("application/json")
@Path("/user/{walletaddressrefuge}/document/{documentid}/issuer/{walletaddressissuer}/verify")
public class VerifyRefugeClaimRoute implements Route {
	private static final Logger LOGGER = Logger.getLogger(VerifyRefugeClaimRoute.class.getName());
	private ObjectMapper mapper;

	public VerifyRefugeClaimRoute() {
		mapper = new ObjectMapper();
	}

	@PATCH
	@ApiOperation(value = "Validate a document from refuge", nickname = "VerifyRefugeClaimRoute")
	@ApiImplicitParams({ //
			// @ApiImplicitParam(required = true, dataType = "string", name = "auth",
			// paramType = "header"), //
			@ApiImplicitParam(required = true, dataType = "string", name = "walletaddressrefuge", paramType = "path"), //
			@ApiImplicitParam(required = true, dataType = "string", name = "documentid", paramType = "path"), //
			@ApiImplicitParam(required = true, dataType = "string", name = "walletaddressissuer", paramType = "path") }) //
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "Success", response = Boolean.class), //
			@ApiResponse(code = 400, message = "Invalid input data", response = ApiError.class), //
			@ApiResponse(code = 500, message = "Error while validating refuge document", response = ApiError.class) //
	})
	@Override
	public Object handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response)
			throws Exception {
		if (StringUtils.isEmpty(request.params("walletaddressrefuge"))
				|| StringUtils.isEmpty(request.params("documentid"))
				|| StringUtils.isEmpty(request.params("walletaddressissuer"))) {
			ApiError error = new ApiError();
			error.setCode(2);
			error.setMessage("Invalid input data");
			response.status(400);
			return mapper.writeValueAsString(error);
		}
		try {
			final Boolean validatedDocumentFromRefuge = BlockchainFacade.getInstance().claimDocumentFromRefuge(
					request.params("walletaddressrefuge"), new BigInteger(request.params("condominiumid")),
					request.params("walletaddressissuer"));
			response.status(200);
			return mapper.writeValueAsString(validatedDocumentFromRefuge);
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
/**
 * 
 */
package blockchain.rest.server.routes;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import blockchain.BlockchainFacade;
import blockchain.rest.server.models.ApiError;
import blockchain.rest.server.models.Document;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@javax.ws.rs.Path("/user/{walletaddress}/document")
public class UploadFileRoute implements Route {
	private static final Logger LOGGER = Logger.getLogger(UploadFileRoute.class.getName());
	private ObjectMapper mapper;

	public UploadFileRoute() {
		mapper = new ObjectMapper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see spark.Route#handle(spark.Request, spark.Response)
	 */
	@POST
	@ApiOperation(value = "Upload a legal document from refuge", nickname = "UploadFileRoute")
	@ApiImplicitParams({ //
			// @ApiImplicitParam(required = true, dataType = "string", name = "auth",
			// paramType = "header"), //
			@ApiImplicitParam(required = true, dataType = "string", name = "walletaddress", paramType = "path"), //
			@ApiImplicitParam(required = true, dataType = "string", name = "type", paramType = "path") }) //
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "Success", response = Document.class), //
			@ApiResponse(code = 400, message = "Invalid input data", response = ApiError.class), //
			@ApiResponse(code = 500, message = "Error while uploading legal document", response = ApiError.class) //
	})
	@Override
	public Object handle(Request request, Response response) throws Exception {
		if (StringUtils.isEmpty(request.params("walletaddress")) || StringUtils.isEmpty(request.params("type"))) {
			ApiError error = new ApiError();
			error.setCode(2);
			error.setMessage("Invalid input data");
			response.status(400);
			return mapper.writeValueAsString(error);
		}
		try {
			String location = "image"; // the directory location where files will be stored
			long maxFileSize = 100000000; // the maximum size allowed for uploaded files
			long maxRequestSize = 100000000; // the maximum size allowed for multipart/form-data requests
			int fileSizeThreshold = 1024; // the size threshold after which files will be written to disk
			MultipartConfigElement multipartConfigElement = new MultipartConfigElement(location, maxFileSize,
					maxRequestSize, fileSizeThreshold);
			request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
			Collection<Part> parts = request.raw().getParts();
			for (Part part : parts) {
				LOGGER.info("Name: " + part.getName());
				LOGGER.info("Size: " + part.getSize());
				LOGGER.info("Filename: " + part.getSubmittedFileName());
			}
			String fName = request.raw().getPart("file").getSubmittedFileName();
			LOGGER.info("Title: " + request.raw().getParameter("title"));
			LOGGER.info("File: " + fName);
			Part uploadedFile = request.raw().getPart("file");
			Path out = Paths.get("image/" + fName);
			try (final InputStream in = uploadedFile.getInputStream()) {
				Files.copy(in, out);
				uploadedFile.delete();
			}
			// cleanup
			multipartConfigElement = null;
			parts = null;
			uploadedFile = null;
			// create the file in the IPFS...
			final IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
			final NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(out.toFile());
			final MerkleNode addResult = ipfs.add(file).get(0);
			final String ipfsHashId = addResult.hash.toHex();
			final Document document = BlockchainFacade.getInstance()
					.uploadDocumentForRefuge(request.params("walletaddress"), request.params("type"), ipfsHashId);
			response.status(200);
			return mapper.writeValueAsString(document);
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

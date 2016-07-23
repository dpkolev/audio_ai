package edu.sofia.fmi.audiorec.controller;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.sofia.fmi.audiorec.database.model.SearchHash;
import edu.sofia.fmi.audiorec.service.SearchHashService;

/**
 * Question Resource
 */
@Component
@Path("/search_hash")
public class SearchHashController {

	private SearchHashService searchHashService;

	@Autowired
	public SearchHashController(SearchHashService searchHashService) {
		this.searchHashService = searchHashService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() throws JSONException {
		List<SearchHash> hashes = searchHashService.findAll();
		return Response.ok(hashes).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) throws JSONException {
		List<SearchHash> hashes = searchHashService.findById(id);
		return Response.ok(hashes).build();
	}

	@GET
	@Path("/song_id/{song_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findBySongId(@PathParam("song_id") long songId)
			throws JSONException {
		List<SearchHash> hashes = searchHashService.findBySongId(songId);
		return Response.ok(hashes).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addHash(SearchHash hash) throws Exception {
		SearchHash result = searchHashService.save(hash);
		return Response
				.created(new URI(
						"/backend/api/v1/search_hash/" + result.getHash()))
				.entity(result).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateHash(SearchHash hash) throws Exception {
		return Response.status(Status.METHOD_NOT_ALLOWED)
				.entity("Cannot update search hashes !!!").build();
	}

	@DELETE
	@Path("/{song_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("song_id") long songId) throws Exception {
		searchHashService.delete(songId);
		return Response.status(Status.OK).entity(songId).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(SearchHash hash) throws Exception {
		String hashStr = new String(hash.toString());
		searchHashService.delete(hash);
		return Response.status(Status.OK).entity(hashStr).build();
	}

}

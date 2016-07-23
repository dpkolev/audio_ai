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

import edu.sofia.fmi.audiorec.database.model.Song;
import edu.sofia.fmi.audiorec.service.SongService;

/**
 * Question Resource
 */
@Component
@Path("/song")
public class SongController {

	private SongService songService;

	@Autowired
	public SongController(SongService songService) {
		this.songService = songService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() throws JSONException {
		List<Song> songs = songService.findAll();
		return Response.ok(songs).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) throws JSONException {
		List<Song> songs = songService.findById(id);
		return Response.ok(songs).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Song song) throws Exception {
		Song result = songService.save(song);
		return Response
				.created(new URI("/backend/api/v1/song/" + result.getId()))
				.entity(result).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Song song) throws Exception {
		Song updatedQuestion = songService.update(song);
		return Response.noContent().entity(updatedQuestion).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(Song song) throws Exception {
		String songStr = new String(song.toString());
		songService.delete(song);
		return Response.ok().entity(songStr).build();
	}

	@DELETE
	@Path("/{song_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("song_id") long songId) throws Exception {
		songService.delete(songId);
		return Response.status(Status.OK).entity(songId).build();
	}

}

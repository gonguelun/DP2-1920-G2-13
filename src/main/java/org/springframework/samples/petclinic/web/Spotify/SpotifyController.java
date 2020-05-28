
package org.springframework.samples.petclinic.web.Spotify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Spotify.mappers.albums.Item;
import org.springframework.samples.petclinic.service.Spotify.SpotifyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SpotifyController {

	private final SpotifyService spotifyService;


	@Autowired
	public SpotifyController(final SpotifyService spotifyService) {
		this.spotifyService = spotifyService;
	}

	@GetMapping("/api/spotify/query")
	public String searchSpotify() {
		return "spotify/search";
	}

	@GetMapping(value = "/api/spotify/search/{query}")
	public String listTracksApi(@PathVariable("query") final String query, final Map<String, Object> model) {
		List<Item> item = new ArrayList<>();

		try {

			item = this.spotifyService.tracksPageApi(query).getAlbums().getItems();

		} catch (Exception e) {

			return "redirect:/oups";
		}

		model.put("albums", item);
		return "spotify/albumList";
	}

}

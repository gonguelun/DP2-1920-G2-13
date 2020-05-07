
package org.springframework.samples.petclinic.web.Spotify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Spotify.mappers.albums.Item;
import org.springframework.samples.petclinic.model.Spotify.mappers.tracks.Track;
import org.springframework.samples.petclinic.service.Spotify.SpotifyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpotifyController {

	private final SpotifyService spotifyService;


	@Autowired
	public SpotifyController(final SpotifyService spotifyService) {
		this.spotifyService = spotifyService;
	}

	// Mapping inicial, aqui se hace la redirección a Spotify, y tras aceptar Spotify nos devuelve a /callback/

	@RequestMapping("/api/spotify/access")
	public String spotifyAccess() {
		return "redirect:" + this.spotifyService.getCodeAndRedirection();
	}

	//	Si el usuario acepta la petición del scope, se recibe esta respuesta (por ejemplo) desde la api de Spotify:
	//	https://example.com/callback?code=NApCCg..BkWtQ&state=profile%2Factivity

	@GetMapping(value = "/callback/", params = "code")
	public String listTracks(@RequestParam("code") final String code, final Map<String, Object> model) {
		List<Track> aux = new ArrayList<Track>();

		try {
			aux = this.spotifyService.listTracks(code);
		} catch (Exception e) {

			return "redirect:/oups";
		}

		model.put("tracks", aux);
		return "spotify/tracksList";
	}

	//	 Si el usuario no acepta la petición, le devuelve a la página principal
	//	https://example.com/callback?error=...state=...
	@GetMapping(value = "/callback/", params = "error")
	public String userHasNotAccepted(@RequestParam("error") final String error) {
		return "redirect:/";
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

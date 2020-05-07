
package org.springframework.samples.petclinic.service.Spotify;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Spotify.mappers.TokenMapper;
import org.springframework.samples.petclinic.model.Spotify.mappers.albums.SearchAlbum;
import org.springframework.samples.petclinic.model.Spotify.mappers.tracks.Track;
import org.springframework.samples.petclinic.model.Spotify.mappers.tracks.TracksPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyService {

	private static final String	SPOTIFY_OBTAIN_CODE		= "https://accounts.spotify.com/authorize";
	private static final String	SPOTIFY_CLIENT_ID		= "58c54265caac4e80aa3bfe97fa094be6";
	private static final String	SPOTIFY_MY_TRACKS_SCOPE	= "user-library-read";
	private static final String	REDIRECT_URI			= "http://localhost:8080/callback/";

	private static final String	SPOTIFY_BASE64CODE		= "NThjNTQyNjVjYWFjNGU4MGFhM2JmZTk3ZmEwOTRiZTY6ODA2OTM4ODI5NzU5NDY4NDk2MjE5ZmQ3MjQwN2RlY2I";
	private static final String	URL_SPOTIFY_TOKEN		= "https://accounts.spotify.com/api/token";
	private static final String	URL_SPOTIFY_MY_TRACKS	= "https://api.spotify.com/v1/me/tracks";


	@Autowired
	public SpotifyService() {
	}

	/**
	 * Gets a token on Spotify API to perform requests.
	 *
	 * @return a {@link TokenMapper} object.
	 */

	@Transactional
	public String getCodeAndRedirection() {

		return SpotifyService.SPOTIFY_OBTAIN_CODE + "?response_type=code" + "&client_id=" + SpotifyService.SPOTIFY_CLIENT_ID + "&scope=" + SpotifyService.SPOTIFY_MY_TRACKS_SCOPE + "&redirect_uri=" + SpotifyService.REDIRECT_URI;
	}

	@Transactional
	public List<Track> listTracks(final String code) {
		List<Track> res = new ArrayList<Track>();
		TracksPage pagina = this.tracksPage(code);
		res = pagina.getItems().stream().map(i -> i.getTrack()).collect(Collectors.toList());
		return res;
	}

	private TracksPage tracksPage(final String code) {		// Recibimos una página que contiene una lista de SavedTracks
		TokenMapper tokenMapper = this.getToken(code);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + tokenMapper.getAccessToken());

		HttpEntity<String> httpEntity = new HttpEntity<>("headers", httpHeaders);

		RestTemplate restTemplate = new RestTemplate();

		TracksPage page = new TracksPage();

		try {
			ResponseEntity<TracksPage> response = restTemplate.exchange(SpotifyService.URL_SPOTIFY_MY_TRACKS, HttpMethod.GET, httpEntity, TracksPage.class);

			if (response.getStatusCode().equals(HttpStatus.OK) && !Objects.isNull(response.getBody())) {
				page = response.getBody();
			}
		} catch (HttpClientErrorException ex) {
			throw new GetSpotifyTracksException(ex.getMessage());
		}

		return page;
	}

	private TokenMapper getToken(final String code) {					// Intercambiar código obtenido por un token haciendo una peticion POST
		HttpHeaders httpHeaders = new HttpHeaders();					// Header con authorization code codificado en base64
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.add("Authorization", "Basic " + SpotifyService.SPOTIFY_BASE64CODE);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>(); // Body con los parametros solicitados (tipo de permiso, código recibido y uri de callback)
		map.add("grant_type", "authorization_code");
		map.add("code", code);
		map.add("redirect_uri", SpotifyService.REDIRECT_URI);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);  // Peticion (Header + body)

		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<TokenMapper> response = restTemplate.exchange(SpotifyService.URL_SPOTIFY_TOKEN, HttpMethod.POST, request, TokenMapper.class);
			return response.getBody();
		} catch (HttpClientErrorException ex) {
			throw new GetSpotifyTokenException(ex.getMessage());
		}
	}

	public SearchAlbum tracksPageApi(final String query) {		// Recibimos una página que contiene una lista de SavedTracks
		TokenMapper tokenMapper = this.getTokenApi();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + tokenMapper.getAccessToken());

		HttpEntity<String> httpEntity = new HttpEntity<>("headers", httpHeaders);

		RestTemplate restTemplate = new RestTemplate();

		SearchAlbum page = new SearchAlbum();

		try {
			ResponseEntity<SearchAlbum> response = restTemplate.exchange("https://api.spotify.com/v1/search?q=" + query + "&type=album&limit=5", HttpMethod.GET, httpEntity, SearchAlbum.class);

			if (response.getStatusCode().equals(HttpStatus.OK) && !Objects.isNull(response.getBody())) {
				page = response.getBody();
			}
		} catch (HttpClientErrorException ex) {
			throw new GetSpotifyTracksException(ex.getMessage());
		}

		return page;
	}

	private TokenMapper getTokenApi() {
		// Intercambiar código obtenido por un token haciendo una peticion POST

		String authToken = EncodeToken.getAuthToken("a50470cef9114d7caf7fb9c074bb12a8", "152f88761b544da380b3983636e72448");

		HttpHeaders httpHeaders = new HttpHeaders();					// Header con authorization code codificado en base64
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.add("Authorization", "Basic " + authToken);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>(); // Body con los parametros solicitados (tipo de permiso, código recibido y uri de callback)
		map.add("grant_type", "client_credentials");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);  // Peticion (Header + body)

		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<TokenMapper> response = restTemplate.exchange(SpotifyService.URL_SPOTIFY_TOKEN, HttpMethod.POST, request, TokenMapper.class);
			return response.getBody();
		} catch (HttpClientErrorException ex) {
			throw new GetSpotifyTokenException(ex.getMessage());
		}
	}

}

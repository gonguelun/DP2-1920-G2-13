
package org.springframework.samples.petclinic.service.Spotify;

/**
 * Exception thrown when trying get albums on Spotify API.
 *
 * @author Roberto Amadeu Neto
 * @since 09/02/2019
 * @version 1.0
 */
public class GetSpotifyTracksException extends RuntimeException {

	public GetSpotifyTracksException(final String message) {
		super("Error trying to get tracks on Spotify API. Cause: " + message);
	}
}

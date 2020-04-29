
package org.springframework.samples.petclinic.model.Spotify.mappers;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * A simple POJO that represents the result of the Spotify API's /token
 * endpoint.
 *
 * @author Roberto Amadeu Neto
 * @since 08/02/2019
 * @since 1.0
 */

@Data
public class TokenMapper implements Serializable {

	@JsonProperty("access_token")
	private String	accessToken;

	@JsonProperty("token_type")
	private String	tokenType;

	@JsonProperty("expires_in")
	private int		expiresIn;

	@JsonProperty("scope")
	private String	scope;

	@JsonProperty("refresh_token")
	private String	refreshToken;


	public TokenMapper() {
	}

}

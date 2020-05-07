
package org.springframework.samples.petclinic.integration;

import org.apache.commons.codec.binary.Base64;

public class EncodeToken {

	public static String getAuthToken(final String clientid, final String clientSecret) {
		String idSecret = clientid + ":" + clientSecret;
		byte[] bytesEncoded = Base64.encodeBase64(idSecret.getBytes());
		return new String(bytesEncoded);
	}
}

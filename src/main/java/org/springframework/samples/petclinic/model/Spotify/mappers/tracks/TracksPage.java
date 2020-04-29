
package org.springframework.samples.petclinic.model.Spotify.mappers.tracks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"items"
})
public class TracksPage {

	@JsonProperty("items")
	private List<Item>			items					= null;
	@JsonIgnore
	private Map<String, Object>	additionalProperties	= new HashMap<String, Object>();


	/**
	 * No args constructor for use in serialization
	 *
	 */
	public TracksPage() {
	}

	/**
	 *
	 * @param items
	 */
	public TracksPage(final List<Item> items) {
		super();
		this.items = items;
	}

	@JsonProperty("items")
	public List<Item> getItems() {
		return this.items;
	}

	@JsonProperty("items")
	public void setItems(final List<Item> items) {
		this.items = items;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(final String name, final Object value) {
		this.additionalProperties.put(name, value);
	}

}

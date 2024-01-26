package vttp2023.batch4.paf.assessment.services;

import java.io.StringReader;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue.ValueType;

@Service
public class ForexService {

	public static final String converterBaseUrl = "https://api.frankfurter.app/latest";
	// TODO: Task 5 
	public float convert(String from, String to, float amount) {

		String fullConversionUrl = UriComponentsBuilder
		.fromUriString(converterBaseUrl)
		.queryParam("amount", amount)
		.queryParam("from", from)
		.queryParam("to", to)
		.toUriString();

		RequestEntity<Void> req = RequestEntity.get(fullConversionUrl).build();

		RestTemplate template = new RestTemplate();

		ResponseEntity<String> resp = template.exchange(req, String.class);

		if (resp.hasBody()) {
			JsonObject jsonObj = Json.createReader(new StringReader(resp.getBody()))
				.readObject();

			float convertedAmt = jsonObj
				.getJsonObject("rates")
				.getJsonNumber(to.toUpperCase())
				.bigDecimalValue()
				.floatValue();

			return convertedAmt;
			
		} else {
			return -1000f;
		}

	}
}

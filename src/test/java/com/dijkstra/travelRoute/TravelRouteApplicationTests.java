package com.dijkstra.travelRoute;

import com.dijkstra.travelRoute.model.dto.RouteDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TravelRouteApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void getGivenRouteByIdShouldReturnOriginAndDestinationAndCost_ID1() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/1", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());

		JsonNode origin = root.path("origin");
		assertThat(origin.asText(), is("GRU"));

		JsonNode destination = root.path("destination");
		assertThat(destination.asText(), is("BRC"));

		JsonNode cost = root.path("cost");
		assertThat(cost.asDouble(), is(10.0));

	}

	@Test
	public void getGivenOriginAndDestinationShouldReturnPathAndCost_GRU_CDG() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/origin/GRU/destination/CDG", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());

		JsonNode name = root.path("path");
		assertThat(name.asText(), is("GRU - BRC - CDG"));

		JsonNode cost = root.path("cost");
		assertThat(cost.asDouble(), is(30.0));
	}

	@Test
	public void getGivenOriginAndDestinationShouldReturnPathAndCost_CDG_BRC() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/origin/cdg/destination/BRC", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());

		JsonNode name = root.path("path");
		assertThat(name.asText(), is("CDG - SCL - ORL - GRU - BRC"));

		JsonNode cost = root.path("cost");
		assertThat(cost.asDouble(), is(140.0));
	}

	@Test
	public void getGivenOriginAndDestinationShouldReturnPathAndCost_GRU_ORL() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/origin/Gru/destination/Orl", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());

		JsonNode name = root.path("path");
		assertThat(name.asText(), is("GRU - BRC - SCL - ORL"));

		JsonNode cost = root.path("cost");
		assertThat(cost.asDouble(), is(35.0));
	}

	@Test
	public void getGivenOriginAndDestinationShouldReturnPathAndCost_PTO_CDG() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/origin/PTO/destination/CDG", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());

		JsonNode name = root.path("path");
		assertThat(name.asText(), is("PTO - SCL - ORL - CDG"));

		JsonNode cost = root.path("cost");
		assertThat(cost.asDouble(), is(32.0));
	}

	@Test
	public void getGivenOriginAndDestinationShouldReturnPathAndCost_CDG_ORL() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/origin/CDG/destination/ORL", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());

		JsonNode name = root.path("path");
		assertThat(name.asText(), is("CDG - SCL - ORL"));

		JsonNode cost = root.path("cost");
		assertThat(cost.asDouble(), is(100.0));
	}

	@Test
	public void getGivenOriginAndDestinationShouldReturnPathAndCost_PTO_GRU() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/origin/PTO/destination/GRU", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());

		JsonNode name = root.path("path");
		assertThat(name.asText(), is("PTO - SCL - ORL - GRU"));

		JsonNode cost = root.path("cost");
		assertThat(cost.asDouble(), is(57.0));
	}

	@Test
	public void getGivenOriginAndDestinationShouldReturnNotFound() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/origin/SCL/destination/PTO", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));

	}

	@Test
	public void postSendRouteDTOShouldReturnCreated() throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<RouteDTO> request = new HttpEntity<>(new RouteDTO("PTO", "BRC", 60.5));
		ResponseEntity<RouteDTO> response = restTemplate
				.exchange("http://localhost:" + port + "/routes", HttpMethod.POST, request, RouteDTO.class);

		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

		RouteDTO route = response.getBody();

		assertThat(route.getOrigin(), is("PTO"));
		assertThat(route.getDestination(), is("BRC"));
		assertThat(route.getCost(), is(60.5));
	}

	@Test
	public void getGivenOriginAndDestinationShouldReturnPathAndCost_PTO_BRC() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/origin/PTO/destination/BRC", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());

		JsonNode name = root.path("path");
		assertThat(name.asText(), is("PTO - BRC"));

		JsonNode cost = root.path("cost");
		assertThat(cost.asDouble(), is(60.5));
	}

	@Test
	public void getGivenRouteByIdShouldReturnOriginAndDestinationAndCost_ID13() throws Exception {

		ResponseEntity<String> response
				= restTemplate.getForEntity("http://localhost:" + port + "/routes/13", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());

		JsonNode origin = root.path("origin");
		assertThat(origin.asText(), is("PTO"));

		JsonNode destination = root.path("destination");
		assertThat(destination.asText(), is("BRC"));

		JsonNode cost = root.path("cost");
		assertThat(cost.asDouble(), is(60.5));

	}

	@Test
	public void deleteRouteByOriginAndDestinationShouldReturnOk_PTO_BRC() {

		restTemplate.delete("http://localhost:" + port + "/routes/origin/PTO/destination/BRC");

	}
}

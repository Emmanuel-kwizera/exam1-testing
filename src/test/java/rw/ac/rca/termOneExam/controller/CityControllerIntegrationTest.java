package rw.ac.rca.termOneExam.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.utils.APICustomResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class CityControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getAll_success() throws JSONException {
        String response = this.testRestTemplate.getForObject("/api/cities/all", String.class);
        JSONAssert.assertEquals("[{\"id\":101,\"name\":\"Kigali\",\"weather\":24.0,\"fahrenheit\":75.2},{\"id\":102,\"name\":\"Musanze\",\"weather\":18.0,\"fahrenheit\":64.4},{\"id\":103,\"name\":\"Rubavu\",\"weather\":20.0,\"fahrenheit\":68.0},{\"id\":104,\"name\":\"Nyagatare\",\"weather\":28.0,\"fahrenheit\":82.4}]", response, true);
    }

    @Test
    public void getById_successObject() {
        City city = this.testRestTemplate.getForObject("/api/cities/id/101",City.class);
        assertEquals("Kigali", city.getName());
        assertEquals(24, city.getWeather());
    }

    @Test
    public void getById_success() throws JSONException {
        ResponseEntity<City> response = this.testRestTemplate.getForEntity("/api/cities/id/101", City.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(24.0, response.getBody().getWeather(),0);
        assertEquals("Kigali", response.getBody().getName());

    }

    @Test
    public void getById_notFound() {
        ResponseEntity<APICustomResponse> response = this.testRestTemplate.getForEntity("/api/cities/id/554", APICustomResponse.class);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("City not found with id 554", response.getBody().getMessage());
    }

    @Test
    public void saveCity_Success() {

        CreateCityDTO city1 = new CreateCityDTO();
        city1.setName("Rusizi");
        city1.setWeather(11);

        ResponseEntity<City> response = this.testRestTemplate.postForEntity("/api/cities/add", city1, City.class);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Rusizi",response.getBody().getName());
        assertEquals(11,response.getBody().getWeather(),0);
    }

    @Test
    public void save_failure() {

        CreateCityDTO city1 = new CreateCityDTO();
        city1.setName("Kigali");
        city1.setWeather(10);

        ResponseEntity<APICustomResponse> response = this.testRestTemplate.postForEntity("/api/cities/add",city1, APICustomResponse.class);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("City name Kigali is registered already", response.getBody().getMessage());
    }


}

package rw.ac.rca.termOneExam.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.repository.ICityRepository;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

    @Mock
    private ICityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    public void getAll_success(){
        when(cityRepository.findAll()).thenReturn(Arrays.asList(new City(101, "Kigali", 24, 75.2),
                new City(102, "Musanze", 14, 64.4)));
        assertEquals(14,cityService.getAll().get(1).getWeather());
    }

    @Test
    public void  getOneById_success(){
        City city = new City(10,"Rubavu",20,68);
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));
        assertEquals(cityService.getById(10L).get(), city);
    }

    @Test
    public void getOneById_fail(){
        Optional emptyOptional = Optional.empty();
        when(cityRepository.findById(10L)).thenReturn(emptyOptional);
        assertTrue(cityService.getById(10L).isEmpty());
    }

    @Test
    public void save_success(){
        CreateCityDTO cityDTO = new CreateCityDTO();
        cityDTO.setName("Dubai");
        cityDTO.setWeather(20);

        City city = new City(cityDTO.getName(),cityDTO.getWeather());
        when(cityRepository.save(any(City.class))).thenReturn(city);
        assertNotNull(cityService.save(cityDTO).getId());
        assertEquals(cityDTO.getWeather(), cityService.save(cityDTO).getWeather(),0);
    }

    @Test
    public void SaveCity_Fail(){
        when(cityRepository.save(any(City.class))).thenReturn(null);
        City city = cityService.save(new CreateCityDTO());
        assertNull(city);
    }
}

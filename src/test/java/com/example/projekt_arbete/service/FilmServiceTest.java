package com.example.projekt_arbete.service;

import com.example.projekt_arbete.dao.IFilmDAO;
import com.example.projekt_arbete.model.CustomUser;
import com.example.projekt_arbete.model.FilmDTO;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.ErrorResponse;
import com.example.projekt_arbete.response.IntegerResponse;
import com.example.projekt_arbete.response.Response;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    //@Mock
    //private FilmRepository filmRepository;

    @Mock
    private IFilmDAO filmDao;
    @Mock
    private RateLimiter rateLimiter;

    @InjectMocks
    private FilmService filmService;

    private FilmModel mockFilm;
    private CustomUser mockUser;

    @BeforeEach
    public void setUp() {

        mockFilm = new FilmModel();
        mockFilm.setId(1);
        mockFilm.setTitle("Test Film");
        mockFilm.setOriginal_title("Test Film");
        mockFilm.setFilmid(101);
        mockFilm.setOrigin_country(Arrays.asList("US"));
        mockFilm.setRuntime(120);
        mockFilm.setBudget(1000000);
        mockFilm.setPoster_path("/22wNUqKyz2m6wzAt31f26H8Y433.jpg");

        filmDao.save(mockFilm);



        mockUser = new CustomUser();
        mockUser.setUsername("testuser");
    }

//    @Test
//    public void testSaveFilm() throws Exception {
//        // Mock URL connection and InputStream
//        URL mockUrl = Mockito.mock(URL.class);
//        URLConnection mockConnection = Mockito.mock(URLConnection.class);
//        InputStream mockInputStream = Mockito.mock(InputStream.class);
//
//        when(mockUrl.openConnection()).thenReturn(mockConnection);
//        when(mockConnection.getInputStream()).thenReturn(mockInputStream);
//
//        // Mock the response for InputStream read
//        ByteArrayOutputStream mockByteArrayOutputStream = Mockito.mock(ByteArrayOutputStream.class);
//        when(mockByteArrayOutputStream.toByteArray()).thenReturn(new byte[0]); // Mock empty byte array
//
//        // Mock FilmRepository save behavior
//        when(filmRepository.save(any(FilmModel.class))).thenReturn(mockFilm);
//        when(filmRepository.findByTitle(anyString())).thenReturn(Optional.empty());
//
//        // Execute the method
//        ResponseEntity<Response> response = filmService.save(mockFilm);
//
//        // Assert the results
//        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
//        verify(mockConnection, times(1)).connect(); // Ensure that connection is attempted once
//    }



    @Test
    public void testGetFilmByIdFound() {

        when(filmDao.findById(1)).thenReturn(Optional.of(mockFilm));

        ResponseEntity<Response> response = filmService.findById(1);

        assertEquals(200, response.getStatusCodeValue(), "Should be 200");
        assertTrue(response.getBody() instanceof FilmModel, "Checks if response.getBody() is a FilmModel");
    }

    @Test
    public void testGetFilmByIdNotFound() {

        when(filmDao.findById(999)).thenReturn(Optional.empty());

        ResponseEntity<Response> response = filmService.findById(999);

        assertEquals(404, response.getStatusCodeValue(), "shouldn't find");
        assertTrue(response.getBody() instanceof ErrorResponse, "throws an ErrorResponse");
    }

    @Test
    public void testDeleteByIDSuccess () throws Exception {

        Integer filmId = 1;
        when(filmDao.findById(filmId)).thenReturn(Optional.of(mockFilm)); // Film exists

        // Act
        ResponseEntity<String> response = filmService.deleteById(filmId);

        // Assert
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
        assertEquals("Film med id 1 tagen borta", response.getBody(), "The response body should contain the success message");

        // Verify that deleteById was called once
        verify(filmDao, times(1)).deleteById(filmId);
    }

    @Test
    public void testDeleteByIdFail () throws Exception {

        Integer filmId = 2;
        when(filmDao.findById(filmId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = filmService.deleteById(filmId);

        assertEquals(404, response.getStatusCodeValue(), "Status code should be 404 Not Found");
        assertEquals("no film found with id: 2", response.getBody(), "The response body should contain the error message");

        // Verify that deleteById was never called because filmId did not match
        verify(filmDao, never()).deleteById(filmId);
    }

    @Test
    public void testDeleteByIdException () throws Exception {

        Integer filmId = 1;
        when(filmDao.findById(filmId)).thenReturn(Optional.of(mockFilm));
        doThrow(new RuntimeException("Database error")).when(filmDao).deleteById(filmId); // exception

        assertThrows(Exception.class, () -> {
            filmService.deleteById(filmId); // should throw an exception due to the error in deleteById
        });
    }

    @Test
    public void testChangeCountryOfOrigin () {
        when(filmDao.findById(1)).thenReturn(Optional.of(mockFilm));

        ResponseEntity<Response> response = filmService.changeCountryOfOrigin(1, "SE");

        assertEquals(200, response.getStatusCodeValue(), "should be 200 ");
        assertEquals("SE", filmDao.findById(1).get().getOrigin_country().get(0) );
        assertTrue(response.getBody() instanceof FilmModel, "Response body should be a FilmModel");
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testSearchFilmByNameFound () {

        when(filmDao.findAll()).thenReturn(Arrays.asList(mockFilm));

        when(filmDao.findByTitle("Test Film")).thenReturn(Optional.of(mockFilm));

        ResponseEntity<Response> response = filmService.searchFilmByName("Test Film");

        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
        assertTrue(response.getBody() instanceof FilmDTO, "Response body should be a FilmDTO");
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testSearchFilmByNameNotFound () {

        when(filmDao.findByTitle("Nonexistent Film")).thenReturn(Optional.empty());

        ResponseEntity<Response> response = filmService.searchFilmByName("Nonexistent Film");

        assertEquals(404, response.getStatusCodeValue(), "Status code should be 404 Not Found");
        assertTrue(response.getBody() instanceof ErrorResponse, "Response body should be an ErrorResponse");
    }

    @Test
    public void testGetFilmByCountry () {

        List<FilmModel> films = Arrays.asList(mockFilm);
        when(filmDao.findAll()).thenReturn(films);
        when(rateLimiter.acquirePermission()).thenReturn(true);

        ResponseEntity<Response> response = filmService.getFilmByCountry("US", "Test Film");

        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");
        assertTrue(response.getBody() instanceof FilmModel, "Response body should be a FilmModel");

    }

    @Test
    public void testGetFilmByCountryRateExceeded () {

        List<FilmModel> films = Arrays.asList(mockFilm);
        lenient().when(filmDao.findAll()).thenReturn(films);
        lenient().when(rateLimiter.acquirePermission()).thenReturn(false);

        ResponseEntity<Response> response = filmService.getFilmByCountry("US", "Test Film");

        assertEquals(429, response.getStatusCodeValue(), "Status code should be 429 Too Many Requests");
        assertTrue(response.getBody() instanceof ErrorResponse, "Response body should be a ErrorResponse ");

    }

    @Test
    public void testGetFilmByCountryNotFound () {

        List<FilmModel> films = Arrays.asList(mockFilm);
        when(filmDao.findAll()).thenReturn(films);
        when(rateLimiter.acquirePermission()).thenReturn(true);

        ResponseEntity<Response> response = filmService.getFilmByCountry("SE", "Test Film");

        assertEquals(400, response.getStatusCodeValue(), "Status code should be 400 Bad Request");
        assertTrue(response.getBody() instanceof ErrorResponse, "Response body should be an ErrorResponse");
    }

    // TODO - update to reflect new changes
    /*
    @Test
    public void testAddOpinion () {

        when(filmDao.findById(1)).thenReturn(Optional.of(mockFilm));

        ResponseEntity<String> response = filmService.addOpinion(1, "bra film!");

        assertEquals(201, response.getStatusCodeValue(), "Status code should be 201 Created");
        assertEquals("bra film!", filmDao.findById(1).get().getOpinion());
        assertEquals("Opinion adderad!", response.getBody(), "Response body should be 'Opinion adderad!'");
    }
    */



    @Test
    public void testGetAverageRuntime () {

        List<FilmModel> films = Arrays.asList(mockFilm);
        when(filmDao.findAll()).thenReturn(films);

        ResponseEntity<Response> response = filmService.getAverageRuntime();
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200 OK");

        IntegerResponse integerResponse = (IntegerResponse) response.getBody();
        assertEquals(120, integerResponse.getAverageRuntime(), "Average runtime should be 120 minutes");
    }
}

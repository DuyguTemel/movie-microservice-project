package com.dtemel.moviecatalogservice.resources;

import com.dtemel.moviecatalogservice.model.CatalogItem;
import com.dtemel.moviecatalogservice.model.Movie;
import com.dtemel.moviecatalogservice.model.Rating;
import com.dtemel.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private WebClient.Builder webClientBuilder;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);


        return ratings.getUserRating().stream().map(resource -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + resource.getMovieId(), Movie.class);

//           Movie movie=webClientBuilder.build().get().uri("http://localhost:8082/movies/" + resource.getMovieId()).retrieve().bodyToMono(Movie.class).block();


            return new CatalogItem(movie.getName(), "Test", resource.getRating());
        }).collect(Collectors.toList());

    }


}

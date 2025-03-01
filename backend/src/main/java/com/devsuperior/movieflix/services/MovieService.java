package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    @Autowired
    private GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {
        Optional<Movie> movie = repository.findById(id);

        if (movie.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found");
        }
        return new MovieDetailsDTO(movie.get());
    }

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findByGenre(Long genreId, Pageable pageable) {
        Genre genre = (genreId == 0) ? null : genreRepository.getReferenceById(genreId);
        Page<Movie> page = repository.searchMovieWithGenre(genre, pageable);
        return page.map(MovieCardDTO::new);
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> findMovieReviewsById(Long id){
        Optional<Movie> movie = repository.findById(id);
        if (movie.isEmpty()){
            throw new ResourceNotFoundException("Movie not found");
        }
        List<Review> list = movie.get().getReviews();
        return list.stream().map(ReviewDTO::new).toList();
    }

}

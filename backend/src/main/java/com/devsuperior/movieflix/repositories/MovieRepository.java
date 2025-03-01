package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieCardProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(nativeQuery = true, value =
            """
            SELECT * FROM(
            SELECT tb_movie.id, tb_movie.title, tb_movie.sub_title AS subTitle, tb_movie.movie_year AS movieYear, tb_movie.img_url AS imgUrl FROM tb_movie
            INNER JOIN tb_genre ON tb_genre.id = tb_movie.genre_id
            WHERE tb_movie.genre_id = :genreId
            ) AS tb_result
            """, countQuery =
            """
            SELECT COUNT(*) FROM(
            SELECT tb_movie.id, tb_movie.title, tb_movie.sub_title AS subTitle, tb_movie.movie_year AS movieYear, tb_movie.img_url AS imgUrl FROM tb_movie
            INNER JOIN tb_genre ON tb_genre.id = tb_movie.genre_id
            WHERE tb_movie.genre_id = :genreId
            ) AS tb_result
            """)
    Page<MovieCardProjection> searchMovies(Long genreId, Pageable pageable);

    @Query("SELECT obj FROM Movie obj JOIN FETCH obj.genre " +
            "WHERE :genre IS NULL OR obj.genre = :genre " +
            "ORDER BY obj.title")
    Page<Movie> searchMovieWithGenre(Genre genre, Pageable pageable);
}

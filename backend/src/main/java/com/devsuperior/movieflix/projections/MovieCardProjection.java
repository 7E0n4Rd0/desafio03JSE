package com.devsuperior.movieflix.projections;

public interface MovieCardProjection extends IdProjection<Long>{
    String getTitle();
    String getSubTitle();
    Integer getMovieYear();
    String getImgUrl();


}

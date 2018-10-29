package com.qa.persistence.repository;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.Collection;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.qa.persistence.domain.Movie;
import com.qa.util.JSONUtil;

// Class to add stuff to database

@Transactional(SUPPORTS)
@Default
public class MovieDBRepository implements MovieRepository {

//	public static void main(String[] st) {
//		System.out.println(u);
//	}

	@PersistenceContext(unitName = "primary")
	private EntityManager manager;

	@Inject
	private JSONUtil util;

	@Override
	public String getAllMovies() {
		Query query = manager.createQuery("Select a FROM Movie a");
		Collection<Movie> movies = (Collection<Movie>) query.getResultList();
		// Converting Object to JSON
		return util.getJSONForObject(movies);
	}

	@Override
	@Transactional(REQUIRED)
	public String addMovie(String movie) {
		Movie aMovie = util.getObjectForJSON(movie, Movie.class);
		manager.persist(aMovie);
		return "{\"message\": \"movie has been sucessfully added\"}";
	}

	@Override
	@Transactional(REQUIRED)
	public String deleteMovie(Long id) {
		Movie movieInDB = findMovie(id);
		if (movieInDB != null) {
			manager.remove(movieInDB);
		}
		return "{\"message\": \"movie sucessfully deleted\"}";
	}
	

//	public String updateMovie(Long id) {
//		Movie movieInDB = findMovie(id);
//		Movie aMovie = util.getObjectForJSON(movie, Movie.class);
//		String query = manager.merge(getMovieById(id));
//		
//		return util.getJSONForObject(movieInDB);
//		
//	}

	
	public String getMovieById(Long id) {
		Movie movieInDB = findMovie(id);
		
		return util.getJSONForObject(movieInDB);
	}

	@Transactional(REQUIRED)
	private Movie findMovie(Long id) {
		return manager.find(Movie.class, id);
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	public void setUtil(JSONUtil util) {
		this.util = util;
	}

}

package by.belstu.it.lyskov.service;

import by.belstu.it.lyskov.entity.Trip;
import by.belstu.it.lyskov.exception.TripNotFoundException;
import by.belstu.it.lyskov.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripService {

    Page<Trip> findAllTrips(Pageable pageable);

    Trip findTrip(Long id) throws TripNotFoundException;

    Page<Trip> findTripsByQuery(String query, Pageable pageable);

    void addTrip(Trip trip);

    void updateTrip(Long id, Trip newTrip) throws TripNotFoundException;

    void updateTripMembers(Long id, Trip newTrip) throws TripNotFoundException, UserNotFoundException;

    void deleteTrip(Long id);
}

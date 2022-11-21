package by.belstu.it.lyskov.service.impl;

import by.belstu.it.lyskov.entity.Trip;
import by.belstu.it.lyskov.entity.User;
import by.belstu.it.lyskov.exception.TripNotFoundException;
import by.belstu.it.lyskov.exception.UserNotFoundException;
import by.belstu.it.lyskov.repository.TripRepository;
import by.belstu.it.lyskov.repository.UserRepository;
import by.belstu.it.lyskov.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripServiceImpl(TripRepository tripRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Trip> findAllTrips(Pageable pageable) {
        return tripRepository.findAll(pageable);
    }

    @Override
    public Trip findTrip(Long id) throws TripNotFoundException {
        Optional<Trip> trip = tripRepository.findById(id);
        if (trip.isEmpty())
            throw new TripNotFoundException("Trip with id \"" + id + "\" doesn't exist!");
        return trip.get();
    }

    @Override
    public Page<Trip> findTripsByQuery(String query, Pageable pageable) {
        return tripRepository.findByTitleContainingOrDescriptionContaining(query, query, pageable);
    }

    @Override
    public void addTrip(Trip trip) {
        tripRepository.save(trip);
    }

    @Override
    public void updateTrip(Long id, Trip newTrip) throws TripNotFoundException {
        Optional<Trip> trip = tripRepository.findById(id);
        if (trip.isEmpty())
            throw new TripNotFoundException("Trip with id \"" + id + "\" doesn't exist!");
        trip.get().setTitle(newTrip.getTitle());
        trip.get().setDescription(newTrip.getDescription());
        trip.get().setStartDate(newTrip.getStartDate());
        trip.get().setFinishDate(newTrip.getFinishDate());
        tripRepository.save(trip.get());
    }

    @Override
    public void updateTripMembers(Long id, Trip newTrip) throws TripNotFoundException, UserNotFoundException {
        Optional<Trip> trip = tripRepository.findById(id);
        if (trip.isEmpty())
            throw new TripNotFoundException("Trip with id \"" + id + "\" doesn't exist!");
        Optional<User> originator = userRepository.findById(newTrip.getOriginator().getId());
        if (originator.isEmpty())
            throw new UserNotFoundException("User with id \"" + id + "\" doesn't exist!");
        Optional<User> companion = userRepository.findById(newTrip.getCompanion().getId());
        if (companion.isEmpty())
            throw new UserNotFoundException("User with id \"" + id + "\" doesn't exist!");
        trip.get().setOriginator(originator.get());
        trip.get().setCompanion(companion.get());
        tripRepository.save(trip.get());
    }

    @Override
    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}

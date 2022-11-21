package by.belstu.it.lyskov.repository;

import by.belstu.it.lyskov.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends PagingAndSortingRepository<Trip, Long> {

    Page<Trip> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);
}

package by.belstu.it.lyskov.repository;

import by.belstu.it.lyskov.entity.Application;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends PagingAndSortingRepository<Application, Long> {

}

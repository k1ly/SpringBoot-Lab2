package by.belstu.it.lyskov.service;

import by.belstu.it.lyskov.entity.Application;
import by.belstu.it.lyskov.exception.ApplicationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {

    Page<Application> findAllApplications(Pageable pageable);

    Application findApplication(Long id) throws ApplicationNotFoundException;

    void addApplication(Application application);

    void updateApplication(Long id, Application newApplication) throws ApplicationNotFoundException;

    void deleteApplication(Long id);
}

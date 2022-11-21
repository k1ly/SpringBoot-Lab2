package by.belstu.it.lyskov.service.impl;

import by.belstu.it.lyskov.entity.Application;
import by.belstu.it.lyskov.exception.ApplicationNotFoundException;
import by.belstu.it.lyskov.repository.ApplicationRepository;
import by.belstu.it.lyskov.service.ApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Page<Application> findAllApplications(Pageable pageable) {
        return applicationRepository.findAll(pageable);
    }

    @Override
    public Application findApplication(Long id) throws ApplicationNotFoundException {
        Optional<Application> application = applicationRepository.findById(id);
        if (application.isEmpty())
            throw new ApplicationNotFoundException("Application with id \"" + id + "\" doesn't exist!");
        return application.get();
    }

    @Override
    public void addApplication(Application application) {
        applicationRepository.save(application);
    }

    @Override
    public void updateApplication(Long id, Application newApplication) throws ApplicationNotFoundException {
        Optional<Application> application = applicationRepository.findById(id);
        if (application.isEmpty())
            throw new ApplicationNotFoundException("Application with id \"" + id + "\" doesn't exist!");
        application.get().setActive(newApplication.getActive());
        applicationRepository.save(application.get());
    }

    @Override
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}

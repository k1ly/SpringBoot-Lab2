package by.belstu.it.lyskov.controller.rest;

import by.belstu.it.lyskov.controller.mapper.DtoMapper;
import by.belstu.it.lyskov.dto.*;
import by.belstu.it.lyskov.entity.Application;
import by.belstu.it.lyskov.exception.ApplicationNotFoundException;
import by.belstu.it.lyskov.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "500", description = "Internal Server error")
})
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final DtoMapper dtoMapper;
    private final ApplicationService applicationService;

    public ApplicationController(DtoMapper dtoMapper, ApplicationService applicationService) {
        this.dtoMapper = dtoMapper;
        this.applicationService = applicationService;
    }

    @PostConstruct
    private void configure() {
        dtoMapper.addTypeMapping(Application.class, ApplicationDto.class, Application::getTrip, ApplicationDto::setTrip);
        dtoMapper.addTypeMapping(Application.class, ApplicationDto.class, Application::getOriginator, ApplicationDto::setOriginator);
        dtoMapper.addTypeMapping(Application.class, ApplicationDto.class, Application::getCompanion, ApplicationDto::setCompanion);
        dtoMapper.addTypeMapping(ApplicationDto.class, Application.class, ApplicationDto::getTrip, Application::setTrip);
        dtoMapper.addTypeMapping(ApplicationDto.class, Application.class, ApplicationDto::getOriginator, Application::setOriginator);
        dtoMapper.addTypeMapping(ApplicationDto.class, Application.class, ApplicationDto::getCompanion, Application::setCompanion);
    }

    @Operation(summary = "Get list of applications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping({"", "/", "/all"})
    public Page<ApplicationDto> applicationList(@PageableDefault Pageable pageable) {
        Page<Application> applications = applicationService.findAllApplications(pageable);
        return new PageImpl<>(dtoMapper.mapAll(applications.getContent(), ApplicationDto.class),
                applications.getPageable(), applications.getTotalElements());
    }

    @Operation(summary = "Get an application by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{id}")
    public ApplicationDto findApplication(@PathVariable Long id) throws ApplicationNotFoundException {
        return dtoMapper.map(applicationService.findApplication(id), ApplicationDto.class);
    }

    @Operation(summary = "Add a new application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/add")
    public void addApplication(@Valid @RequestBody NewApplicationDto applicationDto) {
        applicationService.addApplication(dtoMapper.map(applicationDto, Application.class));
    }

    @Operation(summary = "Update an existing application by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PutMapping("/update/{id}")
    public void updateApplication(@PathVariable Long id, @Valid @RequestBody UpdateApplicationDto applicationDto)
            throws ApplicationNotFoundException {
        applicationService.updateApplication(id, dtoMapper.map(applicationDto, Application.class));
    }

    @Operation(summary = "Delete an existing application by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/delete/{id}")
    public void deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
    }
}
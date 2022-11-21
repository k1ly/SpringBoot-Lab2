package by.belstu.it.lyskov.controller.rest;

import by.belstu.it.lyskov.dto.NewTripDto;
import by.belstu.it.lyskov.dto.TripDto;
import by.belstu.it.lyskov.controller.mapper.DtoMapper;
import by.belstu.it.lyskov.dto.UpdateTripDto;
import by.belstu.it.lyskov.entity.Trip;
import by.belstu.it.lyskov.exception.TripNotFoundException;
import by.belstu.it.lyskov.exception.UserNotFoundException;
import by.belstu.it.lyskov.service.TripService;
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
@RequestMapping("/api/trips")
public class TripController {

    private final DtoMapper dtoMapper;
    private final TripService tripService;

    public TripController(DtoMapper dtoMapper, TripService tripService) {
        this.dtoMapper = dtoMapper;
        this.tripService = tripService;
    }

    @PostConstruct
    private void configure() {
        dtoMapper.addTypeMapping(Trip.class, TripDto.class, Trip::getOriginator, TripDto::setOriginator);
        dtoMapper.addTypeMapping(Trip.class, TripDto.class, Trip::getCompanion, TripDto::setCompanion);
    }

    @Operation(summary = "Get list of trips")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping({"", "/", "/all"})
    public Page<TripDto> tripList(@PageableDefault Pageable pageable, @RequestParam(name = "q", required = false) String query) {
        Page<Trip> trips = query != null ? tripService.findTripsByQuery(query, pageable) : tripService.findAllTrips(pageable);
        return new PageImpl<>(dtoMapper.mapAll(trips.getContent(), TripDto.class),
                trips.getPageable(), trips.getTotalElements());
    }

    @Operation(summary = "Get a trip by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{id}")
    public TripDto findTrip(@PathVariable Long id) throws TripNotFoundException {
        return dtoMapper.map(tripService.findTrip(id), TripDto.class);
    }

    @Operation(summary = "Add a new trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/add")
    public void addTrip(@Valid @RequestBody NewTripDto tripDto) {
        tripService.addTrip(dtoMapper.map(tripDto, Trip.class));
    }

    @Operation(summary = "Update an existing trip by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PutMapping("/update/{id}")
    public void updateTrip(@PathVariable Long id, @Valid @RequestBody UpdateTripDto tripDto) throws TripNotFoundException {
        tripService.updateTrip(id, dtoMapper.map(tripDto, Trip.class));
    }

    @Operation(summary = "Update an existing trip members by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PutMapping("/update-members/{id}")
    public void updateTripMembers(@PathVariable Long id, @Valid @RequestBody UpdateTripDto tripDto)
            throws TripNotFoundException, UserNotFoundException {
        tripService.updateTripMembers(id, dtoMapper.map(tripDto, Trip.class));
    }

    @Operation(summary = "Delete an existing trip by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/delete/{id}")
    public void deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
    }
}
package by.belstu.it.lyskov.controller.rest;

import by.belstu.it.lyskov.dto.NewUserDto;
import by.belstu.it.lyskov.dto.UserDto;
import by.belstu.it.lyskov.controller.mapper.DtoMapper;
import by.belstu.it.lyskov.entity.User;
import by.belstu.it.lyskov.exception.RoleNotFoundException;
import by.belstu.it.lyskov.exception.UserAlreadyExistsException;
import by.belstu.it.lyskov.exception.UserNotFoundException;
import by.belstu.it.lyskov.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@OpenAPIDefinition(info = @Info(title = "Test API", version = "1.0", description = "Lab2 Test REST API"))
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "500", description = "Internal Server error")
})
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final DtoMapper dtoMapper;
    private final UserService userService;

    public UserController(DtoMapper dtoMapper, UserService userService) {
        this.dtoMapper = dtoMapper;
        this.userService = userService;
    }

    @Operation(summary = "Get list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping({"", "/", "/all"})
    public Page<UserDto> userList(@PageableDefault Pageable pageable) {
        Page<User> users = userService.findAllUsers(pageable);
        return new PageImpl<>(dtoMapper.mapAll(users.getContent(), UserDto.class),
                users.getPageable(), users.getTotalElements());
    }

    @Operation(summary = "Get a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable Long id) throws UserNotFoundException {
        return dtoMapper.map(userService.findUser(id), UserDto.class);
    }

    @Operation(summary = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/add")
    public void registerUser(@Valid @RequestBody NewUserDto userDto) throws RoleNotFoundException, UserAlreadyExistsException {
        userService.addUser(dtoMapper.map(userDto, User.class));
    }

    @Operation(summary = "Update an existing user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PutMapping("/update/{id}")
    public void updateUser(@PathVariable Long id, @Valid @RequestBody NewUserDto userDto) throws UserNotFoundException {
        userService.updateUser(id, dtoMapper.map(userDto, User.class));
    }

    @Operation(summary = "Delete an existing user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

package org.example;

import org.example.controllers.RoomController;
import org.example.dtos.*;
import org.example.models.Room;
import org.example.models.RoomType;
import org.example.models.User;
import org.example.models.UserType;
import org.example.repositories.RoomRepository;
import org.example.repositories.UserRepository;
import org.example.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomControllerTests {
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private RoomService roomService;
    private RoomController roomController;

    @BeforeEach
    public void setupTest() throws Exception {
        initializeComponents();
    }

    private void initializeComponents() throws Exception {
        initializeRepositories();
        initializeRoomService();
        initializeRoomController();
    }

    private <T> T createInstance(Class<T> interfaceClass, Reflections reflections) throws Exception {
        Set<Class<? extends T>> implementations = reflections.getSubTypesOf(interfaceClass);
        if (implementations.isEmpty()) {
            throw new Exception("No implementation for " + interfaceClass.getSimpleName() + " found");
        }

        Class<? extends T> implementationClass = implementations.iterator().next();
        Constructor<? extends T> constructor = implementationClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private <T> T createInstanceWithArgs(Class<T> interfaceClass, Reflections reflections, List<Object> dependencies) throws Exception {
        Set<Class<? extends T>> implementations = reflections.getSubTypesOf(interfaceClass);
        if (implementations.isEmpty()) {
            throw new Exception("No implementation for " + interfaceClass.getSimpleName() + " found");
        }
        Class<? extends T> implementationClass = implementations.iterator().next();
        Constructor<?>[] constructors = implementationClass.getConstructors();
        Constructor<?> constructor = Arrays.stream(constructors)
                .filter(constructor1 -> constructor1.getParameterCount() == dependencies.size())
                .findFirst().orElseThrow(() -> new Exception("No constructor with " + dependencies.size() + " arguments found"));
        constructor.setAccessible(true);
        Object[] args = new Object[constructor.getParameterCount()];
        for (int i = 0; i < constructor.getParameterCount(); i++) {
            for (Object dependency : dependencies) {
                if (constructor.getParameterTypes()[i].isInstance(dependency)) {
                    args[i] = dependency;
                    break;
                }
            }
        }
        return (T) constructor.newInstance(args);
    }

    private void initializeRepositories() throws Exception {
        Reflections repositoryReflections = new Reflections(UserRepository.class.getPackageName(), new SubTypesScanner(false));
        this.roomRepository = createInstance(RoomRepository.class, repositoryReflections);
        this.userRepository = createInstance(UserRepository.class, repositoryReflections);
    }

    private void initializeRoomService() throws Exception {
        Reflections serviceReflections = new Reflections(RoomService.class.getPackageName(), new SubTypesScanner(false));
        this.roomService = createInstanceWithArgs(RoomService.class, serviceReflections, Arrays.asList(this.roomRepository, this.userRepository));
    }

    private void initializeRoomController() {
        this.roomController = new RoomController(this.roomService);
    }

    @Test
    public void testAddRoomSuccess() {
        User adminUser = new User();
        adminUser.setUserType(UserType.ADMIN);
        adminUser.setName("admin");
        adminUser.setPassword("admin");
        adminUser.setPhone("1234567890");
        adminUser = userRepository.save(adminUser);

        AddRoomRequestDTO requestDto = new AddRoomRequestDTO();
        requestDto.setUserId(adminUser.getId());
        requestDto.setName("A1");
        requestDto.setDescription("Room number A1");
        requestDto.setPrice(5000.0);
        requestDto.setRoomType("DELUXE");
        AddRoomResponseDto responseDto = roomController.addRoom(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "AddRoomResponseDto status should be SUCCESS");
        assertNotNull(responseDto.getRoom(), "AddRoomResponseDto room should not be NULL");
    }

    @Test
    public void testAddRoomByCustomer() {
        User customerUser = new User();
        customerUser.setUserType(UserType.CUSTOMER);
        customerUser.setName("customer");
        customerUser.setPassword("customer");
        customerUser.setPhone("1234567890");
        customerUser = userRepository.save(customerUser);

        AddRoomRequestDTO requestDto = new AddRoomRequestDTO();
        requestDto.setUserId(customerUser.getId());
        requestDto.setName("A2");
        requestDto.setDescription("Room number A2");
        requestDto.setPrice(7000.0);
        requestDto.setRoomType("SUPER_DELUXE");
        AddRoomResponseDto responseDto = roomController.addRoom(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.FAILURE, "AddRoomResponseDto status should be FAILURE as User is not an Admin");
        assertNull(responseDto.getRoom(), "AddRoomResponseDto room should be NULL");
    }

    @Test
    public void testAddRoomByNonExistingUser() {
        User adminUser = new User();
        adminUser.setUserType(UserType.ADMIN);
        adminUser.setName("admin");
        adminUser.setPassword("admin");
        adminUser.setPhone("1234567890");
        adminUser = userRepository.save(adminUser);

        AddRoomRequestDTO requestDto = new AddRoomRequestDTO();
        requestDto.setUserId(adminUser.getId() + 10);
        requestDto.setName("A1");
        requestDto.setDescription("Room number A1");
        requestDto.setPrice(5000.0);
        requestDto.setRoomType("DELUXE");
        AddRoomResponseDto responseDto = roomController.addRoom(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.FAILURE, "AddRoomResponseDto status should be FAILURE as User is not an Admin");
        assertNull(responseDto.getRoom(), "AddRoomResponseDto room should be NULL");
    }

    @Test
    public void testGetRoomsWithoutFilter() {
        addFewRooms();
        GetRoomsRequestDTO requestDto = new GetRoomsRequestDTO();
        requestDto.setRoomType(null);
        GetRoomResponseDTO responseDto = roomController.getRooms(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "GetRoomsResponseDto status should be SUCCESS");
        assertEquals(responseDto.getRooms().size(), 6, "Number of rooms should be 6");
    }

    @Test
    public void testGetRoomsWithDeluxeFilter() {
        addFewRooms();
        GetRoomsRequestDTO requestDto = new GetRoomsRequestDTO();
        requestDto.setRoomType("DELUXE");
        GetRoomResponseDTO responseDto = roomController.getRooms(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "GetRoomsResponseDto status should be SUCCESS");
        assertEquals(responseDto.getRooms().size(), 2, "Number of DELUXE rooms should be 2");
    }

    @Test
    public void testGetRoomsWithSuperDeluxeFilter() {
        addFewRooms();
        GetRoomsRequestDTO requestDto = new GetRoomsRequestDTO();
        requestDto.setRoomType("SUPER_DELUXE");
        GetRoomResponseDTO responseDto = roomController.getRooms(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "GetRoomsResponseDto status should be SUCCESS");
        assertEquals(responseDto.getRooms().size(), 2, "Number of SUPER DELUXE rooms should be 2");
    }

    @Test
    public void testGetRoomsWithSuiteFilter() {
        addFewRooms();
        GetRoomsRequestDTO requestDto = new GetRoomsRequestDTO();
        requestDto.setRoomType("SUITE");
        GetRoomResponseDTO responseDto = roomController.getRooms(requestDto);
        assertEquals(responseDto.getResponseStatus(), ResponseStatus.SUCCESS, "GetRoomsResponseDto status should be SUCCESS");
        assertEquals(responseDto.getRooms().size(), 2, "Number of SUITE rooms should be 2");
    }

    private void addFewRooms() {
        Room room = new Room();
        room.setName("A1");
        room.setDescriptions("Room number A1");
        room.setRoomType(RoomType.DELUXE);
        room.setPrice(5000.0);
        roomRepository.save(room);

        room = new Room();
        room.setName("A2");
        room.setDescriptions("Room number A2");
        room.setRoomType(RoomType.SUPER_DELUXE);
        room.setPrice(7000.0);
        roomRepository.save(room);

        room = new Room();
        room.setName("A3");
        room.setDescriptions("Room number A3");
        room.setRoomType(RoomType.SUITE);
        room.setPrice(10000.0);
        roomRepository.save(room);

        room = new Room();
        room.setName("B1");
        room.setDescriptions("Room number B1");
        room.setRoomType(RoomType.DELUXE);
        room.setPrice(4000.0);
        roomRepository.save(room);

        room = new Room();
        room.setName("B2");
        room.setDescriptions("Room number B2");
        room.setRoomType(RoomType.SUPER_DELUXE);
        room.setPrice(6000.0);
        roomRepository.save(room);

        room = new Room();
        room.setName("B3");
        room.setDescriptions("Room number B3");
        room.setRoomType(RoomType.SUITE);
        room.setPrice(9000.0);
        roomRepository.save(room);
    }
}

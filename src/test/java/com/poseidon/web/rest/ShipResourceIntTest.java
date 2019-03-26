package com.poseidon.web.rest;

import com.poseidon.ApigatewayApp;

import com.poseidon.domain.Ship;
import com.poseidon.domain.User;
import com.poseidon.domain.ProductOrder;
import com.poseidon.domain.Company;
import com.poseidon.repository.ShipRepository;
import com.poseidon.service.ShipService;
import com.poseidon.web.rest.errors.ExceptionTranslator;
import com.poseidon.service.dto.ShipCriteria;
import com.poseidon.service.ShipQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;


import static com.poseidon.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.poseidon.domain.enumeration.Gender;
/**
 * Test class for the ShipResource REST controller.
 *
 * @see ShipResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApigatewayApp.class)
public class ShipResourceIntTest {

    private static final BigDecimal DEFAULT_SHIP_ID = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHIP_ID = new BigDecimal(2);

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_EMAIL = "NK@w./";
    private static final String UPDATED_EMAIL = "=@;.!D";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ShipService shipService;

    @Autowired
    private ShipQueryService shipQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restShipMockMvc;

    private Ship ship;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShipResource shipResource = new ShipResource(shipService, shipQueryService);
        this.restShipMockMvc = MockMvcBuilders.standaloneSetup(shipResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ship createEntity(EntityManager em) {
        Ship ship = new Ship()
            .shipId(DEFAULT_SHIP_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .gender(DEFAULT_GENDER)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        ship.setUser(user);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        ship.setCompany(company);
        return ship;
    }

    @Before
    public void initTest() {
        ship = createEntity(em);
    }

    @Test
    @Transactional
    public void createShip() throws Exception {
        int databaseSizeBeforeCreate = shipRepository.findAll().size();

        // Create the Ship
        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isCreated());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeCreate + 1);
        Ship testShip = shipList.get(shipList.size() - 1);
        assertThat(testShip.getShipId()).isEqualTo(DEFAULT_SHIP_ID);
        assertThat(testShip.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testShip.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testShip.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testShip.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testShip.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testShip.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testShip.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testShip.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testShip.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    public void createShipWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shipRepository.findAll().size();

        // Create the Ship with an existing ID
        ship.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isBadRequest());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkShipIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipRepository.findAll().size();
        // set the field null
        ship.setShipId(null);

        // Create the Ship, which fails.

        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isBadRequest());

        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipRepository.findAll().size();
        // set the field null
        ship.setEmail(null);

        // Create the Ship, which fails.

        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isBadRequest());

        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipRepository.findAll().size();
        // set the field null
        ship.setPhone(null);

        // Create the Ship, which fails.

        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isBadRequest());

        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShips() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList
        restShipMockMvc.perform(get("/api/ships?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ship.getId().intValue())))
            .andExpect(jsonPath("$.[*].shipId").value(hasItem(DEFAULT_SHIP_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1.toString())))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }
    
    @Test
    @Transactional
    public void getShip() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get the ship
        restShipMockMvc.perform(get("/api/ships/{id}", ship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ship.getId().intValue()))
            .andExpect(jsonPath("$.shipId").value(DEFAULT_SHIP_ID.intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1.toString()))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()));
    }

    @Test
    @Transactional
    public void getAllShipsByShipIdIsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where shipId equals to DEFAULT_SHIP_ID
        defaultShipShouldBeFound("shipId.equals=" + DEFAULT_SHIP_ID);

        // Get all the shipList where shipId equals to UPDATED_SHIP_ID
        defaultShipShouldNotBeFound("shipId.equals=" + UPDATED_SHIP_ID);
    }

    @Test
    @Transactional
    public void getAllShipsByShipIdIsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where shipId in DEFAULT_SHIP_ID or UPDATED_SHIP_ID
        defaultShipShouldBeFound("shipId.in=" + DEFAULT_SHIP_ID + "," + UPDATED_SHIP_ID);

        // Get all the shipList where shipId equals to UPDATED_SHIP_ID
        defaultShipShouldNotBeFound("shipId.in=" + UPDATED_SHIP_ID);
    }

    @Test
    @Transactional
    public void getAllShipsByShipIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where shipId is not null
        defaultShipShouldBeFound("shipId.specified=true");

        // Get all the shipList where shipId is null
        defaultShipShouldNotBeFound("shipId.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where firstName equals to DEFAULT_FIRST_NAME
        defaultShipShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the shipList where firstName equals to UPDATED_FIRST_NAME
        defaultShipShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllShipsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultShipShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the shipList where firstName equals to UPDATED_FIRST_NAME
        defaultShipShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllShipsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where firstName is not null
        defaultShipShouldBeFound("firstName.specified=true");

        // Get all the shipList where firstName is null
        defaultShipShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where lastName equals to DEFAULT_LAST_NAME
        defaultShipShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the shipList where lastName equals to UPDATED_LAST_NAME
        defaultShipShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllShipsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultShipShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the shipList where lastName equals to UPDATED_LAST_NAME
        defaultShipShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllShipsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where lastName is not null
        defaultShipShouldBeFound("lastName.specified=true");

        // Get all the shipList where lastName is null
        defaultShipShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where gender equals to DEFAULT_GENDER
        defaultShipShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the shipList where gender equals to UPDATED_GENDER
        defaultShipShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllShipsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultShipShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the shipList where gender equals to UPDATED_GENDER
        defaultShipShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllShipsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where gender is not null
        defaultShipShouldBeFound("gender.specified=true");

        // Get all the shipList where gender is null
        defaultShipShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where email equals to DEFAULT_EMAIL
        defaultShipShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the shipList where email equals to UPDATED_EMAIL
        defaultShipShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllShipsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultShipShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the shipList where email equals to UPDATED_EMAIL
        defaultShipShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllShipsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where email is not null
        defaultShipShouldBeFound("email.specified=true");

        // Get all the shipList where email is null
        defaultShipShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where phone equals to DEFAULT_PHONE
        defaultShipShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the shipList where phone equals to UPDATED_PHONE
        defaultShipShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllShipsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultShipShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the shipList where phone equals to UPDATED_PHONE
        defaultShipShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllShipsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where phone is not null
        defaultShipShouldBeFound("phone.specified=true");

        // Get all the shipList where phone is null
        defaultShipShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultShipShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the shipList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultShipShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllShipsByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultShipShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the shipList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultShipShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllShipsByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where addressLine1 is not null
        defaultShipShouldBeFound("addressLine1.specified=true");

        // Get all the shipList where addressLine1 is null
        defaultShipShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultShipShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the shipList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultShipShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllShipsByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultShipShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the shipList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultShipShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllShipsByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where addressLine2 is not null
        defaultShipShouldBeFound("addressLine2.specified=true");

        // Get all the shipList where addressLine2 is null
        defaultShipShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where city equals to DEFAULT_CITY
        defaultShipShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the shipList where city equals to UPDATED_CITY
        defaultShipShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllShipsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where city in DEFAULT_CITY or UPDATED_CITY
        defaultShipShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the shipList where city equals to UPDATED_CITY
        defaultShipShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllShipsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where city is not null
        defaultShipShouldBeFound("city.specified=true");

        // Get all the shipList where city is null
        defaultShipShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where country equals to DEFAULT_COUNTRY
        defaultShipShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the shipList where country equals to UPDATED_COUNTRY
        defaultShipShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllShipsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultShipShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the shipList where country equals to UPDATED_COUNTRY
        defaultShipShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllShipsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList where country is not null
        defaultShipShouldBeFound("country.specified=true");

        // Get all the shipList where country is null
        defaultShipShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllShipsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        ship.setUser(user);
        shipRepository.saveAndFlush(ship);
        Long userId = user.getId();

        // Get all the shipList where user equals to userId
        defaultShipShouldBeFound("userId.equals=" + userId);

        // Get all the shipList where user equals to userId + 1
        defaultShipShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllShipsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductOrder order = ProductOrderResourceIntTest.createEntity(em);
        em.persist(order);
        em.flush();
        ship.addOrder(order);
        shipRepository.saveAndFlush(ship);
        Long orderId = order.getId();

        // Get all the shipList where order equals to orderId
        defaultShipShouldBeFound("orderId.equals=" + orderId);

        // Get all the shipList where order equals to orderId + 1
        defaultShipShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }


    @Test
    @Transactional
    public void getAllShipsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        ship.setCompany(company);
        shipRepository.saveAndFlush(ship);
        Long companyId = company.getId();

        // Get all the shipList where company equals to companyId
        defaultShipShouldBeFound("companyId.equals=" + companyId);

        // Get all the shipList where company equals to companyId + 1
        defaultShipShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultShipShouldBeFound(String filter) throws Exception {
        restShipMockMvc.perform(get("/api/ships?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ship.getId().intValue())))
            .andExpect(jsonPath("$.[*].shipId").value(hasItem(DEFAULT_SHIP_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));

        // Check, that the count call also returns 1
        restShipMockMvc.perform(get("/api/ships/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultShipShouldNotBeFound(String filter) throws Exception {
        restShipMockMvc.perform(get("/api/ships?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipMockMvc.perform(get("/api/ships/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingShip() throws Exception {
        // Get the ship
        restShipMockMvc.perform(get("/api/ships/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShip() throws Exception {
        // Initialize the database
        shipService.save(ship);

        int databaseSizeBeforeUpdate = shipRepository.findAll().size();

        // Update the ship
        Ship updatedShip = shipRepository.findById(ship.getId()).get();
        // Disconnect from session so that the updates on updatedShip are not directly saved in db
        em.detach(updatedShip);
        updatedShip
            .shipId(UPDATED_SHIP_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restShipMockMvc.perform(put("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShip)))
            .andExpect(status().isOk());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeUpdate);
        Ship testShip = shipList.get(shipList.size() - 1);
        assertThat(testShip.getShipId()).isEqualTo(UPDATED_SHIP_ID);
        assertThat(testShip.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testShip.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testShip.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testShip.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testShip.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testShip.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testShip.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testShip.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testShip.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void updateNonExistingShip() throws Exception {
        int databaseSizeBeforeUpdate = shipRepository.findAll().size();

        // Create the Ship

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipMockMvc.perform(put("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isBadRequest());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShip() throws Exception {
        // Initialize the database
        shipService.save(ship);

        int databaseSizeBeforeDelete = shipRepository.findAll().size();

        // Delete the ship
        restShipMockMvc.perform(delete("/api/ships/{id}", ship.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ship.class);
        Ship ship1 = new Ship();
        ship1.setId(1L);
        Ship ship2 = new Ship();
        ship2.setId(ship1.getId());
        assertThat(ship1).isEqualTo(ship2);
        ship2.setId(2L);
        assertThat(ship1).isNotEqualTo(ship2);
        ship1.setId(null);
        assertThat(ship1).isNotEqualTo(ship2);
    }
}

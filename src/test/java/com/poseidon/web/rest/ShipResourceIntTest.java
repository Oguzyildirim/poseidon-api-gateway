package com.poseidon.web.rest;

import com.poseidon.PoseidonApiGatewayApp;

import com.poseidon.domain.Ship;
import com.poseidon.domain.User;
import com.poseidon.domain.Company;
import com.poseidon.repository.ShipRepository;
import com.poseidon.repository.search.ShipSearchRepository;
import com.poseidon.service.ShipService;
import com.poseidon.service.dto.ShipDTO;
import com.poseidon.service.mapper.ShipMapper;
import com.poseidon.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.Collections;
import java.util.List;


import static com.poseidon.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.poseidon.domain.enumeration.Gender;
/**
 * Test class for the ShipResource REST controller.
 *
 * @see ShipResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PoseidonApiGatewayApp.class)
public class ShipResourceIntTest {

    private static final BigDecimal DEFAULT_SHIP_ID = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHIP_ID = new BigDecimal(2);

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_EMAIL = "U@uB.t";
    private static final String UPDATED_EMAIL = "}@p.<";

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
    private ShipMapper shipMapper;

    @Autowired
    private ShipService shipService;

    /**
     * This repository is mocked in the com.poseidon.repository.search test package.
     *
     * @see com.poseidon.repository.search.ShipSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShipSearchRepository mockShipSearchRepository;

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
        final ShipResource shipResource = new ShipResource(shipService);
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
        ShipDTO shipDTO = shipMapper.toDto(ship);
        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipDTO)))
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

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(1)).save(testShip);
    }

    @Test
    @Transactional
    public void createShipWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shipRepository.findAll().size();

        // Create the Ship with an existing ID
        ship.setId(1L);
        ShipDTO shipDTO = shipMapper.toDto(ship);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(0)).save(ship);
    }

    @Test
    @Transactional
    public void checkShipIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipRepository.findAll().size();
        // set the field null
        ship.setShipId(null);

        // Create the Ship, which fails.
        ShipDTO shipDTO = shipMapper.toDto(ship);

        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipDTO)))
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
        ShipDTO shipDTO = shipMapper.toDto(ship);

        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipDTO)))
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
        ShipDTO shipDTO = shipMapper.toDto(ship);

        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipDTO)))
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
    public void getNonExistingShip() throws Exception {
        // Get the ship
        restShipMockMvc.perform(get("/api/ships/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShip() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

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
        ShipDTO shipDTO = shipMapper.toDto(updatedShip);

        restShipMockMvc.perform(put("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipDTO)))
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

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(1)).save(testShip);
    }

    @Test
    @Transactional
    public void updateNonExistingShip() throws Exception {
        int databaseSizeBeforeUpdate = shipRepository.findAll().size();

        // Create the Ship
        ShipDTO shipDTO = shipMapper.toDto(ship);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipMockMvc.perform(put("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(0)).save(ship);
    }

    @Test
    @Transactional
    public void deleteShip() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        int databaseSizeBeforeDelete = shipRepository.findAll().size();

        // Delete the ship
        restShipMockMvc.perform(delete("/api/ships/{id}", ship.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(1)).deleteById(ship.getId());
    }

    @Test
    @Transactional
    public void searchShip() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);
        when(mockShipSearchRepository.search(queryStringQuery("id:" + ship.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ship), PageRequest.of(0, 1), 1));
        // Search the ship
        restShipMockMvc.perform(get("/api/_search/ships?query=id:" + ship.getId()))
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

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipDTO.class);
        ShipDTO shipDTO1 = new ShipDTO();
        shipDTO1.setId(1L);
        ShipDTO shipDTO2 = new ShipDTO();
        assertThat(shipDTO1).isNotEqualTo(shipDTO2);
        shipDTO2.setId(shipDTO1.getId());
        assertThat(shipDTO1).isEqualTo(shipDTO2);
        shipDTO2.setId(2L);
        assertThat(shipDTO1).isNotEqualTo(shipDTO2);
        shipDTO1.setId(null);
        assertThat(shipDTO1).isNotEqualTo(shipDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(shipMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(shipMapper.fromId(null)).isNull();
    }
}

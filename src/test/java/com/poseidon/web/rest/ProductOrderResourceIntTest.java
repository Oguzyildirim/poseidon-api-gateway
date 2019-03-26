package com.poseidon.web.rest;

import com.poseidon.ApigatewayApp;

import com.poseidon.domain.ProductOrder;
import com.poseidon.domain.OrderItem;
import com.poseidon.domain.Ship;
import com.poseidon.repository.ProductOrderRepository;
import com.poseidon.service.ProductOrderService;
import com.poseidon.web.rest.errors.ExceptionTranslator;
import com.poseidon.service.dto.ProductOrderCriteria;
import com.poseidon.service.ProductOrderQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.poseidon.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.poseidon.domain.enumeration.OrderStatus;
/**
 * Test class for the ProductOrderResource REST controller.
 *
 * @see ProductOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApigatewayApp.class)
public class ProductOrderResourceIntTest {

    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.COMPLETED;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PENDING;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private ProductOrderQueryService productOrderQueryService;

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

    private MockMvc restProductOrderMockMvc;

    private ProductOrder productOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductOrderResource productOrderResource = new ProductOrderResource(productOrderService, productOrderQueryService);
        this.restProductOrderMockMvc = MockMvcBuilders.standaloneSetup(productOrderResource)
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
    public static ProductOrder createEntity(EntityManager em) {
        ProductOrder productOrder = new ProductOrder()
            .placedDate(DEFAULT_PLACED_DATE)
            .status(DEFAULT_STATUS)
            .code(DEFAULT_CODE);
        // Add required entity
        Ship ship = ShipResourceIntTest.createEntity(em);
        em.persist(ship);
        em.flush();
        productOrder.setShip(ship);
        return productOrder;
    }

    @Before
    public void initTest() {
        productOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductOrder() throws Exception {
        int databaseSizeBeforeCreate = productOrderRepository.findAll().size();

        // Create the ProductOrder
        restProductOrderMockMvc.perform(post("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isCreated());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(DEFAULT_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createProductOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productOrderRepository.findAll().size();

        // Create the ProductOrder with an existing ID
        productOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOrderMockMvc.perform(post("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPlacedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setPlacedDate(null);

        // Create the ProductOrder, which fails.

        restProductOrderMockMvc.perform(post("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setStatus(null);

        // Create the ProductOrder, which fails.

        restProductOrderMockMvc.perform(post("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setCode(null);

        // Create the ProductOrder, which fails.

        restProductOrderMockMvc.perform(post("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductOrders() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList
        restProductOrderMockMvc.perform(get("/api/product-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }
    
    @Test
    @Transactional
    public void getProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get the productOrder
        restProductOrderMockMvc.perform(get("/api/product-orders/{id}", productOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productOrder.getId().intValue()))
            .andExpect(jsonPath("$.placedDate").value(DEFAULT_PLACED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getAllProductOrdersByPlacedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where placedDate equals to DEFAULT_PLACED_DATE
        defaultProductOrderShouldBeFound("placedDate.equals=" + DEFAULT_PLACED_DATE);

        // Get all the productOrderList where placedDate equals to UPDATED_PLACED_DATE
        defaultProductOrderShouldNotBeFound("placedDate.equals=" + UPDATED_PLACED_DATE);
    }

    @Test
    @Transactional
    public void getAllProductOrdersByPlacedDateIsInShouldWork() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where placedDate in DEFAULT_PLACED_DATE or UPDATED_PLACED_DATE
        defaultProductOrderShouldBeFound("placedDate.in=" + DEFAULT_PLACED_DATE + "," + UPDATED_PLACED_DATE);

        // Get all the productOrderList where placedDate equals to UPDATED_PLACED_DATE
        defaultProductOrderShouldNotBeFound("placedDate.in=" + UPDATED_PLACED_DATE);
    }

    @Test
    @Transactional
    public void getAllProductOrdersByPlacedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where placedDate is not null
        defaultProductOrderShouldBeFound("placedDate.specified=true");

        // Get all the productOrderList where placedDate is null
        defaultProductOrderShouldNotBeFound("placedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status equals to DEFAULT_STATUS
        defaultProductOrderShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the productOrderList where status equals to UPDATED_STATUS
        defaultProductOrderShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProductOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultProductOrderShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the productOrderList where status equals to UPDATED_STATUS
        defaultProductOrderShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProductOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status is not null
        defaultProductOrderShouldBeFound("status.specified=true");

        // Get all the productOrderList where status is null
        defaultProductOrderShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductOrdersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where code equals to DEFAULT_CODE
        defaultProductOrderShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the productOrderList where code equals to UPDATED_CODE
        defaultProductOrderShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllProductOrdersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where code in DEFAULT_CODE or UPDATED_CODE
        defaultProductOrderShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the productOrderList where code equals to UPDATED_CODE
        defaultProductOrderShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllProductOrdersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where code is not null
        defaultProductOrderShouldBeFound("code.specified=true");

        // Get all the productOrderList where code is null
        defaultProductOrderShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductOrdersByOrderItemIsEqualToSomething() throws Exception {
        // Initialize the database
        OrderItem orderItem = OrderItemResourceIntTest.createEntity(em);
        em.persist(orderItem);
        em.flush();
        productOrder.addOrderItem(orderItem);
        productOrderRepository.saveAndFlush(productOrder);
        Long orderItemId = orderItem.getId();

        // Get all the productOrderList where orderItem equals to orderItemId
        defaultProductOrderShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the productOrderList where orderItem equals to orderItemId + 1
        defaultProductOrderShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }


    @Test
    @Transactional
    public void getAllProductOrdersByShipIsEqualToSomething() throws Exception {
        // Initialize the database
        Ship ship = ShipResourceIntTest.createEntity(em);
        em.persist(ship);
        em.flush();
        productOrder.setShip(ship);
        productOrderRepository.saveAndFlush(productOrder);
        Long shipId = ship.getId();

        // Get all the productOrderList where ship equals to shipId
        defaultProductOrderShouldBeFound("shipId.equals=" + shipId);

        // Get all the productOrderList where ship equals to shipId + 1
        defaultProductOrderShouldNotBeFound("shipId.equals=" + (shipId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductOrderShouldBeFound(String filter) throws Exception {
        restProductOrderMockMvc.perform(get("/api/product-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restProductOrderMockMvc.perform(get("/api/product-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductOrderShouldNotBeFound(String filter) throws Exception {
        restProductOrderMockMvc.perform(get("/api/product-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductOrderMockMvc.perform(get("/api/product-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProductOrder() throws Exception {
        // Get the productOrder
        restProductOrderMockMvc.perform(get("/api/product-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductOrder() throws Exception {
        // Initialize the database
        productOrderService.save(productOrder);

        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();

        // Update the productOrder
        ProductOrder updatedProductOrder = productOrderRepository.findById(productOrder.getId()).get();
        // Disconnect from session so that the updates on updatedProductOrder are not directly saved in db
        em.detach(updatedProductOrder);
        updatedProductOrder
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);

        restProductOrderMockMvc.perform(put("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductOrder)))
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();

        // Create the ProductOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderMockMvc.perform(put("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductOrder() throws Exception {
        // Initialize the database
        productOrderService.save(productOrder);

        int databaseSizeBeforeDelete = productOrderRepository.findAll().size();

        // Delete the productOrder
        restProductOrderMockMvc.perform(delete("/api/product-orders/{id}", productOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOrder.class);
        ProductOrder productOrder1 = new ProductOrder();
        productOrder1.setId(1L);
        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setId(productOrder1.getId());
        assertThat(productOrder1).isEqualTo(productOrder2);
        productOrder2.setId(2L);
        assertThat(productOrder1).isNotEqualTo(productOrder2);
        productOrder1.setId(null);
        assertThat(productOrder1).isNotEqualTo(productOrder2);
    }
}

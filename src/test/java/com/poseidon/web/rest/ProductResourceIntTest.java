package com.poseidon.web.rest;

import com.poseidon.PoseidonApiGatewayApp;

import com.poseidon.domain.Product;
import com.poseidon.repository.ProductRepository;
import com.poseidon.repository.search.ProductSearchRepository;
import com.poseidon.service.ProductService;
import com.poseidon.service.dto.ProductDTO;
import com.poseidon.service.mapper.ProductMapper;
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
import java.util.Collections;
import java.util.List;


import static com.poseidon.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PoseidonApiGatewayApp.class)
public class ProductResourceIntTest {

    private static final String DEFAULT_LEVEL_ONE = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL_ONE = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL_TWO = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL_TWO = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL_THREE = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL_THREE = "BBBBBBBBBB";

    private static final String DEFAULT_PART_NO = "AAAAAAAAAA";
    private static final String UPDATED_PART_NO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_UOM = "AAAAAAAAAA";
    private static final String UPDATED_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_MTML_UOM = "AAAAAAAAAA";
    private static final String UPDATED_MTML_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String DEFAULT_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_INFORMATION = "BBBBBBBBBB";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    /**
     * This repository is mocked in the com.poseidon.repository.search test package.
     *
     * @see com.poseidon.repository.search.ProductSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductSearchRepository mockProductSearchRepository;

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

    private MockMvc restProductMockMvc;

    private Product product;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productService);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .levelOne(DEFAULT_LEVEL_ONE)
            .levelTwo(DEFAULT_LEVEL_TWO)
            .levelThree(DEFAULT_LEVEL_THREE)
            .partNo(DEFAULT_PART_NO)
            .description(DEFAULT_DESCRIPTION)
            .uom(DEFAULT_UOM)
            .mtmlUom(DEFAULT_MTML_UOM)
            .explanation(DEFAULT_EXPLANATION)
            .picture(DEFAULT_PICTURE)
            .information(DEFAULT_INFORMATION);
        return product;
    }

    @Before
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getLevelOne()).isEqualTo(DEFAULT_LEVEL_ONE);
        assertThat(testProduct.getLevelTwo()).isEqualTo(DEFAULT_LEVEL_TWO);
        assertThat(testProduct.getLevelThree()).isEqualTo(DEFAULT_LEVEL_THREE);
        assertThat(testProduct.getPartNo()).isEqualTo(DEFAULT_PART_NO);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getUom()).isEqualTo(DEFAULT_UOM);
        assertThat(testProduct.getMtmlUom()).isEqualTo(DEFAULT_MTML_UOM);
        assertThat(testProduct.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testProduct.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testProduct.getInformation()).isEqualTo(DEFAULT_INFORMATION);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].levelOne").value(hasItem(DEFAULT_LEVEL_ONE.toString())))
            .andExpect(jsonPath("$.[*].levelTwo").value(hasItem(DEFAULT_LEVEL_TWO.toString())))
            .andExpect(jsonPath("$.[*].levelThree").value(hasItem(DEFAULT_LEVEL_THREE.toString())))
            .andExpect(jsonPath("$.[*].partNo").value(hasItem(DEFAULT_PART_NO.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].uom").value(hasItem(DEFAULT_UOM.toString())))
            .andExpect(jsonPath("$.[*].mtmlUom").value(hasItem(DEFAULT_MTML_UOM.toString())))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION.toString())))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION.toString())));
    }
    
    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.levelOne").value(DEFAULT_LEVEL_ONE.toString()))
            .andExpect(jsonPath("$.levelTwo").value(DEFAULT_LEVEL_TWO.toString()))
            .andExpect(jsonPath("$.levelThree").value(DEFAULT_LEVEL_THREE.toString()))
            .andExpect(jsonPath("$.partNo").value(DEFAULT_PART_NO.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.uom").value(DEFAULT_UOM.toString()))
            .andExpect(jsonPath("$.mtmlUom").value(DEFAULT_MTML_UOM.toString()))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION.toString()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()))
            .andExpect(jsonPath("$.information").value(DEFAULT_INFORMATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .levelOne(UPDATED_LEVEL_ONE)
            .levelTwo(UPDATED_LEVEL_TWO)
            .levelThree(UPDATED_LEVEL_THREE)
            .partNo(UPDATED_PART_NO)
            .description(UPDATED_DESCRIPTION)
            .uom(UPDATED_UOM)
            .mtmlUom(UPDATED_MTML_UOM)
            .explanation(UPDATED_EXPLANATION)
            .picture(UPDATED_PICTURE)
            .information(UPDATED_INFORMATION);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getLevelOne()).isEqualTo(UPDATED_LEVEL_ONE);
        assertThat(testProduct.getLevelTwo()).isEqualTo(UPDATED_LEVEL_TWO);
        assertThat(testProduct.getLevelThree()).isEqualTo(UPDATED_LEVEL_THREE);
        assertThat(testProduct.getPartNo()).isEqualTo(UPDATED_PART_NO);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getUom()).isEqualTo(UPDATED_UOM);
        assertThat(testProduct.getMtmlUom()).isEqualTo(UPDATED_MTML_UOM);
        assertThat(testProduct.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testProduct.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testProduct.getInformation()).isEqualTo(UPDATED_INFORMATION);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).deleteById(product.getId());
    }

    @Test
    @Transactional
    public void searchProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        when(mockProductSearchRepository.search(queryStringQuery("id:" + product.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(product), PageRequest.of(0, 1), 1));
        // Search the product
        restProductMockMvc.perform(get("/api/_search/products?query=id:" + product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].levelOne").value(hasItem(DEFAULT_LEVEL_ONE)))
            .andExpect(jsonPath("$.[*].levelTwo").value(hasItem(DEFAULT_LEVEL_TWO)))
            .andExpect(jsonPath("$.[*].levelThree").value(hasItem(DEFAULT_LEVEL_THREE)))
            .andExpect(jsonPath("$.[*].partNo").value(hasItem(DEFAULT_PART_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].uom").value(hasItem(DEFAULT_UOM)))
            .andExpect(jsonPath("$.[*].mtmlUom").value(hasItem(DEFAULT_MTML_UOM)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        ProductDTO productDTO2 = new ProductDTO();
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO2.setId(productDTO1.getId());
        assertThat(productDTO1).isEqualTo(productDTO2);
        productDTO2.setId(2L);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO1.setId(null);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productMapper.fromId(null)).isNull();
    }
}

package com.poseidon.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProductCategory.
 */
@Entity
@Table(name = "product_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productcategory")
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "level_one")
    private String levelOne;

    @Column(name = "level_two")
    private String levelTwo;

    @Column(name = "level_three")
    private String levelThree;

    @OneToMany(mappedBy = "productCategory")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevelOne() {
        return levelOne;
    }

    public ProductCategory levelOne(String levelOne) {
        this.levelOne = levelOne;
        return this;
    }

    public void setLevelOne(String levelOne) {
        this.levelOne = levelOne;
    }

    public String getLevelTwo() {
        return levelTwo;
    }

    public ProductCategory levelTwo(String levelTwo) {
        this.levelTwo = levelTwo;
        return this;
    }

    public void setLevelTwo(String levelTwo) {
        this.levelTwo = levelTwo;
    }

    public String getLevelThree() {
        return levelThree;
    }

    public ProductCategory levelThree(String levelThree) {
        this.levelThree = levelThree;
        return this;
    }

    public void setLevelThree(String levelThree) {
        this.levelThree = levelThree;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public ProductCategory products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public ProductCategory addProduct(Product product) {
        this.products.add(product);
        product.setProductCategory(this);
        return this;
    }

    public ProductCategory removeProduct(Product product) {
        this.products.remove(product);
        product.setProductCategory(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductCategory productCategory = (ProductCategory) o;
        if (productCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
            "id=" + getId() +
            ", levelOne='" + getLevelOne() + "'" +
            ", levelTwo='" + getLevelTwo() + "'" +
            ", levelThree='" + getLevelThree() + "'" +
            "}";
    }
}

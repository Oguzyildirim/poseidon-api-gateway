package com.poseidon.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "product")
public class Product implements Serializable {

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

    @Column(name = "part_no")
    private String partNo;

    @Column(name = "description")
    private String description;

    @Column(name = "uom")
    private String uom;

    @Column(name = "mtml_uom")
    private String mtmlUom;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "picture")
    private String picture;

    @Column(name = "information")
    private String information;

    @ManyToOne
    @JsonIgnoreProperties("products")
    private ProductCategory productCategory;

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

    public Product levelOne(String levelOne) {
        this.levelOne = levelOne;
        return this;
    }

    public void setLevelOne(String levelOne) {
        this.levelOne = levelOne;
    }

    public String getLevelTwo() {
        return levelTwo;
    }

    public Product levelTwo(String levelTwo) {
        this.levelTwo = levelTwo;
        return this;
    }

    public void setLevelTwo(String levelTwo) {
        this.levelTwo = levelTwo;
    }

    public String getLevelThree() {
        return levelThree;
    }

    public Product levelThree(String levelThree) {
        this.levelThree = levelThree;
        return this;
    }

    public void setLevelThree(String levelThree) {
        this.levelThree = levelThree;
    }

    public String getPartNo() {
        return partNo;
    }

    public Product partNo(String partNo) {
        this.partNo = partNo;
        return this;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getDescription() {
        return description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUom() {
        return uom;
    }

    public Product uom(String uom) {
        this.uom = uom;
        return this;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getMtmlUom() {
        return mtmlUom;
    }

    public Product mtmlUom(String mtmlUom) {
        this.mtmlUom = mtmlUom;
        return this;
    }

    public void setMtmlUom(String mtmlUom) {
        this.mtmlUom = mtmlUom;
    }

    public String getExplanation() {
        return explanation;
    }

    public Product explanation(String explanation) {
        this.explanation = explanation;
        return this;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getPicture() {
        return picture;
    }

    public Product picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInformation() {
        return information;
    }

    public Product information(String information) {
        this.information = information;
        return this;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public Product productCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        return this;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
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
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", levelOne='" + getLevelOne() + "'" +
            ", levelTwo='" + getLevelTwo() + "'" +
            ", levelThree='" + getLevelThree() + "'" +
            ", partNo='" + getPartNo() + "'" +
            ", description='" + getDescription() + "'" +
            ", uom='" + getUom() + "'" +
            ", mtmlUom='" + getMtmlUom() + "'" +
            ", explanation='" + getExplanation() + "'" +
            ", picture='" + getPicture() + "'" +
            ", information='" + getInformation() + "'" +
            "}";
    }
}

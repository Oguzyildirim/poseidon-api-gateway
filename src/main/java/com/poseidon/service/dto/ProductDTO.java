package com.poseidon.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    private String levelOne;

    private String levelTwo;

    private String levelThree;

    private String partNo;

    private String description;

    private String uom;

    private String mtmlUom;

    private String explanation;

    private String picture;

    private String information;


    private Long productCategoryId;

    private String productCategoryLevelOne;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(String levelOne) {
        this.levelOne = levelOne;
    }

    public String getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(String levelTwo) {
        this.levelTwo = levelTwo;
    }

    public String getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(String levelThree) {
        this.levelThree = levelThree;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getMtmlUom() {
        return mtmlUom;
    }

    public void setMtmlUom(String mtmlUom) {
        this.mtmlUom = mtmlUom;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryLevelOne() {
        return productCategoryLevelOne;
    }

    public void setProductCategoryLevelOne(String productCategoryLevelOne) {
        this.productCategoryLevelOne = productCategoryLevelOne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
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
            ", productCategory=" + getProductCategoryId() +
            ", productCategory='" + getProductCategoryLevelOne() + "'" +
            "}";
    }
}

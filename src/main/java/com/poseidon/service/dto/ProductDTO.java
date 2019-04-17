package com.poseidon.service.dto;

public class ProductDTO {

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProductDTO{");
        sb.append("levelOne='").append(levelOne).append('\'');
        sb.append(", levelTwo='").append(levelTwo).append('\'');
        sb.append(", levelThree='").append(levelThree).append('\'');
        sb.append(", partNo='").append(partNo).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", uom='").append(uom).append('\'');
        sb.append(", mtmlUom='").append(mtmlUom).append('\'');
        sb.append(", explanation='").append(explanation).append('\'');
        sb.append(", picture='").append(picture).append('\'');
        sb.append(", information='").append(information).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

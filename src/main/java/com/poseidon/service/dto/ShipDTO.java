package com.poseidon.service.dto;

import com.poseidon.config.Constants;
import com.poseidon.domain.enumeration.Gender;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ShipDTO {


    private BigDecimal shipId;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    private Gender gender;

    @Pattern(regexp= Constants.LOGIN_REGEX, message="Invalid phone number!")
    private String phone;

    @Size(max = 50)
    private String addressLine1;

    @Size(max = 50)
    private String addressLine2;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String country;

    public ShipDTO() {
    }

    public ShipDTO(BigDecimal shipId, @Size(max = 50) String firstName, @Size(max = 50) String lastName, Gender gender, @Pattern(regexp = Constants.LOGIN_REGEX, message = "Invalid phone number!") String phone, @Size(max = 50) String addressLine1, @Size(max = 50) String addressLine2, @Size(max = 50) String city, @Size(max = 50) String country) {
        this.shipId = shipId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.country = country;
    }

    public BigDecimal getShipId() {
        return shipId;
    }

    public void setShipId(BigDecimal shipId) {
        this.shipId = shipId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ShipDTO{");
        sb.append("shipId=").append(shipId);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", gender=").append(gender);
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", addressLine1='").append(addressLine1).append('\'');
        sb.append(", addressLine2='").append(addressLine2).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

package com.poseidon.service.dto;


import com.poseidon.config.Constants;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CompanyDTO {

    @Pattern(regexp= Constants.LOGIN_REGEX, message="Invalid phone number!")
    private String phone;

    @Size(max = 50)
    private String name;

    @Size(max = 50)
    private String addressLine1;

    @Size(max = 50)
    private String addressLine2;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String country;

    public CompanyDTO() {
    }

    public CompanyDTO(@Pattern(regexp = Constants.LOGIN_REGEX, message = "Invalid phone number!") String phone, @NotNull String name, @Size(max = 50) String addressLine1, @Size(max = 50) String addressLine2, @Size(max = 50) String city, @Size(max = 50) String country) {
        this.phone = phone;
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CompanyDTO{");
        sb.append("phone='").append(phone).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", addressLine1='").append(addressLine1).append('\'');
        sb.append(", addressLine2='").append(addressLine2).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

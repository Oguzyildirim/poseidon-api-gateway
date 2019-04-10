package com.poseidon.web.rest.errors;

public class CompanyAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public CompanyAlreadyUsedException() {
        super(ErrorConstants.COMPANY_ALREADY_USED_TYPE, "Company is already in use!", "userManagement", "companyexists");
    }
}

package com.poseidon.web.rest.errors;

public class ShipAlreadyUsedException extends BadRequestAlertException  {

    private static final long serialVersionUID = 1L;

    public ShipAlreadyUsedException() {
        super(ErrorConstants.SHIP_ALREADY_USED_TYPE, "Ship is already in use!", "userManagement", "shipexists");
    }
}


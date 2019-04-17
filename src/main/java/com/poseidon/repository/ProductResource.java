package com.poseidon.repository;

import com.poseidon.service.dto.ProductDTO;
import com.poseidon.service.dto.ProductQueryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


@FeignClient("search-service")
public interface ProductResource {

    @PostMapping(value = "/api/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<ProductDTO> getProducts(@RequestBody @Valid final ProductQueryDTO productQueryDTO);

    @PostMapping(value = "/api/fuzzySearch", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<ProductDTO> getProductsFuzzySearch(@RequestBody final ProductQueryDTO productQueryDTO);

    @GetMapping(value = "/api/statistics", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> indexStatistics(@RequestParam("index") final String index);



}

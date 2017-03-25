package com.hereforbeer.controllers;

import com.hereforbeer.controllers.exchanges.OfferGapDTO;
import com.hereforbeer.offer_gap.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OfferGapController {

    private final OfferService offerService;

    @Autowired
    public OfferGapController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/offer-gaps")
    public ResponseEntity<List<OfferGapDTO>> get() {
        Map<String, Integer> bestOfferGaps = offerService.getBestOfferGaps();
        return ResponseEntity.ok(bestOfferGaps
                .entrySet()
                .stream()
                .map( entry -> new OfferGapDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));

    }
}

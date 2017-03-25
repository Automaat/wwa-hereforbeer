package com.hereforbeer.controllers;

import com.hereforbeer.controllers.exchanges.OfferGapDTO;
import com.hereforbeer.offer_gap.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
class OfferGapController {

    private final OfferService offerService;

    OfferGapController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/offer-gaps")
    ResponseEntity<List<OfferGapDTO>> getOfferGaps() {
        Map<String, Integer> bestOfferGaps = offerService.getBestOfferGaps();
        return ResponseEntity.ok(bestOfferGaps
                .entrySet()
                .stream()
                .map( entry -> OfferGapDTO.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));

    }
}

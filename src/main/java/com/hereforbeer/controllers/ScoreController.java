package com.hereforbeer.controllers;

import com.hereforbeer.controllers.exchanges.ScoreDTO;
import com.hereforbeer.offer_gap.ScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/scores")
@Controller
class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping(params = {"searchPhrase"})
    ResponseEntity<ScoreDTO> getScoreForPhrase(@RequestParam("searchPhrase") String searchPhrase) {
        return ResponseEntity.ok(ScoreDTO.of(scoreService.getMaxScoreForPhrase(searchPhrase)));
    }
}

package baza.trainee.controller;

import baza.trainee.domain.model.MuseumData;
import baza.trainee.service.MuseumDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST controller class for retrieving museum data in the public API.
 * This controller handles public operations with museum data.
 */
@RestController
@RequestMapping("/api/museum_data")
@RequiredArgsConstructor
public class MuseumDataController {

    private final MuseumDataService museumDataService;

    /**
     * Retrieves available museum data.
     *
     * @return The MuseumData object containing all museum data.
     */
    @GetMapping
    public MuseumData getAllData() {
        return museumDataService.getData();
    }
}


package baza.trainee.controller.admin;

import baza.trainee.domain.model.MuseumData;
import baza.trainee.service.MuseumDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * A REST controller class for managing museum data in the admin API.
 * This controller handles operations such as adding and updating museum data.
 */
@RestController
@RequestMapping("/api/admin/museum_data")
@RequiredArgsConstructor
public class MuseumDataAdminController {

    private final MuseumDataService museumDataService;

    /**
     * Adds new museum data.
     *
     * @param museumData The MuseumData object containing new contacts data of museum.
     * @return The MuseumData object that has been added.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MuseumData addData(@RequestBody MuseumData museumData) {
        return museumDataService.add(museumData);
    }

    /**
     * Updates existing museum data.
     *
     * @param museumData The MuseumData object containing updated contacts data of museum.
     * @return The MuseumData object that has been updated.
     */
    @PutMapping
    public MuseumData updateData(@RequestBody MuseumData museumData) {
        return museumDataService.update(museumData);
    }
}

package baza.trainee.service;

import baza.trainee.domain.model.MuseumData;

public interface MuseumDataService {

    /**
     * Updates existing museum data.
     *
     * @param museumData The MuseumData object containing the updated contacts data.
     * @return The MuseumData object that has been updated.
     */
    MuseumData update(MuseumData museumData);

    /**
     * Adds new museum data.
     *
     * @param museumData The MuseumData object containing the contacts data to be added.
     * @return The MuseumData object that has been added.
     */
    MuseumData add(MuseumData museumData);

    /**
     * Retrieves available museum data.
     *
     * @return The MuseumData object containing available museum data.
     */
    MuseumData getData();
}

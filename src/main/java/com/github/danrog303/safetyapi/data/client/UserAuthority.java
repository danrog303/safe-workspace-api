package com.github.danrog303.safetyapi.data.client;

/**
 * Authority (permission) of the application user.
 */
public enum UserAuthority {
    /**
     * This user can fetch the event log of the application.
     */
    GET_EVENT_LOG,

    /**
     * This user can upload new photos,
     * which will be validated to see if all the people in the photo have the necessary protective equipment.
     */
    UPLOAD_PHOTOS
}

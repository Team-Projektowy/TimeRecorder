package com.timerecorder.helpers;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

public class requestProcessingHelper {

    public static void throwUnauthorizedIfRequesterIsNotAdmin(HttpServletRequest request) throws ResponseStatusException {
        Claims claims = (Claims) request.getAttribute("claims");
        if (!(boolean) claims.get("isAdmin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this Time Record");
        }
    }
}

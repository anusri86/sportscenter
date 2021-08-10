package com.example.sportscenter.web;

import com.example.sportscenter.model.CourtBooking;
import com.example.sportscenter.service.CourtBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/api/court-bookings")
@RestController
public class CourtBookingController {

    @Autowired
    CourtBookingService courtBookingService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createBooking(@RequestBody CourtBooking courtBooking) {
        Long bookingId = courtBookingService.createBooking(courtBooking);
        if (bookingId != null) {
            return ResponseEntity.ok("Booking accepted. bookingId = " + bookingId);
        } else {
            return ResponseEntity.ok("Booking rejected. Courts full");
        }
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public CourtBooking getBooking(@PathVariable("id") Long id) {
        return courtBookingService.getBooking(id);
    }

}

package com.example.sportscenter.service;

import com.example.sportscenter.exception.NotFoundException;
import com.example.sportscenter.model.CourtBooking;
import com.example.sportscenter.repository.CourtBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CourtBookingService {

    @Autowired
    CourtBookingRepository courtBookingRepository;

    private final ConcurrentHashMap<String, AtomicInteger> courtBookingCountMap = new ConcurrentHashMap<>();

    @Transactional
    public Long createBooking(CourtBooking courtBooking) {
        String bookingDateString = courtBooking.getBookingDate().toString();
        for (int courtNum = 1; courtNum <= 3; courtNum++) {
            String bookingSlot = bookingDateString.concat(String.valueOf(courtNum));
            if (courtBookingCountMap.containsKey(bookingSlot)) {
                allocateCourtAndSaveBooking(courtBooking, bookingSlot, courtNum);
            } else {
                courtBookingCountMap.put(bookingSlot, new AtomicInteger(1));
                updateBookingAndSave(courtBooking, courtNum);
            }
            if (courtBooking.getId() != null) break;
        }
        return courtBooking.getId();
    }

    public CourtBooking getBooking(Long id) {
        Optional<CourtBooking> booking = courtBookingRepository.findById(id);
        if (booking.isPresent()) {
            return booking.get();
        } else {
            throw new NotFoundException("Booking not found for id = " + id);
        }
    }

    private void allocateCourtAndSaveBooking(CourtBooking courtBooking, String bookingSlot, Integer courtNumber) {
        while (true) {
            AtomicInteger count = courtBookingCountMap.get(bookingSlot);
            int current = count.get();
            if (current == 4) {
                return;
            } else {
                if (count.compareAndSet(current, current + 1)) {
                    updateBookingAndSave(courtBooking, courtNumber);
                    if (current == 3) {
                        sendNotification(courtNumber);
                    }
                    return;
                }
            }
        }
    }

    private void updateBookingAndSave(CourtBooking courtBooking, Integer courtNumber) {
        courtBooking.setCourtNumber(courtNumber);
        courtBooking.setStatus("ACCEPTED");
        courtBookingRepository.save(courtBooking);
    }

    private void sendNotification(Integer courtNumber) {
        courtBookingRepository.findByCourtNumber(courtNumber)
                .forEach(booking -> {
                    booking.setStatus("CONFIRMED");
                    System.out.println("Booking Confirmed. " + booking);
                });
    }

}

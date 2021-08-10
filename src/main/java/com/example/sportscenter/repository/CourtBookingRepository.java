package com.example.sportscenter.repository;

import com.example.sportscenter.model.CourtBooking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourtBookingRepository extends CrudRepository<CourtBooking, Long> {
    List<CourtBooking> findByCourtNumber(Integer courtNumber);
}

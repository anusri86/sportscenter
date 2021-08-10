## Prerequisites

Java 11

## How to Run

Build and run the application using maven wrapper.  
./mvnw clean install  
./mvnw spring-boot:run  

Register interest to play by running below curl command in the terminal.
curl -d '{"customerId":{customerId},"bookingDate":{bookingDate}}' -H 'Content-Type: application/json' http://localhost:8080/api/court-bookings

{customerId} -> Unique customer id (String)
{bookingDate} -> Booking date in dd/MM/yyyy format (String)

Get booking details by running below curl command in the terminal.
curl http://localhost:8080/api/court-bookings/{id}

{id} -> Booking id returned in post command


## Assumptions
* Changes after showing interest is not allowed
* Court is occupied for full day, i.e. only one session per day

## Solution

Booking application will take the customerId and booking date and allocate an available court. 
If allocation is successful it will return a message with bookingId or else will return error message when courts are full. 

In this solution application uses a ConcurrentHashMap to keep record of booking count for each booking slot(bookingDate+courtNumber).
Additionally it uses AtomicInteger counters to ensure thread safety. Courts will be allocated first-come, first-served basis until count reaches 4 for each slot.


## Future Improvements

* Do not allow same customer to book a court more than once in a given day.
* Initialize court booking map on startup using records saved in db.
 eg:- group by booking date+court number and get count






package org.pollub.rental.service;

import java.math.BigDecimal;

//Lab5 : Liskov 3 Start
public interface IRentalFeeStrategy {
    BigDecimal calculateFee(int rentalDays);
    boolean supports(String type);
}
//Lab5 : Liskov 3 End

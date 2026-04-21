package org.pollub.rental.service;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

//Lab5 : Liskov 3 Start
@Component
public class StandardFeeStrategy implements IRentalFeeStrategy {
    
    private static final BigDecimal DAILY_RATE = new BigDecimal("2.50");
    
    @Override
    public BigDecimal calculateFee(int rentalDays) {
        if (rentalDays <= 0) return BigDecimal.ZERO;
        return DAILY_RATE.multiply(new BigDecimal(rentalDays));
    }

    @Override
    public boolean supports(String type) {
        return "STANDARD".equalsIgnoreCase(type) || type == null || type.isBlank();
    }
}
//Lab5 : Liskov 3 End

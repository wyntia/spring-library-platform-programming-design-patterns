package org.pollub.rental.service;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

//Lab5 : Liskov 3 Start
@Component
public class StudentDiscountFeeStrategy implements IRentalFeeStrategy {
    
    // Delegate to StandardFeeStrategy conceptually or implement independently
    private static final BigDecimal DAILY_RATE = new BigDecimal("2.50");
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.80"); // 20% discount
    
    @Override
    public BigDecimal calculateFee(int rentalDays) {
        if (rentalDays <= 0) return BigDecimal.ZERO;
        BigDecimal baseFee = DAILY_RATE.multiply(new BigDecimal(rentalDays));
        return baseFee.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean supports(String type) {
        return "STUDENT".equalsIgnoreCase(type);
    }
}
//Lab5 : Liskov 3 End

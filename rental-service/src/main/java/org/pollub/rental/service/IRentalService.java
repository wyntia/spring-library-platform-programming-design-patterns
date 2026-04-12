package org.pollub.rental.service;

import org.pollub.common.dto.ItemDto;
import org.pollub.common.dto.ReservationResponse;
import org.pollub.rental.model.RentalHistory;

import java.util.List;

public interface IRentalService {
    List<ItemDto> getActiveRentals(Long userId);
    List<RentalHistory> getUserRentalHistory(Long userId);
    List<RentalHistory> getItemRentalHistory(Long itemId);

    ReservationResponse rentItem(Long itemId, Long userId, Long branchId);

    void returnItem(Long itemId, Long branchId);

    //Lab6 : Znaczące nazewnictwo 1 Start
    void extendRental(Long itemId, Long branchId, int days);
    //Lab6 : Znaczące nazewnictwo 1 Stop

    //Lab5 : Liskov 3 Start
    java.math.BigDecimal calculateRentalFee(int days, IRentalFeeStrategy feeStrategy);
    //Lab5 : Liskov 3 End
}

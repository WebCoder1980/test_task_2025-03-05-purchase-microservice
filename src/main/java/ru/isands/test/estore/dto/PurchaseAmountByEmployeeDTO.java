package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PurchaseAmountByEmployeeDTO {
    public PurchaseAmountByEmployeeDTO(Long employeeId, Double totalAmount) {
        setEmployeeId(employeeId);
        setTotalAmount(totalAmount);
    }

    @NonNull
    Long employeeId;

    @NonNull
    Double totalAmount;
}

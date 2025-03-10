package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PurchaseCountByEmployeeDTO {
    public PurchaseCountByEmployeeDTO(Long employeeId, Long totalCount) {
        setEmployeeId(employeeId);
        setTotalCount(totalCount);
    }

    @NonNull
    Long employeeId;

    @NonNull
    Long totalCount;
}

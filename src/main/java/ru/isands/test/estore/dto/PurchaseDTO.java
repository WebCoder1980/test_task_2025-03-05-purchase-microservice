package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PurchaseDTO {
    @NonNull
    Long id;

    @NonNull
    Long electroId;

    @NonNull
    Long employeeId;

    @NonNull
    Long shopId;

    @NonNull
    LocalDateTime purchaseDate;

    @NonNull
    Long type;
}

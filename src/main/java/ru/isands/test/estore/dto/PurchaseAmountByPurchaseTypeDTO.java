package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PurchaseAmountByPurchaseTypeDTO {
    public PurchaseAmountByPurchaseTypeDTO(Long id, Double sum, String name, String address) {
        setId(id);
        setSum(sum);
        setName(name);
        setAddress(address);
    }

    @NonNull
    Long id;

    @NonNull
    Double sum;

    @NonNull
    String name;

    @NonNull
    String address;
}

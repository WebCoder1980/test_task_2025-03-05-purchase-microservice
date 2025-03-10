package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.isands.test.estore.dao.entity.Shop;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class PurchaseJuniorSalesConsultant_smartWatchesDTO {
    public PurchaseJuniorSalesConsultant_smartWatchesDTO(Long employeeId, Long salesCount, String lastName, String firstName, String patronymic, LocalDate birthDate, Long shopId, Boolean gender) {
        setEmployeeId(employeeId);
        setSalesCount(salesCount);
        setLastName(lastName);
        setFirstName(firstName);
        setPatronymic(patronymic);
        setBirthDate(birthDate);
        setShopId(shopId);
        setGender(gender);
    }

    @NotNull
    private Long employeeId;

    @NonNull
    private Long salesCount;

    @NonNull
    private String lastName;

    @NonNull
    private String firstName;

    @NonNull
    private String patronymic;

    @NonNull
    private LocalDate birthDate;

    @NotNull
    private Long shopId;

    @NotNull
    private Boolean gender;
}

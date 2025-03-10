package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class PurchaseCountByEmployeeDTO {
    public PurchaseCountByEmployeeDTO(Long employeeId, Long totalCount, String lastName, String firstName, String patronymic, LocalDate birthDate, Long positionId, Long shopId, Boolean gender) {
        setEmployeeId(employeeId);
        setTotalCount(totalCount);
        setLastName(lastName);
        setFirstName(firstName);
        setPatronymic(patronymic);
        setBirthDate(birthDate);
        setPositionId(positionId);
        setShopId(shopId);
        setGender(gender);
    }

    @NonNull
    private Long employeeId;

    @NonNull
    private Long totalCount;

    @NonNull
    private String lastName;

    @NonNull
    private String firstName;

    @NonNull
    private String patronymic;

    @NonNull
    private LocalDate birthDate;

    @NonNull
    private Long positionId;

    @NotNull
    private Long shopId;

    @NotNull
    private boolean gender;
}

package ru.isands.test.estore.dao.repo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.isands.test.estore.dao.entity.Purchase;
import ru.isands.test.estore.dto.PurchaseAmountByEmployeeDTO;
import ru.isands.test.estore.dto.PurchaseCountByEmployeeDTO;
import ru.isands.test.estore.dto.PurchaseJuniorSalesConsultant_smartWatchesDTO;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query("SELECT new ru.isands.test.estore.dto.PurchaseCountByEmployeeDTO(p.employee.id, COUNT(p.electroItem.id)) " +
            "FROM Purchase p " +
            "GROUP BY p.employee.id " +
            "ORDER BY COUNT(p.electroItem.id) DESC")
    List<PurchaseCountByEmployeeDTO> getTotalCountByEmployee();

    @Query("SELECT new ru.isands.test.estore.dto.PurchaseAmountByEmployeeDTO(p.employee.id, SUM(p.electroItem.price)) " +
            "FROM Purchase p " +
            "GROUP BY p.employee.id " +
            "ORDER BY SUM(p.electroItem.price) DESC")
    List<PurchaseAmountByEmployeeDTO> getTotalAmountByEmployee();

    @Query("SELECT new ru.isands.test.estore.dto.PurchaseJuniorSalesConsultant_smartWatchesDTO(p.employee.id, COUNT(p.electroItem.id), p.employee.lastName, p.employee.firstName, p.employee.patronymic, p.employee.birthDate, p.employee.shop.id, p.employee.gender) " +
            "FROM Purchase p " +
            "WHERE p.employee.position.name = 'Младший продавец-консультант' " +
            "AND p.electroItem.type.name = 'Умные часы' " +
            "GROUP BY p.employee.id, p.employee.lastName, p.employee.firstName, p.employee.patronymic, p.employee.birthDate, p.employee.shop.id, p.employee.gender " +
            "ORDER BY COUNT(p.electroItem.id) DESC"
    )
    List<PurchaseJuniorSalesConsultant_smartWatchesDTO> getJuniorSalesConsultant_smartWatches();
}

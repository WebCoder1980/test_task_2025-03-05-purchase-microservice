package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isands.test.estore.dao.entity.StorePurchaseType;

public interface StorePurchaseTypeRepository extends JpaRepository<StorePurchaseType, Long> {

}
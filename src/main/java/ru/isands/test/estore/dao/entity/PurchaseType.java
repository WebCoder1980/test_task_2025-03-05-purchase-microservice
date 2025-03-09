package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "store_purchase_type")
public class PurchaseType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "purchasetype_counter")
    @TableGenerator(name = "purchasetype_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.dao.entity.PurchaseType", table = "counter", valueColumnName = "currentid", allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    @NotNull
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;
}
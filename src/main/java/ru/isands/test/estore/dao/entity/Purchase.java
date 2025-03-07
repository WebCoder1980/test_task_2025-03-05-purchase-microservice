package ru.isands.test.estore.dao.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "store_purchase")
public class Purchase implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Идентификатор покупки
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "purchase_counter")
	@TableGenerator(name = "purchase_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.dao.entity.Purchase", table = "counter", valueColumnName = "currentid", allocationSize = 1)
	@Column(name = "id_", unique = true, nullable = false)
	Long id;
	
	/**
	 * Идентификатор товара
	 */
	@ManyToOne
	@JoinColumn(name = "electroid", nullable = false)
	ElectroItem electroItem;
	
	/**
	 * Идентификатор сотрудника
	 */
	@ManyToOne
	@JoinColumn(name = "employeeid", nullable = false)
	Employee employee;
	
	/**
	 * Идентификатор магазина
	 */
	@ManyToOne
	@JoinColumn(name = "shopid", nullable = false)
	Shop shop;
	
	/**
	 * Дата совершения покупки
	 */
	@Column(name = "purchasedate", nullable = false)
	LocalDateTime purchaseDate;
	
	/**
	 * Способ оплаты
	 */
	@ManyToOne
	@JoinColumn(name = "typeid", nullable = false)
	PurchaseType type;
}
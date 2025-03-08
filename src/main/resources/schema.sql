CREATE TABLE IF NOT EXISTS counter (
	"name" varchar(75) NOT NULL,
	currentid int8 NOT NULL,
	CONSTRAINT counter_pkey PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS electro_item_type (
    id_ int8 NOT NULL,
    name varchar(150) NOT NULL,
    CONSTRAINT electro_item_type_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS position_types (
    id_ int8 NOT NULL,
    name varchar(150) NOT NULL,
    CONSTRAINT position_types_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS shop (
    id_ int8 NOT NULL,
    name varchar(250) NOT NULL,
    address text NOT NULL,
    CONSTRAINT shop_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS store_employee (
	id_ int8 NOT NULL,
	lastname varchar(100) NOT NULL,
	firstname varchar(100) NOT NULL,
	patronymic varchar(100) NOT NULL,
	birthDate date NOT NULL,
	positionId int8 NOT NULL,
    shopId int8 NOT NULL,
	gender bool NOT NULL,
	CONSTRAINT store_employee_pkey PRIMARY KEY (id_),
	CONSTRAINT fk_employee_position FOREIGN KEY (positionId) REFERENCES position_types(id_),
    CONSTRAINT fk_employee_shop FOREIGN KEY (shopId) REFERENCES shop(id_)
);

CREATE TABLE IF NOT EXISTS electro_item (
    id_ int8 NOT NULL,
    name varchar(150) NOT NULL,
    typeId int8 NOT NULL,
    price numeric(10, 2) NOT NULL,
    quantity int4 NOT NULL,
    archive bool NOT NULL,
    description text,
    CONSTRAINT electro_item_pkey PRIMARY KEY (id_),
    CONSTRAINT fk_electro_item_type FOREIGN KEY (typeId) REFERENCES electro_item_type(id_)
);

CREATE TABLE IF NOT EXISTS store_purchase_type (
    id_ int8 NOT NULL,
    name varchar(150) NOT NULL,
    CONSTRAINT store_purchase_type_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS store_purchase (
    id_ int8 NOT NULL,
    electroId int8 NOT NULL,
    employeeId int8 NOT NULL,
    purchaseDate timestamp NOT NULL,
    typeId int8 NOT NULL,
    shopId int8 NOT NULL,
    CONSTRAINT store_purchase_pkey PRIMARY KEY (id_),
    CONSTRAINT fk_purchases_electro_item FOREIGN KEY (electroId) REFERENCES electro_item(id_),
    CONSTRAINT fk_purchases_employee FOREIGN KEY (employeeId) REFERENCES store_employee(id_),
    CONSTRAINT fk_purchases_shop FOREIGN KEY (shopId) REFERENCES shop(id_),
    CONSTRAINT fk_purchases_purchase_type FOREIGN KEY (typeId) REFERENCES store_purchase_type(id_)
);
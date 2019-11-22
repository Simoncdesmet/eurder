create table CUSTOMER
(
    ID          NUMBER        not null
        constraint CUSTOMER_PK
            primary key,
    FIRSTNAME   VARCHAR2(264),
    LASTNAME    VARCHAR2(264) not null,
    EMAIL       VARCHAR2(264) not null,
    PHONENUMBER VARCHAR2(264),
    EXTERNALID VARCHAR2(264) not null
)
/

create unique index CUSTOMER_EXTERNAL_ID_UINDEX
    on CUSTOMER (EXTERNALID)
/

create sequence customer_seq start with 1 increment by 1;

create table ITEM
(
	ID number not null,
	NAME varchar2(264),
	DESCRIPTION varchar2(264),
	PRICE_IN_EURO number(9,2) not null,
	AMOUNT_IN_STOCK number not null,
	AMOUNT_TO_REORDER number not null,
	EXTERNAL_ID varchar2(264) not null
)
/

create unique index ITEM_EXTERNAL_ID_uindex
	on ITEM (EXTERNAL_ID)
/

create unique index ITEM_ID_uindex
	on ITEM (ID)
/

alter table ITEM
	add constraint ITEM_pk
		primary key (ID)
/

create sequence item_seq start with 1 increment by 1;

create table ITEM_GROUP
(
    ID               NUMBER        not null,
    ITEM_EXTERNAL_ID VARCHAR2(264) not null,
    AMOUNT           NUMBER,
    SHIPPINGDATE     DATE,
    ITEM_GROUP_PRICE NUMBER(9, 2),
    ITEM_NAME        VARCHAR2(264),
    ORDER_ID         NUMBER        not null
        constraint ITEMGR_ORDER_FK
            references ORDERS
)
/

create unique index ITEM_GROUP_ID_UINDEX
    on ITEM_GROUP (ID)
/

alter table ITEM_GROUP
    add constraint ITEM_GROUP_PK
        primary key (ID)
/

create table "ORDER"
(
    ID          NUMBER        not null,
    EXTERNAL_ID VARCHAR2(264) not null,
    TOTAL_PRICE NUMBER(9, 2),
    CUSTOMER_ID VARCHAR2(264) not null
)
/

create unique index ORDER_EXTERNAL_ID_UINDEX
    on ORDERS (EXTERNAL_ID)
/

create unique index ORDER_ID_UINDEX
    on ORDERS (ID)
/

alter table ORDERS
    add constraint ORDER_PK
        primary key (ID)
/

create sequence order_seq start with 1 increment by 1;

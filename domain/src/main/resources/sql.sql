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
    on CUSTOMER (EXTERNAL_ID)
/

create sequence customer_seq start with 1 increment by 1;

-- -----------------------------------------------------
-- Table "rtcustomer".customer_data
-- -----------------------------------------------------
CREATE TABLE rtcustomer.customer_data
(
    ucid bigint NOT NULL,
    customer_email character varying(255) COLLATE pg_catalog."default",
    created_date timestamp without time zone NOT NULL,
    created_by character varying(45) COLLATE pg_catalog."default" NOT NULL,
    modified_date timestamp without time zone,
    modified_by character varying(45) COLLATE pg_catalog."default",
    CONSTRAINT cust_data_pkey PRIMARY KEY (ucid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE rtcustomer.customer_data OWNER to moasan;


-- -----------------------------------------------------
-- Table "rtcustomer".customer_rent_info
-- -----------------------------------------------------
CREATE TABLE rtcustomer.customer_rent_info
(
    rent_info_id integer NOT NULL,
    ucid bigint NOT NULL,
    rent_due_date date,
    last_sync_date timestamp without time zone,
    created_date timestamp without time zone NOT NULL,
    created_by character varying(45) COLLATE pg_catalog."default" NOT NULL,
    modified_date timestamp without time zone,
    modified_by character varying(45) COLLATE pg_catalog."default",
    CONSTRAINT cust_rent_info_pkey PRIMARY KEY (rent_info_id),
    CONSTRAINT cust_rent_info_id_fkey FOREIGN KEY (ucid)
        REFERENCES rtcustomer.customer_data (ucid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE rtcustomer.customer_rent_info OWNER to moasan;

-- -----------------------------------------------------
-- Table "rtcustomer".utility_info
-- -----------------------------------------------------
CREATE TABLE rtcustomer.utility_info
(
    utility_id integer NOT NULL,
    utility_type character varying(255) COLLATE pg_catalog."default",
    created_date timestamp without time zone NOT NULL,
    created_by character varying(45) COLLATE pg_catalog."default",
    modified_date timestamp without time zone,
    modified_by character varying(45) COLLATE pg_catalog."default",
    CONSTRAINT util_info_pkey PRIMARY KEY (utility_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE rtcustomer.utility_info OWNER to moasan;

-- -----------------------------------------------------
-- Table "rtcustomer".customer_utility_info
-- -----------------------------------------------------
CREATE TABLE rtcustomer.customer_utility_info
(
    ucid bigint NOT NULL,
    utility_id integer NOT NULL,
    utility_due_date date NOT NULL,
    utility_sync_date timestamp(4) without time zone,
    created_date timestamp without time zone NOT NULL,
    created_by character varying(45) COLLATE pg_catalog."default" NOT NULL,
    modified_date timestamp without time zone,
    modified_by character varying(45) COLLATE pg_catalog."default",
    CONSTRAINT cust_data_info_id_fkey FOREIGN KEY (ucid)
        REFERENCES rtcustomer.customer_data (ucid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cust_util_info_id_fkey FOREIGN KEY (utility_id)
        REFERENCES rtcustomer.utility_info (utility_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE rtcustomer.customer_utility_info OWNER to moasan;
	
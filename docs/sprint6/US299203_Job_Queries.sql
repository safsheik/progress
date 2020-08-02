CREATE TABLE rtcustomer.customer_orders (
	bcm_order_id serial NOT NULL,
	order_id int8 NOT NULL,
	order_type varchar(48) NOT NULL,
	status varchar(48) NULL,
	rent varchar(48) NULL,
	other varchar(48) NULL,
	total varchar(48) NULL,
	fee varchar(48) NULL,
	paid_for varchar(48) NULL,
	customer_data_id int8 NOT NULL,
	created_date timestamptz NOT NULL,
	CONSTRAINT customer_orders_pk PRIMARY KEY (bcm_order_id)
);

ALTER TABLE rtcustomer.customer_orders ADD CONSTRAINT customer_orders_fk FOREIGN KEY (customer_data_id) REFERENCES rtcustomer.customer_data(customer_data_id);

CREATE TABLE rtcustomer.customer_orders_reported_info (
	order_info_id serial NOT NULL,
	bcm_order_id int8 NOT NULL,
	bureau_type varchar(48) NOT NULL,
	reported_at date NOT NULL,
	created_date timestamptz NOT NULL,
	CONSTRAINT customer_orders_reported_info_pk PRIMARY KEY (order_info_id)
);

ALTER TABLE rtcustomer.customer_orders_reported_info ADD CONSTRAINT customer_orders_reported_info_fk FOREIGN KEY (bcm_order_id) REFERENCES rtcustomer.customer_orders(bcm_order_id);;
GRANT ALL ON TABLE rtcustomer.customer_orders_reported_info TO bcm;


CREATE TABLE rtcustomer.failed_transaction_pull (
	id serial NOT NULL,
	customer_data_id int8 NOT NULL,
	job_id int8 NOT NULL,
	error_message varchar(200) NULL,
	created_date timestamptz NOT NULL,
	CONSTRAINT failed_transaction_pull_pk PRIMARY KEY (id),
	CONSTRAINT failed_transaction_pull_fk FOREIGN KEY (customer_data_id) REFERENCES rtcustomer.customer_data(customer_data_id),
	CONSTRAINT failed_transaction_pull_jobs_fk FOREIGN KEY (job_id) REFERENCES rtcustomer.jobs(job_id)
);
GRANT ALL ON TABLE rtcustomer.failed_transaction_pull TO bcm;



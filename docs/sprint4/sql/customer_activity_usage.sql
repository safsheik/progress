CREATE TABLE rtcustomer.customer_usage_activity (
	activity_id serial NOT NULL,
	ucid int8 NOT NULL,
	brand varchar(400) NOT NULL,
	activity_usage varchar(400) NOT NULL,
	created_date date NOT NULL
);

ALTER TABLE rtcustomer.customer_usage_activity ADD CONSTRAINT customer_usage_activity_fk FOREIGN KEY (ucid) REFERENCES rtcustomer.customer_data(ucid);
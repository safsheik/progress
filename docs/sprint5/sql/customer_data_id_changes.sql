--Drop Constraints
ALTER TABLE rtcustomer.customer_lease_info DROP CONSTRAINT customer_lease_info_fk;
ALTER TABLE rtcustomer.customer_subscription_info DROP CONSTRAINT customer_subscription_info_fk;
ALTER TABLE rtcustomer.payment_account_info DROP CONSTRAINT payment_account_info_fk;
ALTER TABLE rtcustomer.customer_usage_activity DROP CONSTRAINT customer_usage_activity_fk;
ALTER TABLE rtcustomer.customer_utility_info DROP CONSTRAINT customer_utility_info_ucid_fk;

--Add Customer_data column
ALTER TABLE rtcustomer.customer_data ADD customer_data_id serial NOT NULL;

ALTER TABLE rtcustomer.customer_data DROP CONSTRAINT customer_data_pkey;
ALTER TABLE rtcustomer.customer_data ADD CONSTRAINT customer_data_pk PRIMARY KEY (customer_data_id);

ALTER TABLE rtcustomer.customer_lease_info ADD customer_data_id int4 NOT NULL;
ALTER TABLE rtcustomer.customer_lease_info ADD CONSTRAINT customer_lease_info_fk FOREIGN KEY (customer_data_id)
 REFERENCES rtcustomer.customer_data(customer_data_id);
 
ALTER TABLE rtcustomer.customer_subscription_info ADD customer_data_id int4 NOT NULL;
ALTER TABLE rtcustomer.customer_subscription_info ADD CONSTRAINT customer_subscription_info_fk FOREIGN KEY (customer_data_id)
 REFERENCES rtcustomer.customer_data(customer_data_id);
 
ALTER TABLE rtcustomer.payment_account_info ADD customer_data_id int4 NOT NULL;
ALTER TABLE rtcustomer.payment_account_info ADD CONSTRAINT payment_account_info_fk FOREIGN KEY (customer_data_id)
 REFERENCES rtcustomer.customer_data(customer_data_id);


ALTER TABLE rtcustomer.customer_usage_activity ADD customer_data_id int4 NULL;
ALTER TABLE rtcustomer.customer_usage_activity ADD CONSTRAINT customer_usage_activity_fk FOREIGN KEY (customer_data_id)
 REFERENCES rtcustomer.customer_data(customer_data_id);

UPDATE rtcustomer.customer_usage_activity t1
   SET customer_data_id=(SELECT customer_data_id FROM rtcustomer.customer_data t2 WHERE t1.ucid=t2.ucid);

ALTER TABLE rtcustomer.customer_usage_activity ALTER COLUMN customer_data_id SET NOT NULL;
 
ALTER TABLE rtcustomer.customer_utility_info ADD customer_data_id int4 NOT NULL;
ALTER TABLE rtcustomer.customer_utility_info ADD CONSTRAINT customer_utility_info_fk FOREIGN KEY (customer_data_id)
 REFERENCES rtcustomer.customer_data(customer_data_id);
 
ALTER TABLE rtcustomer.external_logs ADD customer_data_id int4 NULL;
ALTER TABLE rtcustomer.external_logs ADD brand varchar(100) NULL;
ALTER TABLE rtcustomer.external_logs ADD http_method varchar(100) NULL;
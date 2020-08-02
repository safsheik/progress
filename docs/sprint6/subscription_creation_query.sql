CREATE TABLE rtcustomer.customer_subscription (
  subscription_id BIGINT NOT NULL,
  customer_data_id int NOT NULL,
  status varchar(48) NOT NULL,
  created_date date NOT NULL,
  modified_date date NULL,
 CONSTRAINT customer_subscription_pk PRIMARY KEY (subscription_id)
);
ALTER TABLE rtcustomer.customer_subscription OWNER to "bcm";


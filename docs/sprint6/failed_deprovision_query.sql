CREATE TABLE rtcustomer.failed_deprovision (
  deprovision_id serial NOT NULL,
  ucid int8 NOT NULL,
  brand varchar(100) NOT NULL,
  pbs_message varchar(1000) NOT NULL,
  error_message varchar(1000) NOT NULL,
  created_date date NOT NULL,
  modified_date date NULL
);
ALTER TABLE rtcustomer.failed_deprovision OWNER to "bcm";
ALTER TABLE rtcustomer.customer_data ALTER COLUMN token_created_date TYPE timestamptz USING token_created_date::timestamptz;
ALTER TABLE rtcustomer.customer_data ALTER COLUMN created_date TYPE timestamptz USING created_date::timestamptz;
ALTER TABLE rtcustomer.customer_data ALTER COLUMN modified_date TYPE timestamptz USING modified_date::timestamptz;

ALTER TABLE rtcustomer.customer_usage_activity ALTER COLUMN created_date TYPE timestamptz USING created_date::timestamptz;
ALTER TABLE rtcustomer.external_logs ALTER COLUMN created_date TYPE timestamptz USING created_date::timestamptz;

ALTER TABLE rtcustomer.external_logs DROP COLUMN modified_date;

ALTER TABLE rtcustomer.webservice_logs ALTER COLUMN created_date TYPE timestamptz USING created_date::timestamptz;

ALTER TABLE rtcustomer.webservice_logs DROP COLUMN modified_date;


CREATE TABLE rtcustomer.jobs (
  job_id serial NOT NULL,
  job_name varchar(48) NOT NULL,
  brand varchar(48) NOT NULL,
  created_date timestamptz NOT NULL,
  modified_date timestamptz NULL,
  period_start_date timestamptz NULL,
  period_end_date timestamptz NULL,
 CONSTRAINT jobs_pk PRIMARY KEY (job_id)
);

CREATE TABLE rtcustomer.job_events (
  job_event_id serial NOT NULL,
  job_id int NOT NULL,
  job_event_code varchar(48) NOT NULL,
   brand varchar(48) NOT NULL,
  details varchar(48) NULL,
  event_date timestamptz NOT NULL,
  created_date timestamptz NOT NULL,
  modified_date timestamptz NULL,
  CONSTRAINT job_events_pk PRIMARY KEY (job_event_id)
);


ALTER TABLE rtcustomer.job_events ADD CONSTRAINT job_events_fk 
FOREIGN KEY (job_id) REFERENCES rtcustomer.jobs(job_id);

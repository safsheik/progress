CREATE TABLE rtcustomer.webservice_logs (
	id SERIAL,
	ucid int8 NOT NULL,
	url varchar(1000) NULL,
	http_status_code int8 NULL,
	http_method varchar(45) NULL,
	request_payload varchar(1000) NULL,
	response_payload varchar(1000) NULL,
	created_date date NOT NULL,
	modified_date date null,
PRIMARY KEY (id)
);

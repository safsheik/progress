ALTER TABLE rtcustomer.external_logs ALTER COLUMN vendor_response_payload TYPE varchar(10000) USING vendor_response_payload::varchar;
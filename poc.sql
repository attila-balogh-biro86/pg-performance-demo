CREATE TABLE cop_request_audit (
    id varchar(255) NOT NULL,
    requester_message_identification varchar(255) NULL,
    responder_message_identification varchar(255) NULL,
    assignee_bic varchar(255) NULL,
    assigner_bic varchar(255) NULL,
    client_id varchar(255) NULL,
    iban varchar(255) NULL,
    identification_type varchar(255) NULL,
    identification_value varchar(255) NULL,
    request_account_type varchar(255) NULL,
    request_id varchar(255) NULL,
    request_status varchar(255) NULL,
    request_time_received timestamp NULL,
    request_type varchar(255) NULL,
    requested_cop_ep_url varchar(255) NULL,
    requested_org_id varchar(255) NULL,
    requested_org_name varchar(255) NULL,
    response_matched bool NULL,
    response_reason_code varchar(255) NULL,
    response_time_sent timestamp NULL,
    user_id varchar(255) NULL,
    user_type varchar(255) NULL,
    scheme_response text NULL,
    "name" varchar(255) NULL,
    organisation_id_type varchar(255) NULL,
    organisation_id_value varchar(255) NULL,
    party_id_match varchar(255) NULL,
    party_name_match varchar(255) NULL,
    CONSTRAINT cop_request_audit_pkey PRIMARY KEY (id)
);


alter table cop_request_audit
add response_name varchar(255);
alter table cop_request_audit
    add scheme_response_code text;
alter table cop_request_audit
    add country varchar(255);
alter table cop_request_audit
    alter column request_status type varchar(255) using request_status::varchar(255);

alter table cop_request_audit
    add account_type varchar(255);

alter table cop_request_audit
   add verification_types varchar(255);

alter table cop_request_audit
   add batch_id varchar(50);

CREATE INDEX cop_request_audit_batch_id_idx ON cop_request_audit (batch_id);

alter table cop_request_audit
   add request_source varchar(50);

alter table cop_request_audit
   add secondary_id varchar(255);

ALTER TABLE cop_request_audit
    ADD COLUMN amount NUMERIC(15, 2),
    ADD COLUMN currency VARCHAR(255);

ALTER TABLE cop_request_audit
    ADD COLUMN transaction_ref_id VARCHAR(255);


ALTER TABLE cop_request_audit
ADD COLUMN request_time_sent timestamp NULL;


ALTER TABLE cop_request_audit
   ADD COLUMN organisation_name varchar(255);

ALTER TABLE cop_request_audit
    ADD COLUMN priority integer DEFAULT 0;

ALTER TABLE cop_request_audit
    ADD COLUMN request_headers jsonb;

ALTER TABLE cop_request_audit
    ADD COLUMN response_headers jsonb;

ALTER TABLE cop_request_audit
    ADD COLUMN response_payload jsonb;

ALTER TABLE cop_request_audit
   ADD COLUMN thumbprint varchar(255);

ALTER TABLE cop_request_audit
    ADD COLUMN payload jsonb;

ALTER TABLE cop_request_audit
    ADD COLUMN validation_error_message text;

ALTER TABLE cop_request_audit
   ADD COLUMN x_requestor_id varchar(255);

---------------------------------------------

update cop_request_audit set request_type = 'CAR'




-- Insert 500 demo transactions
-- Adjustable row count
-- Change 1500000 to any number you want
INSERT INTO cop_request_audit (
    id,
    requester_message_identification,
    responder_message_identification,
    assignee_bic,
    assigner_bic,
    client_id,
    iban,
    identification_type,
    identification_value,
    request_account_type,
    request_id,
    request_status,
    request_time_received,
    request_type,
    requested_cop_ep_url,
    requested_org_id,
    requested_org_name,
    response_matched,
    response_reason_code,
    response_time_sent,
    user_id,
    user_type,
    scheme_response,
    "name",
    organisation_id_type,
    organisation_id_value,
    party_id_match,
    party_name_match
)
SELECT
    gen_random_uuid()::text AS id,
    'REQ-' || g AS requester_message_identification,
    'RESP-' || g AS responder_message_identification,
    'ASSBIC' || (g % 1000) AS assignee_bic,
    'ASNBIC' || (g % 1000) AS assigner_bic,
    'CLIENT-' || (g % 10000) AS client_id,
    'IBAN' || g AS iban,
    'TYPE' || (g % 5) AS identification_type,
    'VAL' || g AS identification_value,
    'NATURAL_PERSON' || (g % 3) AS request_account_type,
    'REQID-' || g AS request_id,
    CASE WHEN g % 2 = 0 THEN 'SUCCESS' ELSE 'FAILURE' END AS request_status,
    NOW() - (g || ' minutes')::interval AS request_time_received,
    'CAR' AS request_type,
    'https://endpoint/' || g AS requested_cop_ep_url,
    'ORG' || (g % 500) AS requested_org_id,
    'Org Name ' || (g % 500) AS requested_org_name,
    (g % 2 = 0) AS response_matched,
    'RC' || (g % 10) AS response_reason_code,
    NOW() - (g || ' minutes')::interval AS response_time_sent,
    'USER-' || (g % 10000) AS user_id,
    CASE WHEN g % 2 = 0 THEN 'ADMIN' ELSE 'CUSTOMER' END AS user_type,
    'Response text ' || g AS scheme_response,
    'Name ' || g AS name,
    'OIDTYPE' || (g % 5) AS organisation_id_type,
    'OIDVAL' || g AS organisation_id_value,
    CASE WHEN g % 2 = 0 THEN 'MATCH' ELSE 'NO_MATCH' END AS party_id_match,
    CASE WHEN g % 2 = 0 THEN 'MATCH' ELSE 'NO_MATCH' END AS party_name_match
FROM generate_series(1, 5000000) g;



select count(*) from cop_request_audit cra


truncate table cop_request_audit_transaction_notes 
truncate table cop_request_audit cascade


-- Approximate row count
SELECT reltuples AS estimated_rows
FROM pg_class
WHERE relname = 'cop_request_audit';

SELECT n_live_tup
FROM pg_stat_all_tables
WHERE relname = 'cop_request_audit';


CREATE TABLE cop_request_audit_transaction_notes (
    "index" int4 NOT NULL,
    transaction_id varchar(255) NOT NULL,
    created_by varchar(255) NULL,
    created_date timestamp NULL,
    first_name varchar(255) NULL,
    last_name varchar(255) NULL,
    note varchar(255) NULL,
    CONSTRAINT cop_request_audit_transaction_notes_pkey PRIMARY KEY (index, transaction_id),
    CONSTRAINT fk8ou32wxsq24kp93k210t8yuuy FOREIGN KEY (transaction_id) REFERENCES cop_request_audit(id)
);




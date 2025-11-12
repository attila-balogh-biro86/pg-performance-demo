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
    response_name varchar(255) NULL,
    scheme_response_code TEXT NULL,
    country varchar(255) NULL,
    account_type varchar(255) NULL,
    verification_types varchar(255) NULL,
    batch_id varchar(50) NULL,
    request_source varchar(50) NULL,
    secondary_id varchar(255) NULL,
    amount NUMERIC(15, 2) NULL,
    currency VARCHAR(255) NULL,
    transaction_ref_id VARCHAR(255) NULL,
    request_time_sent timestamp NULL,
    organisation_name varchar(255) NULL,
    priority integer DEFAULT 0,
    request_headers jsonb NULL,
    response_headers jsonb NULL,
    response_payload jsonb NULL,
    thumbprint varchar(255) NULL,
    payload jsonb NULL,
    validation_error_message text NULL,
    x_requestor_id varchar(255) NULL, 
    CONSTRAINT cop_request_audit_pkey PRIMARY KEY (id)
);



CREATE INDEX cop_request_audit_batch_id_idx ON cop_request_audit (batch_id);
CREATE INDEX idx_tx_id ON cop_request_audit (iban, id);
CREATE INDEX idx_tx_iban_id ON cop_request_audit (iban, id);
CREATE INDEX idx_tx_bic_id  ON cop_request_audit (assigner_bic, id);
CREATE INDEX idx_tx_request_time  ON cop_request_audit (request_time_received);
-- Optional extra filters you often use:
-- CREATE INDEX idx_tx_status_date_id ON transactions (status, transaction_date, id);


---------------------------------------------

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


---------------------------------------------

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Adjust total rows here (e.g., 120000 for 5k/month)
WITH month_series AS (
    SELECT
        generate_series(
            DATE '2024-01-01',
            DATE '2025-12-01',
            INTERVAL '1 month'
        ) AS month_start
),
rows_per_month AS (
    SELECT month_start, 100000 AS rows_in_month FROM month_series  -- set per-month count
)
INSERT INTO cop_request_audit (
    id,
    requester_message_identification,
    responder_message_identification,
    assignee_bic,
    assigner_bic,
    client_id,
    iban,
    request_status,
    request_time_received,
    request_time_sent,
    response_time_sent,
    response_matched,
    response_reason_code,
    requested_org_name,
    "name",
    requested_org_id,
    requested_cop_ep_url,
    request_source,
    scheme_response,
    response_payload,
    thumbprint,
    transaction_ref_id,
    verification_types,
    organisation_name,
    payload
)
SELECT
    -- Basic identifiers
    gen_random_uuid() AS id,
    gen_random_uuid()::text AS requester_message_identification,
    gen_random_uuid()::text AS responder_message_identification,
    ('BKFC' || chr((65 + floor(random() * 26))::int) || 'PLSBSXX') AS assignee_bic,
    ('BKFC' || chr((65 + floor(random() * 26))::int) || 'PLSBSXX') AS assigner_bic,
    'NewRelicApp' AS client_id,
    ('ES' || lpad(floor(random() * 999999999999999999999999)::text, 24, '0')) AS iban,

    'SUCCESS' AS request_status,

    -- Timestamps distributed evenly by month
    m.month_start
      + (random() * (interval '1 month' - interval '1 second')) AS request_time_received,
    m.month_start
      + (random() * (interval '1 month' - interval '1 second')) AS request_time_sent,
    m.month_start
      + (random() * (interval '1 month' - interval '1 second')) AS response_time_sent,

    true AS response_matched,
    'MTCH' AS response_reason_code,
    'Soldo Financial Services Ireland DAC' AS requested_org_name,
    ('Name ' || floor(random()*10000)::text) AS "name",
    'SFSNIE22XXX' AS requested_org_id,
    'https://api.vop-sbx.banfico.io/epc-vop/responder-epc/api/vop/v1/payee-verifications' AS requested_cop_ep_url,
    'SINGLE' AS request_source,

    -- Scheme response JSON
    jsonb_build_object('PartyNameMatch', 'MTCH') AS scheme_response,

    -- Response payload JSON similar to your controller output
    jsonb_build_object(
        'verificationResponse11', jsonb_build_object(
            'data', jsonb_build_object(
                'result', jsonb_build_object(
                    'account', jsonb_build_object(
                        'id', ('ES' || lpad(floor(random()*999999999999999999999999)::text, 24, '0')),
                        'status', 'ACTIVE',
                        'nameCheckStatus', 'FULL_MATCH'
                    ),
                    'xRefId', gen_random_uuid()::text,
                    'responder', jsonb_build_object(
                        'accountVerificationScheme', 'EPC_VOP',
                        'apiVersion', 'V1.0',
                        'schemeResponseCodes', jsonb_build_array(jsonb_build_object('name', 'MTCH')),
                        'receivedTime', (now() - interval '1 day'),
                        'data', encode(convert_to('{"partyNameMatch":"MTCH"}', 'UTF8'), 'base64'),
                        'entity', jsonb_build_object(
                            'idType', 'BICFI',
                            'idValue', ('BKFC' || chr((65 + floor(random() * 26))::int) || 'PLSBSXX')
                        )
                    )
                )
            )
        )
    ) AS response_payload,

    md5(random()::text) AS thumbprint,
    gen_random_uuid()::text AS transaction_ref_id,
    jsonb_build_array('NAME') AS verification_types,
    'Soldo Financial Services Ireland DAC' AS organisation_name,

    -- Payload JSON from the request
    jsonb_build_object(
        'types', jsonb_build_array('NAME'),
        'account', jsonb_build_object(
            'name', ('Name ' || floor(random()*10000)::text),
            'idScheme', 'IBAN',
            'id', ('ES' || lpad(floor(random()*999999999999999999999999)::text, 24, '0')),
            'holdingEntity', jsonb_build_object(
                'type', 'BICFI',
                'value', ('BKFC' || chr((65 + floor(random() * 26))::int) || 'PLSBSXX')
            )
        )
    ) AS payload
FROM rows_per_month m,
     generate_series(1, m.rows_in_month);





explain analyze select count(*) from cop_request_audit cra



select count(id) from cop_request_audit cra


truncate table cop_request_audit cascade


-- Approximate row count
SELECT reltuples AS estimated_rows
FROM pg_class
WHERE relname = 'cop_request_audit';

SELECT n_live_tup
FROM pg_stat_all_tables
WHERE relname = 'cop_request_audit';


explain analyze SELECT  COUNT(*) OVER() AS full_count
FROM (
    SELECT * 
    FROM cop_request_audit
    WHERE 1 = 1
    order by id   -- required for deterministic paging
    LIMIT 10 OFFSET 0
) AS t;


explain analyze SELECT
    t.*,
    COUNT(*) OVER() AS total_count
FROM cop_request_audit t
WHERE 1 = 1
ORDER BY t.id
LIMIT 10 OFFSET 0;


-- Solution space

CREATE INDEX idx_cop_request_audit_request_time_received
    ON cop_request_audit (request_time_received);

CREATE INDEX idx_cop_request_audit_request_time_sent
    ON cop_request_audit (request_time_sent);


EXPLAIN ANALYZE
SELECT a.id, a.request_time_sent, a.response_name, a.payload, a.organisation_name,a. assignee_bic
FROM cop_request_audit a
WHERE request_time_received BETWEEN '2024-05-30' AND '2024-11-30';

ANALYZE cop_request_audit;


-----------------------


CREATE TABLE cop_request_audit_new (
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
    response_name varchar(255) NULL,
    scheme_response_code TEXT NULL,
    country varchar(255) NULL,
    account_type varchar(255) NULL,
    verification_types varchar(255) NULL,
    batch_id varchar(50) NULL,
    request_source varchar(50) NULL,
    secondary_id varchar(255) NULL,
    amount NUMERIC(15, 2) NULL,
    currency VARCHAR(255) NULL,
    transaction_ref_id VARCHAR(255) NULL,
    request_time_sent timestamp NULL,
    organisation_name varchar(255) NULL,
    priority integer DEFAULT 0,
    request_headers jsonb NULL,
    response_headers jsonb NULL,
    response_payload jsonb NULL,
    thumbprint varchar(255) NULL,
    payload jsonb NULL,
    validation_error_message text NULL,
    x_requestor_id varchar(255) NULL, 
    CONSTRAINT cop_request_audit_pkey_new PRIMARY KEY (id, request_time_received)
) PARTITION BY RANGE (request_time_received);

--------------------- create partitions
DO $$
DECLARE
    d date := DATE '2024-01-01';
    end_date date := DATE '2026-01-01';  -- upper bound (exclusive)
    tbl text;
BEGIN
    WHILE d < end_date LOOP
        tbl := format('cop_request_audit_%s', to_char(d, 'YYYY_MM'));
        EXECUTE format(
            'CREATE TABLE %I PARTITION OF cop_request_audit_new
             FOR VALUES FROM (%L) TO (%L);',
            tbl,
            d,
            (d + INTERVAL '1 month')::date
        );
        d := d + INTERVAL '1 month';
    END LOOP;
END $$;
-------------------- Add indexes

DO $$
DECLARE
    part text;
BEGIN
    FOR part IN
        SELECT inhrelid::regclass::text
        FROM pg_inherits
        WHERE inhparent = 'cop_request_audit_new'::regclass
    LOOP
        EXECUTE format(
            'CREATE INDEX %I ON %s (request_time_received);',
            'idx_' || part || '_req_time_received',
            part
        );
    END LOOP;
END $$;

-------------------- Insert data into the new table

INSERT INTO cop_request_audit_new
SELECT * FROM cop_request_audit;


-------------------- Swap the tables

BEGIN;
ALTER TABLE cop_request_audit RENAME TO cop_request_audit_old;
ALTER TABLE cop_request_audit_new RENAME TO cop_request_audit;
COMMIT;

--------------------- Refresh planner statistics --------------
ANALYZE cop_request_audit;

--------------------- Test the partitioning--------------------

EXPLAIN ANALYZE
SELECT count(*)
FROM cop_request_audit
WHERE request_time_received >= '2024-06-01'
  AND request_time_received < '2024-07-01';


EXPLAIN ANALYZE
SELECT *
FROM cop_request_audit;


EXPLAIN ANALYZE
SELECT a.id, a.request_time_sent, a.response_name, a.payload, a.organisation_name, a.assignee_bic
FROM cop_request_audit a
WHERE request_time_received >= '2024-01-01'
  AND request_time_received < '2024-07-31';


EXPLAIN ANALYZE
SELECT
    ra1_0.id,
    ra1_0.organisation_name,
    ra1_0.requester_message_identification,
    ra1_0.responder_message_identification,
    ra1_0.account_type,
    ra1_0.amount,
    ra1_0.assignee_bic,
    ra1_0.assigner_bic,
    ra1_0.batch_id,
    ra1_0.client_id,
    ra1_0.currency,
    ra1_0.iban,
    ra1_0.identification_type,
    ra1_0.identification_value,
    ra1_0.name,
    ra1_0.organisation_id_type,
    ra1_0.organisation_id_value,
    ra1_0.priority,
    ra1_0.request_account_type,
    ra1_0.request_headers,
    ra1_0.request_id,
    ra1_0.request_source,
    ra1_0.request_status,
    ra1_0.request_time_received,
    ra1_0.request_time_sent,
    ra1_0.request_type,
    ra1_0.requested_cop_ep_url,
    ra1_0.requested_org_id,
    ra1_0.requested_org_name,
    ra1_0.response_headers,
    ra1_0.response_matched,
    ra1_0.response_name,
    ra1_0.response_reason_code,
    ra1_0.response_time_sent,
    ra1_0.scheme_response_code,
    ra1_0.scheme_response,
    ra1_0.secondary_id,
    ra1_0.thumbprint,
    ra1_0.transaction_ref_id,
    ra1_0.user_id,
    ra1_0.user_type,
    ra1_0.validation_error_message,
    ra1_0.x_requestor_id 
FROM
    cop_request_audit ra1_0 
WHERE
    1=1 
ORDER BY
    ra1_0.request_time_received desc 
OFFSET
    10 rows 
FETCH 
    FIRST 10 ROWS ONLY

----------------------- Script for creating new partitions automatically------------

CREATE OR REPLACE PROCEDURE create_next_month_partition()
LANGUAGE plpgsql
AS $$
DECLARE
    next_month date := date_trunc('month', now()) + INTERVAL '1 month';
    next_table text := format('cop_request_audit_%s', to_char(next_month, 'YYYY_MM'));
BEGIN
    EXECUTE format(
        'CREATE TABLE IF NOT EXISTS %I PARTITION OF cop_request_audit
         FOR VALUES FROM (%L) TO (%L);',
        next_table,
        next_month::date,
        (next_month + INTERVAL '1 month')::date
    );
END $$;

----------------------- Summary -------------------------------

1. Revert back the service to use pagination.
2. Change the implementation to use a custom query, not select all the fields
3. Implement a script which partitioning the table, move the data into the newly created partitions (composite index must)
4. Ask or implement a script which automatically creates new partitions in the future








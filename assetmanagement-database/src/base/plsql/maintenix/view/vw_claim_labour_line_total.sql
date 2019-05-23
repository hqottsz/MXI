--liquibase formatted sql


--changeSet vw_claim_labour_line_total:1 stripComments:false
CREATE OR REPLACE VIEW VW_CLAIM_LABOUR_LINE_TOTAL
(
	claim_db_id,
	claim_id,
	total_unit_price,
	total_line_price
)
AS
SELECT
	claim.claim_db_id,
	claim.claim_id,
	SUM(NVL(claim_labour_line.unit_price, 0)) AS total_unit_price,
	SUM(NVL(claim_labour_line.line_price, 0)) AS total_line_price
FROM
	claim
	LEFT OUTER JOIN claim_labour_line ON claim.claim_db_id = claim_labour_line.claim_db_id AND claim.claim_id = claim_labour_line.claim_id
GROUP BY claim.claim_db_id, claim.claim_id;
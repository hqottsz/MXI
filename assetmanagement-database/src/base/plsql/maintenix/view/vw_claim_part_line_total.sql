--liquibase formatted sql


--changeSet vw_claim_part_line_total:1 stripComments:false
CREATE OR REPLACE VIEW VW_CLAIM_PART_LINE_TOTAL
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
	SUM(NVL(claim_part_line.unit_price, 0)) AS total_unit_price,
	SUM(NVL(claim_part_line.line_price, 0)) AS total_line_price
FROM
	claim
	LEFT OUTER JOIN claim_part_line ON claim.claim_db_id = claim_part_line.claim_db_id AND claim.claim_id = claim_part_line.claim_id
GROUP BY claim.claim_db_id, claim.claim_id;
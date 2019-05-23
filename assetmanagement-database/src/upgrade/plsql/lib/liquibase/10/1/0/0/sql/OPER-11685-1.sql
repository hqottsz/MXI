--liquibase formatted sql

--changeSet OPER-11685:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE DEADLINE_PKG IS
-- Existing dealine logic still in prep_deadline_pkg
-- This new deadline pkg only contains newly added sp to recude using of deadline job

PROCEDURE updateTaskDeadlineRemainUsage (
   ai_inv_db_id   IN NUMBER,
   ai_inv_id      IN NUMBER
);

END DEADLINE_PKG;
/

--liquibase formatted sql

--changeSet OPER-1071-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment mt_inv_ac_auth_inv_inv_pkg spec definition
CREATE OR REPLACE PACKAGE mt_inv_ac_auth_inv_inv_pkg AS
/********************************************************************************
*
* Package:     mt_inv_ac_auth_inv_inv_pkg
*
* Description:  This package is used to build a log of events that need to be
*				processed for the datamart associated with this package. It defines
*				tables to hold records. For simplicity and speed there are
*				only inserts or deletes.
*
*				action is a simple char that represents i: insert, u:update
*				d:delete statement
*********************************************************************************/
  TYPE gt_inv_rec IS RECORD (
								inv_no_db_id       NUMBER,
								inv_no_id          NUMBER,
								authority_db_id    NUMBER,
								authority_id       NUMBER,
								hr_db_id           NUMBER,
								hr_id              NUMBER,
								all_authority_bool NUMBER,
								locked_bool        NUMBER,
								action             CHAR (1) --iud
							);

  TYPE gt_inv_rec_tab IS TABLE OF gt_inv_rec;

  gtab_inv_ins                          gt_inv_rec_tab;
  gtab_inv_del                          gt_inv_rec_tab;


  gi_ins                                INTEGER := 1;
  gi_del                                INTEGER := 1;
  gi_stream_mode                        INTEGER := 1;

  PROCEDURE    post_update (irec IN gt_inv_rec);
  PROCEDURE    stream_update;
  PROCEDURE    populate_data;
  PROCEDURE	   stream_update_inv_inv;
  PROCEDURE    stream_update_org_hr;
  PROCEDURE    stream_update_org_hr_auth;

END mt_inv_ac_auth_inv_inv_pkg;
/


--changeSet OPER-1071-2:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment mt_inv_ac_auth_inv_inv_pkg body definition
CREATE OR REPLACE PACKAGE BODY mt_inv_ac_auth_inv_inv_pkg AS
/********************************************************************************
*
* Procedure:    populate_data
*
* Arguments:	None - This procedure populates the associated datamart.
*
* Return: N/A
*
* Description:  This procedure does simple truncate and re-insert of data into the
*				datamart. Expected in cases where triggers are disabled for any
*				reason and need to repopulate with fresh data.
********************************************************************************/
    PROCEDURE populate_data IS
    BEGIN
        EXECUTE IMMEDIATE 'TRUNCATE TABLE mt_inv_ac_authority';

    INSERT /*+ APPEND */ INTO mt_inv_ac_authority
		SELECT
		   inv_inv.inv_no_db_id,
		   inv_inv.inv_no_id,
		   org_hr.hr_db_id,
		   org_hr.hr_id,
		   inv_inv.authority_db_id,
		   inv_inv.authority_id
		FROM
		   inv_ac_reg
		   INNER JOIN inv_inv ON
		      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
		      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
		   INNER JOIN org_hr_authority ON
		      org_hr_authority.authority_db_id = inv_inv.authority_db_id AND
		      org_hr_authority.authority_id    = inv_inv.authority_id
		   INNER JOIN org_hr ON
		      org_hr.hr_db_id = org_hr_authority.hr_db_id AND
		      org_hr.hr_id    = org_hr_authority.hr_id
		WHERE
		   inv_inv.inv_class_db_id = 0 AND
		   inv_inv.inv_class_cd    = 'ACFT' AND
           inv_inv.locked_bool     = 0
		   AND
		   org_hr.all_authority_bool = 0
		UNION ALL
		-- inventory with no authority, all hr have authority (exclude those with all_authority_bool)
		SELECT
		   inv_inv.inv_no_db_id,
		   inv_inv.inv_no_id,
		   org_hr.hr_db_id,
		   org_hr.hr_id,
		   NULL AS authority_db_id,
		   NULL AS authority_id
		FROM
		   inv_ac_reg
		   INNER JOIN inv_inv ON
		      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
		      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
		   CROSS JOIN org_hr
		WHERE
		   inv_inv.inv_class_db_id = 0 AND
		   inv_inv.inv_class_cd    = 'ACFT' AND
           inv_inv.locked_bool     = 0
		   AND
		   inv_inv.authority_db_id IS NULL
		   AND
		   org_hr.all_authority_bool = 0
		UNION ALL
		-- hr with all authority have authority over all aircraft
		SELECT
		   inv_inv.inv_no_db_id,
		   inv_inv.inv_no_id,
		   org_hr.hr_db_id,
		   org_hr.hr_id,
		   inv_inv.authority_db_id,
		   inv_inv.authority_id
		FROM
		   inv_ac_reg
		   INNER JOIN inv_inv ON
		      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
		      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
		   CROSS JOIN org_hr
		WHERE
		   inv_inv.inv_class_db_id = 0 AND
		   inv_inv.inv_class_cd    = 'ACFT' AND
           inv_inv.locked_bool     = 0
		   AND
		   org_hr.all_authority_bool = 1
        ;
        COMMIT;
    END populate_data;

/********************************************************************************
*
* Procedure:    reset_collections
* Arguments:	N/A
*
* Return: N/A
*
* Description:  Essentially garbage collection/cleanup procedure internal to pkg
********************************************************************************/

  PROCEDURE reset_collections AS
  BEGIN
       gtab_inv_ins.DELETE;
       gtab_inv_del.DELETE;
       gi_ins :=  1;
	   gi_del :=  1;

       RETURN;
  END;

/********************************************************************************
*
* Procedure:    post_update
* Arguments:	gt_inv_rec			iRec - The record of event to be processed.
*											Defined in package header.
*
*
* Return: N/A
*
*
* Description:  post_update is called after a ROW trigger is fired. It then builds
*				a log of the future operations to execute during streaming phase.
*				This allows any number of row operations to occur with a final
*				execution to occur in the stream_update.
********************************************************************************/

  PROCEDURE    post_update (irec IN gt_inv_rec) AS

	BEGIN
		IF (irec.action = 'I')
		THEN
			IF (NOT gtab_inv_ins.EXISTS (gi_ins))
			THEN
				gtab_inv_ins.extend ();
			END IF;

			gtab_inv_ins (gi_ins) := irec;
			gi_ins := gi_ins + 1;

			RETURN;
		END IF;

		IF (irec.action = 'D')
		THEN
			IF (NOT gtab_inv_del.EXISTS (gi_del))
			THEN
				gtab_inv_del.extend ();
			END IF;

			gtab_inv_del (gi_del) := irec;
			gi_del := gi_del + 1;

			RETURN;
		END IF;

		IF (irec.action = 'U')
		THEN
			IF (NOT gtab_inv_del.EXISTS (gi_del))
			THEN
				gtab_inv_del.extend ();
			END IF;

			gtab_inv_del (gi_del) := irec;
			gi_del := gi_del + 1;


			IF (NOT gtab_inv_ins.EXISTS (gi_ins))
			THEN
				gtab_inv_ins.extend ();
			END IF;

			gtab_inv_ins (gi_ins) := irec;
			gi_ins := gi_ins + 1;

			RETURN;
		END IF;
    EXCEPTION
       WHEN OTHERS THEN
           		  reset_collections;
                  RAISE;
	END post_update;

/********************************************************************************
*
* Procedure:    stream_update
*
* Arguments:	None - This procedure processes N insert and delete records for
*						the associated datamart.
*
*
* Return: N/A
*
*
* Description:  stream_update is called after a TABLE trigger is fired. depending
*				on the mode (determined by source table trigger) the entries in
*				the gtab_inv_ins, gtab_inv_del table objects are processed by each
*				internal pkg procedure to handle them.
*
*				Finally it flushes the log of the events.
********************************************************************************/
  PROCEDURE    stream_update AS

  BEGIN

       IF (gi_stream_mode = 1)
       THEN
            stream_update_inv_inv;
            RETURN;
       END IF;

       IF (gi_stream_mode = 2)
       THEN
            stream_update_org_hr;
            RETURN;
       END IF;

       IF (gi_stream_mode = 3)
       THEN
            stream_update_org_hr_auth;
            RETURN;
       END IF;

       RETURN;

   EXCEPTION
       WHEN OTHERS THEN
               reset_collections;

               RAISE;

  END stream_update;


/********************************************************************************
*
* Procedure:    stream_update_inv_inv
*
* Arguments:	None - This procedure processes N insert and delete records for
*						the associated datamart.
*
*
* Return: N/A
*
*
* Description:  stream_update_inv_inv is called after a TABLE trigger is fired. depending
*				on the mode (determined by source table trigger) the entries in
*				the gtab_inv_ins, gtab_inv_del table objects are processed by each
*				internal pkg procedure to handle them.
*
*				Finally it flushes the log of the events.
********************************************************************************/
PROCEDURE    stream_update_inv_inv AS


       le_ex_dml_errors EXCEPTION;
       PRAGMA exception_init(le_ex_dml_errors, -24381);

       li_exi		INTEGER;

  BEGIN
/********************************************************************************
*
*	Conduct DELETE operations
*
********************************************************************************/
       BEGIN
               FORALL  li_i IN 1..gi_del -1 SAVE EXCEPTIONS
	               DELETE FROM mt_inv_ac_authority
	               WHERE mt_inv_ac_authority.inv_no_db_id = gtab_inv_del(li_i).inv_no_db_id AND
	               		 mt_inv_ac_authority.inv_no_id    = gtab_inv_del(li_i).inv_no_id
               ;

      EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
                               raise_application_error (
                                                           SQL%bulk_exceptions(li_exi).error_code,
                                                           sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                        );
                       END IF;
               END LOOP;
      END;
/********************************************************************************
*
*	Conduct INSERT operations
*
********************************************************************************/
       BEGIN

               FORALL  li_i IN 1..gi_ins -1  SAVE EXCEPTIONS
					INSERT INTO mt_inv_ac_authority
					SELECT
						gtab_inv_ins(li_i).inv_no_db_id,
						gtab_inv_ins(li_i).inv_no_id,
						org_hr.hr_db_id,
						org_hr.hr_id,
						gtab_inv_ins(li_i).authority_db_id,
						gtab_inv_ins(li_i).authority_id
					FROM
						org_hr_authority

					INNER JOIN org_hr ON
						org_hr.hr_db_id = org_hr_authority.hr_db_id AND
						org_hr.hr_id    = org_hr_authority.hr_id
					WHERE
						org_hr.all_authority_bool        = 0 AND
						org_hr_authority.authority_db_id = gtab_inv_ins(li_i).authority_db_id AND
						org_hr_authority.authority_id    = gtab_inv_ins(li_i).authority_id
                        AND
                        gtab_inv_ins(li_i).locked_bool = 0
               ;

               EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
                               raise_application_error (
                                                           SQL%bulk_exceptions(li_exi).error_code,
                                                           sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                        );
                       END IF;
               END LOOP;
       END;


       BEGIN
               FORALL  li_i IN 1..gi_ins -1  SAVE EXCEPTIONS
                       INSERT INTO mt_inv_ac_authority
                       SELECT
                                gtab_inv_ins(li_i).inv_no_db_id,
                                gtab_inv_ins(li_i).inv_no_id,
                                org_hr.hr_db_id,
                                org_hr.hr_id,
                                NULL,
                                NULL
                       FROM
                               org_hr
                       WHERE
                              gtab_inv_ins(li_i).authority_db_id IS NULL AND
                              org_hr.all_authority_bool = 0
                              AND
                              gtab_inv_ins(li_i).locked_bool = 0
                              ;
       EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
                               raise_application_error (
                                                           SQL%bulk_exceptions(li_exi).error_code,
                                                           sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                        );
                       END IF;
               END LOOP;
       END;

       BEGIN
               FORALL  li_i IN 1..gi_ins -1  SAVE EXCEPTIONS
                       INSERT INTO mt_inv_ac_authority
                       SELECT
                            gtab_inv_ins(li_i).inv_no_db_id,
                            gtab_inv_ins(li_i).inv_no_id,
                            org_hr.hr_db_id,
                            org_hr.hr_id,
                            gtab_inv_ins(li_i).authority_db_id,
                            gtab_inv_ins(li_i).authority_id
                       FROM
                            org_hr
                       WHERE
                            org_hr.all_authority_bool = 1
                            AND
                            gtab_inv_ins(li_i).locked_bool = 0
               ;

        EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
                               raise_application_error (
                                                           SQL%bulk_exceptions(li_exi).error_code,
                                                           sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                        );
                       END IF;
               END LOOP;
       END;
/****************************************************
*
*	Flush the log
*
*****************************************************/
	reset_collections;

    EXCEPTION
       WHEN OTHERS THEN
           		  reset_collections;
                  RAISE;
  END stream_update_inv_inv;

/********************************************************************************
*
* Procedure:    stream_update_org_hr
*
* Arguments:	None - This procedure processes N insert and delete records for
*						the associated datamart.
*
*
* Return: N/A
*
*
* Description:  stream_update_org_hr is called after a TABLE trigger is fired. depending
*				on the mode (determined by source table trigger) the entries in
*				the gtab_inv_ins, gtab_inv_del table objects are processed by each
*				internal pkg procedure to handle them.
*
*				Finally it flushes the log of the events.
********************************************************************************/
	  PROCEDURE   stream_update_org_hr IS

       le_ex_dml_errors EXCEPTION;
       PRAGMA exception_init(le_ex_dml_errors, -24381);

       li_exi                                        INTEGER;

  BEGIN
/********************************************************************************
*
*	Conduct DELETE operations
*
********************************************************************************/
		BEGIN
		FORALL  li_i IN 1..gi_del -1 SAVE EXCEPTIONS

			DELETE FROM mt_inv_ac_authority
			WHERE mt_inv_ac_authority.hr_db_id = gtab_inv_del(li_i).hr_db_id
			AND   mt_inv_ac_authority.hr_id    = gtab_inv_del(li_i).hr_id
               ;
      EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
						   raise_application_error (
													   SQL%bulk_exceptions(li_exi).error_code,
													   sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
													);
                       END IF;
               END LOOP;
      END;
/********************************************************************************
*
*	Conduct INSERT operations
*
********************************************************************************/
      BEGIN
               FORALL  li_i IN 1..gi_ins -1  SAVE EXCEPTIONS
                       INSERT INTO mt_inv_ac_authority
						SELECT
						   inv_inv.inv_no_db_id,
						   inv_inv.inv_no_id,
						   gtab_inv_ins(li_i).hr_db_id,
						   gtab_inv_ins(li_i).hr_id,
						   inv_inv.authority_db_id,
						   inv_inv.authority_id
						FROM
						   inv_ac_reg
						   INNER JOIN inv_inv ON
						      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
						      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
						   INNER JOIN org_hr_authority ON
						      org_hr_authority.authority_db_id = inv_inv.authority_db_id AND
						      org_hr_authority.authority_id    = inv_inv.authority_id
						WHERE
						   inv_inv.inv_class_db_id = 0 AND
						   inv_inv.inv_class_cd    = 'ACFT' AND
                           inv_inv.locked_bool     = 0
						   AND
						   gtab_inv_ins(li_i).hr_db_id = org_hr_authority.hr_db_id AND
						   gtab_inv_ins(li_i).hr_id    = org_hr_authority.hr_id
						   AND
						   gtab_inv_ins(li_i).all_authority_bool = 0
                       ;
       EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
                               raise_application_error (
                                                           SQL%bulk_exceptions(li_exi).error_code,
                                                           sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                        );
                       END IF;
               END LOOP;
       END;


      BEGIN
               FORALL  li_i IN 1..gi_ins -1  SAVE EXCEPTIONS
                       INSERT INTO mt_inv_ac_authority
						SELECT
						   inv_inv.inv_no_db_id,
						   inv_inv.inv_no_id,
						   gtab_inv_ins(li_i).hr_db_id,
						   gtab_inv_ins(li_i).hr_id,
						   NULL AS authority_db_id,
						   NULL AS authority_id
						FROM
						   inv_ac_reg
						   INNER JOIN inv_inv ON
						      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
						      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
						WHERE
						   inv_inv.inv_class_db_id = 0 AND
						   inv_inv.inv_class_cd    = 'ACFT' AND
                           inv_inv.locked_bool     = 0
						   AND
						   inv_inv.authority_db_id IS NULL
						   AND
						   gtab_inv_ins(li_i).all_authority_bool = 0
                       ;
       EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
                               raise_application_error (
                                                           SQL%bulk_exceptions(li_exi).error_code,
                                                           sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                        );
                       END IF;
               END LOOP;
       END;

       BEGIN
               FORALL  li_i IN 1..gi_ins -1  SAVE EXCEPTIONS
                       INSERT INTO mt_inv_ac_authority
						SELECT
						   inv_inv.inv_no_db_id,
						   inv_inv.inv_no_id,
						   gtab_inv_ins(li_i).hr_db_id,
						   gtab_inv_ins(li_i).hr_id,
						   inv_inv.authority_db_id,
						   inv_inv.authority_id
						FROM
						   inv_ac_reg
						   INNER JOIN inv_inv ON
						      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
						      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
						WHERE
						   inv_inv.inv_class_db_id = 0 AND
						   inv_inv.inv_class_cd    = 'ACFT' AND
                           inv_inv.locked_bool     = 0
						   AND
						   gtab_inv_ins(li_i).all_authority_bool = 1
               ;
        EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
                               raise_application_error (
                                                           SQL%bulk_exceptions(li_exi).error_code,
                                                           sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                        );
                       END IF;
               END LOOP;
       END;
/****************************************************
*
*	Flush the log
*
*****************************************************/
	reset_collections;

    EXCEPTION
       WHEN OTHERS THEN
           		  reset_collections;
                  RAISE;
END stream_update_org_hr;


/********************************************************************************
*
* Procedure:    stream_update_org_hr_auth
*
* Arguments:	None - This procedure processes N insert and delete records for
*						the associated datamart.
*
*
* Return: N/A
*
*
* Description:  stream_update_org_hr_auth is called after a TABLE trigger is fired. depending
*				on the mode (determined by source table trigger) the entries in
*				the gtab_inv_ins, gtab_inv_del table objects are processed by each
*				internal pkg procedure to handle them.
*
*				Finally it flushes the log of the events.
********************************************************************************/
  PROCEDURE   stream_update_org_hr_auth IS

       le_ex_dml_errors EXCEPTION;
       PRAGMA exception_init(le_ex_dml_errors, -24381);

       li_exi		INTEGER;

  BEGIN
/********************************************************************************
*
*	Conduct DELETE operations
*
********************************************************************************/
       BEGIN
		   FORALL  li_i IN 1..gi_del -1 SAVE EXCEPTIONS

			DELETE FROM mt_inv_ac_authority
			WHERE mt_inv_ac_authority.hr_db_id        = gtab_inv_del(li_i).hr_db_id
			AND   mt_inv_ac_authority.hr_id           = gtab_inv_del(li_i).hr_id
			AND   mt_inv_ac_authority.authority_db_id = gtab_inv_del(li_i).authority_db_id
			AND   mt_inv_ac_authority.authority_id    = gtab_inv_del(li_i).authority_id
		   ;
		EXCEPTION
		WHEN le_ex_dml_errors THEN
		FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
		LOOP
		   IF (SQL%bulk_exceptions(li_exi).error_code != 1)
		   THEN
			   raise_application_error (
								   SQL%bulk_exceptions(li_exi).error_code,
								   sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
								   );
		   END IF;
		END LOOP;
      END;
/********************************************************************************
*
*	Conduct INSERT operations
*
********************************************************************************/
      BEGIN
               FORALL  li_i IN 1..gi_ins -1  SAVE EXCEPTIONS
                       INSERT INTO mt_inv_ac_authority
                       SELECT
                               inv_inv.inv_no_db_id,
                               inv_inv.inv_no_id,
                               gtab_inv_ins(li_i).hr_db_id,
                               gtab_inv_ins(li_i).hr_id,
                               gtab_inv_ins(li_i).authority_db_id,
                               gtab_inv_ins(li_i).authority_id
                       FROM
                              inv_inv

                       WHERE
                             gtab_inv_ins(li_i).authority_db_id = inv_inv.authority_db_id AND
                             gtab_inv_ins(li_i).authority_id    = inv_inv.authority_id    AND
                             inv_inv.inv_class_db_id = 0                                  AND
                             inv_inv.inv_class_cd    = 'ACFT'                             AND
                             inv_inv.locked_bool     = 0                                  AND
                             EXISTS (SELECT NULL
                                     FROM org_hr
                                     WHERE org_hr.hr_db_id = gtab_inv_ins(li_i).hr_db_id
                                     AND   org_hr.hr_id    = gtab_inv_ins(li_i).hr_id
                                     AND   org_hr.all_authority_bool = 0
                                    )
                       ;
       EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
                               raise_application_error (
                                                           SQL%bulk_exceptions(li_exi).error_code,
                                                           sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                        );
                       END IF;
               END LOOP;
       END;

       BEGIN
               FORALL  li_i IN 1..gi_ins -1  SAVE EXCEPTIONS
                       INSERT INTO mt_inv_ac_authority
                       SELECT
                               inv_inv.inv_no_db_id,
                               inv_inv.inv_no_id,
                               gtab_inv_ins(li_i).hr_db_id,
                               gtab_inv_ins(li_i).hr_id,
                               gtab_inv_ins(li_i).authority_db_id,
                               gtab_inv_ins(li_i).authority_id

                       FROM
                              inv_inv,
                              org_hr
                       WHERE
                             gtab_inv_ins(li_i).authority_db_id = inv_inv.authority_db_id AND
                             gtab_inv_ins(li_i).authority_id    = inv_inv.authority_id    AND
                             inv_inv.inv_class_db_id = 0                                  AND
                             inv_inv.inv_class_cd    = 'ACFT'                             AND
                             inv_inv.locked_bool     = 0                                  AND
                             org_hr.hr_db_id = gtab_inv_ins(li_i).hr_db_id                AND
                             org_hr.hr_id    = gtab_inv_ins(li_i).hr_id                   AND
                             org_hr.all_authority_bool = 1
                       ;
       EXCEPTION
               WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
               LOOP
                       IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                       THEN
                               raise_application_error (
                                                           SQL%bulk_exceptions(li_exi).error_code,
                                                           sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                        );
                       END IF;
               END LOOP;
       END;
/****************************************************
*
*	Flush the log
*
*****************************************************/
	reset_collections;

    EXCEPTION
       WHEN OTHERS THEN
           		  reset_collections;
                  RAISE;
  END   stream_update_org_hr_auth;

/****************************************************
*
*	Initialization of global record tab
*
*****************************************************/
BEGIN

  gtab_inv_ins     :=  gt_inv_rec_tab ();
  gtab_inv_del     :=  gt_inv_rec_tab ();


END mt_inv_ac_auth_inv_inv_pkg;
/

--changeSet OPER-1071-2:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment fill MT_INV_AC_AUTHORITY datamart with data
BEGIN
	mt_inv_ac_auth_inv_inv_pkg.populate_data;
END;
/

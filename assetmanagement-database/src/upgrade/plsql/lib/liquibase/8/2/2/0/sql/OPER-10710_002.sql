--liquibase formatted sql


--changeSet OPER-10710:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY utl_sequence_pkg IS
   ----------------------------------------------------------------------------
   -- Object Name : utl_sequence_pkg
   -- Object Type : Package Body
   -- Description : This is the sequence package containing the methods for
   --               managing the sequences in the utl_sequences table.
   ----------------------------------------------------------------------------
   -- Copyright 2017 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Instance variables
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------


   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Private variables
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   kv_pkg_name CONSTANT VARCHAR2(30) := 'utl_sequence_pkg';

   lv_err_msg     VARCHAR2(2000);
   lv_err_code    VARCHAR2(200);
   lv_method_name VARCHAR2(30);

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Private Method Bodies
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------



   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Public method bodies
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   -----------------------------------------------------------------------------
   -- Procedure:   sync_sequences
   -- Arguments:
   -- Description:
   --    Synchronize all sequences from utl_sequences that meet the following
   --    conditions:
   --    1. Sequence name is valid (join to user_sequences)
   --    2. Table_name and column_name is valid (join to user_tab_columns)
   --    3. Sequence is flagged as an Oracle sequence (oracle_seq = 1)
   --    4. Sequence increments by 1 (skip sequences that decend or increment
   --       in steps)
   --    5. Column data_type is NUMBER (skip sequences used to build
   --       alphanumeric values)
   --    6. Sequence name is not in dpo_seq_tracking if db_type_code is
   --       OPER (Deployed Operations)
   --
   --    If the above conditions are met then determine the largest data value
   --    in the related column and get the next_value from utl_sequence.  Use
   --    the larger of these two values to update the sequence.  The next_value
   --    from utl_sequence allows us to set a minimum starting point for the
   --    sequence.
   -----------------------------------------------------------------------------
   PROCEDURE sync_sequences IS

      CURSOR lcur_seq_info IS
         SELECT
            user_sequences.sequence_name,
            user_sequences.cache_size,
            user_sequences.increment_by,
            utl_sequence.next_value,
            utl_sequence.table_name,
            utl_sequence.column_name
         FROM
            utl_sequence
            INNER JOIN user_sequences ON
               user_sequences.sequence_name = utl_sequence.sequence_cd
            INNER JOIN user_tab_columns ON
               user_tab_columns.table_name = utl_sequence.table_name    AND
               user_tab_columns.column_name = utl_sequence.column_name
         WHERE
            utl_sequence.oracle_seq = 1            AND
            user_sequences.increment_by = 1        AND
            user_tab_columns.data_type = 'NUMBER'
         ORDER BY
            user_sequences.sequence_name;

      ln_step            NUMBER(4);
      kv_dbo_seq_table   CONSTANT VARCHAR2(30) := 'DPO_SEQ_TRACKING';
      lv_db_type_cd      mim_db.db_type_cd%TYPE;
      ln_seq_val         NUMBER;
      ln_difference      NUMBER;
      ln_seq_max         NUMBER;
      ln_data_max        NUMBER;
      ln_valid_count     NUMBER;
      ln_resync_count    NUMBER;
      lb_chk_for_dpo_seq BOOLEAN;
      ln_dpo_tab_exists  NUMBER;
      ln_dpo_seq_count   NUMBER;
      lb_skip_sequence   BOOLEAN;
      ln_skip_count      NUMBER;
      lb_set_minimum     BOOLEAN;

   BEGIN

      -- Initialize the count variables
      ln_step := 10;
      ln_valid_count := 0;
      ln_resync_count := 0;
      ln_skip_count := 0;

      -- Reset the lb_chk_for_dpo_seq flag to false
      ln_step := 20;
      lb_chk_for_dpo_seq := FALSE;

      BEGIN

         -- Get the db_type_cd for the local db_id
         ln_step := 30;
         SELECT
            UPPER(db_type_cd)
         INTO
            lv_db_type_cd
         FROM
            mim_local_db
            INNER JOIN mim_db ON
               mim_local_db.db_id = mim_db.db_id;

      EXCEPTION
         WHEN NO_DATA_FOUND THEN

            -- If the db_type_cd is NULL (base db) then use SYS as the default
            ln_step := 40;
            lv_db_type_cd := 'SYS';

      END;

      -- If the db_type_cd is OPER then continue to process for Deployed Operations
      IF lv_db_type_cd = 'OPER'
      THEN

         -- Check if the DPO_SEQ_TRACKING table exists.
         ln_step := 50;
         SELECT
            COUNT(*)
         INTO
            ln_dpo_tab_exists
         FROM
            user_objects
         WHERE
            object_name = kv_dbo_seq_table  AND
            object_type = 'TABLE';

         IF ln_dpo_tab_exists > 0
         THEN

            -- The db_type_dc is OPER and the DPO_SEQ_TRACKING table exists,
            -- therefore, set the lb_chk_for_dpo_seq flag to TRUE.
            ln_step := 60;
            lb_chk_for_dpo_seq := TRUE;

         END IF;

      END IF;


      -- Loop through each sequence
      FOR lrec_seq_info IN lcur_seq_info
      LOOP

         -- Reset the lb_skip_sequence flag to false.
         ln_step := 70;
         lb_skip_sequence := FALSE;

         -- Reset the lb_set_minimum flag to false.
         ln_step := 80;
         lb_set_minimum := FALSE;

         -- If the lb_chk_for_dpo_seq flag is true then determine if this sequence should be skipped or not
         IF lb_chk_for_dpo_seq = TRUE
         THEN

            -- Check if the current sequence is in the DPO_SEQ_TRACKING tracking table
            ln_step := 90;
            EXECUTE IMMEDIATE 'SELECT COUNT(*) FROM ' || kv_dbo_seq_table || ' WHERE sequence_cd = ''' || lrec_seq_info.sequence_name || '''' INTO ln_dpo_seq_count;

            IF ln_dpo_seq_count > 0
            THEN
               -- The current sequence is in the DPO_SEQ_TRACKING tracking table and this is a DPO operations site,
               -- therefore, set the lb_skip_sequence flag to true to avoid processing this sequence.
               ln_step := 100;
               lb_skip_sequence := TRUE;
            END IF;

         END IF;

         -- Check if this sequence should be skipped.
         IF lb_skip_sequence = FALSE
         THEN

            -- The lb_skip_sequence flag is FALSE, therefore, resync the current sequence.

            -- Reset the difference variable
            ln_step := 110;
            ln_difference := 0;

            -- Temporarily disable caching for this sequence to get the true last_number for the sequence.
            -- This step is necessary because when caching is on then the last_number may include values
            -- that have been cached even though they may not have been issued yet by the sequence.
            ln_step := 120;
            EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || lrec_seq_info.sequence_name || ' NOCACHE';

            -- Get the true last_number that has been issued by the sequence now that caching is disabled.
            -- Subtract one because the last_number column actually displays the next number that will be issued.
            ln_step := 130;
            SELECT
               last_number - 1
            INTO
               ln_seq_max
            FROM
               user_sequences
            WHERE
               sequence_name = lrec_seq_info.sequence_name;

            -- Get the largest value from the table and column specified in the utl_sequence table.
            ln_step := 140;
            EXECUTE IMMEDIATE 'SELECT NVL(MAX(' || lrec_seq_info.column_name || '),0) FROM ' || lrec_seq_info.table_name INTO ln_data_max;

            -- If the maximum column value is less than the next_value from utl_sequence
            -- then use the next_value from utl_sequence instead.  This will allow us to advance
            -- a sequence to a new minimum starting point if necessary.
            IF ln_data_max < (NVL(lrec_seq_info.next_value,0) - 1)
            THEN
               ln_step := 150;
               ln_data_max := (NVL(lrec_seq_info.next_value,0) - 1);
               lb_set_minimum := TRUE;
            END IF;

            -- Compare the maximum column value to the last number issued by the sequence.
            ln_step := 160;
            ln_difference := ln_data_max - ln_seq_max;

            -- If the difference is greater than zero then the sequence is lower than the maximum value in the column.
            IF ln_difference > 0
            THEN

               -- The sequence is lower than the maximum column value, therefore, the sequence must be resynced.

               -- Change the increment by to the value of the difference.
               ln_step := 170;
               EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || lrec_seq_info.sequence_name || ' INCREMENT BY ' || ln_difference;

               BEGIN

                  -- Select the nextval of the sequence to advance it by the amount of the difference.
                  ln_step := 180;
                  EXECUTE IMMEDIATE 'SELECT ' || lrec_seq_info.sequence_name || '.NEXTVAL FROM DUAL' INTO ln_seq_val;

               EXCEPTION
                  WHEN OTHERS THEN

                     -- If any error occurs when attempting to pull the nextval then change the increment by back to the
                     -- original value to prevent the sequence from being left with an incorrect increment by value.
                     EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || lrec_seq_info.sequence_name || ' INCREMENT BY ' || lrec_seq_info.increment_by;

                     -- Raise the error.
                     RAISE;

               END;

               -- Change the increment by back to the original value.
               ln_step := 190;
               EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || lrec_seq_info.sequence_name || ' INCREMENT BY ' || lrec_seq_info.increment_by;

               IF lb_set_minimum = TRUE
               THEN

                  -- Notify the user that the sequence has been modified to the minimum value from utl_seqeunce.
                  ln_step := 200;
                  DBMS_OUTPUT.PUT_LINE('INFO: The ' || lrec_seq_info.sequence_name || ' sequence was advanced to ' || ln_seq_val ||
                                       ' to allow the next value to match the minimum value from the UTL_SEQUENCE.NEXT_VALUE column.');

               ELSE

                  -- Notify the user that the sequence has been modified to the last number pulled in the array.
                  ln_step := 210;
                  DBMS_OUTPUT.PUT_LINE('INFO: The ' || lrec_seq_info.sequence_name || ' sequence was advanced to ' || ln_seq_val ||
                                       ' to match the largest value in the ' || lrec_seq_info.table_name || '.' || lrec_seq_info.column_name || ' column.');

               END IF;

               -- Increment the resync count
               ln_step := 220;
               ln_resync_count := ln_resync_count + 1;

            ELSE

               -- The sequence did not require a resync, therefore, increase the valid count.
               ln_step := 230;
               ln_valid_count := ln_valid_count + 1;

            END IF;

            IF lrec_seq_info.cache_size > 0
            THEN
               -- Reset the cache size to its original value if the original value was greater than zero.
               ln_step := 240;
               EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || lrec_seq_info.sequence_name || ' CACHE ' || lrec_seq_info.cache_size;
            END IF;

         ELSE

            -- The lb_skip_sequence variable is TRUE, therefore, notify the user that this sequence was skipped.
            ln_step := 250;
            DBMS_OUTPUT.PUT_LINE('INFO: The ' || lrec_seq_info.sequence_name || ' sequence was not processed because this is a deployed operational site and this sequence uses sequence spacing.');

            -- Increment the skip count
            ln_step := 260;
            ln_skip_count := ln_skip_count + 1;

         END IF;

      END LOOP;

      -- Notify the user as to how many sequences were modified.
      ln_step := 270;
      DBMS_OUTPUT.PUT_LINE('INFO: ' || ln_resync_count || ' sequences were advanced to match the maximum data value or minimum start value.');

      -- Notify the user as to how many sequences were not modified.
      ln_step := 280;
      DBMS_OUTPUT.PUT_LINE('INFO: ' || ln_valid_count || ' sequences were not modified because their last number was already correct.');

      IF ln_skip_count > 0
      THEN

         -- Notify the user as to how many sequences were skipped
         ln_step := 290;
         DBMS_OUTPUT.PUT_LINE('INFO: ' || ln_skip_count || ' sequences were not processed because this is a deployed operational site and these sequences use sequence spacing.');

      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         lv_err_code    := SQLCODE;
         lv_method_name := 'sync_sequences';
         lv_err_msg     := substr(kv_pkg_name || '.' || lv_method_name ||
                                 ', STEP: ' || ln_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 lv_err_msg,
                                 TRUE);

   END sync_sequences;

END utl_sequence_pkg;
/

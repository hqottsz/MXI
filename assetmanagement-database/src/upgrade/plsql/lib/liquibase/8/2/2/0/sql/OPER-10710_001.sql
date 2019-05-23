--liquibase formatted sql


--changeSet OPER-10710:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE utl_sequence_pkg IS
   ----------------------------------------------------------------------------
   -- Object Name : utl_sequence_pkg
   -- Object Type : Package Spec
   -- Description : This is the sequence package containing the methods for
   --               managing the sequences in the utl_sequences table.
   ----------------------------------------------------------------------------
   -- Copyright 2017 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Global types
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Global Constants
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Public Methods
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
   PROCEDURE sync_sequences;

END utl_sequence_pkg;
/

--liquibase formatted sql


--changeSet MX-18382:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "ALERT_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : ALERT_PKG
   -- Object Type : Package Header
   -- Date        : February 22, 2009
   -- Coder       : Ed Irvine
   -- Recent Date : December 7, 2009
   -- Recent Coder: Ed Irvine
   -- Description :
   -- This package contains methods for raising alerts in maintenix from
   -- a PLSQL framework.
   --
   -- Procedures : raise_alert
   ----------------------------------------------------------------------------
   -- Copyright © 2009 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   -----------------------------------------------------------------------------
   -- Local Package Types
   -----------------------------------------------------------------------------
   TYPE t_user_id IS RECORD(
      user_id utl_user_alert.user_id%TYPE);

   TYPE t_tab_alert_users IS TABLE OF t_user_id;
   -----------------------------------------------------------------------------
   -- Local Package Constants
   -----------------------------------------------------------------------------
   c_pkg_name     CONSTANT VARCHAR2(30) := 'ALERT_PKG';
   gv_err_msg_gen CONSTANT VARCHAR2(2000) := 'An Oracle error has occurred details are as follows: ';
   -----------------------------------------------------------------------------
   -- Local Package Variables
   -----------------------------------------------------------------------------
   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);
   -----------------------------------------------------------------------------
   -- Private Function Declarations
   -----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Function:    valid_alert_type
   -- Arguments:   p_alert_type_id   (NUMBER) -- alert type to be created
   --
   -- Returns : BOOLEAN
   -- Description:  Validates alert type is able to be created
   ----------------------------------------------------------------------------
   FUNCTION valid_alert_type(p_alert_type_id IN utl_alert_type.alert_type_id%TYPE)
      RETURN NUMBER;
   ----------------------------------------------------------------------------
   -- Function:    get_alert_id_seq
   --
   -- Returns : NUMBER
   -- Description:  Grabs sequence for alerts
   ----------------------------------------------------------------------------
   FUNCTION get_alert_id_seq RETURN NUMBER;
   ----------------------------------------------------------------------------
   -- Function:    get_alert_log_id_seq
   --
   -- Returns : NUMBER
   -- Description:  Grabs sequence for alerts log
   ----------------------------------------------------------------------------
   FUNCTION get_alert_log_id_seq RETURN NUMBER;
   ----------------------------------------------------------------------------
   -- Function:    get_alert_users
   -- Arguments:   p_alert_type_id
   --
   -- Returns : t_tab_alert_users
   -- Description:  Returns list of users to be notified based on and alert
   --               type id. The function checks the roles associated with
   --               the alert and grabs the list of users
   ----------------------------------------------------------------------------
   FUNCTION get_alert_users(p_alert_type_id IN utl_alert_type.alert_type_id%TYPE)
      RETURN t_tab_alert_users;
   ----------------------------------------------------------------------------
   -- Procedure:   insert_alert
   -- Arguments:   p_alert_type_id   (NUMBER) -- alert type to be created
   --              p_alert_priority  (NUMBER) -- the priority of the alert
   --              p_alert_parm_tb   (GT_TAB_ALERT_PARM) -- plsql table of parameters
   --
   -- Description:  Takes alert information from parameters and enters a record
   --               in
   ----------------------------------------------------------------------------
   PROCEDURE insert_alert(p_alert_type_id  IN utl_alert_type.alert_type_id%TYPE,
                          p_alert_priority IN utl_alert_type.priority%TYPE,
                          p_alert_parm_tb  IN gt_tab_alert_parm);
   ----------------------------------------------------------------------------
   -- Procedure:   raise_alert
   -- Arguments:   p_alert_type_id   (NUMBER) -- alert type to be created
   --              p_alert_priority  (NUMBER) -- the priority of the alert
   --              p_alert_parm_tb   (GT_TAB_ALERT_PARM) -- plsql table of parameters
   --
   -- Description:  Takes alert information as parameters and executes initiating
   --               an alert into the maintenix alert framework.
   ----------------------------------------------------------------------------
   PROCEDURE raise_alert(p_alert_type_id  IN utl_alert_type.alert_type_id%TYPE,
                         p_alert_priority IN utl_alert_type.priority%TYPE DEFAULT 0,
                         p_alert_parm_tb  IN gt_tab_alert_parm) IS

      v_priority utl_alert_type.priority%TYPE;

   BEGIN

      -- First check whether alert type is valid to be raised
      v_step     := 10;
      v_priority := valid_alert_type(p_alert_type_id);
      IF v_priority IS NULL
      THEN
         RAISE gex_alert_notfound;
      ELSE
         -- Check priority of alert is valid
         -- TODO Will need discussion with alerts team to see if DEPLOYED will have different priority levels
         IF v_priority > p_alert_priority
         THEN
            NULL;
         ELSE
            v_step     := 30;
            v_priority := p_alert_priority;
         END IF;
      END IF;

      -- Enter alert record into UTL_ALERT
      -- First grab the sequence for the Primary Key of the UTL ALERT table
      v_step := 40;
      insert_alert(p_alert_type_id  => p_alert_type_id,
                   p_alert_priority => v_priority,
                   p_alert_parm_tb  => p_alert_parm_tb);

   EXCEPTION
      WHEN gex_alert_notfound THEN
         raise_application_error(gc_ex_alert_err,
                                 substr(c_pkg_name || '.raise_alert : ' ||
                                        gv_err_msg_alert_notfound,
                                        1,
                                        2000),
                                 TRUE);
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_alert_err,
                                 substr(c_pkg_name || '.raise_alert : ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' Errored at Step: ' ||
                                        v_step,
                                        1,
                                        2000),
                                 TRUE);
   END raise_alert;
   ----------------------------------------------------------------------------
   -- Procedure:   insert_alert
   -- Arguments:   p_alert_type_id   (NUMBER) -- alert type to be created
   --              p_alert_priority  (NUMBER) -- the priority of the alert
   --              p_alert_parm_tb   (GT_TAB_ALERT_PARM) -- plsql table of parameters
   --
   -- Description:  Takes alert information from parameters and enters a record
   --               in
   ----------------------------------------------------------------------------
   PROCEDURE insert_alert(p_alert_type_id  IN utl_alert_type.alert_type_id%TYPE,
                          p_alert_priority IN utl_alert_type.priority%TYPE,
                          p_alert_parm_tb  IN gt_tab_alert_parm) IS

      v_alert_id     utl_alert.alert_id%TYPE;
      v_alert_log_id utl_alert_log.alert_log_id%TYPE;
      v_parm_id      utl_alert_parm.parm_ord%TYPE := 0;
      v_db_id        mim_local_db.db_id%TYPE;
      v_user_id      utl_user.user_id%TYPE;
      t_tab_users    t_tab_alert_users;

   BEGIN

      -- Get current user id for alert log tables
      v_step := 5;
   
      SELECT user_id
        INTO v_user_id
        FROM utl_user
       WHERE upper(username) = upper('mxsystem');
   
      -- Get pk key for alert record
      v_step     := 10;
      v_alert_id := get_alert_id_seq;

      -- Get current local db_id
      SELECT db_id
        INTO v_db_id
        FROM mim_local_db;

      -- Insert record into UTL_ALERT
      v_step := 20;
      INSERT INTO utl_alert
         (alert_id,
          alert_type_id,
          priority,
          alert_timestamp,
          alert_status_cd,
          utl_id)
      VALUES
         (v_alert_id,
          p_alert_type_id,
          p_alert_priority,
          current_timestamp,
          gc_alert_status_cd_new,
          v_db_id); -- Add current mim_local_db_id;

      -- Insert all records currently in parm_tb
      v_parm_id := 1;

      v_step := 40;
      FOR parm_rec IN p_alert_parm_tb.FIRST .. p_alert_parm_tb.COUNT
      LOOP
         -- Insert a parm record for each in the plsql table
         INSERT INTO utl_alert_parm
            (alert_id,
             parm_id,
             parm_ord,
             parm_type,
             parm_value_sdesc,
             parm_value,
             utl_id)
         VALUES
            (v_alert_id,
             v_parm_id,
             p_alert_parm_tb(parm_rec).parm_ord,
             p_alert_parm_tb(parm_rec).parm_type,
             p_alert_parm_tb(parm_rec).parm_value_sdesc,
             p_alert_parm_tb(parm_rec).parm_value,
             v_db_id);
         -- Move on parm id
         v_parm_id := v_parm_id + 1;
      END LOOP;

      -- Get the table of user ids to be alerted
      v_step      := 50;
      t_tab_users := get_alert_users(p_alert_type_id);

      -- if no users are found then alert is not raised against anyone
      IF t_tab_users.COUNT > 0
      THEN
         -- Enter a notification record for each user into UTL_USER_ALERT
         v_step := 60;
         FOR user_rec IN t_tab_users.FIRST .. t_tab_users.COUNT
         LOOP
            -- Get pk key for alert record
            v_step         := 65;
            v_alert_log_id := get_alert_log_id_seq;

            v_step := 70;
            INSERT INTO utl_user_alert
               (alert_id,
                user_id,
                notified_date,
                assigned_bool,
                utl_id)
            VALUES
               (v_alert_id,
                t_tab_users(user_rec).user_id,
                current_timestamp,
                0,
                v_db_id);
         
         END LOOP;
      END IF;
   
      -- Add utl_alert_log record
      v_step := 70;
      INSERT INTO utl_alert_log
         (alert_id,
          user_id,
          alert_log_id,
          log_dt,
          utl_id,
          system_note)
      VALUES
         (v_alert_id,
          v_user_id,
          v_alert_log_id,
          SYSDATE,
          v_db_id,
          'This alert was created.');
   
      -- Add utl_alert_status_log record
      v_step := 80;
      INSERT INTO utl_alert_status_log
         (alert_id,
          user_id,
          alert_status_cd,
          log_dt,
          utl_id)
      VALUES
         (v_alert_id,
          v_user_id,
          gc_alert_status_cd_new,
          SYSDATE,
          v_db_id);
   
      COMMIT;
   
   EXCEPTION
      WHEN OTHERS THEN
         -- Rollback transaction should there be any errors
         ROLLBACK;
         -- Will need to raise exception if unsuccessful
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_alert_err,
                                 substr(c_pkg_name || '.insert_alert : ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' Errored at Step: ' ||
                                        v_step,
                                        1,
                                        2000),
                                 TRUE);
   END insert_alert;
   ----------------------------------------------------------------------------
   -- Function:    valid_alert_type
   -- Arguments:   p_alert_type_id   (NUMBER) -- alert type to be created
   --
   -- Returns : Returns default priority type
   -- Description:  Validates alert type is able to be created
   ----------------------------------------------------------------------------
   FUNCTION valid_alert_type(p_alert_type_id IN utl_alert_type.alert_type_id%TYPE)
      RETURN NUMBER IS

      CURSOR cur_get_alert_type(pc_alert_type_id IN utl_alert_type.alert_type_id%TYPE) IS
         SELECT uat.priority
           FROM utl_alert_type uat
          WHERE uat.alert_type_id = pc_alert_type_id
            AND uat.active_bool = 1;

      t_alert_type cur_get_alert_type%ROWTYPE;

   BEGIN
      -- Open cursor to see if valid row exists for alert type argument
      OPEN cur_get_alert_type(p_alert_type_id);
      FETCH cur_get_alert_type
         INTO t_alert_type;
      IF cur_get_alert_type%NOTFOUND
      THEN
         -- Return false if alert type cannot be found
         RETURN NULL;
      END IF;

      -- Return true if alert is successfully found
      RETURN t_alert_type.priority;
   EXCEPTION
      WHEN OTHERS THEN
         -- Any errors during this function should return null
         RETURN NULL;
   END valid_alert_type;
   ----------------------------------------------------------------------------
   -- Function:    get_alert_id_seq
   --
   -- Returns : NUMBER
   -- Description:  Grabs sequence for alerts
   ----------------------------------------------------------------------------
   FUNCTION get_alert_id_seq RETURN NUMBER IS

      CURSOR cur_get_alert_seq IS
         SELECT us.sequence_cd
           FROM user_constraints  uc,
                user_cons_columns ucc,
                utl_sequence      us
          WHERE uc.table_name = 'UTL_ALERT'
            AND uc.constraint_type = 'P'
            AND uc.constraint_name = ucc.constraint_name
            AND uc.table_name = us.table_name
            AND ucc.column_name = us.column_name;

      v_seq_name utl_sequence.sequence_cd%TYPE;
      v_alert_id utl_alert.alert_id%TYPE;
      v_stmt     VARCHAR2(2000);

   BEGIN
      -- Open cursor to see if valid row exists for alert type argument
      OPEN cur_get_alert_seq;
      FETCH cur_get_alert_seq
         INTO v_seq_name;
      IF cur_get_alert_seq%NOTFOUND
      THEN
         RETURN NULL;
      END IF;
      -- Get next sequence value
      v_stmt := 'SELECT ' || v_seq_name || '.NEXTVAL FROM DUAL';

      EXECUTE IMMEDIATE v_stmt
         INTO v_alert_id;
      -- Return v_alert_id is successfully found
      RETURN v_alert_id;
   EXCEPTION
      WHEN OTHERS THEN
         -- Any errors during this function should return null
         RETURN NULL;
   END get_alert_id_seq;
   ----------------------------------------------------------------------------
   -- Function:    get_alert_users
   -- Arguments:   p_alert_type_id
   --
   -- Returns : t_tab_alert_users
   -- Description:  Returns list of users to be notified based on and alert
   --               type id. The function checks the roles associated with
   --               the alert and grabs the list of users
   ----------------------------------------------------------------------------
   FUNCTION get_alert_users(p_alert_type_id IN utl_alert_type.alert_type_id%TYPE)
      RETURN t_tab_alert_users IS

      t_tab_user t_tab_alert_users := t_tab_alert_users();

   BEGIN
      -- Bulk collect user into NUMBER collection
      SELECT DISTINCT uur.user_id BULK COLLECT
        INTO t_tab_user
        FROM utl_user_role       uur,
             utl_alert_type_role uatr,
             utl_alert_type      uat
       WHERE uur.role_id = uatr.role_id
         AND uatr.alert_type_id = uat.alert_type_id
         AND uat.alert_type_id = p_alert_type_id;

      RETURN t_tab_user;
   EXCEPTION
      WHEN OTHERS THEN
         RETURN NULL;
   END get_alert_users;
   ----------------------------------------------------------------------------
   -- Function:    get_alert_id_seq
   --
   -- Returns : NUMBER
   -- Description:  Grabs sequence for alerts
   ----------------------------------------------------------------------------
   FUNCTION get_alert_log_id_seq RETURN NUMBER IS

      CURSOR cur_get_alert_log_seq IS
         SELECT us.sequence_cd
           FROM user_constraints  uc,
                user_cons_columns ucc,
                utl_sequence      us
          WHERE uc.table_name = 'UTL_ALERT_LOG'
            AND uc.constraint_type = 'P'
            AND uc.constraint_name = ucc.constraint_name
            AND uc.table_name = us.table_name
            AND ucc.column_name = us.column_name;

      v_seq_name     utl_sequence.sequence_cd%TYPE;
      v_alert_log_id utl_alert_log.alert_log_id%TYPE;
      v_stmt         VARCHAR2(2000);

   BEGIN
      -- Open cursor to see if valid row exists for alert type argument
      OPEN cur_get_alert_log_seq;
      FETCH cur_get_alert_log_seq
         INTO v_seq_name;
      IF cur_get_alert_log_seq%NOTFOUND
      THEN
         RETURN NULL;
      END IF;
      -- Get next sequence value
      v_stmt := 'SELECT ' || v_seq_name || '.NEXTVAL FROM DUAL';

      EXECUTE IMMEDIATE v_stmt
         INTO v_alert_log_id;
      -- Return v_alert_log_id is successfully found
      RETURN v_alert_log_id;
   EXCEPTION
      WHEN OTHERS THEN
         -- Any errors during this function should return null
         RETURN NULL;
   END get_alert_log_id_seq;
   ----------------------------------------------------------------------------
END alert_pkg;
/

package com.mxi.mx.core.unittest.table.sd;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>sd_fault</code> table.
 *
 * @author mbajer
 * @created April 24, 2002
 */
public class SdFault {

   /** Results of the query. */
   private DataSet iActual;

   /** Columns in the table. */
   private String[] iCols = { "fault_db_id", "fault_id", "fail_mode_db_id", "fail_defer_db_id",
         "fail_defer_cd", "fail_mode_id", "fail_catgry_db_id", "defer_ref_sdesc", "fail_catgry_cd",
         "fail_sev_db_id", "fail_sev_cd", "fault_source_cd", "found_by_hr_db_id", "found_by_hr_id",
         "fail_type_cd", "sdr_bool", "flight_stage_cd", "fail_priority_db_id", "fail_priority_cd",
         "op_restriction_ldesc", "defer_cd_sdesc", "eval_bool", "leg_id", "frm_sdesc",
         "fault_log_type_cd", "ext_controlled_bool", "ext_raised_bool" };


   /**
    * Initializes the class.
    *
    * @param aFault
    *           primary key of the table.
    */
   public SdFault(FaultKey aFault) {
      DataSetArgument lWhereClause;

      // Obtain actual value
      lWhereClause = new DataSetArgument();
      lWhereClause.add( "fault_db_id", aFault.getDbId() );
      lWhereClause.add( "fault_id", aFault.getId() );

      iActual = MxDataAccess.getInstance().executeQuery( iCols, "sd_fault", lWhereClause );
   }


   /**
    * Asserts that the <code>fail_sev_cd</code> and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           Expcted value
    */
   public void assertFailSev( String aExpected ) {
      String lActual = iActual.getStringAt( 1, "fail_sev_cd" );
      MxAssert.assertEquals( "fail_sev_cd", aExpected, lActual );
   }


   /**
    * Asserts that the <code>fail_type_cd</code> and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           Expected value
    */
   public void assertFailType( String aExpected ) {
      String lActual;

      lActual = iActual.getStringAt( 1, "fail_type_cd" );
      MxAssert.assertEquals( "fail_type_cd", aExpected, lActual );
   }


   /**
    * Asserts that the <code>fault_source_cd</code> and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           Expected value
    */
   public void assertFaultSource( String aExpected ) {
      String lActual;

      lActual = iActual.getStringAt( 1, "fault_source_cd" );
      MxAssert.assertEquals( "fault_source_cd", aExpected, lActual );
   }

}

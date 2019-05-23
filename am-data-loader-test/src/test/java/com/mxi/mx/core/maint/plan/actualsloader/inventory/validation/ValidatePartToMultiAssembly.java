/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2016 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.core.maint.plan.actualsloader.inventory.validation;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains unit tests for Actuals Loader inventory validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce
 */

public class ValidatePartToMultiAssembly extends ActualsLoaderTest {

   /**
    * Clean up after each individual test
    *
    * @throws Exception
    */
   @Override
   @After
   public void after() throws Exception {

      // clean up the event data
      clearMxTestData();

      super.after();

   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Override
   @Before
   public void before() throws Exception {

      super.before();
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>A part number can fit on multiple assemblies</li>
    * </ul>
    * </p>
    *
    * <p>
    * The test will:
    *
    * <ul>
    * <li>Insert inventory to the staging table</li>
    * <li>Run inventory Validation</li>
    * </ul>
    * </p>
    *
    * <p>
    * And expects the following
    *
    * <ul>
    * <li>Error Code: None</li>
    * <li>No error message should be returned.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_AML_10438_PartFitsOnMultipleAssemblies() throws Exception {

      // determine part_no_id and bom_part_id for target part in test environment
      ResultSet lBomPartIdResultSet = runQuery(
            "SELECT bom_part_id FROM eqp_bom_part WHERE eqp_bom_part.bom_part_cd = 'APU-SYS-1-1-TRK-SER-CHILD'" );
      lBomPartIdResultSet.next();
      String lBomPartId = lBomPartIdResultSet.getString( 1 );

      ResultSet lPartNoIdResultSet = runQuery(
            "SELECT part_no_id FROM eqp_part_no WHERE eqp_part_no.part_no_oem = 'A0000014'" );
      lPartNoIdResultSet.next();
      String lPartNoId = lPartNoIdResultSet.getString( 1 );

      // temporarily add target part number assignment to a second assembly
      Map<String, String> lMapEqpPartBaseline = new LinkedHashMap<String, String>();

      lMapEqpPartBaseline.put( "BOM_PART_DB_ID", "4650" );
      lMapEqpPartBaseline.put( "BOM_PART_ID", lBomPartId );
      lMapEqpPartBaseline.put( "PART_NO_DB_ID", "4650" );
      lMapEqpPartBaseline.put( "PART_NO_ID", lPartNoId );
      lMapEqpPartBaseline.put( "STANDARD_BOOL", "0" );
      lMapEqpPartBaseline.put( "INTERCHG_ORD", "1" );
      lMapEqpPartBaseline.put( "APPROVED_BOOL", "1" );
      lMapEqpPartBaseline.put( "CONDITIONAL_BOOL", "0" );
      lMapEqpPartBaseline.put( "RSTAT_CD", "0" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.EQP_PART_BASELINE, lMapEqpPartBaseline ) );

      // create inventory map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'ACFT-SN-SER-t'" );
      lMapInventory.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventory.put( "INV_CLASS_CD", "'ACFT'" );
      lMapInventory.put( "MANUFACT_CD", "'10001'" );
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'REPREQ'" );
      lMapInventory.put( "AC_REG_CD", "'ACREGt'" );
      lMapInventory.put( "INV_OPER_CD", "'NORM'" );
      lMapInventory.put( "ISSUE_ACCOUNT_CD", "'5'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create inventory map
      Map<String, String> lMapInventorySub = new LinkedHashMap<String, String>();

      lMapInventorySub.put( "PARENT_SERIAL_NO_OEM", "'ACFT-SN-SER-t'" );
      lMapInventorySub.put( "PARENT_PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      lMapInventorySub.put( "PARENT_MANUFACT_CD", "'10001'" );
      lMapInventorySub.put( "SERIAL_NO_OEM", "'SER-ACFT-t'" );
      lMapInventorySub.put( "PART_NO_OEM", "'A0000014'" );
      lMapInventorySub.put( "MANUFACT_CD", "'11111'" );
      lMapInventorySub.put( "INV_CLASS_CD", "'SER'" );
      lMapInventorySub.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY_SUB, lMapInventorySub ) );

      System.out.println(
            "*** Starting validation for test test_AML_10438_PartFitsOnMultipleAssemblies ***" );

      // call inventory validation
      runALValidateInventory();

      // remove temporary part to second assembly assignment
      deleteFromTableWhere( "EQP_PART_BASELINE",
            TableUtil.whereFromTableByMap( lMapEqpPartBaseline ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_AML_10438_PartFitsOnMultipleAssemblies", "PASS" );
   }
}

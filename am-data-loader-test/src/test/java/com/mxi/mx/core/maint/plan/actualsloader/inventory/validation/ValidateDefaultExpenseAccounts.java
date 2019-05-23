/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 * 
 * Copyright 2000-2015 Mxi Technologies, Ltd. All Rights Reserved.
 * 
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.core.maint.plan.actualsloader.inventory.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;


/**
 * This test suite contains unit tests for Actuals Loader task validations for Part Numbers and
 * Serial Numbers
 *
 * @author Andrew Bruce
 */

public class ValidateDefaultExpenseAccounts extends ActualsLoaderTest {

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
    * <li>Part number is known</li>
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
    * <li>Error Code: GBL-00003</li>
    * <li>There is no default expense account specified in the Maintenix database OR Missing one or
    * more expense account in the staging table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_GBL_00003_NoDefaultExpenseAccounts() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000003'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" ); // duplicate fields in database
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create manufacturer map
      Map<String, String> lMapAccount = new LinkedHashMap<String, String>();

      lMapAccount.put( "ACCOUNT_DB_ID", "4650" );
      lMapAccount.put( "ACCOUNT_ID", "100003" );
      lMapAccount.put( "ACCOUNT_TYPE_DB_ID", "0" );
      lMapAccount.put( "ACCOUNT_TYPE_CD", "'EXPENSE'" );
      lMapAccount.put( "ACCOUNT_CD", "'5'" );
      lMapAccount.put( "ACCOUNT_SDESC", "'Expense'" );
      lMapAccount.put( "ACCOUNT_LDESC", "'Expense'" );
      lMapAccount.put( "DEFAULT_BOOL", "1" );
      lMapAccount.put( "CLOSED_BOOL", "0" );

      // insert map
      deleteFromTableWhere( "FNC_ACCOUNT", TableUtil.whereFromTableByMap( lMapAccount ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "FNC_ACCOUNT", lMapAccount ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_GBL_00003_NoDefaultExpenseAccounts", "GBL-00003" );
   }


   /**
    * <p>
    * Given the Preconditions:
    *
    * <ul>
    * <li>Part number is known</li>
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
    * <li>Error Code: GBL-00004</li>
    * <li>Multiple default expense accounts.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_GBL_00004_MultipleDefaultExpenseAccounts() throws Exception {

      // create task map
      Map<String, String> lMapInventory = new LinkedHashMap<String, String>();

      lMapInventory.put( "SERIAL_NO_OEM", "'TRK-10005'" );
      lMapInventory.put( "PART_NO_OEM", "'A0000003'" );
      lMapInventory.put( "INV_CLASS_CD", "'TRK'" );
      lMapInventory.put( "MANUFACT_CD", "'ABC11'" ); // duplicate fields in database
      lMapInventory.put( "LOC_CD", "'OPS'" );
      lMapInventory.put( "INV_COND_CD", "'RFI'" );
      lMapInventory.put( "MANUFACT_DT", "to_date('01/01/2015','mm/dd/yyyy')" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_RI_INVENTORY, lMapInventory ) );

      // create manufacturer map
      Map<String, String> lMapAccount = new LinkedHashMap<String, String>();

      lMapAccount.put( "ACCOUNT_DB_ID", "4650" );
      lMapAccount.put( "ACCOUNT_ID", "999" );
      lMapAccount.put( "ACCOUNT_TYPE_DB_ID", "0" );
      lMapAccount.put( "ACCOUNT_TYPE_CD", "'EXPENSE'" );
      lMapAccount.put( "ACCOUNT_CD", "'99'" );
      lMapAccount.put( "ACCOUNT_SDESC", "'TEST'" );
      lMapAccount.put( "ACCOUNT_LDESC", "'TEST'" );
      lMapAccount.put( "DEFAULT_BOOL", "1" );
      lMapAccount.put( "CLOSED_BOOL", "0" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "FNC_ACCOUNT", lMapAccount ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "FNC_ACCOUNT", TableUtil.whereFromTableByMap( lMapAccount ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_GBL_00004_MultipleDefaultExpenseAccounts", "GBL-00004" );
   }
}

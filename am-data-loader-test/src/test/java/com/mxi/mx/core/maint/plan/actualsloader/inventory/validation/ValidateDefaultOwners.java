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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class ValidateDefaultOwners extends ActualsLoaderTest {

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
    * <li>Error Code: GBL-00001</li>
    * <li>There is no default owner specified in the Maintenix database OR Missing one or more owner
    * code in the staging table.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_GBL_00001_NoDefaultOwners() throws Exception {

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
      Map<String, String> lMapOwner = new LinkedHashMap<String, String>();

      lMapOwner.put( "OWNER_DB_ID", "4650" );
      // lMapOwner.put( "OWNER_ID", "100001" );
      lMapOwner.put( "OWNER_CD", "'MXI'" );
      lMapOwner.put( "OWNER_NAME", "'Mxi Technologies'" );
      lMapOwner.put( "LOCAL_BOOL", "1" );
      lMapOwner.put( "DEFAULT_BOOL", "1" );
      lMapOwner.put( "ORG_DB_ID", "0" );
      lMapOwner.put( "ORG_ID", "1" );

      String lOWNERID = getOWNERID( TableUtil.whereFromTableByMap( lMapOwner ) );
      lMapOwner.put( "OWNER_ID", lOWNERID );

      // insert map
      deleteFromTableWhere( "INV_OWNER", TableUtil.whereFromTableByMap( lMapOwner ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "INV_OWNER", lMapOwner ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_GBL_00001_NoDefaultOwners", "GBL-00001" );
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
    * <li>Error Code: GBL-00002</li>
    * <li>Multiple default owners.</li>
    * </ul>
    * </p>
    *
    * @throws Exception
    *            if there is an error
    */
   @Test
   public void test_GBL_00002_MultipleDefaultOwners() throws Exception {

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
      Map<String, String> lMapOwner = new LinkedHashMap<String, String>();

      lMapOwner.put( "OWNER_DB_ID", "4650" );
      lMapOwner.put( "OWNER_ID", "999" );
      lMapOwner.put( "OWNER_CD", "'MXI'" );
      lMapOwner.put( "OWNER_NAME", "'TEST'" );
      lMapOwner.put( "LOCAL_BOOL", "1" );
      lMapOwner.put( "DEFAULT_BOOL", "1" );
      lMapOwner.put( "ORG_DB_ID", "0" );
      lMapOwner.put( "ORG_ID", "1" );

      // insert map
      runInsert( TableUtil.getInsertForTableByMap( "INV_OWNER", lMapOwner ) );

      // call inventory validation
      runALValidateInventory();

      // insert map
      deleteFromTableWhere( "INV_OWNER", TableUtil.whereFromTableByMap( lMapOwner ) );

      // call inventory validation and check result against expectation
      checkInventory( "test_GBL_00002_MultipleDefaultOwners", "GBL-00002" );
   }


   public String getOWNERID( String aWhere ) {

      ResultSet lResultSet = null;
      String lId = null;
      String lquery = "SELECT OWNER_ID FROM INV_OWNER" + aWhere;

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( lquery,
               ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

         lResultSet = lStatement.executeQuery( lquery );
         lResultSet.next();
         lId = lResultSet.getString( "OWNER_ID" );
         if ( !lResultSet.isLast() )
            throw new IllegalArgumentException( "This query returns more than one row: " + lquery );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return lId;

   }
}

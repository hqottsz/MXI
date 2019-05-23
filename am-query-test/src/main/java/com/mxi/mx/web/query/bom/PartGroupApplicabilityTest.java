
package com.mxi.mx.web.query.bom;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PartGroupApplicabilityTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   /** The part group key */
   public static final PartGroupKey BOM_PART_KEY_1 = new PartGroupKey( 4650, 1 );

   public static final PartGroupKey BOM_PART_KEY_2 = new PartGroupKey( 4650, 2 );


   /**
    * Tests that the query returns two aircraft for<br>
    * Case 1: part group with applicability range N/A. Case 2: part group with applicability range
    * 100-200.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute( BOM_PART_KEY_1 );
      lResult.addSort( "dsString( inventory_key )", true );
      lResult.filterAndSort();

      // *********************************************************************
      // TEST 1:
      // Part Group applicability range = N/A
      // *********************************************************************

      // asserts that 2 rows are returned
      assertEquals( 2, lResult.getRowCount() );

      // assert that part group is not applicable to any aircraft and hence, none of the alternate
      // parts are applicable
      lResult.first();

      MxAssert.assertEquals( "APPLICABLE PART 1", lResult.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "SN ACFT 1", lResult.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "REG CD 1", lResult.getString( "ac_reg_cd" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_group_applicable" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_1" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_2" ) );

      // assert that part group is not applicable to any aircraft and hence, none of the alternate
      // parts are applicable
      lResult.next();

      MxAssert.assertEquals( "NON APPLICABLE PART 2", lResult.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "SN ACFT 2", lResult.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "REG CD 2", lResult.getString( "ac_reg_cd" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_group_applicable" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_1" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_2" ) );

      // ****************************************
      // TEST 2:
      // Part Group applicability range = 100-200
      // Part 1 applicability range = 100-500
      // Part 2 applicability range = N/A
      // Aircraft 1 applicability code = 150
      // Aircraft 2 applicability code = 600
      // ****************************************

      lResult = execute( BOM_PART_KEY_2 );
      lResult.addSort( "dsString( inventory_key )", true );
      lResult.filterAndSort();

      // asserts that 2 rows are returned
      assertEquals( 2, lResult.getRowCount() );

      // Assert aircraft 1 is applicable to part group applicability range and alternate part 1.
      // Since alternate part 2's applicability range is N/A therfore, it is not applicable.
      lResult.first();

      MxAssert.assertEquals( "APPLICABLE PART 1", lResult.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "SN ACFT 1", lResult.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "REG CD 1", lResult.getString( "ac_reg_cd" ) );
      MxAssert.assertEquals( true, lResult.getBoolean( "part_group_applicable" ) );
      MxAssert.assertEquals( true, lResult.getBoolean( "part_1" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_2" ) );

      // Assert aircraft 2 applicable code is out of the part group applicability range and so
      // therefore, it is not applicable to any alternate parts.
      lResult.next();

      MxAssert.assertEquals( "NON APPLICABLE PART 2", lResult.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "SN ACFT 2", lResult.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "REG CD 2", lResult.getString( "ac_reg_cd" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_group_applicable" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_1" ) );
      MxAssert.assertEquals( false, lResult.getBoolean( "part_2" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), PartGroupApplicabilityTest.class,
            PartGroupApplicabilityData.getDataFile() );
   }


   /**
    * Execute the query.
    *
    * @param aPartGroupKey
    *           The bom part key
    *
    * @return dataSet result.
    */
   private DataSet execute( PartGroupKey aPartGroupKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartGroupKey, "aBomPartDbId", "aBomPartId" );

      StringBuffer lSelectColumns = new StringBuffer();
      lSelectColumns.append( "MAX(CASE WHEN alternate_part_key = '4650:10' "
            + "THEN alternate_part_applicable ELSE NULL END ) AS part_1, " );
      lSelectColumns.append( "MAX(CASE WHEN alternate_part_key = '4650:20' "
            + "THEN alternate_part_applicable ELSE NULL END ) AS part_2, " );

      lArgs.addSelect( lSelectColumns.toString() );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}

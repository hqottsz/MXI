
package com.mxi.mx.web.query.taskdefn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * tests the AddPartRequirementSearch Query
 *
 * @author slevert
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AddPartRequirementSearchTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private static final TaskTaskKey TASK_TASK_KEY = new TaskTaskKey( 4650, 23749 );
   private static final PartGroupKey SER_PART_GROUP_KEY = new PartGroupKey( 4650, 33042 );
   private static final PartNoKey SER_PART_NO_KEY = new PartNoKey( 4650, 21206 );
   private static final String SER_OEM_PART_NO = "WBS-C-1.7.5.1.2.SER";


   /**
    * Tests kit contents
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchForAllRecords() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", 4650 );
      lArgs.add( "aTaskId", 23749 );
      lArgs.add( "aOemPartNo", ( String ) null );
      lArgs.add( "aPartName", ( String ) null );
      lArgs.add( "aAssemblyCd", "ASSEMBLY_OR_COMMON_HARDWARE" );
      lArgs.add( "aBomPartCd", ( String ) null );
      lArgs.add( "aSearchMethod", "STARTS_WITH" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 2 rows
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      // a kit part group
      lDs.next();
      assertKitPartGroup( lDs );

      // a non kit part group
      lDs.next();
      assertNonKitPartGroup( lDs );
   }


   /**
    * Tests kit contents
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchForKitOnly() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", 4650 );
      lArgs.add( "aTaskId", 23749 );
      lArgs.add( "aOemPartNo", ( String ) null );
      lArgs.add( "aPartName", "kit" );
      lArgs.add( "aAssemblyCd", "ASSEMBLY_OR_COMMON_HARDWARE" );
      lArgs.add( "aBomPartCd", ( String ) null );
      lArgs.add( "aSearchMethod", "STARTS_WITH" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 2 rows
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // a kit part group
      lDs.next();
      assertKitPartGroup( lDs );
   }


   /**
    * Tests kit contents
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchForNonKit() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", 4650 );
      lArgs.add( "aTaskId", 23749 );
      lArgs.add( "aOemPartNo", ( String ) null );
      lArgs.add( "aPartName", "wbs" );
      lArgs.add( "aAssemblyCd", "ASSEMBLY_OR_COMMON_HARDWARE" );
      lArgs.add( "aBomPartCd", ( String ) null );
      lArgs.add( "aSearchMethod", "STARTS_WITH" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 2 rows
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // a non kit part group
      lDs.next();
      assertNonKitPartGroup( lDs );
   }


   /**
    * Tests kit contents
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchWithInvalidPartName() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", 4650 );
      lArgs.add( "aTaskId", 23749 );
      lArgs.add( "aOemPartNo", ( String ) null );
      lArgs.add( "aPartName", "xxxxx" );
      lArgs.add( "aAssemblyCd", "ASSEMBLY_OR_COMMON_HARDWARE" );
      lArgs.add( "aBomPartCd", ( String ) null );
      lArgs.add( "aSearchMethod", "STARTS_WITH" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 2 rows
      MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   @Test
   public void testRemoveBoolTrueWhenSerializedPart() {

      // Ensure the test part exists.
      MxAssert.assertEquals(
            "Test data does not contain part with oem part no = " + SER_OEM_PART_NO,
            SER_OEM_PART_NO, EqpPartNoTable.findByPrimaryKey( SER_PART_NO_KEY ).getOEMPartNo() );

      // Ensure the test part group exists and has an inventory type of serialized.
      MxAssert.assertEquals(
            "Test data does not contain part group with inv class = " + RefInvClassKey.SER,
            RefInvClassKey.SER, EqpBomPart.findByPrimaryKey( SER_PART_GROUP_KEY ).getInvClass() );

      // Ensure the test part belongs to the SER test part group.
      MxAssert.assertTrue(
            "Test data does not have Part No = " + SER_PART_NO_KEY + " belonging to part group = "
                  + SER_PART_GROUP_KEY,
            EqpPartBaselineTable
                  .findByPrimaryKey( new EqpPartBaselineKey( SER_PART_GROUP_KEY, SER_PART_NO_KEY ) )
                  .exists() );

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", TASK_TASK_KEY.getDbId() );
      lArgs.add( "aTaskId", TASK_TASK_KEY.getId() );
      lArgs.add( "aOemPartNo", SER_OEM_PART_NO );
      lArgs.add( "aPartName", ( String ) null );
      lArgs.add( "aAssemblyCd", "ASSEMBLY_OR_COMMON_HARDWARE" );
      lArgs.add( "aBomPartCd", ( String ) null );
      lArgs.add( "aSearchMethod", "STARTS_WITH" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertTrue( "Expected query to return a row.", lDs.next() );
      assertEquals( "Expected query to return one row.", 1, lDs.getRowCount() );
      assertTrue( "Expected query to return a remove_bool set to true.",
            lDs.getBoolean( "remove_bool" ) );

   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), AddPartRequirementSearchTest.class,
            AddPartRequirementSearchTestData.getDataFile() );
   }


   /**
    * asserts that the data set contains a kit part group
    *
    * @param aDs
    *           the data set to process
    */
   private void assertKitPartGroup( DataSet aDs ) {
      assertRow( aDs, "4650:33368:4650:30126", "4650:33368", "KITSLE", "KItSLE", "4650:30126",
            "KITSLE", true, false, "1", "EA", "0", false, true, true, true );
   }


   /**
    * asserts that the data set contains a non kit part group
    *
    * @param aDs
    *           the data set to process
    */
   private void assertNonKitPartGroup( DataSet aDs ) {
      assertRow( aDs, "4650:33042:4650:21206", "4650:33042", "WBS-C-1.7.5.1.2.SER",
            "WBS-C-1.7.5.1.2 Turn Ins SER Part", "4650:21206", "WBS-C-1.7.5.1.2.SER", false, false,
            "1", "EA", "0", true, true, true, false );
   }


   /**
    * assert the data set
    *
    * @param aDs
    *           the data set
    * @param aPartBaselineKey
    *           Task Part List Key
    * @param aBomPartKey
    *           Req Qt
    * @param aBomPartCd
    *           Remove Bool
    * @param aBomPartName
    *           Part Provider Type Cd
    * @param aPartNoKey
    *           Bom Part Key
    * @param aPartNoOem
    *           Bom Part
    * @param aStandardBool
    *           Eqp Pos Cd
    * @param aTrackedBool
    *           Remove Reason Key
    * @param aReqQt
    *           Remove Reason
    * @param aQtyUnitCd
    *           Req Priority Key
    * @param aDecimalPlacesQt
    *           Req Priority
    * @param aRemoveBool
    *           Part Provider Type
    * @param aInstallBool
    *           Install Bool
    * @param aRequestBool
    *           Request Bool
    * @param aKitGroupBool
    *           Part Key
    */

   private void assertRow( DataSet aDs, String aPartBaselineKey, String aBomPartKey,
         String aBomPartCd, String aBomPartName, String aPartNoKey, String aPartNoOem,
         boolean aStandardBool, boolean aTrackedBool, String aReqQt, String aQtyUnitCd,
         String aDecimalPlacesQt, boolean aRemoveBool, boolean aInstallBool, boolean aRequestBool,
         boolean aKitGroupBool ) {
      MxAssert.assertEquals( "part_baseline_key", aPartBaselineKey,
            aDs.getString( "part_baseline_key" ) );
      MxAssert.assertEquals( "bom_part_key", aBomPartKey, aDs.getString( "bom_part_key" ) );
      MxAssert.assertEquals( "bom_part_cd", aBomPartCd, aDs.getString( "bom_part_cd" ) );
      MxAssert.assertEquals( "bom_part_name", aBomPartName, aDs.getString( "bom_part_name" ) );
      MxAssert.assertEquals( "part_no_key", aPartNoKey, aDs.getString( "part_no_key" ) );
      MxAssert.assertEquals( "part_no_oem", aPartNoOem, aDs.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "standard_bool", aStandardBool, aDs.getBoolean( "standard_bool" ) );
      MxAssert.assertEquals( "tracked_bool", aTrackedBool, aDs.getBoolean( "tracked_bool" ) );
      MxAssert.assertEquals( "req_qt", aReqQt, aDs.getString( "req_qt" ) );
      MxAssert.assertEquals( "qty_unit_cd", aQtyUnitCd, aDs.getString( "qty_unit_cd" ) );
      MxAssert.assertEquals( "decimal_places_qt", aDecimalPlacesQt,
            aDs.getString( "decimal_places_qt" ) );
      MxAssert.assertEquals( "remove_bool", aRemoveBool, aDs.getBoolean( "remove_bool" ) );
      MxAssert.assertEquals( "install_bool", aInstallBool, aDs.getBoolean( "install_bool" ) );
      MxAssert.assertEquals( "request_bool", aRequestBool, aDs.getBoolean( "request_bool" ) );
      MxAssert.assertEquals( "kit_group_bool", aKitGroupBool, aDs.getBoolean( "kit_group_bool" ) );
   }
}

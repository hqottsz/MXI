
package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InvParmDataBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.InventoryParmDataKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefDataTypeAssmblClassKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.assembly.RefDataTypeAssmblClass;
import com.mxi.mx.core.table.eqp.EqpAssmbl;
import com.mxi.mx.core.table.mim.MimDataType;


/**
 * This class tests the com.mxi.mx.web.query.task.AddMeasurementsForCorrectiveTask.qrx and
 * AddMeasurementsForNewCorrectiveTask queries.
 *
 * @author rrear
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AddMeasurementsForCorrectiveTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final DataTypeKey EXPECTED_DATA_TYPE = new DataTypeKey( 4650, 3013 );
   private static final RefAssmblClassKey EXPECTED_ASSMBL_CLASS = new RefAssmblClassKey( 0, "ENG" );

   private static final String QUERY_NEW_TASK = "AddMeasurementsForNewCorrectiveTask";
   private static final String QUERY_OLD_TASK = "AddMeasurementsForCorrectiveTask";

   private final RefDomainTypeKey iMeDomainKey = new RefDomainTypeKey( 0, "ME" );
   private final RefDomainTypeKey iCMEDomainKey = new RefDomainTypeKey( 0, "CME" );
   private final RefEngUnitKey iEngOilUnitKey = new RefEngUnitKey( 0, "LITRES" );

   private DataTypeKey iEngOilKey = null;
   private DataTypeKey iCalAssKey = null;


   void setup() {
      iEngOilKey = MimDataType.create( "ENGOIL", "Engine Oil Uptake", iMeDomainKey, iEngOilUnitKey,
            2, "" );
      iCalAssKey =
            MimDataType.create( "CAL", "CAL (Calendar Measurement)", iCMEDomainKey, null, 0, "" );

      RefDataTypeAssmblClass
            .create( new RefDataTypeAssmblClassKey( EXPECTED_DATA_TYPE, EXPECTED_ASSMBL_CLASS ) );
   }


   @Test
   public void testAssemblyMeasurementWithSavedTask() {

      setup();
      InventoryKey lHInventory =
            new InventoryBuilder().withDescription( "72-00-00 - ENGINE" ).build();
      AssemblyKey lAssKey = new AssemblyKey( 5001, "CFM56" );
      EqpAssmbl lEqpAssembly = new EqpAssmbl( lAssKey );
      lEqpAssembly.setRefAssmblClass( EXPECTED_ASSMBL_CLASS );

      InventoryKey lInv =
            new InventoryBuilder().withDescription( "(1.LT) CFM56-3C (PN: CFM56-3C, SN: XXX)" )
                  .withHighestInventory( lHInventory ).withOriginalAssembly( lAssKey ).build();
      TaskKey lTask = new TaskBuilder().onInventory( lInv ).build();

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lTask, "aEventDbId", "aEventId" );

      DataSet lDataSet = execute( QUERY_OLD_TASK, lArgs );

      // Assert number of rows returned

      assertEquals( 2, lDataSet.getRowCount() );

      /* Insert a measurement for the task. The query should then exclude it. */
      InventoryParmDataKey lParmDataKey = new InvParmDataBuilder().withInventoryKey( lInv )
            .withEventKey( lTask.getEventKey() ).withDataType( iEngOilKey ).build();

      lDataSet = execute( QUERY_OLD_TASK, lArgs );

      assertEquals( 1, lDataSet.getRowCount() );

      lDataSet.next();
      assertEquals( "DATA_TYPE_KEY",
            lDataSet.getString( "DATA_TYPE_DB_ID" ) + ":" + lDataSet.getString( "DATA_TYPE_ID" ),
            lDataSet.getString( "DATA_TYPE_KEY" ) );
      switch ( lDataSet.getString( "DOMAIN_TYPE_CD" ) ) {
         case "ME":
            fail( "Existing measurement type should be excluded: "
                  + lDataSet.getString( "MEASUREMENT_PARAMETER" ) );
            break;
         case "CME":
            MimDataType lCal = MimDataType.findByPrimaryKey( iCalAssKey );
            assertEquals( "Measurement Parameter",
                  lCal.getDataTypeCd() + " (" + lCal.getDataTypeSdesc() + ")",
                  lDataSet.getString( "MEASUREMENT_PARAMETER" ) );
            assertEquals( "Precision Quantity", MimDataType.getEntryPrecQt( iCalAssKey ),
                  lDataSet.getInteger( "ENTRY_PREC_QT" ).intValue() );
            assertNull( "Unit Code", lDataSet.getString( "ENG_UNIT_CD" ) );
            break;
         default:
            fail( "Unknown Domain Type returned: " + lDataSet.getString( "DOMAIN_TYPE_CD" ) );
      }

   }


   /**
    * Test Case 1: Test that the query returns same assembly measurement with inventories
    * corresponding to multiple positions under a config slot the assembly measurement is applicable
    * to.
    */
   @Test
   public void testAssemblyMeasurementWithNewTask() {

      setup();
      DataSet lDataSet = execute( QUERY_NEW_TASK );

      // Assert number of rows returned

      assertEquals( 2, lDataSet.getRowCount() );

      while ( lDataSet.next() ) {
         assertEquals( "DATA_TYPE_KEY",
               lDataSet.getString( "DATA_TYPE_DB_ID" ) + ":" + lDataSet.getString( "DATA_TYPE_ID" ),
               lDataSet.getString( "DATA_TYPE_KEY" ) );
         switch ( lDataSet.getString( "DOMAIN_TYPE_CD" ) ) {
            case "ME":
               MimDataType lEngOil = MimDataType.findByPrimaryKey( iEngOilKey );
               assertEquals( "Measurement Parameter",
                     lEngOil.getDataTypeCd() + " (" + lEngOil.getDataTypeSdesc() + ")",
                     lDataSet.getString( "MEASUREMENT_PARAMETER" ) );
               assertEquals( "Precision Quantity", MimDataType.getEntryPrecQt( iEngOilKey ),
                     lDataSet.getInteger( "ENTRY_PREC_QT" ).intValue() );
               assertEquals( "Unit Code", iEngOilUnitKey.getCd(),
                     lDataSet.getString( "ENG_UNIT_CD" ) );
               break;
            case "CME":
               MimDataType lCal = MimDataType.findByPrimaryKey( iCalAssKey );
               assertEquals( "Measurement Parameter",
                     lCal.getDataTypeCd() + " (" + lCal.getDataTypeSdesc() + ")",
                     lDataSet.getString( "MEASUREMENT_PARAMETER" ) );
               assertEquals( "Precision Quantity", MimDataType.getEntryPrecQt( iCalAssKey ),
                     lDataSet.getInteger( "ENTRY_PREC_QT" ).intValue() );
               assertNull( "Unit Code", lDataSet.getString( "ENG_UNIT_CD" ) );
               break;
            default:
               fail( "Unknown Domain Type returned: " + lDataSet.getString( "DOMAIN_TYPE_CD" ) );
         }
      }

   }


   /**
    * Executes the query.
    *
    * @param aKey
    *           the task key.
    *
    * @return {@link DataSet}
    */
   private DataSet execute( String aQueryName, DataSetArgument aArgs ) {
      // Query to use is one of two, existing in the same package relative to this test.
      String lPath = QueryExecutor.getQueryName( getClass() );
      lPath = lPath.substring( 0, lPath.lastIndexOf( "." ) + 1 );

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            lPath + aQueryName, aArgs );
   }


   private DataSet execute( String aQueryName ) {
      // Build the query arguments
      DataSetArgument lNoArgs = new DataSetArgument();

      return execute( aQueryName, lNoArgs );
   }
}

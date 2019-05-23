
package com.mxi.mx.core.query.inventory.reservation;

import static org.junit.Assert.fail;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefSupplyChainKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ListAlternatePartRequestsFromBomPartTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenario() throws Exception {
      runTestScenario( new ScenarioInvalidStatusReq(), ScenarioInvalidStatusReq.KEY_BOM_PART,
            ScenarioInvalidStatusReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioHistoricReq() throws Exception {
      runTestScenario( new ScenarioHistoricReq(), ScenarioHistoricReq.KEY_BOM_PART,
            ScenarioHistoricReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioHistoricTask() throws Exception {
      runTestScenario( new ScenarioHistoricTask(), ScenarioHistoricTask.KEY_BOM_PART,
            ScenarioHistoricTask.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioInvPredrawn() throws Exception {
      runTestScenario( new ScenarioInvPredrawn(), ScenarioInvPredrawn.KEY_BOM_PART,
            ScenarioInvPredrawn.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioInvPredrBin() throws Exception {
      runTestScenario( new ScenarioInvPredrBin(), ScenarioInvPredrBin.KEY_BOM_PART,
            ScenarioInvPredrBin.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioLocalStockReq() throws Exception {
      runTestScenario( new ScenarioLocalStockReq(), ScenarioLocalStockReq.KEY_BOM_PART,
            ScenarioLocalStockReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioLockedReq() throws Exception {
      runTestScenario( new ScenarioLockedReq(), ScenarioLockedReq.KEY_BOM_PART,
            ScenarioLockedReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioNonScheduledReq() throws Exception {
      runTestScenario( new ScenarioNonScheduledReq(), ScenarioNonScheduledReq.KEY_BOM_PART,
            ScenarioNonScheduledReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioNullInvReq() throws Exception {
      runTestScenario( new ScenarioNullInvReq(), ScenarioNullInvReq.KEY_BOM_PART,
            ScenarioNullInvReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioNullTaskReq() throws Exception {
      runTestScenario( new ScenarioNullTaskReq(), ScenarioNullTaskReq.KEY_BOM_PART,
            ScenarioNullTaskReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioQuarNonRFIReq() throws Exception {
      runTestScenario( new ScenarioQuarNonRFIReq(), ScenarioQuarNonRFIReq.KEY_BOM_PART,
            ScenarioQuarNonRFIReq.KEY_LOCATION );
   }


   /**
    * Tests that the query does not return part-requests that have on-order status
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioOnOrderReq() throws Exception {
      runTestScenario( new ScenarioOnOrderReq(), ScenarioOnOrderReq.KEY_BOM_PART,
            ScenarioOnOrderReq.KEY_LOCATION );
   }


   /**
    * Tests that the query does not return part-requests that have POREQ status
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioPOREQStatusReq() throws Exception {
      runTestScenario( new ScenarioPOREQStatusReq(), ScenarioPOREQStatusReq.KEY_BOM_PART,
            ScenarioPOREQStatusReq.KEY_LOCATION );
   }


   /**
    * Tests that the query does not return part-requests that have been issued already
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioIssuedReq() throws Exception {
      runTestScenario( new ScenarioIssuedReq(), ScenarioIssuedReq.KEY_BOM_PART,
            ScenarioIssuedReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioQuarNullInvReq() throws Exception {
      runTestScenario( new ScenarioQuarNullInvReq(), ScenarioQuarNullInvReq.KEY_BOM_PART,
            ScenarioQuarNullInvReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioQuarRFIInvReq() throws Exception {
      runTestScenario( new ScenarioQuarRFIInvReq(), ScenarioQuarRFIInvReq.KEY_BOM_PART,
            ScenarioQuarRFIInvReq.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioRemoteReqNonStock() throws Exception {
      runTestScenario( new ScenarioRemoteReqNonStock(), ScenarioRemoteReqNonStock.KEY_BOM_PART,
            ScenarioRemoteReqNonStock.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryScenarioRemoteReqStock() throws Exception {
      runTestScenario( new ScenarioRemoteReqStock(), ScenarioRemoteReqStock.KEY_BOM_PART,
            ScenarioRemoteReqStock.KEY_LOCATION );
   }


   /**
    * Tests that the query returns the proper data for the specific Scenario. See the Scenario class
    * for details regarding the test scenario.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQueryStandardScenario() throws Exception {
      runTestScenario( new StandardScenario(), StandardScenario.KEY_BOM_PART,
            StandardScenario.KEY_LOCATION );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aScenario
    *           DOCUMENT_ME
    * @param aPartGroup
    *           DOCUMENT_ME
    * @param aLocation
    *           DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   protected void runTestScenario( StandardScenario aScenario, PartGroupKey aPartGroup,
         LocationKey aLocation ) throws Exception {
      try {
         aScenario.setup();
         aScenario.validate( execute( aPartGroup, aLocation ) );
      } finally {
         aScenario.tearDown();
      }
   }


   /**
    * Execute the query.
    *
    * @param aPartGroup
    *           DOCUMENT ME!
    * @param aLocation
    *           DOCUMENT ME!
    *
    * @return dataSet result.
    */
   private DataSet execute( PartGroupKey aPartGroup, LocationKey aLocation ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartGroup, new String[] { "aBomPartDbId", "aBomPartId" } );
      lArgs.add( aLocation, new String[] { "aLocationDbId", "aLocationId" } );

      // Sort the Dataset
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.inventory.reservation.listAlternatePartRequestsFromBomPart",
            lArgs );

      // Execute the query
      return lDs;
   }


   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It is Historic</li>
    * </ul>
    */
   private static class ScenarioHistoricReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         EvtEventTable lReqEvent = EvtEventTable.findByPrimaryKey( KEY_PART_REQUEST.getEventKey() );
         lReqEvent.setHistBool( true );
         lReqEvent.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>The related Task is Historic</li>
    * </ul>
    */
   private static class ScenarioHistoricTask extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         EvtEventTable lTaskEvent = EvtEventTable.findByPrimaryKey( KEY_TASK.getEventKey() );
         lTaskEvent.setHistBool( true );
         lTaskEvent.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It has an invalid Status</li>
    * </ul>
    */
   private static class ScenarioInvalidStatusReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         EvtEventTable lReqEvent = EvtEventTable.findByPrimaryKey( KEY_PART_REQUEST.getEventKey() );
         lReqEvent.setEventStatus( RefEventStatusKey.PRONORDER );
         lReqEvent.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>The currently Reserved Inventory is Predrawn</li>
    * </ul>
    */
   private static class ScenarioInvPredrawn extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         InvLocTable lLocation = InvLocTable.findByPrimaryKey( KEY_LOCATION );
         lLocation.setLocType( RefLocTypeKey.PREDRAW );
         lLocation.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>The currently Reserved Inventory is Predrawn</li>
    * </ul>
    */
   private static class ScenarioInvPredrBin extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         InvLocTable lLocation = InvLocTable.findByPrimaryKey( KEY_LOCATION );
         lLocation.setLocType( RefLocTypeKey.PREDRBIN );
         lLocation.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It is locally needed and of type Stock</li>
    * </ul>
    */
   private static class ScenarioLocalStockReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST );
         lReqPart.setReqType( RefReqTypeKey.STOCK );
         lReqPart.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It is Locked</li>
    * </ul>
    */
   private static class ScenarioLockedReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST );
         lReqPart.setLockReserveBool( true );
         lReqPart.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It is not scheduled</li>
    * </ul>
    */
   private static class ScenarioNonScheduledReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST );
         lReqPart.setReqLocation( null );
         lReqPart.setRemoteLoc( null );
         lReqPart.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It has on-order status</li>
    * </ul>
    */
   private static class ScenarioOnOrderReq extends StandardScenario {

      /**
       * Creates the data
       */
      @Override
      public void setup() {
         super.setup();

         EvtEventTable lReqEvent = EvtEventTable.findByPrimaryKey( KEY_PART_REQUEST.getEventKey() );
         lReqEvent.setEventStatus( RefEventStatusKey.PRONORDER );
         lReqEvent.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It has POREQ status</li>
    * </ul>
    */
   private static class ScenarioPOREQStatusReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST );
         EvtEventTable lReqEvent = EvtEventTable.findByPrimaryKey( KEY_PART_REQUEST.getEventKey() );
         lReqEvent.setEventStatus( RefEventStatusKey.PRPOREQ );
         lReqEvent.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It is issued already</li>
    * </ul>
    */
   private static class ScenarioIssuedReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         EvtEventTable lReqEvent = EvtEventTable.findByPrimaryKey( KEY_PART_REQUEST.getEventKey() );
         lReqEvent.setEventStatus( RefEventStatusKey.POISSUED );
         lReqEvent.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request where:
    *
    * <ul>
    * <li>It has no reserved inventory</li>
    * </ul>
    */
   private static class ScenarioNullInvReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST );
         lReqPart.setInventory( null );
         lReqPart.update();
      }
   }

   /**
    * Scenario of valid Part Request where:
    *
    * <ul>
    * <li>It has no reserved inventory</li>
    * </ul>
    */
   private static class ScenarioNullTaskReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST );
         lReqPart.setTask( null );
         lReqPart.update();
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It Quarantined</li>
    * <li>The Reserved Inventory is not RFI</li>
    * </ul>
    */
   private static class ScenarioQuarNonRFIReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         EvtEventTable lReqEvent = EvtEventTable.findByPrimaryKey( KEY_PART_REQUEST.getEventKey() );
         lReqEvent.setEventStatus( RefEventStatusKey.PRQUAR );
         lReqEvent.update();

         InvInvTable lInvInv = InvInvTable.findByPrimaryKey( KEY_INVENTORY );
         lInvInv.setInvCond( RefInvCondKey.QUAR );
         lInvInv.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request except:
    *
    * <ul>
    * <li>It Quarantined</li>
    * <li>The Reserved Inventory is null</li>
    * </ul>
    */
   private static class ScenarioQuarNullInvReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         EvtEventTable lReqEvent = EvtEventTable.findByPrimaryKey( KEY_PART_REQUEST.getEventKey() );
         lReqEvent.setEventStatus( RefEventStatusKey.PRQUAR );
         lReqEvent.update();

         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST );
         lReqPart.setInventory( null );
         lReqPart.update();
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      @Override
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that no rows are returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, aDataSet.getRowCount() );
      }
   }

   /**
    * Scenario of valid Part Request where:
    *
    * <ul>
    * <li>It Quarantined</li>
    * <li>The Reserved Inventory is RFI</li>
    * </ul>
    */
   private static class ScenarioQuarRFIInvReq extends StandardScenario {

      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         EvtEventTable lReqEvent = EvtEventTable.findByPrimaryKey( KEY_PART_REQUEST.getEventKey() );
         lReqEvent.setEventStatus( RefEventStatusKey.PRQUAR );
         lReqEvent.update();
      }
   }

   /**
    * Scenario of valid Part Request where:
    *
    * <ul>
    * <li>It is Remote</li>
    * <li>It is a Non-Stock Request</li>
    * </ul>
    */
   private static class ScenarioRemoteReqNonStock extends StandardScenario {

      /** DOCUMENT ME! */
      private static final LocationKey KEY_LOCATION2 = new LocationKey( 5845, 2541 );


      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         // Create a Second Location
         InvLocTable lLocation = InvLocTable.create( KEY_LOCATION2 );
         lLocation.setSupplyLoc( KEY_LOCATION2 );
         lLocation.setSupplyBool( true );
         lLocation.setLocType( RefLocTypeKey.AIRPORT );
         lLocation.insert();

         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST );
         lReqPart.setReqType( RefReqTypeKey.ADHOC );
         lReqPart.setReqLocation( KEY_LOCATION2 );
         lReqPart.setRemoteLoc( KEY_LOCATION );
         lReqPart.update();
      }


      /**
       * Deletes the database data
       */
      @Override
      public void tearDown() {
         super.tearDown();
         try {
            InvLocTable.findByPrimaryKey( KEY_LOCATION2 ).delete();
         } catch ( Exception e ) {
            fail( "Error clearing data. Check first failure/error in test class" );
         }
      }
   }

   /**
    * Scenario of valid Part Request where:
    *
    * <ul>
    * <li>It is Remote</li>
    * <li>It is a Stock Request</li>
    * </ul>
    */
   private static class ScenarioRemoteReqStock extends StandardScenario {

      /** DOCUMENT ME! */
      private static final LocationKey KEY_LOCATION2 = new LocationKey( 5845, 2541 );


      /**
       * Creates the database data
       */
      @Override
      public void setup() {
         super.setup();

         // Create a Second Location
         InvLocTable lLocation = InvLocTable.create( KEY_LOCATION2 );
         lLocation.setSupplyLoc( KEY_LOCATION2 );
         lLocation.setSupplyBool( true );
         lLocation.setLocType( RefLocTypeKey.AIRPORT );
         lLocation.insert();

         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST );
         lReqPart.setReqType( RefReqTypeKey.STOCK );
         lReqPart.setReqLocation( KEY_LOCATION2 );
         lReqPart.setRemoteLoc( KEY_LOCATION );
         lReqPart.update();
      }


      /**
       * Deletes the database data
       */
      @Override
      public void tearDown() {
         super.tearDown();
         try {
            InvLocTable.findByPrimaryKey( KEY_LOCATION2 ).delete();
         } catch ( Exception e ) {
            fail( "Error clearing data. Check first failure/error in test class" );
         }
      }
   }

   /**
    * Basic Scenario of Part Request that:
    *
    * <ul>
    * <li>Not Locked</li>
    * <li>Local to requested location</li>
    * <li>Non Historic</li>
    * <li>Has Reserved Inventory</li>
    * <li>Reserved Inventory not located at Predraw location</li>
    * <li>Non Stock type Request</li>
    * <li>Has a related non-historic Task</li>
    * <li></li>
    * </ul>
    */
   private static class StandardScenario {

      /** DOCUMENT ME! */
      public static final PartGroupKey KEY_BOM_PART = new PartGroupKey( 4650, 1011 );

      /** DOCUMENT ME! */
      protected static final InventoryKey KEY_INVENTORY = new InventoryKey( 3, 3 );

      /** DOCUMENT ME! */
      protected static final PartNoKey KEY_PART_NO = new PartNoKey( 465, 1123 );

      /** DOCUMENT ME! */
      protected static final EqpPartBaselineKey KEY_EQP_PART_BASELINE =
            new EqpPartBaselineKey( KEY_BOM_PART, KEY_PART_NO );

      /** DOCUMENT ME! */
      public static final LocationKey KEY_LOCATION = new LocationKey( 1010, 320 );

      /** DOCUMENT ME! */
      protected static final TaskKey KEY_TASK = new TaskKey( 25, 5214 );

      /** DOCUMENT ME! */
      protected static final PartRequestKey KEY_PART_REQUEST = new PartRequestKey( 1888, 2254 );


      /**
       * Creates the database data
       */
      public void setup() {
         try {

            // Create a Location to be tested
            InvLocTable lLocation = InvLocTable.create( KEY_LOCATION );
            lLocation.setSupplyLoc( KEY_LOCATION );
            lLocation.setSupplyBool( true );
            lLocation.setLocType( RefLocTypeKey.AIRPORT );
            lLocation.insert();

            // Create the Part and Part Group to be tested
            EqpPartNoTable lPart = EqpPartNoTable.create( KEY_PART_NO );
            lPart.setQtyUnit( RefQtyUnitKey.EA );
            lPart.setControlledReserveBool( false );
            lPart.setNoAutoReserveBool( false );
            lPart.setQtyUnit( RefQtyUnitKey.EA );
            lPart.insert();

            EqpPartBaselineTable lPartGroup = EqpPartBaselineTable.create( KEY_EQP_PART_BASELINE );
            lPartGroup.insert();

            // Create a Task for the Request
            SchedStaskTable lTask = SchedStaskTable.create( KEY_TASK );
            lTask.insert();

            EvtEventTable lTaskEvent = EvtEventTable.create( KEY_TASK.getEventKey() );
            lTaskEvent.setHistBool( false );
            lTaskEvent.insert();

            // Create a single Inventory at the location
            InvInvTable lInventory = InvInvTable.create( KEY_INVENTORY );
            lInventory.setInvCond( RefInvCondKey.RFI );
            lInventory.setLocation( KEY_LOCATION );
            lInventory.insert();

            // Create a Part Request
            ReqPartTable lReqPart = ReqPartTable.create( KEY_PART_REQUEST );
            lReqPart.setReqLocation( KEY_LOCATION );
            lReqPart.setRemoteLoc( null );
            lReqPart.setInventory( KEY_INVENTORY );
            lReqPart.setReqBomPart( KEY_BOM_PART );
            lReqPart.setTask( KEY_TASK );
            lReqPart.setReqType( RefReqTypeKey.ADHOC );
            lReqPart.setLockReserveBool( false );
            lReqPart.setReqPriority( RefReqPriorityKey.NORMAL );
            lReqPart.setSupplyChain( RefSupplyChainKey.DEFAULT );
            lReqPart.setQuantityUnit( RefQtyUnitKey.EA );
            lReqPart.insert();

            EvtEventTable lReqEvent = EvtEventTable.create( KEY_PART_REQUEST.getEventKey() );
            lReqEvent.setEventStatus( RefEventStatusKey.PRAVAIL );
            lReqEvent.setHistBool( false );
            lReqEvent.insert();
         } catch ( Exception e ) {
            fail( "Error creating data. Check first failure/error in test class" );
         }
      }


      /**
       * Deletes the database data
       */
      public void tearDown() {
         try {
            InvInvTable.findByPrimaryKey( KEY_INVENTORY ).delete();

            InvLocTable.findByPrimaryKey( KEY_LOCATION ).delete();
            EqpPartNoTable.findByPrimaryKey( KEY_PART_NO ).delete();
            EqpPartBaselineTable.findByPrimaryKey( KEY_EQP_PART_BASELINE ).delete();
            SchedStaskTable.findByPrimaryKey( KEY_TASK ).delete();
            EvtEventTable.findByPrimaryKey( KEY_TASK.getEventKey() ).delete();
            ReqPartTable.findByPrimaryKey( KEY_PART_REQUEST ).delete();
            EvtEventTable.findByPrimaryKey( KEY_PART_REQUEST.getEventKey() ).delete();
         } catch ( Exception e ) {
            fail( "Error clearing data. Check first failure/error in test class" );
         }
      }


      /**
       * Validates the DataSet
       *
       * @param aDataSet
       *           DataSet results
       *
       * @throws Exception
       *            If an error occurs
       */
      public void validate( DataSet aDataSet ) throws Exception {

         // Ensure that row is returned
         MxAssert.assertEquals( "Number of retrieved rows", 1, aDataSet.getRowCount() );

         // Test the first row
         aDataSet.next();
         MxAssert.assertEquals( "part_request_key", KEY_PART_REQUEST,
               aDataSet.getString( "part_request_key" ) );
      }
   }
}

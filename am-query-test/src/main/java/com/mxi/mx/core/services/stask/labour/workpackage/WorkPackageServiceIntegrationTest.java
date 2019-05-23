package com.mxi.mx.core.services.stask.labour.workpackage;

import static com.mxi.am.domain.Domain.createLocation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.ibm.icu.util.Calendar;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.AdhocTask;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventLocationKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.plugin.ordernumber.RepairOrderNumberGenerator;
import com.mxi.mx.core.plugin.ordernumber.WorkOrderNumberGenerator;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtLocTable;
import com.mxi.mx.core.trigger.MxCoreTriggerType;


public class WorkPackageServiceIntegrationTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( 0, "SYSTEM" );

   // Object under test
   private WorkPackageService iWorkPackageService;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      Map<String, Object> lTriggerMap = new HashMap<>( 2 );
      {
         lTriggerMap.put( MxCoreTriggerType.WO_NUM_GEN.toString(), new WorkOrderNumberGenerator() );
         lTriggerMap.put( MxCoreTriggerType.RO_NUM_GEN.toString(),
               new RepairOrderNumberGenerator() );
      }
      TriggerFactory lTriggerFactoryStub = new TriggerFactoryStub( lTriggerMap );
      TriggerFactory.setInstance( lTriggerFactoryStub );

      iWorkPackageService = new WorkPackageService();
   }


   @Test
   public void createQuickCheck_validWorkPackageTO() throws Throwable {

      InventoryKey lAircraft = Domain.createAircraft();
      TaskKey lAdhocTask = Domain.createAdhocTask( new DomainConfiguration<AdhocTask>() {

         @Override
         public void configure( AdhocTask aBuilder ) {
            aBuilder.setInventory( lAircraft );
         }

      } );

      String lWorkPackageName = "Test Work Package";
      Date lStartDate = new Date();
      Date lEndDate = new Date();
      LocationKey lLocation = createWPLocation();

      CreateWorkPackageTO lWorkPackageTO = new CreateWorkPackageTO();
      {
         lWorkPackageTO.setName( lWorkPackageName );
         lWorkPackageTO.setTaskKeys( Arrays.asList( lAdhocTask ) );
         lWorkPackageTO.setStartDate( lStartDate );
         lWorkPackageTO.setEndDate( lEndDate );
         lWorkPackageTO.setWorkLocation( lLocation );
      }

      TaskKey lWorkPackageKey = iWorkPackageService.createQuickCheck( lWorkPackageTO );

      WorkPackageData lActualWPData = getWorkPackageData( lWorkPackageKey );
      assertEquals( lWorkPackageName, lActualWPData.iName );
      assertTrue(
            DateUtils.truncatedEquals( lStartDate, lActualWPData.iStartDate, Calendar.SECOND ) );
      assertTrue( DateUtils.truncatedEquals( lEndDate, lActualWPData.iEndDate, Calendar.SECOND ) );
      assertEquals( lLocation, lActualWPData.iLocation );
   }


   /* Retrieves basic work package data from a given key */
   private WorkPackageData getWorkPackageData( TaskKey aWorkPackageKey ) {
      DataSetArgument lEventTableArgs = new DataSetArgument();
      {
         lEventTableArgs.add( aWorkPackageKey, EvtEventDao.ColumnName.EVENT_DB_ID.name(),
               EvtEventDao.ColumnName.EVENT_ID.name() );
      }
      QuerySet lEventQs =
            QuerySetFactory.getInstance().executeQueryTable( EventKey.TABLE_NAME, lEventTableArgs );
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.add( aWorkPackageKey, EvtLocTable.ColumnName.EVENT_DB_ID.name(),
               EvtEventDao.ColumnName.EVENT_ID.name() );
      }
      QuerySet lLocationQs =
            QuerySetFactory.getInstance().executeQueryTable( EventLocationKey.TABLE_NAME, lArgs );

      assertTrue( String.format(
            "Expected to have work package data for TaskKey %s but the event data could not be retrieved.",
            aWorkPackageKey ), lEventQs.next() );
      assertTrue( String.format(
            "Expected to have work package data for TaskKey %s but the location data could not be retrieved.",
            aWorkPackageKey ), lLocationQs.next() );

      WorkPackageData lData = new WorkPackageData();
      {
         lData.iName = lEventQs.getString( EvtEventDao.ColumnName.EVENT_SDESC.name() );
         lData.iLocation = lLocationQs.getKey( LocationKey.class,
               EvtLocTable.ColumnName.LOC_DB_ID.name(), EvtLocTable.ColumnName.LOC_ID.name() );
         lData.iStartDate = lEventQs.getDate( EvtEventDao.ColumnName.SCHED_START_DT.name() );
         lData.iEndDate = lEventQs.getDate( EvtEventDao.ColumnName.SCHED_END_DT.name() );
      }
      return lData;
   }


   private LocationKey createWPLocation() {
      LocationKey airportLocation = createLocation( airportLoc -> {
         airportLoc.setType( RefLocTypeKey.AIRPORT );
         airportLoc.setIsSupplyLocation( true );
      } );

      return createLocation( workPackageLoc -> {
         workPackageLoc.setCode( "Work package Location" );
         workPackageLoc.setType( RefLocTypeKey.LINE );
         workPackageLoc.setParent( airportLocation );
         workPackageLoc.setSupplyLocation( airportLocation );
      } );
   }


   private static class WorkPackageData {

      private String iName;
      private Date iStartDate;
      private Date iEndDate;
      private LocationKey iLocation;

   }

}

package com.mxi.mx.core.query.event;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.table.acevent.InvCndChgEventTable;
import com.mxi.mx.core.table.acevent.JdbcInvCndChgEventDao;


/**
 * This test class tests the GetLastInventoryConditionChangeEvent.qrx It does not test the services
 * that are used in this test (services are used for setting up of data for the test).
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetLastInventoryConditionChangeEventTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final String NOTE = "User note";

   private HumanResourceKey iHumanResource;
   private LocationKey iSupplyLocation;
   private LocationKey iQuarantineLocation;
   private OwnerKey iLocalOwner;


   @Before
   public void setUp() {

      iSupplyLocation = Domain.createLocation( aSupplyLocation -> {
         aSupplyLocation.setCode( "YYZ" );
         aSupplyLocation.setIsSupplyLocation( true );
      } );

      iQuarantineLocation = Domain.createLocation( aQuarantineLocation -> {
         aQuarantineLocation.setCode( "YYZ/QUAR" );
         aQuarantineLocation.setType( RefLocTypeKey.QUAR );
      } );

      iLocalOwner = Domain.createOwner();

      iHumanResource = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setSupplyLocations( iSupplyLocation );
      } );

   }


   @Test
   public void testLastInventoryConditionChangeEventRetrieved()
         throws MxException, TriggerException {
      InventoryKey lInventory = createInventoryWithCondition( RefInvCondKey.INSPREQ );

      InventoryKey lNewInventory = null;

      // Quarantine the inventory
      InventoryServiceFactory.getInstance().getConditionService().quarantine( lInventory,
            iQuarantineLocation, null, NOTE, iHumanResource );

      DataSet lDs = executeQuery( lInventory );
      assertExist( lDs );
      lDs.next();

      InvCndChgEventKey lInventoryEventKey = new InvCndChgEventKey( lDs.getString( "event_key" ) );

      InvCndChgEventTable lInvCndChgEventTable =
            new JdbcInvCndChgEventDao().findByPrimaryKey( lInventoryEventKey );

      assertEquals( "Unfortunately, Wrong inventory event is fetched.",
            RefEventStatusKey.ACQUAR.getCd(), lInvCndChgEventTable.getEventStatusCd() );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "INV_CND_CHG_INV",
            lInventoryEventKey.getPKWhereArg() );

      while ( lQs.next() ) {
         lNewInventory = lQs.getKey( InventoryKey.class, "INV_NO_DB_ID", "INV_NO_ID" );
      }

      assertEquals( "Unfortunately, Wrong inventory is fetched.", lInventory, lNewInventory );

   }


   /**
    * Create an unserviceable inventory with a given condition.
    *
    * @param aInventoryCondition
    * @return the new inventory key
    */
   private InventoryKey createInventoryWithCondition( RefInvCondKey aInventoryCondition ) {
      return new InventoryBuilder().atLocation( iSupplyLocation ).withClass( RefInvClassKey.SER )
            .withCondition( aInventoryCondition ).withOwner( iLocalOwner ).build();
   }


   private DataSet executeQuery( InventoryKey aInvKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInvKey, "aInventoryDbId", "aInventoryId" );
      lArgs.add( "aEventStatusCd", RefEventStatusKey.ACQUAR.getCd() );
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      return lDs;
   }


   private void assertExist( DataSet aDs ) {
      if ( !aDs.hasNext() ) {
         Assert.fail( "There are no quarantined inventories." );
      }
      assertEquals( 1, aDs.getTotalRowCount() );
   }

}

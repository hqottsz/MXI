
package com.mxi.mx.core.query.timeline;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao;
import com.mxi.mx.core.table.quarantine.QuarQuarTable;


/**
 * This class tests the query com.mxi.mx.core.query.timeline.GetQuarEvents.qrx
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetQuarEventsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private final static String EVENT_DB_ID = "event_db_id";
   private final static String EVENT_ID = "event_id";


   /**
    * Given quarantine events on specified component
    *
    * When the query GetQuarEvents.qrx is executed
    *
    * Then all Quarantine AC events associated with the component are returned
    *
    */
   @Test
   public void testAllQuarantineAcEventsOnTheComponentAreReturnedByComponent() {

      // ARRANGE
      final InvCndChgEventDao lAcEventDao =
            InjectorContainer.get().getInstance( InvCndChgEventDao.class );
      // first AC event
      final InvCndChgEventKey lAcEventKey1 = lAcEventDao.generatePrimaryKey();
      createEvent( lAcEventKey1 );

      // second AC event
      final InvCndChgEventKey lAcEventKey2 = lAcEventDao.generatePrimaryKey();
      createEvent( lAcEventKey2 );

      // third AC event
      final InvCndChgEventKey lAcEventKey3 = lAcEventDao.generatePrimaryKey();
      createEvent( lAcEventKey3 );

      final InventoryKey lComponent1 = Domain.createTrackedInventory();
      final InventoryKey lComponent2 = Domain.createTrackedInventory();

      // first quarantine
      QuarQuarTable lQuarQuar = QuarQuarTable.create();
      lQuarQuar.setIsHistoric( true );
      lQuarQuar.setInventoryKey( lComponent1 );
      lQuarQuar.setEventKey( lAcEventKey1 );
      lQuarQuar.setAcEventKey( lAcEventKey1 );
      lQuarQuar.insert();

      // second quarantine
      lQuarQuar = QuarQuarTable.create();
      lQuarQuar.setInventoryKey( lComponent1 );
      lQuarQuar.setEventKey( lAcEventKey2 );
      lQuarQuar.setAcEventKey( lAcEventKey2 );
      lQuarQuar.insert();

      // third quarantine
      lQuarQuar = QuarQuarTable.create();
      lQuarQuar.setInventoryKey( lComponent2 );
      lQuarQuar.setEventKey( lAcEventKey3 );
      lQuarQuar.setAcEventKey( lAcEventKey3 );
      lQuarQuar.insert();

      // ACT
      final QuerySet lQs = executeQuery( lComponent1.getDbId(), lComponent1.getId() );

      // ASSERT
      assertTrue( lQs.getRowCount() == 2 );
   }


   private void createEvent( InvCndChgEventKey aEventKey ) {
      final DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aEventKey, EVENT_DB_ID, EVENT_ID );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_event", lArgs );
   }


   private QuerySet executeQuery( int aQuarDbId, int aQuarId ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aDbId", aQuarDbId );
      lArgs.add( "aId", aQuarId );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }
}


package com.mxi.mx.web.query.inventory.quarantine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

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
 * This class tests the query com.mxi.mx.web.query.qarantine.HistoricQuarantineInfo.qrx
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class HistoricQuarantineInfoTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   public enum QuarQuarColumName {
      QUAR_DB_ID, QUAR_ID, BARCODE, QUAR_NOTE, QUAR_GDT, HISTORIC_BOOL, INV_DB_ID, INV_ID,
      EVENT_DB_ID, EVENT_ID, AC_EVENT_DB_ID, AC_EVENT_ID

   }


   private final static String GUARANTINE_NOTE =
         "<b><u>Anders Hejlsberg, 16-MAY-2005 13:44</u></b><br/>testing data<br/>";
   private final static String GUARANTINE_BARCODE_1 = "Q0002JRQ";
   private final static String GUARANTINE_BARCODE_2 = "Q0003JRQ";


   /**
    * Given historic quarantine events on specified component
    *
    * When the query HistoricQuarantineInfo.qrx is executed
    *
    * Then the historic quarantine info associated with the component is returned
    *
    */
   @Test
   public void testGetHistoricQuarantineInfo() {

      // ARRANGE
      final InvCndChgEventDao lAcEventDao =
            InjectorContainer.get().getInstance( InvCndChgEventDao.class );
      // first AC event
      final InvCndChgEventKey lAcEventKey1 = lAcEventDao.generatePrimaryKey();
      createEvent( lAcEventKey1 );

      // second AC event
      final InvCndChgEventKey lAcEventKey2 = lAcEventDao.generatePrimaryKey();
      createEvent( lAcEventKey2 );

      final InventoryKey lComponent1 = Domain.createTrackedInventory();
      final InventoryKey lComponent2 = Domain.createTrackedInventory();

      // first quarantine
      final QuarQuarTable lQuarQuar1 = QuarQuarTable.create();
      lQuarQuar1.setIsHistoric( true );
      lQuarQuar1.setInventoryKey( lComponent1 );
      lQuarQuar1.setEventKey( lAcEventKey1 );
      lQuarQuar1.setAcEventKey( lAcEventKey1 );

      lQuarQuar1.setBarcodeSdesc( GUARANTINE_BARCODE_1 );
      final Calendar lQuarCalendar1 = Calendar.getInstance();
      lQuarCalendar1.add( Calendar.DATE, -5 );
      lQuarQuar1.setDate( lQuarCalendar1.getTime() );
      lQuarQuar1.setNote( GUARANTINE_NOTE );

      lQuarQuar1.insert();

      // second quarantine
      final QuarQuarTable lQuarQuar2 = QuarQuarTable.create();
      lQuarQuar1.setIsHistoric( true );
      lQuarQuar2.setInventoryKey( lComponent2 );
      lQuarQuar2.setEventKey( lAcEventKey2 );
      lQuarQuar2.setAcEventKey( lAcEventKey2 );

      lQuarQuar2.setBarcodeSdesc( GUARANTINE_BARCODE_2 );
      final Calendar lQuarCalendar2 = Calendar.getInstance();
      lQuarCalendar2.add( Calendar.DATE, -6 );
      lQuarQuar2.setDate( lQuarCalendar2.getTime() );

      lQuarQuar2.insert();

      // ACT
      final QuerySet lQs = executeQuery( lComponent1.getDbId(), lComponent1.getId() );

      // ASSERT
      assertTrue( lQs.getRowCount() == 1 );

      final QuarQuarTable lQuarQuar = QuarQuarTable.findByPrimaryKey( lQuarQuar1.getPk() );
      final long lExpectedQuarDateL = lQuarCalendar1.getTimeInMillis();
      final Calendar lQuarCalendar = Calendar.getInstance();
      lQuarCalendar.setTime( lQuarQuar.getDate() );
      final long lActualQuarDateL = lQuarCalendar1.getTimeInMillis();

      assertEquals( "Incorrect component", lComponent1, lQuarQuar.getInventoryKey() );
      assertEquals( "Incorrect AC event", lAcEventKey1, lQuarQuar.getAcEventKey() );
      assertEquals( "Incorrect quarantine barcode", GUARANTINE_BARCODE_1,
            lQuarQuar.getBarcodeSdesc() );
      assertTrue( "Incorrect quarantine date in millseconds",
            Math.abs( lExpectedQuarDateL - lActualQuarDateL ) < 1000 );
      assertEquals( "Incorrect quarantine note", GUARANTINE_NOTE, lQuarQuar.getNote() );

   }


   private void createEvent( InvCndChgEventKey aEventKey ) {
      final DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aEventKey, QuarQuarColumName.EVENT_DB_ID.name(),
            QuarQuarColumName.EVENT_ID.name() );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_event", lArgs );
   }


   private QuerySet executeQuery( int aInvNoDbId, int aInvNoId ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aInvNoDbId", aInvNoDbId );
      lArgs.add( "aInvNoId", aInvNoId );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}

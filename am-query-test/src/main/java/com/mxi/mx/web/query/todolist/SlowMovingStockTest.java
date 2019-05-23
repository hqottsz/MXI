package com.mxi.mx.web.query.todolist;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.SerializedInventory;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;


public final class SlowMovingStockTest {

   private static final int MONTHS = 1;

   private static final int PERCENTAGE = 100;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void execute_SlowMovingStock_withReorderLevel() throws MxException {
      setupSlowMovingStock_byReorderLevel( 1 );

      QuerySet lQs = execute();
      Assert.assertEquals( 1, lQs.getRowCount() );
   }


   @Test
   public void execute_SlowMovingStock_withoutReorderLevel() throws MxException {
      setupSlowMovingStock_byReorderLevel( 0 );

      QuerySet lQs = execute();
      Assert.assertEquals( 0, lQs.getRowCount() );
   }


   private void setupSlowMovingStock_byReorderLevel( double aReorderLevel ) {
      final StockNoKey lStockNoKey = new StockBuilder().withQtyUnitKey( RefQtyUnitKey.EA ).build();

      LocationKey lAirport = Domain.createLocation( ( Location aLocation ) -> {
         aLocation.setType( RefLocTypeKey.AIRPORT );
         aLocation.setIsSupplyLocation( true );
      } );

      new StockLevelBuilder( lAirport, lStockNoKey, Domain.createOwner() )
            .withReorderQt( aReorderLevel ).build();

      PartNoKey lPartNoKey = Domain.createPart( ( Part aPartBuilder ) -> {
         aPartBuilder.setStockNoKey( lStockNoKey );
      } );

      InventoryKey lInventoryKey =
            Domain.createSerializedInventory( ( SerializedInventory aInventoryBuilder ) -> {
               aInventoryBuilder.setPartNumber( lPartNoKey );
            } );

      PartRequestKey lPartRequest = new PartRequestBuilder().withReservedInventory( lInventoryKey )
            .withStatus( RefEventStatusKey.PRISSUED ).build();

      EvtEventDao lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );

      EvtEventTable lPartRequestEvent = lEvtEventDao.findByPrimaryKey( lPartRequest.getEventKey() );

      Calendar lCalendar = Calendar.getInstance();
      lCalendar.add( Calendar.DATE, -1 );

      Date lYesterday = lCalendar.getTime();
      lPartRequestEvent.setEventDate( lYesterday );
      lEvtEventDao.update( lPartRequestEvent );

   }


   private QuerySet execute() {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aPercentage", PERCENTAGE );
      lArgs.add( "aMonths", MONTHS );

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}

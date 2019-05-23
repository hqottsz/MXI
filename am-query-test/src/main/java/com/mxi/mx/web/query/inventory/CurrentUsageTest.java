package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Class for performing query tests for Query
 * \maintenix\src\main\resources\com\mxi\mx\web\query\inventory\CurrentUsage.qrx
 *
 */

public final class CurrentUsageTest {

   @ClassRule
   public static FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Test
   public void itReturnsAircraftCurrentUsage() throws Exception {

      InventoryKey lAircraft = Domain.createAircraft( aAC -> {
         aAC.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      QuerySet lQs = executeQuery( lAircraft.getDbId(), lAircraft.getId() );

      assertEquals( "The query did not return the expected number of rows.", 1, lQs.getRowCount() );

      // AC usage
      lQs.next();
      assertEquals( "The usage was not for the expected inventory key.", lAircraft,
            lQs.getKey( InventoryKey.class, "inventory_key" ) );
      assertEquals( "The usage was not for the expected data type.", DataTypeKey.HOURS,
            lQs.getKey( DataTypeKey.class, "data_type_key" ) );
      assertEquals( "The usage was not the expected TSN.", BigDecimal.TEN.stripTrailingZeros(),
            lQs.getBigDecimal( "INVENTORY_TSN_QT" ).stripTrailingZeros() );
   }


   @Test
   public void itReturnsEngineCurrentUsage() throws Exception {

      InventoryKey lEngine = Domain.createEngine( aEngine -> {
         aEngine.addUsage( DataTypeKey.HOURS, BigDecimal.valueOf( 7 ) );
      } );

      QuerySet lQs = executeQuery( lEngine.getDbId(), lEngine.getId() );

      assertEquals( "The query did not return the expected number of rows.", 1, lQs.getRowCount() );

      // Engine usage
      lQs.next();
      assertEquals( "The usage was not for the expected inventory key.",
            lQs.getKey( InventoryKey.class, "inventory_key" ), lEngine );
      assertEquals( "The usage was not for the expected data type.",
            lQs.getKey( DataTypeKey.class, "data_type_key" ), DataTypeKey.HOURS );
      assertEquals( "The usage was not the expected TSN.",
            lQs.getBigDecimal( "INVENTORY_TSN_QT" ).stripTrailingZeros(),
            BigDecimal.valueOf( 7 ).stripTrailingZeros() );
   }


   @Test
   public void itReturnsEngineRootInventoryCurrentUsage() throws Exception {

      InventoryKey lAircraft = Domain.createAircraft( aAC -> {
         aAC.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      InventoryKey lEngine = Domain.createEngine( aEngine -> {
         aEngine.setParent( lAircraft );
         aEngine.addUsage( DataTypeKey.HOURS, BigDecimal.valueOf( 7 ) );
      } );

      QuerySet lQs = executeQuery( lEngine.getDbId(), lEngine.getId() );

      assertEquals( "The query did not return the expected number of rows.", 1, lQs.getRowCount() );

      // Engine usage
      lQs.next();

      assertEquals( "The usage was not the expected TSN.",
            lQs.getBigDecimal( "ROOT_INVENTORY_TSN_QT" ).stripTrailingZeros(),
            BigDecimal.valueOf( 10 ).stripTrailingZeros() );
   }


   private QuerySet executeQuery( int aInvNoDbId, int aInvNoId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aInvNoDbId", aInvNoDbId );
      lArgs.add( "aInvNoId", aInvNoId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}

package com.mxi.mx.web.query.usage;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;
import com.mxi.mx.core.usage.model.UsageType;


/**
 * This is a unit test for UsageHistoryOfAcftAssy.qrx
 *
 */
public class UsageHistoryOfAcftAssyTest {

   private final DataTypeKey iDataTypeHours = DataTypeKey.HOURS;
   private final DataTypeKey iDataTypeCycles = DataTypeKey.CYCLES;
   private final BigDecimal iCurrentHoursUsage = new BigDecimal( 5.0 );
   private final BigDecimal iCurrentCycleUsage = new BigDecimal( 1.0 );
   private final BigDecimal iUsageCycles = new BigDecimal( 1 );
   private final BigDecimal iUsageHours = new BigDecimal( 5 );
   private final BigDecimal iUsageCyclesDelta = new BigDecimal( 1 );
   private final BigDecimal iUsageHoursDelta = new BigDecimal( 5 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void itReturnsUsagesWhenThereIsUsageParamForAnInventory() {

      InventoryKey lAircraftKey = createAircraft();
      createUsageRecord( lAircraftKey, DateUtils.addDays( new Date(), -5 ),
            UsageType.MXADJUSTMENT );
      int lDayCount = 100;
      QuerySet lUsageParamListQs = executeUsageParamListQuery( lAircraftKey );
      assertEquals( "Expected Row number should be 1", 1, lUsageParamListQs.getRowCount() );
      lUsageParamListQs.next();
      String lUsageParamList = lUsageParamListQs.getString( "formatted_usage_param_list" );

      QuerySet lUsageHistoryQs = executeUsageHistory( lAircraftKey, lDayCount, lUsageParamList );
      assertEquals( "Expected Row number should be 1", 1, lUsageHistoryQs.getRowCount() );
      lUsageHistoryQs.next();

      String lHour = "UsgParam0_1";
      String lCycle = "UsgParam0_10";

      assertEquals( "Incorrect hours delta value", iUsageHoursDelta,
            lUsageHistoryQs.getBigDecimal( lHour.concat( "_delta" ) ) );
      assertEquals( "Incorrect hours tsn value", iUsageHours,
            lUsageHistoryQs.getBigDecimal( lHour.concat( "_tsn" ) ) );

      assertEquals( "Incorrect cycle delta value", iUsageCyclesDelta,
            lUsageHistoryQs.getBigDecimal( lCycle.concat( "_delta" ) ) );
      assertEquals( "Incorrect cycle tsn value", iUsageCycles,
            lUsageHistoryQs.getBigDecimal( lCycle.concat( "_tsn" ) ) );

   }


   @Test
   public void itDoesNotReturnUsagesWhenDayCountIsNotInTheRange() {

      InventoryKey lAircraftKey = createAircraft();
      createUsageRecord( lAircraftKey, DateUtils.addDays( new Date(), -5 ),
            UsageType.MXADJUSTMENT );
      int lDayCount = 2;
      QuerySet lUsageParamListQs = executeUsageParamListQuery( lAircraftKey );
      assertEquals( "Expected Expected Row count should be 1", 1, lUsageParamListQs.getRowCount() );
      lUsageParamListQs.next();
      String lUsageParamList = lUsageParamListQs.getString( "formatted_usage_param_list" );

      QuerySet lUsageHistoryQs = executeUsageHistory( lAircraftKey, lDayCount, lUsageParamList );
      assertEquals( "Expected Row count should be 0", 0, lUsageHistoryQs.getRowCount() );
   }


   @Test
   public void itDoesNotReturnUsagesWhenUsageForInventoryDoesNotExist() {

      InventoryKey lEngineKey = createEngine();
      InventoryKey lAircraftKey = createAircraft();
      // create usage record for aircraft only
      createUsageRecord( lAircraftKey, DateUtils.addDays( new Date(), -5 ), UsageType.MXFLIGHT );
      int lDayCount = 100;
      // execute query on Engine
      QuerySet lUsageParamListQs = executeUsageParamListQuery( lEngineKey );
      assertEquals( "Expected Row number should be 1", 1, lUsageParamListQs.getRowCount() );
      lUsageParamListQs.next();
      String lUsageParamList = lUsageParamListQs.getString( "formatted_usage_param_list" );

      QuerySet lUsageHistoryQs = executeUsageHistory( lEngineKey, lDayCount, lUsageParamList );
      assertEquals( "Expected Row number should be 0", 0, lUsageHistoryQs.getRowCount() );
   }


   private UsageAdjustmentId createUsageRecord( final InventoryKey aInventoryKey,
         final Date aUsageDate, final UsageType aUsageType ) {
      // Given a flight against the aircraft with a usage record.
      final UsageAdjustmentId lUsageRecordId =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aUsageAdjustment ) {
                  aUsageAdjustment.setMainInventory( aInventoryKey );
                  aUsageAdjustment.addUsage( aInventoryKey, CYCLES, iUsageCycles,
                        iUsageCyclesDelta );
                  aUsageAdjustment.addUsage( aInventoryKey, HOURS, iUsageHours, iUsageHoursDelta );
                  aUsageAdjustment.setUsageDate( aUsageDate );
                  aUsageAdjustment.setUsageType( aUsageType );

               }
            } );

      return lUsageRecordId;
   }


   private InventoryKey createAircraft() {

      InventoryKey lAircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addUsage( iDataTypeHours, iCurrentHoursUsage );
            aAircraft.addUsage( iDataTypeCycles, iCurrentCycleUsage );
         }
      } );

      return lAircraftKey;

   }


   private InventoryKey createEngine() {

      InventoryKey lEngineKey = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.addUsage( iDataTypeHours, iCurrentHoursUsage );
            aEngine.addUsage( iDataTypeCycles, iCurrentCycleUsage );
         }
      } );

      return lEngineKey;

   }


   private QuerySet executeUsageHistory( InventoryKey aInventory, int aDayCount,
         String aUsageParamList ) {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInventoryDbId", "aInventoryId" );
      lArgs.add( "aDayCount", aDayCount );
      lArgs.addSelect( "SELECT_VALUE", aUsageParamList );

      // Execute the query
      QuerySet lUsageHistoryQs =
            QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getQueryName( getClass() ), lArgs );

      return lUsageHistoryQs;
   }


   private QuerySet executeUsageParamListQuery( InventoryKey aInventory ) {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInventoryDbId", "aInventoryId" );

      // Execute the query
      QuerySet lUsageParamListQs =
            QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
                  "com.mxi.mx.web.query.usage.UsageParamList", lArgs );

      return lUsageParamListQs;
   }

}

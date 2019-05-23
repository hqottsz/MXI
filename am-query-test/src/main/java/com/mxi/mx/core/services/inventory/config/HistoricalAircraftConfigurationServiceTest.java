package com.mxi.mx.core.services.inventory.config;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;


/**
 * Test the HistoricalAircraftConfigurationService class
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class HistoricalAircraftConfigurationServiceTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Test
   public void itReturnsAnAircraftThatHadUsage() {
      HistoricalAircraftConfigurationService iConfigurationService =
            new HistoricalAircraftConfigurationService();

      final InventoryKey lAircraftKey = Domain.createAircraft();

      UsageAdjustmentId lUsageAdjustment =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aUsageRecord ) {
                  aUsageRecord.addUsage( lAircraftKey, DataTypeKey.HOURS, new BigDecimal( 0.0 ),
                        new BigDecimal( 0.0 ) );
                  aUsageRecord.addUsage( lAircraftKey, DataTypeKey.CYCLES, new BigDecimal( 0.0 ),
                        new BigDecimal( 0.0 ) );

               }
            } );

      Map<InventoryKey, InventoryKey> lInstalledKeys =
            iConfigurationService.getInventoryByUsageAdjustment( lUsageAdjustment );

      Assert.assertEquals( "Expected an inventory and only one is installed", lInstalledKeys.size(),
            1 );
      Assert.assertTrue( "Expected the aircraft inventory is installed",
            lInstalledKeys.keySet().iterator().next().equals( lAircraftKey ) );
      Assert.assertEquals( "Expected the aircraft inventory's assembly is itself",
            lInstalledKeys.get( lAircraftKey ), lAircraftKey );

   }
}

package com.mxi.mx.core.unittest.usgrec;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.bom.ConfigSlotService;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.services.inventory.config.AttachableInventoryService;
import com.mxi.mx.core.services.usgrec.UsageRecordService;
import com.mxi.mx.core.unittest.table.inv.InvCurrUsage;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.usage.service.UsageDelta;
import com.mxi.mx.core.usage.service.UsageService;


/**
 * This class is used to test creation of a usage record.
 *
 * IDE-325: Migrated from TestingEndEJB
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class URCreateAndCompleteTest {

   private static final String ALTERNATE_ENGINE_SERIAL_NUMBER = "MotorSich";

   private static final String ENG_SUB_CS = "ENG_SUB_CS";
   private static final String ASSMBL_CD = "B767-200";

   private static final String ACFTS_ENGINE_PART_GROUP_CODE = "ACFTS_ENGINE_PART_GROUP_CODE";
   private static final String ACFTS_ENGINE_CONFIG_SLOT_CODE = "ACFTS_ENGINE_CONFIG_SLOT_CODE";

   /** Attachable service */
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests the creation of a historic usage record will update all applicable ASSY usages. All
    * usage parameters of both ASSY's orig_assmbl and current config slot should be updated. When it
    * is uninstalled then only usage parameters of current config slot should be updated.
    *
    * @throws Exception
    *            if an error ocurrs
    */
   @Test
   public void testCreateHistUsgRecForASSY() throws Exception {
      // *****************************************************************************************
      // 1. the ASSY inventory CF680 is currently installed
      // 2. its original assembly is CF6-80 and current config slot is assigned to B767-200
      // 3. usage parameters on CF6-80 is C and H and current usages were initialized
      // 4. there is no usage parameters assigned to the current config slot for the inventory
      // 5. create a usage record : it will update both C and H values
      // 6. assign a new usage parameter LANDING on the current config slot
      // 7. synchronize usage : it will initialize the LANDING on the inventory
      // 8. create a usage record : it will update C, H, and LANDING values
      // 9. detach the inventory : now the ASSY's config slot will be set to the original one
      // 10. LANDING is not applicable and only C and H are valide parameters
      // 11. create a usage and try to update the LANDING : make sure it shouldn't be update
      // 12. only C and H can be updated in this case
      // *****************************************************************************************

      // Creating aircraft part
      final PartNoKey lAircraftPart = Domain.createPart();

      // Set up an engine part
      final PartNoKey lEnginePart = new PartNoBuilder().withInventoryClass( RefInvClassKey.ASSY )
            .withStatus( RefPartStatusKey.ACTV ).build();

      final AssemblyKey lAircraftAssembly =
            createAcftAssyWithAcftPart( lAircraftPart, lEnginePart );

      final ConfigSlotKey lAirCraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      final ConfigSlotKey lEngineSubConfigSlot =
            Domain.readSubConfigurationSlot( lAirCraftRootConfigSlot, ENG_SUB_CS );

      final LocationKey lLocationKey = new LocationDomainBuilder().isSupplyLocation().build();

      // Create an engine assembly with a sub-config slot.
      final AssemblyKey lEngineAssembly = Domain.createEngineAssembly( aEngAssy -> {
         aEngAssy.setRootConfigurationSlot( aRootCS -> {
            aRootCS.addUsageParameter( DataTypeKey.HOURS );
            aRootCS.addUsageParameter( DataTypeKey.CYCLES );
            aRootCS.setName( "LEFT_ENGINE" );
            aRootCS.setCode( ENG_SUB_CS );
         } );
      } );

      InventoryKey lEngineCF680 = Domain.createEngine( aEngine -> {
         aEngine.setPartNumber( lEnginePart );
         aEngine.setOriginalAssembly( lEngineAssembly );
         aEngine.setSerialNumber( ALTERNATE_ENGINE_SERIAL_NUMBER );
         aEngine.setDescription( "Antonov An-225 Mriya" );
         aEngine.setPosition( new ConfigSlotPositionKey( lEngineSubConfigSlot, 1 ) );
         Map<DataTypeKey, BigDecimal> aUsageMap = new HashMap<DataTypeKey, BigDecimal>();
         aUsageMap.put( DataTypeKey.HOURS, new BigDecimal( 0.0 ) );
         aUsageMap.put( DataTypeKey.CYCLES, new BigDecimal( 0.0 ) );
         aEngine.setUsage( aUsageMap );
         aEngine.setLocation( lLocationKey );
      } );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
         aAircraft.allowSynchronization();
         aAircraft.addUsage( DataTypeKey.HOURS, new BigDecimal( 0.0 ) );
         aAircraft.addUsage( DataTypeKey.CYCLES, new BigDecimal( 0.0 ) );
         aAircraft.addUsage( DataTypeKey.LANDING, new BigDecimal( 0.0 ) );
         aAircraft.addEngine( lEngineCF680 );
      } );

      // check the ASSY inventory is installed
      InvInv lAssyInv = new InvInv( lEngineCF680 );
      assertNotNull( lAssyInv.getNhInvNo() );

      UsageParmKey lUsageParmH = new UsageParmKey( lEngineCF680, DataTypeKey.HOURS );
      UsageParmKey lUsageParmC = new UsageParmKey( lEngineCF680, DataTypeKey.CYCLES );

      // Asserts the values for the CF680
      InvCurrUsage.assertTsnQt( lUsageParmH, 0.0 );
      InvCurrUsage.assertTsnQt( lUsageParmC, 0.0 );

      Date lDate = Date.from( LocalDateTime.of( 2009, 05, 11, 14, 27, 01 )
            .atZone( ZoneId.systemDefault() ).toInstant() );

      CollectedUsageParm[] lCollectedUsageParm =
            getCollectedUsageParm( lUsageParmH, 100, true, lDate );

      new UsageService().accrueUsage( lEngineCF680, getHumanResource(), "Create Usage for May 4",
            lDate, "Usage for May 4", null, this.getUsageDelta( lCollectedUsageParm ), null );

      lDate = Date.from( LocalDateTime.of( 2009, 05, 12, 14, 27, 01 )
            .atZone( ZoneId.systemDefault() ).toInstant() );

      lCollectedUsageParm = getCollectedUsageParm( lUsageParmC, 150, true, lDate );
      new UsageService().accrueUsage( lEngineCF680, getHumanResource(), "Create Usage for May 4",
            lDate, "Usage for May 4", null, this.getUsageDelta( lCollectedUsageParm ), null );

      // Asserts the values for the CF680 have been updated
      InvCurrUsage.assertTsnQt( lUsageParmH, 100.0 );
      InvCurrUsage.assertTsnQt( lUsageParmC, 150.0 );

      /* Confirm that there are now 2 rows in inv_curr_usage for this inventory */
      InvCurrUsage.assertUsageCount( lEngineCF680, 2 );

      // add a new usage parameter on the current assembly
      // Call the service object to conduct assign actions
      ConfigSlotService.assignUsageParameter( lAssyInv.getBOMItem(), DataTypeKey.LANDING, false );

      // Synchronize usage
      UsageRecordService.synchronizeUsage();

      /* There should be 3 rows in inv_curr_usage after sync */
      InvCurrUsage.assertUsageCount( lEngineCF680, 3 );

      // Asserts the newly create value
      UsageParmKey lUsageParmLanding = new UsageParmKey( lEngineCF680, DataTypeKey.LANDING );
      InvCurrUsage.assertTsnQt( lUsageParmLanding, 0.0 );

      lDate = Date.from( LocalDateTime.of( 2009, 05, 13, 14, 27, 01 )
            .atZone( ZoneId.systemDefault() ).toInstant() );

      lCollectedUsageParm = getCollectedUsageParm( lUsageParmH, 150, true, lDate );
      new UsageService().accrueUsage( lEngineCF680, getHumanResource(), "Create Usage for May 4",
            lDate, "Usage for May 4", null, this.getUsageDelta( lCollectedUsageParm ), null );

      lDate = Date.from( LocalDateTime.of( 2009, 05, 14, 14, 27, 01 )
            .atZone( ZoneId.systemDefault() ).toInstant() );

      lCollectedUsageParm = getCollectedUsageParm( lUsageParmC, 250, true, lDate );
      new UsageService().accrueUsage( lAircraft, getHumanResource(), "Create Usage for May 4",
            lDate, "Usage for May 4", null, this.getUsageDelta( lCollectedUsageParm ), null );

      lDate = Date.from( LocalDateTime.of( 2009, 05, 14, 15, 27, 01 )
            .atZone( ZoneId.systemDefault() ).toInstant() );

      lCollectedUsageParm = getCollectedUsageParm( lUsageParmLanding, 2, true, lDate );

      new UsageService().accrueUsage( lAircraft, getHumanResource(), "Create Usage for May 4",
            lDate, "Usage for May 4", null, this.getUsageDelta( lCollectedUsageParm ), null );

      // Asserts the values for the CF680 have been updated
      InvCurrUsage.assertTsnQt( lUsageParmH, 150.0 );
      InvCurrUsage.assertTsnQt( lUsageParmC, 250.0 );
      InvCurrUsage.assertTsnQt( lUsageParmLanding, 2.0 );

      // create the service class instance
      AttachableInventoryService lService =
            new InventoryServiceFactory().getAttachableInventoryService( lEngineCF680 );

      // build refterms
      RefStageReasonKey lReason = new RefStageReasonKey( null );
      // Detach the engine
      lService.detachInventory( null, lLocationKey, getHumanResource(), true, lReason, null );

      lAssyInv = new InvInv( lEngineCF680 );
      assertNull( lAssyInv.getNhInvNo() );

      lDate = Date.from( LocalDateTime.of( 2009, 05, 16, 14, 27, 01 )
            .atZone( ZoneId.systemDefault() ).toInstant() );

      lCollectedUsageParm = getCollectedUsageParm( lUsageParmLanding, 3, true, lDate );
      new UsageService().accrueUsage( lAircraft, getHumanResource(), "Create Usage for May 4",
            lDate, "Usage for May 4", null, this.getUsageDelta( lCollectedUsageParm ), null );

      // it should be still 2 since LANDING is not applicable any more
      InvCurrUsage.assertTsnQt( lUsageParmLanding, 2.0 );
   }


   /**
    * Build a CollectedUsageParm object
    *
    * @param aUsageParmKey
    *           The usage parm key
    * @param aUsageQt
    *           The usage quantity
    * @param aAbsoluteFlag
    *           Boolean to determine if the usage quantity is an absolute TSN value or a delta value
    * @param aCompletionDate
    *           The date this usage record is completed
    *
    * @return The CollectedUsageParam object
    */
   private static CollectedUsageParm[] getCollectedUsageParm( UsageParmKey aUsageParmKey,
         double aUsageQt, boolean aAbsoluteFlag, Date aCompletionDate ) {

      // Create a usage parm
      CollectedUsageParm[] lCollectedUsageParm = new CollectedUsageParm[1];
      lCollectedUsageParm[0] = new CollectedUsageParm();
      lCollectedUsageParm[0].setCollectionDate( aCompletionDate );
      lCollectedUsageParm[0].setAbsoluteFlag( aAbsoluteFlag );
      lCollectedUsageParm[0].setUsageParm( aUsageParmKey );
      if ( aAbsoluteFlag ) {
         lCollectedUsageParm[0].setTsn( aUsageQt );
      } else {
         lCollectedUsageParm[0].setDelta( aUsageQt );
      }

      return lCollectedUsageParm;
   }


   /**
    * Returns the human resource key of the current logged in user.
    *
    * @return the key.
    */
   private HumanResourceKey getHumanResource() {
      return new HumanResourceKey( "1:1" );
   }


   private List<UsageDelta> getUsageDelta( CollectedUsageParm[] aCollectedUsageParms ) {
      return Arrays.stream( aCollectedUsageParms )
            .map( n -> convertCollectedUsageParmToUsageDelta( n ) ).collect( Collectors.toList() );
   }


   /**
    * Convert object of class CollectedUsageParm To object of class UsageDelta
    *
    * @param lCollectedUsageParm
    * @return UsageDelta
    */
   private UsageDelta
         convertCollectedUsageParmToUsageDelta( CollectedUsageParm lCollectedUsageParm ) {

      UsageDelta lUsageDelta = new UsageDelta();

      lUsageDelta.setDelta( lCollectedUsageParm.getDelta() );
      lUsageDelta.setTsiQt( lCollectedUsageParm.getTsi() );
      lUsageDelta.setTsnQt( lCollectedUsageParm.getTsn() );
      lUsageDelta.setTsoQt( lCollectedUsageParm.getTso() );
      lUsageDelta.setUsageParmKey( lCollectedUsageParm.getUsageParm() );
      lUsageDelta.setAbsoluteFlag( lCollectedUsageParm.getAbsoluteFlag() );
      lUsageDelta.setCollectionDate( lCollectedUsageParm.getCollectionDate() );

      return lUsageDelta;
   }


   private AssemblyKey createAcftAssyWithAcftPart( final PartNoKey aAircraftPart,
         PartNoKey aEnginePart ) {
      return Domain.createAircraftAssembly( aAcftAssy -> {
         aAcftAssy.setCode( ASSMBL_CD );

         aAcftAssy.setRootConfigurationSlot( aRootCs -> {
            aRootCs.setCode( "RootSlot" );
            aRootCs.addConfigurationSlot( aConfigSlot -> {
               aConfigSlot.setCode( ACFTS_ENGINE_CONFIG_SLOT_CODE );
               aConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               aConfigSlot.addPartGroup( aPartGroup -> {
                  aPartGroup.setCode( ACFTS_ENGINE_PART_GROUP_CODE );
                  aPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                  aPartGroup.addPart( aAircraftPart );
               } );
            } );
            aRootCs.addUsageParameter( DataTypeKey.HOURS );
            aRootCs.addUsageParameter( DataTypeKey.CYCLES );
            aRootCs.addUsageParameter( DataTypeKey.LANDING );
            aRootCs.addConfigurationSlot( aEngCs -> {
               aEngCs.addUsageParameter( DataTypeKey.HOURS );
               aEngCs.addUsageParameter( DataTypeKey.CYCLES );
               aEngCs.addPartGroup( aPartGroupEngine -> {
                  aPartGroupEngine.setInventoryClass( RefInvClassKey.ASSY );
                  aPartGroupEngine.addPart( aEnginePart );
               } );
               aEngCs.setCode( ENG_SUB_CS );
            } );
         } );
      } );
   }
}

package com.mxi.mx.core.query.inventory.oper;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvInvTable.FinanceStatusCd;


/**
 * This class tests that the inv_status.CalculateStatus function operates correctly and returns the
 * correct new inventory condition.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetCalculatedInventoryStatusTest {

   protected static final String POSITION_CODE = "POSITION_CODE";
   protected static final String PART_GROUP1 = "PART_GROUP1";
   protected static final String PART_GROUP2 = "PART_GROUP2";
   private static final String TRK_CONFIG_SLOT1 = "TRK_CONFIG_SLOT1";
   private static final String TRK_CONFIG_SLOT2 = "TRK_CONFIG_SLOT2";
   private static final String TRK_CONFIG_SLOT_CD1 = "TRK_SLOT_CD1";
   private static final String TRK_CONFIG_SLOT_CD2 = "TRK_SLOT_CD2";
   private static final String ENG_ASSMBL_CD = "CFM380";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey iInventory;


   @Before
   public void loadData() throws Exception {

      iInventory = createEngineWithTRKConfigSlots();
   }


   @Test
   // When completing a work package
   public void testCalculateStatusForIncompleteInventoryWhenRFBEnabled() throws Exception {

      boolean lIsRFBEnabled = true;
      boolean lCompleteBool = false;
      validateCalculateStatusAfterCompleteWorkpackage( lIsRFBEnabled, RefInvCondKey.INREP,
            RefInvCondKey.RFI, lCompleteBool );
   }


   @Test
   // When completing a work package
   public void testCalculateStatusForIncompleteInventoryWhenRFBDisabled() throws Exception {

      boolean lIsRFBEnabled = false;
      boolean lCompleteBool = false;
      validateCalculateStatusAfterCompleteWorkpackage( lIsRFBEnabled, RefInvCondKey.INREP,
            RefInvCondKey.REPREQ, lCompleteBool );
   }


   @Test
   // When completing a work package
   public void testCalculateStatusForCompleteInventoryWhenRFBEnabled() throws Exception {

      boolean lIsRFBEnabled = true;
      boolean lCompleteBool = true;
      validateCalculateStatusAfterCompleteWorkpackage( lIsRFBEnabled, RefInvCondKey.INREP,
            RefInvCondKey.RFI, lCompleteBool );
   }


   @Test
   // When completing a work package
   public void testCalculateStatusForCompleteInventory() throws Exception {

      boolean lIsRFBEnabled = true;
      boolean lCompleteBool = true;
      validateCalculateStatusAfterCompleteWorkpackage( lIsRFBEnabled, RefInvCondKey.INREP,
            RefInvCondKey.RFI, lCompleteBool );
   }


   @Test
   // When detaching a sub-component from a component
   // Assert that for New inventory, condition remains if the old condition is INSPREQ
   public void testCalculateStatusForINSPREQNewInv() throws Exception {

      // when RFB is enabled
      boolean lIsRFBEnabled = true;
      validateCalculateStatusAfterDetachSubcomponent( lIsRFBEnabled, FinanceStatusCd.NEW,
            RefInvCondKey.INSPREQ, RefInvCondKey.INSPREQ );

      // when RFB is disabled
      lIsRFBEnabled = false;
      validateCalculateStatusAfterDetachSubcomponent( lIsRFBEnabled, FinanceStatusCd.NEW,
            RefInvCondKey.INSPREQ, RefInvCondKey.INSPREQ );
   }


   @Test
   // When detaching a sub-component from a component
   // Assert that for Existing inventory, condition changes from INSPREQ to REPREQ
   public void testCalculateStatusForINSPREQInv() throws Exception {

      // when RFB is enabled
      boolean lIsRFBEnabled = true;
      validateCalculateStatusAfterDetachSubcomponent( lIsRFBEnabled, FinanceStatusCd.INSP,
            RefInvCondKey.INSPREQ, RefInvCondKey.REPREQ );

      // when RFB is disabled
      lIsRFBEnabled = false;
      validateCalculateStatusAfterDetachSubcomponent( lIsRFBEnabled, FinanceStatusCd.INSP,
            RefInvCondKey.INSPREQ, RefInvCondKey.REPREQ );
   }


   @Test
   // When detaching a sub-component from a component
   // Assert that for Existing inventory, condition changes from RFI to REPREQ when RFB is enabled
   public void testCalculateStatusForRFIInv() throws Exception {

      // condition changes from RFI to REPREQ when RFB is enabled
      boolean lIsRFBEnabled = true;
      validateCalculateStatusAfterDetachSubcomponent( lIsRFBEnabled, FinanceStatusCd.INSP,
            RefInvCondKey.RFI, RefInvCondKey.REPREQ );

      // NA when RFB is disabled
   }


   /**
    *
    * Validate Calculate Status After Detaching Subcomponent
    *
    * @param aIsRFBEnabled
    * @param aFinanceStatusCd
    * @param aOldInvCondKey
    * @param aExpectedNewInvCondKey
    * @throws Exception
    */
   public void validateCalculateStatusAfterDetachSubcomponent( boolean aIsRFBEnabled,
         FinanceStatusCd aFinanceStatusCd, RefInvCondKey aOldInvCondKey,
         RefInvCondKey aExpectedNewInvCondKey ) throws Exception {

      // complete_bool is always true when sub-component is detached
      boolean lCompleteBool = false;
      validateCalculateStatus( aIsRFBEnabled, aFinanceStatusCd, aOldInvCondKey,
            aExpectedNewInvCondKey, lCompleteBool );
   }


   /**
    *
    * Validate Calculate Status After Completing Work Package
    *
    * @param aIsRFBEnabled
    * @param aOldInvCondKey
    * @param aExpectedNewInvCondKey
    * @param aCompleteBool
    * @throws Exception
    */
   public void validateCalculateStatusAfterCompleteWorkpackage( boolean aIsRFBEnabled,
         RefInvCondKey aOldInvCondKey, RefInvCondKey aExpectedNewInvCondKey, boolean aCompleteBool )
         throws Exception {

      // the inventory is an existing one when completing a work package, so that financial status
      // of the inventory is always INSP.
      validateCalculateStatus( aIsRFBEnabled, FinanceStatusCd.INSP, aOldInvCondKey,
            aExpectedNewInvCondKey, aCompleteBool );
   }


   public void validateCalculateStatus( boolean aIsRFBEnabled, FinanceStatusCd aFinanceStatusCd,
         RefInvCondKey aOldInvCondKey, RefInvCondKey aExpectedNewInvCondKey, boolean aCompleteBool )
         throws Exception {

      // set the config param
      setReadyForBuildConfigParam( aIsRFBEnabled );

      // set the inventory properties
      InvInvTable lTable = InvInvTable.findByPrimaryKey( iInventory );
      // for existing inventory only
      lTable.setFinanceStatusCd( aFinanceStatusCd );
      lTable.setInvCond( aOldInvCondKey );
      lTable.setComplete( aCompleteBool );
      lTable.update();

      DataSet lDataSet = getCalculatedInventoryStatus( iInventory );

      verifyNewCondition( aExpectedNewInvCondKey, lDataSet );
   }


   private void verifyNewCondition( RefInvCondKey aExpectedCond, DataSet aDataSet ) {

      // verify result count
      assertEquals( 1, aDataSet.getRowCount() );
      aDataSet.next();
      // verify that the new calculated condition is same as expected
      assertEquals( aExpectedCond, getNewCond( aDataSet ) );
   }


   /**
    * Get new inventory condition from the data set
    *
    * @param aDataSet
    * @return
    */
   private RefInvCondKey getNewCond( DataSet aDataSet ) {
      return aDataSet.getKey( RefInvCondKey.class, "nc_db_id", "nc_cd" );
   }


   /**
    * Set the global config param value
    *
    */
   private void setReadyForBuildConfigParam( boolean aValue ) {

      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setBoolean( "ENABLE_READY_FOR_BUILD", aValue );
      GlobalParameters.setInstance( lConfigParms );
   }


   /**
    * Executes the query with given arguments
    *
    * @param aInventory
    *           inventory key
    * @return the query result
    */
   private DataSet getCalculatedInventoryStatus( InventoryKey aInventory ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );

      // run the query with given arguments and return the result
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.inventory.oper.getCalculatedInventoryStatus", lArgs );
      return lDs;
   }


   /**
    * Creates Engine inventory with TRK config slots
    *
    * @return the inventory key
    */
   private InventoryKey createEngineWithTRKConfigSlots() {

      // Set up an engine part
      final PartNoKey lEnginePart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withStatus( RefPartStatusKey.ACTV ).build();

      final PartNoKey lChildPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // Set up an engine assembly
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENG_ASSMBL_CD );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( TRK_CONFIG_SLOT1 );
                                          aBuilder.setCode( TRK_CONFIG_SLOT_CD1 );
                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setMandatoryFlag( true );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aPartGroup ) {
                                                      aPartGroup.setCode( PART_GROUP1 );
                                                      aPartGroup.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aPartGroup.addPart( lChildPart );
                                                   }
                                                } );
                                       }

                                    } );
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( TRK_CONFIG_SLOT2 );
                                          aBuilder.setCode( TRK_CONFIG_SLOT_CD2 );
                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setMandatoryFlag( true );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aPartGroup ) {
                                                      aPartGroup.setCode( PART_GROUP2 );
                                                      aPartGroup.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aPartGroup.addPart( lChildPart );
                                                   }
                                                } );
                                       }

                                    } );
                           }
                        } );
               }
            } );

      // create engine inventory
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssy );
            aBuilder.setPartNumber( lEnginePart );
         }
      } );

      // create TRK child inventory and attach to engine
      Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setLastKnownConfigSlot( ENG_ASSMBL_CD, TRK_CONFIG_SLOT_CD1, TRK_CONFIG_SLOT1 );
            aTrk.setParent( lEngine );
         }

      } );

      // create TRK child inventory and attach to engine
      Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setLastKnownConfigSlot( ENG_ASSMBL_CD, TRK_CONFIG_SLOT_CD2, TRK_CONFIG_SLOT2 );
            aTrk.setParent( lEngine );
         }
      } );

      return lEngine;
   }


   @After
   public void teardown() {

      GlobalParameters.setInstance( ParmTypeEnum.LOGIC.name(), null );
   }
}

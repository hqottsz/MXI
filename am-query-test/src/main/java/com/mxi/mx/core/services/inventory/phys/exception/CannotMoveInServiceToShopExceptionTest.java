package com.mxi.mx.core.services.inventory.phys.exception;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;


/**
 * This class tests CannotMoveInServiceToShopException.
 *
 * @author Frank Zhang
 * @created Jan 31, 2018
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CannotMoveInServiceToShopExceptionTest {

   private static String PARAMETER_NAME = "ENABLE_READY_FOR_BUILD";

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule sFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @ClassRule
   public static InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Set the global config param value
    *
    */
   private void setReadyForBuildConfigParam( boolean aValue ) {
      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setBoolean( PARAMETER_NAME, aValue );
      GlobalParameters.setInstance( lConfigParms );
   }


   /**
    *
    * Create inventory with class key, condition key
    *
    * @param aInvClassKey
    * @param aInvCondition
    * @return
    */
   public InventoryKey createInventory( RefInvClassKey aInvClassKey, RefInvCondKey aInvCondition ) {

      return createInventory( aInvClassKey, aInvCondition, true );
   }


   /**
    *
    * Create inventory with class key, condition key, and complete
    *
    * @param aInvClassKey
    * @param aInvCondition
    * @param aIsComplete
    * @return
    */
   public InventoryKey createInventory( RefInvClassKey aInvClassKey, RefInvCondKey aInvCondition,
         boolean aIsComplete ) {

      return new InventoryBuilder().withClass( aInvClassKey ).withCondition( aInvCondition )
            .withComplete( aIsComplete ).build();
   }


   /**
    * Test complete RFI inventory will throw exception when RFB is enabled.
    *
    * @throws Exception
    *
    */
   @Test( expected = CannotMoveInServiceToShopException.class )
   public void testValidateRFIInventoryWhenRFBIsEnabled() throws Exception {

      setReadyForBuildConfigParam( true );

      boolean lIsComplete = true;
      InventoryKey aInventoryKey =
            createInventory( RefInvClassKey.TRK, RefInvCondKey.RFI, lIsComplete );

      CannotMoveInServiceToShopException.validate( aInventoryKey );
   }


   /**
    * Test incomplete RFI ( aka RFB ) inventory will NOT throw exception when RFB is enabled.
    *
    * @throws Exception
    *
    */
   @Test
   public void testValidateRFBInventoryWhenRFBIsEnabled() throws Exception {

      setReadyForBuildConfigParam( true );

      boolean lIsComplete = false;
      InventoryKey aInventoryKey =
            createInventory( RefInvClassKey.TRK, RefInvCondKey.RFI, lIsComplete );

      CannotMoveInServiceToShopException.validate( aInventoryKey );
   }


   /**
    * Test REPREQ inventory will NOT throw exception when RFB is enabled.
    *
    * @throws Exception
    *
    */
   @Test
   public void testValidateREPREQInventoryWhenRFBIsEnabled() throws Exception {

      setReadyForBuildConfigParam( true );

      InventoryKey aInventoryKey = createInventory( RefInvClassKey.TRK, RefInvCondKey.REPREQ );

      CannotMoveInServiceToShopException.validate( aInventoryKey );
   }


   /**
    * Test complete RFI inventory will throw exception when RFB is disabled.
    *
    * @throws Exception
    *
    */
   @Test( expected = CannotMoveInServiceToShopException.class )
   public void testValidateCompleteRFIInventoryWhenRFBIsDisabled() throws Exception {

      setReadyForBuildConfigParam( true );

      boolean lIsComplete = true;
      InventoryKey aInventoryKey =
            createInventory( RefInvClassKey.TRK, RefInvCondKey.RFI, lIsComplete );

      CannotMoveInServiceToShopException.validate( aInventoryKey );
   }


   /**
    * Test incomplete RFI inventory will throw exception when RFB is disabled.
    *
    * @throws Exception
    *
    */
   @Test( expected = CannotMoveInServiceToShopException.class )
   public void testValidateIncompleteRFIInventoryWhenRFBIsDisabled() throws Exception {

      setReadyForBuildConfigParam( false );

      boolean lIsComplete = false;
      InventoryKey aInventoryKey =
            createInventory( RefInvClassKey.TRK, RefInvCondKey.RFI, lIsComplete );

      CannotMoveInServiceToShopException.validate( aInventoryKey );
   }


   /**
    * Test REPREQ inventory will NOT throw exception when RFB is disabled.
    *
    * @throws Exception
    *
    */
   @Test
   public void testValidateREPREQInventoryWhenRFBIsDisabled() throws Exception {

      setReadyForBuildConfigParam( false );

      InventoryKey aInventoryKey = createInventory( RefInvClassKey.TRK, RefInvCondKey.REPREQ );

      CannotMoveInServiceToShopException.validate( aInventoryKey );
   }

}

package com.mxi.mx.core.services.inventory.condition;

import org.junit.Rule;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;


/**
 * This class contains all common utilities of condition service test.
 *
 * @author Libin Cai
 * @created December 23, 2016
 *
 */
public abstract class AbstractConditionServiceTestCase {

   protected HumanResourceKey iHr;
   protected PartNoKey iPart;
   protected LocationKey iSupplyLoc;
   protected LocationKey iDockLocation;
   protected InventoryKey iInventory;
   protected InspectInventoryTO iInspectInventoryTO = new InspectInventoryTO();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   protected void loadData() throws Exception {

      // create hr with user
      iHr = new HumanResourceDomainBuilder().withUsername( "testuser" ).withUserId( 999 ).build();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      iPart = new PartNoBuilder().withStatus( RefPartStatusKey.ACTV ).build();

      iSupplyLoc = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).build();

      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLoc ).build();
   }


   /**
    * Stub class that removes internal dependencies
    */
   protected static class DefaultConditionServiceStub extends DefaultConditionService {

      /**
       * {@inheritDoc}
       */
      @Override
      public InventoryKey markAsInspected( InventoryKey aInventory, Double aQuantity,
            RefStageReasonKey aReason, String aNote, boolean aIsInspectAsServiceable,
            HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {

         // do nothing
         return aInventory;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public void markAsInspectionRequired( InventoryKey aInventory, RefStageReasonKey aReason,
            String aNote, HumanResourceKey aAuthorizingHr, boolean aMoveToUSSTG )
            throws MxException, TriggerException {

         // do nothing
         return;
      }
   }

}

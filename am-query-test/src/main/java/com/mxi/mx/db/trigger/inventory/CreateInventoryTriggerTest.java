package com.mxi.mx.db.trigger.inventory;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.ejb.inventory.InventoryBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.services.inventory.AircraftInventoryProperties;
import com.mxi.mx.core.services.inventory.BinInventoryProperties;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * This class tests the Inventory created triggers.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CreateInventoryTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;

   private InventoryBean inventoryBean;
   private UserParametersStub userParametersStub;

   private LocationKey location;
   private PartNoKey partNoKey;
   private OwnerKey ownerKey;
   private Date tomorrow;
   private HumanResourceKey user;
   private int userId;

   private static String SERIAL_NO = "aserialnumber";
   private static String REGISTRATION_CODE = "TEST";

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );

      inventoryBean = new InventoryBean();
      user = new HumanResourceDomainBuilder().build();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( user ) );
      userId = OrgHr.findByPrimaryKey( user ).getUserId();
      userParametersStub = new UserParametersStub( userId, "SECURED_RESOURCE" );
      userParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            false );
      UserParameters.setInstance( userId, "SECURED_RESOURCE", userParametersStub );

      location = Domain.createLocation();
      partNoKey = Domain.createPart();
      ownerKey = Domain.createOwner();

      Calendar calendar = Calendar.getInstance();
      calendar.add( Calendar.DATE, +1 );
      tomorrow = calendar.getTime();

      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      triggerCalled = false;

      TriggerFactory.setInstance( null );

   }


   @After
   public void teardown() {
      SecurityIdentificationUtils.setInstance( null );
      UserParameters.setInstance( userId, "SECURED_RESOURCE", null );
   }


   @Test
   public void testAircraftCreationTrigger() throws MxException, TriggerException {
      userParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            true );

      AircraftInventoryProperties aircraftInventoryProperties = new AircraftInventoryProperties(
            RefInvClassKey.ACFT_CD, RefInvCondKey.RFI.getCd(), location, partNoKey, false,
            SERIAL_NO, REGISTRATION_CODE, null, ownerKey, RefAccountTypeKey.EXPENSE.getCd() );

      aircraftInventoryProperties.setManufactureDate( tomorrow );

      inventoryBean.create( aircraftInventoryProperties );

      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   @Test
   public void testBinInventoryCreationTrigger()
         throws MxException, TriggerException, SQLException {

      BinInventoryProperties binInventoryProperties =
            new BinInventoryProperties( RefInvClassKey.BATCH_CD, RefInvCondKey.RFI.getCd(),
                  location, partNoKey, false, userId, ownerKey );

      binInventoryProperties.setIssuedBool( true );

      inventoryBean.create( binInventoryProperties );

      assertTrue( "Trigger did not get invoked", triggerCalled );

   }


   /**
    * This method gets called when the trigger is invoked
    *
    * {@inheritDoc}
    */
   @Override
   public void after( UUID aircraftId ) throws TriggerException {

      triggerCalled = true;
   }
}

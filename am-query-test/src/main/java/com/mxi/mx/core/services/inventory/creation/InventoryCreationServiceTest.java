package com.mxi.mx.core.services.inventory.creation;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AssemblySubtypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.production.aircraft.domain.AircraftInductedEvent;
import com.mxi.mx.core.services.inventory.SerializedInventoryProperties;
import com.mxi.mx.core.services.inventory.phys.ManufactureDateSetInFutureException;
import com.mxi.mx.core.table.org.OrgHr;


public class InventoryCreationServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryCreationService iInventoryCreationService;
   private UserParametersStub iUserParametersStub;
   private RecordingEventBus iEventBus;

   private LocationKey iLocation;
   private PartNoKey iPartNoKey;
   private OwnerKey iOwnerKey;
   private Date iTomorrow;
   private HumanResourceKey iUser;
   private int iUserId;


   /**
    * Tests that if ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE is set to be true, user
    * can set the manufactured date to the future.
    */
   @Test
   public void createInventory_withManufacturedDateInTheFutureAndAllowConfigParmIsTrue()
         throws MxException, TriggerException {

      iUserParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            true );
      SerializedInventoryProperties lInventoryProperties =
            new SerializedInventoryProperties( RefInvClassKey.SER_CD, RefInvCondKey.RFI.getCd(),
                  iLocation, iPartNoKey, false, "aserialnumber", null, iOwnerKey );
      lInventoryProperties.setManufactureDate( iTomorrow );

      iInventoryCreationService.createInventory( lInventoryProperties );
   }


   /**
    * Tests that if ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE is set to be false, user
    * cannot set the manufactured date to the future.
    */
   @Test( expected = ManufactureDateSetInFutureException.class )
   public void createInventory_withManufacturedDateInTheFutureAndAllowConfigParmIsFalse()
         throws MxException, TriggerException {

      SerializedInventoryProperties lInventoryProperties =
            new SerializedInventoryProperties( RefInvClassKey.SER_CD, RefInvCondKey.RFI.getCd(),
                  iLocation, iPartNoKey, false, "aserialnumber", null, iOwnerKey );
      lInventoryProperties.setManufactureDate( iTomorrow );

      iInventoryCreationService.createInventory( lInventoryProperties );
   }


   /**
    * Tests that the aircraft inducted event is published when creating an aircraft.
    */
   @Test
   public void publishAircraftInductedEventWhenCreateAircraft() throws Exception {

      // ARRANGE
      final RefInvCondKey lConditionKey = RefInvCondKey.RFI;
      final LocationKey lLocationKey = iLocation;
      final FncAccountKey lChargeToAccountKey = new AccountBuilder()
            .withType( RefAccountTypeKey.EXPENSE ).withCode( "TEST_ACCOUNT_CODE" ).build();
      final FncAccountKey lIssueToAccountKey = lChargeToAccountKey;
      final PartNoKey lPartNoKey = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.ACFT );
         aPart.setFinancialAccount( lChargeToAccountKey );
         aPart.setFinancialType( RefFinanceTypeKey.EXPENSE );
         aPart.setManufacturer( Domain.createManufacturer( aManufacturer -> {
            aManufacturer.setCode( "MANUFACTURE01" );
         } ) );
      } );

      final String lOemSerialNumber = "axon-test1";
      final AssemblySubtypeKey lAssemblySubtype = null;
      final String lRegistrationCode = "axon-test1";
      final boolean lCreateAllSubInventory = false;
      final String lApplicabilityCode = null;
      final FcModelKey lForecastModelKey = Domain.createForecastModel();
      final OwnerKey lOwnerKey = iOwnerKey;
      final Date lReceivedDate = null;
      final Date lManufacturedDate = null;
      final boolean lApplyReceivedDtToSubComponents = false;
      final boolean lApplyManufacturedDtToSubComponents = false;
      HumanResourceKey lUserKey = iUser;
      createAcftAssyWithAcftPart( lPartNoKey );

      // ACTION
      final InventoryKey lNewInventory = iInventoryCreationService.createAircraft( lConditionKey,
            lLocationKey, lPartNoKey, lOemSerialNumber, lAssemblySubtype, lRegistrationCode,
            lCreateAllSubInventory, lApplicabilityCode, lForecastModelKey, lChargeToAccountKey,
            lOwnerKey, lIssueToAccountKey, lReceivedDate, lManufacturedDate,
            lApplyReceivedDtToSubComponents, lApplyManufacturedDtToSubComponents, lUserKey );

      // ASSERT
      assertEquals( new AircraftInductedEvent( new AircraftKey( lNewInventory ), lPartNoKey,
            lRegistrationCode, lOemSerialNumber, lAssemblySubtype, lLocationKey, lConditionKey,
            lOwnerKey, lManufacturedDate, lReceivedDate, lApplyManufacturedDtToSubComponents,
            lApplyReceivedDtToSubComponents, lForecastModelKey, lApplicabilityCode,
            lChargeToAccountKey, lIssueToAccountKey, lUserKey ),
            iEventBus.getEventMessages().get( 0 ).getPayload() );

   }


   @Before
   public void setup() {

      iUser = new HumanResourceDomainBuilder().build();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iUser ) );
      iUserId = OrgHr.findByPrimaryKey( iUser ).getUserId();
      iUserParametersStub = new UserParametersStub( iUserId, "SECURED_RESOURCE" );
      iUserParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            false );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", iUserParametersStub );

      iInventoryCreationService = new InventoryCreationService();

      iLocation = Domain.createLocation();
      iPartNoKey = Domain.createPart();
      iOwnerKey = Domain.createOwner();

      iEventBus = iFakeGuiceDaoRule.select( RecordingEventBus.class ).get();

      Calendar lCal = Calendar.getInstance();
      lCal.add( Calendar.DATE, +1 );
      iTomorrow = lCal.getTime();
   }


   @After
   public void teardown() {
      SecurityIdentificationUtils.setInstance( null );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", null );
   }


   private AssemblyKey createAcftAssyWithAcftPart( final PartNoKey aAircraftPart ) {
      return Domain.createAircraftAssembly( aAcftAssy -> {
         aAcftAssy.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aAircraftPart );
            } );
         } );
      } );
   }

}

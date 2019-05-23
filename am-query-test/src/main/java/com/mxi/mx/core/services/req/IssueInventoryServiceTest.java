package com.mxi.mx.core.services.req;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.message.MxMessage;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.license.CoreLicenseStub;
import com.mxi.mx.core.license.CoreLicenseStub.FeatureSet;
import com.mxi.mx.core.license.MxCoreLicense;
import com.mxi.mx.core.services.inventory.pick.PickedItem;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.inv.InvInv;


@RunWith( Theories.class )
public class IssueInventoryServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private String PART_NO_OEM_SER = "SER_PART_NO_OEM";
   private String PART_NO_OEM_BATCH = "15861BATCH";

   private String PART_NO_SDESC = "PART_NO_SDESC";
   private String SN_BN = "SN_BN";
   private String MANUFACTURE_CD = "ACE";
   private double QTY_AS_1 = 1.0;

   @DataPoint
   public static RefInvClassKey SER = RefInvClassKey.SER;
   @DataPoint
   public static RefInvClassKey BATCH = RefInvClassKey.BATCH;

   private HumanResourceKey iHrKey;
   private int iUserId;
   private PartNoKey iSerPartNo;
   private PartNoKey iBatchPartNo;


   @Before
   public void setUp() {

      MxCoreLicense.setValidator( new CoreLicenseStub( FeatureSet.NONE ) );

      iHrKey = Domain.createHumanResource();
      iUserId = OrgHr.findByPrimaryKey( iHrKey ).getUserId();

      ManufacturerKey lManufacturerKey = new ManufacturerKey( 0, MANUFACTURE_CD );
      iSerPartNo = new PartNoBuilder().withOemPartNo( PART_NO_OEM_SER )
            .withShortDescription( PART_NO_SDESC ).manufacturedBy( lManufacturerKey )
            .withStatus( RefPartStatusKey.ACTV ).withInventoryClass( RefInvClassKey.SER )
            .withFinancialType( RefFinanceTypeKey.ROTABLE ).build();

      iBatchPartNo = new PartNoBuilder().withOemPartNo( PART_NO_OEM_BATCH )
            .withShortDescription( PART_NO_SDESC ).manufacturedBy( lManufacturerKey )
            .withStatus( RefPartStatusKey.ACTV ).withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM )
            .withTotalQuantity( new BigDecimal( QTY_AS_1 ) )
            .withTotalValue( new BigDecimal( "5.00" ) )
            .withAverageUnitPrice( new BigDecimal( "5.00" ) ).build();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHrKey ) );

      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setString( "TOBE_INST_NOT_ISSUED", MxMessage.Type.WARNING.toString() );
      lConfigParms.setString( "TOBE_INST_BATCH_NOT_ISSUED", MxMessage.Type.WARNING.toString() );
      GlobalParameters.setInstance( lConfigParms );

      UserParametersStub lUserParametersStub = new UserParametersStub( iUserId, "LOGIC" );
      lUserParametersStub.setBoolean( "ATTACH_INVENTORY_FROM_DIFFERENT_LOCATION", true );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParametersStub );

   }


   @After
   public void tearDown() {
      MxCoreLicense.setValidator( null );

      GlobalParameters.setInstance( "LOGIC", null );
      UserParameters.setInstance( iUserId, "LOGIC", null );
   }


   /*
    * Test issue after fact for both SER and BATCH inventory when the issued inventory is the same
    * as previously installed one
    */
   @Test
   @Theory
   public void testIssueAfterFactWhenIssuedInventoryIsTheSameAsInstalled(
         RefInvClassKey aInvClassKey ) throws Exception {

      PartNoKey lPartNoKey = RefInvClassKey.SER.equals( aInvClassKey ) ? iSerPartNo : iBatchPartNo;

      // location, hr, part no, part group, inv
      LocationKey lAirportLocationKey =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();
      LocationKey lDockLocationKey = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( lAirportLocationKey ).build();

      OwnerKey lOwnerKey = new OwnerDomainBuilder().build();

      PartGroupKey lPartGroupKey = new PartGroupDomainBuilder( "Part Group" )
            .withInventoryClass( aInvClassKey ).withPartNo( lPartNoKey ).build();

      PartNoKey lMainPartNo = new PartNoBuilder().build();

      InventoryKey lMainInventory = new InventoryBuilder().withPartNo( lMainPartNo )
            .withOwner( lOwnerKey ).atLocation( lAirportLocationKey ).build();
      InventoryKey lInstalledInventory = new InventoryBuilder().withPartNo( lPartNoKey )
            .withSerialNo( SN_BN ).atLocation( lDockLocationKey ).withCondition( RefInvCondKey.RFI )
            .withOwner( lOwnerKey ).build();

      // work package, task with labour
      TaskKey lWorkPackage = new TaskBuilder().atLocation( lAirportLocationKey )
            .onInventory( lMainInventory ).build();

      TaskKey lTask = new TaskBuilder().onInventory( lMainInventory ).withParentTask( lWorkPackage )
            .atLocation( lAirportLocationKey ).withTaskClass( RefTaskClassKey.ADHOC )
            .withLabour( RefLabourSkillKey.LBR, 0 ).build();

      // task part, task install part, and part request
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( lTask ).forPart( lPartNoKey )
            .forPartGroup( lPartGroupKey ).withInstallPart( lPartNoKey )
            .withInstallInventory( lInstalledInventory ).withInstallQuantity( QTY_AS_1 )
            .withInstallSerialNumber( SN_BN ).build();

      TaskInstPartKey lTaskInstPartKey = new TaskInstPartKey( lTaskPartKey, 1 );

      PartRequestKey lPartRequest = new PartRequestBuilder().forTask( lTask )
            .forPartRequirement( lTaskInstPartKey ).withStatus( RefEventStatusKey.PROPEN )
            .requestedBy( iHrKey ).withRequestedQuantity( QTY_AS_1 )
            .isNeededAt( lAirportLocationKey ).withIssueAccount( FncAccountKey.VOID ).build();

      // WHEN
      new CompleteService( lTask ).complete( iHrKey, new Date(), null );

      // assert the part request is awaiting issue
      new EvtEventUtil( lPartRequest.getEventKey() )
            .assertEventStatus( RefEventStatusKey.PRAWAITISSUE );

      // Issue the inventory after installation
      PickIssueTO lPickIssueTO = new PickIssueTO( lPartRequest );
      lPickIssueTO.setReceivedBy( iHrKey, false, "" );
      lPickIssueTO.addPickedItem( new PickedItem( lInstalledInventory, QTY_AS_1, null ) );

      IssueInventoryService.issueInventory( lPickIssueTO, iHrKey );

      // assert the part request is issued
      new EvtEventUtil( lPartRequest.getEventKey() )
            .assertEventStatus( RefEventStatusKey.PRISSUED );

      // assert the requested SER inventory is issued and in INSRV status
      if ( RefInvClassKey.SER.equals( aInvClassKey ) ) {
         InvInv lInvInv = new InvInv( lInstalledInventory );
         lInvInv.assertIssuedBoolean( true );
         lInvInv.assertCondCd( RefInvCondKey.INSRV.getCd() );
      }

   }

}

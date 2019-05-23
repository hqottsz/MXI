package com.mxi.mx.core.services.part;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Manufacturer;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigurationSlotBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.alert.Alert;
import com.mxi.mx.common.alert.AlertEngine;
import com.mxi.mx.common.alert.AlertEngineFake;
import com.mxi.mx.common.alert.MxAlertEngine;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.alert.part.CreatePartAlert;
import com.mxi.mx.core.alert.part.CreatePartAlert.Parameter;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.BitmapKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefAssmblClassKey;
import com.mxi.mx.core.key.RefEngUnitKey;
import com.mxi.mx.core.key.RefFinancialClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartUseKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.services.fnc.InvalidRepairOrderAccountException;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.eqp.EqpPartAltUnit;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.eqp.EqpStockNoTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Integration tests for {@link PartNoService}
 *
 */
public class PartNoServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String PART_NAME = "PART_NAME";
   private static final String INVENTORY_CLASS_CODE = RefInvClassKey.SER.getCd();
   private static final String FINANCIAL_CLASS_CODE = RefFinancialClassKey.KIT.getCd();
   private static final String ACCOUNT_CODE = "ACCOUNT_CODE";
   private static final String OEM_PART_NO = "OEM_PART_NO";
   private static final double SCRAP_RATE = 0.0;
   private static final String MANUFACTURER_CODE = "MAN_CODE";
   private static final String HR_USERNAME = "HR_USERNAME";
   private static final String COMHW_ASSEMBLY_CODE = "COMHW_CD";
   private static final String TOOL_ASSEMBLY_CODE = "TOOL_CD";
   private final BigDecimal BOX_PER_EA = new BigDecimal( 0.25 );
   private final RefQtyUnitKey BOX = new RefQtyUnitKey( 10, "BOX" );
   private final RefQtyUnitKey BAG = new RefQtyUnitKey( 10, "BAG" );
   private final RefQtyUnitKey EA = RefQtyUnitKey.EA;
   private final BigDecimal BAG_QT = new BigDecimal( 2 );
   private PartNoKey iPartNo;

   private PartNoService partNoService;


   @Before
   public void setUp() {
      partNoService = new PartNoService();
   }


   /**
    *
    * Verify that a part can be created using an expense account as its repair order account.
    *
    * @throws Exception
    */
   @Test
   public void itCreatesPartWithExpenseAccountAsItsRepairOrderAccount() throws Exception {

      // Given an expense account.
      FncAccountKey lExpenseAccount = new AccountBuilder().withType( RefAccountTypeKey.EXPENSE )
            .withCode( ACCOUNT_CODE ).build();

      // Given a transfer object with its repair order account set to the expense account.
      PartDetailsTO lTO = generatePartDetailsTOWithMandatoryAttrs();
      lTO.setRepairOrderAccount( ACCOUNT_CODE );

      // When a part is created using the transfer object.
      PartNoKey lPartNo = partNoService.createPartNo( lTO, null, null, null );

      // Then the part is created using the expense account as its repair order account.
      assertEquals( "Unexpected repair order account.",
            EqpPartNoTable.findByPrimaryKey( lPartNo ).getRepairOrderAccount(), lExpenseAccount );

   }


   /**
    *
    * Verify that a part cannot be created using a non-expense account as its repair order account.
    * It is expected that a {@linkplain InvalidRepairOrderAccountException} be thrown.
    *
    * @throws Exception
    */
   @Test( expected = InvalidRepairOrderAccountException.class )
   public void itDoesNotCreatePartWithNonExpenseAccountAsItsRepairOrderAccount() throws Exception {

      // Given an non-expense account.
      new AccountBuilder().withType( RefAccountTypeKey.INVASSET ).withCode( ACCOUNT_CODE ).build();

      // Given a transfer object with its repair order account set to the expense account.
      PartDetailsTO lTO = generatePartDetailsTOWithMandatoryAttrs();
      lTO.setRepairOrderAccount( ACCOUNT_CODE );

      // When a part is created using the transfer object.
      partNoService.createPartNo( lTO, null, null, null );

      // Then an InvalidRepairOrderAccountException is thrown (refer to Test expected class).
   }


   /**
    *
    * Verify that the mandatory part information is persisted when a part is created.
    *
    * @throws Exception
    */
   @Test
   public void itPersistsMandatoryPartInformationWhenCreatingPart() throws Exception {

      // When a part is created with populated mandatory attributes.
      PartDetailsTO lTO = new PartDetailsTO();
      lTO.setFinancialClass( FINANCIAL_CLASS_CODE );
      lTO.setInventoryClass( INVENTORY_CLASS_CODE );
      lTO.setBitmapName( BitmapKey.PLANE1_NAME );
      lTO.setOemPartNo( OEM_PART_NO );
      lTO.setScrapRate( SCRAP_RATE );

      PartNoKey lPartNo = partNoService.createPartNo( lTO, null, null, null );

      // Then the mandatory part information is persisted.
      EqpPartNoTable lEqpPartNo = EqpPartNoTable.findByPrimaryKey( lPartNo );

      assertTrue( "Part not created.", lEqpPartNo.exists() );
      assertEquals( "Unexpected financial class code.", FINANCIAL_CLASS_CODE,
            lEqpPartNo.getFinancialClass().getCd() );
      assertEquals( "Unexpected inventory class code.", INVENTORY_CLASS_CODE,
            lEqpPartNo.getInvClass().getCd() );
      assertEquals( "Unexpected bitmap name.", BitmapKey.PLANE1, lEqpPartNo.getBitMap() );
      assertEquals( "Unexpected OEM part number code.", OEM_PART_NO, lEqpPartNo.getPartNoOEM() );
      assertEquals( "Unexpected scrap rate.", Double.valueOf( SCRAP_RATE ),
            lEqpPartNo.getScrapRatePct() );
   }


   /**
    *
    * Verify that the optional part information is persisted when a part is created. This optional
    * information does not drive additional logic, it is simply stored in the DB.
    *
    * Note: some information refers to ref terms of which there are none in the DB. So these will
    * not be tested (not deemed to be susceptible to failure).
    *
    * @throws Exception
    */
   @Test
   public void itPersistsOptionalPartInformationWhenCreatingPart() throws Exception {

      // When a part is created with optional information.
      PartDetailsTO lTO = generatePartDetailsTOWithMandatoryAttrs();
      lTO.setAbcClassification( RefAbcClassKey.A.getCd() );
      lTO.setCalcABCClass( true );
      lTO.setControlledReservation( false );
      lTO.setCurrent( RefEngUnitKey.CYCLES.getCd(), Double.valueOf( 2.2d ) );
      lTO.setDangerousGoods( "DangerousGoods" );
      lTO.setDataType( DataTypeKey.CDY );
      lTO.setDescription( "Description" );
      lTO.setDimensions( RefEngUnitKey.HOUR.getCd(), Double.valueOf( 3.3d ), Double.valueOf( 4.4d ),
            Double.valueOf( 5.5d ) );
      lTO.setDocumentsRequiredDescription( "DocumentsRequiredDescription" );
      lTO.setECCN( "ECCN" );
      lTO.setETOPS( true );
      lTO.setFrequency( RefEngUnitKey.HOURS.getCd(), Double.valueOf( 6.6d ) );
      lTO.setInspectionRequiredOnReceipt( false );
      lTO.setManufacturer( ManufacturerKey.LOCAL );
      lTO.setModel( "Model" );
      lTO.setMtbf( Double.valueOf( 7.7d ) );
      lTO.setMtbr( Double.valueOf( 8.8d ) );
      lTO.setMtbur( Double.valueOf( 9.9d ) );
      lTO.setMttr( Double.valueOf( 10.1d ) );
      lTO.setNoAutoReservation( true );
      lTO.setPackagingInstructions( "PackagingInstructions" );
      lTO.setPartName( "PartName" );
      lTO.setPartUse( RefPartUseKey.TOOLS.getCd() );
      lTO.setPMAPart( true );
      lTO.setPower( RefEngUnitKey.CYCLES.getCd(), Double.valueOf( 11.11d ) );
      lTO.setProcurablePart( false );
      lTO.setRepairable( true );
      lTO.setRevision( "Revision" );
      lTO.setShelfLife( Double.valueOf( 12.12d ), RefEngUnitKey.HOUR.getCd() );
      lTO.setShipOrShelf( false );
      lTO.setShippingInstructions( "ShippingInstructions" );
      lTO.setStandardUnitOfMeasure( RefQtyUnitKey.EA.getCd() );
      lTO.setStorageInstructions( "StorageInstructions" );
      lTO.setTariffCode( "TariffCode" );
      lTO.setVoltage( RefEngUnitKey.HOURS.getCd(), Double.valueOf( 16.16d ) );
      lTO.setWeight( RefEngUnitKey.CYCLES.getCd(), Double.valueOf( 17.17d ) );

      PartNoKey lPartNo = partNoService.createPartNo( lTO, null, null, null );

      // Then the optional part information is persisted.
      EqpPartNoTable lEqpPartNo = EqpPartNoTable.findByPrimaryKey( lPartNo );

      assertTrue( "Part not created.", lEqpPartNo.exists() );
      assertEquals( "Unexpected Abc Classification.", RefAbcClassKey.A, lEqpPartNo.getAbcClass() );
      assertEquals( "Unexpected Calc ABC Class.", Boolean.TRUE, lEqpPartNo.isCalcAbcClass() );
      assertEquals( "Unexpected Controlled Reservation.", Boolean.FALSE,
            lEqpPartNo.isControlledReservation() );
      assertEquals( "Unexpected Current Eng Unit.", RefEngUnitKey.CYCLES,
            lEqpPartNo.getCurrentEngUnit() );
      assertEquals( "Unexpected Current Qty.", Double.valueOf( 2.2d ), lEqpPartNo.getCurrentOt() );
      assertEquals( "Unexpected Dangerous Goods.", "DangerousGoods", lEqpPartNo.getDgRefSdesc() );
      assertEquals( "Unexpected Data Type.", DataTypeKey.CDY, lEqpPartNo.getDataType() );
      assertEquals( "Unexpected Description.", "Description", lEqpPartNo.getLDesc() );
      assertEquals( "Unexpected Dimension Eng Unit.", RefEngUnitKey.HOUR,
            lEqpPartNo.getDimensionEngUnit() );
      assertEquals( "Unexpected Height.", Double.valueOf( 3.3d ), lEqpPartNo.getHeightQt() );
      assertEquals( "Unexpected Width.", Double.valueOf( 4.4d ), lEqpPartNo.getWidthQt() );
      assertEquals( "Unexpected Length.", Double.valueOf( 5.5d ), lEqpPartNo.getLengthQt() );
      assertEquals( "Unexpected Documents Required Description.", "DocumentsRequiredDescription",
            lEqpPartNo.getDocumentsRequiredDescription() );
      assertEquals( "Unexpected ECCN.", "ECCN", lEqpPartNo.getECCN() );
      assertEquals( "Unexpected ETOPS.", Boolean.TRUE, lEqpPartNo.isETOPSReady() );
      assertEquals( "Unexpected Frequency Eng Unit.", RefEngUnitKey.HOURS,
            lEqpPartNo.getFreqEngUnit() );
      assertEquals( "Unexpected Frequency Qty.", Double.valueOf( 6.6d ), lEqpPartNo.getFreqQt() );
      assertEquals( "Unexpected Inspection Required.", Boolean.FALSE,
            lEqpPartNo.isReceiptInspBool() );
      assertEquals( "Unexpected Manufacturer.", ManufacturerKey.LOCAL,
            lEqpPartNo.getManufacturer() );
      assertEquals( "Unexpected Model.", "Model", lEqpPartNo.getModel() );
      assertEquals( "Unexpected MTBF.", Double.valueOf( 7.7d ), lEqpPartNo.getMtbfQt() );
      assertEquals( "Unexpected MTBR.", Double.valueOf( 8.8d ), lEqpPartNo.getMtbrQt() );
      assertEquals( "Unexpected MTBUR.", Double.valueOf( 9.9d ), lEqpPartNo.getMtburQt() );
      assertEquals( "Unexpected MTTR.", Double.valueOf( 10.10d ), lEqpPartNo.getMttrQt() );
      assertEquals( "Unexpected No Auto Reservation.", Boolean.TRUE,
            lEqpPartNo.isNoAutoReserveBool() );
      assertEquals( "Unexpected Packaging Instructions.", "PackagingInstructions",
            lEqpPartNo.getPackagingLdesc() );
      assertEquals( "Unexpected Part Name.", "PartName", lEqpPartNo.getSDesc() );
      assertEquals( "Unexpected Part Use.", RefPartUseKey.TOOLS, lEqpPartNo.getPartUse() );
      assertEquals( "Unexpected PMA Part.", Boolean.TRUE, lEqpPartNo.isPMA() );
      assertEquals( "Unexpected Power Eng Unit.", RefEngUnitKey.CYCLES,
            lEqpPartNo.getPowerEngUnit() );
      assertEquals( "Unexpected Power Qty.", Double.valueOf( 11.11d ), lEqpPartNo.getPowerQt() );
      assertEquals( "Unexpected Procurable Part.", Boolean.FALSE, lEqpPartNo.isProcurable() );
      assertEquals( "Unexpected Repairable.", Boolean.TRUE, lEqpPartNo.isRepairableBool() );
      assertEquals( "Unexpected Revision.", "Revision", lEqpPartNo.getRevision() );
      assertEquals( "Unexpected Shelf Life Eng Unit.", RefEngUnitKey.HOUR.getCd(),
            lEqpPartNo.getShelfLifeUnitCd() );
      assertEquals( "Unexpected Shelf Life Qty.", Double.valueOf( 12.12d ),
            lEqpPartNo.getShelfLifeQt() );
      assertEquals( "Unexpected Ship Or Shelf.", Boolean.FALSE, lEqpPartNo.isShipOrShelf() );
      assertEquals( "Unexpected Shipping Instructions.", "ShippingInstructions",
            lEqpPartNo.getShippingLdesc() );
      assertEquals( "Unexpected Standard Unit Of Measure.", RefQtyUnitKey.EA,
            lEqpPartNo.getQtyUnit() );
      assertEquals( "Unexpected Storage Instructions.", "StorageInstructions",
            lEqpPartNo.getStorageLdesc() );
      assertEquals( "Unexpected Tariff Code.", "TariffCode", lEqpPartNo.getTariffCd() );
      assertEquals( "Unexpected Voltage Eng Unit.", RefEngUnitKey.HOURS.getCd(),
            lEqpPartNo.getVoltageEngUnitCd() );
      assertEquals( "Unexpected Voltage Qty.", Double.valueOf( 16.16d ),
            lEqpPartNo.getVoltageQt() );
      assertEquals( "Unexpected Weight Eng Unit.", RefEngUnitKey.CYCLES.getCd(),
            lEqpPartNo.getWeightEngUnitCd() );
      assertEquals( "Unexpected Weight Qty.", Double.valueOf( 17.17d ), lEqpPartNo.getWeightQt() );
   }


   /**
    *
    * Verify that a common hardware part group is created for the root config slot of a common
    * hardware assembly when a part is created and that part requires a common hardware part group.
    *
    * @throws Exception
    */
   @Test
   public void itCreatesPartGroupWhenCreatingPartThatRequiresCommonHardwarePartGroup()
         throws Exception {

      // In order for the PartNoService to set up a common hardware part group the following must be
      // available:
      // <pre>
      // - Common hardware assembly with a root config slot.
      // - Manufacturer, whose code will be used to generate a part group code.
      // - Authorizing HR with the permission to approve a part for a part group.
      // </pre>
      AssemblyKey lAssembly = createCommonHardwareAssembly();
      ManufacturerKey lManufacturer = createManufacturer( MANUFACTURER_CODE );
      HumanResourceKey lAuthHr = createAuthorizingHr();

      // When a part is created requiring a common hardware part group.
      PartDetailsTO lTO = new PartDetailsTO();
      lTO.setFinancialClass( FINANCIAL_CLASS_CODE );
      lTO.setInventoryClass( INVENTORY_CLASS_CODE );
      lTO.setBitmapName( BitmapKey.PLANE1_NAME );
      lTO.setOemPartNo( OEM_PART_NO );
      lTO.setScrapRate( SCRAP_RATE );

      lTO.setCreateCOMHWPartGrp( true );
      lTO.setManufacturer( lManufacturer );
      lTO.setPartName( PART_NAME );

      PartNoKey lPartNo = partNoService.createPartNo( lTO, lAuthHr, null, null );

      // Then a common hardware part group is created.
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "eqp_part_baseline",
            lPartNo.getPKWhereArg() );
      lQs.next();
      assertEquals( "Unexpected number of parts groups created.", 1, lQs.getRowCount() );

      EqpBomPart lPartGroup = EqpBomPart
            .findByPrimaryKey( lQs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( "Unexpected part group code.", OEM_PART_NO + " - " + MANUFACTURER_CODE,
            lPartGroup.getBomPartCd() );
      assertEquals( "Unexpected part group name.", PART_NAME, lPartGroup.getBomPartName() );
      assertEquals( "Unexpected part group assembly.", lAssembly, lPartGroup.getAssembly() );
      assertEquals( "Unexpected part group inv class code.", INVENTORY_CLASS_CODE,
            lPartGroup.getInvClassCd() );
      assertEquals( "Unexpected part group part quantity.", Double.valueOf( 1.0 ),
            Double.valueOf( lPartGroup.getPartQt() ) );
   }


   /**
    *
    * Verify that a tool part group is created for the root config slot of a tool assembly when a
    * part is created and that part requires a tool part group.
    *
    * @throws Exception
    */
   @Test
   public void itCreatesPartGroupWhenCreatingPartThatRequiresToolPartGroup() throws Exception {

      // In order for the PartNoService to set up a tool part group the following must be
      // available:
      // <pre>
      // - Tool assembly with a root config slot.
      // - Manufacturer, whose code will be used to generate a part group code.
      // - Authorizing HR with the permission to approve a part for a part group.
      // </pre>
      AssemblyKey lAssembly = createToolAssembly();
      ManufacturerKey lManufacturer = createManufacturer( MANUFACTURER_CODE );
      HumanResourceKey lAuthHr = createAuthorizingHr();

      // When a part is created requiring a tool part group.
      PartDetailsTO lTO = new PartDetailsTO();
      lTO.setFinancialClass( FINANCIAL_CLASS_CODE );
      lTO.setInventoryClass( INVENTORY_CLASS_CODE );
      lTO.setBitmapName( BitmapKey.PLANE1_NAME );
      lTO.setOemPartNo( OEM_PART_NO );
      lTO.setScrapRate( SCRAP_RATE );

      lTO.setCreateToolPartGrp( true );
      lTO.setManufacturer( lManufacturer );
      lTO.setPartName( PART_NAME );

      PartNoKey lPartNo = partNoService.createPartNo( lTO, lAuthHr, null, null );

      // Then a tool part group is created.
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "eqp_part_baseline",
            lPartNo.getPKWhereArg() );
      lQs.next();
      assertEquals( "Unexpected number of parts groups created.", 1, lQs.getRowCount() );

      EqpBomPart lPartGroup = EqpBomPart
            .findByPrimaryKey( lQs.getKey( PartGroupKey.class, "bom_part_db_id", "bom_part_id" ) );
      assertEquals( "Unexpected part group code.", OEM_PART_NO, lPartGroup.getBomPartCd() );
      assertEquals( "Unexpected part group name.", PART_NAME, lPartGroup.getBomPartName() );
      assertEquals( "Unexpected part group assembly.", lAssembly, lPartGroup.getAssembly() );
      assertEquals( "Unexpected part group inv class code.", INVENTORY_CLASS_CODE,
            lPartGroup.getInvClassCd() );
      assertEquals( "Unexpected part group part quantity.", Double.valueOf( 1.0 ),
            Double.valueOf( lPartGroup.getPartQt() ) );
   }


   /**
    *
    * Verify that a stock number is created when creating a part that requires a stock number and
    * provides a standard unit of measure.
    *
    * It is expected that the code and inventory class of the stock number will match that of the
    * created part.
    *
    * @throws Exception
    *
    *            Note on why Ignored: PartNoService uses a class variable to hold a
    *            StockLevelWorkItemGenerator, StockLevelWorkItemGenerator has a class variable to
    *            hold a QueryAccessObject which it populates by calling
    *            QueryAccessObject.getInstance(). Each time a test method is called the
    *            DatabaseConnectionRule sets the QueryAccessObject and when a test method completes
    *            it sets it to null. Unfortunately, the StockLevelWorkItemGenerator class variable
    *            retains its original QueryAccessObject which is closed when DatabaseConnectionRule
    *            sets the QueryAccessObject to null.
    *
    *            Once we can make PartNoService and StockLevelWorkItemGenerator testable then this
    *            test can no longer be ignored.
    *
    */
   @Ignore
   @Test
   public void itCreatesStockNumberWhenCreatingPartThatRequiresStockNumber() throws Exception {

      // When a part is created requesting a stock number to be created.
      PartDetailsTO lTO = new PartDetailsTO();

      lTO.setFinancialClass( FINANCIAL_CLASS_CODE );
      lTO.setInventoryClass( INVENTORY_CLASS_CODE );
      lTO.setBitmapName( BitmapKey.PLANE1_NAME );
      lTO.setOemPartNo( OEM_PART_NO );
      lTO.setScrapRate( SCRAP_RATE );

      lTO.setPartName( PART_NAME );
      lTO.setCreateStockNo( true );
      lTO.setStandardUnitOfMeasure( RefQtyUnitKey.EA.getCd() );
      lTO.setAbcClassification( RefAbcClassKey.B.getCd() );

      PartNoKey lPartNo = partNoService.createPartNo( lTO, null, null, null );

      // Then a stock number is created.
      EqpStockNoTable lStockNumber = EqpStockNoTable
            .findByPrimaryKey( EqpPartNoTable.findByPrimaryKey( lPartNo ).getStockNumber() );
      assertTrue( "Stock number not created.", lStockNumber.exists() );
      assertEquals( "Unexpected unit of measure.", RefQtyUnitKey.EA, lStockNumber.getQtyUnitCd() );
      assertEquals( "Unexpected ABC class code.", RefAbcClassKey.B, lStockNumber.getAbcClass() );

      // Then the stock name, code, and inventory class code match those of the part.
      assertEquals( "Unexpected stock name.", PART_NAME, lStockNumber.getStockNoName() );
      assertEquals( "Unexpected stock code.", OEM_PART_NO, lStockNumber.getStockNoCd() );
      assertEquals( "Unexpected stock inventory class code.", INVENTORY_CLASS_CODE,
            lStockNumber.getInvClassCd() );
   }


   /**
    *
    * Verify that a "create part" alert is sent when creating a part.
    *
    * @throws Exception
    */
   @Test
   public void itSendsAnAlertWhenPartCreated() throws Exception {

      // In order to test the sending of alerts a fake AlertEngine needs to be used.
      AlertEngine lOrigAlertEngine = MxAlertEngine.getInstance();
      MxAlertEngine.setInstance( new AlertEngineFake() );

      // When a part is created.
      PartDetailsTO lTO = generatePartDetailsTOWithMandatoryAttrs();
      PartNoKey lPartNo = partNoService.createPartNo( lTO, null, null, null );

      Set<Alert> lSentAlerts = ( ( AlertEngineFake ) MxAlertEngine.getInstance() ).getSentAlerts();

      // Reset the AlertEngine (before any asserts).
      MxAlertEngine.setInstance( lOrigAlertEngine );

      assertEquals( "Unexpected number of sent alerts.", 1, lSentAlerts.size() );

      // Then a create-part alert was sent.
      Alert lSentAlert = lSentAlerts.iterator().next();
      assertTrue( "Unexpected sent alert type.", lSentAlert instanceof CreatePartAlert );

      // Then the create-part alert parameters contain the part number.
      CreatePartAlert lCreatePartAlert = ( CreatePartAlert ) lSentAlert;
      assertEquals( "Unexpected part number in sent alert.", lPartNo.toString(),
            lCreatePartAlert.getParameters()[Parameter.PART_NO.ordinal()].getValue() );
   }


   /**
    * GIVEN, a batch part with standard uom and other alternate uoms with appropriate conversion
    * factors in comparison to the standard uom
    *
    * WHEN, change standard uom function is performed with new standard uom (EA) with appropriate
    * conversion factor
    *
    * THEN, standard uom should be changed to the new value, old standard uom should be added as an
    * alternate uom with the conversion factor, existing alternate standard uom conversion factors
    * should be changed in comparison to new standard uom
    *
    * @throws MxException
    * @throws SQLException
    */
   @Test
   public void testChangeStandardUnitOfMeasureForBatchToSerial() throws MxException, SQLException {

      // Setup
      final BigDecimal CONVERSION_FACTOR = BOX_PER_EA;

      iPartNo = createPartWithAlternateMeasures( RefInvClassKey.BATCH, BOX, EA );

      // Action

      // perform changeStandardUnitOfMeasure
      PartNoService.changeStandardUnitOfMeasure( iPartNo, EA, CONVERSION_FACTOR.doubleValue() );

      // Asserts

      // assert standard uom is changed to the new value
      assertStandardUom( EA );

      // assert old standard uom is added as alternate uom
      assertQtyConvertQt( BOX, BigDecimal.ONE, CONVERSION_FACTOR );

      // assert existing alternate uom conversion factor is changed
      assertQtyConvertQt( BAG, BAG_QT, CONVERSION_FACTOR );

      // assert new standard uom is removed from alternate uoms
      assertQtyUnitNotExist( EA );

   }


   /**
    * GIVEN, a serial part with standard uom as EA and alternate uoms with appropriate conversion
    * factors in comparison to EA
    *
    * WHEN, change standard uom function is performed with new standard uom with appropriate
    * conversion factor in comparison to EA
    *
    * THEN, standard uom should be changed to the new value, old standard uom should be added as an
    * alternate uom with the conversion factor, existing alternate standard uom conversion factors
    * should be changed in comparison to new standard uom
    *
    * @throws MxException
    * @throws SQLException
    */
   @Test
   public void testChangeStandardUnitOfMeasureForSerialToBatch() throws MxException, SQLException {

      // Setup
      final BigDecimal CONVERSION_FACTOR =
            BigDecimal.ONE.divide( BOX_PER_EA ).setScale( 3, BigDecimal.ROUND_CEILING );

      iPartNo = createPartWithAlternateMeasures( RefInvClassKey.SER, EA, BOX );

      // Action

      // perform changeStandardUnitOfMeasure
      PartNoService.changeStandardUnitOfMeasure( iPartNo, BOX, CONVERSION_FACTOR.doubleValue() );

      // Asserts

      // assert standard uom is changed to the new value
      assertStandardUom( BOX );

      // assert old standard uom is added as alternate uom
      assertQtyConvertQt( EA, BigDecimal.ONE, CONVERSION_FACTOR );

      // assert existing alternate uom conversion factor is changed
      assertQtyConvertQt( BAG, BAG_QT, CONVERSION_FACTOR );

      // assert new standard uom is removed from alternate uoms
      assertQtyUnitNotExist( BOX );

   }


   private void assertStandardUom( RefQtyUnitKey aQtyUnit ) {
      EqpPartNoTable lPart = EqpPartNoTable.findByPrimaryKey( iPartNo );
      assertEquals( lPart.getQtyUnitCd(), aQtyUnit.getCd() );
   }


   private void assertQtyConvertQt( RefQtyUnitKey aQtyUnit, BigDecimal aCurrentConFactor,
         BigDecimal aNewConvFactor ) {
      EqpPartAltUnit lAltUnit = EqpPartAltUnit.findByPrimaryKey( iPartNo, aQtyUnit );
      assertEquals(
            aCurrentConFactor.multiply( aNewConvFactor ).setScale( 3, BigDecimal.ROUND_CEILING ),
            lAltUnit.getQtyConvertQt().setScale( 3, BigDecimal.ROUND_CEILING ) );
   }


   private void assertQtyUnitNotExist( RefQtyUnitKey aQtyUnit ) {
      EqpPartAltUnit lAltUnit = EqpPartAltUnit.findByPrimaryKey( iPartNo, aQtyUnit );
      assertEquals( false, lAltUnit.exists() );
   }


   private PartNoKey createPartWithAlternateMeasures( RefInvClassKey aInvClass,
         RefQtyUnitKey aCurrentStandardUom, RefQtyUnitKey aNewStandardUom ) {

      // create unit of measure data
      DataSetArgument args = new DataSetArgument();
      args.add( BAG, "qty_unit_db_id", "qty_unit_cd" );
      MxDataAccess.getInstance().executeInsert( "ref_qty_unit", args );

      args = new DataSetArgument();
      args.add( BOX, "qty_unit_db_id", "qty_unit_cd" );
      MxDataAccess.getInstance().executeInsert( "ref_qty_unit", args );

      Map<RefQtyUnitKey, BigDecimal> lAlternateUnitTypes = new HashMap<RefQtyUnitKey, BigDecimal>();
      lAlternateUnitTypes.put( BAG, BAG_QT );
      lAlternateUnitTypes.put( aNewStandardUom, new BigDecimal( 3 ) );

      return new PartNoBuilder().withOemPartNo( OEM_PART_NO ).withInventoryClass( aInvClass )
            .withUnitType( aCurrentStandardUom ).withAlternateUnitTypes( lAlternateUnitTypes )
            .build();

   }


   private PartDetailsTO generatePartDetailsTOWithMandatoryAttrs() throws Exception {
      PartDetailsTO lTO = new PartDetailsTO();
      lTO.setFinancialClass( FINANCIAL_CLASS_CODE );
      lTO.setInventoryClass( INVENTORY_CLASS_CODE );
      lTO.setBitmapName( BitmapKey.PLANE1_NAME );
      lTO.setOemPartNo( OEM_PART_NO );
      lTO.setScrapRate( SCRAP_RATE );
      return lTO;
   }


   private AssemblyKey createCommonHardwareAssembly() {
      AssemblyKey lAssemblyKey =
            new AssemblyBuilder( COMHW_ASSEMBLY_CODE ).withClass( RefAssmblClassKey.COMHW ).build();

      ConfigurationSlot lRootConfigSlot = new ConfigurationSlot();
      lRootConfigSlot.setRootAssembly( lAssemblyKey );
      ConfigurationSlotBuilder.build( lRootConfigSlot );

      return lAssemblyKey;
   }


   private AssemblyKey createToolAssembly() {
      AssemblyKey lAssemblyKey =
            new AssemblyBuilder( TOOL_ASSEMBLY_CODE ).withClass( RefAssmblClassKey.TSE ).build();

      ConfigurationSlot lRootConfigSlot = new ConfigurationSlot();
      lRootConfigSlot.setRootAssembly( lAssemblyKey );
      ConfigurationSlotBuilder.build( lRootConfigSlot );

      return lAssemblyKey;
   }


   private ManufacturerKey createManufacturer( final String aManufacturerCode ) {
      return Domain.createManufacturer( new DomainConfiguration<Manufacturer>() {

         @Override
         public void configure( Manufacturer aBuilder ) {
            aBuilder.setCode( aManufacturerCode );
         }
      } );
   }


   private HumanResourceKey createAuthorizingHr() {
      HumanResourceKey lAuthHr =
            new HumanResourceDomainBuilder().withUsername( HR_USERNAME ).build();
      int lUserId = OrgHr.findByPrimaryKey( lAuthHr ).getUserId();
      UserParameters.setInstance( lUserId, "SECURED_RESOURCE",
            new UserParametersFake( lUserId, "SECURED_RESOURCE" ) );
      return lAuthHr;
   }

}

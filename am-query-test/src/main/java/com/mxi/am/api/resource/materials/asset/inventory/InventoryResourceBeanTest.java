package com.mxi.am.api.resource.materials.asset.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBContext;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.asset.inventory.impl.InventoryResourceBean;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class InventoryResourceBeanTest extends ResourceBeanTest {

   private static final String MANUFACT_CODE = "TEST";
   private static final String PART_NUMBER = "15861";
   private static final String SERIAL_NUMBER = "200";
   private static final String ASSEMBLY_ID = "C883165B5E054D98866CCE4285A5F132";
   private static final String CONFIG_SLOT_ID = "3C27E28931D0496A9DE261F8B88DF391";
   private static final String PART_GROUP_ID = "13015E6588494ED1A8CF6EA367824A0C";
   private static final String POSITION_NAME = "1.1";
   private static final String OWNER_ID = "8B30A62959B042F5AA095727EB0A97C5";
   private static final String OWNER_ID_OTHER = "68FD8EDF9131483DBE3BA6ADAB1AE023";
   private static final String LOCATION_ID = "5B5CD8E992DA429E8FAAB0DF1077E0F9";
   private static final String TRK = "TRK";
   private static final String CHILD_PART_NUMBER = "15861-1";
   private static final String CHILD_SERIAL_NUMBER = "XXX";
   private static final String CHILD_CONFIG_SLOT_ID = "5C27E28931D0496A9DE261F8B88DF396";
   private static final String CHILD_PART_GROUP_ID = "F3015E6588494ED1A8CF6EA367824A0A";
   private static final String CHILD_POSITION_NAME = "1.1.1";

   private static final String SINGLE_TRK_PART_NUMBER = "15862";
   private static final String SINGLE_TRK_SERIAL_NUMBER = "300";
   private static final String SINGLE_TRK_CONFIG_SLOT_ID = "AB27E28931D0496A9DE261F8B88DF39C";
   private static final String SINGLE_TRK_PART_GROUP_ID = "A3015E6588494ED1A8CF6EA367824A77";
   private static final String SINGLE_TRK_POSITION_NAME = "1.2";

   private static final String RFI = "RFI";
   private static final String INSPREQ = "INSPREQ";
   private static final String REPREQ = "REPREQ";
   private static final String INREP = "INREP";

   private static final String RECEIVED_CONDITION_NEW = "NEW";
   private static final String RECEIVED_CONDITION_UNKNOWN = "UNKNOWN";

   private static final Date MANUFACTURED_DATE = Date
         .from( LocalDate.of( 2017, 12, 01 ).atStartOfDay( ZoneId.systemDefault() ).toInstant() );
   private static final Date RECEIVED_DATE = Date
         .from( LocalDate.of( 2017, 12, 31 ).atStartOfDay( ZoneId.systemDefault() ).toInstant() );
   private static final Date RELEASE_DATE = Date
         .from( LocalDate.of( 2018, 1, 31 ).atStartOfDay( ZoneId.systemDefault() ).toInstant() );
   private static final String RELEASE_REMARKS = "RAMBLINGS";
   private static final String RELEASE_NUMBER = "2";

   private static final String ERROR_MESSAGE1 =
         "[MXERR-30159] The inventory ' (PN: 15862, SN: 300)' has a condition of 'REPREQ' but should be 'INSPREQ, QUAR, ARCHIVE, SCRAP'.";
   private static final String ERROR_MESSAGE2 =
         "[MXERR-30159] The inventory ' (PN: 15862, SN: 300)' has a condition of 'INREP' but should be 'RFI, QUAR, CONDEMN, REPREQ, INSPREQ'.";

   private static final String INVALID_HIGHEST_INVENTORY_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String INVALID_CONFIG_SLOT_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String INVALID_PART_NO = "FFFFF";
   private static final String INVALID_SERIAL_NO = "FFFFF";

   private static final String HIGHEST_INVENTORY_ID = "FF99005DC21C11E893510050568B2740";
   private static final String BARCODE2 = "I40S0002JH90";
   private static final String BARCODE3 = "I40S0002JH91";
   private static final String SERIAL_NUMBER2 = "201";
   private static final String CONFIG_SLOT_CD = "72-00-00";

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Inject
   InventoryResourceBean iInventoryResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;


   @Before
   public void setUp() throws MxException, AmApiBusinessException {
      InjectorContainer.get().injectMembers( this );

      iInventoryResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

      int lUserId = 2;
      UserParametersFake lUserParms = new UserParametersFake( lUserId, ParmTypeEnum.LOGIC.name() );
      lUserParms.setBoolean( "ALLOW_AUTO_COMPLETION", false );
      UserParameters.setInstance( lUserId, ParmTypeEnum.LOGIC.name(), lUserParms );

   }


   @Test
   @CSIContractTest( { Project.SWA_AC_CAPABILITIES, Project.SWA_AC_STATUS, Project.SWA_FAULT_STATUS,
         Project.SWA_WP_STATUS } )
   public void get_success() throws AmApiResourceNotFoundException {
      Inventory expectedInv = new Inventory();
      expectedInv.setPartNumber( PART_NUMBER );
      expectedInv.setSerialNumber( SERIAL_NUMBER2 );
      expectedInv.setManufacturerCode( MANUFACT_CODE );
      expectedInv.setAssemblyId( ASSEMBLY_ID );
      expectedInv.setConfigSlotId( CONFIG_SLOT_ID );
      expectedInv.setOwnerId( OWNER_ID );
      expectedInv.setLocationId( LOCATION_ID );
      expectedInv.setInventoryClass( TRK );

      Inventory inventory = iInventoryResourceBean.get( HIGHEST_INVENTORY_ID );

      assertEquals( "Inventory returned has an incorrect id: ", HIGHEST_INVENTORY_ID,
            inventory.getId() );

      assertInventory( expectedInv, inventory );
   }


   /**
    * Test create inventory tree.
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void create_success_createNewInventoryTree()
         throws AmApiBusinessException, AmApiResourceNotFoundException {

      Inventory lGivenInventory = getInventory();
      lGivenInventory.setInventoryClass( TRK );

      InventoryModifyParameters lInventoryModifyParameters = new InventoryModifyParameters();
      Inventory lCreatedParentInventory =
            iInventoryResourceBean.create( lGivenInventory, lInventoryModifyParameters );

      Inventory lGivenChildInventory = getChildInventory( lCreatedParentInventory );

      assertNotNull( "Created parent inventory cannot be null.", lCreatedParentInventory );
      assertInventory( lGivenInventory, lCreatedParentInventory );

      InventorySearchParameters lInventorySearchParameters =
            new InventorySearchParameters( lGivenChildInventory.getPartNumber(), null,
                  lGivenChildInventory.getManufacturerCode(), null, null, null, null );
      List<Inventory> lResult = iInventoryResourceBean.search( lInventorySearchParameters );
      assertNotNull( "Retrieved inventory list cannot be null.", lResult );
      assertEquals( "Retrieved inventory list can only contain one inventory.", 1, lResult.size() );
      Inventory lRetrievedChildInventory = lResult.get( 0 );

      assertInventory( lGivenChildInventory, lRetrievedChildInventory );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void create_success() throws AmApiBusinessException, AmApiResourceNotFoundException {
      Inventory lInventory = getInventory();
      lInventory.setReceivedCondition( null );

      Inventory lResponseInventory =
            iInventoryResourceBean.create( lInventory, new InventoryModifyParameters() );
      lInventory.setReceivedCondition( RECEIVED_CONDITION_UNKNOWN );
      lInventory.setInventoryClass( TRK );
      assertNotNull( "Retrieved inventory cannot be null.", lResponseInventory );
      assertInventory( lInventory, lResponseInventory );

   }


   @Test
   @CSIContractTest( Project.UPS )
   public void update_success() throws AmApiBusinessException, AmApiResourceNotFoundException {
      Inventory inventory =
            iInventoryResourceBean.create( getInventory(), new InventoryModifyParameters() );
      assertNotNull( "Created inventory cannot be null.", inventory );

      inventory.setReleaseRemarks( RELEASE_REMARKS );
      inventory.setReleaseDate( RELEASE_DATE );
      inventory.setReleaseNumber( RELEASE_NUMBER );
      inventory.setReceivedCondition( RECEIVED_CONDITION_UNKNOWN );
      inventory.setManufacturedDate( MANUFACTURED_DATE );
      inventory.setReceivedDate( RECEIVED_DATE );
      inventory.setOwnerId( OWNER_ID_OTHER );

      Inventory updatedInventory = iInventoryResourceBean.update( inventory.getId(), inventory,
            new InventoryModifyParameters() );

      assertEquals( "Incorrect release remarks found in updated inventory.", RELEASE_REMARKS,
            updatedInventory.getReleaseRemarks() );
      assertEquals( "Incorrect release date found in updated inventory.", RELEASE_DATE,
            updatedInventory.getReleaseDate() );
      assertEquals( "Incorrect release number found in updated inventory.", RELEASE_NUMBER,
            updatedInventory.getReleaseNumber() );
      assertEquals( "Incorrect manufactured date found in updated inventory.", MANUFACTURED_DATE,
            updatedInventory.getManufacturedDate() );
      assertEquals( "Incorrect received date in updated inventory.", RECEIVED_DATE,
            updatedInventory.getReceivedDate() );
      assertEquals( "Incorrect owner ID found in updated inventory.", OWNER_ID_OTHER,
            updatedInventory.getOwnerId() );
   }


   @Test
   public void update_success_markInventoryAsRFI()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      InventoryModifyParameters lInventoryModifyParameters = new InventoryModifyParameters();
      Inventory lInventory =
            iInventoryResourceBean.create( getSingleTrkInventory(), lInventoryModifyParameters );
      assertNotNull( "Created inventory cannot be null.", lInventory );

      lInventory.setConditionCode( RFI );
      Inventory lUpdatedInventory = iInventoryResourceBean.update( lInventory.getId(), lInventory,
            lInventoryModifyParameters );
      assertNotNull( "Updated inventory cannot be null.", lUpdatedInventory );
      assertEquals( "Incorrect condition code found in updated inventory.", RFI,
            lUpdatedInventory.getConditionCode() );
   }


   @Test
   public void update_success_markInventoryAsINSPREQ()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      InventoryModifyParameters lInventoryModifyParameters = new InventoryModifyParameters();
      Inventory lInventory = getSingleTrkInventory();
      lInventory.setConditionCode( REPREQ );

      lInventory = iInventoryResourceBean.create( lInventory, lInventoryModifyParameters );
      assertNotNull( "Created inventory cannot be null.", lInventory );

      lInventory.setConditionCode( INSPREQ );
      Inventory lUpdatedInventory = iInventoryResourceBean.update( lInventory.getId(), lInventory,
            lInventoryModifyParameters );
      assertNotNull( "Updated inventory cannot be null.", lUpdatedInventory );
      assertEquals( "Incorrect condition code found in updated inventory.", INSPREQ,
            lUpdatedInventory.getConditionCode() );
   }


   @Test
   public void update_failure_markAsRFIException()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      InventoryModifyParameters lInventoryModifyParameters = new InventoryModifyParameters();
      Inventory lInventory = getSingleTrkInventory();
      lInventory.setConditionCode( REPREQ );

      lInventory = iInventoryResourceBean.create( lInventory, lInventoryModifyParameters );
      assertNotNull( "Created inventory cannot be null.", lInventory );

      try {
         lInventory.setConditionCode( RFI );
         iInventoryResourceBean.update( lInventory.getId(), lInventory,
               lInventoryModifyParameters );
         // If an exception isn't thrown, it's important to fail the test
         fail();
      } catch ( Exception e ) {
         assertEquals( "Incorrect error message found in exception.", ERROR_MESSAGE1,
               e.getMessage() );
      }
   }


   @Test
   public void update_failure_setAsINSPREQException()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      InventoryModifyParameters lInventoryModifyParameters = new InventoryModifyParameters();
      Inventory lInventory = getSingleTrkInventory();
      lInventory.setConditionCode( INREP );

      lInventory = iInventoryResourceBean.create( lInventory, lInventoryModifyParameters );
      assertNotNull( "Created inventory cannot be null.", lInventory );

      try {
         lInventory.setConditionCode( INSPREQ );
         lInventory = iInventoryResourceBean.update( lInventory.getId(), lInventory,
               lInventoryModifyParameters );
         // If an exception isn't thrown, it's important to fail the test
         fail();
      } catch ( Exception e ) {
         assertEquals( "Incorrect error message found in exception.", ERROR_MESSAGE2,
               e.getMessage() );
      }
   }


   /**
    *
    * Full coverage of search method. Search with all search parameters. Search returns one
    * inventory
    *
    * @throws AmApiBusinessException
    */
   @Test
   @CSIContractTest( { Project.UPS, Project.AFKLM_IMECH } )
   public void search_success_byAllParameters() throws AmApiBusinessException {

      InventorySearchParameters lInventorySearchParameters = new InventorySearchParameters();
      lInventorySearchParameters.setHighestInvId( HIGHEST_INVENTORY_ID );
      lInventorySearchParameters.setConfigSlotId( CONFIG_SLOT_ID );
      lInventorySearchParameters.setSerialNoOem( SERIAL_NUMBER2 );
      lInventorySearchParameters.setPartNoOem( PART_NUMBER );
      lInventorySearchParameters.setManufactCd( MANUFACT_CODE );
      lInventorySearchParameters.setBarcode( BARCODE2 );
      lInventorySearchParameters.setConfigSlotCd( CONFIG_SLOT_CD );

      List<Inventory> lInventoryList = iInventoryResourceBean.search( lInventorySearchParameters );
      assertTrue( "Retrieved inventory list cannot be empty.",
            CollectionUtils.isNotEmpty( lInventoryList ) );
      assertEquals( "Retrieved inventory list can only contain one inventory.", 1,
            lInventoryList.size() );

      // Assert the search result with search parameters
      Inventory lInventory = lInventoryList.get( 0 );
      assertEquals( lInventory.getHighestInventoryId(),
            lInventorySearchParameters.getHighestInvId() );
      assertEquals( "Incorrect config slot ID found in retrieved inventory.",
            lInventorySearchParameters.getConfigSlotId(), lInventory.getConfigSlotId() );
      assertEquals( "Incorrect serial number found in retrieved inventory.",
            lInventorySearchParameters.getSerialNoOem(), lInventory.getSerialNumber() );
      assertEquals( "Incorrect part number found in retrieved inventory.",
            lInventorySearchParameters.getPartNoOem(), lInventory.getPartNumber() );
      assertEquals( "Incorrect manufacturer code found in retrieved inventory.",
            lInventorySearchParameters.getManufactCd(), lInventory.getManufacturerCode() );
      assertEquals( "Incorrect barcode found in retrieved inventory.",
            lInventorySearchParameters.getBarcode(), lInventory.getBarcode() );

   }


   /**
    * Test multiple search results. Asserts only the expected barcodes.
    *
    */
   @Test
   public void search_success_multipleInventoryReturned() {
      InventorySearchParameters lInventorySearchParameters = new InventorySearchParameters();
      lInventorySearchParameters.setPartNoOem( PART_NUMBER );
      lInventorySearchParameters.setManufactCd( MANUFACT_CODE );

      List<Inventory> lInventoryList = iInventoryResourceBean.search( lInventorySearchParameters );
      assertTrue( "Retrieved inventory list cannot be empty.",
            CollectionUtils.isNotEmpty( lInventoryList ) );
      assertEquals( "Retrieved inventory list must contain two inventories.", 2,
            lInventoryList.size() );

      assertTrue( "The first inventory does not have one of the expected barcodes.",
            lInventoryList.get( 0 ).getBarcode().equals( BARCODE2 )
                  || lInventoryList.get( 0 ).getBarcode().equals( BARCODE3 ) );
      assertTrue( "The second inventory does not have one of the expected barcodes.",
            lInventoryList.get( 1 ).getBarcode().equals( BARCODE2 )
                  || lInventoryList.get( 1 ).getBarcode().equals( BARCODE3 ) );
   }


   /**
    * Test for empty search results. Send invalid search parameters to receive empty results.
    *
    */
   @Test
   public void search_success_emptySearchResults() {
      InventorySearchParameters lInventorySearchParameters = new InventorySearchParameters();
      lInventorySearchParameters.setHighestInvId( INVALID_HIGHEST_INVENTORY_ID );
      lInventorySearchParameters.setConfigSlotId( INVALID_CONFIG_SLOT_ID );
      lInventorySearchParameters.setSerialNoOem( INVALID_SERIAL_NO );
      lInventorySearchParameters.setPartNoOem( INVALID_PART_NO );
      List<Inventory> inventoryList = iInventoryResourceBean.search( lInventorySearchParameters );
      assertTrue( "The retrieved inventory list was not empty.",
            CollectionUtils.isEmpty( inventoryList ) );
   }


   private Inventory getInventory() {
      Inventory lInventory = new Inventory();

      lInventory.setPartNumber( PART_NUMBER );
      lInventory.setSerialNumber( SERIAL_NUMBER );
      lInventory.setManufacturerCode( MANUFACT_CODE );
      lInventory.setAssemblyId( ASSEMBLY_ID );
      lInventory.setConfigSlotId( CONFIG_SLOT_ID );
      lInventory.setPartGroupId( PART_GROUP_ID );
      lInventory.setPositionName( POSITION_NAME );
      lInventory.setConditionCode( RFI );
      lInventory.setOwnerId( OWNER_ID );
      lInventory.setLocationId( LOCATION_ID );
      lInventory.setComplete( true );
      lInventory.setReceivedCondition( RECEIVED_CONDITION_NEW );

      return lInventory;
   }


   private Inventory getChildInventory( Inventory aParentInventory ) {
      Inventory lInventory = new Inventory();

      lInventory.setPartNumber( CHILD_PART_NUMBER );
      lInventory.setSerialNumber( CHILD_SERIAL_NUMBER );
      lInventory.setManufacturerCode( MANUFACT_CODE );
      lInventory.setAssemblyId( ASSEMBLY_ID );
      lInventory.setConfigSlotId( CHILD_CONFIG_SLOT_ID );
      lInventory.setPartGroupId( CHILD_PART_GROUP_ID );
      lInventory.setPositionName( CHILD_POSITION_NAME );
      lInventory.setConditionCode( RFI );
      lInventory.setOwnerId( OWNER_ID );
      lInventory.setLocationId( LOCATION_ID );
      lInventory.setComplete( true );
      lInventory.setReceivedCondition( null );
      lInventory.setInventoryClass( TRK );
      lInventory.setParentId( aParentInventory.getId() );
      lInventory.setHighestInventoryId( aParentInventory.getId() );

      return lInventory;

   }


   private Inventory getSingleTrkInventory() {
      Inventory lInventory = new Inventory();

      lInventory.setPartNumber( SINGLE_TRK_PART_NUMBER );
      lInventory.setSerialNumber( SINGLE_TRK_SERIAL_NUMBER );
      lInventory.setManufacturerCode( MANUFACT_CODE );
      lInventory.setAssemblyId( ASSEMBLY_ID );
      lInventory.setConfigSlotId( SINGLE_TRK_CONFIG_SLOT_ID );
      lInventory.setPartGroupId( SINGLE_TRK_PART_GROUP_ID );
      lInventory.setPositionName( SINGLE_TRK_POSITION_NAME );
      lInventory.setConditionCode( INSPREQ );
      lInventory.setOwnerId( OWNER_ID );
      lInventory.setLocationId( LOCATION_ID );
      lInventory.setComplete( true );

      return lInventory;
   }


   private void assertInventory( Inventory expectedInventory, Inventory actualInventory ) {

      assertEquals( "Incorrect part number found in retrieved inventory.",
            expectedInventory.getPartNumber(), actualInventory.getPartNumber() );
      assertEquals( "Incorrect serial number found in retrieved inventory.",
            expectedInventory.getSerialNumber(), actualInventory.getSerialNumber() );
      assertEquals( "Incorrect manufacturer code found in retrieved inventory.",
            expectedInventory.getManufacturerCode(), actualInventory.getManufacturerCode() );
      assertEquals( "Incorrect assembly ID found in retrieved inventory.",
            expectedInventory.getAssemblyId(), actualInventory.getAssemblyId() );
      assertEquals( "Incorrect config slot ID found in retrieved inventory.",
            expectedInventory.getConfigSlotId(), actualInventory.getConfigSlotId() );
      assertEquals( "Incorrect part group ID found in retrieved inventory.",
            expectedInventory.getPartGroupId(), actualInventory.getPartGroupId() );
      assertEquals( "Incorrect position name found in retrieved inventory.",
            expectedInventory.getPositionName(), actualInventory.getPositionName() );
      assertEquals( "Incorrect owner ID found in retrieved inventory.",
            expectedInventory.getOwnerId(), actualInventory.getOwnerId() );
      assertEquals( "Incorrect location ID found in retrieved inventory.",
            expectedInventory.getLocationId(), actualInventory.getLocationId() );
      assertEquals( "Incorrect inventory class found in retrieved inventory.",
            expectedInventory.getInventoryClass(), actualInventory.getInventoryClass() );
      assertEquals( "Incorrect received condition found in retrieved inventory.",
            expectedInventory.getReceivedCondition(), actualInventory.getReceivedCondition() );

      // We do these assertions if they are child inventories.
      if ( expectedInventory.getParentId() != null ) {
         assertEquals( "Incorrect parent ID found in retrieved inventory.",
               expectedInventory.getParentId(), actualInventory.getParentId() );
         assertEquals( "Incorrect highest inventory ID found in retrieved inventory.",
               expectedInventory.getHighestInventoryId(), actualInventory.getHighestInventoryId() );
      }
   }

}

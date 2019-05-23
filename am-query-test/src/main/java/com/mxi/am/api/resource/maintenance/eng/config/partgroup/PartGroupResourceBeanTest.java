package com.mxi.am.api.resource.maintenance.eng.config.partgroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBContext;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
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
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.eng.config.partgroup.impl.PartGroupResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for PartGroup API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class PartGroupResourceBeanTest extends ResourceBeanTest {

   private static final String PART_GROUP_ALT_ID_1 = "6EE698143CF241749419723E25BE0700";
   private static final String PART_GROUP_CODE_1 = "57104010160B";
   private static final String INV_CLASS_CODE_1 = "BATCH";
   private static final String PART_GROUP_NAME_1 = "FILTER";
   private static final double PART_QT_1 = 1;
   private static final String ASSEMBLY_BOM_ALT_ID_1 = "C7681306E0A042B4A372406FC3F1C02E";
   private static final String PART_NO_ALT_ID_1 = "4FC45B619E364967BD978797B6B2B2C7";
   private static final String PART_NO_OEM_1 = "AN924-6D";
   private static final String MANUFACT_CODE_1 = "88044";
   private static final String PART_STATUS_CODE_1 = "ACTV";
   private static final String PART_BASELINE_CODE_1 = "1234512345";
   private static final boolean STANDARD_BOOL_1 = true;
   private static final boolean APPROVED_BOOL_1 = true;
   private static final String PURCHASE_TYPE_CODE = "CMNHW";

   private static final String PART_GROUP_ALT_ID_2 = "6EE698143CF241749419723E25BE0701";
   private static final String PART_GROUP_CODE_2 = "57104010160C";
   private static final String INV_CLASS_CODE_2 = "TRK";
   private static final String PART_GROUP_NAME_2 = "WASHER";
   private static final double PART_QT_2 = 7;
   private static final String ASSEMBLY_BOM_ALT_ID_2 = "C7681306E0A042B4A372406FC3F1C02F";
   private static final String PART_NO_ALT_ID_2 = "4FC45B619E364967BD978797B6B2B2C8";
   private static final String PART_NO_OEM_2 = "AN924-6E";
   private static final String MANUFACT_CODE_2 = "88045";
   private static final String PART_STATUS_CODE_2 = "ACTV";
   private static final String PART_BASELINE_CODE_2 = "1234512345";
   private static final boolean STANDARD_BOOL_2 = true;
   private static final boolean APPROVED_BOOL_2 = true;

   private static final String PART_GROUP_CODE_TO_UPDATE = "57104010160D";
   private static final String PART_GROUP_NAME_TO_UPDATE = "SCREW";
   private static final double PART_QT_TO_UPDATE = 19;
   private static final String PART_NO_OEM_TO_UPDATE = "AN924-6D";
   private static final String MANUFACT_CODE__TO_UPDATE = "88044";
   private static final String PART_STATUS_CODE_TO_UPDATE = "ACTV";
   private static final String PART_BASELINE_CODE_TO_UPDATE = "1234512345";
   private static final boolean STANDARD_BOOL_TO_UPDATE = true;
   private static final boolean APPROVED_BOOL_TO_UPDATE = false;

   private static final String INVALID_PART_NO_ALT_ID = "4FC45B619E364967BD978797B6B2B2D9";
   private static final String INVALID_PART_GROUP_CODE = "XXX";

   @Inject
   PartGroupResourceBean partGroupResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext EJBContext;

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      partGroupResourceBean.setEJBContext( EJBContext );
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( EJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testGetPartGroupByIdSuccessWithoutParts() throws AmApiResourceNotFoundException {
      PartGroupSearchParameters partGroupSearchParameters = new PartGroupSearchParameters();
      partGroupSearchParameters.setParts( false );
      PartGroup partGroup =
            partGroupResourceBean.get( PART_GROUP_ALT_ID_1, partGroupSearchParameters );
      List<Part> parts = new ArrayList<Part>();
      partGroup.setParts( parts );
      assertPartGroupObjectEquals( getPartGroupWithoutParts(), partGroup );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testGetPartGroupByIdSuccessWithParts() throws AmApiResourceNotFoundException {
      PartGroupSearchParameters partGroupSearchParameters = new PartGroupSearchParameters();
      partGroupSearchParameters.setParts( true );
      PartGroup partGroup =
            partGroupResourceBean.get( PART_GROUP_ALT_ID_1, partGroupSearchParameters );
      assertPartGroupObjectEquals( getPartGroupWithParts(), partGroup );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetPartGroupByIdResourceNotFound() throws AmApiResourceNotFoundException {
      partGroupResourceBean.get( INVALID_PART_NO_ALT_ID, new PartGroupSearchParameters() );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetPartGroupByNullId() throws AmApiResourceNotFoundException {
      partGroupResourceBean.get( null, new PartGroupSearchParameters() );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testSearchPartGroupsByListOfIds() {
      PartGroupSearchParameters partGroupSearchParameters = new PartGroupSearchParameters();
      partGroupSearchParameters.setParts( true );
      List<String> partGroupIds = new ArrayList<>();
      partGroupIds.add( PART_GROUP_ALT_ID_1 );
      partGroupIds.add( PART_GROUP_ALT_ID_2 );
      partGroupSearchParameters.setIds( partGroupIds );
      List<PartGroup> partGroups = partGroupResourceBean.search( partGroupSearchParameters );
      Assert.assertEquals( "Incorrect number of part groups returned: ", 2, partGroups.size() );
      Assert.assertTrue(
            "Part group list [" + partGroups.toString() + "] should contain part group ["
                  + getPartGroupWithParts().toString() + "].",
            ( partGroups.toString() ).contains( ( getPartGroupWithParts().toString() ) ) );
      Assert.assertTrue(
            "Part group list [" + partGroups.toString() + "] should contain part group ["
                  + getPartGroupWithParts2().toString() + "].",
            ( partGroups.toString() ).contains( ( getPartGroupWithParts2().toString() ) ) );
   }


   @Test
   public void testSearchByPartGroupCode() {
      PartGroupSearchParameters partGroupSearchParameters = new PartGroupSearchParameters();
      partGroupSearchParameters.setParts( true );
      partGroupSearchParameters.setPartGroupCode( PART_GROUP_CODE_1 );
      List<PartGroup> partGroups = partGroupResourceBean.search( partGroupSearchParameters );
      Assert.assertEquals( 1, partGroups.size() );
      assertPartGroupObjectEquals( getPartGroupWithParts(), partGroups.get( 0 ) );
   }


   @Test
   public void testSearchPartGroupByInvalidPartGroupIds() {
      PartGroupSearchParameters partGroupSearchParameters = new PartGroupSearchParameters();
      partGroupSearchParameters.setParts( true );
      List<String> partGroupIds = new ArrayList<>();
      partGroupIds.add( INVALID_PART_NO_ALT_ID );
      partGroupSearchParameters.setIds( partGroupIds );
      List<PartGroup> partGroups = partGroupResourceBean.search( partGroupSearchParameters );
      assertTrue( CollectionUtils.isEmpty( partGroups ) );
   }


   @Test
   public void testSearchPartGroupByInvalidPartGroupCode() {
      PartGroupSearchParameters partGroupSearchParameters = new PartGroupSearchParameters();
      partGroupSearchParameters.setParts( true );
      partGroupSearchParameters.setPartGroupCode( INVALID_PART_GROUP_CODE );
      List<PartGroup> partGroups = partGroupResourceBean.search( partGroupSearchParameters );
      assertTrue( CollectionUtils.isEmpty( partGroups ) );
   }


   @Test
   public void testUpdatePartGroupSuccess()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      PartGroup partGroupToUpdate = getPartGroupToUpdate();
      PartGroup partGroup = partGroupResourceBean.update( PART_GROUP_ALT_ID_1, partGroupToUpdate );
      assertPartGroupObjectEquals( getPartGroupAfterUpdate(), partGroup );
   }


   @Test
   public void testUpdatePartGroupNullIdBadRequest()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      try {
         PartGroup partGroupToUpdate = getPartGroupToUpdate();
         PartGroup partGroup = partGroupResourceBean.update( null, partGroupToUpdate );
         Assert.fail( "No bad request exception thrown for null part group id" );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Missing part group Id", exception.getMessage() );
      }
   }


   @Test
   public void testUpdatePartGroupNullPayloadBadRequest()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      try {
         PartGroup partGroup = partGroupResourceBean.update( PART_GROUP_ALT_ID_1, null );
         Assert.fail( "No bad request exception thrown for null payload" );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Missing part group payload", exception.getMessage() );
      }
   }


   private PartGroup getPartGroupToUpdate() {
      PartGroup partGroupToUpdate = new PartGroup();
      partGroupToUpdate.setPartGroupName( PART_GROUP_NAME_TO_UPDATE );
      partGroupToUpdate.setPartGroupCode( PART_GROUP_CODE_TO_UPDATE );
      partGroupToUpdate.setPartQuantity( PART_QT_TO_UPDATE );
      // part details
      List<Part> parts = new ArrayList<>();
      Part part = new Part();
      part.setId( PART_NO_ALT_ID_1 );
      part.setOemPartNumber( PART_NO_OEM_TO_UPDATE );
      part.setManufacturerCode( MANUFACT_CODE__TO_UPDATE );
      part.setApproved( APPROVED_BOOL_TO_UPDATE );
      part.setPartBaselineCode( PART_BASELINE_CODE_TO_UPDATE );
      part.setPartStatus( PART_STATUS_CODE_TO_UPDATE );
      part.setStandard( STANDARD_BOOL_TO_UPDATE );
      parts.add( part );
      partGroupToUpdate.setParts( parts );
      return partGroupToUpdate;
   }


   private PartGroup getPartGroupAfterUpdate() {
      PartGroup partGroup = new PartGroup();
      partGroup.setId( PART_GROUP_ALT_ID_1 );
      partGroup.setPartGroupCode( PART_GROUP_CODE_TO_UPDATE );
      partGroup.setPartGroupName( PART_GROUP_NAME_TO_UPDATE );
      partGroup.setPartGroupInventoryClass( INV_CLASS_CODE_1 );
      partGroup.setPartQuantity( PART_QT_TO_UPDATE );
      partGroup.setConfigurationSlotId( ASSEMBLY_BOM_ALT_ID_1 );
      partGroup.setParts( getPartDetailsAfterUpdate() );
      return partGroup;
   }


   private List<Part> getPartDetailsAfterUpdate() {
      List<Part> parts = new ArrayList<Part>();
      Part part = new Part();
      part.setId( PART_NO_ALT_ID_1 );
      part.setOemPartNumber( PART_NO_OEM_TO_UPDATE );
      part.setManufacturerCode( MANUFACT_CODE__TO_UPDATE );
      part.setPartStatus( PART_STATUS_CODE_TO_UPDATE );
      part.setPartBaselineCode( PART_BASELINE_CODE_TO_UPDATE );
      part.setStandard( STANDARD_BOOL_TO_UPDATE );
      part.setApproved( APPROVED_BOOL_TO_UPDATE );
      parts.add( part );
      return parts;
   }


   private PartGroup getPartGroupWithoutParts() {
      PartGroup partGroup = new PartGroup();
      partGroup.setId( PART_GROUP_ALT_ID_1 );
      partGroup.setPartGroupCode( PART_GROUP_CODE_1 );
      partGroup.setPartGroupName( PART_GROUP_NAME_1 );
      partGroup.setPartGroupInventoryClass( INV_CLASS_CODE_1 );
      partGroup.setPartQuantity( PART_QT_1 );
      partGroup.setConfigurationSlotId( ASSEMBLY_BOM_ALT_ID_1 );
      partGroup.setPurchaseTypeCode( PURCHASE_TYPE_CODE );
      return partGroup;
   }


   private PartGroup getPartGroupWithParts() {
      PartGroup partGroup = new PartGroup();
      partGroup.setId( PART_GROUP_ALT_ID_1 );
      partGroup.setPartGroupCode( PART_GROUP_CODE_1 );
      partGroup.setPartGroupName( PART_GROUP_NAME_1 );
      partGroup.setPartGroupInventoryClass( INV_CLASS_CODE_1 );
      partGroup.setPartQuantity( PART_QT_1 );
      partGroup.setConfigurationSlotId( ASSEMBLY_BOM_ALT_ID_1 );
      partGroup.setPurchaseTypeCode( PURCHASE_TYPE_CODE );
      partGroup.setParts( getPartList() );
      return partGroup;
   }


   private PartGroup getPartGroupWithParts2() {
      PartGroup partGroup = new PartGroup();
      partGroup.setId( PART_GROUP_ALT_ID_2 );
      partGroup.setPartGroupCode( PART_GROUP_CODE_2 );
      partGroup.setPartGroupName( PART_GROUP_NAME_2 );
      partGroup.setPartGroupInventoryClass( INV_CLASS_CODE_2 );
      partGroup.setPartQuantity( PART_QT_2 );
      partGroup.setConfigurationSlotId( ASSEMBLY_BOM_ALT_ID_2 );
      partGroup.setPurchaseTypeCode( PURCHASE_TYPE_CODE );
      partGroup.setParts( getPartList2() );
      return partGroup;
   }


   private List<Part> getPartList() {
      List<Part> parts = new ArrayList<Part>();
      Part part = new Part();
      part.setId( PART_NO_ALT_ID_1 );
      part.setOemPartNumber( PART_NO_OEM_1 );
      part.setManufacturerCode( MANUFACT_CODE_1 );
      part.setPartStatus( PART_STATUS_CODE_1 );
      part.setPartBaselineCode( PART_BASELINE_CODE_1 );
      part.setStandard( STANDARD_BOOL_1 );
      part.setApproved( APPROVED_BOOL_1 );
      parts.add( part );
      return parts;
   }


   private List<Part> getPartList2() {
      List<Part> parts = new ArrayList<Part>();
      Part part = new Part();
      part.setId( PART_NO_ALT_ID_2 );
      part.setOemPartNumber( PART_NO_OEM_2 );
      part.setManufacturerCode( MANUFACT_CODE_2 );
      part.setPartStatus( PART_STATUS_CODE_2 );
      part.setPartBaselineCode( PART_BASELINE_CODE_2 );
      part.setStandard( STANDARD_BOOL_2 );
      part.setApproved( APPROVED_BOOL_2 );
      parts.add( part );
      return parts;
   }


   private void assertPartGroupObjectEquals( PartGroup expectedPartGroup,
         PartGroup actualPartGroup ) {
      assertEquals( "Incorrect ID: ", expectedPartGroup.getId(), actualPartGroup.getId() );
      assertEquals( "Incorrect Part Group Code: ", expectedPartGroup.getPartGroupCode(),
            actualPartGroup.getPartGroupCode() );
      assertEquals( "Incorrect Part Group Inventory Class: ",
            expectedPartGroup.getPartGroupInventoryClass(),
            actualPartGroup.getPartGroupInventoryClass() );
      assertEquals( "Incorrect Part Group Name: ", expectedPartGroup.getPartGroupName(),
            actualPartGroup.getPartGroupName() );
      assertEquals( "Incorrect Part Quantity: ", expectedPartGroup.getPartQuantity(),
            actualPartGroup.getPartQuantity() );
      assertEquals( "Incorrect Configuration Slot ID: ", expectedPartGroup.getConfigurationSlotId(),
            actualPartGroup.getConfigurationSlotId() );
      assertEquals( "Incorrect Purch Type Code: ", expectedPartGroup.getPurchaseTypeCode(),
            actualPartGroup.getPurchaseTypeCode() );
      assertEquals( "Incorrect Parts: ", expectedPartGroup.getParts(), actualPartGroup.getParts() );
   }
}

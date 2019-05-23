package com.mxi.am.api.resource.materials.plan.partrequest;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.plan.partrequest.impl.PartRequestResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtStageDao;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Integration testing for PartRequestResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class PartRequestResourceBeanTest extends ResourceBeanTest {

   @ClassRule
   public static InjectionOverrideRule fakeGuiceDaoRule;

   static {

      Module overrideModule = new AbstractModule() {

         @Override
         protected void configure() {
            bind( PartRequestResource.class ).to( PartRequestResourceBean.class );
            bind( EvtEventDao.class ).to( JdbcEvtEventDao.class );
            bind( EvtStageDao.class ).to( JdbcEvtStageDao.class );
            bind( Security.class ).to( CoreSecurity.class );
         }
      };

      fakeGuiceDaoRule = new InjectionOverrideRule( overrideModule );
   }

   @Inject
   PartRequestResource partRequestResourceBean;

   private static final String AIRCRAFT_ID = "00000000000000000000000000000002";
   private static final String REQUEST_PRIORITY_TO_CREATE = "AOG";
   private static final String PART_ID_TO_CREATE = "FBB6ADDF6E8567ECA5955B1CA243B9FB";
   private static final String NEEDED_QUANTITY_TO_CREATE = "1";
   private static final String WHERE_NEEDED_TO_CREATE = "GBLDOCK";
   private static final String ISSUE_TO_ACCOUNT_TO_CREATE = "EXPENSE";
   private static final String NEEDED_BY_DATE = "2007-01-21T05:00:00Z";

   private static final String NOTE_TO_UPDATE = "Updated part request";

   private static final String REQ_PART_ID1 = "3FA61B0D66FB455CB94D5FAEC334B8EB";
   private static final String REQ_PART_ID2 = "3FA61B0D66FB455CB94D5FAEC334B123";

   private static final String STATUS_CODE1 = "PROPEN";
   private static final String REQUEST_PRIORITY1 = "NORMAL";
   private static final String ISSUE_TO_ACCOUNT1 = null;
   private static final String WHERE_NEEDED1 = "GBLDOCK";
   private static final String PART_ID1 = null;
   private static final String NEEDED_QUANTITY1 = "1";
   private static final String PART_REQUIREMENT_NOTES1 = "part requirement note";
   private static final String PART_GROUP_INVENTORY_CLASS1 = "TRK";
   private static final String USER_STATUS_CODE1 = "OPEN";
   private static final String PART_REQUIREMENT_ID1 = "C1BD9286BB8C11E599EF5D0D0F798A44";
   private static final String TASK_BARCODE1 = "T00011TV";
   private static final String TASK_ID1 = "29F183AB091B44D491A9B290D4E3638E";
   private static final String BARCODE1 = "R00024BP";
   private static final String POSITION_NAME1 = "1.1";
   private static final String NOTE1 =
         "This part request was created when ReqSpecPart And IssueAccount Details are null.";
   private static final String CONFIG_SLOT_ID1 = "12D4ED5635D64B3F977ADC440AD7EEF3";
   private static final String PART_GROUP_ID1 = "6A9D8D3A236F4E678B037DC20102ACAC";
   private static final String REQUEST_TYPE1 = "TASK";
   private static final String POSITION_ID1 = "B58434D07FE045E1AD5DB59F1FBF5460";
   private static final String MASTER_ID1 = "R00024BP";
   private static final String EXTERNAL_REFERENCE1 = "R00025AP-1";
   private static final String PART_PROVIDER_TYPE_CODE1 = "TESTPROV";

   private static final String STATUS_CODE2 = "PROPEN";
   private static final String REQUEST_PRIORITY2 = "NORMAL";
   private static final String ISSUE_TO_ACCOUNT2 = "EXPENSE";
   private static final String WHERE_NEEDED2 = "GBLDOCK";
   private static final String PART_ID2 = "FBB6ADDF6E8567ECA5955B1CA243B9FB";
   private static final String NEEDED_QUANTITY2 = "1";
   private static final String PART_REQUIREMENT_NOTES2 = "part requirement note";
   private static final String PART_GROUP_INVENTORY_CLASS2 = "TRK";
   private static final String USER_STATUS_CODE2 = "OPEN";
   private static final String PART_REQUIREMENT_ID2 = "C1BD9286BB8C11E599EF5D0D0F798A44";
   private static final String TASK_BARCODE2 = "T00011TV";
   private static final String TASK_ID2 = "29F183AB091B44D491A9B290D4E3638E";
   private static final String BARCODE2 = "R00025BP";
   private static final String POSITION_NAME2 = "1.1";
   private static final String NOTE2 =
         "This part request was created when ReqSpecPart And IssueAccount Details are not null.";
   private static final String CONFIG_SLOT_ID2 = "12D4ED5635D64B3F977ADC440AD7EEF3";
   private static final String PART_GROUP_ID2 = "6A9D8D3A236F4E678B037DC20102ACAC";
   private static final String REQUEST_TYPE2 = "TASK";
   private static final String POSITION_ID2 = "B58434D07FE045E1AD5DB59F1FBF5460";
   private static final String MASTER_ID2 = "R00025BP";
   private static final String INVALID_REQUEST_PRIORITY_TO_CREATE = "ABCDE";
   private static final String INVALID_PART_ID_TO_CREATE = "3FA61B0D66FB455CB94D5FAEC334B155";
   private static final String TOO_LONG_REQ_PART_ID_TO_GET = "3FA61B0D66FB455CB94D5FAEC334B12333";
   private static final String INVALID_REQ_PART_ID_TO_GET = "3FA61B0D66FB455CB94D5FAEC334B155";
   private static final String INVALID_REQ_PART_ID_TO_UPDATE = "3FA61B0D66FB455CB94D5FAEC334B155";


   @Before
   public void setUp() throws MxException {

      InjectorContainer.get().injectMembers( this );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();
   }


   /**
    *
    * Test Post method for happy path
    *
    * @throws ParseException
    * @throws AmApiBusinessException
    *
    */
   @Test
   public void testCreatePartRequestSuccess() throws ParseException, AmApiBusinessException {

      List<PartRequest> partRequests =
            partRequestResourceBean.create( constructPartRequestToCreate() );

      assertObjectsEquals( getCreatedPartRequest(), partRequests.get( 0 ) );
   }


   /**
    *
    * Test Post method for precondition failed scenarios
    *
    * @throws ParseException
    * @throws AmApiBusinessException
    *
    */

   @Test
   public void testCreatePartRequestWithInvalidRequestPriority()
         throws ParseException, AmApiBusinessException {

      PartRequest partRequest = constructPartRequestToCreate();
      partRequest.setRequestPriority( INVALID_REQUEST_PRIORITY_TO_CREATE );

      try {
         partRequestResourceBean.create( partRequest );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals(
               "The Request Priority, " + partRequest.getRequestPriority() + ", is invalid.",
               e.getMessage() );
      }
   }


   @Test
   public void testCreatePartRequestWithInvalidPartId()
         throws ParseException, AmApiBusinessException {

      PartRequest partRequest = constructPartRequestToCreate();
      partRequest.setPartId( INVALID_PART_ID_TO_CREATE );

      try {
         partRequestResourceBean.create( partRequest );
      } catch ( AmApiBadRequestException e ) {
         Assert.assertEquals( "Part Id could not be found : " + partRequest.getPartId(),
               e.getMessage() );
      }
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreatePartRequestWithNullRequestPriority()
         throws ParseException, AmApiBusinessException {

      PartRequest partRequest = constructPartRequestToCreate();
      partRequest.setRequestPriority( null );

      partRequestResourceBean.create( partRequest );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreatePartRequestWithNullPartId() throws ParseException, AmApiBusinessException {

      PartRequest partRequest = constructPartRequestToCreate();
      partRequest.setPartId( null );

      partRequestResourceBean.create( partRequest );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreatePartRequestWithNullNeededQuantity()
         throws ParseException, AmApiBusinessException {

      PartRequest partRequest = constructPartRequestToCreate();
      partRequest.setNeededQty( null );

      partRequestResourceBean.create( partRequest );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreatePartRequestWithNullWhereNeeded()
         throws ParseException, AmApiBusinessException {

      PartRequest partRequest = constructPartRequestToCreate();
      partRequest.setWhereNeeded( null );

      partRequestResourceBean.create( partRequest );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreatePartRequestWithNullIssueToAccount()
         throws ParseException, AmApiBusinessException {

      PartRequest partRequest = constructPartRequestToCreate();
      partRequest.setIssueToAccount( null );

      partRequestResourceBean.create( partRequest );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreatePartRequestWithNullNeededByDate()
         throws ParseException, AmApiBusinessException {

      PartRequest partRequest = constructPartRequestToCreate();
      partRequest.setNeededByDate( null );

      partRequestResourceBean.create( partRequest );
   }


   /**
    *
    * Test Get method for happy path - WithoutReqSpecPartAndIssueAccountDetails
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testGetPartRequestSuccessWithoutReqSpecPartAndIssueAccountDetails()
         throws AmApiResourceNotFoundException {

      PartRequest partRequest = partRequestResourceBean.get( REQ_PART_ID1 );
      assertPartRequestObjectsEquals(
            constructPartRequestToGetWithoutReqSpecPartAndIssueAccountDetails(), partRequest );
   }


   /**
    *
    * Test Get method for happy path - WithReqSpecPartAndIssueAccountDetails
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testGetPartRequestSuccessWithReqSpecPartAndIssueAccountDetails()
         throws AmApiResourceNotFoundException {

      PartRequest partRequest = partRequestResourceBean.get( REQ_PART_ID2 );
      assertPartRequestObjectsEquals(
            constructPartRequestToGetWithReqSpecPartAndIssueAccountDetails(), partRequest );
   }


   /**
    *
    * Test get method for failed scenarios
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetPartRequestWithInvalidPartId() throws AmApiResourceNotFoundException {

      partRequestResourceBean.get( INVALID_REQ_PART_ID_TO_GET );
   }


   /**
    *
    * Test get method for failed scenarios
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetPartRequestWithTooLongPartId() throws AmApiResourceNotFoundException {

      partRequestResourceBean.get( TOO_LONG_REQ_PART_ID_TO_GET );
   }


   /**
    *
    * Test Put method for happy path
    *
    * @throws AmApiResourceNotFoundException
    *
    */

   @Test
   @CSIContractTest( Project.UPS )
   public void testUpdatePartRequestSuccess() throws AmApiResourceNotFoundException {

      PartRequest partRequest = partRequestResourceBean.get( REQ_PART_ID1 );
      partRequest.setNote( NOTE_TO_UPDATE );
      partRequest.setPartProviderTypeCode( null );

      PartRequest newPartRequest = partRequestResourceBean.update( REQ_PART_ID1, partRequest );

      assertPartRequestObjectsEquals( partRequest, newPartRequest );
   }


   /**
    *
    * Test Put method for failed scenarios
    *
    * @throws AmApiResourceNotFoundException
    *
    */

   @Test( expected = AmApiResourceNotFoundException.class )
   public void testUpdatePartRequestWithInvalidPartId() throws AmApiResourceNotFoundException {

      PartRequest partRequest = partRequestResourceBean.get( REQ_PART_ID1 );

      partRequestResourceBean.update( INVALID_REQ_PART_ID_TO_UPDATE, partRequest );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testSearchPartRequestByBarcode() {

      PartRequestSearchParameters partRequestSearchParameters = new PartRequestSearchParameters();
      partRequestSearchParameters.setBarcode( BARCODE1 );

      List<PartRequest> partRequests =
            partRequestResourceBean.search( partRequestSearchParameters );
      validatePartRequest( partRequests.get( 0 ) );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testSearchPartRequestByTaskId() {

      PartRequestSearchParameters partRequestSearchParameters = new PartRequestSearchParameters();
      partRequestSearchParameters.setTaskId( TASK_ID1 );

      List<PartRequest> partRequests =
            partRequestResourceBean.search( partRequestSearchParameters );
      validatePartRequest( partRequests.get( 0 ) );
   }


   @Test
   public void testSearchPartRequestByExternalReference() {

      PartRequestSearchParameters partRequestSearchParameters = new PartRequestSearchParameters();
      partRequestSearchParameters.setExternalReference( EXTERNAL_REFERENCE1 );

      List<PartRequest> partRequests =
            partRequestResourceBean.search( partRequestSearchParameters );
      validatePartRequest( partRequests.get( 0 ) );
   }


   @Test
   public void testSearchPartRequestByMasterId() {

      PartRequestSearchParameters partRequestSearchParameters = new PartRequestSearchParameters();
      partRequestSearchParameters.setMasterId( MASTER_ID1 );

      List<PartRequest> partRequests =
            partRequestResourceBean.search( partRequestSearchParameters );
      validatePartRequest( partRequests.get( 0 ) );
   }


   @Test
   public void testSearchPartRequestWithoutAllParameters() {

      PartRequestSearchParameters partRequestSearchParameters = new PartRequestSearchParameters();

      List<PartRequest> partRequests =
            partRequestResourceBean.search( partRequestSearchParameters );
      Assert.assertTrue( "No Part Requests returned", partRequests.size() > 0 );
   }


   @Test
   public void testSearchPartRequestWithAllParameters() {

      PartRequestSearchParameters partRequestSearchParameters = new PartRequestSearchParameters();
      partRequestSearchParameters.setBarcode( BARCODE1 );
      partRequestSearchParameters.setTaskId( TASK_ID1 );
      partRequestSearchParameters.setExternalReference( EXTERNAL_REFERENCE1 );
      partRequestSearchParameters.setMasterId( MASTER_ID1 );

      List<PartRequest> partRequests =
            partRequestResourceBean.search( partRequestSearchParameters );
      validatePartRequest( partRequests.get( 0 ) );
   }


   private void assertPartRequestObjectsEquals( PartRequest expectedPartRequest,
         PartRequest actualPartRequest ) {
      assertEquals( "Incorrect Status Code: ", expectedPartRequest.getStatusCode(),
            actualPartRequest.getStatusCode() );
      assertEquals( "Incorrect Request Priority: ", expectedPartRequest.getRequestPriority(),
            actualPartRequest.getRequestPriority() );
      assertEquals( "Incorrect Issue to Account: ", expectedPartRequest.getIssueToAccount(),
            actualPartRequest.getIssueToAccount() );
      assertEquals( "Incorrect Where Needed: ", expectedPartRequest.getWhereNeeded(),
            actualPartRequest.getWhereNeeded() );
      assertEquals( "Incorrect Part ID: ", expectedPartRequest.getPartId(),
            actualPartRequest.getPartId() );
      assertEquals( "Incorrect Needed Quantity: ", expectedPartRequest.getNeededQty(),
            actualPartRequest.getNeededQty() );
      assertEquals( "Incorrect Part Requirement Notes: ",
            expectedPartRequest.getPartRequirementNotes(),
            actualPartRequest.getPartRequirementNotes() );
      assertEquals( "Incorrect Part Group inventory Class: ",
            expectedPartRequest.getPartGroupInventoryClass(),
            actualPartRequest.getPartGroupInventoryClass() );
      assertEquals( "Incorrect User Status Code: ", expectedPartRequest.getUserStatusCode(),
            actualPartRequest.getUserStatusCode() );
      assertEquals( "Incorrect Part Requirement ID: ", expectedPartRequest.getPartRequirementId(),
            actualPartRequest.getPartRequirementId() );
      assertEquals( "Incorrect Task Barcode: ", expectedPartRequest.getTaskBarcode(),
            actualPartRequest.getTaskBarcode() );
      assertEquals( "Incorrect Task ID: ", expectedPartRequest.getTaskId(),
            actualPartRequest.getTaskId() );
      assertEquals( "Incorrect Barcode: ", expectedPartRequest.getBarcode(),
            actualPartRequest.getBarcode() );
      assertEquals( "Incorrect Position Name: ", expectedPartRequest.getPositionName(),
            actualPartRequest.getPositionName() );
      assertEquals( "Incorrect Note: ", expectedPartRequest.getNote(),
            actualPartRequest.getNote() );
      assertEquals( "Incorrect Config Slot ID: ", expectedPartRequest.getConfigSlotId(),
            actualPartRequest.getConfigSlotId() );
      assertEquals( "Incorrect Part Group ID: ", expectedPartRequest.getPartGroupId(),
            actualPartRequest.getPartGroupId() );
      assertEquals( "Incorrect Request Type: ", expectedPartRequest.getRequestType(),
            actualPartRequest.getRequestType() );
      assertEquals( "Incorrect Position ID: ", expectedPartRequest.getPositionId(),
            actualPartRequest.getPositionId() );
      assertEquals( "Incorrect Master ID: ", expectedPartRequest.getMasterId(),
            actualPartRequest.getMasterId() );
      assertEquals( "Incorrect Part Provider Type Code: ",
            expectedPartRequest.getPartProviderTypeCode(),
            actualPartRequest.getPartProviderTypeCode() );
      assertEquals( "Incorrect Aircraft ID: ", expectedPartRequest.getAircraftId(),
            actualPartRequest.getAircraftId() );
   }


   private PartRequest constructPartRequestToGetWithReqSpecPartAndIssueAccountDetails() {

      PartRequest partRequestToGet = new PartRequest();

      partRequestToGet.setStatusCode( STATUS_CODE2 );
      partRequestToGet.setRequestPriority( REQUEST_PRIORITY2 );
      partRequestToGet.setIssueToAccount( ISSUE_TO_ACCOUNT2 );
      partRequestToGet.setWhereNeeded( WHERE_NEEDED2 );
      partRequestToGet.setPartId( PART_ID2 );
      partRequestToGet.setNeededQty( NEEDED_QUANTITY2 );
      partRequestToGet.setPartRequirementNotes( PART_REQUIREMENT_NOTES2 );
      partRequestToGet.setPartGroupInventoryClass( PART_GROUP_INVENTORY_CLASS2 );
      partRequestToGet.setUserStatusCode( USER_STATUS_CODE2 );
      partRequestToGet.setPartRequirementId( PART_REQUIREMENT_ID2 );
      partRequestToGet.setTaskBarcode( TASK_BARCODE2 );
      partRequestToGet.setTaskId( TASK_ID2 );
      partRequestToGet.setBarcode( BARCODE2 );
      partRequestToGet.setPositionName( POSITION_NAME2 );
      partRequestToGet.setNote( NOTE2 );
      partRequestToGet.setConfigSlotId( CONFIG_SLOT_ID2 );
      partRequestToGet.setPartGroupId( PART_GROUP_ID2 );
      partRequestToGet.setRequestType( REQUEST_TYPE2 );
      partRequestToGet.setPositionId( POSITION_ID2 );
      partRequestToGet.setMasterId( MASTER_ID2 );
      partRequestToGet.setAircraftId( AIRCRAFT_ID );

      return partRequestToGet;
   }


   private PartRequest constructPartRequestToGetWithoutReqSpecPartAndIssueAccountDetails() {

      PartRequest partRequestToGet = new PartRequest();

      partRequestToGet.setStatusCode( STATUS_CODE1 );
      partRequestToGet.setRequestPriority( REQUEST_PRIORITY1 );
      partRequestToGet.setIssueToAccount( ISSUE_TO_ACCOUNT1 );
      partRequestToGet.setWhereNeeded( WHERE_NEEDED1 );
      partRequestToGet.setPartId( PART_ID1 );
      partRequestToGet.setNeededQty( NEEDED_QUANTITY1 );
      partRequestToGet.setPartRequirementNotes( PART_REQUIREMENT_NOTES1 );
      partRequestToGet.setPartGroupInventoryClass( PART_GROUP_INVENTORY_CLASS1 );
      partRequestToGet.setUserStatusCode( USER_STATUS_CODE1 );
      partRequestToGet.setPartRequirementId( PART_REQUIREMENT_ID1 );
      partRequestToGet.setTaskBarcode( TASK_BARCODE1 );
      partRequestToGet.setTaskId( TASK_ID1 );
      partRequestToGet.setBarcode( BARCODE1 );
      partRequestToGet.setPositionName( POSITION_NAME1 );
      partRequestToGet.setNote( NOTE1 );
      partRequestToGet.setConfigSlotId( CONFIG_SLOT_ID1 );
      partRequestToGet.setPartGroupId( PART_GROUP_ID1 );
      partRequestToGet.setRequestType( REQUEST_TYPE1 );
      partRequestToGet.setPositionId( POSITION_ID1 );
      partRequestToGet.setMasterId( MASTER_ID1 );
      partRequestToGet.setPartProviderTypeCode( PART_PROVIDER_TYPE_CODE1 );
      partRequestToGet.setAircraftId( AIRCRAFT_ID );

      return partRequestToGet;
   }


   private PartRequest constructPartRequestToCreate() throws ParseException {

      PartRequest partRequestToCreate = new PartRequest();

      partRequestToCreate.setRequestPriority( REQUEST_PRIORITY_TO_CREATE );
      partRequestToCreate.setPartId( PART_ID_TO_CREATE );
      partRequestToCreate.setNeededQty( NEEDED_QUANTITY_TO_CREATE );
      partRequestToCreate.setWhereNeeded( WHERE_NEEDED_TO_CREATE );
      partRequestToCreate.setIssueToAccount( ISSUE_TO_ACCOUNT_TO_CREATE );

      Date neededByDate;
      try {
         neededByDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( NEEDED_BY_DATE );
      } catch ( ParseException e ) {
         throw ( e );
      }
      partRequestToCreate.setNeededByDate( neededByDate );

      return partRequestToCreate;

   }


   private void assertObjectsEquals( PartRequest expectedPartRequest,
         PartRequest actualPartRequest ) {
      assertEquals( "Incorrect Request Priority: ", expectedPartRequest.getRequestPriority(),
            actualPartRequest.getRequestPriority() );
      assertEquals( "Incorrect Part ID: ", expectedPartRequest.getPartId(),
            actualPartRequest.getPartId() );
      assertEquals( "Incorrect Needed Quantity: ", expectedPartRequest.getNeededQty(),
            actualPartRequest.getNeededQty() );
      assertEquals( "Incorrect value for Where Needed: ", expectedPartRequest.getWhereNeeded(),
            actualPartRequest.getWhereNeeded() );
      assertEquals( "Incorrect Issue to Account: ", expectedPartRequest.getIssueToAccount(),
            actualPartRequest.getIssueToAccount() );
      assertEquals( "Incorrect Needed By Date: ", expectedPartRequest.getNeededByDate(),
            actualPartRequest.getNeededByDate() );
   }


   private PartRequest getCreatedPartRequest() throws ParseException {

      PartRequest partRequest = new PartRequest();
      partRequest.setRequestPriority( REQUEST_PRIORITY_TO_CREATE );
      partRequest.setPartId( PART_ID_TO_CREATE );
      partRequest.setNeededQty( NEEDED_QUANTITY_TO_CREATE );
      partRequest.setWhereNeeded( WHERE_NEEDED_TO_CREATE );
      partRequest.setIssueToAccount( ISSUE_TO_ACCOUNT_TO_CREATE );

      Date neededByDate;
      try {
         neededByDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( NEEDED_BY_DATE );
      } catch ( ParseException e ) {
         throw ( e );
      }
      partRequest.setNeededByDate( neededByDate );

      return partRequest;
   }


   private void validatePartRequest( PartRequest partRequest ) {
      Assert.assertEquals( "Incorrect Part Request ID: ", REQ_PART_ID1, partRequest.getId() );
      Assert.assertEquals( "Incorrect Barcode: ", BARCODE1, partRequest.getBarcode() );
      Assert.assertEquals( "Incorrect Master ID: ", MASTER_ID1, partRequest.getMasterId() );
      Assert.assertEquals( "Incorrect Externel Reference: ", EXTERNAL_REFERENCE1,
            partRequest.getExternalReference() );
      Assert.assertEquals( "Incorrect Part Requirement Notes: ", PART_REQUIREMENT_NOTES1,
            partRequest.getPartRequirementNotes() );
      Assert.assertEquals( "Incorrect Task Barcode: ", TASK_BARCODE1,
            partRequest.getTaskBarcode() );
      Assert.assertEquals( "Incorrect Task ID: ", TASK_ID1, partRequest.getTaskId() );
      Assert.assertEquals( "Incorrect Part Group ID: ", PART_GROUP_ID1,
            partRequest.getPartGroupId() );
      Assert.assertEquals( "Incorrect Part Group Inventory Class: ", PART_GROUP_INVENTORY_CLASS1,
            partRequest.getPartGroupInventoryClass() );
      Assert.assertEquals( "Incorrect Config Slot ID: ", CONFIG_SLOT_ID1,
            partRequest.getConfigSlotId() );
      Assert.assertEquals( "Incorrect Request Type: ", REQUEST_TYPE1,
            partRequest.getRequestType() );
      Assert.assertEquals( "Incorrect Status Code: ", STATUS_CODE1, partRequest.getStatusCode() );
      Assert.assertEquals( "Incorrect Request Priority: ", REQUEST_PRIORITY1,
            partRequest.getRequestPriority() );
      Assert.assertEquals( "Incorrect Issue to Account: ", ISSUE_TO_ACCOUNT1,
            partRequest.getIssueToAccount() );
      Assert.assertEquals( "Incorrect Where Needed: ", WHERE_NEEDED1,
            partRequest.getWhereNeeded() );
      Assert.assertEquals( "Incorrect Part ID: ", PART_ID1, partRequest.getPartId() );
      Assert.assertEquals( "Incorrect Needed Quantity: ", NEEDED_QUANTITY1,
            partRequest.getNeededQty() );
      Assert.assertEquals( "Incorrect Part Requirement ID: ", PART_REQUIREMENT_ID1,
            partRequest.getPartRequirementId() );
      Assert.assertEquals( "Incorrect User Status Code: ", USER_STATUS_CODE1,
            partRequest.getUserStatusCode() );
      Assert.assertEquals( "Incorrect Position Name: ", POSITION_NAME1,
            partRequest.getPositionName() );
      Assert.assertEquals( "Incorrect Aircraft ID: ", AIRCRAFT_ID, partRequest.getAircraftId() );
   }
}

package com.mxi.am.api.resource.maintenance.exec.task.partrequirement;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.exec.task.partrequirement.impl.PartRequirementResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class PartRequirementResourceBeanTest extends ResourceBeanTest {

   private static final String PART_REQUIREMENT_ID = "24F5DD0D311F11E899102104BC642F5F";
   private static final String PART1_KEY_ID = "4650:139570:1:1";
   private static final String PART1_PART_ID = "31191D90095A41628E7B6C0AF98A9BF2";
   private static final String NEW_PART_ID = "41191D90095A41628E7B6C0AF98A9BF3";
   private static final String PART_REQUIREMENT_NOTE = "Testing...";
   private static final String PART1_SERIAL_BATCH_NO = "100";
   private static final String NEW_PART_SERIAL_BATCH_NO = "101";
   private static final Double QUANTITY = 1.0;
   private static final String CONFIG_SLOT_ID = "8312D3CE1ABA4FF396E629AA4FA83037";
   private static final String CONFIG_SLOT_CODE = "72-00-00";
   private static final String POSITION_ID = "D213CA80B16A4F6EB1CB21933AD7304B";
   private static final String POSITION_NAME = "1.1";
   private static final String INCOMPATIBLE_PART_ID = "51191D90095A41628E7B6C0AF98A9BF4";
   private static final String INCOMPATIBLE_SERIAL_BATCH_NO = "102";
   private static final String INCOMPATIBLE_ERROR_MESSAGE =
         "[MXERR-30137] The specified part is not compatible with the part requirement's Part Group.";
   private static final String PART2_KEY_ID = "4650:139570:1:2";
   private static final String PART2_PART_ID = "99191D90095A41628E7B6C0AF98A9BF9";
   private static final String PART2_SERIAL_BATCH_NO = "200";

   @Inject
   PartRequirementResourceBean iPartRequirementResourceBean;

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws MxException, AmApiBusinessException {
      InjectorContainer.get().injectMembers( this );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();
   }


   /**
    * Test get method retrieve part requirement successfully.
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void getPartRequirementSuccess()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      Response lResponse =
            iPartRequirementResourceBean.get( iAuthorizedSecurityContext, PART_REQUIREMENT_ID );
      assertStatus( Status.OK, lResponse );
      PartRequirement lPartRequirement = ( PartRequirement ) lResponse.getEntity();
      assertEquals( "Incorrect ID: ", PART_REQUIREMENT_ID, lPartRequirement.getId() );
      assertEquals( "Incorrect Note: ", null, lPartRequirement.getNote() );
      assertEquals( "Incorrect Part ID: ", PART1_PART_ID,
            lPartRequirement.getInstalledParts().get( 0 ).getPartId() );
      assertEquals( "Incorrect Part Serial Batch No: ", PART1_SERIAL_BATCH_NO,
            lPartRequirement.getInstalledParts().get( 0 ).getSerialBatchNumber() );
      assertEquals( "Incorrect Configuration Slot ID: ", lPartRequirement.getConfigurationSlotId(),
            CONFIG_SLOT_ID );
      assertEquals( "Incorrect Configuration Slot Code: ",
            lPartRequirement.getConfigurationSlotCode(), CONFIG_SLOT_CODE );
      assertEquals( "Incorrect Position ID: ", lPartRequirement.getPositionId(), POSITION_ID );
      assertEquals( "Incorrect Position Name: ", lPartRequirement.getPositionName(),
            POSITION_NAME );
   }


   /**
    * Test put method update part requirement successfully.
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void updatePartRequirementSuccess()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      Response lResponse =
            iPartRequirementResourceBean.get( iAuthorizedSecurityContext, PART_REQUIREMENT_ID );
      assertStatus( Status.OK, lResponse );
      PartRequirement lPartRequirement = ( PartRequirement ) lResponse.getEntity();
      lPartRequirement.setNote( PART_REQUIREMENT_NOTE );
      lPartRequirement.setInstalledParts( constructExpectedInstalledParts() );
      Response lResult = iPartRequirementResourceBean.put( iAuthorizedSecurityContext,
            lPartRequirement.getId(), lPartRequirement, null );
      assertStatus( Status.OK, lResult );
      PartRequirement lUpdatedPartRequirement = ( PartRequirement ) lResult.getEntity();
      assertEquals( "Incorrect Note: ", PART_REQUIREMENT_NOTE, lUpdatedPartRequirement.getNote() );

      assertEquals( "Incorrect Part 1 Key ID: ", PART1_KEY_ID,
            lUpdatedPartRequirement.getInstalledParts().get( 0 ).getKeyId() );
      assertEquals( "Incorrect Part 1 ID: ", NEW_PART_ID,
            lUpdatedPartRequirement.getInstalledParts().get( 0 ).getPartId() );
      assertEquals( "Incorrect Part 1 Serial Batch No: ", NEW_PART_SERIAL_BATCH_NO,
            lPartRequirement.getInstalledParts().get( 0 ).getSerialBatchNumber() );

      assertEquals( "Incorrect Part 2 Key ID : ", PART2_KEY_ID,
            lUpdatedPartRequirement.getInstalledParts().get( 1 ).getKeyId() );
      assertEquals( "Incorrect Part 2 ID: ", PART2_PART_ID,
            lUpdatedPartRequirement.getInstalledParts().get( 1 ).getPartId() );
      assertEquals( "Incorrect Part 2 Serial Batch No: ", PART2_SERIAL_BATCH_NO,
            lPartRequirement.getInstalledParts().get( 1 ).getSerialBatchNumber() );
   }


   /**
    * Test put method update part requirement fails because of incompatible part.
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void updatePartRequirementWithIncompatiblePart()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      Response lResponse =
            iPartRequirementResourceBean.get( iAuthorizedSecurityContext, PART_REQUIREMENT_ID );
      assertStatus( Status.OK, lResponse );
      PartRequirement lPartRequirement = ( PartRequirement ) lResponse.getEntity();
      lPartRequirement.setInstalledParts( constructIncompatibleInstalledParts() );
      lResponse = iPartRequirementResourceBean.put( iAuthorizedSecurityContext,
            lPartRequirement.getId(), lPartRequirement, null );
      assertStatus( Status.INTERNAL_SERVER_ERROR, lResponse );
      assertMessageContains( INCOMPATIBLE_ERROR_MESSAGE, lResponse );
   }


   /**
    * Test get method for unauthorized access.
    *
    */
   @Test
   public void getUnauthorizedPrincipal403() {
      iPartRequirementResourceBean.setSecurityContext( iUnauthorizedSecurityContext );
      Response lResponse =
            iPartRequirementResourceBean.get( iUnauthorizedSecurityContext, PART_REQUIREMENT_ID );
      assertStatus( Status.FORBIDDEN, lResponse );
   }


   private List<InstalledPart> constructExpectedInstalledParts() {
      List<InstalledPart> lInstalledParts = new ArrayList<InstalledPart>();

      InstalledPart lInstalledPart = new InstalledPart();
      lInstalledPart.setKeyId( PART1_KEY_ID );
      lInstalledPart.setQuantity( QUANTITY );
      lInstalledPart.setPartId( NEW_PART_ID );
      lInstalledPart.setSerialBatchNumber( NEW_PART_SERIAL_BATCH_NO );
      lInstalledParts.add( lInstalledPart );

      lInstalledPart = new InstalledPart();
      lInstalledPart.setKeyId( PART2_KEY_ID );
      lInstalledPart.setQuantity( QUANTITY );
      lInstalledPart.setPartId( PART2_PART_ID );
      lInstalledPart.setSerialBatchNumber( PART2_SERIAL_BATCH_NO );
      lInstalledParts.add( lInstalledPart );

      return lInstalledParts;
   }


   private List<InstalledPart> constructIncompatibleInstalledParts() {
      List<InstalledPart> lInstalledParts = new ArrayList<InstalledPart>();

      InstalledPart lInstalledPart = new InstalledPart();
      lInstalledPart.setKeyId( PART1_KEY_ID );
      lInstalledPart.setQuantity( QUANTITY );
      lInstalledPart.setPartId( INCOMPATIBLE_PART_ID );
      lInstalledPart.setSerialBatchNumber( INCOMPATIBLE_SERIAL_BATCH_NO );
      lInstalledParts.add( lInstalledPart );

      lInstalledPart = new InstalledPart();
      lInstalledPart.setKeyId( PART2_KEY_ID );
      lInstalledPart.setQuantity( QUANTITY );
      lInstalledPart.setPartId( PART2_PART_ID );
      lInstalledPart.setSerialBatchNumber( PART2_SERIAL_BATCH_NO );
      lInstalledParts.add( lInstalledPart );

      return lInstalledParts;
   }

}

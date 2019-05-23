package com.mxi.am.api.resource.maintenance.eng.prog.requirementdefinition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.mxi.am.api.resource.maintenance.eng.prog.requirementdefinition.impl.RequirementDefinitionResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class RequirementDefinitionResourceBeanTest extends ResourceBeanTest {

   private static final String ID1 = "E7D8E0A97EF242449FDABD84ACC4A0A8";
   private static final String ID2 = "F7D8E0A97EF242449FDABD84ACC4A0A5";
   private static final String ID3 = "A7D8E0A97EF242449FDABD84ACC4A0A0";
   private static final String CODE1 = "27-306-JIC1";
   private static final String CODE2 = "27-306-JIC2";
   private static final String CODE3 = "27-306-JIC3";
   private static final String APPLICABILITY_CODE1 = "Testing 1";
   private static final String APPLICABILITY_CODE2 = "Testing 2";
   private static final String APPLICABILITY_CODE3 = "Testing 3";
   private static final String ASSEMBLY_CODE = "F-2000";
   private static final String CLASS_CODE = "JIC";
   private static final String STATUS = "ACTV";
   private static final String SCHEDULED_FROM_CODE = "MANUFACT_DT";
   private static final boolean ON_CONDITION = false;
   private static final String WORK_TYPE_CD = "SERVICE";
   private static final String DATA_TYPE_CD = "2000";
   private static final String OEM_PART_NUMBER = "15861";
   private static final String ERROR_MESSAGE =
         "Parameter \"id\" and \"oem_part_number\" cannot be provided at the same time.";

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   @Inject
   RequirementDefinitionResourceBean iRequirementDefinitionResourceBean;


   @Before
   public void setUp() throws MxException, AmApiBusinessException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();
   }


   /**
    * Test search requirement definition by ids.
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void getReqDefByIds() throws AmApiBusinessException, AmApiResourceNotFoundException {
      List<String> lIds = new ArrayList<String>();
      lIds.add( ID1 );
      lIds.add( ID2 );

      Response lResponse = iRequirementDefinitionResourceBean.search( iAuthorizedSecurityContext,
            lIds, false, false, null );
      assertStatus( Status.OK, lResponse );
      List<RequirementDefinition> lRequirementDefinitions =
            ( List<RequirementDefinition> ) lResponse.getEntity();
      assertEquals( "Incorrect number of requirement definitions returned: ", 2,
            lRequirementDefinitions.size() );

      // we cannot control the order of the list from the response
      lIds = new ArrayList<String>();
      lIds.add( lRequirementDefinitions.get( 0 ).getId() );
      lIds.add( lRequirementDefinitions.get( 1 ).getId() );
      assertTrue( "Requirement Definition with ID [" + ID1 + "] was not returned.",
            lIds.contains( ID1 ) );
      assertTrue( "Requirement Definition with ID [" + ID2 + "] was not returned.",
            lIds.contains( ID2 ) );
   }


   /**
    * Test search requirement definition by ids with work types.
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void getReqDefByIdsWithWorkTypesWithoutSchedulingParams()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      List<String> lIds = new ArrayList<String>();
      lIds.add( ID1 );

      Response lResponse = iRequirementDefinitionResourceBean.search( iAuthorizedSecurityContext,
            lIds, true, false, null );
      assertStatus( Status.OK, lResponse );
      List<RequirementDefinition> lRequirementDefinitions =
            ( List<RequirementDefinition> ) lResponse.getEntity();
      assertEquals( "Incorrect number of requirement definitions returned: ", 1,
            lRequirementDefinitions.size() );
      assertEquals( "Incorrect ID: ", ID1, lRequirementDefinitions.get( 0 ).getId() );
      assertEquals( "Incorrect Code: ", CODE1, lRequirementDefinitions.get( 0 ).getCode() );
      assertEquals( "Incorrect Applicability Code: ", APPLICABILITY_CODE1,
            lRequirementDefinitions.get( 0 ).getApplicabilityCode() );
      assertEquals( "Incorrect Assembly Code: ", ASSEMBLY_CODE,
            lRequirementDefinitions.get( 0 ).getAssemblyCode() );
      assertEquals( "Incorrect Class Code: ", CLASS_CODE,
            lRequirementDefinitions.get( 0 ).getClassCode() );
      assertEquals( "Incorrect Status: ", STATUS, lRequirementDefinitions.get( 0 ).getStatus() );
      assertEquals( "Incorrect Scheduled From Code: ", SCHEDULED_FROM_CODE,
            lRequirementDefinitions.get( 0 ).getScheduledFromCode() );
      assertEquals( "Incorrect value for On Condition: ", ON_CONDITION,
            lRequirementDefinitions.get( 0 ).isOnCondition() );
      assertEquals( "Incorrect Work Type Code: ", WORK_TYPE_CD,
            lRequirementDefinitions.get( 0 ).getWorkTypeCodes().get( 0 ) );
      assertEquals( "Incorrect number of scheduling data type codes: ", 0,
            lRequirementDefinitions.get( 0 ).getSchedulingDataTypeCodes().size() );
   }


   /**
    * Test search requirement definition by ids with scheduling params.
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void getReqDefByIdsWithSchedulingParamsWithoutWorkTypes()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      List<String> lIds = new ArrayList<String>();
      lIds.add( ID2 );

      Response lResponse = iRequirementDefinitionResourceBean.search( iAuthorizedSecurityContext,
            lIds, false, true, null );
      assertStatus( Status.OK, lResponse );
      List<RequirementDefinition> lRequirementDefinitions =
            ( List<RequirementDefinition> ) lResponse.getEntity();
      assertEquals( "Incorrect number of requirement definitions returned: ", 1,
            lRequirementDefinitions.size() );
      assertEquals( "Incorrect ID: ", ID2, lRequirementDefinitions.get( 0 ).getId() );
      assertEquals( "Incorrect Code: ", CODE2, lRequirementDefinitions.get( 0 ).getCode() );
      assertEquals( "Incorrect Applicability Code: ", APPLICABILITY_CODE2,
            lRequirementDefinitions.get( 0 ).getApplicabilityCode() );
      assertEquals( "Incorrect Assembly Code: ", ASSEMBLY_CODE,
            lRequirementDefinitions.get( 0 ).getAssemblyCode() );
      assertEquals( "Incorrect Class Code: ", CLASS_CODE,
            lRequirementDefinitions.get( 0 ).getClassCode() );
      assertEquals( "Incorrect Status: ", STATUS, lRequirementDefinitions.get( 0 ).getStatus() );
      assertEquals( "Incorrect Scheduled From Code: ", SCHEDULED_FROM_CODE,
            lRequirementDefinitions.get( 0 ).getScheduledFromCode() );
      assertEquals( "Incorrect value for On Condition: ", ON_CONDITION,
            lRequirementDefinitions.get( 0 ).isOnCondition() );
      assertEquals( "Incorrect Data Type Code: ", DATA_TYPE_CD,
            lRequirementDefinitions.get( 0 ).getSchedulingDataTypeCodes().get( 0 ) );
      assertEquals( "Incorrect number of work type codes: ", 0,
            lRequirementDefinitions.get( 0 ).getWorkTypeCodes().size() );
   }


   /**
    * Test search requirement definition by part number.
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void getReqDefByPartNumber()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      Response lResponse = iRequirementDefinitionResourceBean.search( iAuthorizedSecurityContext,
            null, false, false, OEM_PART_NUMBER );
      assertStatus( Status.OK, lResponse );
      List<RequirementDefinition> lRequirementDefinitions =
            ( List<RequirementDefinition> ) lResponse.getEntity();
      assertEquals( "Incorrect number of requirement definitions returned: ", 1,
            lRequirementDefinitions.size() );
      assertEquals( "Incorrect ID: ", ID3, lRequirementDefinitions.get( 0 ).getId() );
      assertEquals( "Incorrect Code: ", CODE3, lRequirementDefinitions.get( 0 ).getCode() );
      assertEquals( "Incorrect Applicability Code: ", APPLICABILITY_CODE3,
            lRequirementDefinitions.get( 0 ).getApplicabilityCode() );
      assertEquals( "Incorrect Assembly Code: ", ASSEMBLY_CODE,
            lRequirementDefinitions.get( 0 ).getAssemblyCode() );
      assertEquals( "Incorrect Class Code: ", CLASS_CODE,
            lRequirementDefinitions.get( 0 ).getClassCode() );
      assertEquals( "Incorrect Status: ", STATUS, lRequirementDefinitions.get( 0 ).getStatus() );
      assertEquals( "Incorrect Scheduled From Code: ", SCHEDULED_FROM_CODE,
            lRequirementDefinitions.get( 0 ).getScheduledFromCode() );
      assertEquals( "Incorrect value for On Condition: ", ON_CONDITION,
            lRequirementDefinitions.get( 0 ).isOnCondition() );
   }


   /**
    * Test search requirement definition by part number with id list and oem part number are both
    * provided.
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void getReqDefByPartNumberWithIds()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      List<String> lIds = new ArrayList<String>();
      lIds.add( ID3 );

      try {
         iRequirementDefinitionResourceBean.search( iAuthorizedSecurityContext, lIds, false, false,
               OEM_PART_NUMBER );
      } catch ( Exception e ) {
         assertTrue(
               "Error returned doesn't contain the correct message. Expected: [" + ERROR_MESSAGE
                     + "] \n Actual: [" + e.getMessage() + "].",
               e.getMessage().contains( ERROR_MESSAGE ) );
      }
   }


   /**
    * Test search method for unauthorized access.
    *
    */
   @Test
   public void getUnauthorizedPrincipal403() {
      List<String> lIds = new ArrayList<String>();
      lIds.add( ID1 );

      iRequirementDefinitionResourceBean.setSecurityContext( iUnauthorizedSecurityContext );
      Response lResponse = iRequirementDefinitionResourceBean.search( iUnauthorizedSecurityContext,
            lIds, false, false, null );
      assertStatus( Status.FORBIDDEN, lResponse );
   }

}

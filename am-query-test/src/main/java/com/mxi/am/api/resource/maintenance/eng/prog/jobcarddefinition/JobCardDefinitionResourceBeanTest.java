package com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiPreconditionFailException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.impl.JobCardDefinitionResourceBean;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.labourrequirement.LabourRequirement;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.step.CertifyingSkill;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.step.JobCardStep;
import com.mxi.am.api.util.AuthorizationUtil;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke test for JobCardDefinitionResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class JobCardDefinitionResourceBeanTest extends ResourceBeanTest {

   @Inject
   JobCardDefinitionResourceBean jobCardDefinitionResourceBean;

   @Mock
   private AuthorizationUtil authorizationUtil;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Rule
   public InjectionOverrideRule fakeInjectionOverrideRule = new InjectionOverrideRule();

   private static final String JOB_CODE1 = "COMP_JIC_1";
   private static final String JOB_CODE2 = "COMP_JIC_2";
   private static final String JOB_CODE5 = "COMP_JIC_3";
   private static final String JOB_CODE4 = "COMP_JIC_4";
   private static final String JOB_CODE6 = "COMP_JIC_6";
   private static final String JOB_CODE7 = "COMP_JIC_7";
   private static final String JOB_CODE8 = "COMP_JIC_10";
   private static final String JOB_CODE11 = "COMP_JIC_11";
   private static final String JOB_CODE12 = "COMP_JIC_12";
   private static final String JOB_NAME1 = "PartJic1";
   private static final String JOB_NAME2 = "PartJic2";
   private static final String JOB_NAME5 = "PartJic3";
   private static final String JOB_NAME4 = "PartJic4";
   private static final String JOB_NAME6 = "PartJic6";
   private static final String JOB_NAME7 = "PartJic7";
   private static final String JOB_NAME8 = "PartJic10";
   private static final String JOB_NAME11 = "PartJic11";
   private static final String JOB_NAME_MODIFIED = "JicNameModified";
   private static final String JOB_DES_MODIFIED = "JicDesModified";
   private static final String JOB_INSTRUCTION_MODIFIED = "JicInstructionModified";
   private static final String JOB_DOCUMENT_REF_MODIFIED = "JicDocRefModified";

   private static final String ID1 = "00000000000000000000000000000001";
   private static final String ID2 = "00000000000000000000000000000002";
   private static final String ID3 = "00000000000000000000000000000003";
   private static final String ID5 = "00000000000000000000000000000005";
   private static final String ID6 = "00000000000000000000000000000006";
   private static final String ID8 = "00000000000000000000000000000008";
   private static final String ID9 = "00000000000000000000000000000009";
   private static final String ID10 = "00000000000000000000000000000010";
   private static final String ID11 = "00000000000000000000000000000011";
   private static final String ID12 = "00000000000000000000000000000012";

   private static final String INVALID_ID = "00000000000110000000000000000001";
   private static final List<String> ID_LIST = Arrays.asList( ID1, ID2 );
   private static final List<String> INVALID_ID_LIST =
         Arrays.asList( "00000000000000000000000000000003", "00000000000000000000000000000004" );
   private static final String CONFIG_SLOT_CODE = "108";
   private static final String PART_NO_OEM = "1806BATCH";
   private static final String PART_NO_OEM_2 = "15861BATCH";
   private static final String PART_NO_OEM_3 = "1807BATCH";
   private static final String PART_ID_2 = "D80DD8170EB946DFB9A906AF8256A717";
   private static final String ASSEMBLY_CODE = "F-2000";
   private static final String EXT_REFERENCE = "3000";
   private static final String CONFIG_SLOT_ID = "8F90DF4C52224E6694C686915FE2F249";
   private static final String CONFIG_SLOT_ID_8 = "8F90DF4C52224E6694C686915FE2F250";
   private static final String ASSEMBLY_ID = "17FBB18D95F347A38C8B887ADD4D635E";
   private static final String ASSEMBLY_ID_8 = "18FBB18D95F347A38C8B887ADD4D635E";

   private static final Integer STEP_ORDER1 = 1;
   private static final String STEP_DESCRIPTION1 = "Step 1";
   private static final String APPLICABILITY_RANGE1 = "A79-A99";

   private static final Integer STEP_ORDER2 = 2;
   private static final String STEP_DESCRIPTION2 = "Step 2";
   private static final String APPLICABILITY_RANGE2 = "A79-A82";

   private static final Integer STEP_ORDER3 = 1;
   private static final String STEP_DESCRIPTION3 = "Step 3";
   private static final String APPLICABILITY_RANGE3 = "A79-A97";

   private static final Integer STEP_ORDER4 = 2;
   private static final String STEP_DESCRIPTION4 = "Step 4";
   private static final String APPLICABILITY_RANGE4 = "A79-A85";

   private static final String TASK_CLASS = "INSP";
   private static final String ORGANIZATION = "F49D9445729911E7B349CF53298AE07C";
   private static final String STATUS_BUILD = "BUILD";
   private static final String STATUS_REVISION = "REVISION";
   private static final String STATUS_ACTV = "ACTV";
   private static final String STATUS_OBSOLETE = "OBSOLETE";
   private static final String DESCRIPTION = "Data for unit test. Do not modify this.";
   private static final boolean LOCKED = true;

   private static final String PANEL_ID1 = "01EFDA12E1C647C4B9EB8534C4D88B8A";
   private static final String PANEL_ID2 = "26D26DE56C9842D1AC70A58FFDEC3EAE";
   private static final String PANEL_ID3 = "8DA7BCB89C224FD5AC95BAE7CE1B0DE8";
   private static final String PANEL_ID4 = "59429A80B86C464EAB256FEC312568BA";
   private static final List<String> PANEL_ID_LIST1 = Arrays.asList( PANEL_ID1, PANEL_ID2 );
   private static final List<String> PANEL_ID_LIST2 = Arrays.asList( PANEL_ID3, PANEL_ID4 );

   private JobCardDefinition actualJobCardDefinition1;
   private JobCardDefinition actualJobCardDefinition2;
   private JobCardDefinition actualJobCardDefinition3;
   private JobCardDefinition actualJobCardDefinition4;

   private static final String ZONE_ID1 = "C1735860F19D4AE9912C0CD98D2E6567";
   private static final String ZONE_ID2 = "98536177E5004D6E823C9FFDEFD20BA0";
   private static final String ZONE_ID3 = "45338E6C09934A11A23172BCA12192B3";
   private static final String ZONE_ID4 = "75338E6C09934A11A23172BCA12192B6";
   private static final List<String> ZONE_ID_LIST1 = Arrays.asList( ZONE_ID1, ZONE_ID2 );
   private static final List<String> ZONE_ID_LIST2 = Arrays.asList( ZONE_ID3, ZONE_ID4 );

   private static final String LABOR_SKILL_CD1 = "PILOT";
   private static final String LABOR_SKILL_CD2 = "ENG";
   private static final String LABOR_SKILL_CD3 = "LBR";
   private static final String INVALID_LABOR_SKILL_CD = "PILOT2";

   private static final String LABOUR_SKILL_ERROR =
         "[MXERR-33611] The labor skill 'PILOT' is not marked as certification required";
   private static final String DUPLICATE_STEP_LABOUR_SKILL_ERROR =
         "Duplicate labor skill codes are found in step, 1 : ENG";
   private static final String DUPLICATE_LABOUR_REQUIREMENT_SKILL_ERROR =
         "Duplicate labor skill codes are found in labor requirements, PILOT";
   private static final String INVALID_STEP_LABOUR_SKILL_ERROR = "Invalid Labor Skill Code: PILOT2";


   @Before
   public void setUp() throws MxException {

      InjectorContainer.get().injectMembers( this );
      constructExpectedResults();
      jobCardDefinitionResourceBean.setEJBContext( ejbContext );
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

   }


   /**
    *
    * Test method for get Job Card by valid id
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testGetJobCardById() throws AmApiResourceNotFoundException {
      JobCardDefinition jobCardDefinition = jobCardDefinitionResourceBean.get( ID1 );
      assertObjectsEquals( getPartNumBasedDefaultJobCard(), jobCardDefinition );
   }


   /**
    *
    * Test method for get Job Card by null id
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetJobCardByNullId() throws AmApiResourceNotFoundException {
      jobCardDefinitionResourceBean.get( null );
   }


   /**
    *
    * Test method for get Job Card by invalid id
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetJobCardByInvalidId() throws AmApiResourceNotFoundException {
      jobCardDefinitionResourceBean.get( INVALID_ID );
   }


   /**
    *
    * Test method for create config slot based job card
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void createConfigSlotBasedJobCardSucess200()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinitionForPost();
      JobCardDefinition createdJobCardDefinition = jobCardDefinitionResourceBean.post( jobCardObj );
      createdJobCardDefinition =
            jobCardDefinitionResourceBean.get( createdJobCardDefinition.getId() );
      assertObjectsEquals( getConfigSlotBasedJobCardDefinitionForPost(), createdJobCardDefinition );
   }


   /**
    * Test method to create part number based job card
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void createPartNumBasedJobCardSuccess200()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      JobCardDefinition jobCardDefinition = getPartNumBasedJobCardDefinitionForPost();
      JobCardDefinition createdJobCardDefinition =
            jobCardDefinitionResourceBean.post( jobCardDefinition );
      createdJobCardDefinition =
            jobCardDefinitionResourceBean.get( createdJobCardDefinition.getId() );
      assertObjectsEquals( getPartNumBasedJobCardDefinitionForPost(), createdJobCardDefinition );
   }


   /**
    *
    * Test method for create job card with invalid status
    *
    * @throws AmApiBusinessException
    *
    */
   @Test( expected = AmApiBusinessException.class )
   public void createJobCardFailureStatusValidation() throws AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinitionForPost();
      jobCardObj.setStatus( STATUS_ACTV );
      jobCardDefinitionResourceBean.post( jobCardObj );
   }


   /**
    *
    * Test method for create job card with not providing status
    *
    * @throws AmApiBusinessException
    *
    */
   @Test( expected = AmApiBadRequestException.class )
   public void createJobCardFailureEmptyStatusValidation() throws AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinitionForPost();
      jobCardObj.setStatus( "" );
      jobCardDefinitionResourceBean.post( jobCardObj );
   }


   /**
    * Test method for create job card when both config slot and part details are provided
    *
    * @throws AmApiBusinessException
    *
    */
   @Test( expected = AmApiBadRequestException.class )
   public void createJobCardWithBothConfigSlotAndPartDetails() throws AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinitionForPost();
      jobCardObj.setPartId( PART_ID_2 );
      jobCardObj.setPartNoOem( PART_NO_OEM_2 );
      jobCardDefinitionResourceBean.post( jobCardObj );
   }


   /**
    *
    * Test method for create job card not providing organization
    *
    * @throws AmApiBusinessException
    *
    */
   @Test( expected = AmApiBadRequestException.class )
   public void createJobCardFailureOrganizationValidation() throws AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinitionForPost();
      jobCardObj.setOrganization( "" );
      jobCardDefinitionResourceBean.post( jobCardObj );
   }


   /**
    *
    * Test method for create job card revision
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void createJobCardRevisionSucess200()
         throws AmApiBusinessException, AmApiResourceNotFoundException {
      JobCardDefinition jobCardObj = getPartNumBasedJobCardRevisionForPost();
      JobCardDefinition createdJobCardDefinition = jobCardDefinitionResourceBean.post( jobCardObj );
      createdJobCardDefinition =
            jobCardDefinitionResourceBean.get( createdJobCardDefinition.getId() );
      assertObjectsEquals( getPartNumBasedJobCardRevisionForPost(), createdJobCardDefinition );
   }


   /**
    *
    * Test method for create job card/ revision failure for ACTV status
    *
    * @throws AmApiBusinessException
    *
    */
   @Test( expected = AmApiBusinessException.class )
   public void createJobCardFailureForACTV() throws AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedJobCardRevisionForPost();
      jobCardObj.setId( ID6 );
      jobCardObj.setStatus( STATUS_ACTV );
      jobCardDefinitionResourceBean.post( jobCardObj );
   }


   /**
    *
    * Test method for update part no based job card
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void updatePartNoBasedJobCardSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedDefaultJobCard();
      jobCardObj.setName( JOB_NAME_MODIFIED );
      jobCardObj.setDescription( JOB_DES_MODIFIED );
      jobCardObj.setInstructions( JOB_INSTRUCTION_MODIFIED );
      jobCardObj.setDocumentReference( JOB_DOCUMENT_REF_MODIFIED );

      jobCardDefinitionResourceBean.put( ID1, jobCardObj );
      JobCardDefinition jobCardDefinition = jobCardDefinitionResourceBean.get( ID1 );

      assertObjectsEquals( jobCardObj, jobCardDefinition );
   }


   /**
    *
    * Test method for update part no based job card having several parts
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void updatePartNoBasedJobCardWithTwoPartsSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = new JobCardDefinition();
      jobCardObj.setCode( JOB_CODE12 );
      jobCardObj.setPartNoOem( PART_NO_OEM_3 );
      jobCardObj.setName( JOB_NAME_MODIFIED );
      jobCardObj.setDescription( JOB_DES_MODIFIED );
      jobCardObj.setInstructions( JOB_INSTRUCTION_MODIFIED );
      jobCardObj.setDocumentReference( JOB_DOCUMENT_REF_MODIFIED );

      JobCardDefinition jobCardObj1 = new JobCardDefinition();
      jobCardObj1.setCode( JOB_CODE12 );
      jobCardObj1.setPartNoOem( PART_NO_OEM );
      jobCardObj1.setName( JOB_NAME_MODIFIED );
      jobCardObj1.setDescription( JOB_DES_MODIFIED );
      jobCardObj1.setInstructions( JOB_INSTRUCTION_MODIFIED );
      jobCardObj1.setDocumentReference( JOB_DOCUMENT_REF_MODIFIED );

      jobCardDefinitionResourceBean.put( ID12, jobCardObj );
      JobCardDefinitionSearchParameters searchParam = new JobCardDefinitionSearchParameters( null,
            JOB_CODE12, null, null, null, null, false );
      List<JobCardDefinition> jobCardDefinitions =
            jobCardDefinitionResourceBean.search( searchParam );

      assertEquals( 2, jobCardDefinitions.size() );
      for ( JobCardDefinition jobCardDefObj : jobCardDefinitions ) {
         if ( jobCardDefObj.getPartNoOem().equals( jobCardObj.getPartNoOem() ) ) {
            assertObjectsEquals( jobCardObj, jobCardDefObj );
         }
         if ( jobCardDefObj.getPartNoOem().equals( jobCardObj1.getPartNoOem() ) ) {
            assertObjectsEquals( jobCardObj1, jobCardDefObj );
         }
      }
   }


   /**
    *
    * Test method for activate job card
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void activateJobCardSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedDefaultJobCard();
      jobCardObj.setStatus( STATUS_ACTV );
      jobCardDefinitionResourceBean.put( ID1, jobCardObj );
      JobCardDefinition jobCardDefinition = jobCardDefinitionResourceBean.get( ID1 );
      assertEquals( STATUS_ACTV, jobCardDefinition.getStatus() );
   }


   /**
    *
    * Test method for obsolete job card
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void obsoleteJobCardSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedDefaultJobCard();
      jobCardObj.setStatus( STATUS_OBSOLETE );
      jobCardDefinitionResourceBean.put( ID8, jobCardObj );
      JobCardDefinition jobCardDefinition = jobCardDefinitionResourceBean.get( ID8 );
      assertEquals( STATUS_OBSOLETE, jobCardDefinition.getStatus() );
   }


   /**
    *
    * Test method for lock Job Card
    *
    * @throws AmApiResourceNotFoundException
    * @throws AmApiBusinessException
    *
    */
   @Test
   public void lockJobCardSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedDefaultJobCard();
      jobCardObj.setLocked( LOCKED );
      jobCardDefinitionResourceBean.put( ID8, jobCardObj );
      JobCardDefinition jobCardDefinition = jobCardDefinitionResourceBean.get( ID8 );
      assertEquals( LOCKED, jobCardDefinition.isLocked() );
   }


   /**
    *
    * Test method for Existing job card details should not be updated when update message send null
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void updateJobCardWhenEditableAttributesAreNull()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedDefaultJobCard();
      jobCardObj.setName( null );
      jobCardObj.setDescription( null );
      jobCardObj.setInstructions( null );
      jobCardObj.setDocumentReference( null );

      jobCardDefinitionResourceBean.put( ID1, jobCardObj );
      JobCardDefinition jobCardDefinition = jobCardDefinitionResourceBean.get( ID1 );

      assertObjectsEquals( getPartNumBasedDefaultJobCard(), jobCardDefinition );
      assertNotEquals( jobCardObj, jobCardDefinition );
   }


   /**
    *
    * Test method for update job card when job card is not found
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void updateJobCardNotFound()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedDefaultJobCard();
      jobCardObj.setId( ID5 );
      jobCardObj.setName( JOB_NAME_MODIFIED );
      jobCardObj.setDescription( JOB_DES_MODIFIED );
      jobCardObj.setInstructions( JOB_INSTRUCTION_MODIFIED );
      jobCardObj.setDocumentReference( JOB_DOCUMENT_REF_MODIFIED );
      jobCardDefinitionResourceBean.put( ID5, jobCardObj );
   }


   /**
    *
    * Test method for update active job card
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiBusinessException.class )
   public void updateActiveJobCard() throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedDefaultJobCard();
      jobCardObj.setId( ID6 );
      jobCardObj.setName( JOB_NAME_MODIFIED );
      jobCardObj.setDescription( JOB_DES_MODIFIED );
      jobCardObj.setInstructions( JOB_INSTRUCTION_MODIFIED );
      jobCardObj.setDocumentReference( JOB_DOCUMENT_REF_MODIFIED );
      jobCardDefinitionResourceBean.put( ID6, jobCardObj );
   }


   /**
    *
    * Test method for update job cards with a non JIC type task class
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiBadRequestException.class )
   public void updateJobCardWithInvalidTaskClass()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedDefaultJobCard();
      jobCardObj.setId( ID3 );
      jobCardObj.setName( JOB_NAME_MODIFIED );
      jobCardObj.setDescription( JOB_DES_MODIFIED );
      jobCardObj.setInstructions( JOB_INSTRUCTION_MODIFIED );
      jobCardObj.setDocumentReference( JOB_DOCUMENT_REF_MODIFIED );
      jobCardDefinitionResourceBean.put( ID3, jobCardObj );
   }


   /**
    *
    * Test method for update a locked job card
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiBusinessException.class )
   public void updateLockedJobCard() throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getLockedConfigSlotBasedJobCardDefinition();
      jobCardDefinitionResourceBean.put( ID10, jobCardObj );
   }


   /**
    *
    * Test method for remove existing job card zones
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void removeJobCardZonesSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedDefaultJobCard1();
      jobCardObj.setZoneIds( null );
      processJobCardDefinition( jobCardObj, ID9 );
   }


   /**
    *
    * Test method for remove existing job card panels
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void removeJobCardPanelsSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedDefaultJobCard1();
      jobCardObj.setPanelIds( null );
      processJobCardDefinition( jobCardObj, ID9 );
   }


   /**
    *
    * Test method for remove existing job card steps
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void removeJobCardStepsSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getPartNumBasedDefaultJobCard();
      jobCardObj.setJobCardSteps( null );
      processJobCardDefinition( jobCardObj, ID1 );
   }


   /**
    *
    * Test method for add job card zones
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addJobCardZonesSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addZonesToJobCardDefinition( jobCardObj );
      processJobCardDefinition( jobCardObj, ID9 );
   }


   /**
    *
    * Test method for add job card panels
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addJobCardPanelsSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addPanelsToJobCardDefinition( jobCardObj );
      processJobCardDefinition( jobCardObj, ID9 );
   }


   /**
    *
    * Test method for add job steps
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addJobCardStepsSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addStepsToJobCardDefinition( jobCardObj );
      processJobCardDefinition( jobCardObj, ID9 );
   }


   /**
    *
    * Test method for add job steps with skill
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addJobCardStepsWithSkillSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addStepsWithSkillToJobCardDefinition( jobCardObj );
      processJobCardDefinition( jobCardObj, ID9 );
   }


   /**
    *
    * Test method for add job steps with duplicate skills
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    *
    */
   @Test
   public void addJobCardStepsWithDuplicateSkills()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addStepsWithDuplicateSkillToJobCardDefinition( jobCardObj );
      try {
         jobCardDefinitionResourceBean.put( ID9, jobCardObj );
      } catch ( AmApiBadRequestException exception ) {
         assertEquals( DUPLICATE_STEP_LABOUR_SKILL_ERROR, exception.getMessage() );
      }
   }


   /**
    *
    * Test method for add job steps with duplicate skills
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addJobCardStepsWithInvalidSkills()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addStepsWithInvalidSkillToJobCardDefinition( jobCardObj );
      try {
         jobCardDefinitionResourceBean.put( ID9, jobCardObj );
      } catch ( AmApiBadRequestException exception ) {
         assertEquals( INVALID_STEP_LABOUR_SKILL_ERROR, exception.getMessage() );
      }
   }


   /**
    *
    * Test method for add job card labour requirements with duplicate skills
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addJobCardLabourRequirementsWithDuplicateSkills()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addDuplicateLabourRequirementToJobCardDefinition( jobCardObj );
      try {
         jobCardDefinitionResourceBean.put( ID9, jobCardObj );
      } catch ( AmApiBadRequestException exception ) {
         assertEquals( DUPLICATE_LABOUR_REQUIREMENT_SKILL_ERROR, exception.getMessage() );
      }
   }


   /**
    *
    * Test method for add job steps with duplicate skills
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addJobCardLabourRequirementsWithInvalidSkills()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addLabourRequirementsWithInvalidSkillToJobCardDefinition( jobCardObj );
      try {
         jobCardDefinitionResourceBean.put( ID9, jobCardObj );
      } catch ( AmApiBadRequestException exception ) {
         assertEquals( INVALID_STEP_LABOUR_SKILL_ERROR, exception.getMessage() );
      }
   }


   /**
    *
    * Test method for add Job Card Labour Requirement
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addJobCardLabourRequirementSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addLabourRequirementToJobCardDefinition( jobCardObj );
      processJobCardDefinition( jobCardObj, ID9 );
   }


   /**
    *
    * Test method for add Job Card Steps With Invalid Skill
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addJobCardStepsWithSkillRequireInspectionFalse()
         throws AmApiResourceNotFoundException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addStepsWithSkillRequireInspectionFalseToJobCardDefinition( jobCardObj );
      try {
         jobCardDefinitionResourceBean.put( ID9, jobCardObj );
      } catch ( AmApiBusinessException exception ) {
         assertEquals( LABOUR_SKILL_ERROR, exception.getMessage() );
      }

   }


   /**
    *
    * Test method for add job card multiple labor requirements
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void addMultipleJobCardLabourRequirementSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedJobCardDefinition1();
      jobCardObj = addMultipleLabourRequirementToJobCardDefinition( jobCardObj );
      processJobCardDefinition( jobCardObj, ID9 );
   }


   /**
    *
    * Test method for update config slot based job card by removing/adding job card zones, panels &
    * steps
    *
    * @throws AmApiBusinessException
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void updateConfigSlotBasedJobCardSucess200()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition jobCardObj = getConfigSlotBasedDefaultJobCard2();
      jobCardObj = updateZonesInJobCardDefinition( jobCardObj );
      jobCardObj = updatePanelsInJobCardDefinition( jobCardObj );
      jobCardObj = updateStepsInJobCardDefinition( jobCardObj );
      processJobCardDefinition( jobCardObj, ID11 );
   }


   /**
    *
    * Test search method for happy path - Search by ID list
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchJobCardByIDList() throws AmApiResourceNotFoundException {
      JobCardDefinitionSearchParameters searchParam =
            new JobCardDefinitionSearchParameters( ID_LIST, null, null, null, null, null, false );
      List<JobCardDefinition> jobCardDefinitions =
            jobCardDefinitionResourceBean.search( searchParam );
      assertEquals( 2, jobCardDefinitions.size() );
      for ( JobCardDefinition jobCardDefObj : jobCardDefinitions ) {
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition1.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition1, jobCardDefObj );
         }
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition2.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition2, jobCardDefObj );
         }
      }
   }


   /**
    *
    * Test search method for happy path - Search by Task code
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchJobCardByTaskCode() throws AmApiResourceNotFoundException {
      JobCardDefinitionSearchParameters searchParam =
            new JobCardDefinitionSearchParameters( null, JOB_CODE1, null, null, null, null, false );
      List<JobCardDefinition> jobCardDefinitions =
            jobCardDefinitionResourceBean.search( searchParam );
      assertEquals( 1, jobCardDefinitions.size() );
      assertObjectsEquals( actualJobCardDefinition1, jobCardDefinitions.get( 0 ) );
   }


   /**
    *
    * Test search method for happy path - Search by Configuration Slot Code
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchJobCardByConfigSlotCode() throws AmApiResourceNotFoundException {
      JobCardDefinitionSearchParameters searchParam = new JobCardDefinitionSearchParameters( null,
            null, CONFIG_SLOT_CODE, null, null, null, false );
      List<JobCardDefinition> jobCardDefinitions =
            jobCardDefinitionResourceBean.search( searchParam );
      assertEquals( 3, jobCardDefinitions.size() );
      for ( JobCardDefinition jobCardDefObj : jobCardDefinitions ) {
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition3.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition3, jobCardDefObj );
         }
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition4.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition4, jobCardDefObj );
         }
      }
   }


   /**
    *
    * Test search method for happy path - Search by Assembly Code
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchJobCardByAssemblyCode() throws AmApiResourceNotFoundException {
      JobCardDefinitionSearchParameters searchParam = new JobCardDefinitionSearchParameters( null,
            null, null, ASSEMBLY_CODE, null, null, false );
      List<JobCardDefinition> jobCardDefinitions =
            jobCardDefinitionResourceBean.search( searchParam );
      assertEquals( 3, jobCardDefinitions.size() );
      for ( JobCardDefinition jobCardDefObj : jobCardDefinitions ) {
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition3.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition3, jobCardDefObj );
         }
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition4.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition4, jobCardDefObj );
         }
      }
   }


   /**
    *
    * Test search method for happy path - Search by External reference
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchJobCardByExternalReference() throws AmApiResourceNotFoundException {
      JobCardDefinitionSearchParameters searchParam = new JobCardDefinitionSearchParameters( null,
            null, null, null, EXT_REFERENCE, null, false );
      List<JobCardDefinition> jobCardDefinitions =
            jobCardDefinitionResourceBean.search( searchParam );
      assertEquals( 6, jobCardDefinitions.size() );
      for ( JobCardDefinition jobCardDefObj : jobCardDefinitions ) {
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition1.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition1, jobCardDefObj );
         }
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition2.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition2, jobCardDefObj );
         }
      }
   }


   /**
    *
    * Test search method for happy path - Search by Part no
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchJobCardByPartNo() throws AmApiResourceNotFoundException {
      JobCardDefinitionSearchParameters searchParam = new JobCardDefinitionSearchParameters( null,
            null, null, null, null, PART_NO_OEM, false );
      List<JobCardDefinition> jobCardDefinitions =
            jobCardDefinitionResourceBean.search( searchParam );
      assertEquals( 4, jobCardDefinitions.size() );
      for ( JobCardDefinition jobCardDefObj : jobCardDefinitions ) {
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition1.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition1, jobCardDefObj );
         }
         if ( jobCardDefObj.getId().equals( actualJobCardDefinition2.getId() ) ) {
            assertObjectsEquals( actualJobCardDefinition2, jobCardDefObj );
         }
      }
   }


   /**
    *
    * Test search method for failed preconditions(null parameters)
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiPreconditionFailException.class )
   public void testSearchPreconditionsFailed() throws AmApiResourceNotFoundException {
      JobCardDefinitionSearchParameters searchParam =
            new JobCardDefinitionSearchParameters( null, null, null, null, null, null, null );
      jobCardDefinitionResourceBean.search( searchParam );
   }


   /**
    *
    * Test search method for non existing job cards
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchNotFound() throws AmApiResourceNotFoundException {
      JobCardDefinitionSearchParameters searchParam = new JobCardDefinitionSearchParameters(
            INVALID_ID_LIST, null, null, null, null, null, null );
      List<JobCardDefinition> jobCardDefinitions =
            jobCardDefinitionResourceBean.search( searchParam );
      assertEquals( 0, jobCardDefinitions.size() );
   }


   private void constructExpectedResults() {
      actualJobCardDefinition1 = new JobCardDefinition();
      actualJobCardDefinition2 = new JobCardDefinition();
      actualJobCardDefinition3 = new JobCardDefinition();
      actualJobCardDefinition4 = new JobCardDefinition();

      actualJobCardDefinition1.setId( ID1 );
      actualJobCardDefinition1.setCode( JOB_CODE1 );
      actualJobCardDefinition1.setName( JOB_NAME1 );
      actualJobCardDefinition1.setDescription( DESCRIPTION );
      actualJobCardDefinition1.setPartNoOem( PART_NO_OEM );

      // Add job card step list to job card definition object
      List<JobCardStep> jobcardStepList = new ArrayList<JobCardStep>();
      jobcardStepList.add( setJobCardStep1() );
      jobcardStepList.add( setJobCardStep2() );
      actualJobCardDefinition1.setJobCardSteps( jobcardStepList );

      actualJobCardDefinition2.setId( ID2 );
      actualJobCardDefinition2.setCode( JOB_CODE2 );
      actualJobCardDefinition2.setName( JOB_NAME2 );
      actualJobCardDefinition2.setDescription( DESCRIPTION );
      actualJobCardDefinition2.setPartNoOem( PART_NO_OEM );

      actualJobCardDefinition3.setId( ID8 );
      actualJobCardDefinition3.setCode( JOB_CODE5 );
      actualJobCardDefinition3.setName( JOB_NAME5 );
      actualJobCardDefinition3.setDescription( DESCRIPTION );
      actualJobCardDefinition3.setConfigSlotId( CONFIG_SLOT_ID );
      actualJobCardDefinition3.setAssemblyId( ASSEMBLY_ID );

      actualJobCardDefinition4.setId( ID9 );
      actualJobCardDefinition4.setCode( JOB_CODE7 );
      actualJobCardDefinition4.setName( JOB_NAME7 );
      actualJobCardDefinition4.setDescription( DESCRIPTION );
      actualJobCardDefinition4.setConfigSlotId( CONFIG_SLOT_ID );
      actualJobCardDefinition4.setAssemblyId( ASSEMBLY_ID );

   }


   private void processJobCardDefinition( JobCardDefinition jobCardDefinition, String id )
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      JobCardDefinition updatedJobCardDefinition =
            jobCardDefinitionResourceBean.put( id, jobCardDefinition );
      JobCardDefinition jobCardDef =
            jobCardDefinitionResourceBean.get( updatedJobCardDefinition.getId() );
      assertObjectsEquals( jobCardDefinition, jobCardDef );
   }


   private JobCardDefinition getPartNumBasedDefaultJobCard() {
      JobCardDefinition jobCardDefinition = getPartNumBasedJobCardDefinition();
      jobCardDefinition = addStepsToJobCardDefinition( jobCardDefinition );
      return jobCardDefinition;
   }


   /**
    *
    * Return a config slot based job card that has no zones, panels & steps defined in the DB
    *
    */
   private JobCardDefinition getConfigSlotBasedDefaultJobCard1() {
      JobCardDefinition jobCardDefinition = getConfigSlotBasedJobCardDefinition1();
      jobCardDefinition = addZonesToJobCardDefinition( jobCardDefinition );
      jobCardDefinition = addPanelsToJobCardDefinition( jobCardDefinition );
      jobCardDefinition = addStepsToJobCardDefinition( jobCardDefinition );
      return jobCardDefinition;
   }


   /**
    *
    * Return a config slot based job card that has zones, panels & steps defined in the DB
    *
    */
   private JobCardDefinition getConfigSlotBasedDefaultJobCard2() {
      JobCardDefinition jobCardDefinition = getConfigSlotBasedJobCardDefinition2();
      jobCardDefinition = addZonesToJobCardDefinition( jobCardDefinition );
      jobCardDefinition = addPanelsToJobCardDefinition( jobCardDefinition );
      jobCardDefinition = addStepsToJobCardDefinition( jobCardDefinition );
      return jobCardDefinition;
   }


   private JobCardDefinition getPartNumBasedJobCardDefinition() {
      JobCardDefinition jobCardDefinition = new JobCardDefinition();
      jobCardDefinition.setCode( JOB_CODE1 );
      jobCardDefinition.setName( JOB_NAME1 );
      jobCardDefinition.setDescription( DESCRIPTION );
      jobCardDefinition.setPartNoOem( PART_NO_OEM );
      return jobCardDefinition;
   }


   private JobCardDefinition getConfigSlotBasedJobCardDefinition1() {
      JobCardDefinition jobCardDefinition = new JobCardDefinition();
      jobCardDefinition.setCode( JOB_CODE7 );
      jobCardDefinition.setName( JOB_NAME7 );
      jobCardDefinition.setDescription( DESCRIPTION );
      jobCardDefinition.setConfigSlotId( CONFIG_SLOT_ID );
      jobCardDefinition.setAssemblyId( ASSEMBLY_ID );
      return jobCardDefinition;
   }


   private JobCardDefinition getConfigSlotBasedJobCardDefinition2() {
      JobCardDefinition jobCardDefinition = new JobCardDefinition();
      jobCardDefinition.setCode( JOB_CODE11 );
      jobCardDefinition.setName( JOB_NAME11 );
      jobCardDefinition.setDescription( DESCRIPTION );
      jobCardDefinition.setConfigSlotId( CONFIG_SLOT_ID );
      jobCardDefinition.setAssemblyId( ASSEMBLY_ID );
      return jobCardDefinition;
   }


   private JobCardDefinition getLockedConfigSlotBasedJobCardDefinition() {
      JobCardDefinition jobCardDefinition = new JobCardDefinition();
      jobCardDefinition.setCode( JOB_CODE8 );
      jobCardDefinition.setName( JOB_NAME8 );
      jobCardDefinition.setDescription( DESCRIPTION );
      jobCardDefinition.setConfigSlotId( CONFIG_SLOT_ID_8 );
      jobCardDefinition.setAssemblyId( ASSEMBLY_ID_8 );
      jobCardDefinition.setLocked( LOCKED );
      return jobCardDefinition;
   }


   private JobCardDefinition addZonesToJobCardDefinition( JobCardDefinition jobCardDefinition ) {
      jobCardDefinition.setZoneIds( ZONE_ID_LIST1 );
      return jobCardDefinition;
   }


   private JobCardDefinition updateZonesInJobCardDefinition( JobCardDefinition jobCardDefinition ) {
      jobCardDefinition.setZoneIds( ZONE_ID_LIST2 );
      return jobCardDefinition;
   }


   private JobCardDefinition addPanelsToJobCardDefinition( JobCardDefinition jobCardDefinition ) {
      jobCardDefinition.setPanelIds( PANEL_ID_LIST1 );
      return jobCardDefinition;
   }


   private JobCardDefinition
         updatePanelsInJobCardDefinition( JobCardDefinition jobCardDefinition ) {
      jobCardDefinition.setPanelIds( PANEL_ID_LIST2 );
      return jobCardDefinition;
   }


   private JobCardDefinition
         addLabourRequirementToJobCardDefinition( JobCardDefinition jobCardDefinition ) {

      List<LabourRequirement> labourRequirementList = new ArrayList<LabourRequirement>();
      LabourRequirement labourRequirement = new LabourRequirement();

      labourRequirement.setLaborSkill( LABOR_SKILL_CD1 );
      labourRequirement.setRequireCertification( true );
      labourRequirement.setRequireInspection( true );
      labourRequirementList.add( labourRequirement );

      jobCardDefinition.setLabourRequirements( labourRequirementList );
      return jobCardDefinition;
   }


   private JobCardDefinition
         addMultipleLabourRequirementToJobCardDefinition( JobCardDefinition jobCardDefinition ) {

      List<LabourRequirement> labourRequirementList = new ArrayList<LabourRequirement>();
      LabourRequirement labourRequirement1 = new LabourRequirement();

      labourRequirement1.setLaborSkill( LABOR_SKILL_CD1 );
      labourRequirement1.setRequireCertification( true );
      labourRequirement1.setRequireInspection( true );

      LabourRequirement labourRequirement2 = new LabourRequirement();

      labourRequirement2.setLaborSkill( LABOR_SKILL_CD2 );
      labourRequirement2.setRequireCertification( true );
      labourRequirement2.setRequireInspection( true );

      labourRequirementList.add( labourRequirement1 );
      labourRequirementList.add( labourRequirement2 );

      jobCardDefinition.setLabourRequirements( labourRequirementList );
      return jobCardDefinition;
   }


   private JobCardDefinition addStepsToJobCardDefinition( JobCardDefinition jobCardDefinition ) {
      List<JobCardStep> jobcardStepList = new ArrayList<JobCardStep>();
      jobcardStepList.add( setJobCardStep1() );
      jobcardStepList.add( setJobCardStep2() );
      jobCardDefinition.setJobCardSteps( jobcardStepList );
      return jobCardDefinition;
   }


   private JobCardDefinition
         addDuplicateLabourRequirementToJobCardDefinition( JobCardDefinition jobCardDefinition ) {

      List<LabourRequirement> labourRequirementList = new ArrayList<LabourRequirement>();
      LabourRequirement labourRequirement1 = new LabourRequirement();

      labourRequirement1.setLaborSkill( LABOR_SKILL_CD1 );
      labourRequirement1.setRequireCertification( true );
      labourRequirement1.setRequireInspection( true );

      labourRequirementList.add( labourRequirement1 );
      labourRequirementList.add( labourRequirement1 );

      jobCardDefinition.setLabourRequirements( labourRequirementList );
      return jobCardDefinition;
   }


   private JobCardDefinition updateStepsInJobCardDefinition( JobCardDefinition jobCardDefinition ) {
      List<JobCardStep> jobcardStepList = new ArrayList<JobCardStep>();
      jobcardStepList.add( setJobCardStep3() );
      jobcardStepList.add( addSkillTojobcardStep( setJobCardStep4() ) );
      jobCardDefinition.setJobCardSteps( jobcardStepList );
      return jobCardDefinition;
   }


   private JobCardDefinition
         addStepsWithSkillToJobCardDefinition( JobCardDefinition jobCardDefinition ) {
      List<JobCardStep> jobcardStepList = new ArrayList<JobCardStep>();
      jobcardStepList.add( addSkillTojobcardStep( setJobCardStep1() ) );
      jobcardStepList.add( setJobCardStep2() );
      jobCardDefinition.setJobCardSteps( jobcardStepList );
      return jobCardDefinition;
   }


   private JobCardStep addSkillTojobcardStep( JobCardStep jobCardStep ) {
      List<CertifyingSkill> certifyingSkills = new ArrayList<CertifyingSkill>();
      CertifyingSkill certifyingEngSkill = new CertifyingSkill();
      certifyingEngSkill.setLaborSkill( LABOR_SKILL_CD2 );
      certifyingEngSkill.setRequireInspection( true );

      CertifyingSkill certifyingLbrSkill = new CertifyingSkill();
      certifyingLbrSkill.setLaborSkill( LABOR_SKILL_CD3 );
      certifyingLbrSkill.setRequireInspection( true );

      certifyingSkills.add( certifyingEngSkill );
      certifyingSkills.add( certifyingLbrSkill );
      jobCardStep.setCertifyingSkills( certifyingSkills );
      return jobCardStep;
   }


   private JobCardDefinition
         addStepsWithDuplicateSkillToJobCardDefinition( JobCardDefinition jobCardDefinition ) {
      List<JobCardStep> jobcardStepList = new ArrayList<JobCardStep>();
      JobCardStep step = setJobCardStep1();
      List<CertifyingSkill> certifyingSkills = new ArrayList<CertifyingSkill>();
      CertifyingSkill certifyingEngSkill = new CertifyingSkill();
      certifyingEngSkill.setLaborSkill( LABOR_SKILL_CD2 );
      certifyingEngSkill.setRequireInspection( true );
      certifyingSkills.add( certifyingEngSkill );
      certifyingSkills.add( certifyingEngSkill );
      step.setCertifyingSkills( certifyingSkills );
      jobcardStepList.add( step );
      jobCardDefinition.setJobCardSteps( jobcardStepList );
      return jobCardDefinition;
   }


   private JobCardDefinition addStepsWithSkillRequireInspectionFalseToJobCardDefinition(
         JobCardDefinition jobCardDefinition ) {
      List<JobCardStep> jobcardStepList = new ArrayList<JobCardStep>();
      jobcardStepList.add( addSkillRequireInspectionFalseTojobcardStep( setJobCardStep1() ) );
      jobcardStepList.add( setJobCardStep2() );
      jobCardDefinition.setJobCardSteps( jobcardStepList );
      return jobCardDefinition;
   }


   private JobCardStep addSkillRequireInspectionFalseTojobcardStep( JobCardStep jobCardStep ) {
      List<CertifyingSkill> certifyingSkills = new ArrayList<CertifyingSkill>();

      CertifyingSkill certifyingPilotSkill = new CertifyingSkill();
      certifyingPilotSkill.setLaborSkill( LABOR_SKILL_CD1 );
      certifyingPilotSkill.setRequireInspection( false );

      certifyingSkills.add( certifyingPilotSkill );
      jobCardStep.setCertifyingSkills( certifyingSkills );
      return jobCardStep;
   }


   private JobCardDefinition
         addStepsWithInvalidSkillToJobCardDefinition( JobCardDefinition jobCardDefinition ) {
      List<JobCardStep> jobcardStepList = new ArrayList<JobCardStep>();
      JobCardStep step = setJobCardStep1();
      List<CertifyingSkill> certifyingSkills = new ArrayList<CertifyingSkill>();
      CertifyingSkill certifyingEngSkill = new CertifyingSkill();
      certifyingEngSkill.setLaborSkill( INVALID_LABOR_SKILL_CD );
      certifyingEngSkill.setRequireInspection( true );
      certifyingSkills.add( certifyingEngSkill );
      step.setCertifyingSkills( certifyingSkills );
      jobcardStepList.add( step );
      jobCardDefinition.setJobCardSteps( jobcardStepList );
      return jobCardDefinition;
   }


   private JobCardDefinition addLabourRequirementsWithInvalidSkillToJobCardDefinition(
         JobCardDefinition jobCardDefinition ) {
      List<JobCardStep> jobcardStepList = new ArrayList<JobCardStep>();
      jobcardStepList.add( addLabourRequirementsWithInvalidSkillToStep( setJobCardStep1() ) );
      jobcardStepList.add( setJobCardStep2() );
      jobCardDefinition.setJobCardSteps( jobcardStepList );
      return jobCardDefinition;
   }


   private JobCardStep addLabourRequirementsWithInvalidSkillToStep( JobCardStep jobCardStep ) {
      List<CertifyingSkill> certifyingSkills = new ArrayList<CertifyingSkill>();

      CertifyingSkill certifyingPilotSkill = new CertifyingSkill();
      certifyingPilotSkill.setLaborSkill( INVALID_LABOR_SKILL_CD );
      certifyingPilotSkill.setRequireInspection( false );
      certifyingSkills.add( certifyingPilotSkill );
      jobCardStep.setCertifyingSkills( certifyingSkills );
      return jobCardStep;
   }


   private JobCardStep setJobCardStep1() {
      JobCardStep jobCardStep = new JobCardStep();
      jobCardStep.setOrder( STEP_ORDER1 );
      jobCardStep.setDescription( STEP_DESCRIPTION1 );
      jobCardStep.setApplicabilityRange( APPLICABILITY_RANGE1 );
      return jobCardStep;
   }


   private JobCardStep setJobCardStep2() {
      JobCardStep jobCardStep = new JobCardStep();
      jobCardStep.setOrder( STEP_ORDER2 );
      jobCardStep.setDescription( STEP_DESCRIPTION2 );
      jobCardStep.setApplicabilityRange( APPLICABILITY_RANGE2 );
      return jobCardStep;
   }


   private JobCardStep setJobCardStep3() {
      JobCardStep jobCardStep = new JobCardStep();
      jobCardStep.setOrder( STEP_ORDER3 );
      jobCardStep.setDescription( STEP_DESCRIPTION3 );
      jobCardStep.setApplicabilityRange( APPLICABILITY_RANGE3 );
      return jobCardStep;
   }


   private JobCardStep setJobCardStep4() {
      JobCardStep jobCardStep = new JobCardStep();
      jobCardStep.setOrder( STEP_ORDER4 );
      jobCardStep.setDescription( STEP_DESCRIPTION4 );
      jobCardStep.setApplicabilityRange( APPLICABILITY_RANGE4 );
      return jobCardStep;
   }


   private void assertObjectsEquals( JobCardDefinition expectedJobCardDefinition,
         JobCardDefinition actualJobCardDefinition ) {
      assertEquals( expectedJobCardDefinition.getCode(), actualJobCardDefinition.getCode() );
      assertEquals( expectedJobCardDefinition.getName(), actualJobCardDefinition.getName() );
      assertEquals( expectedJobCardDefinition.getDescription(),
            actualJobCardDefinition.getDescription() );
      assertEquals( expectedJobCardDefinition.getInstructions(),
            actualJobCardDefinition.getInstructions() );
      assertEquals( expectedJobCardDefinition.getDocumentReference(),
            actualJobCardDefinition.getDocumentReference() );
      assertEquals( expectedJobCardDefinition.getConfigSlotId(),
            actualJobCardDefinition.getConfigSlotId() );
      assertEquals( expectedJobCardDefinition.getAssemblyId(),
            actualJobCardDefinition.getAssemblyId() );
      assertEquals( expectedJobCardDefinition.getPartNoOem(),
            actualJobCardDefinition.getPartNoOem() );

      assertEquals( true,
            CollectionUtils.isEqualCollection(
                  ( expectedJobCardDefinition.getZoneIds() == null ) ? new ArrayList<String>()
                        : expectedJobCardDefinition.getZoneIds(),
                  ( actualJobCardDefinition.getZoneIds() == null ) ? new ArrayList<String>()
                        : actualJobCardDefinition.getZoneIds() ) );

      assertEquals( true,
            CollectionUtils.isEqualCollection(
                  ( expectedJobCardDefinition.getPanelIds() == null ) ? new ArrayList<String>()
                        : expectedJobCardDefinition.getPanelIds(),
                  ( actualJobCardDefinition.getPanelIds() == null ) ? new ArrayList<String>()
                        : actualJobCardDefinition.getPanelIds() ) );

      assertEquals(
            ( expectedJobCardDefinition.getJobCardSteps() == null ) ? 0
                  : expectedJobCardDefinition.getJobCardSteps().size(),
            ( actualJobCardDefinition.getJobCardSteps() == null ) ? 0
                  : actualJobCardDefinition.getJobCardSteps().size() );

      if ( CollectionUtils.isNotEmpty( expectedJobCardDefinition.getJobCardSteps() )
            && CollectionUtils.isNotEmpty( actualJobCardDefinition.getJobCardSteps() ) ) {

         for ( int i = 0; i < expectedJobCardDefinition.getJobCardSteps().size(); i++ ) {

            assertObjectStepArraysEquals( expectedJobCardDefinition.getJobCardSteps().get( i ),
                  actualJobCardDefinition.getJobCardSteps().get( i ) );
            assertObjectStepSkillsEquals(
                  expectedJobCardDefinition.getJobCardSteps().get( i ).getCertifyingSkills(),
                  actualJobCardDefinition.getJobCardSteps().get( i ).getCertifyingSkills() );
         }
      }

   }


   private void assertObjectStepArraysEquals( JobCardStep expectedStep, JobCardStep actualStep ) {
      assertEquals( expectedStep.getOrder(), actualStep.getOrder() );
      assertEquals( expectedStep.getDescription(), actualStep.getDescription() );
      assertEquals( expectedStep.getApplicabilityRange(), actualStep.getApplicabilityRange() );
   }


   private void assertObjectStepSkillsEquals( List<CertifyingSkill> expectedCertifyingSkills,
         List<CertifyingSkill> actualCertifyingSkills ) {
      if ( CollectionUtils.isNotEmpty( expectedCertifyingSkills )
            && CollectionUtils.isNotEmpty( actualCertifyingSkills ) ) {

         assertEquals( expectedCertifyingSkills.size(), actualCertifyingSkills.size() );
         for ( int i = 0; i < expectedCertifyingSkills.size(); i++ ) {
            assertEquals( expectedCertifyingSkills.get( i ).getLaborSkill(),
                  actualCertifyingSkills.get( i ).getLaborSkill() );
            assertEquals( expectedCertifyingSkills.get( i ).isRequireInspection(),
                  actualCertifyingSkills.get( i ).isRequireInspection() );
         }

      }

   }


   /**
    *
    * Get config slot based job card object for testing
    *
    * @return JobCardDefinition
    *
    */
   private JobCardDefinition getConfigSlotBasedJobCardDefinitionForPost() {
      JobCardDefinition jobCardDefinition = new JobCardDefinition();
      jobCardDefinition.setCode( JOB_CODE4 );
      jobCardDefinition.setName( JOB_NAME4 );
      jobCardDefinition.setConfigSlotId( CONFIG_SLOT_ID );
      jobCardDefinition.setAssemblyId( ASSEMBLY_ID );
      jobCardDefinition.setTaskClass( TASK_CLASS );
      jobCardDefinition.setOrganization( ORGANIZATION );
      jobCardDefinition.setStatus( STATUS_BUILD );
      return jobCardDefinition;
   }


   /**
    * Get part number based job card
    *
    * @return JobCardDefinition
    *
    */
   private JobCardDefinition getPartNumBasedJobCardDefinitionForPost() {
      JobCardDefinition jobCardDefinition = new JobCardDefinition();
      jobCardDefinition.setCode( JOB_CODE6 );
      jobCardDefinition.setName( JOB_NAME6 );
      jobCardDefinition.setPartId( PART_ID_2 );
      jobCardDefinition.setPartNoOem( PART_NO_OEM_2 );
      jobCardDefinition.setTaskClass( TASK_CLASS );
      jobCardDefinition.setOrganization( ORGANIZATION );
      jobCardDefinition.setStatus( STATUS_BUILD );
      return jobCardDefinition;
   }


   /**
    *
    * Get part number based job card revision object for testing
    *
    * @return JobCardDefinition
    *
    */
   private JobCardDefinition getPartNumBasedJobCardRevisionForPost() {
      JobCardDefinition jobCardDefinition = new JobCardDefinition();
      jobCardDefinition.setCode( JOB_CODE5 );
      jobCardDefinition.setName( JOB_NAME5 );
      jobCardDefinition.setDescription( DESCRIPTION );
      jobCardDefinition.setTaskClass( TASK_CLASS );
      jobCardDefinition.setOrganization( ORGANIZATION );
      jobCardDefinition.setStatus( STATUS_REVISION );
      jobCardDefinition.setPartNoOem( PART_NO_OEM );
      jobCardDefinition.setJobCardDefinitionId( ID5 );
      return jobCardDefinition;
   }

}

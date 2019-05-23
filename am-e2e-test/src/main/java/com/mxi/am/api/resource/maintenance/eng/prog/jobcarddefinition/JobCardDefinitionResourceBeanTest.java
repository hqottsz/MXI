package com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.labourrequirement.LabourRequirement;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.step.CertifyingSkill;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.step.JobCardStep;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Job Card Definition API e2e Test
 *
 */
public class JobCardDefinitionResourceBeanTest {

   private final String iApplicationJson = "application/json";
   private final String iJobCardDefinitionPath = "/amapi/" + JobCardDefinition.PATH;
   static final String JSON_FILE_PATH =
         "/am-e2e-test/src/main/java/com/mxi/am/api/resource/maintenance/eng/prog/jobcarddefinition/";

   // JobCardDefinition Data
   private String iUniqueId = null;
   private String iPartNoOem1 = null;
   private String iOrgCode1 = "MXI";
   private String iAssemblyCode1 = "ACFT_CD1";
   private String iAssemblyBomCode1 = "SYS-1-1";
   private String iTaskCode1 = "SYS-JIC-1";
   private String iTaskDefCode1 = "APU-TRK-SOFTWARE-JIC-4";
   private String iZoneCode1 = "ZONE1-1";
   private String iPanelCode1 = "P1-1-001";
   private String ilaborSkillCode1 = "ENG";

   private String iJobCardDescription2 = "JobCardCreatedDescription1";
   private String iJobCardDescriptionModified = "JobCardDescriptionModified";
   private String iJobCardDocumentRefModified = "JobCardDocumentReferenceModified";
   private DatabaseDriver iDriver;

   private String iJobCardDefinitionAltId1;
   private String iJobCardDefinitionName1;
   private String iJobCardDefinitionDocumentRef1;
   private String iJobCardDefinitionDescription1;
   private String iJobCardDefinitionInstructions1;
   private String iJobCardDefinitionStatusCode1;
   private String iJobCardDefinitionExternalRef1;
   private String iJobCardDefinitionTaskClassCd1;
   private String iJobCardDefinitionAssemblyId1;
   private String iJobCardDefinitionId1;
   private String iPartNoId1 = null;
   private String iOrganizationId1;
   private String iConfigurationSlotId1;
   private List<String> iZoneIdList = new ArrayList<String>();
   private List<String> iPanelIdList = new ArrayList<String>();

   private Integer iJobCardDefinitionRevisionNo1 = 1;
   private Boolean iJobCardDefinitionLockedBool1 = false;
   private Integer iStepOrder1 = 1;
   private String iStepDescription1 = "Step 1";
   private String iApplicabilityRange1 = null;
   private Integer iStepOrder2 = 2;
   private String iStepDescription2 = "Step 2";


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Before
   public void setUpData() throws Exception {

      iDriver = new AssetManagementDatabaseDriverProvider().get();

      Result lJobCardDefinitionIdResult = iDriver.select(
            "SELECT alt_id, task_name,task_ref_sdesc, task_ldesc, instruction_ldesc, task_def_status_cd, revision_ord, ext_key_sdesc, task_class_cd, locked_bool FROM task_task WHERE assmbl_cd = ? AND task_cd = ?",
            iAssemblyCode1, iTaskCode1 );

      Result lConfigurationSlotIdResult = iDriver.select(
            "SELECT alt_id FROM eqp_assmbl_bom WHERE assmbl_cd = ? AND assmbl_bom_cd = ?",
            iAssemblyCode1, iAssemblyBomCode1 );

      Result lAssemblyIdResult =
            iDriver.select( "SELECT alt_id FROM eqp_assmbl WHERE assmbl_cd = ?", iAssemblyCode1 );

      Result lOrgCodeResult =
            iDriver.select( "SELECT alt_id FROM org_org WHERE org_cd = ?", iOrgCode1 );

      Result lTaskDefinitionResult = iDriver.select(
            "select task_defn.alt_id FROM task_defn INNER JOIN task_task ON "
                  + "task_task.task_defn_db_id=task_defn.task_defn_db_id AND "
                  + "task_task.task_defn_id=task_defn.task_defn_id WHERE task_task.task_cd=?",
            iTaskDefCode1 );

      Result lZoneResult =
            iDriver.select( "SELECT alt_id FROM eqp_task_zone WHERE zone_cd = ? AND assmbl_cd =?",
                  iZoneCode1, iAssemblyCode1 );

      Result lPanelResult = iDriver.select(
            "SELECT eqp_task_panel.alt_id FROM eqp_task_panel LEFT OUTER JOIN eqp_task_zone ON eqp_task_panel.zone_db_id =  eqp_task_zone.zone_db_id AND eqp_task_panel.zone_id = eqp_task_zone.zone_id WHERE panel_cd=? and eqp_task_zone.zone_cd=?",
            iPanelCode1, iZoneCode1 );

      if ( lJobCardDefinitionIdResult.isEmpty() ) {
         fail( "Could not find the Job Card Definition details of " + iTaskCode1 );
      }

      if ( lConfigurationSlotIdResult.isEmpty() ) {
         fail( "Could not find the configuration slot's id of " + iAssemblyCode1 + ":"
               + iAssemblyBomCode1 );
      }

      if ( lConfigurationSlotIdResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + lConfigurationSlotIdResult.getNumberOfRows() );
      }

      if ( lAssemblyIdResult.isEmpty() ) {
         fail( "Could not find the Assembly id of " + iAssemblyCode1 );
      }

      if ( lAssemblyIdResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + lAssemblyIdResult.getNumberOfRows() );
      }
      if ( StringUtils.isNotBlank( iPartNoOem1 ) ) {
         Result lPartNoResult = iDriver
               .select( "SELECT alt_id FROM eqp_part_no WHERE part_no_oem = ?", iPartNoOem1 );

         if ( lPartNoResult.getNumberOfRows() > 1 ) {
            fail( "Expecting a single row for part no but found "
                  + lPartNoResult.getNumberOfRows() );
         }
         iPartNoId1 = lPartNoResult.get( 0 ).getUuidString( "alt_id" );
      }
      if ( lOrgCodeResult.isEmpty() ) {
         fail( "Could not find the organization: " + iOrgCode1 );
      }

      if ( lOrgCodeResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row for organization but found "
               + lOrgCodeResult.getNumberOfRows() );
      }

      if ( lTaskDefinitionResult.isEmpty() ) {
         fail( "Could not find the Task Definition id of " + iTaskDefCode1 );
      }

      if ( lTaskDefinitionResult.getNumberOfRows() > 1 ) {
         fail( "Expecting a single row but found " + lAssemblyIdResult.getNumberOfRows() );
      }

      iJobCardDefinitionAltId1 = lJobCardDefinitionIdResult.get( 0 ).getUuidString( "alt_id" );
      iJobCardDefinitionName1 = lJobCardDefinitionIdResult.get( 0 ).get( "task_name" );
      iJobCardDefinitionDocumentRef1 = lJobCardDefinitionIdResult.get( 0 ).get( "task_ref_sdesc" );
      iJobCardDefinitionDescription1 = lJobCardDefinitionIdResult.get( 0 ).get( "task_ldesc" );
      iJobCardDefinitionInstructions1 =
            lJobCardDefinitionIdResult.get( 0 ).get( "instruction_ldesc" );
      iJobCardDefinitionStatusCode1 =
            lJobCardDefinitionIdResult.get( 0 ).get( "task_def_status_cd" );
      iJobCardDefinitionExternalRef1 = lJobCardDefinitionIdResult.get( 0 ).get( "ext_key_sdesc" );
      iJobCardDefinitionTaskClassCd1 = lJobCardDefinitionIdResult.get( 0 ).get( "task_class_cd" );
      iJobCardDefinitionAssemblyId1 = lAssemblyIdResult.get( 0 ).getUuidString( "alt_id" );
      iConfigurationSlotId1 = lConfigurationSlotIdResult.get( 0 ).getUuidString( "alt_id" );
      iOrganizationId1 = lOrgCodeResult.get( 0 ).getUuidString( "alt_id" );
      iJobCardDefinitionId1 = lTaskDefinitionResult.get( 0 ).getUuidString( "alt_id" );
      iZoneIdList.clear();
      for ( int i = 0; i < lZoneResult.getNumberOfRows(); i++ ) {
         iZoneIdList.add( lZoneResult.get( i ).getUuidString( "alt_id" ) );
      }
      iPanelIdList.clear();
      for ( int i = 0; i < lPanelResult.getNumberOfRows(); i++ ) {
         iPanelIdList.add( lPanelResult.get( i ).getUuidString( "alt_id" ) );
      }

   }


   @Test
   public void testGetJobCardByIdSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response lResponse = getJobCardDefinitionById( 200, Credentials.AUTHENTICATED,
            iJobCardDefinitionAltId1, iApplicationJson, iApplicationJson );
      assertJobCardDefinition( defaultJobCardDefinitionBuilder(), lResponse );
   }


   @Test
   public void testGetJobCardByIdAuthenticationFailure401() {
      getJobCardDefinitionById( 401, Credentials.UNAUTHENTICATED, iJobCardDefinitionAltId1,
            iApplicationJson, iApplicationJson );
   }


   @Test
   public void testGetJobCardByIdUnauthorized403() {
      getJobCardDefinitionById( 403, Credentials.UNAUTHORIZED, iJobCardDefinitionAltId1,
            iApplicationJson, iApplicationJson );
   }


   @Test
   public void testSearchJobCardByQueryParmSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      JobCardDefinition lJobCardDefinition = setupDataForCreateJobCardDefinition();

      Response lResponse = createJobCardDefinition( 200, Credentials.AUTHENTICATED,
            lJobCardDefinition, iApplicationJson, iApplicationJson );

      JobCardDefinition lJobCardDefinitionCreated =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
                  .readValue( lResponse.getBody().asString(), JobCardDefinition.class );

      lResponse = searchJobCardDefinition( 200, Credentials.AUTHENTICATED,
            lJobCardDefinitionCreated.getCode(), iAssemblyBomCode1, iAssemblyCode1, null, null,
            Integer.valueOf( lJobCardDefinitionCreated.getRevisionNo() ), iApplicationJson,
            iApplicationJson );
      assertJobCardDefinitionForSearch( lJobCardDefinitionCreated, lResponse );
   }


   @Test
   public void testSearchJobCardByQueryParmFailure412() {
      searchJobCardDefinition( 412, Credentials.AUTHENTICATED, "AAAAAAAAAAAAA", null, null, null,
            null, null, iApplicationJson, iApplicationJson );
   }


   @Test
   public void testSearchJobCardByQueryParmAuthenticationFailure401() {
      searchJobCardDefinition( 401, Credentials.UNAUTHENTICATED, null, null, null, null, null, null,
            iApplicationJson, iApplicationJson );
   }


   @Test
   public void testSearchJobCardByQueryParmUnauthorized403() {
      searchJobCardDefinition( 403, Credentials.UNAUTHORIZED, null, null, null, null, null, null,
            iApplicationJson, iApplicationJson );
   }


   @Test
   public void testCreateJobCardDefinitionSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      JobCardDefinition lJobCardDefinition = setupDataForCreateJobCardDefinition();

      Response lResponse = createJobCardDefinition( 200, Credentials.AUTHENTICATED,
            lJobCardDefinition, iApplicationJson, iApplicationJson );

      JobCardDefinition lJobCardDefinitionCreated =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
                  .readValue( lResponse.getBody().asString(), JobCardDefinition.class );
      lJobCardDefinition.setId( lJobCardDefinitionCreated.getId() );
      lJobCardDefinition
            .setJobCardDefinitionId( lJobCardDefinitionCreated.getJobCardDefinitionId() );
      assertObjectsEquals( lJobCardDefinition, lJobCardDefinitionCreated );
   }


   @Test
   public void testCreateJobCardDefinitionAuthenticationFailure401() {
      JobCardDefinition lJobCardDefinition = setupDataForCreateJobCardDefinition();
      createJobCardDefinition( 401, Credentials.UNAUTHENTICATED, lJobCardDefinition,
            iApplicationJson, iApplicationJson );
   }


   @Test
   public void testUpdateJobCardDefinitionSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      JobCardDefinition lJobCardDefinition = new JobCardDefinition();
      lJobCardDefinition = setupDataForUpdateJobCardDefinition();
      Response lResponse = updateJobCardDefinitionById( 200, Credentials.AUTHENTICATED,
            lJobCardDefinition.getId(), lJobCardDefinition, iApplicationJson, iApplicationJson );
      assertJobCardDefinition( lJobCardDefinition, lResponse );
   }


   @Test
   public void testUpdateJobCardDefinitionAuthenticationFailure401()
         throws JsonParseException, JsonMappingException, IOException {
      JobCardDefinition lJobCardDefinition = new JobCardDefinition();
      lJobCardDefinition = setupDataForUpdateJobCardDefinition();
      updateJobCardDefinitionById( 401, Credentials.UNAUTHENTICATED, lJobCardDefinition.getId(),
            lJobCardDefinition, iApplicationJson, iApplicationJson );
   }


   @Test
   public void testUpdateJobCardDefinitionAuthorizationFailure403()
         throws JsonParseException, JsonMappingException, IOException {
      JobCardDefinition lJobCardDefinition = new JobCardDefinition();
      lJobCardDefinition = setupDataForUpdateJobCardDefinition();
      updateJobCardDefinitionById( 403, Credentials.UNAUTHORIZED, lJobCardDefinition.getId(),
            lJobCardDefinition, iApplicationJson, iApplicationJson );
   }


   private Response getJobCardDefinitionById( int aStatusCode, Credentials aSecurity,
         String aJobCardDefinitionId, String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();
      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).expect().statusCode( aStatusCode )
            .when().get( iJobCardDefinitionPath + "/" + aJobCardDefinitionId );

      return lResponse;
   }


   private Response searchJobCardDefinition( int aStatusCode, Credentials aSecurity,
         String aTaskCode, String aConfigSlotCode, String aAssemblyCode, String aExternalReference,
         String aPartNoOem, Integer aLatestRevision, String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();
      Response lResponse;
      if ( aStatusCode == 200 ) {
         lResponse =
               RestAssured.given().queryParam( JobCardDefinitionResource.TASK_CD_PARAM, aTaskCode )
                     .queryParam( JobCardDefinitionResource.CONFIG_SLOT_CD_PARAM, aConfigSlotCode )
                     .queryParam( JobCardDefinitionResource.ASSEMBLY_CD_PARAM, aAssemblyCode )
                     .queryParam( JobCardDefinitionResource.LATEST_REVISION, aLatestRevision )
                     .auth().preemptive().basic( lUserName, lPassword ).accept( aAcceptType )
                     .contentType( aContentType ).expect().statusCode( aStatusCode ).when()
                     .get( iJobCardDefinitionPath );
      } else {
         lResponse = RestAssured.given()
               .queryParam( JobCardDefinitionResource.TASK_CD_PARAM, aTaskCode )
               .queryParam( JobCardDefinitionResource.CONFIG_SLOT_CD_PARAM, aConfigSlotCode )
               .queryParam( JobCardDefinitionResource.ASSEMBLY_CD_PARAM, aAssemblyCode )
               .queryParam( JobCardDefinitionResource.EXTERNAL_REFERENCE_PARAM, aExternalReference )
               .queryParam( JobCardDefinitionResource.PART_NO_OEM_PARAM, aPartNoOem )
               .queryParam( JobCardDefinitionResource.LATEST_REVISION, aLatestRevision ).auth()
               .preemptive().basic( lUserName, lPassword ).accept( aAcceptType )
               .contentType( aContentType ).expect().statusCode( aStatusCode ).when()
               .get( iJobCardDefinitionPath );
      }
      return lResponse;
   }


   private Response updateJobCardDefinitionById( int aStatusCode, Credentials aSecurity,
         String aJobCardDefinitionId, JobCardDefinition aJobCardDefinitionModified,
         String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();
      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).body( aJobCardDefinitionModified )
            .expect().statusCode( aStatusCode ).when()
            .put( iJobCardDefinitionPath + "/" + aJobCardDefinitionId );
      return lResponse;
   }


   private void assertJobCardDefinition( JobCardDefinition aJobCardDefinitionExpected,
         Response aActual ) throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      JobCardDefinition lJCDActual = lObjectMapper.readValue( aActual.getBody().asString(),
            lObjectMapper.getTypeFactory().constructType( JobCardDefinition.class ) );
      assertObjectsEquals( aJobCardDefinitionExpected, lJCDActual );
   }


   private void assertJobCardDefinitionForSearch( JobCardDefinition aJobCardDefinitionExpected,
         Response aActual ) throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper lObjectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<JobCardDefinition> lJobCardDefinitionsActual =
            lObjectMapper.readValue( aActual.getBody().asString(), lObjectMapper.getTypeFactory()
                  .constructCollectionType( List.class, JobCardDefinition.class ) );
      Assert.assertEquals( lJobCardDefinitionsActual.size(), 1 );
      assertObjectsEquals( aJobCardDefinitionExpected, lJobCardDefinitionsActual.get( 0 ) );
   }


   private JobCardDefinition addZonesToJobCardDefinition( JobCardDefinition aJobCardDefinition ) {
      aJobCardDefinition.setZoneIds( iZoneIdList );
      return aJobCardDefinition;
   }


   private JobCardDefinition addPanelsToJobCardDefinition( JobCardDefinition aJobCardDefinition ) {
      aJobCardDefinition.setPanelIds( iPanelIdList );
      return aJobCardDefinition;
   }


   private JobCardDefinition addStepsToJobCardDefinition( JobCardDefinition aJobCardDefinition ) {
      List<JobCardStep> lJobcardStepList = new ArrayList<JobCardStep>();
      JobCardStep lJobCardStep1 = new JobCardStep();
      JobCardStep lJobCardStep2 = new JobCardStep();
      List<CertifyingSkill> lCertifyingSkills = new ArrayList<CertifyingSkill>();
      lJobCardStep1 = setJobCardStep1();
      lJobCardStep1.setCertifyingSkills( lCertifyingSkills );
      lJobcardStepList.add( lJobCardStep1 );
      lJobCardStep2 = setJobCardStep2();
      lJobCardStep2.setCertifyingSkills( lCertifyingSkills );
      lJobcardStepList.add( lJobCardStep2 );
      aJobCardDefinition.setJobCardSteps( lJobcardStepList );
      return aJobCardDefinition;
   }


   private JobCardStep setJobCardStep1() {
      JobCardStep lJobCardStep = new JobCardStep();
      lJobCardStep.setOrder( iStepOrder1 );
      lJobCardStep.setDescription( iStepDescription1 );
      lJobCardStep.setApplicabilityRange( iApplicabilityRange1 );
      return lJobCardStep;
   }


   private JobCardDefinition
         addLabourRequirementToJobCardDefinition( JobCardDefinition aJobCardDefinition ) {

      List<LabourRequirement> lLabourRequirementList = new ArrayList<LabourRequirement>();
      LabourRequirement lLabourRequirement = new LabourRequirement();

      lLabourRequirement.setLaborSkill( ilaborSkillCode1 );
      lLabourRequirement.setRequireCertification( true );
      lLabourRequirement.setRequireInspection( true );
      lLabourRequirementList.add( lLabourRequirement );

      aJobCardDefinition.setLabourRequirements( lLabourRequirementList );
      return aJobCardDefinition;
   }


   private JobCardStep setJobCardStep2() {
      JobCardStep lJobCardStep = new JobCardStep();
      lJobCardStep.setOrder( iStepOrder2 );
      lJobCardStep.setDescription( iStepDescription2 );
      lJobCardStep.setApplicabilityRange( iApplicabilityRange1 );
      return lJobCardStep;
   }


   private JobCardDefinition
         getConfigSlotBasedDefaultJobCardDefinitionDetails( JobCardDefinition aJobCardDefinition ) {
      aJobCardDefinition.setId( iJobCardDefinitionAltId1 );
      aJobCardDefinition.setCode( iTaskCode1 );
      aJobCardDefinition.setName( iJobCardDefinitionName1 );
      aJobCardDefinition.setDescription( iJobCardDefinitionDescription1 );
      aJobCardDefinition.setConfigSlotId( iConfigurationSlotId1 );
      aJobCardDefinition.setAssemblyId( iJobCardDefinitionAssemblyId1 );
      aJobCardDefinition.setPartNoOem( iPartNoOem1 );
      aJobCardDefinition.setLocked( iJobCardDefinitionLockedBool1 );
      aJobCardDefinition.setDocumentReference( iJobCardDefinitionDocumentRef1 );
      aJobCardDefinition.setExternalReference( iJobCardDefinitionExternalRef1 );
      aJobCardDefinition.setInstructions( iJobCardDefinitionInstructions1 );
      aJobCardDefinition.setJobCardDefinitionId( iJobCardDefinitionId1 );
      aJobCardDefinition.setOrganization( iOrganizationId1 );
      aJobCardDefinition.setPartId( iPartNoId1 );
      aJobCardDefinition.setRevisionNo( iJobCardDefinitionRevisionNo1.toString() );
      aJobCardDefinition.setStatus( iJobCardDefinitionStatusCode1 );
      aJobCardDefinition.setTaskClass( iJobCardDefinitionTaskClassCd1 );
      return aJobCardDefinition;
   }


   private JobCardDefinition defaultJobCardDefinitionBuilder() {
      JobCardDefinition lJobCardDefinition = new JobCardDefinition();
      getConfigSlotBasedDefaultJobCardDefinitionDetails( lJobCardDefinition );
      lJobCardDefinition = addZonesToJobCardDefinition( lJobCardDefinition );
      lJobCardDefinition = addPanelsToJobCardDefinition( lJobCardDefinition );
      lJobCardDefinition = addStepsToJobCardDefinition( lJobCardDefinition );
      lJobCardDefinition = addLabourRequirementToJobCardDefinition( lJobCardDefinition );
      return lJobCardDefinition;
   }


   private JobCardDefinition setupDataForCreateJobCardDefinition() {
      JobCardDefinition lJobCardDefinition = new JobCardDefinition();
      getConfigSlotBasedDefaultJobCardDefinitionDetails( lJobCardDefinition );
      lJobCardDefinition.setId( null );
      iUniqueId = UUID.randomUUID().toString().toUpperCase();
      lJobCardDefinition.setCode( iUniqueId );
      lJobCardDefinition.setName( iUniqueId );
      lJobCardDefinition.setDescription( iJobCardDescription2 );
      lJobCardDefinition.setStatus( "BUILD" );
      lJobCardDefinition.setJobCardDefinitionId( null );
      return lJobCardDefinition;
   }


   private JobCardDefinition setupDataForUpdateJobCardDefinition()
         throws JsonParseException, JsonMappingException, IOException {
      JobCardDefinition lJobCardDefinition = setupDataForCreateJobCardDefinition();

      Response lResponse = createJobCardDefinition( 200, Credentials.AUTHENTICATED,
            lJobCardDefinition, iApplicationJson, iApplicationJson );

      JobCardDefinition lJobCardDefinitionCreated =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
                  .readValue( lResponse.getBody().asString(), JobCardDefinition.class );
      lJobCardDefinitionCreated.setDescription( iJobCardDescriptionModified );
      lJobCardDefinitionCreated.setDocumentReference( iJobCardDocumentRefModified );
      lJobCardDefinitionCreated = addPanelsToJobCardDefinition( lJobCardDefinitionCreated );
      lJobCardDefinitionCreated = addStepsToJobCardDefinition( lJobCardDefinitionCreated );
      return lJobCardDefinitionCreated;
   }


   private Response createJobCardDefinition( int aStatusCode, Credentials aSecurity,
         Object aJobCardDefinition, String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).body( aJobCardDefinition ).expect()
            .statusCode( aStatusCode ).when().post( iJobCardDefinitionPath );
      return lResponse;
   }


   private void assertObjectsEquals( JobCardDefinition aExpectedJobCardDefinition,
         JobCardDefinition aActualJobCardDefinition ) {
      assertEquals( aExpectedJobCardDefinition.getCode(), aActualJobCardDefinition.getCode() );
      assertEquals( aExpectedJobCardDefinition.getName(), aActualJobCardDefinition.getName() );
      assertEquals( aExpectedJobCardDefinition.getDescription(),
            aActualJobCardDefinition.getDescription() );
      assertEquals( aExpectedJobCardDefinition.getInstructions(),
            aActualJobCardDefinition.getInstructions() );
      assertEquals( aExpectedJobCardDefinition.getDocumentReference(),
            aActualJobCardDefinition.getDocumentReference() );
      assertEquals( aExpectedJobCardDefinition.getConfigSlotId(),
            aActualJobCardDefinition.getConfigSlotId() );
      assertEquals( aExpectedJobCardDefinition.getAssemblyId(),
            aActualJobCardDefinition.getAssemblyId() );
      assertEquals( aExpectedJobCardDefinition.getPartNoOem(),
            aActualJobCardDefinition.getPartNoOem() );

      assertEquals( true,
            CollectionUtils.isEqualCollection(
                  ( aExpectedJobCardDefinition.getZoneIds() == null ) ? new ArrayList<String>()
                        : aExpectedJobCardDefinition.getZoneIds(),
                  ( aActualJobCardDefinition.getZoneIds() == null ) ? new ArrayList<String>()
                        : aActualJobCardDefinition.getZoneIds() ) );

      assertEquals( true,
            CollectionUtils.isEqualCollection(
                  ( aExpectedJobCardDefinition.getPanelIds() == null ) ? new ArrayList<String>()
                        : aExpectedJobCardDefinition.getPanelIds(),
                  ( aActualJobCardDefinition.getPanelIds() == null ) ? new ArrayList<String>()
                        : aActualJobCardDefinition.getPanelIds() ) );

      assertEquals(
            ( aExpectedJobCardDefinition.getJobCardSteps() == null ) ? 0
                  : aExpectedJobCardDefinition.getJobCardSteps().size(),
            ( aActualJobCardDefinition.getJobCardSteps() == null ) ? 0
                  : aActualJobCardDefinition.getJobCardSteps().size() );

      if ( CollectionUtils.isNotEmpty( aExpectedJobCardDefinition.getJobCardSteps() )
            && CollectionUtils.isNotEmpty( aActualJobCardDefinition.getJobCardSteps() ) ) {

         for ( int i = 0; i < aExpectedJobCardDefinition.getJobCardSteps().size(); i++ ) {

            assertObjectStepArraysEquals( aExpectedJobCardDefinition.getJobCardSteps().get( i ),
                  aActualJobCardDefinition.getJobCardSteps().get( i ) );
            assertObjectStepSkillsEquals(
                  aExpectedJobCardDefinition.getJobCardSteps().get( i ).getCertifyingSkills(),
                  aActualJobCardDefinition.getJobCardSteps().get( i ).getCertifyingSkills() );
         }
      }
   }


   private void assertObjectStepArraysEquals( JobCardStep aExpectedStep, JobCardStep aActualStep ) {
      assertEquals( aExpectedStep.getOrder(), aActualStep.getOrder() );
      assertEquals( aExpectedStep.getDescription(), aActualStep.getDescription() );
      assertEquals( aExpectedStep.getApplicabilityRange(), aActualStep.getApplicabilityRange() );
   }


   private void assertObjectStepSkillsEquals( List<CertifyingSkill> aExpectedCertifyingSkills,
         List<CertifyingSkill> aActualCertifyingSkills ) {
      if ( CollectionUtils.isNotEmpty( aExpectedCertifyingSkills )
            && CollectionUtils.isNotEmpty( aActualCertifyingSkills ) ) {

         assertEquals( aExpectedCertifyingSkills.size(), aActualCertifyingSkills.size() );
         for ( int i = 0; i < aExpectedCertifyingSkills.size(); i++ ) {
            assertEquals( aExpectedCertifyingSkills.get( i ).getLaborSkill(),
                  aActualCertifyingSkills.get( i ).getLaborSkill() );
            assertEquals( aExpectedCertifyingSkills.get( i ).isRequireInspection(),
                  aActualCertifyingSkills.get( i ).isRequireInspection() );
         }
      }
   }

}

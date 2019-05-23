package com.mxi.am.api.resource.maintenance.exec.task.Step;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ejb.EJBContext;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.exec.task.step.SignOff;
import com.mxi.am.api.resource.maintenance.exec.task.step.Step;
import com.mxi.am.api.resource.maintenance.exec.task.step.StepResource;
import com.mxi.am.api.resource.maintenance.exec.task.step.Technician;
import com.mxi.am.api.resource.maintenance.exec.task.step.impl.StepResourceBean;
import com.mxi.am.api.util.Base64EncryptUtils;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for Step Resource Bean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class StepResourceBeanTest extends ResourceBeanTest {

   private static final String TASK_ID = "7EA350AA83914ED98AC6C8D120DB79E1";
   private static final String TASK_ID_UPDATE_WITHOUT_STEPKEY = "123450AA83914ED98AC6C8D120DB7000";
   private static final String TASK_ID_UPDATE_WITH_EMPTY_PAYLOAD =
         "00AC50AA83914ED98AC6C8D120DB7045";
   private static final String TASK_ID_UPDATE_LESS_STEPS = "AC7850AA83914ED98AC6C8D120DB7ACB";
   private static final String TASK_ID_UPDATE_ADD_REORDER_REMOVE =
         "AADD50AA80014ED98AC6C8D120DBEA11";
   private static final String TASK_ID_UPDATE_STEP_HISTORIC_TASK =
         "123450AA83914ED98AC6C8D120D001AA";
   private static final String TASK_ID_UPDATE_STEP_LOCKED_INVENTORY =
         "ACDEE0AA83914ED98AC6C8D120D00EED";
   private static final String STATUS_CODE_COMPLETE = "MXCOMPLETE";
   private static final String STATUS_CODE_PENDING = "MXPENDING";
   private static final String DESCRIPTION = "Open panels";
   private static final String DESCRIPTION2 = "Replace part";
   private static final String DESCRIPTION3 = "Step panels";
   private static final String DESCRIPTION4 = "Replace panel step";
   private static final String UPDATED_DESCRIPTION = "Updated Replace Part";
   private static final String DATE = "2007-01-11T05:00:00Z";
   private static final String NOTES = "note";
   private static final String NOTES2 = "note 2";
   private static final String USER_ID = "E5E5C041DA604CCBBD4353EFDA9EC000";
   private static final String KEY = "4650:1000:100";
   private static final String KEY2 = "4650:1000:101";
   private static final String KEY3 = "4650:1001:1";
   private static final String KEY5 = "4650:1006:1";
   private static final String KEY6 = "4650:1006:112";
   private static final String KEY7 = "4650:1006:113";
   private static final String KEY8 = "4650:1006:117";
   private static final String INVALID_TASK_ID = "XXX";
   private static final String INVALID_KEY = "XXXXX";

   public static final String AUTHORIZED_WITHOUT_REMOVE_PARM = "authorizedWithOutParm";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( StepResource.class ).to( StepResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
            }
         } );

   @Inject
   StepResourceBean stepResourceBean;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      stepResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void search_success_validTaskId() throws AmApiResourceNotFoundException, ParseException {
      List<Step> stepList = stepResourceBean.search( TASK_ID );
      assertStepList( buildStepList(), stepList );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void search_failure_noTaskId() throws AmApiResourceNotFoundException {
      stepResourceBean.search( "" );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void search_failure_notFoundTaskId() throws AmApiResourceNotFoundException {
      stepResourceBean.search( null );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void search_failure_invalidTaskId() throws AmApiResourceNotFoundException {
      stepResourceBean.search( INVALID_TASK_ID );
   }


   /**
    * This test case includes cases, update valid description, update step by passing valid key and
    * null description, reorder the steps, add new steps with out key and remove an existing step
    */
   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void update_success_addReorderRemoveUpdateDescription()
         throws ParseException, AmApiResourceNotFoundException {
      List<Step> steps = stepResourceBean.search( TASK_ID_UPDATE_ADD_REORDER_REMOVE );
      steps.remove( 2 );
      Optional<Step> stepOne = steps.stream().filter( o -> ( o.getOrderNumber() == 1 ) ).findAny();
      if ( stepOne.isPresent() ) {
         stepOne.get().setDescription( UPDATED_DESCRIPTION );
      }
      Optional<Step> stepTwo = steps.stream().filter( o -> ( o.getOrderNumber() == 2 ) ).findAny();
      if ( stepTwo.isPresent() ) {
         stepTwo.get().setDescription( null );
      }
      Collections.reverse( steps );
      List<Step> stepsNew = buildStepListForUpdate();
      stepsNew.get( 0 ).setOrderNumber( 4 );
      steps.addAll( stepsNew );
      List<Step> stepsUpdated = stepResourceBean.update( TASK_ID_UPDATE_ADD_REORDER_REMOVE, steps );

      assertStepList( buildStepListAddRemoveReorderSteps(), stepsUpdated );
   }


   /**
    * Pass only one step without key
    */
   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void update_success_stepPassedWithoutKey()
         throws ParseException, AmApiResourceNotFoundException {
      List<Step> steps = buildStepListForUpdate();
      List<Step> stepsUpdated = stepResourceBean.update( TASK_ID_UPDATE_WITHOUT_STEPKEY, steps );
      steps.get( 0 ).setKey( encrypt( KEY3 ) );
      assertStepList( steps, stepsUpdated );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void update_success_emptyStepList() throws AmApiResourceNotFoundException {
      List<Step> stepList = new ArrayList<Step>();
      List<Step> stepsUpdated =
            stepResourceBean.update( TASK_ID_UPDATE_WITH_EMPTY_PAYLOAD, stepList );
      assertStepList( stepList, stepsUpdated );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void update_success_lessNumberStepList() throws AmApiResourceNotFoundException {
      List<Step> stepList = stepResourceBean.search( TASK_ID_UPDATE_LESS_STEPS );
      stepList.remove( 2 );
      List<Step> stepUpdated = stepResourceBean.update( TASK_ID_UPDATE_LESS_STEPS, stepList );
      assertStepList( stepList, stepUpdated );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void update_failure_notFoundTaskId()
         throws AmApiResourceNotFoundException, ParseException {
      stepResourceBean.update( null, buildStepList() );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void update_failure_invalidTaskId()
         throws AmApiResourceNotFoundException, ParseException {
      stepResourceBean.update( INVALID_TASK_ID, buildStepList() );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void update_failure_noStepPayload() throws AmApiResourceNotFoundException {
      stepResourceBean.update( TASK_ID, null );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void update_failure_validKeyAndEmptyDescription()
         throws AmApiResourceNotFoundException, ParseException {
      List<Step> stepUpdate = buildStepList();
      stepUpdate.get( 0 ).setDescription( "" );
      stepResourceBean.update( TASK_ID, stepUpdate );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void update_failure_invalidKey() throws AmApiResourceNotFoundException, ParseException {
      List<Step> stepUpdate = buildStepList();
      stepUpdate.get( 0 ).setKey( INVALID_KEY );
      stepUpdate.get( 0 ).setDescription( UPDATED_DESCRIPTION );
      stepResourceBean.update( TASK_ID, stepUpdate );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void update_failure_emptyStringKey()
         throws AmApiResourceNotFoundException, ParseException {
      List<Step> stepUpdate = buildStepList();
      stepUpdate.get( 0 ).setKey( "" );
      stepUpdate.get( 0 ).setDescription( UPDATED_DESCRIPTION );
      stepResourceBean.update( TASK_ID, stepUpdate );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void update_failure_removeStepsWithSighOffsParmDisabled()
         throws AmApiResourceNotFoundException, MxException {
      setAuthorizedUser( AUTHORIZED_WITHOUT_REMOVE_PARM );
      initializeSecurityContext();
      List<Step> stepList = new ArrayList<Step>();
      stepResourceBean.update( TASK_ID_UPDATE_WITH_EMPTY_PAYLOAD, stepList );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void update_failure_stepsForHistoricTask() throws AmApiResourceNotFoundException {
      List<Step> stepUpdate = stepResourceBean.search( TASK_ID_UPDATE_STEP_HISTORIC_TASK );
      stepUpdate.get( 0 ).setDescription( UPDATED_DESCRIPTION );
      stepResourceBean.update( TASK_ID_UPDATE_STEP_HISTORIC_TASK, stepUpdate );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void update_failure_stepForLockedInventory() throws AmApiResourceNotFoundException {
      List<Step> stepList = stepResourceBean.search( TASK_ID_UPDATE_STEP_LOCKED_INVENTORY );
      stepList.get( 0 ).setDescription( UPDATED_DESCRIPTION );
      stepResourceBean.update( TASK_ID_UPDATE_STEP_LOCKED_INVENTORY, stepList );
   }


   private void assertStepList( List<Step> expectedStepList, List<Step> actualStepList ) {

      assertEquals( "Number of steps mismatched", expectedStepList.size(), actualStepList.size() );

      for ( Step stepExpected : expectedStepList ) {
         Optional<Step> stepActual = actualStepList.stream()
               .filter( o -> o.getOrderNumber().equals( stepExpected.getOrderNumber() ) ).findAny();
         assertTrue( "No matching steps found for the order number", stepActual.isPresent() );
         assertEquals( "Step description mismatched", stepExpected.getDescription(),
               stepActual.get().getDescription() );
         assertEquals( "Step key mismatched", stepExpected.getKey(), stepActual.get().getKey() );
         assertEquals( "Step status code mismatched", stepExpected.getStatusCode(),
               stepActual.get().getStatusCode() );

         if ( CollectionUtils.isNotEmpty( stepExpected.getSignOff() )
               && CollectionUtils.isNotEmpty( stepActual.get().getSignOff() ) ) {
            assertEquals( "Number of signoff mismatched", stepExpected.getSignOff().size(),
                  stepActual.get().getSignOff().size() );

            for ( SignOff signOffExpected : stepExpected.getSignOff() ) {
               Optional<SignOff> signOffActual = stepActual.get().getSignOff().stream()
                     .filter( o -> o.getOrderNumber().equals( signOffExpected.getOrderNumber() ) )
                     .findAny();
               assertTrue( "No matching signOff found", signOffActual.isPresent() );
               assertEquals( "Status code of signOff mismatched", signOffExpected.getStatusCode(),
                     signOffActual.get().getStatusCode() );
               assertEquals( "Technician of signOff mismatched", signOffExpected.getTechnician(),
                     signOffActual.get().getTechnician() );
            }
         } else if ( CollectionUtils.isEmpty( stepExpected.getSignOff() ) ) {
            assertNull( "SignOff presents unexpectedly", stepActual.get().getSignOff() );
         } else {
            fail( "Expected signOff not found" );
         }
      }
   }


   private List<Step> buildStepList() throws ParseException {
      List<Step> stepList = new ArrayList<Step>();
      Step step1 = new Step();
      Step step2 = new Step();

      Technician technician1 = new Technician();
      Technician technician2 = new Technician();
      Date date = new SimpleDateFormat( "yyyy-MM-dd" ).parse( DATE );

      technician1.setDate( date );
      technician1.setNotes( NOTES );
      technician1.setUserId( USER_ID );

      technician2.setDate( date );
      technician2.setNotes( NOTES2 );
      technician2.setUserId( USER_ID );

      List<SignOff> signOffList1 = new ArrayList<SignOff>();
      SignOff signOff1 = new SignOff();
      signOff1.setOrderNumber( 1 );
      signOff1.setStatusCode( STATUS_CODE_COMPLETE );
      signOff1.setTechnician( technician1 );
      signOffList1.add( signOff1 );

      List<SignOff> signOffList2 = new ArrayList<SignOff>();
      SignOff signOff2 = new SignOff();
      signOff2.setOrderNumber( 1 );
      signOff2.setStatusCode( STATUS_CODE_COMPLETE );
      signOff2.setTechnician( technician2 );
      signOffList2.add( signOff2 );

      step1.setKey( encrypt( KEY ) );
      step1.setOrderNumber( 1 );
      step1.setStatusCode( STATUS_CODE_COMPLETE );
      step1.setDescription( DESCRIPTION );
      step1.setSignOff( signOffList1 );

      step2.setKey( encrypt( KEY2 ) );
      step2.setOrderNumber( 2 );
      step2.setStatusCode( STATUS_CODE_COMPLETE );
      step2.setDescription( DESCRIPTION2 );
      step2.setSignOff( signOffList2 );

      stepList.add( step1 );
      stepList.add( step2 );
      return stepList;
   }


   private List<Step> buildStepListAddRemoveReorderSteps() throws ParseException {
      List<Step> stepList = new ArrayList<Step>();
      Step step1 = new Step();
      Step step2 = new Step();
      Step step3 = new Step();
      Step step4 = new Step();

      Technician technician1 = new Technician();
      Technician technician2 = new Technician();
      Technician technician3 = new Technician();
      Date date = new SimpleDateFormat( "yyyy-MM-dd" ).parse( DATE );

      technician1.setDate( date );
      technician1.setNotes( NOTES );
      technician1.setUserId( USER_ID );

      technician2.setDate( date );
      technician2.setNotes( NOTES2 );
      technician2.setUserId( USER_ID );

      technician3.setDate( date );
      technician3.setNotes( NOTES );
      technician3.setUserId( USER_ID );

      List<SignOff> signOffList1 = new ArrayList<SignOff>();
      SignOff signOff1 = new SignOff();
      signOff1.setOrderNumber( 1 );
      signOff1.setStatusCode( STATUS_CODE_COMPLETE );
      signOff1.setTechnician( technician1 );
      signOffList1.add( signOff1 );

      List<SignOff> signOffList2 = new ArrayList<SignOff>();
      SignOff signOff2 = new SignOff();
      signOff2.setOrderNumber( 1 );
      signOff2.setStatusCode( STATUS_CODE_COMPLETE );
      signOff2.setTechnician( technician2 );
      signOffList2.add( signOff2 );

      List<SignOff> signOffList3 = new ArrayList<SignOff>();
      SignOff signOff3 = new SignOff();
      signOff3.setOrderNumber( 1 );
      signOff3.setStatusCode( STATUS_CODE_COMPLETE );
      signOff3.setTechnician( technician3 );
      signOffList3.add( signOff3 );

      step1.setKey( encrypt( KEY8 ) );
      step1.setOrderNumber( 1 );
      step1.setStatusCode( STATUS_CODE_COMPLETE );
      step1.setDescription( DESCRIPTION4 );
      step1.setSignOff( signOffList1 );

      step2.setKey( encrypt( KEY7 ) );
      step2.setOrderNumber( 2 );
      step2.setStatusCode( STATUS_CODE_COMPLETE );
      step2.setDescription( DESCRIPTION );
      step2.setSignOff( signOffList2 );

      step3.setKey( encrypt( KEY6 ) );
      step3.setOrderNumber( 3 );
      step3.setStatusCode( STATUS_CODE_COMPLETE );
      step3.setDescription( UPDATED_DESCRIPTION );
      step3.setSignOff( signOffList3 );

      step4.setKey( encrypt( KEY5 ) );
      step4.setOrderNumber( 4 );
      step4.setStatusCode( STATUS_CODE_PENDING );
      step4.setDescription( DESCRIPTION3 );

      stepList.add( step1 );
      stepList.add( step2 );
      stepList.add( step3 );
      stepList.add( step4 );
      return stepList;
   }


   private List<Step> buildStepListForUpdate() throws ParseException {
      List<Step> stepList = new ArrayList<Step>();
      Step step1 = new Step();

      Technician technician1 = new Technician();
      Date date = new SimpleDateFormat( "yyyy-MM-dd" ).parse( DATE );

      technician1.setDate( date );
      technician1.setNotes( NOTES );
      technician1.setUserId( USER_ID );

      step1.setOrderNumber( 1 );
      step1.setStatusCode( STATUS_CODE_PENDING );
      step1.setDescription( DESCRIPTION3 );

      stepList.add( step1 );
      return stepList;
   }


   private String encrypt( String stepKey ) {
      return Base64EncryptUtils.encryptValue( stepKey );
   }
}

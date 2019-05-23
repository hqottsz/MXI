package com.mxi.am.api.resource.maintenance.exec.task.historynote;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Database-level tests for the Task History Note API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class TaskHistoryNoteResourceBeanTest extends ResourceBeanTest {

   @Inject
   TaskHistoryNoteResourceBean histNoteResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException exception = ExpectedException.none();

   private static final SimpleDateFormat DATE_FORMAT =
         new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

   HistoryNote histNote1;
   HistoryNote histNote2;
   HistoryNote histNote3;


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      histNoteResourceBean.setEJBContext( ejbContext );
      constructExpectedResults();
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void get_success() throws AmApiResourceNotFoundException {
      HistoryNote note = histNoteResourceBean.get( histNote3.getId() );

      assertHistoryNote( histNote3, note );
   }


   @Test
   public void get_failure_noteNotFound() throws AmApiResourceNotFoundException {
      final String nonExistentId = "00000000000000000000000000000000";

      exception.expect( AmApiResourceNotFoundException.class );
      exception
            .expectMessage( String.format( "HISTORY NOTE ON TASK %s not found", nonExistentId ) );

      histNoteResourceBean.get( nonExistentId );
   }


   @Test
   public void get_failure_nullId() throws AmApiResourceNotFoundException {
      exception.expect( AmApiBadRequestException.class );
      exception.expectMessage( "The history note id parameter must not be null or empty." );

      histNoteResourceBean.get( null );
   }


   @Test
   public void get_failure_emptyId() throws AmApiResourceNotFoundException {
      exception.expect( AmApiBadRequestException.class );
      exception.expectMessage( "The history note id parameter must not be null or empty." );

      histNoteResourceBean.get( "" );
   }


   @Test
   @CSIContractTest( Project.SWA_WP_STATUS )
   public void search_success_byTaskId() {
      HistoryNoteSearchParameters params =
            new HistoryNoteSearchParameters().withTaskIds( Arrays.asList( histNote1.getTaskId() ) );

      List<HistoryNote> historyNotes = histNoteResourceBean.search( params );

      assertHistoryNotes( Arrays.asList( histNote1, histNote2 ), historyNotes );
   }


   @Test
   @CSIContractTest( Project.SWA_WP_STATUS )
   public void search_success_noResults() {
      HistoryNoteSearchParameters params = new HistoryNoteSearchParameters()
            .withTaskIds( Arrays.asList( "00000000000000000000000000000000" ) );

      List<HistoryNote> historyNotes = histNoteResourceBean.search( params );

      assertEquals( "Expected no results returned: ", 0, historyNotes.size() );
   }


   @Test
   public void create_success() throws ParseException {
      HistoryNote createdNote = getHistoryNoteToCreate();
      HistoryNote newNote = histNoteResourceBean.create( createdNote );

      assertHistoryNote( createdNote, newNote );
   }


   private void assertHistoryNotes( List<HistoryNote> expectedHistNoteList,
         List<HistoryNote> actualHistNoteList ) {
      assertEquals( "Number of history notes returned is incorrect: ", expectedHistNoteList.size(),
            actualHistNoteList.size() );

      HistoryNote[] expectedHistNotes =
            expectedHistNoteList.toArray( new HistoryNote[expectedHistNoteList.size()] );
      HistoryNote[] actualHistNotes =
            actualHistNoteList.toArray( new HistoryNote[actualHistNoteList.size()] );

      Arrays.sort( expectedHistNotes, Comparator.comparing( HistoryNote::getId ) );
      Arrays.sort( actualHistNotes, Comparator.comparing( HistoryNote::getId ) );

      for ( int i = 0; i < expectedHistNotes.length; i++ ) {
         assertHistoryNote( expectedHistNotes[i], actualHistNotes[i] );
      }

   }


   private void assertHistoryNote( HistoryNote expected, HistoryNote actual ) {
      if ( expected.getId() != null ) {
         assertEquals( "Id of history note did not match the expected value: ", expected.getId(),
               actual.getId() );
      }
      assertEquals( "Task Id of history note did not match the expected value: ",
            expected.getTaskId(), actual.getTaskId() );
      assertEquals( "User Id of history note did not match the expected value: ",
            expected.getUserId(), actual.getUserId() );
      assertEquals( "Stage Reason Code of history note did not match the expected value: ",
            expected.getStageReasonCode(), actual.getStageReasonCode() );
      assertEquals( "Status of history note did not match the expected value: ",
            expected.getStatus(), actual.getStatus() );
      assertEquals( "Note of history note did not match the expected value: ", expected.getNote(),
            actual.getNote() );
      assertEquals( "Date of history note did not match the expected value: ", expected.getDate(),
            actual.getDate() );

   }


   private HistoryNote getHistoryNoteToCreate() throws ParseException {
      HistoryNote historyNote = new HistoryNote();
      historyNote.setTaskId( "BBBBBBBBBBBBBBBB3333333333333333" );
      historyNote.setUserId( "CCCCCCCCCCCCCCCC1111111111111111" );
      historyNote.setStageReasonCode( "ACSHIP" );
      historyNote.setStatus( "ACARCHIVE" );
      historyNote.setNote( "New history note" );
      historyNote.setDate( DATE_FORMAT.parse( "2019-04-30 15:00:00" ) );

      return historyNote;
   }


   private void constructExpectedResults() throws ParseException {
      histNote1 = new HistoryNote();
      histNote1.setId( "AAAAAAAAAAAAAAAA1111111111111111" );
      histNote1.setTaskId( "BBBBBBBBBBBBBBBB1111111111111111" );
      histNote1.setUserId( "CCCCCCCCCCCCCCCC1111111111111111" );
      histNote1.setStageReasonCode( "NEW" );
      histNote1.setStatus( "IN WORK" );
      histNote1.setNote( "Test note 1 on task" );
      histNote1.setDate( DATE_FORMAT.parse( "2019-04-30 13:00:00" ) );

      histNote2 = new HistoryNote();
      histNote2.setId( "AAAAAAAAAAAAAAAA2222222222222222" );
      histNote2.setTaskId( "BBBBBBBBBBBBBBBB1111111111111111" );
      histNote2.setUserId( "CCCCCCCCCCCCCCCC1111111111111111" );
      histNote2.setStageReasonCode( "OBSOLETE" );
      histNote2.setStatus( "COMMIT" );
      histNote2.setNote( "Test note 2 on task" );
      histNote2.setDate( DATE_FORMAT.parse( "2019-04-30 14:00:00" ) );

      histNote3 = new HistoryNote();
      histNote3.setId( "AAAAAAAAAAAAAAAA3333333333333333" );
      histNote3.setTaskId( "BBBBBBBBBBBBBBBB2222222222222222" );
      histNote3.setUserId( "CCCCCCCCCCCCCCCC1111111111111111" );
      histNote3.setStageReasonCode( "NEW" );
      histNote3.setStatus( "IN WORK" );
      histNote3.setNote( "Test note on task 2" );
      histNote3.setDate( DATE_FORMAT.parse( "2019-04-30 13:00:00" ) );

   }

}

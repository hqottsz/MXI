package com.mxi.am.api.resource.maintenance.exec.work.pkg.historynote;

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
public class WorkPackageHistoryNoteResourceBeanTest extends ResourceBeanTest {

   @Inject
   WorkPackageHistoryNoteResourceBean histNoteResourceBean;

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
   HistoryNote histNote4;


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
      HistoryNote note = histNoteResourceBean.get( histNote4.getId() );

      assertHistoryNote( histNote4, note );
   }


   @Test
   public void get_failure_noteNotFound() throws AmApiResourceNotFoundException {
      final String nonExistentId = "00000000000000000000000000000000";

      exception.expect( AmApiResourceNotFoundException.class );
      exception.expectMessage(
            String.format( "HISTORY NOTE ON WORKPACKAGE %s not found", nonExistentId ) );

      histNoteResourceBean.get( nonExistentId );
   }


   @Test
   public void get_failure_nullId() throws AmApiResourceNotFoundException {
      exception.expect( AmApiBadRequestException.class );
      exception.expectMessage( "The history note id must be provided." );

      histNoteResourceBean.get( null );
   }


   @Test
   public void get_failure_emptyId() throws AmApiResourceNotFoundException {
      exception.expect( AmApiBadRequestException.class );
      exception.expectMessage( "The history note id must be provided." );

      histNoteResourceBean.get( "" );
   }


   @Test
   @CSIContractTest( Project.SWA_WP_STATUS )
   public void search_success_byWorkPackageId() {
      HistoryNoteSearchParameters params =
            new HistoryNoteSearchParameters().withWorkPackageId( histNote1.getWorkPackageId() );

      List<HistoryNote> historyNotes = histNoteResourceBean.search( params );

      assertHistoryNotes( Arrays.asList( histNote1, histNote2, histNote3 ), historyNotes );
      // assert the expected order is correct (latest ones come first)
      assertEquals( "First history note is in the wrong order: ", histNote3.getId(),
            historyNotes.get( 0 ).getId() );
      assertEquals( "Second history note is in the wrong order: ", histNote2.getId(),
            historyNotes.get( 1 ).getId() );
      assertEquals( "Third history note is in the wrong order: ", histNote1.getId(),
            historyNotes.get( 2 ).getId() );
   }


   @Test
   @CSIContractTest( Project.SWA_WP_STATUS )
   public void search_success_noResults() {
      HistoryNoteSearchParameters params = new HistoryNoteSearchParameters()
            .withWorkPackageId( "00000000000000000000000000000000" );

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
            expected.getWorkPackageId(), actual.getWorkPackageId() );
      assertEquals( "User Id of history note did not match the expected value: ",
            expected.getUserId(), actual.getUserId() );
      assertEquals( "Stage Reason Code of history note did not match the expected value: ",
            expected.getStageReasonCode(), actual.getStageReasonCode() );
      assertEquals( "Status of history note did not match the expected value: ",
            expected.getStatusCode(), actual.getStatusCode() );
      assertEquals( "Note of history note did not match the expected value: ", expected.getNote(),
            actual.getNote() );
      assertEquals( "Date of history note did not match the expected value: ", expected.getDate(),
            actual.getDate() );

   }


   private HistoryNote getHistoryNoteToCreate() throws ParseException {
      HistoryNote historyNote = new HistoryNote();
      historyNote.setWorkPackageId( "BBBBBBBBBBBBBBBB3333333333333333" );
      historyNote.setUserId( "CCCCCCCCCCCCCCCC1111111111111111" );
      historyNote.setStageReasonCode( "ACSHIP" );
      historyNote.setStatusCode( "ACARCHIVE" );
      historyNote.setNote( "New history note" );
      historyNote.setDate( DATE_FORMAT.parse( "2019-04-30 15:00:00" ) );

      return historyNote;
   }


   private void constructExpectedResults() throws ParseException {
      histNote1 = new HistoryNote();
      histNote1.setId( "AAAAAAAAAAAAAAAA1111111111111111" );
      histNote1.setWorkPackageId( "BBBBBBBBBBBBBBBB1111111111111111" );
      histNote1.setUserId( "CCCCCCCCCCCCCCCC1111111111111111" );
      histNote1.setStageReasonCode( "OBSOLETE" );
      histNote1.setStatusCode( "CANCEL" );
      histNote1.setNote( "Test Note 1 on WP 1" );
      histNote1.setDate( DATE_FORMAT.parse( "2019-04-30 13:00:00" ) );

      histNote2 = new HistoryNote();
      histNote2.setId( "AAAAAAAAAAAAAAAA2222222222222222" );
      histNote2.setWorkPackageId( "BBBBBBBBBBBBBBBB1111111111111111" );
      histNote2.setUserId( "CCCCCCCCCCCCCCCC1111111111111111" );
      histNote2.setStageReasonCode( "CORRECT" );
      histNote2.setStatusCode( "CFCERT" );
      histNote2.setNote( "Test Note 2 on WP 1" );
      histNote2.setDate( DATE_FORMAT.parse( "2019-04-30 14:00:00" ) );

      histNote3 = new HistoryNote();
      histNote3.setId( "AAAAAAAAAAAAAAAA3333333333333333" );
      histNote3.setWorkPackageId( "BBBBBBBBBBBBBBBB1111111111111111" );
      histNote3.setUserId( "CCCCCCCCCCCCCCCC1111111111111111" );
      histNote3.setStageReasonCode( "CORRECT" );
      histNote3.setStatusCode( "CFCERT" );
      histNote3.setNote( "Test Note 3 on WP 1" );
      histNote3.setDate( DATE_FORMAT.parse( "2019-04-30 15:00:00" ) );

      histNote4 = new HistoryNote();
      histNote4.setId( "AAAAAAAAAAAAAAAA4444444444444444" );
      histNote4.setWorkPackageId( "BBBBBBBBBBBBBBBB2222222222222222" );
      histNote4.setUserId( "CCCCCCCCCCCCCCCC1111111111111111" );
      histNote4.setStageReasonCode( "NEW" );
      histNote4.setStatusCode( "HRACTV" );
      histNote4.setNote( "Test Note 1 on WP 2" );
      histNote4.setDate( DATE_FORMAT.parse( "2019-04-30 15:00:00" ) );

   }

}


package com.mxi.mx.core.adapter.procurement.atasparesinvoice.logic.facade;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.adapter.procurement.atasparesinvoice.logic.Note;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.unittest.table.evt.EvtStage;


/**
 * This class tests the {@link MxCoreFacade} class.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class MxCoreFacadeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that added invoice notes are split when they are longer than 4000 bytes.
    */
   @Test
   public void testThatAddedInvoiceNotesOver4000BytesAreSplit() {
      MxCoreFacade lFacade = new MxCoreFacade();

      List<StringBuffer> lBuffers = new ArrayList<StringBuffer>();
      StringBuffer lBuffer = new StringBuffer();

      List<Note> lNotes = new ArrayList<Note>();

      for ( int i = 1; i < 200; i++ ) {
         String lText = i + " Invoice test note.";

         int lByteLength = ( lBuffer.toString().getBytes().length + lText.getBytes().length );

         if ( lByteLength >= 4000 ) {
            lBuffers.add( lBuffer );
            lBuffer = new StringBuffer();
         }

         lBuffer.append( lText + "\n" );

         Note lNote = new Note( "test.msg.CORE_FACADE_NOTE" );
         lNote.addParams( lText );
         lNotes.add( lNote );
      }

      lBuffers.add( lBuffer );

      EventKey lEventKey = new EventKey( 4650, 1 );

      lFacade.addInvoiceNotes( lEventKey, RefEventStatusKey.PIOPEN, lNotes );

      // invoice note is sent as system note but it is saved in evt_stage table under
      // user_stage_note column with system_bool set to 1
      EvtStage lEvtStage = new EvtStage( lEventKey );
      lEvtStage.assertSystemBool( 1 );
      lEvtStage.assertStageNote( 1, lBuffers.get( 0 ).toString() );
      lEvtStage.assertStageNote( 2, lBuffers.get( 1 ).toString() );
   }

}

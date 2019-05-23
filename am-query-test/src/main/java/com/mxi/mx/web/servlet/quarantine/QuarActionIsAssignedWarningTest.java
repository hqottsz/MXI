package com.mxi.mx.web.servlet.quarantine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.core.key.QuarActionKey;
import com.mxi.mx.core.key.QuarQuarKey;
import com.mxi.mx.core.table.quarantine.QuarActionAssignmentTable;
import com.mxi.mx.core.table.quarantine.QuarActionTable;


public class QuarActionIsAssignedWarningTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private static final String ARBITRARY_KEY_1 = "5432:999";
   private static final String ARBITRARY_KEY_2 = "5431:999";
   private static final String WARNING_MESSAGE = "Arbitrary warning message";


   @Test
   public void itDoesNotAddWarningsWhenNoActionsExist() throws MxException {
      List<QuarActionKey> lQuarantineActionKeys = new ArrayList<>();
      QuarActionIsAssignedWarning lWarning =
            new QuarActionIsAssignedWarning( lQuarantineActionKeys, WARNING_MESSAGE );

      // Populate the warning list if any warnings are relevant
      lWarning.getWarnings();

      Assert.assertTrue( lWarning.getWarning().isEmpty() );
   }


   @Test
   public void itDoesNotAddWarningsWhenNoActionsAreAssigned() throws MxException {
      QuarActionKey lUnassignedActionKey1 = createUnassignedAction( ARBITRARY_KEY_1 );
      QuarActionKey lUnassignedActionKey2 = createUnassignedAction( ARBITRARY_KEY_2 );

      List<QuarActionKey> lQuarantineActionKeys =
            Arrays.asList( lUnassignedActionKey1, lUnassignedActionKey2 );

      QuarActionIsAssignedWarning warning =
            new QuarActionIsAssignedWarning( lQuarantineActionKeys, WARNING_MESSAGE );

      Assert.assertThat( warning.getWarnings(),
            CoreMatchers.not( CoreMatchers.containsString( WARNING_MESSAGE ) ) );
   }


   @Test
   public void itProducesWarningsWhenAtLeastOneQuarantineCorrectiveActionIsAssigned()
         throws MxException {
      QuarActionKey lUnassignedActionKey = createUnassignedAction( ARBITRARY_KEY_1 );
      QuarActionKey lAssignedActionKey = createAssignedAction( ARBITRARY_KEY_2 );

      List<QuarActionKey> lQuarantineActionKeys =
            Arrays.asList( lUnassignedActionKey, lAssignedActionKey );

      QuarActionIsAssignedWarning lWarning =
            new QuarActionIsAssignedWarning( lQuarantineActionKeys, WARNING_MESSAGE );

      Assert.assertThat( lWarning.getWarnings(), CoreMatchers.containsString( WARNING_MESSAGE ) );
   }


   private QuarActionKey createUnassignedAction( String key ) {
      QuarActionTable lQuarantineActionTable = QuarActionTable.create( new QuarQuarKey( key ) );
      return lQuarantineActionTable.insert();
   }


   private QuarActionKey createAssignedAction( String key ) {
      QuarActionKey lQuarantineActionKey = createUnassignedAction( key );

      QuarActionAssignmentTable lAssignmentTable =
            QuarActionAssignmentTable.create( lQuarantineActionKey );
      lAssignmentTable.setAssigned( true );
      lAssignmentTable.insert();
      return lQuarantineActionKey;
   }

}

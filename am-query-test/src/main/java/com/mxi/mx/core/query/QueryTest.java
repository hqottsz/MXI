package com.mxi.mx.core.query;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DataSetArgumentSpec;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.license.MxLicenseFeatureValidator;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.core.common.dataset.SQLStatement;
import com.mxi.mx.core.common.dataset.StatementLoader;
import com.mxi.mx.db.connection.DatabaseProperties;
import com.mxi.mx.db.connection.DatabasePropertiesReader;
import com.mxi.mx.persistence.id.UniqueId;
import com.mxi.mx.testing.LicenseFeatureValidatorStub;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;


/**
 * This class is used to test that qrx files can be compiled into a prepared statement. If a qrx has
 * another automated test, it can be added to QueryTestIgnoreList.txt
 *
 * @author cjb
 * @created June 15, 2005
 */
@RunWith( AllTests.class )
public class QueryTest {

   /**
    * Returns the Query Test Suite
    *
    * @return the test suite
    *
    * @throws Exception
    *            an error occurred
    */
   public static Test suite() throws Exception {
      // database connection details
      DatabaseProperties lProperties = DatabasePropertiesReader.get();

      // fetch all production queries
      List<String> lAllQueries = new QueryResourceDiscovery().getQueryNames();

      // group the queries into buckets so we can execute in parallel
      int lThreadCount = Math.max( Runtime.getRuntime().availableProcessors(), 8 );
      ExecutorService lExecutor = Executors.newFixedThreadPool( lThreadCount );

      // divide up the queries
      List<List<String>> lQueryBuckets = groupQueries( lAllQueries, lThreadCount );

      // log all failures
      final TestSuite lSuite = new TestSuite();

      // each bucket of queries utilizes a shared connection
      for ( List<String> lQueryList : lQueryBuckets ) {
         QueryGroupRunner lRunner = new QueryGroupRunner( lProperties, lQueryList, lSuite );
         lExecutor.execute( lRunner );
      }

      // Wait until all queries are processed before returning
      lExecutor.shutdown();
      while ( !lExecutor.isTerminated() ) {
         lExecutor.awaitTermination( 1, TimeUnit.SECONDS );
      }

      return lSuite;
   }


   /**
    * Divvy up the master list of queries into a series of buckets
    */
   private static List<List<String>> groupQueries( List<String> aAllQueries, int aThreadCount ) {
      List<List<String>> lQueryBuckets = new ArrayList<>();

      int lBucketSize = aAllQueries.size() / aThreadCount;

      for ( int i = 0; i < aThreadCount; i++ ) {
         int lStartIndex = i * lBucketSize;
         int lEndIndex = lStartIndex + lBucketSize;
         if ( i == aThreadCount - 1 ) {
            lEndIndex = aAllQueries.size();
         }

         lQueryBuckets.add( aAllQueries.subList( lStartIndex, lEndIndex ) );
      }

      return lQueryBuckets;
   }


   private static class QueryGroupRunner implements Runnable {

      private final DatabaseProperties iDatabaseProperties;
      private final List<String> iQueryList;

      // maintains the list of test suite failures
      // we only store failures to keep the results smaller
      private TestSuite iTestSuite;


      private QueryGroupRunner(DatabaseProperties aDatabaseProperties, List<String> aQueryList,
            TestSuite aTestSuite) {
         iDatabaseProperties = aDatabaseProperties;
         iQueryList = aQueryList;
         iTestSuite = aTestSuite;
      }


      private Connection getConnection() throws ClassNotFoundException, SQLException {
         // Load Driver
         Class.forName( "oracle.jdbc.OracleDriver" );

         // Create connection
         Connection lDbConnection =
               DriverManager.getConnection( iDatabaseProperties.getJdbcConnectString(),
                     iDatabaseProperties.getUsername(), iDatabaseProperties.getPassword() );

         // Disable auto commit
         lDbConnection.setAutoCommit( false );

         return lDbConnection;
      }


      @Override
      public void run() {
         try {
            Connection lConnection = getConnection();

            for ( String lQueryName : iQueryList ) {
               executeAndLogFailure(
                     new IndividualTestCase( lConnection, "testQueryArguments", lQueryName ) );
               executeAndLogFailure(
                     new IndividualTestCase( lConnection, "testQueryDigitPatterns", lQueryName ) );
               executeAndLogFailure(
                     new IndividualTestCase( lConnection, "testQueryExecution", lQueryName ) );
            }
         } catch ( ClassNotFoundException | SQLException e ) {
            e.printStackTrace();
         }
      }


      private void executeAndLogFailure( IndividualTestCase aTestCase ) {
         TestResult lResult = aTestCase.run();
         if ( !lResult.wasSuccessful() ) {
            iTestSuite.addTest( aTestCase );
         }
      }
   }

   /**
    * The test cases for the query
    */
   public static class IndividualTestCase extends TestCase {

      /** SQL comment pattern "/ *" to first "* /" */
      private static final String COMMENT_PATTERN_1 = "(/\\*)([^/]*)(\\*/)";

      /** SQL comment pattern */
      private static final String COMMENT_PATTERN_2 = "--.*";

      /** SQL Dynamic Clause pattern */
      private static final String DYNAMIC_PATTERN = "\\[[^\\}]*\\]";

      private Connection iDbConnection;

      private final String iQueryFile;

      private SQLStatement iStatement;


      /**
       * Creates a new {@linkplain IndividualTestCase} object.
       *
       * @param aTest
       *           the test case
       * @param aQueryFile
       *           the query
       */
      private IndividualTestCase(Connection aConnection, String aTest, String aQueryFile) {
         super( aTest );

         iDbConnection = aConnection;
         iQueryFile = aQueryFile;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public String getName() {
         return String.format( "%s # %s", iQueryFile, super.getName() );
      }


      /**
       * {@inheritDoc}
       */
      @Override
      @Before
      public void setUp() throws Exception {
         super.setUp();

         // Stubbing out license feature validator so server doesn't need to be running
         MxLicenseFeatureValidator.setValidator( new LicenseFeatureValidatorStub() );

         // Load the query statement
         iStatement = StatementLoader.getInstance().loadSqlStatement( iQueryFile );

         // Prepare the query using the database connection
         iStatement.prepare( iDbConnection );
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public void tearDown() throws Exception {
         MxLicenseFeatureValidator.setValidator( null );

         // rollback our operation
         iDbConnection.rollback();

         super.tearDown();
      }


      /**
       * Tests all the queries for Unused Arguments in the query definition
       *
       * @throws Exception
       *            if an unexpected error occurs
       */
      @org.junit.Test
      public void testQueryArguments() throws Exception {

         // Get text content of the query
         String lQueryContent = iStatement.getOriginalContent().toUpperCase();
         lQueryContent = lQueryContent.replaceAll( COMMENT_PATTERN_1, "" );
         lQueryContent = lQueryContent.replaceAll( COMMENT_PATTERN_2, "" );

         // Loop through all the query arguments for verification
         final DataSetArgumentSpec[] lArgSpecs = iStatement.getArguments();
         for ( DataSetArgumentSpec lArgSpec : lArgSpecs ) {

            String lArgName = lArgSpec.getName();

            // Skip dynamic Bind Arguments
            if ( lArgName.toUpperCase().startsWith( "ABIND" ) ) {
               continue;
            }

            // If the argument cannot be found in the query body, log an error
            if ( lQueryContent.indexOf( ":" + lArgName.toUpperCase() ) < 0 ) {
               fail( "Unused query argument: '" + lArgName + "'" );
            }
         }
      }


      /**
       * Tests all the queries for Illegal Digit Patterns in the query definition
       *
       * @throws Exception
       *            if an unexpected error occurs
       */
      @org.junit.Test
      public void testQueryDigitPatterns() throws Exception {

         // Get text content of the query
         String lQueryContent = iStatement.getOriginalContent();
         lQueryContent = lQueryContent.replaceAll( COMMENT_PATTERN_1, "" );
         lQueryContent = lQueryContent.replaceAll( COMMENT_PATTERN_2, "" );

         // Pattern: 2 Non-Digit characters, one or more Digits, another Non-Digit character
         Pattern lPattern = Pattern.compile( "(\\d+)" );
         Matcher lMatcher = lPattern.matcher( lQueryContent );

         // Loop through any matching sets of digits
         while ( lMatcher.find() ) {
            String lDigitsFound = lMatcher.group( 1 );

            // If it is in our exceptions list, skip it
            if ( ignoreListDigitSeq( lDigitsFound ) ) {
               continue;
            }

            // Otherwise, log it as an error
            fail( "Potential illegal numerical data found: '" + lDigitsFound + "'" );
         }
      }


      /**
       * Tests all the queries for Proper Execution
       *
       * @throws Exception
       *            if an unexpected error occurs
       */
      @org.junit.Test
      public void testQueryExecution() throws Exception {
         try {

            // Skip Dynamic Queries
            String lQueryContent = iStatement.getOriginalContent();
            Pattern lPattern = Pattern.compile( DYNAMIC_PATTERN );
            lQueryContent = lQueryContent.replaceAll( COMMENT_PATTERN_1, "" );
            lQueryContent = lQueryContent.replaceAll( COMMENT_PATTERN_2, "" );
            if ( lPattern.matcher( lQueryContent ).find() ) {
               return; // TODO: Need to find a way to test dynamic queries
            }

            // Build the query arguments
            final DataSetArgument lArgs = new DataSetArgument();
            final DataSetArgumentSpec[] lArgSpecs = iStatement.getArguments();
            for ( DataSetArgumentSpec lArgSpec : lArgSpecs ) {

               // If the type is BOOLEAN, we must handle it differently (can't be null)
               if ( DataTypeUtils.BOOLEAN.equals( lArgSpec.getType() ) ) {
                  lArgs.add( lArgSpec.getName(), false );
               }
               // If the type is ARRAY, we must handle it differently (can't be null)
               else if ( DataTypeUtils.ARRAY.equals( lArgSpec.getType() ) ) {

                  // now we need to figure out what type of array, this isn't pretty
                  if ( isUuidArray( lArgSpec, lQueryContent ) ) {
                     lArgs.addUniqueIdArray( lArgSpec.getName(), new HashSet<UniqueId>( 0 ) );
                  } else if ( isIntArray( lArgSpec, lQueryContent ) ) {
                     lArgs.addIntegerArray( lArgSpec.getName(), new HashSet<Integer>( 0 ) );
                  } else {

                     // hope for the best with a string array
                     lArgs.addStringArray( lArgSpec.getName(), new HashSet<String>( 0 ) );
                  }
               } else if ( DataTypeUtils.DATE.equals( lArgSpec.getType() ) ) {
                  lArgs.add( lArgSpec.getName(), new Date() );
               } else {
                  lArgs.addObject( lArgSpec.getName(), lArgSpec.getType(), lArgSpec.getDefault() );
               }
            }

            // Test Query Execution
            try {
               iStatement.execute( iDbConnection, lArgs );
            } catch ( Exception ex ) {

               // Get the cause and its exception message
               final Throwable lCause = ex.getCause();
               String lMessage = "";
               if ( lCause != null ) {
                  lMessage = lCause.getClass().getName() + ": " + lCause.getMessage();
               } else {
                  lMessage = ex.getClass().getName() + ": " + ex.getMessage();
               }

               // Failure if not "cannot insert NULL", "Connect by filtering phase runs out of temp
               // tablespace", "Inventory context cannot be null"
               if ( ( !lMessage.contains( "ORA-01400" ) ) && ( !lMessage.contains( "ORA-01407" ) )
                     && ( !lMessage.contains( "ORA-30928" ) )
                     && ( !lMessage.contains( "ORA-20111" ) ) ) {
                  throw ex;
               }
            }
         } finally {
            if ( iStatement != null ) {
               iStatement.close(); // Close the statement
            }
         }
      }


      /**
       * Dictates if a digit sequence is to be ignored
       *
       * @param aDigits
       *           The digit sequence to check
       *
       * @return Whether this digit sequence should be skipped for this check
       */
      private boolean ignoreListDigitSeq( String aDigits ) {

         // The list of digit sequences to ignore
         String[] lIgnoreList = { "0", // for 0-level data and false boolean value
               "00", // formatting 999990D00
               "0001", // magic date 0001-01-01
               "1", // for 1 = 1 (tautology), +1 as an offset, 1 as true boolean value, in
                    // S1QUOTES
               "01", // magic date 0001-01-01
               "2", // when there are 2 variables with the same name aParm1, aParm2, in
                    // spec2k, NVL2
               "3", // ordering constant
               "4", // ordering constant
               "5", // ordering constant
               "6", // ordering constant
               "7", // ordering constant
               "8", // used in substr parsing
               "10", // 0-level data type 0:10
               "12", // as in HH12:MI AM
               "16", // in getRaw16Table
               "21", // 0-level data type 0:21
               "23", // 0-level data type 0:23
               "24", // for number of hours in a day
               "30", // 0-level account 0:30
               "59", // last minute of day in 23:59
               "60", // number of minutes in an hour, seconds in a minute
               "3600", // number of seconds in an hour
               "80", // substring length
               "100", // percent multiplier
               "123", // unlikely key value (-123)
               "365", // approximate days in a year
               "1900", // magic date 1900-JAN-01
               "2000", // in spec2000
               "4000", // substring length
               "9999", // unlikely key value (-9999)
               "99999", // unlikely key value (-99999)
               "999990", // formatting string
               "999999", // unlikely key value (-999999)
               "0000000009", // formatting string
               "09", // formatting string
               "0000000000" // added these 0s to the numbers in location code for sorting
                            // alphanumerically
         };
         for ( int i = 0; i < lIgnoreList.length; i++ ) {
            if ( lIgnoreList[i].equals( aDigits ) ) {
               return true;
            }
         }

         return false;
      }


      /**
       * Checks if the argument is an integer array
       *
       * @param aArgSpec
       *           the argument specification
       * @param aQueryContent
       *           the query content
       *
       * @return TRUE if it is an integer array
       */
      private boolean isIntArray( DataSetArgumentSpec aArgSpec, String aQueryContent ) {
         Pattern[] lSpecPatterns = new Pattern[] {
               Pattern.compile( "getIntTable\\(\\s*?:" + aArgSpec.getName() + "\\s*?\\)",
                     Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ),
               Pattern.compile(
                     "getIntIntTable\\(\\s*?:" + aArgSpec.getName() + "\\s*?,\\s*?:\\w*?\\s*?\\)",
                     Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ),
               Pattern.compile(
                     "getIntIntTable\\(\\s*?:\\w*?\\s*?,\\s*?:" + aArgSpec.getName() + "\\s*?\\)",
                     Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ),
               Pattern.compile( "getTableOfIntIntTuple\\(\\s*?:\\w*?\\s*?,\\s*?:"
                     + aArgSpec.getName() + "\\s*?\\)",
                     Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ),
               Pattern.compile(
                     "getTableOfIntIntTuple\\(\\s*?:" + aArgSpec.getName()
                           + "\\s*?,\\s*?:\\w*?\\s*?\\)",
                     Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ),
               Pattern.compile( "getAcftAssyUsageOnDate\\(\\s*?:" + aArgSpec.getName() + "",
                     Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ),
               Pattern.compile(
                     "getAcftAssyUsageOnDate\\(\\s*?:\\w*?\\s*?,\\s*?:" + aArgSpec.getName() + "",
                     Pattern.CASE_INSENSITIVE | Pattern.MULTILINE ) };

         for ( Pattern lSpecPattern : lSpecPatterns ) {
            if ( lSpecPattern.matcher( aQueryContent ).find() ) {
               return true;
            }
         }

         return false;
      }


      /**
       * Checks if the argument is an UniqueId array
       *
       * @param aArgSpec
       *           the argument specification
       * @param aQueryContent
       *           the query content
       *
       * @return TRUE if it is an unique Id array
       */
      private boolean isUuidArray( DataSetArgumentSpec aArgSpec, String aQueryContent ) {
         Pattern lSpecPattern =
               Pattern.compile( "getRaw16Table\\(\\s*?:" + aArgSpec.getName() + "\\s*?\\)",
                     Pattern.CASE_INSENSITIVE | Pattern.MULTILINE );

         return lSpecPattern.matcher( aQueryContent ).find();
      }
   }

   /**
    * This class contains all logic necessary for automated discovery of Maintenix query resources.
    *
    * @author cjb
    * @created June 15, 2005
    */
   private static class QueryResourceDiscovery {

      // The list of queries to ignore
      private static final String IGNORE_LIST_FILE = "QueryTestIgnoreList.txt";

      // Query file extension
      private static final String QUERY_EXT = ".qrx";

      // list of queries that should be ignored
      private static List<String> iIgnoreList = null;

      /** File filter constants. */
      private final org.hamcrest.Matcher<String> QUERY_FILTER = CoreMatchers.endsWith( QUERY_EXT );


      /**
       * Retrieves a list of all query names available as resources in the Maintenix server.
       *
       * @return a list of all query names available as resources in the Maintenix server.
       */
      private List<String> getQueryNames() {

         try {
            // Get a list of archive files for this directory
            List<Path> lResources =
                  ResourceScanner.getResources( getClass().getClassLoader(), QUERY_FILTER );
            List<String> lQueries = new ArrayList<>();
            for ( Path lResource : lResources ) {
               String lQuery = lResource.toString().replace( '\\', '.' ).replace( '/', '.' )
                     .replaceFirst( ".qrx", "" );

               // Ignore some queries
               if ( ignoreListQueryName( lQuery ) ) {
                  continue;
               }

               lQueries.add( lQuery );
            }
            return lQueries;
         } catch ( Exception lException ) {
            throw new MxRuntimeException( "IO error in generating query resources.", lException );
         }
      }


      private static List<String> getIgnoreList() throws Exception {
         // Load the ignore list if not already loaded
         if ( iIgnoreList == null ) {
            iIgnoreList = new ArrayList<>();
            try ( BufferedReader lReader = new BufferedReader( new InputStreamReader(
                  QueryTest.class.getResourceAsStream( IGNORE_LIST_FILE ) ) ); ) {
               String lLine;
               while ( ( lLine = lReader.readLine() ) != null ) {

                  // Do not ignore all tests!
                  if ( "".equals( lLine ) || lLine.startsWith( "#" ) ) {
                     continue;
                  }

                  iIgnoreList.add( lLine );
               }
            }
         }

         return iIgnoreList;
      }


      /**
       * Dictates if a query file is to be ignored
       *
       * @param aQuery
       *           The queryname to check
       *
       * @return Whether this query should be skipped for this check
       *
       * @throws Exception
       *            an error occurred
       */
      private boolean ignoreListQueryName( String aQuery ) throws Exception {

         // Checks if query is in the ignore list
         for ( String lIgnoreDirectory : getIgnoreList() ) {
            if ( aQuery.startsWith( lIgnoreDirectory ) ) {
               return true;
            }
         }

         return false;
      }
   }
}

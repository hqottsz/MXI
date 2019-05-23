
package com.mxi.mx.core.assembler.lrp.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxi.mx.common.serializer.Serializer;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.common.utils.StreamUtils;
import com.mxi.mx.lrp.serializer.ModelSerializerFactory;
import com.mxi.mx.model.AuditData;
import com.mxi.mx.model.ModelObject;
import com.mxi.mx.model.key.CdKey;
import com.mxi.mx.model.key.CdKeyImpl;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.Aircraft;
import com.mxi.mx.model.lrp.AircraftUsageEntry;
import com.mxi.mx.model.lrp.Assembly;
import com.mxi.mx.model.lrp.Block;
import com.mxi.mx.model.lrp.BlockDefinition;
import com.mxi.mx.model.lrp.Contact;
import com.mxi.mx.model.lrp.DataType;
import com.mxi.mx.model.lrp.Event;
import com.mxi.mx.model.lrp.Event.EventStatus;
import com.mxi.mx.model.lrp.ForecastModel;
import com.mxi.mx.model.lrp.ForecastRange;
import com.mxi.mx.model.lrp.ForecastRate;
import com.mxi.mx.model.lrp.GantConfiguration;
import com.mxi.mx.model.lrp.GantConfiguration.ConfigType;
import com.mxi.mx.model.lrp.LRPPlan;
import com.mxi.mx.model.lrp.Location;
import com.mxi.mx.model.lrp.Operator;
import com.mxi.mx.model.lrp.ScheduleRule;
import com.mxi.mx.model.lrp.Severity;
import com.mxi.mx.model.lrp.WorkPackage;
import com.mxi.mx.model.lrp.WorkScope;
import com.mxi.mx.model.user.User;


/**
 * This encapsulates the loading of the unit test data for long range planning.
 *
 * @author ananner
 */
public class DataLoader {

   private static final int TEST_DB_ID = 777;
   private static final IdKey ACTUAL_AIRCRAFT_KEY = new IdKeyImpl( TEST_DB_ID, 1 );
   private static final CdKey ASSEMBLY_KEY = new CdKeyImpl( TEST_DB_ID, "BOEING" );
   private static final IdKey FORECAST_MODEL_KEY = new IdKeyImpl( TEST_DB_ID, 1 );


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   public static void generateModelXMLToConsole() throws Exception {
      getPlan();
   }


   public static LRPPlan getPlan() {
      LRPPlan lPlanToSave = new LRPPlan();

      lPlanToSave.setDescription( "Plan-Description" );
      lPlanToSave.setName( "Plan-Name" );

      lPlanToSave.setAircrafts( getAircrafts() );

      lPlanToSave.setCreateUser( getCreateUser() );
      lPlanToSave.setUpdateUser( getCreateUser() );
      lPlanToSave.setGantConfig( getConfiguration() );
      lPlanToSave.setLocations( getLocations() );
      lPlanToSave.setBlockDefinitions( getBlockDefns() );

      WorkPackage lEvent =
            getEvent( lPlanToSave.getAircrafts().get( 0 ), lPlanToSave.getLocations().get( 0 ) );

      Block lBlock = getBlock( lPlanToSave.getBlockDefinitions().get( 0 ), lEvent );
      lPlanToSave.addBlock( lBlock );

      lPlanToSave.addEvent( lEvent );

      return lPlanToSave;
   }


   /**
    * This is a helper method used to deserialize model objects from Castor XML format so that the
    * model objects can be used for unit tests.
    *
    * @param aXmlFile
    *           The Castor XML file.
    *
    * @return The model object that was persisted.
    *
    * @throws Exception
    *            If there is an error.
    */
   public static ModelObject<?> loadData( String aXmlFile ) throws Exception {
      Serializer<ModelObject<?>> lMarshaller =
            ModelSerializerFactory.getModelObjectSerializer( LRPPlan.class );

      InputStream lResourceStream = DataLoader.class.getResourceAsStream( aXmlFile );
      byte[] lXMLBytes;
      try {
         lXMLBytes = StreamUtils.readBytesFromStream( lResourceStream );
      } finally {
         lResourceStream.close();
      }

      ByteArrayInputStream lXMLInStream = new ByteArrayInputStream( lXMLBytes );

      return lMarshaller.unmarshal( lXMLInStream );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aArgs
    *           DOCUMENT_ME
    */
   public static void main( String[] aArgs ) {
      try {
         System.setProperty( "lrp.serializer", "castor" );
         generateModelXMLToConsole();
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }


   /**
    * This is a helper method used to serialize model objects into Castor XML format so that they
    * can be stored as static data for unit test use.
    *
    * @param aModelObject
    *           The model object to be persisted.
    *
    * @return The XML String
    *
    * @throws Exception
    *            If there is an error.
    */
   public static String serializeModelObject( ModelObject<?> aModelObject ) throws Exception {
      Serializer<ModelObject<?>> lMarshaller =
            ModelSerializerFactory.getModelObjectSerializer( Aircraft.class );

      ByteArrayOutputStream lXMLOutStream = lMarshaller.marshal( aModelObject );

      return lXMLOutStream.toString( "UTF-8" );
   }


   private static List<Aircraft> getAircrafts() {

      List<Aircraft> lAircrafts = new ArrayList<Aircraft>();

      Aircraft lAircraft = new Aircraft();
      lAircraft.setActualRefKey( ACTUAL_AIRCRAFT_KEY );

      lAircraft.setAssembly( getAssembly() );

      // Define the aircraft usage entries
      lAircraft.setCurrentUsages( getUsageEntries() );
      lAircraft.setForecastModel( getForecastModel( getCycleDataType() ) );
      lAircraft.setOperator( getOperator() );

      lAircraft.setFuture( false );
      lAircraft.setReceivedDate( DateUtils.getDate( 2008, 5, 9 ) );
      lAircraft.setManufacturedDate( DateUtils.getDate( 2008, 5, 9 ) );
      lAircraft.setRetirementDate( DateUtils.getDate( 2012, 5, 9 ) );

      lAircraft.setName( "BOEING" );
      lAircraft.setOEMSerialNo( "200" );

      lAircraft.setRegCode( "ACTUAL" );

      lAircrafts.add( lAircraft );

      return lAircrafts;
   }


   private static Assembly getAssembly() {
      Assembly lAssembly = new Assembly();
      lAssembly.setActualRefKey( ASSEMBLY_KEY );

      return lAssembly;
   }


   private static Block getBlock( BlockDefinition aBlockDefinition, WorkPackage aAssociatedEvent ) {
      Block lBlock = new Block();
      lBlock.setName( "TestBlock" );
      lBlock.setDueDate( DateUtils.getDate( 2008, 5, 9 ) );
      lBlock.setBlockDefinition( aBlockDefinition );
      lBlock.setAssociatedEvent( aAssociatedEvent );

      ScheduleRule lRule = new ScheduleRule();
      lRule.setActualRefKey( new IdKeyImpl( 777, 1 ) );

      DataType lDataType = new DataType();
      lDataType.setActualRefKey( new IdKeyImpl( 777, 11 ) );
      lRule.setForecastDataType( lDataType );
      lBlock.setDrivingSchedulingRule( lRule );

      return lBlock;
   }


   private static BlockDefinition getBlockDefn() {
      BlockDefinition lBlockDefn = new BlockDefinition();
      lBlockDefn.setActualRefKey( new IdKeyImpl( 4650, 238163 ) );
      lBlockDefn.setAssembly( getAssembly() );
      lBlockDefn.setCode( "TEST-BLOCK-DEFN-1" );
      lBlockDefn.setName( "Test-Block-Defn-1" );
      lBlockDefn.addWorkType( 10, "ADMIN", "Admin", 10 );

      return lBlockDefn;
   }


   private static List<BlockDefinition> getBlockDefns() {

      List<BlockDefinition> lDefns = new ArrayList<BlockDefinition>();

      BlockDefinition lBlockDefn = getBlockDefn();

      lDefns.add( lBlockDefn );

      return lDefns;
   }


   private static DataType getCdyDataType() {
      DataType lCdyDataType = new DataType();
      lCdyDataType.setDataTypeCode( "DAY" );
      lCdyDataType.setEnglishUnitCode( "DAYS" );
      lCdyDataType.setDomainType( "CA" );

      return lCdyDataType;
   }


   private static AircraftUsageEntry getCdyUsage( DataType aCdyDataType ) {
      AircraftUsageEntry lCdyUsageEntry = new AircraftUsageEntry();
      lCdyUsageEntry.setUsageDataType( aCdyDataType );
      lCdyUsageEntry.setValue( 18 );

      return lCdyUsageEntry;
   }


   private static GantConfiguration getConfiguration() {

      Severity lSeverity = new Severity( "description", "name", "red" );
      lSeverity.setPrimaryKey( new CdKeyImpl( 10, "CRITICAL" ) );

      // GANTT CONFIGURATION
      GantConfiguration lConfig = new GantConfiguration();
      lConfig.addSeverity( ConfigType.ADHOC, lSeverity );
      lConfig.addSeverity( ConfigType.DURGRTMAX, lSeverity );
      lConfig.addSeverity( ConfigType.DURLESSMIN, lSeverity );
      lConfig.addSeverity( ConfigType.EXTDEAD, lSeverity );
      lConfig.addSeverity( ConfigType.OVERDUE, lSeverity );
      lConfig.addSeverity( ConfigType.OVERFLOW, lSeverity );
      lConfig.addSeverity( ConfigType.OVERLAP, lSeverity );
      lConfig.addSeverity( ConfigType.PLANAFTMAXYD, lSeverity );
      lConfig.addSeverity( ConfigType.PLANBFOREMINYD, lSeverity );
      lConfig.addSeverity( ConfigType.OUT_OF_SEQUENCE, lSeverity );
      lConfig.addSeverity( ConfigType.UPDATE_TO_ACTUALS, lSeverity );
      lConfig.addSeverity( ConfigType.EXTR_LIMIT_EXCEEDED, lSeverity );
      lConfig.addSeverity( ConfigType.PRIOR_EFFECTIVE_DATE, lSeverity );
      lConfig.addSeverity( ConfigType.READ_ONLY, lSeverity );
      lConfig.addSeverity( ConfigType.OVERRUNBKTS, lSeverity );

      return lConfig;
   }


   private static User getCreateUser() {
      User lCreateUser = new User();
      lCreateUser.setUserID( 15 );

      return lCreateUser;
   }


   private static DataType getCycleDataType() {
      DataType lCyclesDataType = new DataType();
      lCyclesDataType.setDataTypeCode( "CYCLES" );
      lCyclesDataType.setEnglishUnitCode( "CYCLES" );
      lCyclesDataType.setDomainType( "US" );

      return lCyclesDataType;
   }


   private static AircraftUsageEntry getCycleUsage( DataType aCyclesDataType ) {
      AircraftUsageEntry lCyclesUsageEntry = new AircraftUsageEntry();
      lCyclesUsageEntry.setUsageDataType( aCyclesDataType );
      lCyclesUsageEntry.setValue( 23 );

      return lCyclesUsageEntry;
   }


   private static WorkPackage getEvent( Aircraft aAircraft, Location aLocation ) {
      WorkPackage lWorkPackage = new WorkPackage( Event.Type.WORK_EVENT );

      IdKey lActualWorkEventRef = new IdKeyImpl( 1, 1 );
      lWorkPackage.setActualRefKey( lActualWorkEventRef );

      lWorkPackage.setAircraft( aAircraft );

      AuditData lAuditData = new AuditData();
      lWorkPackage.setAuditData( lAuditData );

      Contact lContactInformation = new Contact();
      lWorkPackage.setContactInformation( lContactInformation );

      String lDescription = "EventDescription";
      lWorkPackage.setDescription( lDescription );

      Calendar lCal = Calendar.getInstance();
      Date lStartDate = lCal.getTime();

      lCal.add( Calendar.HOUR, 12 );

      Date lEndDate = lCal.getTime();
      lWorkPackage.setDateRange( lStartDate, lEndDate );

      lWorkPackage.setLocation( aLocation );

      String lName = "EventName";
      lWorkPackage.setName( lName );

      String lOverflowDetail = "";
      lWorkPackage.setOverflowDetail( lOverflowDetail );

      IdKey lPrimaryKey = new IdKeyImpl( 2, 2 );
      lWorkPackage.setPrimaryKey( lPrimaryKey );

      lWorkPackage.setStatus( EventStatus.ACTV );

      Map<DataType, AircraftUsageEntry> lCurrentUsages =
            new HashMap<DataType, AircraftUsageEntry>();
      lWorkPackage.setUsages( lCurrentUsages );

      WorkScope lWorkScope = new WorkScope();
      lWorkPackage.setWorkScope( lWorkScope );

      return lWorkPackage;
   }


   private static ForecastModel getForecastModel( DataType aCyclesDataType ) {

      // Define the forecast model for the aircraft
      ForecastModel lForecastModel = new ForecastModel();
      lForecastModel.setActualRefKey( FORECAST_MODEL_KEY );
      lForecastModel.setDescription( "This is the forecast model for Aircraft 213" );

      ForecastRange lJanAprilForecastRange = new ForecastRange();
      lJanAprilForecastRange.setStartDay( 1 );
      lJanAprilForecastRange.setStartMonth( 1 );

      ForecastRate lJanAprilUsageForecastRate = new ForecastRate();
      lJanAprilUsageForecastRate.setRate( 1 );
      lJanAprilUsageForecastRate.setUsageType( aCyclesDataType );
      lJanAprilForecastRange.addRate( lJanAprilUsageForecastRate );

      ForecastRange lMayForecastRange = new ForecastRange();
      lMayForecastRange.setStartDay( 1 );
      lMayForecastRange.setStartMonth( 5 );

      ForecastRate lMayUsageForecastRate = new ForecastRate();
      lMayUsageForecastRate.setRate( 2 );
      lMayUsageForecastRate.setUsageType( aCyclesDataType );
      lMayForecastRange.addRate( lMayUsageForecastRate );

      ForecastRange lJuneDecForecastRange = new ForecastRange();
      lJuneDecForecastRange.setStartDay( 1 );
      lJuneDecForecastRange.setStartMonth( 6 );

      ForecastRate lJuneDecUsageForecastRate = new ForecastRate();
      lJuneDecUsageForecastRate.setRate( 1 );
      lJuneDecUsageForecastRate.setUsageType( aCyclesDataType );
      lJuneDecForecastRange.addRate( lJuneDecUsageForecastRate );

      lForecastModel.addRange( lJanAprilForecastRange );
      lForecastModel.addRange( lMayForecastRange );
      lForecastModel.addRange( lJuneDecForecastRange );

      return lForecastModel;
   }


   private static List<Location> getLocations() {
      List<Location> lLocations = new ArrayList<Location>();

      Location lLocation = new Location();
      lLocation.setActualRefKey( new IdKeyImpl( TEST_DB_ID, 6 ) );
      lLocation.setLabourRate( 2.0f );
      lLocation.setDefaultCapacity( 1 );
      lLocation.setName( "VENDOR TRACK" );
      lLocation.setLocationCode( "CA/OTT/LN/VLINE/VTRK" );
      lLocation.setType( "VENTRK (Vendor Track)" );

      lLocations.add( lLocation );

      return lLocations;
   }


   private static Operator getOperator() {
      Operator lOperator = new Operator();
      lOperator.setName( "Test carrier 1" );

      return lOperator;
   }


   private static Map<DataType, AircraftUsageEntry> getUsageEntries() {
      AircraftUsageEntry lCdyUsageEntry = getCdyUsage( getCdyDataType() );

      AircraftUsageEntry lCyclesUsageEntry = getCycleUsage( getCycleDataType() );

      Map<DataType, AircraftUsageEntry> lListUsageEntries =
            new HashMap<DataType, AircraftUsageEntry>();
      lListUsageEntries.put( lCdyUsageEntry.getUsageDataType(), lCdyUsageEntry );
      lListUsageEntries.put( lCyclesUsageEntry.getUsageDataType(), lCyclesUsageEntry );

      return lListUsageEntries;
   }
}

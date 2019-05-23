package com.mxi.am.api.resource.sys.refterm.measurementparameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBContext;

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
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.refterm.measurementparameter.impl.MeasurementParameterResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for MeasurementParameter ResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class MeasurementParameterResourceBeanTest extends ResourceBeanTest {

   private static final String CODE1 = "TSIG";
   private static final String NAME1 = "Take Off Signal";
   private static final String DESCRIPTION1 = "TSIG description";
   private static final String UNIT_CODE1 = "UC1";
   private static final Integer PRECISION1 = 1;
   private static final String DOMAIN_TYPE_CODE1 = "CH";
   private static final String DATA_VALUE_CODE1 = "BUSY";
   private static final String DATA_VALUE_NAME1 = "BUSY Signal";
   private static final String DATA_VALUE_CODE2 = "CLEAR";
   private static final String DATA_VALUE_NAME2 = "CLEAR Signal";

   private static final String CODE2 = "CHAR";
   private static final String NAME2 = "Character Measurement";
   private static final String DESCRIPTION2 = "Character Measurement Test";
   private static final String UNIT_CODE2 = "UC2";
   private static final Integer PRECISION2 = 2;
   private static final String DOMAIN_TYPE_CODE2 = "CME";
   private static final String DATA_VALUE_CODE3 = "TEST";
   private static final String DATA_VALUE_NAME3 = "Test";
   private static final int MEASUREMENT_PARAMETER_RECORD_COUNT = 2;
   private static final String NON_EXISTENT_MEASUREMENT_PARAMETER_CODE = "XXX";

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( MeasurementParameterResource.class )
                     .to( MeasurementParameterResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
            }
         } );

   @Inject
   private MeasurementParameterResourceBean measurementParameterResourceBean;;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      measurementParameterResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testGetMeasurementParameterByCodeSuccess() throws AmApiResourceNotFoundException {
      MeasurementParameter measurementParameter = measurementParameterResourceBean.get( CODE1 );
      assertTrue( "Incorrect data value list returned.",
            measurementParameter.getDataValue().containsAll( getExpectedDataValueList() ) );
      measurementParameter.setDataValue( null );
      assertEquals( "Incorrect measurement parameter returned.",
            defaultMeasurementParameterBuilder(), measurementParameter );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllMeasurementParametersSuccess() {
      List<MeasurementParameter> measurementParameterList =
            measurementParameterResourceBean.search();
      assertEquals( "Expected 2 measurement parameters found in the database.",
            MEASUREMENT_PARAMETER_RECORD_COUNT, measurementParameterList.size() );
      assertTrue( "Incorrect measurement parameter list returned.",
            measurementParameterList.containsAll( defaultMeasurementParameterListBuilder() ) );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetMeasurementParameterByNonExistentCodeFailure()
         throws AmApiResourceNotFoundException {
      measurementParameterResourceBean.get( NON_EXISTENT_MEASUREMENT_PARAMETER_CODE );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetMeasurementParameterByNullCodeFailure()
         throws AmApiResourceNotFoundException {
      measurementParameterResourceBean.get( null );
   }


   List<MeasurementParameter> defaultMeasurementParameterListBuilder() {
      List<MeasurementParameter> measurementParameterList = new ArrayList<MeasurementParameter>();
      MeasurementParameter measurementParameter2 = new MeasurementParameter();

      List<DataValue> dataValueList2 = new ArrayList<DataValue>();
      DataValue dataValue2 = new DataValue();
      dataValue2.setCode( DATA_VALUE_CODE3 );
      dataValue2.setName( DATA_VALUE_NAME3 );
      dataValueList2.add( dataValue2 );

      measurementParameter2.setCode( CODE2 );
      measurementParameter2.setName( NAME2 );
      measurementParameter2.setDescription( DESCRIPTION2 );
      measurementParameter2.setUnitCode( UNIT_CODE2 );
      measurementParameter2.setPrecision( PRECISION2 );
      measurementParameter2.setDomainTypeCode( DOMAIN_TYPE_CODE2 );
      measurementParameter2.setDataValue( dataValueList2 );

      MeasurementParameter measurementParameter1 = defaultMeasurementParameterBuilder();
      measurementParameter1.setDataValue( getExpectedDataValueList() );

      measurementParameterList.add( measurementParameter1 );
      measurementParameterList.add( measurementParameter2 );
      return measurementParameterList;
   }


   private MeasurementParameter defaultMeasurementParameterBuilder() {
      MeasurementParameter measurementParameter = new MeasurementParameter();
      measurementParameter.setCode( CODE1 );
      measurementParameter.setName( NAME1 );
      measurementParameter.setDescription( DESCRIPTION1 );
      measurementParameter.setUnitCode( UNIT_CODE1 );
      measurementParameter.setPrecision( PRECISION1 );
      measurementParameter.setDomainTypeCode( DOMAIN_TYPE_CODE1 );
      return measurementParameter;
   }


   private List<DataValue> getExpectedDataValueList() {
      List<DataValue> dataValueList = new ArrayList<DataValue>();
      DataValue dataValue1 = new DataValue();
      dataValue1.setCode( DATA_VALUE_CODE1 );
      dataValue1.setName( DATA_VALUE_NAME1 );
      DataValue dataValue2 = new DataValue();
      dataValue2.setCode( DATA_VALUE_CODE2 );
      dataValue2.setName( DATA_VALUE_NAME2 );
      dataValueList.add( dataValue1 );
      dataValueList.add( dataValue2 );
      return dataValueList;
   }
}

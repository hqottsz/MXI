package com.mxi.am.ee;

import org.junit.rules.ExternalResource;

import com.mxi.mx.common.alert.AlertEngineCacheLoader;
import com.mxi.mx.common.alert.AlertEngineStub;
import com.mxi.mx.common.alert.MxAlertEngine;
import com.mxi.mx.common.cache.CacheFactory;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.key.PrimaryKeyServiceStub;
import com.mxi.mx.common.services.event.MxEventServiceStub;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactory;
import com.mxi.mx.common.services.sequence.SequenceGeneratorFactoryFake;
import com.mxi.mx.common.services.work.WorkItemGenerator;
import com.mxi.mx.common.services.work.WorkItemGeneratorExecuteImmediateFake;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.license.CoreLicenseStub;
import com.mxi.mx.core.license.MxCoreLicense;
import com.mxi.mx.core.services.events.MxEventsService;
import com.mxi.mx.core.timezone.LocationTimeZoneCacheLoader;
import com.mxi.mx.testing.FakeStringBundles;
import com.mxi.mx.testing.mock.cache.MockCacheFactory;


/**
 * Fakes the Java EE constructors
 */
public final class FakeJavaEeDependenciesRule extends ExternalResource {

   @Override
   protected void before() throws Exception {
      EjbFactory.setSingleton( new EjbFactoryStub() );
      WorkItemGenerator.setInstance( new WorkItemGeneratorExecuteImmediateFake() );
      StringBundles.setSingleton( new FakeStringBundles() );
      TriggerFactory.setInstance( new TriggerFactoryStub() );
      PrimaryKeyService.setSingleton( new PrimaryKeyServiceStub() );
      SequenceGeneratorFactory.setInstance( new SequenceGeneratorFactoryFake() );
      CacheFactory
            .setInstance( new MockCacheFactory( new MockCacheFactory.MockTimeZoneCacheLoader(),
                  new LocationTimeZoneCacheLoader( null ), new AlertEngineCacheLoader() ) );
      MxCoreLicense.setValidator( new CoreLicenseStub( CoreLicenseStub.FeatureSet.ALL ) );
      MxAlertEngine.setInstance( new AlertEngineStub() );

      // Override GlobalParameters to use the database directly (instead of via JavaEE)
      for ( ParmTypeEnum lParmType : ParmTypeEnum.values() ) {
         GlobalParameters.setInstance( new GlobalParametersFake( lParmType.name() ) );
      }

      MxEventsService.setTestInstance( new MxEventServiceStub() );
   }


   @Override
   protected void after() {
      EjbFactory.setSingleton( null );
      WorkItemGenerator.setInstance( null );
      StringBundles.setSingleton( null );
      TriggerFactory.setInstance( null );
      PrimaryKeyService.setSingleton( null );
      SequenceGeneratorFactory.setInstance( null );
      CacheFactory.setInstance( null );
      MxCoreLicense.setValidator( null );
      MxAlertEngine.setInstance( null );
      for ( ParmTypeEnum lParmType : ParmTypeEnum.values() ) {
         GlobalParameters.setInstance( lParmType.name(), null );
      }
      MxEventsService.setTestInstance( null );
   }
}


package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.Attachment;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.table.ietm.IetmTopic;


public class AttachmentBuilder {

   public static IetmTopicKey build( Attachment aAttachment ) {

      IetmTopic lIetmTopic =
            IetmTopic.create( IetmTopic.generatePrimaryKey( aAttachment.getIetmKey() ) );
      lIetmTopic.setTopicSdesc( aAttachment.getName() );
      byte[] lAttachedBlob = ( byte[] ) defaultIfNull( aAttachment.getAttachedBlob(),
            "DEFAULT ATTACHED CONTENT".getBytes() );
      lIetmTopic.setAttachBlob( lAttachedBlob );

      IetmTopicKey lNewIetmTopic = lIetmTopic.insert();

      return lNewIetmTopic;

   }

}

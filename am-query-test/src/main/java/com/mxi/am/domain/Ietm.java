package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;


/**
 * Domain entity for IETM (Interactive Electronic Technical Manuals)
 *
 */
public class Ietm {

   private String iCode;
   private String iName;
   private String iDescription;
   private String iCommonPrefix;
   private List<DomainConfiguration<TechnicalReference>> iTechnicalReferenceConfigurations =
         new ArrayList<DomainConfiguration<TechnicalReference>>();
   private List<DomainConfiguration<Attachment>> iAttachmentConfigurations =
         new ArrayList<DomainConfiguration<Attachment>>();


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public String getDescription() {
      return iDescription;
   }


   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }


   public String getCommonPrefix() {
      return iCommonPrefix;
   }


   public void setCommonPrefix( String aCommonPrefix ) {
      iCommonPrefix = aCommonPrefix;
   }


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public List<DomainConfiguration<TechnicalReference>> getTechnicalReferences() {
      return iTechnicalReferenceConfigurations;
   }


   public void addTechnicalReference(
         DomainConfiguration<TechnicalReference> aTechnicalReferenceConfiguration ) {
      iTechnicalReferenceConfigurations.add( aTechnicalReferenceConfiguration );
   }


   public List<DomainConfiguration<Attachment>> getAttachments() {
      return iAttachmentConfigurations;
   }


   public void addAttachment( DomainConfiguration<Attachment> aAttachmentConfiguration ) {
      iAttachmentConfigurations.add( aAttachmentConfiguration );
   }

}

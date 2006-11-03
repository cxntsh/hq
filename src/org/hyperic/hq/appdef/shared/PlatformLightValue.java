/*
 * xdoclet generated code.
 * legacy DTO pattern (targeted to be replaced with hibernate pojo).
 */
package org.hyperic.hq.appdef.shared;

/**
 * Value object for Platform.
 *
 */
public class PlatformLightValue
   extends org.hyperic.hq.appdef.shared.AppdefResourceValue
   implements java.io.Serializable
{
   private java.lang.String sortName;
   private boolean sortNameHasBeenSet = false;
   private java.lang.String commentText;
   private boolean commentTextHasBeenSet = false;
   private java.lang.String modifiedBy;
   private boolean modifiedByHasBeenSet = false;
   private java.lang.String owner;
   private boolean ownerHasBeenSet = false;
   private java.lang.Integer configResponseId;
   private boolean configResponseIdHasBeenSet = false;
   private java.lang.String certdn;
   private boolean certdnHasBeenSet = false;
   private java.lang.String fqdn;
   private boolean fqdnHasBeenSet = false;
   private java.lang.String name;
   private boolean nameHasBeenSet = false;
   private java.lang.String location;
   private boolean locationHasBeenSet = false;
   private java.lang.String description;
   private boolean descriptionHasBeenSet = false;
   private java.lang.Integer cpuCount;
   private boolean cpuCountHasBeenSet = false;
   private java.lang.Integer id;
   private boolean idHasBeenSet = false;
   private java.lang.Long mTime;
   private boolean mTimeHasBeenSet = false;
   private java.lang.Long cTime;
   private boolean cTimeHasBeenSet = false;
   private java.util.Collection IpValues = new java.util.HashSet();
   private org.hyperic.hq.appdef.shared.PlatformTypeValue PlatformType;
   private boolean PlatformTypeHasBeenSet = false;

   public PlatformLightValue()
   {
   }

   public PlatformLightValue( java.lang.String sortName,java.lang.String commentText,java.lang.String modifiedBy,java.lang.String owner,java.lang.Integer configResponseId,java.lang.String certdn,java.lang.String fqdn,java.lang.String name,java.lang.String location,java.lang.String description,java.lang.Integer cpuCount,java.lang.Integer id,java.lang.Long mTime,java.lang.Long cTime )
   {
	  this.sortName = sortName;
	  sortNameHasBeenSet = true;
	  this.commentText = commentText;
	  commentTextHasBeenSet = true;
	  this.modifiedBy = modifiedBy;
	  modifiedByHasBeenSet = true;
	  this.owner = owner;
	  ownerHasBeenSet = true;
	  this.configResponseId = configResponseId;
	  configResponseIdHasBeenSet = true;
	  this.certdn = certdn;
	  certdnHasBeenSet = true;
	  this.fqdn = fqdn;
	  fqdnHasBeenSet = true;
	  this.name = name;
	  nameHasBeenSet = true;
	  this.location = location;
	  locationHasBeenSet = true;
	  this.description = description;
	  descriptionHasBeenSet = true;
	  this.cpuCount = cpuCount;
	  cpuCountHasBeenSet = true;
	  this.id = id;
	  idHasBeenSet = true;
	  this.mTime = mTime;
	  mTimeHasBeenSet = true;
	  this.cTime = cTime;
	  cTimeHasBeenSet = true;
   }

   //TODO Cloneable is better than this !
   public PlatformLightValue( PlatformLightValue otherValue )
   {
	  this.sortName = otherValue.sortName;
	  sortNameHasBeenSet = true;
	  this.commentText = otherValue.commentText;
	  commentTextHasBeenSet = true;
	  this.modifiedBy = otherValue.modifiedBy;
	  modifiedByHasBeenSet = true;
	  this.owner = otherValue.owner;
	  ownerHasBeenSet = true;
	  this.configResponseId = otherValue.configResponseId;
	  configResponseIdHasBeenSet = true;
	  this.certdn = otherValue.certdn;
	  certdnHasBeenSet = true;
	  this.fqdn = otherValue.fqdn;
	  fqdnHasBeenSet = true;
	  this.name = otherValue.name;
	  nameHasBeenSet = true;
	  this.location = otherValue.location;
	  locationHasBeenSet = true;
	  this.description = otherValue.description;
	  descriptionHasBeenSet = true;
	  this.cpuCount = otherValue.cpuCount;
	  cpuCountHasBeenSet = true;
	  this.id = otherValue.id;
	  idHasBeenSet = true;
	  this.mTime = otherValue.mTime;
	  mTimeHasBeenSet = true;
	  this.cTime = otherValue.cTime;
	  cTimeHasBeenSet = true;
	// TODO Clone is better no ?
	  this.IpValues = otherValue.IpValues;
	// TODO Clone is better no ?
	  this.PlatformType = otherValue.PlatformType;
	  PlatformTypeHasBeenSet = true;

   }

   public java.lang.String getSortName()
   {
	  return this.sortName;
   }

   public void setSortName( java.lang.String sortName )
   {
	  this.sortName = sortName;
	  sortNameHasBeenSet = true;

   }

   public boolean sortNameHasBeenSet(){
	  return sortNameHasBeenSet;
   }
   public java.lang.String getCommentText()
   {
	  return this.commentText;
   }

   public void setCommentText( java.lang.String commentText )
   {
	  this.commentText = commentText;
	  commentTextHasBeenSet = true;

   }

   public boolean commentTextHasBeenSet(){
	  return commentTextHasBeenSet;
   }
   public java.lang.String getModifiedBy()
   {
	  return this.modifiedBy;
   }

   public void setModifiedBy( java.lang.String modifiedBy )
   {
	  this.modifiedBy = modifiedBy;
	  modifiedByHasBeenSet = true;

   }

   public boolean modifiedByHasBeenSet(){
	  return modifiedByHasBeenSet;
   }
   public java.lang.String getOwner()
   {
	  return this.owner;
   }

   public void setOwner( java.lang.String owner )
   {
	  this.owner = owner;
	  ownerHasBeenSet = true;

   }

   public boolean ownerHasBeenSet(){
	  return ownerHasBeenSet;
   }
   public java.lang.Integer getConfigResponseId()
   {
	  return this.configResponseId;
   }

   public void setConfigResponseId( java.lang.Integer configResponseId )
   {
	  this.configResponseId = configResponseId;
	  configResponseIdHasBeenSet = true;

   }

   public boolean configResponseIdHasBeenSet(){
	  return configResponseIdHasBeenSet;
   }
   public java.lang.String getCertdn()
   {
	  return this.certdn;
   }

   public void setCertdn( java.lang.String certdn )
   {
	  this.certdn = certdn;
	  certdnHasBeenSet = true;

   }

   public boolean certdnHasBeenSet(){
	  return certdnHasBeenSet;
   }
   public java.lang.String getFqdn()
   {
	  return this.fqdn;
   }

   public void setFqdn( java.lang.String fqdn )
   {
	  this.fqdn = fqdn;
	  fqdnHasBeenSet = true;

   }

   public boolean fqdnHasBeenSet(){
	  return fqdnHasBeenSet;
   }
   public java.lang.String getName()
   {
	  return this.name;
   }

   public void setName( java.lang.String name )
   {
	  this.name = name;
	  nameHasBeenSet = true;

   }

   public boolean nameHasBeenSet(){
	  return nameHasBeenSet;
   }
   public java.lang.String getLocation()
   {
	  return this.location;
   }

   public void setLocation( java.lang.String location )
   {
	  this.location = location;
	  locationHasBeenSet = true;

   }

   public boolean locationHasBeenSet(){
	  return locationHasBeenSet;
   }
   public java.lang.String getDescription()
   {
	  return this.description;
   }

   public void setDescription( java.lang.String description )
   {
	  this.description = description;
	  descriptionHasBeenSet = true;

   }

   public boolean descriptionHasBeenSet(){
	  return descriptionHasBeenSet;
   }
   public java.lang.Integer getCpuCount()
   {
	  return this.cpuCount;
   }

   public void setCpuCount( java.lang.Integer cpuCount )
   {
	  this.cpuCount = cpuCount;
	  cpuCountHasBeenSet = true;

   }

   public boolean cpuCountHasBeenSet(){
	  return cpuCountHasBeenSet;
   }
   public java.lang.Integer getId()
   {
	  return this.id;
   }

   public void setId( java.lang.Integer id )
   {
	  this.id = id;
	  idHasBeenSet = true;

   }

   public boolean idHasBeenSet(){
	  return idHasBeenSet;
   }
   public java.lang.Long getMTime()
   {
	  return this.mTime;
   }

   public void setMTime( java.lang.Long mTime )
   {
	  this.mTime = mTime;
	  mTimeHasBeenSet = true;

   }

   public boolean mTimeHasBeenSet(){
	  return mTimeHasBeenSet;
   }
   public java.lang.Long getCTime()
   {
	  return this.cTime;
   }

   public void setCTime( java.lang.Long cTime )
   {
	  this.cTime = cTime;
	  cTimeHasBeenSet = true;

   }

   public boolean cTimeHasBeenSet(){
	  return cTimeHasBeenSet;
   }

   protected java.util.Collection addedIpValues = new java.util.HashSet();
   protected java.util.Collection removedIpValues = new java.util.HashSet();
   protected java.util.Collection updatedIpValues = new java.util.HashSet();

   public java.util.Collection getAddedIpValues() { return addedIpValues; }
   public java.util.Collection getRemovedIpValues() { return removedIpValues; }
   public java.util.Collection getUpdatedIpValues() { return updatedIpValues; }

   public org.hyperic.hq.appdef.shared.IpValue[] getIpValues()
   {
	  return (org.hyperic.hq.appdef.shared.IpValue[])this.IpValues.toArray(new org.hyperic.hq.appdef.shared.IpValue[IpValues.size()]);
   }

   public void addIpValue(org.hyperic.hq.appdef.shared.IpValue added)
   {
	  this.IpValues.add(added);
	  if ( ! this.addedIpValues.contains(added))
		 this.addedIpValues.add(added);
   }

   public void removeIpValue(org.hyperic.hq.appdef.shared.IpValue removed)
   {
	  this.IpValues.remove(removed);
	  this.removedIpValues.add(removed);
	  if (this.addedIpValues.contains(removed))
		 this.addedIpValues.remove(removed);
	  if (this.updatedIpValues.contains(removed))
		 this.updatedIpValues.remove(removed);
   }

   public void removeAllIpValues()
   {
        // DOH. Clear the collection - javier 2/24/03
        this.IpValues.clear();
   }

   public void updateIpValue(org.hyperic.hq.appdef.shared.IpValue updated)
   {
	  if ( ! this.updatedIpValues.contains(updated))
		 this.updatedIpValues.add(updated);
   }

   public void cleanIpValue(){
	  this.addedIpValues = new java.util.HashSet();
	  this.removedIpValues = new java.util.HashSet();
	  this.updatedIpValues = new java.util.HashSet();
   }

   public void copyIpValuesFrom(org.hyperic.hq.appdef.shared.PlatformLightValue from)
   {
	  // TODO Clone the List ????
	  this.IpValues = from.IpValues;
   }
   public org.hyperic.hq.appdef.shared.PlatformTypeValue getPlatformType()
   {
	  return this.PlatformType;
   }
   public void setPlatformType( org.hyperic.hq.appdef.shared.PlatformTypeValue PlatformType )
   {
	  this.PlatformType = PlatformType;
	  PlatformTypeHasBeenSet = true;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("sortName=" + getSortName() + " " + "commentText=" + getCommentText() + " " + "modifiedBy=" + getModifiedBy() + " " + "owner=" + getOwner() + " " + "configResponseId=" + getConfigResponseId() + " " + "certdn=" + getCertdn() + " " + "fqdn=" + getFqdn() + " " + "name=" + getName() + " " + "location=" + getLocation() + " " + "description=" + getDescription() + " " + "cpuCount=" + getCpuCount() + " " + "id=" + getId() + " " + "mTime=" + getMTime() + " " + "cTime=" + getCTime());
	  str.append('}');

	  return(str.toString());
   }

   /**
	* A Value object have an identity if its attributes making its Primary Key
	* has all been set.  One object without identity is never equal to any other
	* object.
	*
	* @return true if this instance have an identity.
	*/
   protected boolean hasIdentity()
   {
	  boolean ret = true;
	  ret = ret && idHasBeenSet;
	  return ret;
   }

   public boolean equals(Object other)
   {
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof PlatformLightValue)
	  {
		 PlatformLightValue that = (PlatformLightValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;
		 if( this.id == null )
		 {
			lEquals = lEquals && ( that.id == null );
		 }
		 else
		 {
			lEquals = lEquals && this.id.equals( that.id );
		 }

		 lEquals = lEquals && isIdentical(that);

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public boolean isIdentical(Object other)
   {
	  if (other instanceof PlatformLightValue)
	  {
		 PlatformLightValue that = (PlatformLightValue) other;
		 boolean lEquals = true;
		 if( this.sortName == null )
		 {
			lEquals = lEquals && ( that.sortName == null );
		 }
		 else
		 {
			lEquals = lEquals && this.sortName.equals( that.sortName );
		 }
		 if( this.commentText == null )
		 {
			lEquals = lEquals && ( that.commentText == null );
		 }
		 else
		 {
			lEquals = lEquals && this.commentText.equals( that.commentText );
		 }
		 if( this.modifiedBy == null )
		 {
			lEquals = lEquals && ( that.modifiedBy == null );
		 }
		 else
		 {
			lEquals = lEquals && this.modifiedBy.equals( that.modifiedBy );
		 }
		 if( this.owner == null )
		 {
			lEquals = lEquals && ( that.owner == null );
		 }
		 else
		 {
			lEquals = lEquals && this.owner.equals( that.owner );
		 }
		 if( this.configResponseId == null )
		 {
			lEquals = lEquals && ( that.configResponseId == null );
		 }
		 else
		 {
			lEquals = lEquals && this.configResponseId.equals( that.configResponseId );
		 }
		 if( this.certdn == null )
		 {
			lEquals = lEquals && ( that.certdn == null );
		 }
		 else
		 {
			lEquals = lEquals && this.certdn.equals( that.certdn );
		 }
		 if( this.fqdn == null )
		 {
			lEquals = lEquals && ( that.fqdn == null );
		 }
		 else
		 {
			lEquals = lEquals && this.fqdn.equals( that.fqdn );
		 }
		 if( this.name == null )
		 {
			lEquals = lEquals && ( that.name == null );
		 }
		 else
		 {
			lEquals = lEquals && this.name.equals( that.name );
		 }
		 if( this.location == null )
		 {
			lEquals = lEquals && ( that.location == null );
		 }
		 else
		 {
			lEquals = lEquals && this.location.equals( that.location );
		 }
		 if( this.description == null )
		 {
			lEquals = lEquals && ( that.description == null );
		 }
		 else
		 {
			lEquals = lEquals && this.description.equals( that.description );
		 }
		 if( this.cpuCount == null )
		 {
			lEquals = lEquals && ( that.cpuCount == null );
		 }
		 else
		 {
			lEquals = lEquals && this.cpuCount.equals( that.cpuCount );
		 }
		 if( this.mTime == null )
		 {
			lEquals = lEquals && ( that.mTime == null );
		 }
		 else
		 {
			lEquals = lEquals && this.mTime.equals( that.mTime );
		 }
		 if( this.cTime == null )
		 {
			lEquals = lEquals && ( that.cTime == null );
		 }
		 else
		 {
			lEquals = lEquals && this.cTime.equals( that.cTime );
		 }
		 if( this.getIpValues() == null )
		 {
			lEquals = lEquals && ( that.getIpValues() == null );
		 }
		 else
		 {
            // XXX Covalent Custom - dont compare the arrays, as order is not significant. ever.    
            // - javier 7/16/03
            java.util.Collection cmr1 = java.util.Arrays.asList(this.getIpValues());
            java.util.Collection cmr2 = java.util.Arrays.asList(that.getIpValues());
			// lEquals = lEquals && java.util.Arrays.equals(this.getIpValues() , that.getIpValues()) ;
            lEquals = lEquals && cmr1.containsAll(cmr2);
		 }
		 if( this.PlatformType == null )
		 {
			lEquals = lEquals && ( that.PlatformType == null );
		 }
		 else
		 {
			lEquals = lEquals && this.PlatformType.equals( that.PlatformType );
		 }

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public int hashCode(){
	  int result = 17;
      result = 37*result + ((this.sortName != null) ? this.sortName.hashCode() : 0);

      result = 37*result + ((this.commentText != null) ? this.commentText.hashCode() : 0);

      result = 37*result + ((this.modifiedBy != null) ? this.modifiedBy.hashCode() : 0);

      result = 37*result + ((this.owner != null) ? this.owner.hashCode() : 0);

      result = 37*result + ((this.configResponseId != null) ? this.configResponseId.hashCode() : 0);

      result = 37*result + ((this.certdn != null) ? this.certdn.hashCode() : 0);

      result = 37*result + ((this.fqdn != null) ? this.fqdn.hashCode() : 0);

      result = 37*result + ((this.name != null) ? this.name.hashCode() : 0);

      result = 37*result + ((this.location != null) ? this.location.hashCode() : 0);

      result = 37*result + ((this.description != null) ? this.description.hashCode() : 0);

      result = 37*result + ((this.cpuCount != null) ? this.cpuCount.hashCode() : 0);

      result = 37*result + ((this.id != null) ? this.id.hashCode() : 0);

      result = 37*result + ((this.mTime != null) ? this.mTime.hashCode() : 0);

      result = 37*result + ((this.cTime != null) ? this.cTime.hashCode() : 0);

	  result = 37*result + ((this.getIpValues() != null) ? this.getIpValues().hashCode() : 0);
	  result = 37*result + ((this.PlatformType != null) ? this.PlatformType.hashCode() : 0);
	  return result;
   }

}

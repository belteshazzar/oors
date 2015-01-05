package org.oors;

import java.util.Date;

import javax.persistence.TypedQuery;
import javax.swing.text.html.HTMLDocument;

public abstract class Base extends OorsEventGenerator {

	private static final String NULL_ATTRIBUTE = "Null attribute parameter";
	private static final String INVALID_VALUE_TYPE = "Attribute value type incorrect";
	private static final String INVALID_FOR_TYPE = "Attribute for type incorrect";
	private static final String NULL_VALUE = "Null value passed";
	
	abstract long getId();
	abstract ProjectBranch getProjectBranch();
	
	public boolean getBooleanValue( Attribute attribute ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.BOOLEAN ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);
		
		TypedQuery<AttributeBooleanValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeBooleanValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeBooleanValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			return q.getSingleResult().getValue();
		}
		catch ( Exception ex )
		{
			AttributeBooleanValue asv = new AttributeBooleanValue(attribute,this.getId(),false);
			DataSource.getInstance().persist(asv);
			return asv.getValue();
		}
	}

	public void setBooleanValue( Attribute attribute, boolean value ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.BOOLEAN ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);

		TypedQuery<AttributeBooleanValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeBooleanValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeBooleanValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			q.getSingleResult().setValue(value);
		}
		catch ( Exception ex )
		{
			AttributeBooleanValue asv = new AttributeBooleanValue(attribute,this.getId(),value);
			DataSource.getInstance().persist(asv);
			asv.setValue(value);
		}
	}

	public Date getDateValue( Attribute attribute ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.DATE ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);
		
		TypedQuery<AttributeDateValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeDateValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeDateValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			return q.getSingleResult().getValue();
		}
		catch ( Exception ex )
		{
			AttributeDateValue asv = new AttributeDateValue(attribute,this.getId(),new Date());
			DataSource.getInstance().persist(asv);
			return asv.getValue();
		}
	}

	public void setDateValue( Attribute attribute, Date value ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.DATE ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);

		TypedQuery<AttributeDateValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeDateValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeDateValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			q.getSingleResult().setValue(value);
		}
		catch ( Exception ex )
		{
			AttributeDateValue asv = new AttributeDateValue(attribute,this.getId(),value);
			DataSource.getInstance().persist(asv);
			asv.setValue(value);
		}
	}

	public String getStringValue( Attribute attribute ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.STRING ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);
		
		TypedQuery<AttributeStringValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeStringValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeStringValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			return q.getSingleResult().getValue();
		}
		catch ( Exception ex )
		{
			AttributeStringValue asv = new AttributeStringValue(attribute,this.getId(),"Default");
			DataSource.getInstance().persist(asv);
			return asv.getValue();
		}
	}

	public void setStringValue( Attribute attribute, String value ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.STRING ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);
		if ( value==null ) throw new AttributeException(NULL_VALUE);

		TypedQuery<AttributeStringValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeStringValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeStringValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			q.getSingleResult().setValue(value);
		}
		catch ( Exception ex )
		{
			AttributeStringValue asv = new AttributeStringValue(attribute,this.getId(),value);
			DataSource.getInstance().persist(asv);
			asv.setValue(value);
		}
	}

	public javax.swing.text.html.HTMLDocument getHTMLValue( Attribute attribute ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.HTML ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);
		
		TypedQuery<AttributeHTMLValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeHTMLValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeHTMLValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			return q.getSingleResult().getValue();
		}
		catch ( Exception ex )
		{
			AttributeHTMLValue asv = new AttributeHTMLValue(attribute,this.getId(),new javax.swing.text.html.HTMLDocument());
			DataSource.getInstance().persist(asv);
			return asv.getValue();
		}
	}

	public void setHTMLValue( Attribute attribute, javax.swing.text.html.HTMLDocument value ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.HTML ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);

		TypedQuery<AttributeHTMLValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeHTMLValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeHTMLValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			q.getSingleResult().setValue(value);
		}
		catch ( Exception ex )
		{
			AttributeHTMLValue asv = new AttributeHTMLValue(attribute,this.getId(),value);
			DataSource.getInstance().persist(asv);
			asv.setValue(value);
		}
	}
	public double getNumberValue( Attribute attribute ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.NUMBER ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);
		
		TypedQuery<AttributeNumberValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeNumberValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeNumberValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			return q.getSingleResult().getValue();
		}
		catch ( Exception ex )
		{
			AttributeNumberValue asv = new AttributeNumberValue(attribute,this.getId(),0.0);
			DataSource.getInstance().persist(asv);
			return asv.getValue();
		}
	}

	public void setNumberValue( Attribute attribute, double value ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( attribute.valueType!=AttributeType.NUMBER ) throw new AttributeException(INVALID_VALUE_TYPE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);

		TypedQuery<AttributeNumberValue> q = DataSource
				.getInstance()
				.entityManager
				.createQuery("FROM AttributeNumberValue v WHERE v.projectBranchId = :pb AND v.attributeId = :aid AND v.valueForId = :id",AttributeNumberValue.class);
		q.setParameter("pb", this.getProjectBranch().id);
		q.setParameter("aid", attribute.id);
		q.setParameter("id", this.getId());

		try
		{
			q.getSingleResult().setValue(value);
		}
		catch ( Exception ex )
		{
			AttributeNumberValue asv = new AttributeNumberValue(attribute,this.getId(),value);
			DataSource.getInstance().persist(asv);
			asv.setValue(value);
		}
	}

	public Object getValue( Attribute attribute ) throws AttributeException
	{
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE+", Expected: " +attribute.getForType()+", Found: "+this.getClass());
		
		switch ( attribute.valueType )
		{
			case BOOLEAN: return this.getBooleanValue(attribute);
			case DATE: return this.getDateValue(attribute);
			case HTML: return this.getHTMLValue(attribute);
			case NUMBER: return this.getNumberValue(attribute);
			case STRING: return this.getStringValue(attribute);
			//case USER: return this.getUserValue(attribute);
			//case USERGROUP: return this.getUserGroupValue(attribute);
			default: return null;
		}
	}

	public void setValue(Attribute attribute, Object value) throws AttributeException {
		if ( attribute==null ) throw new AttributeException(NULL_ATTRIBUTE);
		if ( this.getClass().hashCode()!=attribute.getForType().getHash() ) throw new AttributeException(INVALID_FOR_TYPE);
		
		try
		{
			switch ( attribute.valueType )
			{
				case BOOLEAN: this.setBooleanValue(attribute,(Boolean)value);
				case DATE: this.setDateValue(attribute,(Date)value);
				case HTML: this.setHTMLValue(attribute,(HTMLDocument)value);
				case NUMBER: this.setNumberValue(attribute,(Double)value);
				case STRING: this.setStringValue(attribute,(String)value);
				//case USER: this.getUserValue(attribute,value);
				//case USERGROUP: this.getUserGroupValue(attribute,value);
			}
			
		}
		catch ( ClassCastException ccex )
		{
			throw new AttributeException(ccex.getMessage());
		}
	}
}

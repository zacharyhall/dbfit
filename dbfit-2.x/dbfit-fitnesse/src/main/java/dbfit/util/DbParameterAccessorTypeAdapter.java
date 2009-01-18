package dbfit.util;


import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fit.Fixture;
import fit.TypeAdapter;
public class DbParameterAccessorTypeAdapter extends TypeAdapter {	
	private DbParameterAccessor parameterAccessor;
	public DbParameterAccessorTypeAdapter(DbParameterAccessor accessor,Fixture f){
		this.fixture=f;
		this.type=accessor.javaType;
		this.parameterAccessor=accessor;		
	}
	@Override
	public Object get() throws IllegalAccessException,
			InvocationTargetException {
		return parameterAccessor.get();
	}
	@Override
	public void set(Object value) throws Exception {
		parameterAccessor.set(value);
	}
	@Override
	public Object parse(String s) throws Exception {
		return new ParseHelper(this.fixture,this.type).parse(s);
	}
}

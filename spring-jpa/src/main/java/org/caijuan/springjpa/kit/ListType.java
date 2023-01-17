package org.caijuan.springjpa.kit;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ListType<T> extends BaseJsonType {

    private Class<T> innerClass;

    public ListType(Class<T> innerClass) {
        this.innerClass = innerClass;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object owner) throws SQLException {
        Object obj = rs.getObject(names[0]);
        if (obj == null) return null;
        PGobject o = (PGobject) obj;
        if (o.getValue() != null) {
            List<T> listValue = JsonKit.parse2Object(o.getValue(), new TypeReference<List<T>>() {
            });
            if (innerClass != null)
                return listValue.stream().map(v -> JsonKit.parse2Object(JsonKit.parse2Json(v), innerClass)).collect(Collectors.toList());
            return listValue;
        }
        return new ArrayList<T>();
    }

    @Override
    public Class returnedClass() {
        return List.class;
    }

    @Override
    public Object deepCopy(Object originalValue) {
        if (originalValue != null) {
            List<T> copiedValue = JsonKit.parse2Object(originalValue instanceof String ? (String) originalValue : JsonKit.parse2Json(originalValue), new TypeReference<List<T>>() {
            });
            if (innerClass != null)
                return copiedValue.stream().map(v -> JsonKit.parse2Object(JsonKit.parse2Json(v), innerClass)).collect(Collectors.toList());
            return copiedValue;
        }
        return null;
    }
}
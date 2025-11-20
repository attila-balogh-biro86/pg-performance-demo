package com.banfico.serialization;

import com.blazebit.persistence.Keyset;
import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;
import java.util.Base64;

public class KeySetSerializationUtil {

    public static String serialize( Keyset keyset ) {
        // keyset.getTuple() returns Object[] of values
        try {
            byte[] bytes = SerializationUtils.serialize( keyset.getTuple() );
            return Base64.getUrlEncoder().withoutPadding().encodeToString( bytes );
        } catch( Exception e ) {
            throw new IllegalStateException("Failed to serialize keyset", e);
        }
    }

    public static Serializable [] deserialize(String token ) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(token);
            return (Serializable[]) SerializationUtils.deserialize( bytes );
        } catch(Exception e) {
            throw new IllegalArgumentException("Invalid keyset token", e);
        }
    }
}


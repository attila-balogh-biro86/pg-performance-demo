package com.banfico;

import com.banfico.serialization.KeySetSerializationUtil;
import com.blazebit.persistence.DefaultKeyset;
import com.blazebit.persistence.Keyset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.Serializable;
import java.time.OffsetDateTime;

@SpringBootTest
public class KeySetSerializationTest {

    @Test
    public void testKeySetSerialization() throws Exception {
        Serializable[] low = new Serializable[]{
                "000d235d-0b94-4b24-8a69-29b3991f6bd9",
                "2024-03-23T23:29:02.23885Z"
        };

        Serializable[] high = new Serializable[]{
                "000ec784-6a23-4b17-b6c3-6341d138f650",
                "2024-04-06T23:34:00.194202Z"
        };

        Keyset lowKeySet = new DefaultKeyset(low);
        String lowSerializedKeySet = KeySetSerializationUtil.serialize(lowKeySet);
        Assertions.assertNotNull(lowSerializedKeySet);
        Serializable[] lowDecoded = KeySetSerializationUtil.deserialize("rO0ABXVyABdbTGphdmEuaW8uU2VyaWFsaXphYmxlO67QCaxT1-1JAgAAeHAAAAACdAAkMDAwZDIzNWQtMGI5NC00YjI0LThhNjktMjliMzk5MWY2YmQ5dAAaMjAyNC0wMy0yM1QyMzoyOTowMi4yMzg4NVo");
        Assertions.assertArrayEquals(low, lowDecoded);
        OffsetDateTime lowDate = OffsetDateTime.parse(low[1].toString());
        Assertions.assertNotNull(lowDate);


        Keyset highKeySet = new DefaultKeyset(high);
        String highSerializedKeySet = KeySetSerializationUtil.serialize(highKeySet);
        Assertions.assertNotNull(highSerializedKeySet);
        Serializable[] highDecoded = KeySetSerializationUtil.deserialize("rO0ABXVyABdbTGphdmEuaW8uU2VyaWFsaXphYmxlO67QCaxT1-1JAgAAeHAAAAACdAAkMDAwZWM3ODQtNmEyMy00YjE3LWI2YzMtNjM0MWQxMzhmNjUwdAAbMjAyNC0wNC0wNlQyMzozNDowMC4xOTQyMDJa");
        Assertions.assertArrayEquals(high, highDecoded);
        OffsetDateTime highDate = OffsetDateTime.parse(high[1].toString());
        Assertions.assertNotNull(highDate);
    }
}

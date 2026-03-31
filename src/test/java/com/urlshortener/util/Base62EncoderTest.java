package com.urlshortener.util;

import com.getflip.urlshortener.util.Base62Encoder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base62EncoderTest {

    @Test
    void testEncode() {
        assertEquals("a", Base62Encoder.encode(0L));
        assertEquals("dnh", Base62Encoder.encode(12345L));
    }
}

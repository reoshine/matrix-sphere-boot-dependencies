package com.roshine.adp.base.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2022-07-08 21:29
 * @description BigDecimal序列化器
 */
public class BigDecimalStringSerializer extends StdSerializer<BigDecimal> {

    final static BigDecimalStringSerializer instance = new BigDecimalStringSerializer();

    private static final long serialVersionUID = 4529402653219649792L;

    private BigDecimalStringSerializer() {
        super(BigDecimal.class);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeString("0");
        } else {
            String val = value.setScale(2, RoundingMode.HALF_UP).toPlainString();
            gen.writeString(val);
        }
    }
}

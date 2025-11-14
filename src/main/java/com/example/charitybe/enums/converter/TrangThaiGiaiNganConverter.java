package com.example.charitybe.enums.converter;

import com.example.charitybe.enums.TrangThaiGiaiNgan;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiGiaiNganConverter implements AttributeConverter<TrangThaiGiaiNgan, String> {

    @Override
    public String convertToDatabaseColumn(TrangThaiGiaiNgan attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public TrangThaiGiaiNgan convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TrangThaiGiaiNgan.fromValue(dbData);
    }
}

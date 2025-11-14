package com.example.charitybe.enums.converter;

import com.example.charitybe.enums.LoaiGiaiNgan;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LoaiGiaiNganConverter implements AttributeConverter<LoaiGiaiNgan, String> {

    @Override
    public String convertToDatabaseColumn(LoaiGiaiNgan attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public LoaiGiaiNgan convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return LoaiGiaiNgan.fromValue(dbData);
    }
}

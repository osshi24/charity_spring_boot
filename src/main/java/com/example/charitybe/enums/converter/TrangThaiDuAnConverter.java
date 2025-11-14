package com.example.charitybe.enums.converter;

import com.example.charitybe.enums.TrangThaiDuAn;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiDuAnConverter implements AttributeConverter<TrangThaiDuAn, String> {

    @Override
    public String convertToDatabaseColumn(TrangThaiDuAn attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public TrangThaiDuAn convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TrangThaiDuAn.fromValue(dbData);
    }
}

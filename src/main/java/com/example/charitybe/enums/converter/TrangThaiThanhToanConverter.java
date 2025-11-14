package com.example.charitybe.enums.converter;

import com.example.charitybe.enums.TrangThaiThanhToan;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiThanhToanConverter implements AttributeConverter<TrangThaiThanhToan, String> {

    @Override
    public String convertToDatabaseColumn(TrangThaiThanhToan attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public TrangThaiThanhToan convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TrangThaiThanhToan.fromValue(dbData);
    }
}

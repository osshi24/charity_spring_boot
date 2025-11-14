package com.example.charitybe.enums.converter;

import com.example.charitybe.enums.PhuongThucThanhToan;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PhuongThucThanhToanConverter implements AttributeConverter<PhuongThucThanhToan, String> {

    @Override
    public String convertToDatabaseColumn(PhuongThucThanhToan attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public PhuongThucThanhToan convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return PhuongThucThanhToan.fromValue(dbData);
    }
}

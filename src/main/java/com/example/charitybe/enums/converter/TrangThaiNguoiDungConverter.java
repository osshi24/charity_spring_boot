package com.example.charitybe.enums.converter;

import com.example.charitybe.enums.TrangThaiNguoiDung;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiNguoiDungConverter implements AttributeConverter<TrangThaiNguoiDung, String> {

    @Override
    public String convertToDatabaseColumn(TrangThaiNguoiDung attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public TrangThaiNguoiDung convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TrangThaiNguoiDung.fromValue(dbData);
    }
}

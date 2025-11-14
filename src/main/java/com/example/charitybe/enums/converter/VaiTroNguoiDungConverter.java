package com.example.charitybe.enums.converter;

import com.example.charitybe.enums.VaiTroNguoiDung;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VaiTroNguoiDungConverter implements AttributeConverter<VaiTroNguoiDung, String> {

    @Override
    public String convertToDatabaseColumn(VaiTroNguoiDung attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public VaiTroNguoiDung convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return VaiTroNguoiDung.fromValue(dbData);
    }
}

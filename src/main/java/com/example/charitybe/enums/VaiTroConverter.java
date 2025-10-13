// package com.example.charitybe.enums;

// import jakarta.persistence.AttributeConverter;
// import jakarta.persistence.Converter;

// @Converter
// public class VaiTroConverter implements AttributeConverter<VaiTroEnum, Object> {
    
//     @Override
//     public Object convertToDatabaseColumn(VaiTroEnum attribute) {
//         if (attribute == null) {
//             return null;
//         }
//         return attribute.name().toLowerCase();
//     }

//     @Override
//     public VaiTroEnum convertToEntityAttribute(Object dbData) {
//         if (dbData == null) {
//             return null;
//         }
//         String strValue = dbData.toString();
//         if (strValue.isEmpty()) {
//             return null;
//         }
//         return VaiTroEnum.valueOf(strValue.toUpperCase());
//     }
// }
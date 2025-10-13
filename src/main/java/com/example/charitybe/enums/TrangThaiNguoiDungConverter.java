// package com.example.charitybe.enums;

// import jakarta.persistence.AttributeConverter;
// import jakarta.persistence.Converter;

// @Converter
// public class TrangThaiNguoiDungConverter implements AttributeConverter<TrangThaiNguoiDungEnum, Object> {
    
//     @Override
//     public Object convertToDatabaseColumn(TrangThaiNguoiDungEnum attribute) {
//         if (attribute == null) {
//             return null;
//         }
//         // Trả về tên enum với lowercase, Hibernate sẽ tự cast
//         return attribute.name().toLowerCase();
//     }

//     @Override
//     public TrangThaiNguoiDungEnum convertToEntityAttribute(Object dbData) {
//         if (dbData == null) {
//             return null;
//         }
//         String strValue = dbData.toString();
//         if (strValue.isEmpty()) {
//             return null;
//         }
//         return TrangThaiNguoiDungEnum.valueOf(strValue.toUpperCase());
//     }
// }
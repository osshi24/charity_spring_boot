package com.example.charitybe.enums;

public enum PhuongThucThanhToan {
    VNPAY("vnpay"),
    MOMO("momo"),
    CHUYEN_KHOAN("chuyen_khoan");

    private final String value;

    PhuongThucThanhToan(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PhuongThucThanhToan fromValue(String value) {
        for (PhuongThucThanhToan method : PhuongThucThanhToan.values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

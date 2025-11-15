package com.example.charitybe.Services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class PostgRestPermissionInterceptor {

    public boolean hasPermission(HttpServletRequest request, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        String method = request.getMethod();
        String path = request.getRequestURI();
        String role = auth.getAuthorities().isEmpty() ? "" :
                     auth.getAuthorities().iterator().next().getAuthority();

        // ===== DỰ ÁN (du_an) =====
        if (path.contains("/du_an")) {
            if ("POST".equals(method)) {
                // Chỉ Admin và Operator mới tạo được dự án
                return "quan_tri_vien".equals(role) || "dieu_hanh_vien".equals(role);
            }
            if ("DELETE".equals(method)) {
                // Chỉ Admin mới xóa được
                return "quan_tri_vien".equals(role);
            }
            if ("PUT".equals(method) || "PATCH".equals(method)) {
                // Admin, Operator có thể update
                // TODO: Check if user is owner of project
                return "quan_tri_vien".equals(role) || "dieu_hanh_vien".equals(role);
            }
        }

        // ===== QUYÊN GÓP (quyen_gop) =====
        if (path.contains("/quyen_gop")) {
            if ("POST".equals(method)) {
                System.out.println("222222222222222222222222222222222222");
                // Anyone authenticated có thể quyên góp
                return true;
            }
            if ("PUT".equals(method) || "PATCH".equals(method) || "DELETE".equals(method)) {
                // Chỉ Admin mới sửa/xóa quyên góp
                return "quan_tri_vien".equals(role);
            }
        }

        // ===== NGƯỜI DÙNG (nguoi_dung) =====
        if (path.contains("/nguoi_dung")) {
            if ("DELETE".equals(method) || "PATCH".equals(method)) {
                // Chỉ Admin mới xóa/sửa user
                return "quan_tri_vien".equals(role);
            }
        }

        // ===== GIẢI NGÂN (giai_ngan) =====
        if (path.contains("/giai_ngan")) {
            if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) {
                // Admin và Operator mới tạo/sửa giải ngân
                return "quan_tri_vien".equals(role) || "dieu_hanh_vien".equals(role);
            }
            if ("DELETE".equals(method)) {
                // Chỉ Admin
                return "quan_tri_vien".equals(role);
            }
        }

        // ===== TIN TỨC (tin_tuc) =====
        if (path.contains("/tin_tuc")) {
            if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method) || "DELETE".equals(method)) {
                // Admin và Operator mới tạo/sửa/xóa tin tức
                return "quan_tri_vien".equals(role) || "dieu_hanh_vien".equals(role);
            }
        }

        // ===== SỰ KIỆN (su_kien) =====
        if (path.contains("/su_kien")) {
            if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method) || "DELETE".equals(method)) {
                // Admin và Operator mới tạo/sửa/xóa sự kiện
                return "quan_tri_vien".equals(role) || "dieu_hanh_vien".equals(role);
            }
        }

        // ===== TÀI KHOẢN THANH TOÁN (tai_khoan_thanh_toan) =====
        if (path.contains("/tai_khoan_thanh_toan")) {
            if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method) || "DELETE".equals(method)) {
                // Admin và Operator
                return "quan_tri_vien".equals(role) || "dieu_hanh_vien".equals(role);
            }
        }

        // ===== CẬP NHẬT DỰ ÁN (cap_nhat_du_an) =====
        if (path.contains("/cap_nhat_du_an")) {
            if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method) || "DELETE".equals(method)) {
                // Admin và Operator
                return "quan_tri_vien".equals(role) || "dieu_hanh_vien".equals(role);
            }
        }

        // Default: cho phép GET requests và các requests khác
        return true;
    }
}

// package com.example.charitybe.filter;


// import com.google.common.util.concurrent.RateLimiter;
// import jakarta.servlet.*;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// import java.io.IOException;

// @Component
// public class ApiRateLimitingFilter implements Filter {

//     private final RateLimiter rateLimiter;
//     private final boolean isEnabled;

//     // Inject RateLimiter Bean đã tạo ở bước 2
//     public ApiRateLimitingFilter(
//         RateLimiter rateLimiter,
//         @Value("${app.rate-limit.enable:false}") boolean isEnabled) {
        
//         this.rateLimiter = rateLimiter;
//         this.isEnabled = isEnabled;
//     }

//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
//         throws IOException, ServletException {
        
//         // Bỏ qua Rate Limit nếu cấu hình tắt
//         if (!isEnabled) {
//             chain.doFilter(request, response);
//             return;
//         }

//         // 1. Cố gắng lấy token (acquire). Nếu có token -> trả về true
//         // Phương thức tryAcquire() không chặn, nên phản hồi nhanh chóng
//         if (rateLimiter.tryAcquire()) {
//             // 2. Thành công: Tiếp tục xử lý yêu cầu (cho phép qua)
//             chain.doFilter(request, response);
//         } else {
//             // 3. Thất bại: Đã vượt quá giới hạn tốc độ
//             HttpServletResponse httpResponse = (HttpServletResponse) response;
            
//             // Trả về mã lỗi 429 Too Many Requests
//             httpResponse.setStatus(429); 
//             httpResponse.getWriter().write("Too Many Requests: Rate limit exceeded.");
//         }
//     }
// }
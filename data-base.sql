-- ============================================
-- COMPLETE DATABASE SETUP SCRIPT
-- PostgreSQL - Crowdfunding Platform
-- Có thể chạy lại nhiều lần (DROP IF EXISTS)
-- ============================================

-- DROP các bảng theo thứ tự ngược lại (từ child đến parent)
DROP TABLE IF EXISTS "dang_ky_su_kien" CASCADE;
DROP TABLE IF EXISTS "lien_he" CASCADE;
DROP TABLE IF EXISTS "cap_nhat_du_an" CASCADE;
DROP TABLE IF EXISTS "su_kien" CASCADE;
DROP TABLE IF EXISTS "tin_tuc" CASCADE;
DROP TABLE IF EXISTS "chi_tiet_giai_ngan" CASCADE;
DROP TABLE IF EXISTS "giai_ngan" CASCADE;
DROP TABLE IF EXISTS "quyen_gop" CASCADE;
DROP TABLE IF EXISTS "tai_khoan_thanh_toan" CASCADE;
DROP TABLE IF EXISTS "du_an" CASCADE;
DROP TABLE IF EXISTS "danh_muc_du_an" CASCADE;
DROP TABLE IF EXISTS "nguoi_dung" CASCADE;

-- DROP các ENUM types
DROP TYPE IF EXISTS "loai_giai_ngan" CASCADE;
DROP TYPE IF EXISTS "trang_thai_giai_ngan" CASCADE;
DROP TYPE IF EXISTS "trang_thai_thanh_toan" CASCADE;
DROP TYPE IF EXISTS "phuong_thuc_thanh_toan" CASCADE;
DROP TYPE IF EXISTS "trang_thai_du_an" CASCADE;
DROP TYPE IF EXISTS "trang_thai_nguoi_dung" CASCADE;
DROP TYPE IF EXISTS "vai_tro_nguoi_dung" CASCADE;

-- ============================================
-- TẠO CÁC ENUM TYPES
-- ============================================

CREATE TYPE "vai_tro_nguoi_dung" AS ENUM (
  'quan_tri_vien',
  'dieu_hanh_vien',
  'nguoi_dung',
  'tinh_nguyen_vien'
);

CREATE TYPE "trang_thai_nguoi_dung" AS ENUM (
  'hoat_dong',
  'khong_hoat_dong'
);

CREATE TYPE "trang_thai_du_an" AS ENUM (
  'ban_nhap',
  'hoat_dong',
  'hoan_thanh',
  'huy_bo'
);

CREATE TYPE "phuong_thuc_thanh_toan" AS ENUM (
  'vnpay',
  'momo',
  'chuyen_khoan'
);

CREATE TYPE "trang_thai_thanh_toan" AS ENUM (
  'cho_xu_ly',
  'thanh_cong',
  'that_bai'
);

CREATE TYPE "trang_thai_giai_ngan" AS ENUM (
  'cho_duyet',
  'da_duyet',
  'tu_choi'
);

CREATE TYPE "loai_giai_ngan" AS ENUM (
  'tien_mat',
  'chuyen_khoan'
);

-- ============================================
-- TẠO CÁC TABLES
-- ============================================

CREATE TABLE "nguoi_dung" (
  "id" BIGSERIAL PRIMARY KEY,
  "email" VARCHAR(255) UNIQUE NOT NULL,
  "mat_khau_hash" VARCHAR(255) NOT NULL,
  "ten" VARCHAR(100) NOT NULL,
  "ho" VARCHAR(100) NOT NULL,
  "so_dien_thoai" VARCHAR(20),
  "dia_chi" TEXT,
  "vai_tro" vai_tro_nguoi_dung DEFAULT 'nguoi_dung',
  "trang_thai" trang_thai_nguoi_dung DEFAULT 'hoat_dong',
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "danh_muc_du_an" (
  "id" SERIAL PRIMARY KEY,
  "ten" VARCHAR(255) NOT NULL,
  "mo_ta" TEXT,
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "du_an" (
  "id" BIGSERIAL PRIMARY KEY,
  "tieu_de" VARCHAR(255) NOT NULL,
  "mo_ta" TEXT NOT NULL,
  "mo_ta_chi_tiet" TEXT NOT NULL,
  "ma_danh_muc" INTEGER,
  "so_tien_muc_tieu" DECIMAL(15,2) DEFAULT 0,
  "so_tien_hien_tai" DECIMAL(15,2) DEFAULT 0,
  "ngay_bat_dau" DATE NOT NULL,
  "ngay_ket_thuc" DATE NOT NULL,
  "dia_diem" TEXT NOT NULL,
  "trang_thai" trang_thai_du_an DEFAULT 'ban_nhap',
  "thu_vien_anh" VARCHAR(500),
  "muc_do_yeu_tien" INTEGER,
  "nguoi_tao" BIGINT,
  "nguoi_phe_duyet" VARCHAR(500),
  "ngay_cap_nhat" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP),
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "tai_khoan_thanh_toan" (
  "id" BIGSERIAL PRIMARY KEY,
  "ma_du_an" BIGINT NOT NULL,
  "ten_tai_khoan" VARCHAR(255) NOT NULL,
  "so_tai_khoan" VARCHAR(50) NOT NULL,
  "ten_ngan_hang" VARCHAR(255) NOT NULL,
  "chi_nhanh_ngan_hang" VARCHAR(255),
  "la_tai_khoan_mac_dinh" BOOLEAN DEFAULT true,
  "trang_thai" VARCHAR(20) DEFAULT 'hoat_dong',
  "ghi_chu" TEXT,
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "quyen_gop" (
  "id" BIGSERIAL PRIMARY KEY,
  "ma_nguoi_dung" BIGINT,
  "ma_du_an" BIGINT NOT NULL,
  "so_tien" DECIMAL(15,2) NOT NULL,
  "don_vi_tien_te" VARCHAR(255),
  "phuong_thuc_thanh_toan" phuong_thuc_thanh_toan NOT NULL,
  "trang_thai_" trang_thai_thanh_toan DEFAULT 'cho_xu_ly',
  "ma_giao_dich" VARCHAR(255) UNIQUE,
  "loi_nhan" VARCHAR(255),
  "phi_giao_dich" INTEGER,
  "so_tien_thuc" INTEGER,
  "ngay_cap_nhat" TIMESTAMPTZ,
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "giai_ngan" (
  "id" BIGSERIAL PRIMARY KEY,
  "ma_du_an" BIGINT,
  "ma_tai_khoan_du_an" BIGINT,
  "so_tien" DECIMAL(15,2) NOT NULL,
  "loai_giai_ngan" loai_giai_ngan NOT NULL,
  "ngay_giai_ngan" DATE NOT NULL,
  "nguoi_nhan" VARCHAR(255) NOT NULL,
  "thong_tin_nguoi_nhan" JSONB,
  "muc_dich_su_dung" TEXT NOT NULL,
  "tai_lieu_chung_minh" JSONB,
  "nguoi_giai_ngan" BIGINT,
  "nguoi_duyet" BIGINT,
  "ghi_chu" TEXT,
  "trang_thai" trang_thai_giai_ngan DEFAULT 'cho_duyet',
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "chi_tiet_giai_ngan" (
  "id" BIGSERIAL PRIMARY KEY,
  "ma_giai_ngan" BIGINT,
  "so_tien" DECIMAL(15,2) NOT NULL,
  "mo_ta" TEXT
);

CREATE TABLE "tin_tuc" (
  "id" BIGSERIAL PRIMARY KEY,
  "tieu_de" VARCHAR(255) NOT NULL,
  "noi_dung" TEXT NOT NULL,
  "ma_tac_gia" BIGINT,
  "anh_dai_dien" VARCHAR(500),
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE public.refresh_token (
  "id" BIGSERIAL PRIMARY KEY,
  "token" VARCHAR(500) UNIQUE NOT NULL,
  "user_id" BIGINT NOT NULL,
  "expiry_date" TIMESTAMPTZ NOT NULL,
  "revoked" BOOLEAN DEFAULT FALSE,
  "created_at" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "su_kien" (
  "id" BIGSERIAL PRIMARY KEY,
  "tieu_de" VARCHAR(255) NOT NULL,
  "mo_ta" TEXT NOT NULL,
  "thoi_gian_bat_dau" TIMESTAMPTZ NOT NULL,
  "dia_diem" VARCHAR(255) NOT NULL,
  "nguoi_tao" BIGINT,
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "cap_nhat_du_an" (
  "id" BIGSERIAL PRIMARY KEY,
  "ma_du_an" BIGINT,
  "tieu_de" VARCHAR(255) NOT NULL,
  "noi_dung" TEXT NOT NULL,
  "nguoi_tao" BIGINT,
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "lien_he" (
  "id" BIGSERIAL PRIMARY KEY,
  "ten_nguoi_gui" VARCHAR(255) NOT NULL,
  "email" VARCHAR(255) NOT NULL,
  "tieu_de" VARCHAR(255) NOT NULL,
  "noi_dung" TEXT NOT NULL,
  "ngay_tao" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE "dang_ky_su_kien" (
  "id" BIGSERIAL PRIMARY KEY,
  "ma_su_kien" BIGINT,
  "ma_nguoi_dung" BIGINT,
  "ten_nguoi_dang_ky" VARCHAR(255),
  "email_nguoi_dang_ky" VARCHAR(255),
  "ngay_dang_ky" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP)
);

-- ============================================
-- TẠO FOREIGN KEYS
-- ============================================

ALTER TABLE "du_an" ADD FOREIGN KEY ("ma_danh_muc") REFERENCES "danh_muc_du_an" ("id");
ALTER TABLE "du_an" ADD FOREIGN KEY ("nguoi_tao") REFERENCES "nguoi_dung" ("id");

ALTER TABLE "tai_khoan_thanh_toan" ADD FOREIGN KEY ("ma_du_an") REFERENCES "du_an" ("id");

ALTER TABLE "quyen_gop" ADD FOREIGN KEY ("ma_nguoi_dung") REFERENCES "nguoi_dung" ("id");
ALTER TABLE "quyen_gop" ADD FOREIGN KEY ("ma_du_an") REFERENCES "du_an" ("id");

ALTER TABLE "giai_ngan" ADD FOREIGN KEY ("ma_du_an") REFERENCES "du_an" ("id");
ALTER TABLE "giai_ngan" ADD FOREIGN KEY ("ma_tai_khoan_du_an") REFERENCES "tai_khoan_thanh_toan" ("id");
ALTER TABLE "giai_ngan" ADD FOREIGN KEY ("nguoi_giai_ngan") REFERENCES "nguoi_dung" ("id");
ALTER TABLE "giai_ngan" ADD FOREIGN KEY ("nguoi_duyet") REFERENCES "nguoi_dung" ("id");

ALTER TABLE "chi_tiet_giai_ngan" ADD FOREIGN KEY ("ma_giai_ngan") REFERENCES "giai_ngan" ("id");

ALTER TABLE "tin_tuc" ADD FOREIGN KEY ("ma_tac_gia") REFERENCES "nguoi_dung" ("id");

ALTER TABLE "su_kien" ADD FOREIGN KEY ("nguoi_tao") REFERENCES "nguoi_dung" ("id");

ALTER TABLE "cap_nhat_du_an" ADD FOREIGN KEY ("ma_du_an") REFERENCES "du_an" ("id");
ALTER TABLE "cap_nhat_du_an" ADD FOREIGN KEY ("nguoi_tao") REFERENCES "nguoi_dung" ("id");

ALTER TABLE "dang_ky_su_kien" ADD FOREIGN KEY ("ma_su_kien") REFERENCES "su_kien" ("id");
ALTER TABLE "dang_ky_su_kien" ADD FOREIGN KEY ("ma_nguoi_dung") REFERENCES "nguoi_dung" ("id");

-- ============================================
-- INSERT FAKE DATA
-- ============================================

-- 1. BẢNG: nguoi_dung (5 người dùng)
INSERT INTO nguoi_dung (email, mat_khau_hash, ten, ho, so_dien_thoai, dia_chi, vai_tro, trang_thai) VALUES
('admin@charity.vn', '$2a$10$abcdefghijklmnopqrstuvwxyz1234567890', 'Admin', 'Nguyễn Văn', '0901234567', '123 Nguyễn Huệ, Q1, TP.HCM', 'quan_tri_vien', 'hoat_dong'),
('operator@charity.vn', '$2a$10$bcdefghijklmnopqrstuvwxyz1234567891', 'Operator', 'Trần Thị', '0902345678', '456 Lê Lợi, Q3, TP.HCM', 'dieu_hanh_vien', 'hoat_dong'),
('user1@gmail.com', '$2a$10$cdefghijklmnopqrstuvwxyz1234567892', 'Minh', 'Lê Văn', '0903456789', '789 Điện Biên Phủ, Q.Bình Thạnh, TP.HCM', 'nguoi_dung', 'hoat_dong'),
('volunteer@charity.vn', '$2a$10$defghijklmnopqrstuvwxyz1234567893', 'Hoa', 'Phạm Thị', '0904567890', '321 Cách Mạng Tháng 8, Q10, TP.HCM', 'tinh_nguyen_vien', 'hoat_dong'),
('user2@gmail.com', '$2a$10$efghijklmnopqrstuvwxyz1234567894', 'Tuấn', 'Hoàng Văn', '0905678901', '654 Võ Văn Tần, Q3, TP.HCM', 'nguoi_dung', 'khong_hoat_dong');

-- 2. BẢNG: danh_muc_du_an (5 danh mục)
INSERT INTO danh_muc_du_an (ten, mo_ta) VALUES
('Giáo dục', 'Các dự án hỗ trợ giáo dục cho trẻ em nghèo'),
('Y tế', 'Các dự án hỗ trợ y tế, khám chữa bệnh'),
('Môi trường', 'Các dự án bảo vệ môi trường, trồng cây xanh'),
('Nhà ở', 'Các dự án xây dựng nhà cho người nghèo'),
('Cứu trợ khẩn cấp', 'Các dự án cứu trợ thiên tai, hoàn cảnh khó khăn');

-- 3. BẢNG: du_an (5 dự án)
INSERT INTO du_an (tieu_de, mo_ta, mo_ta_chi_tiet, ma_danh_muc, so_tien_muc_tieu, so_tien_hien_tai, ngay_bat_dau, ngay_ket_thuc, dia_diem, trang_thai, thu_vien_anh, muc_do_yeu_tien, nguoi_tao, nguoi_phe_duyet) VALUES
('Xây dựng trường học vùng cao', 'Xây dựng trường tiểu học cho trẻ em vùng cao Sapa', 'Dự án xây dựng 1 trường tiểu học 5 phòng học với đầy đủ bàn ghế, thiết bị học tập cho 200 học sinh vùng cao Sapa, Lào Cai. Trường sẽ được xây dựng kiên cố, đảm bảo an toàn cho học sinh.', 1, 500000000.00, 350000000.00, '2024-01-15', '2024-12-31', 'Xã Tả Van, Sapa, Lào Cai', 'hoat_dong', 'images/projects/truong-hoc-sapa.jpg', 8, 1, 'Nguyễn Văn Admin'),
('Khám chữa bệnh miễn phí', 'Chương trình khám bệnh miễn phí cho người nghèo', 'Tổ chức khám bệnh, cấp thuốc miễn phí cho 500 người nghèo tại các xã vùng sâu, vùng xa. Đội ngũ bác sĩ tình nguyện từ các bệnh viện lớn sẽ trực tiếp khám và tư vấn sức khỏe.', 2, 200000000.00, 180000000.00, '2024-03-01', '2024-06-30', 'Huyện Hóc Môn, TP.HCM', 'hoat_dong', 'images/projects/kham-benh.jpg', 6, 2, 'Trần Thị Operator'),
('Trồng 10,000 cây xanh', 'Trồng cây xanh bảo vệ môi trường', 'Phủ xanh 50 hecta đất trống tại khu vực ven biển, chống xói mòn và tạo không gian xanh cho cộng đồng. Dự kiến trồng 10,000 cây gồm: dừa, phi lao, sao đen.', 3, 100000000.00, 45000000.00, '2024-02-01', '2024-08-31', 'Huyện Cần Giờ, TP.HCM', 'hoat_dong', 'images/projects/trong-cay.jpg', 5, 2, 'Trần Thị Operator'),
('Xây nhà tình thương', 'Xây dựng 5 căn nhà cho hộ nghèo', 'Xây dựng 5 căn nhà cấp 4 kiên cố cho các hộ gia đình khó khăn tại huyện miền núi. Mỗi căn nhà diện tích 40m2 với 2 phòng ngủ, 1 phòng khách và nhà vệ sinh.', 4, 300000000.00, 100000000.00, '2024-04-01', '2024-11-30', 'Huyện Bắc Trà My, Quảng Nam', 'hoat_dong', 'images/projects/nha-tinh-thuong.jpg', 9, 1, 'Nguyễn Văn Admin'),
('Cứu trợ lũ lụt miền Trung', 'Hỗ trợ khẩn cấp đồng bào miền Trung', 'Cung cấp lương thực, nước uống, thuốc men, quần áo cho 1000 hộ gia đình bị ảnh hưởng bởi lũ lụt tại Quảng Bình, Quảng Trị. Tổng cộng 2000 suất quà, mỗi suất trị giá 500,000đ.', 5, 150000000.00, 150000000.00, '2024-10-15', '2024-11-15', 'Quảng Bình, Quảng Trị', 'hoan_thanh', 'images/projects/cuu-tro-lu.jpg', 10, 2, 'Trần Thị Operator');

-- 4. BẢNG: tai_khoan_thanh_toan (5 tài khoản)
INSERT INTO tai_khoan_thanh_toan (ma_du_an, ten_tai_khoan, so_tai_khoan, ten_ngan_hang, chi_nhanh_ngan_hang, la_tai_khoan_mac_dinh, trang_thai, ghi_chu) VALUES
(1, 'Quỹ Xây Trường Sapa', '1234567890', 'Vietcombank', 'Chi nhánh TP.HCM', true, 'hoat_dong', 'Tài khoản chính nhận quyên góp'),
(2, 'Quỹ Y Tế Cộng Đồng', '2345678901', 'BIDV', 'Chi nhánh Tân Bình', true, 'hoat_dong', 'Tài khoản nhận quyên góp khám bệnh'),
(3, 'Quỹ Môi Trường Xanh', '3456789012', 'Techcombank', 'Chi nhánh Quận 1', true, 'hoat_dong', 'Tài khoản dự án trồng cây'),
(4, 'Quỹ Nhà Tình Thương', '4567890123', 'ACB', 'Chi nhánh Quận 3', true, 'hoat_dong', 'Tài khoản xây nhà người nghèo'),
(5, 'Quỹ Cứu Trợ Khẩn Cấp', '5678901234', 'Vietinbank', 'Chi nhánh Đống Đa', true, 'hoat_dong', 'Tài khoản cứu trợ lũ lụt');

-- 5. BẢNG: quyen_gop (5 khoản quyên góp)
INSERT INTO quyen_gop (ma_nguoi_dung, ma_du_an, so_tien, don_vi_tien_te, phuong_thuc_thanh_toan, trang_thai_, ma_giao_dich, loi_nhan, phi_giao_dich, so_tien_thuc) VALUES
(3, 1, 5000000.00, 'VND', 'vnpay', 'thanh_cong', 'VNPAY20241101001', 'Chúc dự án thành công', 50000, 4950000),
(3, 2, 2000000.00, 'VND', 'momo', 'thanh_cong', 'MOMO20241102001', 'Ủng hộ y tế cộng đồng', 20000, 1980000),
(4, 1, 10000000.00, 'VND', 'chuyen_khoan', 'thanh_cong', 'TF20241103001', 'Góp phần xây dựng tương lai cho trẻ em', 0, 10000000),
(5, 3, 3000000.00, 'VND', 'vnpay', 'thanh_cong', 'VNPAY20241104001', 'Bảo vệ môi trường xanh', 30000, 2970000),
(3, 4, 1000000.00, 'VND', 'momo', 'cho_xu_ly', 'MOMO20241105001', 'Giúp đỡ người nghèo có nhà', 10000, 990000);

-- 6. BẢNG: giai_ngan (5 đợt giải ngân)
INSERT INTO giai_ngan (ma_du_an, ma_tai_khoan_du_an, so_tien, loai_giai_ngan, ngay_giai_ngan, nguoi_nhan, thong_tin_nguoi_nhan, muc_dich_su_dung, tai_lieu_chung_minh, nguoi_giai_ngan, nguoi_duyet, ghi_chu, trang_thai) VALUES
(1, 1, 100000000.00, 'chuyen_khoan', '2024-05-15', 'Công ty Xây Dựng ABC', '{"so_dien_thoai": "0281234567", "dia_chi": "456 Lê Duẩn, Lào Cai", "mst": "0123456789"}', 'Thanh toán đợt 1 xây dựng móng và cột trường học', '{"hop_dong": "HD-2024-001.pdf", "bien_ban_nghiem_thu": "BBNT-001.pdf"}', 2, 1, 'Đợt 1: Hoàn thành móng và cột', 'da_duyet'),
(2, 2, 50000000.00, 'chuyen_khoan', '2024-04-20', 'Bệnh viện Quận 12', '{"so_dien_thoai": "0287654321", "dia_chi": "Quận 12, TP.HCM", "mst": "0987654321"}', 'Mua thuốc men và vật tư y tế', '{"hoa_don": "HD-BHYT-001.pdf", "danh_sach_thuoc": "DS-Thuoc.xlsx"}', 2, 1, 'Đợt 1: Mua thuốc và vật tư', 'da_duyet'),
(3, 3, 20000000.00, 'tien_mat', '2024-06-01', 'Hợp tác xã Nông Nghiệp Cần Giờ', '{"so_dien_thoai": "0289876543", "dai_dien": "Nguyễn Văn D", "chuc_vu": "Chủ tịch HTX"}', 'Mua cây giống và phân bón', '{"bien_ban_ban_giao": "BBGG-Cay-001.pdf", "anh_chung_minh": "anh-cay-giong.jpg"}', 4, 2, 'Đợt 1: Mua 5000 cây giống', 'da_duyet'),
(4, 4, 60000000.00, 'chuyen_khoan', '2024-07-10', 'Công ty TNHH Xây Dựng XYZ', '{"so_dien_thoai": "0261234567", "dia_chi": "Quảng Nam", "mst": "0246813579"}', 'Thanh toán xây dựng 2 căn nhà đầu tiên', '{"hop_dong": "HD-XD-2024-002.pdf", "tien_do": "Tiendo-50%.pdf"}', 2, 1, 'Đợt 1: Hoàn thành 2 căn nhà', 'da_duyet'),
(5, 5, 150000000.00, 'tien_mat', '2024-10-20', 'Uỷ ban Nhân dân các xã', '{"so_dien_thoai": "0233456789", "dia_chi": "UBND xã Quảng Bình", "nguoi_nhan": "Chủ tịch UBND"}', 'Phát quà cứu trợ cho người dân', '{"bien_ban_phat_qua": "BBPQ-Lu-001.pdf", "danh_sach_nhan_qua": "DS-Nhan-Qua.xlsx", "anh_phat_qua": "anh-phat-qua.jpg"}', 2, 1, 'Đợt duy nhất: Phát 2000 suất quà', 'da_duyet');

-- 7. BẢNG: chi_tiet_giai_ngan (5 chi tiết)
INSERT INTO chi_tiet_giai_ngan (ma_giai_ngan, so_tien, mo_ta) VALUES
(1, 60000000.00, 'Chi phí vật liệu xây dựng (xi măng, cát, đá, gạch)'),
(1, 40000000.00, 'Chi phí nhân công thi công'),
(2, 35000000.00, 'Mua thuốc kháng sinh, thuốc hạ sốt, vitamin'),
(2, 15000000.00, 'Mua băng gạc, bông y tế, dụng cụ y tế'),
(3, 20000000.00, 'Mua 5000 cây giống và phân bón');

-- 8. BẢNG: tin_tuc (5 tin tức)
INSERT INTO tin_tuc (tieu_de, noi_dung, ma_tac_gia, anh_dai_dien) VALUES
('Khởi công xây dựng trường học Sapa', 'Ngày 15/1/2024, dự án xây dựng trường tiểu học tại Sapa đã chính thức khởi công. Buổi lễ có sự tham gia của đại diện chính quyền địa phương, nhà tài trợ và người dân địa phương. Dự kiến trường sẽ hoàn thành trong năm 2024.', 1, 'images/news/khoi-cong-truong.jpg'),
('Chương trình khám bệnh thu hút đông đảo người dân', 'Chương trình khám bệnh miễn phí đã thu hút hơn 300 người dân tham gia trong ngày đầu tiên. Các bác sĩ tình nguyện đã khám và tư vấn sức khỏe, cấp thuốc miễn phí cho bà con.', 2, 'images/news/kham-benh-mien-phi.jpg'),
('Hoàn thành trồng 5,000 cây xanh đầu tiên', 'Sau 3 tháng triển khai, dự án đã trồng thành công 5,000 cây xanh tại khu vực Cần Giờ. Các tình nguyện viên đã nỗ lực trong điều kiện thời tiết khắc nghiệt để hoàn thành mục tiêu.', 4, 'images/news/trong-cay-thanh-cong.jpg'),
('Bàn giao 2 căn nhà tình thương đầu tiên', 'Hai gia đình khó khăn tại Quảng Nam đã nhận được căn nhà mới với đầy đủ tiện nghi. Niềm vui của các gia đình là động lực lớn để dự án tiếp tục hoàn thành 3 căn nhà còn lại.', 1, 'images/news/ban-giao-nha.jpg'),
('Cứu trợ khẩn cấp đồng bào miền Trung', '2000 suất quà đã được phát đến tay bà con vùng lũ. Mỗi suất quà gồm: 10kg gạo, mì tôm, nước uống, thuốc men và quần áo. Chương trình đã kịp thời hỗ trợ bà con vượt qua khó khăn.', 2, 'images/news/cuu-tro-mien-trung.jpg');

-- 9. BẢNG: su_kien (5 sự kiện)
INSERT INTO su_kien (tieu_de, mo_ta, thoi_gian_bat_dau, dia_diem, nguoi_tao) VALUES
('Lễ khởi công trường học Sapa', 'Lễ khởi công xây dựng trường tiểu học cho trẻ em vùng cao với sự tham gia của nhà tài trợ, chính quyền địa phương và người dân.', '2024-01-15 08:00:00+07', 'Xã Tả Van, Sapa, Lào Cai', 1),
('Ngày hội trồng cây tình nguyện', 'Kêu gọi 100 tình nguyện viên tham gia trồng cây tại khu vực Cần Giờ. Sẽ có xe đưa đón, bữa trưa và quà tặng cho tình nguyện viên.', '2024-06-05 06:00:00+07', 'Rừng ngập mặn Cần Giờ, TP.HCM', 4),
('Chương trình giao lưu với trẻ em vùng cao', 'Tổ chức các hoạt động vui chơi, tặng quà và dạy học cho trẻ em vùng cao. Cần 50 tình nguyện viên tham gia.', '2024-08-20 14:00:00+07', 'Trường Tiểu học Sapa, Lào Cai', 1),
('Hội thảo quyên góp từ thiện hiệu quả', 'Hội thảo chia sẻ kinh nghiệm về các chiến dịch quyên góp thành công, cách quản lý quỹ minh bạch và hiệu quả.', '2024-09-15 13:00:00+07', 'Trung tâm Hội nghị, Quận 1, TP.HCM', 2),
('Lễ bàn giao nhà tình thương', 'Lễ bàn giao 5 căn nhà tình thương cho các hộ nghèo tại Quảng Nam với sự tham gia của nhà tài trợ và chính quyền địa phương.', '2024-11-30 09:00:00+07', 'Xã Trà Leng, Huyện Nam Trà My, Quảng Nam', 1);

-- 10. BẢNG: cap_nhat_du_an (5 cập nhật)
INSERT INTO cap_nhat_du_an (ma_du_an, tieu_de, noi_dung, nguoi_tao) VALUES
(1, 'Hoàn thành đổ móng và dựng cột', 'Sau 4 tháng thi công, chúng tôi đã hoàn thành việc đổ móng và dựng cột cho toàn bộ 5 phòng học. Tiến độ đang đúng kế hoạch. Dự kiến tháng tới sẽ hoàn thành mái và tường.', 1),
(2, 'Khám bệnh cho 300 người dân', 'Trong tuần đầu tiên, chương trình đã khám bệnh cho 300 người dân. Các bệnh phổ biến: đau dạ dày, cao huyết áp, tiểu đường. Tất cả đều được cấp thuốc miễn phí.', 2),
(3, 'Trồng thành công 5000 cây đầu tiên', 'Đội ngũ tình nguyện viên đã nỗ lực trồng 5000 cây trong 3 tháng qua. Tỷ lệ sống của cây đạt 92%. Chúng tôi đang chuẩn bị cho đợt trồng tiếp theo.', 4),
(4, 'Bàn giao 2 căn nhà đầu tiên', 'Hai gia đình đầu tiên đã nhận được căn nhà mới. Niềm vui của các gia đình là động lực lớn để chúng tôi tiếp tục hoàn thành 3 căn nhà còn lại.', 1),
(5, 'Hoàn thành phát quà cứu trợ', 'Tất cả 2000 suất quà đã được phát đến tay bà con. Chương trình đã kịp thời hỗ trợ người dân vùng lũ. Cảm ơn sự đóng góp của tất cả mọi người.', 2);

-- 11. BẢNG: lien_he (5 liên hệ)
INSERT INTO lien_he (ten_nguoi_gui, email, tieu_de, noi_dung) VALUES
('Nguyễn Thị B', 'nguyenthib@gmail.com', 'Hỏi về dự án xây trường', 'Tôi muốn biết thêm chi tiết về dự án xây trường học tại Sapa. Tôi có thể đến thăm công trình được không?'),
('Trần Văn C', 'tranvanc@yahoo.com', 'Muốn tham gia tình nguyện', 'Tôi là sinh viên và muốn tham gia các hoạt động tình nguyện của tổ chức. Vui lòng cho tôi biết cách đăng ký.'),
('Lê Thị D', 'lethid@hotmail.com', 'Đề xuất dự án mới', 'Tôi muốn đề xuất một dự án xây dựng thư viện cho trẻ em vùng xa. Tôi có thể gửi hồ sơ như thế nào?'),
('Phạm Văn E', 'phamvane@gmail.com', 'Hỏi về quyên góp', 'Tôi muốn quyên góp một số tiền nhưng muốn biết chính xác tiền sẽ được sử dụng như thế nào?'),
('Hoàng Thị F', 'hoangthif@outlook.com', 'Báo cáo lỗi website', 'Tôi không thể đăng nhập vào tài khoản. Vui lòng kiểm tra và hỗ trợ. Cảm ơn!');

-- 12. BẢNG: dang_ky_su_kien (5 đăng ký)
INSERT INTO dang_ky_su_kien (ma_su_kien, ma_nguoi_dung, ten_nguoi_dang_ky, email_nguoi_dang_ky) VALUES
(2, 3, 'Lê Văn Minh', 'user1@gmail.com'),
(2, 4, 'Phạm Thị Hoa', 'volunteer@charity.vn'),
(3, 4, 'Phạm Thị Hoa', 'volunteer@charity.vn'),
(4, 3, 'Lê Văn Minh', 'user1@gmail.com'),
(5, 3, 'Lê Văn Minh', 'user1@gmail.com');

-- ============================================
-- HOÀN TẤT
-- ============================================


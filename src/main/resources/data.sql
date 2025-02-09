-- Ensure Brand Table Exists
INSERT INTO tbl_brand (id, name, description, created_at, updated_at, is_deleted)
SELECT 1, 'L''Oreal', 'Thương hiệu mỹ phẩm hàng đầu thế giới', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_brand WHERE id = 1);
INSERT INTO tbl_brand (id, name, description, created_at, updated_at, is_deleted)
SELECT 2, 'The Ordinary', 'Chuyên về serum và dưỡng chất da', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_brand WHERE id = 2);
INSERT INTO tbl_brand (id, name, description, created_at, updated_at, is_deleted)
SELECT 3, 'Innisfree', 'Thương hiệu mỹ phẩm thiên nhiên từ Hàn Quốc', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_brand WHERE id = 3);
INSERT INTO tbl_brand (id, name, description, created_at, updated_at, is_deleted)
SELECT 4, 'Estée Lauder', 'Thương hiệu cao cấp chuyên chăm sóc sắc đẹp', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_brand WHERE id = 4);
INSERT INTO tbl_brand (id, name, description, created_at, updated_at, is_deleted)
SELECT 5, 'Clinique', 'Thương hiệu mỹ phẩm nổi tiếng về các sản phẩm chăm sóc da', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_brand WHERE id = 5);

-- Insert Categories (Ensure Unique UUIDs)
INSERT INTO tbl_category (id, name, slug, description, thumbnail, created_at, updated_at, is_deleted)
VALUES
    (UUID(), 'Serum', 'serum', 'Sản phẩm dưỡng da dạng serum', 'serum.jpg', NOW(), NOW(), 0),
    (UUID(), 'Kem dưỡng', 'kem-duong', 'Dưỡng ẩm cho da mặt', 'kem_duong.jpg', NOW(), NOW(), 0),
    (UUID(), 'Sữa rửa mặt', 'sua-rua-mat', 'Làm sạch da hàng ngày', 'sua_rua_mat.jpg', NOW(), NOW(), 0);
INSERT INTO tbl_category (id, name, slug, description, thumbnail, created_at, updated_at, is_deleted)
VALUES
    (UUID(), 'Mặt nạ', 'mat-na', 'Mặt nạ chăm sóc da', 'mat_na.jpg', NOW(), NOW(), 0),
    (UUID(), 'Dầu gội', 'dau-goi', 'Chăm sóc tóc với các sản phẩm dầu gội', 'dau_goi.jpg', NOW(), NOW(), 0);

-- Insert Origins (Avoid Duplicate Entries)
INSERT INTO tbl_origin (id, name, created_at, updated_at, is_deleted)
SELECT 1, 'Hàn Quốc', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_origin WHERE id = 1);
INSERT INTO tbl_origin (id, name, created_at, updated_at, is_deleted)
SELECT 2, 'Pháp', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_origin WHERE id = 2);
INSERT INTO tbl_origin (id, name, created_at, updated_at, is_deleted)
SELECT 3, 'Mỹ', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_origin WHERE id = 3);
INSERT INTO tbl_origin (id, name, created_at, updated_at, is_deleted)
SELECT 4, 'Nhật Bản', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_origin WHERE id = 4);
INSERT INTO tbl_origin (id, name, created_at, updated_at, is_deleted)
SELECT 5, 'Hà Lan', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_origin WHERE id = 5);

-- Insert Features (Ensure No Duplicates)
INSERT INTO tbl_feature (id, name, created_at, updated_at, is_deleted)
SELECT 1, 'Dưỡng ẩm', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_feature WHERE id = 1);
INSERT INTO tbl_feature (id, name, created_at, updated_at, is_deleted)
SELECT 2, 'Trị mụn', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_feature WHERE id = 2);
INSERT INTO tbl_feature (id, name, created_at, updated_at, is_deleted)
SELECT 3, 'Chống lão hóa', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_feature WHERE id = 3);
INSERT INTO tbl_feature (id, name, created_at, updated_at, is_deleted)
SELECT 4, 'Làm sáng da', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_feature WHERE id = 4);
INSERT INTO tbl_feature (id, name, created_at, updated_at, is_deleted)
SELECT 5, 'Chống nắng', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_feature WHERE id = 5);
INSERT INTO tbl_feature (id, name, created_at, updated_at, is_deleted)
SELECT 6, 'Dưỡng trắng', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_feature WHERE id = 6);
-- Ensure Skin Types are inserted correctly
INSERT INTO tbl_skin_type (id, type, created_at, updated_at, is_deleted)
SELECT 1, 'Da dầu', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_skin_type WHERE id = 1);

INSERT INTO tbl_skin_type (id, type, created_at, updated_at, is_deleted)
SELECT 2, 'Da khô', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_skin_type WHERE id = 2);
INSERT INTO tbl_skin_type (id, type, created_at, updated_at, is_deleted)
SELECT 3, 'Da nhạy cảm', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_skin_type WHERE id = 3);
INSERT INTO tbl_skin_type (id, type, created_at, updated_at, is_deleted)
SELECT 4, 'Da hỗn hợp', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_skin_type WHERE id = 4);
INSERT INTO tbl_skin_type (id, type, created_at, updated_at, is_deleted)
SELECT 5, 'Da mụn', NOW(), NOW(), 0
    WHERE NOT EXISTS (SELECT 1 FROM tbl_skin_type WHERE id = 5);

-- Insert Product
INSERT INTO tbl_product (id, name, price, slug, quantity_per_pack, product_code, description, thumbnail, usage_instruction, expiry_date, brand_id, origin_id, skin_type_id, category_id, created_at, updated_at, is_deleted)
VALUES
    (UUID(), 'Serum Vitamin C', 299000, 'serum-vitamin-c', 1, 'SP001', 'Dưỡng sáng da, chống oxy hóa', 'serum_vitc.jpg', 'Dùng mỗi tối trước khi đi ngủ', '2025-12-31', 1, 1, 2, (SELECT id FROM tbl_category WHERE slug='serum'), NOW(), NOW(), 0),
    (UUID(), 'Kem dưỡng ẩm Innisfree', 350000, 'kem-duong-am-innisfree', 1, 'SP002', 'Cấp ẩm tức thì cho da khô', 'kem_duong_am.jpg', 'Thoa đều lên mặt sáng và tối', '2026-06-30', 3, 1, 2, (SELECT id FROM tbl_category WHERE slug='kem-duong'), NOW(), NOW(), 0),
    (UUID(), 'Sữa rửa mặt The Ordinary', 250000, 'sua-rua-mat-the-ordinary', 1, 'SP003', 'Làm sạch da nhẹ nhàng, không gây kích ứng', 'sua_rua_mat.jpg', 'Sử dụng sáng và tối', '2025-09-15', 2, 2, 1, (SELECT id FROM tbl_category WHERE slug='sua-rua-mat'), NOW(), NOW(), 0),
    (UUID(), 'Kem chống nắng SunBlock', 500000, 'kem-chong-nang-sunblock', 1, 'SP004', 'Chống tia UV, bảo vệ da khỏi ánh nắng', 'kem_chong_nang.jpg', 'Thoa đều trước khi ra ngoài', '2026-12-31', 4, 1, 2, (SELECT id FROM tbl_category WHERE slug='mat-na'), NOW(), NOW(), 0),
    (UUID(), 'Dầu gội xả Dove', 150000, 'dau-goi-xa-dove', 1, 'SP005', 'Dầu gội dưỡng tóc mềm mượt', 'dau_goi_xa.jpg', 'Dùng mỗi ngày để tóc mềm mại', '2025-06-30', 5, 2, 1, (SELECT id FROM tbl_category WHERE slug='dau-goi'), NOW(), NOW(), 0);

-- Insert Product-Feature Relations
INSERT INTO tbl_product_has_feature (product_id, feature_id)
SELECT (SELECT id FROM tbl_product WHERE slug='serum-vitamin-c'), 4
    WHERE NOT EXISTS (SELECT 1 FROM tbl_product_has_feature WHERE product_id = (SELECT id FROM tbl_product WHERE slug='serum-vitamin-c') AND feature_id = 4);

INSERT INTO tbl_product_has_feature (product_id, feature_id)
SELECT (SELECT id FROM tbl_product WHERE slug='kem-duong-am-innisfree'), 1
    WHERE NOT EXISTS (SELECT 1 FROM tbl_product_has_feature WHERE product_id = (SELECT id FROM tbl_product WHERE slug='kem-duong-am-innisfree') AND feature_id = 1);

INSERT INTO tbl_product_has_feature (product_id, feature_id)
SELECT (SELECT id FROM tbl_product WHERE slug='sua-rua-mat-the-ordinary'), 2
    WHERE NOT EXISTS (SELECT 1 FROM tbl_product_has_feature WHERE product_id = (SELECT id FROM tbl_product WHERE slug='sua-rua-mat-the-ordinary') AND feature_id = 2);

INSERT INTO tbl_product_has_feature (product_id, feature_id)
SELECT (SELECT id FROM tbl_product WHERE slug='kem-chong-nang-sunblock'), 5
    WHERE NOT EXISTS (SELECT 1 FROM tbl_product_has_feature WHERE product_id = (SELECT id FROM tbl_product WHERE slug='kem-chong-nang-sunblock') AND feature_id = 5);

INSERT INTO tbl_product_has_feature (product_id, feature_id)
SELECT (SELECT id FROM tbl_product WHERE slug='dau-goi-xa-dove'), 6
    WHERE NOT EXISTS (SELECT 1 FROM tbl_product_has_feature WHERE product_id = (SELECT id FROM tbl_product WHERE slug='dau-goi-xa-dove') AND feature_id = 6);
-- Insert data into role table
INSERT INTO public.role (role_name) VALUES
('Student'),
('Teacher'),
('Cashier'),
('Director'),
('Supervisor');

-- Insert data into account table
INSERT INTO public.account (address, birth_date, email, full_name, gender, password, phone_number, username, role_id) VALUES
('Hà Nội', '2008-05-15', 'student1@example.com', 'Nguyen Van A', 'Nam', 'password123', '0123456789', 'student1', 1),
('Hà Nội', '2008-06-20', 'student2@example.com', 'Tran Thi D', 'Nữ', 'password123', '0123456790', 'student2', 1),
('Hà Nội', '2008-07-15', 'student3@example.com', 'Le Van E', 'Nam', 'password123', '0123456791', 'student3', 1),
('Hà Nội', '2008-08-10', 'student4@example.com', 'Pham Thi F', 'Nữ', 'password123', '0123456792', 'student4', 1),
('Hà Nội', '2008-09-05', 'student5@example.com', 'Nguyen Van G', 'Nam', 'password123', '0123456793', 'student5', 1),
('Hà Nội', '2008-10-01', 'student6@example.com', 'Nguyen Thi H', 'Nữ', 'password123', '0123456794', 'student6', 1),
('Hà Nội', '2008-11-01', 'student7@example.com', 'Le Van I', 'Nam', 'password123', '0123456795', 'student7', 1),
('Hà Nội', '1980-03-20', 'teacher1@example.com', 'Tran Thi B', 'Nữ', 'password123', '0987654321', 'teacher1', 2),
('Hà Nội', '1978-04-15', 'teacher2@example.com', 'Pham Van K', 'Nam', 'password123', '0987654322', 'teacher2', 2),
('Hà Nội', '1975-07-10', 'cashier1@example.com', 'Le Van C', 'Nam', 'password123', '0912345678', 'cashier1', 3),
('Hà Nội', '1982-09-12', 'cashier2@example.com', 'Hoang Thi M', 'Nữ', 'password123', '0912345679', 'cashier2', 3);

-- Insert data into student table
INSERT INTO public.student (id, birth_place, ethnicity, status, account_id) VALUES
('HS001', 'Hà Nội', 'Kinh', 'ACTIVE', 1),
('HS002', 'Hà Nội', 'Kinh', 'ACTIVE', 2),
('HS003', 'Hà Nội', 'Kinh', 'ACTIVE', 3),
('HS004', 'Hà Nội', 'Kinh', 'ACTIVE', 4),
('HS005', 'Hà Nội', 'Kinh', 'ACTIVE', 5),
('HS006', 'Hà Nội', 'Kinh', 'ACTIVE', 6),
('HS007', 'Hà Nội', 'Kinh', 'ACTIVE', 7);

-- Insert data into teacher table
INSERT INTO public.teacher (id, "position", status, account_id) VALUES
('GV001', 'Giáo viên', 'Đang giảng dạy', 8),
('GV002', 'Giáo viên', 'Đang giảng dạy', 9);

-- Insert data into cashier table
INSERT INTO public.cashier (id, status, account_id) VALUES
('CS001', 'Working', 10),
('CS002', 'Working', 11);

-- Insert data into class table
INSERT INTO public.class (class_name) VALUES
('10A1'),
('10A2'),
('10A3');

-- Insert data into subject table
INSERT INTO public.subject (subject_name) VALUES
('Toán'),
('Văn'),
('Anh'),
('Lý'),
('Hóa');

-- Insert data into student_class table
INSERT INTO public.student_class (academic_year, class_id, student_id) VALUES
('2024-2025', 1, 'HS001'),
('2024-2025', 1, 'HS002'),
('2024-2025', 2, 'HS003'),
('2024-2025', 2, 'HS004'),
('2024-2025', 2, 'HS005'),
('2024-2025', 3, 'HS006'),
('2024-2025', 3, 'HS007'),
('2023-2024', 1, 'HS001'),
('2023-2024', 1, 'HS002'),
('2023-2024', 2, 'HS003'),
('2023-2024', 2, 'HS004'),
('2023-2024', 2, 'HS005'),
('2023-2024', 3, 'HS006'),
('2023-2024', 3, 'HS007');

-- Insert data into assignment table
INSERT INTO public.assignment (academic_year, role, semester, class_id, subject_id, teacher_id) VALUES
('2024-2025', 'Subject_Teacher', 1, 1, 1, 'GV001'),
('2024-2025', 'Subject_Teacher', 1, 1, 2, 'GV001'),
('2024-2025', 'Subject_Teacher', 1, 1, 3, 'GV001'),
('2024-2025', 'Subject_Teacher', 2, 1, 1, 'GV001'),
('2024-2025', 'Subject_Teacher', 2, 1, 2, 'GV001'),
('2024-2025', 'Subject_Teacher', 2, 1, 3, 'GV001'),
('2024-2025', 'Subject_Teacher', 1, 2, 1, 'GV002'),
('2024-2025', 'Subject_Teacher', 1, 2, 2, 'GV002'),
('2024-2025', 'Subject_Teacher', 1, 2, 3, 'GV002'),
('2024-2025', 'Subject_Teacher', 2, 2, 1, 'GV002'),
('2024-2025', 'Subject_Teacher', 2, 2, 2, 'GV002'),
('2024-2025', 'Subject_Teacher', 2, 2, 3, 'GV002'),
('2024-2025', 'Subject_Teacher', 1, 3, 4, 'GV001'),
('2024-2025', 'Subject_Teacher', 1, 3, 5, 'GV002'),
('2024-2025', 'Subject_Teacher', 2, 3, 4, 'GV001'),
('2024-2025', 'Subject_Teacher', 2, 3, 5, 'GV002'),
('2023-2024', 'Subject_Teacher', 1, 1, 1, 'GV001'),
('2023-2024', 'Subject_Teacher', 1, 1, 2, 'GV001'),
('2023-2024', 'Subject_Teacher', 1, 1, 3, 'GV001'),
('2023-2024', 'Subject_Teacher', 2, 1, 1, 'GV001'),
('2023-2024', 'Subject_Teacher', 2, 1, 2, 'GV001'),
('2023-2024', 'Subject_Teacher', 2, 1, 3, 'GV001'),
('2023-2024', 'Subject_Teacher', 1, 2, 1, 'GV002'),
('2023-2024', 'Subject_Teacher', 1, 2, 2, 'GV002'),
('2023-2024', 'Subject_Teacher', 1, 2, 3, 'GV002'),
('2023-2024', 'Subject_Teacher', 2, 2, 1, 'GV002'),
('2023-2024', 'Subject_Teacher', 2, 2, 2, 'GV002'),
('2023-2024', 'Subject_Teacher', 2, 2, 3, 'GV002'),
('2023-2024', 'Subject_Teacher', 1, 3, 4, 'GV001'),
('2023-2024', 'Subject_Teacher', 1, 3, 5, 'GV002'),
('2023-2024', 'Subject_Teacher', 2, 3, 4, 'GV001'),
('2023-2024', 'Subject_Teacher', 2, 3, 5, 'GV002');

-- Insert data into behavior table
INSERT INTO public.behavior (academic_year, behavior_score, semester, status, student_id) VALUES
('2024-2025', 95, 1, 'Good', 'HS001'),
('2024-2025', 90, 2, 'Good', 'HS001'),
('2024-2025', 85, 1, 'Good', 'HS002'),
('2024-2025', 88, 2, 'Good', 'HS002'),
('2024-2025', 75, 1, 'Fair', 'HS003'),
('2024-2025', 78, 2, 'Fair', 'HS003'),
('2024-2025', 65, 1, 'Poor', 'HS004'),
('2024-2025', 70, 2, 'Poor', 'HS004'),
('2024-2025', 95, 1, 'Good', 'HS005'),
('2024-2025', 97, 2, 'Good', 'HS005'),
('2024-2025', 80, 1, 'Fair', 'HS006'),
('2024-2025', 82, 2, 'Fair', 'HS006'),
('2024-2025', 90, 1, 'Good', 'HS007'),
('2024-2025', 92, 2, 'Good', 'HS007'),
('2023-2024', 93, 1, 'Good', 'HS001'),
('2023-2024', 91, 2, 'Good', 'HS001'),
('2023-2024', 87, 1, 'Good', 'HS002'),
('2023-2024', 89, 2, 'Good', 'HS002'),
('2023-2024', 77, 1, 'Fair', 'HS003'),
('2023-2024', 79, 2, 'Fair', 'HS003'),
('2023-2024', 67, 1, 'Poor', 'HS004'),
('2023-2024', 69, 2, 'Poor', 'HS004'),
('2023-2024', 94, 1, 'Good', 'HS005'),
('2023-2024', 96, 2, 'Good', 'HS005'),
('2023-2024', 81, 1, 'Fair', 'HS006'),
('2023-2024', 83, 2, 'Fair', 'HS006'),
('2023-2024', 91, 1, 'Good', 'HS007'),
('2023-2024', 93, 2, 'Good', 'HS007');

-- Insert data into invoice table
INSERT INTO public.invoice (academic_year, outstanding_amount, paid_amount, status, total_fee, student_id) VALUES
('2024-2025', 0, 1200, 'Đã thanh toán', 1200, 'HS001'),
('2024-2025', 0, 2000, 'Đã thanh toán', 2000, 'HS002'),
('2024-2025', 200, 2800, 'Thanh toán 1 phần', 3000, 'HS003'),
('2024-2025', 2300, 200, 'Thanh toán 1 phần', 2500, 'HS004'),
('2024-2025', 1100, 100, 'Thanh toán 1 phần', 1200, 'HS005'),
('2023-2024', 0, 1000, 'Đã thanh toán', 1000, 'HS001'),
('2023-2024', 0, 1500, 'Đã thanh toán', 1500, 'HS002'),
('2023-2024', 2000, 0, 'Chưa thanh toán', 2000, 'HS003'),
('2023-2024', 1800, 200, 'Thanh toán 1 phần', 2000, 'HS004'),
('2023-2024', 1200, 0, 'Chưa thanh toán', 1200, 'HS005'),
('2024-2025', 1500, 0, 'Chưa thanh toán', 1500, 'HS002'),
('2024-2025', 1000, 500, 'Thanh toán 1 phần', 1500, 'HS003'),
('2024-2025', 0, 1500, 'Đã thanh toán', 1500, 'HS006'),
('2024-2025', 2000, 0, 'Chưa thanh toán', 2000, 'HS007'),
('2023-2024', 0, 1200, 'Đã thanh toán', 1200, 'HS006'),
('2023-2024', 0, 1800, 'Đã thanh toán', 1800, 'HS007');

-- Insert data into payment_receipt table
INSERT INTO public.payment_receipt (paid_amount, payment_date, cashier_id, invoice_id) VALUES
(500, '2025-05-01', 'CS001', 2),
(600, '2025-05-19', 'CS001', 1),
(1500, '2025-05-19', 'CS002', 2),
(600, '2025-05-19', 'CS001', 1),
(200, '2025-05-19', 'CS002', 4),
(500, '2025-05-20', 'CS002', 12),
(1500, '2025-05-20', 'CS001', 13),
(1200, '2025-05-21', 'CS002', 15),
(1800, '2025-05-21', 'CS001', 16),
(100, '2025-05-22', 'CS001', 5);

-- Insert data into score table
INSERT INTO public.score (academic_year, comments, final_score, score_15m_1, score_15m_2, score_1h_1, score_1h_2, semester, student_id, subject_id, teacher_id) VALUES
('2024-2025', 'Tốt', 8, 8, 7.5, 8.5, 9, 1, 'HS001', 1, 'GV001'),
('2024-2025', 'Khá', 7.5, 7, 7, 8, 8, 1, 'HS001', 2, 'GV001'),
('2024-2025', 'Xuất sắc', 8.5, 9, 8.5, 9, 9.5, 1, 'HS001', 3, 'GV001'),
('2024-2025', 'Tốt', 8.5, 8.5, 8, 9, 9, 2, 'HS001', 1, 'GV001'),
('2024-2025', 'Khá', 8, 7.5, 7.5, 8.5, 8.5, 2, 'HS001', 2, 'GV001'),
('2024-2025', 'Xuất sắc', 9, 9.5, 9, 9.5, 9, 2, 'HS001', 3, 'GV001'),
('2024-2025', 'Khá', 7.5, 7, 7.5, 7, 8, 1, 'HS002', 1, 'GV001'),
('2024-2025', 'Khá', 7, 7, 7, 7.5, 7.5, 1, 'HS002', 2, 'GV001'),
('2024-тян', 'Tốt', 7.5, 8, 8, 7.5, 8, 1, 'HS002', 3, 'GV001'),
('2024-2025', 'Tốt', 7.5, 7.5, 8, 8, 8, 2, 'HS002', 1, 'GV001'),
('2024-2025', 'Khá', 7.5, 7.5, 7.5, 7.5, 8, 2, 'HS002', 2, 'GV001'),
('2024-2025', 'Tốt', 8, 8.5, 8, 8, 8.5, 2, 'HS002', 3, 'GV001'),
('2024-2025', 'Trung bình', 6, 6, 6, 6.5, 6, 1, 'HS003', 1, 'GV002'),
('2024-2025', 'Trung bình', 6, 5.5, 6, 6, 6.5, 1, 'HS003', 2, 'GV002'),
('2024-2025', 'Trung bình', 6.5, 6, 6.5, 6, 6, 1, 'HS003', 3, 'GV002'),
('2024-2025', 'Trung bình', 6, 6.5, 6, 6.5, 6.5, 2, 'HS003', 1, 'GV002'),
('2024-2025', 'Trung bình', 6.5, 6, 6.5, 6, 6, 2, 'HS003', 2, 'GV002'),
('2024-2025', 'Trung bình', 6.5, 6.5, 6.5, 6.5, 6, 2, 'HS003', 3, 'GV002'),
('2024-2025', 'Yếu', 4, 4, 4.5, 4.5, 5, 1, 'HS004', 1, 'GV002'),
('2024-2025', 'Yếu', 4.5, 4.5, 4, 4, 4.5, 1, 'HS004', 2, 'GV002'),
('2024-2025', 'Yếu', 4.5, 5, 4.5, 4.5, 4, 1, 'HS004', 3, 'GV002'),
('2024-2025', 'Yếu', 4.5, 4.5, 5, 4.5, 5, 2, 'HS004', 1, 'GV002'),
('2024-2025', 'Yếu', 4.5, 4, 4.5, 5, 4.5, 2, 'HS004', 2, 'GV002'),
('2024-2025', 'Yếu', 5, 5, 4.5, 5, 4.5, 2, 'HS004', 3, 'GV002'),
('2024-2025', 'Tốt', 8.5, 8.5, 8, 9, 9, 1, 'HS005', 1, 'GV002'),
('2024-2025', 'Tốt', 8.5, 8, 8.5, 8.5, 8, 1, 'HS005', 2, 'GV002'),
('2024-2025', 'Xuất sắc', 9, 9, 8.5, 9, 9, 1, 'HS005', 3, 'GV002'),
('2024-2025', 'Xuất sắc', 9, 9, 8.5, 9, 9, 2, 'HS005', 1, 'GV002'),
('2024-2025', 'Tốt', 8.5, 8.5, 8, 8.5, 8.5, 2, 'HS005', 2, 'GV002'),
('2024-2025', 'Xuất sắc', 9, 9, 9, 9.5, 9, 2, 'HS005', 3, 'GV002'),
('2024-2025', 'Khá', 7, 7, 7.5, 7, 7.5, 1, 'HS006', 4, 'GV001'),
('2024-2025', 'Tốt', 8, 8, 8, 8.5, 8, 1, 'HS006', 5, 'GV002'),
('2024-2025', 'Khá', 7.5, 7.5, 7, 7.5, 8, 2, 'HS006', 4, 'GV001'),
('2024-2025', 'Tốt', 8.5, 8.5, 8, 8.5, 9, 2, 'HS006', 5, 'GV002'),
('2024-2025', 'Tốt', 8, 8, 7.5, 8, 8.5, 1, 'HS007', 4, 'GV001'),
('2024-2025', 'Khá', 7.5, 7, 7.5, 7.5, 8, 1, 'HS007', 5, 'GV002'),
('2024-2025', 'Tốt', 8.5, 8.5, 8, 9, 8.5, 2, 'HS007', 4, 'GV001'),
('2024-2025', 'Khá', 7.5, 7.5, 7, 8, 7.5, 2, 'HS007', 5, 'GV002'),
('2023-2024', 'Tốt', 8.2, 8, 8, 8.5, 8.5, 1, 'HS001', 1, 'GV001'),
('2023-2024', 'Khá', 7.6, 7.5, 7, 8, 7.5, 1, 'HS001', 2, 'GV001'),
('2023-2024', 'Xuất sắc', 8.7, 9, 8.5, 9, 9, 1, 'HS001', 3, 'GV001'),
('2023-2024', 'Tốt', 8.3, 8.5, 8, 8.5, 8.5, 2, 'HS001', 1, 'GV001'),
('2023-2024', 'Khá', 7.8, 7.5, 7.5, 8, 8, 2, 'HS001', 2, 'GV001'),
('2023-2024', 'Xuất sắc', 9.1, 9, 9, 9.5, 9, 2, 'HS001', 3, 'GV001'),
('2023-2024', 'Khá', 7.4, 7, 7.5, 7, 8, 1, 'HS002', 1, 'GV001'),
('2023-2024', 'Khá', 7.2, 7, 7, 7.5, 7.5, 1, 'HS002', 2, 'GV001'),
('2023-2024', 'Tốt', 7.6, 8, 7.5, 7.5, 8, 1, 'HS002', 3, 'GV001'),
('2023-2024', 'Tốt', 7.7, 7.5, 8, 8, 7.5, 2, 'HS002', 1, 'GV001'),
('2023-2024', 'Khá', 7.4, 7.5, 7, 7.5, 7.5, 2, 'HS002', 2, 'GV001'),
('2023-2024', 'Tốt', 8.1, 8, 8, 8.5, 8, 2, 'HS002', 3, 'GV001'),
('2023-2024', 'Trung bình', 6.1, 6, 6, 6.5, 6, 1, 'HS003', 1, 'GV002'),
('2023-2024', 'Trung bình', 6.2, 6, 6, 6.5, 6, 1, 'HS003', 2, 'GV002'),
('2023-2024', 'Trung bình', 6.3, 6.5, 6, 6, 6.5, 1, 'HS003', 3, 'GV002'),
('2023-2024', 'Trung bình', 6.4, 6, 6.5, 6.5, 6, 2, 'HS003', 1, 'GV002'),
('2023-2024', 'Trung bình', 6.1, 6, 6, 6.5, 6, 2, 'HS003', 2, 'GV002'),
('2023-2024', 'Trung bình', 6.6, 6.5, 6.5, 6.5, 6.5, 2, 'HS003', 3, 'GV002'),
('2023-2024', 'Yếu', 4.2, 4, 4.5, 4.5, 4, 1, 'HS004', 1, 'GV002'),
('2023-2024', 'Yếu', 4.3, 4.5, 4, 4, 4.5, 1, 'HS004', 2, 'GV002'),
('2023-2024', 'Yếu', 4.4, 4.5, 4.5, 4, 4.5, 1, 'HS004', 3, 'GV002'),
('2023-2024', 'Yếu', 4.6, 4.5, 5, 4.5, 4.5, 2, 'HS004', 1, 'GV002'),
('2023-2024', 'Yếu', 4.4, 4, 4.5, 4.5, 4.5, 2, 'HS004', 2, 'GV002'),
('2023-2024', 'Yếu', 4.7, 5, 4.5, 5, 4.5, 2, 'HS004', 3, 'GV002'),
('2023-2024', 'Tốt', 8.4, 8.5, 8, 8.5, 9, 1, 'HS005', 1, 'GV002'),
('2023-2024', 'Tốt', 8.3, 8, 8.5, 8.5, 8, 1, 'HS005', 2, 'GV002'),
('2023-2024', 'Xuất sắc', 8.9, 9, 8.5, 9, 9, 1, 'HS005', 3, 'GV002'),
('2023-2024', 'Xuất sắc', 9.1, 9, 9, 9, 9, 2, 'HS005', 1, 'GV002'),
('2023-2024', 'Tốt', 8.4, 8.5, 8, 8.5, 8.5, 2, 'HS005', 2, 'GV002'),
('2023-2024', 'Xuất sắc', 9.2, 9, 9, 9.5, 9, 2, 'HS005', 3, 'GV002'),
('2023-2024', 'Khá', 7.2, 7, 7, 7.5, 7.5, 1, 'HS006', 4, 'GV001'),
('2023-2024', 'Tốt', 8.1, 8, 8, 8.5, 8, 1, 'HS006', 5, 'GV002'),
('2023-2024', 'Khá', 7.4, 7.5, 7, 7.5, 7.5, 2, 'HS006', 4, 'GV001'),
('2023-2024', 'Tốt', 8.3, 8.5, 8, 8.5, 8.5, 2, 'HS006', 5, 'GV002'),
('2023-2024', 'Tốt', 8.2, 8, 7.5, 8, 8.5, 1, 'HS007', 4, 'GV001'),
('2023-2024', 'Khá', 7.3, 7, 7.5, 7.5, 7, 1, 'HS007', 5, 'GV002'),
('2023-2024', 'Tốt', 8.4, 8.5, 8, 8.5, 8.5, 2, 'HS007', 4, 'GV001'),
('2023-2024', 'Khá', 7.6, 7.5, 7, 8, 7.5, 2, 'HS007', 5, 'GV002');

-- Insert data into title table
INSERT INTO public.title (min_avg_score, min_behavior_score, title_name) VALUES
(8, 90, 'Học sinh xuất sắc'),
(6.5, 80, 'Học sinh giỏi'),
(5, 70, 'Học sinh khá'),
(0, 50, 'Học sinh trung bình');

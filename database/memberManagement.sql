-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 24, 2024 lúc 03:41 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE
    memberManagement CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE memberManagement;

--
-- Cơ sở dữ liệu: `memberManagement`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thanhvien`
--
CREATE TABLE `thanhvien` (
  `MaTV` bigint NOT NULL,
  `ho_ten` varchar(100) NOT NULL,
  `Khoa` varchar(100) DEFAULT NULL,
  `Nganh` varchar(100) DEFAULT NULL,
  `SDT` varchar(10) DEFAULT NULL,
  `password` varchar(1000) not null,
  `email` varchar(1000) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `thanhvien`
--

INSERT INTO `thanhvien` (`MaTV`, `ho_ten`, `Khoa`, `Nganh`, `SDT`,`password`,`email`) VALUES
(1120150184, 'Trần Thị Nữ', 'GDTH', 'GDTH', 1111111111,'123456789','test1@gmail.com'),
(1121530087, 'Trần Thiếu Nam', 'TLH', 'QLGD', 1111111112,'123456789','test2@gmail.com'),
(1123330257, 'Ngô Tuyết Nhi', 'QTKD', 'QTKD', 1111111113,'123456789','test3@gmail.com'),
(2147483647, 'Nguyễn Văn Nam', 'CNTT', 'HTTT', 123456789,'123456789','test4@gmail.com');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thietbi`
--

CREATE TABLE `thietbi` (
  `MaTB` int(10) NOT NULL,
  `TenTB` varchar(100) NOT NULL,
  `MoTaTB` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `thietbi`
--

INSERT INTO `thietbi` (`MaTB`, `TenTB`, `MoTaTB`) VALUES
(1, 'Micro', 'Micro không dây MS2023'),
(2, 'Micro', 'Micro không dây MS2024'),
(3, 'Bảng điện tử', 'Bản điện tử trình chiếu');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thongtinsd`
--

CREATE TABLE `thongtinsd` (
  `MaTT` int(10) NOT NULL,
  `MaTV` bigint NOT NULL,
  `MaTB` int(10) DEFAULT NULL,
  `TGVao` datetime DEFAULT NULL,
  `TGMuon` datetime DEFAULT NULL,
  `TGTra` datetime DEFAULT NULL,
  `TGDatCho` datetime null
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `thongtinsd`
--

INSERT INTO `thongtinsd` (`MaTT`, `MaTV`, `MaTB`, `TGVao`, `TGMuon`, `TGTra`,`TGDatCho`) VALUES
(1, 1120150184, NULL, '2024-01-05 09:00:00', NULL, NULL,NULL),
(2, 1123330257, 1, NULL, '2024-02-12 10:00:32', '2024-02-12 14:00:00',NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `xuly`
--

CREATE TABLE `xuly` (
  `MaXL` int(10) NOT NULL,
  `MaTV` bigint NOT NULL,
  `hinh_thucxl` varchar(250) DEFAULT NULL,
  `so_tien` int(100) DEFAULT NULL,
  `NgayXL` datetime DEFAULT NULL,
  `trang_thaixl` int(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `xuly`
--

INSERT INTO `xuly` (`MaXL`, `MaTV`, `hinh_thucxl`, `so_tien`, `NgayXL`, `trang_thaixl`) VALUES
(1, 1121530087, 'Khóa thẻ 1 tháng', NULL, '2023-09-12 08:00:00', 1),
(2, 2147483647, 'Khóa thẻ 2 tháng', NULL, '2023-09-12 08:00:00', 1),
(3, 1123330257, 'Bồi thường mất tài sản', 300000, '2023-09-12 08:00:00', 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `thanhvien`
--
ALTER TABLE `thanhvien`
  ADD PRIMARY KEY (`MaTV`);

--
-- Chỉ mục cho bảng `thietbi`
--
ALTER TABLE `thietbi`
  ADD PRIMARY KEY (`MaTB`);

--
-- Chỉ mục cho bảng `thongtinsd`
--
ALTER TABLE `thongtinsd`
  ADD PRIMARY KEY (`MaTT`),
  ADD KEY `MaTV` (`MaTV`,`MaTB`),
  ADD KEY `MaTB` (`MaTB`);

--
-- Chỉ mục cho bảng `xuly`
--
ALTER TABLE `xuly`
  ADD PRIMARY KEY (`MaXL`),
  ADD KEY `MaTV` (`MaTV`),
  ADD KEY `MaTV_2` (`MaTV`);

ALTER TABLE `xuly`
MODIFY COLUMN `MaXL` int(10) AUTO_INCREMENT;

  ALTER TABLE `thietbi`
MODIFY COLUMN `MaTB` int(10) AUTO_INCREMENT;

  ALTER TABLE `thongtinsd`
MODIFY COLUMN `MaTT` int(10) AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `thongtinsd`
--
ALTER TABLE `thongtinsd`
  ADD CONSTRAINT `thongtinsd_ibfk_1` FOREIGN KEY (`MaTV`) REFERENCES `thanhvien` (`MaTV`),
  ADD CONSTRAINT `thongtinsd_ibfk_2` FOREIGN KEY (`MaTB`) REFERENCES `thietbi` (`MaTB`);

--
-- Các ràng buộc cho bảng `xuly`
--
ALTER TABLE `xuly`
  ADD CONSTRAINT `xuly_ibfk_1` FOREIGN KEY (`MaTV`) REFERENCES `thanhvien` (`MaTV`);
COMMIT;


ALTER TABLE `thongtinsd`
  ADD CONSTRAINT `fk_thongtinsd_MaTV`
  FOREIGN KEY (`MaTV`) REFERENCES `thanhvien` (`MaTV`)
  ON DELETE CASCADE;

ALTER TABLE `xuly`
  ADD CONSTRAINT `fk_xuly_MaTV`
  FOREIGN KEY (`MaTV`) REFERENCES `thanhvien` (`MaTV`)
  ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
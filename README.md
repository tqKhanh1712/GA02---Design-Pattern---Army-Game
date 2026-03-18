# GA02-Design-Pattern-Army-Game

## Giới thiệu

`Army Game` là dự án Java mô phỏng trận chiến giữa hai quân đội, được xây dựng để minh họa cách áp dụng Design Pattern vào một bài toán game đơn giản.

Dự án hỗ trợ 2 cách chơi:
- CLI (menu trong terminal)
- GUI (giao diện đồ họa)

## Điểm nổi bật

- Áp dụng nhiều Design Pattern trong cùng một hệ thống
- Hỗ trợ tạo quân đội, tạo group, thêm lính và mô phỏng trận chiến
- Cho phép chạy nhanh bằng script `.bat` trên Windows
- Có cơ chế tổng kết sau trận đấu

## Design Pattern được sử dụng

- `Factory`: Tạo lính/trang bị theo từng thời đại
- `Composite`: Quản lý cấu trúc quân đội - group - lính
- `Decorator`: Gắn thêm trang bị và tăng sức mạnh linh hoạt
- `Observer`: Theo dõi sự kiện trong trận chiến
- `Proxy`: Bổ sung lớp kiểm soát truy cập đối tượng lính
- `Visitor`: Duyệt và thống kê cấu trúc quân đội

## Cấu trúc thư mục chính

```
GA02-Design-Pattern-Army-Game/
|- README.md
|- HUONG_DAN_CHAY.md
|- HUONG_DAN_FIX_CHU.md
`- Army Game/
   |- run.bat
   |- run_demo.bat
   |- run_gui.bat
   `- src/
```

## Yêu cầu môi trường

- Java JDK 8 trở lên
- Windows CMD/PowerShell (dự án đã kèm script `.bat`)

## Hướng dẫn chạy nhanh

Di chuyển vào thư mục `Army Game` trước khi chạy:

```powershell
cd "Army Game"
```

### 1) Chạy demo tự động

```bat
run_demo.bat
```

Kịch bản demo sẽ tự động tạo dữ liệu mẫu, mô phỏng trận chiến và hiển thị kết quả.

### 2) Chạy chế độ CLI

```bat
run.bat
```

Sử dụng menu để tạo đội hình, quản lý group và bắt đầu trận đấu.

### 3) Chạy chế độ GUI

```bat
run_gui.bat
```

## Xử lý lỗi font tiếng Việt

Nếu ký tự hiển thị sai trên CMD, thử chạy:

```bat
chcp 65001
```

Thông tin chi tiết: `HUONG_DAN_FIX_CHU.md`.

## Chức năng chính

- Chọn thời đại: `Medieval`, `WorldWar`, `Sci-Fi`
- Tạo quân đội và tạo group trong quân đội
- Thêm lính vào root hoặc từng group cụ thể
- Mô phỏng trận chiến giữa 2 quân đội
- Xem thống kê và tổng kết sau trận đấu

## Ghi chú

- Thư mục `docs/archive-md` lưu các tài liệu bổ sung đã được tách khỏi root để repo gọn hơn.
- Dự án phục vụ mục đích học tập và trình bày kiến trúc phần mềm.

## License

Project for educational purposes.


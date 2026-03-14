## Cách khắc phục lỗi format chữ (Méo chữ tiếng Việt)

### Vấn đề
Khi chạy demo, tiếng Việt hiển thị méo như: `╗`, `├`, `─`, `║` thay vì chữ bình thường.

### Giải pháp

#### ✅ **Cách 1: Sử dụng Windows Terminal (Khuyên dùng nhất)**
1. Cài đặt **Windows Terminal** từ Microsoft Store
2. Mở Windows Terminal (không phải CMD/PowerShell cũ)
3. Chạy demo:
   ```powershell
   cd "C:\Users\Acer\source\repos\Nam3_Ki2\tkpm\pj3\GA02-Design-Pattern-Army-Game\Army Game"
   .\run_demo.bat
   ```
   Hoặc double-click `run_demo.bat`

**Tại sao tốt hơn?** Windows Terminal hỗ trợ UTF-8 tốt hơn CMD/PowerShell cũ.

#### ✅ **Cách 2: Sử dụng run_demo.bat (Đơn giản)**
1. Double-click file `run_demo.bat` trong thư mục `Army Game`
2. Hoặc chạy từ CMD (không phải PowerShell):
   ```cmd
   cd "C:\Users\Acer\source\repos\Nam3_Ki2\tkpm\pj3\GA02-Design-Pattern-Army-Game\Army Game"
   run_demo.bat
   ```

#### ⚠️ **Tránh**
- ❌ Chạy từ PowerShell (ISE) cũ - không hỗ trợ UTF-8 tốt
- ❌ Chạy từ Git Bash - có vấn đề với Java encoding

### Nếu vẫn thấy méo chữ
Hãy thử:
1. Thay đổi code page CMD thành UTF-8:
   ```cmd
   chcp 65001
   ```
   Rồi chạy lại `run_demo.bat`

2. Hoặc thay đổi font CMD:
   - Mở CMD → Chuột phải → Properties → Font → Chọn "Cascadia Code" hoặc "Lucida Console"
   - Restart CMD

### Đường dẫn file demo
- **Batch file**: `Army Game\run_demo.bat` ← Chạy file này
- **Script PowerShell**: `Army Game\run_demo.ps1` (dùng nếu bạn thích PowerShell)
- **Source code**: `Army Game\src\DemoScriptRunner.java`


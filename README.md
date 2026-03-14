# GA02-Design-Pattern-Army-Game

## 📖 Mô tả dự án

**Army Game** là một trò chơi mô phỏng chiến thuật quân đội được xây dựng bằng Java, áp dụng các Design Pattern phổ biến để tạo ra một kiến trúc linh hoạt và dễ mở rộng. Người chơi có thể tạo quân đội từ các thế hệ khác nhau (Trung Cổ, Thế Chiến, Viễn Tưởng), trang bị vũ khí/giáp cho binh lính, và tổ chức trận chiến.

## 🎯 Các Design Pattern được sử dụng

### 1. **Factory Pattern** (`factory/`)
- Tạo binh lính và trang bị cho các thế hệ khác nhau
- `MedievalFactory`: Thế hệ Trung Cổ (Sword, Spear, Shield, Armor)
- `WorldWarFactory`: Thế hệ Thế Chiến (Rifle, Grenade, Armor, SteelHelmet)
- `SciFiFactory`: Thế hệ Viễn Tưởng (LaserSword, BioWeapon, NanoArmor)

### 2. **Composite Pattern** (`composite/`)
- Quản lý cấu trúc phân cấp quân đội
- `MilitaryUnit`: Interface chung
- `SoldierLeaf`: Binh lính cá nhân
- `Group`: Nhóm binh lính
- `Army`: Quân đội (có thể chứa nhiều Group)

### 3. **Decorator Pattern** (`equipment/`)
- Trang bị vũ khí và giáp cho binh lính một cách linh hoạt
- `EquipmentDecorator`: Base decorator
- Các trang bị: Sword, Spear, Shield, Armor, Rifle, Grenade, LaserSword, BioWeapon, NanoArmor, SteelHelmet

### 4. **Observer Pattern** (`observer/`)
- Theo dõi và báo cáo sự kiện trong trận chiến
- `Battle`: Subject quản lý trận chiến
- `DeathCountObserver`: Đếm số lượng tử vong
- `DeathNotifierObserver`: Thông báo khi có binh lính tử trận

### 5. **Proxy Pattern** (`proxy/`)
- `SoldierProxy`: Kiểm soát quyền truy cập và thêm logic bảo vệ cho Soldier

### 6. **Visitor Pattern** (`visitor/`)
- Thực hiện các thao tác trên cấu trúc quân đội
- `CountVisitor`: Đếm số lượng binh lính
- `DisplayVisitor`: Hiển thị thông tin quân đội

## 🏗️ Cấu trúc dự án

```
Army Game/
├── src/
│   ├── Main.java                    # Entry point
│   └── armygame/
│       ├── composite/               # Composite Pattern
│       │   ├── MilitaryUnit.java
│       │   ├── SoldierLeaf.java
│       │   ├── Group.java
│       │   └── Army.java
│       ├── core/                    # Core classes
│       │   ├── Soldier.java
│       │   └── SoldierType.java
│       ├── equipment/               # Decorator Pattern
│       │   ├── EquipmentDecorator.java
│       │   ├── Sword.java
│       │   ├── Spear.java
│       │   ├── Shield.java
│       │   ├── Armor.java
│       │   ├── Rifle.java
│       │   ├── Grenade.java
│       │   ├── LaserSword.java
│       │   ├── BioWeapon.java
│       │   ├── NanoArmor.java
│       │   └── SteelHelmet.java
│       ├── factory/                 # Factory Pattern
│       │   ├── SoldierFactory.java
│       │   ├── MedievalFactory.java
│       │   ├── WorldWarFactory.java
│       │   └── SciFiFactory.java
│       ├── game/                    # Game logic
│       │   ├── GameMenu.java
│       │   └── GameState.java
│       ├── observer/                # Observer Pattern
│       │   ├── BattleObserver.java
│       │   ├── Battle.java
│       │   ├── DeathCountObserver.java
│       │   └── DeathNotifierObserver.java
│       ├── proxy/                   # Proxy Pattern
│       │   └── SoldierProxy.java
│       ├── units/                   # Unit types
│       │   ├── Infantry.java
│       │   └── Cavalry.java
│       └── visitor/                 # Visitor Pattern
│           ├── UnitVisitor.java
│           ├── CountVisitor.java
│           └── DisplayVisitor.java
├── run.bat                          # Script chạy game
└── sources.txt                      # Danh sách source files
```

## 🚀 Hướng dẫn chạy

### Yêu cầu hệ thống
- **Java JDK**: Version 8 trở lên
- **Hệ điều hành**: Windows, Linux, hoặc macOS
- **Terminal**: PowerShell, CMD, hoặc Bash

### Cách chạy trên Windows

#### Cách 1: Sử dụng file run.bat (Đơn giản nhất)
1. Mở thư mục `Army Game`
2. Double-click vào file `run.bat`
3. Game sẽ tự động biên dịch và chạy

#### Cách 2: Sử dụng Command Line
```bash
cd "Army Game"
.\run.bat
```

#### Chạy Demo tự động (Batch file - khuyên dùng nhất)
**Cách đơn giản nhất - chỉ cần 1 click:**
1. Mở thư mục `Army Game`
2. Double-click vào file `run_demo.bat`
3. Demo sẽ tự động: tạo 2 quân đội × 2 nhóm/đội × 10 lính/nhóm → bắt đầu trận chiến → xem báo cáo → thoát

**Hoặc chạy từ CMD:**
```cmd
cd "Army Game"
run_demo.bat
```

**Lưu ý**: Để hiển thị tiếng Việt đúng, mở file `HUONG_DAN_FIX_CHU.md` trong thư mục gốc để xem hướng dẫn chi tiết.

### Cách chạy thủ công (Cross-platform)

#### Biên dịch:
```bash
cd "Army Game"
javac -encoding UTF-8 -d out -sourcepath src src/Main.java
```

Hoặc biên dịch tất cả file:
```bash
javac -encoding UTF-8 -d out src/**/*.java
```

#### Chạy:
```bash
java -Dfile.encoding=UTF-8 -cp out Main
```

### Lưu ý
- Để hiển thị tiếng Việt đúng trên CMD, chạy lệnh: `chcp 65001`
- File `run.bat` đã tự động cấu hình UTF-8 encoding

## 🎮 Hướng dẫn chơi

### Menu chính
Khi chạy game, bạn sẽ thấy menu với các lựa chọn:

```
══════════════════════════════════
       ARMY GAME – MENU CHÍNH     
══════════════════════════════════
  [1] Chọn thế hệ (Era)
  [2] Tạo quân đội mới
  [3] Tạo nhóm trong quân đội
  [4] Thêm binh lính vào quân đội/nhóm
  [5] Xem danh sách quân đội
  [6] Bắt đầu trận chiến
  [7] Xem báo cáo trận chiến
  [0] Thoát
```

### Các bước chơi

1. **Chọn thế hệ** (Menu [1])
   - Medieval: Trung Cổ (Kiếm, Giáo, Khiên, Giáp)
   - WorldWar: Thế Chiến (Súng trường, Lựu đạn, Giáp thép, Mũ sắt)
   - Sci-Fi: Viễn Tưởng (Kiếm laser, Vũ khí sinh học, Giáp nano)

2. **Tạo quân đội** (Menu [2])
   - Nhập tên quân đội
   - Quân đội được tạo và lưu vào danh sách

3. **Tạo nhóm** (Menu [3])
   - Chọn quân đội
   - Chọn nơi chèn: `[0]` vào quân đội hoặc `[1+]` vào nhóm cha đã có
   - Nhập tên nhóm, nhóm sẽ xuất hiện trong danh sách để thêm lính

4. **Thêm binh lính** (Menu [4])
   - Chọn quân đội
   - Chọn nơi chèn: `[0]` vào quân đội hoặc `[1+]` vào nhóm (hiển thị từ danh sách nhóm)
   - Chọn loại binh lính (Infantry, Cavalry)
   - Binh lính được thêm vào vị trí đã chọn

5. **Xem quân đội** (Menu [5])
   - Hiển thị danh sách tất cả quân đội
   - Xem chi tiết binh lính và trang bị

6. **Bắt đầu trận chiến** (Menu [6])
   - Chọn 2 quân đội để chiến đấu
   - Hệ thống tự động mô phỏng trận chiến
   - Observer theo dõi và báo cáo sự kiện

7. **Xem báo cáo** (Menu [7])
   - Xem kết quả trận chiến
   - Số lượng tử vong và thống kê

## 🧪 Kiến trúc và Thiết kế

### Ưu điểm của kiến trúc
- **Linh hoạt**: Dễ dàng thêm thế hệ mới, trang bị mới
- **Tái sử dụng**: Code được tổ chức theo pattern, dễ maintain
- **Mở rộng**: Có thể thêm observer, visitor mới mà không ảnh hưởng code cũ
- **Separation of Concerns**: Mỗi package đảm nhiệm một nhiệm vụ riêng

### Luồng hoạt động chính
```
User Input → GameMenu → GameState
    ↓
SoldierFactory (tạo binh lính)
    ↓
EquipmentDecorator (trang bị vũ khí/giáp)
    ↓
Composite Structure (tổ chức quân đội)
    ↓
Battle (trận chiến) → Observers (theo dõi sự kiện)
    ↓
Visitor (hiển thị, thống kê)
```

## 👨‍💻 Phát triển

### Thêm thế hệ mới
1. Tạo class kế thừa `SoldierFactory`
2. Implement các method tạo trang bị
3. Thêm vào menu selection trong `GameMenu`

### Thêm trang bị mới
1. Tạo class kế thừa `EquipmentDecorator`
2. Override method `getDescription()` và `getPower()`
3. Thêm vào factory tương ứng

### Thêm Observer mới
1. Implement interface `BattleObserver`
2. Đăng ký observer vào `Battle`

## 📝 Ghi chú
- Game được phát triển cho mục đích học tập Design Pattern
- Sử dụng UTF-8 encoding để hỗ trợ tiếng Việt
- Code được tổ chức theo chuẩn Java coding convention

## 📄 License
This project is for educational purposes.

---
**Developed with ❤️ using Design Patterns**


# 🚀 HƯỚNG DẪN CHẠY CHƯƠNG TRÌNH & GIẢI THÍCH CÁC FILE

## 📂 Cấu Trúc Thư Mục Chi Tiết

```
GA02-Design-Pattern-Army-Game/
│
├─ README.md ........................ Giới thiệu dự án
├─ HUONG_DAN_FIX_CHU.md ............ Hướng dẫn fix ký tự Tiếng Việt
├─ PHAN_TICH_CODE.md .............. 📍 PHÂN TÍCH CODE (PHẦN 1)
├─ PHAN_TICH_CODE_PART2.md ........ 📍 PHÂN TÍCH CODE (PHẦN 2)
├─ HUONG_DAN_CHAY.md .............. 📍 HƯỚNG DẪN CHẠY (FILE NÀY)
│
└─ Army Game/
   ├─ run.bat ...................... Batch script chạy game (Windows)
   ├─ run_demo.bat ................. Batch script chạy demo (Windows)
   ├─ run_demo.ps1 ................. PowerShell script chạy demo
   ├─ sources.txt .................. Danh sách các file nguồn
   │
   └─ src/
      ├─ Main.java ................. Entry point
      ├─ DemoScriptRunner.java ..... Script chạy tự động (nếu có)
      │
      ├─ armygame/
      │  ├─ composite/
      │  │  ├─ MilitaryUnit.java ... Interface
      │  │  ├─ SoldierLeaf.java .... Leaf node
      │  │  ├─ Group.java ......... Composite node
      │  │  └─ Army.java .......... Root node
      │  │
      │  ├─ core/
      │  │  ├─ Soldier.java ....... Abstract base
      │  │  └─ SoldierType.java ... Enum (Infantry/Cavalry)
      │  │
      │  ├─ equipment/
      │  │  ├─ EquipmentDecorator.java ... Abstract decorator
      │  │  ├─ Sword.java
      │  │  ├─ Shield.java
      │  │  ├─ Armor.java
      │  │  ├─ Spear.java
      │  │  ├─ SteelHelmet.java
      │  │  ├─ Rifle.java
      │  │  ├─ Grenade.java
      │  │  ├─ LaserSword.java
      │  │  ├─ BioWeapon.java
      │  │  └─ NanoArmor.java
      │  │
      │  ├─ factory/
      │  │  ├─ SoldierFactory.java ....... Interface
      │  │  ├─ MedievalFactory.java ..... Concrete factory
      │  │  ├─ WorldWarFactory.java .... Concrete factory
      │  │  └─ SciFiFactory.java ....... Concrete factory
      │  │
      │  ├─ game/
      │  │  ├─ GameMenu.java ......... Menu tương tác
      │  │  └─ GameState.java ....... Lưu trữ trạng thái
      │  │
      │  ├─ observer/
      │  │  ├─ BattleObserver.java ...... Interface observer
      │  │  ├─ Battle.java ............. Subject (Subject Pattern)
      │  │  ├─ DeathCountObserver.java . Observer (đếm số chết)
      │  │  └─ DeathNotifierObserver.java Observer (thông báo)
      │  │
      │  ├─ proxy/
      │  │  └─ SoldierProxy.java ....... Proxy (kiểm soát trang bị)
      │  │
      │  ├─ units/
      │  │  ├─ Infantry.java .......... Bộ binh (concrete soldier)
      │  │  └─ Cavalry.java .......... Kỵ binh (concrete soldier)
      │  │
      │  └─ visitor/
      │     ├─ UnitVisitor.java ....... Interface visitor
      │     ├─ CountVisitor.java ...... Visitor (đếm binh sĩ)
      │     └─ DisplayVisitor.java .... Visitor (hiển thị cây)
      │
      └─ out/ ........................ (Tạo tự động) Compiled bytecode
```

---

## ⚙️ Cách Chạy Chương Trình

### **Phương Pháp 1: Dùng Batch File (Windows CMD)**

```cmd
# Di chuyển vào thư mục Army Game
cd "C:\Users\Acer\source\repos\Nam3_Ki2\tkpm\pj3\GA02-Design-Pattern-Army-Game\Army Game"

# Chạy file run.bat
run.bat
```

**Nội dung file `run.bat`:**
```batch
@echo off
chcp 65001
javac -d out -sourcepath src src/Main.java
java -cp out Main
pause
```

---

### **Phương Pháp 2: Dùng PowerShell**

```powershell
# Di chuyển vào thư mục
cd "C:\Users\Acer\source\repos\Nam3_Ki2\tkpm\pj3\GA02-Design-Pattern-Army-Game\Army Game"

# Chạy PowerShell script
.\run_demo.ps1
```

**Hoặc chạy trực tiếp trong PowerShell:**
```powershell
# Fix UTF-8
$OutputEncoding = [System.Text.UTF8Encoding]::new()
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new()

# Compile
javac -d out -sourcepath src src/Main.java

# Chạy
java -cp out Main
```

---

### **Phương Pháp 3: Chạy Thủ Công từ CMD**

**Bước 1: Compile**
```cmd
javac -d out -sourcepath src src/Main.java
```

**Bước 2: Chạy**
```cmd
java -cp out Main
```

**⚠️ FIX KÝ TỰ TIẾNG VIỆT:**
```cmd
chcp 65001
```

---

## 📋 Giải Thích Các File Quan Trọng

### **Main.java** - Entry Point

```java
public class Main {
    public static void main(String[] args) throws Exception {
        // Thiết lập UTF-8 để hiển thị tiếng Việt
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        
        // Khởi động menu
        new GameMenu().start();
    }
}
```

**Vai trò:**
- ✅ Là điểm bắt đầu của chương trình
- ✅ Fix encoding UTF-8 (để hiển thị ký tự Việt)
- ✅ Khởi tạo GameMenu và bắt đầu vòng lặp

---

### **GameMenu.java** - Vòng Lặp Menu

**Struct:**
```java
public class GameMenu {
    private GameState state;
    private Scanner scanner;
    
    public void start() { /* Main loop */ }
    
    // Menu handlers
    private void handleSelectEra() { /* Chọn thế hệ */ }
    private void handleCreateArmy() { /* Tạo quân đội */ }
    private void handleCreateGroup() { /* Tạo nhóm */ }
    private void handleAddSoldierToArmy() { /* Thêm binh lính */ }
    private void handleViewArmies() { /* Xem danh sách */ }
    private void handleStartBattle() { /* Trận chiến */ }
    private void handleViewBattleReport() { /* Báo cáo */ }
}
```

**Quy trình:**
1. In banner
2. Gọi `handleSelectEra()` (bắt buộc chọn thế hệ)
3. Vòng lặp menu (while running):
   - Hiển thị menu
   - Nhập lựa chọn
   - Gọi handler tương ứng
   - Lặp lại cho đến nhập "0"

---

### **GameState.java** - Lưu Trữ Trạng Thái

```java
public class GameState {
    private SoldierFactory currentFactory;     // Factory hiện tại
    private List<Army> armies;                  // Danh sách quân đội
    
    public void addArmy(Army army) { }
    public Army getArmy(int index) { }
    public List<Army> getArmies() { }
    public SoldierFactory getCurrentFactory() { }
    public void setCurrentFactory(SoldierFactory factory) { }
}
```

**Vai trò:**
- ✅ Lưu factory hiện tại (Medieval/WorldWar/SciFi)
- ✅ Lưu danh sách quân đội đã tạo
- ✅ Cung cấp getter/setter cho GameMenu

---

### **Composite Pattern Files**

#### **MilitaryUnit.java** - Interface

```java
public interface MilitaryUnit {
    String getName();
    int getAttack();           // Tổng sức mạnh
    void defend(int damage);   // Phòng thủ
    int getMemberCount();      // Số binh sĩ
    boolean hasAliveMembers(); // Còn sống?
    void accept(UnitVisitor visitor);
    void display(String indent);
}
```

#### **SoldierLeaf.java** - Leaf (Binh Lính)

```java
public class SoldierLeaf implements MilitaryUnit {
    private SoldierProxy soldier;  // Bọc proxy
    
    @Override
    public int getAttack() {
        return soldier.getAttack();  // Delegate
    }
    
    @Override
    public void defend(int damage) {
        soldier.takeDamage(damage);  // Nhận toàn bộ sát thương
    }
}
```

#### **Group.java** - Composite (Nhóm)

```java
public class Group implements MilitaryUnit {
    protected List<MilitaryUnit> members;
    
    @Override
    public int getAttack() {
        return members.stream()
            .filter(MilitaryUnit::hasAliveMembers)
            .mapToInt(MilitaryUnit::getAttack)
            .sum();  // Cộng tất cả
    }
    
    @Override
    public void defend(int damage) {
        // Chia đều sát thương
        int perMember = damage / alive.size();
        for (member : alive) {
            member.defend(perMember);
        }
    }
}
```

#### **Army.java** - Root Composite

```java
public class Army extends Group {
    private String eraName;  // Medieval/WorldWar/SciFi
    
    // Lấy tất cả binh lính còn sống (flatten tree)
    public List<SoldierLeaf> getAliveLeaves() { }
    
    // Lấy tất cả binh lính (kể cả chết)
    public List<SoldierLeaf> getAllLeaves() { }
}
```

---

### **Factory Pattern Files**

#### **SoldierFactory.java** - Abstract Factory

```java
public interface SoldierFactory {
    SoldierProxy createInfantry(String name);
    SoldierProxy createCavalry(String name);
    String getEraName();
}
```

#### **MedievalFactory.java** - Concrete Factory

```java
public class MedievalFactory implements SoldierFactory {
    
    @Override
    public SoldierProxy createInfantry(String name) {
        SoldierProxy proxy = new SoldierProxy(new Infantry(name));
        proxy.addSword();
        proxy.addShield();
        proxy.addArmor();
        return proxy;
    }
    
    @Override
    public SoldierProxy createCavalry(String name) {
        SoldierProxy proxy = new SoldierProxy(new Cavalry(name));
        proxy.addSpear();
        proxy.addSteelHelmet();
        return proxy;
    }
}
```

**Tương tự:** `WorldWarFactory`, `SciFiFactory`

---

### **Equipment Decorator Files**

#### **EquipmentDecorator.java** - Abstract Base

```java
public abstract class EquipmentDecorator extends Soldier {
    protected Soldier wrapped;
    protected int durability;
    protected int maxDurability;
    
    public abstract String getEquipmentName();
    
    protected void degrade() {
        durability--;
        if (durability <= 0) {
            System.out.println("[HAO MÒN] Trang bị hỏng!");
        }
    }
    
    @Override
    public void takeDamage(int damage) {
        degrade();  // Hao mòn
        wrapped.takeDamage(damage);  // Truyền xuống
    }
}
```

#### **Specific Decorators**

Mỗi trang bị (Sword, Shield, Armor, etc.) extends EquipmentDecorator:

```java
public class Sword extends EquipmentDecorator {
    public Sword(Soldier wrapped) {
        super(wrapped, 15);  // durability = 15
    }
    
    @Override
    public int getAttack() {
        return wrapped.getAttack() + 15;  // +15 ATK
    }
    
    @Override
    public String getEquipmentName() {
        return "Sword";
    }
}
```

---

### **Battle.java** - Trận Chiến (Observer Subject)

```java
public class Battle {
    private Army attacker;
    private Army defender;
    private List<BattleObserver> observers;
    
    public void startBattle() {
        while (attacker.hasAliveMembers() && defender.hasAliveMembers()) {
            executeTurn();
        }
        // Thông báo người chiến thắng
        notifyBattleEnd(winner);
    }
    
    public void executeTurn() {
        // Attack phase 1
        attackPhase(attacker, defender);
        checkDeaths(defender);  // Notify observers
        
        // Attack phase 2
        attackPhase(defender, attacker);
        checkDeaths(attacker);  // Notify observers
    }
}
```

---

### **SoldierProxy.java** - Proxy (Kiểm Soát Trang Bị)

```java
public class SoldierProxy extends Soldier {
    private Soldier soldier;
    private Set<String> equippedSet;  // Tránh trùng lặp
    
    private boolean addEquipment(String name, Function<Soldier, ?> factory) {
        if (equippedSet.contains(name)) {
            System.out.println("Đã có rồi!");
            return false;
        }
        soldier = factory.apply(soldier);  // Wrap
        equippedSet.add(name);
        return true;
    }
    
    public boolean addSword() { return addEquipment("Sword", Sword::new); }
    public boolean addShield() { return addEquipment("Shield", Shield::new); }
    // ...
}
```

---

## 📊 Tổng Kết Các File

| Loại | File | Mục Đích | Pattern |
|------|------|---------|---------|
| **Core** | Soldier.java | Base class | Abstract Base |
| | Infantry.java | Bộ binh | Concrete |
| | Cavalry.java | Kỵ binh | Concrete |
| **Decorator** | EquipmentDecorator.java | Base decorator | Decorator |
| | Sword, Shield, Armor... | Trang bị cụ thể | Decorator |
| **Proxy** | SoldierProxy.java | Kiểm soát trang bị | Proxy |
| **Composite** | MilitaryUnit.java | Interface | Interface |
| | SoldierLeaf.java | Binh lính đơn lẻ | Composite |
| | Group.java | Nhóm | Composite |
| | Army.java | Quân đội | Composite |
| **Factory** | SoldierFactory.java | Interface | Factory |
| | Medieval/WorldWar/SciFi | Concrete factory | Factory |
| **Observer** | BattleObserver.java | Interface | Observer |
| | Battle.java | Subject | Subject |
| | DeathCountObserver | Observer cụ thể | Observer |
| **Visitor** | UnitVisitor.java | Interface | Visitor |
| | CountVisitor, DisplayVisitor | Concrete visitor | Visitor |
| **Game** | GameMenu.java | Menu tương tác | Controller |
| | GameState.java | Lưu trữ | State |
| | Main.java | Entry point | Main |

---

## 🐛 Gỡ Lỗi Phổ Biến

### **Lỗi 1: Ký tự Tiếng Việt không hiển thị**

**Nguyên nhân:** Mã hóa không phải UTF-8

**Cách Fix:**
```cmd
chcp 65001
```

Hoặc thêm vào Main.java:
```java
System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
```

---

### **Lỗi 2: Class không tìm thấy**

**Nguyên nhân:** Compile chưa chính xác

**Cách Fix:**
```cmd
javac -d out -sourcepath src src/Main.java
java -cp out Main
```

---

### **Lỗi 3: Cannot find symbol**

**Nguyên nhân:** Class chưa compile

**Cách Fix:**
```cmd
# Compile tất cả file
javac -d out -sourcepath src src/**/*.java

# Hoặc chỉ Main (nó sẽ tự compile dependencies)
javac -d out -sourcepath src src/Main.java
```

---

### **Lỗi 4: NoClassDefFoundError**

**Nguyên nhân:** Class path không đúng

**Cách Fix:**
```cmd
java -cp out armygame.game.GameMenu
# Không, phải chạy Main!
java -cp out Main
```

---

## 📝 Ví Dụ: Chạy Hoàn Chỉnh

```cmd
C:\> cd "C:\Users\Acer\source\repos\Nam3_Ki2\tkpm\pj3\GA02-Design-Pattern-Army-Game\Army Game"

C:\...\Army Game> chcp 65001
Active code page: 65001

C:\...\Army Game> javac -d out -sourcepath src src/Main.java

C:\...\Army Game> java -cp out Main

╔════════════════════════════════════╗
║         ⚔  ARMY GAME  ⚔           ║
║   Design Pattern Demo – Java       ║
╚════════════════════════════════════╝

─── Chọn thế hệ ───
  [1] Medieval  (Trung Cổ)       – Sword, Spear, Shield, Armor
  [2] WorldWar  (Thế Chiến)      – Rifle, Grenade, Armor, SteelHelmet
  [3] Sci-Fi    (Viễn Tưởng)     – LaserSword, BioWeapon, NanoArmor
  Nhập lựa chọn: 1
  → Đã chọn thế hệ: Medieval

══════════════════════════════════
       ARMY GAME – MENU CHÍNH
  Thế hệ hiện tại: Medieval
══════════════════════════════════
  [1] Chọn thế hệ (Era)
  [2] Tạo quân đội mới
  [3] Tạo nhóm trong quân đội
  [4] Thêm binh lính vào quân đội/nhóm
  [5] Xem danh sách quân đội
  [6] Bắt đầu trận chiến
  [7] Xem báo cáo trận chiến
  [0] Thoát
  Nhập lựa chọn: 2
```

---

## 🎓 Tóm Tắt

1. **Chạy chương trình**: `chcp 65001` → Compile → Run Java
2. **Main.java**: Entry point, fix UTF-8
3. **GameMenu.java**: Vòng lặp menu chính
4. **GameState.java**: Lưu trữ quân đội + factory
5. **Pattern được sử dụng**: Decorator, Proxy, Composite, Factory, Observer, Visitor

🚀 **Bây giờ bạn có thể hiểu và chạy chương trình!**


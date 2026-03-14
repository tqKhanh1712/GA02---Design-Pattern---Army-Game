package armygame.game;

import armygame.composite.Army;
import armygame.composite.Group;
import armygame.composite.SoldierLeaf;
import armygame.factory.MedievalFactory;
import armygame.factory.SciFiFactory;
import armygame.factory.SoldierFactory;
import armygame.factory.WorldWarFactory;
import armygame.observer.Battle;
import armygame.observer.DeathCountObserver;
import armygame.observer.DeathNotifierObserver;
import armygame.proxy.SoldierProxy;
import armygame.visitor.CountVisitor;
import armygame.visitor.DisplayVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * GAME LOOP – Menu tương tác CMD
 * Điều phối toàn bộ game thông qua GameState.
 */
public class GameMenu {

    private final GameState state   = new GameState();
    private final Scanner   scanner = new Scanner(System.in);

    // ── Entry point ──────────────────────────────────────────────────────
    public void start() {
        printBanner();
        // Chọn thế hệ ngay khi bắt đầu
        handleSelectEra();

        boolean running = true;
        while (running) {
            showMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleSelectEra();
                case "2" -> handleCreateArmy();
                case "3" -> handleCreateGroup();
                case "4" -> handleAddSoldierToArmy();
                case "5" -> handleViewArmies();
                case "6" -> handleStartBattle();
                case "7" -> handleViewBattleReport();
                case "0" -> running = false;
                default  -> System.out.println("  Lựa chọn không hợp lệ, thử lại.");
            }
        }
        System.out.println("\nTạm biệt! Cảm ơn đã chơi Army Game.");
        scanner.close();
    }

    // ── Main Menu ────────────────────────────────────────────────────────
    private void showMainMenu() {
        String era = state.getCurrentFactory() == null
                ? "(chưa chọn)" : state.getCurrentFactory().getEraName();
        System.out.println("\n══════════════════════════════════");
        System.out.println("       ARMY GAME – MENU CHÍNH     ");
        System.out.println("  Thế hệ hiện tại: " + era);
        System.out.println("══════════════════════════════════");
        System.out.println("  [1] Chọn thế hệ (Era)");
        System.out.println("  [2] Tạo quân đội mới");
        System.out.println("  [3] Tạo nhóm trong quân đội");
        System.out.println("  [4] Thêm binh lính vào quân đội/nhóm");
        System.out.println("  [5] Xem danh sách quân đội");
        System.out.println("  [6] Bắt đầu trận chiến");
        System.out.println("  [7] Xem báo cáo trận chiến");
        System.out.println("  [0] Thoát");
        System.out.print("  Nhập lựa chọn: ");
    }

    // ── [1] Chọn thế hệ ─────────────────────────────────────────────────
    private void handleSelectEra() {
        System.out.println("\n─── Chọn thế hệ ───");
        System.out.println("  [1] Medieval  (Trung Cổ)       – Sword, Spear, Shield, Armor");
        System.out.println("  [2] WorldWar  (Thế Chiến)      – Rifle, Grenade, Armor, SteelHelmet");
        System.out.println("  [3] Sci-Fi    (Viễn Tưởng)     – LaserSword, BioWeapon, NanoArmor");
        System.out.print("  Nhập lựa chọn: ");

        SoldierFactory factory = switch (scanner.nextLine().trim()) {
            case "1" -> new MedievalFactory();
            case "2" -> new WorldWarFactory();
            case "3" -> new SciFiFactory();
            default  -> { System.out.println("  Không hợp lệ, giữ nguyên."); yield state.getCurrentFactory(); }
        };
        if (factory != null) {
            state.setCurrentFactory(factory);
            System.out.println("  → Đã chọn thế hệ: " + factory.getEraName());
        }
    }

    // ── [2] Tạo quân đội ────────────────────────────────────────────────
    private void handleCreateArmy() {
        if (state.getCurrentFactory() == null) {
            System.out.println("  Vui lòng chọn thế hệ trước!"); return;
        }
        System.out.print("\n  Nhập tên quân đội: ");
        String name = scanner.nextLine().trim();
        if (name.isBlank()) { System.out.println("  Tên không được trống."); return; }

        Army army = new Army(name, state.getCurrentFactory().getEraName());
        state.addArmy(army);
        System.out.println("  → Đã tạo quân đội: " + name
                + " [" + army.getEraName() + "]");
    }

    // ── [4] Thêm binh lính ───────────────────────────────────────────────
    private void handleAddSoldierToArmy() {
        if (state.getArmies().isEmpty()) {
            System.out.println("  Chưa có quân đội nào!"); return;
        }
        // Chọn quân đội
        printArmyList();
        System.out.print("  Chọn quân đội (số thứ tự): ");
        int armyIdx = parseIndex(scanner.nextLine());
        Army army = state.getArmy(armyIdx);
        if (army == null) { System.out.println("  Chỉ số không hợp lệ."); return; }

        // Chọn nhóm hoặc thêm vào root
        System.out.println("\n  Thêm vào:");
        System.out.println("  [0] Trực tiếp vào quân đội");
        List<Group> groups = listAllGroups(army);
        printGroupList(groups);
        System.out.print("  Lựa chọn (0 = quân đội, 1+ = nhóm): ");
        String grpChoice = scanner.nextLine().trim();

        // Tên binh lính
        System.out.print("  Tên binh lính: ");
        String soldierName = scanner.nextLine().trim();
        if (soldierName.isBlank()) { System.out.println("  Tên không được trống."); return; }

        // Loại binh lính
        System.out.println("  [1] Bộ Binh  [2] Kỵ Binh");
        System.out.print("  Loại: ");
        String typeChoice = scanner.nextLine().trim();

        SoldierFactory factory = getFactoryForArmy(army);
        SoldierProxy proxy = typeChoice.equals("2")
                ? factory.createCavalry(soldierName)
                : factory.createInfantry(soldierName);

        SoldierLeaf leaf = new SoldierLeaf(proxy);

        if (grpChoice.equals("0")) {
            army.add(leaf);
        } else {
            int grpIdx = parseIndex(grpChoice) - 1;
            Group group = (grpIdx >= 0 && grpIdx < groups.size()) ? groups.get(grpIdx) : null;
            if (group == null) { System.out.println("  Nhóm không tồn tại, thêm vào quân đội."); army.add(leaf); }
            else group.add(leaf);
        }

        System.out.println("  → Đã thêm: " + proxy.describe());
    }

    // ── [3] Tạo nhóm ────────────────────────────────────────────────────
    private void handleCreateGroup() {
        if (state.getArmies().isEmpty()) {
            System.out.println("  Chưa có quân đội nào!"); return;
        }
        printArmyList();
        System.out.print("  Chọn quân đội (số thứ tự): ");
        Army army = state.getArmy(parseIndex(scanner.nextLine()));
        if (army == null) { System.out.println("  Chỉ số không hợp lệ."); return; }

        List<Group> groups = listAllGroups(army);
        System.out.println("\n  Thêm nhóm vào:");
        System.out.println("  [0] Quân đội (root)");
        printGroupList(groups);
        System.out.print("  Lựa chọn (0 = quân đội, 1+ = nhóm cha): ");
        String parentChoice = scanner.nextLine().trim();

        System.out.print("  Tên nhóm: ");
        String groupName = scanner.nextLine().trim();
        if (groupName.isBlank()) { System.out.println("  Tên không được trống."); return; }

        Group newGroup = new Group(groupName);
        if (parentChoice.equals("0")) {
            army.add(newGroup);
        } else {
            int grpIdx = parseIndex(parentChoice) - 1;
            Group parent = (grpIdx >= 0 && grpIdx < groups.size()) ? groups.get(grpIdx) : null;
            if (parent == null) { System.out.println("  Nhóm không tồn tại, thêm vào quân đội."); army.add(newGroup); }
            else parent.add(newGroup);
        }

        System.out.println("  → Đã tạo nhóm: " + groupName);
    }

    // ── [5] Xem quân đội ─────────────────────────────────────────────────
    private void handleViewArmies() {
        if (state.getArmies().isEmpty()) {
            System.out.println("  Chưa có quân đội nào."); return;
        }
        for (Army army : state.getArmies()) {
            System.out.println();
            army.display("");
            System.out.println();

            // Visitor: hiển thị chi tiết
            DisplayVisitor dv = new DisplayVisitor();
            army.accept(dv);

            // Visitor: đếm
            CountVisitor cv = new CountVisitor();
            army.accept(cv);
            cv.printReport();
        }
    }

    // ── [6] Bắt đầu trận chiến ───────────────────────────────────────────
    private void handleStartBattle() {
        if (state.getArmies().size() < 2) {
            System.out.println("  Cần ít nhất 2 quân đội để chiến đấu!"); return;
        }

        printArmyList();
        System.out.print("  Chọn quân đội TẤN CÔNG (số thứ tự): ");
        Army attacker = state.getArmy(parseIndex(scanner.nextLine()));

        System.out.print("  Chọn quân đội PHÒNG THỦ (số thứ tự): ");
        Army defender = state.getArmy(parseIndex(scanner.nextLine()));

        if (attacker == null || defender == null || attacker == defender) {
            System.out.println("  Lựa chọn không hợp lệ."); return;
        }

        // Reset observers trước trận mới
        DeathCountObserver.getInstance().reset();
        DeathNotifierObserver.getInstance().reset();

        Battle battle = new Battle(attacker, defender);
        battle.addObserver(DeathCountObserver.getInstance());    // Singleton
        battle.addObserver(DeathNotifierObserver.getInstance()); // Singleton
        state.setCurrentBattle(battle);

        battle.startBattle();
    }

    // ── [7] Báo cáo ──────────────────────────────────────────────────────
    private void handleViewBattleReport() {
        if (state.getCurrentBattle() == null) {
            System.out.println("  Chưa có trận chiến nào được thực hiện."); return;
        }
        DeathCountObserver.getInstance().printReport();
        System.out.println("\n  Danh sách tử trận:");
        DeathNotifierObserver.getInstance().getFallenNames()
                .forEach(n -> System.out.println("    • " + n));
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private void printBanner() {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║         ⚔  ARMY GAME  ⚔           ║");
        System.out.println("║   Design Pattern Demo – Java       ║");
        System.out.println("╚════════════════════════════════════╝");
    }

    private void printArmyList() {
        System.out.println("\n  Danh sách quân đội:");
        List<Army> armies = state.getArmies();
        for (int i = 0; i < armies.size(); i++) {
            System.out.printf("  [%d] %s [%s] – %d binh sĩ%n",
                    i, armies.get(i).getName(), armies.get(i).getEraName(),
                    armies.get(i).getMemberCount());
        }
    }

    private void printGroupList(List<Group> groups) {
        for (int i = 0; i < groups.size(); i++) {
            System.out.printf("  [%d] Nhóm: %s%n", i + 1, groups.get(i).getName());
        }
    }

    private int parseIndex(String input) {
        try { return Integer.parseInt(input.trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private List<Group> listAllGroups(Army army) {
        List<Group> result = new ArrayList<>();
        collectGroups(army, result);
        return result;
    }

    private void collectGroups(Group current, List<Group> out) {
        for (var m : current.getMembers()) {
            if (m instanceof Group g) {
                out.add(g);
                collectGroups(g, out);
            }
        }
    }

    /**
     * Lấy factory tương thích với thế hệ của quân đội.
     * Nếu không khớp, dùng factory hiện tại.
     */
    private SoldierFactory getFactoryForArmy(Army army) {
        return switch (army.getEraName()) {
            case "Medieval" -> new MedievalFactory();
            case "WorldWar" -> new WorldWarFactory();
            case "Sci-Fi"   -> new SciFiFactory();
            default         -> state.getCurrentFactory();
        };
    }

}




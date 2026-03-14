import armygame.game.GameMenu;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Chạy demo tự động: giả lập thao tác chọn era, tạo 2 army, mỗi army có 2 nhóm,
 * mỗi nhóm có 10 lính (2 loại khác nhau), xem quân đội, bắt đầu trận chiến, xem báo cáo rồi thoát.
 * Hữu ích khi muốn trình diễn nhanh mà không cần nhập tay.
 */
public class DemoScriptRunner {

    public static void main(String[] args) throws Exception {
        // Thiết lập UTF-8 cho console
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        List<String> scriptLines = new ArrayList<>();

        // 1) Chọn era (Medieval)
        scriptLines.add("1");

        // 2) Tạo 2 quân đội: Alpha, Beta
        scriptLines.add("2"); scriptLines.add("Alpha");
        scriptLines.add("2"); scriptLines.add("Beta");

        // 3) Mỗi quân đội có 2 nhóm (gắn vào root level)
        // Format: menu [3], army idx, parent (0=root), group name
        scriptLines.add("3"); scriptLines.add("0"); scriptLines.add("0"); scriptLines.add("Alpha Group 1");
        scriptLines.add("3"); scriptLines.add("0"); scriptLines.add("0"); scriptLines.add("Alpha Group 2");
        scriptLines.add("3"); scriptLines.add("1"); scriptLines.add("0"); scriptLines.add("Beta Group 1");
        scriptLines.add("3"); scriptLines.add("1"); scriptLines.add("0"); scriptLines.add("Beta Group 2");

        // 4) Thêm lính: mỗi nhóm 10 người, 2 loại khác nhau
        // Alpha Group 1: 10 Infantry
        appendSoldiers(scriptLines, 0, 1, "A1 Soldier", "1", 10);
        // Alpha Group 2: 10 Cavalry
        appendSoldiers(scriptLines, 0, 2, "A2 Soldier", "2", 10);
        // Beta Group 1: 10 Infantry
        appendSoldiers(scriptLines, 1, 1, "B1 Soldier", "1", 10);
        // Beta Group 2: 10 Cavalry
        appendSoldiers(scriptLines, 1, 2, "B2 Soldier", "2", 10);

        // 5) Xem quân đội
        scriptLines.add("5");

        // 6) Bắt đầu trận chiến: Alpha tấn công Beta
        scriptLines.add("6");
        scriptLines.add("0");  // attacker = Alpha
        scriptLines.add("1");  // defender = Beta

        // 7) Xem báo cáo
        scriptLines.add("7");

        // 8) Thoát
        scriptLines.add("0");

        String script = String.join("\n", scriptLines) + "\n";

        // Bơm script vào System.in để GameMenu đọc như nhập tay
        System.setIn(new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)));

        new GameMenu().start();
    }

    /**
     * Thêm nhiều lính vào một nhóm cụ thể.
     * @param cmds danh sách lệnh
     * @param armyIdx chỉ số quân đội (0 = Alpha, 1 = Beta)
     * @param groupIdx chỉ số nhóm (1-based như menu hiển thị)
     * @param baseName tiền tố tên lính
     * @param typeChoice "1" = Infantry, "2" = Cavalry
     * @param count số lượng lính cần thêm
     */
    private static void appendSoldiers(List<String> cmds, int armyIdx, int groupIdx,
                                       String baseName, String typeChoice, int count) {
        for (int i = 1; i <= count; i++) {
            cmds.add("4");                      // Menu: [4] thêm binh lính
            cmds.add(String.valueOf(armyIdx));  // Chọn quân đội
            cmds.add(String.valueOf(groupIdx)); // Chọn nhóm (1+)
            cmds.add(baseName + " " + i);       // Tên binh lính
            cmds.add(typeChoice);               // Loại lính
        }
    }
}



package armygame.observer;

import armygame.composite.Army;
import armygame.composite.SoldierLeaf;
import armygame.core.Soldier;

import java.util.ArrayList;
import java.util.List;

/**
 * SUBJECT – Observer Pattern (Phần 3.1)
 * Quản lý một trận chiến giữa hai quân đội.
 * Thông báo cho tất cả observer khi có binh lính tử trận hoặc trận chiến kết thúc.
 */
public class Battle {

    private final Army attacker;
    private final Army defender;

    private final List<BattleObserver> observers = new ArrayList<>();
    private int turnCount    = 0;
    private int totalDeaths  = 0;
    private boolean active   = false;

    public Battle(Army attacker, Army defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    // ── Quản lý Observer ─────────────────────────────────────────────────
    public void addObserver(BattleObserver obs)    { observers.add(obs); }
    public void removeObserver(BattleObserver obs) { observers.remove(obs); }

    private void notifyDeath(Soldier soldier, String armyName) {
        totalDeaths++;
        observers.forEach(o -> o.onSoldierDeath(soldier, armyName));
    }

    public void notifyBattleEnd(String winner) {
        observers.forEach(o -> o.onBattleEnd(winner, totalDeaths));
    }

    // ── Logic trận chiến ─────────────────────────────────────────────────

    /** Thực hiện một lượt: attacker đánh defender rồi ngược lại */
    public void executeTurn() {
        turnCount++;
        System.out.println("\n══════════ LƯỢT " + turnCount + " ══════════");

        // Attacker đánh Defender
        attackPhase(attacker, defender);
        checkDeaths(defender);

        if (!defender.hasAliveMembers()) return;

        // Defender phản công Attacker
        attackPhase(defender, attacker);
        checkDeaths(attacker);
    }

    /** Quân A tấn công quân B: toàn bộ ATK của A áp lên B */
    private void attackPhase(Army atk, Army def) {
        int totalAtk = atk.getAttack();
        System.out.printf("[%s] tấn công [%s] với %d sức mạnh!%n",
                atk.getName(), def.getName(), totalAtk);
        def.defend(totalAtk);
    }

    /** Kiểm tra binh lính vừa tử trận, gửi thông báo */
    private void checkDeaths(Army army) {
        List<SoldierLeaf> all = army.getAllLeaves();
        for (SoldierLeaf leaf : all) {
            if (!leaf.hasAliveMembers()) {
                // Tránh thông báo lặp (dùng flag đơn giản qua tên)
                Soldier s = leaf.getSoldier();
                if (!reportedDeaths.contains(s.getName())) {
                    reportedDeaths.add(s.getName());
                    notifyDeath(s, army.getName());
                }
            }
        }
    }

    private final List<String> reportedDeaths = new ArrayList<>();

    /** Chạy toàn bộ trận chiến đến khi một bên bị tiêu diệt */
    public void startBattle() {
        active = true;
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       TRẬN CHIẾN BẮT ĐẦU!           ║");
        System.out.printf ("║  %-18s  VS  %-12s║%n", attacker.getName(), defender.getName());
        System.out.println("╚══════════════════════════════════════╝");

        while (attacker.hasAliveMembers() && defender.hasAliveMembers()) {
            executeTurn();
        }

        String winner = attacker.hasAliveMembers() ? attacker.getName() : defender.getName();
        System.out.println("\n★★★ Trận chiến kết thúc! Người chiến thắng: " + winner + " ★★★");
        notifyBattleEnd(winner);
        active = false;
    }

    public boolean isActive()  { return active; }
    public int getTurnCount()  { return turnCount; }
    public int getTotalDeaths(){ return totalDeaths; }
    public Army getAttacker()  { return attacker; }
    public Army getDefender()  { return defender; }
}


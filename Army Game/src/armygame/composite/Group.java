package armygame.composite;

import armygame.visitor.UnitVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSITE – Composite Pattern (Phần 2.1)
 * Đại diện cho một nhóm binh lính có thể chứa các Leaf hoặc Group con.
 *
 * Quy tắc:
 *  - getAttack()   = tổng getAttack() của tất cả thành viên
 *  - defend(dmg)   = chia đều dmg/count cho mỗi thành viên còn sống
 *                    (số dư cộng vào thành viên đầu tiên còn sống)
 */
public class Group implements MilitaryUnit {

    protected String name;
    protected final List<MilitaryUnit> members = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    // ── Quản lý thành viên ────────────────────────────────────────────────
    public void add(MilitaryUnit unit)    { members.add(unit); }
    public void remove(MilitaryUnit unit) { members.remove(unit); }
    public List<MilitaryUnit> getMembers() { return members; }

    // ── MilitaryUnit ──────────────────────────────────────────────────────
    @Override public String getName() { return name; }

    @Override
    public int getAttack() {
        return members.stream()
                .filter(MilitaryUnit::hasAliveMembers)
                .mapToInt(MilitaryUnit::getAttack)
                .sum();
    }

    public int getDefense() {
        return members.stream()
                .filter(MilitaryUnit::hasAliveMembers)
                .mapToInt(this::getMemberDefense)
                .sum();
    }

    private int getMemberDefense(MilitaryUnit unit) {
        if (unit instanceof SoldierLeaf leaf) {
            return leaf.getSoldier().getDefense();
        } else if (unit instanceof Group g) {
            return g.getDefense();
        }
        return 0;
    }

    @Override
    public void defend(int totalDamage) {
        List<MilitaryUnit> alive = members.stream()
                .filter(MilitaryUnit::hasAliveMembers)
                .toList();
        if (alive.isEmpty()) return;

        int perMember = totalDamage / alive.size();
        int remainder = totalDamage % alive.size();

        System.out.printf("  [GROUP: %s] Phân phối %d sát thương cho %d thành viên "
                + "(mỗi người ~%d, dư %d)%n",
                name, totalDamage, alive.size(), perMember, remainder);

        for (int i = 0; i < alive.size(); i++) {
            int dmg = (i == 0) ? perMember + remainder : perMember;
            alive.get(i).defend(dmg);
        }
    }

    @Override
    public int getMemberCount() {
        return members.stream().mapToInt(MilitaryUnit::getMemberCount).sum();
    }

    @Override
    public boolean hasAliveMembers() {
        return members.stream().anyMatch(MilitaryUnit::hasAliveMembers);
    }

    @Override
    public void accept(UnitVisitor visitor) {
        visitor.visit(this);
        members.forEach(m -> m.accept(visitor));
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "┌─ [Nhóm] " + name
                + " (" + getMemberCount() + " binh sĩ)");
        members.forEach(m -> m.display(indent + "│  "));
    }
}


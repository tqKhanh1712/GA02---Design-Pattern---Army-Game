package armygame.game;

import armygame.composite.Army;
import armygame.composite.Group;
import armygame.composite.SoldierLeaf;
import armygame.factory.MedievalFactory;
import armygame.factory.SciFiFactory;
import armygame.factory.SoldierFactory;
import armygame.factory.WorldWarFactory;
import armygame.observer.Battle;
import armygame.observer.BattleObserver;
import armygame.observer.DeathCountObserver;
import armygame.observer.DeathNotifierObserver;
import armygame.proxy.SoldierProxy;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * ENHANCED GUI - Army Game v2.1 with Complete Features
 * Includes: Group management, Army preview, Battle log, Full functionality
 */
public class GameUIFrame extends JFrame {

    private final GameState state = new GameState();
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JPanel setupPanel;
    private BattlePanel battlePanel;
    private BattleResultPanel resultPanel;

    private static final Color BG_DARK = new Color(25, 28, 40);
    private static final Color BG_MEDIUM = new Color(40, 45, 65);
    private static final Color ACCENT_GOLD = new Color(255, 215, 0);
    private static final Color ACCENT_BLUE = new Color(100, 150, 255);
    private static final Color ACCENT_RED = new Color(255, 100, 100);

    public GameUIFrame() {
        setTitle("ARMY GAME - Design Pattern Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(1080, 680));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        setupPanel = createSetupPanel();
        battlePanel = new BattlePanel(this);
        resultPanel = new BattleResultPanel(this);

        cardPanel.add(setupPanel, "SETUP");
        cardPanel.add(battlePanel, "BATTLE");
        cardPanel.add(resultPanel, "RESULT");

        add(cardPanel);
        setVisible(true);
        cardLayout.show(cardPanel, "SETUP");
    }

    public void showBattleScreen(Battle battle) {
        battlePanel.setBattle(battle, this);
        cardLayout.show(cardPanel, "BATTLE");
    }

    public void showResultScreen(String winner, Battle battle) {
        resultPanel.setBattle(battle, winner);
        cardLayout.show(cardPanel, "RESULT");
    }

    public void backToSetup() {
        cardLayout.show(cardPanel, "SETUP");
    }

    // ─────────────────────────────────────────────────────────────
    // SETUP PANEL - Complete Features
    // ─────────────────────────────────────────────────────────────
    private JPanel createSetupPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, BG_DARK, 0, getHeight(), BG_MEDIUM);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("[ ARMY GAME COMMAND CENTER ]");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(ACCENT_GOLD);
        headerPanel.add(titleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_MEDIUM);
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 13));
        styleTabbedPane(tabbedPane);
        tabbedPane.addTab("Army Management", createArmyManagementTab());
        tabbedPane.addTab("Groups", createGroupManagementTab());
        tabbedPane.addTab("Preview", createPreviewTab());
        tabbedPane.addTab("Settings", createSettingsTab());
        
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(tabbedPane, BorderLayout.CENTER);
        centerWrapper.add(Box.createVerticalStrut(30), BorderLayout.SOUTH);
        panel.add(centerWrapper, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(BG_DARK);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JButton btnStartBattle = createStyledButton("START BATTLE", new Color(220, 20, 60), 14);
        btnStartBattle.addActionListener(e -> startBattle());

        JButton btnExit = createStyledButton("EXIT", new Color(100, 100, 110), 12);
        btnExit.addActionListener(e -> System.exit(0));

        footerPanel.add(btnStartBattle);
        footerPanel.add(btnExit);
        panel.add(footerPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ─────────────────────────────────────────────────────────────
    // ARMY MANAGEMENT TAB
    // ─────────────────────────────────────────────────────────────
    private JPanel createArmyManagementTab() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(BG_MEDIUM);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setOpaque(true);

        // Era Selection
        JPanel eraPanel = createSectionPanel("SELECT ERA");
        String[] eras = {"Medieval", "WorldWar", "Sci-Fi"};
        JComboBox<String> eraCombo = createStyledComboBox(eras);
        JButton btnSelectEra = createStyledButton("SELECT ERA", new Color(70, 130, 180), 12);
        btnSelectEra.addActionListener(e -> {
            String selected = (String) eraCombo.getSelectedItem();
            SoldierFactory factory = switch (selected) {
                case "Medieval" -> new MedievalFactory();
                case "WorldWar" -> new WorldWarFactory();
                case "Sci-Fi" -> new SciFiFactory();
                default -> null;
            };
            if (factory != null) {
                state.setCurrentFactory(factory);
                JOptionPane.showMessageDialog(panel, "[OK] Era selected: " + selected,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        eraPanel.add(new JLabel("Era:"));
        eraPanel.add(eraCombo);
        eraPanel.add(btnSelectEra);
        panel.add(eraPanel);
        panel.add(Box.createVerticalStrut(15));

        // Army Creation
        JPanel armyPanel = createSectionPanel("CREATE ARMY");
        JTextField armyNameField = createStyledTextField(20);
        JButton btnCreateArmy = createStyledButton("CREATE", new Color(34, 139, 34), 12);
        btnCreateArmy.addActionListener(e -> {
            if (state.getCurrentFactory() == null) {
                JOptionPane.showMessageDialog(panel, "Please select an era first!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String name = armyNameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Army name cannot be empty!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Army army = new Army(name, state.getCurrentFactory().getEraName());
            state.addArmy(army);
            armyNameField.setText("");
            JOptionPane.showMessageDialog(panel, "[OK] Army '" + name + "' created!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        armyPanel.add(new JLabel("Name:"));
        armyPanel.add(armyNameField);
        armyPanel.add(btnCreateArmy);
        panel.add(armyPanel);
        panel.add(Box.createVerticalStrut(15));

        // Add Soldiers
        JPanel soldierPanel = createSectionPanel("ADD SOLDIERS");
        soldierPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JComboBox<String> armyCombo = createStyledComboBox(new String[]{});
        JTextField soldierNameField = createStyledTextField(15);
        String[] soldierTypes = {"Infantry", "Cavalry"};
        JComboBox<String> typeCombo = createStyledComboBox(soldierTypes);

        JButton btnAddSoldier = createStyledButton("ADD SOLDIER", new Color(255, 140, 0), 12);
        btnAddSoldier.addActionListener(e -> addSoldierAction(armyCombo, soldierNameField, typeCombo, panel));

        JButton btnRefreshArmies = createStyledButton("REFRESH", new Color(100, 100, 150), 12);
        btnRefreshArmies.addActionListener(e -> {
            armyCombo.removeAllItems();
            for (Army army : state.getArmies()) {
                armyCombo.addItem(army.getName());
            }
        });

        soldierPanel.add(new JLabel("Army:"));
        soldierPanel.add(armyCombo);
        soldierPanel.add(new JLabel("Soldier Name:"));
        soldierPanel.add(soldierNameField);
        soldierPanel.add(new JLabel("Type:"));
        soldierPanel.add(typeCombo);
        soldierPanel.add(btnAddSoldier);
        soldierPanel.add(btnRefreshArmies);

        panel.add(soldierPanel);
        panel.add(Box.createVerticalGlue());
        tintLabels(panel);

        return panel;
    }

    // ─────────────────────────────────────────────────────────────
    // GROUP MANAGEMENT TAB (NEW)
    // ─────────────────────────────────────────────────────────────
    private JPanel createGroupManagementTab() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(BG_MEDIUM);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setOpaque(true);

        // Create Group
        JPanel groupPanel = createSectionPanel("CREATE GROUP");
        JComboBox<String> groupArmyCombo = createStyledComboBox(new String[]{});
        JTextField groupNameField = createStyledTextField(20);
        
        JButton btnCreateGroup = createStyledButton("CREATE GROUP", new Color(34, 139, 34), 12);
        btnCreateGroup.addActionListener(e -> {
            if (groupArmyCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(panel, "Create an army first!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String groupName = groupNameField.getText().trim();
            if (groupName.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Group name cannot be empty!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int armyIdx = groupArmyCombo.getSelectedIndex();
            Army army = state.getArmy(armyIdx);
            Group newGroup = new Group(groupName);
            army.add(newGroup);
            groupNameField.setText("");
            JOptionPane.showMessageDialog(panel, "[OK] Group '" + groupName + "' created!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnRefreshGroupArmies = createStyledButton("REFRESH", new Color(100, 100, 150), 12);
        btnRefreshGroupArmies.addActionListener(e -> {
            groupArmyCombo.removeAllItems();
            for (Army army : state.getArmies()) {
                groupArmyCombo.addItem(army.getName());
            }
        });

        groupPanel.setLayout(new GridLayout(3, 2, 10, 10));
        groupPanel.add(new JLabel("Army:"));
        groupPanel.add(groupArmyCombo);
        groupPanel.add(new JLabel("Group Name:"));
        groupPanel.add(groupNameField);
        groupPanel.add(btnCreateGroup);
        groupPanel.add(btnRefreshGroupArmies);
        panel.add(groupPanel);
        panel.add(Box.createVerticalStrut(15));

        // Add Soldiers to Group
        JPanel addToGroupPanel = createSectionPanel("ADD SOLDIERS TO GROUP");
        addToGroupPanel.setLayout(new BorderLayout(10, 10));

        JComboBox<String> groupArmyCombo2 = createStyledComboBox(new String[]{});
        JComboBox<String> groupSelectCombo = createStyledComboBox(new String[]{});
        JComboBox<String> existingSoldiersCombo = createStyledComboBox(new String[]{});

        JButton btnAddToGroup = createStyledButton("ADD TO GROUP", new Color(255, 140, 0), 12);
        btnAddToGroup.addActionListener(e -> {
            if (groupArmyCombo2.getItemCount() == 0) {
                JOptionPane.showMessageDialog(panel, "Create army first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (groupSelectCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(panel, "Create group first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (existingSoldiersCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(panel, "Add soldiers to army first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int armyIdx = groupArmyCombo2.getSelectedIndex();
            int groupIdx = groupSelectCombo.getSelectedIndex();
            int soldierIdx = existingSoldiersCombo.getSelectedIndex();
            
            Army army = state.getArmy(armyIdx);
            Group selectedGroup = null;
            int count = 0;
            for (var member : army.getMembers()) {
                if (member instanceof Group g) {
                    if (count == groupIdx) {
                        selectedGroup = g;
                        break;
                    }
                    count++;
                }
            }

            if (selectedGroup != null) {
                // Get soldier from army
                List<SoldierLeaf> armyLeaves = new ArrayList<>();
                for (var leaf : army.getAllLeaves()) {
                    boolean found = false;
                    for (var member : selectedGroup.getMembers()) {
                        if (member == leaf) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        armyLeaves.add(leaf);
                    }
                }

                if (soldierIdx >= 0 && soldierIdx < armyLeaves.size()) {
                    SoldierLeaf leafToMove = armyLeaves.get(soldierIdx);
                    // Remove from army root and add to group
                    army.remove(leafToMove);
                    selectedGroup.add(leafToMove);
                    
                    // Refresh soldiers list - check ALL groups
                    existingSoldiersCombo.removeAllItems();
                    for (var leaf : army.getAllLeaves()) {
                        boolean inAnyGroup = false;
                        
                        // Check if soldier is in ANY group
                        for (var member : army.getMembers()) {
                            if (member instanceof Group g) {
                                for (var gMember : g.getMembers()) {
                                    if (gMember == leaf) {
                                        inAnyGroup = true;
                                        break;
                                    }
                                }
                                if (inAnyGroup) break;
                            }
                        }
                        
                        // Only show if NOT in any group
                        if (!inAnyGroup) {
                            existingSoldiersCombo.addItem(leaf.getSoldier().describe());
                        }
                    }
                    
                    JOptionPane.showMessageDialog(panel, "[OK] Soldier moved to group!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton btnRefreshGroups = createStyledButton("REFRESH", new Color(100, 100, 150), 12);
        btnRefreshGroups.addActionListener(e -> {
            groupArmyCombo2.removeAllItems();
            for (Army army : state.getArmies()) {
                groupArmyCombo2.addItem(army.getName());
            }
        });

        JButton btnLoadGroups = createStyledButton("LOAD", new Color(100, 100, 150), 12);
        btnLoadGroups.addActionListener(e -> {
            groupSelectCombo.removeAllItems();
            existingSoldiersCombo.removeAllItems();
            if (groupArmyCombo2.getItemCount() > 0) {
                int armyIdx = groupArmyCombo2.getSelectedIndex();
                Army army = state.getArmy(armyIdx);
                for (var member : army.getMembers()) {
                    if (member instanceof Group g) {
                        groupSelectCombo.addItem(g.getName());
                    }
                }
            }
        });

        // Auto-update soldiers when changing group
        groupSelectCombo.addActionListener(e -> {
            existingSoldiersCombo.removeAllItems();
            if (groupArmyCombo2.getItemCount() > 0 && groupSelectCombo.getItemCount() > 0) {
                int armyIdx = groupArmyCombo2.getSelectedIndex();
                int groupIdx = groupSelectCombo.getSelectedIndex();
                
                Army army = state.getArmy(armyIdx);
                
                // Find selected group
                Group selectedGroup = null;
                int count = 0;
                for (var member : army.getMembers()) {
                    if (member instanceof Group g) {
                        if (count == groupIdx) {
                            selectedGroup = g;
                            break;
                        }
                        count++;
                    }
                }
                
                // Show only soldiers NOT in any group
                if (selectedGroup != null) {
                    List<SoldierLeaf> availableSoldiers = new ArrayList<>();
                    
                    // Get all soldiers in army
                    for (var leaf : army.getAllLeaves()) {
                        boolean inAnyGroup = false;
                        
                        // Check if in any group
                        for (var member : army.getMembers()) {
                            if (member instanceof Group g) {
                                for (var gMember : g.getMembers()) {
                                    if (gMember == leaf) {
                                        inAnyGroup = true;
                                        break;
                                    }
                                }
                                if (inAnyGroup) break;
                            }
                        }
                        
                        // Add only if not in any group
                        if (!inAnyGroup) {
                            availableSoldiers.add(leaf);
                        }
                    }
                    
                    // Add direct soldiers (not in groups)
                    for (var member : army.getMembers()) {
                        if (member instanceof SoldierLeaf leaf) {
                            existingSoldiersCombo.addItem(leaf.getSoldier().describe());
                        }
                    }
                }
            }
        });

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.add(new JLabel("Army:"));
        formPanel.add(groupArmyCombo2);
        formPanel.add(new JLabel("Select Group:"));
        formPanel.add(groupSelectCombo);
        formPanel.add(new JLabel("Soldier:"));
        formPanel.add(existingSoldiersCombo);

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(btnRefreshGroups);
        actionPanel.add(btnLoadGroups);
        actionPanel.add(btnAddToGroup);

        addToGroupPanel.add(formPanel, BorderLayout.CENTER);
        addToGroupPanel.add(actionPanel, BorderLayout.SOUTH);

        panel.add(addToGroupPanel);
        panel.add(Box.createVerticalGlue());
        tintLabels(panel);

        return panel;
    }

    private void addSoldierAction(JComboBox<String> armyCombo, JTextField soldierNameField, 
                                   JComboBox<String> typeCombo, JPanel panel) {
        if (state.getCurrentFactory() == null) {
            JOptionPane.showMessageDialog(panel, "Please select an era first!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (armyCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(panel, "Please create an army first!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String soldierName = soldierNameField.getText().trim();
        if (soldierName.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Soldier name cannot be empty!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int armyIdx = armyCombo.getSelectedIndex();
        Army army = state.getArmy(armyIdx);
        String type = (String) typeCombo.getSelectedItem();

        SoldierProxy proxy = type.equals("Cavalry")
                ? state.getCurrentFactory().createCavalry(soldierName)
                : state.getCurrentFactory().createInfantry(soldierName);

        var leaf = new SoldierLeaf(proxy);
        army.add(leaf);
        soldierNameField.setText("");
        JOptionPane.showMessageDialog(panel, "[OK] Soldier added: " + soldierName,
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createSettingsTab() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(BG_MEDIUM);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setOpaque(true);

        JLabel titleLabel = new JLabel("GAME SETTINGS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(ACCENT_GOLD);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));

        JTextArea infoArea = new JTextArea(
            "GAME MECHANICS:\n" +
            "  • Turn-based combat system\n" +
            "  • Damage = Total Army ATK - Individual Defense\n" +
            "  • Damage is distributed equally among alive members\n\n" +
            "DESIGN PATTERNS:\n" +
            "  • Composite: Army hierarchy (Army -> Group -> Soldier)\n" +
            "  • Decorator: Equipment system with durability\n" +
            "  • Factory: Era-specific soldier creation\n" +
            "  • Observer: Battle event notifications\n" +
            "  • Proxy: Soldier access control\n" +
            "  • Visitor: Tree traversal for statistics\n\n" +
            "FEATURES:\n" +
            "  • Create multiple armies\n" +
            "  • Organize soldiers into groups\n" +
            "  • View army composition\n" +
            "  • Watch detailed battle logs\n" +
            "  • See survivor statistics"
        );
        infoArea.setBackground(new Color(50, 50, 70));
        infoArea.setForeground(Color.WHITE);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBackground(new Color(50, 50, 70));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(90, 100, 130), 1));
        panel.add(scrollPane);
        tintLabels(panel);

        return panel;
    }

    private JPanel createPreviewTab() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(BG_MEDIUM);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(true);

        JTextArea textArea = new JTextArea();
        textArea.setBackground(new Color(50, 50, 70));
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        textArea.setEditable(false);

        JButton btnRefresh = createStyledButton("REFRESH PREVIEW", new Color(70, 130, 180), 12);
        btnRefresh.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════\n");
            sb.append("         ARMIES & GROUPS PREVIEW\n");
            sb.append("═══════════════════════════════════════════\n\n");

            if (state.getArmies().isEmpty()) {
                sb.append("No armies created yet.\n");
            } else {
                for (int i = 0; i < state.getArmies().size(); i++) {
                    Army army = state.getArmies().get(i);
                    sb.append(String.format("[%d] %s (%s)\n", i, army.getName(), army.getEraName()));
                    sb.append(String.format("    Total Members: %d\n", army.getMemberCount()));
                    sb.append(String.format("    Total ATK: %d | Total DEF: %d\n", army.getAttack(), army.getDefense()));
                    sb.append(String.format("    Total HP: %d\n", army.getMemberCount() * 100));
                    
                    // Show groups and soldiers
                    List<SoldierLeaf> leaves = army.getAllLeaves();
                    List<Group> groups = new ArrayList<>();
                    for (var member : army.getMembers()) {
                        if (member instanceof Group g) {
                            groups.add(g);
                        } else if (member instanceof SoldierLeaf leaf) {
                            sb.append(String.format("    └─ (Direct) %s\n", leaf.getSoldier().describe()));
                        }
                    }
                    
                    if (!groups.isEmpty()) {
                        sb.append("    GROUPS:\n");
                        for (Group group : groups) {
                            sb.append(String.format("      └─ 📦 %s (%d members)\n", group.getName(), group.getMemberCount()));
                            for (var member : group.getMembers()) {
                                if (member instanceof SoldierLeaf leaf) {
                                    String status = leaf.hasAliveMembers() ? "[ALIVE]" : "[DEAD]";
                                    sb.append(String.format("         %s %s\n", status, leaf.getSoldier().describe()));
                                }
                            }
                        }
                    }
                    sb.append("\n");
                }
            }

            textArea.setText(sb.toString());
        });

        JPanel topPanel = new JPanel();
        topPanel.setBackground(BG_MEDIUM);
        topPanel.add(btnRefresh);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBackground(new Color(50, 50, 70));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(90, 100, 130), 1));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        tintLabels(panel);

        return panel;
    }

    private void startBattle() {
        if (state.getArmies().size() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least 2 armies to start battle!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Army> armies = state.getArmies();
        String[] armyNames = armies.stream().map(Army::getName).toArray(String[]::new);

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridLayout(2, 2, 10, 10));

        JComboBox<String> attackerCombo = createStyledComboBox(armyNames);
        JComboBox<String> defenderCombo = createStyledComboBox(armyNames);
        if (armyNames.length > 1) defenderCombo.setSelectedIndex(1);

        selectionPanel.add(new JLabel("Attacker:"));
        selectionPanel.add(attackerCombo);
        selectionPanel.add(new JLabel("Defender:"));
        selectionPanel.add(defenderCombo);

        int result = JOptionPane.showConfirmDialog(this, selectionPanel, 
            "Select Armies for Battle", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Army attacker = armies.get(attackerCombo.getSelectedIndex());
            Army defender = armies.get(defenderCombo.getSelectedIndex());

            if (attacker == defender) {
                JOptionPane.showMessageDialog(this, "Attacker and defender must be different!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DeathCountObserver.getInstance().reset();
            DeathNotifierObserver.getInstance().reset();

            Battle battle = new Battle(attacker, defender);
            battle.addObserver(DeathCountObserver.getInstance());
            battle.addObserver(DeathNotifierObserver.getInstance());

            showBattleScreen(battle);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // HELPER METHODS
    // ─────────────────────────────────────────────────────────────

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(48, 54, 78));
        TitledBorder titled = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(105, 118, 155), 1),
                title
        );
        titled.setTitleColor(ACCENT_GOLD);
        titled.setTitleFont(new Font("Arial", Font.BOLD, 12));
        panel.setBorder(BorderFactory.createCompoundBorder(
                titled,
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, int fontSize) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, fontSize));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220, 90), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(new Color(60, 60, 80));
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Arial", Font.PLAIN, 12));
        combo.setBorder(BorderFactory.createLineBorder(new Color(110, 120, 150), 1));
        return combo;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBackground(new Color(60, 60, 80));
        field.setForeground(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(110, 120, 150), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }

    private void styleTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                selectedTabPadInsets = new Insets(8, 12, 8, 12);
                tabAreaInsets = new Insets(4, 4, 4, 4);
                contentBorderInsets = new Insets(1, 1, 1, 1);
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                              int x, int y, int w, int h, boolean isSelected) {
                g.setColor(isSelected ? new Color(67, 78, 112) : new Color(44, 50, 73));
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                          int x, int y, int w, int h, boolean isSelected) {
                g.setColor(new Color(100, 112, 145));
                g.drawRect(x, y, w, h);
            }
        });
    }

    private void tintLabels(Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof JLabel label) {
                label.setForeground(new Color(238, 242, 255));
            } else if (c instanceof Container child) {
                tintLabels(child);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // BATTLE PANEL - With Battle Log
    // ═══════════════════════════════════════════════════════════════
    private static class BattlePanel extends JPanel {
        private GameUIFrame frame;
        private Battle battle;
        private List<String> battleLog = new ArrayList<>();
        private Timer animationTimer;
        private int turnNumber = 0;
        private boolean battleOver = false;

        // HP tracking for smooth damage display
        private int attackerMaxHP = 0;
        private int defenderMaxHP = 0;
        private int attackerCurrentHP = 0;
        private int defenderCurrentHP = 0;

        // Custom observer for logging
        private class BattleLogObserver implements BattleObserver {
            @Override
            public void onSoldierDeath(armygame.core.Soldier soldier, String armyName) {
                battleLog.add("[DEAD] " + soldier.getName() + " (" + armyName + ") has fallen!");
            }

            @Override
            public void onBattleEnd(String winner, int totalDeaths) {
                battleLog.add("\n[BATTLE END] " + winner + " WINS!");
            }
        }

        BattlePanel(GameUIFrame frame) {
            this.frame = frame;
            setBackground(BG_DARK);
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

        void setBattle(Battle battle, GameUIFrame frame) {
            this.frame = frame;
            this.battle = battle;
            this.turnNumber = 0;
            this.battleOver = false;
            this.battleLog.clear();

            // Add log observer
            battle.addObserver(new BattleLogObserver());

            // Initialize HP tracking from actual soldier stats
            attackerMaxHP = sumMaxHp(battle.getAttacker());
            defenderMaxHP = sumMaxHp(battle.getDefender());
            attackerCurrentHP = sumCurrentHp(battle.getAttacker());
            defenderCurrentHP = sumCurrentHp(battle.getDefender());

            // Create UI
            removeAll();
            
            // Top: Battle info
            JPanel topPanel = new JPanel();
            topPanel.setBackground(BG_MEDIUM);
            topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            
            JLabel battleTitle = new JLabel("BATTLE IN PROGRESS");
            battleTitle.setFont(new Font("Arial", Font.BOLD, 24));
            battleTitle.setForeground(ACCENT_GOLD);
            topPanel.add(battleTitle);
            add(topPanel, BorderLayout.NORTH);

            // Center: Battle visualization + Log
            JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            centerPanel.setBackground(BG_DARK);
            
            // Left: Battle animation
            JPanel battleVisual = new BattleVisualization(battle, this);
            centerPanel.add(battleVisual);

            // Right: Battle log
            JTextArea logArea = new JTextArea();
            logArea.setBackground(new Color(50, 50, 70));
            logArea.setForeground(Color.WHITE);
            logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            logArea.setEditable(false);
            logArea.setText("Battle started!\n" + battle.getAttacker().getName() + " vs " + battle.getDefender().getName() + "\n\n");
            
            JScrollPane logScroll = new JScrollPane(logArea);
            logScroll.setBackground(new Color(50, 50, 70));
            centerPanel.add(logScroll);

            add(centerPanel, BorderLayout.CENTER);
            revalidate();
            repaint();

            // Start animation
            if (animationTimer != null) animationTimer.stop();
            animationTimer = new Timer(1500, e -> executeTurnWithAnimation(battle, logArea));
            animationTimer.start();
        }

        private void executeTurnWithAnimation(Battle battle, JTextArea logArea) {
            if (battleOver) {
                animationTimer.stop();
                return;
            }

            // Get current status before turn
            int attackerAliveBefore = (int) battle.getAttacker().getAllLeaves().stream()
                    .filter(leaf -> leaf.hasAliveMembers()).count();
            int defenderAliveBefore = (int) battle.getDefender().getAllLeaves().stream()
                    .filter(leaf -> leaf.hasAliveMembers()).count();
            int attackerHpBefore = sumCurrentHp(battle.getAttacker());
            int defenderHpBefore = sumCurrentHp(battle.getDefender());

            if (!battle.getAttacker().hasAliveMembers() || !battle.getDefender().hasAliveMembers()) {
                battleOver = true;
                String winner = battle.getAttacker().hasAliveMembers()
                        ? battle.getAttacker().getName() : battle.getDefender().getName();
                battle.notifyBattleEnd(winner);
                
                // Update log
                StringBuilder log = new StringBuilder(logArea.getText());
                log.append("\n").append(String.join("\n", battleLog));
                logArea.setText(log.toString());

                animationTimer.stop();
                Timer delayTimer = new Timer(2000, e -> frame.showResultScreen(winner, battle));
                delayTimer.setRepeats(false);
                delayTimer.start();
                return;
            }

            // Execute turn
            battle.executeTurn();
            turnNumber = battle.getTurnCount();

            // Get status after turn
            int attackerAliveAfter = (int) battle.getAttacker().getAllLeaves().stream()
                    .filter(leaf -> leaf.hasAliveMembers()).count();
            int defenderAliveAfter = (int) battle.getDefender().getAllLeaves().stream()
                    .filter(leaf -> leaf.hasAliveMembers()).count();

            // Calculate real HP reduction for this turn
            int attackerHpAfter = sumCurrentHp(battle.getAttacker());
            int defenderHpAfter = sumCurrentHp(battle.getDefender());
            int attackerDamage = Math.max(0, attackerHpBefore - attackerHpAfter);
            int defenderDamage = Math.max(0, defenderHpBefore - defenderHpAfter);

            // Update HP tracking (real values)
            attackerCurrentHP = attackerHpAfter;
            defenderCurrentHP = defenderHpAfter;

            // Detailed turn log
            String turnLog = String.format(
                "+- Turn %d\n| %s attacks: Damage = %d (ATK: %d - DEF: %d)\n| %s HP: %d -> %d (--%d)\n| %s HP: %d -> %d (--%d)\n| Survivors: %d vs %d\n+-\n",
                turnNumber,
                battle.getAttacker().getName(), Math.max(0, battle.getAttacker().getAttack() - battle.getDefender().getDefense()),
                battle.getAttacker().getAttack(), battle.getDefender().getDefense(),
                battle.getAttacker().getName(), attackerHpBefore, attackerCurrentHP, attackerDamage,
                battle.getDefender().getName(), defenderHpBefore, defenderCurrentHP, defenderDamage,
                attackerAliveAfter, defenderAliveAfter
            );

            // Update log area
            logArea.append(turnLog);
            logArea.setCaretPosition(logArea.getDocument().getLength());

            repaint();
        }

        private int sumCurrentHp(Army army) {
            return army.getAllLeaves().stream()
                    .mapToInt(leaf -> Math.max(0, leaf.getSoldier().getHealth()))
                    .sum();
        }

        private int sumMaxHp(Army army) {
            return army.getAllLeaves().stream()
                    .mapToInt(leaf -> Math.max(0, leaf.getSoldier().getMaxHealth()))
                    .sum();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    // Battle visualization
    private static class BattleVisualization extends JPanel {
        private Battle battle;
        private BattlePanel battlePanel;

        BattleVisualization(Battle battle, BattlePanel battlePanel) {
            this.battle = battle;
            this.battlePanel = battlePanel;
            setBackground(BG_DARK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (battle == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Responsive two-column layout to avoid bar overlap
            int margin = 20;
            int gap = 24;
            int cardWidth = Math.max(260, (width - (margin * 2) - gap) / 2);
            int leftX = margin;
            int rightX = leftX + cardWidth + gap;
            int topY = 40;

            // Attacker - use tracked HP instead of just alive count
            drawArmy(g2d, leftX, topY, cardWidth, battle.getAttacker(), true,
                    battlePanel.attackerCurrentHP, battlePanel.attackerMaxHP);

            // VS
            g2d.setColor(new Color(200, 50, 50));
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("VS", width / 2 - 18, topY + 180);

            // Defender - use tracked HP
            drawArmy(g2d, rightX, topY, cardWidth, battle.getDefender(), false,
                    battlePanel.defenderCurrentHP, battlePanel.defenderMaxHP);
        }

        private void drawArmy(Graphics2D g2d, int x, int y, int cardWidth, Army army, boolean isLeft, int currentHP, int maxHP) {
            g2d.setColor(isLeft ? ACCENT_BLUE : ACCENT_RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString(army.getName(), x + 6, y);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 13));
            int alive = (int) army.getAllLeaves().stream().filter(l -> l.hasAliveMembers()).count();
            g2d.drawString("Members: " + alive + " / " + army.getMemberCount(), x + 6, y + 30);
            g2d.drawString("ATK: " + army.getAttack() + " | DEF: " + army.getDefense(), x + 6, y + 50);

            // Health bar - use provided HP values for smooth damage display
            int barWidth = Math.max(180, cardWidth - 16);
            int barHeight = 40;

            int barX = x + 6;
            int barY = y + 70;

            g2d.setColor(new Color(50, 50, 70));
            g2d.fillRect(barX, barY, barWidth, barHeight);

            int fillWidth = maxHP > 0 ? (int) ((double) currentHP / maxHP * barWidth) : 0;
            Color barColor = fillWidth > barWidth * 0.5 ? new Color(0, 200, 0)
                    : fillWidth > barWidth * 0.25 ? new Color(255, 165, 0) : new Color(255, 0, 0);
            g2d.setColor(barColor);
            g2d.fillRect(barX, barY, fillWidth, barHeight);

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(barX, barY, barWidth, barHeight);

            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            String hpText = currentHP + " / " + maxHP;
            FontMetrics fm = g2d.getFontMetrics();
            int hpX = barX + (barWidth - fm.stringWidth(hpText)) / 2;
            g2d.setColor(Color.BLACK);
            g2d.drawString(hpText, hpX + 1, barY + 25);
            g2d.setColor(Color.WHITE);
            g2d.drawString(hpText, hpX, barY + 24);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // BATTLE RESULT PANEL - With working buttons
    // ═══════════════════════════════════════════════════════════════
    private static class BattleResultPanel extends JPanel {
        private GameUIFrame frame;
        private Battle battle;
        private String winner;
        private int btn1X, btn2X, btnY, btnW, btnH;
        private int hoveredButton = 0;

        BattleResultPanel(GameUIFrame frame) {
            this.frame = frame;
            setBackground(BG_DARK);
            setLayout(null);
            setFocusable(true);
            
            // Add mouse listener
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    // Check ADJUST ARMIES button
                    if (e.getX() >= btn1X && e.getX() <= btn1X + btnW
                        && e.getY() >= btnY && e.getY() <= btnY + btnH) {
                        frame.backToSetup();
                    }
                    // Check NEW BATTLE button
                    else if (e.getX() >= btn2X && e.getX() <= btn2X + btnW
                             && e.getY() >= btnY && e.getY() <= btnY + btnH) {
                        frame.backToSetup();
                    }
                }
            });

            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseMoved(java.awt.event.MouseEvent e) {
                    int h = 0;
                    if (e.getX() >= btn1X && e.getX() <= btn1X + btnW
                            && e.getY() >= btnY && e.getY() <= btnY + btnH) {
                        h = 1;
                    } else if (e.getX() >= btn2X && e.getX() <= btn2X + btnW
                            && e.getY() >= btnY && e.getY() <= btnY + btnH) {
                        h = 2;
                    }
                    if (h != hoveredButton) {
                        hoveredButton = h;
                        repaint();
                    }
                }
            });
        }

        void setBattle(Battle battle, String winner) {
            this.battle = battle;
            this.winner = winner;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Background overlay
            g2d.setColor(new Color(0, 0, 0, 115));
            g2d.fillRect(0, 0, width, height);

            // Center card
            int cardW = Math.min(860, width - 80);
            int cardH = Math.min(540, height - 90);
            int cardX = (width - cardW) / 2;
            int cardY = (height - cardH) / 2;

            GradientPaint cardGradient = new GradientPaint(
                    cardX, cardY, new Color(34, 42, 70),
                    cardX, cardY + cardH, new Color(23, 29, 50)
            );
            g2d.setPaint(cardGradient);
            g2d.fillRoundRect(cardX, cardY, cardW, cardH, 22, 22);
            g2d.setColor(new Color(105, 118, 155));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(cardX, cardY, cardW, cardH, 22, 22);

            // Header stripe
            g2d.setColor(new Color(51, 61, 96));
            g2d.fillRoundRect(cardX + 1, cardY + 1, cardW - 2, 88, 20, 20);

            // Winner
            g2d.setColor(ACCENT_GOLD);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            String msg = "WINNER: " + winner;
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, cardX + (cardW - fm.stringWidth(msg)) / 2, cardY + 60);

            g2d.setColor(new Color(215, 224, 255));
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            String sub = "Battle completed successfully";
            FontMetrics sfm = g2d.getFontMetrics();
            g2d.drawString(sub, cardX + (cardW - sfm.stringWidth(sub)) / 2, cardY + 84);

            // Statistics
            if (battle != null) {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.drawString("Battle Summary", cardX + 32, cardY + 140);

                g2d.setFont(new Font("Arial", Font.PLAIN, 22));
                int summaryY = cardY + 195;

                Army attacker = battle.getAttacker();
                Army defender = battle.getDefender();

                int attackerAlive = (int) attacker.getAllLeaves().stream().filter(l -> l.hasAliveMembers()).count();
                int defenderAlive = (int) defender.getAllLeaves().stream().filter(l -> l.hasAliveMembers()).count();

                int statW = cardW - 64;
                int statX = cardX + 32;
                int rowH = 50;

                drawStatRow(g2d, statX, summaryY - 30, statW, rowH,
                        attacker.getName() + " survivors", attackerAlive + " / " + attacker.getMemberCount());
                drawStatRow(g2d, statX, summaryY + 28, statW, rowH,
                        defender.getName() + " survivors", defenderAlive + " / " + defender.getMemberCount());
                drawStatRow(g2d, statX, summaryY + 86, statW, rowH,
                        "Total Turns", String.valueOf(battle.getTurnCount()));
                drawStatRow(g2d, statX, summaryY + 144, statW, rowH,
                        "Total Casualties", String.valueOf(battle.getTotalDeaths()));
            }

            // Buttons
            btnW = 240;
            btnH = 52;
            btnY = cardY + cardH - 78;
            int gap = 26;
            btn1X = cardX + (cardW - (btnW * 2 + gap)) / 2;
            btn2X = btn1X + btnW + gap;

            drawButton(g2d, btn1X, btnY, btnW, btnH, 
                "<- ADJUST ARMIES", new Color(100, 150, 255));
            drawButton(g2d, btn2X, btnY, btnW, btnH, 
                "NEW BATTLE ->", new Color(220, 20, 60));
        }

        private void drawStatRow(Graphics2D g2d, int x, int y, int w, int h, String label, String value) {
            g2d.setColor(new Color(50, 61, 95, 220));
            g2d.fillRoundRect(x, y, w, h, 10, 10);
            g2d.setColor(new Color(119, 132, 173));
            g2d.setStroke(new BasicStroke(1f));
            g2d.drawRoundRect(x, y, w, h, 10, 10);

            g2d.setColor(new Color(226, 233, 255));
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            g2d.drawString(label, x + 16, y + 31);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(value, x + w - fm.stringWidth(value) - 16, y + 32);
        }

        private void drawButton(Graphics2D g2d, int x, int y, int w, int h, String text, Color color) {
            Color fill = color;
            if ((hoveredButton == 1 && text.startsWith("<-")) || (hoveredButton == 2 && text.startsWith("NEW"))) {
                fill = color.brighter();
            }

            g2d.setColor(fill);
            g2d.fillRoundRect(x, y, w, h, 12, 12);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(x, y, w, h, 12, 12);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(text, x + (w - fm.stringWidth(text)) / 2, y + ((h + fm.getAscent()) / 2) - 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameUIFrame());
    }
}




























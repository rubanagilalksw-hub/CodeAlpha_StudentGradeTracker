package studentgradetracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    static final Color SIDEBAR_TOP = new Color(108, 52, 220);
    static final Color SIDEBAR_BOT = new Color(180, 60, 220);
    static final Color BG_TOP      = new Color(245, 240, 255);
    static final Color BG_BOT      = new Color(255, 245, 255);
    static final Color PURPLE      = new Color(108, 52, 220);
    static final Color WHITE       = Color.WHITE;

    private ArrayList<Student> students = new ArrayList<>();
    private CardLayout cards = new CardLayout();
    private JPanel contentPane = new JPanel(cards);

    // Exact emojis from the reference screenshot
    private String[] navIcons  = {"🏠", "➕", "👥", "📊", "ℹ"};
    private String[] navNames  = {"Dashboard","Add Student","View Students","Statistics","About"};
    private JButton[] navBtns  = new JButton[5];

    private DashboardPanel   dashPanel;
    private AddStudentPanel  addPanel;
    private ViewStudentsPanel viewPanel;
    private StatisticsPanel  statsPanel;
    private AboutPanel       aboutPanel;

    public MainFrame() {
        setTitle("Student Grade Tracker");
        setSize(980, 640);
        setMinimumSize(new Dimension(900, 580));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        students.add(new Student(1, "Ali Khan",      85));
        students.add(new Student(2, "Sana Fatima",   92));
        students.add(new Student(3, "Hamza Siddiqui",78));
        students.add(new Student(4, "Ayesha Noor",   88));
        students.add(new Student(5, "Usman Ahmed",   65));

        buildUI();
        showCard("Dashboard");
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOT));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);

        contentPane.setOpaque(false);
        dashPanel  = new DashboardPanel(students, this);
        addPanel   = new AddStudentPanel(students, this);
        viewPanel  = new ViewStudentsPanel(students, this);
        statsPanel = new StatisticsPanel(students);
        aboutPanel = new AboutPanel();

        contentPane.add(dashPanel,  "Dashboard");
        contentPane.add(addPanel,   "Add Student");
        contentPane.add(viewPanel,  "View Students");
        contentPane.add(statsPanel, "Statistics");
        contentPane.add(aboutPanel, "About");
        root.add(contentPane, BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel sb = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, SIDEBAR_TOP, 0, getHeight(), SIDEBAR_BOT));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sb.setPreferredSize(new Dimension(175, 0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));

        // Graduation cap icon at top
        sb.add(Box.createVerticalStrut(18));
        JLabel logo = new JLabel("🎓", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));
        logo.setAlignmentX(CENTER_ALIGNMENT);
        sb.add(logo);
        sb.add(Box.createVerticalStrut(14));

        // Nav buttons with real emojis
        for (int i = 0; i < navNames.length; i++) {
            final String name = navNames[i];
            final String icon = navIcons[i];
            JButton btn = makeSideBtn(icon + "  " + name);
            btn.addActionListener(e -> { showCard(name); setActive(name); });
            navBtns[i] = btn;
            sb.add(btn);
            sb.add(Box.createVerticalStrut(3));
        }

        sb.add(Box.createVerticalGlue());

        // Nelson Mandela quote
        JTextArea quote = new JTextArea(
            "\"Education is the most\npowerful weapon which\nyou can use to change\nthe world.\"\n– Nelson Mandela");
        quote.setEditable(false);
        quote.setOpaque(false);
        quote.setForeground(new Color(230, 220, 255));
        quote.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        quote.setAlignmentX(CENTER_ALIGNMENT);
        quote.setMaximumSize(new Dimension(160, 90));
        quote.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 8));
        sb.add(quote);
        sb.add(Box.createVerticalStrut(8));

        // Trophy icon
        JLabel trophy = new JLabel("🏆", SwingConstants.CENTER);
        trophy.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        trophy.setAlignmentX(CENTER_ALIGNMENT);
        sb.add(trophy);
        sb.add(Box.createVerticalStrut(10));

        // Exit button with power emoji
        JButton exitBtn = makeSideBtn("⏻  Exit");
        exitBtn.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "Exit karna chahte hain?", "Exit", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) System.exit(0);
        });
        sb.add(exitBtn);
        sb.add(Box.createVerticalStrut(15));

        setActive("Dashboard");
        return sb;
    }

    private JButton makeSideBtn(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = Boolean.TRUE.equals(getClientProperty("active"));
                if (active) {
                    g2.setColor(new Color(255, 255, 255, 70));
                    g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 12, 12);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 35));
                    g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(WHITE);
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMaximumSize(new Dimension(165, 38));
        b.setPreferredSize(new Dimension(165, 38));
        b.setAlignmentX(CENTER_ALIGNMENT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 8));
        return b;
    }

    public void setActive(String name) {
        for (int i = 0; i < navNames.length; i++) {
            navBtns[i].putClientProperty("active", navNames[i].equals(name));
            navBtns[i].repaint();
        }
    }

    public void showCard(String name) {
        cards.show(contentPane, name);
        setActive(name);
        if (name.equals("Dashboard"))     dashPanel.refresh();
        if (name.equals("View Students")) viewPanel.refresh();
        if (name.equals("Statistics"))    statsPanel.refresh();
    }

    public ArrayList<Student> getStudents() { return students; }
}

package studentgradetracker;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ViewStudentsPanel extends JPanel {

    private ArrayList<Student> students;
    private MainFrame frame;
    private DefaultTableModel model;
    private JTable table;
    private JTextField tfSearch = new JTextField(16);
    private static final Color PURPLE = MainFrame.PURPLE;

    public ViewStudentsPanel(ArrayList<Student> students, MainFrame frame) {
        this.students = students; this.frame = frame;
        setOpaque(false); setLayout(new BorderLayout()); build();
    }

    private void build() {
        add(makeTitle(), BorderLayout.NORTH);

        RoundedPanel card = new RoundedPanel(Color.WHITE, 18);
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(BorderFactory.createEmptyBorder(12, 14, 14, 14));

        // ── header ────────────────────────────────────
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        JLabel lbl = new JLabel("\uD83D\uDC65  VIEW ALL STUDENTS");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(PURPLE);
        hdr.add(lbl, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 185, 230), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        tfSearch.setPreferredSize(new Dimension(160, 30));
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterTable(tfSearch.getText().trim()); }
        });
        right.add(new JLabel("\uD83D\uDD0D "));
        right.add(tfSearch);

        JButton bDel = pill("\uD83D\uDDD1 Delete", new Color(255, 69, 90));
        bDel.addActionListener(e -> deleteSelected());
        right.add(bDel);
        hdr.add(right, BorderLayout.EAST);
        card.add(hdr, BorderLayout.NORTH);

        // ── table ─────────────────────────────────────
        model = new DefaultTableModel(new String[]{"ID","Name","Marks","Percentage","Grade","Letter Grade"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 247, 255));
                return c;
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(34); table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(210, 190, 255));

        JTableHeader th = table.getTableHeader();
        th.setDefaultRenderer(new DefaultTableCellRenderer() {
            { setOpaque(true); }
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setBackground(PURPLE); setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
                return this;
            }
        });
        th.setPreferredSize(new Dimension(0, 36));

        // badge renderer col 5
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                String g = v == null ? "" : v.toString();
                Color bg;
                switch (g) {
                    case "A": bg = new Color(52, 199, 110); break;
                    case "B": bg = new Color(52, 130, 246); break;
                    case "C": bg = new Color(255, 193, 7);  break;
                    case "D": bg = new Color(255, 149, 0);  break;
                    default:  bg = new Color(255, 69, 58);
                }
                JPanel p = new JPanel(new GridBagLayout());
                p.setBackground(sel ? new Color(210,190,255) : (row%2==0 ? Color.WHITE : new Color(250,247,255)));
                JLabel badge = new JLabel(g, SwingConstants.CENTER) {
                    @Override protected void paintComponent(Graphics g2d) {
                        Graphics2D g2 = (Graphics2D) g2d.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(bg); g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20); g2.dispose();
                        super.paintComponent(g2d);
                    }
                };
                badge.setForeground(Color.WHITE); badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
                badge.setOpaque(false); badge.setPreferredSize(new Dimension(36, 24));
                p.add(badge); return p;
            }
        });

        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        cr.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < 5; i++) table.getColumnModel().getColumn(i).setCellRenderer(cr);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        card.add(sp, BorderLayout.CENTER);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(6, 14, 4, 14));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH; gc.weightx = 1; gc.weighty = 1;
        wrap.add(card, gc);
        add(wrap, BorderLayout.CENTER);
        add(makeWave(), BorderLayout.SOUTH);
    }

    private JPanel makeTitle() {
        JPanel p = new JPanel(); p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(14, 0, 6, 0));
        JLabel t1 = new JLabel("Student Grade Tracker", SwingConstants.CENTER);
        t1.setFont(new Font("Comic Sans MS", Font.BOLD, 30)); t1.setForeground(PURPLE);
        t1.setAlignmentX(CENTER_ALIGNMENT);
        JLabel t2 = new JLabel("All Registered Students \u2600\uFE0F", SwingConstants.CENTER);
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t2.setForeground(new Color(130, 110, 160)); t2.setAlignmentX(CENTER_ALIGNMENT);
        p.add(t1); p.add(Box.createVerticalStrut(3)); p.add(t2);
        return p;
    }

    private void filterTable(String q) {
        model.setRowCount(0);
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(q.toLowerCase()) || String.valueOf(s.getId()).contains(q)) {
                model.addRow(new Object[]{s.getId(), s.getName(), s.getMarks()+"/100",
                    String.format("%.0f%%", s.getPercentage()), s.getGrade(), s.getLetterGrade()});
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Select one student!","Info",JOptionPane.INFORMATION_MESSAGE); return; }
        int id = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);
        if (JOptionPane.showConfirmDialog(this, name + " Delete?","Delete",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            students.removeIf(s -> s.getId() == id);
            for (int i = 0; i < students.size(); i++) students.get(i).setId(i+1);
            refresh();
        }
    }

    public void refresh() {
        if (model == null) return;
        model.setRowCount(0);
        for (Student s : students)
            model.addRow(new Object[]{s.getId(), s.getName(), s.getMarks()+"/100",
                String.format("%.0f%%", s.getPercentage()), s.getGrade(), s.getLetterGrade()});
        repaint();
    }

    private JButton pill(String text, Color color) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20); g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE); b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setPreferredSize(new Dimension(100, 30));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel makeWave() {
        JPanel w = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int ww = getWidth(), h = getHeight();
                g2.setPaint(new GradientPaint(0,0,new Color(170,80,230),ww,h,new Color(255,140,80)));
                int n = ww+3; int[] xs = new int[n]; int[] ys = new int[n];
                for (int x = 0; x <= ww; x++) { xs[x]=x; ys[x]=(int)(Math.sin(x*Math.PI*2.0/ww)*12+18); }
                xs[ww+1]=ww; ys[ww+1]=h; xs[ww+2]=0; ys[ww+2]=h;
                g2.fillPolygon(xs, ys, n);
            }
        };
        w.setOpaque(false); w.setPreferredSize(new Dimension(0, 50)); return w;
    }
}

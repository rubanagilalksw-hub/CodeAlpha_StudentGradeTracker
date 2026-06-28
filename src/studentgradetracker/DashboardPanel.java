package studentgradetracker;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class DashboardPanel extends JPanel {

    private ArrayList<Student> students;
    private MainFrame frame;
    private DefaultTableModel model;
    private JLabel lTotal, lAvg, lHigh, lLow;

    private static final Color PURPLE = MainFrame.PURPLE;
    private static final Color GREEN  = new Color(52, 199, 110);
    private static final Color BLUE   = new Color(52, 130, 246);
    private static final Color ORANGE = new Color(255, 149, 0);

    public DashboardPanel(ArrayList<Student> students, MainFrame frame) {
        this.students = students;
        this.frame = frame;
        setOpaque(false);
        setLayout(new BorderLayout(0,0));
        build();
    }

    private void build() {
        // ── Top title bar ────────────────────────────
        add(makeTitle(), BorderLayout.NORTH);

        // ── Center: table + stats side ────────────────
        JPanel center = new JPanel(new BorderLayout(12,0));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(6,14,0,14));

        center.add(makeTableCard(), BorderLayout.CENTER);
        center.add(makeStatsPanel(), BorderLayout.EAST);
        add(center, BorderLayout.CENTER);

        // ── Bottom wave ────────────────────────────────
        add(makeWave(), BorderLayout.SOUTH);
    }

    // ── Title ─────────────────────────────────────────
    private JPanel makeTitle() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(14,0,6,0));

        JLabel t1 = new JLabel("Student Grade Tracker", SwingConstants.CENTER);
        t1.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        t1.setForeground(PURPLE);
        t1.setAlignmentX(CENTER_ALIGNMENT);

        JLabel t2 = new JLabel("Manage, Track & Achieve Excellence! \u2600\uFE0F", SwingConstants.CENTER);
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t2.setForeground(new Color(130,110,160));
        t2.setAlignmentX(CENTER_ALIGNMENT);

        p.add(t1); p.add(Box.createVerticalStrut(3)); p.add(t2);
        return p;
    }

    // ── Table card ────────────────────────────────────
    private JPanel makeTableCard() {
        RoundedPanel card = new RoundedPanel(Color.WHITE, 18);
        card.setLayout(new BorderLayout(0,8));
        card.setBorder(BorderFactory.createEmptyBorder(12,14,14,14));

        // header row
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);

        JLabel lbl = new JLabel("\uD83D\uDC65  STUDENT RECORDS");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(PURPLE);
        hdr.add(lbl, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT,6,0));
        btns.setOpaque(false);
        JButton bExp  = pill("⬇ Export",  GREEN);
        JButton bClear= pill("\uD83D\uDDD1 Clear All", new Color(255,69,90));
        bExp.addActionListener(e -> exportCSV());
        bClear.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this,"Delete All?","Clear",
               JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                students.clear(); refresh();
            }
        });
        btns.add(bExp); btns.add(bClear);
        hdr.add(btns, BorderLayout.EAST);
        card.add(hdr, BorderLayout.NORTH);

        // table
        model = new DefaultTableModel(new String[]{"ID","Name","Grade","Percentage","Letter Grade"},0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        JTable tbl = new JTable(model){
            @Override public Component prepareRenderer(TableCellRenderer r,int row,int col){
                Component c = super.prepareRenderer(r,row,col);
                if(!isRowSelected(row)) c.setBackground(row%2==0?Color.WHITE:new Color(250,247,255));
                return c;
            }
        };
        tbl.setFont(new Font("Segoe UI",Font.PLAIN,13));
        tbl.setRowHeight(34);
        tbl.setShowGrid(false);
        tbl.setIntercellSpacing(new Dimension(0,0));
        tbl.setSelectionBackground(new Color(210,190,255));

        // header style
        JTableHeader th = tbl.getTableHeader();
        th.setDefaultRenderer(new DefaultTableCellRenderer(){
            { setOpaque(true); }
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
                super.getTableCellRendererComponent(t,v,s,f,r,c);
                setBackground(PURPLE); setForeground(Color.WHITE);
                setFont(new Font("Segoe UI",Font.BOLD,13));
                setHorizontalAlignment(CENTER);
                setBorder(BorderFactory.createEmptyBorder(0,6,0,6));
                return this;
            }
        });
        th.setPreferredSize(new Dimension(0,36));
        th.setResizingAllowed(false); th.setReorderingAllowed(false);

        // badge renderer for letter grade col
        tbl.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int row,int col){
                JPanel p = new JPanel(new GridBagLayout());
                p.setBackground(row%2==0?Color.WHITE:new Color(250,247,255));
                if(sel) p.setBackground(new Color(210,190,255));
                String g = v==null?"":v.toString();
                JLabel badge = new JLabel(g,SwingConstants.CENTER){
                    @Override protected void paintComponent(Graphics g2d){
                        Graphics2D g2 = (Graphics2D)g2d.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(badgeColor(g));
                        g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                        g2.dispose();
                        super.paintComponent(g2d);
                    }
                };
                badge.setForeground(Color.WHITE);
                badge.setFont(new Font("Segoe UI",Font.BOLD,12));
                badge.setOpaque(false);
                badge.setPreferredSize(new Dimension(36,24));
                p.add(badge);
                return p;
            }
        });

        // center all columns
        DefaultTableCellRenderer center2 = new DefaultTableCellRenderer();
        center2.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=0;i<4;i++) tbl.getColumnModel().getColumn(i).setCellRenderer(center2);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(40);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(70);
        tbl.getColumnModel().getColumn(3).setPreferredWidth(90);
        tbl.getColumnModel().getColumn(4).setPreferredWidth(90);

        JScrollPane sp = new JScrollPane(tbl);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        card.add(sp, BorderLayout.CENTER);

        return card;
    }

    private Color badgeColor(String g){
        switch(g){
            case "A": return new Color(52,199,110);
            case "B": return new Color(52,130,246);
            case "C": return new Color(255,193,7);
            case "D": return new Color(255,149,0);
            default:  return new Color(255,69,58);
        }
    }

    // ── Stats sidebar ─────────────────────────────────
    private JPanel makeStatsPanel() {
        RoundedPanel p = new RoundedPanel(Color.WHITE,18);
        p.setPreferredSize(new Dimension(195,0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(12,10,12,10));

        JLabel title = new JLabel("\uD83D\uDCC8  STATISTICS");
        title.setFont(new Font("Segoe UI",Font.BOLD,13));
        title.setForeground(PURPLE);
        title.setAlignmentX(CENTER_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(10));

        lTotal = new JLabel("0"); lAvg = new JLabel("0%");
        lHigh  = new JLabel("0%"); lLow = new JLabel("0%");

        p.add(statCard(lTotal,"Total Students",  new Color(240,232,255), PURPLE,        "\uD83D\uDC65"));
        p.add(Box.createVerticalStrut(7));
        p.add(statCard(lAvg,  "Average Percentage",new Color(230,252,240),GREEN,         "\uD83D\uDCC8"));
        p.add(Box.createVerticalStrut(7));
        p.add(statCard(lHigh, "Highest Score",   new Color(229,242,255), BLUE,          "\uD83C\uDFC6"));
        p.add(Box.createVerticalStrut(7));
        p.add(statCard(lLow,  "Lowest Score",    new Color(255,243,224), ORANGE,        "\uD83D\uDCC9"));
        p.add(Box.createVerticalGlue());

        JLabel keep = new JLabel("<html><center>Keep up the<br><b>great work!</b> \uD83C\uDFC6</center></html>",SwingConstants.CENTER);
        keep.setFont(new Font("Segoe UI",Font.PLAIN,12));
        keep.setForeground(new Color(100,60,160));
        keep.setAlignmentX(CENTER_ALIGNMENT);
        p.add(keep);

        updateStats();
        return p;
    }

    private JPanel statCard(JLabel val, String sub, Color bg, Color fg, String icon){
        RoundedPanel c = new RoundedPanel(bg,12);
        c.setLayout(new BorderLayout(4,0));
        c.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE,62));

        JPanel txt = new JPanel(); txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt,BoxLayout.Y_AXIS));
        val.setFont(new Font("Segoe UI",Font.BOLD,20));
        val.setForeground(fg);
        JLabel sub2 = new JLabel(sub);
        sub2.setFont(new Font("Segoe UI",Font.PLAIN,10));
        sub2.setForeground(new Color(110,110,140));
        txt.add(val); txt.add(sub2);
        c.add(txt,BorderLayout.CENTER);

        JLabel ic = new JLabel(icon);
        ic.setFont(new Font("Segoe UI Emoji",Font.PLAIN,22));
        c.add(ic,BorderLayout.EAST);
        return c;
    }

    // ── Bottom wave ───────────────────────────────────
    private JPanel makeWave(){
        JPanel wave = new JPanel(){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                int w=getWidth(), h=getHeight();
                // gradient wave
                GradientPaint gp = new GradientPaint(0,0,new Color(170,80,230),w,h,new Color(255,140,80));
                g2.setPaint(gp);
                int[] xs = new int[w+2];
                int[] ys = new int[w+2];
                for(int x=0;x<=w;x++){
                    xs[x]=x;
                    ys[x]=(int)(Math.sin(x*Math.PI*2.0/w)*12+18);
                }
                xs[w+1]=w; ys[w+1]=h;
                xs[0]=0;   // already set
                int[] fullXs = new int[w+3];
                int[] fullYs = new int[w+3];
                System.arraycopy(xs,0,fullXs,0,w+2);
                fullXs[w+2]=0; fullYs[w+2]=h;
                System.arraycopy(ys,0,fullYs,0,w+2);
                g2.fillPolygon(fullXs,fullYs,w+3);
            }
        };
        wave.setOpaque(false);
        wave.setPreferredSize(new Dimension(0,50));
        return wave;
    }

    // ── Helpers ───────────────────────────────────────
    private JButton pill(String text, Color color){
        JButton b = new JButton(text){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()?color.darker():color);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,12));
        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setPreferredSize(new Dimension(110,30));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public void refresh(){
        if(model==null) return;
        model.setRowCount(0);
        for(Student s:students){
            model.addRow(new Object[]{
                s.getId(), s.getName(), s.getMarks()+"/100",
                String.format("%.0f%%",s.getPercentage()), s.getLetterGrade()
            });
        }
        updateStats();
        repaint();
    }

    private void updateStats(){
        if(lTotal==null) return;
        if(students.isEmpty()){
            lTotal.setText("0"); lAvg.setText("0%"); lHigh.setText("0%"); lLow.setText("0%");
            return;
        }
        double sum=0,hi=0,lo=100;
        for(Student s:students){
            sum+=s.getPercentage();
            if(s.getPercentage()>hi) hi=s.getPercentage();
            if(s.getPercentage()<lo) lo=s.getPercentage();
        }
        lTotal.setText(String.valueOf(students.size()));
        lAvg.setText(String.format("%.2f%%",sum/students.size()));
        lHigh.setText(String.format("%.0f%%",hi));
        lLow.setText(String.format("%.0f%%",lo));
    }

    private void exportCSV(){
        JFileChooser fc=new JFileChooser();
        fc.setSelectedFile(new File("students_export.csv"));
        if(fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
            try(PrintWriter pw=new PrintWriter(fc.getSelectedFile())){
                pw.println("ID,Name,Marks,Percentage,Grade,LetterGrade");
                for(Student s:students)
                    pw.printf("%d,%s,%d,%.0f%%,%s,%s%n",
                        s.getId(),s.getName(),s.getMarks(),s.getPercentage(),s.getGrade(),s.getLetterGrade());
                JOptionPane.showMessageDialog(this,"CSV exported!","Done",JOptionPane.INFORMATION_MESSAGE);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

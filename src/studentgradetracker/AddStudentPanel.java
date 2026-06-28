package studentgradetracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AddStudentPanel extends JPanel {

    private ArrayList<Student> students;
    private MainFrame frame;

    private JTextField tfName  = styledField("Enter student name");
    private JTextField tfMarks = styledField("0 – 100");

    private JLabel lPct   = new JLabel("--%");
    private JLabel lGrade = new JLabel("--");
    private JLabel lLetter= new JLabel("--");

    private static final Color PURPLE = MainFrame.PURPLE;

    public AddStudentPanel(ArrayList<Student> students, MainFrame frame){
        this.students=students; this.frame=frame;
        setOpaque(false);
        setLayout(new BorderLayout());
        build();
    }

    private void build(){
        add(makeTitle(), BorderLayout.NORTH);

        // ── form card centred ──────────────────────────
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(4,18,4,18));

        RoundedPanel card = new RoundedPanel(Color.WHITE,20);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(24,30,24,30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets=new Insets(8,8,8,8); g.anchor=GridBagConstraints.WEST;

        // section header
        g.gridx=0; g.gridy=0; g.gridwidth=2;
        JLabel sec=new JLabel("\uD83D\uDC65  ADD NEW STUDENT");
        sec.setFont(new Font("Segoe UI",Font.BOLD,15));
        sec.setForeground(PURPLE);
        card.add(sec,g);

        // name row
        g.gridwidth=1; g.gridy=1; g.gridx=0;
        card.add(lbl("\uD83D\uDC64  Student Name:"),g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL; g.weightx=1;
        card.add(tfName,g);

        // marks row
        g.gridy=2; g.gridx=0; g.fill=GridBagConstraints.NONE; g.weightx=0;
        card.add(lbl("\uD83D\uDCDD  Marks (out of 100):"),g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL; g.weightx=1;
        tfMarks.addKeyListener(new KeyAdapter(){
            @Override public void keyReleased(KeyEvent e){ autoCalc(); }
        });
        card.add(tfMarks,g);

        // separator
        g.gridy=3; g.gridx=0; g.gridwidth=2; g.fill=GridBagConstraints.HORIZONTAL; g.weightx=1;
        JSeparator sep=new JSeparator();
        sep.setForeground(new Color(210,200,235));
        card.add(sep,g);

        // auto-calc label
        g.gridy=4;
        JLabel al=new JLabel("\u2728  Auto-Calculated Results:");
        al.setFont(new Font("Segoe UI",Font.BOLD,13));
        al.setForeground(new Color(100,60,180));
        card.add(al,g);

        // result cards
        g.gridy=5; g.fill=GridBagConstraints.NONE; g.anchor=GridBagConstraints.CENTER;
        JPanel rp=new JPanel(new FlowLayout(FlowLayout.CENTER,14,0));
        rp.setOpaque(false);
        styleResultLabel(lPct,  new Color(52,130,246));
        styleResultLabel(lGrade,new Color(52,199,110));
        styleResultLabel(lLetter,new Color(140,60,200));
        rp.add(resultCard("Percentage", lPct,  new Color(229,242,255),new Color(52,130,246)));
        rp.add(resultCard("Grade",      lGrade, new Color(230,252,240),new Color(52,199,110)));
        rp.add(resultCard("Letter Grade",lLetter,new Color(248,232,255),new Color(140,60,200)));
        card.add(rp,g);

        // buttons
        g.gridy=6; g.gridwidth=2;
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.CENTER,16,4));
        bp.setOpaque(false);
        JButton bAdd  = actionBtn("+ Add Student", PURPLE);
        JButton bReset= actionBtn("\u21BA  Reset", new Color(255,50,100));
        bAdd.addActionListener(e->doAdd());
        bReset.addActionListener(e->reset());
        bp.add(bAdd); bp.add(bReset);
        card.add(bp,g);

        GridBagConstraints wgc=new GridBagConstraints();
        wgc.fill=GridBagConstraints.BOTH; wgc.weightx=1; wgc.weighty=1;
        wrap.add(card,wgc);
        add(wrap,BorderLayout.CENTER);
        add(makeWave(),BorderLayout.SOUTH);
    }

    private JPanel makeTitle(){
        JPanel p=new JPanel(); p.setOpaque(false);
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(14,0,6,0));
        JLabel t1=new JLabel("Student Grade Tracker",SwingConstants.CENTER);
        t1.setFont(new Font("Comic Sans MS",Font.BOLD,30)); t1.setForeground(PURPLE);
        t1.setAlignmentX(CENTER_ALIGNMENT);
        JLabel t2=new JLabel("Add New Student Below \u2600\uFE0F",SwingConstants.CENTER);
        t2.setFont(new Font("Segoe UI",Font.PLAIN,13));
        t2.setForeground(new Color(130,110,160)); t2.setAlignmentX(CENTER_ALIGNMENT);
        p.add(t1); p.add(Box.createVerticalStrut(3)); p.add(t2);
        return p;
    }

    private void autoCalc(){
        try{
            String s=tfMarks.getText().trim();
            if(s.isEmpty()){reset2();return;}
            int m=Integer.parseInt(s);
            if(m<0||m>100){lPct.setText("!");lGrade.setText("!");lLetter.setText("!");return;}
            lPct.setText(m+"%");
            lGrade.setText(Student.calcGrade(m));
            lLetter.setText(Student.calcLetter(m));
        }catch(NumberFormatException e){lPct.setText("?");lGrade.setText("?");lLetter.setText("?");}
    }

    private void reset2(){lPct.setText("--%");lGrade.setText("--");lLetter.setText("--");}

    private void doAdd(){
        String name=tfName.getText().trim();
        if(name.isEmpty()){JOptionPane.showMessageDialog(this,"Write name!","Error",JOptionPane.WARNING_MESSAGE);return;}
        int m;
        try{
            m=Integer.parseInt(tfMarks.getText().trim());
            if(m<0||m>100) throw new NumberFormatException();
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Enter marks between 0-100!","Error",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int newId=students.isEmpty()?1:students.get(students.size()-1).getId()+1;
        students.add(new Student(newId,name,m));
        JOptionPane.showMessageDialog(this,
            "\u2705 "+name+" Added!\nMarks: "+m+"\nPercentage: "+m+"%\nGrade: "+Student.calcGrade(m)+
            "  |  Letter: "+Student.calcLetter(m),"Student Added!",JOptionPane.INFORMATION_MESSAGE);
        reset();
        frame.showCard("Dashboard");
    }

    private void reset(){
        tfName.setText(""); tfMarks.setText(""); reset2();
    }

    // ── Helpers ───────────────────────────────────────
    private static JLabel lbl(String t){
        JLabel l=new JLabel(t);
        l.setFont(new Font("Segoe UI",Font.PLAIN,13));
        l.setForeground(new Color(60,50,80));
        return l;
    }

    private static JTextField styledField(String hint){
        JTextField tf=new JTextField(22){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(getText().isEmpty()){
                    Graphics2D g2=(Graphics2D)g;
                    g2.setColor(new Color(180,170,200));
                    g2.setFont(new Font("Segoe UI",Font.ITALIC,12));
                    Insets ins=getInsets();
                    g2.drawString(hint,ins.left+2,getHeight()-ins.bottom-5);
                }
            }
        };
        tf.setFont(new Font("Segoe UI",Font.PLAIN,13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,185,230),1,true),
            BorderFactory.createEmptyBorder(7,10,7,10)
        ));
        return tf;
    }

    private void styleResultLabel(JLabel l,Color c){
        l.setFont(new Font("Segoe UI",Font.BOLD,22));
        l.setForeground(c);
        l.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private JPanel resultCard(String title,JLabel val,Color bg,Color fg){
        RoundedPanel c=new RoundedPanel(bg,14);
        c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
        c.setBorder(BorderFactory.createEmptyBorder(10,18,10,18));
        c.setPreferredSize(new Dimension(130,78));
        val.setAlignmentX(CENTER_ALIGNMENT);
        JLabel t=new JLabel(title,SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI",Font.PLAIN,11));
        t.setForeground(new Color(110,100,140));
        t.setAlignmentX(CENTER_ALIGNMENT);
        c.add(val); c.add(Box.createVerticalStrut(2)); c.add(t);
        return c;
    }

    private JButton actionBtn(String text,Color color){
        JButton b=new JButton(text){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()?color.darker():color);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),26,26);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setPreferredSize(new Dimension(160,40));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel makeWave(){
        JPanel w=new JPanel(){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                int ww=getWidth(),h=getHeight();
                GradientPaint gp=new GradientPaint(0,0,new Color(170,80,230),ww,h,new Color(255,140,80));
                g2.setPaint(gp);
                int n=ww+3;
                int[] xs=new int[n]; int[] ys=new int[n];
                for(int x=0;x<=ww;x++){xs[x]=x;ys[x]=(int)(Math.sin(x*Math.PI*2.0/ww)*12+18);}
                xs[ww+1]=ww; ys[ww+1]=h; xs[ww+2]=0; ys[ww+2]=h;
                g2.fillPolygon(xs,ys,n);
            }
        };
        w.setOpaque(false);
        w.setPreferredSize(new Dimension(0,50));
        return w;
    }
}

package studentgradetracker;

import javax.swing.*;
import java.awt.*;

public class AboutPanel extends JPanel {
    private static final Color PURPLE=MainFrame.PURPLE;

    public AboutPanel(){
        setOpaque(false); setLayout(new BorderLayout()); build();
    }

    private void build(){
        add(makeTitle(),BorderLayout.NORTH);

        JPanel wrap=new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(8,40,8,40));

        RoundedPanel card=new RoundedPanel(Color.WHITE,20);
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(28,40,28,40));

        JLabel ico=new JLabel("\uD83C\uDF93",SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji",Font.PLAIN,60));
        ico.setAlignmentX(CENTER_ALIGNMENT); card.add(ico);
        card.add(Box.createVerticalStrut(12));

        JLabel name=new JLabel("Student Grade Tracker",SwingConstants.CENTER);
        name.setFont(new Font("Comic Sans MS",Font.BOLD,22));
        name.setForeground(PURPLE); name.setAlignmentX(CENTER_ALIGNMENT);
        card.add(name);
        card.add(Box.createVerticalStrut(4));

        JLabel ver=new JLabel("Version 2.0  |  Java Swing Application",SwingConstants.CENTER);
        ver.setFont(new Font("Segoe UI",Font.PLAIN,12));
        ver.setForeground(new Color(130,110,160)); ver.setAlignmentX(CENTER_ALIGNMENT);
        card.add(ver);
        card.add(Box.createVerticalStrut(18));

        JSeparator sep=new JSeparator();
        sep.setForeground(new Color(210,200,235));
        sep.setMaximumSize(new Dimension(420,1));
        sep.setAlignmentX(CENTER_ALIGNMENT);
        card.add(sep); card.add(Box.createVerticalStrut(16));

        String[] feats={
            "\u2705  Student records management",
            "\u2705  Enter marks only – grade auto-calculate",
            "\u2705  Statistics & analytics dashboard",
            "\u2705  Search & filter students",
            "\u2705  Export data to CSV",
            "\u2705  Beautiful modern UI"
        };
        for(String f:feats){
            JLabel l=new JLabel(f,SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI",Font.PLAIN,13));
            l.setForeground(new Color(60,50,80));
            l.setAlignmentX(CENTER_ALIGNMENT);
            card.add(l); card.add(Box.createVerticalStrut(5));
        }

        card.add(Box.createVerticalStrut(18));
        JLabel q=new JLabel("<html><center><i>\"Education is the most powerful weapon<br>which you can use to change the world.\"</i><br><b>– Nelson Mandela</b></center></html>",SwingConstants.CENTER);
        q.setFont(new Font("Segoe UI",Font.PLAIN,12));
        q.setForeground(new Color(100,60,160));
        q.setAlignmentX(CENTER_ALIGNMENT);
        card.add(q);

        GridBagConstraints gc=new GridBagConstraints();
        gc.fill=GridBagConstraints.BOTH; gc.weightx=1; gc.weighty=1;
        wrap.add(card,gc);
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
        JLabel t2=new JLabel("About This Application \u2600\uFE0F",SwingConstants.CENTER);
        t2.setFont(new Font("Segoe UI",Font.PLAIN,13));
        t2.setForeground(new Color(130,110,160)); t2.setAlignmentX(CENTER_ALIGNMENT);
        p.add(t1); p.add(Box.createVerticalStrut(3)); p.add(t2);
        return p;
    }

    private JPanel makeWave(){
        JPanel w=new JPanel(){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                int ww=getWidth(),h=getHeight();
                g2.setPaint(new GradientPaint(0,0,new Color(170,80,230),ww,h,new Color(255,140,80)));
                int n=ww+3;int[] xs=new int[n];int[] ys=new int[n];
                for(int x=0;x<=ww;x++){xs[x]=x;ys[x]=(int)(Math.sin(x*Math.PI*2.0/ww)*12+18);}
                xs[ww+1]=ww;ys[ww+1]=h;xs[ww+2]=0;ys[ww+2]=h;
                g2.fillPolygon(xs,ys,n);
            }
        };
        w.setOpaque(false);w.setPreferredSize(new Dimension(0,50));return w;
    }
}

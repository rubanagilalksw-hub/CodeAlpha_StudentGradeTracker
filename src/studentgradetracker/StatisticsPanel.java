package studentgradetracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class StatisticsPanel extends JPanel {
    private ArrayList<Student> students;
    private JPanel body;
    private static final Color PURPLE=MainFrame.PURPLE;

    public StatisticsPanel(ArrayList<Student> students){
        this.students=students; setOpaque(false); setLayout(new BorderLayout()); build();
    }

    private void build(){
        add(makeTitle(),BorderLayout.NORTH);
        body=new JPanel(new GridLayout(2,2,12,12));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(6,14,4,14));
        add(body,BorderLayout.CENTER);
        add(makeWave(),BorderLayout.SOUTH);
        refresh();
    }

    private JPanel makeTitle(){
        JPanel p=new JPanel(); p.setOpaque(false);
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(14,0,6,0));
        JLabel t1=new JLabel("Student Grade Tracker",SwingConstants.CENTER);
        t1.setFont(new Font("Comic Sans MS",Font.BOLD,30)); t1.setForeground(PURPLE);
        t1.setAlignmentX(CENTER_ALIGNMENT);
        JLabel t2=new JLabel("Statistics & Analytics \u2600\uFE0F",SwingConstants.CENTER);
        t2.setFont(new Font("Segoe UI",Font.PLAIN,13));
        t2.setForeground(new Color(130,110,160)); t2.setAlignmentX(CENTER_ALIGNMENT);
        p.add(t1); p.add(Box.createVerticalStrut(3)); p.add(t2);
        return p;
    }

    public void refresh(){
        body.removeAll();
        body.add(summaryCard());
        body.add(barChartCard());
        body.add(gradeDistCard());
        body.add(topStudentsCard());
        body.revalidate(); body.repaint();
    }

    private JPanel summaryCard(){
        RoundedPanel c=card("\uD83D\uDCCB Summary");
        if(students.isEmpty()){c.add(noData());return c;}
        double sum=0,hi=0,lo=100;
        for(Student s:students){sum+=s.getPercentage();hi=Math.max(hi,s.getPercentage());lo=Math.min(lo,s.getPercentage());}
        double avg=sum/students.size();
        long pass=students.stream().filter(s->s.getMarks()>=50).count();

        String[][] rows={
            {"Total Students",String.valueOf(students.size())},
            {"Average Marks",String.format("%.1f%%",avg)},
            {"Highest Score",String.format("%.0f%%",hi)},
            {"Lowest Score",String.format("%.0f%%",lo)},
            {"Pass Rate",String.format("%.0f%%",(pass*100.0)/students.size())}
        };
        Color[] colors={PURPLE,new Color(52,199,110),new Color(52,130,246),new Color(255,149,0),new Color(255,69,90)};
        for(int i=0;i<rows.length;i++){
            JPanel row=new JPanel(new BorderLayout());row.setOpaque(false);
            row.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
            JLabel k=new JLabel(rows[i][0]); k.setFont(new Font("Segoe UI",Font.PLAIN,12));
            JLabel v=new JLabel(rows[i][1]); v.setFont(new Font("Segoe UI",Font.BOLD,14));
            v.setForeground(colors[i]); v.setHorizontalAlignment(SwingConstants.RIGHT);
            row.add(k,BorderLayout.WEST); row.add(v,BorderLayout.EAST);
            c.add(row);
        }
        return c;
    }

    private JPanel barChartCard(){
        RoundedPanel outer=card("\uD83D\uDCCA Marks Bar Chart");
        JPanel chart=new JPanel(){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(students.isEmpty()){g.setColor(Color.GRAY);g.drawString("No data",getWidth()/2-25,getHeight()/2);return;}
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                int w=getWidth(),h=getHeight(),pad=28,bpad=6;
                int bw=Math.max(12,(w-pad*2)/students.size()-bpad);
                Color[] bc={new Color(120,60,210),new Color(52,199,110),new Color(255,193,7),
                            new Color(52,130,246),new Color(255,149,0),new Color(255,69,90)};
                g2.setColor(new Color(220,210,240));
                g2.drawLine(pad,h-pad,w-pad,h-pad);
                g2.drawLine(pad,20,pad,h-pad);
                for(int i=0;i<students.size();i++){
                    Student s=students.get(i);
                    int bh=(int)((s.getMarks()/100.0)*(h-pad-24));
                    int x=pad+i*(bw+bpad)+bpad, y=h-pad-bh;
                    g2.setColor(bc[i%bc.length]);
                    g2.fillRoundRect(x,y,bw,bh,6,6);
                    g2.setColor(new Color(60,40,80));
                    g2.setFont(new Font("Segoe UI",Font.BOLD,10));
                    g2.drawString(s.getMarks()+"",x+bw/2-8,y-3);
                    String nm=s.getName().split(" ")[0];
                    if(nm.length()>5)nm=nm.substring(0,5);
                    g2.setFont(new Font("Segoe UI",Font.PLAIN,9));
                    g2.drawString(nm,x,h-pad+12);
                }
            }
        };
        chart.setOpaque(false);
        outer.setLayout(new BorderLayout());
        outer.add(chart,BorderLayout.CENTER);
        return outer;
    }

    private JPanel gradeDistCard(){
        RoundedPanel c=card("\uD83C\uDFC6 Grade Distribution");
        Map<String,Integer> m=new LinkedHashMap<>();
        m.put("A",0);m.put("B",0);m.put("C",0);m.put("D",0);m.put("F",0);
        for(Student s:students) m.merge(s.getGrade(),1,Integer::sum);
        Color[] cols={new Color(52,199,110),new Color(100,160,255),new Color(52,130,246),
                      new Color(255,193,7),new Color(255,149,0),new Color(255,69,58)};
        int i=0,total=Math.max(1,students.size());
        for(Map.Entry<String,Integer> e:m.entrySet()){
            int cnt=e.getValue();
            final Color col=cols[i%cols.length];
            JPanel row=new JPanel(new BorderLayout(6,0));row.setOpaque(false);
            row.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
            JLabel gl=new JLabel(e.getKey());gl.setFont(new Font("Segoe UI",Font.BOLD,13));
            gl.setForeground(col);gl.setPreferredSize(new Dimension(28,20));
            JPanel bar=new JPanel(){
                @Override protected void paintComponent(Graphics g){
                    super.paintComponent(g);
                    Graphics2D g2=(Graphics2D)g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(230,225,245));g2.fillRoundRect(0,4,getWidth(),getHeight()-8,8,8);
                    int bw=(int)((cnt*1.0/total)*getWidth());
                    if(bw>0){g2.setColor(col);g2.fillRoundRect(0,4,bw,getHeight()-8,8,8);}
                }
            };
            bar.setOpaque(false);
            JLabel cl=new JLabel(String.valueOf(cnt));cl.setFont(new Font("Segoe UI",Font.BOLD,12));
            cl.setForeground(col);cl.setPreferredSize(new Dimension(18,20));cl.setHorizontalAlignment(SwingConstants.RIGHT);
            row.add(gl,BorderLayout.WEST);row.add(bar,BorderLayout.CENTER);row.add(cl,BorderLayout.EAST);
            c.add(row);i++;
        }
        return c;
    }

    private JPanel topStudentsCard(){
        RoundedPanel c=card("\u2B50 Top Students");
        ArrayList<Student> sorted=new ArrayList<>(students);
        sorted.sort((a,b)->Integer.compare(b.getMarks(),a.getMarks()));
        String[] medals={"\uD83E\uDD47","\uD83E\uDD48","\uD83E\uDD49","4.","5."};
        Color[] cols={new Color(255,180,0),new Color(160,160,180),new Color(180,100,40),PURPLE,new Color(52,130,246)};
        int lim=Math.min(5,sorted.size());
        for(int i=0;i<lim;i++){
            Student s=sorted.get(i);
            JPanel row=new JPanel(new BorderLayout(6,0));row.setOpaque(false);
            row.setBorder(BorderFactory.createEmptyBorder(6,4,6,4));
            JLabel rank=new JLabel(medals[i]);rank.setFont(new Font("Segoe UI Emoji",Font.PLAIN,18));
            rank.setPreferredSize(new Dimension(28,20));
            JLabel name=new JLabel(s.getName());name.setFont(new Font("Segoe UI",Font.PLAIN,12));
            JLabel score=new JLabel(s.getMarks()+"%");
            score.setFont(new Font("Segoe UI",Font.BOLD,13));score.setForeground(cols[i]);
            row.add(rank,BorderLayout.WEST);row.add(name,BorderLayout.CENTER);row.add(score,BorderLayout.EAST);
            c.add(row);
        }
        if(students.isEmpty()) c.add(noData());
        return c;
    }

    private RoundedPanel card(String title){
        RoundedPanel p=new RoundedPanel(Color.WHITE,16);
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
        JLabel t=new JLabel(title);
        t.setFont(new Font("Segoe UI",Font.BOLD,13));t.setForeground(PURPLE);t.setAlignmentX(LEFT_ALIGNMENT);
        p.add(t);
        JSeparator sep=new JSeparator();sep.setForeground(new Color(210,200,235));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
        p.add(Box.createVerticalStrut(6)); p.add(sep); p.add(Box.createVerticalStrut(4));
        return p;
    }

    private JLabel noData(){JLabel l=new JLabel("No data yet.");l.setFont(new Font("Segoe UI",Font.ITALIC,12));l.setForeground(Color.GRAY);return l;}

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

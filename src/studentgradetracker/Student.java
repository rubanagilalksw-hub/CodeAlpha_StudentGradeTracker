package studentgradetracker;

public class Student {
    private int id;
    private String name;
    private int marks;
    private double percentage;
    private String grade;
    private String letterGrade;

    public Student(int id, String name, int marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
        this.percentage = marks;
        this.grade = calcGrade(marks);
        this.letterGrade = calcLetter(marks);
    }

    // 15 mark difference per grade: 85-100=A, 70-84=B, 55-69=C, 40-54=D, 0-39=F
    public static String calcGrade(int m) {
        if (m >= 85) return "A";
        else if (m >= 70) return "B";
        else if (m >= 55) return "C";
        else if (m >= 40) return "D";
        else return "F";
    }

    public static String calcLetter(int m) {
        if (m >= 85) return "A";
        else if (m >= 70) return "B";
        else if (m >= 55) return "C";
        else if (m >= 40) return "D";
        else return "F";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public int getMarks() { return marks; }
    public double getPercentage() { return percentage; }
    public String getGrade() { return grade; }
    public String getLetterGrade() { return letterGrade; }
}

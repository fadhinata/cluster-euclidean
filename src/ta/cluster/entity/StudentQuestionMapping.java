package ta.cluster.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name= StudentQuestionMapping.ENTITY_NAME)
@Table(name = StudentQuestionMapping.TABLE_NAME)
public class StudentQuestionMapping implements Serializable {

    public static final String TABLE_NAME = "t_student_question_mapping";
    public static final String ENTITY_NAME = "ta.cluster.entity.StudentQuestionMapping";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_question_mapping_id")
    private long id;
    @Column(name = "student_id")
    private long studentId;
    @Column(name = "question_number")
    private int questionNumber;
    @Column(name = "score")
    private double score;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
    
}

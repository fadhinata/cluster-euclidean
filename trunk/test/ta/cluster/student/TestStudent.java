/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.student;

import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ta.cluster.dao.StudentDao;
import ta.cluster.entity.Student;

/**
 *
 * @author Matt
 */
public class TestStudent {
    private final Logger log = Logger.getLogger(TestStudent.class);
    private StudentDao dao;
    
    @Before
    public void setUp() {
        dao = new StudentDao();
    }
    
    @Test
    public void findAllStudent() {
        log.debug("Find all student ...");
        List<Student> studentList = dao.findAll();
        log.debug("Student list size: " + studentList.size());
    }
    
    @Test
    public void findStudent() {
        log.debug("Find student ...");
        Long id = 1L;
        Student student = dao.findById(id);
        log.debug("Student: " + student.getName());
    }
    
    @Test
    public void addStudentSuccess() {
        log.debug("Add student (success) ...");
        Student student = new Student();
        student.setName("Soda");
        
        try {
            dao.beginTransaction();
            dao.insert(student);
            dao.commitTransaction();
        } catch (Exception e) {
            log.error("Add student success", e);
            dao.rollbackTransaction();
        }
    }
    
    @Test
    public void editStudentSuccess() {
        log.debug("Edit student (success) ...");
        Student student = new Student();
        student.setId(1);
        student.setName("Edi");
        
        try {
            dao.beginTransaction();
            dao.update(student, student.getId());
            dao.commitTransaction();
        } catch (Exception e) {
            log.error("Edit student failed", e);
            dao.rollbackTransaction();
        }
    }
    
    @Test
    public void editStudentFail() {
        log.debug("Edit student (fail) ...");
        Student student = new Student();
        student.setId(3);
        student.setName("Edi");
        
        try {
            dao.beginTransaction();
            dao.update(student, student.getId());
            dao.commitTransaction();
        } catch (Exception e) {
            log.error("Edit student failed", e);
            dao.rollbackTransaction();
        }
    }
    
}

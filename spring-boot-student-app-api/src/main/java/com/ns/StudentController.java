package com.ns;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@RestController
public class StudentController {
    private final List<Student> students = new ArrayList<>();

    public StudentController() {
        students.add(new Student("Nguyen", "A"));
        students.add(new Student( "Tran", "B"));
        students.add(new Student("Le", "C"));
        students.add(new Student("Pham", "D"));
        students.add(new Student("Hoang", "E"));
        students.add(new Student("Vu", "F"));
        students.add(new Student("Dang", "G"));
        students.add(new Student("Do", "H"));
    }

    @GetMapping("/")
    public String hello(HttpServletRequest request) {
        String hostname = System.getenv("HOSTNAME");

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr(); // Lấy IP thực từ request
        }

        String podName = System.getenv("POD_NAME");

        return "Hello from " + hostname + " with IP " + ipAddress + " and pod name " + podName;
    }

    @GetMapping("/crash")
    public String crash() {
        System.exit(1);
        return "Crashed";
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents(@RequestParam(required = false) String firstName) {
        try {
            List<Student> result = new ArrayList<>();

            if (firstName == null) {
                result.addAll(students);
            } else {
                for (Student student : students) {
                    if (student.getFirstName().contains(firstName)) {
                        result.add(student);
                    }
                }
            }

            if (result.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") String id) {
        Optional<Student> studentData = students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst();

        if (studentData.isPresent()) {
            return new ResponseEntity<>(studentData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            // Tạo ID giả lập
            student.setId(String.valueOf(students.size() + 1));
            students.add(student);
            return new ResponseEntity<>(student, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") String id, @RequestBody Student student) {
        Optional<Student> studentData = students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();

        if (studentData.isPresent()) {
            Student _student = studentData.get();
            _student.setFirstName(student.getFirstName());
            _student.setLastName(student.getLastName());
            return new ResponseEntity<>(_student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") String id) {
        try {
            students.removeIf(student -> student.getId().equals(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/students")
    public ResponseEntity<HttpStatus> deleteAllStudents() {
        try {
            students.clear();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

}

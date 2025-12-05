package BYOL;


import java.util.List;

abstract class Stmt {
  interface Visitor<R> {
    R visitStudentStmt(Student student);
    R visitCourseStmt(Course course);   
    R visitMajorStmt(Major major);
    R visitSemesterStmt(Semester semester); 
    R visitScheduleStmt(Schedule schedule);
    R visitPrintStmt(Print stmt);
    R visitFunctionCallStmt(FunctionCall call);
  }

  abstract <R> R accept(Visitor<R> visitor);

    static class Student extends Stmt {
        final String id;
        final String name;
        final String major;
        final int credits;
        final List<String> completedCourses;
    
        Student(String id, String name, String major, int credits, List<String> completedCourses) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.credits = credits;
        this.completedCourses = completedCourses;
        }
    
        @Override
        <R> R accept(Visitor<R> visitor) {
        return visitor.visitStudentStmt(this);
        }
    }

    static class Course extends Stmt {
        final String id;
        final String title;
        final int credits;
        final List<String> prerequisites;
    
        Course(String id, String title, int credits, List<String> prerequisites) {
        this.id = id;
        this.title = title;
        this.credits = credits;
        this.prerequisites = prerequisites;
        }
    
        @Override
        <R> R accept(Visitor<R> visitor) {
        return visitor.visitCourseStmt(this);
        }
    }

    static class Major extends Stmt {
        final String id;
        final List<String> requiredCourses;
    
        Major(String id, List<String> requiredCourses) {
        this.id = id;
        this.requiredCourses = requiredCourses;
        }
    
        @Override
        <R> R accept(Visitor<R> visitor) {
        return visitor.visitMajorStmt(this);
        }
    }

    static class Semester extends Stmt {
        final String id;
        final int year;
        final String term;
    
        Semester(String id, int year, String term) {
        this.id = id;
        this.year = year;
        this.term = term;
        }
    
        @Override
        <R> R accept(Visitor<R> visitor) {
        return visitor.visitSemesterStmt(this);
        }
    }

    static class Schedule extends Stmt {
        final String id;
        final String student;
        final String semester;
        final List<String> courses;
    
        Schedule(String id, String student, String semester, List<String> courses) {
        this.id = id;
        this.student = student;
        this.semester = semester;
        this.courses = courses;
        }
    
        @Override
        <R> R accept(Visitor<R> visitor) {
        return visitor.visitScheduleStmt(this);
        }
    }

    static class Print extends Stmt {
        final String identifier;
    
        Print(String identifier) {
        this.identifier = identifier;
        }
    
        @Override
        <R> R accept(Visitor<R> visitor) {
        return visitor.visitPrintStmt(this);
        }
    }

    static class FunctionCall extends Stmt {
        final String functionName;
        final List<Object> arguments;
    
        FunctionCall(String functionName, List<Object> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
        }
    
        @Override
        <R> R accept(Visitor<R> visitor) {
        return visitor.visitFunctionCallStmt(this);
        }
    }

}

  
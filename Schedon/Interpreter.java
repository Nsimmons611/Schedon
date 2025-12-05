package BYOL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements Stmt.Visitor<Void> {

    private final Map<String, Object> environment = new HashMap<>();

    public void interpret(List<Stmt> statements) {
        for (Stmt statement : statements) {
            execute(statement);
        }
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }
    
    @Override
    public Void visitStudentStmt(Stmt.Student student) {
        Map<String, Object> studentInfo = new HashMap<>();
        studentInfo.put("type", "student");
        studentInfo.put("name", student.name);
        studentInfo.put("major", student.major);
        studentInfo.put("credits", student.credits);
        studentInfo.put("completedCourses", student.completedCourses);
        environment.put(student.id, studentInfo);
        System.out.println("Student added: " + student.name + " with major " + student.major + " (" + student.credits + " credits)");
        return null;
    }

    @Override
    public Void visitCourseStmt(Stmt.Course course) {
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("type", "course");
        courseInfo.put("title", course.title);
        courseInfo.put("credits", course.credits);
        courseInfo.put("prerequisites", course.prerequisites);
        
        environment.put(course.id, courseInfo);
        System.out.println("Course added: " + course.title + " with " + course.credits + " credits");
        return null;
    }

    @Override
    public Void visitMajorStmt(Stmt.Major major) {
        Map<String, Object> majorInfo = new HashMap<>();
        majorInfo.put("type", "major");
        majorInfo.put("requiredCourses", major.requiredCourses);
        
        environment.put(major.id, majorInfo);
        System.out.println("Major added: " + major.id + " with " + major.requiredCourses + " required courses");
        return null;
    }

    @Override
    public Void visitSemesterStmt(Stmt.Semester semester) {
        Map<String, Object> semesterInfo = new HashMap<>();
        semesterInfo.put("type", "semester");
        semesterInfo.put("year", semester.year);
        semesterInfo.put("term", semester.term);
        
        environment.put(semester.id, semesterInfo);
        System.out.println("Semester added: " + semester.id + " for year " + semester.year + " term " + semester.term);
        return null;
    }       

    @Override
    public Void visitScheduleStmt(Stmt.Schedule schedule) { 
        Map<String, Object> scheduleInfo = new HashMap<>();
        scheduleInfo.put("type", "schedule");
        scheduleInfo.put("student", schedule.student);
        scheduleInfo.put("semester", schedule.semester);
        scheduleInfo.put("courses", schedule.courses);
        
        environment.put(schedule.id, scheduleInfo);
        System.out.println("Schedule added: " + schedule.id + " for student " + schedule.student + " in semester " + schedule.semester + " with courses " + schedule.courses);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = environment.get(stmt.identifier);
        if (value != null) {
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> m = (Map<String, Object>) value;
                Object type = m.get("type");
                if ("student".equals(type)) {
                    String name = (String) m.get("name");
                    String major = (String) m.get("major");
                    Object credits = m.get("credits");
                    Object completedCourses = m.get("completedCourses");
                    System.out.println("student " + stmt.identifier + " { name = \"" + name + "\"; major = " + major + "; credits = " + credits + "; completedCourses = " + completedCourses + "; }");
                } else if ("course".equals(type)) {
                    String title = (String) m.get("title");
                    Object credits = m.get("credits");
                    Object prerequisites = m.get("prerequisites");
                    System.out.println("course " + stmt.identifier + " { title = \"" + title + "\"; credits = " + credits + "; prerequisites = " + prerequisites + "; }");
                } else if ("major".equals(type)) {
                    Object req = m.get("requiredCourses");
                    System.out.println("major " + stmt.identifier + " { requiredCourses = " + req + "; }");
                } else if ("semester".equals(type)) {
                    Object year = m.get("year");
                    Object term = m.get("term");
                    System.out.println("semester " + stmt.identifier + " { year = " + year + "; term = " + term + "; }");
                } else if ("schedule".equals(type)) {
                    Object student = m.get("student");
                    Object semester = m.get("semester");
                    Object courses = m.get("courses");
                    System.out.println("schedule " + stmt.identifier + " { student = " + student + "; semester = " + semester + "; courses = " + courses + "; }");
                } else {
                    System.out.println("Print: " + value);
                }
            } else {
                System.out.println("Print: " + value);
            }
        } else {
            System.out.println("Print: Undefined identifier " + stmt.identifier);
        }
        return null;
    }

    private int nextId = 1;

    @Override
    public Void visitFunctionCallStmt(Stmt.FunctionCall call) {
        switch (call.functionName) {
            case "AddStudent":
                if (call.arguments.size() != 4) {
                    System.err.println("AddStudent requires 4 arguments: (String name, String major, int credits, [completedCourses])");
                    return null;
                }
                String studentName = (String) call.arguments.get(0);
                String majorId = (String) call.arguments.get(1);
                int studentCredits = ((Double) call.arguments.get(2)).intValue();
                @SuppressWarnings("unchecked")
                List<String> studentCompletedCourses = (List<String>) call.arguments.get(3);
                String studentId = studentName.substring(0, 1) + studentName.substring(studentName.indexOf(" ") + 1, studentName.indexOf(" ") + 2) + nextId++;

                Map<String, Object> studentInfo = new HashMap<>();
                studentInfo.put("type", "student");
                studentInfo.put("name", studentName);
                studentInfo.put("major", majorId);
                studentInfo.put("credits", studentCredits);
                studentInfo.put("completedCourses", studentCompletedCourses);
                environment.put(studentId, studentInfo);
                System.out.println("Student added with ID: " + studentId);
                break;
                
            case "AddCourse":
                if (call.arguments.size() != 3) {
                    System.err.println("AddCourse requires 3 arguments: (String title, int credits, [prerequisites])");
                    return null;
                }
                String courseTitle = (String) call.arguments.get(0);
                int credits = ((Double) call.arguments.get(1)).intValue();
                @SuppressWarnings("unchecked")
                List<String> prerequisites = (List<String>) call.arguments.get(2);
                String courseId = courseTitle.substring(0, 1) + courseTitle.substring(courseTitle.indexOf("-") + 1, courseTitle.indexOf("-") + 3).toUpperCase() + courseTitle.substring(courseTitle.indexOf(" ") + 1);
                
                Map<String, Object> courseInfo = new HashMap<>();
                courseInfo.put("type", "course");
                courseInfo.put("title", courseTitle);
                courseInfo.put("credits", credits);
                courseInfo.put("prerequisites", prerequisites);
                environment.put(courseId, courseInfo);
                System.out.println("Course added with ID: " + courseId);
                break;
                
            case "AddMajor":
                if (call.arguments.size() != 2) {
                    System.err.println("AddMajor requires 2 arguments: (String majorName, [course1, course2, ...])");
                    return null;
                }
                String majorName = (String) call.arguments.get(0);
                @SuppressWarnings("unchecked")
                List<String> requiredCourses = (List<String>) call.arguments.get(1);
                String majorIdGen = majorName.replaceAll("\\s+", "-");
                
                Map<String, Object> majorInfo = new HashMap<>();
                majorInfo.put("type", "major");
                majorInfo.put("name", majorName);
                majorInfo.put("requiredCourses", requiredCourses);
                environment.put(majorIdGen, majorInfo);
                System.out.println("Major added with ID: " + majorIdGen);
                break;
                
            case "GenerateSchedule":
                if (call.arguments.size() < 2 || call.arguments.size() > 3) {
                    System.err.println("GenerateSchedule requires 2 or 3 arguments: (String studentId, String semesterId, [optionalDesiredCourses])");
                    return null;
                }
                String schedStudentId = (String) call.arguments.get(0);
                String schedSemesterId = (String) call.arguments.get(1);
                
                // Get optional desired courses list
                List<String> desiredCourses = new java.util.ArrayList<>();
                if (call.arguments.size() == 3) {
                    @SuppressWarnings("unchecked")
                    List<String> desiredCoursesArg = (List<String>) call.arguments.get(2);
                    desiredCourses = desiredCoursesArg;
                }
                
                // Get student info
                Object studentObj = environment.get(schedStudentId);
                if (studentObj == null || !(studentObj instanceof Map)) {
                    System.err.println("Student not found: " + schedStudentId);
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> student = (Map<String, Object>) studentObj;
                String studentMajor = (String) student.get("major");
                
                // Get major info
                Object majorObj = environment.get(studentMajor);
                if (majorObj == null || !(majorObj instanceof Map)) {
                    System.err.println("Major not found: " + studentMajor);
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> major = (Map<String, Object>) majorObj;
                @SuppressWarnings("unchecked")
                List<String> majorCourses = (List<String>) major.get("requiredCourses");
                
                // Verify semester exists
                Object semesterObj = environment.get(schedSemesterId);
                if (semesterObj == null || !(semesterObj instanceof Map)) {
                    System.err.println("Semester not found: " + schedSemesterId);
                    return null;
                }
                
                // Get student's completed courses
                @SuppressWarnings("unchecked")
                List<String> schedStudentCompletedCourses = (List<String>) student.get("completedCourses");
                
                // Step 0: Add desired courses first if they meet requirements
                List<String> selectedCourses = new java.util.ArrayList<>();
                int totalCredits = 0;
                
                for (String desiredCourseId : desiredCourses) {
                    Object courseObj = environment.get(desiredCourseId);
                    if (courseObj != null && courseObj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> course = (Map<String, Object>) courseObj;
                        int courseCredits = (int) course.get("credits");
                        @SuppressWarnings("unchecked")
                        List<String> coursePrereqs = (List<String>) course.get("prerequisites");
                        
                        // Check if student has already completed this course
                        if (schedStudentCompletedCourses.contains(desiredCourseId)) {
                            System.out.println("Warning: Desired course " + desiredCourseId + " already completed, skipping");
                            continue;
                        }
                        
                        // Check if prerequisites are met
                        boolean prereqsMet = true;
                        if (coursePrereqs != null && !coursePrereqs.isEmpty()) {
                            for (String prereq : coursePrereqs) {
                                if (!schedStudentCompletedCourses.contains(prereq)) {
                                    prereqsMet = false;
                                    System.out.println("Warning: Desired course " + desiredCourseId + " prerequisites not met, skipping");
                                    break;
                                }
                            }
                        }
                        
                        // Only add if prerequisites are met and within credit limit
                        if (prereqsMet && totalCredits + courseCredits <= 18) {
                            selectedCourses.add(desiredCourseId);
                            totalCredits += courseCredits;
                        } else if (prereqsMet && totalCredits + courseCredits > 18) {
                            System.out.println("Warning: Desired course " + desiredCourseId + " would exceed 18 credit limit, skipping");
                        }
                    }
                }
                
                // Step 1: Add required courses from the major that have prerequisites met

                for (String reqCourseId : majorCourses) {
                    // Skip if already in selected courses (from desired courses)
                    if (selectedCourses.contains(reqCourseId)) {
                        continue;
                    }
                    
                    Object courseObj = environment.get(reqCourseId);
                    if (courseObj != null && courseObj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> course = (Map<String, Object>) courseObj;
                        int courseCredits = (int) course.get("credits");
                        @SuppressWarnings("unchecked")
                        List<String> coursePrereqs = (List<String>) course.get("prerequisites");
                        
                        // Check if student has already completed this course
                        if (schedStudentCompletedCourses.contains(reqCourseId)) {
                            continue; // Skip courses already completed
                        }
                        
                        // Check if prerequisites are met
                        boolean prereqsMet = true;
                        if (coursePrereqs != null && !coursePrereqs.isEmpty()) {
                            for (String prereq : coursePrereqs) {
                                if (!schedStudentCompletedCourses.contains(prereq)) {
                                    prereqsMet = false;
                                    break;
                                }
                            }
                        }
                        
                        // Only add if prerequisites are met and within credit limit
                        if (prereqsMet && totalCredits + courseCredits <= 18) {
                            selectedCourses.add(reqCourseId);
                            totalCredits += courseCredits;
                        }
                    }
                }
                
                // Step 2: If we don't have at least 15 credits, add more courses from environment
                if (totalCredits < 15) {
                    for (Map.Entry<String, Object> entry : environment.entrySet()) {
                        if (totalCredits >= 15) break;
                        
                        Object obj = entry.getValue();
                        if (obj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> item = (Map<String, Object>) obj;
                            
                            // Check if it's a course and not already selected or completed
                            if ("course".equals(item.get("type")) 
                                && !selectedCourses.contains(entry.getKey())
                                && !schedStudentCompletedCourses.contains(entry.getKey())) {
                                
                                int courseCredits = (int) item.get("credits");
                                @SuppressWarnings("unchecked")
                                List<String> coursePrereqs = (List<String>) item.get("prerequisites");
                                
                                // Check if prerequisites are met
                                boolean prereqsMet = true;
                                if (coursePrereqs != null && !coursePrereqs.isEmpty()) {
                                    for (String prereq : coursePrereqs) {
                                        if (!schedStudentCompletedCourses.contains(prereq)) {
                                            prereqsMet = false;
                                            break;
                                        }
                                    }
                                }
                                
                                if (prereqsMet && totalCredits + courseCredits <= 18) {
                                    selectedCourses.add(entry.getKey());
                                    totalCredits += courseCredits;
                                }
                            }
                        }
                    }
                }
                
                // Final check
                if (totalCredits < 15) {
                    System.err.println("Unable to generate schedule: Not enough courses available to meet minimum 15 credits (only " + totalCredits + " credits)");
                    return null;
                }
                
                // Create schedule
                String scheduleId = "schedule" + schedStudentId;
                Map<String, Object> scheduleInfo = new HashMap<>();
                scheduleInfo.put("type", "schedule");
                scheduleInfo.put("student", schedStudentId);
                scheduleInfo.put("semester", schedSemesterId);
                scheduleInfo.put("courses", selectedCourses);
                scheduleInfo.put("totalCredits", totalCredits);
                environment.put(scheduleId, scheduleInfo);
                
                System.out.println("Schedule generated with ID: " + scheduleId + " (" + totalCredits + " credits)");
                break;
                
            case "ScheduleCompleted":
                if (call.arguments.size() != 1) {
                    System.err.println("ScheduleCompleted requires 1 argument: (String scheduleId)");
                    return null;
                }
                String completedScheduleId = (String) call.arguments.get(0);
                
                // Get schedule info
                Object scheduleObjToComplete = environment.get(completedScheduleId);
                if (scheduleObjToComplete == null || !(scheduleObjToComplete instanceof Map)) {
                    System.err.println("Schedule not found: " + completedScheduleId);
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> completedSchedule = (Map<String, Object>) scheduleObjToComplete;
                
                if (!"schedule".equals(completedSchedule.get("type"))) {
                    System.err.println("Error: " + completedScheduleId + " is not a schedule");
                    return null;
                }
                
                String schedStudentIdToUpdate = (String) completedSchedule.get("student");
                @SuppressWarnings("unchecked")
                List<String> scheduleCourses = (List<String>) completedSchedule.get("courses");
                
                // Get student info
                Object studentObjToUpdate = environment.get(schedStudentIdToUpdate);
                if (studentObjToUpdate == null || !(studentObjToUpdate instanceof Map)) {
                    System.err.println("Student not found: " + schedStudentIdToUpdate);
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> studentToUpdate = (Map<String, Object>) studentObjToUpdate;
                
                // Get current completed courses and credits
                @SuppressWarnings("unchecked")
                List<String> currentCompletedCourses = (List<String>) studentToUpdate.get("completedCourses");
                int currentCredits = (int) studentToUpdate.get("credits");
                
                // Add schedule courses to completed courses and calculate credits earned
                int creditsEarned = 0;
                List<String> newlyCompletedCourses = new java.util.ArrayList<>();
                
                for (String schedCourseId : scheduleCourses) {
                    if (!currentCompletedCourses.contains(schedCourseId)) {
                        currentCompletedCourses.add(schedCourseId);
                        newlyCompletedCourses.add(schedCourseId);
                        
                        // Get course credits
                        Object courseObjForCredits = environment.get(schedCourseId);
                        if (courseObjForCredits != null && courseObjForCredits instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> courseForCredits = (Map<String, Object>) courseObjForCredits;
                            creditsEarned += (int) courseForCredits.get("credits");
                        }
                    }
                }
                
                // Update student's credits
                int newTotalCredits = currentCredits + creditsEarned;
                studentToUpdate.put("credits", newTotalCredits);
                
                String studentNameForUpdate = (String) studentToUpdate.get("name");
                System.out.println(studentNameForUpdate + " (" + schedStudentIdToUpdate + ") completed schedule " + completedScheduleId);
                System.out.println("  - Courses completed: " + newlyCompletedCourses);
                System.out.println("  - Credits earned: " + creditsEarned);
                System.out.println("  - Total credits: " + currentCredits + " -> " + newTotalCredits);
                break;
                
            case "CheckClassification":
                if (call.arguments.size() != 1) {
                    System.err.println("CheckClassification requires 1 argument: (String studentId)");
                    return null;
                }
                String classStudentId = (String) call.arguments.get(0);
                
                // Get student info
                Object classStudentObj = environment.get(classStudentId);
                if (classStudentObj == null || !(classStudentObj instanceof Map)) {
                    System.err.println("Student not found: " + classStudentId);
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> classStudent = (Map<String, Object>) classStudentObj;
                
                if (!"student".equals(classStudent.get("type"))) {
                    System.err.println("Error: " + classStudentId + " is not a student");
                    return null;
                }
                
                int classStudentCredits = (int) classStudent.get("credits");
                String classification;
                
                if (classStudentCredits < 30) {
                    classification = "Freshman";
                } else if (classStudentCredits < 60) {
                    classification = "Sophomore";
                } else if (classStudentCredits < 90) {
                    classification = "Junior";
                } else {
                    classification = "Senior";
                }
                
                String classStudentName = (String) classStudent.get("name");
                System.out.println(classStudentName + " (" + classStudentId + ") is a " + classification + " with " + classStudentCredits + " credits");
                break;
                
            case "CheckGraduation":
                if (call.arguments.size() != 1) {
                    System.err.println("CheckGraduation requires 1 argument: (String studentId)");
                    return null;
                }
                String gradStudentId = (String) call.arguments.get(0);
                
                // Get student info
                Object gradStudentObj = environment.get(gradStudentId);
                if (gradStudentObj == null || !(gradStudentObj instanceof Map)) {
                    System.err.println("Student not found: " + gradStudentId);
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> gradStudent = (Map<String, Object>) gradStudentObj;
                
                if (!"student".equals(gradStudent.get("type"))) {
                    System.err.println("Error: " + gradStudentId + " is not a student");
                    return null;
                }
                
                String gradStudentName = (String) gradStudent.get("name");
                int gradStudentCredits = (int) gradStudent.get("credits");
                String gradStudentMajor = (String) gradStudent.get("major");
                
                // Get major info
                Object gradMajorObj = environment.get(gradStudentMajor);
                if (gradMajorObj == null || !(gradMajorObj instanceof Map)) {
                    System.err.println("Major not found: " + gradStudentMajor);
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> gradMajor = (Map<String, Object>) gradMajorObj;
                @SuppressWarnings("unchecked")
                List<String> gradRequiredCourses = (List<String>) gradMajor.get("requiredCourses");
                @SuppressWarnings("unchecked")
                List<String> gradCompletedCourses = (List<String>) gradStudent.get("completedCourses");
                
                // Check which required courses are missing
                List<String> missingCourses = new java.util.ArrayList<>();
                for (String gradCourseId : gradRequiredCourses) {
                    if (!gradCompletedCourses.contains(gradCourseId)) {
                        missingCourses.add(gradCourseId);
                    }
                }
                
                // Check if student meets graduation requirements
                boolean hasEnoughCredits = gradStudentCredits >= 120;
                boolean hasCompletedMajorRequirements = missingCourses.isEmpty();
                
                if (hasEnoughCredits && hasCompletedMajorRequirements) {
                    System.out.println(gradStudentName + " (" + gradStudentId + ") is eligible to graduate!");
                    System.out.println("  - Total credits: " + gradStudentCredits + "/120 ");
                    System.out.println("  - Major requirements: All " + gradRequiredCourses.size() + " required courses completed ");
                } else {
                    System.out.println(gradStudentName + " (" + gradStudentId + ") is NOT eligible to graduate:");
                    if (!hasEnoughCredits) {
                        System.out.println("  - Total credits: " + gradStudentCredits + "/120 (need " + (120 - gradStudentCredits) + " more)");
                    } else {
                        System.out.println("  - Total credits: " + gradStudentCredits + "/120 ");
                    }
                    if (!hasCompletedMajorRequirements) {
                        System.out.println("  - Major requirements: Missing " + missingCourses.size() + " course(s): " + missingCourses);
                    } else {
                        System.out.println("  - Major requirements: All " + gradRequiredCourses.size() + " required courses completed ");
                    }
                }
                break;

            case "SwitchMajor":
                if (call.arguments.size() != 2) {
                    System.err.println("SwitchMajor requires 2 arguments: (String studentId, String newMajorId)");
                    return null;
                }
                String switchStudentId = (String) call.arguments.get(0);
                String newMajorId = (String) call.arguments.get(1);
                
                // Get student info
                Object switchStudentObj = environment.get(switchStudentId);
                if (switchStudentObj == null || !(switchStudentObj instanceof Map)) {
                    System.err.println("Student not found: " + switchStudentId);
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> switchStudent = (Map<String, Object>) switchStudentObj;
                
                if (!"student".equals(switchStudent.get("type"))) {
                    System.err.println("Error: " + switchStudentId + " is not a student");
                    return null;
                }
                
                // Verify new major exists
                Object newMajorObj = environment.get(newMajorId);
                if (newMajorObj == null || !(newMajorObj instanceof Map)) {
                    System.err.println("Major not found: " + newMajorId);
                    return null;
                }
                
                // Update student's major
                String oldMajorId = (String) switchStudent.get("major");
                switchStudent.put("major", newMajorId);
                
                String switchStudentName = (String) switchStudent.get("name");
                System.out.println(switchStudentName + " (" + switchStudentId + ") switched major from " + oldMajorId + " to " + newMajorId);
                break;
                
            default:
                System.err.println("Unknown function: " + call.functionName);
        }
        return null;
    }

}

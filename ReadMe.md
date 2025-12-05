Schedon is a domain-specific language (DSL) designed to simplify academic scheduling and student progression tracking. 
Built to help students by providing an intuitive syntax for modeling courses, majors, prerequisites, and generating valid academic schedules.

Features include:
- Course Management: Define courses with credits and prerequisites
- Major Definitions: Create majors with required course lists
- Student Tracking: Model student progress, credits, and completed courses
- Scheduling: Auto-generate schedules with prerequisite validation
- Progress Simulation: Simulate semester completion and track graduation eligibility
- Classification System: Automatic student year classification 
- Graduation Verification: Check if students meet all requirements
- Prerequisite Enforcement: Ensures students only enroll in courses they're qualified for
I have provide a comprehensive ReadMe file demonstrating all features within the zip.

To run the Interpreter clone the repository, download the zip, and run java BYOL.Schedon. In the terminal, the functions could be used.

Language Syntax

student <id> {
    name = "<full name>";
    major = <majorId>;
    credits = <number>;
    completedCourses = [<courseId1>, <courseId2>, ...];
}

course <id> {
    title = "<course title>";
    credits = <number>;
    prerequisites = [<prereqId1>, <prereqId2>, ...];
}

major <id> {
    requiredCourses = [<courseId1>, <courseId2>, ...];
}

semester <id> {
    year = <year>;
    term = <termName>;
}

print <identifier>;

Schedon Flow
1. Add all available cources
2. Add all Majors
3. Add Student(Yourself)
4. Add Semester
5. Generate Schedules and simulate through college

Author:
Nehemiah Simmons
- GitHub: [@Nsimmons611](https://github.com/Nsimmons611)
- Built as part of CSC 404 - Programming Languages course

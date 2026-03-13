# Schedon: Academic Schedule Programming Language

## Overview
**Schedon** is a domain-specific programming language (DSL) designed for defining, managing, and querying academic course curricula, student majors, and semester schedules. The language emphasizes clear syntax for educational administrators and academic planners to define complex degree requirements, course prerequisites, and generate student schedules.

**File Extension:** `.schedon`

## Language Features

### Core Capabilities
- **Course Management**: Define courses with titles, credit hours, and prerequisite dependencies
- **Major/Degree Definition**: Specify major programs and their required courses
- **Student Profiles**: Create student records with completed courses and current major
- **Semester Planning**: Define academic semesters and term information
- **Schedule Generation**: Create semester schedules for students while respecting prerequisites
- **Query Operations**: Print and inspect defined entities

### Non-Trivial Features
- **Prerequisite Tracking**: Courses maintain dependency graphs; system validates prerequisites
- **Persistent Environment**: Multiple declarations accumulate state throughout program execution
- **Context-Aware Scheduling**: Schedules validate student eligibility based on completed courses

## Formal Grammar (BNF/EBNF)

For a complete formal grammar specification, see [GRAMMAR.md](GRAMMAR.md).

### Quick Grammar Overview

## Keywords
- `course`: Define a course with credits and prerequisites
- `major`: Define a degree program
- `student`: Define a student record
- `semester`: Define an academic semester
- `schedule`: Define a course schedule
- `print`: Output entity definitions
- Built-in functions: `AddCourse()`, `AddMajor()`, `AddStudent()`, etc.

## Data Types
- **String** (`"text"`): Course names, majors, student names
- **Number** (integers): Credits, years, grades
- **List** (`[item1, item2]`): Collections of course IDs
- **Identifier** (unquoted): Variable names, course IDs (e.g., `CSC101`)

## Running Schedon

### Interactive REPL Mode
```bash
java -cp . BYOL.Schedon
```

Interactive prompt allows you to enter Schedon statements one at a time:
```
schedon> course CSC101 { title = "Intro to CS"; credits = 3; prerequisites = []; }
Course added: Intro to CS with 3 credits
schedon> print CSC101
course CSC101 { title = "Intro to CS"; credits = 3; prerequisites = []; }
schedon> 
```

### Script File Mode
```bash
java -cp . BYOL.Schedon schedule.schedon
```

Execute a complete Schedon program from a file (see example files below).

## Usage Examples

See the included `.schedon` example files for comprehensive demonstrations:
- [basic_courses.schedon](basic_courses.schedon): Define courses, prerequisites, and majors
- [student_schedule.schedon](student_schedule.schedon): Create students and their semester schedules
- [university_curriculum.schedon](university_curriculum.schedon): Full university program with multiple majors

**To run an example:**
```bash
java -cp . BYOL.Schedon basic_courses.schedon
```

### Quick Example
```schedon
// Define a course
course CSC101 {
  title = "Introduction to Computer Science";
  credits = 3;
  prerequisites = [];
}

// Define a major
major CompSci {
  requiredCourses = [CSC101];
}

// Create a student
student s1 {
  name = "John Doe";
  major = CompSci;
  credits = 0;
  completedCourses = [];
}

// Print the student info
print s1
```

## Building and Testing

1. **Compile all Java files:**
   ```bash
   javac BYOL/*.java
   ```

2. **Run in REPL mode:**
   ```bash
   java -cp . BYOL.Schedon
   ```

3. **Run a script file:**
   ```bash
   java -cp . BYOL.Schedon example.schedon
   ```

## Project Structure
- `Schedon.java`: Main entry point and REPL implementation
- `Scanner.java`: Lexical analyzer
- `Parser.java`: Syntactic analyzer
- `Interpreter.java`: Semantic analysis and execution engine
- `Stmt.java`: AST node definitions
- `Token.java`: Token representation
- `TokenType.java`: Token type enumeration

## Implementation Notes
- The interpreter uses the Visitor pattern for AST traversal
- Declarations are stored in a persistent environment HashMap
- Prerequisites are validated lazily during schedule generation
- The REPL maintains state across multiple commands
- Script files produce output to stdout for each statement executed

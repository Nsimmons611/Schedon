# Schedon Language Grammar Specification

## Overview
This document provides the formal grammar specification for the Schedon programming language, which is designed for managing academic curricula and scheduling.

## File Extension
All Schedon source files use the `.schedon` extension (e.g., `courses.schedon`, `schedule.schedon`).

## Formal Grammar (BNF)

### Program Structure
```
program             → statement* EOF

statement           → declaration | assignment | functionCall | printStatement

declaration         → courseDecl 
                    | majorDecl 
                    | semesterDecl 
                    | studentDecl 
                    | scheduleDecl
```

### Course Declaration
```
courseDecl          → "course" IDENTIFIER "{" courseBody "}"
courseBody          → courseField*
courseField         → fieldName "=" value ";"
fieldName           → "title" | "credits" | "prerequisites"
value               → literal | list
```

### Major Declaration
```
majorDecl           → "major" IDENTIFIER "{" majorBody "}"
majorBody           → "requiredCourses" "=" courseList ";"
courseList          → "[" IDENTIFIER ("," IDENTIFIER)* "]"
                    | "["  "]"
```

### Student Declaration
```
studentDecl         → "student" IDENTIFIER "{" studentBody "}"
studentBody         → studentField*
studentField        → ("name" "=" STRING ";")
                    | ("major" "=" IDENTIFIER ";")
                    | ("credits" "=" NUMBER ";")
                    | ("completedCourses" "=" courseList ";")
```

### Semester Declaration
```
semesterDecl        → "semester" IDENTIFIER "{" semesterBody "}"
semesterBody        → semesterField*
semesterField       → ("year" "=" NUMBER ";")
                    | ("term" "=" IDENTIFIER ";")
```

### Schedule Declaration
```
scheduleDecl        → "schedule" IDENTIFIER "{" scheduleBody "}"
scheduleBody        → scheduleField*
scheduleField       → ("student" "=" IDENTIFIER ";")
                    | ("semester" "=" IDENTIFIER ";")
                    | ("courses" "=" courseList ";")
```

### Print Statement
```
printStatement      → "print" IDENTIFIER
```

### Literals and Values
```
literal             → STRING 
                    | NUMBER
                    | IDENTIFIER

STRING              → "\"" <any character except quote> "\""
NUMBER              → <digit> (<digit>)*
IDENTIFIER          → <letter> (<letter> | <digit> | "-")*
                    | <letter> ("_" <letter> | <digit>)*

list                → "[" "]"
                    | "[" value ("," value)* "]"

value               → literal | list
```

## Lexical Elements

### Keywords
- `course`: Define a course
- `major`: Define a degree program  
- `student`: Define a student record
- `semester`: Define an academic term
- `schedule`: Define a course schedule
- `print`: Output entity information

### Operators
- `{` `}`: Scope delimiters
- `[` `]`: List delimiters
- `,`: List separator
- `;`: Statement terminator
- `=`: Field assignment

### Token Types
```
IDENTIFIER      - Unquoted names (courses, majors, students, etc.)
STRING          - Double-quoted text ("Introduction to CS")
NUMBER          - Integer values (3, 4, 2025)
SYMBOL          - Operators and delimiters: { } [ ] , ; =
KEYWORD         - Reserved words listed above
EOF             - End of file marker
```

## Symbol Examples

### Course Identifiers
Valid: `CSC101`, `MATH-151`, `PHYS_161`, `CS101`

### Field Names
Only allowed within their respective declaration contexts:
- Course fields: `title`, `credits`, `prerequisites`
- Major fields: `requiredCourses`
- Student fields: `name`, `major`, `credits`, `completedCourses`
- Semester fields: `year`, `term`
- Schedule fields: `student`, `semester`, `courses`

## Production Rules Priority

The grammar employs standard precedence:
1. Identifiers and literals
2. Lists
3. Values and expressions
4. Field assignments
5. Declarations
6. Top-level statements

## Comment Syntax
Single-line comments use the `//` delimiter (as seen in example files).
Block comments are not supported in this version.

## Scope and Visibility
- All declarations are global and persist in the environment
- Identifiers must be unique within their category (courses, majors, students, etc.)
- Previously declared identifiers can be referenced by later declarations
- The `print` statement retrieves and displays previously declared entities

## Validation Rules
1. Referenced identifiers must be previously declared
2. Course prerequisites must reference existing courses
3. Major required courses must reference existing courses
4. Student completed courses must reference existing courses
5. Schedule students must reference existing student declarations
6. Schedule semesters must reference existing semester declarations

## Sample Production Tree
```
program
 ├─ courseDecl (CSC101)
 │  └─ courseField × 3
 ├─ courseDecl (CSC201)
 │  └─ courseField × 3
 ├─ majorDecl (ComputerScience)
 │  └─ courseList [CSC101, CSC201]
 ├─ studentDecl (student1)
 │  └─ studentField × 4
 └─ printStatement (student1)
```

## Version Information
**Grammar Version:** 1.0  
**Language Version:** Schedon 1.0  
**Last Updated:** 2025
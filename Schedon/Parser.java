package BYOL;

import static BYOL.TokenType.*;

import java.util.ArrayList;
import java.util.List;

class Parser {
  private static class ParseError extends RuntimeException {}
  private final List<Token> tokens;
  private int current = 0;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

   List<Stmt> parse() {
        List<Stmt> declarations = new ArrayList<>();
        while (!isAtEnd()) {
            Stmt decl = declaration();
            if (decl != null) declarations.add(decl);
        }
        return declarations;
    }

    // Top-level declarations
    private Stmt declaration() {
        try {
            if (match(STUDENT)) return studentDecl();
            if (match(COURSE)) return courseDecl();
            if (match(MAJOR)) return majorDecl();
            if (match(SEMESTER)) return semesterDecl();
            if (match(SCHEDULE)) return scheduleDecl();
            if (match(PRINT)) return printStmt();
            
            // Check for function call (IDENTIFIER followed by LEFT_PAREN)
            if (check(IDENTIFIER) && peekNext() != null && peekNext().type == LEFT_PAREN) {
                return functionCall();
            }
            
            throw error(peek(), "Expected declaration.");
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    // Student declaration
    Stmt.Student studentDecl() {
        Token id = consume(IDENTIFIER, "Expect student identifier.");
        consume(LEFT_BRACE, "Expect '{' after student id.");

        String nameValue = null;
        String majorValue = null;
        int creditsValue = 0;
        List<String> completedCoursesValue = new ArrayList<>();

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            Token field = consumeFieldName("Expect field name.");
            consume(EQUAL, "Expect '=' after field name.");

            switch (field.lexeme) {
                case "name":
                    Token nameToken = consume(STRING, "Expect string value for name.");
                    nameValue = nameToken.literal.toString();
                    break;
                case "major":
                    Token majorToken = consume(IDENTIFIER, "Expect major identifier.");
                    majorValue = majorToken.lexeme;
                    break;
                case "credits":
                    Token creditsToken = consume(NUMBER, "Expect number of credits.");
                    creditsValue = (int)(double)creditsToken.literal;
                    break;
                case "completedCourses":
                    consume(LEFT_BRACKET, "Expect '[' before course list.");
                    if (!check(RIGHT_BRACKET)) {
                        do {
                            Token courseToken = consume(IDENTIFIER, "Expect course id.");
                            completedCoursesValue.add(courseToken.lexeme);
                        } while (match(COMMA));
                    }
                    consume(RIGHT_BRACKET, "Expect ']' after course list.");
                    break;
                default:
                    throw error(field, "Unknown field '" + field.lexeme + "' in student declaration.");
            }

            consume(SEMICOLON, "Expect ';' after field.");
        }

        consume(RIGHT_BRACE, "Expect '}' after student block.");
        return new Stmt.Student(id.lexeme, nameValue, majorValue, creditsValue, completedCoursesValue);
    }

    // Course declaration
    Stmt.Course courseDecl() {
        Token id = consume(IDENTIFIER, "Expect course identifier.");
        consume(LEFT_BRACE, "Expect '{' after course id.");

        String titleValue = null;
        int creditsValue = -1;
        List<String> prerequisitesValue = new ArrayList<>();

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            Token field = consumeFieldName("Expect field name.");
            consume(EQUAL, "Expect '=' after field name.");

            switch (field.lexeme) {
                case "title":
                    Token titleToken = consume(STRING, "Expect string value for title.");
                    titleValue = titleToken.literal.toString();
                    break;
                case "credits":
                    Token creditsToken = consume(NUMBER, "Expect number of credits.");
                    creditsValue = (int)(double)creditsToken.literal;
                    break;
                case "prerequisites":
                    consume(LEFT_BRACKET, "Expect '[' before prerequisite list.");
                    if (!check(RIGHT_BRACKET)) {
                        do {
                            Token prereqToken = consume(IDENTIFIER, "Expect course id.");
                            prerequisitesValue.add(prereqToken.lexeme);
                        } while (match(COMMA));
                    }
                    consume(RIGHT_BRACKET, "Expect ']' after prerequisite list.");
                    break;
                default:
                    throw error(field, "Unknown field '" + field.lexeme + "' in course declaration.");
            }

            consume(SEMICOLON, "Expect ';' after field.");
        }

        consume(RIGHT_BRACE, "Expect '}' after course block.");
        return new Stmt.Course(id.lexeme, titleValue, creditsValue, prerequisitesValue);
    }

    // Major declaration
    Stmt.Major majorDecl() {
        Token id = consume(IDENTIFIER, "Expect major identifier.");
        consume(LEFT_BRACE, "Expect '{' after major id.");

        List<String> requiredCourses = new ArrayList<>();

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            Token field = consumeFieldName("Expect field name.");
            consume(EQUAL, "Expect '=' after field name.");

            switch (field.lexeme) {
                case "requiredCourses":
                    consume(LEFT_BRACKET, "Expect '[' before course list.");
                    if (!check(RIGHT_BRACKET)) {
                        do {
                            Token courseId = consume(IDENTIFIER, "Expect course id.");
                            requiredCourses.add(courseId.lexeme);
                        } while (match(COMMA));
                    }
                    consume(RIGHT_BRACKET, "Expect ']' after course list.");
                    break;
                default:
                    throw error(field, "Unknown field '" + field.lexeme + "' in major declaration.");
            }

            consume(SEMICOLON, "Expect ';' after field.");
        }

        consume(RIGHT_BRACE, "Expect '}' after major block.");
        return new Stmt.Major(id.lexeme, requiredCourses);
    }

    // Semester declaration
    Stmt.Semester semesterDecl() {
        Token id = consume(IDENTIFIER, "Expect semester identifier.");
        consume(LEFT_BRACE, "Expect '{' after semester id.");

        int yearValue = -1;
        String termValue = null;

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            Token field = consumeFieldName("Expect field name.");
            consume(EQUAL, "Expect '=' after field name.");

            switch (field.lexeme) {
                case "year":
                    Token yearToken = consume(NUMBER, "Expect numeric year.");
                    yearValue = (int)(double)yearToken.literal;
                    break;
                case "term":
                    Token termToken = consume(IDENTIFIER, "Expect term identifier (Fall/Spring/Summer).");
                    termValue = termToken.lexeme;
                    break;
                default:
                    throw error(field, "Unknown field '" + field.lexeme + "' in semester declaration.");
            }

            consume(SEMICOLON, "Expect ';' after field.");
        }

        consume(RIGHT_BRACE, "Expect '}' after semester block.");
        return new Stmt.Semester(id.lexeme, yearValue, termValue);
    }

    // Schedule declaration
    Stmt.Schedule scheduleDecl() {
        Token id = consume(IDENTIFIER, "Expect schedule identifier.");
        consume(LEFT_BRACE, "Expect '{' after schedule id.");

        String studentId = null;
        String semesterId = null;
        List<String> coursesList = new ArrayList<>();

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            Token field = consumeFieldName("Expect field name.");
            consume(EQUAL, "Expect '=' after field name.");

            switch (field.lexeme) {
                case "student":
                    Token studentToken = consume(IDENTIFIER, "Expect student id.");
                    studentId = studentToken.lexeme;
                    break;
                case "semester":
                    Token semesterToken = consume(IDENTIFIER, "Expect semester id.");
                    semesterId = semesterToken.lexeme;
                    break;
                case "courses":
                    consume(LEFT_BRACKET, "Expect '[' before courses list.");
                    if (!check(RIGHT_BRACKET)) {
                        do {
                            Token courseToken = consume(IDENTIFIER, "Expect course id.");
                            coursesList.add(courseToken.lexeme);
                        } while (match(COMMA));
                    }
                    consume(RIGHT_BRACKET, "Expect ']' after courses list.");
                    break;
                default:
                    throw error(field, "Unknown field '" + field.lexeme + "' in schedule declaration.");
            }

            consume(SEMICOLON, "Expect ';' after field.");
        }

        consume(RIGHT_BRACE, "Expect '}' after schedule block.");
        return new Stmt.Schedule(id.lexeme, studentId, semesterId, coursesList);
    }

    // Print statement
    Stmt.Print printStmt() {
        Token id = consume(IDENTIFIER, "Expect identifier to print.");
        consume(SEMICOLON, "Expect ';' after print statement.");
        return new Stmt.Print(id.lexeme);
    }

    // Function call
    Stmt.FunctionCall functionCall() {
        Token funcName = consume(IDENTIFIER, "Expect function name.");
        consume(LEFT_PAREN, "Expect '(' after function name.");
        
        List<Object> args = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
                if (check(STRING)) {
                    Token str = advance();
                    args.add(str.literal);
                } else if (check(NUMBER)) {
                    Token num = advance();
                    args.add(num.literal);
                } else if (check(IDENTIFIER)) {
                    Token id = advance();
                    args.add(id.lexeme);
                } else if (check(LEFT_BRACKET)) {
                    // Parse array argument
                    advance(); // consume '['
                    List<String> list = new ArrayList<>();
                    if (!check(RIGHT_BRACKET)) {
                        do {
                            Token item = consume(IDENTIFIER, "Expect identifier in array.");
                            list.add(item.lexeme);
                        } while (match(COMMA));
                    }
                    consume(RIGHT_BRACKET, "Expect ']' after array.");
                    args.add(list);
                } else {
                    throw error(peek(), "Unexpected argument type.");
                }
            } while (match(COMMA));
        }
        
        consume(RIGHT_PAREN, "Expect ')' after arguments.");
        consume(SEMICOLON, "Expect ';' after function call.");
        return new Stmt.FunctionCall(funcName.lexeme, args);
    }

    private Token peekNext() {
        if (current + 1 >= tokens.size()) return null;
        return tokens.get(current + 1);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private Token consumeFieldName(String message) {
        if (check(IDENTIFIER) || check(STUDENT) || check(COURSE) || check(MAJOR)
                || check(SEMESTER) || check(SCHEDULE)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        System.err.println("[line " + token.line + "] Error at '" + token.lexeme + "': " + message);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return;

            switch (peek().type) {
                case STUDENT:
                case COURSE:
                case MAJOR:
                case SEMESTER:
                case SCHEDULE:
                case PRINT:
                    return;
                default:
                    break;
            }
            advance();
        }
    }
}


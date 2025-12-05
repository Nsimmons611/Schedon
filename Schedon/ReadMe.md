// Courses
AddCourse("Computer-Science 101", 3, []);
AddCourse("Computer-Science 201", 3, [CSC101]);
AddCourse("Computer-Science 301", 4, [CSC101, CSC201]);
AddCourse("Data-Structures 202", 4, [CSC101]);
AddCourse("Algorithms-I 303", 4, [DAS202, CSC201]);
AddCourse("Database-Systems 305", 3, [CSC201]);
AddCourse("Software-Engineering 401", 4, [CSC301, DAS202]);
AddCourse("Artificial-Intelligence 405", 4, [ALG303]);
AddCourse("Computer-Networks 320", 3, [CSC201]);
AddCourse("Cyber-security 410", 4, [CON320, DBS305]);
AddCourse("Intro-Calculus 151", 4, []);
AddCourse("Calculus-II 152", 4, [ICA151]);
AddCourse("Calculus-III 253", 4, [ICA152]);
AddCourse("Linear-Algebra 251", 3, [ICA151]);
AddCourse("Discrete-Math 203", 3, []);
AddCourse("Statistics-I 220", 3, []);
AddCourse("Differential-Equations 352", 4, [ICA152, LIA251]);
AddCourse("Intro-Medical 104", 4, []);
AddCourse("Intermediate-Medical 204", 4, [IME104]);
AddCourse("Advanced-Medical 304", 4, [IME104, IME204]);
AddCourse("Anatomy-I 210", 4, [IBI108]);
AddCourse("Physiology-I 220", 4, [ANA210]);
AddCourse("Pharmacology-I 310", 4, [IME204]);
AddCourse("Clinical-Practice 405", 4, [AME304, PHA310]);
AddCourse("Intro-Biology 108", 4, []);
AddCourse("Intro-Chemistry 102", 4, []);
AddCourse("Organic-Chemistry 202", 4, [ICH102]);
AddCourse("Intro-Physics 161", 4, []);
AddCourse("Physics-II 162", 4, [IPH161]);
AddCourse("Molecular-Biology 310", 4, [IBI108, OCH202]);
AddCourse("Intro-Economics 201", 3, []);
AddCourse("Micro-Economics 202", 3, [IEC201]);
AddCourse("Macro-Economics 203", 3, [IEC201]);
AddCourse("Accounting-I 210", 3, []);
AddCourse("Finance-I 305", 3, [ACC210, IEC201]);
AddCourse("Business-Management 301", 3, []);
AddCourse("Marketing-I 220", 3, []);
AddCourse("Engineering-Design 101", 3, []);
AddCourse("Circuits-I 201", 4, [IPH161]);
AddCourse("Thermo-Dynamics 205", 4, [IPH161, ICA151]);
AddCourse("Materials-Science 210", 3, [ICH102]);
AddCourse("Intro-Music 105", 2, []);
AddCourse("Music-Theory 205", 3, [IMU105]);
AddCourse("Intro-Art 102", 3, []);
AddCourse("Art-History 202", 3, [IAR102]);
AddCourse("English-Composition 110", 3, []);
AddCourse("Creative-Writing 215", 3, [ECO110]);
AddCourse("World-History 115", 3, []);
AddCourse("Philosophy-I 201", 3, []);
AddCourse("Ethics-I 301", 3, [PHI201]);
AddCourse("Intro-Basketball 023", 3, []);
AddCourse("Intro-Breathing 099", 1, []);
AddCourse("Intro-Boxing 102", 3, []);
AddCourse("Yoga-I 101", 2, []);
AddCourse("Nutrition-I 150", 3, []);
AddCourse("Intro-PreCal 101", 4, []);
AddCourse("Intro-Geometry 101", 4, []);
AddCourse("Public-Speaking 120", 3, []);
AddCourse("Critical-Thinking 125", 3, []);

// Majors
AddMajor("Computer-Science", [CSC101, CSC201, CSC301, DAS202, ALG303, DBS305, SOE401]);
AddMajor("Medical-Studies", [IME104, IME204, AME304, ANA210, PHY220, PHA310]);
AddMajor("Pro-Mathematics", [ICA151, ICA152, LIA251, DIM203, STA220]);
AddMajor("Pro-Biology", [IBI108, ANA210, PHY220, OCH202, MOB310]);
AddMajor("Pro-Business", [IEC201, MIC202, MAC203, ACC210, FIN305, BUM301]);
AddMajor("Pro-Engineering", [END101, CIR201, THE205, MAT210, ICA151, IPH161]);


semester fall25 { year = 2025; term = Fall; }
semester spring25 { year = 2025; term = Spring; }

AddStudent("Nehemiah Simmons", Computer-Science, 71, [CSC101, CSC201, CSC301]);
AddStudent(" ", , , []);
SwitchMajor( , );
GenerateSchedule( , );
GenerateSchedule( , , [IBA023]);
GenerateSchedule( , , [IBR099]);
CheckClassification();
ScheduleCompleted();
CheckGraduation( );

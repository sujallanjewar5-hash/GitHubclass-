package com.example.data

import java.io.Serializable

data class MockQuestion(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val marks: Int = 5 // 10 questions x 5 marks = 50 marks
) : Serializable

data class MockChapterTest(
    val id: String,
    val chapterName: String,
    val subject: String,
    val className: String, // "5th", "6th", etc.
    val board: String, // "Maharashtra State Board" or "CBSE"
    val durationMinutes: Int = 45,
    val totalMarks: Int = 50,
    val questions: List<MockQuestion>
) : Serializable

data class PYQPaper(
    val id: String,
    val className: String,
    val board: String,
    val subject: String,
    val year: Int,
    val title: String,
    val durationMinutes: Int = 120,
    val totalMarks: Int = 80
) : Serializable

data class StudyMaterial(
    val id: String,
    val className: String,
    val board: String,
    val subject: String,
    val title: String,
    val type: String, // "Textbook", "Revision Notes", "Formula Sheet"
    val contentPreview: List<ChapterContent>
) : Serializable

data class ChapterContent(
    val chapterNum: Int,
    val name: String,
    val pagesText: List<String>
) : Serializable

object AppData {
    val MockTests = listOf(
        // 10th Class - Maharashtra State Board - Science (Gravitation)
        MockChapterTest(
            id = "10_msb_sci_grav",
            chapterName = "Gravitation",
            subject = "Science",
            className = "10th",
            board = "Maharashtra State Board",
            questions = listOf(
                MockQuestion(1, "What is the value of gravitational acceleration 'g' at the center of Earth?", listOf("9.8 m/s²", "0 m/s²", "4.9 m/s²", "19.6 m/s²"), 1),
                MockQuestion(2, "Who formulated the Laws of Planetary Motion?", listOf("Isaac Newton", "Albert Einstein", "Johannes Kepler", "Galileo Galilei"), 2),
                MockQuestion(3, "The gravitational force between two masses is ________ proportional to the square of the distance between them.", listOf("Directly", "Inversely", "Not", "Exponentially"), 1),
                MockQuestion(4, "What is the escape velocity on the surface of Earth?", listOf("11.2 km/s", "8.0 km/s", "11.2 m/s", "5.6 km/s"), 0),
                MockQuestion(5, "The SI unit of universal gravitational constant 'G' is:", listOf("N m/kg", "N m²/kg²", "N² m²/kg", "m/s²"), 1),
                MockQuestion(6, "If the mass of one of the objects is doubled, the gravitational force between them ________.", listOf("is halved", "is doubled", "remains same", "becomes four times"), 1),
                MockQuestion(7, "The weight of an object on the Moon is about ________ of its weight on Earth.", listOf("1/2", "1/4", "1/6", "1/10"), 2),
                MockQuestion(8, "Which force keeps the planets in orbit around the Sun?", listOf("Centrifugal Force", "Electrostatic Force", "Centripetal (Gravitational) Force", "Magnetic Force"), 2),
                MockQuestion(9, "What happens to the value of 'g' as we go high above the Earth's surface?", listOf("It increases", "It decreases", "It remains constant", "It fluctuates"), 1),
                MockQuestion(10, "A free-falling body moves with constant ________.", listOf("Velocity", "Acceleration", "Speed", "Force"), 1)
            )
        ),
        // 10th Class - Maharashtra State Board - Maths (Linear Equations)
        MockChapterTest(
            id = "10_msb_math_linear",
            chapterName = "Linear Equations in Two Variables",
            subject = "Maths",
            className = "10th",
            board = "Maharashtra State Board",
            questions = listOf(
                MockQuestion(1, "What is the general form of a linear equation in two variables?", listOf("ax + b = 0", "ax² + bx + c = 0", "ax + by + c = 0", "ax/y + b = 0"), 2),
                MockQuestion(2, "If x = 3 and y = 2 satisfy the equation ax + 3y = 12, what is the value of a?", listOf("1", "2", "3", "4"), 1),
                MockQuestion(3, "For Cramer's Rule, what represents the determinant of x secondary to D?", listOf("Dy", "Dx", "D/x", "x.D"), 1),
                MockQuestion(4, "If Dx = 24 and D = 4, what is the value of x?", listOf("96", "4", "6", "1/6"), 2),
                MockQuestion(5, "What is the shape of the graph representing ax + by + c = 0?", listOf("Parabola", "Straight Line", "Circle", "Hyperbola"), 1),
                MockQuestion(6, "The degree of any linear equation is always:", listOf("0", "1", "2", "3"), 1),
                MockQuestion(7, "For simultaneous equations in x and y, if Dy = -15 and D = 5, what is y?", listOf("3", "-3", "10", "-10"), 1),
                MockQuestion(8, "To draw the graph of 4x + 5y = 19, if x = 1, find y:", listOf("2", "3", "4", "5"), 1),
                MockQuestion(9, "How many solutions do two parallel lines on a graph have?", listOf("Infinite", "One", "Zero", "Two"), 2),
                MockQuestion(10, "If a1/a2 = b1/b2 != c1/c2, the equations are:", listOf("Consistent", "Inconsistent (No solution)", "Dependent (Infinite solutions)", "Intersecting"), 1)
            )
        ),
        // 10th Class - CBSE - Science (Chemical Reactions)
        MockChapterTest(
            id = "10_cbse_sci_chem",
            chapterName = "Chemical Reactions and Equations",
            subject = "Science",
            className = "10th",
            board = "CBSE",
            questions = listOf(
                MockQuestion(1, "What type of reaction releases heat to the environment?", listOf("Endothermic", "Exothermic", "Decomposition", "Double Displacement"), 1),
                MockQuestion(2, "Fe₂O₃ + 2Al -> Al₂O₃ + 2Fe is an example of:", listOf("Combination Reaction", "Double Displacement", "Decomposition", "Displacement Reaction"), 3),
                MockQuestion(3, "When carbon dioxide is passed through limewater, it turns ________ due to CaCO₃ formation.", listOf("Blue", "Milky", "Red", "Clear and transparent"), 1),
                MockQuestion(4, "The brown gas evolved during the heating of lead nitrate is:", listOf("NO", "NO₂", "N₂O", "O₂"), 1),
                MockQuestion(5, "What is the color of magnesium ribbon when burned in air?", listOf("Yellowish brown", "Bright white", "Red", "Green"), 1),
                MockQuestion(6, "The chemical formula of Rust is:", listOf("Fe₂O₃.xH₂O", "FeO", "Fe₃O₄", "FeCO₃"), 0),
                MockQuestion(7, "Fatty foods become rancid because of which process?", listOf("Reduction", "Oxidation", "Hydrogenation", "Distillation"), 1),
                MockQuestion(8, "What precipitate is formed when Barium Chloride reacts with Sodium Sulfate?", listOf("Barium sulfate (White)", "Sodium chloride (Yellow)", "Barium sulfate (Blue)", "Sodium sulfate (White)"), 0),
                MockQuestion(9, "Adding zinc granules to dilute hydrochloric acid produces ________ gas.", listOf("Oxygen", "Hydrogen", "Carbon Dioxide", "Chlorine"), 1),
                MockQuestion(10, "A reaction where oxidation and reduction happen simultaneously is:", listOf("Combination", "Neutralization", "Redox", "Precipitation"), 2)
            )
        ),
        // 10th Class - CBSE - Maths (Real Numbers)
        MockChapterTest(
            id = "10_cbse_math_real",
            chapterName = "Real Numbers",
            subject = "Maths",
            className = "10th",
            board = "CBSE",
            questions = listOf(
                MockQuestion(1, "Which of the following is an irrational number?", listOf("2.35", "√4", "√5", "2/3"), 2),
                MockQuestion(2, "The Fundamental Theorem of Arithmetic states that every composite number can be uniquely factored as a product of:", listOf("Even numbers", "Prime numbers", "Odd numbers", "Rational numbers"), 1),
                MockQuestion(3, "If two positive integers a and b are written as a = x³y² and b = xy³, then HCF(a, b) is:", listOf("xy", "xy²", "x³y³", "x²y²"), 1),
                MockQuestion(4, "Product of HCF and LCM of any two numbers a and b is:", listOf("a + b", "a - b", "a * b", "a / b"), 2),
                MockQuestion(5, "If HCF(26, 91) = 13, calculate LCM(26, 91):", listOf("182", "364", "91", "26"), 0),
                MockQuestion(6, "The decimal expansion of 13 / 3125 will:", listOf("Terminate", "Be non-terminating repeating", "Be non-terminating non-repeating", "None of these"), 0),
                MockQuestion(7, "The exponent of 2 in the prime factorization of 144 is:", listOf("2", "4", "5", "3"), 1),
                MockQuestion(8, "What is the HCF of the smallest prime number and smallest composite number?", listOf("1", "2", "3", "4"), 1),
                MockQuestion(9, "π (Pi) is a/an ________ number.", listOf("Integer", "Rational", "Irrational", "Whole"), 2),
                MockQuestion(10, "If d = HCF(45, 105), then d is equal to:", listOf("5", "15", "35", "45"), 1)
            )
        ),
        // 9th Class - Maharashtra State Board - Science (Laws of Motion)
        MockChapterTest(
            id = "9_msb_sci_motion",
            chapterName = "Laws of Motion",
            subject = "Science",
            className = "9th",
            board = "Maharashtra State Board",
            questions = listOf(
                MockQuestion(1, "What is the rate of change of displacement called?", listOf("Speed", "Velocity", "Acceleration", "Force"), 1),
                MockQuestion(2, "Newton's first law of motion is also called the Law of ________.", listOf("Inertia", "Momentum", "Action", "Gravity"), 0),
                MockQuestion(3, "What is the formula for Momentum (P)?", listOf("Mass x Velocity", "Mass / Velocity", "Force x Time", "Acceleration x Mass"), 0),
                MockQuestion(4, "The distance-time graph for uniform motion is a:", listOf("Curved line", "Straight line", "Circle", "Zigzag line"), 1),
                MockQuestion(5, "What is the SI unit of acceleration?", listOf("m/s", "m/s²", "km/h", "N/kg"), 1),
                MockQuestion(6, "For every action, there is an equal and opposite reaction. This is Newton's:", listOf("1st Law", "2nd Law", "3rd Law", "Universal Law"), 2),
                MockQuestion(7, "If a body travels equal distances in equal intervals of time, it is in ________ speed.", listOf("Non-uniform", "Uniform", "Variable", "Instantaneous"), 1),
                MockQuestion(8, "Inertia of a body depends directly on its:", listOf("Velocity", "Shape", "Mass", "Volume"), 2),
                MockQuestion(9, "What force coordinates the motion of a spinning stone on a string?", listOf("Centripetal Force", "Centrifugal Force", "Gravitational Force", "Frictional Force"), 0),
                MockQuestion(10, "The rate of change of momentum is proportional to the applied:", listOf("Velocity", "Mass", "Force", "Displacement"), 2)
            )
        ),
        // 8th Class - Maharashtra State Board - Science (Force and Pressure)
        MockChapterTest(
            id = "8_msb_sci_force",
            chapterName = "Force and Pressure",
            subject = "Science",
            className = "8th",
            board = "Maharashtra State Board",
            questions = listOf(
                MockQuestion(1, "What is the SI unit of Force?", listOf("Watt", "Joule", "Pascal", "Newton"), 3),
                MockQuestion(2, "Pressure is defined as:", listOf("Force x Area", "Force / Area", "Area / Force", "Force + Area"), 1),
                MockQuestion(3, "What is the unit of pressure?", listOf("Newton-meter", "Pascal (N/m²)", "Joule", "Watt"), 1),
                MockQuestion(4, "The upward force acting on an object immersed in a fluid is called:", listOf("Gravitational Force", "Buoyant Force", "Frictional Force", "Magnetic Force"), 1),
                MockQuestion(5, "Atmospheric pressure increases as we go:", listOf("Higher up", "Deeper down", "Remains same", "To the moon"), 1),
                MockQuestion(6, "Which of the following is a non-contact force?", listOf("Frictional Force", "Electrostatic Force", "Muscular Force", "Tension in string"), 1),
                MockQuestion(7, "Friction always acts in a direction ________ to the direction of motion.", listOf("Same", "Opposite", "Perpendicular", "Random"), 1),
                MockQuestion(8, "A sharp knife cuts easily because it exerts ________ pressure for the same force.", listOf("Lesser", "Greater", "Zero", "Same"), 1),
                MockQuestion(9, "The density of water is maximum at what temperature?", listOf("0°C", "4°C", "100°C", "-4°C"), 1),
                MockQuestion(10, "An iron nail sinks in water but a ships floats because of:", listOf("Newton's Law", "Archimedes' Principle", "Pascal's Law", "Ohm's Law"), 1)
            )
        ),
        // 7th Class - Maharashtra State Board - Maths (Integers/Constructions)
        MockChapterTest(
            id = "7_msb_math_construction",
            chapterName = "Geometrical Constructions",
            subject = "Maths",
            className = "7th",
            board = "Maharashtra State Board",
            questions = listOf(
                MockQuestion(1, "The sum of the measures of the angles of a triangle is:", listOf("90°", "180°", "270°", "360°"), 1),
                MockQuestion(2, "An angle bisector divides an angle into how many equal parts?", listOf("Two", "Three", "Four", "None"), 0),
                MockQuestion(3, "The perpendicular bisectors of the sides of a triangle are ________.", listOf("Parallel", "Concurrent", "Perpendicular", "Skew"), 1),
                MockQuestion(4, "The point of concurrence of the angle bisectors of a triangle is called the:", listOf("Circumcentre", "Incentre", "Orthocentre", "Centroid"), 1),
                MockQuestion(5, "To construct a triangle, we need at least ________ elements.", listOf("One", "Two", "Three", "Four"), 2),
                MockQuestion(6, "What is the point of concurrence of the altitudes of a triangle called?", listOf("Circumcentre", "Incentre", "Orthocentre", "Centroid"), 2),
                MockQuestion(7, "A triangle with all three sides of equal length is a/an ________ triangle.", listOf("Equilateral", "Isosceles", "Scalene", "Right-angled"), 0),
                MockQuestion(8, "The point of concurrence of the perpendicular bisectors of a triangle's sides is:", listOf("Circumcentre", "Incentre", "Orthocentre", "Centroid"), 0),
                MockQuestion(9, "A line segment has how many perpendicular bisectors?", listOf("One", "Two", "Infinite", "None"), 0),
                MockQuestion(10, "In a right-angled triangle, the side opposite the right angle is called:", listOf("Base", "Height", "Hypotenuse", "Altitude"), 2)
            )
        ),
        // 6th Class - Maharashtra State Board - Science (Natural Resources)
        MockChapterTest(
            id = "6_msb_sci_nat",
            chapterName = "Natural Resources - Air, Water and Land",
            subject = "Science",
            className = "6th",
            board = "Maharashtra State Board",
            questions = listOf(
                MockQuestion(1, "Which gas has the largest proportion (approx 78%) in the atmosphere?", listOf("Oxygen", "Carbon Dioxide", "Nitrogen", "Argon"), 2),
                MockQuestion(2, "The gas useful for combustion is:", listOf("Nitrogen", "Carbon Dioxide", "Oxygen", "Helium"), 2),
                MockQuestion(3, "What layer of the atmosphere contains the supportive Ozone shield?", listOf("Troposphere", "Stratosphere", "Mesosphere", "Exosphere"), 1),
                MockQuestion(4, "How much of the Earth's surface is covered with water?", listOf("29%", "50%", "71%", "97%"), 2),
                MockQuestion(5, "Humus is the layer of soil formed by decomposition of:", listOf("Rocks", "Metals", "Dead plants & animals", "Plastic waste"), 2),
                MockQuestion(6, "How long does it take for a 2.5 cm thick layer of mature soil to form in nature?", listOf("10 years", "100 years", "1000 years", "5 years"), 2),
                MockQuestion(7, "Which is a component of clean air?", listOf("Soot", "Water Vapor", "Carbon Monoxide", "Acid rain"), 1),
                MockQuestion(8, "Drinking water should be free from micro-organisms and impurities. This process is:", listOf("Purification", "Evaporating", "Straining", "Decomposition"), 0),
                MockQuestion(9, "What is the main source of natural fresh water?", listOf("River", "Sea", "Rain", "Well"), 2),
                MockQuestion(10, "The rocky outer crust of Earth represents which layer?", listOf("Hydrosphere", "Atmosphere", "Lithosphere", "Biosphere"), 2)
            )
        ),
        // 5th Class - Maharashtra State Board - Maths (Roman Numerals)
        MockChapterTest(
            id = "5_msb_math_roman",
            chapterName = "Roman Numerals",
            subject = "Maths",
            className = "5th",
            board = "Maharashtra State Board",
            questions = listOf(
                MockQuestion(1, "What does the Roman numeral 'V' represent?", listOf("1", "5", "10", "50"), 1),
                MockQuestion(2, "Which Roman numeral represents '10'?", listOf("I", "V", "X", "L"), 2),
                MockQuestion(3, "How is '4' written in Roman numerals?", listOf("IIII", "IV", "VI", "IX"), 1),
                MockQuestion(4, "What number is represented by 'XIX'?", listOf("11", "15", "19", "21"), 2),
                MockQuestion(5, "What Roman numeral represents '50'?", listOf("C", "D", "L", "M"), 2),
                MockQuestion(6, "Which letter is used for '100'?", listOf("D", "M", "C", "L"), 2),
                MockQuestion(7, "What is the Roman numeral for '8'?", listOf("IIIV", "VIII", "XVIII", "VII"), 1),
                MockQuestion(8, "The letter for '500' in Roman numerals is:", listOf("M", "D", "C", "L"), 1),
                MockQuestion(9, "How do we write the number '15'?", listOf("VXV", "VVV", "XV", "XIX"), 2),
                MockQuestion(10, "What is the Roman numeral value of 'XXVIII'?", listOf("18", "23", "28", "38"), 2)
            )
        )
    )

    val PYQPapers = listOf(
        PYQPaper("10_msb_math_2025", "10th", "Maharashtra State Board", "Maths", 2025, "10th Board Maths - Part I (Algebra)", 120, 40),
        PYQPaper("10_msb_math_2024", "10th", "Maharashtra State Board", "Maths", 2024, "10th Board Maths - Part II (Geometry)", 120, 40),
        PYQPaper("10_msb_sci_2025", "10th", "Maharashtra State Board", "Science", 2025, "10th Board Science - Part I (Physics/Chemistry)", 120, 40),
        PYQPaper("10_msb_sci_2024", "10th", "Maharashtra State Board", "Science", 2024, "10th Board Science - Part II (Biology/Ecology)", 120, 40),
        PYQPaper("10_cbse_math_2025", "10th", "CBSE", "Maths", 2025, "CBSE Class 10th Maths Standard Solved Paper", 180, 80),
        PYQPaper("10_cbse_math_2024", "10th", "CBSE", "Maths", 2024, "CBSE Class 10th Maths Solved Series B", 180, 80),
        PYQPaper("10_cbse_sci_2025", "10th", "CBSE", "Science", 2025, "CBSE Class 10th Science official Board Paper", 180, 80),
        PYQPaper("10_cbse_sci_2024", "10th", "CBSE", "Science", 2024, "CBSE Class 10th Science official Board Paper B", 180, 80),
        PYQPaper("9_msb_all_2025", "9th", "Maharashtra State Board", "Maths", 2025, "Class 9th Algebra & Geometry Yearly Exam", 120, 80),
        PYQPaper("9_msb_all_2024", "9th", "Maharashtra State Board", "Science", 2024, "Class 9th General Science Annual Question Paper", 120, 80),
        PYQPaper("8_msb_all_2025", "8th", "Maharashtra State Board", "Maths", 2025, "Class 8th Mathematics Term II Board Pattern", 90, 50),
        PYQPaper("7_msb_all_2025", "7th", "Maharashtra State Board", "Science", 2025, "Class 7th General Science Second Semester Review", 90, 50),
        PYQPaper("6_msb_all_2025", "6th", "Maharashtra State Board", "All Subjects", 2025, "Class 6th Comprehensive Term II Examination", 120, 100),
        PYQPaper("5_msb_all_2025", "5th", "Maharashtra State Board", "All Subjects", 2025, "Class 5th Annual Integrated Scholarship Syllabus", 120, 100)
    )

    val StudyMaterials = listOf(
        StudyMaterial(
            id = "10_msb_sci_tb",
            className = "10th",
            board = "Maharashtra State Board",
            subject = "Science",
            title = "Class 10th Science & Technology Part-1",
            type = "Textbook",
            contentPreview = listOf(
                ChapterContent(1, "Gravitation", listOf(
                    "Page 1:\nGravitation is a natural phenomenon by which all things with mass or energy are brought toward one another. Sir Isaac Newton discovered gravitation in 1687. Keplar's laws have 3 crucial planetary rules.",
                    "Page 2:\nCentripetal force is a force that acts on a body moving in a circular path and is directed toward the center. Acceleration due to gravity (g) = GM/R². At the poles, g is 9.83 m/s², while at equator, g is 9.78 m/s²."
                )),
                ChapterContent(2, "Periodic Classification of Elements", listOf(
                    "Page 1:\nElements are made up of same-type atoms. Döbereiner's Triads arranged elements with similar chemical properties in groups of three. Newlands' Law of Octaves states every eighth element has similar properties.",
                    "Page 2:\nMendeleev arranged elements by atomic mass, while Henry Moseley discovered the Modern Periodic Table arranged by atomic numbers (Z). Horizontal rows are called Periods; vertical columns are called Groups."
                ))
            )
        ),
        StudyMaterial(
            id = "10_msb_math_tb",
            className = "10th",
            board = "Maharashtra State Board",
            subject = "Maths",
            title = "Class 10th Mathematics Part-I",
            type = "Textbook",
            contentPreview = listOf(
                ChapterContent(1, "Linear Equations in Two Variables", listOf(
                    "Page 1:\nAn equation which contains two variables and the degree of each term containing variables is one, is called a linear equation in two variables. E.g., ax + by + c = 0.",
                    "Page 2:\nSimultaneous equations can be solved using elimination, substitution, graphical, and Cramer's Rule methods. Gabriel Cramer developed Cramer's Rule using determinants of Dx, Dy, and D."
                )),
                ChapterContent(2, "Quadratic Equations", listOf(
                    "Page 1:\nThe equation ax² + bx + c = 0, where a, b, c are real numbers and a cannot be zero, is a quadratic equation. Roots can be found using factorization or formula: x = (-b ± √(b² - 4ac)) / 2a.",
                    "Page 2:\nThe discriminant is denoted as Δ = b² - 4ac. If Δ = 0, roots are real and equal. If Δ > 0, roots are real and unequal. If Δ < 0, roots are not real."
                ))
            )
        ),
        StudyMaterial(
            id = "10_cbse_sci_tb",
            className = "10th",
            board = "CBSE",
            subject = "Science",
            title = "NCERT Class X Science Textbook notes",
            type = "Revision Notes",
            contentPreview = listOf(
                ChapterContent(1, "Chemical Reactions and Equations", listOf(
                    "Page 1:\nChemical reaction is a process where new substances with new properties are formed. Left side: Reactants. Right side: Products. E.g., 2H₂ + O₂ -> 2H₂O.",
                    "Page 2:\nTypes of reactions:\n1. Combination\n2. Decomposition\n3. Displacement\n4. Double Displacement\n5. Redox. Corrosion and rancidity are everyday oxidation processes."
                )),
                ChapterContent(2, "Acids, Bases and Salts", listOf(
                    "Page 1:\nAcids are sour, turn blue litmus red, liberate H⁺ in solution. Bases are bitter, feel soapy, turn red litmus blue, liberate OH⁻. pH measures H⁺ concentration from 0 to 14.",
                    "Page 2:\nNeutralization: Acid + Base -> Salt + Water. Important salts include Bleaching Powder, Baking Soda, Washing Soda, and Plaster of Paris (CaSO₄.1/2H₂O)."
                ))
            )
        )
    )
}

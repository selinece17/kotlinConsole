/**
A fully functional console-based Hangman game written in Kotlin.
This project demonstrates file handling, input validation, game loops, collections, and persistent high score tracking.
Created Feb 16,2026
*/
import java.io.File
import kotlin.random.Random

/**
 * Entry point of the Hangman game.
 *
 * - Greets the player
 * - Gets player name
 * - Runs game rounds in a loop
 * - Tracks total score
 * - Updates high score file at the end
 */
fun main() {
    println(" Welcome to Hangman!")
    print("Enter your name: ")
    // If user enters blank/null name, default to "Player"
    val playerName = readLine().takeUnless { it.isNullOrBlank() } ?: "Player"
    // Total cumulative score across rounds
    var score = 0
    var replay: String
    // Main game loop (runs at least once
    do {
        // Play one full round and add returned points
        val roundScore = playGame()
        score += roundScore
        println("\nCurrent Score: $score")
        // Input validation loop for repla
        while (true) {
            print("\nDo you want to play again? (y/n): ")
            // Convert input to lowercase to allow Y/y consistency
            replay = readLine()?.lowercase() ?: ""
            if (replay == "y" || replay == "n") break
            println("Invalid input. Please enter 'y' or 'n'.")
        }

    } while (replay == "y")

    // After game ends, update persistent high score file
    updateHighScore(playerName, score)
}

/**
 * Runs one full round of Hangman.
 *
 * Responsibilities:
 * - Lets player choose category
 * - Loads words from selected file
 * - Lets player choose difficulty
 * - Handles guessing logic
 * - Displays hangman stages
 * - Returns points earned in this round
 *
 * @return Int points earned in the round (0 if lost)
 */
fun playGame(): Int {
    var filename: String
    // ---------------- CATEGORY SELECTION ----------------
    // Loop until valid category is chosen
    while (true) {
        println("\nChoose a category:")
        println("1 - Science")
        println("2 - Countries")
        println("3 - Tech")

        print("Enter choice: ")
        val categoryChoice = readLine()

        when (categoryChoice) {
            "1" -> {
                filename = "science.txt"
                break
            }
            "2" -> {
                filename = "countries.txt"
                break
            }
            "3" -> {
                filename = "tech.txt"
                break
            }
            else -> println("Invalid input. Please choose 1, 2, or 3.")
        }
    }

    // ---------------- LOAD WORDS ----------------
    // Try-catch prevents crash if file does not exist
    val words = try {
        File(filename).readLines().filter { it.isNotBlank() }
    } catch (e: Exception) {
        println("File not found.")
        return 0
    }
    // Extra safety check in case file exists but is empty
    if (words.isEmpty()) {
        println("No words found.")
        return 0
    }
    // Randomly select secret word and normalize to lowercase
    val secretWord = words.random().lowercase()

    // -------- DIFFICULTY VALIDATION --------
    var attemptsLeft: Int
    var maxAttempts: Int

    while (true) {
        println("\nChoose difficulty:")
        println("1 - Easy (6 tries)")
        println("2 - Hard (3 tries)")

        print("Enter choice: ")
        val difficultyChoice = readLine()

        when (difficultyChoice) {
            "1" -> {
                attemptsLeft = 6
                maxAttempts = 6
                break
            }
            "2" -> {
                attemptsLeft = 3
                maxAttempts = 3
                break
            }
            else -> println(" Invalid input. Please choose 1 or 2.")
        }
    }

    // Stores unique guessed letters (prevents duplicates automatically)
    val guessedLetters = mutableSetOf<Char>()

    // ---------------- MAIN GAME LOOP ----------------
    while (attemptsLeft > 0) {
        // Draw hangman based on current state
        printHangman(attemptsLeft, maxAttempts)
        // Display partially revealed word
        println("\nWord: ${displayWord(secretWord, guessedLetters)}")
        println("Attempts left: $attemptsLeft")

        print("Enter a letter: ")
        val input = readLine()?.lowercase()

        // -------- INPUT VALIDATION --------
        // Must be:
        // - Not null
        // - Exactly 1 character
        // - Alphabetical
        if (input.isNullOrBlank() || input.length != 1 || !input[0].isLetter()) {
            println("Invalid input.Enter a single letter.\n")
            continue
        }
        val guess = input[0]

        // Prevent penalizing duplicate guesses
        if (guess in guessedLetters) {
            println("Already guessed!\n")
            continue
        }
        guessedLetters.add(guess)

        // If guess not in word, reduce attempts
        if (guess !in secretWord) {
            attemptsLeft--
            println("Wrong guess!\n")
        }

        // -------- WIN CONDITION --------
        // Check if every letter in secretWord has been guessed
        if (secretWord.all { it in guessedLetters }) {
            // Score is based on remaining attempts (reward efficiency)
            val points = attemptsLeft * 10
            println("\n You won! The word was: $secretWord")
            println("You earned $points points!")
            return points
        }
    }
    // -------- LOSS CONDITION --------
    printHangman(0, maxAttempts)
    println("\n Game Over! The word was: $secretWord")
    return 0
}

/**
 * Updates and stores player high scores in a file.
 *
 * Behavior:
 * - Reads existing scores from highscore.txt
 * - Updates current player's total score
 * - Determines global top player
 * - Writes updated scores back to file
 *
 * @param playerName Name of the player
 * @param score Score earned in this session
 */
fun updateHighScore(playerName: String, score: Int) {
    val file = File("highscore.txt")
    if (!file.exists()) {
        file.createNewFile()
    }
    val scores = mutableMapOf<String, Int>()

    // ---- READ EXISTING SCORES ----
    file.forEachLine { line ->
        val parts = line.split(",")
        if (parts.size == 2) {
            val name = parts[0]
            val playerScore = parts[1].toIntOrNull() ?: 0
            scores[name] = playerScore
        }
    }

    // ---- UPDATE CURRENT PLAYER ----
    val previousScore = scores[playerName] ?: 0
    val newTotal = previousScore + score
    scores[playerName] = newTotal

    println("\nYour Total Score: $newTotal")
    // Find highest scoring player
    val topEntry = scores.maxByOrNull { it.value }
    val topPlayer: String
    val topScore: Int

    if (topEntry != null) {
        topPlayer = topEntry.key
        topScore = topEntry.value
    } else {
        topPlayer = playerName
        topScore = newTotal
    }

    println(" Current Global High Score: $topScore by $topPlayer")

    // ---- WRITE BACK TO FILE ----
    file.printWriter().use { out ->
        scores.forEach { (name, playerScore) ->
            out.println("$name,$playerScore")
        }
    }

    if (playerName == topPlayer) {
        println("You are currently the top player!")
    }
}

/**
 * Returns the word formatted for display.
 *
 * Letters that have been guessed are shown.
 * Remaining letters are replaced with underscores.
 *
 * Example:
 * Word = "kotlin"
 * Guessed = {k, t}
 * Output = "k _ t _ _ _"
 *
 * @param word The secret word
 * @param guessed Set of guessed letters
 * @return Formatted display string
 */
fun displayWord(word: String, guessed: Set<Char>): String {
    return word.map {
        if (it in guessed) it else '_'
    }.joinToString(" ")
}

/**
 * Prints the hangman ASCII art depending on remaining attempts.
 *
 * The drawing progresses as wrong guesses increase.
 *
 * @param attemptsLeft Remaining guesses
 * @param maxAttempts Total allowed guesses
 */
fun printHangman(attemptsLeft: Int, maxAttempts: Int) {
    val stages = listOf(
        """
           ------
           |    |
           |
           |
           |
           |
        """,
        """
           ------
           |    |
           |    O
           |
           |
           |
        """,
        """
           ------
           |    |
           |    O
           |    |
           |
           |
        """,
        """
           ------
           |    |
           |    O
           |   /|
           |
           |
        """,
        """
           ------
           |    |
           |    O
           |   /|\
           |
           |
        """,
        """
           ------
           |    |
           |    O
           |   /|\
           |   /
           |
        """,
        """
           ------
           |    |
           |    O
           |   /|\
           |   / \
           |
        """
    )

    // Total body parts = 6
    // Determine which stage to show
    val wrongGuesses = maxAttempts - attemptsLeft
    val partsRemovedPerWrong = 6 / maxAttempts
    val stageIndex = 6 - (wrongGuesses * partsRemovedPerWrong)
    println(stages[stageIndex.coerceIn(0, 6)])
}


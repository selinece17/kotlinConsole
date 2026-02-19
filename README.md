# 🎮 Kotlin Hangman Game

A fully functional console-based **Hangman game** written in Kotlin.\
This project demonstrates file handling, input validation, game loops,
collections, and persistent high score tracking.

------------------------------------------------------------------------

## 📌 Features

-   👤 Player name input (defaults to "Player" if blank)
-   🔁 Multiple game rounds with score accumulation
-   📂 Category selection:
    -   Science
    -   Countries
    -   Tech
-   🎯 Difficulty selection:
    -   Easy (6 attempts)
    -   Hard (3 attempts)
-   🔤 Letter-by-letter guessing with validation
-   🏆 Persistent high score tracking using `highscore.txt`
-   📁 Word lists loaded from external `.txt` files

------------------------------------------------------------------------

## 🛠️ Technologies Used

-   **Kotlin**
-   File I/O (`java.io.File`)
-   Collections (`MutableSet`, `MutableMap`)
-   Exception handling
-   Console input/output

------------------------------------------------------------------------

## ▶️ How to Run

1.  Make sure Kotlin is installed.
2.  Place the word list files (`science.txt`, `countries.txt`,
    `tech.txt`) in the project root.
3.  Run the program from your IDE or terminal.

------------------------------------------------------------------------

## 🎮 How to Play

1.  Enter your name.
2.  Choose a category.
3.  Choose a difficulty.
4.  Guess letters one at a time.
5.  Win by guessing all letters before attempts run out.
6.  Points are calculated as:
    Points = Remaining Attempts × 10
7.  After finishing, you can choose to replay.

------------------------------------------------------------------------

## 🏆 High Score System

-   Scores are stored in `highscore.txt`.
-   Player scores accumulate across sessions.
-   The program:
    -   Reads existing scores
    -   Updates the current player's total
    -   Determines the global top player
    -   Rewrites the file with updated values

File format:

    PlayerName,Score

Example:

    Alice,120
    Bob,80

------------------------------------------------------------------------

## 🔍 Input Validation

The game includes strong validation for:

-   Category selection (must be 1--3)
-   Difficulty selection (must be 1 or 2)
-   Replay input (must be `y` or `n`)
-   Letter guesses:
    -   Must be a single alphabetical character
    -   Duplicate guesses are ignored
    -   Invalid input does not reduce attempts

------------------------------------------------------------------------

## 📜 License

This project is for educational purposes.

package com.mansu.universe
import java.time.LocalDateTime

/** EDIT ME: all personal text and dates live here. */
object Config {
    const val NAME = "Mansu"
    val BIRTH: LocalDateTime = LocalDateTime.of(2003, 10, 25, 0, 0)
    val ANNIV: LocalDateTime = LocalDateTime.of(2022, 2, 9, 0, 0)
    const val GIFT_TITLE = "To My Favorite Person, Mansu ♡"
    const val GIFT_LETTER = "Mansu,\n\nI wanted to give you more than a message on a screen, so I built you a little universe.\n\nThank you for being you: your kindness, your laughter, the way you make ordinary days softer. I hope this year is gentle with you and generous with its joy.\n\n(Edit this letter in Config.kt.) ♡"
    val OPEN_WHEN = listOf(
        "When you miss me" to "Distance doesn't make love smaller. Close your eyes, remember how warm it feels when we're together, and imagine me holding you close. I'm thinking of you, even now.",
        "When you're angry at me" to "I'm not perfect, and I will make mistakes. You're allowed to be upset, and I'll listen when you're ready to talk. Take the time you need. I won't pressure you to forgive me. I only hope we can stay honest and patient with each other.",
        "When you feel sad or low" to "You don't have to be happy all the time. Your feelings matter, even the heavy ones. You deserve kindness, comfort and rest. Be gentle with yourself today. I'm on your side.",
        "When you can't sleep" to "Breathe in slowly, and out. Picture a quiet sky of soft stars drifting past one by one. Nothing needs solving tonight. Let the universe hold you. Goodnight, my love."
    )
    const val FINAL_LETTER = "Dear Mansu,\n\nHappy 23rd birthday.\n\nI made this app because I wanted you to feel celebrated in a way that's a little different. I hope it made you smile.\n\nI admire your heart, and I'm grateful for what you bring into my life. I hope this year brings you happiness, peace, growth, and the courage to chase your dreams. Whatever you choose, I'll be cheering for you.\n\nNo matter how vast the universe is, you will always have a special place in mine. Happy Birthday, my love. ♡\n\nMade with all my heart, for Mansu. ♡\nOctober 25, 2026"
}

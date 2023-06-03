package com.dart69.plannerokapp.profile.presentation

import com.dart69.plannerokapp.R
import java.util.Calendar
import javax.inject.Inject

interface ZodiacSignProvider {

    /**
     * @return zodiac sign name string resource
     * */
    fun provide(epoch: Long): Int

    class Implementation @Inject constructor() : ZodiacSignProvider {
        override fun provide(epoch: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = epoch
            return provide(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }

        private fun provide(month: Int, date: Int): Int = when {
            (month == 1 && date >= 20 || month == 2 && date <= 19)
            -> R.string.aquarius

            (month == 2 || month == 3 && date <= 20)
            -> R.string.pisces

            (month == 12 && date >= 22 || month == 1)
            -> R.string.capricorn

            (month == 11 && date >= 23 || month == 12)
            -> R.string.sagittarius

            (month == 10 && date >= 23 || month == 11)
            -> R.string.scorpio

            (month == 9 && date >= 23 || month == 10)
            -> R.string.libra

            (month == 8 && date >= 23 || month == 9)
            -> R.string.virgo

            (month == 7 && date >= 23 || month == 8)
            -> R.string.leo

            (month == 6 && date >= 21 || month == 7)
            -> R.string.cancer

            (month == 5 && date >= 21 || month == 6)
            -> R.string.gemini

            (month == 4 && date >= 21 || month == 5)
            -> R.string.taurus

            (month == 3 || month == 4)
            -> R.string.aries

            else -> error("Unknown month or date")
        }
    }
}
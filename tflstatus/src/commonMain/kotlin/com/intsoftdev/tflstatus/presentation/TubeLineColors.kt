package com.intsoftdev.tflstatus.presentation

import androidx.compose.ui.graphics.Color
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.BAKERLOO_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.CENTRAL_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.CIRCLE_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.DISTRICT_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.ELIZABETH_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.HAMMERSMITH_CITY_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.JUBILEE_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.LONDON_OVERGROUND_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.METROPOLITAN_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.NORTHERN_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.PICCADILLY_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.VICTORIA_ID
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.WATERLOO_CITY_ID

/**
 * Centralized colors for London Tube lines following TfL's official color scheme
 */
object TubeLineColors {

    // Text colors
    object TextColors {
        val White = Color.White
        val Black = Color.Black
    }

    // Background colors
    object BackgroundColors {
        val SampleBackground = Color(0xFF1A1A2E) // Dark blue background used in samples
        val ContainerBackground = Color(0xff384267)
    }

    // Tube line background colors
    object LineColors {
        val Bakerloo = Color(0xFFB36305)
        val Circle = Color(0xFFFFD300)
        val Central = Color(0xFFE32017)
        val District = Color(0xFF00782A)
        val ElizabethLine = Color(0xFF6950A1)
        val HammersmithCity = Color(0xFFF3A9BB)
        val Jubilee = Color(0xFF7A7A7A)
        val Metropolitan = Color(0xFF9B0056)
        val Northern = Color(0xFF000000)
        val Piccadilly = Color(0xFF003688)
        val Victoria = Color(0xFF0098D4)
        val WaterlooCity = Color(0xFF95CDBA)
        val LondonOverground = Color(0xFFE86A10)
        val Default = Color(0xFF666666)
    }

    /**
     * Data class to represent a line's color scheme
     */
    data class LineColorScheme(
        val backgroundColor: Color,
        val textColor: Color
    )

    /**
     * Get the appropriate color scheme for a tube line based on its name
     */
    fun getLineColorScheme(lineName: String): LineColorScheme {
        return when (lineName.lowercase()) {
            BAKERLOO_ID -> LineColorScheme(LineColors.Bakerloo, TextColors.White)
            CIRCLE_ID -> LineColorScheme(LineColors.Circle, TextColors.Black)
            CENTRAL_ID -> LineColorScheme(LineColors.Central, TextColors.White)
            DISTRICT_ID -> LineColorScheme(LineColors.District, TextColors.White)
            ELIZABETH_ID, "elizabeth line" -> LineColorScheme(
                LineColors.ElizabethLine,
                TextColors.White
            )

            HAMMERSMITH_CITY_ID, "hammersmith & city" -> LineColorScheme(
                LineColors.HammersmithCity,
                TextColors.Black
            )
            JUBILEE_ID -> LineColorScheme(LineColors.Jubilee, TextColors.White)
            METROPOLITAN_ID -> LineColorScheme(LineColors.Metropolitan, TextColors.White)
            NORTHERN_ID -> LineColorScheme(LineColors.Northern, TextColors.White)
            PICCADILLY_ID -> LineColorScheme(LineColors.Piccadilly, TextColors.White)
            VICTORIA_ID -> LineColorScheme(LineColors.Victoria, TextColors.White)
            WATERLOO_CITY_ID, "waterloo & city" -> LineColorScheme(
                LineColors.WaterlooCity,
                TextColors.Black
            )

            LONDON_OVERGROUND_ID, "london overground" -> LineColorScheme(
                LineColors.LondonOverground,
                TextColors.White
            )
            else -> LineColorScheme(LineColors.Default, TextColors.White)
        }
    }
}
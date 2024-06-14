package com.chirathi.funmaths

import android.view.animation.Interpolator

// Custom Interpolator class implementing the Interpolator interface
class NewBounceInterpolator(amplitude: Double, frequency: Double) : Interpolator {
    // Properties to store the amplitude and frequency of the bouncing effect
    private var mAmplitude = 1.0
    private var mFrequency = 10.0

    // Initialization block to set the amplitude and frequency properties
    init {
        mAmplitude = amplitude
        mFrequency = frequency
    }

    // Override the getInterpolation method of the Interpolator interface
    override fun getInterpolation(time: Float): Float {
        // Calculate the interpolation value based on the given time
        return (-1 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 1).toFloat()
    }
}

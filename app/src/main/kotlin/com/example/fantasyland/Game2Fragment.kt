package com.example.fantasyland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class Game2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val metrics: WindowMetrics = windowManager.getCurrentWindowMetrics()
//
//        // Gets all excluding insets
//        val windowInsets = metrics.windowInsets
//        var insets: Insets = windowInsets.getInsetsIgnoringVisibility(
//            WindowInsets.Type.navigationBars()
//                    or WindowInsets.Type.displayCutout()
//        )
//
//        var insetsWidth: Int = insets.right + insets.left
//        var insetsHeight: Int = insets.top + insets.bottom
//
//        // Legacy size that Display#getSize reports
//        val bounds: Rect = metrics.bounds
//        val legacySize: Size = Size(
//            bounds.width() - insetsWidth,
//            bounds.height() - insetsHeight
//        )
    }
}
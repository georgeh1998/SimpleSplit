package com.github.georgeh1998.simplesplit.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Typography定義 (Material3準拠 + 割り勘アプリ向け微調整)
val AppTypography =
    Typography(
        // アプリタイトル
        headlineLarge =
            TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 34.sp,
                letterSpacing = 0.sp,
            ),
        // セクションタイトル（例: "Create New Group"）
        headlineMedium =
            TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
        // 小見出し（例: "Members", "History"）
        titleLarge =
            TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
        // 通常テキスト（ボタン・本文など）
        bodyLarge =
            TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.sp,
            ),
        // サブテキスト（補助説明など）
        bodyMedium =
            TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
            ),
        // 小さなラベルやタブ文字
        labelSmall =
            TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.1.sp,
            ),
    )

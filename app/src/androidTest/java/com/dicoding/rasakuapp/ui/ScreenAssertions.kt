package com.dicoding.jetreward

import androidx.navigation.NavController
import org.junit.*

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    Assert.assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}
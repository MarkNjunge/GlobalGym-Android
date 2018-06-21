package com.marknkamau.globalgym.data.models

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

data class Gym(val gymId: String,
               val name: String,
               val logo: String,
               val phone: String,
               val website: String,
               val openTime: String,
               val closeTime: String,
               val country: String,
               val city: String,
               val images: List<String>,
               val cords: Cords)

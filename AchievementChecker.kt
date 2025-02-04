object AchievementChecker {
    private val achievements = mapOf(
        1000 to "First Steps!",
        5000 to "5K Steps!",
        10000 to "10K Master!"
    )

    fun checkAchievements(context: Context, steps: Int) {
        val sharedPref = context.getSharedPreferences("Achievements", MODE_PRIVATE)
        
        achievements.forEach { (threshold, message) ->
            if (steps >= threshold && !sharedPref.getBoolean("achievement_$threshold", false)) {
                showAchievementNotification(context, message)
                sharedPref.edit().putBoolean("achievement_$threshold", true).apply()
            }
        }
    }

    private fun showAchievementNotification(context: Context, message: String) {
        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setContentTitle("Achievement Unlocked!")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_trophy)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(Random.nextInt(), notification)
    }
}

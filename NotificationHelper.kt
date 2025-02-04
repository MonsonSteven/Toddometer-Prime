object NotificationHelper {
    private const val CHANNEL_ID = "step_channel"

    fun getStepNotification(context: Context, steps: Int): Notification {
        createNotificationChannel(context)
        
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Fitness Tracker")
            .setContentText("Steps: $steps")
            .setSmallIcon(R.drawable.ic_walk)
            .setOngoing(true)
            .build()
    }

    fun updateNotification(context: Context, steps: Int) {
        val notification = getStepNotification(context, steps)
        NotificationManagerCompat.from(context).notify(1, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Step Counter",
                NotificationManager.IMPORTANCE_LOW
            )
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}

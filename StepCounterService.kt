class StepCounterService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var steps = 0
    private var previousSteps = 0
    
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        registerSensor()
        startForeground(1, NotificationHelper.getStepNotification(this, 0))
    }

    private fun registerSensor() {
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            steps++
            updateData()
        }
    }

    private fun updateData() {
        val sharedPref = getSharedPreferences("FitnessData", MODE_PRIVATE)
        val dailySteps = sharedPref.getInt("dailySteps", 0) + (steps - previousSteps)
        previousSteps = steps
        
        with(sharedPref.edit()) {
            putInt("totalSteps", sharedPref.getInt("totalSteps", 0) + (steps - previousSteps))
            putInt("dailySteps", dailySteps)
            apply()
        }

        // Update notification
        NotificationHelper.updateNotification(this, dailySteps)
        
        // Check achievements
        AchievementChecker.checkAchievements(this, dailySteps)
        
        // Send broadcast to update UI
        sendBroadcast(Intent("STEP_UPDATE"))
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

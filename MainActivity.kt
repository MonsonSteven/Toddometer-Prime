class MainActivity : AppCompatActivity() {
    private lateinit var stepTextView: TextView
    private val updateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        stepTextView = findViewById(R.id.stepTextView)
        startService(Intent(this, StepCounterService::class.java))
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(updateReceiver, IntentFilter("STEP_UPDATE"))
        updateUI()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(updateReceiver)
    }

    private fun updateUI() {
        val sharedPref = getSharedPreferences("FitnessData", MODE_PRIVATE)
        val dailySteps = sharedPref.getInt("dailySteps", 0)
        val calories = (dailySteps * 0.04).toInt() // Simple calculation
        val distance = (dailySteps * 0.000762).toInt() // 0.762 meters per step
        
        stepTextView.text = "Steps: $dailySteps\nCalories: $calories kcal\nDistance: $distance km"
    }
}

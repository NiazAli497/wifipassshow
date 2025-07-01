package wifi.key.show.master.show.all.wifi.password.ActivityNew

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import wifi.key.show.master.show.all.wifi.password.databinding.ActivityGetstartBinding

class GetStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetstartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGetstartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textStart.setOnClickListener{
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finishAffinity()
    }
}
package Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityInitialLoginPageBinding

class InitialLoginPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInitialLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialLoginPageBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Klik tombol Login
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, MyLogin::class.java)
            startActivity(intent)
        }

        // Klik tombol Register
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
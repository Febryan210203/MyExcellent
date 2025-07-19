package Profile

import Activity.EditProfileActivity
import Activity.MyLogin
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loadUserData()


        // Tombol Edit Profile
        binding.btnEditProfil.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    // Load data user dari SharedPreferences
    private fun loadUserData() {
        val sharedPref = requireActivity().getSharedPreferences("MyAppPrefs", AppCompatActivity.MODE_PRIVATE)
        val nama = sharedPref.getString("nama", "Nama belum diisi")
        val email = sharedPref.getString("email", "Email belum diisi")
        val alamat = sharedPref.getString("alamat", "Alamat belum diisi")
        val noHp = sharedPref.getString("nohp", "No telepon belum diisi")

        binding.txtNama.text = nama
        binding.txtEmail.text = email
        binding.txtAlamat.text = alamat
        binding.txtPhone.text = noHp


    }

    override fun onResume() {
        super.onResume()
        loadUserData() // Perbarui data saat kembali dari EditProfileActivity

    }


    private fun showLogoutDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()

        val cancelButton = dialogView.findViewById<Button>(R.id.btnCancel)
        val confirmButton = dialogView.findViewById<Button>(R.id.btnConfirm)

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        confirmButton.setOnClickListener {
            Toast.makeText(requireContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MyLogin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

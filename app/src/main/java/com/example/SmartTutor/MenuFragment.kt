package com.example.SmartTutor

import Adapter.MenuAdapter
import Domain.MenuItem
import FiturMenu.KonsultasiFragment
import FiturMenu.PengaturanFragment
import FiturMenu.RiwayatBokingFragment
import FiturMenu.UlasanTutorFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.SmartTutor.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val menuList = listOf(
        MenuItem(R.drawable.konsultasi, "Konsultasi"),
        MenuItem(R.drawable.boking, "Riwayat Booking"),
        MenuItem(R.drawable.ulasan, "Ulasan Tutor"),
        MenuItem(R.drawable.settings, "Pengaturan")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerMenu.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerMenu.adapter = MenuAdapter(menuList) { item ->
            val targetFragment = when (item.title) {
                "Konsultasi" -> KonsultasiFragment()
                "Riwayat Booking" -> RiwayatBokingFragment()
                "Ulasan Tutor" -> UlasanTutorFragment()
                "Pengaturan" -> PengaturanFragment()
                else -> null
            }

            targetFragment?.let {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, it) // Sesuaikan ID ini dengan layoutmu
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

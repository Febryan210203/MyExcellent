//package Adapter
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import com.example.SmartTutor.DisetujuiFragment
//import com.example.SmartTutor.MenungguFragment
//
//class ViewPagerAdapter(historyFragment: FragmentActivity) : FragmentStateAdapter(historyFragment) {
//    override fun getItemCount(): Int {
//        return 1
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return when(position) {
//            0 -> DisetujuiFragment()
//            1 -> MenungguFragment()
//            else -> throw IllegalArgumentException("Invalid position")
//        }
//    }
//}
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.akso.realwear.app.app.attachments.AttachmentsFragment
import com.akso.realwear.app.app.equipment.EquipmentFragment
import com.akso.realwear.app.app.operations.OperationsFragment
import com.akso.realwear.app.app.orderDetails.OrderDetailsFragment

class WorkOrderDetailsFragmentAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 4;
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return OrderDetailsFragment()
            }
            1 -> {
                return OperationsFragment()
            }
            2 -> {
                return EquipmentFragment()
            }
            3 -> {
                return AttachmentsFragment()
            }
            else -> {
                return OrderDetailsFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "1"
            }
            1 -> {
                return "Operations"
            }
            2 -> {
                return "Equipment"
            }
            3 -> {
                return "Attachments"
            }
        }
        return super.getPageTitle(position)
    }
}
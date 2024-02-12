package lydia.yuan.dajavu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import lydia.yuan.dajavu.databinding.FragmentAddressAutoCompleteBinding

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AddressAutoCompleteFragment : Fragment() {


    private var dummyButton: Button? = null
    private var fullscreenContent: View? = null
    private var fullscreenContentControls: View? = null

    private var _binding: FragmentAddressAutoCompleteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddressAutoCompleteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

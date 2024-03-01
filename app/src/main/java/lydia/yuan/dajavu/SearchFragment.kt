package lydia.yuan.dajavu

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import kotlinx.coroutines.launch
import lydia.yuan.dajavu.databinding.FragmentSearchBinding
import lydia.yuan.dajavu.viewmodel.PokemonViewModel


class SearchFragment : Fragment() {

    private lateinit var pokemonViewModel: PokemonViewModel
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding
    private var searchBar: SearchBar? = null
    private var searchView: SearchView? = null
    private var tvLikeButtonStatus: TextView? = null
    private var ibLike: ImageButton? = null
    private var btnGoToAnotherFragment: Button? = null
    private var goToPaginationButton: Button? = null
    private var btnGoToTokenTestScreen: Button? = null

    private var adapter = PokemonAdapter(emptyList())

    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val searchDelay: Long = 500

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pokemonViewModel = ViewModelProvider(
            this,
            PokemonViewModel.Factory
        )[PokemonViewModel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        searchBar = binding?.searchBar
        searchView = binding?.searchView
        ibLike = binding?.ibLike
        tvLikeButtonStatus = binding?.tvLikeButtonStatus
        btnGoToAnotherFragment = binding?.btnGoToAnotherFragment
        goToPaginationButton = binding?.btnGoToPaginationScreen
        btnGoToTokenTestScreen = binding?.btnGoToTokenTestScreen

        ibLike?.setOnClickListener {
            pokemonViewModel.throttledToggleLikeButtonStatus(250)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pokemonViewModel.userLikeThis.collect {
                if (it) {
                    ibLike?.setImageResource(R.drawable.heart_filled)
                } else {
                    ibLike?.setImageResource(R.drawable.heart)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pokemonViewModel.userLikeThisText.collect {
                tvLikeButtonStatus?.text = it
            }
        }

        btnGoToAnotherFragment?.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment_container, AddressAutoCompleteFragment())
            transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
            transaction.commit()
        }

        goToPaginationButton?.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment_container, PaginationScreenFragment())
            transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
            transaction.commit()
        }

        btnGoToTokenTestScreen?.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment_container, TokenTestFragment())
            transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
            transaction.commit()
        }

        searchView?.setupWithSearchBar(searchBar)

        // search on throttling
        searchView?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that characters within `s` are about to be replaced with new text with a length of `after`.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that somewhere within `s`, the characters between `start` and `start + before` are about to be replaced with new text with a length of `count`.
                // You can perform actions here when the text changes.
            }

            override fun afterTextChanged(s: Editable?) {
                // Remove any existing callbacks to avoid multiple calls
                searchRunnable?.let { searchHandler.removeCallbacks(it) }

                // Post a new runnable with a delay
                searchRunnable = Runnable {
                    performSearch(s.toString())
                }

                searchHandler.postDelayed(searchRunnable!!, searchDelay)
            }
        })

        searchView?.editText?.setOnEditorActionListener { _, _, _ ->
            searchBar?.setText(searchView?.text.toString())
            performSearch(searchView?.text.toString())
            false
        }

        return binding?.root
    }

    private fun performSearch(query: String) {
        pokemonViewModel.getEggGroup(query)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBar = binding?.searchBar

        // Observe the pokemon data and update the UI
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            pokemonViewModel.pokemon.collect { pokemonList ->
                // Update your UI with the new data
                Log.d("SearchFragment", "onViewCreated: $pokemonList")
                adapter = PokemonAdapter(pokemonList)
                binding?.recyclerview?.adapter = adapter
            }
        }

        // Set up your RecyclerView with the adapter
        val recyclerView = binding?.recyclerview
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

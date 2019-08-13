package com.example.android.navigation

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TitleFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TitleFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TitleFragment : Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Timber.i("onAttach Called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate Called")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView Called")
        setHasOptionsMenu(true)
        val binding = FragmentTitleBinding.inflate(inflater, container, false)
        binding.playButton.setOnClickListener { view: View ->
            view.findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment())
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.i("onActivityCreated Called")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach Called")
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController()) || super.onOptionsItemSelected(item)
    }

}

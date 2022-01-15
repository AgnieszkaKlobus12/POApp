package com.example.poapp.view.tourist.route

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.poapp.R
import com.example.poapp.viewModel.NewRouteViewModel

class NewRouteFragment(private val routeId: Int?) : Fragment() {

    private val mViewModel: NewRouteViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (routeId != null)
            mViewModel.setRoute(routeId)
        else
            mViewModel.saveRoute()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            mViewModel.removeRoute()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.nav_host_fragment_activity_save_route,
                    RouteListFragment()
                )
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val official = view.findViewById<Button>(R.id.official_button)
        val own = view.findViewById<Button>(R.id.own_button)

        view.findViewById<Button>(R.id.add_mountain_pass_button).setOnClickListener {
            official.visibility = View.VISIBLE
            own.visibility = View.VISIBLE
        }

        view.findViewById<Button>(R.id.add_proof_button).setOnClickListener {
            if (mViewModel.getAllRouteSections().isEmpty()) {
                showAddRouteSectionDialog()
                return@setOnClickListener
            }
            Toast.makeText(activity, R.string.extension_point_label, Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.end_button).setOnClickListener {
            if (mViewModel.getAllRouteSections().isEmpty()) {
                showAddRouteSectionDialog()
                return@setOnClickListener
            }
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.nav_host_fragment_activity_save_route,
                    SaveRouteFragment()
                )
                ?.addToBackStack(null)
                ?.commit()
        }

        official.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.nav_host_fragment_activity_save_route,
                    PickMountainPassFragment(true, mViewModel.route.value!!.id, mViewModel.getLastSection())
                )
                ?.addToBackStack(null)
                ?.commit()
        }

        own.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.nav_host_fragment_activity_save_route,
                    PickMountainPassFragment(false, mViewModel.route.value!!.id, mViewModel.getLastSection())
                )
                ?.addToBackStack(null)
                ?.commit()
        }

        val list = view.findViewById<RecyclerView>(R.id.route_section_list)
        val allRouteSections = mViewModel.getAllRouteSections()
        list.adapter = RouteSectionAdapter(activity as Context, allRouteSections, mViewModel)
    }

    private fun showAddRouteSectionDialog() {
        val alertDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setNeutralButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                setTitle(R.string.alert)
                setMessage(R.string.no_passes_message)
            }
            builder.create()
        }
        alertDialog.show()
        return
    }
}
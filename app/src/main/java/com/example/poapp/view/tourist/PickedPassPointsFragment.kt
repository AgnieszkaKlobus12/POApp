package com.example.poapp.view.tourist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.poapp.R
import com.example.poapp.databinding.FragmentPickedPassPointsBinding
import com.example.poapp.viewModel.MountainPassListViewModel

class PickedPassPointsFragment : Fragment() {

    private var _binding: FragmentPickedPassPointsBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: MountainPassListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickedPassPointsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mViewModel.routeSection.value?.FKodcinekOficjalny != null) {
            val passOfficial = mViewModel.getOfficialPass(mViewModel.routeSection.value?.FKodcinekOficjalny!!)
            binding.passStart.text = mViewModel.getOfficialPoint(passOfficial.FKpunktPoczatkowy).nazwa
            binding.passEnd.text = mViewModel.getOfficialPoint(passOfficial.FKpunktKoncowy).nazwa
            if (passOfficial.FKpunktPosredni != null) {
                binding.passThrough.text = mViewModel.getOfficialPoint(passOfficial.FKpunktPosredni!!).nazwa
            }
            binding.passPoints.text = passOfficial.punkty.toString()
            binding.passName.text = passOfficial.nazwa
        } else {
            val passUser = mViewModel.getUserPass(mViewModel.routeSection.value?.FKodcinekWlasny!!)
            if (passUser.FKpunktPoczatkowyOficjalny != null) {
                binding.passStart.text = mViewModel.getOfficialPoint(passUser.FKpunktPoczatkowyOficjalny).nazwa
            } else {
                binding.passStart.text = mViewModel.getUserPoint(passUser.FKpunktPoczatkowyWlasny!!).nazwa
            }
            if (passUser.FKpunktKoncowyOficjalny != null) {
                binding.passEnd.text = mViewModel.getOfficialPoint(passUser.FKpunktKoncowyOficjalny).nazwa
            } else {
                binding.passEnd.text = mViewModel.getUserPoint(passUser.FKpunktKoncowyWlasny!!).nazwa
            }
            if (passUser.FKpunktPosredniOficjalny != null) {
                binding.passThrough.text = mViewModel.getOfficialPoint(passUser.FKpunktPosredniOficjalny).nazwa
            } else if (passUser.FKpunktPosredniWlasny != null) {
                binding.passThrough.text = mViewModel.getUserPoint(passUser.FKpunktPosredniWlasny).nazwa
            }
            binding.passPoints.text = passUser.punkty.toString()
            binding.passName.text = passUser.nazwa
        }
        binding.addRoutePassButton.setOnClickListener {
            if (binding.timeValue.text.toString().toInt() < 0) {
                // TODO dialog czas musi być większy niż zero
                return@setOnClickListener
            }
            mViewModel.routeSection.value!!.czasPrzejscia = binding.timeValue.text.toString().toInt()
            mViewModel.saveRouteSection()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.nav_host_fragment_activity_save_route,
                    NewRouteFragment(mViewModel.routeId)
                )
                ?.addToBackStack(null)
                ?.commit()
        }
    }

}
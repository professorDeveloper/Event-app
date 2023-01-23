package com.azamovhudstc.eventapp.screen.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.azamovhudstc.eventapp.R
import com.azamovhudstc.eventapp.broadcast.EventsReceiver
import com.azamovhudstc.eventapp.data.local.model.EventData
import com.azamovhudstc.eventapp.data.local.shared.LocalData
import com.azamovhudstc.eventapp.screen.adapter.EventsAdapter
import com.azamovhudstc.eventapp.screen.dialog.EventDialog
import com.azamovhudstc.eventapp.service.EventsService
import com.azamovhudstc.eventapp.viewmodel.EventScreenViewModel
import com.azamovhudstc.eventapp.viewmodel.impl.EventScreenViewModelImpl
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*
import kotlinx.android.synthetic.main.fragment_main_screen.*
import timber.log.Timber

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.fragment_main_screen) {
    private val viewModel: EventScreenViewModel by viewModels<EventScreenViewModelImpl>()
    private val adapter: EventsAdapter by lazy { EventsAdapter() }
    private val eventsReceiver: EventsReceiver by lazy { EventsReceiver() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        subscribeViewModel(viewModel)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeViewBinding()
        subscribeViewModel(viewModel)

    }


    private fun subscribeViewBinding()  {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        listEvents.adapter = adapter
        listEvents.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnEventStateChangeListener { eventId, eventState ->
            Timber.d("setOnEventStateChangeListener: $eventState")
            viewModel.onClickEventChange(eventId, eventState)
        }
    }
    @SuppressLint("FragmentLiveDataObserve")
    private fun subscribeViewModel(viewModel: EventScreenViewModel) = with(viewModel) {
        allEventsLiveData.observe(this@MainScreen, allEventsLiveDataObserver)
        allActionsLiveData.observe(this@MainScreen, allActionsLiveDataObserver)
        showDialogLiveData.observe(this@MainScreen, showDialogLiveDataObserver)
        showShareDialogLiveData.observe(this@MainScreen, showShareDialogLiveDataObserver)
        navigateRateScreenLiveData.observe(
            this@MainScreen,
            navigateRateScreenLiveDataObserver
        )
        quitAppLiveData.observe(this@MainScreen, quitAppLiveDataObserver)
    }
    private val allEventsLiveDataObserver = Observer<List<EventData>> { list ->
        adapter.submitList(list)
    }


    private val allActionsLiveDataObserver = Observer<ArrayList<String>> { allActions ->
        Timber.d("enabledActionsLiveDataObserver: $allActions")
        val intent = Intent(requireContext(), EventsService::class.java)
        intent.putStringArrayListExtra("enabledActions", allActions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            requireActivity().startForegroundService(intent)
        else requireActivity().startService(intent)
    }

    private val showDialogLiveDataObserver = Observer<Unit> {
        val dialog = EventDialog()
        var chek = 0
        dialog.setOnClickEnableAllEventsListener { viewModel.onClickEnableAllActionsButton() }
        dialog.setOnClickDisableAllEventsListener { viewModel.onClickDisableAllActionsButton() }
        dialog.setOnClickRateListener { viewModel.onClickRateButton() }
        dialog.setOnClickShareListener { viewModel.onClickShareButton() }

        dialog.setOnClickQuitAppListener { viewModel.onClickQuitAppButton() }
        dialog.setOnAudioListener {
            dialog.dismiss()
            var dialogs = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val inflater = LayoutInflater.from(requireContext())
            var view = inflater.inflate(R.layout.bottom_sheet_layout, null)
            when(LocalData.getAudioSetting()){
                0->{
                  var checked=  view.radioGroup.get(0) as RadioButton
                    checked.isChecked=true


                }
                1->{
                    var checked=  view.radioGroup.get(1) as RadioButton
                    checked.isChecked=true
                }
                else->{
                    var checked=  view.radioGroup.get(2) as RadioButton
                    checked.isChecked=true
                }
            }
            if (chek != 0) view.radioGroup.check(chek)
            view.save_settings_button.setOnClickListener {
                val checkedRadioButtonId = view.radioGroup.checkedRadioButtonId
                var radioButton: RadioButton = view.findViewById(checkedRadioButtonId)
                when(radioButton.text){
                    "Anna's voice"->{
                        LocalData.setAudioSetting(0)
                    }

                    "Jack's voice"->{
                        LocalData.setAudioSetting(1)
                    }
                    else->{
                        LocalData.setAudioSetting(2)
                    }
                }
                println("RadioButton:$checkedRadioButtonId")
                dialogs.dismiss()
            }
            dialogs.setContentView(view)
            dialogs.create()
            dialogs.show()        }
        dialog.show(childFragmentManager, "EventDialog")
    }

    private val showShareDialogLiveDataObserver = Observer<Int> { title ->
        val appPackageName = requireContext().packageName
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Let me recommend you this application: https://play.google.com/store/apps/details?id=$appPackageName"
        )
        intent.type = "text/plain"
        requireContext().startActivity(Intent.createChooser(intent, resources.getString(title)))
    }

    private val navigateRateScreenLiveDataObserver = Observer<Unit> {
        val appPackageName = requireContext().packageName
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse("market://details?id=$appPackageName")
        requireContext().startActivity(intent)
    }

    private val quitAppLiveDataObserver = Observer<Unit> {
        requireContext().stopService(
            Intent(
                requireContext(),
                EventsService::class.java
            )
        )
        requireActivity().finish()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuMore -> {
                viewModel.onClickButtonMore()
                true
            }
            else -> false
        }
    }


}
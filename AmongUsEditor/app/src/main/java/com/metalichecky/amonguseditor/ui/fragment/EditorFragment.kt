package com.metalichecky.amonguseditor.ui.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.flexbox.*
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.adapter.ItemsAdapter
import com.metalichecky.amonguseditor.model.ProgressData
import com.metalichecky.amonguseditor.model.item.Hat
import com.metalichecky.amonguseditor.model.item.Item
import com.metalichecky.amonguseditor.model.item.Pet
import com.metalichecky.amonguseditor.model.item.Skin
import com.metalichecky.amonguseditor.repo.GamePrefsRepo
import com.metalichecky.amonguseditor.repo.ItemsRepo
import com.metalichecky.amonguseditor.util.FileUtils
import com.metalichecky.amonguseditor.util.TypefaceUtils
import com.metalichecky.amonguseditor.util.setCustomTypeface
import com.metalichecky.amonguseditor.vm.EditorViewModel
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.fragment_editor.*
import timber.log.Timber
import java.io.File

class EditorFragment : BaseFragment() {
    val editorViewModel: EditorViewModel by activityViewModels()

    private lateinit var hatsAdapter: ItemsAdapter
    private lateinit var hatsLayoutManager: FlexboxLayoutManager
    var hatsAdapterListener: ItemsAdapter.Listener = object : ItemsAdapter.Listener {
        override fun onClicked(item: Item, position: Int) {
            if (item is Hat) {
                editorViewModel.selectHat(item)
            }
        }
    }
    private lateinit var skinsAdapter: ItemsAdapter
    private lateinit var skinsLayoutManager: FlexboxLayoutManager
    var skinsAdapterListener: ItemsAdapter.Listener = object : ItemsAdapter.Listener {
        override fun onClicked(item: Item, position: Int) {
            if (item is Skin) {
                editorViewModel.selectSkin(item)
            }
        }
    }
    private lateinit var petsAdapter: ItemsAdapter
    private lateinit var petsLayoutManager: FlexboxLayoutManager
    var petsAdapterListener: ItemsAdapter.Listener = object : ItemsAdapter.Listener {
        override fun onClicked(item: Item, position: Int) {
            if (item is Pet) {
                editorViewModel.selectPet(item)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvHatsTitle.setCustomTypeface(TypefaceUtils.TypeFaces.IN_YOUR_FACE)
        tvSkinsTitle.setCustomTypeface(TypefaceUtils.TypeFaces.IN_YOUR_FACE)
        tvPetsTitle.setCustomTypeface(TypefaceUtils.TypeFaces.IN_YOUR_FACE)

        setupHatsList()
        setupSkinsList()
        setupPetsList()
        editorViewModel.hats.observe(viewLifecycleOwner, Observer {
            it?.let { updateHatsList(it) }
        })
        editorViewModel.selectedHat.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d("current hat id ${it.id} name ${it.name}")
            }
        })
        editorViewModel.skins.observe(viewLifecycleOwner, Observer {
            it?.let { updateSkinsList(it) }
        })
        editorViewModel.selectedSkin.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d("current skin id ${it.id} name ${it.name}")
            }
        })
        editorViewModel.pets.observe(viewLifecycleOwner, Observer {
            it?.let { updatePetsList(it) }
        })
        editorViewModel.selectedPet.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d("current pet id ${it.id} name ${it.name}")
            }
        })
        editorViewModel.progress.observe(viewLifecycleOwner, Observer {
            it?.let{ showProgress(it) }
        })
    }

    private fun setupHatsList() {
        hatsAdapter = ItemsAdapter()
        hatsAdapter.listener = hatsAdapterListener
        hatsLayoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
        hatsLayoutManager.justifyContent = JustifyContent.CENTER
        hatsLayoutManager.alignItems = AlignItems.STRETCH

        rvHats.adapter = hatsAdapter
        rvHats.layoutManager = hatsLayoutManager
    }

    private fun setupSkinsList() {
        skinsAdapter = ItemsAdapter()
        skinsAdapter.listener = skinsAdapterListener
        skinsLayoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
        skinsLayoutManager.justifyContent = JustifyContent.CENTER
        skinsLayoutManager.alignItems = AlignItems.STRETCH

        rvSkins.adapter = skinsAdapter
        rvSkins.layoutManager = skinsLayoutManager
    }

    private fun setupPetsList() {
        petsAdapter = ItemsAdapter()
        petsAdapter.listener = petsAdapterListener
        petsLayoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
        petsLayoutManager.justifyContent = JustifyContent.CENTER
        petsLayoutManager.alignItems = AlignItems.STRETCH

        rvPets.adapter = petsAdapter
        rvPets.layoutManager = petsLayoutManager
    }

    private fun updateHatsList(hats: List<Hat>) {
        hatsAdapter.items = hats
    }

    private fun updateSkinsList(hats: List<Skin>) {
        skinsAdapter.items = hats
    }

    private fun updatePetsList(hats: List<Pet>) {
        petsAdapter.items = hats
    }

    private fun showProgress(progressData: ProgressData) {
        if (progressData.visible) {
            flProgressContainer?.visibility = View.VISIBLE
            pbProgress?.show()
            progressData.message?.stringResId?.let {
                val percentageStr = if (progressData.percentage != null) {
                    String.format("%.1f \\%", progressData.percentage)
                } else {
                    ""
                }
                tvProgressText?.setCustomTypeface(TypefaceUtils.TypeFaces.SKINNY_BOLD)
                tvProgressText?.setText("${getString(it)} $percentageStr")
            }
        } else {
            flProgressContainer?.visibility = View.GONE
            pbProgress?.hide()
        }
    }

    override fun onResume() {
        super.onResume()
        editorViewModel.updatePrefs()
    }

}
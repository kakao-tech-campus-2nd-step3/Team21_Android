package potatocake.katecam.everymoment.presentation.view.main

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.repository.MyInfoRepository
import potatocake.katecam.everymoment.databinding.FragmentSettingBinding
import potatocake.katecam.everymoment.extensions.CustomDialog
import potatocake.katecam.everymoment.extensions.CustomEditDialog
import potatocake.katecam.everymoment.extensions.GalleryUtil
import potatocake.katecam.everymoment.extensions.SendFilesUtil
import potatocake.katecam.everymoment.presentation.viewModel.SettingViewModel
import potatocake.katecam.everymoment.presentation.viewModel.SettingViewModelFactory

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var viewModel: SettingViewModel
    private val myInfoRepository = MyInfoRepository()

    private val galleryUtil = GalleryUtil(this)

    private lateinit var profileImageDialog: CustomDialog
    private lateinit var profileNameDialog: CustomDialog
    private lateinit var nameChangeDialog: CustomEditDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, SettingViewModelFactory(myInfoRepository)).get(SettingViewModel::class.java)
        viewModel.fetchMyInfo()
        observeMyInfo()
        setDialogs()

        binding.accountImage.setOnClickListener {
            profileImageDialog.show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        binding.cameraButton.setOnClickListener {
            profileImageDialog.show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        binding.editButton.setOnClickListener {
            profileNameDialog.show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        binding.autoNotificationToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.auto_notification_isChecked),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.auto_notification_isUnChecked),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.pushNotificationToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.push_notification_isChecked),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.push_notification_isUnChecked),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.timeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            var selectedTime = ""
            when (checkedId) {
                R.id.time15m -> {
                    selectedTime = resources.getString(R.string.time_15min)
                }

                R.id.time20m -> {
                    selectedTime = resources.getString(R.string.time_20min)
                }

                R.id.time25m -> {
                    selectedTime = resources.getString(R.string.time_25min)
                }

                R.id.time30m -> {
                    selectedTime = resources.getString(R.string.time_30min)
                }
            }
            Toast.makeText(
                requireContext(),
                getString(R.string.time_interval_text, selectedTime),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeMyInfo() {
        viewModel.myInfo.observe(viewLifecycleOwner) { myInformation ->
            if (myInformation != null) {
                binding.accountName.text = myInformation.nickname

                Glide.with(requireContext())
                    .load(myInformation.profileImageUrl)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.accountImage)
            }
        }
    }

    private fun setDialogs() {
        profileImageDialog =
            CustomDialog(
                resources.getString(R.string.profile_image_dialog),
                resources.getString(R.string.cancel),
                resources.getString(R.string.change),
                onPositiveClick = {
                    galleryUtil.openGallery(onImageSelected = {
                        addLocalImage(it)
                        updateProfileImg(it)
                        Toast.makeText(requireContext(), getString(R.string.feature_not_available), Toast.LENGTH_LONG).show()
                    })
                }).apply {
                isCancelable = false
            }

        profileNameDialog =
            CustomDialog(
                resources.getString(R.string.profile_name_dialog),
                resources.getString(R.string.cancel),
                resources.getString(R.string.change),
                onPositiveClick = {
                    nameChangeDialog.show(
                        requireActivity().supportFragmentManager,
                        "CustomEditDialog"
                    )
                }).apply {
                isCancelable = false
            }

        nameChangeDialog =
            CustomEditDialog(
                resources.getString(R.string.change_name_dialog),
                binding.accountName.text.toString(),
                resources.getString(R.string.example_name),
                resources.getString(R.string.change_name_dialog_instruction),
                resources.getString(R.string.cancel),
                resources.getString(R.string.save),
                onPositiveClick = {
                    if (checkNickname(it) == -1) {
                        nameChangeDialog.setWrongInstruction(resources.getString(R.string.nickname_less_one))
                    } else if (checkNickname(it) == 1) {
                        nameChangeDialog.setWrongInstruction(resources.getString(R.string.nickname_more_six))
                    } else {
                        binding.accountName.text = it.trim()
                        nameChangeDialog.dismiss()
                        viewModel.updateProfile(it, null)
                    }
                }).apply {
                isCancelable = false
            }
    }

    private fun checkNickname(userInput: String): Int {
        val nickName = userInput.trim()
        if (nickName.isEmpty()) return -1
        else if (nickName.length > 6) return 1
        else return 0
    }

    private fun addLocalImage(imageUri: Uri?) {
        Glide.with(this)
            .load(imageUri)
            .circleCrop()
            .into(binding.accountImage)
    }

    private fun updateProfileImg(uri: Uri?) {
        SendFilesUtil.uriToFile(requireContext(), listOf(uri.toString())) { fileParts ->
            Log.d("SettingFragment", "fileParts size: ${fileParts.size}")
            if (fileParts.isNotEmpty()) {
                viewModel.updateProfile(binding.accountName.text.toString(), fileParts.first())
                Log.d("SettingFragment", "First filePart: ${fileParts.first()}")
            } else {
                Log.e("SettingFragment", "fileParts is empty")
            }
        }
    }

}
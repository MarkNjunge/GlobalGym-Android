package com.marknkamau.globalgym.ui.userDetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup

import com.marknkamau.globalgym.R
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.ui.editProfile.EditProfileActivity
import com.marknkamau.globalgym.utils.trimmedText
import kotlinx.android.synthetic.main.fragment_user_details.*

class UserDetailsFragment : Fragment() {
    private var country = ""
    private var gender = "Female"
    private lateinit var rgGender: RadioGroup

    var onComplete: ((UserDetailsResponse) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_details, container, false)

        rgGender = view.findViewById(R.id.rgGender)
        rgGender.setOnCheckedChangeListener { radioGroup, i ->
            gender = view.findViewById<RadioButton>(rgGender.checkedRadioButtonId).text.toString()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.get(EditProfileActivity.USER_KEY)?.let {
            val user: User = it as User
            etFirstName.setText(user.firstName)
            etLastName.setText(user.lastName)
            etPhone.setText(user.phone)
            etYearBirth.setText(user.yearOfBirth.toString())
            etWeight.setText(user.weight.toString())
            etTargetWeight.setText(user.targetWeight.toString())
            tvCountry.text = user.country
            country = user.country
            if (user.genderFull == "Male") {
                rgGender.check(R.id.rbMale)
            }
            btnContinue.text = getString(R.string.save)
        }

        btnContinue.setOnClickListener {
            registerUser()
        }

        val countryDialog = CountryDialog()
        countryDialog.onSelected {
            country = it
            tvCountry.text = it
        }

        tvCountry.setOnClickListener {
            countryDialog.show(childFragmentManager, "Select country")
        }
    }

    private fun registerUser() {
        val firstName = etFirstName.trimmedText
        val lastName = etLastName.trimmedText
        val phone = etPhone.trimmedText
        val year = etYearBirth.trimmedText
        val weight = etWeight.trimmedText
        val targetWeight = etTargetWeight.trimmedText

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || year.isEmpty() || weight.isEmpty() || targetWeight.isEmpty() || country.isEmpty()) {
            return
        }

        val apiGender = if (gender == "Female" || gender == "Mwanamke") "F" else "M"
        onComplete?.invoke(UserDetailsResponse(firstName, lastName, phone, year.toInt(), country, apiGender, weight.toInt(), targetWeight.toInt()))
    }

    data class UserDetailsResponse(val firstName: String,
                                   val lastName: String,
                                   val phone: String,
                                   val year: Int,
                                   val country: String,
                                   val gender: String,
                                   val weight: Int,
                                   val targetWeight: Int)
}

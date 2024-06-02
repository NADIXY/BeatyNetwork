package com.example.beautynetwork.ui

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.beautynetwork.MainViewModel
import com.example.beautynetwork.R
import com.example.beautynetwork.data.model.user.Profile
import com.example.beautynetwork.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                //Uri bezieht sich auf das Bild
                viewModel.uploadProfilePicture(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Beim Klick auf Logout, wird die Logout Funktion im ViewModel aufgerufen
        binding.logout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Log out")
            builder.setMessage("Are you sure you want to log out?")
            builder.setPositiveButton("Yes") { dialog, which ->
                viewModel.logout()
                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel") { dialog, which -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        // Funktion um Bild vom Gerät auszuwählen
        // Startet den Ressource-Picker und zeigt uns alle Bilder auf dem Gerät an
        binding.ivProfilePic.setOnClickListener {
            getContent.launch("image/*")
        }

        // Snapshot Listener: Hört auf Änderungen in dem Firestore Document, das beobachtet wird
        // Hier: Referenz auf Profil wird beobachtet
        viewModel.profileRef?.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                // Umwandeln des Snapshots in eine Klassen-Instanz von der Klasse Profil und setzen der Felder
                val myProfile = value.toObject(Profile::class.java)
                binding.tietFirstName.setText(myProfile?.firstName)
                binding.tietLastName.setText(myProfile?.lastName)
                binding.tietNumber.setText(myProfile?.number)
                binding.tietEmail.setText(myProfile?.email)
                binding.adressTietAdresse.setText(myProfile?.adress)
                binding.tietDateOfBirth.setText(myProfile?.dateOfBirth)
                binding.tietProfession.setText(myProfile?.profession)
                binding.ivProfilePic.load(myProfile?.profilePicture) {
                    error(R.drawable.baseline_account_box_24)
                }

            }
        }

        // User LiveData aus dem ViewModel wird beobachtet
        // Wenn User gleich null (also der User nicht mehr eingeloggt ist) wird zum SignInFragment navigiert
        viewModel.user.observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().navigate(R.id.signInFragment)
            }
        }

        // Neue Profil-Daten in Firestore speichern
        binding.btSave1.setOnClickListener {

            val firstName = binding.tietFirstName.text.toString()
            val lastName = binding.tietLastName.text.toString()
            val number = binding.tietNumber.text.toString()
            val email = binding.tietEmail.text.toString()
            val adress = binding.adressTietAdresse.text.toString()
            val dateOfBirth = binding.tietDateOfBirth.text.toString()
            val profession = binding.tietProfession.text.toString()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Save")
            builder.setMessage("Are you sure you want to save?")
            builder.setPositiveButton("Yes") { dialog, which ->

                if (firstName != "" && lastName != "" && number != "" && email != "" && adress != ""
                    && dateOfBirth != "" && profession != ""
                ) {
                    val newProfile = Profile(
                        firstName,
                        lastName,
                        number,
                        email,
                        adress,
                        dateOfBirth,
                        profession
                    )

                    viewModel.updateProfile(newProfile)

                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.generalQuestionnaireFragment)

                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()

                }

            }

            builder.setNegativeButton("Cancel") { dialog, which -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()

            findNavController().navigate(R.id.profileFragment)
        }

    }
}
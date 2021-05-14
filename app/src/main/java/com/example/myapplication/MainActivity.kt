package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val users = mutableListOf<User>()


    companion object {
        private val NAME_PATTERN: Pattern = Pattern.compile("^[a-zA-Z\\s]*$")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener {
            add()
        }
        binding.buttonUpdate.setOnClickListener {
            update()
        }
        binding.buttonRemove.setOnClickListener {
            remove()
        }
    }
    /**
     * checks if inputs are valid and saves
     */
    private fun add() {
        validateInput()
        val email = binding.editTextEmail.text.trim().toString()
        val firstName = binding.editTextFirstName.text.trim().toString()
        val lastName = binding.editTextLastName.text.trim().toString()
        val age = binding.editTextAge.text.trim().toString()

        val newUser = User(firstName, lastName, age, email)
        var res = false
        for (user in users) {
            if (user == newUser) {
                binding.successError.text = "Error"
                binding.successError.setTextColor(Color.RED)
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
            } else {
                users.add(newUser)
                binding.successError.text = "Success"
                binding.successError.setTextColor(Color.GREEN)
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * checks if inputs are valid and updates
     */

    private fun update() {
        validateInput()
        val email = binding.editTextEmail.text.trim().toString()
        val firstName = binding.editTextFirstName.text.trim().toString()
        val lastName = binding.editTextLastName.text.trim().toString()
        val age = binding.editTextAge.text.trim().toString()
        for (user in users) {
            if (email == user.email) {
                val editedUser = user.copy(firstName = firstName, lastName = lastName, age = age)
                users.remove(user)
                users.add(editedUser)
                binding.successError.text = "Success"
                binding.successError.setTextColor(Color.GREEN)
                Toast.makeText(this, "User updates successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.successError.text = "Error"
                binding.successError.setTextColor(Color.RED)
                Toast.makeText(this, "You cannot change email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * checks if inputs are valid and removes
     */

    private fun remove() {
        validateInput()
        val email = binding.editTextEmail.text.trim().toString()
        val firstName = binding.editTextFirstName.text.trim().toString()
        val lastName = binding.editTextLastName.text.trim().toString()
        val age = binding.editTextAge.text.trim().toString()
        val newUser = User(firstName, lastName, age, email)
        for (user in users) {
            if (user == newUser) {
                users.remove(newUser)
                binding.successError.text = "Error"
                binding.successError.setTextColor(Color.RED)
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT)
            } else {
                binding.successError.text = "Success"
                binding.successError.setTextColor(Color.GREEN)
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT)
            }
        }
    }

    /**
     * @return true all fields are correct
     */
    private fun validateInput(): Boolean {
        var result = true
        val email = binding.editTextEmail.text.trim()
        val firstName = binding.editTextFirstName.text.trim()
        val lastName = binding.editTextLastName.text.trim()
        val age = binding.editTextAge.text.trim()

        //EMAIL CHECK
        if (email.isEmpty()) {
            binding.editTextEmail.error = getString(R.string.empty_field_error_msg)
            result = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmail.error = getString(R.string.email_error_msg)
            result = false
        }

        //FIRST NAME CHECK
        if (firstName.isEmpty()) {
            binding.editTextFirstName.error = getString(R.string.empty_field_error_msg)
            result = false
        }
        if (!NAME_PATTERN.matcher(firstName).matches()) {
            binding.editTextFirstName.error = getString(R.string.name_error_msg)
            result = false
        }

        //LAST NAME CHECK
        if (lastName.isEmpty()) {
            binding.editTextLastName.error = getString(R.string.empty_field_error_msg)
            result = false
        }
        if (!NAME_PATTERN.matcher(lastName).matches()) {
            binding.editTextLastName.error = getString(R.string.name_error_msg)
            result = false
        }

        //AGE CHECK
        if (age.isEmpty()) {
            binding.editTextAge.error = getString(R.string.empty_field_error_msg)
            result = false
        }

        /*REDUNDANT CODE
         *EditTextAge input type is "Number" that already accepts only Ints
         */
        if (age.toString().toIntOrNull() == null) {
            binding.editTextAge.error = getString(R.string.age_error_msg)
            result = false
        }
        return result
    }
}
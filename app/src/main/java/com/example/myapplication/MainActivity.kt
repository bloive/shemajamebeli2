package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.backup.BackupAgent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log.d
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val users = mutableMapOf<String, User>()
    private val deletedUsers = mutableMapOf<String, User>()
    private var removedUsers = 0

    companion object {
        private val NAME_PATTERN: Pattern by lazy { Pattern.compile("^[a-zA-Z\\s]*$") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.buttonAdd.setOnClickListener {
            add()
        }
        binding.buttonUpdate.setOnClickListener {
            update()
        }
        binding.buttonRemove.setOnClickListener {
            remove()
        }
        updateUsersCount()
    }

    /**
     * checks if inputs are valid and adds new user
     */
    private fun add() {
        hideKeyboard()
        val email = binding.editTextEmail.text.trim().toString()
        val firstName = binding.editTextFirstName.text.trim().toString()
        val lastName = binding.editTextLastName.text.trim().toString()
        val age = binding.editTextAge.text.trim().toString()
        if (validateInput(email, firstName, lastName, age)) {
            val newUser = User(email, firstName, lastName, age)
            if (!users.containsKey(email)) {
                users[email] = newUser
                setTexDesign(true)
                Toast.makeText(this, getString(R.string.user_added), Toast.LENGTH_SHORT).show()
                showProfile(newUser)
            } else {
                setTexDesign(false)
                Toast.makeText(this, getString(R.string.user_not_added), Toast.LENGTH_SHORT).show()
            }
        }
        updateUsersCount()
    }

    /**
     * checks if inputs are valid and updates user data
     */
    private fun update() {
        hideKeyboard()
        val email = binding.editTextEmail.text.trim().toString()
        val firstName = binding.editTextFirstName.text.trim().toString()
        val lastName = binding.editTextLastName.text.trim().toString()
        val age = binding.editTextAge.text.trim().toString()
        if (validateInput(email, firstName, lastName, age)) {
            val newUser = User(email, firstName, lastName, age)
            if (users.containsKey(email)) {
                users[email] = newUser
                setTexDesign(true)
                Toast.makeText(this, getString(R.string.user_updated), Toast.LENGTH_SHORT).show()
                users[email]?.let { showProfile(it) }
            } else {
                setTexDesign(false)
                Toast.makeText(this, getString(R.string.user_not_updated), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * checks if inputs are valid and removes existing user
     */
    private fun remove() {
        hideKeyboard()
        val email = binding.editTextEmail.text.trim().toString()
        if (validateEmail(email)) {
            d("info", "email $email")
            if (users.containsKey(email)) {
                deletedUsers[email] = users[email]!!
                users.remove(email)
                removedUsers++
                setTexDesign(true)
                Toast.makeText(this, getString(R.string.user_deleted), Toast.LENGTH_SHORT).show()
            } else {
                setTexDesign(false)
                Toast.makeText(this, getString(R.string.user_not_deleted), Toast.LENGTH_SHORT).show()
            }
        }
        updateUsersCount()
    }

    /**
    * opens the new user's profile
    */
    private fun showProfile(newUser : User) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("user", newUser)
        startActivity(intent)
    }

    /**
     * opens the new user's profile
     */
    private fun updateUsersCount() {
        binding.activeCount.text = "${getString(R.string.active_users)} ${users.size}"
        binding.removedCount.text = "${getString(R.string.removed_users)} $removedUsers"
    }

    /**
     * sets process info text and color
     */
    private fun setTexDesign(boolean: Boolean) {
        if (boolean) {
            binding.successError.text = getString(R.string.success)
            binding.successError.setTextColor(Color.GREEN)
        } else {
            binding.successError.text = getString(R.string.error)
            binding.successError.setTextColor(Color.RED)
        }

    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    /**
     * @return true if all fields are correct, else false
     */
    private fun validateInput(email:String, firstName: String, lastName :String, age : String): Boolean {
        var result = true

        //EMAIL CHECK
        result = validateEmail(email)

        //FIRST NAME CHECK
        if (firstName.isEmpty()) {
            binding.editTextFirstName.error = getString(R.string.empty_field_error_msg)
            result = false
        } else if (!NAME_PATTERN.matcher(firstName).matches()) {
            binding.editTextFirstName.error = getString(R.string.name_error_msg)
            result = false
        }

        //LAST NAME CHECK
        if (lastName.isEmpty()) {
            binding.editTextLastName.error = getString(R.string.empty_field_error_msg)
            result = false
            d("result", "LN empty $result")
        } else if (!NAME_PATTERN.matcher(lastName).matches()) {
            binding.editTextLastName.error = getString(R.string.name_error_msg)
            result = false
            d("result", "LN not valid $result")
        }

        //AGE CHECK
        if (age.isEmpty()) {
            binding.editTextAge.error = getString(R.string.empty_field_error_msg)
            result = false
            d("result", "age empty $result")
        } else if (age.toIntOrNull() == null) {
            binding.editTextAge.error = getString(R.string.age_error_msg)
            result = false
        }
        return result
    }

    //EMAIL CHECK
    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.editTextEmail.error = getString(R.string.empty_field_error_msg)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmail.error = getString(R.string.email_error_msg)
            false
        } else {
            true
        }
    }
}
package com.example.admint2t

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminDashboardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val inboxButton = view.findViewById<Button>(R.id.btnInbox)
        inboxButton.setOnClickListener {
            val intent = Intent(activity, Inbox::class.java)
            startActivity(intent)
        }

        val newMessageButton = view.findViewById<FloatingActionButton>(R.id.newMessageButton)
        newMessageButton.setOnClickListener {
            val intent = Intent(activity, SendMessage::class.java)
            startActivity(intent)
        }

        return view
    }
}

package com.humber.its2020.ibourit.ui.justgotit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.humber.its2020.ibourit.R

class JustGotItFragment: Fragment() {

    private lateinit var justGotItViewModel: JustGotItViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        justGotItViewModel =
                ViewModelProvider(this).get(JustGotItViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_justgotit, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        justGotItViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title =
            " " + resources.getString(R.string.justgotit)
    }
}
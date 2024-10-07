package com.example.filetest01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filetest01.databinding.Layout1Binding
import com.example.filetest01.ui.theme.Filetest01Theme

class MainActivity : ComponentActivity() {

    private lateinit var binding: Layout1Binding

    private val history = Train(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Layout1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化
        init(history, this)
        val historyRecyclerView = binding.historyRecyclerView
        // 布局管理器
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyRecyclerView.layoutManager = layoutManager
        // 设置适配器
        val adapter = TrainAdapter(history.getTrainList())
        historyRecyclerView.adapter = adapter

        binding.search.setOnClickListener {
            val start = binding.start.text.toString()
            val end = binding.end.text.toString()
            if (history.check(start, end)) {
                history.addTrain(start, end)
                binding.historyRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        binding.clear.setOnClickListener {
            history.clear()
            binding.historyRecyclerView.adapter?.notifyDataSetChanged()
        }

        binding.change.setOnClickListener {
            val temp = binding.start.text
            binding.start.text = binding.end.text
            binding.end.text = temp
        }


    }
}

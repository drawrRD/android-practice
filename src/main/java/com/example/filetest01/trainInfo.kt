package com.example.filetest01

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Serializable

data class TrainInfo(val start: String, val end: String):Serializable


class Train(private val context: Context){
    private val trainList = ArrayList<TrainInfo>()


    fun init_add(start: String, end: String) {
        trainList.add(TrainInfo(start, end))
    }

    fun clear() {
        trainList.clear()
        try {
            context.deleteFile("myData")
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun check(start: String, end: String): Boolean {
        //读取文件，检查是否有重复
        var flag = 0
        try {
            val input = context.openFileInput("myData")
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    val parts = it.split(" ")
                    if(parts[0] == start && parts[1] == end){
                        flag = 1
                        return@forEachLine
                    }
                }
            }
            if (flag == 1)
                return false
            return true
        }
        catch (e: FileNotFoundException) {
            // 文件不存在，直接返回 true
            return true
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return false
    }

    fun addTrain(start: String, end: String) {
        try {
            trainList.add(TrainInfo(start, end))

            val output = context.openFileOutput("myData", Context.MODE_PRIVATE or Context.MODE_APPEND)
            val bufferWriter =  BufferedWriter(OutputStreamWriter(output))
            bufferWriter.use {
                it.write(start)
                it.write(" ")
                it.write(end)
                it.newLine()
            }
            bufferWriter.flush()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getTrainList(): ArrayList<TrainInfo> {
        return trainList
    }

}

fun init(train: Train, context: Context)
{
    try{
        //读取文件，存入trainList
        val input = context.openFileInput("myData")
        val reader = BufferedReader(InputStreamReader(input))
        reader.use {
            reader.forEachLine {
                val parts = it.split(" ")
                train.init_add(parts[0], parts[1])
            }
        }
    }
    catch (e: Exception){
        e.printStackTrace()
    }
}


// view holder
class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val start: TextView = view.findViewById(R.id.hstart)
    val end: TextView = view.findViewById(R.id.hend)
}

class TrainAdapter(private val trainList: ArrayList<TrainInfo>) : RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount(): Int {
        return trainList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history, parent, false)
        val holder = ViewHolder(view)
        return holder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainInfo = trainList[position]
        holder.start.text = trainInfo.start
        holder.end.text = trainInfo.end

    }
}




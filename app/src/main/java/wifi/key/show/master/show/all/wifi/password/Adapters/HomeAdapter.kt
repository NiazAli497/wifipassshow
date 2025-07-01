package wifi.key.show.master.show.all.wifi.password.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wifi.key.show.master.show.all.wifi.password.databinding.ItemMainRecyclerBinding
import wifi.key.show.master.show.all.wifi.password.utils.MainRecyclerData

class HomeAdapter(val contex:Context, val dataList: ArrayList<MainRecyclerData>) : RecyclerView.Adapter<HomeAdapter.MainViewHolder>() {

    var pos= 0
    var onClick: ((pos:Int) -> Unit?)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
       return MainViewHolder(ItemMainRecyclerBinding.inflate(LayoutInflater.from(contex),parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.iconMain.setImageResource(dataList.get(position).iconID)
        holder.binding.textInfo.setText(dataList.get(position).textName)
        holder.itemView.setOnClickListener {
            onClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
       return dataList.size
    }

    inner class MainViewHolder(val binding: ItemMainRecyclerBinding) : RecyclerView.ViewHolder(binding.root)
}
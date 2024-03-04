package uz.akra.internet.adapters

import android.icu.text.Transliterator.Position
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.akra.internet.databinding.ItemRevViewBinding
import uz.akra.internet.models.MyCurrency


class MyRvAdapter(var list: List<MyCurrency>, val rvAction: RvAction):RecyclerView.Adapter<MyRvAdapter.Vh>() {

    inner class Vh(val itemRvBinding: ItemRevViewBinding):
            RecyclerView.ViewHolder(itemRvBinding.root){

                fun onBind(myCurrency: MyCurrency, position: Int){
                    itemRvBinding.tvCurrName.text = myCurrency.CcyNm_EN
                    itemRvBinding.tvUzbekcha.text = myCurrency.CcyNm_UZ
                    itemRvBinding.tvUzbsom.text = myCurrency.Rate.toString()
                    itemRvBinding.tvDate.text = myCurrency.Date


                    itemRvBinding.root.setOnClickListener {
                        rvAction.onClick(myCurrency, position)
                    }

//                    itemRvBinding.btnLike.setOnClickListener {
//                        rvAction.onClickLiked(myCurrency, position)
//                    }
//
//                    if (myCurrency.liked == 1){
//                        itemRvBinding.btnLike.setImageResource(R.drawable.ic_heart)
//                    }else{
//                        itemRvBinding.btnLike.setImageResource(R.drawable.ic_heartt)
//                    }

                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRevViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)

        val currentCurrency = list[position]
        // Ma'lumotlarni ViewHolder ga joylash
        holder.itemRvBinding.tvUzbsom.text = currentCurrency.Rate.toString()
    }

    interface RvAction{
        fun onClick(myCurrency: MyCurrency, position: Int)
//        fun onClickLiked(myCurrency: MyCurrency, position: Int)
    }

}
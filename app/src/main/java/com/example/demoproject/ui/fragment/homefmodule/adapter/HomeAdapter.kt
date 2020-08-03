package com.example.demoproject.ui.fragment.homefmodule.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demoproject.R
import com.example.demoproject.data.db.UserListEntity
import com.example.demoproject.ui.fragment.homefmodule.HomeNavigator
import kotlinx.android.synthetic.main.customer_item_view.view.*
import java.util.*


class HomeAdapter(
    val context: Context,
    var userList: ArrayList<UserListEntity>,
    var homeNavigator: HomeNavigator
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.customer_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtContactName?.text =
            userList.get(position).first_name + "  " + userList.get(position).last_name
        holder.txtContactNo.text = userList.get(position).email

        if (userList.get(position).avatar != "") {
            holder.imgProfilePhoto.visibility = View.VISIBLE
            holder.txtCustomerInital.visibility = View.INVISIBLE

            Glide.with(context)
                .load(userList.get(position).avatar)
                .placeholder(R.drawable.ic_dummy_user)
                .into(holder.imgProfilePhoto)

        } else {
            holder.imgProfilePhoto.visibility = View.GONE
            holder.txtCustomerInital.visibility = View.VISIBLE
            holder.txtCustomerInital.text = userList.get(position).first_name

        }
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))


        val unwrappedDrawable =
            AppCompatResources.getDrawable(context, R.drawable.blue_customer_icon_background)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, color)

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                homeNavigator.clickOnCustomerItemView(userList.get(position)!!)
            }
        })
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtContactName = view.txtContactName
        val txtContactNo = view.txtContactNo
        val txtCustomerInital = view.txtCustomerInital
        val imgProfilePhoto = view.imgCustomerImage

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
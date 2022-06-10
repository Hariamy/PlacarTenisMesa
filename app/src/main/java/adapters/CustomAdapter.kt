package adapters

import android.content.ClipData
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.placartenismesa.DetalhesPartidaActivity
import com.example.placartenismesa.FimPartidaActivity
import com.example.placartenismesa.HistoricoActivity
import data.Placar
import com.example.placartenismesa.R
import data.Constantes



class CustomAdapter(private val mList: List<Placar>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_historico, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val placar = mList[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.partida.text = placar.nome_partida
        holder.ganhador.text = placar.jogador_1.nome
        holder.placar.text = placar.jogador_1.cont_set_ganho.toString() + " X " + placar.jogador_2.cont_set_ganho.toString()

        if (placar.ganhador == Constantes.JOGADOR_2) {
            holder.ganhador.text = placar.jogador_2.nome
            holder.placar.text = placar.jogador_2.cont_set_ganho.toString() + " X " + placar.jogador_1.cont_set_ganho.toString()
        }


        holder.lnCell.setOnClickListener {
            val intent: Intent = Intent(holder.context, DetalhesPartidaActivity::class.java).apply {
                putExtra(Constantes.PLACAR, placar)
            }
            holder.context.startActivity(intent)

        }

    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val partida: TextView = ItemView.findViewById(R.id.tvPartidaCard)
        val ganhador: TextView = ItemView.findViewById(R.id.tvGanhadorCard)
        val placar: TextView = ItemView.findViewById(R.id.tvPlacarCard)

        val lnCell: ConstraintLayout = ItemView.findViewById(R.id.inCell)
        val context = ItemView.context
    }
}

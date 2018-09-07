package br.com.livroandroid.carros.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import com.squareup.picasso.Picasso

// Define o construtor que recebe (carros,onClick)
class CarroAdapter (
        val carros: List<Carro>,
        val onClick: (Carro) -> Unit) :
        RecyclerView.Adapter<CarroAdapter.CarrosViewHolder>() {

    // Retorna a quantidade de carros na lista
    override fun getItemCount() = this.carros.size

    class CarrosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tNome: TextView = view.findViewById(R.id.text)
        var img: ImageView = view.findViewById(R.id.img)
    }

    // Infla o layout do adapter e retorna o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_carro,
                parent, false)
        return CarrosViewHolder(view)
    }

    // Faz o bind para atualizar o valor das views com os dados do Carro
    override fun onBindViewHolder(holder: CarrosViewHolder, position: Int) {
        val context = holder.itemView.context

        // Atualiza os dados do carro

        val carro = carros[position]
        holder.tNome.text = carro.nome

        Picasso.get().load(carro.urlFoto).into(holder.img)

        // Adiciona o evento de clique na linha
        holder.itemView.setOnClickListener { onClick(carro) }
    }
}
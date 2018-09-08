package br.com.livroandroid.carros.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.extensions.loadUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_carro.view.*
import kotlinx.android.synthetic.main.include_progress.view.*

// Define o construtor que recebe (carros,onClick)
class CarroAdapter (
        val carros: List<Carro>,
        val onClick: (Carro) -> Unit) :
        RecyclerView.Adapter<CarroAdapter.CarrosViewHolder>() {

    // Retorna a quantidade de carros na lista
    override fun getItemCount() = this.carros.size

    class CarrosViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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

        val view = holder.itemView

        // Atualiza os dados do carro
        val carro = carros[position]

        with(carro) {
            view.textView.text = nome

            //Picasso.get().load(urlFoto).into(view.img)
            view.img.loadUrl(urlFoto, view.progress)

            // Adiciona o evento de clique na linha
            view.setOnClickListener { onClick(this) }
        }
    }
}
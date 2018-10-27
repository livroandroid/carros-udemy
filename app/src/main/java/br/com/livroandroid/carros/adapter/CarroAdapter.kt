package br.com.livroandroid.carros.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.extensions.loadUrl
import kotlinx.android.synthetic.main.adapter_carro.view.*
import kotlinx.android.synthetic.main.include_progress.view.*

// Define o construtor que recebe (carros,onClick)
class CarroAdapter (
        val context: Context,
        val carros: MutableList<Carro>,
        val onClick: (Carro, longClick: Boolean) -> Unit
        /*val onLongClick: (Carro) -> Boolean*/):
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

            view.img.loadUrl(urlFoto, view.progress)

            // Adiciona o evento de clique na linha
            view.setOnClickListener { onClick(this, false) }

            view.setOnLongClickListener { onClick(this, true)
                true
            }

            // Pinta o fundo de azul se a linha estiver selecionada
            val corFundo = ContextCompat.getColor(context,if (carro.selected) R.color.colorPrimary else R.color.white)
            view.card_view.setCardBackgroundColor(corFundo)
            // A cor do texto é branca ou azul, depende da cor do fundo.
            val corFonte = ContextCompat.getColor(context, if (carro.selected) R.color.white else R.color.colorPrimary)
            view.textView.setTextColor(corFonte)
        }


    }
}